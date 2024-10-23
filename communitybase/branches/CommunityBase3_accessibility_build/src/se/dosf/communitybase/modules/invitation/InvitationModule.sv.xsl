<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:include href="InvitationModuleTemplates.xsl"/>
	
	<!-- Module XSL Variables -->
	<xsl:variable name="java.sectionInvitationMessage">$section.name med rollen $role.name</xsl:variable>
	
	<!-- Validation -->
	<xsl:variable name="i18n.PasswordDontMatch">De angivna lösenorden överensstämmer inte</xsl:variable>
	<xsl:variable name="i18n.InvalidPasswordStrength">Oj, du valde ett lite för enkelt eller kort lösenord. Ditt lösenord måste innehålla minst 8 tecken varav en siffra och en stor bokstav</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownMessageKey">Ett okänt fel har uppstått</xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded">Maximal tillåten filstorlek för profilbild överskriden</xsl:variable>
	<xsl:variable name="i18n.UnableToParseRequest">Det gick inte att ladda upp profilbilden</xsl:variable>
	<xsl:variable name="i18n.UnableToParseProfileImage">Det gick inte att ladda upp profilbilden</xsl:variable>
	<xsl:variable name="i18n.UnableToDeleteProfileImage">Det gick inte att ta bort profilbilden, försök igen</xsl:variable>
	<xsl:variable name="i18n.InvalidProfileImageFileFormat">Otillåtet filformat på profilbilden. Tillåtna filtyper är png, jpg, gif och bmp.</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.Firstname">Du måste ange ett förnamn</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.Lastname">Du måste ange ett efternamn</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.Email">Du måste ange en e-postadress</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.Phone">Du måste ange ett telefonnummer</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.MobilePhone">Du måste ange ett mobiltelefonnummer</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.Organization">Du måste ange en organisation</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.VerificationCode">Du måste ange en bekräftelsekod</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.Password">Du måste ange ett lösenord</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Firstname">Ett förnamn får maximalt innehålla 30 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Lastname">Ett efternamn får maximalt innehålla 50 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Email">En e-postadress får maximalt innehålla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.InvalidFormat.User.Email">En e-postadress måste anges i formatet avsändare@domän.se</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Phone">Ett telefonnummer får maximalt innehålla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Phone2">Ett telefonnummer får maximalt innehålla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.MobilePhone">Ett telefonnummer får maximalt innehålla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.VerificationCode">Bekräftelsekoden får maximalt innehålla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Organization">Namnet på organisationen får maximalt innehålla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Title">Titeln får maximalt innehålla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Password">Lösenordet får maximalt innehålla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.PasswordMatch">Du måste ange ett lösenord</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.PasswordMatch">Lösenordet får maximalt innehålla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Description">Beskrivningen får maximalt innehålla 4000 tecken</xsl:variable>
	<xsl:variable name="i18n.EmailAlreadyTaken">E-postadressen du har angett används redan av en annan användare</xsl:variable>
	
	<!-- Internationalization -->
	<xsl:variable name="i18n.Firstname">Förnamn</xsl:variable>
	<xsl:variable name="i18n.Lastname">Efternamn</xsl:variable>
	<xsl:variable name="i18n.Email">E-postadress</xsl:variable>
	<xsl:variable name="i18n.Phone">Telefon</xsl:variable>
	<xsl:variable name="i18n.Organization">Organisation</xsl:variable>
	<xsl:variable name="i18n.Title">Befattning</xsl:variable>
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.ChoosePassword">Välj lösenord</xsl:variable>
	<xsl:variable name="i18n.PasswordStrengthDescription">Minst 8 tecken varav en siffra och en stor bokstav</xsl:variable>
	<xsl:variable name="i18n.ConfirmPassword">Bekräfta lösenord</xsl:variable>
	<xsl:variable name="i18n.Save">Spara och gå vidare</xsl:variable>
	<xsl:variable name="i18n.ProfileImage">Profilbild</xsl:variable>
	<xsl:variable name="i18n.MaximumFileUpload">Maximal filstorlek vid uppladdning</xsl:variable>
	<xsl:variable name="i18n.MobilePhone">Mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.ValidationCode">Bekräftelsekod</xsl:variable>
	<xsl:variable name="i18n.SendVerificationCode">Skicka kod</xsl:variable>
	<xsl:variable name="i18n.WrongVerificationCode">Felaktig bekräftelsekod</xsl:variable>
	<xsl:variable name="i18n.InvalidPhoneNumber">Ogiltigt format på telefonnummer</xsl:variable>
	<xsl:variable name="i18n.CouldNotSendSMS">Kunde inte skicka bekräftelsekod</xsl:variable>
	<xsl:variable name="i18n.RegistrationTextNotApproved">Du måste godkänna användarvillkoren</xsl:variable>
	<xsl:variable name="i18n.Close">Stäng</xsl:variable>
	<xsl:variable name="i18n.RequiredFieldsAreMarked">Obligatoriska fält är markerade med en *</xsl:variable>

</xsl:stylesheet>
