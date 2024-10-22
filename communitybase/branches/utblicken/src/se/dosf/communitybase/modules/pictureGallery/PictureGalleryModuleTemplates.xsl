<?xml version="1.0" encoding="ISO-8859-1" ?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>
	
	<xsl:template match="document">
	
		<script type="text/javascript" language="Javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/confirmDelete.js" />
		<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/gallery.js"></script>
		<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/hideshow.js"></script>	

		<div class="contentitem">
		
			<div class="normal">	
				<h1><xsl:value-of select="/document/module/name" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="$document.header" />:<xsl:text>&#x20;</xsl:text><xsl:value-of select="group/name" /></h1>
			</div>
			<xsl:apply-templates select="pictureGallery" />
			<xsl:apply-templates select="showGallery" />
			<xsl:apply-templates select="addGallery" />
			<xsl:apply-templates select="updateGallery" />
			<xsl:apply-templates select="addImages" />
			<xsl:apply-templates select="multiUploader" />
			<xsl:apply-templates select="showImage" />
			<xsl:apply-templates select="noFlashSupport" />
		
		</div>	

	</xsl:template>
	
	<xsl:template match="multiUploader">
	
		<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/swfupload/base/swfupload.js"></script>
		<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/swfupload/js/swfupload.queue.js"></script>
		<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/swfupload/js/fileprogress.js"></script>
		<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/swfupload/js/{$multiUploader.language}"></script>
		<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/swfupload/js/handlers.js"></script>
		<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/flashDetector.js"></script>
	
		<xsl:variable name="relationType">
			<xsl:call-template name="getRelationType" >
				<xsl:with-param name="relationType" select="gallery/relationType" />
			</xsl:call-template>
		</xsl:variable>
	
		<script language="javascript">
			<![CDATA[
				var redirecturl = "]]><xsl:value-of select="/document/requestinfo/currentURI" /><![CDATA[/]]><xsl:value-of select="/document/module/alias" /><![CDATA[/]]><xsl:value-of select="/document/group/groupID" /><![CDATA[/simple]]><xsl:value-of select="$relationType" /><![CDATA[Uploader/]]><xsl:value-of select="gallery/sectionID" /><![CDATA[/noflash";
				checkFlashSupport(redirecturl);
			]]>
		</script>
		
		<form name="uploadForm" id="uploadForm" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/show{$relationType}Gallery/{gallery/sectionID}" enctype="multipart/form-data" method="post">
			              
            <div class="divNormal">
            	<h1 class="normalTableHeading" colspan="4" align="left">
					<xsl:value-of select="$multiUploader.header" /><xsl:text>&#x20;</xsl:text>"<xsl:value-of select="gallery/sectionName"/>"
				</h1>
				<xsl:apply-templates select="validationException/validationError"/>
				
				<div class="full">
					<p class="info"><img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/information.png" /><xsl:value-of select="$multiUploader.information" />.</p>
					<div style="width: 65%" class="floatleft fieldset flash" id="fsUploadProgress">
						<span class="legend"><xsl:value-of select="$multiUploader.queue" /></span>
						<div class="marginleft margintop floatright">
							<span id="spanButtonPlaceHolder"></span>
							<br />
							<input type="button" value="{$multiUploader.clearlist}" id="btnClear" onclick="swfu.cancelQueue();" disabled="disabled" />
						</div>
						<div id="queueMessage"><xsl:value-of select="$multiUploader.noimages" />.</div>
					</div>
					<br />
					<div style="clear: both" />
					
					<div style="margin-left: 5px" id="divStatus"><xsl:value-of select="$multiUploader.nouploaded" /></div>

					<div style="clear: both" />
					<div class="bigmargintop" style="width: 69%">
						<div class="floatright bigmargintop">
							<input type="button" value="{$multiUploader.btnSubmit}" id="btnSubmit" onclick="swfu.startUpload(); document.uploadForm.btnCancel.disabled = false" disabled="disabled" />
							<input class="bigmarginleft" id="btnCancel" type="button" value="{$multiUploader.btnCancel}" onclick="swfu.cancelQueue();" disabled="disabled" />
							<input class="bigmarginleft" type="submit" value="{$multiUploader.btnDone}" id="btnDone" />
						</div>
					</div>
				</div>
				
			</div>

			<script language="javascript">
				
				<![CDATA[
				var swfu;
		
					var settings = {
						flash_url : "]]><xsl:value-of select="/document/requestinfo/contextpath" /><![CDATA[/static/f/]]><xsl:value-of select="/document/module/sectionID" />/<xsl:value-of select="/document/module/moduleID" /><![CDATA[/js/swfupload/base/swfupload.swf",
						upload_url: "]]><xsl:value-of select="/document/requestinfo/currentURI" /><![CDATA[/]]><xsl:value-of select="/document/module/alias" /><![CDATA[/]]><xsl:value-of select="/document/group/groupID" /><![CDATA[/upload]]><xsl:value-of select="$relationType" /><![CDATA[/]]><xsl:value-of select="gallery/sectionID" /><![CDATA[;jsessionid=]]><xsl:value-of select="sessionID" /><![CDATA[",
						file_size_limit : "120 MB",
						file_types : "*.jpg;*.jpeg;*.gif;*.bmp;*.png",
						file_types_description : "All Files",
						file_upload_limit : 100,
						file_queue_limit : 0,
						custom_settings : {
							progressTarget : "fsUploadProgress",
							cancelButtonId : "btnCancel"
						},
						debug: false,
		
						// Button settings
						button_image_url : "]]><xsl:value-of select="/document/requestinfo/contextpath" /><![CDATA[/static/f/]]><xsl:value-of select="/document/module/sectionID" />/<xsl:value-of select="/document/module/moduleID" /><![CDATA[/js/swfupload/pics/]]><xsl:value-of select="$multiUploader.uploadbutton" /><![CDATA[",
						button_placeholder_id : "spanButtonPlaceHolder",
						// button_width: 61,
						button_width: 82,
						button_height: 22,
						
						// The event handler functions are defined in handlers.js
						file_queued_handler : fileQueued,
						file_queue_error_handler : fileQueueError,
						file_dialog_complete_handler : fileDialogComplete,
						upload_start_handler : uploadStart,
						upload_progress_handler : uploadProgress,
						upload_error_handler : uploadError,
						upload_success_handler : uploadSuccess,
						upload_complete_handler : uploadComplete,
						queue_complete_handler : queueComplete	// Queue plugin event
					};
		
					swfu = new SWFUpload(settings);
					]]>
					
			</script>

		</form>
	</xsl:template>
	
	<xsl:template match="pictureGallery">
		
		<div class="noBorderDiv">
			<p class="info">
				<img style="vertical-align: bottom" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/new.png" /> = <xsl:value-of select="$pictureGallery.new" />
			</p>
			<div class="divNormal">
				<h1 class="normalTableHeading" colspan="4" align="left"><xsl:value-of select="$pictureGallery.header" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="../group/name"/></h1>
			
				<xsl:choose>
					<xsl:when test="galleries/gallery[relationType = 'GROUP']">
						<xsl:apply-templates select="galleries/gallery[relationType = 'GROUP']" />
					</xsl:when>
					<xsl:otherwise>
						<p><xsl:value-of select="$pictureGallery.group.noalbum" /></p>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:if test="/document/isAdmin">
					<div style="clear: both;" />	
					<div class="addGalleries">
						<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addGroupGallery">
							<xsl:value-of select="$pictureGallery.group.add" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="/document/group/name" />
						</a>	
					</div>
				</xsl:if>
			</div>

		</div>	
		
		<div class="noBorderDiv">
			<div class="divNormal">
				<h1 class="normalTableHeading" colspan="4" align="left"><xsl:value-of select="$pictureGallery.header" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="../group/school/name"/></h1>
			
				<xsl:choose>
					<xsl:when test="galleries/gallery[relationType = 'SCHOOL']">
						<xsl:apply-templates select="galleries/gallery[relationType = 'SCHOOL']" />		
					</xsl:when>
					<xsl:otherwise>
						<p><xsl:value-of select="$pictureGallery.school.noalbum" /></p>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:if test="/document/isAdmin">
					<div class="addGalleries">
						<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addSchoolGallery">
							<xsl:value-of select="$pictureGallery.school.add" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="/document/group/school/name" />
						</a>	
					</div>
				</xsl:if>
			</div>
		</div>
		
	</xsl:template>	
	
	<xsl:template match="gallery">
	
		<xsl:variable name="relationType">
			<xsl:choose>
				<xsl:when test="relationType = 'GROUP'">
					<xsl:text>Group</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>School</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<div class="full floatleft borderbottom">
			<xsl:if test="/document/isAdmin">				
				<div class="margintop floatright">
					<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/update{$relationType}Gallery/{sectionID}" Title="{$pictureGallery.update.title} &quot;{sectionName}&quot;">
		 			<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png" alt=""/></a>
		 			<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/delete{$relationType}Gallery/{sectionID}" Title="{$pictureGallery.delete.title} &quot;{sectionName}&quot;" onclick="return confirmDelete('{$pictureGallery.delete.confirm} &quot;{sectionName}&quot;?')">
		 			<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" alt=""/></a>
				</div>
			</xsl:if>				
			
			<xsl:if test="randomFile">
				<div class="floatleft marginright margintop marginbottom" style="margin-right: 10px; margin-top: 5px; margin-bottom: 5px">
					<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/show{$relationType}Gallery/{sectionID}" Title="{$pictureGallery.show.title}">
			 		<img src="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/small{$relationType}Thumb/{sectionID}/{randomFile}" alt="{$pictureGallery.show.title}"/></a>
			 	</div>
			</xsl:if>

		 	<div class="marginleft">
				<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/show{$relationType}Gallery/{sectionID}" Title="{$pictureGallery.show.title}">
					<h3 class="linkarrow">
						<img style="margin-right: 3px" src="{/document/requestinfo/contextpath}/images/MenuArrowSingle.gif" /><xsl:text>&#x20;</xsl:text>	
						<xsl:value-of select="sectionName" /> (<xsl:value-of select="numPics" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="$pictureGallery.images" />)
						<xsl:if test="../../userLastLoginInMillis">
							<xsl:if test="postedInMillis > ../../userLastLoginInMillis">
								<img style="vertical-align: bottom" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/new.png" />
							</xsl:if>
						</xsl:if>
					</h3>	
				</a>
				
				<p>
					<xsl:call-template name="replaceLineBreak">
						<xsl:with-param name="string" select="description"/>
					</xsl:call-template>					
				</p>
			</div>
		</div>
	
	</xsl:template>
	
	<xsl:template match="showGallery">
		
		<xsl:variable name="relationType">
			<xsl:call-template name="getRelationType" >
				<xsl:with-param name="relationType" select="gallery/relationType" />
			</xsl:call-template>
		</xsl:variable>
		
		<div class="noBorderDiv">
			<div class="divNormal">
		  			<h1 class="normalTableHeading" colspan="4" align="left"><xsl:value-of select="gallery/sectionName"/> (<xsl:value-of select="gallery/numPics"/><xsl:text>&#x20;</xsl:text><xsl:value-of select="$showGallery.pictures" />)</h1>
	                 <table class="marginbottom">
	                     <tr>
	                         <td>
	                 			<xsl:value-of select="$showGallery.page" /><xsl:text>&#x20;</xsl:text><strong><xsl:value-of select="gallery/currentPage"/></strong><xsl:text>&#x20;</xsl:text><xsl:value-of select="$showGallery.pagecount" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="gallery/pages"/>
	                 		</td>
	                 		<td><xsl:text>&#x20;&#x20;|&#x20;&#x20;</xsl:text></td>
	                         <td>
	                 			<xsl:choose>
							<xsl:when test="gallery/prevPage">
								<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/show{$relationType}Gallery/{gallery/sectionID}/{gallery/prevPage}" title="Föregående" >
								<img alt="{$showGallery.previousLink.title}" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/left.gif" /><xsl:text>&#x20;&#x20;</xsl:text><xsl:value-of select="$showGallery.previousLink.text" /></a>
							</xsl:when>
							<xsl:otherwise>
								<div class="disabledtext"><img alt="{$showGallery.previousImage.alt}" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/left_light.gif" /><xsl:text>&#x20;&#x20;</xsl:text><xsl:value-of select="$showGallery.previousLink.text" /></div>
							</xsl:otherwise>
						</xsl:choose>
	                         </td>
	                         <td><xsl:text>&#x20;&#x20;|&#x20;&#x20;</xsl:text></td>
	                         <td>
	                             <xsl:choose>
							<xsl:when test="gallery/nextPage">
								<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/show{$relationType}Gallery/{gallery/sectionID}/{gallery/nextPage}" title="Nästa" >
								<xsl:value-of select="$showGallery.nextLink.text" /><xsl:text>&#x20;&#x20;</xsl:text><img alt="{$showGallery.nextImage.alt}" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/right.gif" /></a>
							</xsl:when>
							<xsl:otherwise>
								<div class="disabledtext"><xsl:value-of select="$showGallery.nextLink.text" /><xsl:text>&#x20;&#x20;</xsl:text><img alt="{$showGallery.nextImage.alt}" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/right_light.gif" /></div>
							</xsl:otherwise>
						</xsl:choose>
	                        </td>
	                        <td><xsl:text>&#x20;&#x20;|&#x20;&#x20;</xsl:text></td>
	                         
	                        <xsl:if test="/document/isAdmin='true'">
		                        <td>
			                          	<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/simple{$relationType}Uploader/{gallery/sectionID}" title="{$showGallery.addImagesLinkSimple.title}" >
										<xsl:value-of select="$showGallery.addImagesLinkSimple.text" />
								</a>
		                        </td>
		                        <td><xsl:text>&#x20;&#x20;|&#x20;&#x20;</xsl:text></td>
		                        <td>
			                          	<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/multi{$relationType}Uploader/{gallery/sectionID}" title="{$showGallery.addImagesLinkAdvanced.title}" >
										<xsl:value-of select="$showGallery.addImagesLinkAdvanced.text" />
								</a>
		                        </td>
		                        <td><xsl:text>&#x20;&#x20;|&#x20;&#x20;</xsl:text></td>
	                        </xsl:if>
	                         
	                        <td>
	                        	<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}" title="{$showGallery.showAllGalleriesLink.title}" >
									<xsl:value-of select="$showGallery.showAllGalleriesLink.text" />
								</a>
	                        </td>
	                     </tr>
	                 </table>
	                 
	                 <div class="full floatleft margintop">
	                 	<xsl:choose>
							<xsl:when test="gallery/files/file">
								<xsl:apply-templates select="gallery/files/file" mode="list"/>
							</xsl:when>
							<xsl:otherwise>
								<p><xsl:value-of select="$showGallery.noImagesInGallery" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="gallery/name"/></p>
							</xsl:otherwise>
						</xsl:choose>
					</div>	
			</div>
		</div>
	</xsl:template>
	
	<xsl:template match="gallery/files/file" mode="list">
		<xsl:variable name="relationType">
			<xsl:call-template name="getRelationType" >
				<xsl:with-param name="relationType" select="../../relationType" />
			</xsl:call-template>
		</xsl:variable>

		<div class="floatleft margintop marginbottom marginleft marginright">
		 	<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/show{$relationType}Image/{../../sectionID}/{fileID}" Title="{$file.link.title}">
		 	<img src="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/small{$relationType}Thumb/{../../sectionID}/{fileID}" alt=""/></a>
		</div>
	</xsl:template>
	
	<xsl:template match="showImage">
	
		<xsl:variable name="relationType">
			<xsl:call-template name="getRelationType" >
				<xsl:with-param name="relationType" select="gallery/relationType" />
			</xsl:call-template>
		</xsl:variable>

        <div class="noBorderDiv">
			<div class="divNormal">
		  		<h1 class="normalTableHeading" colspan="4" align="left"><xsl:value-of select="gallery/sectionName"/> (<xsl:value-of select="gallery/numPics"/><xsl:text>&#x20;</xsl:text><xsl:value-of select="$showImage.pictures" />)</h1> 
         
		        <xsl:if test="/document/isAdmin='true'">
			        <div class="floatright">
					<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/delete{$relationType}Image/{gallery/sectionID}/{gallery/file/fileID}" Title="{$showImage.deleteImageLink.title} &quot;{gallery/file/filename}&quot;" onclick="return confirmDelete('{$showImage.deleteImageLink.confirm} &quot;{gallery/file/filename}&quot;?')">
					<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" alt=""/></a>
				 	</div>
		        </xsl:if>
		         
		         <table class="marginbottom">
		         	<tr>
		            	<td>
		             		<xsl:value-of select="gallery/file/filename"/><xsl:text>&#x20;</xsl:text><xsl:value-of select="$showImage.picture" /><xsl:text>&#x20;</xsl:text><strong><xsl:value-of select="gallery/currentPic"/></strong><xsl:text>&#x20;</xsl:text><xsl:value-of select="$showImage.pictureCount" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="gallery/numPics"/>
		             	</td>
		             	<td><xsl:text>&#x20;&#x20;|&#x20;&#x20;</xsl:text></td>
		                <td>
		             		<xsl:choose>
								<xsl:when test="gallery/prevImage">
									<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/show{$relationType}Image/{gallery/sectionID}/{gallery/prevImage}" title="{$showImage.previousLink.title}" >
									<img alt="{$showImage.previousImage.alt}" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/left.gif" /><xsl:text>&#x20;&#x20;</xsl:text><xsl:value-of select="$showImage.previousLink.text" /></a>
								</xsl:when>
								<xsl:otherwise>
									<div class="disabledtext"><img alt="{$showImage.previousImage.alt}" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/left_light.gif" /><xsl:text>&#x20;&#x20;</xsl:text><xsl:value-of select="$showImage.previousLink.text" /></div>
								</xsl:otherwise>
							</xsl:choose>
		               	</td>
		                <td><xsl:text>&#x20;&#x20;|&#x20;&#x20;</xsl:text></td>
		                <td>
			                <xsl:choose>
								<xsl:when test="gallery/nextImage">
									<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/show{$relationType}Image/{gallery/sectionID}/{gallery/nextImage}" title="{$showImage.nextLink.title}" >
									<xsl:value-of select="$showImage.nextLink.text" /><xsl:text>&#x20;&#x20;</xsl:text><img alt="{$showImage.nextImage.alt}" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/right.gif" /></a>
								</xsl:when>
								<xsl:otherwise>
									<div class="disabledtext"><xsl:value-of select="$showImage.nextLink.text" /><xsl:text>&#x20;&#x20;</xsl:text><img alt="{$showImage.nextImage.alt}" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/right_light.gif" /></div>
								</xsl:otherwise>
							</xsl:choose>
		                </td>
		                <td><xsl:text>&#x20;&#x20;|&#x20;&#x20;</xsl:text></td>
		                <td>
		                    <a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/show{$relationType}Gallery/{gallery/sectionID}/1" title="{$showImage.showThumbsLink.title}" ><xsl:value-of select="$showImage.showThumbsLink.text" /></a>
		                </td>
		                <td><xsl:text>&#x20;&#x20;|&#x20;&#x20;&#x20;&#x20;</xsl:text></td>
		             </tr>
				</table>
				
		        <div class="full marginleft margintop">
		        	<xsl:choose>
						<xsl:when test="gallery/file">
							<xsl:apply-templates select="gallery/file"/> 
						</xsl:when>
					</xsl:choose>			
		     	</div>     
	 
	     	</div>
     
     	</div>
     	   	
	</xsl:template>
	
	<xsl:template match="gallery/file">
	
		<xsl:variable name="relationType">
			<xsl:call-template name="getRelationType" >
				<xsl:with-param name="relationType" select="../relationType" />
			</xsl:call-template>
		</xsl:variable>
	
		 <a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/get{$relationType}Image/{../sectionID}/{fileID}" Target="_blank" Title="{$gallery.file.showFullImageLink.title}">
		 <img src="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/medium{$relationType}Thumb/{../sectionID}/{fileID}" alt=""/></a>
		 <xsl:if test="../../allowComments = 'true'">
			 <div class="full">
				<div>
					<div class="floatleft">
						<table>
					 		<tr>
								<td>
									<b> <xsl:value-of select="$gallery.file.comments" /><xsl:text>&#x20;</xsl:text>(<xsl:choose>
										<xsl:when test="comments/commentsNum">
											<xsl:value-of select="comments/commentsNum"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:text>0</xsl:text>
										</xsl:otherwise>
									</xsl:choose>)</b>
								</td>
								
								<xsl:choose>
									<xsl:when test="comments/commentsNum > 0">
										<td><xsl:text>&#x20;&#x20;|&#x20;&#x20;</xsl:text></td>
										<td>    
											<form name="viewComments" method="post" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/show{$relationType}Image/{../sectionID}/{fileID}">
							    				<input type="hidden" name="viewComments" />	
								    			<xsl:choose>
									    			<xsl:when test="comments/showAll">
									    					<a href="javascript:viewComments('false')" title="{$gallery.file.hide.comments}"><xsl:value-of select="$gallery.file.hide.comments" /></a>
									    			</xsl:when>
									    			<xsl:otherwise>
										    				<a href="javascript:viewComments('true')" title="{$gallery.file.show.comments}"><xsl:value-of select="$gallery.file.show.comments" /></a>
									    			</xsl:otherwise>
								    			</xsl:choose>
							    			</form>
										</td>
									</xsl:when>
								</xsl:choose>
					 		</tr>
						</table>
					</div>
					<br /><br />
				</div>
				<xsl:choose>
					<xsl:when test="comments/commentsNum > 0">
						<xsl:apply-templates select="comments/comment"/>
					</xsl:when>
					<xsl:otherwise>
						<div class="bordertop full">
							<p><xsl:value-of select="$gallery.file.noComments" /></p>
						</div>
					</xsl:otherwise>
				</xsl:choose>
			</div>
			
			<xsl:if test="../../allowComments">
				<form name="addCommentText" method="post">
					<xsl:apply-templates select="validationException/validationError" />
					<xsl:value-of select="$gallery.file.addcomment" />
					<textarea name="commentText" rows="2" cols="88" style="width: 99%;" />
					<div class="floatright margintop">
						<input type="submit" value="{$gallery.file.submit}"/>
					</div>
				</form>
			</xsl:if>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="comment">
		
		<xsl:variable name="relationType">
			<xsl:call-template name="getRelationType" >
				<xsl:with-param name="relationType" select="../../../relationType" />
			</xsl:call-template>
		</xsl:variable>
		
		<div class="floatleft bordertop full marginbottom">
			<div class="marginleft">
				<xsl:if test="/document/isAdmin = 'true'">
					<div class="margintop floatright">
			 			<a href="javascript:hideShow('showComment_{commentID}');hideShow('updateComment_{commentID}');" Title="{$comment.updateCommentLink.title}">
			 			<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png" alt=""/></a>			 			
			 			<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/delete{$relationType}Comment/{commentID}" Title="{$comment.deleteCommentLink.title}">
			 			<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" alt=""/></a>
					</div>
				</xsl:if>
				<div id="showComment_{commentID}">
					<p>
						<xsl:call-template name="replaceLineBreak">
							<xsl:with-param name="string" select="comment"/>
						</xsl:call-template>
					</p>
				</div>
				<xsl:if test="/document/isAdmin = 'true'">
					<div id="updateComment_{commentID}" style="display: none;">
						<form method="POST" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/update{$relationType}Comment/{commentID}">
							<textarea name="comment" cols="88" style="width: 99%">
								<xsl:call-template name="replace-string">
									<xsl:with-param name="text" select="comment"/>
									<xsl:with-param name="from" select="'&#13;'"/>
									<xsl:with-param name="to" select="''"/>
								</xsl:call-template>						
							</textarea>
							
							<div class="floatright margintop">
								<input type="submit" value="{$comment.submit}"/>
							</div>
							<br/>
							<br/>					
						</form>
					</div>
				</xsl:if>				
			</div>
			<div class="floatright">
				<i>
					<xsl:choose>
						<xsl:when test="user">
							<xsl:value-of select="user" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$comment.anonymousUser" />
						</xsl:otherwise>
					</xsl:choose>
				</i>
				<br/>
				<xsl:value-of select="date" />
			</div>
		</div>
	</xsl:template>
	
	
	<xsl:template match="addGallery">
	
		<xsl:variable name="relationType">
			<xsl:call-template name="getRelationType" >
				<xsl:with-param name="relationType" select="relationType" />
			</xsl:call-template>
		</xsl:variable>
		
		<form name="addGallery" method="post" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/add{$relationType}Gallery" enctype="multipart/form-data">
          
            <div class="divNormal">
            	<h1 class="normalTableHeading" colspan="4" align="left">
					<xsl:value-of select="$addGallery.header" /><xsl:text>&#x20;</xsl:text>
					<xsl:choose>
						<xsl:when test="$relationType = 'Group'">
							<xsl:value-of select="$addGallery.group" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="/document/group/name" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$addGallery.school" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="/document/group/school/name" />
						</xsl:otherwise>
					</xsl:choose>
				</h1>
				<xsl:apply-templates select="validationException/validationError"/>
				<p>
					<xsl:value-of select="$addGallery.name" />:
					<br />
					<input type="text" name="name" size="72" value="{requestparameters/parameter[name='name']/value}"/>
				</p>
				<p>
					<xsl:value-of select="$addGallery.description" />:
					<br/>
					<textarea name="description" cols="54">
						<xsl:call-template name="replace-string">
							<xsl:with-param name="text" select="requestparameters/parameter[name='description']/value"/>
							<xsl:with-param name="from" select="'&#13;'"/>
							<xsl:with-param name="to" select="''"/>
						</xsl:call-template>
					</textarea>
				</p>
