package se.dosf.communitybase.modules.pictureGallery.cruds;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.cruds.IntegerBasedCommunityBaseCRUD;
import se.dosf.communitybase.modules.pictureGallery.PictureGalleryModule;
import se.dosf.communitybase.modules.pictureGallery.beans.Gallery;
import se.dosf.communitybase.modules.pictureGallery.beans.Picture;
import se.dosf.communitybase.utils.AccessibleFactory;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class GalleryCRUD extends IntegerBasedCommunityBaseCRUD<Gallery, PictureGalleryModule> {

	protected final QueryParameterFactory<Gallery, Integer> galleryIDParamFactory;

	protected final AnnotatedDAOWrapper<Gallery, Integer> galleryCRUDDAO;
	
	protected final StringPopulator stringPopulator = new StringPopulator();

	private AccessibleFactory accessibleFactory;

	public GalleryCRUD(AnnotatedDAOWrapper<Gallery, Integer> galleryCRUDDAO, BeanRequestPopulator<Gallery> populator, String typeElementName, String typeLogName, String listMethodAlias, PictureGalleryModule pictureGalleryModule) {

		super(galleryCRUDDAO, populator, typeElementName, typeLogName, listMethodAlias, pictureGalleryModule);

		this.galleryCRUDDAO = galleryCRUDDAO;

		this.accessibleFactory = new AccessibleFactory(pictureGalleryModule);
		this.galleryIDParamFactory = this.galleryCRUDDAO.getAnnotatedDAO().getParamFactory("galleryID", Integer.class);
	}

	@Override
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, List<ValidationError> validationErrors) throws Exception {

		log.info("User " + user + " listing " + this.typeLogPluralName);

		Document doc = this.callback.createDocument(req, uriParser, user);
		Element listTypeElement = doc.createElement("List" + this.typeElementPluralName);
		doc.getFirstChild().appendChild(listTypeElement);

		Collection<Gallery> galleries = this.getAllBeans(user, req, uriParser); // Get accessible galleries
		List<Picture> pictures;
		Random rand;

		Element categoriesElement = doc.createElement("Categories");

		// Append random picture for each gallery
		if(galleries != null){
			
			boolean usesCategories = false;
			
			for(Gallery gallery : galleries){
				if(gallery.getCategory() != null){
					usesCategories = true;
					break;
				}
			}
		
			List<Element> categories = new ArrayList<Element>();
			
			galleryLoop: for(Gallery gallery : galleries){
				Element galleryElement = gallery.toXML(doc);
				pictures = gallery.getPictures();
				
				if(!CollectionUtils.isEmpty(pictures)){
					rand = new Random();
					galleryElement.appendChild(XMLUtils.createElement("randomPic", pictures.get(rand.nextInt(pictures.size())).getPictureID().toString(), doc));
					galleryElement.appendChild(XMLUtils.createElement("numPics", String.valueOf(pictures.size()), doc));
				}else{
					galleryElement.appendChild(XMLUtils.createElement("numPics", 0, doc));
				}

				if(callback.hasUpdateAccess(gallery, user)) {

					XMLUtils.appendNewElement(doc, galleryElement, "hasUpdateAccess", "true");
				}
				
				String category = gallery.getCategory();
				
				if(category == null){
					if(usesCategories){
						category = callback.getOthersCategoryName();
					}else{
						category = "";
					}
				}
				
				for(Element elem : categories){
					if(StringUtils.compare(elem.getChildNodes().item(0).getChildNodes().item(0).getNodeValue(), category)){
						elem.getChildNodes().item(1).appendChild(galleryElement);
						continue galleryLoop;
					}
				}
				
				Element categoryElement = doc.createElement("Category");
				XMLUtils.appendNewElement(doc, categoryElement, "name", category);
				
				Element galleriesElement = doc.createElement(this.typeElementPluralName);
				galleriesElement.appendChild(galleryElement);
				
				categoryElement.appendChild(galleriesElement);
				categories.add(categoryElement);
			}
			
			Collections.sort(categories, new Comparator<Element>() {
				@Override
				public int compare(Element o1, Element o2) {
					String s1 = o1.getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
					String s2 = o2.getChildNodes().item(0).getChildNodes().item(0).getNodeValue();

					if(s1 == null && s2 == null){
						return 0;
					}else if(s1 == null){
						return -1;
					}else if(s2 == null){
						return 1;
					}else{
						return s1.compareToIgnoreCase(s2);
					}
				}}
			);
			
			for(Element elem : categories){
				categoriesElement.appendChild(elem);
			}
		}

		listTypeElement.appendChild(categoriesElement);

		if(validationErrors != null){
			XMLUtils.append(doc, listTypeElement, validationErrors);
			listTypeElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		this.appendListFormData(doc, listTypeElement, user, req, uriParser, validationErrors);

		SimpleForegroundModuleResponse moduleResponse = new SimpleForegroundModuleResponse(doc, getListTitle(req, user, uriParser));
		moduleResponse.addBreadcrumbsLast(getListBreadcrumbs(req, user, uriParser, validationErrors));

		return moduleResponse;
	}

	@Override
	protected void appendListFormData(Document doc, Element listTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser, List<ValidationError> validationErrors) throws SQLException {

		CommunityGroup group = (CommunityGroup)req.getAttribute("group");

		listTypeElement.appendChild(XMLUtils.createElement("userLastLoginInMillis", String.valueOf(user.getLastLogin().getTime()), doc));
		XMLUtils.append(doc, listTypeElement, "SortingCriterias", "Criteria", PictureGalleryModule.SORT_COLUMNS);

		// Append sorting preferences as request parameters
		Element requestParametersElement = RequestUtils.getRequestParameters(req, doc);
		Element param1 = doc.createElement("parameter");
		XMLUtils.appendNewElement(doc, param1, "name", "orderby");
		XMLUtils.appendNewElement(doc, param1, "value", req.getAttribute("pictureGallery.sortingPreferences.criteria"));
		Element param2 = doc.createElement("parameter");
		XMLUtils.appendNewElement(doc, param2, "name", "reverse");
		XMLUtils.appendNewElement(doc, param2, "value", req.getAttribute("pictureGallery.sortingPreferences.reverse"));
		requestParametersElement.appendChild(param1);
		requestParametersElement.appendChild(param2);
		listTypeElement.appendChild(requestParametersElement);

		callback.appendGroupAndAccess(doc, group, user); // Append user access

		XMLUtils.appendNewElement(doc, listTypeElement, "allowsGalleryDownload", callback.getAllowsGalleryDownload());

		super.appendListFormData(doc, listTypeElement, user, req, uriParser, validationErrors);
	}

	@Override
	public ForegroundModuleResponse show(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		Gallery bean;

		if(uriParser.size() > 3 && NumberUtils.isInt(uriParser.get(3))){

			HighLevelQuery<Gallery> query = new HighLevelQuery<Gallery>(Gallery.PICTURE_RELATION, Gallery.GROUP_RELATION, Gallery.SCHOOL_RELATION);

			query.disableAutoRelations(true);
			query.addParameter(this.galleryIDParamFactory.getParameter(NumberUtils.toInt(uriParser.get(3))));

			if((bean = this.galleryCRUDDAO.getAnnotatedDAO().get(query)) != null){
				return this.showBean(bean, req, res, user, uriParser, (List<ValidationError>)null);
			}

			return list(req, res, user, uriParser, Collections.singletonList(new ValidationError("ShowFailed" + typeElementName + "NotFound")));
		}

		throw new URINotFoundException(uriParser);
	}

	@Override
	protected void appendShowFormData(Gallery bean, Document doc, Element showTypeElement, CommunityUser user, HttpServletRequest req, HttpServletResponse res, URIParser uriParser) throws SQLException, IOException, Exception {

		//TODO add support for sorting the files inside the album here...

		callback.appendGroupAndAccess(doc, (CommunityGroup)req.getAttribute("group"), user); // Append user access

		if(callback.getAllowsGalleryDownload()){
			XMLUtils.appendNewElement(doc, showTypeElement, "allowsGalleryDownload", callback.getAllowsGalleryDownload());
		}

		if(callback.hasUpdateAccess(bean, user)) {
			XMLUtils.appendNewElement(doc, showTypeElement, "hasUpdateAccess", "true");
		}

		List<Picture> pictures = bean.getPictures();

		int num = pictures != null ? pictures.size() : 0;

		showTypeElement.appendChild(XMLUtils.createElement("numPics", String.valueOf(num), doc));

		int nrOfThumbsPerPage = callback.getNumOfThumbsPerAlbumPage();

		int pagesInGallery = num % nrOfThumbsPerPage == 0 ? num / nrOfThumbsPerPage : (num / nrOfThumbsPerPage) + 1;

		Integer page = 1;

		if(uriParser.size() == 5){
			page = NumberUtils.toInt(uriParser.get(4));

			if(page == null || page <= 0){
				throw new URINotFoundException(uriParser);
			}
		}

		if(num == 0){

			showTypeElement.appendChild(XMLUtils.createElement("pages", "1", doc));
			showTypeElement.appendChild(XMLUtils.createElement("currentPage", "1", doc));
			showTypeElement.appendChild(XMLUtils.createElement("numPics", "0", doc));

		}else if(page <= pagesInGallery){

			// find next and previous page
			Integer nextPage = page == pagesInGallery ? null : page + 1;
			Integer prevPage = page == 1 ? null : page - 1;

			// calculate start- and endindex
			int startIndex = (nrOfThumbsPerPage * page) - nrOfThumbsPerPage;
			int endIndex = startIndex + nrOfThumbsPerPage;
			if(page == pagesInGallery){
				endIndex = pictures.size();
			}

			showTypeElement.appendChild(XMLUtils.createElement("pages", String.valueOf(pagesInGallery), doc));
			showTypeElement.appendChild(XMLUtils.createElement("currentPage", String.valueOf(page), doc));
			showTypeElement.appendChild(XMLUtils.createElement("numPics", String.valueOf(num), doc));

			if(nextPage != null){
				showTypeElement.appendChild(XMLUtils.createElement("nextPage", nextPage.toString(), doc));
			}
			if(prevPage != null){
				showTypeElement.appendChild(XMLUtils.createElement("prevPage", prevPage.toString(), doc));
			}

			Element displayPicturesElement = doc.createElement("DisplayPictures");

			// Set pictures to show on this page
			for(int i = startIndex; i < endIndex; i++){
				Picture picture = pictures.get(i);
				displayPicturesElement.appendChild(XMLUtils.createElement("pictureID", picture.getPictureID().toString(), doc));
			}

			showTypeElement.appendChild(displayPicturesElement);

		}else{
			throw new URINotFoundException(uriParser);
		}

	}

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws Exception {

		this.appendUpdateFormData(null, doc, addTypeElement, user, req, uriParser);
	}

	protected List<String> getGalleryCategories(CommunityGroup group) throws SQLException {

		String sql = "SELECT DISTINCT g.category FROM picturegallery_galleries g WHERE g.galleryID IN (SELECT DISTINCT galleryID FROM picturegallery_gallerygroups WHERE groupID = ? ) AND g.category IS NOT null ORDER BY g.category ASC";
		
		ArrayListQuery<String> query = new ArrayListQuery<String>(galleryCRUDDAO.getAnnotatedDAO().getDataSource(), sql, stringPopulator);

		query.setInt(1, group.getGroupID());
		
		return query.executeQuery();
	}

	@Override
	protected void appendUpdateFormData(Gallery bean, Document doc, Element updateTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws Exception {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");
		
		callback.appendGroupAndAccess(doc, group, user); // Append user access

		// Append accessible groups and schools for the treeview
		Element accessibleXML = this.accessibleFactory.getAccessibleXML(user, doc);
		if(accessibleXML == null){
			throw new AccessDeniedException("Permission denied for user " + user);
		}

		updateTypeElement.appendChild(user.toXML(doc));
		updateTypeElement.appendChild(accessibleXML);
		
		Element categoriesElement = doc.createElement("Categories");
		
		Element categoryElement = doc.createElement("Category");
		XMLUtils.appendNewElement(doc, categoryElement, "name", callback.getEmptyCategoryName());
		XMLUtils.appendNewElement(doc, categoryElement, "value", "");
		categoriesElement.appendChild(categoryElement);
		
		List<String> categories = getGalleryCategories(group);
		
		if(bean != null && bean.getCategory() != null && (categories == null || !categories.contains(bean.getCategory()))){
			
			if(categories == null){
				categories = new ArrayList<String>(1);
			}
			
			categories.add(bean.getCategory());
			Collections.sort(categories);
		}
		
		if(categories != null){
			for(String category : categories){
				
				categoryElement = doc.createElement("Category");
				XMLUtils.appendNewElement(doc, categoryElement, "name", category);
				XMLUtils.appendNewElement(doc, categoryElement, "value", category);
				categoriesElement.appendChild(categoryElement);
			}
		}
		
		categoryElement = doc.createElement("Category");
		XMLUtils.appendNewElement(doc, categoryElement, "name", callback.getNewCategoryName());
		XMLUtils.appendNewElement(doc, categoryElement, "value", "_new");
		categoriesElement.appendChild(categoryElement);
		
		updateTypeElement.appendChild(categoriesElement);
	}

	protected Gallery populateRequest(Gallery gallery, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws SerialException, SQLException, ValidationException, AccessDeniedException {

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		this.accessibleFactory.validateAndSetAccess(req, gallery, validationErrors, user);
		if(!validationErrors.isEmpty()){
			throw new ValidationException(validationErrors);
		}

		return gallery;
	}

	@Override
	protected Gallery populateFromAddRequest(HttpServletRequest req, CommunityUser user, URIParser uriParser) throws ValidationException, Exception {

		Gallery gallery = super.populateFromAddRequest(req, user, uriParser);
		this.populateRequest(gallery, req, user, uriParser);
		gallery.setPosted(new Timestamp(System.currentTimeMillis()));
		return gallery;
	}

	@Override
	protected Gallery populateFromUpdateRequest(Gallery bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws ValidationException, Exception {

		Gallery gallery = super.populateFromUpdateRequest(bean, req, user, uriParser);
		this.populateRequest(gallery, req, user, uriParser);
		return gallery;
	}

	@Override
	protected List<Gallery> getAllBeans(CommunityUser user, HttpServletRequest req, URIParser uriParser) throws SQLException {

		CommunityGroup group = (CommunityGroup)req.getAttribute("group");

		return callback.getGalleryDAO().getGalleries(group, (String)req.getAttribute("pictureGallery.sortingPreferences.criteria"), (Order)req.getAttribute("pictureGallery.sortingPreferences.order"),false);
	}

	@Override
	protected void redirectToListMethod(HttpServletRequest req, HttpServletResponse res, Gallery bean) throws Exception {

		CommunityGroup group = (CommunityGroup)req.getAttribute("group");
		if(bean != null){
			res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/" + group.getGroupID() + "#" + bean.getGalleryID());
		}else{
			res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/" + group.getGroupID());
		}
	}

	@Override
	protected ForegroundModuleResponse beanAdded(Gallery bean, HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		CommunityGroup group = (CommunityGroup)req.getAttribute("group");
		res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/" + group.getGroupID() + "/showGallery/" + bean.getGalleryID());
		return null;
	}

	@Override
	protected void deleteBean(Gallery bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws Exception {

		callback.removeGallery(bean);

		super.deleteBean(bean, req, user, uriParser);
	}

	@Override
	protected List<Breadcrumb> getAddBreadcrumbs(HttpServletRequest req, CommunityUser user, URIParser uriParser) {

		CommunityGroup group = (CommunityGroup)req.getAttribute("group");
		return CollectionUtils.getList(callback.getGroupBreadcrumb(group), callback.getModuleBreadcrumb(group), callback.getAddBreadCrumb(group));
	}

	@Override
	protected List<Breadcrumb> getUpdateBreadcrumbs(Gallery bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws Exception {

		CommunityGroup group = (CommunityGroup)req.getAttribute("group");
		return CollectionUtils.getList(callback.getGroupBreadcrumb(group), callback.getModuleBreadcrumb(group), callback.getUpdateBreadCrumb(group, bean));
	}

	@Override
	protected List<Breadcrumb> getShowBreadcrumbs(Gallery bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) {

		CommunityGroup group = (CommunityGroup)req.getAttribute("group");
		return CollectionUtils.getList(callback.getGroupBreadcrumb(group), callback.getModuleBreadcrumb(group), this.callback.getShowGalleryBreadcrumb(group, bean));
	}

	@Override
	protected List<Breadcrumb> getListBreadcrumbs(HttpServletRequest req, CommunityUser user, URIParser uriParser, List<ValidationError> validationErrors) {

		CommunityGroup group = (CommunityGroup)req.getAttribute("group");
		return CollectionUtils.getList(callback.getGroupBreadcrumb(group), callback.getModuleBreadcrumb(group));
	}

	@Override
	public Gallery getRequestedBean(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, String getMode) throws SQLException, AccessDeniedException {

		Gallery gallery = super.getRequestedBean(req, res, user, uriParser, getMode);
		this.checkShowAccess(gallery, user, req, uriParser);
		return gallery;
	}

	@Override
	protected void checkShowAccess(Gallery bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException {

		CommunityGroup group = (CommunityGroup)req.getAttribute("group");
		if(bean != null && !callback.checkAccess(bean, group)){
			throw new AccessDeniedException("Show access for gallery denied");
		}
	}

	@Override
	protected void checkUpdateAccess(Gallery bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException {

		if(!callback.hasUpdateAccess(bean, user)){
			throw new AccessDeniedException("Update access for gallery denied");
		}
	}

	@Override
	protected void checkDeleteAccess(Gallery bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException {

		if(!callback.hasUpdateAccess(bean, user)){
			throw new AccessDeniedException("Delete access for secion denied");
		}
	}
}