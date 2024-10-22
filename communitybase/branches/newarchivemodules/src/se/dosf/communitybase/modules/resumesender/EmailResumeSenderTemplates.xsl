<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"/>

	<xsl:template match="document">
	
		<html>
			<head>
			</head>
			<body>
				
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td align="left" style="font-size: 1.4em; font-weight: bold; padding-bottom: 5px"><xsl:value-of select="$i18n.Header" /></td>
					</tr>
					<tr>
						<td>
							<xsl:apply-templates select="GlobalResume/globalResumeHTML" />
						</td>
					</tr>
					<xsl:if test="group">
						<tr>
							<td align="left" style="font-size: 1.2em; font-weight: bold; padding-bottom: 3px"><xsl:value-of select="$i18n.NewEvents" /></td>
						</tr>
						<tr>
							<td><xsl:value-of select="$i18n.History" /><xsl:text>&#x20;</xsl:text>(<xsl:value-of select="user/lastLogin"/>):</td>
						</tr>
						<tr>
							<td>
								<xsl:apply-templates select="group"/>
							</td>
						</tr>
					</xsl:if>	
				</table>
				
				<xsl:if test="group">
					<p>
						<xsl:value-of select="$i18n.Information.Part1" /><xsl:text>&#x20;</xsl:text><a href="{baseURL}"><xsl:value-of select="$i18n.Link" /></a>.
					</p>
				</xsl:if>
				
				<p>
					<xsl:value-of select="$i18n.Information.Part2" /><xsl:text>&#x20;</xsl:text><a href="{baseURL}"><xsl:value-of select="$i18n.Link" /></a>.
				</p>
				
				<p>
					<xsl:value-of select="$i18n.MunicipalityLink" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="municipalityName" /><xsl:text>&#x20;</xsl:text><a href="{municipalityURL}"><xsl:value-of select="municipalityURL" /></a>
				</p>
							
			</body>
		</html>
		
	</xsl:template>
	
	<xsl:template match="globalResumeHTML">
		
		<xsl:value-of select="." disable-output-escaping="yes" />
		
	</xsl:template>
	
	
	<xsl:template match="group">
	
		<table cellspacing="0" cellpadding="0" style="padding-bottom: 5px; padding-top: 5px">
			<tr>
				<th align="left" style="font-size: 1.05em; padding-bottom: 4px">
					<xsl:value-of select="name"/> (<xsl:value-of select="school/name"/>)
				</th>
			</tr>
			<tr>
				<td>
					<xsl:apply-templates select="module"/>
				</td>
			</tr>
		</table>
		
	</xsl:template>
	
	<xsl:template match="module">
	
		<table cellspacing="0" cellpadding="0">
			<tr>
				<th align="left">
					<xsl:value-of select="name"/>
				</th>
			</tr>
			<xsl:apply-templates select="event"/>
		</table>
		
	</xsl:template>
	
	<xsl:template match="event">
		
		<tr>
		
			<td style="padding-bottom: 4px">
				<a href="{/document/baseURL}{fullAlias}" title="{title}"><xsl:value-of select="title"/> (<xsl:value-of select="added"/>)</a>
			</td>
		
		</tr>

	</xsl:template>
								
</xsl:stylesheet>