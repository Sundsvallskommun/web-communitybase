<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="GlobalAdminModuleTemplates.xsl" />
	
	<!-- BreadCrumb variables -->
	
	<xsl:variable name="renameScoolBreadCrumbTest">Nime� koulu uudelleen: </xsl:variable>
	<xsl:variable name="updateGroupBreadCrumbTest">Nime� ryhm� uudelleen: </xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="renameSchool.header" select="'Nime� koulu uudelleen'" />
	<xsl:variable name="renameSchool.name" select="'Nimi'" />
	<xsl:variable name="renameSchool.submit" select="'Tallenna muutokset'" />
	
	<xsl:variable name="group.rename.header" select="'Nime� ryhm� uudelleen'" />
	<xsl:variable name="group.rename.name" select="'Nimi'" />
	<xsl:variable name="group.rename.email" select="'S�hk�posti (optional)'" />
	<xsl:variable name="group.rename.submit" select="'Tallenna muutokset'" />
	
	<xsl:variable name="systemInfoElement.header" select="'Hallitse esikouluja'" />
	<xsl:variable name="systemInfoElement.school.header" select="'Esikoulut'" />
	<xsl:variable name="systemInfoElement.school.noschool" select="'Esikouluja ei l�ydy'" />
	<xsl:variable name="systemInfoElement.school.addschool" select="'Lis�� esikoulu'" />
	<xsl:variable name="systemInfoElement.school.addschool.name" select="'Nimi'" />
	<xsl:variable name="systemInfoElement.admins.header" select="'Yll�pit�j�'" />
	<xsl:variable name="systemInfoElement.admins.noadmins" select="'Yll�pit�j�� ei l�ydy'" />
	<xsl:variable name="systemInfoElement.invitedadmins.header" select="'Kutsu yll�pit�ji�'" />
	<xsl:variable name="systemInfoElement.invitedadmins.noadmins" select="'Kutsuttuja yll�pit�ji� ei l�ydy'" />
	<xsl:variable name="systemInfoElement.addadmins.header" select="'Lis�� yll�pit�j�'" />
	<xsl:variable name="systemInfoElement.addadmins.email" select="'S�hk�posti'" />
	<xsl:variable name="systemInfoElement.addadmins.submit" select="'Lis��'" />
	
	<xsl:variable name="schools.tree.title" select="'Esikoulut'" />
	<xsl:variable name="schools.tree.expandAll" select="'Laajenna kaikki'" />
	<xsl:variable name="schools.tree.collapseAll" select="'Supista kaikki'" />
	
	<xsl:variable name="school.tree.changename.title" select="'Vaihda koulun nime�'" />
	<xsl:variable name="school.tree.delete.title" select="'Poista koulu'" />
	
	<xsl:variable name="group.tree.changename.title" select="'Vaihda ryhm�n nime�'" />
	<xsl:variable name="group.tree.delete.title" select="'Poista ryhm�'" />
	
	<xsl:variable name="user.admin.delete.title" select="'Poista yll�pit�j�'" />
	
	<xsl:variable name="invitation.admin.expires" select="'Voimassaolo'" />
	<xsl:variable name="invitation.admin.sendnew.title" select="'L�het� kutsu uudestaan'" />
	<xsl:variable name="invitation.admin.delete.title" select="'Poista kutsu'" />
	
	<xsl:variable name="schoolInfoElement.header" select="'Hallitse esikouluja'" />
	<xsl:variable name="schoolInfoElement.groups.header" select="'Ryhm�t'" />
	<xsl:variable name="schoolInfoElement.groups.nogroups" select="'Ryhmi� ei l�ydy'" />
	<xsl:variable name="schoolInfoElement.addgroup.header" select="'Lis�� ryhm�'" />
	<xsl:variable name="schoolInfoElement.addgroup.name" select="'Nimi'" />
	<xsl:variable name="schoolInfoElement.addgroup.submit" select="'Lis��'" />
	<xsl:variable name="schoolInfoElement.resources.header" select="'Yhteyshenkil�'" />
	<xsl:variable name="schoolInfoElement.resources.noresources" select="'Yhteyshenkil�� ei ole'" />
	<xsl:variable name="schoolInfoElement.invitedresources.header" select="'Kutsu yhteyshenkil�it�'" />
	<xsl:variable name="schoolInfoElement.invitedresources.noinvited" select="'Ei kutsuttuja yhteyshenkil�it�'" />
	<xsl:variable name="schoolInfoElement.addresource.header" select="'Lis�� yhteyshenkil�'" />	
	<xsl:variable name="schoolInfoElement.addresource.email" select="'S�hk�posti'" />
	<xsl:variable name="schoolInfoElement.addresource.submit" select="'Lis��'" />
	
	<xsl:variable name="group.changename.title" select="'Muuta ryhm�n nime�'" />
	<xsl:variable name="group.delete.title" select="'Poista ryhm�'" />
	
	<xsl:variable name="user.schooladmin.delete.title" select="'Poista yll�pit�j�'" />
	
	<xsl:variable name="invitation.schooladmin.expires" select="'Voimassaolo'" />
	<xsl:variable name="invitation.schooladmin.sendnew.title" select="'L�het� kutsu uudestaan'" />
	<xsl:variable name="invitation.schooladmin.delete.title" select="'Poista kutsu'" />
	
	<xsl:variable name="validationError.RequiredField" select="'Kentt� on pakollinen'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Kent�n sis�lt� ei vastaa vaadittua muotoa'" />
	<xsl:variable name="validationError.Other" select="'Polku ei ole kelvollinen, muuta kentt��'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Tuntematon virhe kent�ss�'" />
	<xsl:variable name="validationError.field.name" select="'nimi'" />
	<xsl:variable name="validationError.field.email" select="'s�hk�posti'" />
	<xsl:variable name="validationError.messageKey.DeleteFailedUserNotFound" select="'K�ytt�j�� jota olit poistamassa, ei l�ydy'" />
	<xsl:variable name="validationError.messageKey.RenameFailedGroupNotFound" select="'Ryhm�� jota olit nime�m�ss�, ei l�ydy'"/>
	<xsl:variable name="validationError.messageKey.DeleteFailedGroupNotFound" select="'Ryhm�� jota olit poistamassa, ei l�ydy'"/>
	<xsl:variable name="validationError.unknownMessageKey" select="'Tuntematon virhe'" />	

</xsl:stylesheet>
