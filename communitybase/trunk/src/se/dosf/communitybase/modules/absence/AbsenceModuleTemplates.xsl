<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>
	
	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:include href="AbsenceModuleCommon.xsl" />
	
	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/js/absencemodule.js
		/utils/js/confirmDelete.js
		/utils/js/datepicker/datepicker.js
		/utils/js/timepicker/jquery.timepicker.js
	</xsl:variable>

	<xsl:variable name="links">
		/utils/js/datepicker/css/datepicker.css
		/utils/js/timepicker/css/jquery.timepicker.css
	</xsl:variable>

	<xsl:variable name="modulePath"><xsl:value-of select="/Document/requestinfo/currentURI" />/<xsl:value-of select="/Document/module/alias" />/<xsl:value-of select="/Document/group/groupID" /></xsl:variable>
	<xsl:variable name="moduleImagePath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>

	<xsl:template match="Document">
		
		<div class="contentitem">
	
			<h1><xsl:value-of select="/Document/module/name" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="$i18n.for" />:<xsl:text>&#x20;</xsl:text><xsl:value-of select="/Document/group/name" /></h1>
			
			<xsl:apply-templates select="ListAbsences" />
			<xsl:apply-templates select="AddAbsence" />
			<xsl:apply-templates select="UpdateAbsence" />
			<xsl:apply-templates select="ShowAbsence" />

		</div>
		
	</xsl:template>
	
	<xsl:template match="ListAbsences">
	
		<table class="floatleft bordertop borderbottom borderleft borderright full">
			<th width="16" />
			<th><xsl:value-of select="$i18n.Children" /></th>
			<th><xsl:value-of select="$i18n.Date" /></th>
			<th><xsl:value-of select="$i18n.Time" /></th>
			<th><xsl:value-of select="$i18n.Added" /></th>
			<th width="32" />
			<xsl:choose>
				<xsl:when test="Absences/Absence">
					<xsl:apply-templates select="Absences/Absence" mode="list" />
				</xsl:when>
				<xsl:otherwise>
					<tr><td colspan="6"><xsl:value-of select="$i18n.NoReportedAbsence" /></td></tr>
				</xsl:otherwise>
			</xsl:choose>
			
		</table>
		
		<div class="floatright text-align-right bigmargintop">
			<a href="{$modulePath}/addabsence">
				<xsl:value-of select="$i18n.AddAbsence" />
			</a>	
		</div>
		
	</xsl:template>
	
	<xsl:template match="Absence" mode="list">
		
		<tr>
			<td><img class="vertigal-align-middle" src="{$moduleImagePath}/user.png" /></td>
			<td><a href="{$modulePath}/showabsence/{absenceID}" title="{$i18n.ShowAbsence}"><xsl:value-of select="name" /></a></td>
			<td>
				<a href="{$modulePath}/showabsence/{absenceID}" title="{$i18n.ShowAbsence}">
					<xsl:value-of select="startDate" />
					<xsl:if test="daysBetween > 1">
						<xsl:text>&#x20;-&#x20;</xsl:text>
						<xsl:value-of select="endDate" />
					</xsl:if>
				</a>
			</td>
			<td>
				<a href="{$modulePath}/showabsence/{absenceID}" title="{$i18n.ShowAbsence}">
					<xsl:call-template name="getFormattedTime">
						<xsl:with-param name="daysBetween" select="daysBetween" />
						<xsl:with-param name="startTime" select="startTime" />
						<xsl:with-param name="endTime" select="endTime" />
					</xsl:call-template>
				</a>
			</td>
			<td><a href="{$modulePath}/showabsence/{absenceID}" title="{$i18n.ShowAbsence}"><xsl:value-of select="posted" /></a></td>
			<td>
				<xsl:call-template name="createUpdateDeleteLinks">
					<xsl:with-param name="mode" select="'Public'" />
				</xsl:call-template>
			</td>
		</tr>
		
	</xsl:template>	
	
	<xsl:template match="ShowAbsence">
		
		<xsl:apply-templates select="Absence" mode="show" />
		
		<div class="floatright text-align-right">
			<input type="button" value="{$i18n.Back}" onclick="window.location = '{/Document/requestinfo/currentURI}/{/Document/module/alias}/{/Document/group/groupID}'" />
		</div>
		
	</xsl:template>
	
	<xsl:template match="AddAbsence">
	
		<p class="info"><img src="{$moduleImagePath}/information.png" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.LastReportHourPart1" /><xsl:text>&#x20;</xsl:text><b><xsl:value-of select="/Document/lastReportHour" /><xsl:text>:00</xsl:text></b></p>
		<p class="info"><img src="{$moduleImagePath}/warning.png" class="vertical-align-middle" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.NoSensitiveInformation" /></p>
	
		<form method="post" name="addAbsence" id="addAbsence" action="{/Document/requestinfo/uri}">
			
			<div class="content-box">
				<h1 class="header">
					<span><xsl:value-of select="$i18n.AddAbsence" /></span>
				</h1>
				
				<div class="content">
						
					<xsl:apply-templates select="validationException/validationError" />
					
					<xsl:call-template name="AbsenceForm" />
					
					<div class="floatright text-align-right">
						<input type="submit" value="{$i18n.AddAbsence}" />
					</div>
					
				</div>
			</div>
			
		</form>
		
	</xsl:template>
	
	<xsl:template match="UpdateAbsence">
		
		<p class="info"><img src="{$moduleImagePath}/warning.png" class="vertical-align-middle" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.NoSensitiveInformation" /></p>
	
		<form method="post" name="updateAbsence" id="updateAbsence" action="{/Document/requestinfo/uri}">
			
			<div class="content-box">
				<h1 class="header">
					<span><xsl:value-of select="$i18n.UpdateAbsence" /></span>
				</h1>
				
				<div class="content">
						
					<xsl:apply-templates select="validationException/validationError" />
					
					<xsl:call-template name="AbsenceForm">
						<xsl:with-param name="absence" select="Absence" />
					</xsl:call-template>
					
					<div class="floatright text-align-right">
						<input type="submit" value="{$i18n.UpdateAbsence}" />
					</div>
					
				</div>
			</div>
			
		</form>
		
	</xsl:template>
	
</xsl:stylesheet>