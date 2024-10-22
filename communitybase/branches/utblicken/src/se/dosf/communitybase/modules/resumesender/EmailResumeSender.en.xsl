<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="EmailResumeSenderTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'Email summary for Parents Meeting'" />
	<xsl:variable name="document.history" select="'The following has happened since your last visit'" />
	<xsl:variable name="document.information.part1" select="'A summation of events since your last visit is also shown on the first page in'" />
	<xsl:variable name="document.information.part2" select="'If you want to, you can edit the settings for the email summary under &quot;My settings&quot; in'" />
	<xsl:variable name="document.link" select="'Parents Meeting'" />
	<xsl:variable name="document.municipalitylink" select="'Link to'" />

</xsl:stylesheet>
