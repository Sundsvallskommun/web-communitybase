<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:include href="SearchModuleTemplates.xsl"/>
	
	<!-- Module XSL variables -->
	<xsl:variable name="java.users">Personer</xsl:variable>
	<xsl:variable name="java.sections">Samarbetsrum</xsl:variable>
	<xsl:variable name="java.file">Filer</xsl:variable>
	<xsl:variable name="java.task">Uppgifter</xsl:variable>
	<xsl:variable name="java.post">Inl�gg</xsl:variable>
	<xsl:variable name="java.calendar">Kalender</xsl:variable>
	<xsl:variable name="java.page">Sidor i samarbetsrum</xsl:variable>
	<xsl:variable name="java.tags">Taggar</xsl:variable>
	<xsl:variable name="java.tag">Taggat inneh�ll</xsl:variable>

	<!-- Internationalization -->
	<xsl:variable name="i18n.Search">S�k</xsl:variable>
	<xsl:variable name="i18n.AllResults">Alla s�kresultat</xsl:variable>
	<xsl:variable name="i18n.Showing">Visar</xsl:variable>
	<xsl:variable name="i18n.of">av</xsl:variable>
	<xsl:variable name="i18n.ShowMoreUsers">Visa fler </xsl:variable>
	<xsl:variable name="i18n.ShowLessUsers">Visa f�rre</xsl:variable>
	<xsl:variable name="i18n.ShowMoreSections">Visa fler</xsl:variable>
	<xsl:variable name="i18n.ShowLessSections">Visa f�rre</xsl:variable>
	<xsl:variable name="i18n.members">deltagare</xsl:variable>
	<xsl:variable name="i18n.NoHits">S�kningen gav inga tr�ffar.</xsl:variable>
	<xsl:variable name="i18n.ShowMore">Visa fler</xsl:variable>
	<xsl:variable name="i18n.ShowLess">Visa f�rre</xsl:variable>
	<xsl:variable name="i18n.ShowMoreTags">Visa fler</xsl:variable>
	<xsl:variable name="i18n.ShowLessTags">Visa f�rre</xsl:variable>
	<xsl:variable name="i18n.IsExternal">Extern</xsl:variable>
	<xsl:variable name="i18n.SearchWord">S�kord</xsl:variable>
	<xsl:variable name="i18n.TypeOfResult">Typ av resultat</xsl:variable>
	<xsl:variable name="i18n.FilterResult">Filtrera s�kresultatet</xsl:variable>
	<xsl:variable name="i18n.SecrecyLevel">Sekretessniv�</xsl:variable>
	<xsl:variable name="i18n.Closed">St�ngt</xsl:variable>
	<xsl:variable name="i18n.Hidden">Dolt</xsl:variable>
	<xsl:variable name="i18n.Open">�ppet</xsl:variable>
	
</xsl:stylesheet>
