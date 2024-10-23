<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="AbsenceModuleTemplates.xsl"/>
	
	<xsl:include href="AbsenceModuleCommon.sv.xsl"/>
	
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.sv.xsl"/>
	
	<xsl:variable name="addAbsenceBreadCrumb">Rapportera fr�nvaro</xsl:variable>
	<xsl:variable name="updateAbsenceBreadCrumb">�ndra rapporterad fr�nvaro</xsl:variable>
	
	<xsl:variable name="i18n.for">f�r</xsl:variable>
	<xsl:variable name="i18n.NoReportedAbsence">Du har inte rapporterat n�gon fr�nvaro i den h�r gruppen</xsl:variable>

</xsl:stylesheet>
