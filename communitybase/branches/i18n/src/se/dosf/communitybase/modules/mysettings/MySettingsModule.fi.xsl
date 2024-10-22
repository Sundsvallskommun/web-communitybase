<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="MySettingsModuleTemplates.xsl" />

	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="MySettingsModule.information1" select="'Tervetuloa omiin asetuksiin. T��ll� voit muokata omia tietojasi, s�hk�postip�ivitysten asetuksia s�hk�postiosoitettasi tai salasanaasi'" />
	<xsl:variable name="MySettingsModule.information2" select="'S�hk�postip�ivitykset ovat s�hk�posteja, jotka l�hetet��n p�ivitt�in. Ne sis�lt�v�t lyhyen yhteenvedon tapahtumista viime k�yntisi j�lkeen'" />
	<xsl:variable name="MySettingsModule.information3" select="'Huomaa, ett� jos vaihdat s�hk�postiosoitteesi tai salasanasi Vanhempainkokouksessa, se muuttaa my�s kirjautumistietojasi'" />
	<xsl:variable name="MySettingsModule.header" select="'Muuta asetuksia ja tietoja'" />
	
	<xsl:variable name="user.personinfo" select="'Henkil�tiedot'" />
	<xsl:variable name="user.firstname" select="'Etunimi'" />
	<xsl:variable name="user.lastname" select="'Sukunimi'" />
	<xsl:variable name="user.phoneHome" select="'Puhelin (koti)'" />
	<xsl:variable name="user.phoneMobile" select="'Puhelin (matka)'" />
	<xsl:variable name="user.phoneWork" select="'Puhelin (ty�)'" />
	<xsl:variable name="user.accountinfo" select="'K�ytt�j�tunnuksen tiedot'" />
	<xsl:variable name="user.email" select="'S�hk�posti'" />
	<xsl:variable name="user.language" select="'Kieli'" />
	<xsl:variable name="user.changepassword" select="'Vaihda salasana'" />	
	<xsl:variable name="user.newpassword" select="'Uusi salasana'" />
	<xsl:variable name="user.minimumchars" select="'v�hint��n 6 merkki�'" />	
	<xsl:variable name="user.confirmpassword" select="'Varmista salasana'" />
	<xsl:variable name="user.emailresume.header" select="'S�hk�postip�ivitys'" />
	<xsl:variable name="user.emailresume.description" select="'Haluan vastaanottaa s�hk�postip�ivityksi� p�ivitt�in kello'" />
	<xsl:variable name="user.submit" select="'Tallenna asetukset'" />	
		
	<xsl:variable name="validationError.RequiredField" select="'Kentt� on pakollinen'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Osoitteen tulee alkaa http://, https:// tai ftp://'" />
	<xsl:variable name="validationError.TooLong" select="'Kent�n sis�lt� on liian pitk�'" />
	<xsl:variable name="validationError.TooShort" select="'Kent�n sis�lt� on liian lyhyt'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Tuntematon virhe kent�ss�'" />
	
	<xsl:variable name="validationError.field.email" select="'s�hk�posti'" />
	<xsl:variable name="validationError.field.password" select="'salasana'" />
	<xsl:variable name="validationError.field.firstname" select="'etunimi'" />
	<xsl:variable name="validationError.field.lastname" select="'sukunimi'" />
	<xsl:variable name="validationError.field.authority" select="'k�ytt�oikeus'" />
	<xsl:variable name="validationError.messageKey.UpdateFailedUserNotFound" select="'K�ytt�j�� jota yritit muokata, ei ole olemassa'" />
	<xsl:variable name="validationError.messageKey.UserAlreadyExist" select="'The given email address already exist'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Tuntematon virhe'" />

</xsl:stylesheet>
