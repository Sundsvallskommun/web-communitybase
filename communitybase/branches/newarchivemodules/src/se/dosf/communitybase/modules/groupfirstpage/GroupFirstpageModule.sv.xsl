<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="GroupFirstpageModuleTemplates.xsl" />
	
	<xsl:variable name="newFirstpageText">Ny förstasida: </xsl:variable>
	<xsl:variable name="noFirstpageText">Ingen första sida</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'för'" />
	<xsl:variable name="groupFirstpageModule.delete.title" select="'Ta bort förstasidan för gruppen'" />
	<xsl:variable name="groupFirstpageModule.delete.confirm" select="'Är du säker på att du vill ta bort förstasidan för gruppen'" />
	<xsl:variable name="groupFirstpageModule.update.title" select="'Ändra förstasidan för gruppen'" />
	<xsl:variable name="groupFirstpageModule.lastupdated" select="'Sidan ändrades senast'" />
	
	<xsl:variable name="updatefirstpage.header" select="'Ändra gruppens förstasida'" />
	<xsl:variable name="updatefirstpage.title" select="'Rubrik'" />
	<xsl:variable name="updatefirstpage.content" select="'Innehåll'" />
	<xsl:variable name="updatefirstpage.changeimage" select="'Byt ut bild'" />
	<xsl:variable name="updatefirstpage.uploadimage" select="'Ladda upp bild'" />
	<xsl:variable name="updatefirstpage.notrequired" select="'Frivilligt'" />
	<xsl:variable name="updatefirstpage.newimage" select="'Placera ny bild'" />
	<xsl:variable name="updatefirstpage.imagelocation" select="'Placera bilden'" />
	<xsl:variable name="updatefirstpage.imagelocation.above" select="'Ovanför innehållet'" />
	<xsl:variable name="updatefirstpage.imagelocation.below" select="'Nedanför innehållet'" />
	<xsl:variable name="updatefirstpage.currentimage.imagelocation" select="'Placera nuvarande bild'" />
	<xsl:variable name="updatefirstpage.currentimage.delete" select="'Ta bort nuvarande bild'" />
	<xsl:variable name="updatefirstpage.currentimage.save" select="'Spara ändringar'" />
		
	<xsl:variable name="validationError.RequiredField" select="'Du måste fylla i fältet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt värde på fältet'" />
	<xsl:variable name="validationError.TooLong" select="'För stort värde på fältet'" />
	<xsl:variable name="validationError.Other" select="'Sökvägen är inte giltig, ändra fältet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Okänt fel på fältet'" />
	<xsl:variable name="validationError.field.name" select="'namn'" />
	<xsl:variable name="validationError.field.title" select="'rubrik'" />
	<xsl:variable name="validationError.field.description" select="'beskrivning'" />
	<xsl:variable name="validationError.field.commentText" select="'kommentar'" />
	<xsl:variable name="validationError.field.url" select="'sökväg'" />
	<xsl:variable name="validationError.field.text" select="'innehåll'" />
	<xsl:variable name="validationError.messageKey.InvalidContent" select="'Ogiltiga tecken i innehållet'" />
	<xsl:variable name="validationError.messageKey.BadFileFormat" select="'Felaktigt filformat'" />
	<xsl:variable name="validationError.messageKey.NoFile" select="'Du har inte valt någon bild'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett okänt fel har uppstått'" />				

</xsl:stylesheet>