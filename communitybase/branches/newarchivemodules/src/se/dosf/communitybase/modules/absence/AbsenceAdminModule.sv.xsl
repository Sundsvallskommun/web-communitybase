<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="AbsenceAdminModuleTemplates.xsl"/>
	
	<xsl:include href="AbsenceModuleCommon.sv.xsl"/>
	
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.sv.xsl"/>
	
	<xsl:variable name="addAbsenceBreadCrumb">Rapportera frånvaro</xsl:variable>
	<xsl:variable name="updateAbsenceBreadCrumb">Ändra rapporterad frånvaro</xsl:variable>
	<xsl:variable name="userSettingsBreadCrumb">Inställningar frånvaroresumé</xsl:variable>
	
	<xsl:variable name="i18n.AbsenceAdministration">Administrera frånvaro</xsl:variable>
	<xsl:variable name="i18n.SearchCriterias">Sökkriterier</xsl:variable>
	<xsl:variable name="i18n.NoAccess">Du har inte rättighet till någon förskola</xsl:variable>
	<xsl:variable name="i18n.ShowReportedAbsenceFor">Visa rapporterad frånvaro för</xsl:variable>
	<xsl:variable name="i18n.Search">Sök</xsl:variable>
	
	<xsl:variable name="i18n.Group">Grupp</xsl:variable>
	<xsl:variable name="i18n.School">Förskola</xsl:variable>
	<xsl:variable name="i18n.NoAbsenceFound">Ingen rapporterad frånvaro hittades med de givna sökkriterierna</xsl:variable>
	<xsl:variable name="i18n.SortBy">Sortera frånvaro på</xsl:variable>
	<xsl:variable name="i18n.ASC">Stigande</xsl:variable>
	<xsl:variable name="i18n.DESC">Fallande</xsl:variable>

	<xsl:variable name="i18n.AbsenceUserSettings">Inställningar frånvaroresumé</xsl:variable>
	<xsl:variable name="i18n.AbsenceNotificationDescription">Jag vill få frånvaroresumé via e-post varje morgon på vilka barn som är frånvarande den aktuella dagen</xsl:variable>

	<xsl:variable name="i18n.SaveChanges">Spara ändringar</xsl:variable>

</xsl:stylesheet>
