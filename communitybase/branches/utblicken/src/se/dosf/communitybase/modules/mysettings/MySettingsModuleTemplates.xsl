<?xml version="1.0" encoding="ISO-8859-1" ?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:template match="document">

		<div class="contentitem">

			<div class="normal">
				<h1>
					<xsl:value-of select="/document/module/name" />
				</h1>
			</div>

			<xsl:apply-templates select="MySettingsModule" />

		</div>
	</xsl:template>
	
	<xsl:template match="MySettingsModule">
		<form method="post" name="mysettings" id="mysettings" action="{/document/requestinfo/url}">
		
			<p class="info">
				<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/information.png" />
				<xsl:text>&#x20;</xsl:text>
				<xsl:value-of select="$MySettingsModule.information1" />. <br />
				<xsl:value-of select="$MySettingsModule.information2" />.
			</p>
			<p class="info">
				<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/warning.png" />
				<xsl:text>&#x20;</xsl:text>
				<xsl:value-of select="$MySettingsModule.information3" />.
			</p>
			
			<div class="divNormal">
				
				<h1 class="normalTableHeading" colspan="4" align="left">
					<xsl:value-of select="$MySettingsModule.header" />
				</h1>
				<xsl:apply-templates select="validationException/validationError" />
				<xsl:apply-templates select="user" mode="form" />
			
			</div>
			
		</form>
	</xsl:template>

	<xsl:template match="user" mode="form">
	
			<div class="noBorderDiv">
			
				<div class="full bigmarginbottom">
					<b><xsl:value-of select="$user.personinfo" /></b>
					<br /><hr />
				</div>
				<div class="bigmarginbottom">
					<div class="floatleft" style="width: 20%">
						<xsl:value-of select="$user.firstname" />:
					</div>
					<div>
						<input type="text" name="firstname" size="40">
							<xsl:attribute name="value">
								<xsl:choose>
									<xsl:when test="../requestparameters">
										<xsl:value-of select="../requestparameters/parameter[name='firstname']/value"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="firstname"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:attribute>
						</input>
					</div>
				</div>
				<div class="bigmarginbottom">
					<div class="floatleft" style="width: 20%">
						<xsl:value-of select="$user.lastname" />:
					</div>
					<div>	
						<input type="text" name="lastname" size="40">
							<xsl:attribute name="value">
								<xsl:choose>
									<xsl:when test="../requestparameters">
										<xsl:value-of select="../requestparameters/parameter[name='lastname']/value"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="lastname"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:attribute>
						</input>
					</div>
				</div>
				<div class="bigmarginbottom">
					<div class="floatleft" style="width: 20%">
						<xsl:value-of select="$user.phoneHome" />:
					</div>
					<div>
						<input type="text" name="phoneHome" size="40">
							<xsl:attribute name="value">
								<xsl:choose>
									<xsl:when test="../requestparameters">
										<xsl:value-of select="../requestparameters/parameter[name='phoneHome']/value"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="communityUserAttributes/phoneHome"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:attribute>
						</input>
					</div>
				</div>
				<div class="bigmarginbottom">
					<div class="floatleft" style="width: 20%">
						<xsl:value-of select="$user.phoneMobile" />:
					</div>
					<div>
						<input type="text" name="phoneMobile" size="40">
							<xsl:attribute name="value">
								<xsl:choose>
									<xsl:when test="../requestparameters">
										<xsl:value-of select="../requestparameters/parameter[name='phoneMobile']/value"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="communityUserAttributes/phoneMobile"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:attribute>
						</input>
					</div>
				</div>
				<div class="bigmarginbottom">
					<div class="floatleft" style="width: 20%">
						<xsl:value-of select="$user.phoneWork" />:
					</div>
					<div>
						<input type="text" name="phoneWork" size="40">
							<xsl:attribute name="value">
								<xsl:choose>
									<xsl:when test="../requestparameters">
										<xsl:value-of select="../requestparameters/parameter[name='phoneWork']/value"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="communityUserAttributes/phoneWork"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:attribute>
						</input>
					</div>
				</div>
			
			
				<br />
				
				<div class="full bigmarginbottom">
					<b><xsl:value-of select="$user.accountinfo" /></b>
					<br /><hr />
				</div>
				<div class="bigmarginbottom">
					<div class="floatleft" style="width: 20%">
						<xsl:value-of select="$user.email" />:
					</div>
					<div>
						<input type="text" name="email" size="40">
							<xsl:attribute name="value">
								<xsl:choose>
									<xsl:when test="../requestparameters">
										<xsl:value-of select="../requestparameters/parameter[name='email']/value"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="email"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:attribute>
						</input>
					</div>
				</div>
				<div class="floatleft bigmarginbottom" style="width: 20%">
					<input type="checkbox" name="changepassword" onclick="document.mysettings.password.disabled=!this.checked; document.mysettings.passwordReference.disabled=!this.checked">
						<xsl:if test="../requestparameters/parameter[name='changepassword'][value='on']">
							<xsl:attribute name="checked" />
						</xsl:if>	
					</input>
					<xsl:value-of select="$user.changepassword" />:
				</div>
				<div style="clear: both" />
				<div class="bigmarginbottom">
					<div class="floatleft" style="width: 20%">
						<label for="password"><xsl:value-of select="$user.newpassword" />: <br />(<xsl:value-of select="$user.minimumchars" />)</label>
					</div>
					<div>
						<input type="password" name="password" value="{../requestparameters/parameter[name='password']/value}" size="40" >
							<xsl:choose>
								<xsl:when test="../requestparameters">
									<xsl:if test="not(../requestparameters/parameter[name='changepassword'])">
										<xsl:attribute name="disabled" />
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<xsl:attribute name="disabled" />
								</xsl:otherwise>
							</xsl:choose>
						</input>
					</div>
				</div>
				<div class="bigmarginbottom">
					<div class="floatleft" style="width: 20%">
						<label for="password"><xsl:value-of select="$user.confirmpassword" />:</label>
					</div>
					<div>
						<input type="password" name="passwordReference" value="{../requestparameters/parameter[name='passwordReference']/value}" size="40" >
							<xsl:choose>
								<xsl:when test="../requestparameters">
									<xsl:if test="not(../requestparameters/parameter[name='changepassword'])">
										<xsl:attribute name="disabled" />
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<xsl:attribute name="disabled" />
								</xsl:otherwise>
							</xsl:choose>
						</input>
					</div>
				</div>	
				
				<br />
				
				<div class="full bigmarginbottom">
					<b><xsl:value-of select="$user.emailresume.header" /></b>
					<br /><hr />
				</div>
				<div class="bigmarginbottom">
					<div class="floatleft" style="width: 40%">
						<input type="checkbox" name="emailresume" onclick="document.mysettings.resumetime.disabled=!this.checked" >
							<xsl:if test="communityUserAttributes/resume">
								<xsl:attribute name="checked" />
							</xsl:if>
						</input>
						<xsl:value-of select="$user.emailresume.description" />:
					</div>
					<div>
						<select name="resumetime">
	
							<option value="0">00:00</option>
							<option value="1">01:00</option>
							<option value="2">02:00</option>
							<option value="3">03:00</option>
							<option value="4">04:00</option>
							<option value="5">05:00</option>
							<option value="6">06:00</option>
							<option value="7">07:00</option>
							<option value="8">08:00</option>
							<option value="9">09:00</option>
							<option value="10">10:00</option>
							<option value="11">11:00</option>
							<option value="12">12:00</option>
							<option value="13">13:00</option>
							<option value="14">14:00</option>
							<option value="15">15:00</option>
							<option value="16" selected="true">16:00</option>
							<option value="17">17:00</option>
							<option value="18">18:00</option>
							<option value="19">19:00</option>
							<option value="20">20:00</option>
							<option value="21">21:00</option>
							<option value="22">22:00</option>
							<option value="23">23:00</option>
							
						</select>
						
						<xsl:choose>
							<xsl:when test="not(communityUserAttributes/resume)">
								<script language="javascript">
									document.mysettings.resumetime.disabled = true;
								</script>
							</xsl:when>
							<xsl:otherwise>
								<script language="javascript">
									document.mysettings.resumetime.selectedIndex = <xsl:value-of select="communityUserAttributes/resume" />;
								</script>
							</xsl:otherwise>
						</xsl:choose>
						
					</div>	
				</div>			
				<div align="right">
					<input type="submit" value="{$user.submit}" />
				</div>
			</div>
	</xsl:template>


	<xsl:template match="validationError">
		<xsl:if test="fieldName and validationErrorType">
			<p class="error">
				<xsl:choose>
					<xsl:when test="validationErrorType='RequiredField'">
						<xsl:text><xsl:value-of select="$validationError.RequiredField"/></xsl:text>
					</xsl:when>
					<xsl:when test="validationErrorType='InvalidFormat'">
						<xsl:text><xsl:value-of select="$validationError.InvalidFormat"/></xsl:text>
					</xsl:when>
					<xsl:when test="validationErrorType='TooShort'">
						<xsl:text><xsl:value-of select="$validationError.TooShort" /></xsl:text>
					</xsl:when>
					<xsl:when test="validationErrorType='TooLong'">
						<xsl:text><xsl:value-of select="$validationError.TooLong" /></xsl:text>
					</xsl:when>									
					<xsl:otherwise>
						<xsl:text><xsl:value-of select="$validationError.unknownValidationErrorType"/></xsl:text>
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:choose>
					<xsl:when test="fieldName = 'email'">
						<xsl:value-of select="$validationError.field.email" />!
					</xsl:when>
					<xsl:when test="fieldName = 'password'">
						<xsl:value-of select="$validationError.field.password" />!
					</xsl:when>
					<xsl:when test="fieldName = 'förnamn'">
						<xsl:value-of select="$validationError.field.firstname" />!
					</xsl:when>
					<xsl:when test="fieldName = 'efternamn'">
						<xsl:value-of select="$validationError.field.lastname" />!
					</xsl:when>
					<xsl:when test="fieldName = 'GroupAccessLevel'">
						<xsl:value-of select="$validationError.field.authority" />!
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
					<xsl:when test="messageKey='UpdateFailedUserNotFound'">
						<xsl:value-of select="$validationError.messageKey.UpdateFailedUserNotFound" />!
					</xsl:when>	
					<xsl:when test="messageKey='UserAlreadyExist'">
						<xsl:value-of select="$validationError.messageKey.UserAlreadyExist" />!
					</xsl:when>		
					<xsl:otherwise>
						<xsl:value-of select="$validationError.unknownMessageKey" />!
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
	</xsl:template>	
</xsl:stylesheet>