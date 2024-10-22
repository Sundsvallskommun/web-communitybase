<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:template match="Document">

		<div id="DeleteSectionModuleModule" class="contentitem of-module">

			<div class="of-inner-padded">

				<xsl:value-of select="Message" disable-output-escaping="yes"/>

			</div>

			<footer class="of-text-right of-inner-padded of-no-bg">
				
				<a class="of-btn of-btn-rodon of-btn-inline" href="{/Document/requestinfo/contextpath}{FullAlias}/delete/{section/sectionID}">
					<span>
						<xsl:value-of select="$i18n.DeleteSection"/>
					</span>
				</a>					
				
				<span class="of-btn-link">
					<xsl:value-of select="$i18n.or"/>
					<xsl:text>&#160;</xsl:text>
					<a href="{/Document/requestinfo/contextpath}/{section/alias}">
						<xsl:value-of select="$i18n.Cancel"/>
					</a></span>
			</footer>

		</div>

	</xsl:template>

</xsl:stylesheet>