<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="FileArchiveModuleTemplates.xsl"/>
	
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.fi.xsl"/>
	
	<!-- Breadcrumbs -->
	<xsl:variable name="addSectionBreadCrumb">Lisää kategoria </xsl:variable>
	<xsl:variable name="addFileBreadCrumb">Lisää tiedosto </xsl:variable>
	<xsl:variable name="updateSectionBreadCrumb">Lisää kategoria </xsl:variable>
	<xsl:variable name="updateFileBreadCrumb">Päivitä tiedosto</xsl:variable>
	<xsl:variable name="newFileText">Uusi tiedosto: </xsl:variable>
	
	<!-- i18n indirect phrases -->
	<xsl:variable name="i18n.file.newNotification">Uudet tiedostot edellisen käyntisi jälkeen</xsl:variable>
	<xsl:variable name="i18n.section.publishTo">Gör kategorin åtkomlig för</xsl:variable>
	<xsl:variable name="i18n.noaccess">Du har inte rättighet till någon förskola</xsl:variable>
	
	<!-- i18n translations  -->
	<xsl:variable name="i18n.for">kohteelle</xsl:variable>
	<xsl:variable name="i18n.AddSection">Lisää kategoria</xsl:variable>
	<xsl:variable name="i18n.Filename">Filnamn</xsl:variable>
	<xsl:variable name="i18n.Posted">Datum</xsl:variable>
	<xsl:variable name="i18n.Section">Kategori</xsl:variable>
	<xsl:variable name="i18n.AlphaToOmega">A till Ö</xsl:variable>
	<xsl:variable name="i18n.OmegaToAlpha">Ö till A</xsl:variable>
	<xsl:variable name="i18n.NewestFirst">Senast först</xsl:variable>
	<xsl:variable name="i18n.OldestFirst">Äldst först</xsl:variable>
	<xsl:variable name="i18n.DeleteSection">Ta bort kategori</xsl:variable>
	<xsl:variable name="i18n.ShownFor">Visas för</xsl:variable>
	<xsl:variable name="i18n.NoFiles">Det finns för närvarande inga filer här</xsl:variable>
	<xsl:variable name="i18n.Name">Namn</xsl:variable>
	<xsl:variable name="i18n.EditSection">Ändra kategorin</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara ändringar</xsl:variable>
	<xsl:variable name="i18n.AllFiles">Alla filer</xsl:variable>
	<xsl:variable name="i18n.AddFile">Lägg till fil</xsl:variable>
	<xsl:variable name="i18n.in">i</xsl:variable>
	<xsl:variable name="i18n.File">Fil</xsl:variable>
	<xsl:variable name="i18n.AllowedFiletypes">Tillåtna filtyper</xsl:variable>
	<xsl:variable name="i18n.MaxFilesizeIs">Största tillåtna filstorlek är</xsl:variable>
	<xsl:variable name="i18n.MB">MB</xsl:variable>
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.optional">valfritt</xsl:variable>
	<xsl:variable name="i18n.EditFile">Ändra filen</xsl:variable>
	<xsl:variable name="i18n.DeleteFile">Ta bort filen</xsl:variable>
	<xsl:variable name="i18n.BelongsTo">Tillhör</xsl:variable>
	<xsl:variable name="i18n.AddedBy">Inlagd av</xsl:variable>
	<xsl:variable name="i18n.atTime">klockan</xsl:variable>
	<xsl:variable name="i18n.OrderBy">Sortera på</xsl:variable>

	<!-- Validation -->
	<xsl:variable name="validationError.RequiredField" select="'Du måste fylla i fältet'"/>
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt värde på fältet'"/>
	<xsl:variable name="validationError.TooShort" select="'För litet värde på fältet'"/>
	<xsl:variable name="validationError.TooLong" select="'För stort värde på fältet'"/>
	<xsl:variable name="validationError.Other" select="'Sökvägen är inte giltig, ändra fältet'"/>
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Okänt fel på fältet'"/>
	
	<xsl:variable name="validationError.field.name" select="'namn'"/>
	<xsl:variable name="validationError.field.description" select="'beskrivning'"/>
	
	<xsl:variable name="validationError.messageKey.BadFileFormat" select="'Filen har felaktigt filformat'"/>
	<xsl:variable name="validationError.messageKey.NoFile" select="'Du har inte valt någon bild'"/>
	<xsl:variable name="validationError.messageKey.InvalidContent" select="'Ogiltiga tecken i innehållet'"/>
	<xsl:variable name="validationError.messageKey.UnableToParseRequest" select="'Ett fel inträffade när bilden skulle laddas upp'"/>
	<xsl:variable name="validationError.messageKey.NoGroup" select="'Du måste välja minst en grupp att publicera till'"/>
	<xsl:variable name="validationError.messageKey.FileTooBig" select="'Filen du valt att ladda upp är för stor'"/>
	<xsl:variable name="validationError.messageKey.FileTooSmall" select="'Filen du valt att ladda upp är för liten'"/>
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett okänt fel har uppstått'"/>				

	<!-- OLD  -->
	<!-- 
	<xsl:variable name="newFileText">Uusi tiedosto: </xsl:variable>
	<xsl:variable name="addSectionBreadcrumb">Lisää kategoria </xsl:variable>
	<xsl:variable name="updateGroupCategoryBreadcrumb">Muokkaa ryhmäkategoriaa </xsl:variable>
	<xsl:variable name="updateSchoolCategoryBreadcrumb">Muokkaa koulukategoriaa </xsl:variable>
	<xsl:variable name="addFileBreadcrumb">Lisää tiedosto </xsl:variable>
	<xsl:variable name="updateFileBreadcrumb">Päivitä tiedosto</xsl:variable>
	 -->
	<!-- Naming template.mode.field.type -->
	<!-- 
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
