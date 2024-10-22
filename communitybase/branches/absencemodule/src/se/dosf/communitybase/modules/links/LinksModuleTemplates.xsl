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
			<xsl:apply-templates select="addGlobalLink"/>
			<xsl:apply-templates select="addGroupLink"/>
			<xsl:apply-templates select="addSchoolLink"/>
		</div>
	</xsl:template>	
		
	<xsl:template match="linkModule">
	
		<xsl:call-template name="globalLinks" />		
		
		<xsl:call-template name="groupLinks" />

		<xsl:call-template name="schoolLinks" />
		
	</xsl:template>
	
	<xsl:template name="groupLinks">
		
		<div class="content-box">
			<h1 class="header"><xsl:value-of select="/document/group/name"/></h1>
			
			<div class="content">
			
				<xsl:choose>
					<xsl:when test="groupLinks/link">
						<xsl:apply-templates select="groupLinks/link">
							<xsl:with-param name="type" select="'Group'" />
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<p><xsl:value-of select="$groupLinks.nolinks" /></p>
					</xsl:otherwise>
				</xsl:choose>
			
			</div>
			
		</div>
		
		<xsl:if test="isGroupAdmin or isGlobalAdmin or isSchoolAdmin">
			<div class="floatleft full bigmarginbottom">
				<xsl:call-template name="grouplinkRef" />
			</div>
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template name="schoolLinks">
		
		<div class="content-box">
			<h1 class="header"><xsl:value-of select="$schoolLinks.header" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="schoolName"/></h1>
			
			<div class="content">
				
				<xsl:choose>
					<xsl:when test="schoolLinks/link">
						<xsl:apply-templates select="schoolLinks/link">
							<xsl:with-param name="type" select="'School'" />
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<p><xsl:value-of select="$schoolLinks.nolinks" /></p>
					</xsl:otherwise>
				</xsl:choose>
			
			</div>
			
		</div>
		
		<xsl:if test="isGlobalAdmin or isSchoolAdmin">
			<div class="floatleft full bigmarginbottom">
				<xsl:call-template name="schoollinkRef" />
			</div>
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template name="globalLinks">
		
		<div class="content-box">
			<h1 class="header"><xsl:value-of select="$globalLinks.header" /></h1>
			
			<div class="content">
			
				<xsl:choose>
					<xsl:when test="globalLinks/link">
						<xsl:apply-templates select="globalLinks/link">
							<xsl:with-param name="isGlobal" select="'true'" />
							<xsl:with-param name="type" select="'Global'" />
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<p><xsl:value-of select="$globalLinks.nolinks" /></p>
					</xsl:otherwise>
				</xsl:choose>
			
			</div>
			
		</div>
		
		<xsl:if test="isGlobalAdmin">
			<div class="floatleft full bigmarginbottom">
				<xsl:call-template name="globallinkRef" />
			</div>
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template match="link">
		
		<xsl:param name="isGlobal" select="'false'" />
		<xsl:param name="type" />
		
		<div class="floatleft full marginbottom hover">
			
			<div class="floatleft ninety">
				<xsl:call-template name="replaceLineBreak">
					<xsl:with-param name="string" select="description"/>
				</xsl:call-template>
				<xsl:text>&#160;</xsl:text>
				<xsl:if test="postedInMillis > ../../userLastLoginInMillis">
					<img style="vertical-align: bottom" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/new.png" />
				</xsl:if>
			</div>

			<xsl:if test="($type = 'Global' and ../../isGlobalAdmin) or ($type = 'School' and (../../isSchoolAdmin or ../../isGlobalAdmin)) or ($type = 'Group' and (../../isGroupAdmin or ../../isSchoolAdmin or ../../isGlobalAdmin))">
			
				<div class="floatright ten">
					
					<div class="floatright marginleft">
						<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/delete{$type}Link/{linkID}" onclick="return confirmDelete('{$links.delete.confirm} &quot;{url}&quot;?')">
							<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" title="{$links.delete.title}" />
						</a>	
					</div>
					<div class="floatright">
						<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/update{$type}Link/{linkID}" >
							<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png" title="{$links.update.title}"/>
						</a>	
					</div>
					
				</div>
				
			</xsl:if>
			
			<div class="floatleft full">
				
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
			
			<div class="floatleft full">
				
				<p class="addedBy"><xsl:value-of select="$link.postedBy" />: 
				<xsl:choose>
					<xsl:when test="postedBy/user">
						<xsl:apply-templates select="postedBy/user/firstname" /><xsl:text>&#160;</xsl:text><xsl:apply-templates select="postedBy/user/lastname" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$link.postedBy" />
					</xsl:otherwise>
				</xsl:choose>,<xsl:text>&#160;</xsl:text><xsl:value-of select="posted" /></p>
				
			</div>
			
		</div>
		
	</xsl:template>
	
	<xsl:template name="grouplinkRef">
		<div class="text-align-right">
			<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addGroupLink">
				<xsl:value-of select="$grouplinkRef.add" />
			</a>	
		</div>
	</xsl:template>
	
	<xsl:template name="schoollinkRef">
		<div class="text-align-right">
			<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addSchoolLink">
				<xsl:value-of select="$schoollinkRef.add" />
			</a>
		</div>
	</xsl:template>
	
	<xsl:template name="globallinkRef">
		<div class="text-align-right">	
			<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addGlobalLink">
				<xsl:value-of select="$globallinkRef.add" />
			</a>	
		</div>
	</xsl:template>
	
	<xsl:template match="postedBy">
		<xsl:value-of select="username" />
	</xsl:template>

	<xsl:template match="updateLink">
		
		<form method="POST" action="{/document/requestinfo/uri}" ACCEPT-CHARSET="ISO-8859-1">
			
			<div class="content-box">
				
				<h1 class="header">
					<xsl:value-of select="$updateLink.header" /><xsl:text>&#160;</xsl:text>"<xsl:value-of select="substring(link/description,1, 30)"/>"
				</h1>
				
				<div class="content">
				
					<xsl:apply-templates select="validationException/validationError"/>
					
					<p>
						<label for="url"><xsl:value-of select="$link.address" />:</label>
						<br/>
						<input type="text" id="url" name="url" size="72">							
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
					</p>
					
					<p>
						<label for="description"><xsl:value-of select="$link.description" />:</label>
						<br/>
						<textarea rows="5" id="description" name="description" cols="55">							
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
					</p>

					<div class="text-align-right">
						<input type="submit" value="{$updateLink.submit}"/>
					</div>	
					
				</div>
												
			</div>
		</form>
	</xsl:template>
	
	<xsl:template match="addGroupLink">
		
		<form method="POST" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addGroupLink" ACCEPT-CHARSET="ISO-8859-1">
			
			<xsl:call-template name="addLinkForm">
				<xsl:with-param name="header" select="$addGroupLink.header" />
			</xsl:call-template>
			
		</form>
		
	</xsl:template>
	
	<xsl:template match="addSchoolLink">
		
		<form method="POST" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addSchoolLink" ACCEPT-CHARSET="ISO-8859-1">
			
			<xsl:call-template name="addLinkForm">
				<xsl:with-param name="header" select="$addSchoolLink.header" />
			</xsl:call-template>
			
		</form>
		
	</xsl:template>
	
	<xsl:template match="addGlobalLink">
		
		<form method="POST" action ="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addGlobalLink" ACCEPT-CHARSET="ISO-8859-1">
			
			<xsl:call-template name="addLinkForm">
				<xsl:with-param name="header" select="$addGlobalLink.header" />
			</xsl:call-template>
			
		</form>
		
	</xsl:template>
	
	<xsl:template name="addLinkForm">
		
		<xsl:param name="header" />
		
		<div class="content-box">
			<h1 class="header"><xsl:value-of select="$header" /></h1>

			<div class="content">

				<xsl:apply-templates select="validationException/validationError"/>
				
				<p>
					<label for="url"><xsl:value-of select="$link.address" />:</label>
					<br/>
					<input type="text" id="url" name="url" size="72" value="{requestparameters/parameter[name='url']/value}" />
				</p>

				<p>
					<label for="description"><xsl:value-of select="$link.description" />:</label>
					<br/>
					<textarea rows="5" id="description" name="description" cols="55">
						<xsl:call-template name="replace-string">
							<xsl:with-param name="text" select="requestparameters/parameter[name='description']/value"/>
							<xsl:with-param name="from" select="'&#13;'"/>
							<xsl:with-param name="to" select="''"/>
						</xsl:call-template>
					</textarea>
				</p>
				
				<div class="text-align-right">
					<input type="submit" value="{$link.submit}"/>
				</div>	
			
			</div>	
										
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError">
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