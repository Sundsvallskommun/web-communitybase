package se.dosf.communitybase.modules.resumesender;

import java.io.StringWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Event;
import se.dosf.communitybase.daos.CommunityGroupDAO;
import se.dosf.communitybase.daos.CommunityModuleDAO;
import se.dosf.communitybase.daos.CommunityUserDAO;
import se.dosf.communitybase.modules.CommunityModule;
import se.dosf.communitybase.modules.firstpage.FirstpageModule;
import se.dosf.communitybase.utils.ModuleUtils;
import se.unlogic.emailutils.framework.EmailHandler;
import se.unlogic.emailutils.framework.InvalidEmailAddressException;
import se.unlogic.emailutils.framework.NoEmailSendersFoundException;
import se.unlogic.emailutils.framework.SimpleEmail;
import se.unlogic.emailutils.framework.UnableToProcessEmailException;
import se.unlogic.emailutils.validation.StringEmailValidator;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.SettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.hierarchy.foregroundmodules.ModuleSetting;
import se.unlogic.standardutils.datatypes.SimpleEntry;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.time.MillisecondTimeUnits;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.timer.RunnableTimerTask;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;


public class EmailResumeSender extends AnnotatedForegroundModule implements Runnable{

	private static final ArrayList<SettingDescriptor> SETTINGDESCRIPTORS = new ArrayList<SettingDescriptor>();

