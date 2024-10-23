<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="UserRemoverTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'Kiitos osallistumisestasi Vanhempainkokoukseen'" />
	<xsl:variable name="document.information1.part1" select="'K�ytt�j�tunnuksesi'" />
	<xsl:variable name="document.information1.part2" select="'on poistettu Vanhempainkokouksesta'" />
	<xsl:variable name="document.information2.part1" select="'Jos mietit kuinka ottaa yhteytt� kouluun tai k�yd� sivustolla'" />
	<xsl:variable name="document.information2.part2" select="'ja ota yhteytt� Vanhempainkokouksesta vastaaviin henkil�ihin'" />
	
	<xsl:variable name="document.about.part1" select="'Jos haluat tiet�� lis�� Vanhempainkokouksen toiminnasta, voit lukea niist� osoitteessa'" />
	<xsl:variable name="document.about.part2" select="'Vanhempainkokous'" />
	<xsl:variable name="document.municipalitylink" select="'Linkki'" />

</xsl:stylesheet>
