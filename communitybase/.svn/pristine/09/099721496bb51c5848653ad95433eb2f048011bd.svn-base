<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />
	
	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:template match="Document">

		<div id="SectionAccessDeniedHandlerModule" class="contentitem">
			<div class="section-content">
				<xsl:value-of select="Message" disable-output-escaping="yes"/>
				
				<xsl:if test="MemberManagers">
					<h3><xsl:value-of select="$i18n.MemberAdministrators"/></h3>
					
					<xsl:apply-templates select="MemberManagers/user"/>
				</xsl:if>
				
				<xsl:if test="AllowsAccessRequest">
					<h3 class="mt-3"><xsl:value-of select="$i18n.RequestAccess"/></h3>
					
					<xsl:choose>
						<xsl:when test="HasAccessRequest">
							<div class="alert alert-info">
								<xsl:value-of select="$i18n.HasAccessRequest"/>
							</div>
						</xsl:when>
						<xsl:when test="RequestAdded">
							<div class="alert alert-success">
								<xsl:value-of select="$i18n.RequestAdded"/>
							</div>
						</xsl:when>
						<xsl:otherwise>
							<form action="{/Document/requestinfo/contextpath}/{/Document/module/alias}/{SectionID}" method="post">
								<div class="form-group bigmargintop">
									<label for="requestComment"><xsl:value-of select="$i18n.RequestComment"/></label>
									
									<xsl:call-template name="createTextArea">
										<xsl:with-param name="name" select="'requestComment'"/>
										<xsl:with-param name="id" select="'requestComment'"/>
										<xsl:with-param name="maxlength" select="'1024'"/>
										<xsl:with-param name="class">
											<xsl:text>form-control required</xsl:text>
											
											<xsl:if test="EmptyComment or CommentTooLong">
												<xsl:text> has-danger</xsl:text>
											</xsl:if>
										</xsl:with-param>
										<xsl:with-param name="rows" select="'2'"/>
									</xsl:call-template>
									
									<xsl:choose>
										<xsl:when test="EmptyComment">
											<span class="validationerror error marginleft">
												<i class="icons icon-warning error" aria-hidden="true"/>
												
												<span class="italic"><xsl:value-of select="$i18n.RequiredField"/></span>
											</span>
										</xsl:when>
										<xsl:when test="CommentTooLong">
											<span class="validationerror error marginleft">
												<i class="icons icon-warning error" aria-hidden="true"/>
												
												<span class="italic"><xsl:value-of select="$i18n.FieldTooLong"/></span>
											</span>
										</xsl:when>
									</xsl:choose>
								</div>
								
								<div class="text-align-right">
									<button type="submit" class="btn btn-success">
										<i class="icons icon-send" aria-hidden="true"/>
										
										<span><xsl:value-of select="$i18n.Send" /></span>
									</button>
								</div>
							</form>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
			</div>
		</div>

	</xsl:template>

	<xsl:template match="user">

		<div class="clearfix marginbottom">
			<figure class="profile medium floatleft bigmarginright">
				<img alt="{firstname} {lastname}" src="{/Document/requestinfo/contextpath}{/Document/ProfileImageAlias}/{userID}" />
			</figure>
			
			<div>
				<a href="{/Document/requestinfo/contextpath}{/Document/ShowProfileAlias}/{userID}">
					<xsl:value-of select="firstname" /><xsl:text>&#160;</xsl:text><xsl:value-of select="lastname" />
				</a>
				
				<xsl:if test="Attributes/Attribute[Name = 'organization']/Value">
					<ul class="inline">
						<li><xsl:value-of select="Attributes/Attribute[Name = 'organization']/Value"/></li>
					</ul>
				</xsl:if>
			</div>
		</div>

	</xsl:template>

</xsl:stylesheet>