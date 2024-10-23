<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:include href="UserProfileModuleTemplates.xsl"/>
	
	<xsl:variable name="i18n.Firstname">Förnamn</xsl:variable>
	<xsl:variable name="i18n.Lastname">Efternamn</xsl:variable>
	<xsl:variable name="i18n.Email">E-postadress</xsl:variable>
	<xsl:variable name="i18n.Phone">Telefon</xsl:variable>
	<xsl:variable name="i18n.Organization">Företag/befattning</xsl:variable>
	<xsl:variable name="i18n.Optional">Frivilligt</xsl:variable>
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	
	
	
	<xsl:variable name="i18n.Save">Spara ändringar</xsl:variable>
	
	<xsl:variable name="i18n.PasswordDontMatch">De angivna lösenorden överensstämmer inte</xsl:variable>
	<xsl:variable name="i18n.InvalidPasswordStrength">Lösenordet måste innehålla minst 8 tecken varav en siffra, en liten bokstav och en stor bokstav</xsl:variable>

	<xsl:variable name="i18n.UpdateProfile">Ändra min profil</xsl:variable>
	<xsl:variable name="i18n.NoDescription">har inte angett någon beskrivning om sig själv</xsl:variable>
	
	<xsl:variable name="i18n.ProfileImage">Profilbild</xsl:variable>
	
	<xsl:variable name="i18n.MaximumFileUpload">Maximal filstorlek vid uppladdning</xsl:variable>
	<xsl:variable name="i18n.RemoveProfileImage">Ta bort</xsl:variable>
	<xsl:variable name="i18n.Cancel">Avbryt</xsl:variable>

	<xsl:variable name="i18n.validationError.RequiredField">Fältet får inte vara tomt</xsl:variable>
	<xsl:variable name="i18n.validationError.InvalidFormat">Felaktigt format på fältet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong">För långt värde på fältet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooShort">För kort värde på fältet</xsl:variable>
	<xsl:variable name="i18n.validationError.Other">Ett okänt fel</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownValidationErrorType">Ett okänt fel har uppstått</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownMessageKey">Ett okänt fel har uppstått</xsl:variable>
	
	<xsl:variable name="i18n.FileSizeLimitExceeded">Maximal tillåten filstorlek för profilbild överskriden</xsl:variable>
	<xsl:variable name="i18n.UnableToParseRequest">Det gick inte att ladda upp profilbilden</xsl:variable>
	<xsl:variable name="i18n.UnableToParseProfileImage">Det gick inte att ladda upp profilbilden</xsl:variable>
	<xsl:variable name="i18n.UnableToDeleteProfileImage">Det gick inte att ta bort profilbilden, försök igen</xsl:variable>
	<xsl:variable name="i18n.InvalidProfileImageFileFormat">Otillåtet filformat på profilbilden. Tillåtna filtyper är png, jpg, gif och bmp.</xsl:variable>
	
	<xsl:variable name="i18n.UpdateResumeSettings">Inställningar för e-postresumé</xsl:variable>

	<xsl:variable name="i18n.NewPassword">Nytt lösenord</xsl:variable>
	<xsl:variable name="i18n.NewPasswordConfirmation">Nytt lösenord bekräftelse</xsl:variable>
	<xsl:variable name="i18n.ChangePasswordText">Skriv här om du vill ändra ditt lösenord</xsl:variable>
</xsl:stylesheet>
