<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="GlobalAdminModuleTemplates.xsl" />
	
	<!-- BreadCrumb variables -->
	
	<xsl:variable name="renameScoolBreadCrumbTest">Nimeä koulu uudelleen: </xsl:variable>
	<xsl:variable name="updateGroupBreadCrumbTest">Nimeä ryhmä uudelleen: </xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="renameSchool.header" select="'Nimeä koulu uudelleen'" />
	<xsl:variable name="renameSchool.name" select="'Nimi'" />
	<xsl:variable name="renameSchool.submit" select="'Tallenna muutokset'" />
	
	<xsl:variable name="group.rename.header" select="'Nimeä ryhmä uudelleen'" />
	<xsl:variable name="group.rename.name" select="'Nimi'" />
	<xsl:variable name="group.rename.email" select="'Sähköposti (optional)'" />
	<xsl:variable name="group.rename.submit" select="'Tallenna muutokset'" />
	
	<xsl:variable name="systemInfoElement.header" select="'Hallitse esikouluja'" />
	<xsl:variable name="systemInfoElement.school.header" select="'Esikoulut'" />
	<xsl:variable name="systemInfoElement.school.noschool" select="'Esikouluja ei löydy'" />
	<xsl:variable name="systemInfoElement.school.addschool" select="'Lisää esikoulu'" />
	<xsl:variable name="systemInfoElement.school.addschool.name" select="'Nimi'" />
	<xsl:variable name="systemInfoElement.admins.header" select="'Ylläpitäjä'" />
	<xsl:variable name="systemInfoElement.admins.noadmins" select="'Ylläpitäjää ei löydy'" />
	<xsl:variable name="systemInfoElement.invitedadmins.header" select="'Kutsu ylläpitäjiä'" />
	<xsl:variable name="systemInfoElement.invitedadmins.noadmins" select="'Kutsuttuja ylläpitäjiä ei löydy'" />
	<xsl:variable name="systemInfoElement.addadmins.header" select="'Lisää ylläpitäjä'" />
	<xsl:variable name="systemInfoElement.addadmins.email" select="'Sähköposti'" />
	<xsl:variable name="systemInfoElement.addadmins.submit" select="'Lisää'" />
	
	<xsl:variable name="schools.tree.title" select="'Esikoulut'" />
	<xsl:variable name="schools.tree.expandAll" select="'Laajenna kaikki'" />
	<xsl:variable name="schools.tree.collapseAll" select="'Supista kaikki'" />
	
	<xsl:variable name="school.tree.changename.title" select="'Vaihda koulun nimeä'" />
	<xsl:variable name="school.tree.delete.title" select="'Poista koulu'" />
	
	<xsl:variable name="group.tree.changename.title" select="'Vaihda ryhmän nimeä'" />
	<xsl:variable name="group.tree.delete.title" select="'Poista ryhmä'" />
	
	<xsl:variable name="user.admin.delete.title" select="'Poista ylläpitäjä'" />
	
	<xsl:variable name="invitation.admin.expires" select="'Voimassaolo'" />
	<xsl:variable name="invitation.admin.sendnew.title" select="'Lähetä kutsu uudestaan'" />
	<xsl:variable name="invitation.admin.delete.title" select="'Poista kutsu'" />
	
	<xsl:variable name="schoolInfoElement.header" select="'Hallitse esikouluja'" />
	<xsl:variable name="schoolInfoElement.groups.header" select="'Ryhmät'" />
	<xsl:variable name="schoolInfoElement.groups.nogroups" select="'Ryhmiä ei löydy'" />
	<xsl:variable name="schoolInfoElement.addgroup.header" select="'Lisää ryhmä'" />
	<xsl:variable name="schoolInfoElement.addgroup.name" select="'Nimi'" />
	<xsl:variable name="schoolInfoElement.addgroup.submit" select="'Lisää'" />
	<xsl:variable name="schoolInfoElement.resources.header" select="'Yhteyshenkilö'" />
	<xsl:variable name="schoolInfoElement.resources.noresources" select="'Yhteyshenkilöä ei ole'" />
	<xsl:variable name="schoolInfoElement.invitedresources.header" select="'Kutsu yhteyshenkilöitä'" />
	<xsl:variable name="schoolInfoElement.invitedresources.noinvited" select="'Ei kutsuttuja yhteyshenkilöitä'" />
	<xsl:variable name="schoolInfoElement.addresource.header" select="'Lisää yhteyshenkilö'" />	
	<xsl:variable name="schoolInfoElement.addresource.email" select="'Sähköposti'" />
	<xsl:variable name="schoolInfoElement.addresource.submit" select="'Lisää'" />
	
	<xsl:variable name="group.changename.title" select="'Muuta ryhmän nimeä'" />
	<xsl:variable name="group.delete.title" select="'Poista ryhmä'" />
	
	<xsl:variable name="user.schooladmin.delete.title" select="'Poista ylläpitäjä'" />
	
	<xsl:variable name="invitation.schooladmin.expires" select="'Voimassaolo'" />
	<xsl:variable name="invitation.schooladmin.sendnew.title" select="'Lähetä kutsu uudestaan'" />
	<xsl:variable name="invitation.schooladmin.delete.title" select="'Poista kutsu'" />
	
	<xsl:variable name="validationError.RequiredField" select="'Kenttä on pakollinen'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Kentän sisältö ei vastaa vaadittua muotoa'" />
	<xsl:variable name="validationError.Other" select="'Polku ei ole kelvollinen, muuta kenttää'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Tuntematon virhe kentässä'" />
	<xsl:variable name="validationError.field.name" select="'nimi'" />
	<xsl:variable name="validationError.field.email" select="'sähköposti'" />
	<xsl:variable name="validationError.messageKey.DeleteFailedUserNotFound" select="'Käyttäjää jota olit poistamassa, ei löydy'" />
	<xsl:variable name="validationError.messageKey.RenameFailedGroupNotFound" select="'Ryhmää jota olit nimeämässä, ei löydy'"/>
	<xsl:variable name="validationError.messageKey.DeleteFailedGroupNotFound" select="'Ryhmää jota olit poistamassa, ei löydy'"/>
	<xsl:variable name="validationError.unknownMessageKey" select="'Tuntematon virhe'" />	

</xsl:stylesheet>
