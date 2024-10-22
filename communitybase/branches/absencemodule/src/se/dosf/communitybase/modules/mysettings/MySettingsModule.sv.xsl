<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="MySettingsModuleTemplates.xsl" />

	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="MySettingsModule.information1" select="'Välkommen till mina inställningar. Här kan du du ändra personuppgifter, e-postresume, e-postadress och lösenord'" />
	<xsl:variable name="MySettingsModule.information2" select="'E-postresume är ett e-postmeddelande som skickas varje dag med ett kort utdrag av vad som hänt sedan du senast var inloggad'" />
	<xsl:variable name="MySettingsModule.information3" select="'Observera att om du byter e-postadress eller lösenord så kommer det att påverka din inloggning i föräldramötet'" />
	<xsl:variable name="MySettingsModule.header" select="'Ändra inställningar och uppgifter'" />
	
	<xsl:variable name="user.personinfo" select="'Personuppgifter'" />
	<xsl:variable name="user.firstname" select="'Förnamn'" />
	<xsl:variable name="user.lastname" select="'Efternamn'" />
	<xsl:variable name="user.phoneHome" select="'Telefon hem'" />
	<xsl:variable name="user.phoneMobile" select="'Telefon mobil'" />
	<xsl:variable name="user.phoneWork" select="'Telefon arbete'" />
	<xsl:variable name="user.accountinfo" select="'Kontouppgifter'" />
	<xsl:variable name="user.email" select="'E-post'" />
	<xsl:variable name="user.changepassword" select="'Byt lösenord'" />	
	<xsl:variable name="user.newpassword" select="'Nytt lösenord'" />
	<xsl:variable name="user.minimumchars" select="'minst 6 tecken'" />	
	<xsl:variable name="user.confirmpassword" select="'Bekräfta lösenord'" />
	<xsl:variable name="user.emailresume.header" select="'E-post resumé'" />
	<xsl:variable name="user.emailresume.description" select="'Jag vill få e-postresumé varje dag klockan'" />	
	<xsl:variable name="user.submit" select="'Spara ändringar'" />	
		
	<xsl:variable name="validationError.RequiredField" select="'Du måste fylla i fältet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt format på fältet'" />
	<xsl:variable name="validationError.TooLong" select="'För långt innehåll i fältet'" />
	<xsl:variable name="validationError.TooShort" select="'För kort innehåll i fältet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Okänt fel på fältet'" />
	
	<xsl:variable name="validationError.field.email" select="'e-post'" />
	<xsl:variable name="validationError.field.password" select="'lösenord'" />
	<xsl:variable name="validationError.field.firstname" select="'förnamn'" />
	<xsl:variable name="validationError.field.lastname" select="'efternamn'" />
	<xsl:variable name="validationError.field.phoneHome" select="'telefon hem'" />
	<xsl:variable name="validationError.field.phoneWork" select="'telefon arbete'" />
	<xsl:variable name="validationError.field.phoneMobile" select="'telefon mobil'" />
	<xsl:variable name="validationError.field.authority" select="'behörighet'" />
	<xsl:variable name="validationError.messageKey.UpdateFailedUserNotFound" select="'Användaren du försöker uppdatera hittades inte'" />
	<xsl:variable name="validationError.messageKey.UserAlreadyExist" select="'Det finns redan en användare med den angivna e-postadressen'" />
	<xsl:variable name="validationError.messageKey.PasswordsDontMatch" select="'Lösenorden matchar inte'" />
	
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett okänt fel har uppstått'" />				

</xsl:stylesheet>