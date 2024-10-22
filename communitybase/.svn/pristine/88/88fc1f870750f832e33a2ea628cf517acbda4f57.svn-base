<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<!-- This stylesheets needs a major removal of duplicated code! -->

	<!-- In preparation for the coming translation -->

	<xsl:template match="document">
		<script type="text/javascript" language="Javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/confirmDelete.js" />
		<div class="contentitem">
				
			<h1><xsl:value-of select="/document/module/name" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$document.header" /><xsl:text>:&#160;</xsl:text><xsl:value-of select="/document/group/name" /></h1>
			
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
				<div class="content-box">
					<h1 class="header">
						<span><xsl:value-of select="$communitymodule.noforum" /><xsl:text>&#160;</xsl:text><xsl:value-of select="/document/group/name"/></span>
					</h1>
					
					<div class="content">
					
						<p><xsl:value-of select="$communitymodule.noforums" /><xsl:text>&#160;</xsl:text><xsl:value-of select="/document/group/name"/></p>
					
					</div>
				</div>
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="community">
					<xsl:apply-templates select="community" />
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
		
		<xsl:if test="admin">
			<div class="text-align-right">
				<xsl:call-template name="forumRef" />
			</div>
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template match="community">
		
		<div class="content-box">
			
			<h1 class="header">
				<span>
					<a class="floatleft" href="{/document/requestinfo/uri}/showCommunityThreads/{forumID}">
						<xsl:value-of select="name"/>
					</a>
					<xsl:if test="../admin">
						<div class="floatright">
							<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/updateCommunity/{forumID}" >
								<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png" title="{$community.update.title}"/>
							</a>
							<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/deleteCommunity/{forumID}" onclick="return confirmDelete('{$community.delete.confirm} &quot;{name}&quot;?')">
								<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" title="{$community.delete.title}"/>
							</a>	
						</div>
					</xsl:if>
				</span>
			</h1>
			
			<div class="content">
						
				<xsl:call-template name="replaceLineBreak">
					<xsl:with-param name="string" select="description"/>
				</xsl:call-template>
			
			</div>

		</div>
		
	</xsl:template>
	
	<xsl:template name="forumRef">
		<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addCommunity">
			<xsl:value-of select="$forumref.addForum" />
		</a>
	</xsl:template>
	
	<xsl:template match="updateCommunity">
		
		<form method="POST" action="{/document/requestinfo/uri}" ACCEPT-CHARSET="ISO-8859-1">
			
			<div class="content-box">									
				<h1 class="header">
					<span><xsl:value-of select="$updateCommunity.header" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="community/name" /></span>
				</h1>
				
				<div class="content">
					
					<xsl:apply-templates select="validationException/validationError" />
					
					<p>
						<label for="name"><xsl:value-of select="$updateCommunity.name" />:</label><br/>
						<input type="text" id="name" name="name" size="50" maxlength="50">
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
					</p>
					
					<p>
						<label for="description"><xsl:value-of select="$updateCommunity.description" />:</label><br/>
						<textarea id="description" name="description" rows="5" cols="58">
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
					</p>
					
					<div class="text-align-right">
						<input type="submit" value="{$updateCommunity.submit}"/>
					</div>
				
				</div>
				
			</div>
			
		</form>
		
	</xsl:template>
	
	<xsl:template match="addCommunity">
		
		<form method="POST" action="{/document/requestinfo/uri}" ACCEPT-CHARSET="ISO-8859-1">
		
			<div class="content-box">		
						
				<h1 class="header">
					<span><xsl:value-of select="$addCommunity.header" /></span>
				</h1>
				
				<div class="content">
				
					<xsl:apply-templates select="validationException/validationError"/>
					
					<p>
						<label for="name"><xsl:value-of select="$addCommunity.name" />:</label><br/>
						<input type="text" id="name" name="name" size="50" maxlength="50" value="{requestparameters/parameter[name='name']/value}" />
					</p>
					
					<p>
						<label for="description"><xsl:value-of select="$addCommunity.description" />:</label><br/>
						<textarea id="description" name="description"  rows="5" cols="58">
							<xsl:call-template name="replace-string">
								<xsl:with-param name="text" select="requestparameters/parameter[name='description']/value"/>
								<xsl:with-param name="from" select="'&#13;'"/>
								<xsl:with-param name="to" select="''"/>
							</xsl:call-template>		
						</textarea>
					</p>
									
					<div class="text-align-right">
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
		<div>
			<p class="text-align-right">
				<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addCommunityThread/{community/forumID}"><xsl:value-of select="$showCommunityThreads.addThread" /></a>
			</p>
		</div>
	</xsl:template>
	
	<xsl:template match="thread">
		
		<div class="content-box">	
			
			<h1 class="header">
				<span>
					<a class="floatleft" href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/showCommunityPosts/{forumThreadID}">
						<xsl:value-of select="subject"/>
					</a>
					<xsl:choose>
						<xsl:when test="../admin">
							<div class="floatright" >
								<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/deleteCommunityThread/{forumThreadID}" onclick="return confirmDelete('{$thread.delete.confirm.part1} &quot;{subject}&quot;? {$thread.delete.confirm.part2} {replies} {$thread.delete.confirm.part3}')">
									<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" title="{$thread.delete.title}"/>
								</a>	
							</div>
							<div class="floatright">
								<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/updateCommunityThread/{forumThreadID}" >
									<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png" title="{$thread.update.title}"/>
								</a>	
							</div>
						</xsl:when>
						<xsl:when test="owner">
							<div class="floatright">
								<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/updateCommunityThread/{forumThreadID}" >
									<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png" title="{$thread.update.title}"/>
								</a>	
							</div>
						</xsl:when>
					</xsl:choose>
				</span>
			</h1>
			
			<div class="content">
			
				<div class="floatleft full">
					
					<div class="floatleft marginbottom full">
						<xsl:value-of select="$thread.answers" />: <xsl:value-of select="replies"/>	
					</div>
					
					<div class="floatleft marginbottom full" >
						<p class="addedBy">
							<xsl:value-of select="$thread.postedBy" />: 
							<xsl:choose>
								<xsl:when test="postedBy/user">
									<xsl:value-of select="postedBy/user/firstname"/><xsl:text>&#160;</xsl:text><xsl:value-of select="postedBy/user/lastname"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$thread.deletedUser" />
								</xsl:otherwise>
							</xsl:choose>,<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="postedBy/postedDate"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$thread.time" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="postedBy/postedTime"/>
				 		</p>
					</div>
					
				</div>
				<xsl:if test="changedBy">
					
					<div class="floatleft marginbottom full">
						<p class="addedBy"><xsl:value-of select="$thread.lastchanged" />: 
							<xsl:choose>
								<xsl:when test="changedBy/user">
									<xsl:value-of select="changedBy/user/firstname"/><xsl:text>&#160;</xsl:text><xsl:value-of select="changedBy/user/lastname"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$thread.deletedUser" />
								</xsl:otherwise>
							</xsl:choose>,<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="changedBy/changedDate"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$thread.time" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="changedBy/changedTime"/>
				 		</p>
		 			</div>			
				</xsl:if>
			
			</div>
			
		</div>
	</xsl:template>
	
	<xsl:template match="addCommunityThread">
		
		<form method="post" action="{/document/requestinfo/uri}" ACCEPT-CHARSET="ISO-8859-1">
			<div class="content-box">		
					
				<h1 class="header"><span><xsl:value-of select="$addCommunityThread.header" /><xsl:text>&#160;</xsl:text><xsl:value-of select="community/name"/></span></h1>
				
				<div class="content">
				
					<xsl:apply-templates select="validationException/validationError" />
					
					<p>
						<label for="subject"><xsl:value-of select="$addCommunityThread.subject" />:</label><br/>
						<input type="text" id="subject" name="subject" size="50" maxlength="50" value="{requestparameters/parameter[name='subject']/value}" />
					</p>
					
					<p>
						<label for="message"><xsl:value-of select="$addCommunityThread.text" />:</label><br/>
						<textarea rows="16" id="message" name="message" cols="58">							
							<xsl:call-template name="replace-string">
								<xsl:with-param name="text" select="/document/addCommunityThread/requestparameters/parameter[name='message']/value"/>
								<xsl:with-param name="from" select="'&#13;'"/>
								<xsl:with-param name="to" select="''"/>
							</xsl:call-template>							
						</textarea>
					</p>
								
					<div class="text-align-right">
						<input type="submit" value="{$addCommunityThread.submit}"/>
					</div>
				
				</div>
				
			</div>
		</form>	
		
	</xsl:template>
	
	<xsl:template match="updateCommunityThread">
		
		<form method="POST" action="{/document/requestinfo/uri}" ACCEPT-CHARSET="ISO-8859-1">
			
			<div class="content-box">									
				<h1 class="header">
					<span><xsl:value-of select="$updateCommunityThread.header" /></span>
				</h1>
				
				<div class="content">
				
					<xsl:apply-templates select="validationException/validationError" />
					
					<p>
						<label for="subject"><xsl:value-of select="$updateCommunityThread.subject" />:</label><br/>
						<input type="text" id="subject" name="subject" size="50" maxlength="50">
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
					</p>
					
					<p>
						<label for="message"><xsl:value-of select="$updateCommunityThread.text" />:</label><br/>
						<textarea rows="16" id="message" name="message" cols="58">
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
					</p>

					<div class="text-align-right">			
						<input type="submit" value="{$updateCommunityThread.submit}"/>
					</div>
				
				</div>
				
			</div>
			
		</form>
		
	</xsl:template>
	
	
	<xsl:template match="showCommunityPosts">
		
		<div class="content-box-no-header">	
			
			<div class="floatleft ninety">
				<xsl:call-template name="replaceLineBreak">
					<xsl:with-param name="string" select="thread/message"/>
				</xsl:call-template>  						
			</div>
			
			<xsl:choose>
				<xsl:when test="admin">
					<div class="floatright ten">
						<div class="floatright" >
							<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/deleteCommunityThread/{thread/forumThreadID}" onclick="return confirmDelete('{$showCommunityPosts.delete.confirm.part1} &quot;{subject}&quot; {$showCommunityPosts.delete.confirm.part2}?')">
								<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" title="{$showCommunityPosts.delete.title}"/>
							</a>	
						</div>
						<div class="floatright">
							<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/updateCommunityThread/{thread/forumThreadID}" >
								<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png" title="{$showCommunityPosts.update.title}"/>
							</a>	
						</div>
					</div>
				</xsl:when>
			</xsl:choose>
			
			<div class="floatleft margintop marginbottom full">
				
				<div class="floatleft marginbottom fifty">
					
					<p class="addedBy lightbackground"><xsl:value-of select="$thread.postedBy" />: 
						<xsl:choose>
							<xsl:when test="thread/postedBy/user">
								<xsl:value-of select="thread/postedBy/user/firstname"/><xsl:text>&#160;</xsl:text><xsl:value-of select="thread/postedBy/user/lastname"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$thread.deletedUser" />
							</xsl:otherwise>
						</xsl:choose>,<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="thread/postedBy/postedDate"/>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$thread.time" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="thread/postedBy/postedTime"/>
			 		</p>
				</div>
				
				<xsl:if test="thread/changedBy">
					<div class="floatleft marginbottom fifty">
						<p class="addedBy lightbackground"><xsl:value-of select="$thread.lastchanged" />: 
							<xsl:choose>
								<xsl:when test="thread/changedBy/user">
									<xsl:value-of select="thread/changedBy/user/firstname"/><xsl:text>&#160;</xsl:text><xsl:value-of select="thread/changedBy/user/lastname"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$thread.deletedUser" />
								</xsl:otherwise>
							</xsl:choose>,<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="thread/changedBy/changedDate"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$thread.time" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="thread/changedBy/changedTime"/>
				 		</p>
		 			</div>
				</xsl:if>
				
			 </div>
			 
		</div>
		
		<xsl:if test="thread/post">
			<xsl:apply-templates select="thread/post"/>
		</xsl:if>
		
		<div class="floatleft full">
			
			<xsl:apply-templates select="validationException/validationError"/>	
			
			<form method="POST" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/showCommunityPosts/{thread/forumThreadID}" ACCEPT-CHARSET="ISO-8859-1">
				<p>
					<h4><label for="message"><xsl:value-of select="$showCommunityPosts.answer" />: <xsl:value-of select="thread/subject" /></label></h4>
					<textarea rows="10" id="message" name="message" cols="83" />
				</p>
				<div class="text-align-right">
					<input class="floatright" type="submit" value="Lägg till ny post"/>
				</div>
			</form>
					
		</div>
		
	</xsl:template>

	<xsl:template match="thread/post">
		
		<div class="content-box-no-header">
			
			<div class="floatleft ninety">
				<xsl:call-template name="replaceLineBreak">
					<xsl:with-param name="string" select="message"/>
				</xsl:call-template>  						
			</div>
		
			<xsl:choose>
				<xsl:when test="../../admin">
					<div class="floatright ten">
						<div class="floatright" >
							<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/deleteCommunityPost/{postID}" onclick="return confirmDelete('{$post.delete.confirm} &quot;{substring(message,1, 15)}&quot;?')">
								<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" title="{$post.delete.title}"/>
							</a>	
						</div>
						<div class="floatright">
							<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/updateCommunityPost/{postID}" >
								<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png" title="{$post.update.title}"/>
							</a>	
						</div>
					</div>
				</xsl:when>
				<xsl:when test="owner">
					<div class="floatright ten">
						<div class="floatright" >
							<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/updateCommunityPost/{postID}" >
								<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png" title="{$thread.update.title}"/>
							</a>
						</div>
					</div>
				</xsl:when>
			</xsl:choose>
	
			<div class="floatleft margintop marginbottom full">
				
				<div class="floatleft marginbottom fifty">
				
					<p class="addedBy lightbackground"><xsl:value-of select="$thread.postedBy" />: 
						<xsl:choose>
							<xsl:when test="postedBy/user">
								<xsl:value-of select="postedBy/user/firstname"/><xsl:text>&#160;</xsl:text><xsl:value-of select="postedBy/user/lastname"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$thread.deletedUser" />
							</xsl:otherwise>
						</xsl:choose>,<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="postedBy/postedDate"/>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$thread.time" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="postedBy/postedTime"/>
			 		</p>
			 		
				</div>
				<div class="floatleft marginbottom fifty">
					<xsl:if test="changedBy">
		 				<p class="addedBy lightbackground"><xsl:value-of select="$thread.lastchanged" />: 
							<xsl:choose>
								<xsl:when test="changedBy/user">
									<xsl:value-of select="changedBy/user/firstname"/><xsl:text>&#160;</xsl:text><xsl:value-of select="changedBy/user/lastname"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$thread.deletedUser" />
								</xsl:otherwise>
							</xsl:choose>,<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="changedBy/changedDate"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$thread.time" />:<xsl:text>&#160;</xsl:text><xsl:value-of select="changedBy/changedTime"/>
				 		</p>
					</xsl:if>
				</div>
			</div>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="updateCommunityPost">
		
		<form method="POST" action="{/document/requestinfo/uri}" ACCEPT-CHARSET="ISO-8859-1">
			
			<div class="content-box">
			
				<h1 class="header"><span><xsl:value-of select="$updateCommunityPost.header" /></span></h1>
				
				<div class="content">
			
					<xsl:apply-templates select="validationException/validationError"/>	
					
					<p>
						<textarea rows="16" name="message" cols="80">							
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
					</p>
					
					<div class="text-align-right">
						<input type="submit" value="{$updateCommunityPost.submit}"/>
					</div>
			
				</div>
			
			</div>
			
		</form>
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
				<xsl:when test="fieldName = 'subject'">
					<xsl:value-of select="$validationError.field.subject" />!
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