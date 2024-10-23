<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="GlobalAdminModuleTemplates.xsl" />
	
	<!-- BreadCrumb variables -->
	
	<xsl:variable name="renameScoolBreadCrumbTest">Byt namn p� skolan: </xsl:variable>
	<xsl:variable name="updateGroupBreadCrumbTest">�ndra gruppen: </xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="renameSchool.header" select="'Byt namn p� f�rskolan'" />
	<xsl:variable name="renameSchool.name" select="'Namn'" />
	<xsl:variable name="renameSchool.submit" select="'Spara �ndringar'" />
	
	<xsl:variable name="group.rename.header" select="'Byt namn p� gruppen'" />
	<xsl:variable name="group.rename.name" select="'Namn'" />
	<xsl:variable name="group.rename.email" select="'E-post (valfritt)'" />
	<xsl:variable name="group.rename.submit" select="'Spara �ndringar'" />
	
	<xsl:variable name="systemInfoElement.header" select="'Administrera f�rskolor'" />
	<xsl:variable name="systemInfoElement.school.header" select="'F�rskolor'" />
	<xsl:variable name="systemInfoElement.school.noschool" select="'Det finns inga f�rskolor'" />
	<xsl:variable name="systemInfoElement.school.addschool" select="'L�gg till f�rskola'" />
	<xsl:variable name="systemInfoElement.school.addschool.name" select="'Namn'" />
	<xsl:variable name="systemInfoElement.admins.header" select="'Administrat�rer'" />
	<xsl:variable name="systemInfoElement.admins.noadmins" select="'Det finns inga administrat�rer'" />
	<xsl:variable name="systemInfoElement.invitedadmins.header" select="'Inbjudna administrat�rer'" />
	<xsl:variable name="systemInfoElement.invitedadmins.noadmins" select="'Det finns inga inbjudna administrat�rer'" />
	<xsl:variable name="systemInfoElement.addadmins.header" select="'L�gg till administrat�r'" />
	<xsl:variable name="systemInfoElement.addadmins.email" select="'E-post'" />
	<xsl:variable name="systemInfoElement.addadmins.submit" select="'L�gg till'" />
	
	<xsl:variable name="schools.tree.title" select="'F�rskolor'" />
	<xsl:variable name="schools.tree.expandAll" select="'F�ll ut alla'" />
	<xsl:variable name="schools.tree.collapseAll" select="'St�ng alla'" />
	
	<xsl:variable name="school.tree.changename.title" select="'Byt namn p� f�rskolan'" />
	<xsl:variable name="school.tree.delete.title" select="'Ta bort f�rskolan'" />
	
	<xsl:variable name="group.tree.changename.title" select="'Byt namn p� gruppen'" />
	<xsl:variable name="group.tree.delete.title" select="'Ta bort gruppen'" />
	
	<xsl:variable name="user.admin.delete.title" select="'Ta bort anv�ndaren'" />
	
	<xsl:variable name="invitation.admin.expires" select="'Giltig t.o.m'" />
	<xsl:variable name="invitation.admin.sendnew.title" select="'Skicka inbjudan p� nytt till'" />
	<xsl:variable name="invitation.admin.delete.title" select="'Ta bort inbjudan'" />
	
	<xsl:variable name="schoolInfoElement.header" select="'Administrera f�rskolan'" />
	<xsl:variable name="schoolInfoElement.groups.header" select="'Grupper'" />
	<xsl:variable name="schoolInfoElement.groups.nogroups" select="'Det finns inga grupper'" />
	<xsl:variable name="schoolInfoElement.addgroup.header" select="'L�gg till grupp'" />
	<xsl:variable name="schoolInfoElement.addgroup.name" select="'Namn'" />
	<xsl:variable name="schoolInfoElement.addgroup.submit" select="'L�gg till'" />
	<xsl:variable name="schoolInfoElement.resources.header" select="'Resurspersoner'" />
	<xsl:variable name="schoolInfoElement.resources.noresources" select="'Det finns inga resurspersoner'" />
	<xsl:variable name="schoolInfoElement.invitedresources.header" select="'Inbjudna resurspersoner'" />
	<xsl:variable name="schoolInfoElement.invitedresources.noinvited" select="'Det finns inga inbjudna resurspersoner'" />
	<xsl:variable name="schoolInfoElement.addresource.header" select="'L�gg till resursperson'" />	
	<xsl:variable name="schoolInfoElement.addresource.email" select="'E-post'" />
	<xsl:variable name="schoolInfoElement.addresource.submit" select="'L�gg till'" />
	
	<xsl:variable name="group.changename.title" select="'Byt namn p� gruppen'" />
	<xsl:variable name="group.delete.title" select="'Ta bort gruppen'" />
	
	<xsl:variable name="user.schooladmin.delete.title" select="'Ta bort anv�ndaren'" />
	
	<xsl:variable name="invitation.schooladmin.expires" select="'Giltig t.o.m'" />
	<xsl:variable name="invitation.schooladmin.sendnew.title" select="'Skicka inbjudan p� nytt till'" />
	<xsl:variable name="invitation.schooladmin.delete.title" select="'Ta bort inbjudan'" />
	
	<xsl:variable name="validationError.RequiredField" select="'Du m�ste fylla i f�ltet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt v�rde p� f�ltet'" />
	<xsl:variable name="validationError.Other" select="'S�kv�gen �r inte giltig, �ndra f�ltet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Ok�nt fel p� f�ltet'" />
	<xsl:variable name="validationError.field.name" select="'namn'" />
	<xsl:variable name="validationError.field.email" select="'e-post'" />
	<xsl:variable name="validationError.messageKey.DeleteFailedUserNotFound" select="'Anv�ndaren du f�rs�ker ta bort hittades inte'" />
	<xsl:variable name="validationError.messageKey.RenameFailedGroupNotFound" select="'Gruppen du f�rs�ker byta namn p� hittades inte'"/>
	<xsl:variable name="validationError.messageKey.DeleteFailedGroupNotFound" select="'Gruppen du f�rs�ker ta bort hittades inte'"/>
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett ok�nt fel har uppst�tt'" />			

</xsl:stylesheet>