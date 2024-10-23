<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="NewsLetterModuleTemplates.xsl" />
	
	<xsl:variable name="newNewsLetterText">Nytt nyhetsbrev: </xsl:variable>
	<xsl:variable name="updateNewsletterBreadCrumb">Ändra nyhetsbrev</xsl:variable>
	<xsl:variable name="readReceiptBreadCrumb">Läskvitto</xsl:variable>
	<xsl:variable name="addNewsletterBreadCrumb">Lägg till nyhetsbrev</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'för'" />
	<xsl:variable name="newslettermodule.noNewsletters.header" select="'Inga nyhetsbrev'" />
	<xsl:variable name="newslettermodule.readreceipt.title" select="'Visa läskvitto för'" />
	<xsl:variable name="newslettermodule.update.title" select="'Ändra nyhetsbrevet'" />
	<xsl:variable name="newslettermodule.delete.title" select="'Ta bort nyhetsbrevet'" />
	<xsl:variable name="newslettermodule.delete.confirm" select="'Är du säker på att du vill ta bort nyhetsbrevet'" />
	<xsl:variable name="newslettermodule.postedBy" select="'Inlagd av'" />
	<xsl:variable name="newslettermodule.deletedUser" select="'Borttagen användare'" />
	<xsl:variable name="newslettermodule.noNewsletters" select="'Det finns för närvarande inga nyhetsbrev'" />	
	<xsl:variable name="newslettermodule.submit" select="'Lägg till nyhetsbrev'" />	
	<xsl:variable name="newslettermodule.moreNewsletters" select="'Fler nyhetsbrev för'" />	
	<xsl:variable name="newslettermodule.information" select="'För att växla mellan nyhetsbreven nedan, dubbelklicka på önskat nyhetsbrev eller markera och klicka på knappen &quot;Visa nyhetsbrev&quot;'" />
	<xsl:variable name="newslettermodule.showNewsletter" select="'Visa nyhetsbrev'" />
	
	<xsl:variable name="newsletter.title" select="'Rubrik'" />
	<xsl:variable name="newsletter.content" select="'Innehåll'" />
	<xsl:variable name="newsletter.imagelocation" select="'Placera bilden'" />
	<xsl:variable name="newsletter.imagelocation.above" select="'Ovanför innehållet'" />
	<xsl:variable name="newsletter.imagelocation.below" select="'Nedanför innehållet'" />
	<xsl:variable name="newsletter.notrequired" select="'Frivilligt'" />
	
	<xsl:variable name="addNewsletter.header" select="'Lägg till nyhetsbrev'" />
	<xsl:variable name="addNewsletter.uploadimage" select="'Ladda upp bild'" />
	<xsl:variable name="addNewsletter.submit" select="'Lägg till nyhetsbrev'" />
	
	<xsl:variable name="updateNewsletter.header" select="'Ändra nyhetsbrevet'" />
	<xsl:variable name="updateNewsletter.changeimage" select="'Byt ut bild'" />
	<xsl:variable name="updateNewsletter.newimage" select="'Placera ny bild'" />
	<xsl:variable name="updateNewsletter.currentimage.imagelocation" select="'Placera nuvarande bild'" />
	<xsl:variable name="updateNewsletter.currentimage.delete" select="'Ta bort nuvarande bild'" />
	<xsl:variable name="updateNewsletter.submit" select="'Spara ändringar'" />
	
	<xsl:variable name="showReadReceipt.header" select="'Läskvitto för nyhetsbrevet'" />
	<xsl:variable name="showReadReceipt.summary.part1" select="'Totalt'" />
	<xsl:variable name="showReadReceipt.summary.part2" select="'användare har läst nyhetsbrevet'" />
	<xsl:variable name="showReadReceipt.name" select="'Namn'" />
	<xsl:variable name="showReadReceipt.firstread" select="'Först läst'" />
	<xsl:variable name="showReadReceipt.lastread" select="'Senast läst'" />
	<xsl:variable name="showReadReceipt.noreceipt" select="'Ingen användare har läst nyhetsbrevet'" />
	
	<xsl:variable name="validationError.RequiredField" select="'Du måste fylla i fältet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt värde på fältet'" />
	<xsl:variable name="validation.TooShort" select="'För kort innehåll i fältet'" />
	<xsl:variable name="validation.TooLong" select="'För långt innehåll i fältet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Okänt fel på fältet'" />
	<xsl:variable name="validationError.Other" select="'Sökvägen är inte giltig, ändra fältet'" />
	
	<xsl:variable name="validationError.field.title" select="'rubrik'" />
	<xsl:variable name="validationError.field.description" select="'beskrivning'" />
	<xsl:variable name="validationError.field.commentText" select="'kommentar'" />
	<xsl:variable name="validationError.field.url" select="'sökväg'" />
	<xsl:variable name="validationError.field.content" select="'innehåll'" />
	
	<xsl:variable name="validationError.messageKey.BadFileFormat" select="'Filen har felaktigt filformat'" />
	<xsl:variable name="validationError.messageKey.NoFile" select="'Du har inte valt någon bild'" />
	<xsl:variable name="validationError.messageKey.InvalidContent" select="'Ogiltiga tecken i innehållet'" />
	<xsl:variable name="validationError.messageKey.UnableToParseRequest" select="'Ett fel inträffade när bilden skulle laddas upp'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett okänt fel har uppstått'" />				

</xsl:stylesheet>