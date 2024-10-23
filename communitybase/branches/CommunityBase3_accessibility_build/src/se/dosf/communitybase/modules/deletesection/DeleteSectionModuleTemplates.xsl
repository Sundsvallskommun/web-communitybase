<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:template match="Document">

		<div id="DeleteSectionModuleModule" class="contentitem">
			<section>
				<header>
					<h1><xsl:value-of select="/Document/ModuleName"/></h1>
				</header>
				
				<div class="section-content">
					<xsl:value-of select="Message" disable-output-escaping="yes"/>
				</div>
				
				<footer class="d-flex">
					<div class="ml-auto">
						<a href="{/Document/requestinfo/contextpath}/{section/alias}" class="btn marginright btn-danger">
							<i class="icons icon-ban" aria-hidden="true"/>
							<span><xsl:value-of select="$i18n.Cancel"/></span>
						</a>
						
						<a href="{/Document/requestinfo/contextpath}{FullAlias}/delete/{section/sectionID}" class="btn marginright btn-success">
							<i class="icons icon-delete" aria-hidden="true"/>
							<span><xsl:value-of select="$i18n.DeleteSection"/></span>
						</a>
					</div>
				</footer>
			</section>
		</div>

	</xsl:template>

</xsl:stylesheet>