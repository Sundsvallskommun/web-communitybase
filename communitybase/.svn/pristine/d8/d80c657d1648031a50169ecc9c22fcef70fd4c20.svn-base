<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.xsl"/>

	<xsl:variable name="scriptPath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/js</xsl:variable>
	<xsl:variable name="imagePath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/addsectionmodule.js
	</xsl:variable>

	<xsl:template match="Document">
		
		<div id="AddSectionModule" class="contentitem of-module">
			
			<xsl:apply-templates select="AddSection" />
			<xsl:apply-templates select="NoSectionTypesAvailable" />
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="AddSection">
		
		<div class="of-block">
			<form id="create_room" method="post" action="{/Document/requestinfo/uri}" enctype="multipart/form-data">

				<header class="of-inner-padded-trl">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="name" select="'name'"/>
						<xsl:with-param name="placeholder" select="$i18n.NamePlaceholder"/>
						<xsl:with-param name="class" select="'of-inline-input'"/>
					</xsl:call-template>
				</header>

				<xsl:if test="validationException/validationError">		
					<div class="validationerrors of-hidden">
						<xsl:apply-templates select="validationException/validationError" />
					</div>
				</xsl:if>

				<article class="of-inner-padded">

					<div>
						<label class="of-block-label">
							<span>
								<xsl:value-of select="$i18n.Description"/>
							</span>
							<xsl:call-template name="createTextArea">
								<xsl:with-param name="name" select="'description'"/>
								<xsl:with-param name="rows" select="8"/>
							</xsl:call-template>
							<span class="description"><xsl:value-of select="$i18n.MaxDescriptionChars"/></span>
						</label>
					</div>

					<div>
						<label class="of-block-label">
							<span data-of-description="{$i18n.Optional}">
								<xsl:value-of select="$i18n.LogoOrImage"/>
							</span>
						</label>
						<div class="of-fileupload of-margin-top">
							<div class="of-upload">
								<!-- <h2><xsl:value-of select="$i18n.DropFileHere" /></h2> -->
								<input type="file" name="file"></input>
								<span class="of-filesize-info"><xsl:value-of select="$i18n.MaximumFileUpload" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="MaxAllowedFileSize" /></span>
							</div>
						</div>
					</div>

					<div class="of-c-md-3 of-c-xxl-6">

						<label class="of-block-label">
							<span><xsl:value-of select="$i18n.Secrecy"/></span>
						</label>
						
						<label class="of-inline-block-label">
							
							<xsl:call-template name="createRadio">
								<xsl:with-param name="name" select="'accessMode'"/>
								<xsl:with-param name="value" select="'OPEN'"/>
								<xsl:with-param name="checked" select="'true'"/>
							</xsl:call-template>
							
							<em class="of-radio"/>
							<span class="of-icon">
								<i>
									<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 512 512">
										<use xlink:href="#eye"/>
									</svg>
								</i>
								<span><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.Open"/></span>
							</span>
						</label>
						
						<label class="of-inline-block-label">
							
							<xsl:call-template name="createRadio">
								<xsl:with-param name="name" select="'accessMode'"/>
								<xsl:with-param name="value" select="'CLOSED'"/>
							</xsl:call-template>							
							
							<em class="of-radio"/>
							<span class="of-icon">
								<i>
									<svg xmlns="http://www.w3.org/2000/svg"	xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 512 512">
										<use xlink:href="#lock"/>
									</svg>
								</i>
								<span><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.Closed"/></span>
							</span>
						</label>
						
						<label class="of-inline-block-label">
							
							<xsl:call-template name="createRadio">
								<xsl:with-param name="name" select="'accessMode'"/>
								<xsl:with-param name="value" select="'HIDDEN'"/>
							</xsl:call-template>								
							
							<em class="of-radio"/>
							<span class="of-icon">
								<i>
									<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 512 512">
										<use xlink:href="#hidden"/>
									</svg>
								</i>
								<span><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.Hidden"/></span>
							</span>
						</label>

					</div>

					<div class="of-c-md-1 of-c-xxl-4 of-omega">
						
						<xsl:if test="count(SectionTypes/SectionType) = 1">
							<xsl:attribute name="class">of-c-md-1 of-c-xxl-4 of-omega of-hidden</xsl:attribute>
						</xsl:if>
						<label class="of-block-label">
							<span><xsl:value-of select="$i18n.SelectRoomType"/></span>
							
							<xsl:call-template name="createOFDropdown">
								<xsl:with-param name="name" select="'sectionTypeID'"/>
								<xsl:with-param name="element" select="SectionTypes/SectionType"/>
								<xsl:with-param name="labelElementName" select="'name'"/>
								<xsl:with-param name="valueElementName" select="'sectionTypeID'"/>
								<xsl:with-param name="class" select="'of-margin-top'"/>
							</xsl:call-template>

						</label>
						
					</div>

				</article>

				<footer class="of-text-right of-inner-padded of-no-bg">
					<input type="submit" class="of-btn of-btn-inline of-btn-gronsta" value="{$i18n.createRoom}"/>
					<span class="of-btn-link">
						<xsl:value-of select="$i18n.or"/>
						<xsl:text>&#160;</xsl:text>
						<a href="{/Document/requestinfo/contextpath}">
							<xsl:value-of select="$i18n.Cancel"/>
						</a></span>
				</footer>
			</form>
		</div>
		
	</xsl:template>
		
	<xsl:template match="NoSectionTypesAvailable">
		
		<!-- TODO -->
		
		<h1>Du har inte behörighet att lägga till samarbetsrum...</h1>
	
	</xsl:template>
	
	<xsl:template match="validationError">
		
		<xsl:if test="fieldName and validationErrorType">
			
			<span class="validationerror" data-parameter="{fieldName}">
				<span class="description error" >
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
				<i><svg viewBox="0 0 512 512" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns="http://www.w3.org/2000/svg"><use xlink:href="#error"/></svg></i>
			</span>
		</xsl:if>
		<xsl:if test="messageKey">
			<xsl:choose>
				<xsl:when test="messageKey='FileSizeLimitExceeded'">
					<span class="validationerror" data-parameter="file">
						<span class="description error" >
							<xsl:value-of select="$i18n.FileSizeLimitExceeded" />
						</span>
					</span>
				</xsl:when>
				<xsl:when test="messageKey='UnableToParseRequest'">
					<span class="validationerror" data-parameter="file">
						<span class="description error" >
							<xsl:value-of select="$i18n.UnableToParseRequest" />
						</span>
					</span>
				</xsl:when>
				<xsl:when test="messageKey='UnableToParseProfileImage'">
					<span class="validationerror" data-parameter="file">
						<span class="description error" >
							<xsl:value-of select="$i18n.UnableToParseProfileImage" />
						</span>
					</span>
				</xsl:when>
				<xsl:when test="messageKey='UnableToDeleteProfileImage'">
					<span class="validationerror" data-parameter="file">
						<span class="description error" >
							<xsl:value-of select="$i18n.UnableToDeleteProfileImage" />
						</span>
					</span>
				</xsl:when>
				<xsl:when test="messageKey='InvalidProfileImageFileFormat'">
					<span class="validationerror" data-parameter="file">
						<span class="description error" >
							<xsl:value-of select="$i18n.InvalidProfileImageFileFormat" />
						</span>
					</span>
				</xsl:when>
				<xsl:otherwise>
					<p class="error"><xsl:value-of select="$i18n.validationError.unknownMessageKey" />!</p>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
		
	</xsl:template>
	
</xsl:stylesheet>