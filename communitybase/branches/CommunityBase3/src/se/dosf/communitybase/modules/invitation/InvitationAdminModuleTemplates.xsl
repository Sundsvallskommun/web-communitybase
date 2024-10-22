<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:template match="Document">
		
		<div class="contentitem">
			<xsl:apply-templates select="ListInvitations"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="ListInvitations">
		
		<h1 class="mb-4"><xsl:value-of select="$i18n.Invitations"/></h1>
			
		<xsl:apply-templates select="Invitations/Invitation" mode="list"/>
		
	</xsl:template>
	
	<xsl:template match="Invitation" mode="list">
		
		<div class="mb-2 d-flex align-items-center">
			<div class="mr-3">
				<i class="icons icon-user big" aria-hidden="true"/>
			</div>
			
			<div>
				<div>
					<xsl:value-of select="email"/>
					
					<xsl:text> (</xsl:text>
					<xsl:value-of select="$i18n.LastSent"/>
					<xsl:text>: </xsl:text>
					<xsl:value-of select="lastSent"/>
					<xsl:text>)</xsl:text>
				</div>
				
				<div>
					<a href="{../../RegistrationBaseURL}{invitationID}/{linkID}">
						<xsl:value-of select="../../RegistrationBaseURL"/>
						<xsl:value-of select="invitationID"/>
						<xsl:text>/</xsl:text>
						<xsl:value-of select="linkID"/>
					</a>
				</div>
			</div>
		</div>
		
	</xsl:template>
	
</xsl:stylesheet>