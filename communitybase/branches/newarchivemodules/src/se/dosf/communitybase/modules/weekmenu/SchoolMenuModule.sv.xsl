<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:include href="SchoolMenuModuleTemplates.xsl"/>
	
	<xsl:variable name="newSchoolMenuText">Matsedel publicerad f�r vecka </xsl:variable>
	<xsl:variable name="addBreadCrumb">Ange matsedel</xsl:variable>
	<xsl:variable name="updateBreadCrumb">�ndra matsedel</xsl:variable>
	
	<xsl:variable name="i18n.AddSchoolMenu.title">Ange matsedel f�r vecka </xsl:variable>
	<xsl:variable name="i18n.UpdateSchoolMenu.title">�ndra matsedel f�r vecka </xsl:variable>
	<xsl:variable name="i18n.ShowSchoolMenu.title">Matsedel f�r vecka </xsl:variable>
	
	<xsl:variable name="i18n.ShowSchoolMenu.Add">Ange matsedel</xsl:variable>
	<xsl:variable name="i18n.ShowSchoolMenu.Update">�ndra matsedel</xsl:variable>
	
	<xsl:variable name="i18n.Add">Spara matsedel</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara �ndringar</xsl:variable>
	
	<xsl:variable name="i18n.Monday">M�ndag</xsl:variable>
	<xsl:variable name="i18n.Tuesday">Tisdag</xsl:variable>
	<xsl:variable name="i18n.Wednesday">Onsdag</xsl:variable>
	<xsl:variable name="i18n.Thursday">Torsdag</xsl:variable>
	<xsl:variable name="i18n.Friday">Fredag</xsl:variable>
	<xsl:variable name="i18n.Saturday">L�rdag</xsl:variable>
	<xsl:variable name="i18n.Sunday">S�ndag</xsl:variable>
	
	<xsl:variable name="i18n.WeekShort">V</xsl:variable>
	<xsl:variable name="i18n.AddSchoolMenu.OffsetWeekPrefix">Vecka </xsl:variable>
	<xsl:variable name="i18n.ShowSchoolMenu.PreviousWeek">F�reg�ende Vecka</xsl:variable>
	<xsl:variable name="i18n.ShowSchoolMenu.NextWeek">N�sta Vecka</xsl:variable>
	
	<xsl:variable name="i18n.ValidationError.RequiredField">Du m�ste ange: </xsl:variable>
	<xsl:variable name="i18n.ValidationError.InvalidFormat">Ogiltigt format: </xsl:variable>
	<xsl:variable name="i18n.ValidationError.TooShort">F�r kort: </xsl:variable>
	<xsl:variable name="i18n.ValidationError.TooLong">F�r l�ngt: </xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnknownValidationErrorType">Ok�nt validerings fel</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnknownFault">Ok�nt fel</xsl:variable>
	
	<xsl:variable name="i18n.ShowSchoolMenu.NoMenu">Ingen matsedel har publicerats f�r denna vecka.</xsl:variable>
	<xsl:variable name="i18n.AddSchoolMenu.InvalidDate">Du f�r inte ange matsedel p� redan passerade veckor.</xsl:variable>
	<xsl:variable name="i18n.AddSchoolMenu.NoTemplate">Kommunen har inte lagt upp en rullande matsedel f�r den h�r veckan.</xsl:variable>
	<xsl:variable name="i18n.AddSchoolMenu.UsingTemplate">Formul�ret �r f�rifyllt med kommunens rullande matsedel.</xsl:variable>
	<xsl:variable name="i18n.TemplateOffset">Utg�ngsvecka i det rullande matsedel schemat</xsl:variable>
	
	<xsl:variable name="i18n.AddSchoolMenu.ConfirmOverwriteChanges">Du har gjort �ndringar i matsedeln, skriv �ver med kommunens rullande matsedel?</xsl:variable>
</xsl:stylesheet>
