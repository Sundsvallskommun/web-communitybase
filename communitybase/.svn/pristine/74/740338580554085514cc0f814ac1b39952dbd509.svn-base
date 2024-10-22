<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="modulePath"><xsl:value-of select="/Document/requestinfo/currentURI" />/<xsl:value-of select="/Document/module/alias" />/<xsl:value-of select="/Document/group/groupID" /></xsl:variable>
	<xsl:variable name="moduleImagePath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/pics</xsl:variable>
	<xsl:variable name="moduleScriptPath"><xsl:value-of select="/Document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/Document/module/sectionID" />/<xsl:value-of select="/Document/module/moduleID" />/js</xsl:variable>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/utils/treeview/jquery.cookie.js
		/utils/treeview/jquery.jstree.custom.js
		/utils/treeview/communitybase.treeview.js
		/utils/js/communitybase.common.js
		/js/uploadify/swfobject.js
		/js/uploadify/jquery.uploadify.v2.1.4.js
		/utils/js/confirmDelete.js
		/js/gallery.js
		/js/gallery-sortable.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/js/uploadify/css/uploadify.css
		/utils/treeview/themes/communitybase/style.css
	</xsl:variable>
	
	
	<xsl:template match="Document">
	
		<script type="text/javascript" src="{$moduleScriptPath}/uploadify/languages/{$i18n.multiUploader.language}"></script>
		
		<div class="contentitem">
		
			<h1>
				<xsl:value-of select="/Document/module/name" />
				<xsl:text>&#x20;</xsl:text>
				<xsl:value-of select="$i18n.for" />:
				<xsl:text>&#x20;</xsl:text>
				<xsl:value-of select="/Document/group/name" />
			</h1>
			
			<xsl:apply-templates select="ListGallerys" />
			<xsl:apply-templates select="AddGallery" />
			<xsl:apply-templates select="UpdateGallery" />
			<xsl:apply-templates select="ShowGallery" />
			
			<xsl:apply-templates select="ListPictures" />
			<xsl:apply-templates select="AddPicture" />
			<xsl:apply-templates select="ShowPicture" />
			
			<xsl:apply-templates select="simpleUploader" />
			<xsl:apply-templates select="multiUploader" />
			
		</div>	

	</xsl:template>
	
	
	<!-- List galleries -->
	<xsl:template match="ListGallerys">
	
		<div class="floatleft marginbottom">
			<p class="info">
				<img src="{$moduleImagePath}/new.png" />
				<xsl:text>&#160;=&#160;</xsl:text>
				<xsl:value-of select="$i18n.album.newNotification" />.
			</p>
		</div>
		
		<xsl:if test="validationError">
			<div class="floatleft">
				<xsl:apply-templates select="validationError"/>
			</div>
		</xsl:if>
	
		<div class="floatright">
			<div class="floatright marginleft">
				<xsl:call-template name="addReverseSortingCheckbox"/>
			</div>
			<div class="floatright">
				<xsl:call-template name="addSortingCriteriasDropdown"/>
			</div>
		</div>
		
		<xsl:apply-templates select="Categories/Category"/>
				
		<xsl:if test="../isAdmin or ../isSysAdmin">
			<div class="floatleft full bigmarginbottom">
				<div class="text-align-right">
					<a href="{$modulePath}/addGallery">
						<xsl:value-of select="$i18n.AddAlbum" />
					</a>	
				</div>
			</div>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="Category">
	
		<h2 class="clearboth">
			<xsl:value-of select="name"/>
		</h2>
	
		<xsl:apply-templates select="Gallerys/Gallery" mode="list"/>
		
	</xsl:template>
	
	<!-- Gallery -->
	<xsl:template match="Gallery" mode="list">
		
		<div class="content-box">
			
			<a name="{sectionID}"/>
			
			<h1 class="header">
				<span>
					<div class="display-inline-block full">
						<div class="floatleft">
							<xsl:value-of select="name"/>
						</div>
					
						<!-- Icons -->
						<div class="floatright">
								
							<!-- CRUD links -->
							<xsl:if test="hasUpdateAccess">				
							<div class="floatright marginleft">
								<a href="{$modulePath}/deleteGallery/{galleryID}" Title="{$i18n.DeleteAlbum} &quot;{name}&quot;" onclick="return confirmDelete('{$i18n.album.deleteConfirmation} &quot;{name}&quot;?')">
			 						<img src="{$moduleImagePath}/delete.png" alt=""/>
			 					</a>
							</div>	
							<div class="floatright marginleft">
								<a href="{$modulePath}/updateGallery/{galleryID}" Title="{$i18n.EditAlbum} &quot;{name}&quot;">
									<img src="{$moduleImagePath}/edit.png" alt=""/>
								</a>
			 				</div>
							</xsl:if>
				
				
							<!-- Download link -->
							<xsl:if test="numPics > 0 and ../../allowsGalleryDownload = 'true'">
							<div class="floatright">
								<a href="{$modulePath}/downloadGallery/{galleryID}" title="{$i18n.DownloadAlbum} &quot;{name}&quot; {$i18n.asAZipFile}">
									<img src="{$moduleImagePath}/zip.png" />	
								</a>
							</div>
							</xsl:if>
						</div>
					</div>
				</span>
			</h1>
			<div class="content">
			
				<!-- Date and visability -->
				<div class="marginleft floatright">
					<div class="addedBy">
						<xsl:value-of select="$i18n.ShownFor" />:<xsl:text>&#x20;</xsl:text>
						<xsl:call-template name="createPublishingInformation">
							<xsl:with-param name="element" select="." />
							<xsl:with-param name="id" select="galleryID" />
							<xsl:with-param name="moduleImagePath" select="$moduleImagePath" />
							<xsl:with-param name="displayInline" select="'true'" />
						</xsl:call-template>
					</div>
				</div>
				
				<div class="floatright">
					<div class="addedBy">
						<xsl:value-of select="$i18n.Created" />: 
						<xsl:value-of select="postedDate"/>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$i18n.atTime" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="postedTime"/>
						<xsl:text>.</xsl:text>
					</div>
				</div>
			
				<div class="floatleft margin">
			
					<!-- Random thumb -->
					<xsl:if test="randomPic">
						<div class="floatleft bigmarginright">
							<a href="{$modulePath}/showGallery/{galleryID}" Title="{$i18n.ShownAlbum}">
					 			<img src="{$modulePath}/smallThumb/{randomPic}" alt="{$i18n.ShownAlbum}" />
				 			</a>
				 		</div>
					</xsl:if>
			
					<!-- Gallery name and description -->
					<div class="floatleft bigmargintop">
						<a href="{$modulePath}/showGallery/{galleryID}" Title="{$i18n.ShownAlbum}">
							<h3 style="margin-top: 0">
								<img style="margin-right: 3px" src="{/Document/requestinfo/contextpath}/images/MenuArrowSingle.gif" /><xsl:text>&#x20;</xsl:text>	
								<xsl:value-of select="name" /> (<xsl:value-of select="numPics" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="$i18n.pictures" />)
								<xsl:if test="../../userLastLoginInMillis">
									<xsl:if test="postedInMillis > ../../userLastLoginInMillis">
										<img src="{$moduleImagePath}/new.png" class="vertical-align-bottom marginleft" />
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
				
			</div>
		</div>
	</xsl:template>
	
	
	<!-- Multi uploader -->
	<xsl:template match="multiUploader">
		
		<script type="text/javascript">
			noFlashURI = '<xsl:value-of select="$modulePath" />/simpleUploader/<xsl:value-of select="Gallery/galleryID" />/noflash/' + REQUIRED_FLASH_VERSION;
			swfURI = '<xsl:value-of select="$moduleScriptPath" />/uploadify/uploadify.swf';
			uploadURI = '<xsl:value-of select="$modulePath" />/upload/<xsl:value-of select="Gallery/galleryID" />;jsessionid=<xsl:value-of select="sessionID" />'
			imageURI = '<xsl:value-of select="$moduleScriptPath" />/uploadify/pics';
			buttonImage = '<xsl:value-of select="$i18n.multiUploader.uploadbutton" />';
			sizeLimit = <xsl:value-of select="diskThreshold" />;
		</script>
		
		<form name="uploadForm" id="uploadForm" action="{$modulePath}/showGallery/{Gallery/galleryID}" enctype="multipart/form-data" method="post">
		
			 <div class="content-box">
            	<h1 class="header">
					<span><xsl:value-of select="$i18n.AddPicturesToAlbum" /><xsl:text>&#x20;</xsl:text>"<xsl:value-of select="Gallery/name"/>"</span>
				</h1>
				
				<div class="content">
					<xsl:apply-templates select="validationException/validationError"/>
				
					<div class="full uploadify">
						
						<div class="margintop floatleft">
							<div class="floatleft"><input id="file_upload" type="file" class="floatleft" name="file_upload" /></div>
							<button id="clear_button" type="button" class="floatleft marginleft" onclick="$('#file_upload').uploadifyClearQueue();" disabled="disabled"><i/><xsl:value-of select="$i18n.ClearList" /></button>
							<button id="upload_button" type="button" class="floatleft marginleft" onclick="$('#file_upload').uploadifyUpload(); $('#upload_button').attr('disabled','disabled'); $('#cancel_button').removeAttr('disabled');" disabled="disabled"><i/><xsl:value-of select="$i18n.UploadImages" /></button>
							<button id="cancel_button" class="floatleft marginleft" type="button" onclick="$('#file_upload').uploadifyClearQueue();" disabled="disabled"><i/><xsl:value-of select="$i18n.CancelUpload" /></button>
						</div>
						
						<div class="floatleft margintop full">
							<div id="custom-queue" class="uploadifyQueue">
								<div class="header"><xsl:value-of select="$i18n.SelectedImagesOnQueue" />:</div>
								<div id="queue-message"></div>
							</div>
						</div>
						
						<div class="floatleft full">
							<div class="floatright bigmargintop">
								<input id="done_button" type="submit" value="{$i18n.Back}" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</xsl:template>
	
	
	<!-- Show Gallery --> 
	<xsl:template match="ShowGallery">
		<div class="content-box">
			<h1 class="header">
				<span>
					<xsl:value-of select="Gallery/name" />
					<xsl:text>&#x20;</xsl:text>
					<xsl:text>(</xsl:text>
					<xsl:value-of select="numPics"/>
					<xsl:text>&#x20;</xsl:text>
					<xsl:value-of select="$i18n.pictures" />
					<xsl:text>)</xsl:text>
				</span>
			</h1>
			
			<div class="content">
				<table class="marginbottom">
					<tr>
						<td>
							<xsl:value-of select="$i18n.Page" />
							<xsl:text>&#x20;</xsl:text>
							<strong><xsl:value-of select="currentPage"/></strong>
							<xsl:text>&#x20;</xsl:text>
							<xsl:value-of select="$i18n.of" />
							<xsl:text>&#x20;</xsl:text>
							<xsl:value-of select="pages"/>
						</td>
						<td>
		                 	<xsl:text>&#x20;&#x20;|&#x20;&#x20;</xsl:text>
						</td>
						<td>
							<xsl:choose>
								<xsl:when test="prevPage">
									<a href="{$modulePath}/showGallery/{Gallery/galleryID}/{prevPage}" title="{$i18n.Previous}" >
										<img alt="{$i18n.Previous}" src="{$moduleImagePath}/left.gif" />
										<xsl:text>&#x20;&#x20;</xsl:text>
										<xsl:value-of select="$i18n.Previous" />
									</a>
								</xsl:when>
								<xsl:otherwise>
									<div class="disabledtext">
										<img alt="{$i18n.Previous}" src="{$moduleImagePath}/left_light.gif" />
										<xsl:text>&#x20;&#x20;</xsl:text>
										<xsl:value-of select="$i18n.Previous" />
									</div>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td>
							<xsl:text>&#x20;&#x20;|&#x20;&#x20;</xsl:text>
						</td>
						<td>
							<xsl:choose>
								<xsl:when test="nextPage">
									<a href="{$modulePath}/showGallery/{Gallery/galleryID}/{nextPage}" title="{$i18n.Next}" >
										<xsl:value-of select="$i18n.Next" />
										<xsl:text>&#x20;&#x20;</xsl:text>
										<img alt="{$i18n.Next}" src="{$moduleImagePath}/right.gif" />
									</a>
								</xsl:when>
								<xsl:otherwise>
									<div class="disabledtext">
										<xsl:value-of select="$i18n.Next" />
										<xsl:text>&#x20;&#x20;</xsl:text>
										<img alt="{$i18n.Next}" src="{$moduleImagePath}/right_light.gif" />
									</div>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td>
							<xsl:text>&#x20;&#x20;|&#x20;&#x20;</xsl:text>
						</td>
						<xsl:if test="hasUpdateAccess">
							<td>
								<a href="{$modulePath}/simpleUploader/{Gallery/galleryID}" title="{$i18n.AddImagesSimple}" >
									<xsl:value-of select="$i18n.AddImagesSimple" />
								</a>
							</td>
							<td>
								<xsl:text>&#x20;&#x20;|&#x20;&#x20;</xsl:text>
							</td>
							<td>
								<a href="{$modulePath}/multiUploader/{Gallery/galleryID}" title="{$i18n.AddImagesAdvanced}" >
									<xsl:value-of select="$i18n.AddImagesAdvanced" />
								</a>
							</td>
							<td>
								<xsl:text>&#x20;&#x20;|&#x20;&#x20;</xsl:text>
							</td>
						</xsl:if>
						<td>
							<a href="{$modulePath}" title="{$i18n.ShowAllAlbums}" >
								<xsl:value-of select="$i18n.ShowAllAlbums" />
							</a>
						</td>
						<xsl:if test="numPics > 0 and allowsGalleryDownload = 'true'">
							<td>
								<xsl:text>&#x20;|&#x20;</xsl:text>
							</td>
							<td>
								<a href="{$modulePath}/downloadGallery/{Gallery/galleryID}" title="{$i18n.DownloadAlbum} &quot;{Gallery/name}&quot; {$i18n.asAZipFile}">
									<img src="{$moduleImagePath}/zip.png" />
								</a>
							</td>
						</xsl:if>
					</tr>
				</table>

				<!-- Display page pictures (a subset of all pictures in gallery) -->
				<div class="full floatleft margintop">
					<xsl:choose>
						<xsl:when test="Gallery/pictures/Picture">
							<xsl:apply-templates select="Gallery/pictures/Picture[pictureID = current()/DisplayPictures/pictureID]" mode="listByAlbum"/>
						</xsl:when>
						<xsl:otherwise>
							<p>
								<xsl:value-of select="$i18n.NoPicturesInAlbum" />
								<xsl:text>&#x20;</xsl:text>
								<xsl:value-of select="Gallery/name"/>
							</p>
						</xsl:otherwise>
					</xsl:choose>
				</div>	
			</div>
		</div>
	</xsl:template>
	
	
	<!-- List Pictures -->
	<xsl:template match="ListPictures">
		<xsl:variable name="sortingCriteria">
			<xsl:if test="requestparameters/parameter[name = 'orderby']">
				<xsl:value-of select="requestparameters/parameter[name = 'orderby']/value"/>
			</xsl:if>
		</xsl:variable>
		<xsl:variable name="reverse">
			<xsl:if test="requestparameters/parameter[name = 'reverse']/value = 'true'">
				<xsl:value-of select="'true'"/>
			</xsl:if>
		</xsl:variable>
		
		<div class="floatleft marginbottom">
			<p class="info">
				<img src="{$moduleImagePath}/new.png" />
				<xsl:text>&#160;=&#160;</xsl:text>
				<xsl:value-of select="$i18n.picture.newNotification" />.
			</p>
		</div>
		
		<div class="floatright">
			<div class="floatright marginleft">
				<xsl:call-template name="addReverseSortingCheckbox"/>
			</div>
			<div class="floatright">
				<xsl:call-template name="addSortingCriteriasDropdown"/>
			</div>
		</div>
		
		<div class="content-box">
			<h1 class="header">
				<span>
					<xsl:value-of select="$i18n.AllPictures"/>
					<xsl:text>&#160;</xsl:text>
					<xsl:text>(</xsl:text>
					<xsl:value-of select="numPics"/>
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="$i18n.pictures"/>
					<xsl:text>)</xsl:text>
					<xsl:if test="numPics > 0 and allowsGalleryDownload = 'true'">
						<div class="floatright">
							<a href="{$modulePath}/downloadPictures" title="{$i18n.DownloadAllPictures} {$i18n.asAZipFile}">
								<img src="{$moduleImagePath}/zip.png" />
							</a>
						</div>
					</xsl:if>
				</span>
			</h1>
			<div class="content">
			
				<!-- Pagination -->
				<table class="marginbottom">
					<tr>
						<td>
							<xsl:value-of select="$i18n.Page" />
							<xsl:text>&#x20;</xsl:text>
							<strong><xsl:value-of select="currentPage"/></strong>
							<xsl:text>&#x20;</xsl:text>
							<xsl:value-of select="$i18n.of" />
							<xsl:text>&#x20;</xsl:text>
							<xsl:value-of select="pages"/>
						</td>
						<td>
		                 	<xsl:text>&#x20;&#x20;|&#x20;&#x20;</xsl:text>
						</td>
						<td>
							<xsl:choose>
								<xsl:when test="prevPage">
									<a title="{$i18n.Previous}" href="{$modulePath}/pictures/{prevPage}" >
										<img alt="{$i18n.Previous}" src="{$moduleImagePath}/left.gif" />
										<xsl:text>&#x20;&#x20;</xsl:text>
										<xsl:value-of select="$i18n.Previous" />
									</a>
								</xsl:when>
								<xsl:otherwise>
									<div class="disabledtext">
										<img alt="{$i18n.Previous}" src="{$moduleImagePath}/left_light.gif" />
										<xsl:text>&#x20;&#x20;</xsl:text>
										<xsl:value-of select="$i18n.Previous" />
									</div>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td>
							<xsl:text>&#x20;&#x20;|&#x20;&#x20;</xsl:text>
						</td>
						<td>
							<xsl:choose>
								<xsl:when test="nextPage">
									<a href="{$modulePath}/pictures/{nextPage}" title="{$i18n.Next}" >
										<xsl:value-of select="$i18n.Next" />
										<xsl:text>&#x20;&#x20;</xsl:text>
										<img alt="{$i18n.Next}" src="{$moduleImagePath}/right.gif" />
									</a>
								</xsl:when>
								<xsl:otherwise>
									<div class="disabledtext">
										<xsl:value-of select="$i18n.Next" />
										<xsl:text>&#x20;&#x20;</xsl:text>
										<img alt="{$i18n.Next}" src="{$moduleImagePath}/right_light.gif" />
									</div>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
				</table>
				
				<!-- Pictures -->
				<xsl:choose>
					<xsl:when test="Pictures/Picture">
						<xsl:apply-templates select="Pictures/Picture[pictureID = current()/DisplayPictures/pictureID]" mode="list">
							<xsl:with-param name="page" select="currentPage" />
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<div class="floatleft full margintop marginbottom hover">
							<xsl:value-of select="$i18n.NoPictures"/>
						</div>
					</xsl:otherwise>
				</xsl:choose>
			</div>
		</div>
	</xsl:template>
	
	
	<!-- Picture by album -->
	<xsl:template match="Picture" mode="listByAlbum">
		<div class="floatleft margintop marginbottom marginleft marginright">
		 	<a href="{$modulePath}/showPicture/{pictureID}" Title="{$i18n.ShowLargerPicture}">
		 		<img src="{$modulePath}/smallThumb/{pictureID}" alt=""/>
		 	</a>
		</div>
	</xsl:template>
	
	
	<!-- Pictures in flat hierarchy -->
	<xsl:template match="Picture" mode="list">
		<xsl:param name="page"/>
		<div class="floatleft full marginleft margintop marginbottom hover">
			
			<div class="floatleft bigmarginright" style="text-align: center;">
				<a href="{$modulePath}/showPicture/{pictureID}/{$page}" Title="{$i18n.ShowLargerPicture}">
			 		<img src="{$modulePath}/smallThumb/{pictureID}" alt=""/>
			 	</a>
			 </div>
			 
			 <div class="floatleft">
			 
			 	<!-- Filename & new image icon -->
			 	<div class="floatleft">
				 	
				 	<a href="{$modulePath}/showPicture/{pictureID}/{$page}" Title="{$i18n.ShowLargerPicture}">
				 		<xsl:value-of select="filename"/>
				 	</a>
				 	
				 	<xsl:if test="postedInMillis > ../../userLastLoginInMillis">
						<xsl:text>&#160;</xsl:text>
						<img src="{$moduleImagePath}/new.png" class="vertical-align-bottom" />
					</xsl:if>
					
				</div>
				
				<!-- Gallery link -->
				<div class="floatleft clearboth">
			 		<p class="tiny">
						<xsl:value-of select="$i18n.BelongsTo"/>
						<xsl:text>&#160;</xsl:text>
						<a href="{$modulePath}/showGallery/{Gallery/galleryID}"><xsl:value-of select="Gallery/name"/></a>
					</p>
				</div>
				
				<!-- Add by and when -->
				<div class="floatleft marginbottom clearboth">
					<p>
						<xsl:value-of select="$i18n.UploadedBy" />:
						<xsl:choose>
							<xsl:when test="poster/user">
								<xsl:value-of select="poster/user/firstname"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="poster/user/lastname"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$i18n.RemovedUser" />
							</xsl:otherwise>
						</xsl:choose> 
						<xsl:text>,&#160;</xsl:text>
						<xsl:value-of select="postedDate"/>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$i18n.atTime" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="postedTime"/>
						<xsl:text>.</xsl:text>
						<xsl:text>&#x20;</xsl:text>
					</p>
				</div>
			</div>
			
			<!-- Delete icon -->
			<div class="floatright">
				<xsl:if test="hasUpdateAccess">
					<div class="floatright margintop marginleft marginright">
						<a href="{$modulePath}/deletePicture/{pictureID}" onclick="return confirmDelete('{$i18n.DeletePicture} {filename}?')">
							<img src="{$moduleImagePath}/delete.png" title="{$i18n.DeletePicture}"/>
						</a>	
					</div>
				</xsl:if>
			</div>
			
		</div>
	</xsl:template>
	
	
	<!-- Picture with/without comments -->
	<xsl:template match="Picture">
		
		<!-- Medium thumb -->
		<a href="{$modulePath}/getImage/{pictureID}" Target="_blank" Title="{$i18n.ShowLargerPicture}">
			<img src="{$modulePath}/mediumThumb/{pictureID}" alt=""/>
		</a>
		
		<xsl:if test="../ImageRedistributionDisclaimer">
			<p class="error"><xsl:value-of select="../ImageRedistributionDisclaimer"/></p>
		</xsl:if>
		
		<!-- Picture Comments -->
		<xsl:if test="../allowComments = 'true'">
			<div class="full">
				<div>
					<div class="floatleft">
						<table>
							<tr>
								<td>
									<b>
										<xsl:value-of select="$i18n.Comments" />
										<xsl:text>&#x20;</xsl:text>
										<xsl:text>(</xsl:text>
										<xsl:choose>
											<xsl:when test="comments/Comment">
												<xsl:value-of select="count(comments/Comment)"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:text>0</xsl:text>
											</xsl:otherwise>
										</xsl:choose>
										<xsl:text>)</xsl:text>
									</b>
								</td>
								
								<xsl:choose>
									<xsl:when test="comments/Comment">
										<td><xsl:text>&#x20;&#x20;|&#x20;&#x20;</xsl:text></td>
										<td>    
											<form name="viewComments" method="post" action="{$modulePath}/viewcomments/{pictureID}">
							    				<input type="hidden" name="viewComments" />	
								    			<xsl:choose>
									    			<xsl:when test="../showAll">
														<a href="javascript:viewComments('false')" title="{$i18n.HideComments}">
															<xsl:value-of select="$i18n.HideComments" />
														</a>
									    			</xsl:when>
									    			<xsl:otherwise>
														<a href="javascript:viewComments('true')" title="{$i18n.ShowComments}">
															<xsl:value-of select="$i18n.ShowComments" />
														</a>
									    			</xsl:otherwise>
								    			</xsl:choose>
							    			</form>
										</td>
									</xsl:when>
								</xsl:choose>
					 		</tr>
						</table>
					</div>
					<br />
					<br />
				</div>
				
				<!-- Comment list -->
				<xsl:choose>
					<xsl:when test="../showAll and comments/Comment">
						<xsl:apply-templates select="comments/Comment"/>
					</xsl:when>
					<xsl:otherwise>
						<div class="bordertop full">
							<p>
								<xsl:value-of select="$i18n.NoCommentsForThisPicture" />
							</p>
						</div>
					</xsl:otherwise>
				</xsl:choose>
				
			</div>
			
			<!-- Comment Editor -->
			<xsl:if test="../allowComments">
				<form name="addCommentText" method="post" action="{$modulePath}/addcomment/{pictureID}">
					<xsl:apply-templates select="validationException/validationError" />
					<label for="comment"><xsl:value-of select="$i18n.Comment" />:</label>
					<textarea id="comment" name="comment" rows="2" cols="88" style="width: 98%;" />
					<div class="floatright margintop">
						<input type="submit" value="{$i18n.AddComment}"/>
					</div>
				</form>
			</xsl:if>
		</xsl:if>
	</xsl:template>
	
	
	<!-- Show Picture -->
	<xsl:template match="ShowPicture">
		
		<div class="content-box">
		
			<h1 class="header">
				<span>
					<xsl:value-of select="Picture/Gallery/name" />
					<xsl:text>&#x20;</xsl:text>
					<xsl:text>(</xsl:text>
					<xsl:value-of select="numPics"/>
					<xsl:text>&#x20;</xsl:text>
					<xsl:value-of select="$i18n.pictures" />
					<xsl:text>)</xsl:text>
				</span>
			</h1>
			
			<div class="content">
			
				<!-- Delete link -->
				<xsl:if test="hasUpdateAccess">
					<div class="floatright">
						<a href="{$modulePath}/deletePicture/{Picture/pictureID}" Title="{$i18n.DeletePicture} &quot;{Picture/filename}&quot;" onclick="return confirmDelete('{$i18n.picture.deleteConfirmation} &quot;{Picture/filename}&quot;?')">
							<img src="{$moduleImagePath}/delete.png" alt=""/>
						</a>
					</div>
				</xsl:if>
				
				<!-- Pagination -->
				<table class="marginbottom">
					<tr>
						<td>
							<xsl:value-of select="Picture/filename"/>
							<xsl:text>&#x20;</xsl:text>
							<xsl:value-of select="$i18n.picture" />
							<xsl:text>&#x20;</xsl:text>
							<strong><xsl:value-of select="currentPic"/></strong>
							<xsl:text>&#x20;</xsl:text>
							<xsl:value-of select="$i18n.of" />
							<xsl:text>&#x20;</xsl:text>
							<xsl:value-of select="numPics"/>
						</td>
						<td>
			             	<xsl:text>&#x20;&#x20;|&#x20;&#x20;</xsl:text>
						</td>
						<td>
							<xsl:choose>
								<xsl:when test="prev">
									<a href="{$modulePath}/showPicture/{prev}" title="{$i18n.Previous}" >
										<img alt="{$i18n.Previous}" src="{$moduleImagePath}/left.gif" />
										<xsl:text>&#x20;&#x20;</xsl:text>
										<xsl:value-of select="$i18n.Previous" />
									</a>
								</xsl:when>
								<xsl:otherwise>
									<div class="disabledtext">
										<img alt="{$i18n.Previous}" src="{$moduleImagePath}/left_light.gif" />
										<xsl:text>&#x20;&#x20;</xsl:text>
										<xsl:value-of select="$i18n.Previous" />
									</div>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td>
							<xsl:text>&#x20;&#x20;|&#x20;&#x20;</xsl:text>
						</td>
						<td>
							<xsl:choose>
								<xsl:when test="next">
									<a href="{$modulePath}/showPicture/{next}" title="{$i18n.Next}" >
										<xsl:value-of select="$i18n.Next" />
										<xsl:text>&#x20;&#x20;</xsl:text>
										<img alt="{$i18n.Next}" src="{$moduleImagePath}/right.gif" />
									</a>
								</xsl:when>
								<xsl:otherwise>
									<div class="disabledtext">
										<xsl:value-of select="$i18n.Next" />
										<xsl:text>&#x20;&#x20;</xsl:text>
										<img alt="{$i18n.Next}" src="{$moduleImagePath}/right_light.gif" />
									</div>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td>
							<xsl:text>&#x20;&#x20;|&#x20;&#x20;</xsl:text>
						</td>
						<td>
							<a href="{$modulePath}/showGallery/{Picture/Gallery/galleryID}" title="{$i18n.SmallPictures}" >
								<xsl:value-of select="$i18n.SmallPictures" />
							</a>
						</td>
						<td>
							<xsl:text>&#x20;&#x20;|&#x20;&#x20;&#x20;&#x20;</xsl:text>
						</td>
						<xsl:if test="originatingPage">
						<td>
							<a href="{$modulePath}/pictures/{originatingPage}" title="{$i18n.SmallPictures}" >
								<xsl:value-of select="$i18n.Back" />
							</a>
						</td>
						</xsl:if>
					</tr>
				</table>
			    
				<!-- Medium thumb -->
				<div class="full marginleft marginbottom margintop">
					<xsl:choose>
						<xsl:when test="Picture">
							<xsl:apply-templates select="Picture"/> 
						</xsl:when>
					</xsl:choose>			
				</div>
			
			</div>
		</div>
	</xsl:template>
	
	
	<!-- Comment -->	 
	<xsl:template match="Comment">
		
		<xsl:variable name="hasUpdateAccess" select="../../../hasUpdateAccess" />
		
		<div class="floatleft bordertop full marginbottom">
			<div class="marginleft">
				<xsl:if test="$hasUpdateAccess">
					<div class="margintop floatright">
			 			<a href="javascript:hideShow('showComment_{commentID}');hideShow('updateComment_{commentID}');" Title="{$i18n.UpdateComment}">
			 			<img src="{$moduleImagePath}/edit.png" alt=""/></a>			 			
			 			<a href="{$modulePath}/deleteComment/{commentID}" Title="{$i18n.DeleteComment}">
			 			<img src="{$moduleImagePath}/delete.png" alt=""/></a>
					</div>
				</xsl:if>
				<div id="showComment_{commentID}">
					<p>
						<xsl:call-template name="replaceLineBreak">
							<xsl:with-param name="string" select="comment"/>
						</xsl:call-template>
					</p>
				</div>
				<xsl:if test="$hasUpdateAccess">
					<div id="updateComment_{commentID}" style="display: none;">
						<form method="POST" action="{$modulePath}/updateComment/{commentID}">
							<textarea name="comment" cols="88" style="width: 99%">
								<xsl:call-template name="replace-string">
									<xsl:with-param name="text" select="comment"/>
									<xsl:with-param name="from" select="'&#13;'"/>
									<xsl:with-param name="to" select="''"/>
								</xsl:call-template>						
							</textarea>
							<div class="floatright margintop">
								<input type="submit" value="{$i18n.SaveChanges}"/>
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
						<xsl:when test="poster/user">
							<xsl:value-of select="poster/user/firstname"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="poster/user/lastname"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$i18n.AnonymousUser" />
						</xsl:otherwise>
					</xsl:choose> 
				</i>
				<br/>
				<xsl:value-of select="posted" />
			</div>
		</div>
	</xsl:template>


	<!-- Add Gallery -->
	<xsl:template match="AddGallery">
		<form method="post" name="addGallery" id="addGallery" action="{/Document/requestinfo/uri}" accept-charset="ISO-8859-1">
			<div class="content-box">
				<h1 class="header">
					<span><xsl:value-of select="$i18n.AddAlbum" /></span>
				</h1>
				<div class="content">
					<xsl:apply-templates select="validationException/validationError" />
					<p>
						<label for="name"><xsl:value-of select="$i18n.Name" />:</label><br />
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="'id'"/>
							<xsl:with-param name="name" select="'name'"/>
							<xsl:with-param name="requestparameters" select="requestparameters"/>
						</xsl:call-template>
					</p>
					<p>
						<label for="description"><xsl:value-of select="$i18n.Description" />:</label>
						<br/>
						<xsl:call-template name="createTextArea">
							<xsl:with-param name="id" select="'description'"/>
							<xsl:with-param name="name" select="'description'"/>
							<xsl:with-param name="requestparameters" select="requestparameters" />
						</xsl:call-template>
					</p>
					<p>
						<label for="description"><xsl:value-of select="$i18n.Category" />:</label>
						<br/>
						
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id" select="'category-dropdown'"/>
							<xsl:with-param name="name" select="'category'"/>
							<xsl:with-param name="element" select="Categories/Category"/>
							<xsl:with-param name="labelElementName" select="'name'"/>
							<xsl:with-param name="valueElementName" select="'value'"/>
						</xsl:call-template>
						
						<xsl:if test="requestparameters/parameter[name = 'notCategory']">
							<div class="hidden" id="auto-select-new-category"/>
						</xsl:if>
						
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="'new-category'"/>
							<xsl:with-param name="name" select="'category'"/>
							<xsl:with-param name="class" select="'hidden margintop'"/>
						</xsl:call-template>
					</p>
					<p>
						<label for="publish"><xsl:value-of select="$i18n.album.publishTo" />:</label>				
						<div class="content-box-no-header">
							<xsl:choose>
								<xsl:when test="schools/school">
									<xsl:call-template name="createTreeview">
										<xsl:with-param name="id" select="'gallery-communitybase-treeview'" />
										<xsl:with-param name="schools" select="schools" />
										<xsl:with-param name="requestparameters" select="requestparameters" />
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<p><xsl:value-of select="$i18n.noaccess" /></p>
								</xsl:otherwise>
							</xsl:choose>
						</div>
					</p>
					<div class="text-align-right">
						<input type="submit" value="{$i18n.CreateAlbum}" />
					</div>
				</div>
			</div>
		</form>
	</xsl:template>
	
	
	<!-- Update Gallery -->
	<xsl:template match="UpdateGallery">
		<form method="post" name="updateGallery" id="updateGallery" action="{/Document/requestinfo/uri}" accept-charset="ISO-8859-1">
			<div class="content-box">
				<h1 class="header">
					<span>
						<xsl:value-of select="$i18n.EditAlbum" />
						<xsl:text>:</xsl:text>
						<xsl:text>&#x20;</xsl:text>
						<xsl:value-of select="Gallery/name" />
					</span>
				</h1>
				<div class="content">
					<xsl:apply-templates select="validationException/validationError" />
					<p>
						<label for="name"><xsl:value-of select="$i18n.Name" />:</label><br />
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="'id'"/>
							<xsl:with-param name="name" select="'name'"/>
							<xsl:with-param name="element" select="Gallery"/>
							<xsl:with-param name="requestparameters" select="requestparameters"/>
						</xsl:call-template>
					</p>
					<p>
						<label for="description"><xsl:value-of select="$i18n.Description" />:</label>
						<br/>
						<xsl:call-template name="createTextArea">
							<xsl:with-param name="id" select="'description'"/>
							<xsl:with-param name="name" select="'description'"/>
							<xsl:with-param name="element" select="Gallery"/>
							<xsl:with-param name="requestparameters" select="requestparameters" />
						</xsl:call-template>
					</p>
					<p>
						<label for="description"><xsl:value-of select="$i18n.Category" />:</label>
						<br/>
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id" select="'category-dropdown'"/>
							<xsl:with-param name="name" select="'category'"/> 
							<xsl:with-param name="element" select="Categories/Category"/>
							<xsl:with-param name="labelElementName" select="'name'"/>
							<xsl:with-param name="valueElementName" select="'value'"/>
							<xsl:with-param name="selectedValue" select="Gallery/category"/>
							<xsl:with-param name="simple" select="false"/>
						</xsl:call-template>
						
						<xsl:if test="requestparameters/parameter[name = 'notCategory']">
							<div class="hidden" id="auto-select-new-category"/>
						</xsl:if>
						
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="'new-category'"/>
							<xsl:with-param name="name" select="'category'"/>
							<xsl:with-param name="class" select="'hidden margintop'"/>
						</xsl:call-template>
					</p>
					<p>
						<label for="publish"><xsl:value-of select="$i18n.album.publishTo" />:</label>				
						<div class="content-box-no-header">
							<xsl:choose>
								<xsl:when test="schools/school">
									<xsl:call-template name="createTreeview">
										<xsl:with-param name="id" select="'gallery-communitybase-treeview'" />
										<xsl:with-param name="element" select="Gallery" />
										<xsl:with-param name="schools" select="schools" />
										<xsl:with-param name="requestparameters" select="requestparameters" />
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<p><xsl:value-of select="$i18n.noaccess" /></p>
								</xsl:otherwise>
							</xsl:choose>
						</div>
					</p>
					<div class="text-align-right">
						<input type="submit" value="{$i18n.SaveChanges}" />
					</div>
				</div>
			</div>
		</form>
	</xsl:template>
	
	
	<!-- Simple uploader -->	 
	<xsl:template match="simpleUploader">
		<form name="addImages" method="post" action="{$modulePath}/simpleUploader/{Gallery/galleryID}" enctype="multipart/form-data">
           
			<div class="content-box">
				<h1 class="header">
					<span>
						<xsl:value-of select="$i18n.AddPicturesToAlbum" />
						<xsl:text>&#x20;</xsl:text>
						<xsl:text>"</xsl:text>
						<xsl:value-of select="Gallery/name"/>
						<xsl:text>"</xsl:text>
					</span>
				</h1>
				<div class="content">
					<xsl:apply-templates select="validationException/validationError"/>
					<xsl:if test="noflash = 'true'">
						<p class="error">
							<img src="{$moduleImagePath}/information.png" alt="" />
							<xsl:text>&#x20;</xsl:text>
							<xsl:value-of select="$i18n.requireAFP" />
							<xsl:text>&#x20;</xsl:text>
							<xsl:value-of select="/Document/requestinfo/uriparts/uripart[position = 5]/value" />
							<xsl:text>&#x20;</xsl:text>
							<xsl:value-of select="$i18n.orLater" />.
							<xsl:text>&#x20;</xsl:text>
							<xsl:value-of select="$i18n.Click" />
							<xsl:text>&#x20;</xsl:text>
							<b>
								<a href="http://get.adobe.com/flashplayer/?promoid=DXLUJ" target="_blank" >
									<xsl:value-of select="$i18n.here" />
								</a>
							</b>
							<xsl:text>&#x20;</xsl:text>
							<xsl:value-of select="$i18n.downloadAFPorUploadPictures" />.
						</p>
					</xsl:if>
					<p>
						<label for="fileUpload"><xsl:value-of select="$i18n.uploadPicturesOrZip" />:<br /></label>
						<input type="file" id="fileUpload" name="fileUpload" size="59" value="{requestparameters/parameter[name='file']/value}"/>
					</p>
					<div class="text-align-right">
						<input type="submit" value="{$i18n.AddImages}"/>
					</div>
				</div>
			</div>
		</form>
	</xsl:template>
	 
	
	<!-- Localization of sorting criteria -->
	<xsl:template name="getSortingCriteraName">
		<xsl:param name="sortingCriteria"/>
		<xsl:choose>
			<xsl:when test="$sortingCriteria = 'filename'">
				<xsl:value-of select="$i18n.Filename"/>
			</xsl:when>
			<xsl:when test="$sortingCriteria = 'posted'">
				<xsl:value-of select="$i18n.Posted"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$i18n.Album"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	
	<!-- Dropdown for sorting criteria, selection by request params -->
	<xsl:template name="addSortingCriteriasDropdown">
		<xsl:param name="sortingCriterias" select="SortingCriterias"/>
		<xsl:param name="requestparameters" select="requestparameters"/>
		<div class="floatright">
    		<xsl:value-of select="$i18n.OrderBy"/>
    		<xsl:text>&#160;</xsl:text>
        	<select id="sortingcriterias" name="orderby" ref="{$modulePath}">
				<xsl:for-each select="$sortingCriterias/Criteria" >
					<option value="{.}">
						<xsl:if test="$requestparameters/parameter[name = 'orderby']/value = .">
							<xsl:attribute name="selected" />
						</xsl:if>
						<xsl:call-template name="getSortingCriteraName">
							<xsl:with-param name="sortingCriteria" select="."/>
						</xsl:call-template>
					</option>
				</xsl:for-each>
			</select>
    	</div>	
	</xsl:template>
	
	
	<!-- Checkbox for reverse sort order, selection by request params -->
	<xsl:template name="addReverseSortingCheckbox">
		<xsl:param name="requestparameters" select="requestparameters"/>
		<div class="floatright">
			<input id="reverseorderfalse" name="reverse" type="radio" value="false" ref="{$modulePath}">
				<xsl:if test="$requestparameters/parameter[name = 'reverse']/value = 'false'">
					<xsl:attribute name="checked"/>
				</xsl:if>
			</input>
			<label for="reverseorderfalse">
				<xsl:choose>
					<xsl:when test="$requestparameters/parameter[name = 'orderby']/value = 'filename'">
						<xsl:value-of select="$i18n.AlphaToOmega"/>
					</xsl:when>
					<xsl:when test="$requestparameters/parameter[name = 'orderby']/value = 'posted'">
						<xsl:value-of select="$i18n.NewestFirst"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.AlphaToOmega"/>
					</xsl:otherwise>
				</xsl:choose>
			</label>
			<input id="reverseordertrue" name="reverse" type="radio" value="true" ref="{$modulePath}">
				<xsl:if test="$requestparameters/parameter[name = 'reverse']/value = 'true'">
					<xsl:attribute name="checked"/>
				</xsl:if>
			</input>
			<label for="reverseordertrue">
				<xsl:choose>
					<xsl:when test="$requestparameters/parameter[name = 'orderby']/value = 'filename'">
						<xsl:value-of select="$i18n.OmegaToAlpha"/>
					</xsl:when>
					<xsl:when test="$requestparameters/parameter[name = 'orderby']/value = 'posted'">
						<xsl:value-of select="$i18n.OldestFirst"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.OmegaToAlpha"/>
					</xsl:otherwise>
				</xsl:choose>
			</label>
		</div>
	</xsl:template>
	
	
	<!-- Validation -->
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
					<xsl:when test="messageKey='InvalidFileFormat'">
						<xsl:value-of select="$validationError.messageKey.BadFileFormat" />!
					</xsl:when>
					<xsl:when test="messageKey='NoImage'">
						<xsl:value-of select="$validationError.messageKey.NoImage" />!
					</xsl:when>
					<xsl:when test="messageKey='NoGroup'">
						<xsl:value-of select="$validationError.messageKey.NoGroup"/>!
					</xsl:when>					
					<xsl:when test="messageKey='noValidGalleryFound'">
						<xsl:value-of select="$validationError.messageKey.InvalidGallery" />!
					</xsl:when>
					<xsl:when test="messageKey='ShowFailedGalleryNotFound'">
						<xsl:value-of select="$validationError.messageKey.InvalidGallery" />!
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