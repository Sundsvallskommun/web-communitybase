<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="RequestPasswordModuleTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="RequestNewPassword.header" select="'Glömt ditt lösenord'" />
	<xsl:variable name="RequestNewPassword.information" select="'Fyll i din e-postadress i formuläret nedan och klicka på &quot;Begär nytt lösenord&quot; så skickas ett nytt lösenord till dig'" />
	<xsl:variable name="RequestNewPassword.email" select="'E-postadess'" />
	<xsl:variable name="RequestNewPassword.submit" select="'Begär nytt lösenord'" />
	
	<xsl:variable name="NewPasswordSent.header" select="'Glömt ditt lösenord'" />
	<xsl:variable name="NewPasswordSent.notificationmessage" select="'Ett nytt lösenord har skickats till din e-postadress'" />
	<xsl:variable name="NewPasswordSent.back" select="'Tillbaka till inloggningssidan'" />
	
	<xsl:variable name="RequestNewPasswordEmail.header" select="'Här kommer ett nytt lösenord för att logga in på Föräldramötet'" />
	<xsl:variable name="RequestNewPasswordEmail.newpassword" select="'Nytt lösenord'" />
	<xsl:variable name="RequestNewPasswordEmail.information" select="'Du kan byta lösenord om du vill när du loggat in på webbplatsen. Du gör detta genom att klicka på &quot;Mina inställningar&quot;'" />
	<xsl:variable name="RequestNewPasswordEmail.about.part1" select="'Vill du veta mer om tankarna bakom Föräldramötet kan du läsa om dem på'" />
	<xsl:variable name="RequestNewPasswordEmail.about.part2" select="'Föräldramötet'" />
	<xsl:variable name="RequestNewPasswordEmail.municipalitylink" select="'Länk till'" />
	
	<xsl:variable name="validationError.RequiredField" select="'Du måste fylla i fältet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt värde på fältet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Okänt fel på fältet'" />
	<xsl:variable name="validationError.field.email" select="'e-post'" />
	<xsl:variable name="validationError.messageKey.UserNotFound" select="'E-post adressen du angivit existerar inte i föräldramötet'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett okänt fel har uppstått'" />				

</xsl:stylesheet>