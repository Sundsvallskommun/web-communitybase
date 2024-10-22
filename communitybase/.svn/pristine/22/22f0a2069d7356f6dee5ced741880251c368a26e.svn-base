<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="scriptPath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/js</xsl:variable>
	<xsl:variable name="imagePath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js?v=1
		/js/validation.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/addsectionmodule.js
	</xsl:variable>

	<xsl:template match="Document">
		
		<div id="AddSectionModule" class="contentitem">
			<xsl:apply-templates select="AddSection" />
			<xsl:apply-templates select="NoSectionTypesAvailable" />
		</div>
		
	</xsl:template>
	
	<xsl:template match="AddSection">
		
		<section>
			<header>
				<h1><xsl:value-of select="$i18n.CreateNewSection"/></h1>
			</header>
			
			<form id="create_room" method="post" action="{/Document/requestinfo/uri}" enctype="multipart/form-data" data-validation="">
				<div class="section-content">
					<xsl:if test="validationException/validationError[fieldName]">
						<xsl:apply-templates select="validationException/validationError[fieldName]" mode="sectionfields"/>
					</xsl:if>
					
					<xsl:if test="validationException/validationError[not(fieldName)]">
						<xsl:apply-templates select="validationException/validationError[not(fieldName)]" mode="general"/>
					</xsl:if>
					
					<div class="text-muted small mb-2">
						<i class="icons icon-info" aria-hidden="true"/>
						<span><xsl:value-of select="$i18n.RequiredFieldsAreMarked"/></span>
					</div>
					
					<div class="form-group">
						<label for="name" id="nameLabel" data-validation-required="{$i18n.validationError.Required.Section.Name}"><xsl:value-of select="$i18n.NamePlaceholder"/> *</label>
						
						<xsl:call-template name="createTextField">
							<xsl:with-param name="name" select="'name'"/>
							<xsl:with-param name="id" select="'name'"/>
							<xsl:with-param name="class" select="'form-control'"/>
							<xsl:with-param name="required" select="'required'"/>
							<xsl:with-param name="maxlength" select="'255'"/>
							<xsl:with-param name="data-validation" select="'#nameLabel'"/>
						</xsl:call-template>
					</div>
	
					<div class="form-group">
						<label for="description"><xsl:value-of select="$i18n.Description"/></label>
						
						<xsl:call-template name="createTextArea">
							<xsl:with-param name="name" select="'description'"/>
							<xsl:with-param name="id" select="'description'"/>
							<xsl:with-param name="class" select="'form-control'"/>
							<xsl:with-param name="rows" select="'4'"/>
							<xsl:with-param name="maxlength" select="'4000'"/>
							<xsl:with-param name="aria-describedby" select="'descriptionHelp'"/>
						</xsl:call-template>
						
						<div class="small text-muted" id="descriptionHelp"><xsl:value-of select="$i18n.MaxDescriptionChars"/></div>
					</div>

					<div class="form-group">
						<label for="file"><xsl:value-of select="$i18n.LogoOrImage"/></label>
						
						<div class="fileupload-area bigmarginbottom">
							<div>
								<input type="file" class="form-control-file margin-auto" id="file" name="file" aria-describedby="fileHelp"></input>
								
								<div class="small text-muted" id="fileHelp">
									<xsl:value-of select="$i18n.MaximumFileUpload" />
									<xsl:text>:&#160;</xsl:text>
									<xsl:value-of select="MaxAllowedFileSize" />
								</div>
							</div>
						</div>
					</div>

					<div id="accessModes">
						<fieldset class="form-group">
							<legend id="accessModeLabel">
								<xsl:value-of select="$i18n.SecrecyLevel"/> *
							</legend>
							
							<div class="form-check">
								<xsl:call-template name="createRadio">
									<xsl:with-param name="name" select="'accessMode'"/>
									<xsl:with-param name="id" select="'accessMode-open'"/>
									<xsl:with-param name="value" select="'OPEN'"/>
									<xsl:with-param name="element" select="section/Attributes/Attribute[Name = 'accessMode']"/>
									<xsl:with-param name="elementName" select="'Value'"/>
									<xsl:with-param name="class" select="'form-check-input'"/>
									<xsl:with-param name="title" select="$i18n.OpenAccessModeTitle"/>
									<xsl:with-param name="data-validation" select="'#accessModeLabel'"/>
								</xsl:call-template>
								
								<label class="form-check-label" for="accessMode-open" title="{$i18n.OpenAccessModeTitle}">
									<i class="icons icon-eye" aria-hidden="true"/>
									
									<span><xsl:value-of select="$i18n.Open"/></span>
								</label>
							</div>
							
							<div class="form-check">
								<xsl:call-template name="createRadio">
									<xsl:with-param name="name" select="'accessMode'"/>
									<xsl:with-param name="id" select="'accessMode-closed'"/>
									<xsl:with-param name="value" select="'CLOSED'"/>
									<xsl:with-param name="element" select="section/Attributes/Attribute[Name = 'accessMode']"/>
									<xsl:with-param name="elementName" select="'Value'"/>
									<xsl:with-param name="class" select="'form-check-input'"/>
									<xsl:with-param name="title" select="$i18n.ClosedAccessModeTitle"/>
									<xsl:with-param name="data-validation" select="'#accessModeLabel'"/>
								</xsl:call-template>
								
								<label class="form-check-label" for="accessMode-closed" title="{$i18n.ClosedAccessModeTitle}">
									<i class="icons icon-lock" aria-hidden="true"/>
									
									<span><xsl:value-of select="$i18n.Closed"/></span>
								</label>
							</div>
							
							<div class="form-check">
								<xsl:call-template name="createRadio">
									<xsl:with-param name="name" select="'accessMode'"/>
									<xsl:with-param name="id" select="'accessMode-hidden'"/>
									<xsl:with-param name="value" select="'HIDDEN'"/>
									<xsl:with-param name="element" select="section/Attributes/Attribute[Name = 'accessMode']"/>
									<xsl:with-param name="elementName" select="'Value'"/>
									<xsl:with-param name="class" select="'form-check-input'"/>
									<xsl:with-param name="title" select="$i18n.HiddenAccessModeTitle"/>
									<xsl:with-param name="data-validation" select="'#accessModeLabel'"/>
								</xsl:call-template>
								
								<label class="form-check-label" for="accessMode-hidden" title="{$i18n.HiddenAccessModeTitle}">
									<i class="icons icon-eye-slash" aria-hidden="true"/>
									
									<span><xsl:value-of select="$i18n.Hidden"/></span>
								</label>
							</div>
						</fieldset>
						
						<script>
							SectionAccess = {
								<xsl:for-each select="SectionTypes/SectionType">
									"<xsl:value-of select="./sectionTypeID"/>" : {
									
										<xsl:for-each select="./AccessModes/AccessMode">
											"<xsl:value-of select="."/>" : "true",
										</xsl:for-each>
										"dummy" : "false"
									},
								</xsl:for-each>
								"dummy" : "false"
							};
							
							SectionTypeDeleteModes = {
								<xsl:for-each select="SectionTypes/SectionType">
									"<xsl:value-of select="sectionTypeID"/>" : "<xsl:value-of select="deleteDateMode"/>"
								</xsl:for-each>
							};
						</script>
					</div>

					<div class="form-group">
						<xsl:if test="count(SectionTypes/SectionType) = 1">
							<xsl:attribute name="class">form-group hidden</xsl:attribute>
						</xsl:if>
						
						<label for="sectionTypeID"><xsl:value-of select="$i18n.SelectRoomType"/></label>
							
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id" select="'sectionTypeID'"/>
							<xsl:with-param name="name" select="'sectionTypeID'"/>
							<xsl:with-param name="element" select="SectionTypes/SectionType"/>
							<xsl:with-param name="labelElementName" select="'name'"/>
							<xsl:with-param name="valueElementName" select="'sectionTypeID'"/>
							<xsl:with-param name="class" select="'form-control col-md-4'"/>
						</xsl:call-template>
					</div>
					
					<div class="form-group hidden" id="deleteDateWrapper">
						<div class="form-group">
							<label for="sectionDeleteDate">
								<xsl:value-of select="$i18n.RoomDeleteDate"/>
								
								<xsl:if test="count(SectionTypes/SectionType) = 1 and SectionType/deleteDateMode = 'REQUIRED'">
									<xsl:text> *</xsl:text>
								</xsl:if>
							</label>
							
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="'sectionDeleteDate'"/>
								<xsl:with-param name="name" select="'sectionDeleteDate'"/>
								<xsl:with-param name="class" select="'form-control col-md-4'"/>
								<xsl:with-param name="type" select="'date'"/>
								<xsl:with-param name="aria-describedby" select="'deleteDateOptionalText'"/>
								<xsl:with-param name="required">
									<xsl:if test="count(SectionTypes/SectionType) = 1 and SectionType/deleteDateMode = 'REQUIRED'">
										<xsl:text>required</xsl:text>
									</xsl:if>
								</xsl:with-param>
								<xsl:with-param name="data-validation" select="'#sectionDeleteDateLabel'"/>
							</xsl:call-template>
							
							<div class="small text-muted hidden" id="deleteDateOptionalText">
								<xsl:if test="count(SectionTypes/SectionType) > 1 or SectionType/deleteDateMode != 'REQUIRED'">
									<xsl:value-of select="$i18n.Optional"/>
								</xsl:if>
							</div>
						</div>
					</div>
				</div>
				
				<footer class="d-flex">
					<div class="ml-auto">
						<a href="{/Document/requestinfo/contextpath}/" class="btn marginright btn-danger">
							<i class="icons icon-ban" aria-hidden="true"/>
							
							<span><xsl:value-of select="$i18n.Cancel"/></span>
						</a>
						
						<button type="submit" class="btn btn-success">
							<i class="icons icon-plus" aria-hidden="true"/>
							
							<span><xsl:value-of select="$i18n.createRoom"/></span>
						</button>
					</div>
				</footer>
			</form>
		</section>
		
	</xsl:template>
		
	<xsl:template match="NoSectionTypesAvailable">
		
		<!-- TODO -->
		
		<h1>Du har inte behörighet att lägga till samarbetsrum...</h1>
	
	</xsl:template>
	
	<xsl:template match="validationError" mode="sectionfields">
	
		<div class="validation-error" hidden="hidden" data-validation-field="[name={fieldName}]">
			<xsl:choose>
				<xsl:when test="fieldName = 'name'">
					<xsl:choose>
						<xsl:when test="validationErrorType = 'RequiredField'">
							<xsl:value-of select="$i18n.validationError.Required.Section.Name"/>
						</xsl:when>
						<xsl:when test="validationErrorType = 'TooLong'">
							<xsl:value-of select="$i18n.validationError.TooLong.Section.Name"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				
				<xsl:when test="fieldName = 'description'">
					<xsl:choose>
						<xsl:when test="validationErrorType = 'TooLong'">
							<xsl:value-of select="$i18n.validationError.TooLong.Section.Description"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				
				<xsl:when test="fieldName = 'accessMode'">
					<xsl:choose>
						<xsl:when test="validationErrorType = 'InvalidFormat'">
							<xsl:value-of select="$i18n.validationError.InvalidFormat.Section.AccessMode"/>
						</xsl:when>
						<xsl:when test="validationErrorType = 'Required'">
							<xsl:value-of select="$i18n.validationError.Required.Section.AccessMode"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				
				<xsl:when test="fieldName = 'sectionTypeID'">
					<xsl:choose>
						<xsl:when test="validationErrorType = 'Required'">
							<xsl:value-of select="$i18n.validationError.Required.Section.SectionType"/>
						</xsl:when>
						<xsl:when test="validationErrorType = 'InvalidFormat'">
							<xsl:value-of select="$i18n.validationError.InvalidFormat.Section.SectionType"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				
				<xsl:when test="fieldName = 'sectionDeleteDate'">
					<xsl:choose>
						<xsl:when test="validationErrorType = 'Required'">
							<xsl:value-of select="$i18n.validationError.Required.Section.DeleteDate"/>
						</xsl:when>
						<xsl:when test="validationErrorType = 'InvalidFormat'">
							<xsl:value-of select="$i18n.validationError.InvalidFormat.Section.DeleteDate"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError" mode="general">
		
		<xsl:choose>
			<xsl:when test="messageKey = 'FileSizeLimitExceeded'">
				<div class="alert alert-danger" role="alert">
					<i class="icons icon-warning" aria-hidden="true"/>
					
					<span><xsl:value-of select="$i18n.FileSizeLimitExceeded" />!</span>
				</div>
			</xsl:when>
			
			<xsl:when test="messageKey = 'UnableToParseRequest'">
				<div class="alert alert-danger" role="alert">
					<i class="icons icon-warning" aria-hidden="true"/>
					
					<span><xsl:value-of select="$i18n.UnableToParseRequest" />!</span>
				</div>
			</xsl:when>
			
			<xsl:when test="messageKey = 'UnableToParseProfileImage'">
				<div class="alert alert-danger" role="alert">
					<i class="icons icon-warning" aria-hidden="true"/>
					
					<span><xsl:value-of select="$i18n.UnableToParseProfileImage" />!</span>
				</div>
			</xsl:when>
			
			<xsl:when test="messageKey = 'UnableToDeleteProfileImage'">
				<div class="alert alert-danger" role="alert">
					<i class="icons icon-warning" aria-hidden="true"/>
					
					<span><xsl:value-of select="$i18n.UnableToDeleteProfileImage" />!</span>
				</div>
			</xsl:when>
			
			<xsl:when test="messageKey = 'InvalidProfileImageFileFormat'">
				<div class="alert alert-danger" role="alert">
					<i class="icons icon-warning" aria-hidden="true"/>
					
					<span><xsl:value-of select="$i18n.InvalidProfileImageFileFormat" />!</span>
				</div>
			</xsl:when>
			
			<xsl:when test="messageKey = 'InvalidSectionAccess'">
				<div class="alert alert-danger" role="alert">
					<i class="icons icon-warning" aria-hidden="true"/>
					
					<span><xsl:value-of select="$i18n.InvalidSectionAccess" />!</span>
				</div>
			</xsl:when>
			
			<xsl:when test="messageKey = 'InvalidSectionType'">
				<div class="alert alert-danger" role="alert">
					<i class="icons icon-warning" aria-hidden="true"/>
					
					<span><xsl:value-of select="$i18n.InvalidSectionType" />!</span>
				</div>
			</xsl:when>
			
			<xsl:otherwise>
				<div class="alert alert-danger" role="alert">
					<i class="icons icon-warning" aria-hidden="true"/>
					
					<span><xsl:value-of select="$i18n.validationError.unknownMessageKey" />!</span>
				</div>
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	
</xsl:stylesheet>