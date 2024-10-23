<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="FileArchiveModuleTemplates.xsl" />
	
	<xsl:variable name="newFileText">Ny fil: </xsl:variable>
	<xsl:variable name="addSectionBreadcrumb">L�gg till kategori </xsl:variable>
	<xsl:variable name="updateGroupCategoryBreadcrumb">Uppdatera gruppkategori </xsl:variable>
	<xsl:variable name="updateSchoolCategoryBreadcrumb">Uppdatera skolkategori </xsl:variable>
	<xsl:variable name="addFileBreadcrumb">L�gg till fil </xsl:variable>
	<xsl:variable name="updateFileBreadcrumb">Uppdatera fil</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="fileArchiveModule.header" select="'f�r'" />
	
	<xsl:variable name="fileArchiveModule.newfile.description" select="'Ny fil sedan du bes�kte filarkivet senast'" />
	<xsl:variable name="fileArchiveModule.nofiles" select="'Det finns inga filer f�r'" />
	
	<xsl:variable name="groupFiles.header" select="'Filer f�r'" />
	<xsl:variable name="groupFiles.help" select="'F�r att kunna ladda upp gruppfiler m�ste du f�rst l�gga till en kategori'" />
	<xsl:variable name="groupFiles.addcategory" select="'L�gg till ny gruppkategori'" />
	
	<xsl:variable name="schoolFiles.header" select="'Filer f�r'" />
	<xsl:variable name="schoolFiles.help" select="'F�r att kunna ladda upp skolfiler m�ste du f�rst l�gga till en kategori'" />
	<xsl:variable name="schoolFiles.addcategory" select="'L�gg till ny skolkategori'" />
	
	<xsl:variable name="section.delete.confirm" select="'Ta bort kategorin'" />
	<xsl:variable name="section.nofiles" select="'Det finns f�r n�rvarande inga filer h�r'" />
	<xsl:variable name="section.addfile" select="'L�gg till ny fil'" />
	
	<xsl:variable name="file.delete.confirm" select="'Ta bort filen'" />
	<xsl:variable name="file.delete.title" select="'Ta bort fil'" />
	<xsl:variable name="file.edit.title" select="'Redigera fil'" />
	<xsl:variable name="file.addedby" select="'Inlagd av'" />
	<xsl:variable name="file.userdeleted" select="'Borttagen anv�ndare'" />
	<xsl:variable name="file.time" select="'klockan'" />
	
	<xsl:variable name="addSection.header" select="'L�gg till kategori'" />
	<xsl:variable name="addSection.name" select="'Namn'" />
	<xsl:variable name="addGroupSection.submit" select="'L�gg till gruppkategori'" />
	<xsl:variable name="addSchoolSection.submit" select="'L�gg till skolkategori'" />
	
	<xsl:variable name="updateSection.name" select="'Namn'" />
	<xsl:variable name="updateSection.submit" select="'Spara �ndringar'" />
	<xsl:variable name="updateGroupSection.header" select="'Uppdatera gruppkategori'" />
	<xsl:variable name="updateSchoolSection.header" select="'Uppdatera skolkategori'" />
	
	<xsl:variable name="addFile.header" select="'L�gg till fil'" />
	<xsl:variable name="addFile.allowedfiles" select="'Till�tna filtyper'" />
	<xsl:variable name="addFile.maximumsize" select="'St�rsta till�tna filstorlek �r'" />
	<xsl:variable name="addFile.mb" select="'MB'" />
	<xsl:variable name="addFile.file" select="'Fil'" />
	<xsl:variable name="addFile.description" select="'Beskrivning (valfritt)'" />
	<xsl:variable name="addFile.submit" select="'L�gg till fil'" />
	
	<xsl:variable name="updateFile.header" select="'Uppdatera'" />
	<xsl:variable name="updateFile.name" select="'Namn'" />
	<xsl:variable name="updateFile.description" select="'Beskrivning (valfritt)'" />
	<xsl:variable name="updateFile.category" select="'Kategori'" />
	<xsl:variable name="updateFile.submit" select="'Spara �ndringar'" />
	
	<xsl:variable name="validationError.RequiredField" select="'Du m�ste fylla i f�ltet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt v�rde p� f�ltet'" />
	<xsl:variable name="validationError.TooLong" select="'F�r stort v�rde p� f�ltet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Ok�nt fel p� f�ltet'" />
	<xsl:variable name="validationError.field.name" select="'namn'" />
	<xsl:variable name="validationError.field.description" select="'beskrivning'" />
	<xsl:variable name="validationError.messageKey.InvalidFileFormat" select="'Felaktigt filformat'" />
	<xsl:variable name="validationError.messageKey.NoFileAttached" select="'Du m�ste bifoga en fil'" />
	<xsl:variable name="validationError.messageKey.FileTooBig" select="'Filen du valt att ladda upp �r f�r stor'" />
	<xsl:variable name="validationError.messageKey.FileTooSmall" select="'Filen du valt att ladda upp �r f�r liten'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett ok�nt fel har uppst�tt'" />				

</xsl:stylesheet>