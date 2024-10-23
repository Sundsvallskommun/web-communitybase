<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:template match="groups" mode="calendar" >
	
		<xsl:apply-templates select="calendarResume" mode="calendar" />
	
	</xsl:template>
	
	<xsl:template match="calendarResume" mode="calendar">
		
		<div class="floatleft full marginbottom">
			<xsl:choose>
				<xsl:when test="not(year)">
					<div class="floatleft full">
						<h3>
							<xsl:value-of select="$calendarResume.noposts" />
						</h3>
					</div>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="year" mode="calendar"/>
				</xsl:otherwise>
			</xsl:choose>
		</div>
		
	</xsl:template>
	
	<xsl:template match="year" mode="calendar">
		
		<div class="floatleft full marginbottom margintop">
			<h3 class="nomargin nopadding"><xsl:value-of select="yearnumber"/></h3>
		</div>
		
		<div class="floatleft full">
			<xsl:apply-templates select="month" mode="calendar"/>
		</div>
	
	</xsl:template>
 
	<xsl:template match="month" mode="calendar">
		
		<div class="floatleft full">
			<h4 class="nomargin nopadding"><xsl:value-of select="monthnumber"/></h4>					
		</div>	
		
		<div class="floatleft full">
			<xsl:apply-templates select="day" mode="calendar"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="day" mode="calendar">
		
		<div class="content-box-no-header">
			
			<h4 class="nomargin nopadding marginbottom">
				<xsl:value-of select="monthday"/> - <xsl:value-of select="weekday"/>
			</h4>
		
			<div class="floatleft twenty">
				<xsl:value-of select="$calendarResume.time" />:
			</div>
			<div class="floatleft forty">
				<xsl:value-of select="$calendarResume.title" />:
			</div>
			<div class="floatleft forty">
				<xsl:value-of select="$calendarResume.publishedTo" />:
			</div>
			
			<xsl:apply-templates select="calendarPost[not(id = preceding-sibling::calendarPost/id)]" mode="calendar" />
		
		</div>
	
	</xsl:template>

	<xsl:template match="calendarPost" mode="calendar">
		
		<div class="floatleft full lightbackground">
			<div class="floatleft twenty">
				<xsl:if test="startTime!='0'">
					<xsl:value-of select="startTime"/>
					<xsl:if test="endTime!='0'">
						 - 
						<xsl:value-of select="endTime"/>
					</xsl:if>
				</xsl:if>
			</div>
			<div class="floatleft forty">
				<a href="{/document/requestinfo/contextpath}/{url}">
					<xsl:value-of select="title"/>
				</a>
			</div>
			<div class="floatleft forty">
				<xsl:call-template name="createPublishingInformation">
					<xsl:with-param name="element" select="." />
					<xsl:with-param name="moduleImagePath" select="$moduleImagePath" />
					<xsl:with-param name="displayInline" select="'true'" />
				</xsl:call-template>			
			</div>
		</div>
	
	</xsl:template>
	
</xsl:stylesheet>