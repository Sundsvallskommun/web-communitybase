<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="LoginModuleTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="Login.header" select="'Kirjaudu'" />
	
	<xsl:variable name="AccountDisabled.header" select="'Kirjaudu'" />
	<xsl:variable name="AccountDisabled.text" select="'Tunnuksesi on suljettu. Ota yhteyttä ylläpitäjään saadaksesi lisätietoja!'" />
	
	<xsl:variable name="LoginFailed.header" select="'Kirjaudu'" />
	<xsl:variable name="LoginFailed.text" select="'Väärä tunnus tai salasana!'" />
	
	<xsl:variable name="LoginForm.username" select="'Tunnus:'" />
	<xsl:variable name="LoginForm.password" select="'Salasana:'" />
	<xsl:variable name="LoginForm.submit" select="'Kirjaudu'" />
</xsl:stylesheet>
