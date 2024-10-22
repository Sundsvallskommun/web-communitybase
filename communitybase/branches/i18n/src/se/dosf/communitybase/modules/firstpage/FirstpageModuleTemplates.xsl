<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="calendarResume.xsl" />

	<xsl:template match="document">
		<div class="contentitem">	
			<h1><xsl:value-of select="$document.header" /></h1>
			
			<table class="calendar">
				<tr>
					<th>
					<div class="line">
						<h2>
							<span>
								<xsl:value-of select="$document.calendarresume" />
							</span>
						</h2>
					</div>
					</th>
				</tr>
				<xsl:if test="groups/calendarResume">		
					<tr>
						<td>
							<xsl:apply-templates select="groups" mode="calendar" />
						</td>
					</tr>
				</xsl:if>
			</table>
			
			
			<table class="calendar">
				<tr>
					<th>
					<div class="line">
					<h2>
						<xsl:choose>
							<xsl:when test="groups/group/groupModules/module/events">
								<span>
									<xsl:value-of select="$document.history.title" />:
								</span>
							</xsl:when>
							<xsl:otherwise>
								<span>
									<xsl:value-of select="$document.noposts" />
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</h2>
					</div>
					</th>
				</tr>			
			</table>
			<xsl:if test="groups/group/groupModules/module/events">
				<xsl:apply-templates select="groups"/>
			</xsl:if>
		</div>
	</xsl:template>

<!--<xsl:template match="groups/group/groupModules/module/events/calendar">
		<p>Kalender template</p>
	</xsl:template>-->
	
	<xsl:template match="groups">
		<xsl:apply-templates select="group"/>
	</xsl:template>
	
	<xsl:template match="group">
		
		<xsl:if test="groupModules/module/events">
			<table>
				<tr>
					<th align="left">
						<xsl:value-of select="name" />
					</th>
				</tr>
				<tr>	
					<td>
						<xsl:apply-templates select="groupModules/module"/>
					</td>
				</tr>
			</table>
		</xsl:if >
	</xsl:template>
		
	<xsl:template match="module">
		<table class="normal">
			<tr>
				<th class="normalTh">
					<xsl:value-of select="name" />
				</th>
			</tr>
			<xsl:apply-templates select="events/event"/>
		</table>
	</xsl:template>	

	<xsl:template match="event">
		<tr>
			<td>
				<a href="{/document/requestinfo/contextpath}{/document/section/fullAlias}{fullAlias}" title="{description}">
					<xsl:value-of select="title" /><xsl:text>&#160;</xsl:text>(<xsl:value-of select="added" />)
				</a>
			</td>
		</tr>
	</xsl:template>
	
</xsl:stylesheet>