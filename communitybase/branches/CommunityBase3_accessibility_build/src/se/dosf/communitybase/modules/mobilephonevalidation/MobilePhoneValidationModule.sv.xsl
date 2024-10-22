<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:include href="MobilePhoneValidationModuleTemplates.xsl"/>
	
	<xsl:variable name="i18n.Save">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.validationError.RequiredField">Oj, det h�r f�ltet beh�ver du ocks� fylla i</xsl:variable>
	<xsl:variable name="i18n.validationError.InvalidFormat">Felaktigt format p� f�ltet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong">F�r l�ngt v�rde p� f�ltet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooShort">F�r kort v�rde p� f�ltet</xsl:variable>
	<xsl:variable name="i18n.validationError.Other">Ett ok�nt fel</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownValidationErrorType">Ett ok�nt fel har uppst�tt</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownMessageKey">Ett ok�nt fel har uppst�tt</xsl:variable>
	<xsl:variable name="i18n.MobilePhone">Mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.InvalidPhoneNumber">Ogiltigt format p� telefonnummer</xsl:variable>
	<xsl:variable name="i18n.CouldNotSendSMS">Kunde inte skicka bekr�ftelsekod</xsl:variable>
	<xsl:variable name="i18n.SendVerificationCode">Skicka kod</xsl:variable>
	<xsl:variable name="i18n.ValidationCode">Bekr�ftelsekod</xsl:variable>
	<xsl:variable name="i18n.WrongVerificationCode">Felaktig bekr�ftelsekod</xsl:variable>
	<xsl:variable name="i18n.MobilePhoneMissing">Mobiltelefon saknas</xsl:variable>
	<xsl:variable name="i18n.ValidationText">Ditt konto saknar mobiltelefonnummer, detta beh�vs f�r att du skall kunna logga in. Forts�ttsningsvis kommer vi att skicka ut en veriferingskod till detta nummer vid inloggning.</xsl:variable>
</xsl:stylesheet>
