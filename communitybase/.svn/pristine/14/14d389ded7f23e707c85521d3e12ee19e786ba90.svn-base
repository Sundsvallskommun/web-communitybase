<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="scriptPath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/js</xsl:variable>
	<xsl:variable name="imagePath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js?v=1
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/js/mobilephonevalidation.js
	</xsl:variable>

	<xsl:template match="Document">
		
		<div id="MobilePhoneValidationModule" class="contentitem">
			
			<section>
				<header>
					<h1><xsl:value-of select="$i18n.MobilePhoneMissing" /></h1>
				</header>
				
				<form action="{/Document/requestinfo/uri}" method="post">
					<article>
						<xsl:if test="validationException/validationError">
							<div class="validationerrors hidden">
								<xsl:apply-templates select="validationException/validationError" />
							</div>
						</xsl:if>
						
						<div class="alert alert-info">
							<xsl:value-of select="$i18n.ValidationText"/>
						</div>
						
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label for="mobilePhone"><xsl:value-of select="$i18n.MobilePhone"/> (07xxxxxxxx)</label>
									
									<div class="input-group">
										<xsl:call-template name="createTextField">
											<xsl:with-param name="id" select="'mobilePhone'" />
											<xsl:with-param name="name" select="'mobilePhone'" />
											<xsl:with-param name="value" select="user/Attributes/Attribute[Name='mobilePhone']/Value" />
											<xsl:with-param name="class" select="'form-control'"/>
										</xsl:call-template>
										
										<div class="input-group-addon pointer" id="sendcode" data-url="{/Document/requestinfo/currentURI}/{/Document/module/alias}/sendverification" data-invalidnumber="{$i18n.InvalidPhoneNumber}" data-unknown="{$i18n.CouldNotSendSMS}">
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
										<xsl:with-param name="class" select="'form-control'"/>
									</xsl:call-template>
								</div>
							</div>
						</div>
					</article>
			
					<footer class="d-flex">
						<div class="ml-auto">
							<button type="submit" class="btn btn-success"><i class="icons icon-check"></i><span><xsl:value-of select="$i18n.Save" /></span></button>
						</div>
					</footer>
				</form>
			</section>
			
		</div>
		
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