<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:include href="WeekMenuTemplateAdminModuleTemplates.xsl"/>
	
	<xsl:variable name="i18n.ListWeekMenuTemplates.title">Rullande scheman för matsedel</xsl:variable>
	<xsl:variable name="i18n.ListWeekMenuTemplates.NoWeekMenuTemplatesFound">Inga rullande scheman hittades.</xsl:variable>
	<xsl:variable name="i18n.ListWeekMenuTemplates.Add">Lägg till rullande schema</xsl:variable>
	<xsl:variable name="i18n.AddWeekMenuTemplate.title">Lägg till rullande schema</xsl:variable>
	<xsl:variable name="i18n.UpdateWeekMenuTemplate.title">Ändra rullande schema</xsl:variable>
	
	<xsl:variable name="i18n.Name">Namn</xsl:variable>
	<xsl:variable name="i18n.WeeksCount">Antal veckor</xsl:variable>
	<xsl:variable name="i18n.Start">Start</xsl:variable>
	<xsl:variable name="i18n.End">Slut</xsl:variable>
	
	<xsl:variable name="i18n.Add">Lägg till</xsl:variable>
	<xsl:variable name="i18n.Update">Ändra</xsl:variable>
	<xsl:variable name="i18n.Delete">Ta bort</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara ändringar</xsl:variable>
	
	<xsl:variable name="i18n.Monday">Måndag</xsl:variable>
	<xsl:variable name="i18n.Tuesday">Tisdag</xsl:variable>
	<xsl:variable name="i18n.Wednesday">Onsdag</xsl:variable>
	<xsl:variable name="i18n.Thursday">Torsdag</xsl:variable>
	<xsl:variable name="i18n.Friday">Fredag</xsl:variable>
	<xsl:variable name="i18n.Saturday">Lördag</xsl:variable>
	<xsl:variable name="i18n.Sunday">Söndag</xsl:variable>
	
	<xsl:variable name="i18n.Week">Vecka</xsl:variable>
	<xsl:variable name="i18n.RollingWeeks">Antal rullande veckor</xsl:variable>
	<xsl:variable name="i18n.StartYear">Start år</xsl:variable>
	<xsl:variable name="i18n.StartWeek">Start vecka</xsl:variable>
	<xsl:variable name="i18n.EndYear">Slut år</xsl:variable>
	<xsl:variable name="i18n.EndWeek">Slut vecka</xsl:variable>
	
	<xsl:variable name="i18n.ValidationError.RequiredField">Du måste fylla i fältet </xsl:variable>
	<xsl:variable name="i18n.ValidationError.InvalidFormat">Felaktigt värde på fältet </xsl:variable>
	<xsl:variable name="i18n.ValidationError.TooShort">För kort innehåll i fältet </xsl:variable>
	<xsl:variable name="i18n.ValidationError.TooLong">För långt innehåll i fältet </xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnknownValidationErrorType">Okänt fel på fältet </xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnknownFault">Ett okänt fel har uppstått.</xsl:variable>
	
	<xsl:variable name="i18n.ValidationError.StartDateIsInThePast">Startdatum får inte ha passerat</xsl:variable>
	<xsl:variable name="i18n.ValidationError.EndDateIsInThePast">Slutdatum måste vara i framtiden</xsl:variable>
	<xsl:variable name="i18n.ValidationError.StartIsAfterEnd">Startdatum måste vara innan slutdatum</xsl:variable>
	<xsl:variable name="i18n.ValidationError.StartDateOverlapsWithOther">Startdatumet krockar med ett annat rullande schema</xsl:variable>
	<xsl:variable name="i18n.ValidationError.EndDateOverlapsWithOther">Slutdatumet krockar med et annat rullande schema</xsl:variable>
	<xsl:variable name="i18n.ValidationError.DateContainsOther">Ett annat rullande schema innersluts av tidsperioden du angett</xsl:variable>
	
	<xsl:variable name="i18n.ValidationError.OutOfRangeWeek">Ogitligt värde i fältet </xsl:variable>
	<xsl:variable name="i18n.ValidationError.OutOfRangeWeekBetween">. Måste vara mellan </xsl:variable>
	<xsl:variable name="i18n.ValidationError.OutOfRangeWeekBetweenAnd"> och </xsl:variable>
</xsl:stylesheet>
