<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="PictureGalleryModuleTemplates.xsl" />
	
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.sv.xsl" />
	
	<!-- Breadcrumbs -->
	<xsl:variable name="addGalleryBreadCrumb">Lägg till album</xsl:variable>
	<xsl:variable name="editGalleryBreadCrumb">Ändra album</xsl:variable>
	<xsl:variable name="addImagesBreadCrumb">Lägg till bilder</xsl:variable>
	<xsl:variable name="newGalleryText">Nytt album: </xsl:variable>
	<xsl:variable name="updateGalleryBreadCrumb">Ändra album</xsl:variable>
	<xsl:variable name="addPictureBreadCrumb">Lägg till bilder</xsl:variable>
	<xsl:variable name="newPicturesInGalleryText">Nya bilder i album: </xsl:variable>
	<xsl:variable name="othersCategory">Övriga</xsl:variable>
	
	
	<!-- i18n indirect phrases -->
	<xsl:variable name="i18n.multiUploader.language">language.sv.js</xsl:variable>
	<xsl:variable name="i18n.multiUploader.uploadbutton">upload.sv.png</xsl:variable>
	<xsl:variable name="i18n.album.newNotification">Nytt album sedan du besökte bildarkivet senast</xsl:variable>
	<xsl:variable name="i18n.picture.newNotification">Ny bild sedan du besökte bildarkivet senast</xsl:variable>
	<xsl:variable name="i18n.album.deleteConfirmation">Är du säker på att du vill ta bort albumet</xsl:variable>
	<xsl:variable name="i18n.picture.deleteConfirmation">Är du säker på att du vill ta bort bilden</xsl:variable>
	<xsl:variable name="i18n.album.publishTo">Gör albumet synligt för</xsl:variable>
	<xsl:variable name="i18n.noaccess">Du har inte rättighet till någon förskola</xsl:variable>
	<xsl:variable name="i18n.requireAFP">För att ladda upp flera bilder samtidigt krävs Adobe Flash Player version</xsl:variable>
	<xsl:variable name="i18n.downloadAFPorUploadPictures">för att ladda ner senaste versionen av Adobe Flash Player eller ladda upp en bild/zip-fil nedan</xsl:variable>
	<xsl:variable name="i18n.uploadPicturesOrZip">Ladda upp bild/bilder (zip fil eller enskild bild)</xsl:variable>
	
	
	<!-- i18n translations -->
	<xsl:variable name="i18n.for">för</xsl:variable>
	<xsl:variable name="i18n.AddAlbum">Lägg till album</xsl:variable>
	<xsl:variable name="i18n.DeleteAlbum">Ta bort albumet</xsl:variable>
	<xsl:variable name="i18n.EditAlbum">Ändra albumet</xsl:variable>
	<xsl:variable name="i18n.DownloadAlbum">Ladda ner albumet</xsl:variable>
	<xsl:variable name="i18n.asAZipFile">som en zip-fil</xsl:variable>
	<xsl:variable name="i18n.ShownFor">Visas för</xsl:variable>
	<xsl:variable name="i18n.ShownAlbum">Visa albumet</xsl:variable>
	<xsl:variable name="i18n.pictures">bilder</xsl:variable>
	<xsl:variable name="i18n.AddPicturesToAlbum">Lägg till bilder i albumet</xsl:variable>
	<xsl:variable name="i18n.ClearList">Töm listan</xsl:variable>
	<xsl:variable name="i18n.CancelUpload">Avbryt uppladdning</xsl:variable>
	<xsl:variable name="i18n.UploadImages">Ladda upp bild(er)</xsl:variable>
	<xsl:variable name="i18n.SelectedImagesOnQueue">Valda bilder på kö</xsl:variable>
	<xsl:variable name="i18n.Back">Tillbaka</xsl:variable>
	<xsl:variable name="i18n.DeletePicture">Ta bort bilden</xsl:variable>
	<xsl:variable name="i18n.picture">bild</xsl:variable>
	<xsl:variable name="i18n.of">av</xsl:variable>
	<xsl:variable name="i18n.Previous">Föregående</xsl:variable>
	<xsl:variable name="i18n.Next">Nästa</xsl:variable>
	<xsl:variable name="i18n.SmallPictures">Små bilder</xsl:variable>
	<xsl:variable name="i18n.UpdateComment">Uppdatera kommentaren</xsl:variable>
	<xsl:variable name="i18n.DeleteComment">Ta bort kommentaren</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.AnonymousUser">Anonym användare</xsl:variable>
	<xsl:variable name="i18n.Name">Namn</xsl:variable>
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.CreateAlbum">Skapa album</xsl:variable>
	<xsl:variable name="i18n.orLater">eller senare</xsl:variable>
	<xsl:variable name="i18n.Click">Klicka</xsl:variable>
	<xsl:variable name="i18n.here">här</xsl:variable>
	<xsl:variable name="i18n.AddImages">Lägg till bild(er)</xsl:variable>
	<xsl:variable name="i18n.Page">Sida</xsl:variable>
	<xsl:variable name="i18n.AddImagesSimple">Lägg till bilder (enkel)</xsl:variable>
	<xsl:variable name="i18n.AddImagesAdvanced">Lägg till bilder (avancerad)</xsl:variable>
	<xsl:variable name="i18n.ShowAllAlbums">Visa alla album</xsl:variable>
	<xsl:variable name="i18n.NoPicturesInAlbum">Det finns inga bilder i album</xsl:variable>
	<xsl:variable name="i18n.NoPictures">Det finns inga bilder</xsl:variable>
	<xsl:variable name="i18n.ShowLargerPicture">Visa större bild</xsl:variable>
	<xsl:variable name="i18n.AllPictures">Alla bilder</xsl:variable>
	<!-- 
	<xsl:variable name="i18n.RemovedUser">Borttagen användare</xsl:variable>
	 -->
	<xsl:variable name="i18n.Comments">Kommentarer</xsl:variable>
	<xsl:variable name="i18n.Comment">Kommentar</xsl:variable>
	<xsl:variable name="i18n.HideComments">Dölj kommentarer</xsl:variable>
	<xsl:variable name="i18n.ShowComments">Visa kommentarer</xsl:variable>
	<xsl:variable name="i18n.NoCommentsForThisPicture">Det finns inga kommentarer för denna bild</xsl:variable>
	<xsl:variable name="i18n.AddComment">Lägg till kommentar</xsl:variable>
	<xsl:variable name="i18n.Filename">Filnamn</xsl:variable>
	<xsl:variable name="i18n.Posted">Datum</xsl:variable>
	<xsl:variable name="i18n.Album">Album</xsl:variable>
	<xsl:variable name="i18n.AlphaToOmega">A till Ö</xsl:variable>
	<xsl:variable name="i18n.OmegaToAlpha">Ö till A</xsl:variable>
	<xsl:variable name="i18n.NewestFirst">Senast först</xsl:variable>
	<xsl:variable name="i18n.OldestFirst">Äldst först</xsl:variable>
	<xsl:variable name="i18n.BelongsTo">Tillhör</xsl:variable>
	<xsl:variable name="i18n.DownloadAllPictures">Ladda ned alla biler</xsl:variable>
	<xsl:variable name="i18n.Created">Skapat</xsl:variable>
	<xsl:variable name="i18n.atTime">klockan</xsl:variable>
	<xsl:variable name="i18n.UploadedBy">Uppladdad av</xsl:variable>
	<xsl:variable name="i18n.OrderBy">Sortera på</xsl:variable>
	<xsl:variable name="i18n.Category">Kategori</xsl:variable>
	<xsl:variable name="i18n.NewCategory">Ny kategori</xsl:variable>
	<xsl:variable name="i18n.EmptyCategory">Ingen kategori</xsl:variable>

	
	<!-- Validation -->
	<xsl:variable name="validationError.messageKey.InvalidGallery">Efterfrågat album finns ej, eller är ej tillgängligt</xsl:variable> 
	<xsl:variable name="validationError.RequiredField" select="'Du måste fylla i fältet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt format på fältet'" />
	<xsl:variable name="validation.TooLong" select="'För långt innehåll i fältet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Okänt fel på fältet'" />
	<xsl:variable name="validationError.Other" select="'Sökvägen är inte giltig, ändra fältet'" />	
	
	<xsl:variable name="validationError.field.name" select="'namn'" />
	<xsl:variable name="validationError.field.description" select="'beskrivning'" />
	<xsl:variable name="validationError.field.commentText" select="'kommentar'" />
	<xsl:variable name="validationError.field.url" select="'sökväg'" />
	
	<xsl:variable name="validationError.messageKey.NoGroup" select="'Du måste välja minst en grupp att publicera till'" />
	<xsl:variable name="validationError.messageKey.BadFileFormat" select="'Filen har felaktigt filformat'" />
	<xsl:variable name="validationError.messageKey.NoImage" select="'Du måste välja en bild eller zip-fil med bilder att ladda upp'" />	
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett okänt fel har uppstått'" />

</xsl:stylesheet>