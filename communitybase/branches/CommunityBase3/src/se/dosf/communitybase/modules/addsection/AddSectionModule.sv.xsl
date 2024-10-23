<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:include href="AddSectionModuleTemplates.xsl"/>
	
	
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	
	<xsl:variable name="i18n.MaximumFileUpload">Maximal filstorlek vid uppladdning</xsl:variable>
	
	<xsl:variable name="i18n.Cancel">Avbryt</xsl:variable>

	<xsl:variable name="i18n.validationError.RequiredField">Oj, det h�r f�ltet beh�ver du ocks� fylla i</xsl:variable>
	<xsl:variable name="i18n.validationError.InvalidFormat">Felaktigt format p� f�ltet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong">F�r l�ngt v�rde p� f�ltet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooShort">F�r kort v�rde p� f�ltet</xsl:variable>
	<xsl:variable name="i18n.validationError.Other">Ett ok�nt fel</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownValidationErrorType">Ett ok�nt fel har uppst�tt</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownMessageKey">Ett ok�nt fel har uppst�tt</xsl:variable>
	
	<xsl:variable name="i18n.FileSizeLimitExceeded">Maximal till�ten filstorlek f�r profilbild �verskriden</xsl:variable>
	<xsl:variable name="i18n.UnableToParseRequest">Det gick inte att ladda upp profilbilden</xsl:variable>
	<xsl:variable name="i18n.UnableToParseProfileImage">Det gick inte att ladda upp profilbilden</xsl:variable>
	<xsl:variable name="i18n.UnableToDeleteProfileImage">Det gick inte att ta bort profilbilden, f�rs�k igen</xsl:variable>
	<xsl:variable name="i18n.InvalidProfileImageFileFormat">Otill�tet filformat p� profilbilden. Till�tna filtyper �r png, jpg, gif och bmp.</xsl:variable>
	<xsl:variable name="i18n.InvalidSectionAccess">Du har valt en ogiltig sektretess f�r den valda rumstypen</xsl:variable>
	<xsl:variable name="i18n.InvalidSectionType">Du har valt en ogiltig rums typ</xsl:variable>
	
	<xsl:variable name="i18n.NamePlaceholder">Namnge samarbetsrum</xsl:variable>
	<xsl:variable name="i18n.MaxDescriptionChars">Max 4000 tecken</xsl:variable>
	<xsl:variable name="i18n.LogoOrImage">Logotyp/Bild</xsl:variable>
	<xsl:variable name="i18n.Secrecy">Sekretess</xsl:variable>
	<xsl:variable name="i18n.Open">�ppet</xsl:variable>
	<xsl:variable name="i18n.Closed">St�ngt</xsl:variable>
	<xsl:variable name="i18n.Hidden">Dolt</xsl:variable>
	<xsl:variable name="i18n.createRoom">Skapa samarbetsrum</xsl:variable>
	
	<xsl:variable name="i18n.SelectRoomType">V�lj rumstyp</xsl:variable>
	
	<xsl:variable name="i18n.CreateNewSection">Skapa nytt samarbetsrum</xsl:variable>
	<xsl:variable name="i18n.RoomDeleteDate">Datum f�r automatisk st�ngning av rum</xsl:variable>
	<xsl:variable name="i18n.Optional">Ej obligatoriskt</xsl:variable>
</xsl:stylesheet>
