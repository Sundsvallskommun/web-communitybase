<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="LoginModuleTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="Login.header" select="'Login'" />
	
	<xsl:variable name="AccountDisabled.header" select="'Login'" />
	<xsl:variable name="AccountDisabled.text" select="'Your account is disabled, contact the administrator for more information!'" />
	
	<xsl:variable name="LoginFailed.header" select="'Login'" />
	<xsl:variable name="LoginFailed.text" select="'Invalid username or password!'" />
	
	<xsl:variable name="LoginForm.username" select="'Username:'" />
	<xsl:variable name="LoginForm.password" select="'Password:'" />
	<xsl:variable name="LoginForm.submit" select="'Login'" />
</xsl:stylesheet>