package se.dosf.communitybase.modules.newsletter.cruds;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.cruds.IntegerBasedCommunityBaseCRUD;
import se.dosf.communitybase.daos.ReceiptDAO;
import se.dosf.communitybase.modules.newsletter.NewsLetterModule;
import se.dosf.communitybase.modules.newsletter.beans.NewsLetter;
import se.dosf.communitybase.modules.newsletter.beans.NewsLetterReceipt;
import se.dosf.communitybase.utils.PublishingFactory;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.foregroundmodules.imagegallery.SimpleFileFilter;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.image.ImageUtils;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.standardutils.xml.XMLable;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.URIParser;

public class NewsLetterCRUD extends IntegerBasedCommunityBaseCRUD<NewsLetter, NewsLetterModule> {

	public static final Field RECEIPT_RELATION = ReflectionUtils.getField(NewsLetter.class, "receipts");

	protected final QueryParameterFactory<NewsLetter, Integer> newsLetterIDParamFactory;

	protected final ReceiptDAO<NewsLetterReceipt> receiptDAO;
	protected final AnnotatedDAOWrapper<NewsLetter, Integer> newsLetterCRUDDAO;

	private NewsLetterModule newsLetterModule;

	private PublishingFactory publishingFactory;

	public NewsLetterCRUD(AnnotatedDAOWrapper<NewsLetter, Integer> newsLetterCRUDDAO, BeanRequestPopulator<NewsLetter> populator, String typeElementName, String typeLogName, String listMethodAlias, NewsLetterModule newsLetterModule, ReceiptDAO<NewsLetterReceipt> receiptDAO) {

		super(newsLetterCRUDDAO, populator, typeElementName, typeLogName, listMethodAlias, newsLetterModule);

		this.receiptDAO = receiptDAO;
		this.newsLetterCRUDDAO = newsLetterCRUDDAO;
		this.newsLetterModule = newsLetterModule;

		this.publishingFactory = new PublishingFactory(newsLetterModule);
		this.newsLetterIDParamFactory = this.newsLetterCRUDDAO.getAnnotatedDAO().getParamFactory("newsletterID", Integer.class);
	}

	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		NewsLetter newsLetter = null;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3))) {

			if((newsLetter = this.newsLetterCRUDDAO.get(Integer.valueOf(uriParser.get(3)))) != null && this.newsLetterModule.checkAccess(newsLetter, group)){

				log.info("User " + user + " requested newsletter " + newsLetter + " in group " + group);

				req.setAttribute("newsLetter", newsLetter);

			} else {
				throw new URINotFoundException(this.newsLetterModule.getSectionDescriptor(), this.newsLetterModule.getModuleDescriptor(), uriParser);
			}

		}

		req.setAttribute("group", group);

		ForegroundModuleResponse moduleResponse = super.list(req, res, user, uriParser, null);

		if(newsLetter != null || (newsLetter = (NewsLetter) req.getAttribute("firstnewsletter")) != null) {

			this.receiptDAO.checkReadStatus(user, newsLetter.getNewsletterID());

			Document doc = moduleResponse.getDocument();

			if(this.newsLetterModule.hasUpdateAccess(newsLetter, user)) {
				XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "hasUpdateAccess", true);
			}
		}

		return moduleResponse;
	}

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws Exception {

		this.newsLetterModule.appendGroupAndAccess(doc, (CommunityGroup) req.getAttribute("group"), user);

		Element publishingXML = this.publishingFactory.getPublisherXML(user, doc);

		if(publishingXML == null) {
			throw new AccessDeniedException(this.newsLetterModule.getSectionDescriptor(), this.newsLetterModule.getModuleDescriptor(), "Permission denied for user " + user);
		}

		addTypeElement.appendChild(user.toXML(doc));
		addTypeElement.appendChild(publishingXML);
	}

	@Override
	protected void appendUpdateFormData(NewsLetter bean, Document doc, Element updateTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws Exception {

		this.appendAddFormData(doc, updateTypeElement, user, req, uriParser);

	}

	protected NewsLetter populateRequest(NewsLetter newsLetter, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws SerialException, SQLException, ValidationException {

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		boolean pictureUpload = req.getParameter("uploadCheck") != null;

		if(pictureUpload && (req instanceof MultipartRequest)){

			MultipartRequest multipartRequest = (MultipartRequest)req;

			newsLetter.setImage(this.getUploadedImage(multipartRequest, user, uriParser, validationErrors));

			newsLetter.setImageLocation(multipartRequest.getParameter("imagelocation"));

		}

		this.publishingFactory.validateAndSetPublishing(req, newsLetter, validationErrors);

		if(!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}

		newsLetter.setPoster(user);

		return newsLetter;
	}

	@Override
	protected NewsLetter populateFromAddRequest(HttpServletRequest req, CommunityUser user, URIParser uriParser) throws ValidationException, SQLException {

		NewsLetter newsLetter = super.populateFromAddRequest(req, user, uriParser);

		this.populateRequest(newsLetter, req, user, uriParser);

		newsLetter.setDate(new Timestamp(System.currentTimeMillis()));

		return newsLetter;
	}

	@Override
	protected NewsLetter populateFromUpdateRequest(NewsLetter bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws ValidationException, SQLException {

		NewsLetter newsLetter = super.populateFromUpdateRequest(bean, req, user, uriParser);

		this.populateRequest(newsLetter, req, user, uriParser);

		String imageLocation = req.getParameter("imagelocation.old");

		if(imageLocation != null){
			newsLetter.setImageLocation(imageLocation);
		}

		boolean deleteImage = req.getParameter("deleteImage") != null;

		if(deleteImage){
			newsLetter.setImage(null);
			newsLetter.setImageLocation(null);
		}

		return newsLetter;
	}

	@Override
	protected void appendListFormData(Document doc, Element listTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser, ValidationError validationError) throws SQLException {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");
		NewsLetter newsLetter = (NewsLetter) req.getAttribute("newsLetter");

		if(newsLetter != null || (newsLetter = (NewsLetter) req.getAttribute("firstnewsletter")) != null) {
			newsLetter.setFullDate(this.newsLetterModule.getUserLanguage(user));
			listTypeElement.appendChild(newsLetter.toXML(doc));
		}

		this.newsLetterModule.appendGroupAndAccess(doc, group, user);

		super.appendListFormData(doc, listTypeElement, user, req, uriParser, validationError);
	}

	@Override
	protected Collection<? extends XMLable> getAllBeans(CommunityUser user, HttpServletRequest req, URIParser uriParser) throws SQLException {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		LowLevelQuery<NewsLetter> query = new LowLevelQuery<NewsLetter>();

		query.setSql("SELECT DISTINCT n.* FROM " + this.newsLetterCRUDDAO.getAnnotatedDAO().getTableName() + " as n " +
				"LEFT OUTER JOIN groupnewsletters as gn ON n.weekLetterID = gn.weekLetterID " +
				"LEFT OUTER JOIN schoolnewsletters as sn ON n.weekLetterID = sn.weekLetterID " +
				"WHERE gn.groupID = ? OR sn.schoolID = ? OR n.global = ? " +
				"ORDER BY date DESC");

		query.addParameters(group.getGroupID(), group.getSchool().getSchoolID(), true);

		List<NewsLetter> newsLetters = this.newsLetterCRUDDAO.getAnnotatedDAO().getAll(query);

		if(newsLetters != null) {
			req.setAttribute("firstnewsletter", newsLetters.get(0));
		}

		return newsLetters;
	}

	@Override
	protected HttpServletRequest parseRequest(HttpServletRequest req, CommunityUser user) throws ValidationException {

		if(MultipartRequest.isMultipartRequest(req)){

			try {
				return new MultipartRequest(this.newsLetterModule.getRamThreshold() * BinarySizes.MegaByte, this.newsLetterModule.getDiskThreshold() * BinarySizes.MegaByte, req);

			} catch (SizeLimitExceededException e) {

				throw new ValidationException(new ValidationError("FileSizeLimitExceeded"));

			} catch (FileSizeLimitExceededException e) {

				throw new ValidationException(new ValidationError("FileSizeLimitExceeded"));

			} catch (FileUploadException e) {

				throw new ValidationException(new ValidationError("UnableToParseRequest"));
			}
		}

		return req;
	}

	@Override
	protected void releaseRequest(HttpServletRequest req, CommunityUser user) {

		if (req instanceof MultipartRequest) {

			((MultipartRequest)req).deleteFiles();
		}
	}

	@Override
	protected ForegroundModuleResponse beanDeleted(NewsLetter bean, HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		return super.beanDeleted(null, req, res, user, uriParser);

	}

	public ForegroundModuleResponse showReadReceipt(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		if (uriParser.size() > 3 && NumberUtils.isInt(uriParser.get(3))) {

			HighLevelQuery<NewsLetter> query = new HighLevelQuery<NewsLetter>();

			query.addRelation(RECEIPT_RELATION);

			query.addParameter(this.newsLetterIDParamFactory.getParameter(NumberUtils.toInt(uriParser.get(3))));

			NewsLetter newsLetter = this.newsLetterCRUDDAO.getAnnotatedDAO().get(query);

			if (newsLetter != null) {

				this.checkUpdateAccess(newsLetter, user, req, uriParser);

				log.info("User " + user + " listing newsletter read receipts for " + newsLetter + " in group " + group);

				Document doc = this.newsLetterModule.createDocument(req, uriParser, user);

				this.newsLetterModule.appendGroupAndAccess(doc, group, user);

				Element showReceiptElement = doc.createElement("ShowReadReceipt");

				doc.getFirstChild().appendChild(showReceiptElement);

				showReceiptElement.appendChild(newsLetter.toXML(doc));

				return new SimpleForegroundModuleResponse(doc, this.newsLetterModule.getModuleDescriptor().getName(), this.newsLetterModule.getGroupBreadcrumb(group), this.newsLetterModule.getModuleBreadcrumb(group), this.newsLetterModule.getReadReceiptBreadCrumb());

			}

		}

		throw new URINotFoundException(this.newsLetterModule.getSectionDescriptor(), this.newsLetterModule.getModuleDescriptor(), uriParser);

	}

	private Blob getUploadedImage(MultipartRequest multipartRequest, User user, URIParser uriParser, List<ValidationError> validationErrors) throws SerialException, SQLException {

		FileItem fileItem = null;

		if (multipartRequest.getFileCount() > 0) {

			fileItem = multipartRequest.getFile(0);

			if (StringUtils.isEmpty(fileItem.getName())) {

				validationErrors.add(new ValidationError("NoFile"));

			} else {

				if(!SimpleFileFilter.isValidFilename(fileItem.getName())){
					validationErrors.add(new ValidationError("BadFileFormat"));
				}

			}

			if(validationErrors.isEmpty()) {

				try {

					BufferedImage image = ImageUtils.getImage(fileItem.getInputStream());

					BufferedImage smallImage = ImageUtils.scaleImage(image, 400, 500, Image.SCALE_SMOOTH,BufferedImage.TYPE_INT_RGB);

					ByteArrayOutputStream smallImageStream = new ByteArrayOutputStream();

					ImageIO.write(smallImage, "jpg", smallImageStream);

					byte[] smallImageByteArray = smallImageStream.toByteArray();

					return new SerialBlob(smallImageByteArray);

				} catch (Exception e) {
					validationErrors.add(new ValidationError("UnableToParseRequest"));
				}

			}

		}

		return null;
	}

	@Override
	protected void redirectToListMethod(HttpServletRequest req, HttpServletResponse res, NewsLetter bean) throws Exception {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		boolean access = this.newsLetterModule.checkAccess(bean, group);

		res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/" + group.getGroupID() + (bean != null && access ? listMethodAlias + "/" + bean.getNewsletterID() : ""));

	}

	@Override
	protected List<Breadcrumb> getAddBreadcrumbs(HttpServletRequest req, CommunityUser user, URIParser uriParser) {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		return CollectionUtils.getList(this.newsLetterModule.getGroupBreadcrumb(group), this.newsLetterModule.getModuleBreadcrumb(group), this.newsLetterModule.getAddBreadCrumb(group));
	}

	@Override
	protected List<Breadcrumb> getUpdateBreadcrumbs(NewsLetter bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws Exception {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		return CollectionUtils.getList(this.newsLetterModule.getGroupBreadcrumb(group), this.newsLetterModule.getModuleBreadcrumb(group), this.newsLetterModule.getUpdateBreadCrumb(group, bean));
	}

	@Override
	protected List<Breadcrumb> getShowBreadcrumbs(NewsLetter bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		return CollectionUtils.getList(this.newsLetterModule.getGroupBreadcrumb(group), this.newsLetterModule.getModuleBreadcrumb(group));
	}

	@Override
	protected List<Breadcrumb> getListBreadcrumbs(HttpServletRequest req, CommunityUser user, URIParser uriParser, ValidationError validationError) {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		return CollectionUtils.getList(this.newsLetterModule.getGroupBreadcrumb(group), this.newsLetterModule.getModuleBreadcrumb(group));
	}

	@Override
	public NewsLetter getRequestedBean(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, String getMode) throws SQLException, AccessDeniedException {

		NewsLetter newsLetter = super.getRequestedBean(req, res, user, uriParser, getMode);

		this.checkShowAccess(newsLetter, user, req, uriParser);

		return newsLetter;

	}

	@Override
	protected void checkShowAccess(NewsLetter bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		if(bean != null && !this.newsLetterModule.checkAccess(bean, group)) {
			throw new AccessDeniedException(this.newsLetterModule.getSectionDescriptor(), this.newsLetterModule.getModuleDescriptor(), "Show access for newsletter denied");
		}

	}

	@Override
	protected void checkUpdateAccess(NewsLetter bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException {

		if(!this.newsLetterModule.hasUpdateAccess(bean, user)) {
			throw new AccessDeniedException(this.newsLetterModule.getSectionDescriptor(), this.newsLetterModule.getModuleDescriptor(), "Update access for newsletter denied");
		}

	}

	@Override
	protected void checkDeleteAccess(NewsLetter bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException {

		if(!this.newsLetterModule.hasUpdateAccess(bean, user)) {
			throw new AccessDeniedException(this.newsLetterModule.getSectionDescriptor(), this.newsLetterModule.getModuleDescriptor(), "Delete access for newsletter denied");
		}

	}

}
