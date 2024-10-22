package se.dosf.communitybase.modules.groupfirstpage;

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
import se.dosf.communitybase.beans.Event;
import se.dosf.communitybase.beans.IdentifiedEvent;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.enums.ModuleType;
import se.dosf.communitybase.modules.AnnotatedCommunityModule;
import se.dosf.communitybase.modules.CommunityModule;
import se.dosf.communitybase.modules.groupfirstpage.beans.GroupFirstpage;
import se.dosf.communitybase.modules.groupfirstpage.daos.GroupFirstpageDAO;
import se.dosf.communitybase.modules.groupfirstpage.populators.GroupFirstpagePopulator;
import se.dosf.communitybase.utils.AccessUtils;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.SettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.foregroundmodules.ModuleSetting;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.StringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class GroupFirstpageModule extends AnnotatedCommunityModule implements CommunityModule {

	private static final ArrayList<SettingDescriptor> SETTINGDESCRIPTORS = new ArrayList<SettingDescriptor>();

	static {
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextAreaSetting("noFirstpageText", "Standardtext", "Standardtext på första sidan för gruppen", false, "", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("diskThreshold", "Max upload size", "Maxmium upload size in megabytes allowed in a single post request", false, "100", new StringIntegerValidator(1,null)));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("ramThreshold", "RAM threshold", "How many megabytes of RAM to use as buffer during file uploads. If the threshold is exceeded the files are written to disk instead.", false, "20", new StringIntegerValidator(1,null)));
	}


	private static GroupFirstpagePopulator FIRSTPAGE_POPULATOR = new GroupFirstpagePopulator();
	private GroupFirstpageDAO groupFirstpageDAO;

	@XSLVariable
	protected String newFirstpageText = "New firstpage: ";

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name="Editor CSS",description="Path to the desired CSS stylesheet for FCKEditor (relative from the contextpath)",required=false)
	protected String cssPath;
	
	@ModuleSetting
	protected String noFirstpageText = "No firstpage";

	@ModuleSetting
	protected Integer diskThreshold = 100;

	@ModuleSetting
	protected Integer ramThreshold = 20;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptorBean, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		super.init(moduleDescriptorBean, sectionInterface, dataSource);

		this.groupFirstpageDAO = new GroupFirstpageDAO(dataSource);

	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {
		super.update(moduleDescriptor, dataSource);

		this.groupFirstpageDAO = new GroupFirstpageDAO(dataSource);

	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		Document doc = this.createDocument(req, uriParser, group, user);

		Element firstpageElement = doc.createElement("groupFirstpageModule");

		doc.getFirstChild().appendChild(firstpageElement);

		GroupFirstpage groupFirstpage = this.groupFirstpageDAO.get(group);

		if(groupFirstpage != null){

			firstpageElement.appendChild(groupFirstpage.toXML(doc));

		}else{

			XMLUtils.appendNewElement(doc, firstpageElement, "noFirstpageText", this.noFirstpageText);

		}

		return new SimpleForegroundModuleResponse(doc, getGroupBreadcrumb(group));

	}

	@GroupMethod
	public ForegroundModuleResponse update(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		ValidationException validationException = null;

		MultipartRequest requestWrapper = null;

		GroupFirstpage firstpage = this.groupFirstpageDAO.get(group);

		if(req.getMethod().equalsIgnoreCase("POST")){

			try{

				requestWrapper = new MultipartRequest(this.ramThreshold * BinarySizes.MegaByte, this.diskThreshold * BinarySizes.MegaByte, req);

				firstpage = FIRSTPAGE_POPULATOR.populate((firstpage != null ? firstpage : new GroupFirstpage()),requestWrapper);

				firstpage.setGroupID(group.getGroupID());

				this.groupFirstpageDAO.update(firstpage);

				this.redirectToDefaultMethod(req, res, group);

				return null;

			}catch(ValidationException e){

				validationException = e;

			}catch (FileUploadException e) {

				log.info("Error uploading image in newsletter caused by read time out, uploaded by user " + user);

			}

		}

		Document doc = this.createDocument(req, uriParser, group, user);

		Element updateFirstpageElement = doc.createElement("updatefirstpage");

		doc.getFirstChild().appendChild(updateFirstpageElement);

		if(firstpage != null){

			updateFirstpageElement.appendChild(firstpage.toXML(doc));

		}

		if(validationException != null){
			updateFirstpageElement.appendChild(RequestUtils.getRequestParameters(requestWrapper, doc));
			updateFirstpageElement.appendChild(validationException.toXML(doc));
		}

		return new SimpleForegroundModuleResponse(doc, getGroupBreadcrumb(group));

	}

	@GroupMethod
	public ForegroundModuleResponse delete(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		GroupFirstpage firstpage = null;

		if (uriParser.size() != 3 || (firstpage = this.groupFirstpageDAO.get(group)) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);

		} else {

			this.groupFirstpageDAO.delete(firstpage);

			this.redirectToDefaultMethod(req, res, group);

			return null;

		}

	}

	@GroupMethod
	public ForegroundModuleResponse getImage(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		GroupFirstpage firstpage = null;

		// check that the requested gallery exist
		if (uriParser.size() != 3 || (firstpage = this.groupFirstpageDAO.get(group)) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);

		} else {

			try {

				writePicture(firstpage.getImage(), firstpage.getTitle() + ".jpg", res);

			} catch (IOException e) {

				log.info("Caught exception " + e + " while sending picture in groupfirstpage for group " + group + " requested by user " + user);

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

	public List<? extends Event> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp) throws Exception {

		List<IdentifiedEvent> events = this.groupFirstpageDAO.getEvents(group, startStamp);

		if(events != null){

			for(IdentifiedEvent event : events){
				event.setTitle(this.newFirstpageText + StringUtils.substring(event.getTitle(), 50, "..."));
				event.setDescription(event.getTitle());
				event.setFullAlias(this.getFullAlias(group));
			}
		}

		return events;

	}

	@Override
	public ModuleType getModuleType() {
		return ModuleType.Hidden;
	}

	@Override
	protected boolean moduleEnabled(CommunityGroup group) throws SQLException {
		return true;
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, CommunityGroup group, CommunityUser user){

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		document.appendChild(group.toXML(doc));
		if(this.cssPath != null) {
			XMLUtils.appendNewElement(doc, document, "cssPath", this.cssPath);
		}
		document.appendChild(XMLUtils.createElement("isAdmin", String.valueOf(AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER)), doc));
		document.appendChild(XMLUtils.createElement("isSysAdmin", String.valueOf(user.isAdmin()), doc));
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

}
