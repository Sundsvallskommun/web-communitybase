<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="CalendarModuleTemplates.xsl" />
	
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.sv.xsl" />
	
	<xsl:variable name="newCalendarPostTest">Ny kalenderpost: </xsl:variable>

	<xsl:variable name="updatePostBreadCrumb">Ändra kalenderpost</xsl:variable>
	<xsl:variable name="readReceiptBreadCrumb">Läskvitto</xsl:variable>
	<xsl:variable name="addPostBreadCrumb">Lägg till kalenderpost</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'för'" />
	<xsl:variable name="calendar.script.language" select="'CalendarLanguage.se.js'" />
	
	<xsl:variable name="calendarmodule.information" select="'För att lägga till en kalenderpost dubbelklicka på önskad dag eller klicka på länken'" />
	<xsl:variable name="calendarmodule.addlink.title" select="'Lägg till kalenderpost'" />
	
	<xsl:variable name="addPost.header" select="'Lägg till kalenderpost'" />
	<xsl:variable name="addPost.submit" select="'Lägg till'" />
	<xsl:variable name="updatePost.header" select="'Ändra kalenderposten'" />
	<xsl:variable name="updatePost.submit" select="'Spara ändringar'" />
	<xsl:variable name="post.date" select="'Datum'" />
	<xsl:variable name="post.starttime" select="'Starttid'" />
	<xsl:variable name="post.endtime" select="'Sluttid'" />
	<xsl:variable name="post.time.description" select="'Start- och sluttid är frivilligt'" />
	<xsl:variable name="post.showcalendarpost" select="'Visa kalenderposten för'" />
	<xsl:variable name="post.showcalendarpost.group" select="'Gruppen'" />
	<xsl:variable name="post.showcalendarpost.school" select="'Hela förskolan'" />
	<xsl:variable name="post.showcalendarpost.all" select="'Alla förskolor'" />
	<xsl:variable name="post.name" select="'Namn'" />
	<xsl:variable name="post.description" select="'Beskrivning'" />
	<xsl:variable name="post.back" select="'Tillbaka'" />

	<xsl:variable name="showPost.date.text" select="'den'" />
	<xsl:variable name="showPost.date.week" select="'vecka'" />
	<xsl:variable name="showPost.time" select="'Tid'" />
	<xsl:variable name="showPost.title" select="'Rubrik'" />
	<xsl:variable name="showPost.group" select="'Gruppen'" />
	<xsl:variable name="showPost.school" select="'Förskolan'" />
	<xsl:variable name="showPost.publishedTo" select="'Visas för'" />
	<xsl:variable name="showPost.showreadreceipt.title" select="'Visa läskvitto för kalenderposten'" />
	<xsl:variable name="showPost.changepost.title" select="'Ändra kalenderposten'" />
	<xsl:variable name="showPost.delete.confirm" select="'Är du säker på att du vill ta bort kalenderposten'" />
	<xsl:variable name="showPost.delete.title" select="'Ta bort kalenderposten'" />
	<xsl:variable name="showPost.postedby" select="'Inlagd av'" />
	<xsl:variable name="showPost.deleteduser" select="'Borttagen användare'" />
	
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
	<xsl:variable name="validationError.TooShort" select="'För litet värde på fältet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Okänt fel på fältet'" />
	<xsl:variable name="validationError.field.name" select="'namn'" />
	<xsl:variable name="validationError.field.date" select="'datum'" />
	<xsl:variable name="validationError.field.description" select="'beskrivning'" />
	<xsl:variable name="validationError.messageKey.InvalidContent" select="'Ogiltiga tecken i innehållet'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett okänt fel har uppstått'" />				

</xsl:stylesheet>