<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>
	
	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	
	<xsl:variable name="modulePath"><xsl:value-of select="/Document/requestinfo/currentURI" />/<xsl:value-of select="/Document/module/alias" />/<xsl:value-of select="/Document/group/groupID" /></xsl:variable>
	<xsl:variable name="moduleImagePath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>
	
	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/utils/treeview/jquery.cookie.js
		/utils/treeview/jquery.jstree.custom.js
		/utils/treeview/communitybase.treeview.js
		/utils/js/communitybase.common.js
		/utils/js/confirmDelete.js
		/js/linkarchive-sortable.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/utils/treeview/themes/communitybase/style.css
	</xsl:variable>
	
	<xsl:template match="Document">
		
		<div class="contentitem">
	
			<h1>
				<xsl:value-of select="/Document/module/name" />
				<xsl:text>&#x20;</xsl:text>
				<xsl:value-of select="$i18n.for" />
				<xsl:text>:</xsl:text>
				<xsl:text>&#x20;</xsl:text>
				<xsl:value-of select="/Document/group/name" />
			</h1>
			
			<xsl:apply-templates select="ListLinks" />
			<xsl:apply-templates select="AddLink" />
			<xsl:apply-templates select="UpdateLink" />
		
			
		</div>
	</xsl:template>
	
	<xsl:template match="ListLinks">
	
		<div class="floatleft marginbottom">
			<p class="info">
				<img src="{$moduleImagePath}/new.png" />
				<xsl:text>&#160;=&#160;</xsl:text>
				<xsl:value-of select="$i18n.link.newNotification" />.
			</p>
		</div>
	
		<div class="floatright">
			<div class="floatright marginleft">
				<xsl:call-template name="addReverseSortingCheckbox"/>
			</div>
			<div class="floatright">
				<xsl:call-template name="addSortingCriteriasDropdown"/>
			</div>
		</div>
		
		<div class="content-box">
			<h1 class="header">
				<span><xsl:value-of select="$i18n.AllLinks"/></span>
			</h1>
			<div class="content">
				<xsl:choose>
					<xsl:when test="Links/Link">
						<xsl:apply-templates select="Links/Link" mode="list"/>
					</xsl:when>
					<xsl:otherwise>
						<div class="floatleft full margintop marginbottom hover">
							<xsl:value-of select="$i18n.NoLinks"/>
						</div>
					</xsl:otherwise>
				</xsl:choose>
			</div>
		</div>
		
		<xsl:if test="../isAdmin or ../isSysAdmin">
			<div class="floatleft full bigmarginbottom">
				<div class="text-align-right">
					<a href="{$modulePath}/addLink">
						<xsl:value-of select="$i18n.AddLink" />
					</a>	
				</div>
			</div>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="Link" mode="list">
		<div class="floatleft full margintop marginbottom hover">
			<div class="floatleft ninety">
				<p class="nomargin">
					<xsl:call-template name="replaceLineBreak">
						<xsl:with-param name="string" select="description"/>
					</xsl:call-template>
					<xsl:if test="postedInMillis > ../../userLastLoginInMillis">
						<xsl:text>&#160;</xsl:text>
						<img src="{$moduleImagePath}/new.png" class="vertical-align-middle" />
					</xsl:if>
				</p>
			</div>
			<div class="floatleft ninety">
				<a href="{url}" target="_blank">
					<xsl:choose>
						<xsl:when test="string-length(url)>60">
							<xsl:value-of select="substring(url,1,60)"/>...
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="url"/>
						</xsl:otherwise>
					</xsl:choose>
				</a>
			</div>
			<div class="floatright ten">
				<xsl:if test="hasUpdateAccess">
					<div class="floatright margintop marginleft">
						<a href="{$modulePath}/deleteLink/{linkID}" onclick="return confirmDelete('{$i18n.DeleteLink} {url}?')">
							<img src="{$moduleImagePath}/delete.png" title="{$i18n.DeleteLink}"/>
						</a>	
					</div>
					<div class="floatright margintop">
						<a href="{$modulePath}/updateLink/{linkID}" >
							<img src="{$moduleImagePath}/edit.png" title="{$i18n.EditLink}"/>
						</a>	
					</div>
				</xsl:if>
			</div>
			<div class="floatleft full marginbottom">
				<div class="floatleft">
					<div class="addedBy">
						<xsl:value-of select="$i18n.AddedBy" />: 
						<xsl:choose>
							<xsl:when test="poster/user">
								<xsl:value-of select="poster/user/firstname"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="poster/user/lastname"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$i18n.RemovedUser" />
							</xsl:otherwise>
						</xsl:choose>
						<xsl:text>,&#160;</xsl:text>
						<xsl:value-of select="postedDate"/>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$i18n.atTime" />
						<xsl:text>:</xsl:text>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="postedTime"/>
					</div>
				</div>
				<div class="marginleft floatleft">
					<div class="addedBy">
						<xsl:value-of select="$i18n.ShownFor" />:<xsl:text>&#x20;</xsl:text>
						<xsl:call-template name="createPublishingInformation">
							<xsl:with-param name="element" select="." />
							<xsl:with-param name="id" select="linkID" />
							<xsl:with-param name="moduleImagePath" select="$moduleImagePath" />
							<xsl:with-param name="displayInline" select="'true'" />
						</xsl:call-template>
					</div>
				</div>
			</div>
		</div>
	</xsl:template>
	
	<xsl:template match="AddLink">
		<form method="post" name="addLink" id="addLink" action="{/Document/requestinfo/uri}" accept-charset="ISO-8859-1">
			<div class="content-box">
				<h1 class="header">
					<span><xsl:value-of select="$i18n.AddLink" /></span>
				</h1>
				<div class="content">
					<xsl:apply-templates select="validationException/validationError" />
					<p>
						<label for="url"><xsl:value-of select="$i18n.URL" />:</label><br />
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="'url'"/>
							<xsl:with-param name="name" select="'url'"/>
							<xsl:with-param name="size" select="72"/>
							<xsl:with-param name="requestparameters" select="requestparameters"/>
						</xsl:call-template>
					</p>
					<p>
						<label for="description">
							<xsl:value-of select="$i18n.Description" />
							<xsl:text>:</xsl:text>
						</label>
						<br/>
						<xsl:call-template name="createTextArea">
							<xsl:with-param name="id" select="'description'"/>
							<xsl:with-param name="name" select="'description'"/>
							<xsl:with-param name="requestparameters" select="requestparameters" />
						</xsl:call-template>
					</p>
					<p>
						<label for="publish"><xsl:value-of select="$i18n.link.publishTo" />:</label>				
						<div class="content-box-no-header">
							<xsl:choose>
								<xsl:when test="schools/school">
									<xsl:call-template name="createTreeview">
										<xsl:with-param name="id" select="'link-communitybase-treeview'" />
										<xsl:with-param name="schools" select="schools" />
										<xsl:with-param name="requestparameters" select="requestparameters" />
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<p><xsl:value-of select="$i18n.noaccess" /></p>
								</xsl:otherwise>
							</xsl:choose>
						</div>
					</p>
					<div class="text-align-right">
						<input type="submit" value="{$i18n.AddLink}" />
					</div>
				</div>
			</div>
		</form>
	</xsl:template>
	
	<xsl:template match="UpdateLink">
		<form method="post" name="updateLink" id="updateLink" action="{/Document/requestinfo/uri}" accept-charset="ISO-8859-1">
			<div class="content-box">
				<h1 class="header">
					<span>
						<xsl:value-of select="$i18n.EditLink" />
						<xsl:text>:</xsl:text>
						<xsl:text>&#x20;</xsl:text>
						<xsl:text>"</xsl:text>
						<xsl:value-of select="substring(Link/description,1, 30)"/>
						<xsl:text>"</xsl:text>
					</span>
				</h1>
				<div class="content">
					<xsl:apply-templates select="validationException/validationError" />
					<p>
						<label for="url"><xsl:value-of select="$i18n.URL" />:</label><br />
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="'url'"/>
							<xsl:with-param name="name" select="'url'"/>
							<xsl:with-param name="element" select="Link"/>
							<xsl:with-param name="requestparameters" select="requestparameters"/>
						</xsl:call-template>
					</p>
					<p>
						<label for="description">
							<xsl:value-of select="$i18n.Description" />
							<xsl:text>:</xsl:text>
						</label>
						<br/>
						<xsl:call-template name="createTextArea">
							<xsl:with-param name="id" select="'description'"/>
							<xsl:with-param name="name" select="'description'"/>
							<xsl:with-param name="element" select="Link"/>
							<xsl:with-param name="requestparameters" select="requestparameters" />
						</xsl:call-template>
					</p>
					<p>
						<label for="publish"><xsl:value-of select="$i18n.link.publishTo" />:</label>				
						<div class="content-box-no-header">
							<xsl:choose>
								<xsl:when test="schools/school">
									<xsl:call-template name="createTreeview">
										<xsl:with-param name="id" select="'link-communitybase-treeview'" />
										<xsl:with-param name="element" select="Link" />
										<xsl:with-param name="schools" select="schools" />
										<xsl:with-param name="requestparameters" select="requestparameters" />
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<p><xsl:value-of select="$i18n.noaccess" /></p>
								</xsl:otherwise>
							</xsl:choose>
						</div>
					</p>
					<div class="text-align-right">
						<input type="submit" value="{$i18n.SaveChanges}" />
					</div>
				</div>
			</div>
		</form>
	</xsl:template>
	
	<!-- Localization of sorting criteria -->
	<xsl:template name="getSortingCriteraName">
		<xsl:param name="sortingCriteria"/>
		<xsl:choose>
			<xsl:when test="$sortingCriteria = 'description'">
				<xsl:value-of select="$i18n.Description"/>
			</xsl:when>
			<xsl:when test="$sortingCriteria = 'posted'">
				<xsl:value-of select="$i18n.Posted"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$i18n.URL"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- Dropdown for sorting criteria, selection by request params -->
	<xsl:template name="addSortingCriteriasDropdown">
		<xsl:param name="sortingCriterias" select="SortingCriterias"/>
		<xsl:param name="requestparameters" select="requestparameters"/>
		<div class="floatright">
    		<xsl:value-of select="$i18n.OrderBy"/>
    		<xsl:text>&#160;</xsl:text>
        	<select id="sortingcriterias" name="orderby" ref="{$modulePath}">
				<xsl:for-each select="$sortingCriterias/Criteria" >
					<option value="{.}">
						<xsl:if test="$requestparameters/parameter[name = 'orderby']/value = .">
							<xsl:attribute name="selected" />
						</xsl:if>
						<xsl:call-template name="getSortingCriteraName">
							<xsl:with-param name="sortingCriteria" select="."/>
						</xsl:call-template>
					</option>
				</xsl:for-each>
			</select>
    	</div>
	</xsl:template>
	
	<!-- Checkbox for reverse sort order, selection by request params -->
	<xsl:template name="addReverseSortingCheckbox">
		<xsl:param name="requestparameters" select="requestparameters"/>
		<div class="floatright">
			<input id="reverseorderfalse" name="reverse" type="radio" value="false" ref="{$modulePath}">
				<xsl:if test="$requestparameters/parameter[name = 'reverse']/value = 'false'">
					<xsl:attribute name="checked"/>
				</xsl:if>
			</input>
			<label for="reverseorderfalse">
				<xsl:choose>
					<xsl:when test="$requestparameters/parameter[name = 'orderby']/value = 'description'">
						<xsl:value-of select="$i18n.AlphaToOmega"/>
					</xsl:when>
					<xsl:when test="$requestparameters/parameter[name = 'orderby']/value = 'posted'">
						<xsl:value-of select="$i18n.NewestFirst"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.AlphaToOmega"/>
					</xsl:otherwise>
				</xsl:choose>
			</label>
			<input id="reverseordertrue" name="reverse" type="radio" value="true" ref="{$modulePath}">
				<xsl:if test="$requestparameters/parameter[name = 'reverse']/value = 'true'">
					<xsl:attribute name="checked"/>
				</xsl:if>
			</input>
			<label for="reverseordertrue">
				<xsl:choose>
					<xsl:when test="$requestparameters/parameter[name = 'orderby']/value = 'description'">
						<xsl:value-of select="$i18n.OmegaToAlpha"/>
					</xsl:when>
					<xsl:when test="$requestparameters/parameter[name = 'orderby']/value = 'posted'">
						<xsl:value-of select="$i18n.OldestFirst"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.OmegaToAlpha"/>
					</xsl:otherwise>
				</xsl:choose>
			</label>
		</div>
	</xsl:template>
	
	<xsl:template match="validationError">
		<xsl:if test="fieldName and validationErrorType">
			<p class="error">
				<xsl:choose>
					<xsl:when test="validationErrorType='RequiredField'">
						<xsl:text><xsl:value-of select="$validationError.RequiredField"/></xsl:text>
					</xsl:when>
					<xsl:when test="validationErrorType='InvalidFormat'">
						<xsl:text><xsl:value-of select="$validationError.InvalidFormat"/></xsl:text>
					</xsl:when>
					<xsl:when test="validationErrorType='TooShort'">
						<xsl:text><xsl:value-of select="$validationError.TooShort" /></xsl:text>
					</xsl:when>
					<xsl:when test="validationErrorType='TooLong'">
						<xsl:text><xsl:value-of select="$validationError.TooLong" /></xsl:text>
					</xsl:when>									
					<xsl:otherwise>
						<xsl:text><xsl:value-of select="$validationError.unknownValidationErrorType"/></xsl:text>
					</xsl:otherwise>					
				</xsl:choose>
				
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:choose>
					<xsl:when test="fieldName = 'url'">
						<xsl:value-of select="$validationError.field.url"/>!
					</xsl:when>
					<xsl:when test="fieldName = 'description'">
						<xsl:value-of select="$validationError.field.description"/>!
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="fieldName"/>
					</xsl:otherwise>
				</xsl:choose>	
			</p>
		</xsl:if>
		<xsl:if test="messageKey">
			<p class="error">
				<xsl:choose>
					<xsl:when test="messageKey='NoGroup'">
						<xsl:value-of select="$validationError.messageKey.NoGroup"/>!
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$validationError.unknownMessageKey" />!
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
	</xsl:template>
	
</xsl:stylesheet>