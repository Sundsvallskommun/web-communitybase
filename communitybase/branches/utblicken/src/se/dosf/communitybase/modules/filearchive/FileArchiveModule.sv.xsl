<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="FileArchiveModuleTemplates.xsl" />
	
	<xsl:variable name="newFileText">Ny fil: </xsl:variable>
	<xsl:variable name="addSectionBreadcrumb">Lägg till kategori </xsl:variable>
	<xsl:variable name="updateGroupCategoryBreadcrumb">Uppdatera gruppkategori </xsl:variable>
	<xsl:variable name="updateSchoolCategoryBreadcrumb">Uppdatera skolkategori </xsl:variable>
	<xsl:variable name="addFileBreadcrumb">Lägg till fil </xsl:variable>
	<xsl:variable name="updateFileBreadcrumb">Uppdatera fil</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="fileArchiveModule.header" select="'för'" />
	
	<xsl:variable name="fileArchiveModule.newfile.description" select="'Ny fil sedan du besökte filarkivet senast'" />
	<xsl:variable name="fileArchiveModule.nofiles" select="'Det finns inga filer för'" />
	
	<xsl:variable name="groupFiles.header" select="'Filer för'" />
	<xsl:variable name="groupFiles.help" select="'För att kunna ladda upp gruppfiler måste du först lägga till en kategori'" />
	<xsl:variable name="groupFiles.addcategory" select="'Lägg till ny gruppkategori'" />
	
	<xsl:variable name="schoolFiles.header" select="'Filer för'" />
	<xsl:variable name="schoolFiles.help" select="'För att kunna ladda upp skolfiler måste du först lägga till en kategori'" />
	<xsl:variable name="schoolFiles.addcategory" select="'Lägg till ny skolkategori'" />
	
	<xsl:variable name="section.delete.confirm" select="'Ta bort kategorin'" />
	<xsl:variable name="section.nofiles" select="'Det finns för närvarande inga filer här'" />
	<xsl:variable name="section.addfile" select="'Lägg till ny fil'" />
	
	<xsl:variable name="file.delete.confirm" select="'Ta bort filen'" />
	<xsl:variable name="file.delete.title" select="'Ta bort fil'" />
	<xsl:variable name="file.edit.title" select="'Redigera fil'" />
	<xsl:variable name="file.addedby" select="'Inlagd av'" />
	<xsl:variable name="file.userdeleted" select="'Borttagen användare'" />
	<xsl:variable name="file.time" select="'klockan'" />
	
	<xsl:variable name="addSection.header" select="'Lägg till kategori'" />
	<xsl:variable name="addSection.name" select="'Namn'" />
	<xsl:variable name="addGroupSection.submit" select="'Lägg till gruppkategori'" />
	<xsl:variable name="addSchoolSection.submit" select="'Lägg till skolkategori'" />
	
	<xsl:variable name="updateSection.name" select="'Namn'" />
	<xsl:variable name="updateSection.submit" select="'Spara ändringar'" />
	<xsl:variable name="updateGroupSection.header" select="'Uppdatera gruppkategori'" />
	<xsl:variable name="updateSchoolSection.header" select="'Uppdatera skolkategori'" />
	
	<xsl:variable name="addFile.header" select="'Lägg till fil'" />
	<xsl:variable name="addFile.allowedfiles" select="'Tillåtna filtyper'" />
	<xsl:variable name="addFile.maximumsize" select="'Största tillåtna filstorlek är'" />
	<xsl:variable name="addFile.mb" select="'MB'" />
	<xsl:variable name="addFile.file" select="'Fil'" />
	<xsl:variable name="addFile.description" select="'Beskrivning (valfritt)'" />
	<xsl:variable name="addFile.submit" select="'Lägg till fil'" />
	
	<xsl:variable name="updateFile.header" select="'Uppdatera'" />
	<xsl:variable name="updateFile.name" select="'Namn'" />
	<xsl:variable name="updateFile.description" select="'Beskrivning (valfritt)'" />
	<xsl:variable name="updateFile.category" select="'Kategori'" />
	<xsl:variable name="updateFile.submit" select="'Spara ändringar'" />
	
	<xsl:variable name="validationError.RequiredField" select="'Du måste fylla i fältet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt värde på fältet'" />
	<xsl:variable name="validationError.TooLong" select="'För stort värde på fältet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Okänt fel på fältet'" />
	<xsl:variable name="validationError.field.name" select="'namn'" />
	<xsl:variable name="validationError.field.description" select="'beskrivning'" />
	<xsl:variable name="validationError.messageKey.InvalidFileFormat" select="'Felaktigt filformat'" />
	<xsl:variable name="validationError.messageKey.NoFileAttached" select="'Du måste bifoga en fil'" />
	<xsl:variable name="validationError.messageKey.FileTooBig" select="'Filen du valt att ladda upp är för stor'" />
	<xsl:variable name="validationError.messageKey.FileTooSmall" select="'Filen du valt att ladda upp är för liten'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett okänt fel har uppstått'" />				

</xsl:stylesheet>