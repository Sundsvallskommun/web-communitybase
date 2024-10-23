<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:include href="AddSectionModuleTemplates.xsl"/>
	
	<!-- Validation -->
	<xsl:variable name="i18n.validationError.Required.Section.Name">Du m�ste ange ett namn f�r rummet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.Section.Name">Namnet f�r rummet f�r maximalt inneh�lla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.Section.Description">Beskrivningen av rummet f�r maximalt inneh�lla 4000 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.InvalidFormat.Section.AccessMode">Ogiltigt val av sekretessniv�</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.Section.AccessMode">Du m�ste ange en sekretessniv�</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.Section.SectionType">Du m�ste ange en rumstyp f�r rummet</xsl:variable>
	<xsl:variable name="i18n.validationError.InvalidFormat.Section.SectionType">Du har valt en ogiltig rumstyp f�r rummet</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.Section.DeleteDate">Du m�ste ange ett datum f�r automatisk st�ngning av rummet</xsl:variable>
	<xsl:variable name="i18n.validationError.InvalidFormat.Section.DeleteDate">Ogiltigt format p� datumet f�r automatisk st�ngning av rummet</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownMessageKey">Ett ok�nt fel har uppst�tt</xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded">Maximal till�ten filstorlek f�r profilbild �verskriden</xsl:variable>
	<xsl:variable name="i18n.UnableToParseRequest">Det gick inte att ladda upp profilbilden</xsl:variable>
	<xsl:variable name="i18n.UnableToParseProfileImage">Det gick inte att ladda upp profilbilden</xsl:variable>
	<xsl:variable name="i18n.UnableToDeleteProfileImage">Det gick inte att ta bort profilbilden, f�rs�k igen</xsl:variable>
	<xsl:variable name="i18n.InvalidProfileImageFileFormat">Otill�tet filformat p� profilbilden. Till�tna filtyper �r png, jpg, gif och bmp.</xsl:variable>
	<xsl:variable name="i18n.InvalidSectionAccess">Du har valt en ogiltig sektretess f�r den valda rumstypen</xsl:variable>
	<xsl:variable name="i18n.InvalidSectionType">Du har valt en ogiltig rums typ</xsl:variable>
	
	<!-- Internationalization -->
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.MaximumFileUpload">Maximal filstorlek vid uppladdning</xsl:variable>
	<xsl:variable name="i18n.Cancel">Avbryt</xsl:variable>
	<xsl:variable name="i18n.NamePlaceholder">Namnge samarbetsrum</xsl:variable>
	<xsl:variable name="i18n.MaxDescriptionChars">Max 4000 tecken</xsl:variable>
	<xsl:variable name="i18n.LogoOrImage">Logotyp/Bild</xsl:variable>
	<xsl:variable name="i18n.SecrecyLevel">Sekretessniv�</xsl:variable>
	<xsl:variable name="i18n.Open">�ppet</xsl:variable>
	<xsl:variable name="i18n.Closed">St�ngt</xsl:variable>
	<xsl:variable name="i18n.Hidden">Dolt</xsl:variable>
	<xsl:variable name="i18n.createRoom">Skapa samarbetsrum</xsl:variable>
	<xsl:variable name="i18n.SelectRoomType">V�lj rumstyp</xsl:variable>
	<xsl:variable name="i18n.CreateNewSection">Skapa nytt samarbetsrum</xsl:variable>
	<xsl:variable name="i18n.RoomDeleteDate">Datum f�r automatisk st�ngning av rum</xsl:variable>
	<xsl:variable name="i18n.Optional">Ej obligatoriskt</xsl:variable>
	<xsl:variable name="i18n.OpenAccessModeTitle">�ppet rum som alla medarbetare kan se och f�lja</xsl:variable>
	<xsl:variable name="i18n.ClosedAccessModeTitle">St�ngt rum som alla medarbetare kan se, deltagare bjuds in</xsl:variable>
	<xsl:variable name="i18n.HiddenAccessModeTitle">Hemligt rum som endast inbjudna deltagare ser</xsl:variable>
	<xsl:variable name="i18n.RequiredFieldsAreMarked">Obligatoriska f�lt �r markerade med en *</xsl:variable>

</xsl:stylesheet>
