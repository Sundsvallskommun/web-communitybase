<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:include href="InvitationModuleTemplates.xsl"/>
	
	<xsl:variable name="java.sectionInvitationMessage">$section.name med rollen $role.name</xsl:variable>

	<xsl:variable name="i18n.Firstname">F�rnamn</xsl:variable>
	<xsl:variable name="i18n.Lastname">Efternamn</xsl:variable>
	<xsl:variable name="i18n.Email">E-postadress</xsl:variable>
	<xsl:variable name="i18n.Phone">Telefon</xsl:variable>
	<xsl:variable name="i18n.Organization">Organisation</xsl:variable>
	<xsl:variable name="i18n.Title">Befattning</xsl:variable>
	
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.ChoosePassword">V�lj l�senord</xsl:variable>
	<xsl:variable name="i18n.PasswordStrengthDescription">Minst 8 tecken varav en siffra och en stor bokstav</xsl:variable>
	<xsl:variable name="i18n.ConfirmPassword">Bekr�fta l�senord</xsl:variable>
	<xsl:variable name="i18n.Save">Spara och g� vidare</xsl:variable>
	
	<xsl:variable name="i18n.PasswordDontMatch">De angivna l�senorden �verensst�mmer inte</xsl:variable>
	<xsl:variable name="i18n.InvalidPasswordStrength">Oj, du valde ett lite f�r enkelt eller kort l�senord. Ditt l�senord m�ste inneh�lla minst 8 tecken varav en siffra och en stor bokstav</xsl:variable>

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
	
	<xsl:variable name="i18n.ProfileImage">Profilbild</xsl:variable>
	
	<xsl:variable name="i18n.MaximumFileUpload">Maximal filstorlek vid uppladdning</xsl:variable>
	<xsl:variable name="i18n.MobilePhone">Mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.ValidationCode">Bekr�ftelsekod</xsl:variable>
	<xsl:variable name="i18n.SendVerificationCode">Skicka kod</xsl:variable>
	<xsl:variable name="i18n.WrongVerificationCode">Felaktig bekr�ftelsekod</xsl:variable>
	<xsl:variable name="i18n.InvalidPhoneNumber">Ogiltigt format p� telefonnummer</xsl:variable>
	<xsl:variable name="i18n.CouldNotSendSMS">Kunde inte skicka bekr�ftelsekod</xsl:variable>
	<xsl:variable name="i18n.RegistrationTextNotApproved">Du m�ste godk�nna anv�ndarvillkoren</xsl:variable>
	<xsl:variable name="i18n.ReadMore">L�s mer</xsl:variable>
	<xsl:variable name="i18n.Close">St�ng</xsl:variable>
</xsl:stylesheet>
