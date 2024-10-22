package se.dosf.communitybase.modules.pictureGallery.cruds;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.cruds.IntegerBasedCommunityBaseCRUD;
import se.dosf.communitybase.modules.pictureGallery.PictureGalleryModule;
import se.dosf.communitybase.modules.pictureGallery.beans.Gallery;
import se.dosf.communitybase.modules.pictureGallery.beans.Picture;
import se.dosf.communitybase.modules.pictureGallery.enums.ImageSize;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.dao.querys.PreparedStatementQuery;
import se.unlogic.standardutils.db.DBUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class PictureCRUD extends IntegerBasedCommunityBaseCRUD<Picture, PictureGalleryModule> {

	private final AnnotatedDAO<Picture> pictureDAO;
	private final QueryParameterFactory<Picture, Integer> pictureIDParamFactory;
	private final QueryParameterFactory<Picture, Gallery> pictureGalleryParamFactory;

	public PictureCRUD(AnnotatedDAOWrapper<Picture, Integer> pictureCRUDDAO, String typeElementName, String typeLogName, String listMethodAlias, PictureGalleryModule pictureArchiveModule) {

		super(pictureCRUDDAO, null, typeElementName, typeLogName, listMethodAlias, pictureArchiveModule);

		this.pictureDAO = pictureCRUDDAO.getAnnotatedDAO();

		this.pictureIDParamFactory = this.pictureDAO.getParamFactory("pictureID", Integer.class);
		this.pictureGalleryParamFactory = this.pictureDAO.getParamFactory("gallery", Gallery.class);
	}

	@Override
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, List<ValidationError> validationErrors) throws Exception {

		this.checkListAccess(user,req,uriParser);

		log.info("User " + user + " listing " + this.typeLogPluralName);

		Document doc = this.callback.createDocument(req, uriParser, user);
		Element listTypeElement = doc.createElement("List" + this.typeElementPluralName);
		doc.getFirstChild().appendChild(listTypeElement);

		List<Picture> pictures = this.getAllBeans(user, req, uriParser);

		this.appendAllBeans(pictures, doc, listTypeElement, user, req, uriParser, validationErrors);

		if (validationErrors != null) {
			XMLUtils.append(doc, listTypeElement, validationErrors);
			listTypeElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		this.appendListFormData(doc, listTypeElement, user, req, uriParser, validationErrors);

		int num = pictures != null ? pictures.size() : 0;

		listTypeElement.appendChild(XMLUtils.createElement("numPics", String.valueOf(num), doc));

		int nrOfThumbsPerPage = this.callback.getNumOfThumbsPerFilePage();

		int pagesInGallery = num % nrOfThumbsPerPage == 0 ? num / nrOfThumbsPerPage : (num / nrOfThumbsPerPage) + 1;

		Integer page = 1;

		if (uriParser.size() == 4) {
			page = NumberUtils.toInt(uriParser.get(3));

			if (page == null || page <= 0) {
				throw new URINotFoundException(uriParser);
			}
		}

		if (num == 0) {

			listTypeElement.appendChild(XMLUtils.createElement("pages", "1", doc));
			listTypeElement.appendChild(XMLUtils.createElement("currentPage", "1", doc));

		} else if (page <= pagesInGallery) {

			// find next and previous page
			Integer nextPage = page == pagesInGallery ? null : page + 1;
			Integer prevPage = page == 1 ? null : page - 1;

			// calculate start- and endindex
			int startIndex = (nrOfThumbsPerPage * page) - nrOfThumbsPerPage;
			int endIndex = startIndex + nrOfThumbsPerPage;
			if (page == pagesInGallery) {
				endIndex = pictures.size();
			}

			//Element filesElement = doc.createElement("files");

			listTypeElement.appendChild(XMLUtils.createElement("pages", String.valueOf(pagesInGallery), doc));
			listTypeElement.appendChild(XMLUtils.createElement("currentPage", String.valueOf(page), doc));

			if (nextPage != null) {
				listTypeElement.appendChild(XMLUtils.createElement("nextPage", nextPage.toString(), doc));
			}
			if (prevPage != null) {
				listTypeElement.appendChild(XMLUtils.createElement("prevPage", prevPage.toString(), doc));
			}

			Element displayPicturesElement = doc.createElement("DisplayPictures");

			// Set pictures to show on this page
			for (int i = startIndex; i < endIndex; i++) {
				Picture picture = pictures.get(i);
				displayPicturesElement.appendChild(XMLUtils.createElement("pictureID", picture.getPictureID().toString(), doc));
			}

			listTypeElement.appendChild(displayPicturesElement);

		} else {
			throw new URINotFoundException(uriParser);
		}

		SimpleForegroundModuleResponse moduleResponse = new SimpleForegroundModuleResponse(doc, getListTitle(req, user, uriParser));

		moduleResponse.addBreadcrumbsLast(getListBreadcrumbs(req, user, uriParser,validationErrors));

		return moduleResponse;
	}


	@Override
	protected void appendListFormData(Document doc, Element listTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser, List<ValidationError> validationErrors) throws SQLException {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

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

		this.callback.appendGroupAndAccess(doc, group, user); // Append user access

		XMLUtils.appendNewElement(doc, listTypeElement, "allowsGalleryDownload", this.callback.getAllowsGalleryDownload());

		super.appendListFormData(doc, listTypeElement, user, req, uriParser, validationErrors);
	}

	protected void appendAllBeans(List<Picture> pictures, Document doc, Element listTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser, List<ValidationError> validationErrors) throws SQLException {

		if(!CollectionUtils.isEmpty(pictures)) {

			Element sectionsElement = XMLUtils.appendNewElement(doc, listTypeElement, this.typeElementPluralName);

			for(Picture picture : pictures) {

				Element pictureElement = picture.toXML(doc);

				if(callback.hasUpdateAccess(picture, user)) {

					XMLUtils.appendNewElement(doc, pictureElement, "hasUpdateAccess", "true");

				}

				sectionsElement.appendChild(pictureElement);

			}

		}

	}

	@Override
	public ForegroundModuleResponse show(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, List<ValidationError> validationErrors) throws Exception {

		Picture picture = getRequestedBean(req, res, user, uriParser, ImageSize.MEDIUM.toString());

		if (picture == null) {
			throw new URINotFoundException(uriParser);
		}

		return showBean(picture, req, res, user, uriParser, validationErrors);
	}

	@Override
	protected void appendShowFormData(Picture bean, Document doc, Element showTypeElement, CommunityUser user, HttpServletRequest req, HttpServletResponse res, URIParser uriParser) throws SQLException, IOException, Exception {

		this.callback.appendGroupAndAccess(doc, (CommunityGroup) req.getAttribute("group"), user); // Append user access

		if(callback.hasUpdateAccess(bean, user)) {
			XMLUtils.appendNewElement(doc, showTypeElement, "hasUpdateAccess", "true");
		}
		
		XMLUtils.appendNewCDATAElement(doc, showTypeElement, "ImageRedistributionDisclaimer", callback.getImageRedistributionDisclaimer());

		HighLevelQuery<Picture> query = new HighLevelQuery<Picture>();

		query.addParameter(pictureGalleryParamFactory.getParameter(bean.getGallery()));
		query.disableAutoRelations(true);
		
		List<Picture> pictures = this.pictureDAO.getAll(query);

		boolean found = false;
		int currentIdx = -1;
		Picture picture = null;
		for (int i = 0; i < pictures.size(); i++) {
			picture = pictures.get(i);
			if (picture.getPictureID().equals(bean.getPictureID())) {
				found = true;
				currentIdx = i;
				break;
			}
		}

		if (found) {

			//log.info("User " + user + " requested picture " + picture + " in gallery " + gallery);

			// Append pagination logics
			Integer nextPicture = currentIdx < pictures.size() - 1 ? pictures.get(currentIdx + 1).getPictureID() : null;
			Integer prevPicture = currentIdx > 0 ? pictures.get(currentIdx - 1).getPictureID() : null;

			showTypeElement.appendChild(XMLUtils.createElement("numPics", pictures.size(), doc));
			showTypeElement.appendChild(XMLUtils.createElement("currentPic", currentIdx + 1, doc));

			if (nextPicture != null) {
				showTypeElement.appendChild(XMLUtils.createElement("next", nextPicture.toString(), doc));
			}
			if (prevPicture != null) {
				showTypeElement.appendChild(XMLUtils.createElement("prev", prevPicture.toString(), doc));
			}

			showTypeElement.appendChild(XMLUtils.createElement("allowComments", this.callback.getAllowsComments(), doc));

			// Append user preference for showing/not showing picture comments
			if (this.callback.getAllowsComments()) {
				Boolean showAll = (Boolean) req.getSession().getAttribute(this.callback.getModuleDescriptor().getModuleID() + ".showAll");
				if (showAll == null || showAll) {
					showTypeElement.appendChild(XMLUtils.createElement("showAll", "true", doc));
				}
			}

			// Append originating page in order to be able to return to the right page within pagination
			if(uriParser.size() > 4) {
				showTypeElement.appendChild(XMLUtils.createElement("originatingPage", uriParser.get(4), doc));
			}


		} else {
			throw new URINotFoundException(uriParser);
		}
	}

	@Override
	protected List<Picture> getAllBeans(CommunityUser user, HttpServletRequest req, URIParser uriParser) throws SQLException {

		//This is done in two separate queries since MySQL handles nested queries very slowly

		// Get pictures accessible from group or school
		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		Connection connection = null;
		ArrayListQuery<Integer> galleryQuery = null;

		try{
			//long startTime = System.currentTimeMillis();

			connection = pictureDAO.getDataSource().getConnection();

			galleryQuery = new ArrayListQuery<Integer>(connection, false, "SELECT DISTINCT n.galleryID FROM picturegallery_galleries as n " +
					"LEFT OUTER JOIN picturegallery_gallerygroups as gg ON n.galleryID = gg.galleryID " +
					"LEFT OUTER JOIN picturegallery_galleryschools as gs ON n.galleryID = gs.galleryID " +
					"WHERE gg.groupID = ? OR gs.schoolID = ? OR n.global = true", IntegerPopulator.getPopulator());

			galleryQuery.setInt(1, group.getGroupID());
			galleryQuery.setInt(2, group.getSchool().getSchoolID());

			List<Integer> galleryIDs = galleryQuery.executeQuery();

			if(galleryIDs == null){

				return null;
			}

			LowLevelQuery<Picture> query = new LowLevelQuery<Picture>();

			//Exclude blobs
			String sql = "SELECT pictureID, galleryID, filename, posted, poster, null as small, null as medium, null as full FROM " + this.pictureDAO.getTableName() +
					" WHERE galleryID IN (" + StringUtils.toCommaSeparatedString(galleryIDs) + ")";

			if(req.getAttribute("pictureGallery.sortingPreferences.criteria") != null) {
				sql += " ORDER BY " + req.getAttribute("pictureGallery.sortingPreferences.criteria");
			}

			if(req.getAttribute("pictureGallery.sortingPreferences.order") != null) {
				sql += " " + req.getAttribute("pictureGallery.sortingPreferences.order");
			}

			query.setSql(sql);
			query.disableAutoRelations(true);
			query.addRelation(Picture.GALLERY_RELATION);

			//log.fatal("Query time: " + TimeUtils.millisecondsToString(System.currentTimeMillis() - startTime) + " ms");

			return this.pictureDAO.getAll(query,connection);

		}finally{

			PreparedStatementQuery.autoCloseQuery(galleryQuery);
			DBUtils.closeConnection(connection);
		}
	}

	@Override
	protected void redirectToListMethod(HttpServletRequest req, HttpServletResponse res, Picture bean) throws Exception {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		if(bean != null) {
			res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/" + group.getGroupID() + "#" + bean.getGallery().getGalleryID());
		} else {
			res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/" + group.getGroupID());
		}
	}

	@Override
	protected List<Breadcrumb> getShowBreadcrumbs(Picture bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) {
		CommunityGroup group = (CommunityGroup) req.getAttribute("group");
		return CollectionUtils.getList(this.callback.getGroupBreadcrumb(group), this.callback.getModuleBreadcrumb(group), this.callback.getShowGalleryBreadcrumb(group, bean.getGallery()), this.callback.getShowPictureBreadcrumb(group, bean));
	}

	@Override
	protected List<Breadcrumb> getListBreadcrumbs(HttpServletRequest req, CommunityUser user, URIParser uriParser, List<ValidationError> validationErrors) {
		CommunityGroup group = (CommunityGroup) req.getAttribute("group");
		return CollectionUtils.getList(this.callback.getGroupBreadcrumb(group), this.callback.getModuleBreadcrumb(group));
	}


	@Override
	public Picture getRequestedBean(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, String getMode) throws SQLException, AccessDeniedException {

		Picture picture = null;

		if (uriParser.size() > 3 && NumberUtils.isInt(uriParser.get(3))) {

			HighLevelQuery<Picture> query = new HighLevelQuery<Picture>();

			query.disableAutoRelations(true);
			query.addRelations(Picture.GALLERY_RELATION, Gallery.GROUP_RELATION, Gallery.SCHOOL_RELATION, Picture.COMMENT_RELATION);
//
//			ImageSize imageSize = EnumUtils.toEnum(ImageSize.class, getMode);
//
//			// Exclude blobs if imageSize is set
//			if(imageSize != null) {
//
//				if(imageSize == ImageSize.SMALL){
//
//					query.addExcludedField(Picture.MEDIUM_THUMB_FIELD);
//					query.addExcludedField(Picture.DATA_FIELD);
//
//				}else if(imageSize == ImageSize.MEDIUM){
//
//					query.addExcludedField(Picture.SMALL_THUMB_FIELD);
//					query.addExcludedField(Picture.DATA_FIELD);
//
//				}else{
//
//					query.addExcludedField(Picture.SMALL_THUMB_FIELD);
//					query.addExcludedField(Picture.MEDIUM_THUMB_FIELD);
//				}
//			}

			query.addParameter(this.pictureIDParamFactory.getParameter(NumberUtils.toInt(uriParser.get(3))));

			picture = this.pictureDAO.get(query);

			this.checkShowAccess(picture, user, req, uriParser);

		}

		return picture;
	}

	@Override
	protected void checkShowAccess(Picture bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException {
		CommunityGroup group = (CommunityGroup) req.getAttribute("group");
		if(bean != null && !this.callback.checkAccess(bean.getGallery(), group)) {
			throw new AccessDeniedException("Show access for picture denied");
		}
	}

	@Override
	protected void checkDeleteAccess(Picture bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException {
		if(!this.callback.hasUpdateAccess(bean, user)) {
			throw new AccessDeniedException("Delete access for picture denied");
		}
	}

	@Override
	protected ForegroundModuleResponse beanDeleted(Picture bean, HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		callback.removePictureFromDisk(bean);

		res.sendRedirect(req.getContextPath() + callback.getFullAlias((CommunityGroup) req.getAttribute("group")) + "/showGallery/" + bean.getGallery().getGalleryID());

		return null;
	}
}