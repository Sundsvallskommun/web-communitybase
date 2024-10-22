<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>
	
	<xsl:template match="document">
		<script type="text/javascript" language="Javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/confirmDelete.js" />	
		<div class="contentitem">
			<xsl:apply-templates select="fileArchiveModule" />
			<xsl:apply-templates select="addGroupSection" />
			<xsl:apply-templates select="updateGroupSection" />
			<xsl:apply-templates select="addGroupFile" />
			<xsl:apply-templates select="updateGroupFile"/>
			<xsl:apply-templates select="updateSchoolFile"/>
			<xsl:apply-templates select="addSchoolFile"/>
			<xsl:apply-templates select="addSchoolSection"/>
			<xsl:apply-templates select="updateSchoolSection"/>
		</div>
	</xsl:template>
	
	<xsl:template match="fileArchiveModule">
		
		<h1><xsl:value-of select="/document/module/name" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$fileArchiveModule.header" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="/document/group/name" /></h1>
			
		<p class="info">
			<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/new.png" /><xsl:text>&#160;=&#160;</xsl:text><xsl:value-of select="$fileArchiveModule.newfile.description" />.
		</p>
		
		<xsl:call-template name="groupFiles" />
		
		<xsl:call-template name="schoolFiles" />
		
	</xsl:template>
	
	<xsl:template name="groupFiles">
		
		<div class="content-box">
			<h1 class="header"><xsl:value-of select="$groupFiles.header" /><xsl:text>&#160;</xsl:text><xsl:value-of select="/document/group/name"/></h1>
			
			<div class="content">
			
				<xsl:choose>
					<xsl:when test="groupFiles/section">
						<xsl:apply-templates select="groupFiles/section" />
					</xsl:when>
					<xsl:otherwise>
						<p>(<xsl:value-of select="$groupFiles.help" />)</p>
					</xsl:otherwise>
				</xsl:choose>
			
			</div>
			
		</div>
		
		<xsl:if test="isGroupAdmin or isSchoolAdmin">
			<div class="floatleft full bigmarginbottom">
				<div class="text-align-right">
					<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addGroupSection">
						<xsl:value-of select="$groupFiles.addcategory" />
					</a>	
				</div>
			</div>
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template name="schoolFiles">
		
		<div class="content-box">
			<h1 class="header"><xsl:value-of select="$schoolFiles.header" /><xsl:text>&#160;</xsl:text><xsl:value-of select="/document/group/school/name"/></h1>
			
			<div class="content">
			
				<xsl:choose>
					<xsl:when test="schoolFiles/section">
						<xsl:apply-templates select="schoolFiles/section" />
					</xsl:when>
					<xsl:otherwise>
						<p>(<xsl:value-of select="$schoolFiles.help" />)</p>
					</xsl:otherwise>
				</xsl:choose>
			
			</div>
			
		</div>
		
		<xsl:if test="isSchoolAdmin">
			<div class="floatleft full">
				<div class="text-align-right">
					<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addSchoolSection">
						<xsl:value-of select="$schoolFiles.addcategory" />
					</a>
				</div>
			</div>
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template match="section">
		
		<div class="floatleft full bigmarginbottom">
			
			<div class="floatleft full marginbottom" >
			
				<div class="floatleft full">
					<h3 class="floatleft ninety">
						<xsl:value-of select="sectionName"/>
					</h3>
					
					<xsl:if test="(sectionType = 'Group' and (../../isGroupAdmin or ../../isSchoolAdmin)) or (sectionType = 'School' and ../../isSchoolAdmin)">
						<h3 class="floatright ten">
							<div class="floatright marginleft">
								<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/delete{sectionType}Section/{sectionID}" onclick="return confirmDelete('{$section.delete.confirm} {sectionName}?')">
									<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png"/>
								</a>
							</div>	
							<div class="floatright">
								<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/update{sectionType}Section/{sectionID}">
									<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png"  />
								</a>
							</div>
						</h3>
					</xsl:if>

				</div>
				
			</div>
			
			<xsl:choose>
				<xsl:when test="file">
					<xsl:apply-templates select="file" />
				</xsl:when>
				<xsl:otherwise>
					<div class="floatleft full">
						<p class="margintop"><xsl:value-of select="$section.nofiles" /></p>
					</div>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:if test="(sectionType = 'Group' and (../../isGroupAdmin or ../../isSchoolAdmin)) or (sectionType = 'School' and ../../isSchoolAdmin)">
				<div class="floatleft full">
					<div class="text-align-right">
						<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/add{sectionType}File/{sectionID}">
							<xsl:value-of select="$section.addfile" />
						</a>	
					</div>
				</div>
			</xsl:if>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="file">
		
		<div class="floatleft full margintop marginbottom hover">
			
			<div class="floatleft">
				
				<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/download{../sectionType}File/{fileID}">
					<xsl:value-of select="filename"/>
				</a>
				
				<xsl:text>&#160;</xsl:text>
				
				<xsl:if test="postedInMillis > ../../../userLastLoginInMillis">
					<img style="vertical-align: bottom" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/new.png" />
				</xsl:if>
				
			</div>
			
			<div class="floatright ten">
			
				<xsl:if test="(../sectionType = 'Group' and (../../../isGroupAdmin or ../../../isSchoolAdmin)) or (../sectionType = 'School' and ../../../isSchoolAdmin)">
					<div class="floatright margintop marginleft">
						<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/delete{../sectionType}File/{fileID}" onclick="return confirmDelete('{$file.delete.confirm} {filename}?')">
							<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" title="{$file.delete.title}"/>
						</a>	
					</div>
					<div class="floatright margintop">
						<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/update{../sectionType}File/{fileID}" >
							<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png" title="{$file.edit.title}"/>
						</a>	
					</div>
				</xsl:if>
				
			</div>
			
			<div class="floatleft ninety">
				
				<p class="nomargin">
					<xsl:call-template name="replaceLineBreak">
						<xsl:with-param name="string" select="description"/>
					</xsl:call-template>
				</p>
				
			</div>
			
			<div class="floatleft ninety marginbottom">
				
				<p class="addedBy">
					<xsl:value-of select="$file.addedby" />: 
					<xsl:choose>
						<xsl:when test="postedBy/user">
							<xsl:value-of select="postedBy/user/firstname"/><xsl:text>&#160;</xsl:text><xsl:value-of select="postedBy/user/lastname"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$file.userdeleted" />
						</xsl:otherwise>
					</xsl:choose>
					<xsl:text>,&#160;</xsl:text><xsl:value-of select="postedBy/postedDate"/><xsl:text>&#160;</xsl:text>
					<xsl:value-of select="$file.time" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="postedBy/postedTime"/>
		 		</p>
		 		
			</div>
			
		</div>
	</xsl:template>
	
	<xsl:template match="postedBy">
		<xsl:value-of select="username" />
	</xsl:template>
		
	<xsl:template match="addGroupSection">
		
		<h1>
			<xsl:value-of select="/document/module/name"/><xsl:text>&#160;</xsl:text><xsl:value-of select="$fileArchiveModule.header" />:  
			<xsl:value-of select="/document/group/name"/>
		</h1>		
		
		<form method="post" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addGroupSection" ACCEPT-CHARSET="ISO-8859-1">
			
			<div class="content-box">
				<h1 class="header"><xsl:value-of select="$addSection.header" /></h1>								
				
				<div class="content">
				
					<xsl:apply-templates select="validationException/validationError" />
					<p>
						<label for="sectioname"><xsl:value-of select="$addSection.name" />:</label>
						<br />
						<input type="text" id="sectioname" name="sectionname" size="72" value="{requestparameters/parameter[name='sectioname']/value}" />
					</p>
					<div class="text-align-right">
						<input type="submit" value="{$addGroupSection.submit}"/>
					</div>
				
				</div>
				
			</div>
			
		</form>	
	</xsl:template>
	
	<xsl:template match="updateGroupSection">
		
		<h1>
			<xsl:value-of select="/document/module/name"/><xsl:text>&#160;</xsl:text><xsl:value-of select="$fileArchiveModule.header" />:  
			<xsl:value-of select="/document/group/name"/>
		</h1>
		
		<form method="post" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/updateGroupSection/{section/sectionID}" ACCEPT-CHARSET="ISO-8859-1">
			
			<div class="content-box">
				<h1 class="header">
					<xsl:value-of select="$updateGroupSection.header" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="section/sectionName"/>
				</h1>
				<div class="content">
					
					<p>
						<label for="sectioname"><xsl:value-of select="$updateSection.name" />:</label>
						<br/>
						<input type="text" id="sectioname" name="sectionname" size="72" value="{section/sectionName}"/>
					</p>
					
					<div class="text-align-right">
						<input type="submit" value="{$updateSection.submit}"/>
					</div>
				
				</div>
				
			</div>
			
		</form>
	</xsl:template>
	
	<xsl:template match="addGroupFile">
		
		<h1>
			<xsl:value-of select="/document/module/name"/><xsl:text>&#160;</xsl:text><xsl:value-of select="$fileArchiveModule.header" />: 
			<xsl:value-of select="/document/group/name"/>
		</h1>
		
		<div class="content">

			<form method="POST" ACCEPT-CHARSET="ISO-8859-1" ENCTYPE="multipart/form-data" action="{/document/requestinfo/uri}">
				
				<div class="content-box">
					
					<h1 class="header">
						<b><xsl:value-of select="$addFile.header" /></b>
					</h1>
					
					<div class="content">
						
						<xsl:apply-templates select="validationException/validationError" />
					
						<p>
							<label for="file"><xsl:value-of select="$addFile.file" />:</label>
							<br/>
							<input type="file" size="55" id="file" name="file" />
						</p>
					
						<div class="floatleft full marginbottom">
							<p class="info">
								<xsl:if test="allowedFileTypes">
									<xsl:value-of select="$addFile.allowedfiles" />: 
									<xsl:for-each select = "allowedFileTypes/extension">
										<xsl:value-of select="." />, <xsl:text></xsl:text>
									</xsl:for-each>
								</xsl:if>
							</p>
							<p class="info">(<xsl:value-of select="$addFile.maximumsize" /><xsl:text>&#160;</xsl:text><xsl:value-of select="diskThreshold" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$addFile.mb" />)</p>
						</div>
						
						<p>
							<label for="description"><xsl:value-of select="$addFile.description" />:</label>
							<br/>
							<textarea id="description" rows="4" cols="65" name="description">
								<xsl:call-template name="replace-string">
									<xsl:with-param name="text" select="requestparameters/parameter[name='description']/value"/>
									<xsl:with-param name="from" select="'&#13;'"/>
									<xsl:with-param name="to" select="''"/>
								</xsl:call-template>
							</textarea>
						</p>
						
						<div class="text-align-right">
							<input type="submit" value="{$addFile.submit}" />
						</div>
					
					</div>
					
				</div>
				
			</form>
		
		</div>
		
	</xsl:template>
	
	<xsl:template match="updateGroupFile">
		
		<h1>
			<xsl:value-of select="/document/module/name"/><xsl:text>&#160;</xsl:text><xsl:value-of select="$fileArchiveModule.header" />: 
			<xsl:value-of select="/document/group/name"/>
		</h1>
			
		<form method="POST" ACCEPT-CHARSET="ISO-8859-1"  action="{/document/requestinfo/uri}">
			
			<div class="content-box">
				<h1 class="header"><xsl:value-of select="$updateFile.header" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="file/filename"/></h1>
				
				<div class="content">
				
					<p>
						<xsl:value-of select="$updateFile.name" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="file/filename"/>
					</p>
				
					<p>
						<label for="description"><xsl:value-of select="$updateFile.description" />:</label>
						<br/>
						<textarea rows="4" cols="65" id="description" name="description">
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
										<xsl:with-param name="text" select="file/description"/>
										<xsl:with-param name="from" select="'&#13;'"/>
										<xsl:with-param name="to" select="''"/>
									</xsl:call-template>									
								</xsl:otherwise>
							</xsl:choose>
						</textarea>
					</p>
					
					<p>
						<label for="sectionID"><xsl:value-of select="$updateFile.category" />:</label>
						<br/>
						<select id="sectionID" name="sectionID">
							<xsl:for-each select="section" >
								<option value="{sectionID}">
									<xsl:if test="../file/sectionID = sectionID">
										<xsl:attribute name="selected" />
									</xsl:if>
									<xsl:value-of select="sectionName" />
								</option>
							</xsl:for-each>
						</select>
					</p>
					
					<div class="full floatleft" style="text-align: right;">
						<input type="submit" value="{$updateFile.submit}" />
					</div>
				
				</div>
				
			</div>
		</form>
		
	</xsl:template>
	
	<xsl:template match="updateSchoolFile">

		<h1>
			<xsl:value-of select="/document/module/name"/><xsl:text>&#160;</xsl:text><xsl:value-of select="$fileArchiveModule.header" />: 
			<xsl:value-of select="/document/group/name"/>
		</h1>
		
		<form method="POST" ACCEPT-CHARSET="ISO-8859-1"  action="{/document/requestinfo/uri}">
			
			<div class="content-box">
				
				<h1 class="header"><xsl:value-of select="$updateFile.header" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="file/filename"/></h1>

				<div class="content">	
				
					<p>
						<xsl:value-of select="$updateFile.name" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="file/filename"/>
					</p>
					
					<p>
						<label for="description"><xsl:value-of select="$updateFile.description" />:</label>
						<br/>
						<textarea rows="4" cols="65" id="description" name="description">
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
										<xsl:with-param name="text" select="file/description"/>
										<xsl:with-param name="from" select="'&#13;'"/>
										<xsl:with-param name="to" select="''"/>
									</xsl:call-template>									
								</xsl:otherwise>
							</xsl:choose>
						</textarea>
					</p>
	
					<p>
						<label for="sectionID"><xsl:value-of select="$updateFile.category" />:</label>
						<br/>
						<select id="sectionID" name="sectionID">
							<xsl:for-each select="section" >
								<option value="{sectionID}">
									<xsl:if test="../file/sectionID = sectionID">
										<xsl:attribute name="selected" />
									</xsl:if>
									<xsl:value-of select="sectionName" />
								</option>
							</xsl:for-each>
						</select>
					</p>
	
					<div class="text-align-right">
						<input type="submit" value="{$updateFile.submit}" />
					</div>
				
				</div>
				
			</div>
			
		</form>

	</xsl:template>
	
	<xsl:template match="addSchoolFile">
		
		<h1>
			<xsl:value-of select="/document/module/name"/><xsl:text>&#160;</xsl:text><xsl:value-of select="$fileArchiveModule.header" />: 
			<xsl:value-of select="/document/group/name"/>
		</h1>
			
		<form method="POST" ACCEPT-CHARSET="ISO-8859-1" ENCTYPE="multipart/form-data" action="{/document/requestinfo/uri}">
			
			<div class="content-box">
				
				<h1 class="header"><xsl:value-of select="$addFile.header" /></h1>

				<div class="content">

					<xsl:apply-templates select="validationException/validationError" />
				
					<p>
						<label for="file"><xsl:value-of select="$addFile.file" />:</label>
						<br/>
						<input type="file" size="55" id="file" name="file" />
					</p>
					
					<div class="floatleft full marginbottom">
						<p class="info">
							<xsl:if test="allowedFileTypes">
								<xsl:value-of select="$addFile.allowedfiles" />: 
								<xsl:for-each select = "allowedFileTypes/extension">
									<xsl:value-of select="." />, <xsl:text></xsl:text>
								</xsl:for-each>
							</xsl:if>
						</p>
						<p class="info">(<xsl:value-of select="$addFile.maximumsize" /><xsl:text>&#160;</xsl:text><xsl:value-of select="diskThreshold" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$addFile.mb" />)</p>
					</div>
					
					<p>
						<label for="description"><xsl:value-of select="$addFile.description" />:</label>
						<br/>
						<textarea id="description" rows="4" cols="65" name="description">
							<xsl:call-template name="replace-string">
								<xsl:with-param name="text" select="requestparameters/parameter[name='description']/value"/>
								<xsl:with-param name="from" select="'&#13;'"/>
								<xsl:with-param name="to" select="''"/>
							</xsl:call-template>
						</textarea>
					</p>
					
					<div class="text-align-right">
						<input type="submit" value="{$addFile.submit}" />
					</div>
				
				</div>
				
			</div>
			
		</form>
		
	</xsl:template>
	
	<xsl:template match="addSchoolSection">
		
		<h1>
			<xsl:value-of select="/document/module/name"/><xsl:text>&#160;</xsl:text><xsl:value-of select="$fileArchiveModule.header" />:  
			<xsl:value-of select="/document/group/name"/>
		</h1>
	
		<form method="post" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addSchoolSection" ACCEPT-CHARSET="ISO-8859-1">
			
			<div class="content-box">									
				
				<h1 class="header"><xsl:value-of select="$addSection.header" /></h1>

				<div class="content">

					<xsl:apply-templates select="validationException/validationError" />
					
					<p>
						<label for="sectioname"><xsl:value-of select="$addSection.name" />:</label>
						<br />
						<input type="text" id="sectioname" name="sectionname" size="72" value="{requestparameters/parameter[name='sectioname']/value}" />
					</p>
									
					<div class="text-align-right">
						<input type="submit" value="{$addSchoolSection.submit}"/>
					</div>
				
				</div>
				
			</div>
			
		</form>		
	</xsl:template>
	
	<xsl:template match="updateSchoolSection">
		
		<h1>
			<xsl:value-of select="/document/module/name"/><xsl:text>&#160;</xsl:text><xsl:value-of select="$fileArchiveModule.header" />:  
			<xsl:value-of select="/document/group/name"/>
		</h1>
	
		<form method="post" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/updateSchoolSection/{section/sectionID}" ACCEPT-CHARSET="ISO-8859-1">
			
			<div class="content-box">
				
				<h1 class="header"><xsl:value-of select="$updateSchoolSection.header" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="section/sectionName"/></h1>
	
				<div class="content">
	
					<p>
						<label for="sectioname"><xsl:value-of select="$updateSection.name" />:</label>
						<br/>
						<input type="text" id="sectioname" name="sectionname" size="72" value="{section/sectionName}"/>
					</p>
						
					<div class="text-align-right">
						<input type="submit" value="{$updateSection.submit}"/>
					</div>
					
				</div>
				
			</div>
			
		</form>
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
					<xsl:when test="validationErrorType='TooLong'">
						<xsl:value-of select="$validationError.TooLong" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$validationError.unknownValidationErrorType" />
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:choose>
					<xsl:when test="fieldName = 'sectionname'">
						<xsl:value-of select="$validationError.field.name" />!
					</xsl:when>
					<xsl:when test="fieldName = 'description'">
						<xsl:value-of select="$validationError.field.description" />!
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
					<xsl:when test="messageKey='InvalidFileFormat'">
						<xsl:value-of select="$validationError.messageKey.InvalidFileFormat" />! 
					</xsl:when>
					<xsl:when test="messageKey='NoFileAttached'">
						<xsl:value-of select="$validationError.messageKey.NoFileAttached" />! 
					</xsl:when>
					<xsl:when test="messageKey='FileTooBig'">
						<xsl:value-of select="$validationError.messageKey.FileTooBig" />!
					</xsl:when>
					<xsl:when test="messageKey='FileTooSmall'">
						<xsl:value-of select="$validationError.messageKey.FileTooSmall" />!
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$validationError.unknownMessageKey" />!
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
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