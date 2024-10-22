<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="MySettingsModuleTemplates.xsl" />

	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="MySettingsModule.information1" select="'Tervetuloa omiin asetuksiin. Täällä voit muokata omia tietojasi, sähköpostipäivitysten asetuksia sähköpostiosoitettasi tai salasanaasi'" />
	<xsl:variable name="MySettingsModule.information2" select="'Sähköpostipäivitykset ovat sähköposteja, jotka lähetetään päivittäin. Ne sisältävät lyhyen yhteenvedon tapahtumista viime käyntisi jälkeen'" />
	<xsl:variable name="MySettingsModule.information3" select="'Huomaa, että jos vaihdat sähköpostiosoitteesi tai salasanasi Vanhempainkokouksessa, se muuttaa myös kirjautumistietojasi'" />
	<xsl:variable name="MySettingsModule.header" select="'Muuta asetuksia ja tietoja'" />
	
	<xsl:variable name="user.personinfo" select="'Henkilötiedot'" />
	<xsl:variable name="user.firstname" select="'Etunimi'" />
	<xsl:variable name="user.lastname" select="'Sukunimi'" />
	<xsl:variable name="user.phoneHome" select="'Puhelin (koti)'" />
	<xsl:variable name="user.phoneMobile" select="'Puhelin (matka)'" />
	<xsl:variable name="user.phoneWork" select="'Puhelin (työ)'" />
	<xsl:variable name="user.accountinfo" select="'Käyttäjätunnuksen tiedot'" />
	<xsl:variable name="user.email" select="'Sähköposti'" />
	<xsl:variable name="user.language" select="'Kieli'" />
	<xsl:variable name="user.changepassword" select="'Vaihda salasana'" />	
	<xsl:variable name="user.newpassword" select="'Uusi salasana'" />
	<xsl:variable name="user.minimumchars" select="'vähintään 6 merkkiä'" />	
	<xsl:variable name="user.confirmpassword" select="'Varmista salasana'" />
	<xsl:variable name="user.emailresume.header" select="'Sähköpostipäivitys'" />
	<xsl:variable name="user.emailresume.description" select="'Haluan vastaanottaa sähköpostipäivityksiä päivittäin kello'" />
	<xsl:variable name="user.submit" select="'Tallenna asetukset'" />	
		
	<xsl:variable name="validationError.RequiredField" select="'Kenttä on pakollinen'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Osoitteen tulee alkaa http://, https:// tai ftp://'" />
	<xsl:variable name="validationError.TooLong" select="'Kentän sisältö on liian pitkä'" />
	<xsl:variable name="validationError.TooShort" select="'Kentän sisältö on liian lyhyt'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Tuntematon virhe kentässä'" />
	
	<xsl:variable name="validationError.field.email" select="'sähköposti'" />
	<xsl:variable name="validationError.field.password" select="'salasana'" />
	<xsl:variable name="validationError.field.firstname" select="'etunimi'" />
	<xsl:variable name="validationError.field.lastname" select="'sukunimi'" />
	<xsl:variable name="validationError.field.authority" select="'käyttöoikeus'" />
	<xsl:variable name="validationError.messageKey.UpdateFailedUserNotFound" select="'Käyttäjää jota yritit muokata, ei ole olemassa'" />
	<xsl:variable name="validationError.messageKey.UserAlreadyExist" select="'The given email address already exist'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Tuntematon virhe'" />

</xsl:stylesheet>
