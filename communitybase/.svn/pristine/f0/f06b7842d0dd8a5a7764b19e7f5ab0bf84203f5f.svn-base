<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="FileArchiveModuleTemplates.xsl"/>
	
	<xsl:variable name="newFileText">New file: </xsl:variable>
	<xsl:variable name="addSectionBreadcrumb">Add category </xsl:variable>
	<xsl:variable name="updateGroupCategoryBreadcrumb">Update group category </xsl:variable>
	<xsl:variable name="updateSchoolCategoryBreadcrumb">Update school category </xsl:variable>
	<xsl:variable name="addFileBreadcrumb">Add file </xsl:variable>
	<xsl:variable name="updateFileBreadcrumb">Update file</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="fileArchiveModule.header" select="'for'"/>
	
	<xsl:variable name="fileArchiveModule.newfile.description" select="'New files since you last visited the file archive'"/>
	<xsl:variable name="fileArchiveModule.nofiles" select="'There are no files for'"/>
	
	<xsl:variable name="groupFiles.header" select="'Files for'"/>
	<xsl:variable name="groupFiles.help" select="'To be able to upload group files, you first have to add a category'"/>
	<xsl:variable name="groupFiles.addcategory" select="'Add new group category'"/>
	
	<xsl:variable name="schoolFiles.header" select="'Files for'"/>
	<xsl:variable name="schoolFiles.help" select="'To be able to upload school files, you first have to add a category'"/>
	<xsl:variable name="schoolFiles.addcategory" select="'Add new school category'"/>
	
	<xsl:variable name="section.delete.confirm" select="'Delete the category'"/>
	<xsl:variable name="section.nofiles" select="'There are no files here at the moment'"/>
	<xsl:variable name="section.addfile" select="'Add new file'"/>
	
	<xsl:variable name="file.delete.confirm" select="'Delete file'"/>
	<xsl:variable name="file.delete.title" select="'Delete file'"/>
	<xsl:variable name="file.edit.title" select="'Edit file'"/>
	<xsl:variable name="file.addedby" select="'Submitted by'"/>
	<xsl:variable name="file.userdeleted" select="'Deleted user'"/>
	<xsl:variable name="file.time" select="'at'"/>
	
	<xsl:variable name="addSection.header" select="'Add category'"/>
	<xsl:variable name="addSection.name" select="'Name'"/>
	<xsl:variable name="addGroupSection.submit" select="'Add group category'"/>
	<xsl:variable name="addSchoolSection.submit" select="'Add school category'"/>
	
	<xsl:variable name="updateSection.name" select="'Name'"/>
	<xsl:variable name="updateSection.submit" select="'Save changes'"/>
	<xsl:variable name="updateGroupSection.header" select="'Update group category'"/>
	<xsl:variable name="updateSchoolSection.header" select="'Update school category'"/>
	
	<xsl:variable name="addFile.header" select="'Add file'"/>
	<xsl:variable name="addFile.allowedfiles" select="'Allowed file types'"/>
	<xsl:variable name="addFile.maximumsize" select="'Maximum file size allowed is'"/>
	<xsl:variable name="addFile.mb" select="'MB'"/>
	<xsl:variable name="addFile.file" select="'File'"/>
	<xsl:variable name="addFile.description" select="'Description (optional)'"/>
	<xsl:variable name="addFile.submit" select="'Add file'"/>
	
	<xsl:variable name="updateFile.header" select="'Update'"/>
	<xsl:variable name="updateFile.name" select="'Name'"/>
	<xsl:variable name="updateFile.description" select="'Description (optional)'"/>
	<xsl:variable name="updateFile.category" select="'Category'"/>
	<xsl:variable name="updateFile.submit" select="'Save changes'"/>
	
	<xsl:variable name="validationError.RequiredField" select="'You have to fill in the field'"/>
	<xsl:variable name="validationError.InvalidFormat" select="'The value of the field is incorrect'"/>
	<xsl:variable name="validationError.TooLong" select="'The value of the field is too big'"/>
	<xsl:variable name="validationError.unknownValidationErrorType" select="'The field has an unknown error'"/>
	<xsl:variable name="validationError.field.name" select="'name'"/>
	<xsl:variable name="validationError.field.description" select="'description'"/>
	<xsl:variable name="validationError.messageKey.InvalidFileFormat" select="'Incorrect file name'"/>
	<xsl:variable name="validationError.messageKey.NoFileAttached" select="'You have to attach a file'"/>
	<xsl:variable name="validationError.messageKey.FileTooBig" select="'The file you are trying to upload is too big'"/>
	<xsl:variable name="validationError.messageKey.FileTooSmall" select="'The file you are trying to upload is too small'"/>
	<xsl:variable name="validationError.unknownMessageKey" select="'An unknown error has occured'"/>				

