<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="LinkArchiveModuleTemplates.xsl" />
	
	<xsl:variable name="newLinkText">New link: </xsl:variable>
	<xsl:variable name="addGroupLinkBreadcrumb">Add group link</xsl:variable>
	<xsl:variable name="addSchoolLinkBreadcrumb">Add school link</xsl:variable>
	<xsl:variable name="addGlobalLinkBreadcrumb">Add global link</xsl:variable>
	<xsl:variable name="updateLinkBreadcrumb">Update link</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'for'" />
	<xsl:variable name="linksmodule.nolinks1" select="'No links for'" />
	<xsl:variable name="linksmodule.nolinks2" select="'At the moment there are no links for'" />	
	<xsl:variable name="linksmodule.nomunicipalitylinks1" select="'No municipality links'" />
	<xsl:variable name="linksmodule.nomunicipalitylinks2" select="'At the moment there are no municipality links'" />
	
	<xsl:variable name="grouplinkRef.add" select="'Add group link'" />
	<xsl:variable name="schoollinkRef.add" select="'Add school link'" />
	<xsl:variable name="globallinkRef.add" select="'Add global link'" />
		
	<xsl:variable name="groupLinks.nolinks" select="'At the moment there are no group links'" />
	
	<xsl:variable name="schoolLinks.header" select="'Links for'" />
	<xsl:variable name="schoolLinks.nolinks" select="'At the moment there are no school links'" />
	
	<xsl:variable name="globalLinks.header" select="'Municipality links'" />
	<xsl:variable name="globalLinks.nolinks" select="'At the moment there are no municipality links'" />
	
	<xsl:variable name="links.delete.confirm" select="'Are you sure you want to delete the link'" />
	<xsl:variable name="links.delete.title" select="'Delete link'" />
	<xsl:variable name="links.update.title" select="'Edit link'" />
	
	<xsl:variable name="link.address" select="'Address'" />
	<xsl:variable name="link.description" select="'Description'" />
	<xsl:variable name="link.postedBy" select="'Submitted by'" />
	<xsl:variable name="link.deletedUser" select="'Deleted user'" />
	<xsl:variable name="link.submit" select="'Add link'" />
	
	<xsl:variable name="updateLink.header" select="'Update link'" />
	<xsl:variable name="updateLink.submit" select="'Save changes'" />
	
	<xsl:variable name="addGroupLink.header" select="'Add link for the group'" />
		
	<xsl:variable name="addSchoolLink.header" select="'Add link for the school'" />
		
	<xsl:variable name="addGlobalLink.header" select="'Add link for the municipality'" />
			
	<xsl:variable name="validationError.RequiredField" select="'You have to fill in the field'" />
	<xsl:variable name="validationError.InvalidFormat" select="'The address has to start with http://, https:// or ftp://'" />
	<xsl:variable name="validationError.TooLong" select="'The content in the field is too long'" />
	<xsl:variable name="validationError.TooShort" select="'The content in the field is too short'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'The field has an unknown error'" />
	
	<xsl:variable name="validationError.field.description" select="'description'" />
	<xsl:variable name="validationError.field.url" select="'address'" />

</xsl:stylesheet>
