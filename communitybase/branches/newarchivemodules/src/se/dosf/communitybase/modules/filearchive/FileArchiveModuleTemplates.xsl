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
		/js/filearchive-sortable.js
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
			
			<xsl:apply-templates select="ListSections" />
			<xsl:apply-templates select="AddSection" />
			<xsl:apply-templates select="UpdateSection" />
			
			<xsl:apply-templates select="ListFiles" />
			<xsl:apply-templates select="AddFile" />
			<xsl:apply-templates select="UpdateFile" />
			
		</div>
	</xsl:template>
	
	<xsl:template match="ListSections">
	
		<div class="floatleft marginbottom">
			<p class="info">
				<img src="{$moduleImagePath}/new.png" />
				<xsl:text>&#160;=&#160;</xsl:text>
				<xsl:value-of select="$i18n.file.newNotification" />.
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
	
		<xsl:apply-templates select="Sections/Section" mode="list"/>
		
		<xsl:if test="../isAdmin or ../isSysAdmin">
			<div class="floatleft full bigmarginbottom">
				<div class="text-align-right">
					<a href="{$modulePath}/addSection">
						<xsl:value-of select="$i18n.AddSection" />
					</a>	
				</div>
			</div>
		</xsl:if>
	</xsl:template>

	<xsl:template match="Section" mode="list">
		
		<div class="content-box">
			<a name="{sectionID}"/>
			<h1 class="header">
				<span>
					<div class="display-inline-block full">
						<div class="floatleft ninety">
							<xsl:value-of select="name"/>
						</div>
						<xsl:if test="hasUpdateAccess">
							<div class="floatright ten">
								<div class="floatright marginleft">
									<a href="{$modulePath}/deleteSection/{sectionID}" onclick="return confirmDelete('{$i18n.DeleteSection} {name}?')">
										<img src="{$moduleImagePath}/delete.png"/>
									</a>
								</div>	
								<div class="floatright">
									<a href="{$modulePath}/updateSection/{sectionID}">
										<img src="{$moduleImagePath}/edit.png"  />
									</a>
								</div>
							</div>
						</xsl:if>
					</div>
				</span>
			</h1>
			<div class="content">
				<div class="bigmarginleft floatright">
					<div class="addedBy">
						<xsl:value-of select="$i18n.ShownFor" />:<xsl:text>&#x20;</xsl:text>
							<xsl:call-template name="createPublishingInformation">
							<xsl:with-param name="element" select="." />
							<xsl:with-param name="id" select="sectionID" />
							<xsl:with-param name="moduleImagePath" select="$moduleImagePath" />
							<xsl:with-param name="displayInline" select="'true'" />
						</xsl:call-template>
					</div>
				</div>
				
				<xsl:choose>
					<xsl:when test="files/File">
						<xsl:apply-templates select="files/File" mode="listBySection"/>
					</xsl:when>
					<xsl:otherwise>
						<div class="floatleft full margintop marginbottom hover">
							<xsl:value-of select="$i18n.NoFiles"/>
						</div>
					</xsl:otherwise>
				</xsl:choose>
				
				
				<xsl:if test="hasUpdateAccess">
					<div class="floatleft full bigmarginbottom">
						<div class="text-align-right">
							<a href="{$modulePath}/addFile/{sectionID}">
								<xsl:value-of select="$i18n.AddFile" />
							</a>	
						</div>
					</div>
				</xsl:if>
				
			</div>
		</div>
	</xsl:template>
	
	<xsl:template match="AddSection">
		<form method="post" name="addSection" id="addSection" action="{/Document/requestinfo/uri}" accept-charset="ISO-8859-1">
			<div class="content-box">
				<h1 class="header">
					<span><xsl:value-of select="$i18n.AddSection" /></span>
				</h1>
				<div class="content">
					<xsl:apply-templates select="validationException/validationError" />
					<p>
						<label for="name"><xsl:value-of select="$i18n.Name" />:</label><br />
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="'id'"/>
							<xsl:with-param name="name" select="'name'"/>
							<xsl:with-param name="requestparameters" select="requestparameters"/>
						</xsl:call-template>
					</p>
					<p>
						<label for="publish"><xsl:value-of select="$i18n.section.publishTo" />:</label>				
						<div class="content-box-no-header">
							<xsl:choose>
								<xsl:when test="schools/school">
									<xsl:call-template name="createTreeview">
										<xsl:with-param name="id" select="'section-communitybase-treeview'" />
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
						<input type="submit" value="{$i18n.AddSection}" />
					</div>
				</div>
			</div>
		</form>
	</xsl:template>
	
	<xsl:template match="UpdateSection">
		<form method="post" name="updateSection" id="updateSection" action="{/Document/requestinfo/uri}" accept-charset="ISO-8859-1">
			<div class="content-box">
				<h1 class="header">
					<span>
						<xsl:value-of select="$i18n.EditSection" />
						<xsl:text>:</xsl:text>
						<xsl:text>&#x20;</xsl:text>
						<xsl:value-of select="Section/name" />
					</span>
				</h1>
				<div class="content">
					<xsl:apply-templates select="validationException/validationError" />
					<p>
						<label for="name"><xsl:value-of select="$i18n.Name" />:</label><br />
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="'id'"/>
							<xsl:with-param name="name" select="'name'"/>
							<xsl:with-param name="element" select="Section"/>
							<xsl:with-param name="requestparameters" select="requestparameters"/>
						</xsl:call-template>
					</p>
					<p>
						<label for="publish"><xsl:value-of select="$i18n.section.publishTo" />:</label>				
						<div class="content-box-no-header">
							<xsl:choose>
								<xsl:when test="schools/school">
									<xsl:call-template name="createTreeview">
										<xsl:with-param name="id" select="'section-communitybase-treeview'" />
										<xsl:with-param name="element" select="Section" />
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
	
	<xsl:template match="ListFiles">
	
		<div class="floatleft marginbottom">
			<p class="info">
				<img src="{$moduleImagePath}/new.png" />
				<xsl:text>&#160;=&#160;</xsl:text>
				<xsl:value-of select="$i18n.file.newNotification" />.
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
				<span><xsl:value-of select="$i18n.AllFiles"/></span>
			</h1>
			<div class="content">
				<xsl:choose>
					<xsl:when test="Files/File">
						<xsl:apply-templates select="Files/File" mode="list"/>
					</xsl:when>
					<xsl:otherwise>
						<div class="floatleft full margintop marginbottom hover">
							<xsl:value-of select="$i18n.NoFiles"/>
						</div>
					</xsl:otherwise>
				</xsl:choose>
			</div>
		</div>
	</xsl:template>
	
	<!-- File listing in section hierarchy, each section -->
	<xsl:template match="File" mode="listBySection">
		<xsl:call-template name="file">
			<xsl:with-param name="file" select="."/>
			<xsl:with-param name="userLastLoginInMillis" select="../../../../userLastLoginInMillis"/>
			<xsl:with-param name="hasUpdateAccess" select="../../hasUpdateAccess" />
		</xsl:call-template>
	</xsl:template>
	
	<!-- File listing in flat hierarchy, all sections -->
	<xsl:template match="File" mode="list">
		<xsl:call-template name="file">
			<xsl:with-param name="file" select="."/>
			<xsl:with-param name="userLastLoginInMillis" select="../../userLastLoginInMillis"/>
			<xsl:with-param name="section" select="Section"/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="AddFile">
		<form method="post" name="addFile" id="addFile" action="{/Document/requestinfo/uri}" enctype="multipart/form-data" accept-charset="ISO-8859-1">
			<div class="content-box">
				<h1 class="header">
					<span>
						<xsl:value-of select="$i18n.AddFile" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$i18n.in" />
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="Section/name" />
					</span>
				</h1>
				<div class="content">
					<xsl:apply-templates select="validationException/validationError" />
					<p>
						<label for="file"><xsl:value-of select="$i18n.File" />:</label>
						<br/>
						<input type="file" size="55" id="file" name="file" />
						<input type="hidden" name="sectionID" value="{Section/sectionID}" />
					</p>
					<div class="floatleft full marginbottom">
						<p class="info">
							<xsl:if test="allowedFileTypes">
								<xsl:value-of select="$i18n.AllowedFiletypes" />: 
								<xsl:for-each select = "allowedFileTypes/extension">
									<xsl:value-of select="." />, <xsl:text></xsl:text>
								</xsl:for-each>
							</xsl:if>
						</p>
						<p class="info">
							<xsl:text>(</xsl:text>
							<xsl:value-of select="$i18n.MaxFilesizeIs" />
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="diskThreshold" />
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$i18n.MB" />
							<xsl:text>)</xsl:text>
						</p>
					</div>
					<p>
						<label for="description">
							<xsl:value-of select="$i18n.Description" />
							<xsl:text>&#160;</xsl:text>
							<xsl:text>(</xsl:text>
							<xsl:value-of select="$i18n.optional" />
							<xsl:text>)</xsl:text>
							<xsl:text>:</xsl:text>
						</label>
						<br/>
						<xsl:call-template name="createTextArea">
							<xsl:with-param name="id" select="'description'"/>
							<xsl:with-param name="name" select="'description'"/>
							<xsl:with-param name="requestparameters" select="requestparameters" />
						</xsl:call-template>
					</p>
					<div class="text-align-right">
						<input type="submit" value="{$i18n.AddFile}" />
					</div>
				</div>
			</div>
		</form>
	</xsl:template>
	
	<xsl:template match="UpdateFile">
		<form method="post" name="updateFile" id="updateFile" action="{/Document/requestinfo/uri}" accept-charset="ISO-8859-1">
			<div class="content-box">
				<h1 class="header">
					<span>
						<xsl:value-of select="$i18n.EditFile" />
						<xsl:text>:</xsl:text>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="File/filename"/>
					</span>
				</h1>
				<div class="content">
					<xsl:apply-templates select="validationException/validationError" />
					<p>
						<xsl:value-of select="$i18n.Name" />
						<xsl:text>:</xsl:text>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="File/filename"/>
					</p>
					<p>
						<label for="description">
							<xsl:value-of select="$i18n.Description" />
							<xsl:text>&#160;</xsl:text>
							<xsl:text>(</xsl:text>
							<xsl:value-of select="$i18n.optional" />
							<xsl:text>)</xsl:text>
							<xsl:text>:</xsl:text>
						</label>
						<br/>
						<xsl:call-template name="createTextArea">
							<xsl:with-param name="id" select="'description'"/>
							<xsl:with-param name="name" select="'description'"/>
							<xsl:with-param name="element" select="File"/>
							<xsl:with-param name="requestparameters" select="requestparameters" />
						</xsl:call-template>
					</p>
					<p>
						<label for="sectionID"><xsl:value-of select="$i18n.Section" />:</label>
						<br/>
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id" select="'sectionID'"/>
							<xsl:with-param name="name" select="'sectionID'"/>
							<xsl:with-param name="element" select="Section"/>
							<xsl:with-param name="valueElementName" select="'sectionID'"/>
							<xsl:with-param name="labelElementName" select="'name'"/>
							<xsl:with-param name="selectedValue" select="File/Section/sectionID"/>
							<xsl:with-param name="requestparameters" select="requestparameters"/>
						</xsl:call-template>
					</p>
					<div class="text-align-right">
						<input type="submit" value="{$i18n.SaveChanges}" />
					</div>
				</div>
			</div>
		</form>
	</xsl:template>
	
	
	<!-- Generic file listing template -->
	<xsl:template name="file">
		<xsl:param name="file"/>
		<xsl:param name="userLastLoginInMillis" />
		<xsl:param name="hasUpdateAccess" select="hasUpdateAccess" />
		
		<xsl:param name="section" select="null"/>
		<div class="floatleft full margintop marginbottom hover">
			<div class="floatleft ninety">
				<a href="{$modulePath}/downloadFile/{$file/fileID}">
					<xsl:value-of select="$file/filename"/>
				</a>
				<xsl:text>&#160;(</xsl:text>
				<xsl:value-of select="formatedSize" />
				<xsl:text>)</xsl:text>
				<xsl:if test="$file/postedInMillis > $userLastLoginInMillis">
					<xsl:text>&#160;</xsl:text>
					<img src="{$moduleImagePath}/new.png" class="vertical-align-middle" />
				</xsl:if>
			</div>
			<div class="floatright ten">
				<xsl:if test="$hasUpdateAccess">
					<div class="floatright margintop marginleft">
						<a href="{$modulePath}/deleteFile/{$file/fileID}" onclick="return confirmDelete('{$i18n.DeleteFile} {$file/filename}?')">
							<img src="{$moduleImagePath}/delete.png" title="{$i18n.DeleteFile}"/>
						</a>	
					</div>
					<div class="floatright margintop">
						<a href="{$modulePath}/updateFile/{$file/fileID}" >
							<img src="{$moduleImagePath}/edit.png" title="{$i18n.EditFile}"/>
						</a>	
					</div>
				</xsl:if>
			</div>
			<xsl:if test="$section">
				<div class="floatleft ninety">
					<p class="tiny">
						<xsl:value-of select="$i18n.BelongsTo"/>
						<xsl:text>&#160;</xsl:text>
						<a href="{$modulePath}/sections/#{$section/sectionID}">
							<xsl:value-of select="$section/name"/>
						</a>
					</p>
				</div>
			</xsl:if>
			<div class="floatleft ninety">
				<p class="nomargin">
					<xsl:call-template name="replaceLineBreak">
						<xsl:with-param name="string" select="$file/description"/>
					</xsl:call-template>
				</p>
			</div>
			<div class="floatleft ninety marginbottom">
				<p class="addedBy">
					<xsl:value-of select="$i18n.AddedBy" />: 
					<xsl:choose>
						<xsl:when test="$file/poster/user">
							<xsl:value-of select="$file/poster/user/firstname"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$file/poster/user/lastname"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$i18n.RemovedUser" />
						</xsl:otherwise>
					</xsl:choose>
					<xsl:text>,&#160;</xsl:text>
					<xsl:value-of select="$file/postedDate"/>
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="$i18n.atTime" />
					<xsl:text>:</xsl:text>
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="$file/postedTime"/>
		 		</p>
			</div>
		</div>
	</xsl:template>
	
	<!-- Localization of sorting criteria -->
	<xsl:template name="getSortingCriteraName">
		<xsl:param name="sortingCriteria"/>
		<xsl:choose>
			<xsl:when test="$sortingCriteria = 'filename'">
				<xsl:value-of select="$i18n.Filename"/>
			</xsl:when>
			<xsl:when test="$sortingCriteria = 'posted'">
				<xsl:value-of select="$i18n.Posted"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$i18n.Section"/>
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
					<xsl:when test="$requestparameters/parameter[name = 'orderby']/value = 'filename'">
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
					<xsl:when test="$requestparameters/parameter[name = 'orderby']/value = 'filename'">
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
						<xsl:text><xsl:value-of select="$i18n.validationError.RequiredField"/></xsl:text>
					</xsl:when>
					<xsl:when test="validationErrorType='InvalidFormat'">
						<xsl:text><xsl:value-of select="$i18n.validationError.InvalidFormat"/></xsl:text>
					</xsl:when>
					<xsl:when test="validationErrorType='TooShort'">
						<xsl:text><xsl:value-of select="$i18n.validationError.TooShort" /></xsl:text>
					</xsl:when>
					<xsl:when test="validationErrorType='TooLong'">
						<xsl:text><xsl:value-of select="$i18n.validationError.TooLong" /></xsl:text>
					</xsl:when>									
					<xsl:when test="validationErrorType='Other'">
						<xsl:text><xsl:value-of select="$i18n.validationError.Other" /></xsl:text>
					</xsl:when>	
					<xsl:otherwise>
						<xsl:text><xsl:value-of select="$i18n.validationError.unknownValidationErrorType"/></xsl:text>
					</xsl:otherwise>					
				</xsl:choose>
				
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:choose>
					<xsl:when test="fieldName = 'name'">
						<xsl:value-of select="$i18n.validationError.field.name"/>!
					</xsl:when>
					<xsl:when test="fieldName = 'description'">
						<xsl:value-of select="$i18n.validationError.field.description"/>!
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
					<xsl:when test="messageKey='InvalidFileFormat'">
						<xsl:value-of select="$i18n.validationError.BadFileFormat"/>!
					</xsl:when>
					<xsl:when test="messageKey='NoFileAttached'">
						<xsl:value-of select="$i18n.validationError.NoFile"/>!
					</xsl:when>
					<xsl:when test="messageKey='FileTooSmall'">
						<xsl:value-of select="$i18n.validationError.FileTooSmall"/>!
					</xsl:when>
					<xsl:when test="messageKey='RequestedSectionNotFound'">
						<xsl:value-of select="$i18n.validationError.RequestedSectionNotFound"/>!
					</xsl:when>
					<xsl:when test="messageKey='FileSizeLimitExceeded'">
						<xsl:value-of select="$i18n.validationError.FileSizeLimitExceeded"/>!
					</xsl:when>																
					<xsl:when test="messageKey='NoGroup'">
						<xsl:value-of select="$i18n.validationError.NoGroup"/>!
					</xsl:when>
					<xsl:when test="messageKey='UnableToParseRequest'">
						<xsl:value-of select="$i18n.validationError.UnableToParseRequest"/>!
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.validationError.unknownMessageKey" />!
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
	</xsl:template>
	
</xsl:stylesheet>