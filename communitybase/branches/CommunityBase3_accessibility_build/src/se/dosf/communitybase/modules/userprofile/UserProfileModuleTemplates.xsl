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
			<div class="section-content d-flex">
				<div class="word-wrap-break">
					<h1><xsl:value-of select="$fullName" /></h1>
					
					<div class="inline-box mb-2">
						<xsl:if test="user/Attributes/Attribute[Name='title']">
							<div><xsl:value-of select="user/Attributes/Attribute[Name='title']/Value" /></div>
						</xsl:if>
						
						<xsl:if test="user/Attributes/Attribute[Name='organization']">
							<xsl:choose>
								<xsl:when test="UserGroupProviderAlias and user/Attributes/Attribute[Name='organizationID']/Value">
									<div>
										<a href="{/Document/requestinfo/contextpath}{UserGroupProviderAlias}?organizationID={user/Attributes/Attribute[Name='organizationID']/Value}">
											<xsl:value-of select="user/Attributes/Attribute[Name='organization']/Value" />
										</a>
									</div>
								</xsl:when>
								<xsl:otherwise>
									<div><xsl:value-of select="user/Attributes/Attribute[Name='organization']/Value" /></div>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
						
						<xsl:if test="user/Attributes/Attribute[Name='phone']">
							<div><a href="tel:{user/Attributes/Attribute[Name='phone']/Value}"><xsl:value-of select="user/Attributes/Attribute[Name='phone']/Value" /></a></div>
						</xsl:if>
						
						<xsl:if test="user/Attributes/Attribute[Name='phone2']">
							<div><a href="tel:{user/Attributes/Attribute[Name='phone2']/Value}"><xsl:value-of select="user/Attributes/Attribute[Name='phone2']/Value" /></a></div>
						</xsl:if>
						
						<xsl:if test="user/Attributes/Attribute[Name='mobilePhone']">
							<div><a href="tel:{user/Attributes/Attribute[Name='mobilePhone']/Value}"><xsl:value-of select="user/Attributes/Attribute[Name='mobilePhone']/Value" /></a></div>
						</xsl:if>
						
						<xsl:if test="user/email">
							<div><a href="mailto:{user/email}"><xsl:value-of select="user/email" /></a></div>
						</xsl:if>
					</div>
					
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
				
				<figure class="profile xl ml-auto">
					<img id="fullProfileImage" alt="{$fullName}" src="{/Document/requestinfo/contextpath}{/Document/ProfileImageAlias}/{user/userID}?t={Timestamp}" />
				</figure>
			</div>

			<xsl:variable name="userID" select="user/userID" />
			<xsl:variable name="hasUpdateAccess" select="/Document/user[userID = $userID] and (not(LockUpdateToExternalUsers) or user/Attributes/Attribute[Name = 'isExternal']/Value = 'true')" />
			
			<xsl:if test="$hasUpdateAccess or ResumeSettingsURI">
				<footer class="d-flex">
					<div class="ml-auto">
						<xsl:if test="ResumeSettingsURI">
							<a href="{ResumeSettingsURI}" class="btn btn-success bigmarginright ">
								<i class="icons icon-mail" aria-hidden="true"/>
								<span><xsl:value-of select="$i18n.UpdateResumeSettings" /></span>
							</a>
						</xsl:if>
						
						<xsl:if test="$hasUpdateAccess">
							<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/update/{user/userID}" class="btn btn-success">
								<i class="icons icon-edit" aria-hidden="true"/>
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
			
			<form action="{/Document/requestinfo/uri}" method="post" enctype="multipart/form-data" data-validation="">
				<div class="section-content">
					<xsl:if test="validationException/validationError[fieldName]">
						<xsl:apply-templates select="validationException/validationError[fieldName]" mode="userfields"/>
					</xsl:if>
					
					<xsl:if test="validationException/validationError[not(fieldName)]">
						<xsl:apply-templates select="validationException/validationError[not(fieldName)]" mode="general"/>
					</xsl:if>
					
					<div class="text-muted small mb-2">
						<i class="icons icon-info" aria-hidden="true"/>
						<span><xsl:value-of select="$i18n.RequiredFieldsAreMarked"/></span>
					</div>
					
					<div class="row">
						<div class="col-12 col-md-6">
							<div class="form-group">
								<label for="firstname" id="firstnameLabel" data-validation-required="{$i18n.validationError.Required.User.Firstname}"><xsl:value-of select="$i18n.Firstname"/> *</label>
								
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="'firstname'" />
									<xsl:with-param name="name" select="'firstname'" />
									<xsl:with-param name="element" select="user" />
									<xsl:with-param name="class" select="'form-control'"/>
									<xsl:with-param name="required" select="'required'"/>
									<xsl:with-param name="autocomplete" select="'given-name'"/>
									<xsl:with-param name="data-validation" select="'#firstnameLabel'"/>
									<xsl:with-param name="maxlength" select="'30'"/>
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
								<label for="lastname" id="lastnameLabel" data-validation-required="{$i18n.validationError.Required.User.Lastname}"><xsl:value-of select="$i18n.Lastname"/> *</label>
								
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="'lastname'" />
									<xsl:with-param name="name" select="'lastname'" />
									<xsl:with-param name="element" select="user" />
									<xsl:with-param name="class" select="'form-control'"/>
									<xsl:with-param name="required" select="'required'"/>
									<xsl:with-param name="autocomplete" select="'family-name'"/>
									<xsl:with-param name="data-validation" select="'#lastnameLabel'"/>
									<xsl:with-param name="maxlength" select="'50'"/>
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
								<label for="email" id="emailLabel" data-validation-required="{$i18n.validationError.Required.User.Email}"><xsl:value-of select="$i18n.Email"/> *</label>
								
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="'email'" />
									<xsl:with-param name="name" select="'email'" />
									<xsl:with-param name="element" select="user" />
									<xsl:with-param name="class" select="'form-control'"/>
									<xsl:with-param name="autocomplete" select="'email'"/>
									<xsl:with-param name="required" select="'required'"/>
									<xsl:with-param name="maxlength" select="'255'"/>
									<xsl:with-param name="data-validation" select="'#emailLabel'"/>
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
								<label for="phone" id="phoneLabel" data-validation-required="{$i18n.validationError.Required.User.Phone}">
									<xsl:value-of select="$i18n.Phone"/>
									
									<xsl:if test="PhoneRequired = 'true'">
										<xsl:text> *</xsl:text>
									</xsl:if>
								</label>
								
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="'phone'" />
									<xsl:with-param name="name" select="'phone'" />
									<xsl:with-param name="value" select="user/Attributes/Attribute[Name='phone']/Value" />
									<xsl:with-param name="autocomplete" select="'tel'"/>
									<xsl:with-param name="class" select="'form-control'"/>
									<xsl:with-param name="maxlength" select="'255'"/>
									<xsl:with-param name="data-validation" select="'#phoneLabel'"/>
									<xsl:with-param name="required">
										<xsl:if test="PhoneRequired = 'true'">
											<xsl:text>required</xsl:text>
										</xsl:if>
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
									<label for="phone2" id="phone2Label"><xsl:value-of select="$i18n.Phone"/></label>
									
									<xsl:call-template name="createTextField">
										<xsl:with-param name="id" select="'phone2'" />
										<xsl:with-param name="name" select="'phone2'" />
										<xsl:with-param name="value" select="user/Attributes/Attribute[Name='phone2']/Value" />
										<xsl:with-param name="class" select="'form-control'"/>
										<xsl:with-param name="maxlength" select="'255'"/>
										<xsl:with-param name="data-validation" select="'#phone2Label'"/>
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
									<label for="mobilePhone" id="mobilePhoneLabel" data-validation-required="{$i18n.validationError.Required.User.MobilePhone}"><xsl:value-of select="$i18n.MobilePhone"/> (07xxxxxxxx) *</label>
									
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
											<xsl:with-param name="required" select="'required'"/>
											<xsl:with-param name="maxlength" select="'255'"/>
											<xsl:with-param name="data-validation" select="'#mobilePhoneLabel'"/>
										</xsl:call-template>
										
										<div class="input-group-addon" role="button" id="sendcode" data-url="{/Document/requestinfo/currentURI}/{/Document/module/alias}/sendverification" data-invalidnumber="{$i18n.InvalidPhoneNumber}" data-unknown="{$i18n.CouldNotSendSMS}">
											<i class="icons icon-send" aria-hidden="true"/>
											<span class="input-group-text"><xsl:value-of select="$i18n.SendVerificationCode"/></span>
										</div>
									</div>
									
									<small class="text-danger" id="mobilePhoneError"></small>
								</div>
							</div>
							
							<div class="col-md-6">
								<div class="form-group">
									<label for="verificationCode" id="verificationCodeLabel" data-validation-required="{$i18n.validationError.Required.User.VerificationCode}"><xsl:value-of select="$i18n.ValidationCode"/> *</label>
											
									<xsl:call-template name="createTextField">
										<xsl:with-param name="name" select="'verificationCode'"/>
										<xsl:with-param name="id" select="'verificationCode'"/>
										<xsl:with-param name="required" select="'required'"/>
										<xsl:with-param name="maxlength" select="'255'"/>
										<xsl:with-param name="data-validation" select="'#verificationCodeLabel'"/>
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
								<label for="organization" id="organizationLabel" data-validation-required="{$i18n.validationError.Required.User.Organization}"><xsl:value-of select="$i18n.Organization"/> *</label>
								
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="'organization'" />
									<xsl:with-param name="name" select="'organization'" />
									<xsl:with-param name="value" select="user/Attributes/Attribute[Name='organization']/Value" />
									<xsl:with-param name="class" select="'form-control'"/>
									<xsl:with-param name="required" select="'required'"/>
									<xsl:with-param name="maxlength" select="'255'"/>
									<xsl:with-param name="autocomplete" select="'organization'"/>
									<xsl:with-param name="data-validation" select="'#organizationLabel'"/>
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
								<label for="title" id="titleLabel"><xsl:value-of select="$i18n.Title"/></label>
								
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="'title'" />
									<xsl:with-param name="name" select="'title'" />
									<xsl:with-param name="value" select="user/Attributes/Attribute[Name='title']/Value" />
									<xsl:with-param name="class" select="'form-control'"/>
									<xsl:with-param name="autocomplete" select="'organization-title'"/>
									<xsl:with-param name="data-validation" select="'#titleLabel'"/>
									<xsl:with-param name="maxlength" select="'255'"/>
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
									<label for="password" id="passwordLabel"><xsl:value-of select="$i18n.NewPassword"/></label>
									
									<xsl:call-template name="createPasswordField">
										<xsl:with-param name="id" select="'password'" />
										<xsl:with-param name="name" select="'password'" />
										<xsl:with-param name="class" select="'form-control'"/>
										<xsl:with-param name="autocomplete" select="'new-password'"/>
										<xsl:with-param name="maxlength" select="'255'"/>
										<xsl:with-param name="data-validation" select="'#passwordLabel'"/>
										<xsl:with-param name="aria-describedby" select="'passwordHelpText'"/>
									</xsl:call-template>
									
									<div class="text-muted small" id="passwordHelpText">
										<xsl:value-of select="$i18n.ChangePasswordText"/>
									</div>
								</div>
							</div>
							
							
							<div class="col-12 col-md-6">
								<div class="form-group">
									<label for="passwordMatch" id="password2Label"><xsl:value-of select="$i18n.NewPasswordConfirmation"/></label>
									
									<xsl:call-template name="createPasswordField">
										<xsl:with-param name="id" select="'passwordMatch'" />
										<xsl:with-param name="name" select="'passwordMatch'" />
										<xsl:with-param name="class" select="'form-control'"/>
										<xsl:with-param name="autocomplete" select="'new-password'"/>
										<xsl:with-param name="maxlength" select="'255'"/>
										<xsl:with-param name="data-validation" select="'#password2Label'"/>
										<xsl:with-param name="aria-describedby" select="'password2HelpText'"/>
									</xsl:call-template>
									
									<div class="text-muted small" id="password2HelpText">
										<xsl:value-of select="$i18n.ChangePasswordText"/>
									</div>
								</div>
							</div>
						</div>
					</xsl:if>
					
					<div class="row">
						<div class="col-12">
							<div class="form-group">
								<label for="description" id="descriptionLabel"><xsl:value-of select="$i18n.Description"/></label>
								
								<xsl:call-template name="createTextArea">
									<xsl:with-param name="id" select="'description'" />
									<xsl:with-param name="name" select="'description'" />
									<xsl:with-param name="value" select="user/Attributes/Attribute[Name='description']/Value" />
									<xsl:with-param name="class" select="'form-control'"/>
									<xsl:with-param name="rows" select="null"/>
									<xsl:with-param name="maxlength" select="'4000'"/>
									<xsl:with-param name="data-validation" select="'#descriptionLabel'"/>
								</xsl:call-template>
							</div>
						</div>
					</div>
					
					<label for="profileImageFile" id="profileimagelabel"><xsl:value-of select="$i18n.ProfileImage"/></label>
					
					<xsl:variable name="showProfileImage" select="HasProfileImage and not(requestparameters/parameter[name='deleteProfileImage']/value = 'true')" />
					
					<div class="fileupload-area mb-2">
						<div id="profileImageWrapper">
							<xsl:if test="$showProfileImage">
								<xsl:attribute name="class">hidden</xsl:attribute>
							</xsl:if>
							
							<div id="crop-wrapper" data-imagesize-x="{ImageSize}" data-imagesize-y="{ImageSize}">
								<input type="file" aria-describedby="profileImageFileHelp" data-validation="#profileimagelabel" class="form-control-file margin-auto" id="profileImageFile" name="file" accept="image/*" data-arialabel="{$i18n.UseArrowsToMoveImage}" data-zoomlabel="{$i18n.UseArrowsToAdjustZoom}"/>
							
								<div class="small text-muted mb-3" id="profileImageFileHelp">
									<xsl:value-of select="$i18n.MaximumFileUpload" />
									<xsl:text>:&#160;</xsl:text>
									<xsl:value-of select="MaxAllowedFileSize" />
								</div>
								
								<div class="crop-result hidden"></div>
								
								<input type="hidden" name="crop-coordinates" class="crop-coordinates"/>
							</div>
							
							<script>
								croppieUpload($("#crop-wrapper"));
							</script>
							
							<xsl:call-template name="createHiddenField">
								<xsl:with-param name="name" select="'deleteProfileImage'" />
								<xsl:with-param name="value" select="'false'" />
							</xsl:call-template>
						</div>
						
						<xsl:if test="$showProfileImage">
							<div id="profileImageUploadWrapper">
								<figure class="group large margin-auto">
									<img alt="{$fullName}" src="{/Document/requestinfo/contextpath}{/Document/ProfileImageAlias}/{user/userID}?t={Timestamp}" />
								</figure>
								
								<a href="#" class="btn btn-danger mt-2" id="removeprofileimage">
									<i class="icons icon-delete" aria-hidden="true"/>
									
									<span><xsl:value-of select="$i18n.RemoveProfileImage" /></span>
								</a>
							</div>
						</xsl:if>
					</div>
				</div>
		
				<footer class="d-flex">
					<div class="ml-auto">
						<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/show/{user/userID}" class="btn btn-danger cancel-btn bigmarginright"><i class="icons icon-ban" aria-hidden="true"/><span><xsl:value-of select="$i18n.Cancel" /></span></a>
						
						<button type="submit" class="btn btn-success"><i class="icons icon-check" aria-hidden="true"/><span><xsl:value-of select="$i18n.Save" /></span></button>
					</div>
				</footer>
			</form>
		</section>
	
	</xsl:template>
	
	<xsl:template match="validationError" mode="userfields">
	
		<div class="validation-error" hidden="hidden" data-validation-field="[name={fieldName}]">
			<xsl:choose>
				<xsl:when test="fieldName = 'firstname'">
					<xsl:choose>
						<xsl:when test="validationErrorType = 'RequiredField'">
							<xsl:value-of select="$i18n.validationError.Required.User.Firstname"/>
						</xsl:when>
						<xsl:when test="validationErrorType = 'TooLong'">
							<xsl:value-of select="$i18n.validationError.TooLong.User.Firstname"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				
				<xsl:when test="fieldName = 'lastname'">
					<xsl:choose>
						<xsl:when test="validationErrorType = 'RequiredField'">
							<xsl:value-of select="$i18n.validationError.Required.User.Lastname"/>
						</xsl:when>
						<xsl:when test="validationErrorType = 'TooLong'">
							<xsl:value-of select="$i18n.validationError.TooLong.User.Lastname"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				
				<xsl:when test="fieldName = 'email'">
					<xsl:choose>
						<xsl:when test="validationErrorType = 'RequiredField'">
							<xsl:value-of select="$i18n.validationError.Required.User.Email"/>
						</xsl:when>
						<xsl:when test="validationErrorType = 'TooLong'">
							<xsl:value-of select="$i18n.validationError.TooLong.User.Email"/>
						</xsl:when>
						<xsl:when test="validationErrorType = 'InvalidFormat'">
							<xsl:value-of select="$i18n.validationError.InvalidFormat.User.Email"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				
				<xsl:when test="fieldName = 'phone'">
					<xsl:choose>
						<xsl:when test="validationErrorType = 'RequiredField'">
							<xsl:value-of select="$i18n.validationError.Required.User.Phone"/>
						</xsl:when>
						<xsl:when test="validationErrorType = 'TooLong'">
							<xsl:value-of select="$i18n.validationError.TooLong.User.Phone"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				
				<xsl:when test="fieldName = 'phone2'">
					<xsl:choose>
						<xsl:when test="validationErrorType = 'TooLong'">
							<xsl:value-of select="$i18n.validationError.TooLong.User.Phone2"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				
				<xsl:when test="fieldName = 'mobilePhone'">
					<xsl:choose>
						<xsl:when test="validationErrorType = 'RequiredField'">
							<xsl:value-of select="$i18n.validationError.Required.User.MobilePhone"/>
						</xsl:when>
						<xsl:when test="validationErrorType = 'TooLong'">
							<xsl:value-of select="$i18n.validationError.TooLong.User.MobilePhone"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				
				<xsl:when test="fieldName = 'verificationCode'">
					<xsl:choose>
						<xsl:when test="validationErrorType = 'RequiredField'">
							<xsl:value-of select="$i18n.validationError.Required.User.VerificationCode"/>
						</xsl:when>
						<xsl:when test="validationErrorType = 'TooLong'">
							<xsl:value-of select="$i18n.validationError.TooLong.User.VerificationCode"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				
				<xsl:when test="fieldName = 'organization'">
					<xsl:choose>
						<xsl:when test="validationErrorType = 'RequiredField'">
							<xsl:value-of select="$i18n.validationError.Required.User.Organization"/>
						</xsl:when>
						<xsl:when test="validationErrorType = 'TooLong'">
							<xsl:value-of select="$i18n.validationError.TooLong.User.Organization"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				
				<xsl:when test="fieldName = 'title'">
					<xsl:choose>
						<xsl:when test="validationErrorType = 'TooLong'">
							<xsl:value-of select="$i18n.validationError.TooLong.User.Title"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				
				<xsl:when test="fieldName = 'password'">
					<xsl:choose>
						<xsl:when test="validationErrorType = 'RequiredField'">
							<xsl:value-of select="$i18n.validationError.Required.User.Password"/>
						</xsl:when>
						<xsl:when test="validationErrorType = 'TooLong'">
							<xsl:value-of select="$i18n.validationError.TooLong.User.Password"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				
				<xsl:when test="fieldName = 'passwordMatch'">
					<xsl:choose>
						<xsl:when test="validationErrorType = 'RequiredField'">
							<xsl:value-of select="$i18n.validationError.Required.User.PasswordMatch"/>
						</xsl:when>
						<xsl:when test="validationErrorType = 'TooLong'">
							<xsl:value-of select="$i18n.validationError.TooLong.User.PasswordMatch"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				
				<xsl:when test="fieldName = 'description'">
					<xsl:choose>
						<xsl:when test="validationErrorType = 'TooLong'">
							<xsl:value-of select="$i18n.validationError.TooLong.User.Description"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError" mode="general">
		
		<xsl:choose>
			<xsl:when test="messageKey = 'PasswordDontMatch'">
				<div class="validation-error" hidden="hidden" data-validation-field="[name=password]">
					<xsl:value-of select="$i18n.PasswordDontMatch" />
				</div>
			</xsl:when>
			
			<xsl:when test="messageKey = 'InvalidPasswordStrength'">
				<div class="validation-error" hidden="hidden" data-validation-field="[name=password]">
					<xsl:value-of select="$i18n.InvalidPasswordStrength" />
				</div>
			</xsl:when>
			
			<xsl:when test="messageKey = 'FileSizeLimitExceeded'">
				<div class="validation-error" hidden="hidden" data-validation-field="[name=file]">
					<xsl:value-of select="$i18n.FileSizeLimitExceeded" />
				</div>
			</xsl:when>
			
			<xsl:when test="messageKey = 'UnableToParseRequest'">
				<div class="validation-error" hidden="hidden" data-validation-field="[name=file]">
					<xsl:value-of select="$i18n.UnableToParseRequest" />
				</div>
			</xsl:when>
			
			<xsl:when test="messageKey = 'UnableToParseProfileImage'">
				<div class="validation-error" hidden="hidden" data-validation-field="[name=file]">
					<xsl:value-of select="$i18n.UnableToParseProfileImage" />
				</div>
			</xsl:when>
			
			<xsl:when test="messageKey = 'UnableToDeleteProfileImage'">
				<div class="validation-error" hidden="hidden" data-validation-field="[name=file]">
					<xsl:value-of select="$i18n.UnableToDeleteProfileImage" />
				</div>
			</xsl:when>
			
			<xsl:when test="messageKey = 'InvalidProfileImageFileFormat'">
				<div class="validation-error" hidden="hidden" data-validation-field="[name=file]">
					<xsl:value-of select="$i18n.InvalidProfileImageFileFormat" />
				</div>
			</xsl:when>
			
			<xsl:when test="messageKey = 'EmailAlreadyTaken'">
				<div class="validation-error" hidden="hidden" data-validation-field="[name=email]">
					<xsl:value-of select="$i18n.EmailAlreadyTaken" />
				</div>
			</xsl:when>
			
			<xsl:when test="messageKey = 'WrongVerificationCode'">
				<div class="validation-error" hidden="hidden" data-validation-field="[name=verificationCode]">
					<xsl:value-of select="$i18n.WrongVerificationCode" />
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