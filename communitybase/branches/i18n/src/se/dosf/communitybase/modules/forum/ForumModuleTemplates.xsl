<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<!-- This stylesheets needs a major removal of duplicated code! -->

	<!-- In preparation for the coming translation -->

	<xsl:template match="document">
		<script type="text/javascript" language="Javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/confirmDelete.js" />
		<div class="contentitem">
			<div class="normal">	
				<h1><xsl:value-of select="/document/module/name" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$document.header" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="/document/group/name" /></h1>
			</div>
			<xsl:apply-templates select="communityModule" />
			<xsl:apply-templates select="updateCommunity" />
			<xsl:apply-templates select="deleteCommunity" />
			<xsl:apply-templates select="addCommunity" />
			<xsl:apply-templates select="showCommunityThreads" />
			<xsl:apply-templates select="addCommunityThread" />
			<xsl:apply-templates select="updateCommunityThread" />
			<xsl:apply-templates select="deleteCommunityThread" />
			<xsl:apply-templates select="showCommunityPosts" />
			<xsl:apply-templates select="updateCommunityPost"/>		
			<xsl:apply-templates select="deleteCommunityPost"/>	
		</div>
	</xsl:template>
	
	<xsl:template match="communityModule">
		<xsl:choose>
			<xsl:when test="noCommunities">
				<div class="divNormal">
					<h1 class="normalTableHeading" colspan="4" align="left">
						<xsl:value-of select="$communitymodule.noforum" /><xsl:text>&#160;</xsl:text><xsl:value-of select="/document/group/name"/>
					</h1>
					<p><xsl:value-of select="$communitymodule.noforums" /><xsl:text>&#160;</xsl:text><xsl:value-of select="/document/group/name"/></p>
				</div>
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="community">
					<xsl:apply-templates select="community" />
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:if test="admin">
			<div class="addForums">
				<xsl:call-template name="forumRef" />
			</div>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="community">
		<div class="divNormal">
			<div class="normalTableHeading">
				<xsl:if test="../admin">
					<xsl:attribute name="style">
						width: 49.9em; float: left;
					</xsl:attribute>
				</xsl:if>
				<a href="{/document/requestinfo/uri}/showCommunityThreads/{forumID}">
					<xsl:value-of select="name"/>
				</a>
			</div>
			<xsl:if test="../admin">
				<div class="floatleft" style="width: 9.2em; background-color: #ccc; padding: 2px 0 0 3px;">
					<div class="floatright" >
						<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/deleteCommunity/{forumID}" onclick="return confirmDelete('{$community.delete.confirm} &quot;{name}&quot;?')">
							<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" title="{$community.delete.title}"/>
						</a>	
					</div>
					<div class="floatright" style="margin-right: 3px">
						<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/updateCommunity/{forumID}" >
							<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png" title="{$community.update.title}"/>
						</a>	
					</div>
				</div>
			</xsl:if>
			<div class="noBorderDiv">
				<xsl:call-template name="replaceLineBreak">
					<xsl:with-param name="string" select="description"/>
				</xsl:call-template>   			
			</div>
		</div>
	</xsl:template>
	
	<xsl:template name="forumRef">
		<div class="addForums">
			<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addCommunity">
				<xsl:value-of select="$forumref.addForum" />
			</a>	
		</div>
	</xsl:template>
	
	<xsl:template match="updateCommunity">
		<form method="POST" action="{/document/requestinfo/uri}" ACCEPT-CHARSET="ISO-8859-1">
			<div class="divNormal">									
				<h1 class="normalTableHeading">
					<xsl:value-of select="$updateCommunity.header" />
				</h1>
				<div>
					<xsl:apply-templates select="validationException/validationError" />
				</div>
				<div class="noBorderDiv" style="padding: 5px">
					<div style="width: 100px; float: left">
						<b>
							<xsl:value-of select="$updateCommunity.name" />:
						</b>
					</div>
					<div class="floatleft">
						<input type='text' name='name' size='50' maxlength='50'>
							<xsl:attribute name="value">
								<xsl:choose>
									<xsl:when test="requestparameters">
										<xsl:value-of select="requestparameters/parameter[name='name']/value"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="community/name"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:attribute>
						</input>
					</div>
				</div>
				<div class="noBorderDiv" style="padding: 5px">
					<div style="width: 100px; float: left">
						<b><xsl:value-of select="$updateCommunity.description" />:</b>
					</div>
					<div style="floatleft">
						<textarea name='description'  rows="5" cols="58">
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
										<xsl:with-param name="text" select="community/description"/>
										<xsl:with-param name="from" select="'&#13;'"/>
										<xsl:with-param name="to" select="''"/>
									</xsl:call-template>									
								</xsl:otherwise>
							</xsl:choose>
						</textarea>
					</div>
				</div>
				<div class="noBorderDiv">
					<div class="floatleft" style="padding: 5px; text-align: right; width: 80%;">
						<input type="submit" value="{$updateCommunity.submit}"/>
					</div>
				</div>
			</div>
		</form>
	</xsl:template>
	
	<xsl:template match="addCommunity">
		<form method="POST" action="{/document/requestinfo/uri}" ACCEPT-CHARSET="ISO-8859-1">
			<div class="divNormal">		

				<xsl:apply-templates select="validationException/validationError"/>
						
				<h1 class="normalTableHeading">
					<xsl:value-of select="$addCommunity.header" />
				</h1>
				<div class="noBorderDiv" style="padding: 5px">
					<div style="width: 100px; float: left">
						<b><xsl:value-of select="$addCommunity.name" />: </b>
					</div>
					<div class="floatleft">
						<input type='text' name='name' size='50' maxlength='50' value="{requestparameters/parameter[name='name']/value}" />
					</div>
				</div>
				<div class="noBorderDiv" style="padding: 5px">
					<div style="width: 100px; float: left">
						<b><xsl:value-of select="$addCommunity.description" />: </b>
					</div>
					<div class="floatleft">
						<textarea name='description'  rows="5" cols="58">
							<xsl:call-template name="replace-string">
								<xsl:with-param name="text" select="requestparameters/parameter[name='description']/value"/>
								<xsl:with-param name="from" select="'&#13;'"/>
								<xsl:with-param name="to" select="''"/>
							</xsl:call-template>		
						</textarea>
					</div>
				</div>				
				<div class="noBorderDiv">
					<div class="floatleft" style="padding: 5px; text-align: right; width: 80%;">
						<input type="submit" value="{$addCommunity.submit}"/>
					</div>
				</div>
			</div>
		</form>	
	</xsl:template>
	
	<xsl:template match="showCommunityThreads">
		<xsl:if test="not(thread)">
			<p><xsl:value-of select="$showCommunityThreads.noThreads" /></p>
		</xsl:if>
		
		<xsl:apply-templates select="thread"/> 
		<div class="noBorderDiv">
			<p style="text-align: right;">
				<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addCommunityThread/{community/forumID}"><xsl:value-of select="$showCommunityThreads.addThread" /></a>
			</p>
		</div>
	</xsl:template>
	
	<xsl:template match="thread">
		<div class="divNormal">	
			<div class="normalTableHeading floatleft" style="width:50em;">			
				<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/showCommunityPosts/{forumThreadID}">
					<xsl:value-of select="subject"/>
				</a>
			</div>
			<xsl:choose>
				<xsl:when test="../admin">
					<div class="floatleft" style="width: 9.2em; background-color: #ccc; padding: 2px 0 0 3px;">
						<div class="floatright" >
							<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/deleteCommunityThread/{forumThreadID}" onclick="return confirmDelete('{$thread.delete.confirm.part1} &quot;{subject}&quot;? {$thread.delete.confirm.part2} {replies} {$thread.delete.confirm.part3}')">
								<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" title="{$thread.delete.title}"/>
							</a>	
						</div>
						<div class="floatright" style="margin-right: 3px">
							<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/updateCommunityThread/{forumThreadID}" >
								<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png" title="{$thread.update.title}"/>
							</a>	
						</div>
					</div>
				</xsl:when>
				<xsl:when test="owner">
					<div class="floatleft" style="width: 9.2em; background-color: #ccc; padding: 2px 0 0 3px;">
						<div class="floatright" style="margin-right: 3px">
							<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/updateCommunityThread/{forumThreadID}" >
								<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png" title="{$thread.update.title}"/>
							</a>	
						</div>
					</div>
				</xsl:when>
			</xsl:choose>
		
			<div class="floatleft" style="width: 65em;">
				<div class="floatleft" >
					<div class="floatleft bigmarginbottom">
						<p style="font-style: italic; padding: 1px 1px 1px 3px; width: 55em; background-color: #F0F2F4;">
							<xsl:value-of select="$thread.postedBy" />: 
							<xsl:choose>
								<xsl:when test="postedBy/user">
									<xsl:value-of select="postedBy/user/firstname"/><xsl:text>&#160;</xsl:text><xsl:value-of select="postedBy/user/lastname"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$thread.deletedUser" />
								</xsl:otherwise>
							</xsl:choose>
							,<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="postedBy/postedDate"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$thread.time" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="postedBy/postedTime"/>
				 		</p>
					</div>
				</div>
				
				<div class="floatleft">
					<p style="padding: 1px 1px 1px 3px; width: 9.2em; background-color: #F0F2F4;">
						<xsl:value-of select="$thread.answers" />: <xsl:value-of select="replies"/>	
					</p>
				</div>
			</div>
			<xsl:if test="changedBy">
				<div class="floatleft" style="width: 65em;">
					<div class="floatleft marginbottom">
						<p style="font-style: italic; padding: 1px 1px 1px 3px; width: 64.5em; background-color: #dcdee1;"><xsl:value-of select="$thread.lastchanged" />: 
						<xsl:choose>
							<xsl:when test="changedBy/user">
								<xsl:value-of select="changedBy/user/firstname"/><xsl:text>&#160;</xsl:text><xsl:value-of select="changedBy/user/lastname"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$thread.deletedUser" />
							</xsl:otherwise>
						</xsl:choose>
						,<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="changedBy/changedDate"/>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$thread.time" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="changedBy/changedTime"/>
				 		</p>
		 			</div>
	 			</div>			
			</xsl:if>
		</div>
	</xsl:template>
	
	<xsl:template match="addCommunityThread">
		<form method="post" action="{/document/requestinfo/uri}" ACCEPT-CHARSET="ISO-8859-1">
			<div class="divNormal">		
					
				<h1 class="normalTableHeading"><xsl:value-of select="$addCommunityThread.header" /><xsl:text>&#160;</xsl:text><xsl:value-of select="community/name"/></h1>
				
				<xsl:apply-templates select="validationException/validationError" />
				
				<div class="noBorderDiv" style="padding: 5px">
					<div style="width: 100px; float: left">
						<b><xsl:value-of select="$addCommunityThread.subject" />:</b>
					</div>
					<div class="floatleft">
						<input type='text' name='subject' size='50' maxlength='50' value="{requestparameters/parameter[name='subject']/value}" />
					</div>
				</div>
				<div class="noBorderDiv" style="padding: 5px">
					<div style="width: 100px; float: left">
						<b><xsl:value-of select="$addCommunityThread.text" />:</b>
					</div>
					<div class="floatleft">
						<textarea rows="16" name="message" cols="58" >							
							<xsl:call-template name="replace-string">
								<xsl:with-param name="text" select="/document/addCommunityThread/requestparameters/parameter[name='message']/value"/>
								<xsl:with-param name="from" select="'&#13;'"/>
								<xsl:with-param name="to" select="''"/>
							</xsl:call-template>							
						</textarea>
					</div>
				</div>				
				<div class="noBorderDiv">
					<div class="floatleft" style="width: 80%; padding: 5px; text-align:right">
						<input type="submit" value="{$addCommunityThread.submit}"/>
					</div>
				</div>
			</div>
		</form>	
	</xsl:template>
	
	<xsl:template match="updateCommunityThread">
		<form method="POST" action="{/document/requestinfo/uri}" ACCEPT-CHARSET="ISO-8859-1">
			<div class="divNormal">									
				<h1 class="normalTableHeading">
					<xsl:value-of select="$updateCommunityThread.header" />
				</h1>
				
				<div>
					<xsl:apply-templates select="validationException/validationError" />
				</div>				
				
				<div style="float: left; padding: 5px">
					<div style="width: 100px; float: left"><b><xsl:value-of select="$updateCommunityThread.subject" />:</b></div>
					<div style="float: left">
						<input type='text' name='subject' size='50' maxlength='50'>
							<xsl:attribute name="value">
								
								<xsl:choose>
									<xsl:when test="requestparameters">
										<xsl:value-of select="requestparameters/parameter[name='subject']/value"/>									
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="thread/subject"/>								
									</xsl:otherwise>
								</xsl:choose>									
							</xsl:attribute>						
						</input>
					</div>
				</div>
				<div style="float: left; padding: 5px">
					<div class="floatleft" style="width: 100px;"><b><xsl:value-of select="$updateCommunityThread.text" />:</b></div>
					<div class="floatleft">
						<textarea rows="16" name="message" cols="58" >
							<xsl:choose>
								<xsl:when test="requestparameters">
									<xsl:call-template name="replace-string">
										<xsl:with-param name="text" select="requestparameters/parameter[name='message']/value"/>
										<xsl:with-param name="from" select="'&#13;'"/>
										<xsl:with-param name="to" select="''"/>
									</xsl:call-template>									
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="replace-string">
										<xsl:with-param name="text" select="thread/message"/>
										<xsl:with-param name="from" select="'&#13;'"/>
										<xsl:with-param name="to" select="''"/>
									</xsl:call-template>									
								</xsl:otherwise>
							</xsl:choose>											
						</textarea>					
					</div>
				</div>	
				<div class="noBorderDiv">
					<div class="floatleft" style="width: 80%; padding: 5px; text-align:right">			
						<input type="submit" value="{$updateCommunityThread.submit}"/>
					</div>
				</div>
			</div>
		</form>
	</xsl:template>
	
	
	<xsl:template match="showCommunityPosts">
		<div class="divNormal">	
			<div class="noBorderDiv floatleft" style="width: 54em;">
				<xsl:call-template name="replaceLineBreak">
					<xsl:with-param name="string" select="thread/message"/>
				</xsl:call-template>  						
			</div>
			<xsl:choose>
				<xsl:when test="admin">
					<div class="floatleft" style="width: 9.2em; padding: 2px 0 0 3px;">
						<div class="floatright" >
							<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/deleteCommunityThread/{thread/forumThreadID}" onclick="return confirmDelete('{$showCommunityPosts.delete.confirm.part1} &quot;{subject}&quot; {$showCommunityPosts.delete.confirm.part2}?')">
								<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" title="{$showCommunityPosts.delete.title}"/>
							</a>	
						</div>
						<div class="floatright" style="margin-right: 3px">
							<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/updateCommunityThread/{thread/forumThreadID}" >
								<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png" title="{$showCommunityPosts.update.title}"/>
							</a>	
						</div>
					</div>
				</xsl:when>
			</xsl:choose>
			
			<div class="floatleft" style="width: 65em;">
				<div class="floatleft marginbottom">
					<p style="font-style: italic; padding: 1px 1px 1px 3px; width: 64.5em; background-color: #F0F2F4;"><xsl:value-of select="$thread.postedBy" />: 
						<xsl:choose>
							<xsl:when test="thread/postedBy/user">
								<xsl:value-of select="thread/postedBy/user/firstname"/><xsl:text>&#160;</xsl:text><xsl:value-of select="thread/postedBy/user/lastname"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$thread.deletedUser" />
							</xsl:otherwise>
						</xsl:choose>
						,<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="thread/postedBy/postedDate"/>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$thread.time" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="thread/postedBy/postedTime"/>
			 		</p>
				</div>
					<xsl:if test="thread/changedBy">
						<div class="floatleft" style="width: 65em;">
							<div class="floatleft marginbottom">
								<p style="font-style: italic; padding: 1px 1px 1px 3px; width: 64.5em; background-color: #dcdee1;"><xsl:value-of select="$thread.lastchanged" />: 
								<xsl:choose>
									<xsl:when test="thread/changedBy/user">
										<xsl:value-of select="thread/changedBy/user/firstname"/><xsl:text>&#160;</xsl:text><xsl:value-of select="thread/changedBy/user/lastname"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$thread.deletedUser" />
									</xsl:otherwise>
								</xsl:choose>
								,<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="thread/changedBy/changedDate"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$thread.time" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="thread/changedBy/changedTime"/>
						 		</p>
				 			</div>
			 			</div>
					</xsl:if>
			 </div>
			</div>
		
		<xsl:if test="thread/post">
			<xsl:apply-templates select="thread/post"/>
		</xsl:if>
		
		<div class="noBorderDiv">
			<div>		
				<xsl:apply-templates select="validationException/validationError"/>	
			</div>
			<div class="floatleft">
				<form method="POST" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/showCommunityPosts/{thread/forumThreadID}" ACCEPT-CHARSET="ISO-8859-1">
					<table class="calendar">
						<tr>
							<th align="left" class="marginTop"><xsl:value-of select="$showCommunityPosts.answer" />: <xsl:value-of select="thread/subject" /></th>
						</tr>
						<tr>
							<td align="right"><textarea rows="10" name="message" cols="89"/></td>
						</tr>				
						<tr>
							<td align="right"><input type="submit" value="Lägg till ny post"/></td>
						</tr>
					</table>
				</form>	
			</div>
		</div>
		
	</xsl:template>

	<xsl:template match="thread/post">
		<div class="divNormal">
			<div class="noBorderDiv floatleft" style="width: 54em;">			
				<xsl:call-template name="replaceLineBreak">
					<xsl:with-param name="string" select="message"/>
				</xsl:call-template>  
			</div>
		
			<xsl:choose>
				<xsl:when test="../../admin">
					<div class="floatleft" style="width: 9.2em; padding: 2px 0 0 3px;">
						<div class="floatright" >
							<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/deleteCommunityPost/{postID}" onclick="return confirmDelete('{$post.delete.confirm} &quot;{substring(message,1, 15)}&quot;?')">
								<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" title="{$post.delete.title}"/>
							</a>	
						</div>
						<div class="floatright" style="margin-right: 3px">
							<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/updateCommunityPost/{postID}" >
								<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png" title="{$post.update.title}"/>
							</a>	
						</div>
					</div>
				</xsl:when>
				<xsl:when test="owner">
					<div class="floatleft" style="width: 9.2em; padding: 2px 0 0 3px;">
						<div class="floatright" style="margin-right: 3px">
							<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/updateCommunityPost/{postID}" >
								<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png" title="{$thread.update.title}"/>
							</a>	
						</div>
					</div>
				</xsl:when>
			</xsl:choose>
	
			 <div class="floatleft" style="width: 65em;">
				<div class="floatleft marginbottom">
					<p style="font-style: italic; padding: 1px 1px 1px 3px; width: 64.5em; background-color: #F0F2F4;"><xsl:value-of select="$thread.postedBy" />: 
						<xsl:choose>
							<xsl:when test="postedBy/user">
								<xsl:value-of select="postedBy/user/firstname"/><xsl:text>&#160;</xsl:text><xsl:value-of select="postedBy/user/lastname"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$thread.deletedUser" />
							</xsl:otherwise>
						</xsl:choose>
						,<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="postedBy/postedDate"/>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$thread.time" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="postedBy/postedTime"/>
			 		</p>
				</div>
			</div>
			<xsl:if test="changedBy">
				<div class="floatleft marginbottom">
	 				<p style="font-style: italic; padding: 1px 1px 1px 3px; width: 64.5em; background-color: #dcdee1; margin-bottom: 2px;"><xsl:value-of select="$thread.lastchanged" />: 
						<xsl:choose>
							<xsl:when test="changedBy/user">
								<xsl:value-of select="changedBy/user/firstname"/><xsl:text>&#160;</xsl:text><xsl:value-of select="changedBy/user/lastname"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$thread.deletedUser" />
							</xsl:otherwise>
						</xsl:choose>
						,<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="changedBy/changedDate"/>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$thread.time" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="changedBy/changedTime"/>
			 		</p>
	 			</div>
			</xsl:if>
		</div>
	</xsl:template>
	
	<xsl:template match="updateCommunityPost">
		<form method="POST" action="{/document/requestinfo/uri}" ACCEPT-CHARSET="ISO-8859-1">
			
			<xsl:apply-templates select="validationException/validationError"/>	
			
			<table class="calendar">
				<tr>
					<th align="left" class="marginTop"><xsl:value-of select="$updateCommunityPost.header" />: </th>
				</tr>
				<tr>
					<td align="center">
						<textarea rows="16" name="message" cols="65">							
							<xsl:choose>
								<xsl:when test="requestparameters">
									<xsl:call-template name="replace-string">
										<xsl:with-param name="text" select="requestparameters/parameter[name='message']/value"/>
										<xsl:with-param name="from" select="'&#13;'"/>
										<xsl:with-param name="to" select="''"/>
									</xsl:call-template>									
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="replace-string">
										<xsl:with-param name="text" select="post/message"/>
										<xsl:with-param name="from" select="'&#13;'"/>
										<xsl:with-param name="to" select="''"/>
									</xsl:call-template>									
								</xsl:otherwise>
							</xsl:choose>							
						</textarea>
					</td>
				</tr>				
				<tr>
					<td align="right"><input type="submit" value="{$updateCommunityPost.submit}"/></td>
				</tr>
			</table>
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
					<xsl:text><xsl:value-of select="$validation.tooShort" /></xsl:text>
				</xsl:when>
				<xsl:when test="validationErrorType='TooLong'">
					<xsl:text><xsl:value-of select="$validation.tooLong" /></xsl:text>
				</xsl:when>									
				<xsl:otherwise>
					<xsl:text><xsl:value-of select="$validationError.unknownValidationErrorType"/></xsl:text>
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
				<xsl:when test="fieldName = 'message'">
					<xsl:value-of select="$validationError.field.message" />!
				</xsl:when>																																											
				<xsl:otherwise>
					<xsl:value-of select="fieldName"/>
				</xsl:otherwise>
			</xsl:choose>				
		</p>
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
      
</xsl:stylesheet>