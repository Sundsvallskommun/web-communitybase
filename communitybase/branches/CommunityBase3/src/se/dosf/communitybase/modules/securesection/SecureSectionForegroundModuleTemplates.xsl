<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:template match="Document">
		
		<div class="contentitem">
			<section>
				<header>
					<h1><xsl:value-of select="$i18n.AccessDenied"/></h1>
				</header>
				
				<article>
					<xsl:value-of select="AccessDeniedText" disable-output-escaping="yes"/>
				</article>
			</section>
		</div>
		
	</xsl:template>
	
</xsl:stylesheet>