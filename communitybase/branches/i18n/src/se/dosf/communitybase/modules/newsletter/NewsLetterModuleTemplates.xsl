<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	

	<xsl:template match="document">
		
		<script type="text/javascript" language="Javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/confirmDelete.js" />
		
		<div class="contentitem">
			<div class="normal">	
				<h1><xsl:value-of select="/document/module/name" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="$document.header" />:<xsl:text>&#x20;</xsl:text><xsl:value-of select="/document/group/name" /></h1>
			</div>
			
			<xsl:apply-templates select="newslettermodule"/>
			<xsl:apply-templates select="addNewsletter"/>
			<xsl:apply-templates select="updateNewsletter" />
			<xsl:apply-templates select="showReadReceipt" />
		</div>
	</xsl:template>
	
	<xsl:template match="newslettermodule">
	
		<div class="noBorderDiv">
			<div class="divNormal">
				<h1 class="normalTableHeading" colspan="4" align="left">
					<xsl:choose>
						<xsl:when test="showNewsletter/newsletter">
							<xsl:value-of select="showNewsletter/newsletter/title" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$newslettermodule.noNewsletters.header" />
						</xsl:otherwise>
					</xsl:choose>
				</h1>
				<xsl:if test="/document/isAdmin">
					<div class="floatright">
						<xsl:if test="showNewsletter/newsletter">
							<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/showReadReceipt/{showNewsletter/newsletter/newsletterID}" Title="{$newslettermodule.readreceipt.title} &quot;{showNewsletter/newsletter/title}&quot;">
				 			<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/chart.png" alt=""/></a>
							<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/updateNewsletter/{showNewsletter/newsletter/newsletterID}" Title="{$newslettermodule.update.title} &quot;{showNewsletter/newsletter/title}&quot;">
				 			<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png" alt=""/></a>
				 			<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/deleteNewsletter/{showNewsletter/newsletter/newsletterID}" Title="{$newslettermodule.delete.title} &quot;{showNewsletter/newsletter/title}&quot;" onclick="return confirmDelete('{$newslettermodule.delete.confirm} &quot;{showNewsletter/newsletter/title}&quot;?')">
				 			<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" alt=""/></a>
						</xsl:if>
						<br />
					</div>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="showNewsletter/newsletter">
						<div class="bigmargintop">
							<xsl:if test="showNewsletter/newsletter/imagelocation = 'ABOVE'">
								<img class="bigmarginbottom" src="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/getImage/{showNewsletter/newsletter/newsletterID}" />
							</xsl:if>
							<br />
							<xsl:value-of select="showNewsletter/newsletter/message" disable-output-escaping="yes" />
							<xsl:if test="showNewsletter/newsletter/imagelocation = 'BELOW'">
								<img class="bigmargintop" src="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/getImage/{showNewsletter/newsletter/newsletterID}" />
							</xsl:if>
							<div style="background-color: #F0F2F4">
								<p class="addedby">
									<i>
										<xsl:value-of select="$newslettermodule.postedBy" />: 
										<xsl:choose>
											<xsl:when test="showNewsletter/postedUser/user">
												<xsl:value-of select="showNewsletter/postedUser/user/firstname" /><xsl:text> </xsl:text><xsl:value-of select="showNewsletter/postedUser/user/lastname" /> 
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="$newslettermodule.deletedUser" />
											</xsl:otherwise>
										</xsl:choose>
										, <xsl:value-of select="showNewsletter/newsletter/fullposted" />
									</i>
								</p>
							</div>
						</div>
					</xsl:when>
					<xsl:otherwise>
						<p><xsl:value-of select="$newslettermodule.noNewsletters" /></p>
					</xsl:otherwise>
				</xsl:choose>

			</div>
			<xsl:if test="/document/isAdmin">
				<div style="clear: both;" />	
				<div class="addGalleries noprint">
					<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addNewsletter">
						<xsl:value-of select="$newslettermodule.submit" />
					</a>	
				</div>
			</xsl:if>
			<div class="noBorderDiv noprint">
				
				<p class="info"><img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/information.png" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="$newslettermodule.information" /></p>
				
				<h2><xsl:value-of select="$newslettermodule.moreNewsletters" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="/document/group/name" /></h2>
				<form method="get" name="showNewsletter">
					<select name="newsletterlist" size="10" class="full" ondblclick="window.location = '{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/show/' + document.showNewsletter.newsletterlist.options[document.showNewsletter.newsletterlist.selectedIndex].value">
						<xsl:apply-templates select="newsletters/newsletter" mode="list" />				
					</select>				
					<div align="right" class="margintop">
						<input type="button" onclick="window.location = '{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/show/' + document.showNewsletter.newsletterlist.options[document.showNewsletter.newsletterlist.selectedIndex].value" value="{$newslettermodule.showNewsletter}" />
					</div>
				</form>
			</div>
		</div>
	
	</xsl:template>
	
	<xsl:template match="newsletter" mode="list">
	
		<option value="{newsletterID}">
			<xsl:choose>
				<xsl:when test="../../showNewsletter">
					<xsl:if test="newsletterID = ../../showNewsletter/newsletter/newsletterID">
						<xsl:attribute name="selected" />
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<xsl:if test="newsletterID = ../../newsletters/newsletter[position() = 1]/newsletterID">
						<xsl:attribute name="selected" />
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:value-of select="postedDate" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="title" />
		</option>	
	
	</xsl:template>
	
	<xsl:template match="addNewsletter">
		<form method="post" name="addNewsletter" id="addNewsletter" action="{/document/requestinfo/url}" enctype="multipart/form-data" accept-charset="ISO-8859-1">
			
			<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/fckeditor/fckeditor.js" />

			<div class="divNormal">
				<h1 class="normalTableHeading" colspan="4" align="left">
						<xsl:value-of select="$addNewsletter.header" />
					</h1>
				<div style="width: 65em">
					<xsl:apply-templates select="validationException/validationError" />
					<div class="marginbottom">
						<xsl:value-of select="$newsletter.title" />:<br />
						<input type="text" name="title" size="40" value="{requestparameters/parameter[name='title']/value}" />
						
					</div>
					<div style="margin-top: 10px" class="bigmargintop bigmarginbottom">
						<p>
							<xsl:value-of select="$newsletter.content" />:<br />
							<textarea name="text" rows="20">
								<xsl:choose>
									<xsl:when test="requestparameters/parameter[name='text']">
										<xsl:value-of select="requestparameters/parameter[name='text']/value"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="page/text"/>
									</xsl:otherwise>
								</xsl:choose>
							</textarea>						
							
							<xsl:call-template name="initializeFckEditor" >
								<xsl:with-param name="field">text</xsl:with-param>
							</xsl:call-template>
							
						</p>
						<p>
							<input type="checkbox" name="uploadCheck" onclick="document.getElementById('imagelocation1').disabled=!this.checked; document.getElementById('imagelocation2').disabled=!this.checked; document.addNewsletter.zipFile.disabled=!this.checked">
								<xsl:if test="requestparameters/parameter[name='uploadCheck'][value='on']">
									<xsl:attribute name="checked">true</xsl:attribute>
									
								</xsl:if>
							</input>
							<xsl:value-of select="$addNewsletter.uploadimage" /><xsl:text>&#x20;</xsl:text>(<xsl:value-of select="$newsletter.notrequired" />)<xsl:text>&#x20;</xsl:text><br />
							<input type="file" name="zipFile" size="59" disabled="true" value="{requestparameters/parameter[name='zipFile']/value}"/>
							<xsl:if test="requestparameters/parameter[name='uploadCheck'][value='on']">
								<script>
									document.addNewsletter.zipFile.disabled = false;
								</script>
							</xsl:if>
						</p>
						<p>
							<xsl:value-of select="$newsletter.imagelocation" />:<br />
							<input type="radio" id="imagelocation2" name="imagelocation" value="ABOVE" disabled="true" checked="true">
								<xsl:choose>
									<xsl:when test="requestparameters/parameter[name='imagelocation']">
										<xsl:if test="requestparameters/parameter[name='imagelocation']/value = 'ABOVE'">
											<xsl:attribute name="checked" />
										</xsl:if>
									</xsl:when>
								</xsl:choose>
							</input>
							<xsl:value-of select="$newsletter.imagelocation.above" /><br />
							<input type="radio" id="imagelocation1" name="imagelocation" value="BELOW" disabled="true">
								<xsl:choose>
									<xsl:when test="requestparameters/parameter[name='imagelocation']">
										<xsl:if test="requestparameters/parameter[name='imagelocation']/value = 'BELOW'">
											<xsl:attribute name="checked" />
										</xsl:if>
									</xsl:when>
								</xsl:choose>
							</input>
							<xsl:value-of select="$newsletter.imagelocation.below" />
							<xsl:if test="requestparameters/parameter[name='uploadCheck'][value='on']">
								<script language="javascript">
									document.getElementById('imagelocation1').disabled = false;
									document.getElementById('imagelocation2').disabled = false;
								</script>
							</xsl:if>
						</p>
						
					</div>
					
					<div class="bigmarginbottom" align="right">
						<input type="submit" value="{$addNewsletter.submit}" />
					</div>	
				</div>
			</div>

		</form>
	</xsl:template>
	
	<xsl:template match="updateNewsletter">
		
		<form method="post" name="updateNewsletter" id="updateNewsletter" action="{/document/requestinfo/url}" enctype="multipart/form-data" accept-charset="ISO-8859-1">
			
			<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/fckeditor/fckeditor.js" />
			
			<div class="divNormal">
				<h1 class="normalTableHeading" colspan="4" align="left">
					<xsl:value-of select="$updateNewsletter.header" /><xsl:text>&#x20;</xsl:text>"<xsl:value-of select="newsletter/title" />"
				</h1>
				<div style="width: 65em">
					<xsl:apply-templates select="validationException/validationError" />
					<div class="marginbottom">
						<xsl:value-of select="$newsletter.title" />: <br />
						<input type="text" name="title" size="40">
							<xsl:attribute name="value">
								<xsl:choose>
									<xsl:when test="requestparameters/parameter[name='title']/value">
										<xsl:value-of select="requestparameters/parameter[name='title']/value" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="newsletter/title" />
									</xsl:otherwise>
								</xsl:choose>
							</xsl:attribute>
						</input>
						<br />
					</div>
					<div style="margin-top: 10px" class="bigmargintop bigmarginbottom">
						<p>
							<xsl:value-of select="$newsletter.content" />:<br />
							<textarea name="text" rows="20">
								<xsl:choose>
									<xsl:when test="requestparameters/parameter[name='text']">
										<xsl:value-of select="requestparameters/parameter[name='text']/value"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="newsletter/message"/>
									</xsl:otherwise>
								</xsl:choose>
							</textarea>						
							
							<xsl:call-template name="initializeFckEditor" >
								<xsl:with-param name="field">text</xsl:with-param>
							</xsl:call-template>
							
						</p>
						<p>
							<input type="checkbox" name="uploadCheck">
								<xsl:if test="requestparameters/parameter[name='uploadCheck'][value='on']">
									<xsl:attribute name="checked">true</xsl:attribute>
									<script language="javascript">
										document.getElementById('imagelocation1.old').disabled = true;
										document.getElementById('imagelocation2.old').disabled = true;
									</script>
								</xsl:if>
								<xsl:attribute name="onclick">
									document.getElementById('imagelocation1').disabled=!this.checked; 
									document.getElementById('imagelocation2').disabled=!this.checked; 
									document.updateNewsletter.zipFile.disabled=!this.checked;
									<xsl:if test="newsletter/imagelocation">
										document.getElementById('imagelocation1.old').disabled=this.checked; 
										document.getElementById('imagelocation2.old').disabled=this.checked; 
										document.getElementById('deleteImage').disabled=this.checked; 
										document.getElementById('deleteImage').checked=false; 
									</xsl:if>
								</xsl:attribute>
							</input>
							<xsl:value-of select="$updateNewsletter.changeimage" /><xsl:text>&#x20;</xsl:text>(<xsl:value-of select="$newsletter.notrequired" />)<br />
							<input type="file" name="zipFile" size="59" disabled="true" value="{requestparameters/parameter[name='zipFile']/value}"/>
							<xsl:if test="requestparameters/parameter[name='uploadCheck'][value='on']">
								<script>
									document.updateNewsletter.uploadCheck.checked = true;
								</script>
							</xsl:if>
							
						</p>
						<p>
							<xsl:value-of select="$updateNewsletter.newimage" />:<br />
							<input type="radio" id="imagelocation2" name="imagelocation" value="ABOVE" disabled="true" checked="true">
								<xsl:choose>
									<xsl:when test="requestparameters/parameter[name='imagelocation']">
										<xsl:if test="requestparameters/parameter[name='imagelocation']/value = 'ABOVE'">
											<xsl:attribute name="checked" />
										</xsl:if>
									</xsl:when>
								</xsl:choose>
							</input>
							<xsl:value-of select="$newsletter.imagelocation.above" /><br />
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
										document.updateNewsletter.imagelocation1.disabled = false;
										document.updateNewsletter.imagelocation2.disabled = false;
									</script>
								</xsl:if>
							</input>
							<xsl:value-of select="$newsletter.imagelocation.below" />
						</p>
						<xsl:if test="newsletter/imagelocation">
							<p>
								<xsl:value-of select="$updateNewsletter.currentimage.imagelocation" />:<br />
								<input type="radio" id="imagelocation2.old" name="imagelocation.old" value="ABOVE" checked="true">
									<xsl:choose>
										<xsl:when test="requestparameters/parameter[name='imagelocation.old']">
											<xsl:if test="requestparameters/parameter[name='imagelocation.old']/value = 'ABOVE'">
												<xsl:attribute name="checked" />
											</xsl:if>
										</xsl:when>
										<xsl:otherwise>
											<xsl:if test="newsletter/imagelocation = 'ABOVE'">
												<xsl:attribute name="checked" />
											</xsl:if>
										</xsl:otherwise>
									</xsl:choose>
								</input>
								
								<xsl:value-of select="$newsletter.imagelocation.above" /><br />
								<input type="radio" id="imagelocation1.old" name="imagelocation.old" value="BELOW">
									<xsl:choose>
										<xsl:when test="requestparameters/parameter[name='imagelocation.old']">
											<xsl:if test="requestparameters/parameter[name='imagelocation.old']/value = 'BELOW'">
												<xsl:attribute name="checked" />
											</xsl:if>
										</xsl:when>
										<xsl:otherwise>
											<xsl:if test="newsletter/imagelocation = 'BELOW'">
												<xsl:attribute name="checked" />
											</xsl:if>
										</xsl:otherwise>
									</xsl:choose>
	
								</input>
								<xsl:value-of select="$newsletter.imagelocation.below" />
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
								<xsl:value-of select="$updateNewsletter.currentimage.delete" />
							</p>
						</xsl:if>
					</div>
					
					<div align="right">
						<input type="submit" value="{$updateNewsletter.submit}" />
					</div>	
				</div>
			</div>

		</form>
	
	</xsl:template>
	
	<xsl:template match="showReadReceipt">
		
		<div class="noBorderDiv">
			<div class="divNormal">
				<h1 class="normalTableHeading" colspan="4" align="left">
					<xsl:value-of select="$showReadReceipt.header" /><xsl:text>&#x20;</xsl:text>"<xsl:value-of select="newsletter/title" />"
				</h1>
				<p><xsl:value-of select="$showReadReceipt.summary.part1" /><xsl:text>&#x20;</xsl:text><b><xsl:value-of select="count(receipt)" /></b><xsl:text>&#x20;</xsl:text><xsl:value-of select="$showReadReceipt.summary.part2" />.</p>
				<div class="floatleft full bigmarginbottom">
					<div class="half floatleft marginleft">
						<b><xsl:value-of select="$showReadReceipt.name" />:</b>
					</div>
					<div style="width: 30%" class="floatleft marginleft">
						<b><xsl:value-of select="$showReadReceipt.firstread" />:</b>
					</div>
					<div class="floatleft">
						<b><xsl:value-of select="$showReadReceipt.lastread" />:</b>
					</div>
				</div>
				<xsl:choose>
					<xsl:when test="receipt">
						<xsl:apply-templates select="receipt" />
					</xsl:when>
					<xsl:otherwise>
						<div class="floatleft full marginbottom border">
							<div class="floatleft full margintop marginbottom marginleft">
								<xsl:value-of select="$showReadReceipt.noreceipt" />
							</div>
						</div>
					</xsl:otherwise>
				</xsl:choose>
			</div>
		</div>

	</xsl:template>
	
	<xsl:template match="receipt">
		
		<div class="floatleft full marginbottom border">
		
			<div class="floatleft full margintop marginbottom marginleft">
			
				<div class="half floatleft">
					<xsl:value-of select="user/firstname" /><xsl:text> </xsl:text><xsl:value-of select="user/lastname" />
					<xsl:if test="user/communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation/Comment != ''">
						<xsl:text> (</xsl:text><xsl:value-of select="user/communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation/Comment" /><xsl:text>)</xsl:text>
					</xsl:if>
				</div>
				<div style="width: 30%" class="floatleft">
					<xsl:value-of select="firstReadTime" />
				</div>
				<div class="floatleft">
					<xsl:value-of select="lastReadTime" />
				</div>
				
			</div>
			
		</div>
		
	</xsl:template>
	
	<xsl:template name="initializeFckEditor">
		
		<xsl:param name="field" />
		
		<script>	
			var sBasePath = '<xsl:value-of select="/document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/document/module/sectionID"/>/<xsl:value-of select="/document/module/moduleID"/>/fckeditor/';
			
			var oFCKeditor = new FCKeditor( '<xsl:value-of select="$field" />' ) ;
			oFCKeditor.BasePath	= sBasePath ;
			oFCKeditor.Height	= 400 ;
			oFCKeditor.Config.LinkBrowserURL = sBasePath + "editor/filemanager/browser/default/browser.html?Connector=<xsl:value-of select="/document/requestinfo/uri"/>/../../connector";
			oFCKeditor.Config.ImageBrowserURL = sBasePath + "editor/filemanager/browser/default/browser.html?Type=Image&amp;Connector=<xsl:value-of select="/document/requestinfo/uri"/>/../../connector";
			oFCKeditor.Config.FlashBrowserURL = sBasePath + "editor/filemanager/browser/default/browser.html?Type=Flash&amp;Connector=<xsl:value-of select="/document/requestinfo/uri"/>/../../connector";							
			
			oFCKeditor.ToolbarSet = 'CustomToolbar';
			
			oFCKeditor.Config.EditorAreaCSS = '<xsl:value-of select="/document/requestinfo/contextpath"/>/css/style.css'
			
			oFCKeditor.ReplaceTextarea() ;								
		</script> 
	
	</xsl:template>
	
	<xsl:template match="validationError">
		<xsl:if test="fieldName and validationErrorType">
			<p class="error">
				<xsl:choose>
					<xsl:when test="validationErrorType='RequiredField'">
						<xsl:text><xsl:value-of select="$validationError.RequiredField"/></xsl:text>
					</xsl:when>
					<xsl:when test="validationErrorType='InvalidFormat'">
						<xsl:text><xsl:value-of select="$validationError.InvalidFormat"/></xsl:text>
					</xsl:when>
					<xsl:when test="validationErrorType='TooShort'">
						<xsl:text><xsl:value-of select="$validation.TooShort" /></xsl:text>
					</xsl:when>
					<xsl:when test="validationErrorType='TooLong'">
						<xsl:text><xsl:value-of select="$validation.TooLong" /></xsl:text>
					</xsl:when>									
					<xsl:when test="validationErrorType='Other'">
						<xsl:text><xsl:value-of select="$validationError.Other" /></xsl:text>
					</xsl:when>	
					<xsl:otherwise>
						<xsl:text><xsl:value-of select="$validationError.unknownValidationErrorType"/></xsl:text>
					</xsl:otherwise>					
				</xsl:choose>
				
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:choose>
					<xsl:when test="fieldName = 'title'">
						<xsl:value-of select="$validationError.field.title"/>!
					</xsl:when>
					<xsl:when test="fieldName = 'description'">
						<xsl:value-of select="$validationError.field.description"/>!
					</xsl:when>
					<xsl:when test="fieldName = 'commentText'">
						<xsl:value-of select="$validationError.field.commentText"/>!
					</xsl:when>
					<xsl:when test="fieldName = 'url'">
						<xsl:value-of select="$validationError.field.url" />!
					</xsl:when>
					<xsl:when test="fieldName = 'text'">
						<xsl:value-of select="$validationError.field.content" />!
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
						<xsl:value-of select="$validationError.messageKey.BadFileFormat"/>!
					</xsl:when>
					<xsl:when test="messageKey='NoFile'">
						<xsl:value-of select="$validationError.messageKey.NoFile"/>!
					</xsl:when>
					<xsl:when test="messageKey='InvalidContent'">
						<xsl:value-of select="$validationError.messageKey.InvalidContent"/>!
					</xsl:when>
					<xsl:when test="UnableToParseRequest">
						<xsl:value-of select="$validationError.messageKey.UnableToParseRequest"/>!
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$validationError.unknownMessageKey" />!
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
	</xsl:template>
	
	
</xsl:stylesheet>