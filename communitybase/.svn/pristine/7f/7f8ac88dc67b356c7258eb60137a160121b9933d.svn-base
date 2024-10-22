package se.dosf.communitybase.modules.newsletter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.fileupload.FileUploadException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.annotations.GroupMethod;
import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.IdentifiedEvent;
import se.dosf.communitybase.beans.Receipt;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.modules.AnnotatedCommunityModule;
import se.dosf.communitybase.modules.CommunityModule;
import se.dosf.communitybase.modules.newsletter.beans.NewsLetter;
import se.dosf.communitybase.modules.newsletter.daos.NewsLetterModuleDAO;
import se.dosf.communitybase.modules.newsletter.populators.NewsLetterPopulator;
import se.dosf.communitybase.utils.AccessUtils;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.SettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.foregroundmodules.ModuleSetting;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.StringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class NewsLetterModule extends AnnotatedCommunityModule implements CommunityModule {

	private static final ArrayList<SettingDescriptor> SETTINGDESCRIPTORS = new ArrayList<SettingDescriptor>();

	static {
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("diskThreshold", "Max upload size", "Maxmium upload size in megabytes allowed in a single post request", false, "100", new StringIntegerValidator(1,null)));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("ramThreshold", "RAM threshold", "How many megabytes of RAM to use as buffer during file uploads. If the threshold is exceeded the files are written to disk instead.", false, "20", new StringIntegerValidator(1,null)));
	}

	private NewsLetterModuleDAO newsletterModuleDAO;
	private static NewsLetterPopulator POPULATOR = new NewsLetterPopulator();
	private UserHandler userHandler;

	@XSLVariable
	protected String newNewsLetterText = "New newsletter: ";

	@XSLVariable
	protected String updateNewsletterBreadCrumb = "Update newsletter";

	@XSLVariable
	protected String addNewsletterBreadCrumb = "Add newsletter";

	@XSLVariable
	protected String readReceiptBreadCrumb = "Readreceipt";

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name="Editor CSS",description="Path to the desired CSS stylesheet for FCKEditor (relative from the contextpath)",required=false)
	protected String cssPath;
	
	@ModuleSetting
	protected Integer diskThreshold = 100;

	@ModuleSetting
	protected Integer ramThreshold = 20;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptorBean, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptorBean, sectionInterface, dataSource);

		this.userHandler = this.sectionInterface.getSystemInterface().getUserHandler();

		this.createDAOs(this.dataSource);

	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		super.update(moduleDescriptor, dataSource);

		this.userHandler = this.sectionInterface.getSystemInterface().getUserHandler();

		this.createDAOs(this.dataSource);

	}

	@Override
	protected void createDAOs(DataSource dataSource){

		this.newsletterModuleDAO = new NewsLetterModuleDAO(dataSource, this.userHandler);

	}


	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.show(req, res, user, uriParser, group);

	}

	@GroupMethod
	public ForegroundModuleResponse show(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		Document doc = this.createDocument(req, uriParser, group, user);

		Element newsletterModuleElement = doc.createElement("newslettermodule");

		doc.getFirstChild().appendChild(newsletterModuleElement);

		Element newslettersElement = doc.createElement("newsletters");
		Element showNewsletter = doc.createElement("showNewsletter");
		newsletterModuleElement.appendChild(showNewsletter);

		NewsLetter newsletter = null;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3))) {

			if((newsletter = this.newsletterModuleDAO.get(Integer.valueOf(uriParser.get(3)))) != null){

				log.info("User " + user + " requested newsletter " + newsletter + " in group " + group);

				this.checkNewsletterReadStatus(user, newsletter);

				this.appendNewsletterXML(newsletter, doc, showNewsletter);

			}else{
				throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
			}

		}

		// get all newsletters in group
		log.info("User " + user + " listing newsletters in group " + group);

		ArrayList<NewsLetter> newsletters = this.newsletterModuleDAO.getAll(group);

		if (newsletters != null) {

			if (!showNewsletter.hasChildNodes()) {

				newsletter = newsletters.get(0);

				this.checkNewsletterReadStatus(user, newsletter);

				this.appendNewsletterXML(newsletter, doc, showNewsletter);

			}

			newsletterModuleElement.appendChild(newslettersElement);

			for (NewsLetter letter : newsletters) {

				newslettersElement.appendChild(letter.toXML(doc));

			}

		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group));

	}

	@GroupMethod
	public ForegroundModuleResponse addNewsletter(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		ValidationException validationException = null;

		MultipartRequest requestWrapper = null;

		if (req.getMethod().equalsIgnoreCase("POST")) {

			NewsLetter newsletter = new NewsLetter();
			newsletter.setUserID(user.getUserID());
			newsletter.setGroupID(group.getGroupID());
			try {

				requestWrapper = new MultipartRequest(this.ramThreshold * BinarySizes.MegaByte, this.diskThreshold * BinarySizes.MegaByte, req);

				newsletter = POPULATOR.populate(newsletter, requestWrapper);

				log.info("User " + user + " adding newsletter " + newsletter + " to group " + group);

				Integer newsletterID = this.newsletterModuleDAO.add(newsletter);

				res.sendRedirect(uriParser.getCurrentURI(true) + this.getFullAlias(group) + "/show/" + newsletterID);

				return null;

			} catch (ValidationException e) {
				validationException = e;
			} catch (FileUploadException e) {

				log.info("Error uploading image in newsletter caused by read time out, uploaded by user " + user);

			}
		}

		Document doc = this.createDocument(req, uriParser, group, user);
		Element addNewsLetterElement = doc.createElement("addNewsletter");
		doc.getFirstChild().appendChild(addNewsLetterElement);

		if (validationException != null) {
			addNewsLetterElement.appendChild(RequestUtils.getRequestParameters(requestWrapper, doc));
			addNewsLetterElement.appendChild(validationException.toXML(doc));
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group), new Breadcrumb(addNewsletterBreadCrumb, addNewsletterBreadCrumb, "#"));

	}

	@GroupMethod
	public ForegroundModuleResponse updateNewsletter(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		this.checkAdminAccess(user, group);

		NewsLetter newsletter = null;

		ValidationException validationException = null;

		MultipartRequest requestWrapper = null;

		if (uriParser.size() != 4 || !NumberUtils.isInt(uriParser.get(3)) || (newsletter = this.newsletterModuleDAO.get(Integer.valueOf(uriParser.get(3)))) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);

		} else {

			if(req.getMethod().equalsIgnoreCase("POST")){

				try{

					log.info("User " + user + " updating newsletter " + newsletter);

					requestWrapper = new MultipartRequest(this.ramThreshold * BinarySizes.MegaByte, this.diskThreshold * BinarySizes.MegaByte, req);

					newsletter = POPULATOR.populate(newsletter, requestWrapper);

					log.info("User " + user + " updating newsletter " + newsletter + " in group " + group);

					this.newsletterModuleDAO.update(newsletter);

					res.sendRedirect(uriParser.getCurrentURI(true) + this.getFullAlias(group) + "/show/" + newsletter.getNewsletterID());

					return null;

				} catch(ValidationException e){
					validationException = e;
				} catch (FileUploadException e) {

					log.info("Error uploading image in newsletter caused by read time out, uploaded by user " + user);

				}

			}

			Document doc = this.createDocument(req, uriParser, group, user);
			Element updateNewsLetterElement = doc.createElement("updateNewsletter");
			doc.getFirstChild().appendChild(updateNewsLetterElement);

			updateNewsLetterElement.appendChild(newsletter.toXML(doc));

			if(validationException != null){
				updateNewsLetterElement.appendChild(RequestUtils.getRequestParameters(requestWrapper, doc));
				updateNewsLetterElement.appendChild(validationException.toXML(doc));
			}

			return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group), new Breadcrumb(updateNewsletterBreadCrumb, updateNewsletterBreadCrumb, "#"));

		}

	}

	@GroupMethod
	public ForegroundModuleResponse deleteNewsletter(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws URINotFoundException, NumberFormatException, SQLException, IOException, AccessDeniedException {

		this.checkAdminAccess(user, group);

		NewsLetter newsletter = null;

		if (uriParser.size() != 4 || !NumberUtils.isInt(uriParser.get(3)) || (newsletter = this.newsletterModuleDAO.get(Integer.valueOf(uriParser.get(3)))) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);

		} else {

			log.info("User " + user + " deleting newsletter " + newsletter);

			log.info("User " + user + " deleting newsletter " + newsletter + " from group " + group);

			this.newsletterModuleDAO.delete(newsletter);

			this.redirectToDefaultMethod(req, res, group);

			return null;
		}

	}

	@GroupMethod
	public ForegroundModuleResponse showReadReceipt(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		NewsLetter newsLetter;

		if (uriParser.size() != 4 || !NumberUtils.isInt(uriParser.get(3)) || (newsLetter = this.newsletterModuleDAO.get(Integer.valueOf(uriParser.get(3)))) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);

		} else {

			log.info("User " + user + " listing newsletter read receipts for " + newsLetter + " in group " + group);

			Document doc = this.createDocument(req, uriParser, group, user);

			Element showReceiptElement = doc.createElement("showReadReceipt");

			doc.getFirstChild().appendChild(showReceiptElement);

			showReceiptElement.appendChild(newsLetter.toXML(doc));

			ArrayList<Receipt> receipts = this.newsletterModuleDAO.getNewsletterReceipt(newsLetter);

			XMLUtils.append(doc, showReceiptElement, receipts);

			return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group), new Breadcrumb(readReceiptBreadCrumb, readReceiptBreadCrumb, "#"));

		}

	}

	@GroupMethod
	public ForegroundModuleResponse getImage(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		NewsLetter newsletter = null;

		// check that the requested gallery exist
		if (uriParser.size() != 4 || !NumberUtils.isInt(uriParser.get(3)) || (newsletter = this.newsletterModuleDAO.get(Integer.valueOf(uriParser.get(3)))) == null) {
			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
		} else {

			try {
				writePicture(newsletter.getImage(), newsletter.getTitle() + ".jpg", res);
			} catch (IOException e) {
				log.info("Caught exception " + e + " while sending picture in newsletter " + newsletter + " requested by user " + user);
			}

		}

		return null;

	}

	public static void writePicture(Blob blob, String filename, HttpServletResponse res) throws SQLException, IOException {

		// send thumb to user
		res.setContentLength((int) blob.length());

		res.setContentType("image/jpeg");
		res.setHeader("Content-Disposition", "inline; filename=" + filename);

		InputStream in = blob.getBinaryStream();
		OutputStream out = res.getOutputStream();

		byte[] buf = new byte[1024];
		int count = 0;

		while ((count = in.read(buf)) >= 0) {
			out.write(buf, 0, count);
		}
		in.close();
		out.close();

	}

	private void appendNewsletterXML(NewsLetter newsletter, Document doc, Element element){

		element.appendChild(newsletter.toXML(doc));

		Element postedUserElement = this.getPostedUserXML(newsletter, doc);

		if(postedUserElement != null){
			element.appendChild(postedUserElement);
		}

	}

	private Element getPostedUserXML(NewsLetter newsletter, Document doc){

		CommunityUser postedUser = (CommunityUser)this.userHandler.getUser(newsletter.getUserID(), false);

		if(postedUser != null){

			Element postedUserElement = doc.createElement("postedUser");

			postedUserElement.appendChild(postedUser.toXML(doc));

			return postedUserElement;

		}

		return null;

	}

	private void checkNewsletterReadStatus(CommunityUser user, NewsLetter newsletter) throws SQLException{
		this.newsletterModuleDAO.checkNewsletterReadStatus(user, newsletter);
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, CommunityGroup group, CommunityUser user) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		document.appendChild(group.toXML(doc));
		if(this.cssPath != null) {
			XMLUtils.appendNewElement(doc, document, "cssPath", this.cssPath);
		}
		if (AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER)) {
			document.appendChild(XMLUtils.createElement("isAdmin", "true", doc));
		}
		doc.appendChild(document);

		return doc;
	}

	@Override
	public List<? extends SettingDescriptor> getSettings() {

		ArrayList<SettingDescriptor> settingsList = new ArrayList<SettingDescriptor>();

		List<? extends SettingDescriptor> superSettings = super.getSettings();

		if (superSettings != null) {

			settingsList.addAll(superSettings);
		}

		settingsList.addAll(SETTINGDESCRIPTORS);

		return settingsList;
	}

	public List<IdentifiedEvent> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp) throws Exception {

		List<IdentifiedEvent> events = this.newsletterModuleDAO.getEvents(group, startStamp);

		if(events != null){

			for(IdentifiedEvent event : events){
				event.setTitle(this.newNewsLetterText + StringUtils.substring(event.getTitle(), 50, "..."));
				event.setDescription(event.getTitle());
				event.setFullAlias(this.getFullAlias(group) + "?newsletterid=" + event.getId());
			}
		}

		return events;
	}

}
