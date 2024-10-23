package se.dosf.communitybase.populators;

import java.sql.ResultSet;
import java.sql.SQLException;

import se.dosf.communitybase.beans.Invitation;
import se.unlogic.standardutils.dao.BeanResultSetPopulator;
import se.unlogic.standardutils.i18n.Language;

public class InvitationPopulator implements BeanResultSetPopulator<Invitation> {

	public Invitation populate(ResultSet rs) throws SQLException {

		Invitation invitation = new Invitation();

		invitation.setInvitationID(rs.getInt("invitationID"));
		invitation.setEmail(rs.getString("email"));
		invitation.setLanguage(Language.getLanguage(rs.getString("languageCode")));
		invitation.setExpires(rs.getTimestamp("expires"));
		invitation.setUpdated(rs.getTimestamp("updated"));
		invitation.setLastSent(rs.getTimestamp("lastSent"));
		invitation.setLinkID(rs.getString("linkID"));
		invitation.setAdmin(rs.getBoolean("admin"));
		invitation.setResend(rs.getBoolean("resend"));
		invitation.setSenderEmail(rs.getString("senderEmail"));

		return invitation;
	}

}
