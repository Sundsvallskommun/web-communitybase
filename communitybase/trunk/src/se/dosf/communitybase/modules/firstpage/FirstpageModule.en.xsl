<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="FirstpageModuleTemplates.xsl" />
	
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.en.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'Welcome to Parents Meeting'" />
	<xsl:variable name="document.calendarresume" select="'Calendar summary'" />
	<xsl:variable name="document.history.title" select="'The following has happened since your last visit'" />

	<xsl:variable name="document.noposts" select="'No new posts since your last visit'" />

	<xsl:variable name="calendarResume.noposts" select="'There are no upcoming calendar post'" />
	<xsl:variable name="calendarResume.time" select="'Time'" />
	<xsl:variable name="calendarResume.title" select="'Title'" />
	<xsl:variable name="calendarResume.group" select="'Group'" />
	<xsl:variable name="calendarResume.publishedTo" select="'Shown for'" />
	<xsl:variable name="calendarResume.groups" select="'groups'" />
	<xsl:variable name="calendarResume.schools" select="'preschools'" />

	<xsl:variable name="calendarPost.global" select="'Several groups'" />

</xsl:stylesheet>
