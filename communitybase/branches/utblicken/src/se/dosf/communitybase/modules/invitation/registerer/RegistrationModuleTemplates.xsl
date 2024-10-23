<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"/>

	<xsl:template match="document">
		<div class="contentitem holderWide2">
						
			<xsl:apply-templates select="register"/>
			<xsl:apply-templates select="registered"/>		
		
		</div>
	</xsl:template>
		
	<xsl:template match="registered">
	
		<div class="normal">	
			<h1><xsl:value-of select="$registered.header" />!</h1>
		</div>
	
		<p><xsl:value-of select="$registered.text" />.</p>
	
	</xsl:template>	
		
	<xsl:template match="register">

		<div class="normal">	
			<h1><xsl:value-of select="$register.header" />!</h1>
		</div>

		<p><xsl:value-of select="$register.text" />.</p>

		<xsl:apply-templates select="validationException/validationError"/>

		<form method="post" action="{/document/requestinfo/url}">
			<h2><xsl:value-of select="$register.rules.header" /></h2>
	
			<textarea cols="80" rows="6" readonly="true">
				<xsl:value-of select="registerRules" />
			</textarea>
	
			<p>
				<input type="checkbox" name="license" value="true">
								
					<xsl:if test="requestparameters/parameter[name='license']/value">
						<xsl:attribute name="checked"/>
					</xsl:if>
					
				</input>
				<xsl:value-of select="$register.confirm" />.
			</p>
			
			<p><xsl:value-of select="$register.information" />.</p>
	
			<p><xsl:value-of select="$register.email" />: <b><xsl:value-of select="invitation/email"/></b></p>
			
			<div class="loginFormContainer">	
				<div class="loginFormAround">
					<div class="loginFormLeft">
						<label for="firstname"><xsl:value-of select="$register.firstname" />:</label>
					</div>
					<div class="loginFormMiddle">
						<p>
							<input type="text" name="firstname" id="firstname" value="{requestparameters/parameter[name='firstname']/value}"/>
						</p>
					</div>
				</div>
				
				<div class="loginFormAround">
					<div class="loginFormLeft">
						<label for="lastname"><xsl:value-of select="$register.lastname" />:</label>
					</div>
					<div class="loginFormMiddle">
						<p>
							<input type="text" name="lastname" id="lastname" value="{requestparameters/parameter[name='lastname']/value}"/>
						</p>
					</div>
				</div>		
				
				<div class="loginFormAround">
					<div class="loginFormLeft">
						<label for="password"><xsl:value-of select="$register.password" />:</label>
					</div>
					<div class="loginFormMiddle">
						<p>
							<input type="password" name="password" id="password" value="{requestparameters/parameter[name='password']/value}"/>
						</p>
					</div>
				</div>		
				
				<div class="loginFormAround">
					<div class="loginFormLeft">
						<label for="passwordReference"><xsl:value-of select="$register.confirmpassword" />:</label>
					</div>
					<div class="loginFormMiddle">
						<p>
							<input type="password" name="passwordReference" id="passwordReference" value="{requestparameters/parameter[name='passwordReference']/value}"/>
						</p>
					</div>
				</div>
				
				<div class="loginFormAround">
					<div class="loginFormLeft">
						<label for="phoneHome"><xsl:value-of select="$register.phonehome" />:</label>
					</div>
					<div class="loginFormMiddle">
						<p>
							<input type="text" name="phoneHome" id="phoneHome" value="{requestparameters/parameter[name='phoneHome']/value}"/>
						</p>
					</div>
				</div>
				
				<div class="loginFormAround">
					<div class="loginFormLeft">
						<label for="phoneMobile"><xsl:value-of select="$register.phonemobile" />:</label>
					</div>
					<div class="loginFormMiddle">
						<p>
							<input type="text" name="phoneMobile" id="phoneMobile" value="{requestparameters/parameter[name='phoneMobile']/value}"/>
						</p>
					</div>
				</div>	
				
				<div class="loginFormAround">
					<div class="loginFormLeft">
						<label for="phoneWork"><xsl:value-of select="$register.phonework" />:</label>
					</div>
					<div class="loginFormMiddle">
						<p>
							<input type="text" name="phoneWork" id="phoneWork" value="{requestparameters/parameter[name='phoneWork']/value}"/>
						</p>
					</div>
				</div>					
			</div>
			<div style="clear: both" />
			<div class="floatright" >
				<input type="submit" value="{$register.submit}"/>
			</div>			
		</form>									
	</xsl:template>		
	
	<xsl:template match="validationError">
		<xsl:if test="fieldName and validationErrorType and not(messageKey)">
			<p class="error">
				<xsl:choose>
					<xsl:when test="validationErrorType='RequiredField'">
						<xsl:value-of select="$validationError.RequiredField" />
					</xsl:when>
					<xsl:when test="validationErrorType='InvalidFormat'">
						<xsl:value-of select="$validationError.InvalidFormat" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooShort'">
						<xsl:value-of select="$validationError.TooShort" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooLong'">
						<xsl:value-of select="$validationError.TooLong" />
					</xsl:when>														
					<xsl:otherwise>
						<xsl:value-of select="$validationError.unknownValidationErrorType" />
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:choose>
					<xsl:when test="fieldName = 'firstname'">
						<xsl:value-of select="$validationError.field.firstname"/>!
					</xsl:when>
					<xsl:when test="fieldName = 'lastname'">
						<xsl:value-of select="$validationError.field.lastname"/>!
					</xsl:when>	
					<xsl:when test="fieldName = 'password'">
						<xsl:value-of select="$validationError.field.password"/>!
					</xsl:when>																																																				
					<xsl:otherwise>
						<xsl:value-of select="fieldName"/>!
					</xsl:otherwise>
				</xsl:choose>			
			</p>
		</xsl:if>
		
		<xsl:if test="messageKey">
			<p class="error">
				<xsl:choose>
					<xsl:when test="messageKey='license'">
						<xsl:value-of select="$validationError.messageKey.license" />!
					</xsl:when>					
					<xsl:when test="messageKey='passwordsDontMatch'">
						<xsl:value-of select="$validationError.messageKey.passwordsDontMatch" />!
					</xsl:when>										
					<xsl:otherwise>
						<xsl:value-of select="$validationError.unknownMessageKey"/>!
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
	</xsl:template>						
</xsl:stylesheet>