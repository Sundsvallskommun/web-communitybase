<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:include href="OldContentRemovalModuleTemplates.xsl"/>
	
	<xsl:variable name="i18n.ValidationError.RequiredField">Du måste fylla i fältet </xsl:variable>
	<xsl:variable name="i18n.ValidationError.InvalidFormat">Felaktigt värde på fältet </xsl:variable>
	<xsl:variable name="i18n.ValidationError.TooShort">För kort innehåll i fältet </xsl:variable>
	<xsl:variable name="i18n.ValidationError.TooLong">För långt innehåll i fältet </xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnknownValidationErrorType">Okänt fel på fältet </xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnknownFault">Ett okänt fel har uppstått.</xsl:variable>
	<xsl:variable name="i18n.ValidationError.EndDateIsInTheFuture">Slut datum för gallring är i framtiden!</xsl:variable>
	<xsl:variable name="i18n.ValidationError.EndDateIsTooRecentPre">Slut datum för gallring måste vara äldre än </xsl:variable>
	<xsl:variable name="i18n.ValidationError.EndDateIsTooRecentPost"> månader.</xsl:variable>
	<xsl:variable name="i18n.ValidationError.MustSelectModules">Du måste välja åtminstone en modul att gallra i</xsl:variable>

	<xsl:variable name="i18n.OldContentRemoval.SelectModules.title">Gallring i </xsl:variable>
	<xsl:variable name="i18n.OldContentRemoval.SelectModules.description">Välj vilka moduler du vill gallra i och fram till vilket datum som du vill gallra. Du måste välja ett datum som är äldre än </xsl:variable>
	<xsl:variable name="i18n.OldContentRemoval.SelectModules.descriptionPost"> månad(er).</xsl:variable>
	<xsl:variable name="i18n.OldContentRemoval.SelectModules.NoModules">Finns inga moduler att gallra</xsl:variable>
	<xsl:variable name="i18n.OldContentRemoval.SelectModules.SelectForOldRemoval">Gallra</xsl:variable>
	<xsl:variable name="i18n.Continue">Fortsätt</xsl:variable>
	<xsl:variable name="i18n.ModuleName">Modulnamn</xsl:variable>
	<xsl:variable name="i18n.Field.EndDate">Gallra poster fram till och med datum</xsl:variable>
	<xsl:variable name="i18n.Module">Moduler</xsl:variable>
	
	
	
	
	
	
	<xsl:variable name="i18n.OldContentRemoval.SelectContent.title">Gallring i </xsl:variable>
	<xsl:variable name="i18n.OldContentRemoval.SelectContent.descriptionPre">Här visas alla </xsl:variable>
	<xsl:variable name="i18n.OldContentRemoval.SelectContent.descriptionMid"> poster som är äldre än </xsl:variable>
	<xsl:variable name="i18n.OldContentRemoval.SelectContent.descriptionPost"> och som du har rättigheter att ta bort. Klicka på namnet i en rad för att öppna posten i en ny flik för granskning.</xsl:variable>
	<xsl:variable name="i18n.OldContentRemoval.SelectContent.description2">Markera de poster du vill ta bort. Om en post är synlig för flera grupper så fortsätter den att synas för de andra grupperna. Du kan kryssa i/ur kryssrutan vid "Ta bort" för att markera alla i kolumnen.</xsl:variable>
	<xsl:variable name="i18n.OldContentRemoval.SelectContent.NoOldContent">Inget innehåll att gallra hittades.</xsl:variable>
	<xsl:variable name="i18n.OldContentRemoval.SelectContent.SelectForDeletion">Ta bort</xsl:variable>
	<xsl:variable name="i18n.OldContentRemoval.SelectContent.DeletePostsConfirmation">Är du säker på att du vill ta bort markerade poster</xsl:variable>
	<xsl:variable name="i18n.CreationDate">Skapad</xsl:variable>
	<xsl:variable name="i18n.LastModified">Senast ändrad</xsl:variable>
	<xsl:variable name="i18n.EndDate">Slut datum</xsl:variable>
	<xsl:variable name="i18n.Open">Öppna</xsl:variable>
	
	<xsl:variable name="i18n.Content.Pictures">Bilder</xsl:variable>
	<xsl:variable name="i18n.Content.Files">Filer</xsl:variable>
	
	<xsl:variable name="i18n.OldContentRemoval.ContentDeleted.title">Gallring utförd</xsl:variable>
	<xsl:variable name="i18n.OldContentRemoval.ContentDeleted.DeletedPre">Du har tagit bort</xsl:variable>
	<xsl:variable name="i18n.OldContentRemoval.ContentDeleted.DeletedPost">gamla poster.</xsl:variable>
	
</xsl:stylesheet>
