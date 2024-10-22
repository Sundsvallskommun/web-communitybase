<?xml version="1.0" encoding="ISO-8859-1" ?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:template match="document">

		<xsl:apply-templates select="RequestNewPassword" />
		<xsl:apply-templates select="NewPasswordSent" />
		<xsl:apply-templates select="RequestNewPasswordEmail" />
		
	</xsl:template>
	
	<xsl:template match="RequestNewPassword">
	
		<div class="contentitem">
			
			<form method="post" name="mysettings" id="mysettings" action="{/document/requestinfo/url}">
				<div class="divNormal">

					<h1 class="normalTableHeading" colspan="4" align="left">
						<xsl:value-of select="$RequestNewPassword.header" />?
					</h1>
					
					<xsl:apply-templates select="validationException/validationError" />
					
					<p><xsl:value-of select="$RequestNewPassword.information" />.</p>
					<div class="bigmarginbottom">
						<div class="floatleft" style="width: 15%">
							<xsl:value-of select="$RequestNewPassword.email" />:
						</div>
						<div>
							<input type="text" name="email" size="40">
								<xsl:attribute name="value">
									<xsl:if test="requestparameters">
										<xsl:value-of select="requestparameters/parameter[name='email']/value"/>
									</xsl:if>
								</xsl:attribute>
							</input>
						</div>
					</div>
					<div align="right">
						<input type="submit" value="{$RequestNewPassword.submit}" />
					</div>
				</div>
			</form>
			
		</div>
	
	</xsl:template>
	
	<xsl:template match="NewPasswordSent">
	
		<div class="divNormal">

			<h1 class="normalTableHeading" colspan="4" align="left">
				<xsl:value-of select="$NewPasswordSent.header" />?
			</h1>
			
			<p style="color: green"><b><xsl:value-of select="$NewPasswordSent.notificationmessage" />.</b></p>
			<div align="right">
				<a href="{/document/requestinfo/currentURI}/login"><xsl:value-of select="$NewPasswordSent.back" /></a>
			</div>
			
		</div>
	
	</xsl:template>
	
	<xsl:template match="RequestNewPasswordEmail">
	
		<html>
			<head>
			</head>
			<body>
				<table>
					<tr>
						<th align="left"><xsl:value-of select="$RequestNewPasswordEmail.header" />:</th>
					</tr>
					<tr>
						<td>
							<br/>
							<xsl:value-of select="$RequestNewPasswordEmail.newpassword" />:
							<xsl:text>&#x20;</xsl:text>
							<xsl:value-of select="user/password"/>
						</td>
					</tr>
					<tr>
						<td>
							<br/>
							<xsl:value-of select="$RequestNewPasswordEmail.information" />.							
						</td>
					</tr>	
					<tr>
						<td>
							<xsl:value-of select="$RequestNewPasswordEmail.about.part1" /><xsl:text>&#x20;</xsl:text><a href="{systemURL}"><xsl:value-of select="$RequestNewPasswordEmail.about.part2" /></a>.
						</td>
					</tr>
					<tr>
						<td>
							<xsl:value-of select="$RequestNewPasswordEmail.municipalitylink" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="municipalityName" /><xsl:text>&#x20;</xsl:text>
							<a href="{municipalityURL}"><xsl:value-of select="municipalityURL" /></a>
						</td>
					</tr>					
				</table>				
			</body>
		</html>

	
	</xsl:template>

	<xsl:template match="validationError">
		<xsl:if test="fieldName and validationErrorType">
			<p class="error">
				<xsl:choose>
					<xsl:when test="validationErrorType='RequiredField'">
						<xsl:value-of select="$validationError.RequiredField" />
					</xsl:when>
					<xsl:when test="validationErrorType='InvalidFormat'">
						<xsl:value-of select="$validationError.InvalidFormat" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$validationError.unknownValidationErrorType" />
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:choose>
					<xsl:when test="fieldName = 'email'">
						<xsl:value-of select="$validationError.field.email" />!
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
					<xsl:when test="messageKey = 'UserNotFound'">
						<xsl:value-of select="$validationError.messageKey.UserNotFound" />!
					</xsl:when>			
					<xsl:otherwise>
						<xsl:value-of select="$validationError.unknownMessageKey" />!
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
	</xsl:template>	
</xsl:stylesheet>