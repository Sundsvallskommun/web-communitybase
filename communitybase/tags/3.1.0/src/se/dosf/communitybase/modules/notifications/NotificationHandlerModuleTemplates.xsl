<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl" />
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.xsl" />

	<xsl:template match="Document">

		<div class="contentitem of-module">

			<div class="of-block">

				<div class="of-inner-padded-trl">
					<h1>
						<xsl:value-of select="$i18n.AllNotifications" />
					</h1>
				</div>

				<div class="of-inner-padded">

					<ul class="of-notifications-list-x">
						
						<xsl:choose>
							<xsl:when test="Day">
								<xsl:apply-templates select="Day"/>
							</xsl:when>
							<xsl:otherwise>
								<li><div class="of-separator-text"><xsl:value-of select="$i18n.NoNotifications" /></div></li>
							</xsl:otherwise>
						</xsl:choose>
						
						
					</ul>

				</div>


			</div>

		</div>

	</xsl:template>

	<xsl:template match="Day">
	
		<li class="title">
			<div class="of-separator-text">
				<span><xsl:value-of select="Name"/></span>
			</div>
		</li>
		
		<xsl:apply-templates select="ViewFragment"/>
	
	</xsl:template>

	<xsl:template match="ViewFragment">
	
		<xsl:value-of select="HTML" disable-output-escaping="yes"/>
	
	</xsl:template>

</xsl:stylesheet>