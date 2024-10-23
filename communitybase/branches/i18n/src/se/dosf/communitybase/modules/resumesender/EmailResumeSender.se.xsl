<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="EmailResumeSenderTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'E-post resume fr�n F�r�ldram�tet'" />
	<xsl:variable name="document.history" select="'F�ljande har h�nt sen ditt senaste bes�k'" />
	<xsl:variable name="document.information.part1" select="'En summering av h�ndelser sedan ert senaste bes�k visas �ven p� f�rsta sidan i'" />
	<xsl:variable name="document.information.part2" select="'Om ni skulle vilja �ndra inst�llningarna f�r e-post resumen s� kan du g�ra det under &quot;Mina inst�llningar&quot; i'" />
	<xsl:variable name="document.link" select="'F�r�ldram�tet'" />
	<xsl:variable name="document.municipalitylink" select="'L�nk till'" />

</xsl:stylesheet>