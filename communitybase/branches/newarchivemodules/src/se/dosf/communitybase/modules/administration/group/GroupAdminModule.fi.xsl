<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="GroupAdminModuleTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.administration.by" select="'av'" />
	
	<xsl:variable name="user.update.header" select="'Muokkaa käyttäjää'" />
	<xsl:variable name="user.update.authorize" select="'Salli pääsy'" />
	<xsl:variable name="user.update.parent" select="'Vanhemmat'" />
	<xsl:variable name="user.update.educationalist" select="'Pedagogi'" />
	<xsl:variable name="user.update.admin" select="'Ylläpitäjä'" />
	<xsl:variable name="user.update.comment.member" select="'Lapsen / Lasten etu- ja sukunimi'" />
	<xsl:variable name="user.update.comment.admin" select="'Kommentoi'" />
	<xsl:variable name="user.update.email" select="'Sähköposti'" />
	<xsl:variable name="user.update.phonehome" select="'Kotipuhelin'" />
	<xsl:variable name="user.update.mobilephone" select="'Matkapuhelin'" />
	<xsl:variable name="user.update.phonework" select="'Työpuhelin'" />
	<xsl:variable name="user.update.submit" select="'Tallenna muutokset'" />
	
	<xsl:variable name="users.move.header" select="'Move users'" />
	<xsl:variable name="users.move.text.part1" select="'You have choosed to move'" />
	<xsl:variable name="users.move.text.part2" select="'users.'" />
	<xsl:variable name="users.move.description" select="'Click on the group which users should be moved to.'" />
	<xsl:variable name="users.copy.header" select="'Copy users'" />
	<xsl:variable name="users.copy.text.part1" select="'You have choosen to copy'" />
	<xsl:variable name="users.copy.text.part2" select="'users.'" />
	<xsl:variable name="users.copy.description" select="'Click on the group which users should be copied to.'" />
	<xsl:variable name="schools.tree.title" select="'Preschools'" />
	<xsl:variable name="schools.tree.expandAll" select="'Expand all'" />
	<xsl:variable name="schools.tree.collapseAll" select="'Collapse all'" />
	
	<xsl:variable name="groupInfoElement.users.header" select="'Käyttäjät'" />
	<xsl:variable name="groupInfoElement.users.nousers" select="'Tässä ryhmässä ei ole käyttäjiä'" />
	<xsl:variable name="groupInfoElement.users.moveusers" select="'Move marked users'" />
	<xsl:variable name="groupInfoElement.users.copyusers" select="'Copy marked users'" />
	<xsl:variable name="groupInfoElement.invitedusers.header" select="'Kutsu käyttäjiä'" />
	<xsl:variable name="groupInfoElement.invitedusers.noinvited" select="'Tässä ryhmässä ei ole kutsuttuja käyttäjiä'" />
	<xsl:variable name="groupInfoElement.adduser.header" select="'Lisää käyttäjä'" />
	<xsl:variable name="groupInfoElement.adduser.email" select="'Sähköposti'" />
	<xsl:variable name="groupInfoElement.adduser.role" select="'Rooli'" />
	<xsl:variable name="groupInfoElement.adduser.admin.comment" select="'Kommentoi'" />
	<xsl:variable name="groupInfoElement.adduser.member.comment" select="'Lapsen / lasten etu- ja sukunimi'" />
	<xsl:variable name="groupInfoElement.adduser.parent" select="'Vanhemmat'" />
	<xsl:variable name="groupInfoElement.adduser.educationalist" select="'Pedagogi'" />
	<xsl:variable name="groupInfoElement.adduser.admin" select="'Ylläpitäjä'" />
	<xsl:variable name="groupInfoElement.adduser.submit" select="'Lisää'" />
	<xsl:variable name="groupInfoElement.modules.header" select="'Moduuli'" />
	
	<xsl:variable name="user.showOrChangeUser.title" select="'Näytä/muokkaa käyttäjää'" />
	<xsl:variable name="user.delete.title" select="'Poista käyttäjä'" />
	<xsl:variable name="user.member" select="'Vanhemmat'" />
	<xsl:variable name="user.educationalist" select="'Pedagogi'" />
	<xsl:variable name="user.admin" select="'Ylläpitäjä'" />
	<xsl:variable name="user.unknown" select="'Tuntematon'" />
	
	<xsl:variable name="invitation.expires" select="'Voimassaolo'" />
	<xsl:variable name="invitation.member" select="'Vanhemmat'" />
	<xsl:variable name="invitation.educationalist" select="'Pedagogi'" />
	<xsl:variable name="invitation.admin" select="'Ylläpitäjä'" />
	<xsl:variable name="invitation.unknown" select="'Tuntematon'" />
	<xsl:variable name="invitation.sendnew.title" select="'Lähetä kutsu uudestaan'" />
	<xsl:variable name="invitation.delete.title" select="'Poista kutsu'" />

	<xsl:variable name="modules.disable.title" select="'Ota moduuli pois käytöstä'" />
	<xsl:variable name="modules.enable.title" select="'Ota moduuli käyttöön'" />
	
	<xsl:variable name="validationError.RequiredField" select="'Kenttä on pakollinen'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Kentän sisältö ei vastaa vaadittua muotoilua'" />
	<xsl:variable name="validationError.Other" select="'Hakupolku ei ole kelvollinen, muokkaa kenttää'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Tuntematon virhe kentässä'" />
	<xsl:variable name="validationError.field.email" select="'sähköposti'" />
	<xsl:variable name="validationError.field.access" select="'käyttöoikeus'" />
	<xsl:variable name="validationError.messageKey.DeleteFailedUserNotFound" select="'Käyttäjää jota yritit poistaa, ei löydy'" />
	<xsl:variable name="validationError.messageKey.UpdateFailedUserNotFound" select="'Käyttäjää jota yritit muokata, ei lyödy'" />
	<xsl:variable name="validationError.messageKey.NoUserChoosen" select="'You need to choose at least one user to move/copy'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Tuntematon virhe'" />

</xsl:stylesheet>
