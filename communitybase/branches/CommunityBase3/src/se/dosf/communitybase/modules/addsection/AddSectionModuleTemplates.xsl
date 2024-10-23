<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="scriptPath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/js</xsl:variable>
	<xsl:variable name="imagePath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js?v=1
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
			
			<form id="create_room" method="post" action="{/Document/requestinfo/uri}" enctype="multipart/form-data">
				<article>
					<xsl:if test="validationException/validationError">		
						<div class="validationerrors hidden">
							<xsl:apply-templates select="validationException/validationError[not(messageKey='InvalidSectionAccess') and not(fieldName='accessMode')]" />
						</div>
					</xsl:if>
					
					<div class="form-group">
						<label for="name"><xsl:value-of select="$i18n.NamePlaceholder"/></label>
						
						<xsl:call-template name="createTextField">
							<xsl:with-param name="name" select="'name'"/>
							<xsl:with-param name="id" select="'name'"/>
							<xsl:with-param name="class" select="'form-control required'"/>
						</xsl:call-template>
					</div>
	
					<div class="form-group">
						<label for="description"><xsl:value-of select="$i18n.Description"/></label>
						
						<xsl:call-template name="createTextArea">
							<xsl:with-param name="name" select="'description'"/>
							<xsl:with-param name="id" select="'description'"/>
							<xsl:with-param name="class" select="'form-control required'"/>
							<xsl:with-param name="rows" select="8"/>
						</xsl:call-template>
						
						<small class="form-text text-muted"><xsl:value-of select="$i18n.MaxDescriptionChars"/></small>
					</div>

					<div class="form-group">
						<label for="file"><xsl:value-of select="$i18n.LogoOrImage"/></label>
						
						<div class="fileupload-area bigmarginbottom">
							<div>
								<input type="file" class="form-control-file margin-auto" name="file"></input>
								
								<small class="form-text text-muted">
									<xsl:value-of select="$i18n.MaximumFileUpload" />
									<xsl:text>:&#160;</xsl:text>
									<xsl:value-of select="MaxAllowedFileSize" />
								</small>
							</div>
						</div>
					</div>

					<div class="form-group" id="accessModes">
						<label><xsl:value-of select="$i18n.Secrecy"/></label>
						
						<xsl:if test="validationException/validationError[messageKey='InvalidSectionAccess' or fieldName='accessMode']">		
							<div class="full error">
								<xsl:apply-templates select="validationException/validationError[messageKey='InvalidSectionAccess' or fieldName='accessMode']" />
							</div>
						</xsl:if>
						
						<label class="radio bigmargintop">
							<xsl:call-template name="createRadio">
								<xsl:with-param name="name" select="'accessMode'"/>
								<xsl:with-param name="value" select="'OPEN'"/>
								<xsl:with-param name="checked" select="'true'"/>
							</xsl:call-template>
							
							<em/>
							
							<i class="icons icon-eye"></i>
							<span><xsl:value-of select="$i18n.Open"/></span>
						</label>
						
						<label class="radio">
							<xsl:call-template name="createRadio">
								<xsl:with-param name="name" select="'accessMode'"/>
								<xsl:with-param name="value" select="'CLOSED'"/>
							</xsl:call-template>
							
							<em/>
							
							<i class="icons icon-lock"></i>
							<span><xsl:value-of select="$i18n.Closed"/></span>
						</label>
						
						<label class="radio">
							<xsl:call-template name="createRadio">
								<xsl:with-param name="name" select="'accessMode'"/>
								<xsl:with-param name="value" select="'HIDDEN'"/>
							</xsl:call-template>
							
							<em/>
							
							<i class="icons icon-eye-slash"></i>
							<span><xsl:value-of select="$i18n.Hidden"/></span>
						</label>
						
						<script type="text/javascript">
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
						<label for="sectionDeleteDate"><xsl:value-of select="$i18n.RoomDeleteDate"/></label>
						
						<div class="input-group date" data-provide="datepicker">
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="'sectionDeleteDate'"/>
								<xsl:with-param name="name" select="'sectionDeleteDate'"/>
								<xsl:with-param name="class" select="'form-control col-md-2'"/>
							</xsl:call-template>
							
							<span class="input-group-addon pointer"><i class="icons icon-calendar"></i></span>
						</div>
						
						<small class="form-text text-muted hidden" id="deleteDateOptionalText"><xsl:value-of select="$i18n.Optional"/></small>
					</div>
				</article>
				
				<footer class="clearfix">
					<button type="submit" class="btn float-right marginright btn-success">
						<i class="icons icon-plus"></i>
						<span><xsl:value-of select="$i18n.createRoom"/></span>
					</button>
					
					<a href="{/Document/requestinfo/contextpath}/" class="btn float-right marginright btn-danger">
						<i class="icons icon-ban"></i>
						<span><xsl:value-of select="$i18n.Cancel"/></span>
					</a>
				</footer>
			</form>
		</section>
		
	</xsl:template>
		
	<xsl:template match="NoSectionTypesAvailable">
		
		<!-- TODO -->
		
		<h1>Du har inte behörighet att lägga till samarbetsrum...</h1>
	
	</xsl:template>
	
	<xsl:template match="validationError">
		
		<xsl:if test="fieldName and validationErrorType">
			<div data-parameter="{fieldName}" class="validationerror">
				<i class="icons icon-warning"></i>
				<span>
					<xsl:choose>
						<xsl:when test="validationErrorType='RequiredField'">
							<xsl:value-of select="$i18n.validationError.RequiredField" />
						</xsl:when>
						<xsl:when test="validationErrorType='InvalidFormat'">
							<xsl:value-of select="$i18n.validationError.InvalidFormat" />
						</xsl:when>
						<xsl:when test="validationErrorType='TooShort'">
							<xsl:value-of select="$i18n.validationError.TooShort" />
						</xsl:when>
						<xsl:when test="validationErrorType='TooLong'">
							<xsl:value-of select="$i18n.validationError.TooLong" />
						</xsl:when>
						<xsl:when test="validationErrorType='Other'">
							<xsl:value-of select="$i18n.validationError.Other" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$i18n.validationError.unknownValidationErrorType" />
						</xsl:otherwise>
					</xsl:choose>
				</span>
			</div>
		</xsl:if>
		
		<xsl:if test="messageKey">
			<xsl:choose>
				<xsl:when test="messageKey='FileSizeLimitExceeded'">
					<div class="validationerror" data-parameter="file">
						<i class="icons icon-warning"></i>
						<span><xsl:value-of select="$i18n.FileSizeLimitExceeded" /></span>
					</div>
				</xsl:when>
				<xsl:when test="messageKey='UnableToParseRequest'">
					<div class="validationerror" data-parameter="file">
						<i class="icons icon-warning"></i>
						<span><xsl:value-of select="$i18n.UnableToParseRequest" /></span>
					</div>
				</xsl:when>
				<xsl:when test="messageKey='UnableToParseProfileImage'">
					<div class="validationerror" data-parameter="file">
						<i class="icons icon-warning"></i>
						<span><xsl:value-of select="$i18n.UnableToParseProfileImage" /></span>
					</div>
				</xsl:when>
				<xsl:when test="messageKey='UnableToDeleteProfileImage'">
					<div class="validationerror" data-parameter="file">
						<i class="icons icon-warning"></i>
						<span><xsl:value-of select="$i18n.UnableToDeleteProfileImage" /></span>
					</div>
				</xsl:when>
				<xsl:when test="messageKey='InvalidProfileImageFileFormat'">
					<div class="validationerror" data-parameter="file">
						<i class="icons icon-warning"></i>
						<span><xsl:value-of select="$i18n.InvalidProfileImageFileFormat" /></span>
					</div>
				</xsl:when>
				<xsl:when test="messageKey='InvalidSectionAccess'">
					<div class="validationerror">
						<i class="icons icon-warning"></i>
						<span><xsl:value-of select="$i18n.InvalidSectionAccess" /></span>
					</div>
				</xsl:when>
				<xsl:when test="messageKey='InvalidSectionType'">
					<div class="validationerror" data-parameter="sectionTypeID">
						<i class="icons icon-warning"></i>
						<span><xsl:value-of select="$i18n.InvalidSectionType" /></span>
					</div>
				</xsl:when>
				<xsl:otherwise>
					<div class="validationerror">
						<i class="icons icon-warning"></i>
						<span><xsl:value-of select="$i18n.validationError.unknownMessageKey" />!</span>
					</div>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
		
	</xsl:template>
	
</xsl:stylesheet>