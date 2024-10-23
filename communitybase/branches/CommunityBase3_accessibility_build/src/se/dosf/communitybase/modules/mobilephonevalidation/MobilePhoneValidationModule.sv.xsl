<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:include href="MobilePhoneValidationModuleTemplates.xsl"/>
	
	<xsl:variable name="i18n.Save">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.validationError.RequiredField">Oj, det här fältet behöver du också fylla i</xsl:variable>
	<xsl:variable name="i18n.validationError.InvalidFormat">Felaktigt format på fältet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong">För långt värde på fältet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooShort">För kort värde på fältet</xsl:variable>
	<xsl:variable name="i18n.validationError.Other">Ett okänt fel</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownValidationErrorType">Ett okänt fel har uppstått</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownMessageKey">Ett okänt fel har uppstått</xsl:variable>
	<xsl:variable name="i18n.MobilePhone">Mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.InvalidPhoneNumber">Ogiltigt format på telefonnummer</xsl:variable>
	<xsl:variable name="i18n.CouldNotSendSMS">Kunde inte skicka bekräftelsekod</xsl:variable>
	<xsl:variable name="i18n.SendVerificationCode">Skicka kod</xsl:variable>
	<xsl:variable name="i18n.ValidationCode">Bekräftelsekod</xsl:variable>
	<xsl:variable name="i18n.WrongVerificationCode">Felaktig bekräftelsekod</xsl:variable>
	<xsl:variable name="i18n.MobilePhoneMissing">Mobiltelefon saknas</xsl:variable>
	<xsl:variable name="i18n.ValidationText">Ditt konto saknar mobiltelefonnummer, detta behövs för att du skall kunna logga in. Fortsättsningsvis kommer vi att skicka ut en veriferingskod till detta nummer vid inloggning.</xsl:variable>
</xsl:stylesheet>