<xsl:variable name="i18n.for">i18n.for</xsl:variable>
<xsl:variable name="i18n.file.newNotification">i18n.file.newNotification</xsl:variable>
<xsl:variable name="i18n.AddSection">i18n.AddSection</xsl:variable>
<xsl:variable name="i18n.DeleteSection">i18n.DeleteSection</xsl:variable>
<xsl:variable name="i18n.ShownFor">i18n.ShownFor</xsl:variable>
<xsl:variable name="i18n.NoFiles">i18n.NoFiles</xsl:variable>
<xsl:variable name="i18n.AddFile">i18n.AddFile</xsl:variable>
<xsl:variable name="i18n.Name">i18n.Name</xsl:variable>
<xsl:variable name="i18n.section.publishTo">i18n.section.publishTo</xsl:variable>
<xsl:variable name="i18n.noaccess">i18n.noaccess</xsl:variable>
<xsl:variable name="i18n.EditSection">i18n.EditSection</xsl:variable>
<xsl:variable name="i18n.SaveChanges">i18n.SaveChanges</xsl:variable>
<xsl:variable name="i18n.AllFiles">i18n.AllFiles</xsl:variable>
<xsl:variable name="i18n.in">i18n.in</xsl:variable>
<xsl:variable name="i18n.File">i18n.File</xsl:variable>
<xsl:variable name="i18n.AllowedFiletypes">i18n.AllowedFiletypes</xsl:variable>
<xsl:variable name="i18n.MaxFilesizeIs">i18n.MaxFilesizeIs</xsl:variable>
<xsl:variable name="i18n.MB">i18n.MB</xsl:variable>
<xsl:variable name="i18n.Description">i18n.Description</xsl:variable>
<xsl:variable name="i18n.optional">i18n.optional</xsl:variable>
<xsl:variable name="i18n.EditFile">i18n.EditFile</xsl:variable>
<xsl:variable name="i18n.Section">i18n.Section</xsl:variable>
<xsl:variable name="i18n.DeleteFile">i18n.DeleteFile</xsl:variable>
<xsl:variable name="i18n.BelongsTo">i18n.BelongsTo</xsl:variable>
<xsl:variable name="i18n.AddedBy">i18n.AddedBy</xsl:variable>
<xsl:variable name="i18n.RemovedUser">i18n.RemovedUser</xsl:variable>
<xsl:variable name="i18n.atTime">i18n.atTime</xsl:variable>
<xsl:variable name="i18n.Filename">i18n.Filename</xsl:variable>
<xsl:variable name="i18n.Posted">i18n.Posted</xsl:variable>
<xsl:variable name="i18n.OrderBy">i18n.OrderBy</xsl:variable>
<xsl:variable name="i18n.AlphaToOmega">i18n.AlphaToOmega</xsl:variable>
<xsl:variable name="i18n.NewestFirst">i18n.NewestFirst</xsl:variable>
<xsl:variable name="i18n.OmegaToAlpha">i18n.OmegaToAlpha</xsl:variable>
<xsl:variable name="i18n.OldestFirst">i18n.OldestFirst</xsl:variable>
<xsl:variable name="i18n.validationError.RequiredField">i18n.validationError.RequiredField</xsl:variable>
<xsl:variable name="i18n.validationError.InvalidFormat">i18n.validationError.InvalidFormat</xsl:variable>
<xsl:variable name="i18n.validationError.TooShort">i18n.validationError.TooShort</xsl:variable>
<xsl:variable name="i18n.validationError.TooLong">i18n.validationError.TooLong</xsl:variable>
<xsl:variable name="i18n.validationError.Other">i18n.validationError.Other</xsl:variable>
<xsl:variable name="i18n.validationError.unknownValidationErrorType">i18n.validationError.unknownValidationErrorType</xsl:variable>
<xsl:variable name="i18n.validationError.field.name">i18n.validationError.field.name</xsl:variable>
<xsl:variable name="i18n.validationError.field.description">i18n.validationError.field.description</xsl:variable>
<xsl:variable name="i18n.validationError.BadFileFormat">i18n.validationError.BadFileFormat</xsl:variable>
<xsl:variable name="i18n.validationError.NoFile">i18n.validationError.NoFile</xsl:variable>
<xsl:variable name="i18n.validationError.FileTooSmall">i18n.validationError.FileTooSmall</xsl:variable>
<xsl:variable name="i18n.validationError.RequestedSectionNotFound">i18n.validationError.RequestedSectionNotFound</xsl:variable>
<xsl:variable name="i18n.validationError.FileSizeLimitExceeded">i18n.validationError.FileSizeLimitExceeded</xsl:variable>
<xsl:variable name="i18n.validationError.NoGroup">i18n.validationError.NoGroup</xsl:variable>
<xsl:variable name="i18n.validationError.UnableToParseRequest">i18n.validationError.UnableToParseRequest</xsl:variable>
<xsl:variable name="i18n.validationError.unknownMessageKey">i18n.validationError.unknownMessageKey</xsl:variable>
</xsl:stylesheet>
