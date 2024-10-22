<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />
	
	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl" />

	<xsl:template match="Document">
		
		<xsl:choose>
			<xsl:when test="Format = 'EMAIL'">
				<xsl:call-template name="NameChangeEmail"/>
			</xsl:when>
			<xsl:when test="Format = 'LIST'">
				<xsl:call-template name="NameChangeList"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="NameChangePopup"/>
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	
	<xsl:template name="NameChangeEmail">
		
		<li>
			<a href="{/Document/ContextPath}{/Document/ShowProfileAlias}/{user/userID}">
				<xsl:call-template name="user">
					<xsl:with-param name="user" select="user"/>
				</xsl:call-template>
			</a>

			<xsl:text>&#160;</xsl:text>
			
			<xsl:value-of select="$i18n.HasDeletedSection"/>
			
			<xsl:text>&#160;</xsl:text>
			
			<xsl:value-of select="SectionName"/>
			
			<xsl:text>&#160;(</xsl:text>
			
			<xsl:value-of select="Posted"/>
			
			<xsl:text>)</xsl:text>
		</li>

	</xsl:template>	
	
	<xsl:template name="NameChangeList">

		<li>
			<i class="icons icon-settings" aria-hidden="true"/>
			
			<span class="bold">
				<xsl:call-template name="user">
					<xsl:with-param name="user" select="user"/>
				</xsl:call-template>
			</span>
			
			<xsl:text> </xsl:text>
			
			<xsl:value-of select="$i18n.HasDeletedSection"/>
			
			<xsl:text> </xsl:text>
			
			<span class="bold">
				<xsl:value-of select="SectionName"/>
			</span>
			
			<div class="inline-box">
				<div><xsl:value-of select="Posted"/></div>
			</div>
		</li>

	</xsl:template>
	
	<xsl:template name="NameChangePopup">
		
		<a id="notification-{ID}" class="dropdown-item" href="#">
			<xsl:if test="Unread">
				<xsl:attribute name="class">dropdown-item new</xsl:attribute>
			</xsl:if>
			
			<xsl:if test="Show">
				<xsl:attribute name="data-show"></xsl:attribute>			
			</xsl:if>			
			
			<div>
				<span class="bold">
					<xsl:call-template name="user">
						<xsl:with-param name="user" select="user"/>
					</xsl:call-template>
				</span>
				
				<xsl:text> </xsl:text>
				
				<xsl:value-of select="$i18n.HasDeletedSection"/>
				
				<xsl:text> </xsl:text>
				
				<span class="bold">
					<xsl:value-of select="SectionName"/>
				</span>
			</div>
			
			<div class="inline-box">
				<div>
					<i class="icons icon-settings" aria-hidden="true"/>
					
					<span>
						<xsl:value-of select="ModuleName"/>
					</span>
				</div>
				
				<div><xsl:value-of select="Posted"/></div>
			</div>
		</a>

	</xsl:template>

	<xsl:template name="user">
		
		<xsl:param name="user"/>
		
		<xsl:choose>
			<xsl:when test="$user">
				<xsl:value-of select="$user/firstname"/>
				<xsl:text>&#x20;</xsl:text>
				<xsl:value-of select="$user/lastname"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$i18n.DeletedUser"/>
			</xsl:otherwise>
		</xsl:choose> 	
	</xsl:template>	
	
</xsl:stylesheet>