<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>
	
	<xsl:include href="CommonTemplates.xsl"/>
	
	<xsl:include href="classpath://se/dosf/communitybase/utils/treeview/TreeviewTemplates.xsl"/>
	
	<xsl:variable name="i18n.AllSchools" select="'Kaikki esikouluissa'" />
	<xsl:variable name="i18n.groups" select="'ryhmät'" />
	<xsl:variable name="i18n.schools" select="'esikoulujen'" />
	
</xsl:stylesheet>