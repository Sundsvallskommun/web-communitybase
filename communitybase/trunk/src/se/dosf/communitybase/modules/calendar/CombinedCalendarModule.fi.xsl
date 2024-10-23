<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="CombinedCalendarModuleTemplates.xsl" />
	
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.fi.xsl" />
	
	<xsl:variable name="updatePostBreadCrumb">P�ivit� kalenteritapahtuma</xsl:variable>
	<xsl:variable name="readReceiptBreadCrumb">Kuittaus</xsl:variable>
	<xsl:variable name="addPostBreadCrumb">Lis�� tapahtuma</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'f�r'" />
	<xsl:variable name="calendar.script.language" select="'CalendarLanguage.fi.js'" />
	
	<xsl:variable name="calendarmodule.information" select="'Lis�t�ksesi kalenteritapahtuman, tuplaklikkaa haluamaasi p�iv�� ja klikkaa linkki�'" />
	<xsl:variable name="calendarmodule.addlink.title" select="'Lis�� tapahtuma'" />
	
	<xsl:variable name="addPost.header" select="'Lis�� tapahtuma'" />
	<xsl:variable name="addPost.submit" select="'Lis��'" />
	<xsl:variable name="updatePost.header" select="'Muuta tapahtumaa'" />
	<xsl:variable name="updatePost.submit" select="'Tallenna muutokset'" />
	<xsl:variable name="post.date" select="'P�iv�m��r�'" />
	<xsl:variable name="post.starttime" select="'Aloitusaika'" />
	<xsl:variable name="post.endtime" select="'Lopetusaika'" />
	<xsl:variable name="post.time.description" select="'Alku- ja lopetusajat ovat avoimia'" />
	<xsl:variable name="post.publish" select="'Julkaise tapahtuma'" />
	<xsl:variable name="post.noaccess" select="'Sinulla ei ole p��sy� esikouluun'" />
	<xsl:variable name="post.name" select="'Nimi'" />
	<xsl:variable name="post.description" select="'Kuvaus'" />
	<xsl:variable name="post.back" select="'Takaisin'" />

	<xsl:variable name="schools.tree.title" select="'Esikoulu'" />

	<xsl:variable name="showPost.date.text" select="'den'" />
	<xsl:variable name="showPost.date.week" select="'viikko'" />
	<xsl:variable name="showPost.time" select="'Aika'" />
	<xsl:variable name="showPost.title" select="'Otsikko'" />
	<xsl:variable name="showPost.group" select="'Ryhm�'" />
	<xsl:variable name="showPost.all" select="'Samtliga f�rskolor'" />
	<xsl:variable name="showPost.publishedTo" select="'Shown for'" />
	<xsl:variable name="showPost.showreadreceipt.title" select="'Visa l�skvitto f�r kalenderposten'" />
	<xsl:variable name="showPost.changepost.title" select="'P�ivit� tapahtuma'" />
	<xsl:variable name="showPost.delete.confirm" select="'Haluatko varmasti poistaa tapahtuman'" />
	<xsl:variable name="showPost.delete.title" select="'Poista tapahtuma'" />
	<xsl:variable name="showPost.postedby" select="'Luonut'" />
	<xsl:variable name="showPost.deleteduser" select="'Poistettu k�ytt�j�'" />
	
	<xsl:variable name="showReadReceipt.header" select="'L�skvitto f�r kalenderposten'" />
	<xsl:variable name="showReadReceipt.summary.part1" select="'Totalt'" />
	<xsl:variable name="showReadReceipt.summary.part2" select="'anv�ndare har l�st kalenderposten'" />
	<xsl:variable name="showReadReceipt.name" select="'Namn'" />
	<xsl:variable name="showReadReceipt.firstread" select="'F�rst l�st'" />
	<xsl:variable name="showReadReceipt.lastread" select="'Senast l�st'" />
	<xsl:variable name="showReadReceipt.noreceipt" select="'Ingen anv�ndare har l�st kalenderposten'" />
	<xsl:variable name="showReadReceipt.back" select="'Tillbaka'" />
	
	<xsl:variable name="validationError.RequiredField" select="'Du m�ste fylla i f�ltet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt v�rde p� f�ltet'" />
	<xsl:variable name="validationError.TooLong" select="'F�r stort v�rde p� f�ltet'" />
	<xsl:variable name="validationError.TooShort" select="'Kent�n sis�lt� on liian lyhyt'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Ok�nt fel p� f�ltet'" />
	<xsl:variable name="validationError.field.name" select="'namn'" />
	<xsl:variable name="validationError.field.date" select="'datum'" />
	<xsl:variable name="validationError.field.description" select="'beskrivning'" />
	<xsl:variable name="validationError.messageKey.InvalidContent" select="'Ogiltiga tecken i inneh�llet'" />
	<xsl:variable name="validationError.messageKey.NoGroup" select="'Du m�ste v�lja minst en grupp att publicera till'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett ok�nt fel har uppst�tt'" />				

</xsl:stylesheet>
