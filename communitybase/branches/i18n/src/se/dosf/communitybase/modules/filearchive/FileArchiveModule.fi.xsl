<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="FileArchiveModuleTemplates.xsl" />
	
	<xsl:variable name="newFileText">Uusi tiedosto: </xsl:variable>
	<xsl:variable name="addSectionBreadcrumb">Lis�� kategoria </xsl:variable>
	<xsl:variable name="updateGroupCategoryBreadcrumb">Muokkaa ryhm�kategoriaa </xsl:variable>
	<xsl:variable name="updateSchoolCategoryBreadcrumb">Muokkaa koulukategoriaa </xsl:variable>
	<xsl:variable name="addFileBreadcrumb">Lis�� tiedosto </xsl:variable>
	<xsl:variable name="updateFileBreadcrumb">P�ivit� tiedosto</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="fileArchiveModule.header" select="'kohteelle'" />
	
	<xsl:variable name="fileArchiveModule.newfile.description" select="'Uudet tiedostot edellisen k�yntisi j�lkeen'" />
	<xsl:variable name="fileArchiveModule.nofiles" select="'Tiedostoja ei ole kohteelle'" />
	
	<xsl:variable name="groupFiles.header" select="'Tiedostot ryhm�lle'" />
	<xsl:variable name="groupFiles.help" select="'L�hett��ksesi monta tiedostoa, sinun tulee lis�t� kategoria'" />
	<xsl:variable name="groupFiles.addcategory" select="'Lis�� ryhm�kategoria'" />
	
	<xsl:variable name="schoolFiles.header" select="'Tiedostot koululle'" />
	<xsl:variable name="schoolFiles.help" select="'L�hett��ksesi monta tiedostoa, sinun tulee lis�t� kategoria'" />
	<xsl:variable name="schoolFiles.addcategory" select="'Lis�� koulukategoria'" />
	
	<xsl:variable name="section.delete.confirm" select="'Poista kategoria'" />
	<xsl:variable name="section.nofiles" select="'Tiedostoja ei l�ydy'" />
	<xsl:variable name="section.addfile" select="'Lis�� uusi tiedosto'" />
	
	<xsl:variable name="file.delete.confirm" select="'Poista tiedosto'" />
	<xsl:variable name="file.delete.title" select="'Poista tiedosto'" />
	<xsl:variable name="file.edit.title" select="'Muokkaa tiedostoa'" />
	<xsl:variable name="file.addedby" select="'Lis�nnyt'" />
	<xsl:variable name="file.userdeleted" select="'Poistettu k�ytt�j�'" />
	<xsl:variable name="file.time" select="'kello'" />
	
	<xsl:variable name="addSection.header" select="'Lis�� kategoria'" />
	<xsl:variable name="addSection.name" select="'Nimi'" />
	<xsl:variable name="addGroupSection.submit" select="'Lis�� ryhm�kategoria'" />
	<xsl:variable name="addSchoolSection.submit" select="'Lis�� koulukategoria'" />
	
	<xsl:variable name="updateSection.name" select="'Nimi'" />
	<xsl:variable name="updateSection.submit" select="'Tallenna muutokset'" />
	<xsl:variable name="updateGroupSection.header" select="'Muokkaa ryhm�kategoriaa'" />
	<xsl:variable name="updateSchoolSection.header" select="'Muokkaa koulukategoriaa'" />
	
	<xsl:variable name="addFile.header" select="'Lis�� tiedosto'" />
	<xsl:variable name="addFile.allowedfiles" select="'Sallitut tiedostomuodot'" />
	<xsl:variable name="addFile.maximumsize" select="'Suurin sallittu tiedostokoko on'" />
	<xsl:variable name="addFile.mb" select="'MB'" />
	<xsl:variable name="addFile.file" select="'Tiedosto'" />
	<xsl:variable name="addFile.description" select="'Kuvaus (vapaaehtoinen)'" />
	<xsl:variable name="addFile.submit" select="'Lis�� tiedosto'" />
	
	<xsl:variable name="updateFile.header" select="'Muokkaa'" />
	<xsl:variable name="updateFile.name" select="'Nimi'" />
	<xsl:variable name="updateFile.description" select="'Kuvaus (vapaaehtoinen)'" />
	<xsl:variable name="updateFile.category" select="'Kategoria'" />
	<xsl:variable name="updateFile.submit" select="'Tallenna muutokset'" />
	
	<xsl:variable name="validationError.RequiredField" select="'Kentt� on pakollinen'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Kent�n sis�lt� ei vastaa vaadittua muotoa'" />
	<xsl:variable name="validationError.TooLong" select="'Kent�n sis�lt� on liian pitk�'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Tuntematon virhe kent�ss�'" />
	<xsl:variable name="validationError.field.name" select="'nimi'" />
	<xsl:variable name="validationError.field.description" select="'kuvaus'" />
	<xsl:variable name="validationError.messageKey.InvalidFileFormat" select="'Ep�kelpo tiedostomuoto'" />
	<xsl:variable name="validationError.messageKey.NoFileAttached" select="'Sinun t�ytyy liitt�� tiedosto'" />
	<xsl:variable name="validationError.messageKey.FileTooBig" select="'L�hett�m�si tiedosto on liian suuri'" />
	<xsl:variable name="validationError.messageKey.FileTooSmall" select="'L�hett�m�si tiedosto on liian pieni'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Tuntematon virhe'" />				

</xsl:stylesheet>
