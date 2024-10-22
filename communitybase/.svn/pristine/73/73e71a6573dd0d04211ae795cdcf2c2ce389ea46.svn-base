<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:template match="Document">

		<div id="SectionAccessDeniedHandlerModule" class="contentitem of-module">

			<div class="of-inner-padded">

				<xsl:value-of select="Message" disable-output-escaping="yes"/>

				<xsl:if test="MemberManagers">
				
					<h3><xsl:value-of select="$i18n.MemberAdministrators"/></h3>
					
					<ul class="of-participant-list of-inner-padded-b">
						
						<xsl:apply-templates select="MemberManagers/user"/>
						
					</ul>					
				
				</xsl:if>

			</div>

		</div>

	</xsl:template>

	<xsl:template match="user">

		<li>
			<figure class="of-profile of-figure-sm">
				<img alt="{firstname} {lastname}" src="{/Document/requestinfo/contextpath}{/Document/ProfileImageAlias}/{userID}" />
			</figure>
			
			<header class="of-inline">
			
				<a href="{/Document/requestinfo/contextpath}{/Document/ShowProfileAlias}/{userID}">
					<xsl:value-of select="firstname" /><xsl:text>&#160;</xsl:text><xsl:value-of select="lastname" />
				</a>
				
				<ul class="of-meta-line">
					<li><xsl:value-of select="Attributes/Attribute[Name = 'organization']/Value"/></li>
				</ul>
			</header>
		</li>

	</xsl:template>

</xsl:stylesheet>