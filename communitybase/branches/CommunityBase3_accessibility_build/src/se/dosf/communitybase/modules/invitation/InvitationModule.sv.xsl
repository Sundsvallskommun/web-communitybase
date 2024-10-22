<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>
	
	<xsl:include href="InvitationModuleTemplates.xsl"/>
	
	<!-- Module XSL Variables -->
	<xsl:variable name="java.sectionInvitationMessage">$section.name med rollen $role.name</xsl:variable>
	
	<!-- Validation -->
	<xsl:variable name="i18n.PasswordDontMatch">De angivna l�senorden �verensst�mmer inte</xsl:variable>
	<xsl:variable name="i18n.InvalidPasswordStrength">Oj, du valde ett lite f�r enkelt eller kort l�senord. Ditt l�senord m�ste inneh�lla minst 8 tecken varav en siffra och en stor bokstav</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownMessageKey">Ett ok�nt fel har uppst�tt</xsl:variable>
	<xsl:variable name="i18n.FileSizeLimitExceeded">Maximal till�ten filstorlek f�r profilbild �verskriden</xsl:variable>
	<xsl:variable name="i18n.UnableToParseRequest">Det gick inte att ladda upp profilbilden</xsl:variable>
	<xsl:variable name="i18n.UnableToParseProfileImage">Det gick inte att ladda upp profilbilden</xsl:variable>
	<xsl:variable name="i18n.UnableToDeleteProfileImage">Det gick inte att ta bort profilbilden, f�rs�k igen</xsl:variable>
	<xsl:variable name="i18n.InvalidProfileImageFileFormat">Otill�tet filformat p� profilbilden. Till�tna filtyper �r png, jpg, gif och bmp.</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.Firstname">Du m�ste ange ett f�rnamn</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.Lastname">Du m�ste ange ett efternamn</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.Email">Du m�ste ange en e-postadress</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.Phone">Du m�ste ange ett telefonnummer</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.MobilePhone">Du m�ste ange ett mobiltelefonnummer</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.Organization">Du m�ste ange en organisation</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.VerificationCode">Du m�ste ange en bekr�ftelsekod</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.Password">Du m�ste ange ett l�senord</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Firstname">Ett f�rnamn f�r maximalt inneh�lla 30 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Lastname">Ett efternamn f�r maximalt inneh�lla 50 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Email">En e-postadress f�r maximalt inneh�lla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.InvalidFormat.User.Email">En e-postadress m�ste anges i formatet avs�ndare@dom�n.se</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Phone">Ett telefonnummer f�r maximalt inneh�lla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Phone2">Ett telefonnummer f�r maximalt inneh�lla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.MobilePhone">Ett telefonnummer f�r maximalt inneh�lla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.VerificationCode">Bekr�ftelsekoden f�r maximalt inneh�lla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Organization">Namnet p� organisationen f�r maximalt inneh�lla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Title">Titeln f�r maximalt inneh�lla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Password">L�senordet f�r maximalt inneh�lla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.Required.User.PasswordMatch">Du m�ste ange ett l�senord</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.PasswordMatch">L�senordet f�r maximalt inneh�lla 255 tecken</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong.User.Description">Beskrivningen f�r maximalt inneh�lla 4000 tecken</xsl:variable>
	<xsl:variable name="i18n.EmailAlreadyTaken">E-postadressen du har angett anv�nds redan av en annan anv�ndare</xsl:variable>
	
	<!-- Internationalization -->
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
	<xsl:variable name="i18n.ProfileImage">Profilbild</xsl:variable>
	<xsl:variable name="i18n.MaximumFileUpload">Maximal filstorlek vid uppladdning</xsl:variable>
	<xsl:variable name="i18n.MobilePhone">Mobiltelefon</xsl:variable>
	<xsl:variable name="i18n.ValidationCode">Bekr�ftelsekod</xsl:variable>
	<xsl:variable name="i18n.SendVerificationCode">Skicka kod</xsl:variable>
	<xsl:variable name="i18n.WrongVerificationCode">Felaktig bekr�ftelsekod</xsl:variable>
	<xsl:variable name="i18n.InvalidPhoneNumber">Ogiltigt format p� telefonnummer</xsl:variable>
	<xsl:variable name="i18n.CouldNotSendSMS">Kunde inte skicka bekr�ftelsekod</xsl:variable>
	<xsl:variable name="i18n.RegistrationTextNotApproved">Du m�ste godk�nna anv�ndarvillkoren</xsl:variable>
	<xsl:variable name="i18n.Close">St�ng</xsl:variable>
	<xsl:variable name="i18n.RequiredFieldsAreMarked">Obligatoriska f�lt �r markerade med en *</xsl:variable>

</xsl:stylesheet>
