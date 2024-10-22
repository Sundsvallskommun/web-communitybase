<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="PictureGalleryModuleTemplates.xsl" />
	
	<xsl:variable name="addGalleryBreadCrumb">Lisää albumi</xsl:variable>
	<xsl:variable name="editGalleryBreadCrumb">Muokkaa albumia</xsl:variable>
	<xsl:variable name="addImagesBreadCrumb">Lisää kuvia</xsl:variable>
	<xsl:variable name="newGalleryText">Uusi albumi: </xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'kohteelle'" />
	
	<xsl:variable name="multiUploader.language" select="'language.fi.js'" />
	<xsl:variable name="multiUploader.uploadbutton" select="'uploadbutton.fi.png'" />
	<xsl:variable name="multiUploader.header" select="'Lisää kuvia albumiin'" />
	<xsl:variable name="multiUploader.queue" select="'Valitut kuvat ovat jonossa'" />
	<xsl:variable name="multiUploader.information" select="'Valitse haluamasi kuvat ladattavaksi albumiin, sitten klikkaa nappia &quot;Lähetä kuva(t)&quot; aloittaaksesi lähetyksen'" />
	<xsl:variable name="multiUploader.noimages" select="'Ei kuvia tällä hetkellä'" />
	<xsl:variable name="multiUploader.nouploaded" select="'Ei ladattuja kuvia'" />
	<xsl:variable name="multiUploader.clearlist" select="'Tyhjää lista'" />
	<xsl:variable name="multiUploader.btnSubmit" select="'Lähetä kuva(t)'" />
	<xsl:variable name="multiUploader.btnCancel" select="'Peruuta lähetys'" />
	<xsl:variable name="multiUploader.btnDone" select="'Takaisin'" />
	
	<xsl:variable name="addImages.header" select="'Lisää kuvia albumiin'" />
	<xsl:variable name="addImages.information.part1" select="'Usean kuvan yhtäaikainen lähetys vaatii Adobe Flash Player-lisäosan'" />
	<xsl:variable name="addImages.information.part2" select="'ladataksesi Adobe Flash Playerin tai voit lähettää kuva/zip-tiedoston alhaalta'" />
	<xsl:variable name="addImages.information.click" select="'Klikkaa'" />
	<xsl:variable name="addImages.information.link" select="'tästä'" />
	<xsl:variable name="addImages.upload" select="'Lähetä kuva/kuvia (zip-tiedosto tai yksittäinen kuva)'" />
	<xsl:variable name="addImages.submit" select="'Lähetä kuva(t)'" />
	<xsl:variable name="addImages.diskThreshold" select="'Suurin sallittu tiedoston koko on'"/>

	
	<xsl:variable name="pictureGallery.header" select="'Albumi'" />
	<xsl:variable name="pictureGallery.new" select="'Uusi albumi edellisen käyntisi jälkeen'" />
	<xsl:variable name="pictureGallery.group.noalbum" select="'Ryhmässä ei ole albumeita tällä hetkellä'" />
	<xsl:variable name="pictureGallery.group.add" select="'Lisää albumi ryhmään'" />
	<xsl:variable name="pictureGallery.school.add" select="'Lisää albumi kouluun'" />
	<xsl:variable name="pictureGallery.school.noalbum" select="'Koulussa ei ole albumeita tällä hetkellä'" />
	
	<xsl:variable name="pictureGallery.update.title" select="'Muokkaa albumia'" />
	<xsl:variable name="pictureGallery.delete.title" select="'Poista albumi'" />
	<xsl:variable name="pictureGallery.delete.confirm" select="'Haluatko varmasti poistaa albumin'" />
	<xsl:variable name="pictureGallery.show.title" select="'Näytä albumi'" />
	<xsl:variable name="pictureGallery.images" select="'kuvia'" />
	
	
	<xsl:variable name="showGallery.pictures" select="'kuvaa'" />
	<xsl:variable name="showGallery.page" select="'Sivu'" />
	<xsl:variable name="showGallery.pagecount" select="'/'" />
	<xsl:variable name="showGallery.previousLink.title" select="'Edellinen'" />
	<xsl:variable name="showGallery.previousImage.alt" select="'Edellinen'" />
	<xsl:variable name="showGallery.previousLink.text" select="'Edellinen'" />
	<xsl:variable name="showGallery.nextLink.title" select="'Seuraava'" />
	<xsl:variable name="showGallery.nextLink.text" select="'Seuraava'" />	
	<xsl:variable name="showGallery.nextImage.alt" select="'Seuraava'" />
	<xsl:variable name="showGallery.addImagesLinkSimple.title" select="'Lisää kuvia (yksinkertainen)'" />
	<xsl:variable name="showGallery.addImagesLinkSimple.text" select="'Lisää kuvia (yksinkertainen)'" />
	<xsl:variable name="showGallery.addImagesLinkAdvanced.title" select="'Lisää kuvia (kehittynyt)'" />
	<xsl:variable name="showGallery.addImagesLinkAdvanced.text" select="'Lisää kuvia (kehittynyt)'" />
	<xsl:variable name="showGallery.showAllGalleriesLink.title" select="'Näytä kaikki albumit'" />
	<xsl:variable name="showGallery.showAllGalleriesLink.text" select="'Näytä kaikki albumt'" />
	<xsl:variable name="showGallery.noImagesInGallery" select="'Albumista ei löydy kuvia'" />
	
	<xsl:variable name="file.link.title" select="'Näytä suuri kuva'" />
	
	<xsl:variable name="showImage.pictures" select="'kuvaa'" />
	<xsl:variable name="showImage.deleteImageLink.title" select="'Poista kuva'" />
	<xsl:variable name="showImage.deleteImageLink.confirm" select="'Haluatko varmasti poistaa kuvan'" />
	<xsl:variable name="showImage.picture" select="'kuva'" />
	<xsl:variable name="showImage.pictureCount" select="'/'" />
	<xsl:variable name="showImage.previousLink.title" select="'Edellinen'" />
	<xsl:variable name="showImage.previousImage.alt" select="'Edellinen'" />
	<xsl:variable name="showImage.previousLink.text" select="'Edellinen'" />
	<xsl:variable name="showImage.nextLink.title" select="'Seuraava'" />
	<xsl:variable name="showImage.nextLink.text" select="'Seuraava'" />	
	<xsl:variable name="showImage.nextImage.alt" select="'Seuraava'" />	
	<xsl:variable name="showImage.showThumbsLink.title" select="'Pienet kuvat'" />
	<xsl:variable name="showImage.showThumbsLink.text" select="'Pienet kuvat'" />
	
	<xsl:variable name="gallery.file.showFullImageLink.title" select="'Näytä suuri kuva'" />
	<xsl:variable name="gallery.file.comments" select="'Kommentit'" />
	<xsl:variable name="gallery.file.hide.comments" select="'Piilota kommentit'" />					
	<xsl:variable name="gallery.file.show.comments" select="'Näytä kommentit'" />
	<xsl:variable name="gallery.file.noComments" select="'Kuvalla ei ole kommentteja'" />
	<xsl:variable name="gallery.file.addcomment" select="'Kommentit'" />
	<xsl:variable name="gallery.file.submit" select="'Lisää kommentti'" />
	
	<xsl:variable name="comment.updateCommentLink.title" select="'Muokkaa kommenttia'" />
	<xsl:variable name="comment.deleteCommentLink.title" select="'Poista kommentti'" />
	<xsl:variable name="comment.submit" select="'Tallenna'" />
	<xsl:variable name="comment.anonymousUser" select="'Anonyymi käyttäjä'" />
	
	<xsl:variable name="addGallery.header" select="'Lisää albumi'" />
	<xsl:variable name="addGallery.group" select="'ryhmä'" />
	<xsl:variable name="addGallery.school" select="'koulu'" />
	<xsl:variable name="addGallery.name" select="'Nimi'" />
	<xsl:variable name="addGallery.description" select="'Kuvaus'" />
	<xsl:variable name="addGallery.submit" select="'Luo albumi'" />
	
	<xsl:variable name="updateGallery.header" select="'Muokkaa albumiat'" />
	<xsl:variable name="updateGallery.name" select="'Nimi'" />
	<xsl:variable name="updateGallery.description" select="'Kuvaus'" />
	<xsl:variable name="updateGallery.submit" select="'Tallenna muutokset'" />

	
	<xsl:variable name="validationError.RequiredField" select="'Kenttä on pakollinen'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Kentän sisältö ei vastaa vaadittua muotoa'" />
	<xsl:variable name="validation.TooLong" select="'Kentän sisältö on liian pitkä'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Tuntematon virhe kentässä'" />
	<xsl:variable name="validationError.Other" select="'Hakupolku ei ole kelvollinen'" />	
	
	<xsl:variable name="validationError.field.name" select="'nimi'" />
	<xsl:variable name="validationError.field.description" select="'kuvaus'" />
	<xsl:variable name="validationError.field.commentText" select="'kommentti'" />
	<xsl:variable name="validationError.field.url" select="'polku'" />
	
	<xsl:variable name="validationError.messageKey.BadFileFormat" select="'Tiedoston muoto ei kelpaa'" />
	<!-- TODO: NoImage language variable need to bee translated -->
	<xsl:variable name="validationError.messageKey.NoImage" select="'Tuntematon virhe'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Tuntematon virhe'" />				

</xsl:stylesheet>
