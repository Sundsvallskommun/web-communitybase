<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="FileArchiveModuleTemplates.xsl"/>
	
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.sv.xsl"/>
	
	<!-- Breadcrumbs -->
	<xsl:variable name="addSectionBreadCrumb">L�gg till kategori</xsl:variable>
	<xsl:variable name="addFileBreadCrumb">L�gg till fil</xsl:variable>
	<xsl:variable name="updateSectionBreadCrumb">�ndra kategori</xsl:variable>
	<xsl:variable name="updateFileBreadCrumb">�ndra fil</xsl:variable>
	<xsl:variable name="newFileText">Ny fil: </xsl:variable>
	
	<!-- i18n indirect phrases -->
	<xsl:variable name="i18n.file.newNotification">Ny fil sedan du bes�kte filarkivet senast</xsl:variable>
	<xsl:variable name="i18n.section.publishTo">G�r kategorin �tkomlig f�r</xsl:variable>
	<xsl:variable name="i18n.noaccess">Du har inte r�ttighet till n�gon f�rskola</xsl:variable>
	
	<!-- i18n translations  -->
	<xsl:variable name="i18n.for">f�r</xsl:variable>
	<xsl:variable name="i18n.AddSection">L�gg till kategori</xsl:variable>
	<xsl:variable name="i18n.Filename">Filnamn</xsl:variable>
	<xsl:variable name="i18n.Posted">Datum</xsl:variable>
	<xsl:variable name="i18n.Section">Kategori</xsl:variable>
	<xsl:variable name="i18n.AlphaToOmega">A till �</xsl:variable>
	<xsl:variable name="i18n.OmegaToAlpha">� till A</xsl:variable>
	<xsl:variable name="i18n.NewestFirst">Senast f�rst</xsl:variable>
	<xsl:variable name="i18n.OldestFirst">�ldst f�rst</xsl:variable>
	<xsl:variable name="i18n.DeleteSection">Ta bort kategori</xsl:variable>
	<xsl:variable name="i18n.ShownFor">Visas f�r</xsl:variable>
	<xsl:variable name="i18n.NoFiles">Det finns f�r n�rvarande inga filer h�r</xsl:variable>
	<xsl:variable name="i18n.Name">Namn</xsl:variable>
	<xsl:variable name="i18n.EditSection">�ndra kategorin</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.AllFiles">Alla filer</xsl:variable>
	<xsl:variable name="i18n.AddFile">L�gg till fil</xsl:variable>
	<xsl:variable name="i18n.in">i</xsl:variable>
	<xsl:variable name="i18n.File">Fil</xsl:variable>
	<xsl:variable name="i18n.AllowedFiletypes">Till�tna filtyper</xsl:variable>
	<xsl:variable name="i18n.MaxFilesizeIs">St�rsta till�tna filstorlek �r</xsl:variable>
	<xsl:variable name="i18n.MB">MB</xsl:variable>
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.optional">valfritt</xsl:variable>
	<xsl:variable name="i18n.EditFile">�ndra filen</xsl:variable>
	<xsl:variable name="i18n.DeleteFile">Ta bort filen</xsl:variable>
	<xsl:variable name="i18n.BelongsTo">Tillh�r</xsl:variable>
	<xsl:variable name="i18n.AddedBy">Inlagd av</xsl:variable>
	<xsl:variable name="i18n.atTime">klockan</xsl:variable>
	<xsl:variable name="i18n.OrderBy">Sortera p�</xsl:variable>

	<!-- Validation -->
	<xsl:variable name="i18n.validationError.RequiredField" select="'Du m�ste fylla i f�ltet'"/>
	<xsl:variable name="i18n.validationError.InvalidFormat" select="'Felaktigt v�rde p� f�ltet'"/>
	<xsl:variable name="i18n.validationError.TooShort" select="'F�r litet v�rde p� f�ltet'"/>
	<xsl:variable name="i18n.validationError.TooLong" select="'F�r stort v�rde p� f�ltet'"/>
	<xsl:variable name="i18n.validationError.Other" select="'S�kv�gen �r inte giltig, �ndra f�ltet'"/>
	<xsl:variable name="i18n.validationError.unknownValidationErrorType" select="'Ok�nt fel p� f�ltet'"/>
	
	<xsl:variable name="i18n.validationError.field.name" select="'namn'"/>
	<xsl:variable name="i18n.validationError.field.description" select="'beskrivning'"/>
	
	<xsl:variable name="i18n.validationError.BadFileFormat" select="'Filen har felaktigt filformat'"/>
	<xsl:variable name="i18n.validationError.NoFile" select="'Du har inte valt n�gon fil'"/>
	
	<xsl:variable name="i18n.validationError.UnableToParseRequest" select="'Ett fel intr�ffade n�r filen skulle laddas upp'"/>
	<xsl:variable name="i18n.validationError.NoGroup" select="'Du m�ste v�lja minst en grupp att publicera till'"/>
	
	<xsl:variable name="i18n.validationError.FileTooSmall" select="'Filen du valt att ladda upp �r f�r liten'"/>
	<xsl:variable name="i18n.validationError.unknownMessageKey" select="'Ett ok�nt fel har uppst�tt'"/>				

	<xsl:variable name="i18n.validationError.RequestedSectionNotFound">Den beg�rda kategorin hittades inte</xsl:variable>
	<xsl:variable name="i18n.validationError.FileSizeLimitExceeded">Den valda filen �verskrider den maximalt till�tna filstorleken</xsl:variable>
</xsl:stylesheet>
