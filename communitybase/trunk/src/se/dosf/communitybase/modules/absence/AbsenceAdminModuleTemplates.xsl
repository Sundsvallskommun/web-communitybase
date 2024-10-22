<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>
	
	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	
	<xsl:include href="AbsenceModuleCommon.xsl" />
	
	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/js/absencemodule.js
		/utils/js/confirmDelete.js
		/utils/js/datepicker/datepicker.js
		/utils/js/timepicker/jquery.timepicker.js
		/utils/treeview/jquery.cookie.js
		/utils/treeview/jquery.jstree.custom.js
		/utils/treeview/communitybase.treeview.js
	</xsl:variable>

	<xsl:variable name="links">
		/utils/treeview/themes/communitybase/style.css
		/utils/js/datepicker/css/datepicker.css
		/utils/js/timepicker/css/jquery.timepicker.css
	</xsl:variable>

	<xsl:variable name="modulePath"><xsl:value-of select="/Document/requestinfo/currentURI" />/<xsl:value-of select="/Document/module/alias" /></xsl:variable>
	<xsl:variable name="moduleImagePath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>

	<xsl:template match="Document">
		
		<div class="contentitem">
	
			<xsl:apply-templates select="ListAbsences" />
			<xsl:apply-templates select="AddAbsence" />
			<xsl:apply-templates select="UpdateAbsence" />
			<xsl:apply-templates select="ShowAbsence" />

		</div>
		
	</xsl:template>
	
	<xsl:template match="ListAbsences">
	
		<h1><xsl:value-of select="$i18n.AbsenceAdministration" /></h1>
		
		<div class="content-box noprint">
			<h1 class="header"><span><xsl:value-of select="$i18n.SearchCriterias" /></span></h1>
			
			<div class="content">
			
				<xsl:apply-templates select="validationException/validationError" />
			
				<form method="post" name="searchAbsence" id="searchAbsenceForm" action="{/Document/requestinfo/uri}#search">
			
					<div class="floatleft full bigmarginbottom">
						<div class="floatleft bigmarginright">
							<label for="startDate"><xsl:value-of select="$i18n.From" /><xsl:text>:&#x20;</xsl:text></label>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="'startDate'"/>
								<xsl:with-param name="name" select="'startDate'"/>
								<xsl:with-param name="title" select="$i18n.From"/>
								<xsl:with-param name="size" select="'10'" />
								<xsl:with-param name="element" select="AbsenceSearch" />
								<xsl:with-param name="value" select="currentDate" />
								<xsl:with-param name="class" select="'dateformat-Y-ds-m-ds-d show-weeks fill-grid no-animation disable-drag'" />
							</xsl:call-template>
						</div>
						<div class="floatleft">
							<label for="endDate"><xsl:value-of select="$i18n.To" /><xsl:text>:&#x20;</xsl:text></label>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="'endDate'"/>
								<xsl:with-param name="name" select="'endDate'"/>
								<xsl:with-param name="title" select="$i18n.To"/>
								<xsl:with-param name="size" select="'10'" />
								<xsl:with-param name="element" select="AbsenceSearch" />
								<xsl:with-param name="value" select="currentDate" />
								<xsl:with-param name="class" select="'dateformat-Y-ds-m-ds-d show-weeks fill-grid no-animation disable-drag'" />
							</xsl:call-template>
						</div>
						<div class="floatright">
							<label for="orderBy"><xsl:value-of select="$i18n.SortBy" /><xsl:text>:&#x20;</xsl:text></label>
							<xsl:call-template name="createOrderBySelector" /><xsl:text>&#x20;&#x20;</xsl:text>
							<xsl:call-template name="createOrderSelector" />
						</div>
					</div>
				
					<div class="floatleft full bigmargintop">
						<label for="absenceadmin-communitybase-treeview"><xsl:value-of select="$i18n.ShowReportedAbsenceFor" /><xsl:text>:&#x20;</xsl:text></label>
						<xsl:choose>
							<xsl:when test="schools/school">
								<xsl:call-template name="createTreeview">
									<xsl:with-param name="id" select="'absenceadmin-communitybase-treeview'" />
									<xsl:with-param name="element" select="AbsenceSearch" />
									<xsl:with-param name="schools" select="schools" />
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<p><xsl:value-of select="$i18n.NoAccess" /></p>
							</xsl:otherwise>
						</xsl:choose>
						
					</div>
	
					<xsl:call-template name="createHiddenField">
						<xsl:with-param name="id" select="'orderByField'" />
						<xsl:with-param name="name" select="'orderBy'" />
						<xsl:with-param name="disabled" select="'disabled'" />
						<xsl:with-param name="element" select="AbsenceSearch" />
					</xsl:call-template>
		
					<xsl:call-template name="createHiddenField">
						<xsl:with-param name="id" select="'orderField'" />
						<xsl:with-param name="name" select="'order'" />
						<xsl:with-param name="disabled" select="'disabled'" />
						<xsl:with-param name="element" select="AbsenceSearch" />
					</xsl:call-template>
		
					<div class="floatright bigmargintop">
						<input type="submit" value="{$i18n.Search}" />
					</div>
	
				</form>
	
			</div>
			
		</div>
		
		<xsl:apply-templates select="AbsenceSearch" />
		
		<div class="floatright text-align-right bigmargintop">
			<a href="{$modulePath}/addabsence">
				<xsl:value-of select="$i18n.AddAbsence" />
			</a>	
		</div>
		
	</xsl:template>
	
	<xsl:template match="AbsenceSearch">
		
		<a class="clearboth floatleft" name="search" />		
		
		<table class="floatleft bordertop borderbottom borderleft borderright full">
			<th width="16" />
			<th>
				<xsl:attribute name="class"><xsl:if test="orderBy = 'name'">italic</xsl:if></xsl:attribute>
				<a href="javascript:void(0);" onclick="orderBy('name');"><xsl:value-of select="$i18n.Children" /></a>
			</th>
			<th>
				<xsl:attribute name="class"><xsl:if test="orderBy = 'group'">italic</xsl:if></xsl:attribute>
				<a href="javascript:void(0);" onclick="orderBy('group');"><xsl:value-of select="$i18n.Group" /></a>
			</th>
			<th >
				<xsl:attribute name="class"><xsl:if test="orderBy = 'school'">italic</xsl:if></xsl:attribute>
				<a href="javascript:void(0);" onclick="orderBy('school');"><xsl:value-of select="$i18n.School" /></a>
			</th>
			<th>
				<xsl:attribute name="class"><xsl:if test="orderBy = 'startTime'">italic</xsl:if></xsl:attribute>
				<a href="javascript:void(0);" onclick="orderBy('startTime');"><xsl:value-of select="$i18n.Date" /></a>
			</th>
			<th>
				<xsl:value-of select="$i18n.Time" />
			</th>
			<th>
				<xsl:attribute name="class"><xsl:if test="orderBy = 'posted'">italic</xsl:if></xsl:attribute>
				<a href="javascript:void(0);" onclick="orderBy('posted');"><xsl:value-of select="$i18n.Added" /></a>
			</th>
			<th width="32" />
			<xsl:choose>
				<xsl:when test="../Absences/Absence">
					<xsl:apply-templates select="../Absences/Absence" mode="search-list" />
				</xsl:when>
				<xsl:otherwise>
					<tr><td colspan="8"><xsl:value-of select="$i18n.NoAbsenceFound" /></td></tr>
				</xsl:otherwise>
			</xsl:choose>
		</table>
		
	</xsl:template>
	
	<xsl:template match="Absence" mode="search-list">
	
		<tr>
			<td><img class="vertigal-align-middle" src="{$moduleImagePath}/user.png" /></td>
			<td><a href="{$modulePath}/showabsence/{absenceID}" title="{$i18n.ShowAbsence}"><xsl:value-of select="name" /></a></td>
			<td><a href="{$modulePath}/showabsence/{absenceID}" title="{$i18n.ShowAbsence}"><xsl:value-of select="group/name" /></a></td>
			<td><a href="{$modulePath}/showabsence/{absenceID}" title="{$i18n.ShowAbsence}"><xsl:value-of select="group/school/name" /></a></td>
			<td>
				<a href="{$modulePath}/showabsence/{absenceID}" title="{$i18n.ShowAbsence}">
					<xsl:value-of select="startDate" />
					<xsl:if test="daysBetween > 1">
						<xsl:text>&#x20;-&#x20;</xsl:text>
						<xsl:value-of select="endDate" />
					</xsl:if>
				</a>
			</td>
			<td>
				<a href="{$modulePath}/showabsence/{absenceID}" title="{$i18n.ShowAbsence}">
					<xsl:call-template name="getFormattedTime">
						<xsl:with-param name="daysBetween" select="daysBetween" />
						<xsl:with-param name="startTime" select="startTime" />
						<xsl:with-param name="endTime" select="endTime" />
					</xsl:call-template>
				</a>
			</td>
			<td>
				<a href="{$modulePath}/showabsence/{absenceID}" title="{$i18n.ShowAbsence}">
					<xsl:value-of select="posted" />
				</a>
			</td>
			<td>
				<a href="{$modulePath}/updateabsence/{absenceID}" title="{$i18n.UpdateAbsence}"><img src="{$moduleImagePath}/edit.png" /></a>
				<a href="{$modulePath}/deleteabsence/{absenceID}" title="{$i18n.DeleteAbsence}" onclick="return confirmDelete('{$i18n.DeleteAbsenceConfirm}?')"><img src="{$moduleImagePath}/delete.png" /></a>
			</td>
		</tr>
	
	</xsl:template>
	
	<xsl:template match="ShowAbsence">
		
		<xsl:apply-templates select="Absence" mode="show">
			<xsl:with-param name="mode" select="'Admin'" />
		</xsl:apply-templates>
		
		<div class="floatright text-align-right">
			<input type="button" value="{$i18n.Back}" onclick="window.location = '{/Document/requestinfo/currentURI}/{/Document/module/alias}#search'" />
		</div>
		
	</xsl:template>
	
	<xsl:template match="AddAbsence">
	
		<p class="info"><img src="{$moduleImagePath}/warning.png" class="vertical-align-middle" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.NoSensitiveInformation" /></p>
	
		<form method="post" name="addAbsence" id="addAbsence" action="{/Document/requestinfo/uri}">
			
			<div class="content-box">
				<h1 class="header">
					<span><xsl:value-of select="$i18n.AddAbsence" /></span>
				</h1>
				
				<div class="content">
						
					<xsl:apply-templates select="validationException/validationError" />
					
					<xsl:apply-templates select="schools" mode="absenceadmin-tree" />
					
					<xsl:call-template name="AbsenceForm" />
					
					<div class="floatright text-align-right">
						<input type="submit" value="{$i18n.AddAbsence}" />
					</div>
					
				</div>
			</div>
			
		</form>
		
	</xsl:template>
	
	<xsl:template match="UpdateAbsence">
	
		<p class="info"><img src="{$moduleImagePath}/warning.png" class="vertical-align-middle" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.NoSensitiveInformation" /></p>
	
		<form method="post" name="updateAbsence" id="updateAbsence" action="{/Document/requestinfo/uri}">
			
			<div class="content-box">
				<h1 class="header">
					<span><xsl:value-of select="$i18n.UpdateAbsence" /></span>
				</h1>
				
				<div class="content">
						
					<xsl:apply-templates select="validationException/validationError" />
					
					<xsl:apply-templates select="schools" mode="absenceadmin-tree" />
					
					<xsl:call-template name="AbsenceForm">
						<xsl:with-param name="absence" select="Absence" />
					</xsl:call-template>
					
					<div class="floatright text-align-right">
						<input type="submit" value="{$i18n.UpdateAbsence}" />
					</div>
					
				</div>
			</div>
			
		</form>
		
	</xsl:template>
	
	<xsl:template name="createOrderBySelector">
	
		<select id="orderBy" name="orderBy">
			<option value="name"><xsl:value-of select="$i18n.Children" /></option>
			<option value="group"><xsl:value-of select="$i18n.Group" /></option>
			<option value="school"><xsl:value-of select="$i18n.School" /></option>
			<option value="startTime"><xsl:value-of select="$i18n.Date" /></option>
			<option value="posted"><xsl:value-of select="$i18n.Added" /></option>
		</select>
	
	</xsl:template>
	
	<xsl:template name="createOrderSelector">
	
		<select id="order" name="order">
			<option value="ASC"><xsl:value-of select="$i18n.ASC" /></option>
			<option value="DESC"><xsl:value-of select="$i18n.DESC" /></option>
		</select>
	
	</xsl:template>
	
	<xsl:template match="schools" mode="absenceadmin-tree">
	
		<div class="jstree-actionbar">
			
			<a id="jstree-expand-all" href="javascript:void(0);" title="{$i18n.ExpandAll}">
			<xsl:value-of select="$i18n.ExpandAll" /></a><xsl:text>&#160;|&#160;</xsl:text>
			<a id="jstree-collapse-all" href="javascript:void(0);" title="{$i18n.CollapseAll}">
			<xsl:value-of select="$i18n.CollapseAll" /></a>
			
		</div>
	
		<div class="jstree-loading jstree-scrollable" style="height: 150px">
	
			<span class="jstree-loading-text"><xsl:value-of select="$i18n.LoadingSchoolsAndGroups" />...</span>
	
			<div id="addabsence-communitybase-treeview" class="communitybase-treeview jstree-show-icons hidden">
				<ul rel="global">
					<li id="global_global" rel="global" class="jstree-force-open">
						<a href="javascript:void(0);" title="{$i18n.TreeTitle}"><xsl:text>&#x20;</xsl:text><xsl:value-of select="$i18n.TreeTitle" /></a>
						<ul rel="school" class="jstree-clickable-nodes">	
							<xsl:apply-templates select="school" mode="absenceadmin-tree" />
						</ul>
					</li>
				</ul>
			</div>
		
		</div>
			
	</xsl:template>
	
	<xsl:template match="school" mode="absenceadmin-tree">
	
		<xsl:variable name="schoolID" select="schoolID" />
			
		<li id="schoolbase_{$schoolID}" rel="school">
			<a href="javascript:void(0);" title="{name}">
				<xsl:value-of select="name" />
			</a>
			<xsl:if test="groups/group">
				<ul rel="group">
					<xsl:apply-templates select="groups/group" mode="absenceadmin-tree" />
				</ul>
			</xsl:if>
		</li>
	
	</xsl:template>
	
	<xsl:template match="group" mode="absenceadmin-tree">
	
		<xsl:variable name="groupID" select="groupID" />
		<xsl:variable name="schoolID" select="../../schoolID" />

		<li id="groupschool.{$schoolID}_{$schoolID}:{groupID}" rel="group">
	   		<a href="javascript:void(0);" title="{name}">
	   			<xsl:call-template name="createRadio">
	   				<xsl:with-param name="name" select="'groupID'" />
	   				<xsl:with-param name="value" select="groupID" />
	   				<xsl:with-param name="class" select="'nomargin vertical-align-top'" />
	   				<xsl:with-param name="element" select="../../../../Absence/group" />
	   				<xsl:with-param name="requestparameters" select="../../../../requestparameters" />
	   			</xsl:call-template><xsl:text>&#160;</xsl:text>
	   			<xsl:value-of select="name" />
	   		</a>
	    </li>
	
	</xsl:template>
	
</xsl:stylesheet>