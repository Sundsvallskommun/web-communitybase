<?xml version="1.0" encoding="ISO-8859-1" ?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:template match="document">

		<script 
			type="text/javascript"
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/askBeforeRedirect.js">
		</script>

		<div class="contentitem holderWide2">

			<xsl:apply-templates select="systemInfoElement" />
			<xsl:apply-templates select="schoolInfoElement" />
			<xsl:apply-templates select="renameSchool"/>
			<xsl:apply-templates select="updateGroup/group" mode="update"/>
		</div>
	</xsl:template>

	<xsl:template match="renameSchool">
	
		<form method="post" action="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/school/schoolID}/renameSchool">
			
			<div class="normal">
				<h1><xsl:value-of select="$renameSchool.header" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="/document/school/name" /></h1>
			</div>
			
			<xsl:apply-templates select="../validationException/validationError"/>
			
			<div class="adminDiv">				
				<div class="noBorderDiv" style="padding: 2px 2px 2px 3px;">
					<div class="floatleft" style="width: 120px;">
						<p class="heading"><xsl:value-of select="$renameSchool.name" />:</p> 
					</div>
					<div class="floatleft">
						<input type="text" name="schoolName" size="70">
							<xsl:choose>
								<xsl:when test="../requestparameters">
									<xsl:attribute name="value">
										<xsl:value-of select="../requestparameters/parameter[name='schoolName']/value"/>
									</xsl:attribute>						
								</xsl:when>
								<xsl:otherwise>
									<xsl:attribute name="value">
										<xsl:value-of select="/document/school/name"/>
									</xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>					
						</input>
					</div>
				</div>			
			</div>
			<div class="floatright">
				<input type="submit" value="{$renameSchool.submit}"/>
			</div>		
		</form>
	</xsl:template>

	<xsl:template match="group" mode="update">
	
		<form method="post" action="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/school/schoolID}/updateGroup/{groupID}">
			
			<div class="normal">
				<h1><xsl:value-of select="$group.rename.header" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="name" /></h1>
			</div>			
			
			<xsl:apply-templates select="../validationException/validationError"/>
			
			<div class="adminDiv">
				<div class="noBorderDiv" style="padding: 2px 2px 2px 3px;">
					<div class="floatleft bigmarginbottom">
						<div class="floatleft" style="width: 120px;">
							<p class="heading"><xsl:value-of select="$group.rename.name" />:</p> 
						</div>
						<div class="floatleft">
							<input type="text" name="groupName">
								<xsl:choose>
									<xsl:when test="../requestparameters">
										<xsl:attribute name="value">
											<xsl:value-of select="../requestparameters/parameter[name='groupName']/value"/>
										</xsl:attribute>						
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="value">
											<xsl:value-of select="name"/>
										</xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>					
							</input>
						</div>
					</div>
					<xsl:choose>
						<xsl:when test="../useGroupEmail = 'true'">
							<div class="clearboth floatleft bigmargintop">
								<div class="floatleft" style="width: 120px;">
									<p class="heading"><xsl:value-of select="$group.rename.email" />:</p> 
								</div>
								<div class="floatleft">
									<xsl:call-template name="createTextField">
										<xsl:with-param name="name" select="'email'" />
										<xsl:with-param name="value" select="email" />
										<xsl:with-param name="requestparameters" select="../requestparameters" />
									</xsl:call-template>
								</div>
							</div>
						</xsl:when>
						<xsl:when test="email">
							<input type="hidden" name="email" value="{email}" />
						</xsl:when>
					</xsl:choose>
				</div>			
			</div>
			<div class="floatright">
				<input type="submit" value="{$group.rename.submit}"/>
			</div>		
		</form>
	</xsl:template>

	<xsl:template match="systemInfoElement">
		
		<div class="normal">
			<h1><xsl:value-of select="$systemInfoElement.header" /></h1>
		</div>
		
		<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/dtree/dtree.js"/>
		
		<xsl:apply-templates select="validationException/validationError"/>
		
		<div class="adminDiv">
		
			<div class="normalTableHeading floatleft" style="width: 58.5em;">
				<b><xsl:value-of select="$systemInfoElement.school.header" /></b>
			</div>

			<xsl:apply-templates select="schools" mode="tree"/>
		
			<xsl:if test="not(schools)">
				<p><xsl:value-of select="$systemInfoElement.school.noschool" /></p>
			</xsl:if>
		</div>
		
		<xsl:if test="/document/user/admin = 'true'">
		
			<div class="adminDiv">
			
				<div class="normalTableHeading floatleft" style="width: 58.5em;">
					<b><xsl:value-of select="$systemInfoElement.school.addschool" /></b>
				</div>
				
				<div class="noBorderDiv">
					<form method="post" action="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/addSchool">
						<xsl:value-of select="$systemInfoElement.school.addschool.name" />: <input type="text" name="schoolName" size="70" value="{requestparameters/parameter[name='schoolName']/value}"/>
						
						<div class="floatright">
							<input type="submit" value="{$systemInfoElement.addadmins.submit}"/>	
						</div>		
					</form>	
				</div>	
			</div>		
			
			<div class="adminDiv">
			
				<div class="normalTableHeading floatleft" style="width: 58.5em;">
					<b><xsl:value-of select="$systemInfoElement.admins.header" /></b>
				</div>
				
				<xsl:if test="not(admins)">
					<p><xsl:value-of select="$systemInfoElement.admins.noadmins" /></p>
				</xsl:if>
		
				<xsl:apply-templates select="admins/user" mode="admin"/>
				
			</div>		
			
			<div class="adminDiv">
			
				<div class="normalTableHeading floatleft" style="width: 58.5em;">
					<b><xsl:value-of select="$systemInfoElement.invitedadmins.header" /></b>
				</div>
				
				<xsl:if test="not(invitations)">
					<p><xsl:value-of select="$systemInfoElement.invitedadmins.noadmins" /></p>
				</xsl:if>
		
				<xsl:apply-templates select="invitations/invitation" mode="admin"/>
				
			</div>
			
			<div class="adminDiv">
			
				<div class="normalTableHeading floatleft" style="width: 58.5em;">
					<b><xsl:value-of select="$systemInfoElement.addadmins.header" /></b>
				</div>
				
				<div class="noBorderDiv">
					<form method="post" action="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/addAdmin">
						<div class="floatleft bigmarginright">
							<xsl:value-of select="$systemInfoElement.addadmins.email" />: <input type="text" name="email" size="50" value="{requestparameters/parameter[name='email']/value}"/>
						</div>

						<xsl:if test="languages">
							<div class="floatleft bigmarginleft">
								<xsl:value-of select="$systemInfoElement.addadmins.language" />: 
								<select style="width: 80px;" name="language">
									<xsl:apply-templates select="languages/language">
										<xsl:with-param name="requestparameters" select="requestparameters" />
									</xsl:apply-templates>
								</select>
							</div>
						</xsl:if>
						<div class="floatright">
							<input type="submit" value="{$systemInfoElement.addadmins.submit}"/>
						</div>		
					</form>	
				</div>	
			</div>		
		</xsl:if>
	</xsl:template>

	<xsl:template match="schools" mode="tree">
		<div class="dtree">
			<p><a href="javascript: schooltree{/document/module/moduleID}.openAll();"><xsl:value-of select="$schools.tree.expandAll" /></a> | <a href="javascript: schooltree{/document/module/moduleID}.closeAll();"><xsl:value-of select="$schools.tree.collapseAll" /></a></p>

			<script type="text/javascript">
				schooltree<xsl:value-of select="/document/module/moduleID"/> = new dTree('schooltree<xsl:value-of select="/document/module/moduleID"/>','<xsl:value-of select="/document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/document/module/sectionID"/>/<xsl:value-of select="/document/module/moduleID"/>/dtree/');
				schooltree<xsl:value-of select="/document/module/moduleID"/>.icon.root = '<xsl:value-of select="/document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/document/module/sectionID"/>/<xsl:value-of select="/document/module/moduleID"/>/dtree/img/globe.gif';
				
				schooltree<xsl:value-of select="/document/module/moduleID"/>.add('base','-1','<xsl:value-of select="$schools.tree.title" />','','<xsl:value-of select="$schools.tree.title" />','','<xsl:value-of select="/document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/document/module/sectionID"/>/<xsl:value-of select="/document/module/moduleID"/>/dtree/img/folder.gif','<xsl:value-of select="/document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/document/module/sectionID"/>/<xsl:value-of select="/document/module/moduleID"/>/dtree/img/folderopen.gif','','');				
				
				<xsl:apply-templates select="school" mode="tree"/>
				
				document.write(schooltree<xsl:value-of select="/document/module/moduleID"/>);
			</script>
		</div>		
	</xsl:template>

	<xsl:template match="school" mode="tree">
	
		<xsl:variable name="name">
            <xsl:call-template name="common_js_escape">
               <xsl:with-param name="text" select="name" />
            </xsl:call-template>			
		</xsl:variable>
			
		schooltree<xsl:value-of select="/document/module/moduleID"/>.add('school<xsl:value-of select="schoolID"/>','base','<xsl:value-of select="$name"/>','<xsl:value-of select="/document/requestinfo/contextpath"/><xsl:value-of select="/document/section/fullAlias"/>/<xsl:value-of select="/document/module/alias"/>/<xsl:value-of select="schoolID"/>','<xsl:value-of select="$name"/>','','<xsl:value-of select="/document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/document/module/sectionID"/>/<xsl:value-of select="/document/module/moduleID"/>/pics/school.png','<xsl:value-of select="/document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/document/module/sectionID"/>/<xsl:value-of select="/document/module/moduleID"/>/pics/school.png','',
		
		'<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/dtree/img/empty.gif"/>
		
		<xsl:if test="/document/user/admin='true'">
			<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{schoolID}/renameSchool" title="{$school.tree.changename.title}: {$name}">
				<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png"/>
			</a>
			
			<a href="javascript:askBeforeRedirect(\'{$school.tree.delete.title}: {$name}?\',\'{/document/requestinfo/currentURI}/{/document/module/alias}/{schoolID}/deleteSchool\');" title="{$school.tree.delete.title}: {$name}">
				<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png"/>
			</a>		
		</xsl:if>');
		
		<xsl:apply-templates select="groups/group" mode="tree"/>
	</xsl:template>

	<xsl:template match="group" mode="tree">
	
		<xsl:variable name="name">
            <xsl:call-template name="common_js_escape">
               <xsl:with-param name="text" select="name" />
            </xsl:call-template>			
		</xsl:variable>
			
		schooltree<xsl:value-of select="/document/module/moduleID"/>.add('group<xsl:value-of select="groupID"/>','school<xsl:value-of select="../../schoolID"/>','<xsl:value-of select="$name"/>','<xsl:value-of select="/document/requestinfo/contextpath"/><xsl:value-of select="/document/section/fullAlias"/>/<xsl:value-of select="/document/groupAdminModuleAlias"/>/<xsl:value-of select="groupID"/>','<xsl:value-of select="$name"/>','','<xsl:value-of select="/document/requestinfo/contextpath"/>/static/f/<xsl:value-of select="/document/module/sectionID"/>/<xsl:value-of select="/document/module/moduleID"/>/pics/group.png','','',
		
		'<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/dtree/img/empty.gif"/>
		
		<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{../../schoolID}/updateGroup/{groupID}" title="{$group.tree.changename.title}: {$name}">
			<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png"/>
		</a>
		
		<a href="javascript:askBeforeRedirect(\'{$group.tree.delete.title}: {$name}?\',\'{/document/requestinfo/currentURI}/{/document/module/alias}/{../../schoolID}/deleteGroup/{groupID}\');" title="{$group.tree.delete.title}: {$name}">
			<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png"/>
		</a>');
	</xsl:template>

	<xsl:template match="user" mode="admin">
		<div class="floatleft full marginbottom border">
			<div class="floatleft">
				<img class="alignbottom" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/user.png" />

				<xsl:text>&#x20;</xsl:text>

				<xsl:value-of select="firstname" />

				<xsl:text>&#x20;</xsl:text>

				<xsl:value-of select="lastname" />
			</div>
			<div class="floatright marginright">
				<a
					href="javascript:askBeforeRedirect('{$user.admin.delete.title}: {firstname} {lastname}?','{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/removeAdmin/{userID}')"
					title="{$user.admin.delete.title}: {firstname} {lastname}">
					<img class="alignbottom" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete_user.png" />
				</a>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="invitation" mode="admin">
		<div class="floatleft full marginbottom border">
			<div class="floatleft">
				<img class="alignbottom"
					src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/mail.png" />

				<xsl:text>&#x20;</xsl:text>

				<xsl:value-of select="email" />

				<xsl:text>&#x20;</xsl:text>

				(<xsl:value-of select="$invitation.admin.expires" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="expires" />)
			</div>
			<div class="floatright marginright">			
				<a href="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/resendInvitation/{invitationID}"
					title="{$invitation.admin.sendnew.title} {email}">
					<img class="alignbottom"
						src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/send_invitation.png" />
				</a>				
				<a
					href="javascript:askBeforeRedirect('{$invitation.admin.delete.title} {email}?','{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/removeInvitation/{invitationID}')"
					title="{$invitation.admin.delete.title} {email}?">
					<img class="alignbottom"
						src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" />
				</a>
			</div>
		</div>
	</xsl:template>
	
	<xsl:template match="schoolInfoElement">

			<div class="normal">
				<h1><xsl:value-of select="$schoolInfoElement.header" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="school/name"/></h1>
			</div>

		<xsl:apply-templates select="validationException/validationError"/>

		<div class="adminDiv">
		
			<div class="normalTableHeading floatleft" style="width: 58.5em;">
				<b><xsl:value-of select="$schoolInfoElement.groups.header" /></b>
			</div>

			<xsl:apply-templates select="school/groups/group"/>
		
			<xsl:if test="not(school/groups)">
				<p><xsl:value-of select="$schoolInfoElement.groups.nogroups" /></p>
			</xsl:if>
		</div>
		
		<div class="adminDiv">	
			<div class="normalTableHeading floatleft" style="width: 58.5em;">
				<b><xsl:value-of select="$schoolInfoElement.addgroup.header" /></b>
			</div>
			
			<div class="noBorderDiv">
				<form method="post" action="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/school/schoolID}/addGroup">
					<table class="full floatleft nopadding vertical-align-middle">
						<tr>
							<td>
								<xsl:value-of select="$schoolInfoElement.addgroup.name" />:
							</td>
							<td>
								<input type="text" name="groupName" size="70" value="{requestparameters/parameter[name='groupName']/value}"/>
							</td>
							<td class="text-align-right">
								<xsl:if test="useGroupEmail = 'false'">
									<input type="submit" value="{$schoolInfoElement.addgroup.submit}"/>
								</xsl:if>
							</td>
						</tr>
						<xsl:if test="useGroupEmail = 'true'">
							<tr>
								<td>
									<xsl:value-of select="$group.rename.email" />:
								</td>
								<td>
									<xsl:call-template name="createTextField">
										<xsl:with-param name="name" select="'email'" />
										<xsl:with-param name="value" select="email" />
										<xsl:with-param name="size" select="'70'" />
									</xsl:call-template>
								</td>
								<td class="text-align-right"><input type="submit" value="{$schoolInfoElement.addgroup.submit}"/></td>
							</tr>
						</xsl:if>
					</table>							
				</form>	
			</div>
				
		</div>		
		
		<div class="adminDiv">
		
			<div class="normalTableHeading floatleft" style="width: 58.5em;">
				<b><xsl:value-of select="$schoolInfoElement.resources.header" /></b>
			</div>
			
			<xsl:if test="not(school/admins)">
				<p><xsl:value-of select="$schoolInfoElement.resources.noresources" /></p>
			</xsl:if>
	
			<xsl:apply-templates select="school/admins/user" mode="schooladmin"/>
			
		</div>		
		
		<div class="adminDiv">
		
			<div class="normalTableHeading floatleft" style="width: 58.5em;">
				<b><xsl:value-of select="$schoolInfoElement.invitedresources.header" /></b>
			</div>
			
			<xsl:if test="not(invitations)">
				<p><xsl:value-of select="$schoolInfoElement.invitedresources.noinvited" /></p>
			</xsl:if>
	
			<xsl:apply-templates select="invitations/invitation" mode="schooladmin"/>
			
		</div>
		
		<div class="adminDiv">
		
			<div class="normalTableHeading floatleft" style="width: 58.5em;">
				<b><xsl:value-of select="$schoolInfoElement.addresource.header" /></b>
			</div>
			
			<div class="noBorderDiv">
				<form method="post" action="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/school/schoolID}/addSchoolAdmin">
	 
					<div class="floatleft bigmarginright">
						<xsl:value-of select="$schoolInfoElement.addresource.email" />: <input type="text" name="email" size="50" value="{requestparameters/parameter[name='email']/value}"/>
					</div>
					
					<xsl:if test="languages">
						<div class="floatleft bigmarginleft">
							<xsl:value-of select="$systemInfoElement.addadmins.language" />: 
							<select style="width: 80px;" name="language">
								<xsl:apply-templates select="languages/language">
									<xsl:with-param name="requestparameters" select="requestparameters" />
								</xsl:apply-templates>
							</select>
						</div>
					</xsl:if>

					<div class="floatright">
						<input type="submit" value="{$schoolInfoElement.addresource.submit}"/>						
					</div>		
				</form>
			</div>	
		</div>		
	</xsl:template>	
	
	<xsl:template match="group">
		<div class="floatleft full marginbottom border">
			<div class="floatleft">
				<a href="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/groupAdminModuleAlias}/{groupID}">
					<img class="alignbottom" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/group.png" />
	
					<xsl:text>&#x20;</xsl:text>
	
					<xsl:value-of select="name" />				
				</a>
			</div>
			<div class="floatright marginright">
				<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/school/schoolID}/updateGroup/{groupID}" title="{$group.changename.title}: {name}">
					<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png"/>
				</a>			
				<a
					href="javascript:askBeforeRedirect('{$group.delete.title} {name}?','{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/school/schoolID}/deleteGroup/{groupID}')"
					title="{$group.delete.title}: {firstname} {lastname}">
					<img class="alignbottom" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" />
				</a>
			</div>
		</div>
	</xsl:template>	
	
	<xsl:template match="user" mode="schooladmin">
		<div class="floatleft full marginbottom border">
			<div class="floatleft">
				<img class="alignbottom" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/user.png" />

				<xsl:text>&#x20;</xsl:text>

				<xsl:value-of select="firstname" />

				<xsl:text>&#x20;</xsl:text>

				<xsl:value-of select="lastname" />
			</div>
			<div class="floatright marginright">
				<a
					href="javascript:askBeforeRedirect('{$user.schooladmin.delete.title}: {firstname} {lastname}?','{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/school/schoolID}/removeSchoolAdmin/{userID}')"
					title="{$user.schooladmin.delete.title}: {firstname} {lastname}">
					<img class="alignbottom" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete_user.png" />
				</a>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="invitation" mode="schooladmin">
		<div class="floatleft full marginbottom border">
			<div class="floatleft">
				<img class="alignbottom"
					src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/mail.png" />

				<xsl:text>&#x20;</xsl:text>

				<xsl:value-of select="email" />

				<xsl:text>&#x20;</xsl:text>

				(<xsl:value-of select="$invitation.schooladmin.expires" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="expires" />)
			</div>
			<div class="floatright marginright">
				<a href="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/school/schoolID}/resendInvitation/{invitationID}"
					title="{$invitation.schooladmin.sendnew.title} {email}">
					<img class="alignbottom"
						src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/send_invitation.png" />
				</a>			
				<a
					href="javascript:askBeforeRedirect('{$invitation.schooladmin.delete.title} {email}?','{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/school/schoolID}/removeInvitation/{invitationID}')"
					title="{$invitation.schooladmin.delete.title} {email}?">
					<img class="alignbottom"
						src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" />
				</a>
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
						<xsl:value-of select="$validationError.unknownValidationErrorType" />
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:choose>
					<xsl:when test="fieldName = 'email'">
						<xsl:value-of select="$validationError.field.email" />!
					</xsl:when>
					<xsl:when test="fieldName = 'schoolName' or fieldName = 'groupName'">
						<xsl:value-of select="$validationError.field.name" />!
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
					<xsl:when test="messageKey='DeleteFailedUserNotFound'">
						<xsl:value-of select="$validationError.messageKey.DeleteFailedUserNotFound" />!
					</xsl:when>
					<xsl:when test="messageKey='RenameFailedGroupNotFound'">
						<xsl:value-of select="$validationError.messageKey.RenameFailedGroupNotFound" />!
					</xsl:when>
					<xsl:when test="messageKey='DeleteFailedGroupNotFound'">
						<xsl:value-of select="$validationError.messageKey.DeleteFailedGroupNotFound" />!
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
	
	<xsl:template match="language">
		<xsl:param name="user" select="null"/>
		<xsl:param name="requestparameters" select="null"/>
		<option value="{code}">
			<xsl:if test="$requestparameters/parameter[name='language']/value = code or (not($requestparameters) and $user/language/code = code)">
				<xsl:attribute name="selected">selected</xsl:attribute>
			</xsl:if>
			<xsl:value-of select="localName" />
		</option>
	</xsl:template>
</xsl:stylesheet>