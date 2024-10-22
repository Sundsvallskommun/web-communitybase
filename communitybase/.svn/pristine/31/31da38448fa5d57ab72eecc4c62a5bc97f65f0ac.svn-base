<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>
	
	<xsl:template match="Document">
		
		<xsl:choose>
			<xsl:when test="SelectProvider">

				<div class="of-absolute-center-wrap">
		
					<section class="of-omega of-c-sm-fixed-4 of-center">
						<header class="of-icon of-text-center of-inner-padded-t of-inner-padded-b-half">
							<h1 class="of-icon">
								<span><xsl:value-of select="$i18n.Header" /></span>
							</h1>
						</header>
						<div class="of-block of-clear of-padding-mobile">
							<div class="of-inner-padded">
								
								<xsl:apply-templates select="SelectProvider"/>
				
							</div>
						</div>
						<div class="of-text-center">
							<div class="of-inner-padded-t-half">
								<a href="#" class="of-footer-logo of-login-logo">
								</a>
							</div>
						</div>
					</section>
				</div>
			
			</xsl:when>
			<xsl:otherwise>
			
				<div class="contentitem">
					<xsl:apply-templates select="Configure"/>
				</div>			
			
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>	
	
	<xsl:template match="ProviderConfiguration">
	
		<div class="loginprovider bigmarginbottom">
			
			<xsl:value-of select="description" disable-output-escaping="yes"/>
			
			<form method="GET" action="{/Document/requestinfo/contextpath}{../../FullAlias}/login">
			
				<xsl:if test="../../Redirect">
					<input type="hidden" name="redirect" value="{../../Redirect}"/>
				</xsl:if>
			
				<input type="hidden" name="provider" value="{providerID}"/>
			
				<input type="submit" value="{buttonText}" class="of-btn of-btn-block of-btn-gronsta"/>
			
			</form>
			
			<xsl:if test="position() != last()">
				<br/>
			</xsl:if>
		</div>
	
	</xsl:template>

</xsl:stylesheet>