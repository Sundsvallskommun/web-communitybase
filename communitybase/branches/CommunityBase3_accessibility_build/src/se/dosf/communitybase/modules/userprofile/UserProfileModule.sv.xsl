<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:include href="UserProfileModuleTemplates.xsl"/>
	
	<!-- Validation -->
	<xsl:variable name="i18n.validationError.Required.User.Firstname">Du måste ange ett förnamn</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.Lastname">Du måste ange ett efternamn</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.Email">Du måste ange en e-postadress</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.Phone">Du måste ange ett telefonnummer</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.MobilePhone">Du måste ange ett mobiltelefonnummer</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.Organization">Du måste ange en organisation</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Firstname">Ett förnamn får maximalt innehålla 30 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Lastname">Ett efternamn får maximalt innehålla 50 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Email">En e-postadress får maximalt innehålla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.InvalidFormat.User.Email">En e-postadress måste anges i formatet avsändare@domän.se</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Phone">Ett telefonnummer får maximalt innehålla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Phone2">Ett telefonnummer får maximalt innehålla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.MobilePhone">Ett telefonnummer får maximalt innehålla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.VerificationCode">Du måste ange en bekräftelsekod</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.VerificationCode">Bekräftelsekoden får maximalt innehålla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Organization">Namnet på organisationen får maximalt innehålla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Title">Titeln får maximalt innehålla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.Password">Du måste ange ett lösenord</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Password">Lösenordet får maximalt innehålla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.PasswordMatch">Du måste ange ett lösenord</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.PasswordMatch">Lösenordet får maximalt innehålla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Description">Beskrivningen får maximalt innehålla 4000 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownMessageKey">Ett okänt fel har uppstått</xsl:variable>
	
	<!-- Internationalization -->
	<xsl:variable name="i18n.Firstname">Förnamn</xsl:variable>
	<xsl:variable name="i18n.Lastname">Efternamn</xsl:variable>
	<xsl:variable name="i18n.Email">E-postadress</xsl:variable>
	<xsl:variable name="i18n.Phone">Telefon</xsl:variable>
	<xsl:variable name="i18n.Organization">Organisation</xsl:variable>
	<xsl:variable name="i18n.Title">Befattning</xsl:variable>
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
	<xsl:variable name="i18n.FileSizeLimitExceeded">Maximal tillåten filstorlek för profilbild överskriden</xsl:variable>
	<xsl:variable name="i18n.UnableToParseRequest">Det gick inte att ladda upp profilbilden</xsl:variable>
	<xsl:variable name="i18n.UnableToParseProfileImage">Det gick inte att ladda upp profilbilden</xsl:variable>
	<xsl:variable name="i18n.UnableToDeleteProfileImage">Det gick inte att ta bort profilbilden, försök igen</xsl:variable>
	<xsl:variable name="i18n.InvalidProfileImageFileFormat">Otillåtet filformat på profilbilden. Tillåtna filtyper är png, jpg, gif och bmp.</xsl:variable>
	<xsl:variable name="i18n.UpdateResumeSettings">Inställningar för e-postresumé</xsl:variable>
	<xsl:variable name="i18n.NewPassword">Nytt lösenord</xsl:variable>
	<xsl:variable name="i18n.NewPasswordConfirmation">Nytt lösenord bekräftelse</xsl:variable>
	<xsl:variable name="i18n.ChangePasswordText">Skriv här om du vill ändra ditt lösenord</xsl:variable>
	<xsl:variable name="i18n.EmailAlreadyTaken">E-postadressen används redan av en annan användarprofil</xsl:variable>
	<xsl:variable name="i18n.MobilePhone">Mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.InvalidPhoneNumber">Ogiltigt format på telefonnummer</xsl:variable>
	<xsl:variable name="i18n.CouldNotSendSMS">Kunde inte skicka bekräftelsekod</xsl:variable>
	<xsl:variable name="i18n.SendVerificationCode">Skicka kod</xsl:variable>
	<xsl:variable name="i18n.ValidationCode">Bekräftelsekod</xsl:variable>
	<xsl:variable name="i18n.ChangeMobilePhone">Byt mobiltelefonnummer</xsl:variable>
	<xsl:variable name="i18n.WrongVerificationCode">Felaktig bekräftelsekod</xsl:variable>
	<xsl:variable name="i18n.UseArrowsToMoveImage">Använd piltangenterna för att flytta bilden i beskärningsfönstret</xsl:variable>
	<xsl:variable name="i18n.UseArrowsToAdjustZoom">Använd piltangenterna för att förstora eller förminska bilden i beskärningsfönstret</xsl:variable>
	<xsl:variable name="i18n.RequiredFieldsAreMarked">Obligatoriska fält är markerade med en *</xsl:variable>

</xsl:stylesheet>
