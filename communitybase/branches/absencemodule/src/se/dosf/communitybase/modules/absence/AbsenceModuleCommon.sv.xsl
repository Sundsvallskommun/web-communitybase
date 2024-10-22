<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:variable name="i18n.Children">Barn</xsl:variable>
	
	<xsl:variable name="i18n.Date">Datum</xsl:variable>
	<xsl:variable name="i18n.date">datum</xsl:variable>
	<xsl:variable name="i18n.Time">Tid</xsl:variable>
	<xsl:variable name="i18n.time">tid</xsl:variable>
	<xsl:variable name="i18n.Added">Rapporterad</xsl:variable>
	<xsl:variable name="i18n.Updated">Ändrad</xsl:variable>
	<xsl:variable name="i18n.Back">Tillbaka</xsl:variable>
	
	<xsl:variable name="i18n.AddAbsence">Rapportera frånvaro</xsl:variable>
	<xsl:variable name="i18n.UpdateAbsence">Ändra rapporterad frånvaro</xsl:variable>
	<xsl:variable name="i18n.DeleteAbsenceConfirm">Är du säker på att du vill ta bort rapporterad frånvaro</xsl:variable>
	<xsl:variable name="i18n.DeleteAbsence">Ta bort rapporterad frånvaro</xsl:variable>
	<xsl:variable name="i18n.UpdateAbsenceDisabled">Tiden för att ändra den här rapporterade frånvaron har passerats</xsl:variable>
	<xsl:variable name="i18n.DeleteAbsenceDisabled">Tiden för att ta bort den här rapporterade frånvaron har passerats</xsl:variable>
	<xsl:variable name="i18n.ShowAbsence">Visa mer information kring frånvaron</xsl:variable>
	
	<xsl:variable name="i18n.day">heldag</xsl:variable>
	<xsl:variable name="i18n.days">heldagar</xsl:variable>
	
	<xsl:variable name="i18n.Name">Barnets namn</xsl:variable>
	<xsl:variable name="i18n.Comment">Kommentar</xsl:variable>
	<xsl:variable name="i18n.name">barnets namn</xsl:variable>
	<xsl:variable name="i18n.comment">kommentar</xsl:variable>
	<xsl:variable name="i18n.Period">Period</xsl:variable>
	<xsl:variable name="i18n.OneDay">En dag</xsl:variable>
	<xsl:variable name="i18n.SeveralDays">Flera dagar</xsl:variable>
	
	<xsl:variable name="i18n.WholeDay">Heldag</xsl:variable>
	<xsl:variable name="i18n.PartOfDay">Del av dag</xsl:variable>
	<xsl:variable name="i18n.StartTime">Starttid</xsl:variable>
	<xsl:variable name="i18n.startTime">starttid</xsl:variable>
	<xsl:variable name="i18n.StartDate">Startdatum</xsl:variable>
	<xsl:variable name="i18n.EndDate">Slutdatum</xsl:variable>
	<xsl:variable name="i18n.from">från</xsl:variable>
	<xsl:variable name="i18n.to">till</xsl:variable>
	<xsl:variable name="i18n.From">Från</xsl:variable>
	<xsl:variable name="i18n.To">Till</xsl:variable>
	<xsl:variable name="i18n.EndTime">Sluttid</xsl:variable>
	<xsl:variable name="i18n.endTime">sluttid</xsl:variable>
	
	<xsl:variable name="i18n.LastReportHourPart1">Observera att du endast kan lämna frånvaro för samma dag före klockan</xsl:variable>
	
	<xsl:variable name="i18n.validationError.RequiredField">Du måste fylla i fältet</xsl:variable>
	<xsl:variable name="i18n.validationError.InvalidFormat">Felaktigt format på fältet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong">För långt värde på fältet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooShort">För kort värde på fältet</xsl:variable>
	<xsl:variable name="i18n.validationError.Other">Ett okänt fel har uppstått</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownValidationErrorType">Ett okänt fel har uppstått</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownMessageKey">Ett okänt fel har uppstått</xsl:variable>
	
	<xsl:variable name="i18n.validationError.DaysBetweenToSmall">Du måste välja flera dagar. Om du endast vill rapportera frånvaro för 1 dag välj istället perioden 'En dag'</xsl:variable>
	<xsl:variable name="i18n.validationError.EndTimeBeforeStartTime">Kontrollera tiden du angett. Starttiden måste vara före sluttiden</xsl:variable>
	<xsl:variable name="i18n.validationError.TimeBeforeLastReportTime">Du kan inte rapportera in frånvaro via Föräldramötet för angivet datum</xsl:variable>
	<xsl:variable name="i18n.validationError.NoGroup">Du måste välja minst en grupp</xsl:variable>
	
	
</xsl:stylesheet>
