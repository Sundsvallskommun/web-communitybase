<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:include href="AddSectionModuleTemplates.xsl"/>
	
	<!-- Validation -->
	<xsl:variable name="i18n.validationError.Required.Section.Name">Du måste ange ett namn för rummet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.Section.Name">Namnet för rummet får maximalt innehålla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.Section.Description">Beskrivningen av rummet får maximalt innehålla 4000 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.InvalidFormat.Section.AccessMode">Ogiltigt val av sekretessnivå</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.Section.AccessMode">Du måste ange en sekretessnivå</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.Section.SectionType">Du måste ange en rumstyp för rummet</xsl:variable>
	<xsl:variable name="i18n.validationError.InvalidFormat.Section.SectionType">Du har valt en ogiltig rumstyp för rummet</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.Section.DeleteDate">Du måste ange ett datum för automatisk stängning av rummet</xsl:variable>
	<xsl:variable name="i18n.validationError.InvalidFormat.Section.DeleteDate">Ogiltigt format på datumet för automatisk stängning av rummet</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownMessageKey">Ett okänt fel har uppstått</xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded">Maximal tillåten filstorlek för profilbild överskriden</xsl:variable>
	<xsl:variable name="i18n.UnableToParseRequest">Det gick inte att ladda upp profilbilden</xsl:variable>
	<xsl:variable name="i18n.UnableToParseProfileImage">Det gick inte att ladda upp profilbilden</xsl:variable>
	<xsl:variable name="i18n.UnableToDeleteProfileImage">Det gick inte att ta bort profilbilden, försök igen</xsl:variable>
	<xsl:variable name="i18n.InvalidProfileImageFileFormat">Otillåtet filformat på profilbilden. Tillåtna filtyper är png, jpg, gif och bmp.</xsl:variable>
	<xsl:variable name="i18n.InvalidSectionAccess">Du har valt en ogiltig sektretess för den valda rumstypen</xsl:variable>
	<xsl:variable name="i18n.InvalidSectionType">Du har valt en ogiltig rums typ</xsl:variable>
	
	<!-- Internationalization -->
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.MaximumFileUpload">Maximal filstorlek vid uppladdning</xsl:variable>
	<xsl:variable name="i18n.Cancel">Avbryt</xsl:variable>
	<xsl:variable name="i18n.NamePlaceholder">Namnge samarbetsrum</xsl:variable>
	<xsl:variable name="i18n.MaxDescriptionChars">Max 4000 tecken</xsl:variable>
	<xsl:variable name="i18n.LogoOrImage">Logotyp/Bild</xsl:variable>
	<xsl:variable name="i18n.SecrecyLevel">Sekretessnivå</xsl:variable>
	<xsl:variable name="i18n.Open">Öppet</xsl:variable>
	<xsl:variable name="i18n.Closed">Stängt</xsl:variable>
	<xsl:variable name="i18n.Hidden">Dolt</xsl:variable>
	<xsl:variable name="i18n.createRoom">Skapa samarbetsrum</xsl:variable>
	<xsl:variable name="i18n.SelectRoomType">Välj rumstyp</xsl:variable>
	<xsl:variable name="i18n.CreateNewSection">Skapa nytt samarbetsrum</xsl:variable>
	<xsl:variable name="i18n.RoomDeleteDate">Datum för automatisk stängning av rum</xsl:variable>
	<xsl:variable name="i18n.Optional">Ej obligatoriskt</xsl:variable>
	<xsl:variable name="i18n.OpenAccessModeTitle">Öppet rum som alla medarbetare kan se och följa</xsl:variable>
	<xsl:variable name="i18n.ClosedAccessModeTitle">Stängt rum som alla medarbetare kan se, deltagare bjuds in</xsl:variable>
	<xsl:variable name="i18n.HiddenAccessModeTitle">Hemligt rum som endast inbjudna deltagare ser</xsl:variable>
	<xsl:variable name="i18n.RequiredFieldsAreMarked">Obligatoriska fält är markerade med en *</xsl:variable>

</xsl:stylesheet>
