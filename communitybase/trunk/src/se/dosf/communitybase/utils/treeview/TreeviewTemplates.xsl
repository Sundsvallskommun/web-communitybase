<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>
	
	<xsl:template name="createTreeview">
		
		<xsl:param name="id" />
		<xsl:param name="schools" />
		<xsl:param name="rootNodeTitle" select="$i18n.TreeTitle" />
		<xsl:param name="expandAllTitle" select="$i18n.ExpandAll" />
		<xsl:param name="collapseAllTitle" select="$i18n.CollapseAll" />
		<xsl:param name="loadingTitle" select="$i18n.LoadingSchoolsAndGroups" />
		<xsl:param name="createCheckboxes" select="'true'" />
		<xsl:param name="useScrollableArea" select="'true'" />
		<xsl:param name="element" select="null" />
		<xsl:param name="requestparameters" select="requestparameters" />
		<xsl:param name="globalAccess" select="/Document/isSysAdmin" />
		<xsl:param name="schoolAccess" select="user/communityUserAttributes/schools/school" />
	
		<xsl:apply-templates select="$schools" mode="tree">
			<xsl:with-param name="id" select="$id" />
			<xsl:with-param name="element" select="$element" />
			<xsl:with-param name="rootNodeTitle" select="$rootNodeTitle" />
			<xsl:with-param name="expandAllTitle" select="$expandAllTitle" />
			<xsl:with-param name="collapseAllTitle" select="$collapseAllTitle" />
			<xsl:with-param name="loadingTitle" select="$loadingTitle" />
			<xsl:with-param name="createCheckboxes" select="$createCheckboxes" />
			<xsl:with-param name="useScrollableArea" select="$useScrollableArea" />
			<xsl:with-param name="requestparameters" select="$requestparameters" />
			<xsl:with-param name="globalAccess" select="$globalAccess" />
			<xsl:with-param name="schoolAccess" select="$schoolAccess" />
		</xsl:apply-templates>
	
	</xsl:template>
	
	<xsl:template match="schools" mode="tree">
	
		<xsl:param name="id" />
		<xsl:param name="element" />
		<xsl:param name="requestparameters" />
		<xsl:param name="rootNodeTitle" />
		<xsl:param name="expandAllTitle" />
		<xsl:param name="collapseAllTitle" />
		<xsl:param name="loadingTitle" />
		<xsl:param name="createCheckboxes" />
		<xsl:param name="useScrollableArea" />
		<xsl:param name="globalAccess" />
		<xsl:param name="schoolAccess" />
	
		<div class="jstree-actionbar"><a id="jstree-expand-all" href="javascript:void(0);" title="{$expandAllTitle}"><xsl:value-of select="$expandAllTitle" /></a><xsl:text>&#160;|&#160;</xsl:text><a id="jstree-collapse-all" href="javascript:void(0);" title="{$collapseAllTitle}"><xsl:value-of select="$collapseAllTitle" /></a></div>

		<div class="jstree-loading" style="height: 150px">
		
			<xsl:attribute name="class"><xsl:if test="$useScrollableArea = 'true'">jstree-loading jstree-scrollable</xsl:if></xsl:attribute>
			
			<span class="jstree-loading-text hidden"><xsl:value-of select="$loadingTitle" />...</span>
			
			<div id="{$id}" class="communitybase-treeview">
				<xsl:attribute name="class">
					<xsl:if test="$createCheckboxes = 'true'">
						<xsl:text>jstree-create-checkboxes </xsl:text>
					</xsl:if>
					<xsl:text>jstree-show-icons communitybase-treeview hidden</xsl:text>
				</xsl:attribute>
				<ul rel="global">
					<li id="global_global" rel="global">
						
						<xsl:attribute name="class">
							<xsl:choose>
								<xsl:when test="$requestparameters"><xsl:if test="$requestparameters/parameter[name = 'global']">jstree-checked</xsl:if></xsl:when>
								<xsl:when test="$element/global = 'true'">jstree-checked </xsl:when>
							</xsl:choose>
							<xsl:if test="$globalAccess = 'false'">jstree-checkbox-disabled </xsl:if>
							<xsl:text> jstree-force-open</xsl:text>
						</xsl:attribute>
						
						<a href="javascript:void(0);" title="{$rootNodeTitle}"><xsl:value-of select="$rootNodeTitle" /></a>
						<ul rel="school" class="jstree-clickable-nodes">	
							<xsl:apply-templates select="school" mode="tree">
								<xsl:with-param name="element" select="$element" />
								<xsl:with-param name="requestparameters" select="$requestparameters" />
								<xsl:with-param name="globalAccess" select="$globalAccess" />
								<xsl:with-param name="schoolAccess" select="$schoolAccess" />
							</xsl:apply-templates>
						</ul>
					</li>
				</ul>
			</div>
		
		</div>
			
	</xsl:template>
	
	<xsl:template match="school" mode="tree">
	
		<xsl:param name="element" />
		<xsl:param name="requestparameters" />
		<xsl:param name="globalAccess" />
		<xsl:param name="schoolAccess" />
	
		<xsl:variable name="schoolID" select="schoolID" />
			
		<li id="schoolbase_{$schoolID}" rel="school">
			
			<xsl:attribute name="class">
				<xsl:choose>
					<xsl:when test="$requestparameters"><xsl:if test="$requestparameters/parameter[name = 'schoolbase']/value = $schoolID">jstree-checked </xsl:if></xsl:when>
					<xsl:when test="$element/schools/school[schoolID = $schoolID]">jstree-checked </xsl:when>
				</xsl:choose>
				<xsl:if test="not($schoolAccess[schoolID = $schoolID]) and $globalAccess = 'false'">jstree-checkbox-disabled</xsl:if>
			</xsl:attribute>
			
			<a href="javascript:void(0);" title="{name}"><xsl:value-of select="name" /></a>
			<xsl:if test="groups/group">
				<ul rel="group">
					<xsl:apply-templates select="groups/group" mode="tree">
						<xsl:with-param name="element" select="$element" />
						<xsl:with-param name="requestparameters" select="$requestparameters" />
					</xsl:apply-templates>
				</ul>
			</xsl:if>
		</li>
	
	</xsl:template>
	
	<xsl:template match="group" mode="tree">
	
		<xsl:param name="element" />
		<xsl:param name="requestparameters" />
	
		<xsl:variable name="groupID" select="groupID" />
		<xsl:variable name="schoolID" select="../../schoolID" />
	
		<li id="groupschool.{$schoolID}_{$schoolID}:{groupID}" rel="group">
	   		
	   		<xsl:attribute name="class">
				<xsl:choose>
					<xsl:when test="$requestparameters"><xsl:if test="$requestparameters/parameter[name = concat('groupschool.',$schoolID)]/value = concat($schoolID,':',groupID)">jstree-checked</xsl:if></xsl:when>
					<xsl:when test="$element/groups/group[groupID = $groupID]">jstree-checked</xsl:when>
				</xsl:choose>
			</xsl:attribute>
	   		
	   		<a href="javascript:void(0);" title="{name}"><xsl:value-of select="name" /></a>
	    </li>
	
	</xsl:template>
	
</xsl:stylesheet>