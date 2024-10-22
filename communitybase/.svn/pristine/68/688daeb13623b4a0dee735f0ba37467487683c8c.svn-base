<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="calendarResume.xsl" />

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/utils/js/communitybase.common.js
	</xsl:variable>

	<xsl:variable name="moduleImagePath"><xsl:value-of select="/document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/document/module/sectionID" />/<xsl:value-of select="/document/module/moduleID" />/pics</xsl:variable>

	<xsl:template match="document">
		
		<div class="contentitem">	
			
			<h1><xsl:value-of select="$document.header" /></h1>
			
			<div class="line">
				<h2>
					<span>
						<xsl:value-of select="$document.calendarresume" />
					</span>
				</h2>
			</div>
			
			<div class="floatleft full">
				
				<xsl:if test="groups/calendarResume">		
					
					<div class="floatleft full">
						<xsl:apply-templates select="groups" mode="calendar" />
					</div>
					
				</xsl:if>
				
			</div>
			
			<div class="clearboth" />
			
			<div class="line">
				<h2>
					<span>
						<xsl:choose>
							<xsl:when test="groups/group/groupModules/module/events">
								<xsl:value-of select="$document.history.title" />:
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$document.noposts" />
							</xsl:otherwise>
						</xsl:choose>
					</span>
				</h2>
			</div>
			
			<xsl:if test="groups/group/groupModules/module/events">
				<xsl:apply-templates select="groups"/>
			</xsl:if>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="groups">
		
		<xsl:apply-templates select="group"/>
		
	</xsl:template>
	
	<xsl:template match="group">
		
		<div class="floatleft full margintop marginbottom">
			<h3 class="nomargin nopadding"><xsl:value-of select="name" /></h3>			
		</div>
		
		<div class="floatleft full">
			<xsl:apply-templates select="groupModules/module"/>
		</div>
		
	</xsl:template>
		
	<xsl:template match="module">
		
		<div class="content-box-no-header">
			<h4 class="nomargin nopadding lightbackground">
				<xsl:text>&#160;&#160;</xsl:text><xsl:value-of select="name" />
			</h4>
			
			<xsl:apply-templates select="events/event"/>
			
		</div>

	</xsl:template>	

	<xsl:template match="event">
		
		<p>
			<a href="{/document/requestinfo/contextpath}{fullAlias}" title="{description}">
				<xsl:value-of select="title" /><xsl:text>&#160;</xsl:text>(<xsl:value-of select="added" />)
			</a>
		</p>

	</xsl:template>
	
</xsl:stylesheet>