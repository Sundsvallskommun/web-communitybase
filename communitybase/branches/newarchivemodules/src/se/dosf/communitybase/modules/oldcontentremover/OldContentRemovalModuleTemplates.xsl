<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl" />

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/jquery.tablesorter.min.js
		/js/init.tablesorter.js
		/utils/js/datepicker/datepicker.js
		/utils/js/communitybase.common.js
	</xsl:variable>

	<xsl:variable name="links">
		/css/tablesorter/tablesorter.css
		/utils/js/datepicker/css/datepicker.css
	</xsl:variable>

	<xsl:template match="Document">

		<div id="WeekMenuTemplateAdminModule" class="contentitem">

			<xsl:apply-templates select="SelectModules" />
			<xsl:apply-templates select="SelectContent" />
			<xsl:apply-templates select="ContentDeleted" />
		</div>

	</xsl:template>

	<xsl:template match="SelectModules">

		<h1>
			<xsl:value-of select="$i18n.OldContentRemoval.SelectModules.title" />
			<xsl:value-of select="group/name" />
		</h1>
		
		<div class="full floatleft marginbottom">
			<xsl:value-of select="$i18n.OldContentRemoval.SelectModules.description" />
			<xsl:value-of select="MinimumAge" />
			<xsl:value-of select="$i18n.OldContentRemoval.SelectModules.descriptionPost" />
		</div>

		<xsl:apply-templates select="validationException/validationError" />
		
		<xsl:choose>
			<xsl:when test="Modules">

				<form method="post" action="{/Document/requestinfo/uri}">

					<div class="full bigmarginbottom">
					
						<label for="endDate"><xsl:value-of select="$i18n.Field.EndDate" />: </label>
					
						<xsl:call-template name="createDateField">
							<xsl:with-param name="id" select="'endDate'" />
							<xsl:with-param name="name" select="'endDate'" />
							<xsl:with-param name="class" select="'dateformat-Y-ds-m-ds-d show-weeks fill-grid no-animation disable-drag'" />
						</xsl:call-template>
					</div>
					
					<table class="full border">
						<thead class="sortable">
							<tr>
								<th>
									<xsl:value-of select="$i18n.ModuleName" />
								</th>
								<th width="50" class="no-sort">
									<xsl:value-of select="$i18n.OldContentRemoval.SelectModules.SelectForOldRemoval" />
								</th>
							</tr>
						</thead>

						<tbody>
							<xsl:apply-templates select="Modules/Module" />
						</tbody>

					</table>

					<div class="floatright">
						<input type="submit" value="{$i18n.Continue}" />
					</div>

				</form>

			</xsl:when>
			<xsl:otherwise>

				<div class="floatleft full">
					<xsl:value-of
						select="$i18n.OldContentRemoval.SelectModules.NoModules" />
				</div>

			</xsl:otherwise>
		</xsl:choose>

	</xsl:template>

	<xsl:template name="ModuleName">

		<xsl:value-of select="Name"/>

	</xsl:template>

	<xsl:template match="Module">

		<tr>
			<td>
				<xsl:call-template name="ModuleName"/>
			</td>
			<td class="text-align-center">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" select="'module'" />
					<xsl:with-param name="value" select="ID" />
					<xsl:with-param name="checked" select="'true'" />
					<xsl:with-param name="id">
						<xsl:value-of select="'module-'"/>
						<xsl:value-of select="ID"/>
					</xsl:with-param>
					<xsl:with-param name="class" select="'vertical-align-middle'" />
					<xsl:with-param name="requestparameters" select="../../requestparameters" />
				</xsl:call-template>
			</td>
		</tr>

	</xsl:template>

	<xsl:template match="SelectContent">
	
		<script type="text/javascript">
			$(document).ready(function() {
				$(".all-checkbox").click(function(){
					var c = $(this).prop("checked");
					$(this).closest("table").find("input[type='checkbox']").prop("checked", c);
				});	
			});
		</script>
	
		<h1>
			<xsl:value-of select="$i18n.OldContentRemoval.SelectContent.title" />
			<xsl:value-of select="group/name" />
		</h1>
		
		<div class="full floatleft marginbottom">
			<p>
				<xsl:value-of select="$i18n.OldContentRemoval.SelectContent.descriptionPre" />
				
				<strong>
					<xsl:value-of select="count(Contents/Module//Content)" />
				</strong>
				
				<xsl:value-of select="$i18n.OldContentRemoval.SelectContent.descriptionMid" />
				
				<strong>
					<xsl:value-of select="EndDate" />
				</strong>
				
				<xsl:value-of select="$i18n.OldContentRemoval.SelectContent.descriptionPost" />
			</p>
			
			<p>
				<xsl:value-of select="$i18n.OldContentRemoval.SelectContent.description2" />
			</p>
		</div>

		<xsl:apply-templates select="validationException/validationError" />

		<form method="post" action="{/Document/requestinfo/contextpath}/{/Document/module/alias}/delete">

			<xsl:for-each select="Contents/Module">
			
				<xsl:call-template name="createHiddenField">
					<xsl:with-param name="name" select="'module'" />
					<xsl:with-param name="value" select="ID" />
				</xsl:call-template>
	
				<table class="floatleft full border">
					<thead class="sortable">
						<tr>
							<th>
								<xsl:call-template name="ModuleName"/>
							</th>
							<th width="110">
								<xsl:choose>
									<xsl:when test="Name = 'PictureGalleryModule' or Name = 'FileArchiveModule'">
										<xsl:value-of select="$i18n.LastModified"/>
									</xsl:when>
									<xsl:when test="Name = 'NewsLetterModule'">
										<xsl:value-of select="$i18n.CreationDate"/>
									</xsl:when>
									<xsl:when test="Name = 'CalendarModule'">
										<xsl:value-of select="$i18n.EndDate"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$i18n.CreationDate"/>
									</xsl:otherwise>
								</xsl:choose>
							</th>
							<th width="70" class="no-sort">
								<xsl:value-of select="$i18n.OldContentRemoval.SelectContent.SelectForDeletion" />
								
								<xsl:call-template name="createCheckbox">
									<xsl:with-param name="id" select="'SelectAll'" />
									<xsl:with-param name="class" select="'vertical-align-middle all-checkbox'" />
									<xsl:with-param name="requestparameters" select="../../requestparameters" />
								</xsl:call-template>
							</th>
						</tr>
					</thead>

					<tbody>
					
						<xsl:choose>
							<xsl:when test="Content">
									
										<xsl:apply-templates select="Content" />
									
							</xsl:when>
							<xsl:otherwise>
			
								<tr>
									<td colspan="3">
										<xsl:value-of	select="$i18n.OldContentRemoval.SelectContent.NoOldContent" />
									</td>
								</tr>
			
							</xsl:otherwise>
						</xsl:choose>
						
					</tbody>

				</table>

			</xsl:for-each>
			
			<xsl:call-template name="createHiddenField">
				<xsl:with-param name="name" select="'groupID'" />
				<xsl:with-param name="value" select="group/groupID" />
			</xsl:call-template>
			
			<xsl:call-template name="createHiddenField">
				<xsl:with-param name="name" select="'endDate'" />
				<xsl:with-param name="value" select="EndDate" />
			</xsl:call-template>

			<div class="floatright">
				<input type="submit" value="{$i18n.Continue}" onclick="return confirm('{$i18n.OldContentRemoval.SelectContent.DeletePostsConfirmation}?')"/>
			</div>

		</form>

	</xsl:template>
	
	<xsl:template match="Content">
	
		<!-- TODO move all module specific i18n stuff to the corresponding module -->
		<xsl:variable name="name">
			<xsl:choose>
				<xsl:when test="Type = 'se.dosf.communitybase.modules.pictureGallery.beans.Gallery'">
					<xsl:call-template name="string-replace-all">
				    <xsl:with-param name="text" select="Name" />
				    <xsl:with-param name="replace" select="'%PICTURES%'" />
				    <xsl:with-param name="by" select="$i18n.Content.Pictures" />
				  </xsl:call-template>
				</xsl:when>
				<xsl:when test="Type = 'se.dosf.communitybase.modules.filearchive.beans.Section'">
					<xsl:call-template name="string-replace-all">
				    <xsl:with-param name="text" select="Name" />
				    <xsl:with-param name="replace" select="'%FILES%'" />
				    <xsl:with-param name="by" select="$i18n.Content.Files" />
				  </xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="Name"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<tr>
			<td>
				<a
					href="{/Document/requestinfo/currentURI}{URL}" target="_blank"
					title="{$i18n.Open}: {$name}">
					<xsl:value-of select="$name" />
				</a>
			</td>
			<td>
				<a
					href="{/Document/requestinfo/currentURI}{URL}" target="_blank"
					title="{$i18n.Open}: {$name}">
					<xsl:value-of select="CreationDate" />
				</a>
			</td>
			<td class="text-align-center">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="name" >
						<xsl:value-of select="'contentFromModule-'" />
						<xsl:value-of select="../ID" />
					</xsl:with-param>
					<xsl:with-param name="id" >
						<xsl:value-of select="'contentFromModule-'" />
						<xsl:value-of select="../ID" />
						<xsl:value-of select="ID" />
					</xsl:with-param>
					<xsl:with-param name="value" select="ID" />
					<xsl:with-param name="class" select="'vertical-align-middle'" />
				</xsl:call-template>
			</td>
		</tr>

	</xsl:template>

	<xsl:template match="ContentDeleted">

		<h1>
			<xsl:value-of select="$i18n.OldContentRemoval.ContentDeleted.title" />
		</h1>
		
		<xsl:value-of select="$i18n.OldContentRemoval.ContentDeleted.DeletedPre"/>
		<xsl:text>&#160;</xsl:text>
		<xsl:value-of select="DeletedCount"/>
		<xsl:text>&#160;</xsl:text>
		<xsl:value-of select="$i18n.OldContentRemoval.ContentDeleted.DeletedPost"/>

	</xsl:template>

	<xsl:template match="validationError">

		<xsl:if test="fieldName and validationErrorType and not(messageKey)">
			<p class="error">
			
				<xsl:choose>
					<xsl:when test="fieldName = 'module' and validationErrorType='RequiredField'">
						<xsl:value-of select="$i18n.ValidationError.MustSelectModules" />
					</xsl:when>
					<xsl:otherwise>
					
						<xsl:choose>
							<xsl:when test="validationErrorType='RequiredField'">
								<xsl:value-of select="$i18n.ValidationError.RequiredField" />
							</xsl:when>
							<xsl:when test="validationErrorType='InvalidFormat'">
								<xsl:value-of select="$i18n.ValidationError.InvalidFormat" />
							</xsl:when>
							<xsl:when test="validationErrorType='TooShort'">
								<xsl:value-of select="$i18n.ValidationError.TooShort" />
							</xsl:when>
							<xsl:when test="validationErrorType='TooLong'">
								<xsl:value-of select="$i18n.ValidationError.TooLong" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$i18n.ValidationError.UnknownValidationErrorType" />
							</xsl:otherwise>
						</xsl:choose>
		
						<xsl:text>&#x20;</xsl:text>
		
						<xsl:choose>
							<xsl:when test="fieldName = 'endDate'">
								<xsl:value-of select="$i18n.Field.EndDate" />
							</xsl:when>
							<xsl:when test="fieldName = 'module'">
								<xsl:value-of select="$i18n.Module" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="fieldName" />
							</xsl:otherwise>
						</xsl:choose>
						
					</xsl:otherwise>
				</xsl:choose>

				<xsl:text>!</xsl:text>
			</p>
		</xsl:if>

		<xsl:if test="messageKey">
			<p class="error">
				<xsl:choose>
					<xsl:when test="messageKey='EndDateIsInTheFuture'">
						<xsl:value-of select="$i18n.ValidationError.EndDateIsInTheFuture" />
					</xsl:when>
					<xsl:when test="messageKey='EndDateIsTooRecent'">
						<xsl:value-of select="$i18n.ValidationError.EndDateIsTooRecentPre" />
						<xsl:value-of select="fieldName" />
						<xsl:value-of select="$i18n.ValidationError.EndDateIsTooRecentPost" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.ValidationError.UnknownFault" />
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>

	</xsl:template>
	
	<xsl:template name="string-replace-all">
	  <xsl:param name="text" />
	  <xsl:param name="replace" />
	  <xsl:param name="by" />
	  <xsl:choose>
	    <xsl:when test="contains($text, $replace)">
	      <xsl:value-of select="substring-before($text,$replace)" />
	      <xsl:value-of select="$by" />
	      <xsl:call-template name="string-replace-all">
	        <xsl:with-param name="text"
	        select="substring-after($text,$replace)" />
	        <xsl:with-param name="replace" select="$replace" />
	        <xsl:with-param name="by" select="$by" />
	      </xsl:call-template>
	    </xsl:when>
	    <xsl:otherwise>
	      <xsl:value-of select="$text" />
	    </xsl:otherwise>
	  </xsl:choose>
	</xsl:template>

</xsl:stylesheet>

