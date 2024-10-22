<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="FirstpageModuleTemplates.xsl" />
	
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.sv.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'Välkommen till Föräldramötet'" />
	<xsl:variable name="document.calendarresume" select="'Kalender resumé'" />
	<xsl:variable name="document.history.title" select="'Följande har hänt sedan ditt senaste besök'" />

	<xsl:variable name="document.noposts" select="'Inga nya poster har tillkommit sedan ditt senaste besök'" />

	<xsl:variable name="calendarResume.noposts" select="'Det finns inga kommande kalenderposter'" />
	<xsl:variable name="calendarResume.time" select="'Tid'" />
	<xsl:variable name="calendarResume.title" select="'Rubrik'" />
	<xsl:variable name="calendarResume.group" select="'Grupp'" />
	<xsl:variable name="calendarResume.publishedTo" select="'Visas för'" />
	<xsl:variable name="calendarResume.groups" select="'grupper'" />
	<xsl:variable name="calendarResume.schools" select="'förskolor'" />

	<xsl:variable name="calendarPost.global" select="'Samtliga förskolor'" />

</xsl:stylesheet>