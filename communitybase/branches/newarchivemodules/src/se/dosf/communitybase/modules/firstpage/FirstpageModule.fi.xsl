<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="FirstpageModuleTemplates.xsl" />
	
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.fi.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'Tervetuloa Vanhempainkokoukseen'" />
	<xsl:variable name="document.calendarresume" select="'Kalenteri'" />
	<xsl:variable name="document.history.title" select="'Tapahtumat viime käyntisi jälkeen'" />

	<xsl:variable name="document.noposts" select="'Ei uusia viestejä viime käyntisi jälkeen'" />

	<xsl:variable name="calendarResume.noposts" select="'Ei tulevia tapahtumia'" />
	<xsl:variable name="calendarResume.time" select="'Aika'" />
	<xsl:variable name="calendarResume.title" select="'Otsikko'" />
	<xsl:variable name="calendarResume.group" select="'Ryhmä'" />
	<xsl:variable name="calendarResume.publishedTo" select="'Shown for'" />
	<xsl:variable name="calendarResume.groups" select="'groups'" />
	<xsl:variable name="calendarResume.schools" select="'preschools'" />

	<xsl:variable name="calendarPost.global" select="'Kaikki koulut'" />

</xsl:stylesheet>
