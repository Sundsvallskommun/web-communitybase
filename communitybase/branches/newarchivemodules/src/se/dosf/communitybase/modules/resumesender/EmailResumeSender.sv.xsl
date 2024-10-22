<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="EmailResumeSenderTemplates.xsl" />
	
	<xsl:variable name="i18n.Header" select="'E-postresumé från Föräldramötet'" />
	
	<xsl:variable name="i18n.NewEvents" select="'Nya händelser'" />
	<xsl:variable name="i18n.History" select="'Följande har hänt sedan ditt senaste besök'" />
	<xsl:variable name="i18n.Information.Part1" select="'En summering av händelser sedan ditt senaste besök visas även på förstasidan i'" />
	<xsl:variable name="i18n.Information.Part2" select="'Om du skulle vilja ändra inställningarna för e-postresumén så kan du göra det under &quot;Mina inställningar&quot; i'" />
	<xsl:variable name="i18n.Link" select="'Föräldramötet'" />
	<xsl:variable name="i18n.MunicipalityLink" select="'Länk till'" />

</xsl:stylesheet>