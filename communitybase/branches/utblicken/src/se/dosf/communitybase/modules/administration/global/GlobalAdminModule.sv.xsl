<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="GlobalAdminModuleTemplates.xsl" />
	
	<!-- BreadCrumb variables -->
	
	<xsl:variable name="renameScoolBreadCrumbTest">Byt namn på skolan: </xsl:variable>
	<xsl:variable name="updateGroupBreadCrumbTest">Ändra gruppen: </xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="renameSchool.header" select="'Byt namn på förskolan'" />
	<xsl:variable name="renameSchool.name" select="'Namn'" />
	<xsl:variable name="renameSchool.submit" select="'Spara ändringar'" />
	
	<xsl:variable name="group.rename.header" select="'Byt namn på gruppen'" />
	<xsl:variable name="group.rename.name" select="'Namn'" />
	<xsl:variable name="group.rename.email" select="'E-post (valfritt)'" />
	<xsl:variable name="group.rename.submit" select="'Spara ändringar'" />
	
	<xsl:variable name="systemInfoElement.header" select="'Administrera förskolor'" />
	<xsl:variable name="systemInfoElement.school.header" select="'Förskolor'" />
	<xsl:variable name="systemInfoElement.school.noschool" select="'Det finns inga förskolor'" />
	<xsl:variable name="systemInfoElement.school.addschool" select="'Lägg till förskola'" />
	<xsl:variable name="systemInfoElement.school.addschool.name" select="'Namn'" />
	<xsl:variable name="systemInfoElement.admins.header" select="'Administratörer'" />
	<xsl:variable name="systemInfoElement.admins.noadmins" select="'Det finns inga administratörer'" />
	<xsl:variable name="systemInfoElement.invitedadmins.header" select="'Inbjudna administratörer'" />
	<xsl:variable name="systemInfoElement.invitedadmins.noadmins" select="'Det finns inga inbjudna administratörer'" />
	<xsl:variable name="systemInfoElement.addadmins.header" select="'Lägg till administratör'" />
	<xsl:variable name="systemInfoElement.addadmins.email" select="'E-post'" />
	<xsl:variable name="systemInfoElement.addadmins.submit" select="'Lägg till'" />
	
	<xsl:variable name="schools.tree.title" select="'Förskolor'" />
	<xsl:variable name="schools.tree.expandAll" select="'Fäll ut alla'" />
	<xsl:variable name="schools.tree.collapseAll" select="'Stäng alla'" />
	
	<xsl:variable name="school.tree.changename.title" select="'Byt namn på förskolan'" />
	<xsl:variable name="school.tree.delete.title" select="'Ta bort förskolan'" />
	
	<xsl:variable name="group.tree.changename.title" select="'Byt namn på gruppen'" />
	<xsl:variable name="group.tree.delete.title" select="'Ta bort gruppen'" />
	
	<xsl:variable name="user.admin.delete.title" select="'Ta bort användaren'" />
	
	<xsl:variable name="invitation.admin.expires" select="'Giltig t.o.m'" />
	<xsl:variable name="invitation.admin.sendnew.title" select="'Skicka inbjudan på nytt till'" />
	<xsl:variable name="invitation.admin.delete.title" select="'Ta bort inbjudan'" />
	
	<xsl:variable name="schoolInfoElement.header" select="'Administrera förskolan'" />
	<xsl:variable name="schoolInfoElement.groups.header" select="'Grupper'" />
	<xsl:variable name="schoolInfoElement.groups.nogroups" select="'Det finns inga grupper'" />
	<xsl:variable name="schoolInfoElement.addgroup.header" select="'Lägg till grupp'" />
	<xsl:variable name="schoolInfoElement.addgroup.name" select="'Namn'" />
	<xsl:variable name="schoolInfoElement.addgroup.submit" select="'Lägg till'" />
	<xsl:variable name="schoolInfoElement.resources.header" select="'Resurspersoner'" />
	<xsl:variable name="schoolInfoElement.resources.noresources" select="'Det finns inga resurspersoner'" />
	<xsl:variable name="schoolInfoElement.invitedresources.header" select="'Inbjudna resurspersoner'" />
	<xsl:variable name="schoolInfoElement.invitedresources.noinvited" select="'Det finns inga inbjudna resurspersoner'" />
	<xsl:variable name="schoolInfoElement.addresource.header" select="'Lägg till resursperson'" />	
	<xsl:variable name="schoolInfoElement.addresource.email" select="'E-post'" />
	<xsl:variable name="schoolInfoElement.addresource.submit" select="'Lägg till'" />
	
	<xsl:variable name="group.changename.title" select="'Byt namn på gruppen'" />
	<xsl:variable name="group.delete.title" select="'Ta bort gruppen'" />
	
	<xsl:variable name="user.schooladmin.delete.title" select="'Ta bort användaren'" />
	
	<xsl:variable name="invitation.schooladmin.expires" select="'Giltig t.o.m'" />
	<xsl:variable name="invitation.schooladmin.sendnew.title" select="'Skicka inbjudan på nytt till'" />
	<xsl:variable name="invitation.schooladmin.delete.title" select="'Ta bort inbjudan'" />
	
	<xsl:variable name="validationError.RequiredField" select="'Du måste fylla i fältet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt värde på fältet'" />
	<xsl:variable name="validationError.Other" select="'Sökvägen är inte giltig, ändra fältet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Okänt fel på fältet'" />
	<xsl:variable name="validationError.field.name" select="'namn'" />
	<xsl:variable name="validationError.field.email" select="'e-post'" />
	<xsl:variable name="validationError.messageKey.DeleteFailedUserNotFound" select="'Användaren du försöker ta bort hittades inte'" />
	<xsl:variable name="validationError.messageKey.RenameFailedGroupNotFound" select="'Gruppen du försöker byta namn på hittades inte'"/>
	<xsl:variable name="validationError.messageKey.DeleteFailedGroupNotFound" select="'Gruppen du försöker ta bort hittades inte'"/>
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett okänt fel har uppstått'" />			

</xsl:stylesheet>