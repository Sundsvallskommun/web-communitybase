<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="GroupAdminModuleTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.administration.by" select="'av'" />
	
	<xsl:variable name="user.update.header" select="'Uppdatera användaren'" />
	<xsl:variable name="user.update.authorize" select="'Behörighet'" />
	<xsl:variable name="user.update.parent" select="'Förälder'" />
	<xsl:variable name="user.update.educationalist" select="'Pedagog'" />
	<xsl:variable name="user.update.admin" select="'Administratör'" />
	<xsl:variable name="user.update.comment.member" select="'Barnet/Barnens för- och efternamn'" />
	<xsl:variable name="user.update.comment.admin" select="'Kommentar'" />
	<xsl:variable name="user.update.email" select="'E-post'" />
	<xsl:variable name="user.update.phonehome" select="'Telefon hem'" />
	<xsl:variable name="user.update.mobilephone" select="'Telefon mobil'" />
	<xsl:variable name="user.update.phonework" select="'Telefon arbete'" />
	<xsl:variable name="user.update.submit" select="'Spara ändringar'" />
	
	<xsl:variable name="users.move.header" select="'Flytta användare'" />
	<xsl:variable name="users.move.text.part1" select="'Du har valt att flytta'" />
	<xsl:variable name="users.move.text.part2" select="'st användare.'" />
	<xsl:variable name="users.move.description" select="'Klicka på den grupp som användaren/användarna skall flyttas till.'" />
	<xsl:variable name="schools.tree.title" select="'Förskolor'" />
	<xsl:variable name="schools.tree.expandAll" select="'Fäll ut alla'" />
	<xsl:variable name="schools.tree.collapseAll" select="'Stäng alla'" />
	
	<xsl:variable name="groupInfoElement.users.header" select="'Användare'" />
	<xsl:variable name="groupInfoElement.users.nousers" select="'Det finns inga användare i den här gruppen'" />
	<xsl:variable name="groupInfoElement.users.moveusers" select="'Flytta markerade användare'" />
	<xsl:variable name="groupInfoElement.invitedusers.header" select="'Inbjudna användare'" />
	<xsl:variable name="groupInfoElement.invitedusers.noinvited" select="'Det finns inga inbjudna användare i den här gruppen'" />
	<xsl:variable name="groupInfoElement.adduser.header" select="'Lägg till användare'" />
	<xsl:variable name="groupInfoElement.adduser.email" select="'E-post'" />
	<xsl:variable name="groupInfoElement.adduser.role" select="'Roll'" />
	<xsl:variable name="groupInfoElement.adduser.admin.comment" select="'Kommentar'" />
	<xsl:variable name="groupInfoElement.adduser.member.comment" select="'Barnet/Barnens för- och efternamn'" />
	<xsl:variable name="groupInfoElement.adduser.parent" select="'Förälder'" />
	<xsl:variable name="groupInfoElement.adduser.user" select="'Användare'" />
	<xsl:variable name="groupInfoElement.adduser.educationalist" select="'Pedagog'" />
	<xsl:variable name="groupInfoElement.adduser.admin" select="'Administratör'" />
	<xsl:variable name="groupInfoElement.adduser.submit" select="'Lägg till'" />
	<xsl:variable name="groupInfoElement.modules.header" select="'Moduler'" />
	
	<xsl:variable name="user.showOrChangeUser.title" select="'Visa/ändra användaren'" />
	<xsl:variable name="user.delete.title" select="'Ta bort användaren'" />
	<xsl:variable name="user.member" select="'Förälder'" />
	<xsl:variable name="user.educationalist" select="'Pedagog'" />
	<xsl:variable name="user.admin" select="'Administratör'" />
	<xsl:variable name="user.unknown" select="'Okänd behörighet'" />
	
	<xsl:variable name="invitation.expires" select="'Giltig t.o.m'" />
	<xsl:variable name="invitation.member" select="'Förälder'" />
	<xsl:variable name="invitation.educationalist" select="'Pedagog'" />
	<xsl:variable name="invitation.admin" select="'Administratör'" />
	<xsl:variable name="invitation.unknown" select="'Okänd behörighet'" />
	<xsl:variable name="invitation.sendnew.title" select="'Skicka inbjudan på nytt till'" />
	<xsl:variable name="invitation.delete.title" select="'Ta bort inbjudan'" />

	<xsl:variable name="modules.disable.title" select="'Avaktivera modulen'" />
	<xsl:variable name="modules.enable.title" select="'Aktivera modulen'" />
	
	<xsl:variable name="validationError.RequiredField" select="'Du måste fylla i fältet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt värde på fältet'" />
	<xsl:variable name="validationError.Other" select="'Sökvägen är inte giltig, ändra fältet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Okänt fel på fältet'" />
	<xsl:variable name="validationError.field.email" select="'e-post'" />
	<xsl:variable name="validationError.field.access" select="'behörighet'" />
	<xsl:variable name="validationError.messageKey.DeleteFailedUserNotFound" select="'Användaren du försöker ta bort hittades inte'" />
	<xsl:variable name="validationError.messageKey.UpdateFailedUserNotFound" select="'Användaren du försöker uppdatera hittades inte'" />
	<xsl:variable name="validationError.messageKey.NoUserChoosen" select="'Du har inte valt någon användare att flytta'" />
	
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett okänt fel har uppstått'" />				

</xsl:stylesheet>