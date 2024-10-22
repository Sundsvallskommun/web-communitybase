<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="NewsLetterModuleTemplates.xsl" />
	
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.en.xsl" />
	
	<xsl:variable name="newNewsLetterText">New newsletter: </xsl:variable>
	<xsl:variable name="updateNewsletterBreadCrumb">Change newsletter</xsl:variable>
	<xsl:variable name="readReceiptBreadCrumb">Read receipt</xsl:variable>
	<xsl:variable name="addNewsletterBreadCrumb">Add newsletter</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'for'" />
	<xsl:variable name="newslettermodule.noNewsletters.header" select="'No new newsletters'" />
	<xsl:variable name="newslettermodule.readreceipt.title" select="'Show read receipt for'" />
	<xsl:variable name="newslettermodule.update.title" select="'Edit newsletter'" />
	<xsl:variable name="newslettermodule.delete.title" select="'Delete newsletter'" />
	<xsl:variable name="newslettermodule.delete.confirm" select="'Are you sure you want to delete the newsletter'" />
	<xsl:variable name="newslettermodule.postedBy" select="'Submitted by'" />
	<xsl:variable name="newslettermodule.deletedUser" select="'Deleted user'" />
	<xsl:variable name="newslettermodule.noNewsletters" select="'At the moment there are no newsletters'" />	
	<xsl:variable name="newslettermodule.submit" select="'Add newsletter'" />	
	<xsl:variable name="newslettermodule.moreNewsletters" select="'More newsletters for'" />	
	<xsl:variable name="newslettermodule.information" select="'To switch between the newsletters below, double click on the desired newsletter, or highlight and click the &quot;Show newsletter&quot; button'" />
	<xsl:variable name="newslettermodule.showNewsletter" select="'Show newsletter'" />
	
	<xsl:variable name="newsletter.title" select="'Titel'" />
	<xsl:variable name="newsletter.content" select="'Content'" />
	<xsl:variable name="newsletter.imagelocation" select="'Place image'" />
	<xsl:variable name="newsletter.imagelocation.above" select="'Above the content'" />
	<xsl:variable name="newsletter.imagelocation.below" select="'Below the content'" />
	<xsl:variable name="newsletter.notrequired" select="'Optional'" />
	
	<xsl:variable name="addNewsletter.header" select="'Add newsletter'" />
	<xsl:variable name="addNewsletter.uploadimage" select="'Upload image'" />
	<xsl:variable name="addNewsletter.submit" select="'Add newsletter'" />
	
	<xsl:variable name="updateNewsletter.header" select="'Edit newsletter'" />
	<xsl:variable name="updateNewsletter.changeimage" select="'Change image'" />
	<xsl:variable name="updateNewsletter.newimage" select="'Place new image'" />
	<xsl:variable name="updateNewsletter.currentimage.imagelocation" select="'Place current image'" />
	<xsl:variable name="updateNewsletter.currentimage.delete" select="'Delete current image'" />
	<xsl:variable name="updateNewsletter.submit" select="'Save changes'" />
	
	<xsl:variable name="showReadReceipt.header" select="'Read receipt for the newsletter'" />
	<xsl:variable name="showReadReceipt.summary.part1" select="'A total of'" />
	<xsl:variable name="showReadReceipt.summary.part2" select="'users have read the newsletter'" />
	<xsl:variable name="showReadReceipt.name" select="'Name'" />
	<xsl:variable name="showReadReceipt.firstread" select="'First read'" />
	<xsl:variable name="showReadReceipt.lastread" select="'Last read'" />
	<xsl:variable name="showReadReceipt.noreceipt" select="'No users have read the newsletter'" />
	
	<xsl:variable name="validationError.RequiredField" select="'You have to fill in the field'" />
	<xsl:variable name="validationError.InvalidFormat" select="'The field has an invalid value'" />
	<xsl:variable name="validation.TooShort" select="'The content in the field is too short'" />
	<xsl:variable name="validation.TooLong" select="'The content in the field is too long'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'The field has an unknown error'" />
	<xsl:variable name="validationError.Other" select="'The path is invalid, please edit the field'" />
	
	<xsl:variable name="validationError.field.title" select="'Title'" />
	<xsl:variable name="validationError.field.description" select="'description'" />
	<xsl:variable name="validationError.field.commentText" select="'comment'" />
	<xsl:variable name="validationError.field.url" select="'path'" />
	<xsl:variable name="validationError.field.content" select="'content'" />
	
	<xsl:variable name="validationError.messageKey.BadFileFormat" select="'The file format of the file is invalid'" />
	<xsl:variable name="validationError.messageKey.NoFile" select="'You have not selected an image'" />
	<xsl:variable name="validationError.messageKey.InvalidContent" select="'The content has invalid characters'" />
	<xsl:variable name="validationError.messageKey.UnableToParseRequest" select="'An error has occured when uploading the image'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'An unknown error has occured'" />				

</xsl:stylesheet>
