<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />
	
	<xsl:template match="document">
		<div class="languageContainer">
			<xsl:apply-templates select="languages"/>
		</div>
	</xsl:template>
	
	<xsl:template match="languages">
		
		<!-- Determine current language -->
		<xsl:variable name="currentLang">
			<xsl:for-each select="language"> 
				<xsl:if test="starts-with(substring-after(/document/requestinfo/uri,/document/requestinfo/contextpath),concat('/',code))"><xsl:value-of select="code"/></xsl:if>
			</xsl:for-each>
		</xsl:variable>
	<!--
		<p>Current Language: <xsl:value-of select="$currentLang"/></p>
	-->
		<!-- Determine remaining URL -->
		<xsl:variable name="remainingURL">
			<xsl:choose>
				<xsl:when test="$currentLang != ''">
						<xsl:value-of select="substring-after(/document/requestinfo/uri,concat(concat('/',$currentLang),'/'))"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="substring-after(/document/requestinfo/uri,/document/requestinfo/contextpath)"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!--
		<p>Remaining URL: <xsl:value-of select="$remainingURL"/></p>
		-->
		<div>
			<form name="setlanguage" method="post" action="{/document/requestinfo/contextpath}/{endpoint}">
				<input name="languageCode" type="hidden" />
				<input name="redirectTo" type="hidden" />
				<xsl:apply-templates select="language">
					<xsl:with-param name="remainingURL" select="$remainingURL"/>
				</xsl:apply-templates>
			</form>
		</div>
	</xsl:template>

	<xsl:template match="language">
		<xsl:param name="remainingURL"/>
		<span class="langImage">
			<a href="javascript:document.setlanguage.languageCode.value='{code}';document.setlanguage.redirectTo.value='{/document/requestinfo/contextpath}/{code}/{$remainingURL}';document.setlanguage.submit();">
				<img title="{localName}" alt="{localName}" src="{/document/requestinfo/contextpath}/images/{code}.gif"/>
			</a>
		</span>
	</xsl:template>

</xsl:stylesheet>