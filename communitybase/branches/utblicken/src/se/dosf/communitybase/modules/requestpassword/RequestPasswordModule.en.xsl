<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="RequestPasswordModuleTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="RequestNewPassword.header" select="'Forgot your password?'" />
	<xsl:variable name="RequestNewPassword.information" select="'Please fill in you email address in the form below and click on the &quot;Request new password&quot; button, and a new password will be sent to you'" />
	<xsl:variable name="RequestNewPassword.email" select="'Email address'" />
	<xsl:variable name="RequestNewPassword.submit" select="'Request new password'" />
	
	<xsl:variable name="NewPasswordSent.header" select="'Forgot your password?'" />
	<xsl:variable name="NewPasswordSent.notificationmessage" select="'A new password has been sent to your email address'" />
	<xsl:variable name="NewPasswordSent.back" select="'Go back to the login page'" />
	
	<xsl:variable name="RequestNewPasswordEmail.header" select="'Here is your new Parents Meeting password'" />
	<xsl:variable name="RequestNewPasswordEmail.newpassword" select="'New password'" />
	<xsl:variable name="RequestNewPasswordEmail.information" select="'You can change your password after you have logged in. Do this by clicking on the &quot;My Settings&quot; button'" />
	<xsl:variable name="RequestNewPasswordEmail.about.part1" select="'If you want to know more about the thoughts behind Parents Meeting, please go to'" />
	<xsl:variable name="RequestNewPasswordEmail.about.part2" select="'Parents Meeting'" />
	<xsl:variable name="RequestNewPasswordEmail.municipalitylink" select="'Link to'" />
	
	<xsl:variable name="validationError.RequiredField" select="'You have to fill in the field'" />
	<xsl:variable name="validationError.InvalidFormat" select="'The field has an invalid format'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'The field has an unknown error'" />
	<xsl:variable name="validationError.field.email" select="'email'" />
	<xsl:variable name="validationError.messageKey.UserNotFound" select="'The email address you have specified could not be found in Parents Meeting'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'An unknown error has occured'" />				

</xsl:stylesheet>
