<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:template match="Document">	
		<div class="contentitem">
			<h1><xsl:value-of select="$header"/></h1>
			<p><xsl:value-of select="$text"/></p>
		</div>
	</xsl:template>

</xsl:stylesheet>