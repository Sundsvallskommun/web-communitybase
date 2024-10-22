<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="MySettingsModuleTemplates.xsl" />

	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="MySettingsModule.information1" select="'V�lkommen till mina inst�llningar. H�r kan du du �ndra personuppgifter, e-postresume, e-postadress och l�senord'" />
	<xsl:variable name="MySettingsModule.information2" select="'E-postresume �r ett e-postmeddelande som skickas varje dag med ett kort utdrag av vad som h�nt sedan du senast var inloggad'" />
	<xsl:variable name="MySettingsModule.information3" select="'Observera att om du byter e-postadress eller l�senord s� kommer det att p�verka din inloggning i f�r�ldram�tet'" />
	<xsl:variable name="MySettingsModule.header" select="'�ndra inst�llningar och uppgifter'" />
	
	<xsl:variable name="user.personinfo" select="'Personuppgifter'" />
	<xsl:variable name="user.firstname" select="'F�rnamn'" />
	<xsl:variable name="user.lastname" select="'Efternamn'" />
	<xsl:variable name="user.phoneHome" select="'Telefon hem'" />
	<xsl:variable name="user.phoneMobile" select="'Telefon mobil'" />
	<xsl:variable name="user.phoneWork" select="'Telefon arbete'" />
	<xsl:variable name="user.accountinfo" select="'Kontouppgifter'" />
	<xsl:variable name="user.email" select="'E-post'" />
	<xsl:variable name="user.changepassword" select="'Byt l�senord'" />	
	<xsl:variable name="user.newpassword" select="'Nytt l�senord'" />
	<xsl:variable name="user.minimumchars" select="'minst 6 tecken'" />	
	<xsl:variable name="user.confirmpassword" select="'Bekr�fta l�senord'" />
	<xsl:variable name="user.emailresume.header" select="'E-post resum�'" />
	<xsl:variable name="user.emailresume.description" select="'Jag vill f� e-postresum� varje dag klockan'" />	
	<xsl:variable name="user.submit" select="'Spara �ndringar'" />	
		
	<xsl:variable name="validationError.RequiredField" select="'Du m�ste fylla i f�ltet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt format p� f�ltet'" />
	<xsl:variable name="validationError.TooLong" select="'F�r l�ngt inneh�ll i f�ltet'" />
	<xsl:variable name="validationError.TooShort" select="'F�r kort inneh�ll i f�ltet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Ok�nt fel p� f�ltet'" />
	
	<xsl:variable name="validationError.field.email" select="'e-post'" />
	<xsl:variable name="validationError.field.password" select="'l�senord'" />
	<xsl:variable name="validationError.field.firstname" select="'f�rnamn'" />
	<xsl:variable name="validationError.field.lastname" select="'efternamn'" />
	<xsl:variable name="validationError.field.phoneHome" select="'telefon hem'" />
	<xsl:variable name="validationError.field.phoneWork" select="'telefon arbete'" />
	<xsl:variable name="validationError.field.phoneMobile" select="'telefon mobil'" />
	<xsl:variable name="validationError.field.authority" select="'beh�righet'" />
	<xsl:variable name="validationError.messageKey.UpdateFailedUserNotFound" select="'Anv�ndaren du f�rs�ker uppdatera hittades inte'" />
	<xsl:variable name="validationError.messageKey.UserAlreadyExist" select="'Det finns redan en anv�ndare med den angivna e-postadressen'" />
	<xsl:variable name="validationError.messageKey.PasswordsDontMatch" select="'L�senorden matchar inte'" />
	
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett ok�nt fel har uppst�tt'" />				

</xsl:stylesheet>