	static {
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("emailSubject", "Subject", "The text displayed in the subject of sent e-mail resumes", true, "Resume from CommunityBase", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("senderName", "Sender name", "The name displayed in the sender field of sent e-mail resumes", true, "CommunityBase", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("senderAddress", "Sender address", "The sender address", true, "not.set@not.set.com", new StringEmailValidator()));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("baseURL", "Base URL", "The full URL to this site", true, "http://not.set.com", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("municipalityURL", "Municipality URL", "The full URL to the municipality site", true, "http://not.set.com", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("municipalityName", "Municipality name", "The name of the municipality which uses communitybase", true, "Not set", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("emailContentType", "Content type", "The content type of generated e-mails", true, "text/html", null));
	}

	private CommunityModuleDAO moduleDAO;
	private CommunityUserDAO userDAO;

	@ModuleSetting
	protected String emailSubject = "Resume from CommunityBase";

	@ModuleSetting
	protected String senderName = "CommunityBase";

	@ModuleSetting
	protected String senderAddress = "not.set@not.set.com";

	@ModuleSetting
	protected String baseURL = "http://not.set.com";

	@ModuleSetting
	protected String municipalityURL = "http://not.set.com";

	@ModuleSetting
	protected String municipalityName = "Not set";

	@ModuleSetting
	protected String emailContentType = "text/html";

	private boolean running = false;
	private boolean abort = false;

	private Integer testTime = null;

	private final Timer timer = new Timer(true);
	private TimerTask timerTask;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor,	SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		super.init(moduleDescriptor, sectionInterface, dataSource);

		this.timerTask = new RunnableTimerTask(this);

		timer.scheduleAtFixedRate(timerTask, (61 - TimeUtils.getMinutes(System.currentTimeMillis())) * MillisecondTimeUnits.MINUTE, MillisecondTimeUnits.HOUR);
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {
		super.update(moduleDescriptor, dataSource);
	}

	@Override
	protected void createDAOs(DataSource dataSource) {

		this.userDAO = new CommunityUserDAO(dataSource);
		this.userDAO.setGroupDao(new CommunityGroupDAO(dataSource));
		this.moduleDAO = new CommunityModuleDAO(dataSource);
	}

	@Override
	public void unload() throws Exception {

		if(this.running){
			this.abort = true;
		}

		this.timerTask.cancel();
		this.timer.cancel();

		super.unload();
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("<div class=\"contentitem\">");
		stringBuilder.append("<h1>" + this.moduleDescriptor.getName() + "</h1>");

		if (!running) {
			stringBuilder.append("<p>Current status: waiting</p>");
		} else {
			stringBuilder.append("<p>Current status: <b>running!</b></p>");
		}

		stringBuilder.append("</div>");

		return new SimpleForegroundModuleResponse(stringBuilder.toString(), moduleDescriptor.getName(), this.getDefaultBreadcrumb());
	}

	@WebPublic
	public ForegroundModuleResponse abort(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		this.abort = true;

		redirectToDefaultMethod(req, res);
		return null;
	}

	@WebPublic
	public ForegroundModuleResponse trigger(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		Integer triggerTime = null;

		if(!running && uriParser.size() == 3 && (triggerTime = NumberUtils.toInt(uriParser.get(2))) != null && triggerTime >= 0 && triggerTime <= 23){

			log.info("Manually triggering sending of e-post resume's at " + triggerTime);

			this.testTime = triggerTime;

			this.run();
		}

		redirectToDefaultMethod(req, res);
		return null;
	}

	public void run() {

		if(running){

			log.warn("Concurrent execution detected!");

		}else{
			if(this.sectionInterface.getSystemInterface().getSystemStatus() == SystemStatus.Started){

				try {
					this.running = true;
					this.abort = false;

					int currentHour = TimeUtils.getHour(System.currentTimeMillis());

					if(testTime != null){
						currentHour = testTime;
					}

					log.info("Generating e-mail resume for users that have selected to get e-mail resume at " + currentHour);

					Transformer transformer = null;

					try {
						transformer = this.sectionInterface.getModuleXSLTCache().getModuleTranformer(this.moduleDescriptor);
					} catch (TransformerConfigurationException e1) {

						log.error("Unable to get transformer from ModuleXSLTCache, aborting", e1);
						return;
					}

					if(transformer == null){

						log.error("Found no cached stylesheet in ModuleXSLTCache, aborting");
						return;
					}

					EmailHandler emailHandler = this.sectionInterface.getSystemInterface().getEmailHandler();

					if(!emailHandler.hasSenders()){

						log.error("No e-mail senders registered in email handler, aborting");
						return;
					}

					ArrayList<CommunityUser> users = this.userDAO.getUsers(currentHour);

					if(users != null){

						FirstpageModule firstpageModule = ModuleUtils.getCachedModule(sectionInterface, FirstpageModule.class);

						if(firstpageModule == null){
							log.error("Unable to find FirstpageModule, aborting");
							return;
						}

						Map<ForegroundModuleDescriptor, CommunityModule> moduleMap = firstpageModule.getPublicModuleMap();

						if(moduleMap.isEmpty()){

							log.error("No started community modules found, aborting");
							return;
						}

						for(CommunityUser user : users){

							if(abort){
								log.info("Abort flag set, stopping generating of email resumes");
							}

							if(user.getGroups() == null || user.getGroups().isEmpty()){
								log.info("User " + user + " is not member of any groups, skipping");
								continue;
							}

							HashMap<CommunityGroup, ArrayList<SimpleEntry<ForegroundModuleDescriptor, List<? extends Event>>>> userEventMap = new HashMap<CommunityGroup, ArrayList<SimpleEntry<ForegroundModuleDescriptor,List<? extends Event>>>>();

							Timestamp lastResume = null;

							if(user.getLastLogin() == null){

								log.info("User " + user + " has never logged in, skipping");
								continue;

							}else if(user.getLastResume() == null){

								//User has never received an e-mail resume use last login timestamp
								lastResume = user.getLastLogin();

							}else if(user.getLastLogin().getTime() > user.getLastResume().getTime()){

								//User has logged in since the last resume was sent, use last login timestamp
								lastResume = user.getLastLogin();
							}else{
								lastResume = user.getLastResume();
							}

							for(CommunityGroup group : user.getCommunityGroups()){

								ArrayList<Integer> groupModuleIDs = this.moduleDAO.getEnabledModules(group);

								if(groupModuleIDs == null){
									continue;
								}

								ArrayList<SimpleEntry<ForegroundModuleDescriptor, List<? extends Event>>> groupEvents = new ArrayList<SimpleEntry<ForegroundModuleDescriptor,List<? extends Event>>>();

								for(Integer moduleID : groupModuleIDs){

									Entry<ForegroundModuleDescriptor, CommunityModule> currentModuleEntry = null;

									for (Entry<ForegroundModuleDescriptor, CommunityModule> moduleEntry : moduleMap.entrySet()) {

										if (moduleEntry.getKey().getModuleID().equals(moduleID)) {

											currentModuleEntry = moduleEntry;
											break;
										}
									}

									if(currentModuleEntry != null){

										try {
											List<? extends Event> moduleEvents = currentModuleEntry.getValue().getGroupResume(group, user, lastResume);

											if(moduleEvents != null && !moduleEvents.isEmpty()){
												groupEvents.add(new SimpleEntry<ForegroundModuleDescriptor, List<? extends Event>>(currentModuleEntry.getKey(), moduleEvents));
											}

										} catch (Exception e) {

											log.error("Unable to get resume for from module " + currentModuleEntry.getKey() + " for user " + user + " and group " + group,e);
										}
									}
								}

								if(!groupEvents.isEmpty()){
									userEventMap.put(group, groupEvents);
								}
							}

							if(userEventMap.isEmpty()){
								continue;
							}

							//Create and send resume mail
							Document doc = XMLUtils.createDomDocument();

							Element documentElement = doc.createElement("document");
							doc.appendChild(documentElement);

							for(Entry<CommunityGroup, ArrayList<SimpleEntry<ForegroundModuleDescriptor, List<? extends Event>>>> groupEntry : userEventMap.entrySet()){

								Element groupElement = groupEntry.getKey().toXML(doc);
								documentElement.appendChild(groupElement);

								for(SimpleEntry<ForegroundModuleDescriptor, List<? extends Event>> moduleEntry : groupEntry.getValue()){

									Element moduleElement = moduleEntry.getKey().toXML(doc);
									groupElement.appendChild(moduleElement);

									for(Event event : moduleEntry.getValue()){

										moduleElement.appendChild(event.toXML(doc));
									}
								}
							}

							documentElement.appendChild(user.toXML(doc));
							documentElement.appendChild(XMLUtils.createCDATAElement("baseURL", this.baseURL, doc));
							documentElement.appendChild(XMLUtils.createCDATAElement("municipalityName", this.municipalityName, doc));
							documentElement.appendChild(XMLUtils.createCDATAElement("municipalityURL", this.municipalityURL, doc));


							StringWriter messageWriter = new StringWriter();

							try {

								transformer.transform(new DOMSource(doc), new StreamResult(messageWriter));

							} catch (TransformerException e) {

								log.error("Unable to transform e-mail resume for user " + user,e);
								continue;
							}

							SimpleEmail email = new SimpleEmail();

							email.setSubject(this.emailSubject);
							email.setMessage(messageWriter.toString());
							email.setSenderAddress(this.senderAddress);
							email.setSenderName(this.senderName);
							email.addRecipient(user.getEmail());
							email.setMessageContentType(emailContentType);

							log.info("Sending e-mail resume to user " + user);

							try {
								emailHandler.send(email);

								this.userDAO.setLastResume(user.getUserID(),new Timestamp(System.currentTimeMillis()));

							} catch (NoEmailSendersFoundException e) {

								log.error("Unable to send e-mail resume to user " + user,e);

							} catch (UnableToProcessEmailException e) {

								log.error("Unable to send e-mail resume to user " + user,e);
							}
						}

					}else{
						log.info("No users with resume at " + currentHour + " found");
					}

				} catch (SQLException e) {

					log.error("Error generating e-mail resumes",e);

				} catch (InvalidEmailAddressException e) {

					log.error("Error generating e-mail resumes",e);

				}finally{
					this.testTime = null;
					this.running = false;
				}

			}else{
				log.info("System status is " + this.sectionInterface.getSystemInterface().getSystemStatus() + ", aborting");
			}
		}
	}

	@Override
	public List<? extends SettingDescriptor> getSettings() {

		List<? extends SettingDescriptor> superSettings = super.getSettings();

		if (superSettings != null) {
			ArrayList<SettingDescriptor> combinedSettings = new ArrayList<SettingDescriptor>();

			combinedSettings.addAll(superSettings);
			combinedSettings.addAll(SETTINGDESCRIPTORS);

			return combinedSettings;
		} else {
			return SETTINGDESCRIPTORS;
		}
	}
}
