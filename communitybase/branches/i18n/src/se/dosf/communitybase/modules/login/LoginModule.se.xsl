<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="LoginModuleTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="Login.header" select="'Inloggning'" />
	
	<xsl:variable name="AccountDisabled.header" select="'Inloggning'" />
	<xsl:variable name="AccountDisabled.text" select="'Ditt konto �r avst�ngt, kontakta systemadministrat�ren f�r mer information.'" />
	
	<xsl:variable name="LoginFailed.header" select="'Inloggning'" />
	<xsl:variable name="LoginFailed.text" select="'Felaktigt anv�ndarnamn eller l�senord!'" />
	
	<xsl:variable name="LoginForm.username" select="'Anv�ndarnamn:'" />
	<xsl:variable name="LoginForm.password" select="'L�senord:'" />
	<xsl:variable name="LoginForm.submit" select="'Logga in'" />
</xsl:stylesheet>