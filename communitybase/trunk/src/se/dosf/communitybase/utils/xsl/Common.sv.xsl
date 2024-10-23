<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>
	
	<xsl:include href="CommonTemplates.xsl"/>
	
	<xsl:include href="classpath://se/dosf/communitybase/utils/treeview/TreeviewTemplates.xsl"/>
	
	<xsl:variable name="i18n.AllSchools" select="'Samtliga f�rskolor'" />
	<xsl:variable name="i18n.groups" select="'grupper'" />
	<xsl:variable name="i18n.schools" select="'f�rskolor'" />
	
	<xsl:variable name="i18n.CollapseAll" select="'St�ng alla'" />
	<xsl:variable name="i18n.ExpandAll" select="'F�ll ut alla'" />
	<xsl:variable name="i18n.TreeTitle" select="'F�rskolor'" />
	<xsl:variable name="i18n.LoadingSchoolsAndGroups" select="'H�mtar f�rskolor och grupper'" />	
	
	<xsl:variable name="i18n.RemovedUser" select="'Borttagen anv�ndare'" />
	<xsl:variable name="i18n.by" select="'av'" />
	
</xsl:stylesheet>