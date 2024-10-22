<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>
	
	<xsl:template match="Document">
		
		<xsl:choose>
			<xsl:when test="SelectProvider">
				<div class="col-12 col-md-8 col-lg-5 mx-auto">
					<div class="contentitem">
						<section>
							<header>
								<h2>
									<span><xsl:value-of select="$i18n.Header" /></span>
								</h2>
							</header>
							
							<div class="section-content">
								<xsl:apply-templates select="SelectProvider"/>					
							</div>
						</section>
					</div>
				</div>
			</xsl:when>
			<xsl:otherwise>
				<div class="col-12">
					<div class="contentitem">
						<xsl:apply-templates select="Configure"/>
					</div>
				</div>
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>	
	
	<xsl:template match="ProviderConfiguration">
	
		<div class="loginprovider">
			
			<xsl:value-of select="description" disable-output-escaping="yes"/>
			
			<form method="GET" action="{/Document/requestinfo/contextpath}{../../FullAlias}/login">
			
				<xsl:if test="../../Redirect">
					<input type="hidden" name="redirect" value="{../../Redirect}"/>
				</xsl:if>
			
				<input type="hidden" name="provider" value="{providerID}"/>
			
				<input type="submit" value="{buttonText}" class="btn btn-success full"/>
			
			</form>
			
			<xsl:if test="position() != last()">
				<br/>
			</xsl:if>
			
		</div>
	
	</xsl:template>

</xsl:stylesheet>