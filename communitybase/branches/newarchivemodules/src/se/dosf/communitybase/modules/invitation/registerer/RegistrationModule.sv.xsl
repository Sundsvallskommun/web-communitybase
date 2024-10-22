<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="RegistrationModuleTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="registered.header" select="'Registreringen slutförd'" />
	<xsl:variable name="registered.text" select="'Din registrering är nu slutförd och du kan nu logga in i Föräldramötet genom att klicka på &quot;Logga in&quot; länken som du finner i övre högra hörnet på sidan'" />
	
	<xsl:variable name="register.header" select="'Välkommen till Föräldramötet'" />	
	<xsl:variable name="register.text" select="'Du har blivit inbjuden till Föräldramötet, en internetportal där du får tillgång till information om dina barns skolgång dygnet runt, året runt. För att kunna använda denna tjänst måste du först registrera dig, via formuläret nedan'" />	
	<xsl:variable name="register.rules.header" select="'Förhållningsregler'" />

	<!-- Important to hold the format in this variable -->
	<xsl:variable name="register.rules.text" />
	
	<xsl:variable name="register.confirm" select="'Jag har tagit del av förhållningsreglerna och godkänner dessa'" />
	<xsl:variable name="register.information" select="'För att kunna logga in i föräldramötet måste du välja ett lösenord. Du måste även fylla i för- och efternamn, övriga fält är valfria'" />
	<xsl:variable name="register.email" select="'Ditt användarnamn är samma som din E-postadress'" />
	<xsl:variable name="register.firstname" select="'Förnamn'" />
	<xsl:variable name="register.lastname" select="'Efternamn'" />
	<xsl:variable name="register.password" select="'Lösenord (minst 6 tecken)'" />
	<xsl:variable name="register.confirmpassword" select="'Bekräfta lösenord'" />
	<xsl:variable name="register.phonehome" select="'Telefon hem'" />
	<xsl:variable name="register.phonemobile" select="'Telefon mobil'" />
	<xsl:variable name="register.phonework" select="'Telefon arbete'" />
	<xsl:variable name="register.submit" select="'Skapa konto'" />
	
	
	<xsl:variable name="validationError.RequiredField" select="'Du måste fylla i fältet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt format på fältet'" />
	<xsl:variable name="validationError.TooLong" select="'För långt innehåll i fältet'" />
	<xsl:variable name="validationError.TooShort" select="'För kort innehåll i fältet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Okänt fel på fältet'" />
	
	<xsl:variable name="validationError.field.firstname" select="'förnamn'" />
	<xsl:variable name="validationError.field.lastname" select="'efternamn'" />
	<xsl:variable name="validationError.field.password" select="'lösenord'" />
	<xsl:variable name="validationError.field.commentText" select="'kommentar'" />
	<xsl:variable name="validationError.field.url" select="'sökväg'" />
	<xsl:variable name="validationError.field.text" select="'innehåll'" />
	
	<xsl:variable name="validationError.messageKey.license" select="'Du har inte godkänt förhållningsreglerna'" />
	<xsl:variable name="validationError.messageKey.passwordsDontMatch" select="'Lösenorden överenstämmer inte'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett okänt fel har uppstått'" />				

</xsl:stylesheet>