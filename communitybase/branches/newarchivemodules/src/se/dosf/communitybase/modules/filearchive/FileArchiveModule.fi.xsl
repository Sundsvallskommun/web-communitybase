<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="FileArchiveModuleTemplates.xsl"/>
	
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.fi.xsl"/>
	
	<!-- Breadcrumbs -->
	<xsl:variable name="addSectionBreadCrumb">Lis�� kategoria </xsl:variable>
	<xsl:variable name="addFileBreadCrumb">Lis�� tiedosto </xsl:variable>
	<xsl:variable name="updateSectionBreadCrumb">Lis�� kategoria </xsl:variable>
	<xsl:variable name="updateFileBreadCrumb">P�ivit� tiedosto</xsl:variable>
	<xsl:variable name="newFileText">Uusi tiedosto: </xsl:variable>
	
	<!-- i18n indirect phrases -->
	<xsl:variable name="i18n.file.newNotification">Uudet tiedostot edellisen k�yntisi j�lkeen</xsl:variable>
	<xsl:variable name="i18n.section.publishTo">G�r kategorin �tkomlig f�r</xsl:variable>
	<xsl:variable name="i18n.noaccess">Du har inte r�ttighet till n�gon f�rskola</xsl:variable>
	
	<!-- i18n translations  -->
	<xsl:variable name="i18n.for">kohteelle</xsl:variable>
	<xsl:variable name="i18n.AddSection">Lis�� kategoria</xsl:variable>
	<xsl:variable name="i18n.Filename">Filnamn</xsl:variable>
	<xsl:variable name="i18n.Posted">Datum</xsl:variable>
	<xsl:variable name="i18n.Section">Kategori</xsl:variable>
	<xsl:variable name="i18n.AlphaToOmega">A till �</xsl:variable>
	<xsl:variable name="i18n.OmegaToAlpha">� till A</xsl:variable>
	<xsl:variable name="i18n.NewestFirst">Senast f�rst</xsl:variable>
	<xsl:variable name="i18n.OldestFirst">�ldst f�rst</xsl:variable>
	<xsl:variable name="i18n.DeleteSection">Ta bort kategori</xsl:variable>
	<xsl:variable name="i18n.ShownFor">Visas f�r</xsl:variable>
	<xsl:variable name="i18n.NoFiles">Det finns f�r n�rvarande inga filer h�r</xsl:variable>
	<xsl:variable name="i18n.Name">Namn</xsl:variable>
	<xsl:variable name="i18n.EditSection">�ndra kategorin</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.AllFiles">Alla filer</xsl:variable>
	<xsl:variable name="i18n.AddFile">L�gg till fil</xsl:variable>
	<xsl:variable name="i18n.in">i</xsl:variable>
	<xsl:variable name="i18n.File">Fil</xsl:variable>
	<xsl:variable name="i18n.AllowedFiletypes">Till�tna filtyper</xsl:variable>
	<xsl:variable name="i18n.MaxFilesizeIs">St�rsta till�tna filstorlek �r</xsl:variable>
	<xsl:variable name="i18n.MB">MB</xsl:variable>
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.optional">valfritt</xsl:variable>
	<xsl:variable name="i18n.EditFile">�ndra filen</xsl:variable>
	<xsl:variable name="i18n.DeleteFile">Ta bort filen</xsl:variable>
	<xsl:variable name="i18n.BelongsTo">Tillh�r</xsl:variable>
	<xsl:variable name="i18n.AddedBy">Inlagd av</xsl:variable>
	<xsl:variable name="i18n.atTime">klockan</xsl:variable>
	<xsl:variable name="i18n.OrderBy">Sortera p�</xsl:variable>

	<!-- Validation -->
	<xsl:variable name="validationError.RequiredField" select="'Du m�ste fylla i f�ltet'"/>
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt v�rde p� f�ltet'"/>
	<xsl:variable name="validationError.TooShort" select="'F�r litet v�rde p� f�ltet'"/>
	<xsl:variable name="validationError.TooLong" select="'F�r stort v�rde p� f�ltet'"/>
	<xsl:variable name="validationError.Other" select="'S�kv�gen �r inte giltig, �ndra f�ltet'"/>
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Ok�nt fel p� f�ltet'"/>
	
	<xsl:variable name="validationError.field.name" select="'namn'"/>
	<xsl:variable name="validationError.field.description" select="'beskrivning'"/>
	
	<xsl:variable name="validationError.messageKey.BadFileFormat" select="'Filen har felaktigt filformat'"/>
	<xsl:variable name="validationError.messageKey.NoFile" select="'Du har inte valt n�gon bild'"/>
	<xsl:variable name="validationError.messageKey.InvalidContent" select="'Ogiltiga tecken i inneh�llet'"/>
	<xsl:variable name="validationError.messageKey.UnableToParseRequest" select="'Ett fel intr�ffade n�r bilden skulle laddas upp'"/>
	<xsl:variable name="validationError.messageKey.NoGroup" select="'Du m�ste v�lja minst en grupp att publicera till'"/>
	<xsl:variable name="validationError.messageKey.FileTooBig" select="'Filen du valt att ladda upp �r f�r stor'"/>
	<xsl:variable name="validationError.messageKey.FileTooSmall" select="'Filen du valt att ladda upp �r f�r liten'"/>
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett ok�nt fel har uppst�tt'"/>				

	<!-- OLD  -->
	<!-- 
	<xsl:variable name="newFileText">Uusi tiedosto: </xsl:variable>
	<xsl:variable name="addSectionBreadcrumb">Lis�� kategoria </xsl:variable>
	<xsl:variable name="updateGroupCategoryBreadcrumb">Muokkaa ryhm�kategoriaa </xsl:variable>
	<xsl:variable name="updateSchoolCategoryBreadcrumb">Muokkaa koulukategoriaa </xsl:variable>
	<xsl:variable name="addFileBreadcrumb">Lis�� tiedosto </xsl:variable>
	<xsl:variable name="updateFileBreadcrumb">P�ivit� tiedosto</xsl:variable>
	 -->
	<!-- Naming template.mode.field.type -->
	<!-- 
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
 -->
<xsl:variable name="i18n.RemovedUser">i18n.RemovedUser</xsl:variable>
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
