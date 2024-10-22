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
		/js/userprofilemodule.js
	</xsl:variable>

	<xsl:template match="Document">
		
		<div id="UserProfileModule" class="contentitem of-module of-block">
			
			<xsl:apply-templates select="ShowUserProfile" />
			<xsl:apply-templates select="UpdateUserProfile" />
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="ShowUserProfile">
		
		<xsl:variable name="fullName">
			<xsl:value-of select="user/firstname" /><xsl:text>&#160;</xsl:text><xsl:value-of select="user/lastname" />
		</xsl:variable>
		
		<xsl:variable name="userID" select="user/userID" />
		<xsl:variable name="hasUpdateAccess" select="/Document/user[userID = $userID] and user/Attributes/Attribute[Name = 'isExternal']/Value = 'true'" />
		<xsl:variable name="isMe" select="/Document/user[userID = $userID]" />
		
		<div class="of-block">
			<div class="of-inner-padded of-clear">
				<figure class="of-profile of-figure-xxl of-left">
					<img alt="{$fullName}" src="{/Document/requestinfo/contextpath}{/Document/ProfileImageAlias}/{user/userID}" />
				</figure>

				<header class="of-overflow">
					
<!-- 					<xsl:if test="ResumeSettingsURI">
					
						<div class="of-right of-hide-to-md">
							<a href="{ResumeSettingsURI}" class="of-btn of-btn-inline of-btn-outline of-btn-xs of-icon">
								<i><svg viewBox="0 0 512 512" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns="http://www.w3.org/2000/svg"><use xlink:href="#mail"/></svg></i>
								<span><xsl:value-of select="$i18n.UpdateResumeSettings" /></span>
							</a>
						</div>						
					
					</xsl:if>
					
					<xsl:if test="$hasUpdateAccess">
						<div class="of-right of-hide-to-md clearboth">
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/update/{user/userID}" class="of-btn of-btn-inline of-btn-outline of-btn-xs of-icon">
								<i><svg viewBox="0 0 512 512" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns="http://www.w3.org/2000/svg"><use xlink:href="#write"/></svg></i>
								<span><xsl:value-of select="$i18n.UpdateProfile" /></span>
							</a>
						</div>
					</xsl:if> -->
					
					<h1><xsl:value-of select="$fullName" /></h1>
					<ul class="of-meta-line">
						<xsl:if test="user/Attributes/Attribute[Name='title']">
							<li><xsl:value-of select="user/Attributes/Attribute[Name='title']/Value" /></li>
						</xsl:if>
						<xsl:if test="user/Attributes/Attribute[Name='organization']">
							<li><xsl:value-of select="user/Attributes/Attribute[Name='organization']/Value" /></li>
						</xsl:if>
						<xsl:if test="user/Attributes/Attribute[Name='phone']">
							<li><a href="callto:{user/Attributes/Attribute[Name='phone']/Value}"><xsl:value-of select="user/Attributes/Attribute[Name='phone']/Value" /></a></li>
						</xsl:if>
						<li><a href="mailto:{user/email}"><xsl:value-of select="user/email" /></a></li>
					</ul>
				</header>

				<div class="of-inner-padded-t-half">
					<xsl:choose>
						<xsl:when test="user/Attributes/Attribute[Name='description']">
							<p>
								<xsl:call-template name="replaceLineBreak">
									<xsl:with-param name="string" select="user/Attributes/Attribute[Name='description']/Value"/>
								</xsl:call-template>
							</p>
						</xsl:when>
						<xsl:otherwise>
							<p><xsl:value-of select="user/firstname" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$i18n.NoDescription" />.</p>
						</xsl:otherwise>
					</xsl:choose>
				</div>
			</div>

			<xsl:if test="$hasUpdateAccess or ResumeSettingsURI">
			
				<div class="of-inner-padded-rbl of-text-right">
				
					<xsl:if test="$hasUpdateAccess">
					
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/update/{user/userID}" class="of-btn of-btn-inline of-btn-outline of-btn-xs of-icon">
							<i><svg viewBox="0 0 512 512" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns="http://www.w3.org/2000/svg"><use xlink:href="#write"/></svg></i>
							<span><xsl:value-of select="$i18n.UpdateProfile" /></span>
						</a>
											
						<br/><br/>
					
					</xsl:if>
					
					<xsl:if test="ResumeSettingsURI">
					
						<a href="{ResumeSettingsURI}" class="of-btn of-btn-inline of-btn-outline of-btn-xs of-icon">
							<i><svg viewBox="0 0 512 512" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns="http://www.w3.org/2000/svg"><use xlink:href="#mail"/></svg></i>
							<span><xsl:value-of select="$i18n.UpdateResumeSettings" /></span>
						</a>
						
					</xsl:if>
				</div>
			
			</xsl:if>
			
			<xsl:if test="$isMe and not($hasUpdateAccess) and InternalUserInfoText">
				
				<div class="of-inner-padded-rbl">
				
					<span><xsl:value-of select="InternalUserInfoText" disable-output-escaping="yes"/></span>
					
				</div>
				
				</xsl:if>

		</div>
		
	</xsl:template>
	
	<xsl:template match="UpdateUserProfile">
		
		<xsl:variable name="fullName">
			<xsl:value-of select="user/firstname" /><xsl:text>&#160;</xsl:text><xsl:value-of select="user/lastname" />
		</xsl:variable>
		
		<header class="of-inner-padded-trl">
			<h1><xsl:value-of select="$i18n.UpdateProfile" /></h1>
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
								<xsl:with-param name="element" select="user" />
							</xsl:call-template>
						</label>
					</div>
	
					<div class="of-omega-sm of-omega-md-extend of-c-lg-4 of-c-xxxl-6 of-omega-xxxl">
						<label data-of-required="" class="of-block-label">
							<span><xsl:value-of select="$i18n.Lastname" /></span>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="'lastname'" />
								<xsl:with-param name="name" select="'lastname'" />
								<xsl:with-param name="element" select="user" />
							</xsl:call-template>
						</label>
					</div>
				</div>
	
				<div class="of-clear">
					<div class="of-omega-sm of-omega-md of-c-lg-4 of-c-xxxl-6">
						<label class="of-block-label of-icon">
							<span><xsl:value-of select="$i18n.Email" /></span>
							<xsl:call-template name="createTextField">
								<xsl:with-param name="id" select="'email'" />
								<xsl:with-param name="name" select="'email'" />
								<xsl:with-param name="element" select="user" />
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
								<xsl:with-param name="value" select="user/Attributes/Attribute[Name='phone']/Value" />
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
								<xsl:with-param name="value" select="user/Attributes/Attribute[Name='organization']/Value" />
							</xsl:call-template>
						</label>
					</div>
				</div>
				
				<div class="of-clear">
					<div class="of-omega-sm of-omega-md of-c-lg-4 of-c-xxxl-6">
						<label data-of-required="match_password" data-of-optional="" class="of-block-label of-icon">
							<span><xsl:value-of select="$i18n.NewPassword" /></span>
							<xsl:call-template name="createPasswordField">
								<xsl:with-param name="id" select="'password'" />
								<xsl:with-param name="name" select="'password'" />
								<xsl:with-param name="placeholder" select="$i18n.ChangePasswordText" />
							</xsl:call-template>
							<i><svg viewBox="0 0 512 512" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns="http://www.w3.org/2000/svg"><use xlink:href="#lock"/></svg></i>
						</label>
					</div>
	
					<div class="of-omega-sm of-omega-md-extend of-c-lg-4 of-c-xxxl-6 of-omega-xxxl">
						<label data-of-required="match_password" data-of-optional="" class="of-block-label">
							<span><xsl:value-of select="$i18n.NewPasswordConfirmation" /></span>
							<xsl:call-template name="createPasswordField">
								<xsl:with-param name="id" select="'passwordMatch'" />
								<xsl:with-param name="name" select="'passwordMatch'" />
								<xsl:with-param name="placeholder" select="$i18n.ChangePasswordText" />
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
							<xsl:with-param name="value" select="user/Attributes/Attribute[Name='description']/Value" />
						</xsl:call-template>
					</label>
				</div>
	
				<div class="of-c-xs-2 of-c-sm-4 of-c-md-4 of-c-lg-8 of-omega">
					<label class="of-block-label">
						<span data-of-description="{$i18n.Optional}"><xsl:value-of select="$i18n.ProfileImage" /></span>
					</label>
					
					<xsl:variable name="showProfileImage" select="HasProfileImage and not(requestparameters/parameter[name='deleteProfileImage']/value = 'true')" />
					
					<div class="of-fileupload of-margin-top">
						<xsl:if test="$showProfileImage">
							<xsl:attribute name="class">of-fileupload of-margin-top of-file-uploaded</xsl:attribute>
						</xsl:if>
						<div class="of-upload">
							<!-- <h2><xsl:value-of select="$i18n.DropFileHere" /></h2> -->
							<input type="file" name="file"></input>
							<span class="of-filesize-info"><xsl:value-of select="$i18n.MaximumFileUpload" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="MaxAllowedFileSize" /></span>
						</div>
						<div class="of-uploaded">
							<xsl:if test="$showProfileImage">
								<figure>
									<img alt="{$fullName}" src="{/Document/requestinfo/contextpath}{/Document/ProfileImageAlias}/{user/userID}" />
								</figure>
								<a href="#" class="of-btn of-btn-vattjom of-btn-sm of-btn-inline"><xsl:value-of select="$i18n.RemoveProfileImage" /></a>
								<xsl:call-template name="createHiddenField">
									<xsl:with-param name="name" select="'deleteProfileImage'" />
									<xsl:with-param name="value" select="'false'" />
								</xsl:call-template>
							</xsl:if>
						</div>
					</div>
				</div>
	
			</article>
	
			<footer class="of-no-bg of-text-right of-inner-padded">
				<button class="of-btn of-btn-inline of-btn-gronsta" type="submit"><xsl:value-of select="$i18n.Save" /></button>
				<span class="of-btn-link">eller <a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/show/{user/userID}"><xsl:value-of select="$i18n.Cancel" /></a></span>
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