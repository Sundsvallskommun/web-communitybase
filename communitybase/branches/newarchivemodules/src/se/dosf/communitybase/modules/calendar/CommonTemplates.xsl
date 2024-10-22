<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:template match="GlobalResume">
		
		<table cellspacing="0" cellpadding="0" style="padding-bottom: 10px">
			<tr>
				<td style="font-size: 1.2em; font-weight: bold"><xsl:value-of select="$i18n.Reminders" /></td>
			</tr>
			<tr>
				<td><xsl:value-of select="$i18n.GlobalResumeHeader" /><xsl:text>&#160;(</xsl:text><xsl:value-of select="date" /><xsl:text>):</xsl:text></td>
			</tr>
			<tr>
				<td>
					<table cellspacing="0" cellpadding="0" style="padding-top: 5px">
						<tr>
							<th align="left" style="width: 120px"><xsl:value-of select="$i18n.Time" /></th>
							<th align="left"><xsl:value-of select="$i18n.Title" /></th>
						</tr>
						<xsl:apply-templates select="calendarPost" />
					</table>
				</td>
			</tr>
		</table>
	
	</xsl:template>
	
	<xsl:template match="calendarPost">
		
		<tr>
			<td>
				<xsl:value-of select="startTime" /><xsl:text>&#160;-&#160;</xsl:text><xsl:value-of select="endTime" />
			</td>
			<td>
				<a href="{../combinedCalendarURL}/showPost/{id}" title="{title}"><xsl:value-of select="title" /></a>
			</td>
		</tr>
	
	</xsl:template>
	
</xsl:stylesheet>