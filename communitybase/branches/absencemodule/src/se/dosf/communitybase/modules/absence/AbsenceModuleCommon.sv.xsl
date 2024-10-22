<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:variable name="i18n.Children">Barn</xsl:variable>
	
	<xsl:variable name="i18n.Date">Datum</xsl:variable>
	<xsl:variable name="i18n.date">datum</xsl:variable>
	<xsl:variable name="i18n.Time">Tid</xsl:variable>
	<xsl:variable name="i18n.time">tid</xsl:variable>
	<xsl:variable name="i18n.Added">Rapporterad</xsl:variable>
	<xsl:variable name="i18n.Updated">�ndrad</xsl:variable>
	<xsl:variable name="i18n.Back">Tillbaka</xsl:variable>
	
	<xsl:variable name="i18n.AddAbsence">Rapportera fr�nvaro</xsl:variable>
	<xsl:variable name="i18n.UpdateAbsence">�ndra rapporterad fr�nvaro</xsl:variable>
	<xsl:variable name="i18n.DeleteAbsenceConfirm">�r du s�ker p� att du vill ta bort rapporterad fr�nvaro</xsl:variable>
	<xsl:variable name="i18n.DeleteAbsence">Ta bort rapporterad fr�nvaro</xsl:variable>
	<xsl:variable name="i18n.UpdateAbsenceDisabled">Tiden f�r att �ndra den h�r rapporterade fr�nvaron har passerats</xsl:variable>
	<xsl:variable name="i18n.DeleteAbsenceDisabled">Tiden f�r att ta bort den h�r rapporterade fr�nvaron har passerats</xsl:variable>
	<xsl:variable name="i18n.ShowAbsence">Visa mer information kring fr�nvaron</xsl:variable>
	
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
	<xsl:variable name="i18n.from">fr�n</xsl:variable>
	<xsl:variable name="i18n.to">till</xsl:variable>
	<xsl:variable name="i18n.From">Fr�n</xsl:variable>
	<xsl:variable name="i18n.To">Till</xsl:variable>
	<xsl:variable name="i18n.EndTime">Sluttid</xsl:variable>
	<xsl:variable name="i18n.endTime">sluttid</xsl:variable>
	
	<xsl:variable name="i18n.LastReportHourPart1">Observera att du endast kan l�mna fr�nvaro f�r samma dag f�re klockan</xsl:variable>
	
	<xsl:variable name="i18n.validationError.RequiredField">Du m�ste fylla i f�ltet</xsl:variable>
	<xsl:variable name="i18n.validationError.InvalidFormat">Felaktigt format p� f�ltet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooLong">F�r l�ngt v�rde p� f�ltet</xsl:variable>
	<xsl:variable name="i18n.validationError.TooShort">F�r kort v�rde p� f�ltet</xsl:variable>
	<xsl:variable name="i18n.validationError.Other">Ett ok�nt fel har uppst�tt</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownValidationErrorType">Ett ok�nt fel har uppst�tt</xsl:variable>
	<xsl:variable name="i18n.validationError.unknownMessageKey">Ett ok�nt fel har uppst�tt</xsl:variable>
	
	<xsl:variable name="i18n.validationError.DaysBetweenToSmall">Du m�ste v�lja flera dagar. Om du endast vill rapportera fr�nvaro f�r 1 dag v�lj ist�llet perioden 'En dag'</xsl:variable>
	<xsl:variable name="i18n.validationError.EndTimeBeforeStartTime">Kontrollera tiden du angett. Starttiden m�ste vara f�re sluttiden</xsl:variable>
	<xsl:variable name="i18n.validationError.TimeBeforeLastReportTime">Du kan inte rapportera in fr�nvaro via F�r�ldram�tet f�r angivet datum</xsl:variable>
	<xsl:variable name="i18n.validationError.NoGroup">Du m�ste v�lja minst en grupp</xsl:variable>
	
	
</xsl:stylesheet>
