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
		<div class="normal">	
			<h1><xsl:value-of select="/document/module/name" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$fileArchiveModule.header" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="/document/group/name" /></h1>
		</div>	
		<p class="info">
			<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/new.png" /><xsl:text>&#160;=&#160;</xsl:text><xsl:value-of select="$fileArchiveModule.newfile.description" />.
		</p>
		<xsl:choose>
			<xsl:when test="noFiles">
				<div class="divNormal">
					<xsl:value-of select="$fileArchiveModule.nofiles" /><xsl:text>&#160;</xsl:text><xsl:value-of select="groupname"/>
				</div>
				<xsl:if test="admin">
					<div class="addFiles">
						<!-- <xsl:call-template name="fileRef" />-->
					</div>
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="groupFiles">
					<xsl:apply-templates select="groupFiles" />
				</xsl:if>
				<xsl:if test="schoolFiles">
					<xsl:apply-templates select="schoolFiles" />
				</xsl:if>
				<xsl:if test="globalFiles">
					<xsl:apply-templates select="globalFiles" />
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="groupFiles">
		<div class="divNormal">
			<h1 class="normalTableHeading" colspan="4" align="left"><xsl:value-of select="$groupFiles.header" /><xsl:text>&#160;</xsl:text><xsl:value-of select="/document/group/name"/>
			</h1>
			<xsl:apply-templates select="section" />
			<xsl:if test="noSection">
				<div class="noBorderDiv">
					<p>(<xsl:value-of select="$groupFiles.help" />)</p>
				</div>
			</xsl:if>
		</div>
		<xsl:if test="../admin">
			<div class="floatleft full">
				<div class="addFiles">
					<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addGroupSection">
						<xsl:value-of select="$groupFiles.addcategory" />
					</a>	
				</div>
			</div>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="schoolFiles">
		<div class="divNormal">
			<h1 class="normalTableHeading" colspan="4" align="left"><xsl:value-of select="$schoolFiles.header" /><xsl:text>&#160;</xsl:text><xsl:value-of select="/document/group/school/name"/>
			</h1>
			<xsl:apply-templates select="section" />
			<xsl:if test="noSection">
				<div class="noBorderDiv">
					<p>(<xsl:value-of select="$schoolFiles.help" />)</p>
				</div>
			</xsl:if>
		</div>
		<xsl:if test="../admin">
			<div class="floatleft full">
				<div class="addFiles">
					<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addSchoolSection">
						<xsl:value-of select="$schoolFiles.addcategory" />
					</a>	
				</div>
			</div>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="section">
		<div class="tableCalendar">
			<div class="noBorderDiv" >
				<div class="marginTop">
					<xsl:if test="/document/fileArchiveModule/admin = 'true'">
						<xsl:attribute name="style">
							float: left; width: 90%;
						</xsl:attribute>
					</xsl:if>
					<h3><xsl:value-of select="sectionName"/></h3>
				</div>
				<xsl:if test="/document/fileArchiveModule/admin = 'true'">
					<div class="bigmarginTop" style="width: 10%; float: left; padding-top: 3px;">
						<div class="marginTop" style="float: right; margin-left: 3px; ">
							<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/delete{sectionType}Section/{sectionID}" onclick="return confirmDelete('{$section.delete.confirm} {sectionName}?')">
								<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png"/>
							</a>
						</div>	
						<div class="marginTop" style="float: right; ">
							<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/update{sectionType}Section/{sectionID}">
								<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png"  />
							</a>
						</div>
					</div>
				</xsl:if>
			</div>
			<div class="floatleft full">
				<xsl:if test="noFiles">
					<p class="margintop marginleft"><xsl:value-of select="$section.nofiles" /></p>
				</xsl:if>
			</div>
			<xsl:apply-templates select="file" />
		</div>
		
		<xsl:if test="/document/fileArchiveModule/admin = 'true'">
			<div class="floatleft full">
				<div class="addFiles">
					<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/add{sectionType}File/{sectionID}">
						<xsl:value-of select="$section.addfile" />
					</a>	
				</div>
			</div>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="file">
		<div class="tableCalendar">
			<div class="bigmarginleft margintop" style="float: left; width: 90%;  margin-bottom: 5px;">
				<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/download{../sectionType}File/{fileID}">
					<xsl:value-of select="filename"/>
				</a>
				<xsl:text>&#160;</xsl:text>
				<xsl:if test="postedInMillis > ../../../userLastLoginInMillis">
					<img style="vertical-align: bottom" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/new.png" />
				</xsl:if>
			</div>
			<xsl:if test="/document/fileArchiveModule/admin = 'true'">
				<div style="width: 7%; float: right">
					<div class="marginTop" style="float: right; margin-left: 3px;">
						<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/delete{../sectionType}File/{fileID}" onclick="return confirmDelete('{$file.delete.confirm} {filename}?')">
							<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" title="{$file.delete.title}"/>
						</a>	
					</div>
					<div class="marginTop" style="float: right;">
						<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/update{../sectionType}File/{fileID}" >
							<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png" title="{$file.edit.title}"/>
						</a>	
					</div>
				</div>
			</xsl:if>
			<div style="float: left; width: 90%;">
				<p class="bigmarginleft">
					<xsl:call-template name="replaceLineBreak">
						<xsl:with-param name="string" select="description"/>
						<!-- <xsl:value-of select="description"/>-->
					</xsl:call-template>
				</p>
			</div>
			<div class="floatleft bigmarginbottom" style="width: 99.5%">
				<p class="addedByContainer">
					<xsl:value-of select="$file.addedby" />: 
					<xsl:choose>
						<xsl:when test="postedBy/user">
							<xsl:value-of select="postedBy/user/firstname"/><xsl:text>&#160;</xsl:text><xsl:value-of select="postedBy/user/lastname"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$file.userdeleted" />
						</xsl:otherwise>
					</xsl:choose>
					,<xsl:text>&#160;</xsl:text><xsl:value-of select="postedBy/postedDate"/><xsl:text>&#160;</xsl:text>
					<xsl:value-of select="$file.time" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="postedBy/postedTime"/>
		 		</p>
			</div>
		</div>
	</xsl:template>
	
	<xsl:template match="postedBy">
		<xsl:value-of select="username" />
	</xsl:template>
		
	<xsl:template match="addGroupSection">
	<div class="normal">
			<h1>
				<xsl:value-of select="/document/module/name"/><xsl:text>&#160;</xsl:text><xsl:value-of select="$fileArchiveModule.header" />:  
				<xsl:value-of select="/document/group/name"/>
			</h1>
		</div>		
		<form method="post" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addGroupSection" ACCEPT-CHARSET="ISO-8859-1">
			<div class="divNormal">									
				<div class="normalTableHeading floatleft">
					<b colspan="2" align="left"><xsl:value-of select="$addSection.header" /></b>
				</div>
				<div class="noBorderDiv">
					<xsl:apply-templates select="validationException/validationError" />
				</div>
				<div class="noBorderDiv" style="padding: 2px 2px 2px 3px;">
					<div class="floatleft" style="width: 120px;"><xsl:value-of select="$addSection.name" />:</div>
					<div class="floatleft"><input type='text' name='sectionname' size='65' maxlength='65'/></div>
				</div>				
				<div class="noBorderDiv" style="padding: 2px 2px 2px 3px;">
					<div class="floatright">
						<input type="submit" value="{$addGroupSection.submit}"/>
					</div>
				</div>
			</div>
		</form>	
	</xsl:template>
	
	<xsl:template match="updateGroupSection">
		<div class="normal">
			<h1>
				<xsl:value-of select="/document/module/name"/><xsl:text>&#160;</xsl:text><xsl:value-of select="$fileArchiveModule.header" />:  
				<xsl:value-of select="/document/group/name"/>
			</h1>
		</div>		
		<form method="post" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/updateGroupSection/{section/sectionID}" ACCEPT-CHARSET="ISO-8859-1">
			<div class="divNormal">
				<div class="normalTableHeading floatleft">
					<b><xsl:value-of select="$updateGroupSection.header" /></b>
				</div>
				<div class="noBorderDiv" style="padding: 2px 2px 2px 3px;">
					<div class="floatleft" style="width: 120px;">
						<p class="heading"><xsl:value-of select="$updateSection.name" />:</p>
					</div>
					<div class="floatleft">
						<input type='text' name='sectionname' size='65' maxlength='65' value="{section/sectionName}"/>
					</div>
				</div>
				<div class="noBorderDiv" style="padding: 2px 2px 2px 3px;">	
					<div class="floatright">
						<input type="submit" value="{$updateSection.submit}"/>
					</div>
				</div>
			</div>
		</form>
	</xsl:template>
	
	<xsl:template match="addGroupFile">
		<div class="normal">
			<h1>
				<xsl:value-of select="/document/module/name"/><xsl:text>&#160;</xsl:text><xsl:value-of select="$fileArchiveModule.header" />: 
				<xsl:value-of select="/document/group/name"/>
			</h1>
			
			<form method="POST" ACCEPT-CHARSET="ISO-8859-1" ENCTYPE="multipart/form-data" action="{/document/requestinfo/uri}">
				<div class="divNormal">
					<div class="normalTableHeading floatleft">
						<b><xsl:value-of select="$addFile.header" /></b>
					</div>
					<div class="full floatleft">
						<xsl:apply-templates select="validationException/validationError" />
					</div>
					<div class="noBorderDiv">
						<div class="floatleft" style="width: 150px;"><p class="heading"><xsl:value-of select="$addFile.file" />:</p></div>
						<div class="floatleft"><INPUT TYPE="FILE" size="55" NAME="file" /></div>
					</div>
					<!-- TODO fixa utskriften av filformaten -->
					<div class="floatleft full" style="text-align:center;">
						<p class="info" style="color: black">
							<xsl:if test="allowedFileTypes">
								<xsl:value-of select="$addFile.allowedfiles" />: 
								<xsl:for-each select = "allowedFileTypes/extension">
									<xsl:value-of select="." />, <xsl:text></xsl:text>
								</xsl:for-each>
							</xsl:if>
						</p>
						<p class="info">(<xsl:value-of select="$addFile.maximumsize" /><xsl:text>&#160;</xsl:text><xsl:value-of select="diskThreshold" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$addFile.mb" />)</p>
					</div>
					<div class="noBorderDiv">
						<div class="floatleft" style="width: 150px;"><p class="heading"><xsl:value-of select="$addFile.description" />:</p></div>
						<div class="floatleft">
							<textarea rows="4" cols="65" name="description">
								<xsl:call-template name="replace-string">
									<xsl:with-param name="text" select="requestparameters/parameter[name='description']/value"/>
									<xsl:with-param name="from" select="'&#13;'"/>
									<xsl:with-param name="to" select="''"/>
								</xsl:call-template>
							</textarea>
						</div>
					</div>
					<div class="floatleft" style="text-align: right; width: 62em;">
						<input type="submit" value="{$addFile.submit}" />
					</div>
				</div>
			</form>
		</div>
	</xsl:template>
	
	<xsl:template match="updateGroupFile">
		<div class="normal">
			<h1>
				<xsl:value-of select="/document/module/name"/><xsl:text>&#160;</xsl:text><xsl:value-of select="$fileArchiveModule.header" />: 
				<xsl:value-of select="/document/group/name"/>
			</h1>
			<!-- TODO fixa heading och fet text -->
			
			<form method="POST" ACCEPT-CHARSET="ISO-8859-1"  action="{/document/requestinfo/uri}">
				<div class="divNormal">
					<h1 class="normalTableHeading"><xsl:value-of select="$updateFile.header" /></h1>
					<div class="noBorderDiv">
						<div class="full floatleft">
							<div class="floatleft" style="width: 150px; color: #000;">
								<p style="font-weight: bold;"><xsl:value-of select="$updateFile.name" />:</p>
							</div>
							<div class="floatleft">
								<p style="color: #000;"><xsl:value-of select="file/filename"/></p>	
							</div>
						</div>
					</div>
					<div class="noBorderDiv">
						<div class="floatleft" style="width: 150px; color: #000;">
							<p style="font-weight: bold;"><xsl:value-of select="$updateFile.description" /></p>
						</div>
						<div class="floatleft">
							<p style="color: #000;">
								<textarea rows="4" cols="65" name='description'>
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
						</div>
					</div>
					<div class="noBorderDiv">
						<div class="floatleft" style="width: 150px; color: #000;">
							<p style="font-weight: bold;"><xsl:value-of select="$updateFile.category" />:</p>
						</div>
						<div class="floatleft" style="color: #000;">
							<select name='sectionID'>
								<xsl:for-each select="section" >
									<option value="{sectionID}">
										<xsl:if test="../file/sectionID = sectionID">
											<xsl:attribute name="selected" />
										</xsl:if>
										<xsl:value-of select="sectionName" />
									</option>
								</xsl:for-each>
							</select>
						</div>
					</div>
					<div class="full floatleft" style="text-align: right;">
						<input type="submit" value="Ändra fil" />
					</div>
				</div>
			</form>
		</div>
	</xsl:template>
	
	<xsl:template match="updateSchoolFile">
		<div class="normal">
			<h1>
				<xsl:value-of select="/document/module/name"/><xsl:text>&#160;</xsl:text><xsl:value-of select="$fileArchiveModule.header" />: 
				<xsl:value-of select="/document/group/name"/>
			</h1>
			<!-- TODO fixa heading -->
			<form method="POST" ACCEPT-CHARSET="ISO-8859-1"  action="{/document/requestinfo/uri}">
				<div class="divNormal">
					<h1 class="normalTableHeading"><xsl:value-of select="$updateFile.header" />: <xsl:value-of select="file/filename"/></h1>
					<div class="noBorderDiv">
						<div class="full floatleft">
							<div class="floatleft" style="width: 150px; color: #000;">
								<p style="font-weight: bold;"><xsl:value-of select="$updateFile.name" />:</p>
							</div>
							<div class="floatleft">
								<p style="color: #000;"><xsl:value-of select="file/filename"/></p>	
							</div>
						</div>
					</div>
					<div class="noBorderDiv">
						<div class="floatleft" style="width: 150px; color: #000;">
							<p style="font-weight: bold;"><xsl:value-of select="$updateFile.description" />:</p>
						</div>
						<div class="floatleft">
							<p style="color: #000;">
								<textarea rows="4" cols="65" name='description'>
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
						</div>
					</div>
					<div class="noBorderDiv">
						<div class="floatleft" style="width: 150px; color: #000;">
							<p style="font-weight: bold;"><xsl:value-of select="$updateFile.category" />:</p>
						</div>
						<div class="floatleft" style="color: #000;">
							<select name='sectionID'>
								<xsl:for-each select="section" >
									<option value="{sectionID}">
										<xsl:if test="../file/sectionID = sectionID">
											<xsl:attribute name="selected" />
										</xsl:if>
										<xsl:value-of select="sectionName" />
									</option>
								</xsl:for-each>
							</select>
						</div>
					</div>
					<div class="full floatleft" style="text-align: right;">
						<input type="submit" value="{$updateFile.submit}" />
					</div>
				</div>
			</form>
		</div>
	</xsl:template>
	
	<xsl:template match="addSchoolFile">
		<div class="normal">
			<h1>
				<xsl:value-of select="/document/module/name"/><xsl:text>&#160;</xsl:text><xsl:value-of select="$fileArchiveModule.header" />: 
				<xsl:value-of select="/document/group/name"/>
			</h1>
			
			<form method="POST" ACCEPT-CHARSET="ISO-8859-1" ENCTYPE="multipart/form-data" action="{/document/requestinfo/uri}">
				<div class="divNormal">
					<div class="normalTableHeading floatleft">
						<b><xsl:value-of select="$addFile.header" /></b>
					</div>
					<div class="full floatleft">
						<xsl:apply-templates select="validationException/validationError" />
					</div>
					<div class="noBorderDiv">
						<div class="floatleft" style="width: 150px;"><p class="heading"><xsl:value-of select="$addFile.header" />:</p></div>
						<div class="floatleft"><input type="FILE" size="50" NAME="file" /></div>
					</div>
					<div class="floatleft full" style="text-align:center;">
						<p class="tiny" style="color: black">
							<xsl:if test="allowedFileTypes">
								<xsl:value-of select="$addFile.allowedfiles" />: 
								<xsl:for-each select = "allowedFileTypes/extension">
									<xsl:value-of select="." />, <xsl:text></xsl:text>
								</xsl:for-each>
							</xsl:if>
						</p>
						<p class="info">(<xsl:value-of select="$addFile.maximumsize" />)</p>
					</div>
					<div class="noBorderDiv">
						<div class="floatleft" style="width: 150px;"><p class="heading"><xsl:value-of select="$addFile.description" />:	</p></div>
						<div class="floatleft">
							<textarea rows="4" cols="65" name="description">
								<xsl:call-template name="replace-string">
									<xsl:with-param name="text" select="requestparameters/parameter[name='description']/value"/>
									<xsl:with-param name="from" select="'&#13;'"/>
									<xsl:with-param name="to" select="''"/>
								</xsl:call-template>
							</textarea>
						</div>
					</div>
					<div class="floatleft" style="text-align: right; width: 62em;">
						<input type="submit" value="{$addFile.submit}" />
					</div>
				</div>
			</form>
		</div>
	</xsl:template>
	
	<xsl:template match="addSchoolSection">
		<div class="normal">
			<h1>
				<xsl:value-of select="/document/module/name"/><xsl:text>&#160;</xsl:text><xsl:value-of select="$fileArchiveModule.header" />:  
				<xsl:value-of select="/document/group/name"/>
			</h1>
		</div>		
		
		
		<form method="post" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addSchoolSection" ACCEPT-CHARSET="ISO-8859-1">
			<div class="divNormal">									
				<div class="normalTableHeading floatleft">
					<b colspan="2" align="left"><xsl:value-of select="$addSection.header" /></b>
				</div>
				<div class="noBorderDiv">
					<xsl:apply-templates select="validationException/validationError" />
				</div>
				<div class="noBorderDiv" style="padding: 2px 2px 2px 3px;">
					<div class="floatleft" style="width: 120px;"><xsl:value-of select="$addSection.name" />:</div>
					<div class="floatleft"><input type='text' name='sectionname' size='65' maxlength='65'/></div>
				</div>				
				<div class="noBorderDiv" style="padding: 2px 2px 2px 3px;">
					<div class="floatright">
						<input type="submit" value="{$addSchoolSection.submit}"/>
					</div>
				</div>
			</div>
		</form>		
	</xsl:template>
	
	<xsl:template match="updateSchoolSection">
		<div class="normal">
			<h1>
				<xsl:value-of select="/document/module/name"/><xsl:text>&#160;</xsl:text><xsl:value-of select="$fileArchiveModule.header" />:  
				<xsl:value-of select="/document/group/name"/>
			</h1>
		</div>		
		<form method="post" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/updateSchoolSection/{section/sectionID}" ACCEPT-CHARSET="ISO-8859-1">
			<div class="divNormal">
				<div class="normalTableHeading floatleft">
					<b><xsl:value-of select="$updateSchoolSection.header" /></b>
				</div>
				<div class="noBorderDiv" style="padding: 2px 2px 2px 3px;">
					<div class="floatleft" style="width: 120px;">
						<p class="heading"><xsl:value-of select="$updateSection.name" />:</p>
					</div>
					<div class="floatleft">
						<input type='text' name='sectionname' size='65' maxlength='65' value="{section/sectionName}"/>
					</div>
				</div>
					
				<div class="floatright">
					<td colspan="2" align="right"><input type="submit" value="{$updateSection.submit}"/></td>
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