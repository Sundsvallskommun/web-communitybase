<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="UserRemoverTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'Tack f�r att Du anv�nt F�r�ldram�tet'" />
	<xsl:variable name="document.information1.part1" select="'Ditt anv�ndarkonto'" />
	<xsl:variable name="document.information1.part2" select="'p� F�r�ldram�tet har tagits bort'" />
	<xsl:variable name="document.information2.part1" select="'�r du fundersam p� varf�r kan du kontakta skolan eller bes�ka webbplatsen'" />
	<xsl:variable name="document.information2.part2" select="'och kontakta n�gon av oss som ansvarar f�r F�r�ldram�tet'" />
	
	<xsl:variable name="document.about.part1" select="'Vill du veta mer om tankarna bakom F�r�ldram�tet kan du l�sa om dem p�'" />
	<xsl:variable name="document.about.part2" select="'F�r�ldram�tet'" />
	<xsl:variable name="document.municipalitylink" select="'L�nk till'" />

</xsl:stylesheet>