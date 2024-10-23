<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="CombinedCalendarModuleTemplates.xsl" />
	
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.fi.xsl" />
	
	<xsl:variable name="updatePostBreadCrumb">Päivitä kalenteritapahtuma</xsl:variable>
	<xsl:variable name="readReceiptBreadCrumb">Kuittaus</xsl:variable>
	<xsl:variable name="addPostBreadCrumb">Lisää tapahtuma</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'för'" />
	<xsl:variable name="calendar.script.language" select="'CalendarLanguage.fi.js'" />
	
	<xsl:variable name="calendarmodule.information" select="'Lisätäksesi kalenteritapahtuman, tuplaklikkaa haluamaasi päivää ja klikkaa linkkiä'" />
	<xsl:variable name="calendarmodule.addlink.title" select="'Lisää tapahtuma'" />
	
	<xsl:variable name="addPost.header" select="'Lisää tapahtuma'" />
	<xsl:variable name="addPost.submit" select="'Lisää'" />
	<xsl:variable name="updatePost.header" select="'Muuta tapahtumaa'" />
	<xsl:variable name="updatePost.submit" select="'Tallenna muutokset'" />
	<xsl:variable name="post.date" select="'Päivämäärä'" />
	<xsl:variable name="post.starttime" select="'Aloitusaika'" />
	<xsl:variable name="post.endtime" select="'Lopetusaika'" />
	<xsl:variable name="post.time.description" select="'Alku- ja lopetusajat ovat avoimia'" />
	<xsl:variable name="post.publish" select="'Julkaise tapahtuma'" />
	<xsl:variable name="post.noaccess" select="'Sinulla ei ole pääsyä esikouluun'" />
	<xsl:variable name="post.name" select="'Nimi'" />
	<xsl:variable name="post.description" select="'Kuvaus'" />
	<xsl:variable name="post.back" select="'Takaisin'" />

	<xsl:variable name="schools.tree.title" select="'Esikoulu'" />

	<xsl:variable name="showPost.date.text" select="'den'" />
	<xsl:variable name="showPost.date.week" select="'viikko'" />
	<xsl:variable name="showPost.time" select="'Aika'" />
	<xsl:variable name="showPost.title" select="'Otsikko'" />
	<xsl:variable name="showPost.group" select="'Ryhmä'" />
	<xsl:variable name="showPost.all" select="'Samtliga förskolor'" />
	<xsl:variable name="showPost.publishedTo" select="'Shown for'" />
	<xsl:variable name="showPost.showreadreceipt.title" select="'Visa läskvitto för kalenderposten'" />
	<xsl:variable name="showPost.changepost.title" select="'Päivitä tapahtuma'" />
	<xsl:variable name="showPost.delete.confirm" select="'Haluatko varmasti poistaa tapahtuman'" />
	<xsl:variable name="showPost.delete.title" select="'Poista tapahtuma'" />
	<xsl:variable name="showPost.postedby" select="'Luonut'" />
	<xsl:variable name="showPost.deleteduser" select="'Poistettu käyttäjä'" />
	
	<xsl:variable name="showReadReceipt.header" select="'Läskvitto för kalenderposten'" />
	<xsl:variable name="showReadReceipt.summary.part1" select="'Totalt'" />
	<xsl:variable name="showReadReceipt.summary.part2" select="'användare har läst kalenderposten'" />
	<xsl:variable name="showReadReceipt.name" select="'Namn'" />
	<xsl:variable name="showReadReceipt.firstread" select="'Först läst'" />
	<xsl:variable name="showReadReceipt.lastread" select="'Senast läst'" />
	<xsl:variable name="showReadReceipt.noreceipt" select="'Ingen användare har läst kalenderposten'" />
	<xsl:variable name="showReadReceipt.back" select="'Tillbaka'" />
	
	<xsl:variable name="validationError.RequiredField" select="'Du måste fylla i fältet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt värde på fältet'" />
	<xsl:variable name="validationError.TooLong" select="'För stort värde på fältet'" />
	<xsl:variable name="validationError.TooShort" select="'Kentän sisältö on liian lyhyt'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Okänt fel på fältet'" />
	<xsl:variable name="validationError.field.name" select="'namn'" />
	<xsl:variable name="validationError.field.date" select="'datum'" />
	<xsl:variable name="validationError.field.description" select="'beskrivning'" />
	<xsl:variable name="validationError.messageKey.InvalidContent" select="'Ogiltiga tecken i innehållet'" />
	<xsl:variable name="validationError.messageKey.NoGroup" select="'Du måste välja minst en grupp att publicera till'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett okänt fel har uppstått'" />				

</xsl:stylesheet>
