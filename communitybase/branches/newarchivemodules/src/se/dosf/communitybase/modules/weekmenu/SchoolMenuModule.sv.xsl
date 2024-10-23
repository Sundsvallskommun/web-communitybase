<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:include href="SchoolMenuModuleTemplates.xsl"/>
	
	<xsl:variable name="newSchoolMenuText">Matsedel publicerad för vecka </xsl:variable>
	<xsl:variable name="addBreadCrumb">Ange matsedel</xsl:variable>
	<xsl:variable name="updateBreadCrumb">Ändra matsedel</xsl:variable>
	
	<xsl:variable name="i18n.AddSchoolMenu.title">Ange matsedel för vecka </xsl:variable>
	<xsl:variable name="i18n.UpdateSchoolMenu.title">Ändra matsedel för vecka </xsl:variable>
	<xsl:variable name="i18n.ShowSchoolMenu.title">Matsedel för vecka </xsl:variable>
	
	<xsl:variable name="i18n.ShowSchoolMenu.Add">Ange matsedel</xsl:variable>
	<xsl:variable name="i18n.ShowSchoolMenu.Update">Ändra matsedel</xsl:variable>
	
	<xsl:variable name="i18n.Add">Spara matsedel</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara Ändringar</xsl:variable>
	
	<xsl:variable name="i18n.Monday">Måndag</xsl:variable>
	<xsl:variable name="i18n.Tuesday">Tisdag</xsl:variable>
	<xsl:variable name="i18n.Wednesday">Onsdag</xsl:variable>
	<xsl:variable name="i18n.Thursday">Torsdag</xsl:variable>
	<xsl:variable name="i18n.Friday">Fredag</xsl:variable>
	<xsl:variable name="i18n.Saturday">Lördag</xsl:variable>
	<xsl:variable name="i18n.Sunday">Söndag</xsl:variable>
	
	<xsl:variable name="i18n.WeekShort">V</xsl:variable>
	<xsl:variable name="i18n.AddSchoolMenu.OffsetWeekPrefix">Vecka </xsl:variable>
	<xsl:variable name="i18n.ShowSchoolMenu.PreviousWeek">Föregående Vecka</xsl:variable>
	<xsl:variable name="i18n.ShowSchoolMenu.NextWeek">Nästa Vecka</xsl:variable>
	
	<xsl:variable name="i18n.ValidationError.RequiredField">Du måste ange: </xsl:variable>
	<xsl:variable name="i18n.ValidationError.InvalidFormat">Ogiltigt format: </xsl:variable>
	<xsl:variable name="i18n.ValidationError.TooShort">För kort: </xsl:variable>
	<xsl:variable name="i18n.ValidationError.TooLong">För långt: </xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnknownValidationErrorType">Okänt validerings fel</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnknownFault">Okänt fel</xsl:variable>
	
	<xsl:variable name="i18n.ShowSchoolMenu.NoMenu">Ingen matsedel har publicerats för denna vecka.</xsl:variable>
	<xsl:variable name="i18n.AddSchoolMenu.InvalidDate">Du får inte ange matsedel på redan passerade veckor.</xsl:variable>
	<xsl:variable name="i18n.AddSchoolMenu.NoTemplate">Kommunen har inte lagt upp en rullande matsedel för den här veckan.</xsl:variable>
	<xsl:variable name="i18n.AddSchoolMenu.UsingTemplate">Formuläret är förifyllt med kommunens rullande matsedel.</xsl:variable>
	<xsl:variable name="i18n.TemplateOffset">Utgångsvecka i det rullande matsedel schemat</xsl:variable>
	
	<xsl:variable name="i18n.AddSchoolMenu.ConfirmOverwriteChanges">Du har gjort ändringar i matsedeln, skriv över med kommunens rullande matsedel?</xsl:variable>
</xsl:stylesheet>
