<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="RequestPasswordModuleTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="RequestNewPassword.header" select="'Gl�mt ditt l�senord'" />
	<xsl:variable name="RequestNewPassword.information" select="'Fyll i din e-postadress i formul�ret nedan och klicka p� &quot;Beg�r nytt l�senord&quot; s� skickas ett nytt l�senord till dig'" />
	<xsl:variable name="RequestNewPassword.email" select="'E-postadess'" />
	<xsl:variable name="RequestNewPassword.submit" select="'Beg�r nytt l�senord'" />
	
	<xsl:variable name="NewPasswordSent.header" select="'Gl�mt ditt l�senord'" />
	<xsl:variable name="NewPasswordSent.notificationmessage" select="'Ett nytt l�senord har skickats till din e-postadress'" />
	<xsl:variable name="NewPasswordSent.back" select="'Tillbaka till inloggningssidan'" />
	
	<xsl:variable name="RequestNewPasswordEmail.header" select="'H�r kommer ett nytt l�senord f�r att logga in p� F�r�ldram�tet'" />
	<xsl:variable name="RequestNewPasswordEmail.newpassword" select="'Nytt l�senord'" />
	<xsl:variable name="RequestNewPasswordEmail.information" select="'Du kan byta l�senord om du vill n�r du loggat in p� webbplatsen. Du g�r detta genom att klicka p� &quot;Mina inst�llningar&quot;'" />
	<xsl:variable name="RequestNewPasswordEmail.about.part1" select="'Vill du veta mer om tankarna bakom F�r�ldram�tet kan du l�sa om dem p�'" />
	<xsl:variable name="RequestNewPasswordEmail.about.part2" select="'F�r�ldram�tet'" />
	<xsl:variable name="RequestNewPasswordEmail.municipalitylink" select="'L�nk till'" />
	
	<xsl:variable name="validationError.RequiredField" select="'Du m�ste fylla i f�ltet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt v�rde p� f�ltet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Ok�nt fel p� f�ltet'" />
	<xsl:variable name="validationError.field.email" select="'e-post'" />
	<xsl:variable name="validationError.messageKey.UserNotFound" select="'E-post adressen du angivit existerar inte i f�r�ldram�tet'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett ok�nt fel har uppst�tt'" />				

</xsl:stylesheet>