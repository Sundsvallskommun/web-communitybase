<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="NewsLetterModuleTemplates.xsl" />
	
	<xsl:variable name="newNewsLetterText">Nytt nyhetsbrev: </xsl:variable>
	<xsl:variable name="updateNewsletterBreadCrumb">�ndra nyhetsbrev</xsl:variable>
	<xsl:variable name="readReceiptBreadCrumb">L�skvitto</xsl:variable>
	<xsl:variable name="addNewsletterBreadCrumb">L�gg till nyhetsbrev</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'f�r'" />
	<xsl:variable name="newslettermodule.noNewsletters.header" select="'Inga nyhetsbrev'" />
	<xsl:variable name="newslettermodule.readreceipt.title" select="'Visa l�skvitto f�r'" />
	<xsl:variable name="newslettermodule.update.title" select="'�ndra nyhetsbrevet'" />
	<xsl:variable name="newslettermodule.delete.title" select="'Ta bort nyhetsbrevet'" />
	<xsl:variable name="newslettermodule.delete.confirm" select="'�r du s�ker p� att du vill ta bort nyhetsbrevet'" />
	<xsl:variable name="newslettermodule.postedBy" select="'Inlagd av'" />
	<xsl:variable name="newslettermodule.deletedUser" select="'Borttagen anv�ndare'" />
	<xsl:variable name="newslettermodule.noNewsletters" select="'Det finns f�r n�rvarande inga nyhetsbrev'" />	
	<xsl:variable name="newslettermodule.submit" select="'L�gg till nyhetsbrev'" />	
	<xsl:variable name="newslettermodule.moreNewsletters" select="'Fler nyhetsbrev f�r'" />	
	<xsl:variable name="newslettermodule.information" select="'F�r att v�xla mellan nyhetsbreven nedan, dubbelklicka p� �nskat nyhetsbrev eller markera och klicka p� knappen &quot;Visa nyhetsbrev&quot;'" />
	<xsl:variable name="newslettermodule.showNewsletter" select="'Visa nyhetsbrev'" />
	
	<xsl:variable name="newsletter.title" select="'Rubrik'" />
	<xsl:variable name="newsletter.content" select="'Inneh�ll'" />
	<xsl:variable name="newsletter.imagelocation" select="'Placera bilden'" />
	<xsl:variable name="newsletter.imagelocation.above" select="'Ovanf�r inneh�llet'" />
	<xsl:variable name="newsletter.imagelocation.below" select="'Nedanf�r inneh�llet'" />
	<xsl:variable name="newsletter.notrequired" select="'Frivilligt'" />
	
	<xsl:variable name="addNewsletter.header" select="'L�gg till nyhetsbrev'" />
	<xsl:variable name="addNewsletter.uploadimage" select="'Ladda upp bild'" />
	<xsl:variable name="addNewsletter.submit" select="'L�gg till nyhetsbrev'" />
	
	<xsl:variable name="updateNewsletter.header" select="'�ndra nyhetsbrevet'" />
	<xsl:variable name="updateNewsletter.changeimage" select="'Byt ut bild'" />
	<xsl:variable name="updateNewsletter.newimage" select="'Placera ny bild'" />
	<xsl:variable name="updateNewsletter.currentimage.imagelocation" select="'Placera nuvarande bild'" />
	<xsl:variable name="updateNewsletter.currentimage.delete" select="'Ta bort nuvarande bild'" />
	<xsl:variable name="updateNewsletter.submit" select="'Spara �ndringar'" />
	
	<xsl:variable name="showReadReceipt.header" select="'L�skvitto f�r nyhetsbrevet'" />
	<xsl:variable name="showReadReceipt.summary.part1" select="'Totalt'" />
	<xsl:variable name="showReadReceipt.summary.part2" select="'anv�ndare har l�st nyhetsbrevet'" />
	<xsl:variable name="showReadReceipt.name" select="'Namn'" />
	<xsl:variable name="showReadReceipt.firstread" select="'F�rst l�st'" />
	<xsl:variable name="showReadReceipt.lastread" select="'Senast l�st'" />
	<xsl:variable name="showReadReceipt.noreceipt" select="'Ingen anv�ndare har l�st nyhetsbrevet'" />
	
	<xsl:variable name="validationError.RequiredField" select="'Du m�ste fylla i f�ltet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt v�rde p� f�ltet'" />
	<xsl:variable name="validation.TooShort" select="'F�r kort inneh�ll i f�ltet'" />
	<xsl:variable name="validation.TooLong" select="'F�r l�ngt inneh�ll i f�ltet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Ok�nt fel p� f�ltet'" />
	<xsl:variable name="validationError.Other" select="'S�kv�gen �r inte giltig, �ndra f�ltet'" />
	
	<xsl:variable name="validationError.field.title" select="'rubrik'" />
	<xsl:variable name="validationError.field.description" select="'beskrivning'" />
	<xsl:variable name="validationError.field.commentText" select="'kommentar'" />
	<xsl:variable name="validationError.field.url" select="'s�kv�g'" />
	<xsl:variable name="validationError.field.content" select="'inneh�ll'" />
	
	<xsl:variable name="validationError.messageKey.BadFileFormat" select="'Filen har felaktigt filformat'" />
	<xsl:variable name="validationError.messageKey.NoFile" select="'Du har inte valt n�gon bild'" />
	<xsl:variable name="validationError.messageKey.InvalidContent" select="'Ogiltiga tecken i inneh�llet'" />
	<xsl:variable name="validationError.messageKey.UnableToParseRequest" select="'Ett fel intr�ffade n�r bilden skulle laddas upp'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett ok�nt fel har uppst�tt'" />				

</xsl:stylesheet>