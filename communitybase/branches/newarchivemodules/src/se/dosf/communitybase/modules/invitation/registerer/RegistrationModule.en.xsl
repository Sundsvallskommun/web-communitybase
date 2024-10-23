<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="RegistrationModuleTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="registered.header" select="'Registration completed'" />
	<xsl:variable name="registered.text" select="'Your registration is now completed and you can login to Parents Meeting by clicking on the &quot;Login&quot; link in the upper right corner of the page'" />
	
	<xsl:variable name="register.header" select="'Welcome to Parents Meeting'" />	
	<xsl:variable name="register.text" select="'You have been invited to Parents Meeting, an Internet portal where you gain access to information about your childrens schooling around-the-clock. To be able to use this service, you first have to fill in the registration form below'" />	
	<xsl:variable name="register.rules.header" select="'Rules of conduct'" />

	<!-- Important to hold the format in this variable -->
	<xsl:variable name="register.rules.text">
	
Rules of conduct for Parents' Meeting
You may not defame, revile, harass, discriminate against, persecute or threaten others or otherwise violate the rights of others.
Observe common netiquette. Messages in discussions that could be interpreted as offensive will be removed by the preschool staff.
No sensitive information may be published.
Preschool staff must act with caution and remember that no pictures or information that may be offensive will be added on Parents' Meeting.

Copyright
You may only submit materials to the system, which you yourself own the copyright to or have permission to redistribute. Such authorization may only be given by the copyright owner. Copyright law is very broad and applies to virtually all types of texts, newspaper articles, pictures, drawings, computer software and music, and numerous others. If you submit materials which others hold the copyright to, you may violate copyright law and be burdened with legal liability and penalties. You may only use material from Parents' Meeting for private purposes, and you may not reproduce, copy, sell or otherwise use the material from the Parents' Meeting without permission from the copyright holders of material.

Electronic mail
Electronic mail which is only sent to named subscribers in the system, is private. Note that the recipient of private mail may forward it to other persons or use it as a post in i.e. conferences.
	
Personal Information
Personal data records are treated in accordance with the Personal Data Act (SFS No. 1998:204). This information is stored using IT for administrative purposes. You have the right to request extractions and corrections. The Privacy Manager in this case is the Childcare and Education Committee, Sundsvall municipality, 851 85 Sundsvall.

	</xsl:variable>
	
	<xsl:variable name="register.confirm" select="'I have read the Rules of Conduct and accept them.'" />
	<xsl:variable name="register.information" select="'To be able to login to Parents Meeting you have to select a password. You also have to fill in your first and last name. The other fields are optional.'" />
	<xsl:variable name="register.email" select="'Your username is the same as your email address'" />
	<xsl:variable name="register.firstname" select="'First name'" />
	<xsl:variable name="register.lastname" select="'Last name'" />
	<xsl:variable name="register.password" select="'Password (atleast 6 characters)'" />
	<xsl:variable name="register.confirmpassword" select="'Confirm password'" />
	<xsl:variable name="register.phonehome" select="'Telephone home'" />
	<xsl:variable name="register.phonemobile" select="'Telephone mobile'" />
	<xsl:variable name="register.phonework" select="'Telephone work'" />
	<xsl:variable name="register.submit" select="'Create account'" />
	
	
	<xsl:variable name="validationError.RequiredField" select="'You have to fill in the field'" />
	<xsl:variable name="validationError.InvalidFormat" select="'The field has an invalid format'" />
	<xsl:variable name="validationError.TooLong" select="'The content in the field is too long'" />
	<xsl:variable name="validationError.TooShort" select="'The content in the field is too short'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'The field has an unknown error'" />
	
	<xsl:variable name="validationError.field.firstname" select="'first name'" />
	<xsl:variable name="validationError.field.lastname" select="'last name'" />
	<xsl:variable name="validationError.field.password" select="'password'" />
	<xsl:variable name="validationError.field.commentText" select="'comment'" />
	<xsl:variable name="validationError.field.url" select="'path'" />
	<xsl:variable name="validationError.field.text" select="'content'" />
	
	<xsl:variable name="validationError.messageKey.license" select="'You have not accepted the rules of conduct'" />
	<xsl:variable name="validationError.messageKey.passwordsDontMatch" select="'The passwords do not match'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'An unknown error has occured'" />				

</xsl:stylesheet>
