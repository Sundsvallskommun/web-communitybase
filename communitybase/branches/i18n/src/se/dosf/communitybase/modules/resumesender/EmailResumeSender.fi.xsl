<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="EmailResumeSenderTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'S�hk�postip�ivitys Vanhempainkokouksesta'" />
	<xsl:variable name="document.history" select="'N�m� ovat tapahtuneet viime k�yntisi j�lkeen'" />
	<xsl:variable name="document.information.part1" select="'Yhteenveto viime k�yntisi j�lkeisist� tapahtumista on n�kyvill� my�s etusivulla'" />
	<xsl:variable name="document.information.part2" select="'Jos haluat vaihtaa s�hk�postiasetuksiasi, voit tehd� sen k�ym�ll� sivulla &quot;Omat asetukset&quot; '" />
	<xsl:variable name="document.link" select="'Vanhempainkokous'" />
	<xsl:variable name="document.municipalitylink" select="'Linkki'" />

</xsl:stylesheet>