<!-- 			<p>
					<input type="checkbox" name="uploadCheck" onclick="document.addGallery.zipFile.disabled=!this.checked">
						<xsl:if test="requestparameters/parameter[name='uploadCheck'][value='on']">
							<xsl:attribute name="checked">true</xsl:attribute>
						</xsl:if>
					</input>
					Ladda upp bilder (zip)<br />
					<input type="file" name="zipFile" size="59" disabled="true" value="{requestparameters/parameter[name='zipFile']/value}"/>
					<xsl:if test="requestparameters/parameter[name='uploadCheck'][value='on']">
						<script>
							enableUpload('true');
						</script>
					</xsl:if>
				</p>		
-->				
				<div class="floatright">
					<input type="submit" value="{$addGallery.submit}" />		
				</div>
			</div>
		</form>
	</xsl:template>
	
	<xsl:template match="updateGallery">	
		
		<xsl:variable name="relationType">
			<xsl:call-template name="getRelationType" >
				<xsl:with-param name="relationType" select="gallery/relationType" />
			</xsl:call-template>
		</xsl:variable>
			
		<form name="updateForm" method="post" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/update{$relationType}Gallery/{gallery/sectionID}">
		             
            <div class="divNormal">
            	<h1 class="normalTableHeading" colspan="4" align="left">
					<xsl:value-of select="$updateGallery.header" /><xsl:text>&#x20;</xsl:text>"<xsl:value-of select="gallery/sectionName"/>"
				</h1>
				
				<xsl:apply-templates select="validationException/validationError" />
				<p>
					<xsl:value-of select="$updateGallery.name" />:
					<br />
					<input type="text" name="name" size="72">
						<xsl:attribute name="value">
							<xsl:choose>
								<xsl:when test="requestparameters/parameter[name='name']/value">
									<xsl:value-of select="requestparameters/parameter[name='name']/value"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="gallery/sectionName"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:attribute>
					</input>
				</p>
				<p>
					<xsl:value-of select="$updateGallery.description" />:
					<br/>
					<textarea name="description" cols="54">
						<xsl:choose>
							<xsl:when test="requestparameters/parameter[name='description']/value">
								<xsl:call-template name="replace-string">
									<xsl:with-param name="text" select="requestparameters/parameter[name='description']/value"/>
									<xsl:with-param name="from" select="'&#13;'"/>
									<xsl:with-param name="to" select="''"/>
								</xsl:call-template>									
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="replace-string">
									<xsl:with-param name="text" select="gallery/description"/>
									<xsl:with-param name="from" select="'&#13;'"/>
									<xsl:with-param name="to" select="''"/>
								</xsl:call-template>								
							</xsl:otherwise>
						</xsl:choose>
					</textarea>
				</p>

				<div class="floatright">
					<input type="submit" value="{$updateGallery.submit}"/>			
				</div>
			</div>
		</form>		
	</xsl:template>
	
	<xsl:template match="addImages">	
	
		<xsl:variable name="relationType">
			<xsl:call-template name="getRelationType" >
				<xsl:with-param name="relationType" select="gallery/relationType" />
			</xsl:call-template>
		</xsl:variable>
	
		<form name="addImages" method="post" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/simple{$relationType}Uploader/{gallery/sectionID}" enctype="multipart/form-data">
           
            <div class="divNormal">
            	<h1 class="normalTableHeading" colspan="4" align="left">
					<xsl:value-of select="$addImages.header" /><xsl:text>&#x20;</xsl:text>"<xsl:value-of select="gallery/sectionName"/>"
				</h1>
				<xsl:apply-templates select="validationException/validationError"/>
				<xsl:if test="noflash = 'true'">
					<p class="error">
						<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/information.png" alt="" />
						<xsl:value-of select="$addImages.information.part1" />.<xsl:text>&#x20;</xsl:text><xsl:value-of select="$addImages.information.click" /><xsl:text>&#x20;</xsl:text><b><a href="http://get.adobe.com/flashplayer/?promoid=DXLUJ" target="_blank" ><xsl:value-of select="addImages.information.link" /></a></b><xsl:text>&#x20;</xsl:text><xsl:value-of select="$addImages.information.part2" />.
					</p>
				</xsl:if>
				<p>
					<xsl:value-of select="$addImages.upload" /><br />
					<input type="file" name="fileUpload" size="59" value="{requestparameters/parameter[name='file']/value}"/>
				</p>
				<div class="floatright">
					<input type="submit" value="{$addImages.submit}"/>
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
	
	<xsl:template name="getRelationType">
		<xsl:param name="relationType" />
		<xsl:choose>
			<xsl:when test="$relationType = 'GROUP'">
				<xsl:text>Group</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>School</xsl:text>
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
					<xsl:when test="validationErrorType='Other'">
						<xsl:value-of select="$validationError.Other" />
					</xsl:when>	
					<xsl:when test="validationErrorType='TooLong'">
						<xsl:value-of select="$validation.TooLong" />
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
					<xsl:when test="messageKey='NoImage'">
						<xsl:value-of select="$validationError.messageKey.NoImage" />!
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$validationError.unknownMessageKey" />!
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
	
		<xsl:apply-templates select="message"/>	
	</xsl:template>
	
	
</xsl:stylesheet>