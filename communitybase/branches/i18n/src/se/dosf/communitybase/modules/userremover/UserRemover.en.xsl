<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="UserRemoverTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'Thanks for using Parents Meeting'" />
	<xsl:variable name="document.information1.part1" select="'Your user account'" />
	<xsl:variable name="document.information1.part2" select="'on Parents Meeting has been deleted'" />
	<xsl:variable name="document.information2.part1" select="'If you want more information, please contact the school or visit the webpage'" />
	<xsl:variable name="document.information2.part2" select="'and contact one of us who are responsible for Parents Meeting'" />
	
	<xsl:variable name="document.about.part1" select="'If you want to read more about the thoughts behind Parents Meeting, please go to'" />
	<xsl:variable name="document.about.part2" select="'Parents Meeting'" />
	<xsl:variable name="document.municipalitylink" select="'Link to'" />

</xsl:stylesheet>
