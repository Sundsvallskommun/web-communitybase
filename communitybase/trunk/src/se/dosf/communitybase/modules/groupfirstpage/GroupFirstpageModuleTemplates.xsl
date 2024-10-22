<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/dosf/communitybase/utils/ckeditor/CKEditor.xsl" />

	<xsl:variable name="scripts">
		/utils/ckeditor/ckeditor.js
	</xsl:variable>	
	

	<xsl:template match="document">
		
		<script type="text/javascript" language="Javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/confirmDelete.js" />
		
		<div class="contentitem">

			<h1><xsl:value-of select="/document/module/name" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$document.header" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="/document/group/name" /></h1>

			<xsl:apply-templates select="groupFirstpageModule"/>
			<xsl:apply-templates select="updatefirstpage"/>
			
		</div>
	</xsl:template>
	
	<xsl:template match="groupFirstpageModule">
		
		<div class="floatleft full">
			
			<xsl:if test="/document/isAdmin">
				<div class="floatright">
					<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/update" Title="{$groupFirstpageModule.update.title}">
		 			<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png" alt=""/></a>
		 			<xsl:if test="groupFirstpage">
		 				<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/delete" Title="{$groupFirstpageModule.delete.title}" onclick="return confirmDelete('{$groupFirstpageModule.delete.confirm}?')">
		 				<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" alt=""/></a>
					</xsl:if>
				</div>
			</xsl:if>
			
			<div class="line">
				<h2>
					<span>
						<xsl:choose>
							<xsl:when test="groupFirstpage/title">
								<xsl:value-of select="groupFirstpage/title" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$noFirstpageText" />
							</xsl:otherwise>
						</xsl:choose>
					</span>
				</h2>
			</div>
			
			<div class="floatleft full margintop">
				
				<xsl:choose>
					<xsl:when test="groupFirstpage/content">
						<xsl:if test="groupFirstpage/imagelocation = 'ABOVE'">
							<img class="bigmarginbottom" src="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/getImage" />
						</xsl:if>
						<xsl:value-of select="groupFirstpage/content" disable-output-escaping="yes" />
						<xsl:if test="groupFirstpage/imagelocation = 'BELOW'">
							<img class="bigmarginbottom" src="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/getImage" />
						</xsl:if>
						<hr />

						<p class="addedBy">
							<xsl:value-of select="$groupFirstpageModule.lastupdated" />: <xsl:value-of select="groupFirstpage/changed" />
						</p>

					</xsl:when>
					<xsl:otherwise>
						<p><b><xsl:value-of select="noFirstpageText" /></b></p>
					</xsl:otherwise>
				</xsl:choose>
				
			</div>
			
		</div><!--
		
		<table class="floatleft full">
			
			<tbody>
				<tr>
					<th>		
						<xsl:if test="/document/isAdmin">
							<div class="floatright">
								<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/update" Title="{$groupFirstpageModule.update.title}">
					 			<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png" alt=""/></a>
					 			<xsl:if test="groupFirstpage">
					 				<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/delete" Title="{$groupFirstpageModule.delete.title}" onclick="return confirmDelete('{$groupFirstpageModule.delete.confirm}?')">
					 				<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" alt=""/></a>
								</xsl:if>
							</div>
						</xsl:if>
						
					</th>
				</tr>
				<tr>
					<td style="padding-left: 5px">
						<xsl:choose>
							<xsl:when test="groupFirstpage/content">
								<xsl:if test="groupFirstpage/imagelocation = 'ABOVE'">
									<img class="bigmarginbottom" src="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/getImage" />
								</xsl:if>
								<xsl:value-of select="groupFirstpage/content" disable-output-escaping="yes" />
								<xsl:if test="groupFirstpage/imagelocation = 'BELOW'">
									<img class="bigmarginbottom" src="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/getImage" />
								</xsl:if>
								<hr />

								<p class="addedBy">
									<xsl:value-of select="$groupFirstpageModule.lastupdated" />: <xsl:value-of select="groupFirstpage/changed" />
								</p>

							</xsl:when>
							<xsl:otherwise>
								<p><b><xsl:value-of select="noFirstpageText" /></b></p>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
			</tbody>
			
		</table>
		
		
	--></xsl:template>
	
	<xsl:template match="updatefirstpage">
		
		<form method="post" name="updateFirstpage" id="updateFirstpage" action="{/document/requestinfo/uri}" enctype="multipart/form-data" accept-charset="ISO-8859-1">
	
			<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/ckeditor/ckeditor.js" />
		
			<div class="content-box">
				<h1 class="header">
					<span><xsl:value-of select="$updatefirstpage.header" /></span>
				</h1>
				
				<div class="content">
				
					<xsl:apply-templates select="validationException/validationError" />
				
					<p>
						<label for="title"><xsl:value-of select="$updatefirstpage.title" />:</label><br />
						<input type="text" id="title" name="title" size="40">
							<xsl:attribute name="value">
								<xsl:choose>
									<xsl:when test="requestparameters/parameter[name='title']/value">
										<xsl:value-of select="requestparameters/parameter[name='title']/value" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="groupFirstpage/title" />
									</xsl:otherwise>
								</xsl:choose>
							</xsl:attribute>
						</input>
					</p>
					
					<p>
						<label for="text"><xsl:value-of select="$updatefirstpage.content" />:</label><br />
						<textarea class="fckeditor" id="text" name="text" rows="20">
							<xsl:choose>
								<xsl:when test="requestparameters/parameter[name='text']">
									<xsl:value-of select="requestparameters/parameter[name='text']/value"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="groupFirstpage/content"/>
								</xsl:otherwise>
							</xsl:choose>
						</textarea>						
						
						<xsl:call-template name="initializeFckEditor" />
					</p>
					<p>
						<input type="checkbox" id="uploadCheck" name="uploadCheck">
							<xsl:attribute name="onclick">
								document.getElementById('imagelocation1').disabled=!this.checked; 
								document.getElementById('imagelocation2').disabled=!this.checked; 
								document.updateFirstpage.image.disabled=!this.checked;
								<xsl:if test="groupFirstpage/imagelocation">
									document.getElementById('imagelocation1.old').disabled=this.checked; 
									document.getElementById('imagelocation2.old').disabled=this.checked; 
									document.getElementById('deleteImage').disabled=this.checked; 
									document.getElementById('deleteImage').checked=false; 
								</xsl:if>
							</xsl:attribute>								
							<xsl:if test="requestparameters/parameter[name='uploadCheck'][value='on']">
								<xsl:attribute name="checked">true</xsl:attribute>
								<script language="javascript">
									<xsl:if test="groupFirstpage/imagelocation">
										document.getElementById('imagelocation1.old').disabled = true;
										document.getElementById('imagelocation2.old').disabled = true;
									</xsl:if>
								</script>
							</xsl:if>
						</input>
						
						<label for="uploadCheck"><xsl:choose><xsl:when test="groupFirstpage/imagelocation"><xsl:value-of select="$updatefirstpage.changeimage" /></xsl:when><xsl:otherwise><xsl:value-of select="$updatefirstpage.uploadimage" /></xsl:otherwise></xsl:choose><xsl:text>&#160;</xsl:text>(<xsl:value-of select="$updatefirstpage.notrequired" />)</label><br />
						<input type="file" name="image" size="59" disabled="true" value="{requestparameters/parameter[name='image']/value}"/>
													
					</p>
					<p>
						<label for="imagelocation2"><xsl:choose><xsl:when test="groupFirstpage/imagelocation"><xsl:value-of select="$updatefirstpage.newimage" />:</xsl:when><xsl:otherwise><xsl:value-of select="$updatefirstpage.imagelocation" />:</xsl:otherwise></xsl:choose></label><br />
						<input type="radio" id="imagelocation2" name="imagelocation" value="ABOVE" disabled="true" checked="true">
							<xsl:choose>
								<xsl:when test="requestparameters/parameter[name='imagelocation']">
									<xsl:if test="requestparameters/parameter[name='imagelocation']/value = 'ABOVE'">
										<xsl:attribute name="checked" />
									</xsl:if>
								</xsl:when>
							</xsl:choose>
						</input>
						<label for="imagelocation2"><xsl:value-of select="$updatefirstpage.imagelocation.above" /></label><br />
						<input type="radio" id="imagelocation1" name="imagelocation" value="BELOW" disabled="true" >
							<xsl:choose>
								<xsl:when test="requestparameters/parameter[name='imagelocation']">
									<xsl:if test="requestparameters/parameter[name='imagelocation']/value = 'BELOW'">
										<xsl:attribute name="checked" />
									</xsl:if>
								</xsl:when>
							</xsl:choose>
							<xsl:if test="requestparameters/parameter[name='uploadCheck'][value='on']">
								<script>
									document.updateFirstpage.imagelocation1.disabled = false;
									document.updateFirstpage.imagelocation2.disabled = false;
									document.updateFirstpage.uploadCheck.checked = true;
									document.updateFirstpage.image.disabled = false;
								</script>
							</xsl:if>
						</input>
						<label for="imagelocation1"><xsl:value-of select="$updatefirstpage.imagelocation.below" /></label>
					</p>
					<xsl:if test="groupFirstpage/imagelocation">
						<p>
							<label for="imagelocation2.old"><xsl:value-of select="$updatefirstpage.currentimage.imagelocation" />:</label><br />
							<input type="radio" id="imagelocation2.old" name="imagelocation.old" value="ABOVE" checked="true">
								<xsl:choose>
									<xsl:when test="requestparameters/parameter[name='imagelocation.old']">
										<xsl:if test="requestparameters/parameter[name='imagelocation.old']/value = 'ABOVE'">
											<xsl:attribute name="checked" />
										</xsl:if>
									</xsl:when>
									<xsl:otherwise>
										<xsl:if test="groupFirstpage/imagelocation = 'ABOVE'">
											<xsl:attribute name="checked" />
										</xsl:if>
									</xsl:otherwise>
								</xsl:choose>
							</input>
							
							<label for="imagelocation2.old"><xsl:value-of select="$updatefirstpage.imagelocation.above" /></label><br />
							<input type="radio" id="imagelocation1.old" name="imagelocation.old" value="BELOW">
								<xsl:choose>
									<xsl:when test="requestparameters/parameter[name='imagelocation.old']">
										<xsl:if test="requestparameters/parameter[name='imagelocation.old']/value = 'BELOW'">
											<xsl:attribute name="checked" />
										</xsl:if>
									</xsl:when>
									<xsl:otherwise>
										<xsl:if test="groupFirstpage/imagelocation = 'BELOW'">
											<xsl:attribute name="checked" />
										</xsl:if>
									</xsl:otherwise>
								</xsl:choose>

							</input>
							<label for="imagelocation1.old"><xsl:value-of select="$updatefirstpage.imagelocation.below" /></label>
							<xsl:if test="requestparameters/parameter[name='uploadCheck'][value='on']">
								<script language="javascript">
									document.getElementById('imagelocation1.old').disabled = true;
									document.getElementById('imagelocation2.old').disabled = true;
								</script>
							</xsl:if>
						</p>
						<p>
							<input type="checkbox" id="deleteImage" name="deleteImage" >
								<xsl:if test="requestparameters/parameter[name='deleteImage'][value='on']">
									<xsl:attribute name="checked">true</xsl:attribute>
								</xsl:if>
								<xsl:attribute name="onclick">
									document.getElementById('imagelocation1.old').disabled=this.checked; 
									document.getElementById('imagelocation2.old').disabled=this.checked;
								</xsl:attribute>
							</input>
							<label for="deleteImage"><xsl:value-of select="$updatefirstpage.currentimage.delete" /></label>
						</p>
					</xsl:if>	
					
					<div class="text-align-right">
						<input type="submit" value="{$updatefirstpage.currentimage.save}" />
					</div>		
				
				</div>
				
			</div>
	
		</form>
	
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
					<xsl:when test="validationErrorType='Other'">
						<xsl:value-of select="$validationError.Other" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$validationError.unknownValidationErrorType" />
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:choose>
					<xsl:when test="fieldName = 'name'">
						<xsl:value-of select="$validationError.field.name" />!
					</xsl:when>
					<xsl:when test="fieldName = 'description'">
						<xsl:value-of select="$validationError.field.description" />!
					</xsl:when>
					<xsl:when test="fieldName = 'commentText'">
						<xsl:value-of select="$validationError.field.commentText" />!
					</xsl:when>
					<xsl:when test="fieldName = 'url'">
						<xsl:value-of select="$validationError.field.url" />!
					</xsl:when>
					<xsl:when test="fieldName = 'title'">
						<xsl:value-of select="$validationError.field.title" />!
					</xsl:when>
					<xsl:when test="fieldName = 'text'">
						<xsl:value-of select="$validationError.field.text" />!
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
					<xsl:when test="messageKey='BadFileFormat'">
						<xsl:value-of select="$validationError.messageKey.BadFileFormat" />!
					</xsl:when>
					<xsl:when test="messageKey='NoFile'">
						<xsl:value-of select="$validationError.messageKey.NoFile" />!
					</xsl:when>
					<xsl:when test="messageKey='InvalidContent'">
						<xsl:value-of select="$validationError.messageKey.InvalidContent" />!
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$validationError.unknownMessageKey" />!
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
	</xsl:template>
	
	
</xsl:stylesheet>