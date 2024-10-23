<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="PictureGalleryModuleTemplates.xsl" />
	
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.sv.xsl" />
	
	<!-- Breadcrumbs -->
	<xsl:variable name="addGalleryBreadCrumb">L�gg till album</xsl:variable>
	<xsl:variable name="editGalleryBreadCrumb">�ndra album</xsl:variable>
	<xsl:variable name="addImagesBreadCrumb">L�gg till bilder</xsl:variable>
	<xsl:variable name="newGalleryText">Nytt album: </xsl:variable>
	<xsl:variable name="updateGalleryBreadCrumb">�ndra album</xsl:variable>
	<xsl:variable name="addPictureBreadCrumb">L�gg till bilder</xsl:variable>
	<xsl:variable name="newPicturesInGalleryText">Nya bilder i album: </xsl:variable>
	<xsl:variable name="othersCategory">�vriga</xsl:variable>
	
	
	<!-- i18n indirect phrases -->
	<xsl:variable name="i18n.multiUploader.language">language.sv.js</xsl:variable>
	<xsl:variable name="i18n.multiUploader.uploadbutton">upload.sv.png</xsl:variable>
	<xsl:variable name="i18n.album.newNotification">Nytt album sedan du bes�kte bildarkivet senast</xsl:variable>
	<xsl:variable name="i18n.picture.newNotification">Ny bild sedan du bes�kte bildarkivet senast</xsl:variable>
	<xsl:variable name="i18n.album.deleteConfirmation">�r du s�ker p� att du vill ta bort albumet</xsl:variable>
	<xsl:variable name="i18n.picture.deleteConfirmation">�r du s�ker p� att du vill ta bort bilden</xsl:variable>
	<xsl:variable name="i18n.album.publishTo">G�r albumet synligt f�r</xsl:variable>
	<xsl:variable name="i18n.noaccess">Du har inte r�ttighet till n�gon f�rskola</xsl:variable>
	<xsl:variable name="i18n.requireAFP">F�r att ladda upp flera bilder samtidigt kr�vs Adobe Flash Player version</xsl:variable>
	<xsl:variable name="i18n.downloadAFPorUploadPictures">f�r att ladda ner senaste versionen av Adobe Flash Player eller ladda upp en bild/zip-fil nedan</xsl:variable>
	<xsl:variable name="i18n.uploadPicturesOrZip">Ladda upp bild/bilder (zip fil eller enskild bild)</xsl:variable>
	
	
	<!-- i18n translations -->
	<xsl:variable name="i18n.for">f�r</xsl:variable>
	<xsl:variable name="i18n.AddAlbum">L�gg till album</xsl:variable>
	<xsl:variable name="i18n.DeleteAlbum">Ta bort albumet</xsl:variable>
	<xsl:variable name="i18n.EditAlbum">�ndra albumet</xsl:variable>
	<xsl:variable name="i18n.DownloadAlbum">Ladda ner albumet</xsl:variable>
	<xsl:variable name="i18n.asAZipFile">som en zip-fil</xsl:variable>
	<xsl:variable name="i18n.ShownFor">Visas f�r</xsl:variable>
	<xsl:variable name="i18n.ShownAlbum">Visa albumet</xsl:variable>
	<xsl:variable name="i18n.pictures">bilder</xsl:variable>
	<xsl:variable name="i18n.AddPicturesToAlbum">L�gg till bilder i albumet</xsl:variable>
	<xsl:variable name="i18n.ClearList">T�m listan</xsl:variable>
	<xsl:variable name="i18n.CancelUpload">Avbryt uppladdning</xsl:variable>
	<xsl:variable name="i18n.UploadImages">Ladda upp bild(er)</xsl:variable>
	<xsl:variable name="i18n.SelectedImagesOnQueue">Valda bilder p� k�</xsl:variable>
	<xsl:variable name="i18n.Back">Tillbaka</xsl:variable>
	<xsl:variable name="i18n.DeletePicture">Ta bort bilden</xsl:variable>
	<xsl:variable name="i18n.picture">bild</xsl:variable>
	<xsl:variable name="i18n.of">av</xsl:variable>
	<xsl:variable name="i18n.Previous">F�reg�ende</xsl:variable>
	<xsl:variable name="i18n.Next">N�sta</xsl:variable>
	<xsl:variable name="i18n.SmallPictures">Sm� bilder</xsl:variable>
	<xsl:variable name="i18n.UpdateComment">Uppdatera kommentaren</xsl:variable>
	<xsl:variable name="i18n.DeleteComment">Ta bort kommentaren</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.AnonymousUser">Anonym anv�ndare</xsl:variable>
	<xsl:variable name="i18n.Name">Namn</xsl:variable>
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.CreateAlbum">Skapa album</xsl:variable>
	<xsl:variable name="i18n.orLater">eller senare</xsl:variable>
	<xsl:variable name="i18n.Click">Klicka</xsl:variable>
	<xsl:variable name="i18n.here">h�r</xsl:variable>
	<xsl:variable name="i18n.AddImages">L�gg till bild(er)</xsl:variable>
	<xsl:variable name="i18n.Page">Sida</xsl:variable>
	<xsl:variable name="i18n.AddImagesSimple">L�gg till bilder (enkel)</xsl:variable>
	<xsl:variable name="i18n.AddImagesAdvanced">L�gg till bilder (avancerad)</xsl:variable>
	<xsl:variable name="i18n.ShowAllAlbums">Visa alla album</xsl:variable>
	<xsl:variable name="i18n.NoPicturesInAlbum">Det finns inga bilder i album</xsl:variable>
	<xsl:variable name="i18n.NoPictures">Det finns inga bilder</xsl:variable>
	<xsl:variable name="i18n.ShowLargerPicture">Visa st�rre bild</xsl:variable>
	<xsl:variable name="i18n.AllPictures">Alla bilder</xsl:variable>
	<!-- 
	<xsl:variable name="i18n.RemovedUser">Borttagen anv�ndare</xsl:variable>
	 -->
	<xsl:variable name="i18n.Comments">Kommentarer</xsl:variable>
	<xsl:variable name="i18n.Comment">Kommentar</xsl:variable>
	<xsl:variable name="i18n.HideComments">D�lj kommentarer</xsl:variable>
	<xsl:variable name="i18n.ShowComments">Visa kommentarer</xsl:variable>
	<xsl:variable name="i18n.NoCommentsForThisPicture">Det finns inga kommentarer f�r denna bild</xsl:variable>
	<xsl:variable name="i18n.AddComment">L�gg till kommentar</xsl:variable>
	<xsl:variable name="i18n.Filename">Filnamn</xsl:variable>
	<xsl:variable name="i18n.Posted">Datum</xsl:variable>
	<xsl:variable name="i18n.Album">Album</xsl:variable>
	<xsl:variable name="i18n.AlphaToOmega">A till �</xsl:variable>
	<xsl:variable name="i18n.OmegaToAlpha">� till A</xsl:variable>
	<xsl:variable name="i18n.NewestFirst">Senast f�rst</xsl:variable>
	<xsl:variable name="i18n.OldestFirst">�ldst f�rst</xsl:variable>
	<xsl:variable name="i18n.BelongsTo">Tillh�r</xsl:variable>
	<xsl:variable name="i18n.DownloadAllPictures">Ladda ned alla biler</xsl:variable>
	<xsl:variable name="i18n.Created">Skapat</xsl:variable>
	<xsl:variable name="i18n.atTime">klockan</xsl:variable>
	<xsl:variable name="i18n.UploadedBy">Uppladdad av</xsl:variable>
	<xsl:variable name="i18n.OrderBy">Sortera p�</xsl:variable>
	<xsl:variable name="i18n.Category">Kategori</xsl:variable>
	<xsl:variable name="i18n.NewCategory">Ny kategori</xsl:variable>
	<xsl:variable name="i18n.EmptyCategory">Ingen kategori</xsl:variable>

	
	<!-- Validation -->
	<xsl:variable name="validationError.messageKey.InvalidGallery">Efterfr�gat album finns ej, eller �r ej tillg�ngligt</xsl:variable> 
	<xsl:variable name="validationError.RequiredField" select="'Du m�ste fylla i f�ltet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt format p� f�ltet'" />
	<xsl:variable name="validation.TooLong" select="'F�r l�ngt inneh�ll i f�ltet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Ok�nt fel p� f�ltet'" />
	<xsl:variable name="validationError.Other" select="'S�kv�gen �r inte giltig, �ndra f�ltet'" />	
	
	<xsl:variable name="validationError.field.name" select="'namn'" />
	<xsl:variable name="validationError.field.description" select="'beskrivning'" />
	<xsl:variable name="validationError.field.commentText" select="'kommentar'" />
	<xsl:variable name="validationError.field.url" select="'s�kv�g'" />
	
	<xsl:variable name="validationError.messageKey.NoGroup" select="'Du m�ste v�lja minst en grupp att publicera till'" />
	<xsl:variable name="validationError.messageKey.BadFileFormat" select="'Filen har felaktigt filformat'" />
	<xsl:variable name="validationError.messageKey.NoImage" select="'Du m�ste v�lja en bild eller zip-fil med bilder att ladda upp'" />	
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett ok�nt fel har uppst�tt'" />

</xsl:stylesheet>