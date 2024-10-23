<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="CalendarModuleTemplates.xsl" />
	
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.sv.xsl" />
	
	<xsl:variable name="newCalendarPostTest">Ny kalenderpost: </xsl:variable>

	<xsl:variable name="updatePostBreadCrumb">�ndra kalenderpost</xsl:variable>
	<xsl:variable name="readReceiptBreadCrumb">L�skvitto</xsl:variable>
	<xsl:variable name="addPostBreadCrumb">L�gg till kalenderpost</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'f�r'" />
	<xsl:variable name="calendar.script.language" select="'CalendarLanguage.se.js'" />
	
	<xsl:variable name="calendarmodule.information" select="'F�r att l�gga till en kalenderpost dubbelklicka p� �nskad dag eller klicka p� l�nken'" />
	<xsl:variable name="calendarmodule.addlink.title" select="'L�gg till kalenderpost'" />
	
	<xsl:variable name="addPost.header" select="'L�gg till kalenderpost'" />
	<xsl:variable name="addPost.submit" select="'L�gg till'" />
	<xsl:variable name="updatePost.header" select="'�ndra kalenderposten'" />
	<xsl:variable name="updatePost.submit" select="'Spara �ndringar'" />
	<xsl:variable name="post.date" select="'Datum'" />
	<xsl:variable name="post.starttime" select="'Starttid'" />
	<xsl:variable name="post.endtime" select="'Sluttid'" />
	<xsl:variable name="post.time.description" select="'Start- och sluttid �r frivilligt'" />
	<xsl:variable name="post.showcalendarpost" select="'Visa kalenderposten f�r'" />
	<xsl:variable name="post.showcalendarpost.group" select="'Gruppen'" />
	<xsl:variable name="post.showcalendarpost.school" select="'Hela f�rskolan'" />
	<xsl:variable name="post.showcalendarpost.all" select="'Alla f�rskolor'" />
	<xsl:variable name="post.name" select="'Namn'" />
	<xsl:variable name="post.description" select="'Beskrivning'" />
	<xsl:variable name="post.back" select="'Tillbaka'" />

	<xsl:variable name="showPost.date.text" select="'den'" />
	<xsl:variable name="showPost.date.week" select="'vecka'" />
	<xsl:variable name="showPost.time" select="'Tid'" />
	<xsl:variable name="showPost.title" select="'Rubrik'" />
	<xsl:variable name="showPost.group" select="'Gruppen'" />
	<xsl:variable name="showPost.school" select="'F�rskolan'" />
	<xsl:variable name="showPost.publishedTo" select="'Visas f�r'" />
	<xsl:variable name="showPost.showreadreceipt.title" select="'Visa l�skvitto f�r kalenderposten'" />
	<xsl:variable name="showPost.changepost.title" select="'�ndra kalenderposten'" />
	<xsl:variable name="showPost.delete.confirm" select="'�r du s�ker p� att du vill ta bort kalenderposten'" />
	<xsl:variable name="showPost.delete.title" select="'Ta bort kalenderposten'" />
	<xsl:variable name="showPost.postedby" select="'Inlagd av'" />
	<xsl:variable name="showPost.deleteduser" select="'Borttagen anv�ndare'" />
	
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
	<xsl:variable name="validationError.TooShort" select="'F�r litet v�rde p� f�ltet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Ok�nt fel p� f�ltet'" />
	<xsl:variable name="validationError.field.name" select="'namn'" />
	<xsl:variable name="validationError.field.date" select="'datum'" />
	<xsl:variable name="validationError.field.description" select="'beskrivning'" />
	<xsl:variable name="validationError.messageKey.InvalidContent" select="'Ogiltiga tecken i inneh�llet'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett ok�nt fel har uppst�tt'" />				

</xsl:stylesheet>