<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:template match="document">
		<script type="text/javascript" language="Javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/confirmDelete.js" />
	
		<div class="contentitem holderWide2">
			<div class="normal">
				<h1>	
					<xsl:value-of select="/document/module/name" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="$document.header" />:<xsl:text>&#x20;</xsl:text><xsl:value-of select="/document/group/name"/>
				</h1>
			</div>	
			<xsl:apply-templates select="linkModule" />
			<xsl:apply-templates select="updateLink"/>
			<xsl:apply-templates select="addGroupLink"/>
			<xsl:apply-templates select="addSchoolLink"/>
			<xsl:apply-templates select="addGlobalLink"/>
			<xsl:apply-templates select="replaceLineBreak"/>
		</div>
	</xsl:template>	
		
	<xsl:template match="linkModule">
		<xsl:if test="noGroupLinks">
			<div class="divNormal">
				<h1 class="normalTableHeading" colspan="4" align="left">
					<xsl:value-of select="$linksmodule.nolinks1" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="/document/group/name"/>
				</h1>
				<p><xsl:value-of select="$linksmodule.nolinks2" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="/document/group/name"/></p>
			</div>
			
			<xsl:if test="admin">
				<div class="addLinks">
					<xsl:call-template name="grouplinkRef" />
				</div>
			</xsl:if>
		</xsl:if>
		<xsl:if test="noSchoolLinks">
			<div class="divNormal">
				<h1 class="normalTableHeading" colspan="4" align="left">
					<xsl:value-of select="$linksmodule.nolinks1" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="/document/group/school/name"/>
				</h1>
				<p><xsl:value-of select="$linksmodule.nolinks2" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="/document/group/school/name"/></p>
			</div>
			
			<xsl:if test="admin">
				<div class="addLinks">
					<xsl:call-template name="schoollinkRef" />
				</div>
			</xsl:if>
		</xsl:if>
		<xsl:if test="noGlobalLinks">
			<div class="divNormal">
				<h1 class="normalTableHeading" colspan="4" align="left">
					<xsl:value-of select="$linksmodule.nomunicipalitylinks1" />
				</h1>
				<p><xsl:value-of select="$linksmodule.nomunicipalitylinks2" /></p>
			</div>
			
			<xsl:if test="globalAdmin">
				<div class="addLinks">
					<xsl:call-template name="globallinkRef" />
				</div>
			</xsl:if>
		</xsl:if>
		<xsl:if test="groupLinks">
			<xsl:apply-templates select="groupLinks" />
		</xsl:if>
		<xsl:if test="schoolLinks">
			<xsl:apply-templates select="schoolLinks" />
		</xsl:if>
		<xsl:if test="globalLinks">
			<xsl:apply-templates select="globalLinks" />
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="grouplinkRef">
		<div class="addLinks">
			<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addGroupLink">
				<xsl:value-of select="$grouplinkRef.add" />
			</a>	
		</div>
	</xsl:template>
	
	<xsl:template name="schoollinkRef">
	<div class="addLinks">
		<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addSchoolLink">
			<xsl:value-of select="$schoollinkRef.add" />
		</a>
	</div>
	</xsl:template>
	
	<xsl:template name="globallinkRef">
	<div class="addLinks">	
		<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addGlobalLink">
			<xsl:value-of select="$globallinkRef.add" />
		</a>	
	</div>
	</xsl:template>
	
	<xsl:template match="groupLinks">
		<div class="noBorderDiv">
			<div class="divNormal">
				<h1 class="normalTableHeading" colspan="4" align="left"><xsl:value-of select="/document/group/name"/></h1>
				<xsl:apply-templates select="link" />
			
				<xsl:if test="not(link)">
					<p><xsl:value-of select="$groupLinks.nolinks" /></p>
				</xsl:if>
			</div>
			<xsl:if test="../admin">
				<div>
					<xsl:call-template name="grouplinkRef" />
				</div>
			</xsl:if>
		</div>
	</xsl:template>
	
	<xsl:template match="schoolLinks">
		<div class="noBorderDiv">
			<div class="divNormal">
				<h1 class="normalTableHeading" colspan="4" align="left"><xsl:value-of select="$schoolLinks.header" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="../schoolName"/></h1>
				<xsl:apply-templates select="link" />
			
				<xsl:if test="not(link)">
					<p><xsl:value-of select="$schoolLinks.nolinks" /></p>
				</xsl:if>
			</div>
			<xsl:if test="../admin">
				<div>
					<xsl:call-template name="schoollinkRef" />
				</div>
			</xsl:if>
		</div>
	</xsl:template>
	
	<xsl:template match="globalLinks">
		<div class="noBorderDiv">
			<div class="divNormal">
				<h1 class="normalTableHeading" colspan="4" align="left"><xsl:value-of select="$globalLinks.header" /></h1>
				<xsl:apply-templates select="link" />
				
				<xsl:if test="not(link)">
					<p><xsl:value-of select="$globalLinks.nolinks" /></p>
				</xsl:if>
			</div>
			<xsl:if test="../globalAdmin">
				<div>
					<xsl:call-template name="globallinkRef" />
				</div>
			</xsl:if>
		</div>
	</xsl:template>
	
	<xsl:template match="link">
		<div class="tableCalendar">
			<div style="float: left; width: 90%;  margin-bottom: 5px;">
				<xsl:call-template name="replaceLineBreak">
					<xsl:with-param name="string" select="description"/>
					<!-- <xsl:value-of select="description"/>-->
				</xsl:call-template>
				<xsl:text>&#160;</xsl:text>
				<xsl:if test="postedInMillis > ../../userLastLoginInMillis">
					<img style="vertical-align: bottom" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/new.png" />
				</xsl:if>
			</div>
			<xsl:if test="../../admin">
				<div style="width: 10%; float: right">
					<div style="float: right; margin-left: 3px;">
						<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/deleteLink/{linkID}" onclick="return confirmDelete('{$links.delete.confirm} &quot;{url}&quot;?')">
							<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" title="{$links.delete.title}" />
						</a>	
					</div>
					<div style="float: right;">
						<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/updateLink/{linkID}" >
							<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png" title="{$links.update.title}"/>
						</a>	
					</div>
				</div>
			</xsl:if>
			<div class="tableCalendar">
				<a target="blank">
					<xsl:attribute name="href">	
						<xsl:value-of select="url"/>
					</xsl:attribute>
					<xsl:choose>
						<xsl:when test="string-length(url)>60">
							<xsl:value-of select="substring(url,1,60)"/>...
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="url"/>
						</xsl:otherwise>
					</xsl:choose>
				</a>		
			</div>
			<div class="tableCalendarGrey">
				<p><xsl:value-of select="$link.postedBy" />: 
				<xsl:choose>
					<xsl:when test="postedBy/user">
						<xsl:apply-templates select="postedBy/user/firstname" /><xsl:text>&#160;</xsl:text><xsl:apply-templates select="postedBy/user/lastname" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$link.postedBy" />
					</xsl:otherwise>
				</xsl:choose>
				,<xsl:text>&#160;</xsl:text><xsl:value-of select="posted" /></p>
			</div>
		</div>
	</xsl:template>
	<xsl:template match="postedBy">
		<xsl:value-of select="username" />
	</xsl:template>

	<xsl:template match="updateLink">
		<form method="POST" action="{/document/requestinfo/uri}" ACCEPT-CHARSET="ISO-8859-1">
			<div class="divNormal">
				<h1 class="normalTableHeading">
					<xsl:value-of select="$updateLink.header" /><xsl:text>&#160;</xsl:text>"<xsl:value-of select="substring(link/description,1, 30)"/>"
				</h1>
				
				<xsl:apply-templates select="validationException/validationError"/>
				
				<div>
					<div class="bigmargintop"><xsl:value-of select="$link.address" />:</div>
					<div>
						<input type="text" name="url" size="60">							
							<xsl:attribute name="value">
								<xsl:choose>
									<xsl:when test="requestparameters">
										<xsl:value-of select="requestparameters/parameter[name='url']/value"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="link/url"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:attribute>							
						</input>
					</div>
				</div>
				<div>
					<div class="bigmargintop"><xsl:value-of select="$link.description" />:</div>
					<div>
						<textarea rows="5" name="description" cols="48">							
							<xsl:choose>
								<xsl:when test="requestparameters">
									<xsl:call-template name="replace-string">
										<xsl:with-param name="text" select="requestparameters/parameter[name='description']/value"/>
										<xsl:with-param name="from" select="'&#13;'"/>
										<xsl:with-param name="to" select="''"/>
									</xsl:call-template>									
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="replace-string">
										<xsl:with-param name="text" select="link/description"/>
										<xsl:with-param name="from" select="'&#13;'"/>
										<xsl:with-param name="to" select="''"/>
									</xsl:call-template>									
								</xsl:otherwise>
							</xsl:choose>							
						</textarea>
					</div>
				</div>
				<div class="floatleft bigmargintop" style="text-align:right; width: 36.5em;">
						<input type="submit" value="{$updateLink.submit}"/>
				</div>									
			</div>
		</form>
	</xsl:template>
	
	<xsl:template match="addGroupLink">
		<form method="POST" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addGroupLink" ACCEPT-CHARSET="ISO-8859-1">
			<div class="divNormal">
				<h1 class="normalTableHeading"><xsl:value-of select="$addGroupLink.header" /><xsl:text>&#x20;</xsl:text>
					<xsl:value-of select="/document/group/name"/>
				</h1>
				
				<xsl:apply-templates select="validationException/validationError"/>
				
				<div>
					<div class="bigmargintop"><xsl:value-of select="$link.address" />:</div>
					<div>
						<input type="text" name="url" size="60" value="{requestparameters/parameter[name='url']/value}" />
					</div>
				</div>
				<div>
					<div class="bigmargintop"><xsl:value-of select="$link.description" />:</div>
					<div>
						<textarea rows="5" name="description" cols="48">
							<xsl:call-template name="replace-string">
								<xsl:with-param name="text" select="requestparameters/parameter[name='description']/value"/>
								<xsl:with-param name="from" select="'&#13;'"/>
								<xsl:with-param name="to" select="''"/>
							</xsl:call-template>
						</textarea>
					</div>
				</div>
				<div class="floatleft bigmargintop" style="text-align:right; width: 36.5em;">
					<input type="submit" value="{$link.submit}"/>
				</div>									
			</div>
		</form>
	</xsl:template>
	
	<xsl:template match="addSchoolLink">
		<form method="POST" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addSchoolLink" ACCEPT-CHARSET="ISO-8859-1">
			<div class="divNormal">
				<h1 class="normalTableHeading"><xsl:value-of select="$addSchoolLink.header" /><xsl:text>&#x20;</xsl:text>
					<xsl:value-of select="/document/group/school/name"/>
				</h1>
				
				<xsl:apply-templates select="validationException/validationError"/>
				
				<div>
					<div class="bigmargintop"><xsl:value-of select="$link.address" />:</div>
					<div>
						<input type="text" name="url" size="60" value="{requestparameters/parameter[name='url']/value}" />
					</div>
				</div>
				<div>
					<div class="bigmargintop"><xsl:value-of select="$link.description" />:</div>
					<div>
						<textarea rows="5" name="description" cols="48">
							<xsl:call-template name="replace-string">
								<xsl:with-param name="text" select="requestparameters/parameter[name='description']/value"/>
								<xsl:with-param name="from" select="'&#13;'"/>
								<xsl:with-param name="to" select="''"/>
							</xsl:call-template>
						</textarea>
					</div>
				</div>
				<div class="floatleft bigmargintop" style="text-align:right; width: 36.5em;">
					<input type="submit" value="{$link.submit}"/>
				</div>									
			</div>
		</form>
	</xsl:template>
	
	<xsl:template match="addGlobalLink">
		<form method="POST" action ="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addGlobalLink" ACCEPT-CHARSET="ISO-8859-1">
			<div class="divNormal">
				<h1 class="normalTableHeading"><xsl:value-of select="$addGlobalLink.header" /></h1>

				<xsl:apply-templates select="validationException/validationError"/>

				<div>
					<div class="bigmargintop"><xsl:value-of select="$link.address" />:</div>
					<div>
						<input type="text" name="url" size="60" value="{requestparameters/parameter[name='url']/value}"/>
					</div>
				</div>
				<div>
					<div class="bigmargintop"><xsl:value-of select="$link.description" />:</div>
					<div>
						<textarea rows="5" name="description" cols="48">
							<xsl:call-template name="replace-string">
								<xsl:with-param name="text" select="requestparameters/parameter[name='description']/value"/>
								<xsl:with-param name="from" select="'&#13;'"/>
								<xsl:with-param name="to" select="''"/>
							</xsl:call-template>
						</textarea>
					</div>
				</div>
				<div class="floatleft bigmargintop" style="text-align:right; width: 36.5em;">
					<input type="submit" value="{$link.submit}"/>
				</div>									
			</div>
		</form>
	</xsl:template>
	
	
	<xsl:template match="validationError">
		<p class="error_wrong_input">		
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
				<xsl:when test="fieldName = 'description'">
					<xsl:value-of select="$validationError.field.description" />!
				</xsl:when>
				<xsl:when test="fieldName = 'url'">
					<xsl:value-of select="$validationError.field.url" />!
				</xsl:when>																																						
				<xsl:otherwise>
					<xsl:value-of select="fieldName"/>
				</xsl:otherwise>
			</xsl:choose>				
		</p>
	
	</xsl:template>
	
	<xsl:template name="replaceLineBreak">
	    <xsl:param name="string"/>
	    <xsl:choose>
	        <xsl:when test="contains($string,'&#13;')">
	            <xsl:value-of select="substring-before($string,'&#13;')"/>
	            <br/>
	            <xsl:call-template name="replaceLineBreak">
	                <xsl:with-param name="string" select="substring-after($string,'&#13;')"/>
	            </xsl:call-template>
	        </xsl:when>
	        <xsl:otherwise>
	            <xsl:value-of select="$string"/>
	        </xsl:otherwise>
	    </xsl:choose>
	</xsl:template>
	
	<xsl:template name="replace-string">

           <xsl:param name="text" />
           <xsl:param name="from" />
           <xsl:param name="to" />

           <xsl:choose>
                 <xsl:when test="contains($text, $from)">
                       <xsl:variable name="before" select="substring-before($text, $from)" />
                       <xsl:variable name="after" select="substring-after($text, $from)" />
                       <xsl:variable name="prefix" select="concat($before, $to)" />
                       
                       <xsl:value-of select="$before" />
                       <xsl:value-of select="$to" />

                       <xsl:call-template name="replace-string">
                             <xsl:with-param name="text" select="$after" />
                             <xsl:with-param name="from" select="$from" />
                             <xsl:with-param name="to" select="$to" />
                       </xsl:call-template>
                 </xsl:when>

                 <xsl:otherwise>
                       <xsl:value-of select="$text" />
                 </xsl:otherwise>
           </xsl:choose>
     </xsl:template>	
</xsl:stylesheet>