<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="GroupAdminModuleTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.administration.by" select="'av'" />
	
	<xsl:variable name="user.update.header" select="'Uppdatera anv�ndaren'" />
	<xsl:variable name="user.update.authorize" select="'Beh�righet'" />
	<xsl:variable name="user.update.parent" select="'F�r�lder'" />
	<xsl:variable name="user.update.educationalist" select="'Pedagog'" />
	<xsl:variable name="user.update.admin" select="'Administrat�r'" />
	<xsl:variable name="user.update.comment.member" select="'Barnet/Barnens f�r- och efternamn'" />
	<xsl:variable name="user.update.comment.admin" select="'Kommentar'" />
	<xsl:variable name="user.update.email" select="'E-post'" />
	<xsl:variable name="user.update.phonehome" select="'Telefon hem'" />
	<xsl:variable name="user.update.mobilephone" select="'Telefon mobil'" />
	<xsl:variable name="user.update.phonework" select="'Telefon arbete'" />
	<xsl:variable name="user.update.submit" select="'Spara �ndringar'" />
	
	<xsl:variable name="users.move.header" select="'Flytta anv�ndare'" />
	<xsl:variable name="users.move.text.part1" select="'Du har valt att flytta'" />
	<xsl:variable name="users.move.text.part2" select="'st anv�ndare.'" />
	<xsl:variable name="users.move.description" select="'Klicka p� den grupp som anv�ndaren/anv�ndarna skall flyttas till.'" />
	<xsl:variable name="schools.tree.title" select="'F�rskolor'" />
	<xsl:variable name="schools.tree.expandAll" select="'F�ll ut alla'" />
	<xsl:variable name="schools.tree.collapseAll" select="'St�ng alla'" />
	
	<xsl:variable name="groupInfoElement.users.header" select="'Anv�ndare'" />
	<xsl:variable name="groupInfoElement.users.nousers" select="'Det finns inga anv�ndare i den h�r gruppen'" />
	<xsl:variable name="groupInfoElement.users.moveusers" select="'Flytta markerade anv�ndare'" />
	<xsl:variable name="groupInfoElement.invitedusers.header" select="'Inbjudna anv�ndare'" />
	<xsl:variable name="groupInfoElement.invitedusers.noinvited" select="'Det finns inga inbjudna anv�ndare i den h�r gruppen'" />
	<xsl:variable name="groupInfoElement.adduser.header" select="'L�gg till anv�ndare'" />
	<xsl:variable name="groupInfoElement.adduser.email" select="'E-post'" />
	<xsl:variable name="groupInfoElement.adduser.role" select="'Roll'" />
	<xsl:variable name="groupInfoElement.adduser.admin.comment" select="'Kommentar'" />
	<xsl:variable name="groupInfoElement.adduser.member.comment" select="'Barnet/Barnens f�r- och efternamn'" />
	<xsl:variable name="groupInfoElement.adduser.parent" select="'F�r�lder'" />
	<xsl:variable name="groupInfoElement.adduser.user" select="'Anv�ndare'" />
	<xsl:variable name="groupInfoElement.adduser.educationalist" select="'Pedagog'" />
	<xsl:variable name="groupInfoElement.adduser.admin" select="'Administrat�r'" />
	<xsl:variable name="groupInfoElement.adduser.submit" select="'L�gg till'" />
	<xsl:variable name="groupInfoElement.modules.header" select="'Moduler'" />
	
	<xsl:variable name="user.showOrChangeUser.title" select="'Visa/�ndra anv�ndaren'" />
	<xsl:variable name="user.delete.title" select="'Ta bort anv�ndaren'" />
	<xsl:variable name="user.member" select="'F�r�lder'" />
	<xsl:variable name="user.educationalist" select="'Pedagog'" />
	<xsl:variable name="user.admin" select="'Administrat�r'" />
	<xsl:variable name="user.unknown" select="'Ok�nd beh�righet'" />
	
	<xsl:variable name="invitation.expires" select="'Giltig t.o.m'" />
	<xsl:variable name="invitation.member" select="'F�r�lder'" />
	<xsl:variable name="invitation.educationalist" select="'Pedagog'" />
	<xsl:variable name="invitation.admin" select="'Administrat�r'" />
	<xsl:variable name="invitation.unknown" select="'Ok�nd beh�righet'" />
	<xsl:variable name="invitation.sendnew.title" select="'Skicka inbjudan p� nytt till'" />
	<xsl:variable name="invitation.delete.title" select="'Ta bort inbjudan'" />

	<xsl:variable name="modules.disable.title" select="'Avaktivera modulen'" />
	<xsl:variable name="modules.enable.title" select="'Aktivera modulen'" />
	
	<xsl:variable name="validationError.RequiredField" select="'Du m�ste fylla i f�ltet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt v�rde p� f�ltet'" />
	<xsl:variable name="validationError.Other" select="'S�kv�gen �r inte giltig, �ndra f�ltet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Ok�nt fel p� f�ltet'" />
	<xsl:variable name="validationError.field.email" select="'e-post'" />
	<xsl:variable name="validationError.field.access" select="'beh�righet'" />
	<xsl:variable name="validationError.messageKey.DeleteFailedUserNotFound" select="'Anv�ndaren du f�rs�ker ta bort hittades inte'" />
	<xsl:variable name="validationError.messageKey.UpdateFailedUserNotFound" select="'Anv�ndaren du f�rs�ker uppdatera hittades inte'" />
	<xsl:variable name="validationError.messageKey.NoUserChoosen" select="'Du har inte valt n�gon anv�ndare att flytta'" />
	
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett ok�nt fel har uppst�tt'" />				

</xsl:stylesheet>