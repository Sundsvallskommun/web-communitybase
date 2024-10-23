package se.dosf.communitybase.modules.messagedispatcher.beans;

import java.io.Serializable;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.School;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLGenerator;

@XMLElement
public class DispatchMessage implements Elementable, Serializable {

	private static final long serialVersionUID = 1188589847928029492L;

	@WebPopulate(paramName = "send-sms")
	@XMLElement
	private Boolean sendSMS;

	@WebPopulate(paramName = "send-email")
	@XMLElement
	private Boolean sendEmail;

	@WebPopulate(paramName = "sms-text", maxLength = 255)
	@XMLElement
	private String SMSText;

	@WebPopulate(paramName = "email-text", maxLength = 65535)
	@XMLElement
	private String emailText;

	@WebPopulate(paramName = "email-subject", maxLength = 255)
	@XMLElement
	private String emailSubject;

	@WebPopulate(paramName = "role-publisher")
	@XMLElement
	private Boolean sendToPublishers;

	@WebPopulate(paramName = "role-member")
	@XMLElement
	private Boolean sendToMembers;

	private List<CommunityUser> recipients;

	@XMLElement(name = "schools")
	private List<School> selectedSchoolsAndGroups;

	public Boolean getSendSMS() {

		return sendSMS;
	}

	public void setSendSMS(Boolean sendSMS) {

		this.sendSMS = sendSMS;
	}

	public Boolean getSendEmail() {

		return sendEmail;
	}

	public void setSendEmail(Boolean sendEmail) {

		this.sendEmail = sendEmail;
	}

	public String getSMSText() {

		return SMSText;
	}

	public void setSMSText(String textSMS) {

		this.SMSText = textSMS;
	}

	public String getEmailText() {

		return emailText;
	}

	public void setEmailText(String textEmail) {

		this.emailText = textEmail;
	}

	public Boolean getSendToPublishers() {

		return sendToPublishers;
	}

	public void setSendToPublishers(Boolean sendToPublishers) {

		this.sendToPublishers = sendToPublishers;
	}

	public Boolean getSendToMembers() {

		return sendToMembers;
	}

	public void setSendToMembers(Boolean sendToMembers) {

		this.sendToMembers = sendToMembers;
	}

	public List<CommunityUser> getRecipients() {

		return recipients;
	}

	public void setRecipients(List<CommunityUser> recipients) {

		this.recipients = recipients;
	}

	public String getEmailSubject() {

		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {

		this.emailSubject = emailSubject;
	}

	public List<School> getSelectedSchoolsAndGroups() {

		return selectedSchoolsAndGroups;
	}

	public void setSelectedSchoolsAndGroups(List<School> selectedSchoolsAndGroups) {

		this.selectedSchoolsAndGroups = selectedSchoolsAndGroups;
	}

	@Override
	public Element toXML(Document doc) {

		return XMLGenerator.toXML(this, doc);
	}

}
