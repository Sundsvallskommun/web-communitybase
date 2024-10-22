<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="MembersModuleTemplates.xsl" />

	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header.part1" select="'in'" />
	<xsl:variable name="document.header.part2" select="'users'" />
		
	<xsl:variable name="Members.publishers" select="'Educationalists'" />
	<xsl:variable name="Members.members" select="'Parents'" />
	<xsl:variable name="Members.nousers" select="'There are no users in this group'" />
	
	<xsl:variable name="i18n.ShowUser" select="'Show user'" />
	<xsl:variable name="i18n.HideUser" select="'Hide user'" />
			
	<xsl:variable name="user.member" select="'Educationalist'" />	
	<xsl:variable name="user.admin" select="'Administrator'" />	
	<xsl:variable name="user.email" select="'Email'" />
	<xsl:variable name="user.phoneHome" select="'Telephone home'" />
	<xsl:variable name="user.phoneMobile" select="'Telephone mobile'" />
	<xsl:variable name="user.phoneWork" select="'Telephone work'" />

	<xsl:variable name="i18n.CheckAll" select="'Check all'" />
	<xsl:variable name="i18n.UnCheckAll" select="'Uncheck all'" />
	<xsl:variable name="i18n.SendEmailsToMembers" select="'Send email to selected members'" />

</xsl:stylesheet>
