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
		/js/invitationmodule.js
	</xsl:variable>

	<xsl:template match="Document">
		
		<div id="InvitationModule" class="contentitem of-module">
			
			<xsl:apply-templates select="Register" />
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="Register">
		
		<header class="of-inner-padded-rl of-inner-padded-t-half of-welcome of-text-center">
			<xsl:value-of select="RegistrationText" disable-output-escaping="yes" />
		</header>
		
		<form action="{/Document/requestinfo/uri}" method="post" class="of-form" enctype="multipart/form-data">
			
			<xsl:if test="validationException/validationError">		
				<div class="validationerrors of-hidden">
					<xsl:apply-templates select="validationException/validationError" />
				</div>
			</xsl:if>
			
			<article class="of-inner-padded">
	
				<div class="of-clear">
					<div class="of-omega-sm of-omega-md of-c-lg-4 of-c-xxxl-6">
						<label data-of-required="" class="of-block-label">
							<span><xsl:value-of select="$i18n.Firstname" /></span>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="'firstname'" />
								<xsl:with-param name="name" select="'firstname'" />
							</xsl:call-template>
						</label>
					</div>
	
					<div class="of-omega-sm of-omega-md-extend of-c-lg-4 of-c-xxxl-6 of-omega-xxxl">
						<label data-of-required="" class="of-block-label">
							<span><xsl:value-of select="$i18n.Lastname" /></span>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="'lastname'" />
								<xsl:with-param name="name" select="'lastname'" />
							</xsl:call-template>
						</label>
					</div>
				</div>
	
				<div class="of-clear">
					<div class="of-omega-sm of-omega-md of-c-lg-4 of-c-xxxl-6">
						<label class="of-block-label of-input-disabled of-icon">
							<span><xsl:value-of select="$i18n.Email" /></span>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="'email'" />
								<xsl:with-param name="name" select="'email'" />
								<xsl:with-param name="value" select="Invitation/email" />
								<xsl:with-param name="requestparameters" select="null" />
								<xsl:with-param name="disabled" select="'disabled'" />
							</xsl:call-template>
							<i><svg viewBox="0 0 512 512" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns="http://www.w3.org/2000/svg"><use xlink:href="#lock"/></svg></i>
						</label>
					</div>
	
					<div class="of-omega-sm of-omega-md-extend of-c-lg-4 of-c-xxxl-6 of-omega-xxxl">
						<label data-of-required="" class="of-block-label">
							<span><xsl:value-of select="$i18n.Phone" /></span>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="'phone'" />
								<xsl:with-param name="name" select="'phone'" />
							</xsl:call-template>
						</label>
					</div>
				</div>
	
				<div class="of-clear">
					<div class="of-omega-sm of-omega-md of-c-lg-4 of-c-xxxl-6">
						<label data-of-required="" class="of-block-label">
							<span><xsl:value-of select="$i18n.Organization" /></span>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="'organization'" />
								<xsl:with-param name="name" select="'organization'" />
							</xsl:call-template>
						</label>
					</div>
				</div>
	
				<div class="of-c-xs-2 of-c-sm-4 of-c-md-4 of-c-lg-8 of-omega">
					<label class="of-block-label">
						<span data-of-description="{$i18n.Optional}"><xsl:value-of select="$i18n.Description" /></span>
						<div class="of-auto-resize-clone" style=""><br class="lbr" /></div>
						<xsl:call-template name="createTextArea">
							<xsl:with-param name="id" select="'description'" />
							<xsl:with-param name="name" select="'description'" />
							<xsl:with-param name="class" select="'of-auto-resize'" />
						</xsl:call-template>
					</label>
				</div>
	
				<div class="of-c-xs-2 of-c-sm-4 of-c-md-4 of-c-lg-8 of-omega">
					<label class="of-block-label">
						<span data-of-description="{$i18n.Optional}"><xsl:value-of select="$i18n.ProfileImage" /></span>
					</label>
					<div class="of-fileupload of-margin-top">
						<div class="of-upload">
							<!-- <h2><xsl:value-of select="$i18n.DropFileHere" /></h2> -->
							<input type="file" name="file"></input>
							<span class="of-filesize-info"><xsl:value-of select="$i18n.MaximumFileUpload" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="MaxAllowedFileSize" /></span>
						</div>
						<div class="of-uploaded">
							<figure></figure>
							<a href="#" class="of-btn of-btn-vattjom of-btn-sm of-btn-inline">Ta bort</a>
						</div>
					</div>
				</div>
	
				<div class="of-clear">
	
					<div class="of-omega-sm of-omega-md of-c-lg-4 of-c-xxxl-6">
						<label data-of-required="match_password" class="of-block-label">
							<span><xsl:value-of select="$i18n.ChoosePassword" /></span>
							<xsl:call-template name="createPasswordField">
								<xsl:with-param name="id" select="'password'" />
								<xsl:with-param name="name" select="'password'" />
							</xsl:call-template>
							<span class="description">(<xsl:value-of select="$i18n.PasswordStrengthDescription" />)</span>
						</label>
					</div>
	
					<div class="of-omega-sm of-omega-md-extend of-c-lg-4 of-c-xxxl-6 of-omega-xxxl">
						<label data-of-required="match_password" class="of-block-label">
							<span><xsl:value-of select="$i18n.ConfirmPassword" /></span>
							<xsl:call-template name="createPasswordField">
								<xsl:with-param name="id" select="'passwordMatch'" />
								<xsl:with-param name="name" select="'passwordMatch'" />
							</xsl:call-template>
						</label>
					</div>
				</div>
	
			</article>
	
			<footer class="of-no-bg of-inner-padded of-clear">
				<div class="of-right-from-sm">
					<button class="of-btn of-btn-block of-btn-gronsta" type="submit"><xsl:value-of select="$i18n.Save" /></button>
				</div>
			</footer>

		</form>
		
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
				<xsl:when test="messageKey='PasswordDontMatch'">
					<span class="validationerror" data-parameter="passwordMatch">
						<span class="description error" >
							<xsl:value-of select="$i18n.PasswordDontMatch" />
						</span>
					</span>
				</xsl:when>
				<xsl:when test="messageKey='InvalidPasswordStrength'">
					<span class="validationerror" data-parameter="password">
						<span class="description error" >
							<xsl:value-of select="$i18n.InvalidPasswordStrength" />
						</span>
					</span>
				</xsl:when>
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