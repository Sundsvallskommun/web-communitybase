<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="PictureGalleryModuleTemplates.xsl" />
	
	<xsl:variable name="addGalleryBreadCrumb">L�gg till album</xsl:variable>
	<xsl:variable name="editGalleryBreadCrumb">�ndra album</xsl:variable>
	<xsl:variable name="addImagesBreadCrumb">L�gg till bilder</xsl:variable>
	<xsl:variable name="newGalleryText">Nytt album: </xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'f�r'" />
	
	<xsl:variable name="multiUploader.language" select="'language.se.js'" />
	<xsl:variable name="multiUploader.uploadbutton" select="'uploadbutton.se.png'" />
	
	<xsl:variable name="multiUploader.header" select="'L�gg till bilder i albumet'" />
	<xsl:variable name="multiUploader.queue" select="'Valda bilder p� k�'" />
	<xsl:variable name="multiUploader.information" select="'V�lj de bilder du vill ladda upp i albumet och klicka d�refter p� knappen &quot;Ladda upp bild(er)&quot; f�r att starta uppladdningen'" />
	<xsl:variable name="multiUploader.noimages" select="'Det finns f�r tillf�llet inga bilder i k�n'" />
	<xsl:variable name="multiUploader.nouploaded" select="'Inga bilder uppladdade'" />
	<xsl:variable name="multiUploader.clearlist" select="'T�m listan'" />
	<xsl:variable name="multiUploader.btnSubmit" select="'Ladda upp bild(er)'" />
	<xsl:variable name="multiUploader.btnCancel" select="'Avbryt uppladdning'" />
	<xsl:variable name="multiUploader.btnDone" select="'Tillbaka'" />
	
	<xsl:variable name="addImages.header" select="'L�gg till bilder i albumet'" />
	<xsl:variable name="addImages.information.part1" select="'F�r att ladda upp flera bilder samtidigt kr�vs Adobe Flash Player'" />
	<xsl:variable name="addImages.information.part2" select="'f�r att ladda ner Adobe Flash Player eller ladda upp en bild/zip-fil nedan'" />
	<xsl:variable name="addImages.information.click" select="'Klicka'" />
	<xsl:variable name="addImages.information.link" select="'h�r'" />
	<xsl:variable name="addImages.upload" select="'Ladda upp bild/bilder (zip fil eller enskild bild)'" />
	<xsl:variable name="addImages.submit" select="'L�gg till bild(er)'" />
	<xsl:variable name="addImages.diskThreshold" select="'Max till�tna filstorlek �r'"/>

	
	<xsl:variable name="pictureGallery.header" select="'Album f�r'" />
	<xsl:variable name="pictureGallery.new" select="'Nytt album sedan du bes�kte bildarkivet senast'" />
	<xsl:variable name="pictureGallery.group.noalbum" select="'Det finns f�r n�rvarande inga album i gruppen'" />
	<xsl:variable name="pictureGallery.group.add" select="'L�gg till album i'" />
	<xsl:variable name="pictureGallery.school.add" select="'L�gg till album p�'" />
	<xsl:variable name="pictureGallery.school.noalbum" select="'Det finns f�r n�rvarande inga album i skolan'" />
	
	<xsl:variable name="pictureGallery.update.title" select="'�ndra albumet'" />
	<xsl:variable name="pictureGallery.delete.title" select="'Ta bort albumet'" />
	<xsl:variable name="pictureGallery.delete.confirm" select="'�r du s�ker p� att du vill ta bort albumet'" />
	<xsl:variable name="pictureGallery.show.title" select="'Visa albumet'" />
	<xsl:variable name="pictureGallery.images" select="'bilder'" />
	
	
	<xsl:variable name="showGallery.pictures" select="'bilder'" />
	<xsl:variable name="showGallery.page" select="'Sida'" />
	<xsl:variable name="showGallery.pagecount" select="'av'" />
	<xsl:variable name="showGallery.previousLink.title" select="'F�reg�ende'" />
	<xsl:variable name="showGallery.previousImage.alt" select="'F�reg�ende'" />
	<xsl:variable name="showGallery.previousLink.text" select="'F�reg�ende'" />
	<xsl:variable name="showGallery.nextLink.title" select="'N�sta'" />
	<xsl:variable name="showGallery.nextLink.text" select="'N�sta'" />	
	<xsl:variable name="showGallery.nextImage.alt" select="'N�sta'" />
	<xsl:variable name="showGallery.addImagesLinkSimple.title" select="'L�gg till bilder (enkel)'" />
	<xsl:variable name="showGallery.addImagesLinkSimple.text" select="'L�gg till bilder (enkel)'" />
	<xsl:variable name="showGallery.addImagesLinkAdvanced.title" select="'L�gg till bilder (avancerad)'" />
	<xsl:variable name="showGallery.addImagesLinkAdvanced.text" select="'L�gg till bilder (avancerad)'" />
	<xsl:variable name="showGallery.showAllGalleriesLink.title" select="'Visa alla album'" />
	<xsl:variable name="showGallery.showAllGalleriesLink.text" select="'Visa alla album'" />
	<xsl:variable name="showGallery.noImagesInGallery" select="'Det finns inga bilder i album'" />
	
	<xsl:variable name="file.link.title" select="'Visa st�rre bild'" />
	
	<xsl:variable name="showImage.pictures" select="'bilder'" />
	<xsl:variable name="showImage.deleteImageLink.title" select="'Ta bort bilden'" />
	<xsl:variable name="showImage.deleteImageLink.confirm" select="'�r du s�ker p� att du vill ta bort bilden'" />
	<xsl:variable name="showImage.picture" select="'bild'" />
	<xsl:variable name="showImage.pictureCount" select="'av'" />
	<xsl:variable name="showImage.previousLink.title" select="'F�reg�ende'" />
	<xsl:variable name="showImage.previousImage.alt" select="'F�reg�ende'" />
	<xsl:variable name="showImage.previousLink.text" select="'F�reg�ende'" />
	<xsl:variable name="showImage.nextLink.title" select="'N�sta'" />
	<xsl:variable name="showImage.nextLink.text" select="'N�sta'" />	
	<xsl:variable name="showImage.nextImage.alt" select="'N�sta'" />	
	<xsl:variable name="showImage.showThumbsLink.title" select="'Sm� bilder'" />
	<xsl:variable name="showImage.showThumbsLink.text" select="'Sm� bilder'" />
	
	<xsl:variable name="gallery.file.showFullImageLink.title" select="'Visa st�rre bild'" />
	<xsl:variable name="gallery.file.comments" select="'Kommentarer'" />
	<xsl:variable name="gallery.file.hide.comments" select="'D�lj kommentarer'" />					
	<xsl:variable name="gallery.file.show.comments" select="'Visa kommentarer'" />
	<xsl:variable name="gallery.file.noComments" select="'Det finns inga kommentarer f�r denna bild'" />
	<xsl:variable name="gallery.file.addcomment" select="'Kommentar'" />
	<xsl:variable name="gallery.file.submit" select="'L�gg till kommentar'" />
	
	<xsl:variable name="comment.updateCommentLink.title" select="'Uppdatera kommentaren'" />
	<xsl:variable name="comment.deleteCommentLink.title" select="'Ta bort kommentaren'" />
	<xsl:variable name="comment.submit" select="'Spara �ndringar'" />
	<xsl:variable name="comment.anonymousUser" select="'Anonym anv�ndare'" />
	
	<xsl:variable name="addGallery.header" select="'L�gg till album f�r'" />
	<xsl:variable name="addGallery.group" select="'gruppen'" />
	<xsl:variable name="addGallery.school" select="'skolan'" />
	<xsl:variable name="addGallery.name" select="'Namn'" />
	<xsl:variable name="addGallery.description" select="'Beskrivning'" />
	<xsl:variable name="addGallery.submit" select="'Skapa album'" />
	
	<xsl:variable name="updateGallery.header" select="'�ndra albumet'" />
	<xsl:variable name="updateGallery.name" select="'Namn'" />
	<xsl:variable name="updateGallery.description" select="'Beskrivning'" />
	<xsl:variable name="updateGallery.submit" select="'Spara �ndringar'" />

	
	<xsl:variable name="validationError.RequiredField" select="'Du m�ste fylla i f�ltet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt format p� f�ltet'" />
	<xsl:variable name="validation.TooLong" select="'F�r l�ngt inneh�ll i f�ltet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Ok�nt fel p� f�ltet'" />
	<xsl:variable name="validationError.Other" select="'S�kv�gen �r inte giltig, �ndra f�ltet'" />	
	
	<xsl:variable name="validationError.field.name" select="'namn'" />
	<xsl:variable name="validationError.field.description" select="'beskrivning'" />
	<xsl:variable name="validationError.field.commentText" select="'kommentar'" />
	<xsl:variable name="validationError.field.url" select="'s�kv�g'" />
	
	<xsl:variable name="validationError.messageKey.BadFileFormat" select="'Filen har felaktigt filformat'" />
	<xsl:variable name="validationError.messageKey.NoImage" select="'Du m�ste v�lja en bild eller zip-fil med bilder att ladda upp'" />	
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett ok�nt fel har uppst�tt'" />

</xsl:stylesheet>