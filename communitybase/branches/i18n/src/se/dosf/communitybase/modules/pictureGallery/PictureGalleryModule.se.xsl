<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="PictureGalleryModuleTemplates.xsl" />
	
	<xsl:variable name="addGalleryBreadCrumb">Lägg till album</xsl:variable>
	<xsl:variable name="editGalleryBreadCrumb">Ändra album</xsl:variable>
	<xsl:variable name="addImagesBreadCrumb">Lägg till bilder</xsl:variable>
	<xsl:variable name="newGalleryText">Nytt album: </xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'för'" />
	
	<xsl:variable name="multiUploader.language" select="'language.se.js'" />
	<xsl:variable name="multiUploader.uploadbutton" select="'uploadbutton.se.png'" />
	
	<xsl:variable name="multiUploader.header" select="'Lägg till bilder i albumet'" />
	<xsl:variable name="multiUploader.queue" select="'Valda bilder på kö'" />
	<xsl:variable name="multiUploader.information" select="'Välj de bilder du vill ladda upp i albumet och klicka därefter på knappen &quot;Ladda upp bild(er)&quot; för att starta uppladdningen'" />
	<xsl:variable name="multiUploader.noimages" select="'Det finns för tillfället inga bilder i kön'" />
	<xsl:variable name="multiUploader.nouploaded" select="'Inga bilder uppladdade'" />
	<xsl:variable name="multiUploader.clearlist" select="'Töm listan'" />
	<xsl:variable name="multiUploader.btnSubmit" select="'Ladda upp bild(er)'" />
	<xsl:variable name="multiUploader.btnCancel" select="'Avbryt uppladdning'" />
	<xsl:variable name="multiUploader.btnDone" select="'Tillbaka'" />
	
	<xsl:variable name="addImages.header" select="'Lägg till bilder i albumet'" />
	<xsl:variable name="addImages.information.part1" select="'För att ladda upp flera bilder samtidigt krävs Adobe Flash Player'" />
	<xsl:variable name="addImages.information.part2" select="'för att ladda ner Adobe Flash Player eller ladda upp en bild/zip-fil nedan'" />
	<xsl:variable name="addImages.information.click" select="'Klicka'" />
	<xsl:variable name="addImages.information.link" select="'här'" />
	<xsl:variable name="addImages.upload" select="'Ladda upp bild/bilder (zip fil eller enskild bild)'" />
	<xsl:variable name="addImages.submit" select="'Lägg till bild(er)'" />
	<xsl:variable name="addImages.diskThreshold" select="'Max tillåtna filstorlek är'"/>

	
	<xsl:variable name="pictureGallery.header" select="'Album för'" />
	<xsl:variable name="pictureGallery.new" select="'Nytt album sedan du besökte bildarkivet senast'" />
	<xsl:variable name="pictureGallery.group.noalbum" select="'Det finns för närvarande inga album i gruppen'" />
	<xsl:variable name="pictureGallery.group.add" select="'Lägg till album i'" />
	<xsl:variable name="pictureGallery.school.add" select="'Lägg till album på'" />
	<xsl:variable name="pictureGallery.school.noalbum" select="'Det finns för närvarande inga album i skolan'" />
	
	<xsl:variable name="pictureGallery.update.title" select="'Ändra albumet'" />
	<xsl:variable name="pictureGallery.delete.title" select="'Ta bort albumet'" />
	<xsl:variable name="pictureGallery.delete.confirm" select="'Är du säker på att du vill ta bort albumet'" />
	<xsl:variable name="pictureGallery.show.title" select="'Visa albumet'" />
	<xsl:variable name="pictureGallery.images" select="'bilder'" />
	
	
	<xsl:variable name="showGallery.pictures" select="'bilder'" />
	<xsl:variable name="showGallery.page" select="'Sida'" />
	<xsl:variable name="showGallery.pagecount" select="'av'" />
	<xsl:variable name="showGallery.previousLink.title" select="'Föregående'" />
	<xsl:variable name="showGallery.previousImage.alt" select="'Föregående'" />
	<xsl:variable name="showGallery.previousLink.text" select="'Föregående'" />
	<xsl:variable name="showGallery.nextLink.title" select="'Nästa'" />
	<xsl:variable name="showGallery.nextLink.text" select="'Nästa'" />	
	<xsl:variable name="showGallery.nextImage.alt" select="'Nästa'" />
	<xsl:variable name="showGallery.addImagesLinkSimple.title" select="'Lägg till bilder (enkel)'" />
	<xsl:variable name="showGallery.addImagesLinkSimple.text" select="'Lägg till bilder (enkel)'" />
	<xsl:variable name="showGallery.addImagesLinkAdvanced.title" select="'Lägg till bilder (avancerad)'" />
	<xsl:variable name="showGallery.addImagesLinkAdvanced.text" select="'Lägg till bilder (avancerad)'" />
	<xsl:variable name="showGallery.showAllGalleriesLink.title" select="'Visa alla album'" />
	<xsl:variable name="showGallery.showAllGalleriesLink.text" select="'Visa alla album'" />
	<xsl:variable name="showGallery.noImagesInGallery" select="'Det finns inga bilder i album'" />
	
	<xsl:variable name="file.link.title" select="'Visa större bild'" />
	
	<xsl:variable name="showImage.pictures" select="'bilder'" />
	<xsl:variable name="showImage.deleteImageLink.title" select="'Ta bort bilden'" />
	<xsl:variable name="showImage.deleteImageLink.confirm" select="'Är du säker på att du vill ta bort bilden'" />
	<xsl:variable name="showImage.picture" select="'bild'" />
	<xsl:variable name="showImage.pictureCount" select="'av'" />
	<xsl:variable name="showImage.previousLink.title" select="'Föregående'" />
	<xsl:variable name="showImage.previousImage.alt" select="'Föregående'" />
	<xsl:variable name="showImage.previousLink.text" select="'Föregående'" />
	<xsl:variable name="showImage.nextLink.title" select="'Nästa'" />
	<xsl:variable name="showImage.nextLink.text" select="'Nästa'" />	
	<xsl:variable name="showImage.nextImage.alt" select="'Nästa'" />	
	<xsl:variable name="showImage.showThumbsLink.title" select="'Små bilder'" />
	<xsl:variable name="showImage.showThumbsLink.text" select="'Små bilder'" />
	
	<xsl:variable name="gallery.file.showFullImageLink.title" select="'Visa större bild'" />
	<xsl:variable name="gallery.file.comments" select="'Kommentarer'" />
	<xsl:variable name="gallery.file.hide.comments" select="'Dölj kommentarer'" />					
	<xsl:variable name="gallery.file.show.comments" select="'Visa kommentarer'" />
	<xsl:variable name="gallery.file.noComments" select="'Det finns inga kommentarer för denna bild'" />
	<xsl:variable name="gallery.file.addcomment" select="'Kommentar'" />
	<xsl:variable name="gallery.file.submit" select="'Lägg till kommentar'" />
	
	<xsl:variable name="comment.updateCommentLink.title" select="'Uppdatera kommentaren'" />
	<xsl:variable name="comment.deleteCommentLink.title" select="'Ta bort kommentaren'" />
	<xsl:variable name="comment.submit" select="'Spara ändringar'" />
	<xsl:variable name="comment.anonymousUser" select="'Anonym användare'" />
	
	<xsl:variable name="addGallery.header" select="'Lägg till album för'" />
	<xsl:variable name="addGallery.group" select="'gruppen'" />
	<xsl:variable name="addGallery.school" select="'skolan'" />
	<xsl:variable name="addGallery.name" select="'Namn'" />
	<xsl:variable name="addGallery.description" select="'Beskrivning'" />
	<xsl:variable name="addGallery.submit" select="'Skapa album'" />
	
	<xsl:variable name="updateGallery.header" select="'Ändra albumet'" />
	<xsl:variable name="updateGallery.name" select="'Namn'" />
	<xsl:variable name="updateGallery.description" select="'Beskrivning'" />
	<xsl:variable name="updateGallery.submit" select="'Spara ändringar'" />

	
	<xsl:variable name="validationError.RequiredField" select="'Du måste fylla i fältet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt format på fältet'" />
	<xsl:variable name="validation.TooLong" select="'För långt innehåll i fältet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Okänt fel på fältet'" />
	<xsl:variable name="validationError.Other" select="'Sökvägen är inte giltig, ändra fältet'" />	
	
	<xsl:variable name="validationError.field.name" select="'namn'" />
	<xsl:variable name="validationError.field.description" select="'beskrivning'" />
	<xsl:variable name="validationError.field.commentText" select="'kommentar'" />
	<xsl:variable name="validationError.field.url" select="'sökväg'" />
	
	<xsl:variable name="validationError.messageKey.BadFileFormat" select="'Filen har felaktigt filformat'" />
	<xsl:variable name="validationError.messageKey.NoImage" select="'Du måste välja en bild eller zip-fil med bilder att ladda upp'" />	
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett okänt fel har uppstått'" />

</xsl:stylesheet>