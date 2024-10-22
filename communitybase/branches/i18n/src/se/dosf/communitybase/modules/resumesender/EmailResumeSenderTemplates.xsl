<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"/>

	<xsl:template match="document">
		<html>
			<head>
			</head>
			<body>
				<table class="normal">
					<tr>
						<th align="left"><xsl:value-of select="$document.header" /></th>
					</tr>
					<tr>
						<td ><xsl:value-of select="$document.history" /><xsl:text>&#x20;</xsl:text>(<xsl:value-of select="user/lastLogin"/>):</td>
					</tr>
					<tr>
						<td>
							<xsl:apply-templates select="group"/>
						</td>
					</tr>
					<tr>
						<td>
							<xsl:value-of select="$document.information.part1" /><xsl:text>&#x20;</xsl:text><a href="{baseURL}"><xsl:value-of select="$document.link" /></a>.
							<xsl:value-of select="$document.information.part2" /><xsl:text>&#x20;</xsl:text><a href="{baseURL}"><xsl:value-of select="$document.link" /></a>.						
						</td>
					</tr>
					<tr>
						<td>
							<xsl:value-of select="$document.municipalitylink" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="municipalityName" /><xsl:text>&#x20;</xsl:text>
							<a href="{municipalityURL}"><xsl:value-of select="municipalityURL" /></a>
						</td>
					</tr>					
				</table>				
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="group">
		<table>
			<tr>
				<th align="left">
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
		<table class="normal">
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
			<td>
				<a href="{/document/baseURL}{fullAlias}" title="{description}"><xsl:value-of select="title"/> (<xsl:value-of select="added"/>)</a>
			</td>
		</tr>
	</xsl:template>							
</xsl:stylesheet>