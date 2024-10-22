<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>
	
	<xsl:include href="classpath://se/dosf/communitybase/utils/fckeditor/FCKEditor.xsl" />

	<xsl:variable name="scripts">
		/utils/treeview/jquery.cookie.js
		/utils/treeview/jquery.jstree.custom.js
		/utils/treeview/communitybase.treeview.js
		/utils/fckeditor/fckeditor.js
		/utils/js/confirmDelete.js
		/utils/js/communitybase.common.js
	</xsl:variable>

	<xsl:variable name="links">
		/utils/treeview/themes/communitybase/style.css
	</xsl:variable>

	<xsl:variable name="modulePath"><xsl:value-of select="/Document/requestinfo/currentURI" />/<xsl:value-of select="/Document/module/alias" />/<xsl:value-of select="/Document/group/groupID" /></xsl:variable>
	<xsl:variable name="moduleImagePath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>

	<xsl:template match="Document">
		
		<div class="contentitem">
	
			<h1><xsl:value-of select="/Document/module/name" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="$document.header" />:<xsl:text>&#x20;</xsl:text><xsl:value-of select="/Document/group/name" /></h1>
			
			<xsl:apply-templates select="ListNewsLetters" />
			<xsl:apply-templates select="AddNewsLetter" />
			<xsl:apply-templates select="UpdateNewsLetter" />
			<xsl:apply-templates select="ShowReadReceipt" />

		</div>
	</xsl:template>
	
	<xsl:template match="ListNewsLetters">
	
		<div class="floatleft full">
			<div class="content-box">
				<h1 class="header">
					<xsl:choose>
						<xsl:when test="NewsLetter">
							<xsl:value-of select="NewsLetter/title" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$newslettermodule.noNewsletters.header" />
						</xsl:otherwise>
					</xsl:choose>
				</h1>
				
				<div class="content">
					
					<xsl:choose>
						<xsl:when test="NewsLetter">
							<xsl:apply-templates select="NewsLetter" mode="show" />
						</xsl:when>
						<xsl:otherwise>
							<p><xsl:value-of select="$newslettermodule.noNewsletters" /></p>
						</xsl:otherwise>
					</xsl:choose>
	
					<xsl:if test="/Document/isAdmin">
						<div class="clearboth" />	
						<div class="text-align-right bigmarginbottom">
							<a href="{$modulePath}/addNewsletter">
								<xsl:value-of select="$newslettermodule.submit" />
							</a>	
						</div>
					</xsl:if>
					<div class="floatleft full noprint">
						
						<p class="info"><img src="{$moduleImagePath}/information.png" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="$newslettermodule.information" /></p>
						
						<h2 class="floatleft"><xsl:value-of select="$newslettermodule.moreNewsletters" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="/Document/group/name" /></h2>
						
						<div class="floatleft full">
						
							<form method="GET" name="showNewsletter">
								
								<select name="newsletterlist" size="10" class="full" ondblclick="window.location = '{$modulePath}/show/' + document.showNewsletter.newsletterlist.options[document.showNewsletter.newsletterlist.selectedIndex].value">
									<xsl:apply-templates select="NewsLetters/NewsLetter" mode="list" />				
								</select>				
								<div class="text-align-right margintop">
									<input type="button" onclick="window.location = '{$modulePath}/show/' + document.showNewsletter.newsletterlist.options[document.showNewsletter.newsletterlist.selectedIndex].value" value="{$newslettermodule.showNewsletter}" />
								</div>
								
							</form>
						
						</div>
						
					</div>
			
				</div>
			
			</div>
			
		</div>		
	
	</xsl:template>
	
	<xsl:template match="NewsLetter" mode="show">
	
		<xsl:if test="/Document/hasUpdateAccess">
			<div class="floatright">
				<a href="{$modulePath}/showReadReceipt/{newsletterID}" Title="{$newslettermodule.readreceipt.title} &quot;{title}&quot;">
	 				<img src="{$moduleImagePath}/chart.png" alt="" />
	 			</a>
				<a href="{$modulePath}/updateNewsletter/{newsletterID}" Title="{$newslettermodule.update.title} &quot;{title}&quot;">
	 				<img src="{$moduleImagePath}/edit.png" alt="" />
	 			</a>
	 			<a href="{$modulePath}/deleteNewsletter/{newsletterID}" Title="{$newslettermodule.delete.title} &quot;{title}&quot;" onclick="return confirmDelete('{$newslettermodule.delete.confirm} &quot;{title}&quot;?')">
	 				<img src="{$moduleImagePath}/delete.png" alt="" />
	 			</a>
				<br />
			</div>
		</xsl:if>
	
		<div class="margintop">
			<xsl:if test="imageLocation = 'ABOVE'">
				<img class="bigmarginbottom" src="{$modulePath}/getImage/{newsletterID}" />
			</xsl:if>
			
			<xsl:value-of select="message" disable-output-escaping="yes" />
			
			<xsl:if test="imageLocation = 'BELOW'">
				<img class="bigmargintop" src="{$modulePath}/getImage/{newsletterID}" />
			</xsl:if>
			
			<div class="floatleft bigmargintop bordertop full">
				<div class="floatleft">
					<p class="addedBy">
						<xsl:value-of select="$newslettermodule.postedBy" />:<xsl:text>&#x20;</xsl:text>
						<xsl:choose>
							<xsl:when test="poster/user">
								<xsl:value-of select="poster/user/firstname" /><xsl:text> </xsl:text><xsl:value-of select="poster/user/lastname" /> 
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$newslettermodule.deletedUser" />
							</xsl:otherwise>
						</xsl:choose>, <xsl:value-of select="fullDate" />
						
					</p>
				</div>
				<div class="bigmarginleft floatleft">
					<div class="addedBy">
						<xsl:value-of select="$newslettermodule.publishedTo" />:<xsl:text>&#x20;</xsl:text>
						<xsl:call-template name="createPublishingInformation">
							<xsl:with-param name="element" select="." />
							<xsl:with-param name="moduleImagePath" select="$moduleImagePath" />
							<xsl:with-param name="displayInline" select="'true'" />
						</xsl:call-template>
					</div>
				</div>
			</div>
		</div>
	
	</xsl:template>
	
	<xsl:template match="NewsLetter" mode="list">
		
		<option value="{newsletterID}">
			<xsl:if test="newsletterID = ../../NewsLetter/newsletterID">
				<xsl:attribute name="selected" />
			</xsl:if>
			<xsl:value-of select="substring(date,1,10)" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="title" />
		</option>
		
	</xsl:template>
	
	<xsl:template match="AddNewsLetter">
		
		<form method="post" name="addNewsletter" id="addNewsletter" action="{/Document/requestinfo/uri}" enctype="multipart/form-data" accept-charset="ISO-8859-1">
			
			<div class="content-box">
				<h1 class="header">
						<xsl:value-of select="$addNewsletter.header" />
				</h1>
				
				<div class="content">
						
					<xsl:apply-templates select="validationException/validationError" />
					
					<p>
						<label for="title"><xsl:value-of select="$newsletter.title" />:</label><br />
						<input type="text" id="title" name="title" size="55" value="{requestparameters/parameter[name='title']/value}" />
					</p>
					<p>
						<label for="publish"><xsl:value-of select="$newsletter.publish" />:</label>				
						<div class="content-box-no-header">
					
							<xsl:choose>
								<xsl:when test="schools/school">
									
									<xsl:call-template name="createTreeview">
										<xsl:with-param name="id" select="'newsletter-communitybase-treeview'" />
										<xsl:with-param name="schools" select="schools" />
									</xsl:call-template>
									
								</xsl:when>
								<xsl:otherwise>
									<p><xsl:value-of select="$newsletter.noaccess" /></p>
								</xsl:otherwise>
							</xsl:choose>
							
						</div>
					</p>
					
					<div class="clearboth floatleft full">
					
						<p>
							<label for="text"><xsl:value-of select="$newsletter.content" />:</label><br />
							<textarea class="fckeditor" id="text" name="text" rows="20">
								<xsl:choose>
									<xsl:when test="requestparameters/parameter[name='text']">
										<xsl:value-of select="requestparameters/parameter[name='text']/value"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="page/text"/>
									</xsl:otherwise>
								</xsl:choose>
							</textarea>						
							
							<xsl:call-template name="initializeNewFckEditor" />
							
						</p>
					
					</div>
					
					<p>
						<input type="checkbox" id="uploadCheck" name="uploadCheck" onclick="document.getElementById('imagelocation1').disabled=!this.checked; document.getElementById('imagelocation2').disabled=!this.checked; document.addNewsletter.zipFile.disabled=!this.checked">
							<xsl:if test="requestparameters/parameter[name='uploadCheck'][value='on']">
								<xsl:attribute name="checked">true</xsl:attribute>
							</xsl:if>
						</input>
						<label for="uploadCheck"><xsl:value-of select="$addNewsletter.uploadimage" /><xsl:text>&#x20;</xsl:text>(<xsl:value-of select="$newsletter.notrequired" />)<xsl:text>&#x20;</xsl:text></label><br />
						<input type="file" id="zipFile" name="zipFile" size="59" disabled="true" value="{requestparameters/parameter[name='zipFile']/value}"/>
						<xsl:if test="requestparameters/parameter[name='uploadCheck'][value='on']">
							<script>
								document.addNewsletter.zipFile.disabled = false;
							</script>
						</xsl:if>
					</p>
					<p>
						<label for="imagelocation2"><xsl:value-of select="$newsletter.imagelocation" />:</label><br />
						<input type="radio" id="imagelocation2" name="imagelocation" value="ABOVE" disabled="true" checked="true">
							<xsl:choose>
								<xsl:when test="requestparameters/parameter[name='imagelocation']">
									<xsl:if test="requestparameters/parameter[name='imagelocation']/value = 'ABOVE'">
										<xsl:attribute name="checked" />
									</xsl:if>
								</xsl:when>
							</xsl:choose>
						</input>
						<label for="imagelocation2"><xsl:value-of select="$newsletter.imagelocation.above" /></label><br />
						<input type="radio" id="imagelocation1" name="imagelocation" value="BELOW" disabled="true">
							<xsl:choose>
								<xsl:when test="requestparameters/parameter[name='imagelocation']">
									<xsl:if test="requestparameters/parameter[name='imagelocation']/value = 'BELOW'">
										<xsl:attribute name="checked" />
									</xsl:if>
								</xsl:when>
							</xsl:choose>
						</input>
						<label for="imagelocation1"><xsl:value-of select="$newsletter.imagelocation.below" /></label>
						
						<xsl:if test="requestparameters/parameter[name='uploadCheck'][value='on']">
							<script language="javascript">
								document.getElementById('imagelocation1').disabled = false;
								document.getElementById('imagelocation2').disabled = false;
							</script>
						</xsl:if>
					</p>
					
					<div class="text-align-right">
						<input type="submit" value="{$addNewsletter.submit}" />
					</div>	
				
				</div>
				
			</div>

		</form>
		
	</xsl:template>
	
	<xsl:template match="UpdateNewsLetter">
	
		<form method="post" name="updateNewsletter" id="updateNewsletter" action="{/Document/requestinfo/uri}" enctype="multipart/form-data" accept-charset="ISO-8859-1">
			
			<div class="content-box">
				<h1 class="header">
					<xsl:value-of select="$updateNewsletter.header" />:<xsl:text>&#x20;</xsl:text><xsl:value-of select="NewsLetter/title" />
				</h1>
				
				<div class="content">
				
					<xsl:apply-templates select="validationException/validationError" />
				
					<p>
						<label for="title"><xsl:value-of select="$newsletter.title" />:</label><br />
						<input type="text" id="title" name="title" size="40">
							<xsl:attribute name="value">
								<xsl:choose>
									<xsl:when test="requestparameters/parameter[name='title']/value">
										<xsl:value-of select="requestparameters/parameter[name='title']/value" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="NewsLetter/title" />
									</xsl:otherwise>
								</xsl:choose>
							</xsl:attribute>
						</input>
					</p>
					<p>
						<label for="publish"><xsl:value-of select="$newsletter.publish" />:</label>				
						<div class="content-box-no-header">
					
							<xsl:choose>
								<xsl:when test="schools/school">
									<xsl:call-template name="createTreeview">
										<xsl:with-param name="id" select="'newsletter-communitybase-treeview'" />
										<xsl:with-param name="element" select="NewsLetter" />
										<xsl:with-param name="schools" select="schools" />
										<xsl:with-param name="requestparameters" select="requestparameters" />
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<p><xsl:value-of select="$newsletter.noaccess" /></p>
								</xsl:otherwise>
							</xsl:choose>
							
						</div>
					</p>
					
					<div class="clearboth floatleft full">					
						<p>
							<label for="text"><xsl:value-of select="$newsletter.content" />:</label><br />
							<textarea class="fckeditor" id="text" name="text" rows="20">
								<xsl:choose>
									<xsl:when test="requestparameters/parameter[name='text']">
										<xsl:value-of select="requestparameters/parameter[name='text']/value"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="NewsLetter/message"/>
									</xsl:otherwise>
								</xsl:choose>
							</textarea>						
							
							<xsl:call-template name="initializeNewFckEditor" />
						</p>
					</div>
					
					<p>
						<input type="checkbox" id="uploadCheck" name="uploadCheck">
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
								<xsl:if test="NewsLetter/imageLocation">
									document.getElementById('imagelocation1.old').disabled=this.checked; 
									document.getElementById('imagelocation2.old').disabled=this.checked; 
									document.getElementById('deleteImage').disabled=this.checked; 
									document.getElementById('deleteImage').checked=false; 
								</xsl:if>
							</xsl:attribute>
						</input>
						<label for="uploadCheck"><xsl:value-of select="$updateNewsletter.changeimage" /><xsl:text>&#x20;</xsl:text>(<xsl:value-of select="$newsletter.notrequired" />)</label><br />
						<input type="file" name="zipFile" size="59" disabled="true" value="{requestparameters/parameter[name='zipFile']/value}"/>
						<xsl:if test="requestparameters/parameter[name='uploadCheck'][value='on']">
							<script>
								document.updateNewsletter.uploadCheck.checked = true;
							</script>
						</xsl:if>
							
					</p>
					<p>
						<label for="imagelocation2"><xsl:value-of select="$updateNewsletter.newimage" />:<br /></label>
						<input type="radio" id="imagelocation2" name="imagelocation" value="ABOVE" disabled="true" checked="true">
							<xsl:choose>
								<xsl:when test="requestparameters/parameter[name='imagelocation']">
									<xsl:if test="requestparameters/parameter[name='imagelocation']/value = 'ABOVE'">
										<xsl:attribute name="checked" />
									</xsl:if>
								</xsl:when>
							</xsl:choose>
						</input>
						<label for="imagelocation2"><xsl:value-of select="$newsletter.imagelocation.above" /></label><br />
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
						<label for="imagelocation1"><xsl:value-of select="$newsletter.imagelocation.below" /></label>
					</p>
					
					<xsl:if test="NewsLetter/imageLocation">
						<p>
							<label for="imagelocation2.old"><xsl:value-of select="$updateNewsletter.currentimage.imagelocation" />:<br /></label>
							<input type="radio" id="imagelocation2.old" name="imagelocation.old" value="ABOVE" checked="true">
								<xsl:choose>
									<xsl:when test="requestparameters/parameter[name='imagelocation.old']">
										<xsl:if test="requestparameters/parameter[name='imagelocation.old']/value = 'ABOVE'">
											<xsl:attribute name="checked" />
										</xsl:if>
									</xsl:when>
									<xsl:otherwise>
										<xsl:if test="NewsLetter/imageLocation = 'ABOVE'">
											<xsl:attribute name="checked" />
										</xsl:if>
									</xsl:otherwise>
								</xsl:choose>
							</input>
							
							<label for="imagelocation2.old"><xsl:value-of select="$newsletter.imagelocation.above" /></label><br />
							<input type="radio" id="imagelocation1.old" name="imagelocation.old" value="BELOW">
								<xsl:choose>
									<xsl:when test="requestparameters/parameter[name='imagelocation.old']">
										<xsl:if test="requestparameters/parameter[name='imagelocation.old']/value = 'BELOW'">
											<xsl:attribute name="checked" />
										</xsl:if>
									</xsl:when>
									<xsl:otherwise>
										<xsl:if test="NewsLetter/imageLocation = 'BELOW'">
											<xsl:attribute name="checked" />
										</xsl:if>
									</xsl:otherwise>
								</xsl:choose>

							</input>
							<label for="imagelocation1.old"><xsl:value-of select="$newsletter.imagelocation.below" /></label>
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
							<label for="deleteImage"><xsl:value-of select="$updateNewsletter.currentimage.delete" /></label>
						</p>
					</xsl:if>
					
					<div class="text-align-right">
						<input type="submit" value="{$updateNewsletter.submit}" />
					</div>	
				</div>
			</div>

		</form>
	
	</xsl:template>
	
	<xsl:template match="ShowReadReceipt">
		
		<div class="content-box">
			<h1 class="header">
				<xsl:value-of select="$showReadReceipt.header" />:<xsl:text>&#x20;</xsl:text><xsl:value-of select="NewsLetter/title" />
			</h1>
			
			<div class="content">
			
				<p><xsl:value-of select="$showReadReceipt.summary.part1" /><xsl:text>&#x20;</xsl:text><b><xsl:value-of select="count(NewsLetter/receipts/receipt)" /></b><xsl:text>&#x20;</xsl:text><xsl:value-of select="$showReadReceipt.summary.part2" />.</p>
				
				<div class="floatleft full marginbottom">
					<div class="half floatleft marginleft">
						<b><xsl:value-of select="$showReadReceipt.name" />:</b>
					</div>
					<div class="floatleft thirty">
						<b><xsl:value-of select="$showReadReceipt.firstread" />:</b>
					</div>
					<div class="floatleft">
						<b><xsl:value-of select="$showReadReceipt.lastread" />:</b>
					</div>
				</div>
				<xsl:choose>
					<xsl:when test="NewsLetter/receipts">
						<xsl:apply-templates select="NewsLetter/receipts/receipt" />
					</xsl:when>
					<xsl:otherwise>
						<div class="floatleft full marginbottom border">
							<div class="floatleft full margintop marginbottom marginleft">
								<xsl:value-of select="$showReadReceipt.noreceipt" />
							</div>
						</div>
					</xsl:otherwise>
				</xsl:choose>
			
				<div class="floatright bigmargintop">
					<input type="button" onclick="window.location = '{/Document/requestinfo/currentURI}/{/Document/module/alias}/{/Document/group/groupID}/show/{NewsLetter/newsletterID}'" value="{$showReadReceipt.back}" />
				</div>
			
			</div>
			
		</div>

	</xsl:template>
	
	<xsl:template match="receipt">
		
		<div class="floatleft full marginbottom border">
		
			<div class="floatleft full margintop marginbottom marginleft">
			
				<div class="half floatleft">
					<xsl:value-of select="user/firstname" /><xsl:text> </xsl:text><xsl:value-of select="user/lastname" />
					<xsl:if test="user/communityUserAttributes/groups/group[groupID = /Document/group/groupID]/GroupRelation/Comment != ''">
						<xsl:text> (</xsl:text><xsl:value-of select="user/communityUserAttributes/groups/group[groupID = /Document/group/groupID]/GroupRelation/Comment" /><xsl:text>)</xsl:text>
					</xsl:if>
				</div>
				<div class="floatleft thirty">
					<xsl:value-of select="firstReadTime" />
				</div>
				<div class="floatleft">
					<xsl:value-of select="lastReadTime" />
				</div>
				
			</div>
			
		</div>
		
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
					<xsl:when test="messageKey='NoGroup'">
						<xsl:value-of select="$validationError.messageKey.NoGroup"/>!
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