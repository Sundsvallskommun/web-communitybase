<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="FileArchiveModuleTemplates.xsl"/>
	
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.sv.xsl"/>
	
	<!-- Breadcrumbs -->
	<xsl:variable name="addSectionBreadCrumb">Lägg till kategori</xsl:variable>
	<xsl:variable name="addFileBreadCrumb">Lägg till fil</xsl:variable>
	<xsl:variable name="updateSectionBreadCrumb">Ändra kategori</xsl:variable>
	<xsl:variable name="updateFileBreadCrumb">Ändra fil</xsl:variable>
	<xsl:variable name="newFileText">Ny fil: </xsl:variable>
	
	<!-- i18n indirect phrases -->
	<xsl:variable name="i18n.file.newNotification">Ny fil sedan du besökte filarkivet senast</xsl:variable>
	<xsl:variable name="i18n.section.publishTo">Gör kategorin åtkomlig för</xsl:variable>
	<xsl:variable name="i18n.noaccess">Du har inte rättighet till någon förskola</xsl:variable>
	
	<!-- i18n translations  -->
	<xsl:variable name="i18n.for">för</xsl:variable>
	<xsl:variable name="i18n.AddSection">Lägg till kategori</xsl:variable>
	<xsl:variable name="i18n.Filename">Filnamn</xsl:variable>
	<xsl:variable name="i18n.Posted">Datum</xsl:variable>
	<xsl:variable name="i18n.Section">Kategori</xsl:variable>
	<xsl:variable name="i18n.AlphaToOmega">A till Ö</xsl:variable>
	<xsl:variable name="i18n.OmegaToAlpha">Ö till A</xsl:variable>
	<xsl:variable name="i18n.NewestFirst">Senast först</xsl:variable>
	<xsl:variable name="i18n.OldestFirst">Äldst först</xsl:variable>
	<xsl:variable name="i18n.DeleteSection">Ta bort kategori</xsl:variable>
	<xsl:variable name="i18n.ShownFor">Visas för</xsl:variable>
	<xsl:variable name="i18n.NoFiles">Det finns för närvarande inga filer här</xsl:variable>
	<xsl:variable name="i18n.Name">Namn</xsl:variable>
	<xsl:variable name="i18n.EditSection">Ändra kategorin</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.AllFiles">Alla filer</xsl:variable>
	<xsl:variable name="i18n.AddFile">Lägg till fil</xsl:variable>
	<xsl:variable name="i18n.in">i</xsl:variable>
	<xsl:variable name="i18n.File">Fil</xsl:variable>
	<xsl:variable name="i18n.AllowedFiletypes">Tillåtna filtyper</xsl:variable>
	<xsl:variable name="i18n.MaxFilesizeIs">Största tillåtna filstorlek är</xsl:variable>
	<xsl:variable name="i18n.MB">MB</xsl:variable>
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.optional">valfritt</xsl:variable>
	<xsl:variable name="i18n.EditFile">Ändra filen</xsl:variable>
	<xsl:variable name="i18n.DeleteFile">Ta bort filen</xsl:variable>
	<xsl:variable name="i18n.BelongsTo">Tillhör</xsl:variable>
	<xsl:variable name="i18n.AddedBy">Inlagd av</xsl:variable>
	<xsl:variable name="i18n.atTime">klockan</xsl:variable>
	<xsl:variable name="i18n.OrderBy">Sortera på</xsl:variable>

	<!-- Validation -->
	<xsl:variable name="i18n.validationError.RequiredField" select="'Du måste fylla i fältet'"/>
	<xsl:variable name="i18n.validationError.InvalidFormat" select="'Felaktigt värde på fältet'"/>
	<xsl:variable name="i18n.validationError.TooShort" select="'För litet värde på fältet'"/>
	<xsl:variable name="i18n.validationError.TooLong" select="'För stort värde på fältet'"/>
	<xsl:variable name="i18n.validationError.Other" select="'Sökvägen är inte giltig, ändra fältet'"/>
	<xsl:variable name="i18n.validationError.unknownValidationErrorType" select="'Okänt fel på fältet'"/>
	
	<xsl:variable name="i18n.validationError.field.name" select="'namn'"/>
	<xsl:variable name="i18n.validationError.field.description" select="'beskrivning'"/>
	
	<xsl:variable name="i18n.validationError.BadFileFormat" select="'Filen har felaktigt filformat'"/>
	<xsl:variable name="i18n.validationError.NoFile" select="'Du har inte valt någon fil'"/>
	
	<xsl:variable name="i18n.validationError.UnableToParseRequest" select="'Ett fel inträffade när filen skulle laddas upp'"/>
	<xsl:variable name="i18n.validationError.NoGroup" select="'Du måste välja minst en grupp att publicera till'"/>
	
	<xsl:variable name="i18n.validationError.FileTooSmall" select="'Filen du valt att ladda upp är för liten'"/>
	<xsl:variable name="i18n.validationError.unknownMessageKey" select="'Ett okänt fel har uppstått'"/>				

	<xsl:variable name="i18n.validationError.RequestedSectionNotFound">Den begärda kategorin hittades inte</xsl:variable>
	<xsl:variable name="i18n.validationError.FileSizeLimitExceeded">Den valda filen överskrider den maximalt tillåtna filstorleken</xsl:variable>
</xsl:stylesheet>
