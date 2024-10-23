<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="EmailResumeSenderTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'Sähköpostipäivitys Vanhempainkokouksesta'" />
	<xsl:variable name="document.history" select="'Nämä ovat tapahtuneet viime käyntisi jälkeen'" />
	<xsl:variable name="document.information.part1" select="'Yhteenveto viime käyntisi jälkeisistä tapahtumista on näkyvillä myös etusivulla'" />
	<xsl:variable name="document.information.part2" select="'Jos haluat vaihtaa sähköpostiasetuksiasi, voit tehdä sen käymällä sivulla &quot;Omat asetukset&quot; '" />
	<xsl:variable name="document.link" select="'Vanhempainkokous'" />
	<xsl:variable name="document.municipalitylink" select="'Linkki'" />

</xsl:stylesheet>
