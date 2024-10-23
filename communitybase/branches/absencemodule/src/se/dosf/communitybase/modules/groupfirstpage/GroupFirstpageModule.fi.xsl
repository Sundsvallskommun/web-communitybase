<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="GroupFirstpageModuleTemplates.xsl" />
	
	<xsl:variable name="newFirstpageText">Uusi etusivu: </xsl:variable>
	<xsl:variable name="noFirstpageText">Ei etusivua</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'f�r'" />
	<xsl:variable name="groupFirstpageModule.delete.title" select="'Poista ryhm�n etusivu'" />
	<xsl:variable name="groupFirstpageModule.delete.confirm" select="'Haluatko varmasti poistaa ryhm�n etusivun'" />
	<xsl:variable name="groupFirstpageModule.update.title" select="'Muokkaa ryhm�n etusivua'" />
	<xsl:variable name="groupFirstpageModule.lastupdated" select="'Sivu viimeksi p�ivitetty'" />
	
	<xsl:variable name="updatefirstpage.header" select="'Muokkaa ryhm�n etusivua'" />
	<xsl:variable name="updatefirstpage.title" select="'Otsikko'" />
	<xsl:variable name="updatefirstpage.content" select="'Sis�lt�'" />
	<xsl:variable name="updatefirstpage.changeimage" select="'Vaihda kuva'" />
	<xsl:variable name="updatefirstpage.uploadimage" select="'L�het� kuva'" />
	<xsl:variable name="updatefirstpage.notrequired" select="'Vapaaehtoinen'" />
	<xsl:variable name="updatefirstpage.newimage" select="'Sijoita uusi kuva'" />
	<xsl:variable name="updatefirstpage.imagelocation" select="'Sijoita kuva'" />
	<xsl:variable name="updatefirstpage.imagelocation.above" select="'Sis�llysluettelon yl�puolella'" />
	<xsl:variable name="updatefirstpage.imagelocation.below" select="'Sis�llysluettelon alapuolella'" />
	<xsl:variable name="updatefirstpage.currentimage.imagelocation" select="'Sijoita nykyinen kuva'" />
	<xsl:variable name="updatefirstpage.currentimage.delete" select="'Poista nykyinen kuva'" />
	<xsl:variable name="updatefirstpage.currentimage.save" select="'Save changes'" />
		
	<xsl:variable name="validationError.RequiredField" select="'Kentt� on pakollinen'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Kent�n sis�lt� ei vastaa vaadittua muotoa'" />
	<xsl:variable name="validationError.TooLong" select="'Kent�n sis�lt� on liian pitk�'" />
	<xsl:variable name="validationError.Other" select="'Polku ei ole kelvollinen, muokkaa kentt��'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Tuntematon virhe kent�ss�'" />
	<xsl:variable name="validationError.field.name" select="'nimi'" />
	<xsl:variable name="validationError.field.title" select="'otsikko'" />
	<xsl:variable name="validationError.field.description" select="'kuvaus'" />
	<xsl:variable name="validationError.field.commentText" select="'kommentti'" />
	<xsl:variable name="validationError.field.url" select="'hakupolku'" />
	<xsl:variable name="validationError.field.text" select="'sis�lt�'" />
	<xsl:variable name="validationError.messageKey.InvalidContent" select="'Ep�kelpoja merkkej� sis�ll�ss�'" />
	<xsl:variable name="validationError.messageKey.BadFileFormat" select="'Ep�kelpo tiedostomuoto'" />
	<xsl:variable name="validationError.messageKey.NoFile" select="'Et ole valinnut kuvaa'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Tuntematon virhe'" />				

</xsl:stylesheet>
