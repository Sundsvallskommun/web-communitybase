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
		/js/invitationmodule.js
	</xsl:variable>

	<xsl:template match="Document">
		
		<div id="InvitationModule" class="contentitem">
			<xsl:apply-templates select="Register" />
		</div>
		
	</xsl:template>
	
	<xsl:template match="Register">
		
		<section>
			<header>
				<xsl:value-of select="RegistrationText" disable-output-escaping="yes" />
			</header>
			
			<form action="{/Document/requestinfo/uri}" id="registrationForm" method="post" enctype="multipart/form-data" data-validation="">
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
						<div class="col-md-6">
							<div class="form-group">
								<label for="firstname" id="firstnameLabel" data-validation-required="{$i18n.validationError.Required.User.Firstname}">
									<xsl:value-of select="$i18n.Firstname"/> *
								</label>
										
								<xsl:call-template name="createTextField">
									<xsl:with-param name="name" select="'firstname'"/>
									<xsl:with-param name="id" select="'firstname'"/>
									<xsl:with-param name="class" select="'form-control'"/>
									<xsl:with-param name="autocomplete" select="'given-name'"/>
									<xsl:with-param name="data-validation" select="'#firstnameLabel'"/>
									<xsl:with-param name="maxlength" select="'30'"/>
									<xsl:with-param name="required" select="'required'"/>
								</xsl:call-template>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="form-group">
								<label for="lastname" id="lastnameLabel" data-validation-required="{$i18n.validationError.Required.User.Lastname}">
									<xsl:value-of select="$i18n.Lastname"/> *
								</label>
										
								<xsl:call-template name="createTextField">
									<xsl:with-param name="name" select="'lastname'"/>
									<xsl:with-param name="id" select="'lastname'"/>
									<xsl:with-param name="class" select="'form-control'"/>
									<xsl:with-param name="autocomplete" select="'family-name'"/>
									<xsl:with-param name="data-validation" select="'#lastnameLabel'"/>
									<xsl:with-param name="maxlength" select="'50'"/>
									<xsl:with-param name="required" select="'required'"/>
								</xsl:call-template>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-md-6">
							<div class="form-group">
								<label for="email" id="emailLabel" data-validation-required="{$i18n.validationError.Required.User.Email}">
									<xsl:value-of select="$i18n.Email"/>
								</label>
										
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="'email'" />
									<xsl:with-param name="name" select="'email'" />
									<xsl:with-param name="value" select="Invitation/email" />
									<xsl:with-param name="requestparameters" select="null" />
									<xsl:with-param name="disabled" select="'disabled'" />
									<xsl:with-param name="class" select="'form-control'"/>
									<xsl:with-param name="required" select="'required'"/>
									<xsl:with-param name="maxlength" select="'255'"/>
									<xsl:with-param name="data-validation" select="'#emailLabel'"/>
								</xsl:call-template>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="form-group">
								<label for="phone" id="phoneLabel" data-validation-required="{$i18n.validationError.Required.User.Phone}">
									<xsl:value-of select="$i18n.Phone"/>
									
									<xsl:if test="PhoneRequired = 'true'">
										<xsl:text> *</xsl:text>
									</xsl:if>
								</label>
										
								<xsl:call-template name="createTextField">
									<xsl:with-param name="name" select="'phone'"/>
									<xsl:with-param name="id" select="'phone'"/>
									<xsl:with-param name="class" select="'form-control'"/>
									<xsl:with-param name="autocomplete" select="'tel'"/>
									<xsl:with-param name="required">
										<xsl:if test="PhoneRequired = 'true'">
											<xsl:text>required</xsl:text>
										</xsl:if>
									</xsl:with-param>
									<xsl:with-param name="maxlength" select="'255'"/>
									<xsl:with-param name="data-validation" select="'#phoneLabel'"/>
								</xsl:call-template>
							</div>
						</div>
					</div>
					
					<xsl:if test="PhoneValidationSupported">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label for="mobilePhone" id="mobilePhoneLabel" data-validation-required="{$i18n.validationError.Required.User.MobilePhone}"><xsl:value-of select="$i18n.MobilePhone"/> (07xxxxxxxx) *</label>
									
									<div class="input-group">
										<xsl:call-template name="createTextField">
											<xsl:with-param name="id" select="'mobilePhone'" />
											<xsl:with-param name="name" select="'mobilePhone'" />
											<xsl:with-param name="class" select="'form-control'"/>
											<xsl:with-param name="required" select="'required'"/>
											<xsl:with-param name="maxlength" select="'255'"/>
											<xsl:with-param name="data-validation" select="'#mobilePhoneLabel'"/>
										</xsl:call-template>
										
										<div class="input-group-addon pointer" id="sendcode" data-url="{/Document/requestinfo/currentURI}/{/Document/module/alias}/sendverification" data-invalidnumber="{$i18n.InvalidPhoneNumber}" data-unknown="{$i18n.CouldNotSendSMS}">
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
										<xsl:with-param name="class" select="'form-control'"/>
										<xsl:with-param name="required" select="'required'"/>
										<xsl:with-param name="maxlength" select="'255'"/>
										<xsl:with-param name="data-validation" select="'#verificationCodeLabel'"/>
									</xsl:call-template>
								</div>
							</div>
						</div>
					</xsl:if>
					
					<div class="row">
						<div class="col-md-6">
							<div class="form-group">
								<label for="organization" id="organizationLabel" data-validation-required="{$i18n.validationError.Required.User.Organization}"><xsl:value-of select="$i18n.Organization"/> (*)</label>
										
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="'organization'" />
									<xsl:with-param name="name" select="'organization'" />
									<xsl:with-param name="class" select="'form-control'"/>
									<xsl:with-param name="autocomplete" select="'organization'"/>
									<xsl:with-param name="required" select="'required'"/>
									<xsl:with-param name="maxlength" select="'255'"/>
									<xsl:with-param name="data-validation" select="'#organizationLabel'"/>
								</xsl:call-template>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="form-group">
								<label for="title" id="titleLabel"><xsl:value-of select="$i18n.Title"/></label>
										
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="'title'" />
									<xsl:with-param name="name" select="'title'" />
									<xsl:with-param name="class" select="'form-control'"/>
									<xsl:with-param name="autocomplete" select="'organization-title'"/>
									<xsl:with-param name="data-validation" select="'#titleLabel'"/>
									<xsl:with-param name="maxlength" select="'255'"/>
								</xsl:call-template>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-12">
							<div class="form-group">
								<label for="description" id="descriptionLabel"><xsl:value-of select="$i18n.Description"/></label>
								
								<xsl:call-template name="createTextArea">
									<xsl:with-param name="id" select="'description'" />
									<xsl:with-param name="name" select="'description'" />
									<xsl:with-param name="class" select="'form-control'" />
									<xsl:with-param name="rows" select="null"/>
									<xsl:with-param name="maxlength" select="'4000'"/>
									<xsl:with-param name="data-validation" select="'#descriptionLabel'"/>
								</xsl:call-template>
							</div>
						</div>
					</div>
					
					<label for="profileImageFile" id="profileimagelabel"><xsl:value-of select="$i18n.ProfileImage"/></label>
					
					<div class="fileupload-area bigmarginbottom">
						<div>
							<input type="file" class="form-control-file margin-auto" id="profileImageFile" name="file" data-validation="#profileimagelabel"></input>
							
							<small class="form-text text-muted">
								<xsl:value-of select="$i18n.MaximumFileUpload" />
								<xsl:text>:&#160;</xsl:text>
								<xsl:value-of select="MaxAllowedFileSize" />
							</small>
						</div>
					</div>
		
					<div class="row">
						<div class="col-md-6">
							<div class="form-group">
								<label for="password" id="passwordLabel" data-validation-required="{$i18n.validationError.Required.User.Password}"><xsl:value-of select="$i18n.ChoosePassword"/> *</label>
								
								<xsl:call-template name="createPasswordField">
									<xsl:with-param name="name" select="'password'"/>
									<xsl:with-param name="id" select="'password'"/>
									<xsl:with-param name="class" select="'form-control'"/>
									<xsl:with-param name="autocomplete" select="'new-password'"/>
									<xsl:with-param name="maxlength" select="'255'"/>
									<xsl:with-param name="required" select="'required'"/>
									<xsl:with-param name="aria-describedby" select="'passwordHelpText'"/>
									<xsl:with-param name="data-validation" select="'#passwordLabel'"/>
								</xsl:call-template>
								
								<div class="small text-muted" id="passwordHelpText"><xsl:value-of select="$i18n.PasswordStrengthDescription" /></div>
							</div>
						</div>
		
						<div class="col-md-6">
							<div class="form-group">
								<label for="passwordMatch" id="password2Label" data-validation-required="{$i18n.validationError.Required.User.Password}"><xsl:value-of select="$i18n.ConfirmPassword"/> *</label>
								
								<xsl:call-template name="createPasswordField">
									<xsl:with-param name="name" select="'passwordMatch'"/>
									<xsl:with-param name="id" select="'passwordMatch'"/>
									<xsl:with-param name="class" select="'form-control'"/>
									<xsl:with-param name="autocomplete" select="'new-password'"/>
									<xsl:with-param name="maxlength" select="'255'"/>
									<xsl:with-param name="required" select="'required'"/>
									<xsl:with-param name="data-validation" select="'#password2Label'"/>
								</xsl:call-template>
							</div>
						</div>
					</div>
					
					<xsl:if test="RegistrationApprovalLabel">
						<div class="row mt-3">
							<div class="col-12">
								<div class="form-check">
									<label class="form-check-label mr-1">
										<xsl:call-template name="createCheckbox">
											<xsl:with-param name="name" select="'approveRegistrationText'"/>
											<xsl:with-param name="id" select="'approveRegistrationText'"/>
											<xsl:with-param name="class" select="'form-check-input'"/>
										</xsl:call-template>
										
										<xsl:value-of select="RegistrationApprovalLabel"/>
									</label>
									
									<a href="#" id="showApprovalText" data-toggle="modal" data-target="#registrationapprovalmodal"><xsl:value-of select="ModalLinkLabel"/></a>
									
									<input type="hidden" name="dummyValidationField"/>
								</div>
							</div>
						</div>
					</xsl:if>
				</div>
				
				<footer class="d-flex">
					<button class="btn btn-success ml-auto" type="submit">
						<i class="icons icon-check" aria-hidden="true"/>
						
						<span><xsl:value-of select="$i18n.Save" /></span>
					</button>
				</footer>
			</form>
		</section>
		
		<xsl:if test="RegistrationApprovalLabel">
			<div class="modal fade" id="registrationapprovalmodal" tabindex="-1" role="dialog" aria-labelledby="registrationApprovalModalTitle">
				<div class="modal-dialog modal-lg" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title" id="registrationApprovalModalTitle"><xsl:value-of select="RegistrationApprovalTitle"/></h5>
							
							<button type="button" class="close" data-dismiss="modal" aria-label="{$i18n.Close}">
								<span><xsl:value-of select="$i18n.Close"/></span>
								
								<i class="icons icon-close" aria-hidden="true"/>
							</button>
						</div>
						
						<div class="modal-body">
							<xsl:value-of select="RegistrationApprovalText" disable-output-escaping="yes" />
							
							<div class="mt-2 text-align-right">
								<button class="btn btn-danger" data-dismiss="modal">
									<span><xsl:value-of select="$i18n.Close"/></span>
								</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</xsl:if>
		
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
			
			<xsl:when test="messageKey = 'RegistrationTextNotApproved'">
				<div class="alert alert-danger" role="alert">
					<i class="icons icon-warning" aria-hidden="true"/>
					
					<span><xsl:value-of select="$i18n.RegistrationTextNotApproved" />!</span>
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