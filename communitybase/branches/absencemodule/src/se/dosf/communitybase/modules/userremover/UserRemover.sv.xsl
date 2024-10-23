<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="UserRemoverTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'Tack för att Du använt Föräldramötet'" />
	<xsl:variable name="document.information1.part1" select="'Ditt användarkonto'" />
	<xsl:variable name="document.information1.part2" select="'på Föräldramötet har tagits bort'" />
	<xsl:variable name="document.information2.part1" select="'Är du fundersam på varför kan du kontakta skolan eller besöka webbplatsen'" />
	<xsl:variable name="document.information2.part2" select="'och kontakta någon av oss som ansvarar för Föräldramötet'" />
	
	<xsl:variable name="document.about.part1" select="'Vill du veta mer om tankarna bakom Föräldramötet kan du läsa om dem på'" />
	<xsl:variable name="document.about.part2" select="'Föräldramötet'" />
	<xsl:variable name="document.municipalitylink" select="'Länk till'" />

</xsl:stylesheet>