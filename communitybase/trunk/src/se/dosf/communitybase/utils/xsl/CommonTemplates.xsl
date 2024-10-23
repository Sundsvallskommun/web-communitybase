<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>
	
	<xsl:template name="createPublishingInformation">
		
		<xsl:param name="element" />
		<xsl:param name="moduleImagePath" />
		<xsl:param name="displayInline" select="'false'" />
		
		<xsl:choose>
			<xsl:when test="$element/global = 'true'">
				<img class="alignmiddle" src="{$moduleImagePath}/global.png"/><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.AllSchools" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="$element/schools/school">
					<xsl:variable name="schoolCount" select="count($element/schools/school)" />
					<img class="alignmiddle" src="{$moduleImagePath}/school.png"/><xsl:text>&#160;</xsl:text>
					<xsl:choose>
						<xsl:when test="$schoolCount > 1">
							<a id="school_tooltip" class="tooltip_trigger" href="javascript:void(0);"><xsl:value-of select="$schoolCount" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.schools" /></a>
							<div class="tooltip hidden" id="school_tooltip_container">
								<img class="alignmiddle" src="{$moduleImagePath}/school.png"/><xsl:text>&#160;</xsl:text>
								<xsl:for-each select="$element/schools/school">
									<xsl:value-of select="name" />
									<xsl:if test="position() != last()">
										<xsl:text>,&#x20;</xsl:text>
									</xsl:if>
								</xsl:for-each>
							</div>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$element/schools/school/name" />
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
				<xsl:if test="$element/groups/group">
					<xsl:choose><xsl:when test="$element/schools/school and $displayInline = 'false'"><br /></xsl:when><xsl:when test="$element/schools/school"><xsl:text>&#160;</xsl:text></xsl:when></xsl:choose>
					<xsl:variable name="groupCount" select="count($element/groups/group)" />
					<img class="alignmiddle" src="{$moduleImagePath}/group.png"/><xsl:text>&#160;</xsl:text>
					<xsl:choose>
						<xsl:when test="$groupCount > 1">
							<a id="group_tooltip" class="tooltip_trigger" href="javascript:void(0);"><xsl:value-of select="$groupCount" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.groups" /></a>
							<div class="tooltip hidden" id="group_tooltip_container">
								<img class="alignmiddle" src="{$moduleImagePath}/group.png"/><xsl:text>&#160;</xsl:text>
								<xsl:for-each select="$element/groups/group">
									<xsl:value-of select="name" />
									<xsl:if test="position() != last()">
										<xsl:text>,&#x20;</xsl:text>
									</xsl:if>
								</xsl:for-each>
							</div>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$element/groups/group/name" />
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>			
	
	</xsl:template>
	
	<xsl:template name="createPostedByUserText">
		
		<xsl:param name="date" />
		<xsl:param name="user" />
		
		<xsl:value-of select="$date" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="$i18n.by" /><xsl:text>&#x20;</xsl:text>
		<xsl:choose>
			<xsl:when test="$user">
				<xsl:value-of select="$user/firstname" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="$user/lastname" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$i18n.RemovedUser" />
			</xsl:otherwise>
		</xsl:choose>
	
	</xsl:template>
	
</xsl:stylesheet>