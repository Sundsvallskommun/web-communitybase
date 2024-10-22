<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="PictureGalleryModuleTemplates.xsl" />
	
	<xsl:variable name="addGalleryBreadCrumb">Add album</xsl:variable>
	<xsl:variable name="editGalleryBreadCrumb">Edit album</xsl:variable>
	<xsl:variable name="addImagesBreadCrumb">Add images</xsl:variable>
	<xsl:variable name="newGalleryText">New album: </xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'for'" />
	
	<xsl:variable name="multiUploader.language" select="'language.en.js'" />
	<xsl:variable name="multiUploader.uploadbutton" select="'upload.en.png'" />
	<xsl:variable name="multiUploader.header" select="'Add images to the gallery'" />
	<xsl:variable name="multiUploader.queue" select="'Selected images in queue'" />
	<xsl:variable name="multiUploader.information" select="'Select the images that you want to upload to the gallery, and click on the &quot;Upload image(s)&quot; button to start the upload'" />
	<xsl:variable name="multiUploader.noimages" select="'At the moment there are no images in the queue'" />
	<xsl:variable name="multiUploader.nouploaded" select="'There are no uploaded images'" />
	<xsl:variable name="multiUploader.clearlist" select="'Clear list'" />
	<xsl:variable name="multiUploader.btnSubmit" select="'Upload image(s)'" />
	<xsl:variable name="multiUploader.btnCancel" select="'Cancel upload'" />
	<xsl:variable name="multiUploader.btnDone" select="'Back'" />
	
	<xsl:variable name="addImages.header" select="'Add images to the gallery'" />
	<xsl:variable name="addImages.information.part1" select="'You need Adobe Flash Player to be able to upload multiple images.'" />
	<xsl:variable name="addImages.information.part2" select="'to download Adobe Flash Player or upload an image/zip file below'" />
	<xsl:variable name="addImages.information.click" select="'Click'" />
	<xsl:variable name="addImages.information.link" select="'here'" />
	<xsl:variable name="addImages.upload" select="'Upload image(s)(zip file or a single image)'" />
	<xsl:variable name="addImages.submit" select="'Add image(s)'" />
	<xsl:variable name="addImages.diskThreshold" select="'Maximum file size allowed'"/>

	
	<xsl:variable name="pictureGallery.header" select="'Gallery for'" />
	<xsl:variable name="pictureGallery.new" select="'New galleries since you last visited the image gallery'" />
	<xsl:variable name="pictureGallery.group.noalbum" select="'At the moment there are no galleries in the group'" />
	<xsl:variable name="pictureGallery.group.add" select="'Add gallery to'" />
	<xsl:variable name="pictureGallery.school.add" select="'Add gallery on'" />
	<xsl:variable name="pictureGallery.school.noalbum" select="'At the moment there are no galleries in the school'" />
	
	<xsl:variable name="pictureGallery.update.title" select="'Edit gallery'" />
	<xsl:variable name="pictureGallery.delete.title" select="'Delete gallery'" />
	<xsl:variable name="pictureGallery.delete.confirm" select="'Are you sure you want to delete the gallery'" />
	<xsl:variable name="pictureGallery.show.title" select="'Show gallery'" />
	<xsl:variable name="pictureGallery.images" select="'images'" />
	
	<xsl:variable name="pictureGallery.downloadGroupGallery.part1" select="'Download gallery'" />
	<xsl:variable name="pictureGallery.downloadGroupGallery.part2" select="'as a zip-file'" />
	
	<xsl:variable name="showGallery.pictures" select="'images'" />
	<xsl:variable name="showGallery.page" select="'Page'" />
	<xsl:variable name="showGallery.pagecount" select="'of'" />
	<xsl:variable name="showGallery.previousLink.title" select="'Previous'" />
	<xsl:variable name="showGallery.previousImage.alt" select="'Previous'" />
	<xsl:variable name="showGallery.previousLink.text" select="'Previous'" />
	<xsl:variable name="showGallery.nextLink.title" select="'Next'" />
	<xsl:variable name="showGallery.nextLink.text" select="'Next'" />	
	<xsl:variable name="showGallery.nextImage.alt" select="'Next'" />
	<xsl:variable name="showGallery.addImagesLinkSimple.title" select="'Add images (standard)'" />
	<xsl:variable name="showGallery.addImagesLinkSimple.text" select="'Add images (standard)'" />
	<xsl:variable name="showGallery.addImagesLinkAdvanced.title" select="'Add images (advanced)'" />
	<xsl:variable name="showGallery.addImagesLinkAdvanced.text" select="'Add images (advanced)'" />
	<xsl:variable name="showGallery.showAllGalleriesLink.title" select="'Show all galleries'" />
	<xsl:variable name="showGallery.showAllGalleriesLink.text" select="'Show all galleries'" />
	<xsl:variable name="showGallery.noImagesInGallery" select="'There are no images in the gallery'" />
	
	<xsl:variable name="file.link.title" select="'Show bigger image'" />
	
	<xsl:variable name="showImage.pictures" select="'images'" />
	<xsl:variable name="showImage.deleteImageLink.title" select="'Delete image'" />
	<xsl:variable name="showImage.deleteImageLink.confirm" select="'Are you sure you want to delete the image'" />
	<xsl:variable name="showImage.picture" select="'image'" />
	<xsl:variable name="showImage.pictureCount" select="'of'" />
	<xsl:variable name="showImage.previousLink.title" select="'Previous'" />
	<xsl:variable name="showImage.previousImage.alt" select="'Previous'" />
	<xsl:variable name="showImage.previousLink.text" select="'Previous'" />
	<xsl:variable name="showImage.nextLink.title" select="'Next'" />
	<xsl:variable name="showImage.nextLink.text" select="'Next'" />	
	<xsl:variable name="showImage.nextImage.alt" select="'Next'" />	
	<xsl:variable name="showImage.showThumbsLink.title" select="'Thumbnails'" />
	<xsl:variable name="showImage.showThumbsLink.text" select="'Thumbnails'" />
	
	<xsl:variable name="gallery.file.showFullImageLink.title" select="'Show bigger image'" />
	<xsl:variable name="gallery.file.comments" select="'Comment'" />
	<xsl:variable name="gallery.file.hide.comments" select="'Hide commments'" />					
	<xsl:variable name="gallery.file.show.comments" select="'Show comments'" />
	<xsl:variable name="gallery.file.noComments" select="'There are no comments on this image'" />
	<xsl:variable name="gallery.file.addcomment" select="'Comment'" />
	<xsl:variable name="gallery.file.submit" select="'Add comment'" />
	
	<xsl:variable name="comment.updateCommentLink.title" select="'Update comment'" />
	<xsl:variable name="comment.deleteCommentLink.title" select="'Delete comment'" />
	<xsl:variable name="comment.submit" select="'Save changes'" />
	<xsl:variable name="comment.anonymousUser" select="'Anonymous user'" />
	
	<xsl:variable name="addGallery.header" select="'Add gallery for'" />
	<xsl:variable name="addGallery.group" select="'group'" />
	<xsl:variable name="addGallery.school" select="'School'" />
	<xsl:variable name="addGallery.name" select="'Name'" />
	<xsl:variable name="addGallery.description" select="'Description'" />
	<xsl:variable name="addGallery.submit" select="'Create gallery'" />
	

	<xsl:variable name="updateGallery.header" select="'Edit gallery'" />
	<xsl:variable name="updateGallery.name" select="'Name'" />
	<xsl:variable name="updateGallery.description" select="'Description'" />
	<xsl:variable name="updateGallery.submit" select="'Save changes'" />

	
	<xsl:variable name="validationError.RequiredField" select="'You have to fill in the field'" />
	<xsl:variable name="validationError.InvalidFormat" select="'The field has an invalid format'" />
	<xsl:variable name="validation.TooLong" select="'The content in the field is too long'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'The field has an unknown error'" />
	<xsl:variable name="validationError.Other" select="'The path is not valid, please edit the field'" />	
	
	<xsl:variable name="validationError.field.name" select="'name'" />
	<xsl:variable name="validationError.field.description" select="'description'" />
	<xsl:variable name="validationError.field.commentText" select="'comment'" />
	<xsl:variable name="validationError.field.url" select="'path'" />
	
	<xsl:variable name="validationError.messageKey.BadFileFormat" select="'The file has an invalid format'" />
	<xsl:variable name="validationError.messageKey.NoImage" select="'You have to choose an image or zip-file to upload'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'An unknown error has occured'" />				

</xsl:stylesheet>
