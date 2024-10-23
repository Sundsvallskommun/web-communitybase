<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="MembersModuleTemplates.xsl" />

	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header.part1" select="'i'" />
	<xsl:variable name="document.header.part2" select="'st'" />
		
	<xsl:variable name="Members.publishers" select="'Pedagogit'" />
	<xsl:variable name="Members.members" select="'Vanhemmat'" />
	<xsl:variable name="Members.nousers" select="'Ryhm�ss� ei ole k�ytt�ji�'" />
		
	<xsl:variable name="i18n.ShowUser" select="'Show user'" />
	<xsl:variable name="i18n.HideUser" select="'Hide user'" />
			
	<xsl:variable name="user.member" select="'Pedagogi'" />	
	<xsl:variable name="user.admin" select="'Yll�pit�j�'" />	
	<xsl:variable name="user.email" select="'S�hk�posti'" />
	<xsl:variable name="user.phoneHome" select="'Puhelin (koti)'" />
	<xsl:variable name="user.phoneMobile" select="'Puhelin (matka)'" />
	<xsl:variable name="user.phoneWork" select="'Puhelin (ty�)'" />

	<xsl:variable name="i18n.CheckAll" select="'Check all'" />
	<xsl:variable name="i18n.UnCheckAll" select="'Uncheck all'" />
	<xsl:variable name="i18n.SendEmailsToMembers" select="'Send email to selected members'" />

</xsl:stylesheet>
