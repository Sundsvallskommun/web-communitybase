<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="scriptPath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/js</xsl:variable>
	<xsl:variable name="imagePath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js?v=1
	</xsl:variable>
	
	<xsl:variable name="links">
		/utils/js/croppie/croppie.css
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/userprofilemodule.js
		/utils/js/croppie/croppie.js
		/utils/js/croppie/croppie-upload.js
	</xsl:variable>

	<xsl:template match="Document">
		
		<div id="UserProfileModule" class="contentitem">
			<xsl:apply-templates select="ShowUserProfile" />
			<xsl:apply-templates select="UpdateUserProfile" />
		</div>
		
	</xsl:template>
	
	<xsl:template match="ShowUserProfile">
		
		<xsl:variable name="fullName">
			<xsl:value-of select="user/firstname" /><xsl:text>&#160;</xsl:text><xsl:value-of select="user/lastname" />
		</xsl:variable>
		
		<section>
			<article class="clearfix">
				<figure class="floatleft profile xl-margin-right xl">
					<img alt="{$fullName}" src="{/Document/requestinfo/contextpath}{/Document/ProfileImageAlias}/{user/userID}?t={Timestamp}" />
				</figure>
	
				<div>
					<h1><xsl:value-of select="$fullName" /></h1>
					
					<ul class="inline">
						<xsl:if test="user/Attributes/Attribute[Name='title']">
							<li><xsl:value-of select="user/Attributes/Attribute[Name='title']/Value" /></li>
						</xsl:if>
						
						<xsl:if test="user/Attributes/Attribute[Name='organization']">
							<xsl:choose>
								<xsl:when test="UserGroupProviderAlias and user/Attributes/Attribute[Name='organizationID']/Value">
									<li>
										<a href="{/Document/requestinfo/contextpath}{UserGroupProviderAlias}?organizationID={user/Attributes/Attribute[Name='organizationID']/Value}">
											<xsl:value-of select="user/Attributes/Attribute[Name='organization']/Value" />
										</a>
									</li>
								</xsl:when>
								<xsl:otherwise>
									<li><xsl:value-of select="user/Attributes/Attribute[Name='organization']/Value" /></li>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
						
						<xsl:if test="user/Attributes/Attribute[Name='phone']">
							<li><a href="tel:{user/Attributes/Attribute[Name='phone']/Value}"><xsl:value-of select="user/Attributes/Attribute[Name='phone']/Value" /></a></li>
						</xsl:if>
						
						<xsl:if test="user/Attributes/Attribute[Name='phone2']">
							<li><a href="tel:{user/Attributes/Attribute[Name='phone2']/Value}"><xsl:value-of select="user/Attributes/Attribute[Name='phone2']/Value" /></a></li>
						</xsl:if>
						
						<xsl:if test="user/Attributes/Attribute[Name='mobilePhone']">
							<li><a href="tel:{user/Attributes/Attribute[Name='mobilePhone']/Value}"><xsl:value-of select="user/Attributes/Attribute[Name='mobilePhone']/Value" /></a></li>
						</xsl:if>
						
						<xsl:if test="user/email">
							<li><a href="mailto:{user/email}"><xsl:value-of select="user/email" /></a></li>
						</xsl:if>
					</ul>
					
					<xsl:choose>
						<xsl:when test="user/Attributes/Attribute[Name='description']">
							<p>
								<xsl:call-template name="replaceLineBreak">
									<xsl:with-param name="string" select="user/Attributes/Attribute[Name='description']/Value"/>
								</xsl:call-template>
							</p>
						</xsl:when>
						<xsl:when test="not(HideDefaultDescription)">
							<p>
								<xsl:value-of select="user/firstname" />
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$i18n.NoDescription" />.
							</p>
						</xsl:when>
					</xsl:choose>
				</div>
			</article>

			<xsl:variable name="userID" select="user/userID" />
			<xsl:variable name="hasUpdateAccess" select="/Document/user[userID = $userID] and (not(LockUpdateToExternalUsers) or user/Attributes/Attribute[Name = 'isExternal']/Value = 'true')" />
			
			<xsl:if test="$hasUpdateAccess or ResumeSettingsURI">
				<footer class="d-flex">
					<div class="ml-auto">
						<xsl:if test="ResumeSettingsURI">
							<a href="{ResumeSettingsURI}" class="btn btn-success bigmarginright ">
								<i class="icons icon-mail"></i>
								<span><xsl:value-of select="$i18n.UpdateResumeSettings" /></span>
							</a>
						</xsl:if>
						
						<xsl:if test="$hasUpdateAccess">
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/update/{user/userID}" class="btn btn-success">
								<i class="icons icon-edit"></i>
								<span><xsl:value-of select="$i18n.UpdateProfile" /></span>
							</a>
						</xsl:if>
					</div>
				</footer>
			</xsl:if>
		</section>
		
	</xsl:template>
	
	<xsl:template match="UpdateUserProfile">
		
		<xsl:variable name="fullName">
			<xsl:value-of select="user/firstname" /><xsl:text>&#160;</xsl:text><xsl:value-of select="user/lastname" />
		</xsl:variable>
		
		<section>
			<header>
				<h1><xsl:value-of select="$i18n.UpdateProfile" /></h1>
			</header>
			
			<form action="{/Document/requestinfo/uri}" method="post" enctype="multipart/form-data">
				<article>
					<xsl:if test="validationException/validationError">
						<div class="validationerrors hidden">
							<xsl:apply-templates select="validationException/validationError" />
						</div>
					</xsl:if>
					
					<div class="row">
						<div class="col-12 col-md-6">
							<div class="form-group">
								<label for="firstname" class="form-control-label"><xsl:value-of select="$i18n.Firstname"/></label>
								
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="'firstname'" />
									<xsl:with-param name="name" select="'firstname'" />
									<xsl:with-param name="element" select="user" />
									<xsl:with-param name="class" select="'form-control required'"/>
									<xsl:with-param name="disabled">
										<xsl:if test="DisablePersonData">
											<xsl:text>disabled</xsl:text>
										</xsl:if>
									</xsl:with-param>
								</xsl:call-template>
							</div>
						</div>
						<div class="col-12 col-md-6">
							<div class="form-group">
								<label for="lastname" class="form-control-label"><xsl:value-of select="$i18n.Lastname"/></label>
								
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="'lastname'" />
									<xsl:with-param name="name" select="'lastname'" />
									<xsl:with-param name="element" select="user" />
									<xsl:with-param name="class" select="'form-control required'"/>
									<xsl:with-param name="disabled">
										<xsl:if test="DisablePersonData">
											<xsl:text>disabled</xsl:text>
										</xsl:if>
									</xsl:with-param>
								</xsl:call-template>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-12 col-md-6">
							<div class="form-group">
								<label for="email" class="form-control-label"><xsl:value-of select="$i18n.Email"/></label>
								
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="'email'" />
									<xsl:with-param name="name" select="'email'" />
									<xsl:with-param name="element" select="user" />
									<xsl:with-param name="class" select="'form-control'"/>
									<xsl:with-param name="disabled">
										<xsl:if test="DisablePersonData">
											<xsl:text>disabled</xsl:text>
										</xsl:if>
									</xsl:with-param>
								</xsl:call-template>
							</div>
						</div>
						<div class="col-12 col-md-6">
							<div class="form-group">
								<label for="phone" class="form-control-label"><xsl:value-of select="$i18n.Phone"/></label>
								
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="'phone'" />
									<xsl:with-param name="name" select="'phone'" />
									<xsl:with-param name="value" select="user/Attributes/Attribute[Name='phone']/Value" />
									<xsl:with-param name="class">
										<xsl:choose>
											<xsl:when test="PhoneRequired = 'true'">
												<xsl:text>form-control required</xsl:text>
											</xsl:when>
											<xsl:otherwise>
												<xsl:text>form-control</xsl:text>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:with-param>
									<xsl:with-param name="disabled">
										<xsl:if test="DisablePersonData">
											<xsl:text>disabled</xsl:text>
										</xsl:if>
									</xsl:with-param>
								</xsl:call-template>
							</div>
						</div>
						
						<xsl:if test="user/Attributes/Attribute[Name='phone2']/Value">
							<div class="col-12 col-md-6">
								<div class="form-group">
									<label for="phone2" class="form-control-label"><xsl:value-of select="$i18n.Phone"/></label>
									
									<xsl:call-template name="createTextField">
										<xsl:with-param name="id" select="'phone2'" />
										<xsl:with-param name="name" select="'phone2'" />
										<xsl:with-param name="value" select="user/Attributes/Attribute[Name='phone2']/Value" />
										<xsl:with-param name="class" select="'form-control'"/>
										<xsl:with-param name="disabled">
											<xsl:if test="DisablePersonData">
												<xsl:text>disabled</xsl:text>
											</xsl:if>
										</xsl:with-param>
									</xsl:call-template>
								</div>
							</div>
						</xsl:if>
						
						<xsl:if test="PhoneValidationSupported">
							<div class="col-md-6">
								<div class="form-group">
									<label for="mobilePhone"><xsl:value-of select="$i18n.MobilePhone"/> (07xxxxxxxx)</label>
									
									<div class="input-group">
										<xsl:call-template name="createTextField">
											<xsl:with-param name="id" select="'mobilePhone'" />
											<xsl:with-param name="name" select="'mobilePhone'" />
											<xsl:with-param name="value" select="user/Attributes/Attribute[Name='mobilePhone']/Value" />
											<xsl:with-param name="disabled">
												<xsl:if test="not(requestparameters/parameter[name='mobilePhone'])">
													disabled
												</xsl:if>
											</xsl:with-param>
											<xsl:with-param name="class" select="'form-control'"/>
										</xsl:call-template>
										
										<div class="input-group-addon" id="sendcode" data-url="{/Document/requestinfo/currentURI}/{/Document/module/alias}/sendverification" data-invalidnumber="{$i18n.InvalidPhoneNumber}" data-unknown="{$i18n.CouldNotSendSMS}">
											<i class="icons icon-send"/>
											<span class="input-group-text"><xsl:value-of select="$i18n.SendVerificationCode"/></span>
										</div>
									</div>
									
									<small class="text-danger" id="mobilePhoneError"></small>
								</div>
							</div>
							
							<div class="col-md-6">
								<div class="form-group">
									<label for="verificationCode"><xsl:value-of select="$i18n.ValidationCode"/></label>
											
									<xsl:call-template name="createTextField">
										<xsl:with-param name="name" select="'verificationCode'"/>
										<xsl:with-param name="id" select="'verificationCode'"/>
										<xsl:with-param name="disabled">
											<xsl:if test="not(requestparameters/parameter[name='verificationCode'])">
												disabled
											</xsl:if>
										</xsl:with-param>
										<xsl:with-param name="class" select="'form-control'"/>
									</xsl:call-template>
								</div>
							</div>
							
							<div class="col-12">
								<div class="form-group">
									<div class="form-check">
										<label class="form-check-label" for="changeMobilePhone">
											<xsl:call-template name="createCheckbox">
												<xsl:with-param name="name" select="'changeMobilePhone'"/>
												<xsl:with-param name="id" select="'changeMobilePhone'"/>
												<xsl:with-param name="class" select="'form-check-input'"/>
											</xsl:call-template>
											
											<xsl:value-of select="$i18n.ChangeMobilePhone"/>
										</label>
									</div>
								</div>
							</div>
						</xsl:if>
						
						<div class="col-12 col-md-6">
							<div class="form-group">
								<label for="organization" class="form-control-label"><xsl:value-of select="$i18n.Organization"/></label>
								
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="'organization'" />
									<xsl:with-param name="name" select="'organization'" />
									<xsl:with-param name="value" select="user/Attributes/Attribute[Name='organization']/Value" />
									<xsl:with-param name="class" select="'form-control required'"/>
									<xsl:with-param name="disabled">
										<xsl:if test="DisablePersonCompany">
											<xsl:text>disabled</xsl:text>
										</xsl:if>
									</xsl:with-param>
								</xsl:call-template>
							</div>
						</div>						
						
						<div class="col-12 col-md-6">
							<div class="form-group">
								<label for="title" class="form-control-label"><xsl:value-of select="$i18n.Title"/></label>
								
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="'title'" />
									<xsl:with-param name="name" select="'title'" />
									<xsl:with-param name="value" select="user/Attributes/Attribute[Name='title']/Value" />
									<xsl:with-param name="class" select="'form-control'"/>
									<xsl:with-param name="disabled">
										<xsl:if test="DisablePersonCompany">
											<xsl:text>disabled</xsl:text>
										</xsl:if>
									</xsl:with-param>
								</xsl:call-template>
							</div>
						</div>
					</div>
		
					<xsl:if test="user/Attributes/Attribute[Name = 'isExternal']/Value = 'true' or ForceExternalUpdateForm">
						<div class="row">
							<div class="col-12 col-md-6">
								<div class="form-group">
									<label for="password" class="form-control-label"><xsl:value-of select="$i18n.NewPassword"/></label>
									
									<xsl:call-template name="createPasswordField">
										<xsl:with-param name="id" select="'password'" />
										<xsl:with-param name="name" select="'password'" />
										<xsl:with-param name="placeholder" select="$i18n.ChangePasswordText" />
										<xsl:with-param name="class" select="'form-control'"/>
									</xsl:call-template>
								</div>
							</div>
							<div class="col-12 col-md-6">
								<div class="form-group">
									<label for="passwordMatch" class="form-control-label"><xsl:value-of select="$i18n.NewPasswordConfirmation"/></label>
									
									<xsl:call-template name="createPasswordField">
										<xsl:with-param name="id" select="'passwordMatch'" />
										<xsl:with-param name="name" select="'passwordMatch'" />
										<xsl:with-param name="placeholder" select="$i18n.ChangePasswordText" />
										<xsl:with-param name="class" select="'form-control'"/>
									</xsl:call-template>
								</div>
							</div>
						</div>
					</xsl:if>
					
					<div class="row">
						<div class="col-12">
							<div class="form-group">
								<label for="description" class="form-control-label"><xsl:value-of select="$i18n.Description"/></label>
								
								<xsl:call-template name="createTextArea">
									<xsl:with-param name="id" select="'description'" />
									<xsl:with-param name="name" select="'description'" />
									<xsl:with-param name="value" select="user/Attributes/Attribute[Name='description']/Value" />
									<xsl:with-param name="class" select="'form-control'"/>
									<xsl:with-param name="rows" select="null"/>
									<xsl:with-param name="disabled">
										<xsl:if test="DisablePersonData and not(AllowChangingDescription)">
											<xsl:text>true</xsl:text>
										</xsl:if>
									</xsl:with-param>
								</xsl:call-template>
							</div>
						</div>
					</div>
					
					<xsl:variable name="showProfileImage" select="HasProfileImage and not(requestparameters/parameter[name='deleteProfileImage']/value = 'true')" />
					
					<xsl:if test="not(DisablePersonData) or AllowChangingProfileImage">
						<label><xsl:value-of select="$i18n.ProfileImage"/></label>
						
						<div class="fileupload-area bigmarginbottom">
							<div>
								<xsl:if test="$showProfileImage">
									<xsl:attribute name="class">hidden</xsl:attribute>
								</xsl:if>
								
								<div id="crop-wrapper" data-imagesize-x="{ImageSize}" data-imagesize-y="{ImageSize}">
									<div class="crop-result hidden"></div>
									<input type="hidden" name="crop-coordinates" class="crop-coordinates"/>
									<input type="file" class="form-control-file margin-auto" name="file" accept="image/*"/>
								</div>
								
								<script>
									croppieUpload($("#crop-wrapper"));
								</script>
								
								<small class="form-text text-muted">
									<xsl:value-of select="$i18n.MaximumFileUpload" />
									<xsl:text>:&#160;</xsl:text>
									<xsl:value-of select="MaxAllowedFileSize" />
								</small>
								
								<xsl:call-template name="createHiddenField">
									<xsl:with-param name="name" select="'deleteProfileImage'" />
									<xsl:with-param name="value" select="'false'" />
								</xsl:call-template>
							</div>
							
							<xsl:if test="$showProfileImage">
								<div>
									<figure class="group large margin-auto">
										<img alt="{$fullName}" src="{/Document/requestinfo/contextpath}{/Document/ProfileImageAlias}/{user/userID}?t={Timestamp}" />
									</figure>
									
									<a href="#" class="btn btn-danger bigmargintop" id="removeprofileimage">
										<i class="icons icon-delete"></i>
										<span><xsl:value-of select="$i18n.RemoveProfileImage" /></span>
									</a>
								</div>
							</xsl:if>
						</div>
					</xsl:if>
				</article>
		
				<footer class="d-flex">
					<div class="ml-auto">
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/show/{user/userID}" class="btn btn-danger cancel-btn bigmarginright"><i class="icons icon-ban"></i><span><xsl:value-of select="$i18n.Cancel" /></span></a>
						<button type="submit" class="btn btn-success"><i class="icons icon-check"></i><span><xsl:value-of select="$i18n.Save" /></span></button>
					</div>
				</footer>
			</form>
		</section>
	
	</xsl:template>
	
	<xsl:template match="validationError">
		
		<xsl:if test="fieldName and validationErrorType">
			
			<div class="validationerror" data-parameter="{fieldName}">
				<i class="icons icon-warning"></i>
				<span class="italic">
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
				<xsl:when test="messageKey='PasswordDontMatch'">
					<div class="validationerror" data-parameter="passwordMatch">
						<i class="icons icon-warning"></i>
						<span><xsl:value-of select="$i18n.PasswordDontMatch" /></span>
					</div>
				</xsl:when>
				<xsl:when test="messageKey='InvalidPasswordStrength'">
					<div class="validationerror" data-parameter="password">
						<i class="icons icon-warning"></i>
						<span><xsl:value-of select="$i18n.InvalidPasswordStrength" /></span>
					</div>
				</xsl:when>
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
				<xsl:when test="messageKey='EmailAlreadyTaken'">
					<div class="validationerror" data-parameter="file">
						<i class="icons icon-warning"></i>
						<span><xsl:value-of select="$i18n.EmailAlreadyTaken" /></span>
					</div>
				</xsl:when>
				<xsl:when test="messageKey='WrongVerificationCode'">
					<div class="validationerror" data-parameter="verificationCode">
						<i class="icons icon-warning"/>
						<span><xsl:value-of select="$i18n.WrongVerificationCode" /></span>
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