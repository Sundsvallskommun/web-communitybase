<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:template match="groups" mode="calendar" >
		<xsl:apply-templates select="calendarResume" mode="calendar" />
	</xsl:template>
	
	<xsl:template match="calendarResume" mode="calendar">
		<table class="calendar">
			<xsl:choose>
				<xsl:when test="not(year)">
					<tr>
						<td class="NoPosts" align="left">
							<!-- Skriver ut på första sidan -->
							<xsl:value-of select="$calendarResume.noposts" />
						</td>
					</tr>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="year" mode="calendar"/>
				</xsl:otherwise>
			</xsl:choose>
		</table>
	</xsl:template>
	
	<xsl:template match="year" mode="calendar">
		<tr>
			<th class="nopadding normalTh" align="left">
				<div class="marginleft"><xsl:value-of select="yearnumber"/></div>
			</th>
		</tr>
		<tr>
			<td class="nopadding">
				<xsl:apply-templates select="month" mode="calendar"/>
			</td>
		</tr>
	</xsl:template>
 
	<xsl:template match="month" mode="calendar">
		<table class="full">
			<tr>
				<th align="left">
					<xsl:value-of select="monthnumber"/>
				</th>
			</tr>
			<tr>
				<td class="nopadding">
					<xsl:apply-templates select="day" mode="calendar"/>
				</td>
			</tr>
		</table>
	</xsl:template>
	
	<xsl:template match="day" mode="calendar">
		<table class="calendarday">
			<tr>
				<th colspan="3" align="left">
					<xsl:value-of select="monthday"/>
					 - 
					 <xsl:value-of select="weekday"/>
				 </th>
			</tr>
			<tr>
				<td width="150"><xsl:value-of select="$calendarResume.time" />:</td>
				<td><xsl:value-of select="$calendarResume.title" />:</td>
				<td width="150"><xsl:value-of select="$calendarResume.group" />:</td>
			</tr>
			
			<xsl:apply-templates select="calendarPost" mode="calendar" />
			
		</table>
	</xsl:template>

	<xsl:template match="calendarPost" mode="calendar">
		
		<tr class="calendarRow">
			<td align="left" >
				<xsl:if test="startTime!='0'">
					<xsl:value-of select="startTime"/>
					<xsl:if test="endTime!='0'">
						 - 
						<xsl:value-of select="endTime"/>
					</xsl:if>
				</xsl:if>
			</td>
			<td align="left">
				<a href="{/document/requestinfo/contextpath}/{url}">
					<xsl:value-of select="title"/>
				</a>
			</td>
			<td align="left" >
				<xsl:choose>
					<xsl:when test="groupName">
						<xsl:value-of select="groupName" />
					</xsl:when>
					<xsl:when test="schoolName">
						<xsl:value-of select="schoolName" />
					</xsl:when>
					<xsl:when test="global">
						<xsl:value-of select="$calendarPost.global" />
					</xsl:when>
				</xsl:choose>
			</td>
		</tr>
		
	</xsl:template>
</xsl:stylesheet>