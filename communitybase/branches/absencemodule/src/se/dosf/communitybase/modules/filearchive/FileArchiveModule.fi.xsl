<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="FileArchiveModuleTemplates.xsl" />
	
	<xsl:variable name="newFileText">Uusi tiedosto: </xsl:variable>
	<xsl:variable name="addSectionBreadcrumb">Lisää kategoria </xsl:variable>
	<xsl:variable name="updateGroupCategoryBreadcrumb">Muokkaa ryhmäkategoriaa </xsl:variable>
	<xsl:variable name="updateSchoolCategoryBreadcrumb">Muokkaa koulukategoriaa </xsl:variable>
	<xsl:variable name="addFileBreadcrumb">Lisää tiedosto </xsl:variable>
	<xsl:variable name="updateFileBreadcrumb">Päivitä tiedosto</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="fileArchiveModule.header" select="'kohteelle'" />
	
	<xsl:variable name="fileArchiveModule.newfile.description" select="'Uudet tiedostot edellisen käyntisi jälkeen'" />
	<xsl:variable name="fileArchiveModule.nofiles" select="'Tiedostoja ei ole kohteelle'" />
	
	<xsl:variable name="groupFiles.header" select="'Tiedostot ryhmälle'" />
	<xsl:variable name="groupFiles.help" select="'Lähettääksesi monta tiedostoa, sinun tulee lisätä kategoria'" />
	<xsl:variable name="groupFiles.addcategory" select="'Lisää ryhmäkategoria'" />
	
	<xsl:variable name="schoolFiles.header" select="'Tiedostot koululle'" />
	<xsl:variable name="schoolFiles.help" select="'Lähettääksesi monta tiedostoa, sinun tulee lisätä kategoria'" />
	<xsl:variable name="schoolFiles.addcategory" select="'Lisää koulukategoria'" />
	
	<xsl:variable name="section.delete.confirm" select="'Poista kategoria'" />
	<xsl:variable name="section.nofiles" select="'Tiedostoja ei löydy'" />
	<xsl:variable name="section.addfile" select="'Lisää uusi tiedosto'" />
	
	<xsl:variable name="file.delete.confirm" select="'Poista tiedosto'" />
	<xsl:variable name="file.delete.title" select="'Poista tiedosto'" />
	<xsl:variable name="file.edit.title" select="'Muokkaa tiedostoa'" />
	<xsl:variable name="file.addedby" select="'Lisännyt'" />
	<xsl:variable name="file.userdeleted" select="'Poistettu käyttäjä'" />
	<xsl:variable name="file.time" select="'kello'" />
	
	<xsl:variable name="addSection.header" select="'Lisää kategoria'" />
	<xsl:variable name="addSection.name" select="'Nimi'" />
	<xsl:variable name="addGroupSection.submit" select="'Lisää ryhmäkategoria'" />
	<xsl:variable name="addSchoolSection.submit" select="'Lisää koulukategoria'" />
	
	<xsl:variable name="updateSection.name" select="'Nimi'" />
	<xsl:variable name="updateSection.submit" select="'Tallenna muutokset'" />
	<xsl:variable name="updateGroupSection.header" select="'Muokkaa ryhmäkategoriaa'" />
	<xsl:variable name="updateSchoolSection.header" select="'Muokkaa koulukategoriaa'" />
	
	<xsl:variable name="addFile.header" select="'Lisää tiedosto'" />
	<xsl:variable name="addFile.allowedfiles" select="'Sallitut tiedostomuodot'" />
	<xsl:variable name="addFile.maximumsize" select="'Suurin sallittu tiedostokoko on'" />
	<xsl:variable name="addFile.mb" select="'MB'" />
	<xsl:variable name="addFile.file" select="'Tiedosto'" />
	<xsl:variable name="addFile.description" select="'Kuvaus (vapaaehtoinen)'" />
	<xsl:variable name="addFile.submit" select="'Lisää tiedosto'" />
	
	<xsl:variable name="updateFile.header" select="'Muokkaa'" />
	<xsl:variable name="updateFile.name" select="'Nimi'" />
	<xsl:variable name="updateFile.description" select="'Kuvaus (vapaaehtoinen)'" />
	<xsl:variable name="updateFile.category" select="'Kategoria'" />
	<xsl:variable name="updateFile.submit" select="'Tallenna muutokset'" />
	
	<xsl:variable name="validationError.RequiredField" select="'Kenttä on pakollinen'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Kentän sisältö ei vastaa vaadittua muotoa'" />
	<xsl:variable name="validationError.TooLong" select="'Kentän sisältö on liian pitkä'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Tuntematon virhe kentässä'" />
	<xsl:variable name="validationError.field.name" select="'nimi'" />
	<xsl:variable name="validationError.field.description" select="'kuvaus'" />
	<xsl:variable name="validationError.messageKey.InvalidFileFormat" select="'Epäkelpo tiedostomuoto'" />
	<xsl:variable name="validationError.messageKey.NoFileAttached" select="'Sinun täytyy liittää tiedosto'" />
	<xsl:variable name="validationError.messageKey.FileTooBig" select="'Lähettämäsi tiedosto on liian suuri'" />
	<xsl:variable name="validationError.messageKey.FileTooSmall" select="'Lähettämäsi tiedosto on liian pieni'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Tuntematon virhe'" />				

</xsl:stylesheet>
