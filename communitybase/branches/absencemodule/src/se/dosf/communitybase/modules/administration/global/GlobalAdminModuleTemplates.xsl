<?xml version="1.0" encoding="ISO-8859-1" ?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/utils/treeview/jquery.cookie.js
		/utils/treeview/jquery.jstree.custom.js
		/utils/treeview/communitybase.treeview.js
		/utils/js/confirmDelete.js
	</xsl:variable>	

	<xsl:variable name="links">
		/css/globaladmin.treeview.css
		/utils/treeview/themes/communitybase/style.css
	</xsl:variable>

	<xsl:variable name="moduleImagePath"><xsl:value-of select="/document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/document/module/sectionID" />/<xsl:value-of select="/document/module/moduleID" />/pics</xsl:variable>

	<xsl:template match="document">

		<script 
			type="text/javascript"
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/askBeforeRedirect.js">
		</script>

		<div class="contentitem">

			<xsl:apply-templates select="systemInfoElement" />
			<xsl:apply-templates select="schoolInfoElement" />
			<xsl:apply-templates select="renameSchool"/>
			<xsl:apply-templates select="updateGroup/group" mode="update"/>
			
		</div>
		
	</xsl:template>

	<xsl:template match="renameSchool">
	
		<h1><xsl:value-of select="$systemInfoElement.header" /></h1>
	
		<form method="post" action="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/school/schoolID}/renameSchool">
			
			<div class="content-box">				

				<h1 class="header">
					<xsl:value-of select="$renameSchool.header" />: <xsl:text>&#x20;</xsl:text><xsl:value-of select="/document/school/name" />
				</h1>

				<div class="content">
				
					<xsl:apply-templates select="../validationException/validationError"/>
				
					<p>
						<label for="shoolName"><xsl:value-of select="$renameSchool.name" />:</label><br/>
						<input type="text" id="shoolName" name="schoolName" size="70">
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
					</p>
							
					<div class="text-align-right">
						<input type="submit" value="{$renameSchool.submit}"/>
					</div>
					
				</div>
			
			</div>
					
		</form>
	</xsl:template>

	<xsl:template match="group" mode="update">
		
		<h1><xsl:value-of select="$systemInfoElement.header" /></h1>
		
		<form method="post" action="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/school/schoolID}/updateGroup/{groupID}">
			
			<xsl:apply-templates select="../validationException/validationError"/>
			
			<div class="content-box">
				
				<h1 class="header">
					<xsl:value-of select="$group.rename.header" />: <xsl:text>&#x20;</xsl:text><xsl:value-of select="name" />
				</h1>
				
				<div class="content">
				
					<p>
						<label for="groupName"><xsl:value-of select="$renameSchool.name" />:</label><br/>
						<input type="text" id="groupName" name="groupName" size="70">
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
					</p>
					
					<xsl:choose>
						<xsl:when test="../useGroupEmail = 'true'">
							<p>
								<label for="email"><xsl:value-of select="$group.rename.email" />:</label><br/>
								<xsl:call-template name="createTextField">
									<xsl:with-param name="id" select="'email'" />
									<xsl:with-param name="name" select="'email'" />
									<xsl:with-param name="value" select="email" />
									<xsl:with-param name="size" select="'70'" />
									<xsl:with-param name="requestparameters" select="../requestparameters" />
								</xsl:call-template>
							</p>
						</xsl:when>
						<xsl:when test="email">
							<input type="hidden" name="email" value="{email}" />
						</xsl:when>
					</xsl:choose>
				
					<div class="text-align-right">
						<input type="submit" value="{$group.rename.submit}"/>
					</div>
				
				</div>
		
			</div>
					
		</form>
	</xsl:template>

	<xsl:template match="systemInfoElement">
		
		<h1><xsl:value-of select="$systemInfoElement.header" /></h1>
		
		<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/dtree/dtree.js"/>
		
		<xsl:apply-templates select="validationException/validationError"/>
		
		<div class="content-box">
		
			<h1 class="header">
				<xsl:value-of select="$systemInfoElement.school.header" />
			</h1>
	
			<div class="content">
	
				<xsl:apply-templates select="schools" mode="admin-tree"/>
			
				<xsl:if test="not(schools)">
					<p><xsl:value-of select="$systemInfoElement.school.noschool" /></p>
				</xsl:if>
				
			</div>
			
		</div>
		
		<xsl:if test="/document/user/admin = 'true'">
		
			<div class="content-box">
			
				<h1 class="header">
					<xsl:value-of select="$systemInfoElement.school.addschool" />
				</h1>
				
				<div class="content">
				
					<form method="post" action="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/addSchool">
						<label for="schoolName"><xsl:value-of select="$systemInfoElement.school.addschool.name" />:</label><xsl:text>&#160;&#160;&#160;</xsl:text>
						<input type="text" id="schoolName" name="schoolName" size="70" value="{requestparameters/parameter[name='schoolName']/value}"/>
						
						<div class="floatright">
							<input type="submit" value="Lägg till"/>	
						</div>		
					</form>	
				
				</div>
				
			</div>		
			
			<div class="content-box">
			
				<h1 class="header">
					<xsl:value-of select="$systemInfoElement.admins.header" />
				</h1>
				
				<div class="content">
				
					<xsl:if test="not(admins)">
						<p><xsl:value-of select="$systemInfoElement.admins.noadmins" /></p>
					</xsl:if>
			
					<xsl:apply-templates select="admins/user" mode="admin"/>
				
				</div>
				
			</div>		
			
			<div class="content-box">
			
				<h1 class="header">
					<xsl:value-of select="$systemInfoElement.invitedadmins.header" />
				</h1>
				
				<div class="content">
				
					<xsl:if test="not(invitations)">
						<p><xsl:value-of select="$systemInfoElement.invitedadmins.noadmins" /></p>
					</xsl:if>
			
					<xsl:apply-templates select="invitations/invitation" mode="admin"/>
				
				</div>
				
			</div>
			
			<div class="content-box">
			
				<h1 class="header">
					<xsl:value-of select="$systemInfoElement.addadmins.header" />
				</h1>
				
				<div class="content">
				
					<form method="post" action="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/addAdmin">
						<label for="email"><xsl:value-of select="$systemInfoElement.addadmins.email" />:</label><xsl:text>&#160;&#160;&#160;</xsl:text>
						<input type="text" id="email" name="email" size="70" value="{requestparameters/parameter[name='email']/value}"/>
						
						<div class="floatright">
							<input type="submit" value="{$systemInfoElement.addadmins.submit}"/>
						</div>		
					</form>	
					
				</div>	
			</div>		
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="user" mode="admin">
		<div class="floatleft full marginbottom border">
			<div class="floatleft">
				<img class="alignmiddle" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/user.png" />

				<xsl:text>&#x20;</xsl:text>

				<xsl:value-of select="firstname" />

				<xsl:text>&#x20;</xsl:text>

				<xsl:value-of select="lastname" />
			</div>
			<div class="floatright marginright">
				<a
					href="javascript:askBeforeRedirect('{$user.admin.delete.title}: {firstname} {lastname}?','{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/removeAdmin/{userID}')"
					title="{$user.admin.delete.title}: {firstname} {lastname}">
					<img class="alignmiddle" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete_user.png" />
				</a>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="invitation" mode="admin">
		<div class="floatleft full marginbottom border">
			<div class="floatleft">
				<img class="alignmiddle"
					src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/mail.png" />

				<xsl:text>&#x20;</xsl:text>

				<xsl:value-of select="email" />

				<xsl:text>&#x20;</xsl:text>

				(<xsl:value-of select="$invitation.admin.expires" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="expires" />)
			</div>
			<div class="floatright marginright">			
				<a href="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/resendInvitation/{invitationID}"
					title="{$invitation.admin.sendnew.title} {email}">
					<img class="alignmiddle"
						src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/send_invitation.png" />
				</a>				
				<a
					href="javascript:askBeforeRedirect('{$invitation.admin.delete.title} {email}?','{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/removeInvitation/{invitationID}')"
					title="{$invitation.admin.delete.title} {email}?">
					<img class="alignmiddle"
						src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" />
				</a>
			</div>
		</div>
	</xsl:template>
	
	<xsl:template match="schoolInfoElement">

		<h1><xsl:value-of select="$schoolInfoElement.header" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="school/name"/></h1>

		<xsl:apply-templates select="validationException/validationError"/>

		<div class="content-box">
		
			<h1 class="header">
				<xsl:value-of select="$schoolInfoElement.groups.header" />
			</h1>
			
			<div class="content">

				<xsl:apply-templates select="school/groups/group"/>
			
				<xsl:if test="not(school/groups)">
					<p><xsl:value-of select="$schoolInfoElement.groups.nogroups" /></p>
				</xsl:if>
			
			</div>
			
		</div>
		
		<div class="content-box">	
			<h1 class="header">
				<xsl:value-of select="$schoolInfoElement.addgroup.header" />
			</h1>
			
			<div class="content">
				
				<form method="post" action="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/school/schoolID}/addGroup">
					
					<table class="full floatleft nopaddings vertical-align-middle">
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
		
		<div class="content-box">
		
			<h1 class="header">
				<xsl:value-of select="$schoolInfoElement.resources.header" />
			</h1>
			
			<div class="content">
			
				<xsl:if test="not(school/admins)">
					<p><xsl:value-of select="$schoolInfoElement.resources.noresources" /></p>
				</xsl:if>
		
				<xsl:apply-templates select="school/admins/user" mode="schooladmin"/>
			
			</div>
			
		</div>		
		
		<div class="content-box">
		
			<h1 class="header">
				<xsl:value-of select="$schoolInfoElement.invitedresources.header" />
			</h1>
			
			<div class="content">
			
				<xsl:if test="not(invitations)">
					<p><xsl:value-of select="$schoolInfoElement.invitedresources.noinvited" /></p>
				</xsl:if>
		
				<xsl:apply-templates select="invitations/invitation" mode="schooladmin"/>
			
			</div>
			
		</div>
		
		<div class="content-box">
		
			<h1 class="header">
				<xsl:value-of select="$schoolInfoElement.addresource.header" />
			</h1>
			
			<div class="content">
				
				<div class="full floatleft">
				
					<form method="post" action="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/school/schoolID}/addSchoolAdmin">
						<label for="email"><xsl:value-of select="$schoolInfoElement.addresource.email" />:</label><xsl:text>&#160;&#160;&#160;</xsl:text>
						<input type="text" id="email" name="email" size="70" value="{requestparameters/parameter[name='email']/value}"/>
						
						<div class="floatright">
							<input type="submit" value="{$schoolInfoElement.addresource.submit}"/>						
						</div>		
					</form>	
				
				</div>
				
			</div>	
		</div>		
	</xsl:template>	
	
	<xsl:template match="group">
		<div class="floatleft full marginbottom border">
			<div class="floatleft">
				<a href="{/document/requestinfo/contextpath}/{/document/groupAdminModuleAlias}/{groupID}">
					<img class="alignmiddle" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/group.png" />
					<xsl:text>&#x20;</xsl:text>
				</a>
				<a href="{/document/requestinfo/contextpath}/{/document/groupAdminModuleAlias}/{groupID}">
					<xsl:value-of select="name" />				
				</a>
			</div>
			<div class="floatright marginright">
				<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/school/schoolID}/updateGroup/{groupID}" title="{$group.changename.title}: {name}">
					<img class="alignmiddle" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png"/>
				</a>			
				<a
					href="javascript:askBeforeRedirect('{$group.delete.title} {name}?','{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/school/schoolID}/deleteGroup/{groupID}')"
					title="{$group.delete.title}: {firstname} {lastname}">
					<img class="alignmiddle" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" />
				</a>
			</div>
		</div>
	</xsl:template>	
	
	<xsl:template match="user" mode="schooladmin">
		<div class="floatleft full marginbottom border">
			<div class="floatleft">
				<img class="alignmiddle" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/user.png" />

				<xsl:text>&#x20;</xsl:text>

				<xsl:value-of select="firstname" />

				<xsl:text>&#x20;</xsl:text>

				<xsl:value-of select="lastname" />
			</div>
			<div class="floatright marginright">
				<a
					href="javascript:askBeforeRedirect('{$user.schooladmin.delete.title}: {firstname} {lastname}?','{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/school/schoolID}/removeSchoolAdmin/{userID}')"
					title="{$user.schooladmin.delete.title}: {firstname} {lastname}">
					<img class="alignmiddle" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete_user.png" />
				</a>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="invitation" mode="schooladmin">
		<div class="floatleft full marginbottom border">
			<div class="floatleft">
				<img class="alignmiddle"
					src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/mail.png" />

				<xsl:text>&#x20;</xsl:text>

				<xsl:value-of select="email" />

				<xsl:text>&#x20;</xsl:text>

				(<xsl:value-of select="$invitation.schooladmin.expires" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="expires" />)
			</div>
			<div class="floatright marginright">
				<a href="{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/school/schoolID}/resendInvitation/{invitationID}"
					title="{$invitation.schooladmin.sendnew.title} {email}">
					<img class="alignmiddle"
						src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/send_invitation.png" />
				</a>			
				<a
					href="javascript:askBeforeRedirect('{$invitation.schooladmin.delete.title} {email}?','{/document/requestinfo/contextpath}{/document/section/fullAlias}/{/document/module/alias}/{/document/school/schoolID}/removeInvitation/{invitationID}')"
					title="{$invitation.schooladmin.delete.title} {email}?">
					<img class="alignmiddle"
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
	
	<xsl:template match="schools" mode="admin-tree">
	
		<div class="jstree-actionbar">
			<a id="jstree-expand-all" href="javascript:void(0);" title="{$schools.tree.expandAll}">
			<xsl:value-of select="$schools.tree.expandAll" /></a><xsl:text>&#160;|&#160;</xsl:text>
			<a id="jstree-collapse-all" href="javascript:void(0);" title="{$schools.tree.collapseAll}">
			<xsl:value-of select="$schools.tree.collapseAll" /></a>
			<xsl:if test="../filteringEnabled = 'true'">
				<xsl:text>&#160;|&#160;</xsl:text>
				<xsl:value-of select="$schools.tree.filter" />:<xsl:text>&#160;</xsl:text><input type="text" id="search-tree-field" />
				<img id="search-tree-field-restore" class="pointer alignmiddle marginleft" src="{$moduleImagePath}/delete12x12.png" title="{$schools.tree.restore}" />
			</xsl:if>
		</div>
	
		<div class="jstree-loading">
	
			<span class="jstree-loading-text hidden"><xsl:value-of select="$schools.tree.loadingTitle" />...</span>
	
			<div id="globaladmin-communitybase-treeview" class="jstree-search communitybase-treeview globaladmin-tree hidden">
				<ul rel="global">
					<li id="global_global" rel="global" class="jstree-force-open">
						<a href="javascript:void(0);" title="{$schools.tree.title}"><img class="alignmiddle" src="{$moduleImagePath}/global.png"/><xsl:text>&#x20;</xsl:text><xsl:value-of select="$schools.tree.title" /></a>
						<ul rel="school">	
							<xsl:apply-templates select="school" mode="admin-tree" />
						</ul>
					</li>
				</ul>
			</div>
		
		</div>
			
	</xsl:template>
	
	<xsl:template match="school" mode="admin-tree">
	
		<xsl:variable name="schoolID" select="schoolID" />
			
		<li id="schoolbase_{$schoolID}" rel="school">
			<div class="item">
				<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{schoolID}" title="{name}">
					<img class="alignmiddle" src="{$moduleImagePath}/school.png"/><xsl:text>&#x20;</xsl:text>
				</a>
				<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{schoolID}" title="{name}">
					<xsl:value-of select="name" />
				</a>
			</div>
			<div class="tools">
				<xsl:if test="/document/user/admin='true'">
					<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{schoolID}/renameSchool" title="{$school.tree.changename.title}: {name}">
						<img class="alignmiddle" src="{$moduleImagePath}/edit.png"/>
					</a>
					<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{schoolID}/deleteSchool" onclick="return confirmDelete('{$school.tree.delete.title}: {name}?')" title="{$school.tree.delete.title}: {name}">
						<img class="alignmiddle" src="{$moduleImagePath}/delete.png"/>
					</a>
				</xsl:if>
			</div>
			<xsl:if test="groups/group">
				<ul rel="group">
					<xsl:apply-templates select="groups/group" mode="admin-tree" />
				</ul>
			</xsl:if>
		</li>
	
	</xsl:template>
	
	<xsl:template match="group" mode="admin-tree">
	
		<xsl:variable name="groupID" select="groupID" />
		<xsl:variable name="schoolID" select="../../schoolID" />

		<li id="groupschool.{$schoolID}_{$schoolID}:{groupID}" rel="group">
	   		<div class="item">
		   		<a href="{/document/requestinfo/contextpath}/{/document/groupAdminModuleAlias}/{groupID}" title="{name}">
		   			<img class="alignmiddle" src="{$moduleImagePath}/group.png"/><xsl:text>&#x20;</xsl:text>
		   		</a>
		   		<a href="{/document/requestinfo/contextpath}/{/document/groupAdminModuleAlias}/{groupID}" title="{name}">
		   			<xsl:value-of select="name" />
		   		</a>
	   		</div>
	   		<div class="tools">
		   		<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{$schoolID}/updateGroup/{groupID}" title="{$group.tree.changename.title}: {name}">
					<img class="alignmiddle" src="{$moduleImagePath}/edit.png"/>
				</a>
				<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{$schoolID}/deleteGroup/{groupID}" onclick="return confirmDelete('{$group.tree.delete.title}: {name}?')" title="{$group.tree.delete.title}: {name}">
					<img class="alignmiddle" src="{$moduleImagePath}/delete.png"/>
				</a>
			</div>
	    </li>
	
	</xsl:template>
	
</xsl:stylesheet>