<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="FirstpageModuleTemplates.xsl" />
	
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.sv.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'V�lkommen till F�r�ldram�tet'" />
	<xsl:variable name="document.calendarresume" select="'Kalender resum�'" />
	<xsl:variable name="document.history.title" select="'F�ljande har h�nt sedan ditt senaste bes�k'" />

	<xsl:variable name="document.noposts" select="'Inga nya poster har tillkommit sedan ditt senaste bes�k'" />

	<xsl:variable name="calendarResume.noposts" select="'Det finns inga kommande kalenderposter'" />
	<xsl:variable name="calendarResume.time" select="'Tid'" />
	<xsl:variable name="calendarResume.title" select="'Rubrik'" />
	<xsl:variable name="calendarResume.group" select="'Grupp'" />
	<xsl:variable name="calendarResume.publishedTo" select="'Visas f�r'" />
	<xsl:variable name="calendarResume.groups" select="'grupper'" />
	<xsl:variable name="calendarResume.schools" select="'f�rskolor'" />

	<xsl:variable name="calendarPost.global" select="'Samtliga f�rskolor'" />

</xsl:stylesheet>