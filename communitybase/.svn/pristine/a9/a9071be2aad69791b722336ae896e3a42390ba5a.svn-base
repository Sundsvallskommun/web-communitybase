<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />
	
	<xsl:template match="document">

		<script 
			type="text/javascript"
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/askBeforeRedirect.js">
		</script>

		<div class="contentitem">

			<h1>
				<xsl:value-of select="/document/module/name" /><xsl:text>&#x20;</xsl:text>
				<xsl:value-of select="$document.administration.by" /><xsl:text>&#x20;</xsl:text>
				<xsl:value-of select="group/name" />
			</h1>

			<xsl:apply-templates select="GroupInfoElement" />
			<xsl:apply-templates select="UpdateUser/user" mode="update"/>
			<xsl:apply-templates select="MoveUsers" />
			<xsl:apply-templates select="CopyUsers" />
		</div>
	</xsl:template>

	<xsl:template match="user" mode="update">
	
		<form method="post" name="updateuser" action="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/group/groupID}/updateUser/{userID}">
			
			<div class="content-box">
				<h1 class="header">
					<span>
						<xsl:value-of select="$user.update.header" />:
						
						<xsl:text>&#x20;</xsl:text>
						
						<xsl:value-of select="firstname" />
	
						<xsl:text>&#x20;</xsl:text>
	
						<xsl:value-of select="lastname" />
					</span>
				</h1>
	
				<div class="content">
	
					<xsl:apply-templates select="../validationException/validationError"/>
				
					<p>
						<label for="GroupAccessLevel"><xsl:value-of select="$user.update.authorize" />:</label><br/>
						<select id="GroupAccessLevel" name="GroupAccessLevel" onchange="this.options[this.selectedIndex].value != 'MEMBER' ? document.getElementById('commenttext').innerHTML = '{$user.update.comment.admin}:' : document.getElementById('commenttext').innerHTML = '{$user.update.comment.member}:'">
			
							<option value="MEMBER">					
								<xsl:choose>
									<xsl:when test="../requestparameters">
										<xsl:if test="../requestparameters/parameter[name='GroupAccessLevel']/value = 'MEMBER'">
											<xsl:attribute name="selected"/>
										</xsl:if>						
									</xsl:when>
									<xsl:otherwise>
										<xsl:if test="communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation/GroupAccessLevel = 'MEMBER'">
											<xsl:attribute name="selected"/>
										</xsl:if>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:value-of select="$user.update.parent" />
							</option>
							
							<option value="PUBLISHER">						
								<xsl:choose>
									<xsl:when test="../requestparameters">
										<xsl:if test="../requestparameters/parameter[name='GroupAccessLevel']/value = 'PUBLISHER'">
											<xsl:attribute name="selected"/>
										</xsl:if>						
									</xsl:when>
									<xsl:otherwise>
										<xsl:if test="communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation/GroupAccessLevel = 'PUBLISHER'">
											<xsl:attribute name="selected"/>
										</xsl:if>
									</xsl:otherwise>
								</xsl:choose>						
								<xsl:value-of select="$user.update.educationalist" />
							</option>
							
							<xsl:if test="/document/isSchoolAdmin">
								<option value="ADMIN">				
									<xsl:choose>
										<xsl:when test="../requestparameters">
											<xsl:if test="../requestparameters/parameter[name='GroupAccessLevel']/value = 'ADMIN'">
												<xsl:attribute name="selected"/>
											</xsl:if>						
										</xsl:when>
										<xsl:otherwise>
											<xsl:if test="communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation/GroupAccessLevel = 'ADMIN'">
												<xsl:attribute name="selected"/>
											</xsl:if>
										</xsl:otherwise>
									</xsl:choose>														
									<xsl:value-of select="$user.update.admin" />
								</option>
							</xsl:if>
						</select>
					</p>
				
					<p>
						<label id="commenttext" for="comment">
							<xsl:choose>
								<xsl:when test="../requestparameters/parameter[name = 'GroupAccessLevel']/value != 'MEMBER' or communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation/GroupAccessLevel = 'ADMIN' or communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation/GroupAccessLevel = 'PUBLISHER'">
									<xsl:value-of select="$user.update.comment.admin" />:
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$user.update.comment.member" />:
								</xsl:otherwise>
							</xsl:choose>
						</label><br/>
						<input type="text" id="comment" name="comment" size="70">
							<xsl:choose>
								<xsl:when test="../requestparameters">
									<xsl:attribute name="value">
										<xsl:value-of select="../requestparameters/parameter[name='comment']/value"/>
									</xsl:attribute>						
								</xsl:when>
								<xsl:otherwise>
									<xsl:attribute name="value">
										<xsl:value-of select="communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation/Comment"/>
									</xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>					
						</input>
					</p>
					
					<div class="floatleft seventy">
					
						<p>
							<div class="floatleft twenty"><label for="email"><xsl:value-of select="$user.update.email" />:</label></div><a href="mailto:{email}"><xsl:value-of select="email" /></a>
						</p>
						
						<xsl:if test="communityUserAttributes/phoneHome">
							<p>
								<div class="floatleft twenty"><label for="phonehome"><xsl:value-of select="$user.update.phonehome" />:</label></div><xsl:value-of select="communityUserAttributes/phoneHome" />
							</p>						
						</xsl:if>
						<xsl:if test="communityUserAttributes/phoneMobile">
							<p>
								<div class="floatleft twenty"><label for="phonemobile"><xsl:value-of select="$user.update.mobilephone" />:</label></div><xsl:value-of select="communityUserAttributes/phoneMobile" />
							</p>
						</xsl:if>
						<xsl:if test="communityUserAttributes/phoneWork">
							<p>
								<div class="floatleft twenty"><label for="phonework"><xsl:value-of select="$user.update.phonework" />:</label></div><xsl:value-of select="communityUserAttributes/phoneWork" />
							</p>
						</xsl:if>
					
					</div>			
				
					<div class="clearboth floatright">
						<input type="submit" value="{$user.update.submit}"/>
					</div>	
			
				</div>
			
			</div>
				
		</form>
	</xsl:template>

	<xsl:template match="MoveUsers">
		
		<form method="POST" name="moveusers" action="">
		
			<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/dtree/dtree.js"/>
			<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/GroupAdminModule.js"/>
			
			<xsl:for-each select="user">
				<input type="hidden" name="move.userID" value="{userID}" />
			</xsl:for-each>
			
			<div class="content-box">
				
				<h1 class="header">
					<span><xsl:value-of select="$users.move.header" /></span>
				</h1>
				
				<div class="content">
				
					<p><xsl:value-of select="$users.move.text.part1" /><xsl:text>&#x20;</xsl:text><b><xsl:value-of select="count(user)" /></b><xsl:text>&#x20;</xsl:text><xsl:value-of select="$users.move.text.part2" /></p>
					<p><xsl:value-of select="$users.move.description" /></p>
					
					<xsl:apply-templates select="schools" mode="tree" >
						<xsl:with-param name="method" select="'moveUsers'"/>
					</xsl:apply-templates>
				
				</div>
				
			</div>

		</form>
		
	</xsl:template>
	
	<xsl:template match="CopyUsers">
		
		<form method="POST" name="moveusers" action="">
		
			<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/dtree/dtree.js"/>
			<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/GroupAdminModule.js"/>
			
			<xsl:for-each select="user">
				<input type="hidden" name="move.userID" value="{userID}" />
			</xsl:for-each>
			
			<div class="content-box">
				
				<h1 class="header">
					<span><xsl:value-of select="$users.copy.header" /></span>
				</h1>
				
				<div class="content">
				
					<p><xsl:value-of select="$users.copy.text.part1" /><xsl:text>&#x20;</xsl:text><b><xsl:value-of select="count(user)" /></b><xsl:text>&#x20;</xsl:text><xsl:value-of select="$users.copy.text.part2" /></p>
					<p><xsl:value-of select="$users.copy.description" /></p>
					
					<xsl:apply-templates select="schools" mode="tree" >
						<xsl:with-param name="method" select="'copyUsers'"/>
					</xsl:apply-templates>
				
				</div>
				
			</div>

		</form>
		
	</xsl:template>

	<!-- Tree templates   -->
	
	<xsl:template match="schools" mode="tree">
		<xsl:param name="method"/>
		
		<div class="dtree">
			<p><a href="javascript: schooltree{/document/module/moduleID}.openAll();"><xsl:value-of select="$schools.tree.expandAll" /></a> | <a href="javascript: schooltree{/document/module/moduleID}.closeAll();"><xsl:value-of select="$schools.tree.collapseAll" /></a></p>

			<script type="text/javascript">
				schooltree<xsl:value-of select="/document/module/moduleID"/> = new dTree('schooltree<xsl:value-of select="/document/module/moduleID"/>','<xsl:value-of select="/document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/document/module/sectionID"/>/<xsl:value-of select="/document/module/moduleID"/>/dtree/');
				schooltree<xsl:value-of select="/document/module/moduleID"/>.icon.root = '<xsl:value-of select="/document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/document/module/sectionID"/>/<xsl:value-of select="/document/module/moduleID"/>/dtree/img/globe.gif';
				
				schooltree<xsl:value-of select="/document/module/moduleID"/>.add('base','-1','<xsl:value-of select="$schools.tree.title" />','','<xsl:value-of select="$schools.tree.title" />','','<xsl:value-of select="/document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/document/module/sectionID"/>/<xsl:value-of select="/document/module/moduleID"/>/dtree/img/folder.gif','<xsl:value-of select="/document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/document/module/sectionID"/>/<xsl:value-of select="/document/module/moduleID"/>/dtree/img/folderopen.gif','','');				
				
				<xsl:apply-templates select="school" mode="tree">
					<xsl:with-param name="method" select="$method"/>
				</xsl:apply-templates>
				
				document.write(schooltree<xsl:value-of select="/document/module/moduleID"/>);
			</script>
		</div>
				
	</xsl:template>

	<xsl:template match="school" mode="tree">
		<xsl:param name="method"/>
		
		<xsl:variable name="name">
            <xsl:call-template name="common_js_escape">
               <xsl:with-param name="text" select="name" />
            </xsl:call-template>			
		</xsl:variable>
			
		schooltree<xsl:value-of select="/document/module/moduleID"/>.add('school<xsl:value-of select="schoolID"/>','base','<xsl:value-of select="$name"/>','','<xsl:value-of select="$name"/>','','<xsl:value-of select="/document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/document/module/sectionID"/>/<xsl:value-of select="/document/module/moduleID"/>/pics/school.png','<xsl:value-of select="/document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/document/module/sectionID"/>/<xsl:value-of select="/document/module/moduleID"/>/pics/school.png','');
		
		<xsl:apply-templates select="groups/group" mode="tree">
		<xsl:with-param name="method" select="$method"/>
		</xsl:apply-templates>
		
	</xsl:template>

	<xsl:template match="group" mode="tree">
		<xsl:param name="method"/>
	
		<xsl:variable name="name">
            <xsl:call-template name="common_js_escape">
               <xsl:with-param name="text" select="name" />
            </xsl:call-template>			
		</xsl:variable>
			
		schooltree<xsl:value-of select="/document/module/moduleID"/>.add('group<xsl:value-of select="groupID"/>','school<xsl:value-of select="../../schoolID"/>','<xsl:value-of select="$name"/>','javascript:moveUsers(\'<xsl:value-of select="/document/requestinfo/contextpath"/><xsl:value-of select="/document/section/fullAlias" />/<xsl:value-of select="/document/module/alias" />/<xsl:value-of select="/document/group/groupID" />/<xsl:value-of select="$method" />/<xsl:value-of select="groupID" />\')','<xsl:value-of select="$name"/>','','<xsl:value-of select="/document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/document/module/sectionID"/>/<xsl:value-of select="/document/module/moduleID"/>/pics/group.png','','');
		
	</xsl:template>

	<!-- End tree templates  -->

	<xsl:template match="GroupInfoElement">
		
		<xsl:apply-templates select="validationException/validationError"/>
		
		<xsl:if test="invitationAreaOnTop = 'true'">
			<xsl:call-template name="createInvitationArea" />
		</xsl:if>
		
		<div class="content-box">
		
			<form method="POST" name="moveusers" action="">
				<script type="text/javascript">
					function moveUsers() {
						document.moveusers.action = "<xsl:value-of select="/document/requestinfo/contextpath"/><xsl:value-of select="/document/section/fullAlias"/>/<xsl:value-of select="/document/module/alias"/>/<xsl:value-of select="/document/group/groupID"/>/moveUsers";
						document.moveusers.submit()
					}
					
					function copyUsers() {
						document.moveusers.action = "<xsl:value-of select="/document/requestinfo/contextpath"/><xsl:value-of select="/document/section/fullAlias"/>/<xsl:value-of select="/document/module/alias"/>/<xsl:value-of select="/document/group/groupID"/>/copyUsers";
						document.moveusers.submit()
					}
				</script>
		
				<h1 class="header">
					<span><xsl:value-of select="$groupInfoElement.users.header" /></span>
				</h1>
	
				<div class="content">
	
					<xsl:choose>
						<xsl:when test="Users/user">
							<xsl:apply-templates select="Users/user" />
							
							<div class="margintop marginbottom floatright">
								<a href="#" onclick="javascript:copyUsers()">
									<img class="alignmiddle" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/copyusers.png" /><xsl:text>&#x20;</xsl:text>
								</a>
								<a href="#" onclick="javascript:copyUsers()">
									<xsl:value-of select="$groupInfoElement.users.copyusers" />
								</a>
								
								<a class="marginleft" href="#" onclick="javascript:moveUsers()">
									<img class="alignmiddle" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/moveusers.png" /><xsl:text>&#x20;</xsl:text>
								</a>
								<a href="#" onclick="javascript:moveUsers()">
									<xsl:value-of select="$groupInfoElement.users.moveusers" />
								</a>
							</div>
							
						</xsl:when>
						<xsl:otherwise>
							<p><xsl:value-of select="$groupInfoElement.users.nousers" /></p>
						</xsl:otherwise>
					</xsl:choose>
	
				</div>
	
			</form>
			
		</div>
		
		<xsl:if test="invitationAreaOnTop = 'false'">
			<xsl:call-template name="createInvitationArea" />
		</xsl:if>
		
		<div class="content-box">
		
			<h1 class="header">
				<span><xsl:value-of select="$groupInfoElement.modules.header" /></span>
			</h1>

			<div class="content">

				<xsl:apply-templates select="AvailableModules/module" />
				
			</div>
			
		</div>
		
		<xsl:if test="OldContentRemover">
			<div class="content-box">
			
				<h1 class="header">
					<span><xsl:value-of select="$groupInfoElement.oldContent.header" /></span>
				</h1>
	
				<div class="content">
				
					<div class="floatleft">
						<xsl:value-of select="$groupInfoElement.oldContent.descriptPre"/>
						<xsl:value-of select="OldContentRemover/Count"/>
						<xsl:value-of select="$groupInfoElement.oldContent.descriptPost"/>
					</div>
					
					<div class="floatright">
						<input type="button" value="{$groupInfoElement.oldContent.begin}"
						 onClick="location.href='{/document/requestinfo/contextpath}{OldContentRemover/URL}/select/{/document/group/groupID}'"/>
					</div>
					
				</div>
				
			</div>
		</xsl:if>

	</xsl:template>
	
	<xsl:template name="createInvitationArea">
		
		<div class="content-box">
		
			<h1 class="header">
				<span><xsl:value-of select="$groupInfoElement.adduser.header" /></span>
			</h1>
			
			<div class="content">
				
				<form method="post" name="adduser" action="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/group/groupID}/addUser">
					
					<table class="floatleft nopaddings full">
						<tr>
							<td><label for="email"><xsl:value-of select="$groupInfoElement.adduser.email" />:</label></td>
							<td><label for="GroupAccessLevel"><xsl:value-of select="$groupInfoElement.adduser.role" />:</label></td>
							<td>
								<label id="commenttext" for="comment">
									<xsl:choose>
										<xsl:when test="requestparameters/parameter[name = 'GroupAccessLevel']/value != 'MEMBER'">
											<xsl:value-of select="$groupInfoElement.adduser.admin.comment" />:
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$groupInfoElement.adduser.member.comment" />:
										</xsl:otherwise>
									</xsl:choose>
								</label>
							</td>
							<td></td>
						</tr>
						<tr>
							<td>
								<input type="text" id="email" name="email" size="40" value="{requestparameters/parameter[name='email']/value}"/>
							</td>
							<td>
								<select id="GroupAccessLevel" name="GroupAccessLevel" onchange="this.options[this.selectedIndex].value != 'MEMBER' ? document.getElementById('commenttext').innerHTML = '{$groupInfoElement.adduser.admin.comment}:' : document.getElementById('commenttext').innerHTML = '{$groupInfoElement.adduser.member.comment}:'" style="width: 100px">
									
									<option value="MEMBER">
										<xsl:if test="requestparameters/parameter[name='GroupAccessLevel']/value = 'MEMBER'">
											<xsl:attribute name="selected"/>
										</xsl:if>
										<xsl:value-of select="$groupInfoElement.adduser.parent" />
									</option>
									<option value="PUBLISHER">
										<xsl:if test="requestparameters/parameter[name='GroupAccessLevel']/value = 'PUBLISHER'">
											<xsl:attribute name="selected"/>
										</xsl:if>						
										<xsl:value-of select="$groupInfoElement.adduser.educationalist" />
									</option>
									
									<xsl:if test="/document/isSchoolAdmin">
										<option value="ADMIN">
											<xsl:if test="requestparameters/parameter[name='GroupAccessLevel']/value = 'ADMIN'">
												<xsl:attribute name="selected"/>
											</xsl:if>							
											<xsl:value-of select="$groupInfoElement.adduser.admin" />
										</option>
									</xsl:if>
								</select>
							</td>
							<td>
								<input type="text" id="comment" name="comment" size="30" value="{requestparameters/parameter[name='comment']/value}"/>
							</td>
							<td>
								<input type="submit" value="{$groupInfoElement.adduser.submit}"/>
							</td>
						</tr>
					</table>
							
				</form>	
			</div>	
		</div>
	
		<div class="content-box">
		
			<h1 class="header">
				<span><xsl:value-of select="$groupInfoElement.invitedusers.header" /></span>
			</h1>

			<div class="content">
			
				<xsl:if test="not(Invitations)">
					<div class="noBorderDiv"><p><xsl:value-of select="$groupInfoElement.invitedusers.noinvited" /></p></div>
				</xsl:if>
		
				<xsl:apply-templates select="Invitations/invitation" />
			
			</div>
			
		</div>
	
	</xsl:template>

	<xsl:template match="user">

		<div class="floatleft full marginbottom border">
			<div class="floatleft">
				<xsl:choose>
					<xsl:when test="/document/isSchoolAdmin or communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation[GroupAccessLevel != 'ADMIN']">
						
						<a href="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/group/groupID}/updateUser/{userID}" title="'{$user.showOrChangeUser.title}' {firstname} {lastname}">
							<img class="alignmiddle" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/user.png" />
							<xsl:text>&#x20;</xsl:text>
						</a>
						<a href="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/group/groupID}/updateUser/{userID}" title="'{$user.showOrChangeUser.title}' {firstname} {lastname}">
		
							<xsl:value-of select="firstname" />
		
							<xsl:text>&#x20;</xsl:text>
		
							<xsl:value-of select="lastname" />
		
							<xsl:text>&#x20;</xsl:text>
		
							<xsl:if
								test="communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation/Comment != ''">
								(<xsl:value-of select="communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation/Comment" />)
							</xsl:if>
						</a>
					</xsl:when>
					<xsl:otherwise>
						
						<img class="alignmiddle" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/user.png" />
	
						<xsl:text>&#x20;</xsl:text>
	
						<xsl:value-of select="firstname" />
	
						<xsl:text>&#x20;</xsl:text>
	
						<xsl:value-of select="lastname" />
	
						<xsl:text>&#x20;</xsl:text>
	
						<xsl:if test="communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation/Comment != ''">
							(<xsl:value-of select="communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation/Comment" />)
						</xsl:if>
					</xsl:otherwise>			
				</xsl:choose>
			</div>
			<div class="floatright marginright">

				<xsl:choose>
					<xsl:when
						test="communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation[GroupAccessLevel = 'MEMBER']">
						<xsl:value-of select="$user.member" />
					</xsl:when>
					<xsl:when
						test="communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation[GroupAccessLevel = 'PUBLISHER']">
						<xsl:value-of select="$user.educationalist" />
					</xsl:when>
					<xsl:when
						test="communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation[GroupAccessLevel = 'ADMIN']">
						<xsl:value-of select="$user.admin" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$user.unknown" />
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:choose>
					<xsl:when test="/document/isSchoolAdmin or communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation[GroupAccessLevel != 'ADMIN']">
						<a href="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/group/groupID}/updateUser/{userID}" title="'{$user.showOrChangeUser.title}' {firstname} {lastname}" class="marginleft">
							<img class="alignmiddle"
								src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit_user.png" />
						</a>
	
						<a
							href="javascript:askBeforeRedirect('{$user.delete.title}: {firstname} {lastname}?','{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/group/groupID}/removeUser/{userID}')"
							title="{$user.delete.title}: {firstname} {lastname}">
							<img class="alignmiddle"
								src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete_user.png" />
						</a>
						<input class="alignbottom" type="checkbox" name="move.userID" value="{userID}" />
					</xsl:when>
					<xsl:otherwise>
						<!-- Fyll ut med något här! -->
					</xsl:otherwise>
				</xsl:choose>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="invitation">
		<div class="floatleft full marginbottom border">
			<div class="floatleft">
				<img class="alignmiddle"
					src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/mail.png" />

				<xsl:text>&#x20;</xsl:text>

				<xsl:value-of select="email" />

				<xsl:text>&#x20;</xsl:text>

				(<xsl:value-of select="$invitation.expires" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="expires" />)
			</div>
			<div class="floatright marginright">

				<xsl:choose>
					<xsl:when
						test="groups/group[groupID = /document/group/groupID]/GroupRelation[GroupAccessLevel = 'MEMBER']">
						<xsl:value-of select="$invitation.member" />
					</xsl:when>
					<xsl:when
						test="groups/group[groupID = /document/group/groupID]/GroupRelation[GroupAccessLevel = 'PUBLISHER']">
						<xsl:value-of select="$invitation.educationalist" />
					</xsl:when>
					<xsl:when
						test="groups/group[groupID = /document/group/groupID]/GroupRelation[GroupAccessLevel = 'ADMIN']">
						<xsl:value-of select="$invitation.admin" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$invitation.unknown" />
					</xsl:otherwise>
				</xsl:choose>

				<a href="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/group/groupID}/resendInvitation/{invitationID}"
					title="{$invitation.sendnew.title} {email}" class="marginleft">
					<img class="alignmiddle"
						src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/send_invitation.png" />
				</a>
				<a
					href="javascript:askBeforeRedirect('Ta bort inbjudan {email}?','{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/group/groupID}/removeInvitation/{invitationID}')"
					title="{$invitation.delete.title} {email}?">
					<img class="alignmiddle"
						src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" />
				</a>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="module">
		<div class="floatleft full marginbottom border">
			<div class="floatleft">
				<a href="{/document/requestinfo/contextpath}{link}"><img class="alignmiddle" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/module.png" /><xsl:text>&#x20;</xsl:text></a>
				<a href="{/document/requestinfo/contextpath}{link}"><xsl:value-of select="name" /></a>
			</div>
			<div class="floatright marginright">

				<xsl:choose>
					<xsl:when test="moduleID = ../../EnabledModules/moduleID">
						<a href="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/group/groupID}/disableModule/{moduleID}"
							title="{$modules.disable.title} {name}">
							<img class="alignbottom"
								src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/started.png" />
						</a>
					</xsl:when>
					<xsl:otherwise>
						<a href="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/group/groupID}/enableModule/{moduleID}"
							title="{$modules.enable.title} {name}">
							<img class="alignbottom"
								src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/stopped.png" />
						</a>
					</xsl:otherwise>
				</xsl:choose>
			</div>
		</div>
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
					<xsl:otherwise>
						<xsl:value-of select="$validationError.Other" />
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:choose>
					<xsl:when test="fieldName = 'email'">
						<xsl:value-of select="$validationError.field.email" />! 
					</xsl:when>
					<xsl:when test="fieldName = 'GroupAccessLevel'">
						<xsl:value-of select="$validationError.field.access" />!
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
					<xsl:when test="messageKey='UpdateFailedUserNotFound'">
						<xsl:value-of select="$validationError.messageKey.UpdateFailedUserNotFound" />!
					</xsl:when>
					<xsl:when test="messageKey='DeleteFailedUserNotFound'">
						<xsl:value-of select="$validationError.messageKey.DeleteFailedUserNotFound" />!
					</xsl:when>	
					<xsl:when test="messageKey='NoUserChoosen'">
						<xsl:value-of select="$validationError.messageKey.NoUserChoosen" />!
					</xsl:when>	
					<xsl:otherwise>
						<xsl:value-of select="$validationError.unknownMessageKey" />!
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
	</xsl:template>	
	
	<xsl:template name="common_js_escape">
		<!-- required -->
		<xsl:param name="text"/>
		<xsl:variable name="tmp">		
			<xsl:call-template name="replace-substring">
				<xsl:with-param name="from" select="'&quot;'"/>
				<xsl:with-param name="to">\"</xsl:with-param>
				<xsl:with-param name="value">
					<xsl:call-template name="replace-substring">
						<xsl:with-param name="from">&apos;</xsl:with-param>
						<xsl:with-param name="to">\'</xsl:with-param>
						<xsl:with-param name="value" select="$text" />
					</xsl:call-template>
				</xsl:with-param>
			</xsl:call-template>	
		</xsl:variable>
		<xsl:value-of select="$tmp" />
	</xsl:template>

	<xsl:template name="replace-substring">
	      <xsl:param name="value" />
	      <xsl:param name="from" />
	      <xsl:param name="to" />
	      <xsl:choose>
	         <xsl:when test="contains($value,$from)">
	            <xsl:value-of select="substring-before($value,$from)" />
	            <xsl:value-of select="$to" />
	            <xsl:call-template name="replace-substring">
	               <xsl:with-param name="value" select="substring-after($value,$from)" />
	               <xsl:with-param name="from" select="$from" />
	               <xsl:with-param name="to" select="$to" />
	            </xsl:call-template>
	         </xsl:when>
	         <xsl:otherwise>
	            <xsl:value-of select="$value" />
	         </xsl:otherwise>
	      </xsl:choose>
	</xsl:template>

</xsl:stylesheet>