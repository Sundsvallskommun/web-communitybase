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
						<th align="left"><xsl:value-of select="$document.header" />!</th>
					</tr>
					<tr>
						<td>
							<xsl:value-of select="$document.information1.part1" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="UserDeletedNotification/user/email"/><xsl:text>&#x20;</xsl:text><xsl:value-of select="$document.information1.part2" />. 
							<xsl:value-of select="$document.information2.part1" /><xsl:text>&#x20;</xsl:text><a href="{systemURL}"><xsl:value-of select="systemURL" /></a><xsl:text>&#x20;</xsl:text><xsl:value-of select="$document.information2.part2" />.
						</td>
					</tr>
					<tr>
						<td>
							<xsl:value-of select="$document.about.part1" /><xsl:text>&#x20;</xsl:text><a href="{systemURL}"><xsl:value-of select="$document.about.part2" /></a>.
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
	
</xsl:stylesheet>