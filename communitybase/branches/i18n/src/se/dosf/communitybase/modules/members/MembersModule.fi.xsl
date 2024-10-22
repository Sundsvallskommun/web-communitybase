<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="MembersModuleTemplates.xsl" />

	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header.part1" select="'i'" />
	<xsl:variable name="document.header.part2" select="'st'" />
		
	<xsl:variable name="Members.publishers" select="'Pedagogit'" />
	<xsl:variable name="Members.members" select="'Vanhemmat'" />
	<xsl:variable name="Members.nousers" select="'Ryhmässä ei ole käyttäjiä'" />
			
	<xsl:variable name="user.member" select="'Pedagogi'" />	
	<xsl:variable name="user.admin" select="'Ylläpitäjä'" />	
	<xsl:variable name="user.email" select="'Sähköposti'" />
	<xsl:variable name="user.phoneHome" select="'Puhelin (koti)'" />
	<xsl:variable name="user.phoneMobile" select="'Puhelin (matka)'" />
	<xsl:variable name="user.phoneWork" select="'Puhelin (työ)'" />


</xsl:stylesheet>
