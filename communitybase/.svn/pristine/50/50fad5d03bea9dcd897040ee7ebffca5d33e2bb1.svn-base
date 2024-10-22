<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:template match="document">
		<div class="contentitem">	
			<xsl:apply-templates select="LoginFailed"/>
			<xsl:apply-templates select="AccountDisabled"/>
			<xsl:apply-templates select="Login"/>
		</div>		
	</xsl:template>
	
	<xsl:template match="Login">
		<h1><xsl:value-of select="$Login.header"/></h1>
		
		<xsl:call-template name="LoginForm"/>
	</xsl:template>
	 
	<xsl:template match="AccountDisabled">
		<h1><xsl:value-of select="$AccountDisabled.header"/></h1>
		
		<p class="error"><xsl:value-of select="$AccountDisabled.text"/></p>
	</xsl:template>		
	
	<xsl:template match="LoginFailed">
		<h1><xsl:value-of select="$LoginFailed.header"/></h1>
		
		<p class="error"><xsl:value-of select="$LoginFailed.text"/></p>
		
		<xsl:call-template name="LoginForm"/>
	</xsl:template>	
	
	<xsl:template name="LoginForm">
		<form method="post" ACCEPT-CHARSET="ISO-8859-1" action="{/document/requestinfo/uri}?{/document/requestinfo/querystring}">
			 
			<table>
				<tr>
					<td><xsl:value-of select="$LoginForm.username"/></td>
					<td><input type="text" name="username" size="40"/></td>				
				</tr>
				<tr>
					<td><xsl:value-of select="$LoginForm.password"/></td>
					<td><input type="password" name="password" size="40"/></td>				
				</tr>
				<tr>
					<td colspan="2" align="right" class="t-right"><input type="submit" value="{$LoginForm.submit}"/></td>
				</tr>			
			</table>
		
			
		</form>			
	</xsl:template>	
</xsl:stylesheet>