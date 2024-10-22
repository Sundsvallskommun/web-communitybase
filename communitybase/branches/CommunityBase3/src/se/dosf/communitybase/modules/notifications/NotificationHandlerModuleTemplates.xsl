<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl" />

	<xsl:template match="Document">

		<div class="contentitem">
			<section>
				<header>
					<h1><xsl:value-of select="$i18n.AllNotifications" /></h1>
				</header>
				<article>
					<xsl:choose>
						<xsl:when test="Day">
							<xsl:apply-templates select="Day"/>
						</xsl:when>
						<xsl:otherwise>
							<div class="italic"><xsl:value-of select="$i18n.NoNotifications" /></div>
						</xsl:otherwise>
					</xsl:choose>
				</article>
			</section>
		</div>

	</xsl:template>

	<xsl:template match="Day">
	
		<div class="xl-margin-bottom">
			<h3 class="inline-header"><xsl:value-of select="Name"/></h3>
			
			<xsl:apply-templates select="ViewFragment"/>
		</div>
	
	</xsl:template>

	<xsl:template match="ViewFragment">
	
		<xsl:value-of select="HTML" disable-output-escaping="yes"/>
	
	</xsl:template>

</xsl:stylesheet>