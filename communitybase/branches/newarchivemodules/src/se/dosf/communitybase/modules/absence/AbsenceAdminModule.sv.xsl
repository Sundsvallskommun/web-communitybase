<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="AbsenceAdminModuleTemplates.xsl"/>
	
	<xsl:include href="AbsenceModuleCommon.sv.xsl"/>
	
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.sv.xsl"/>
	
	<xsl:variable name="addAbsenceBreadCrumb">Rapportera fr�nvaro</xsl:variable>
	<xsl:variable name="updateAbsenceBreadCrumb">�ndra rapporterad fr�nvaro</xsl:variable>
	<xsl:variable name="userSettingsBreadCrumb">Inst�llningar fr�nvaroresum�</xsl:variable>
	
	<xsl:variable name="i18n.AbsenceAdministration">Administrera fr�nvaro</xsl:variable>
	<xsl:variable name="i18n.SearchCriterias">S�kkriterier</xsl:variable>
	<xsl:variable name="i18n.NoAccess">Du har inte r�ttighet till n�gon f�rskola</xsl:variable>
	<xsl:variable name="i18n.ShowReportedAbsenceFor">Visa rapporterad fr�nvaro f�r</xsl:variable>
	<xsl:variable name="i18n.Search">S�k</xsl:variable>
	
	<xsl:variable name="i18n.Group">Grupp</xsl:variable>
	<xsl:variable name="i18n.School">F�rskola</xsl:variable>
	<xsl:variable name="i18n.NoAbsenceFound">Ingen rapporterad fr�nvaro hittades med de givna s�kkriterierna</xsl:variable>
	<xsl:variable name="i18n.SortBy">Sortera fr�nvaro p�</xsl:variable>
	<xsl:variable name="i18n.ASC">Stigande</xsl:variable>
	<xsl:variable name="i18n.DESC">Fallande</xsl:variable>

	<xsl:variable name="i18n.AbsenceUserSettings">Inst�llningar fr�nvaroresum�</xsl:variable>
	<xsl:variable name="i18n.AbsenceNotificationDescription">Jag vill f� fr�nvaroresum� via e-post varje morgon p� vilka barn som �r fr�nvarande den aktuella dagen</xsl:variable>

	<xsl:variable name="i18n.SaveChanges">Spara �ndringar</xsl:variable>

</xsl:stylesheet>
