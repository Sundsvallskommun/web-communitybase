<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>
	
	<xsl:template match="Document">
		
		<html>
			<head>
			</head>
			<body>
				
				<div class="contentitem">
					<h2 style="margin: 0 0 5px 0"><xsl:value-of select="$i18n.AbsenceNotificationHeader" /></h2>
					
					<p style="margin: 0 0 5px 0"><xsl:value-of select="$i18n.AbsenceNotificationDescription" /><xsl:text>&#x20;</xsl:text><b><xsl:value-of select="date" /></b>:</p>
					
					<xsl:apply-templates select="group" />
				</div>
				
			</body>
		</html>
		
	</xsl:template>
	
	<xsl:template match="group">
		
		<h3 style="margin: 10px 0 5px 0"><xsl:value-of select="name" /></h3>	
		<p style="margin: 0 0 10px 0"><xsl:value-of select="school/name" /></p>
		
		<table class="margintop" cellspacing="0">
			
			<th  width="150px" align="left" style="background-color: #CCCCCC; padding: 5px;">
				<xsl:value-of select="$i18n.Children" />
			</th>
			<th width="180px" align="left" style="background-color: #CCCCCC; padding: 5px;">
				<xsl:value-of select="$i18n.Date" />
			</th>
			<th width="95px" align="left" style="background-color: #CCCCCC; padding: 5px;">
				<xsl:value-of select="$i18n.Time" />
			</th>
			<th width="130px" align="left" style="background-color: #CCCCCC; padding: 5px;">
				<xsl:value-of select="$i18n.Added" />
			</th>

			<xsl:apply-templates select="Absence" />
			
		</table>
	
	</xsl:template>
	
	<xsl:template match="Absence">
		
		<xsl:variable name="showAbsenceURI">
			<xsl:value-of select="/Document/baseURL" /><xsl:value-of select="/Document/adminModuleAlias" />/showabsence/<xsl:value-of select="absenceID" />
		</xsl:variable>
		
		<tr>
			<td style="padding: 5px;"><a href="{$showAbsenceURI}" title="{$i18n.ShowAbsence}"><xsl:value-of select="name" /></a></td>
			<td style="padding: 5px;">
				<a href="{$showAbsenceURI}" title="{$i18n.ShowAbsence}">
					<xsl:value-of select="startDate" />
					<xsl:if test="daysBetween > 1">
						<xsl:text>&#x20;-&#x20;</xsl:text>
						<xsl:value-of select="endDate" />
					</xsl:if>
				</a>
			</td>
			<td style="padding: 5px;">
				<a href="{$showAbsenceURI}" title="{$i18n.ShowAbsence}">
					<xsl:call-template name="getFormattedTime">
						<xsl:with-param name="daysBetween" select="daysBetween" />
						<xsl:with-param name="startTime" select="startTime" />
						<xsl:with-param name="endTime" select="endTime" />
					</xsl:call-template>
				</a>
			</td>
			<td style="padding: 5px;">
				<a href="{$showAbsenceURI}" title="{$i18n.ShowAbsence}">
					<xsl:value-of select="posted" />
				</a>
			</td>
		</tr>
	
	</xsl:template>
	
	<xsl:template name="getFormattedTime">
		
		<xsl:param name="daysBetween" />
		<xsl:param name="startTime" />
		<xsl:param name="endTime" />
		
		<xsl:choose>
			<xsl:when test="$daysBetween and $startTime = $endTime">
				<xsl:value-of select="$daysBetween" /><xsl:text>&#x20;</xsl:text>
				<xsl:choose>
					<xsl:when test="$daysBetween = 1">
						<xsl:value-of select="$i18n.day" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.days" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$startTime" /><xsl:text>&#x20;-&#x20;</xsl:text><xsl:value-of select="$endTime" />
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	
</xsl:stylesheet>