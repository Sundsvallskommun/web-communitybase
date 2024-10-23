<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="NewsLetterModuleTemplates.xsl" />
	
	<xsl:variable name="newNewsLetterText">Uusi uutiskirje: </xsl:variable>
	<xsl:variable name="updateNewsletterBreadCrumb">Muokkaa uutiskirjettä</xsl:variable>
	<xsl:variable name="readReceiptBreadCrumb">Kuittaa luetuksi</xsl:variable>
	<xsl:variable name="addNewsletterBreadCrumb">Lisää uutiskirje</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'för'" />
	<xsl:variable name="newslettermodule.noNewsletters.header" select="'Ei uutiskirjeitä'" />
	<xsl:variable name="newslettermodule.readreceipt.title" select="'Kuittaa luetuksi'" />
	<xsl:variable name="newslettermodule.update.title" select="'Muokkaa uutiskirjettä'" />
	<xsl:variable name="newslettermodule.delete.title" select="'Poista uutiskirje'" />
	<xsl:variable name="newslettermodule.delete.confirm" select="'Haluatko varmasti poistaa uutiskirjeen'" />
	<xsl:variable name="newslettermodule.postedBy" select="'Lähettänyt'" />
	<xsl:variable name="newslettermodule.deletedUser" select="'Poistettu käyttäjä'" />
	<xsl:variable name="newslettermodule.noNewsletters" select="'Uutiskirjeitä ei ole'" />	
	<xsl:variable name="newslettermodule.submit" select="'Lisää uutiskirje'" />	
	<xsl:variable name="newslettermodule.moreNewsletters" select="'Lisää uutiskirjeitä'" />	
	<xsl:variable name="newslettermodule.information" select="'Vaihtaaksesi uutiskirjeiden välillä, tuplaklikkaa haluamaasi uutiskirjettä, tai merkitse ja klikkaa &quot;Näytä uutiskirje&quot;'" />
	<xsl:variable name="newslettermodule.showNewsletter" select="'Näytä uutiskirje'" />
	
	<xsl:variable name="newsletter.title" select="'Otsikko'" />
	<xsl:variable name="newsletter.content" select="'Sisältö'" />
	<xsl:variable name="newsletter.imagelocation" select="'Sijoita kuva'" />
	<xsl:variable name="newsletter.imagelocation.above" select="'Sisällön yläpuolella'" />
	<xsl:variable name="newsletter.imagelocation.below" select="'Sisällön alapuolella'" />
	<xsl:variable name="newsletter.notrequired" select="'Vapaaehtoinen'" />
	
	<xsl:variable name="addNewsletter.header" select="'Lisää uutiskirje'" />
	<xsl:variable name="addNewsletter.uploadimage" select="'Lähetä kuva'" />
	<xsl:variable name="addNewsletter.submit" select="'Lisää uutiskirje'" />
	
	<xsl:variable name="updateNewsletter.header" select="'Muokkaa uutiskirjettä'" />
	<xsl:variable name="updateNewsletter.changeimage" select="'Vaihda kuva'" />
	<xsl:variable name="updateNewsletter.newimage" select="'Sijoita kuva'" />
	<xsl:variable name="updateNewsletter.currentimage.imagelocation" select="'Sijoita nykyinen kuva'" />
	<xsl:variable name="updateNewsletter.currentimage.delete" select="'Poista nykyinen kuva'" />
	<xsl:variable name="updateNewsletter.submit" select="'Tallenna muutokset'" />
	
	<xsl:variable name="showReadReceipt.header" select="'Kuittaa luetuksi'" />
	<xsl:variable name="showReadReceipt.summary.part1" select="'Yhteensä'" />
	<xsl:variable name="showReadReceipt.summary.part2" select="'käyttäjää ovat lukeneet uutiskirjeen'" />
	<xsl:variable name="showReadReceipt.name" select="'Nimi'" />
	<xsl:variable name="showReadReceipt.firstread" select="'Ensin luettu'" />
	<xsl:variable name="showReadReceipt.lastread" select="'Viimeksi luettu'" />
	<xsl:variable name="showReadReceipt.noreceipt" select="'Kukaan ei ole lukenut uutiskirjettä'" />
	
	<xsl:variable name="validationError.RequiredField" select="'Kenttä on pakollinen'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Kentän sisältö ei vastaa vaadittua muotoa'" />
	<xsl:variable name="validation.TooShort" select="'Kentän sisältö on liian lyhyt'" />
	<xsl:variable name="validation.TooLong" select="'Kentän sisältö on liian pitkä'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Tuntematon virhe kentässä'" />
	<xsl:variable name="validationError.Other" select="'Hakupolku ei kelpaa, muokkaa kenttää'" />
	
	<xsl:variable name="validationError.field.title" select="'otsikko'" />
	<xsl:variable name="validationError.field.description" select="'kuvaus'" />
	<xsl:variable name="validationError.field.commentText" select="'kommentti'" />
	<xsl:variable name="validationError.field.url" select="'polku'" />
	<xsl:variable name="validationError.field.content" select="'sisältö'" />
	
	<xsl:variable name="validationError.messageKey.BadFileFormat" select="'Tiedostomuoto ei kelpaa'" />
	<xsl:variable name="validationError.messageKey.NoFile" select="'Et ole valinnut kuvaa'" />
	<xsl:variable name="validationError.messageKey.InvalidContent" select="'Virheellisiä merkkejä sisällössä'" />
	<xsl:variable name="validationError.messageKey.UnableToParseRequest" select="'Kuvan lähetyksessä tapahtui virhe'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Tapahtui tuntematon virhe'" />

</xsl:stylesheet>
