<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="MembersModuleTemplates.xsl" />

	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header.part1" select="'i'" />
	<xsl:variable name="document.header.part2" select="'st'" />
		
	<xsl:variable name="Members.publishers" select="'Pedagoger'" />
	<xsl:variable name="Members.members" select="'Föräldrar'" />
	<xsl:variable name="Members.nousers" select="'Det finns inga användare i den här gruppen'" />
			
	<xsl:variable name="i18n.ShowUser" select="'Visa användaren'" />
	<xsl:variable name="i18n.HideUser" select="'Dölj användaren'" />
			
	<xsl:variable name="user.member" select="'Pedagog'" />	
	<xsl:variable name="user.admin" select="'Administratör'" />	
	<xsl:variable name="user.email" select="'E-post'" />
	<xsl:variable name="user.phoneHome" select="'Telefon hem'" />
	<xsl:variable name="user.phoneMobile" select="'Telefon mobil'" />
	<xsl:variable name="user.phoneWork" select="'Telefon arbete'" />

	<xsl:variable name="i18n.CheckAll" select="'Markera alla'" />
	<xsl:variable name="i18n.UnCheckAll" select="'Avmarkera alla'" />
	<xsl:variable name="i18n.SendEmailsToMembers" select="'Skicka e-post till markerade medlemmar'" />

</xsl:stylesheet>