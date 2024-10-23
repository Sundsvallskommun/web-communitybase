<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/membersmodule.js
	</xsl:variable>

	<xsl:template match="document">
		<div class="contentitem">
				
			<h1>
				<xsl:value-of select="/document/module/name" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="$document.header.part1" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="/document/group/name" /> 
				<xsl:text>&#x20;(</xsl:text>
				<xsl:choose>
					<xsl:when test="/document/isAdmin">
						<xsl:value-of select="count(Members/user)"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="count(Members/user) - count(Members/HiddenUsers/userID)"/>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:text>&#x20;</xsl:text><xsl:value-of select="$document.header.part2" />.)
			</h1>
			<xsl:apply-templates select="Members" />
		</div>
	</xsl:template>
	
	<xsl:template match="Members">
		
		<xsl:call-template name="createLinks" />
		
		<xsl:if test="user/communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation[GroupAccessLevel = 'ADMIN' or GroupAccessLevel = 'PUBLISHER']">
			<h2 class="thirty"><xsl:value-of select="$Members.publishers" /></h2>
			<xsl:apply-templates select="user[communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation[GroupAccessLevel = 'ADMIN' or GroupAccessLevel = 'PUBLISHER']]"/>
		</xsl:if>
		
		<xsl:if test="user/communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation[GroupAccessLevel = 'MEMBER']">
			<h2 class="thirty"><xsl:value-of select="$Members.members" /></h2>
			<xsl:apply-templates select="user[communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation[GroupAccessLevel = 'MEMBER']]"/>
		</xsl:if>
		
		<xsl:call-template name="createLinks" />
		
		<xsl:if test="not(user)">
			<h2><xsl:value-of select="$Members.nousers" /></h2>
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template match="user">
		
		<xsl:variable name="userID" select="userID" />
		
		<xsl:variable name="isHidden">
			<xsl:if test="../HiddenUsers[userID=$userID]">
				<xsl:text>true</xsl:text>
			</xsl:if>
		</xsl:variable>
		
		<xsl:if test="$isHidden != 'true' or /document/isAdmin">
		
			<div class="content-box">
				
				<xsl:if test="$isHidden = 'true'">
					<xsl:attribute name="class">content-box disabled</xsl:attribute>
				</xsl:if>
				
				<h1 class="header">
					<div class="floatleft">
						<b>
							<xsl:value-of select="firstname"/>
							
							<xsl:text>&#x20;</xsl:text>
												
							<xsl:value-of select="lastname"/>
							
							<xsl:text>&#x20;</xsl:text>
							
							<xsl:choose>
								<xsl:when test="communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation/Comment != ''">
									(<xsl:value-of select="communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation/Comment" />)
								</xsl:when>
								<xsl:when test="communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation[GroupAccessLevel = 'PUBLISHER']">
									(<xsl:value-of select="$user.member" />)
								</xsl:when>
								<xsl:when test="communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation[GroupAccessLevel = 'ADMIN']">
									(<xsl:value-of select="$user.admin" />)
								</xsl:when>
							</xsl:choose>
						</b>
					</div>
					<xsl:if test="/document/isAdmin">
						<div class="floatright">
							<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/toggleVisibility/{userID}">
								<xsl:choose>
									<xsl:when test="$isHidden = 'true'">
										<xsl:attribute name="title"><xsl:value-of select="$i18n.ShowUser" />: <xsl:value-of select="email" /></xsl:attribute>
										<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/hidden.png" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="title"><xsl:value-of select="$i18n.HideUser" />: <xsl:value-of select="email" /></xsl:attribute>
										<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/visible.png" />
									</xsl:otherwise>
								</xsl:choose>
							</a>
						</div>
					</xsl:if>
				</h1>
				
				<div class="content">
				
					<div class="floatleft full marginbottom">
						
						<div class="floatleft" style="width: 120px;">
							<p class="nomargin">
								<span class="floatleft"><xsl:value-of select="$user.email" />:</span>
								<xsl:call-template name="createCheckbox">
									<xsl:with-param name="id" select="$userID" />
									<xsl:with-param name="name" select="'emailselection'" />
									<xsl:with-param name="class" select="'floatright marginright vertical-align-middle'" />
								</xsl:call-template>
							</p>
						</div>
						<div class="floatleft">
							<a id="member_{$userID}" href="mailto:{email}"><xsl:value-of select="email" /></a>
						</div>
						
					</div>
					
					<xsl:if test="communityUserAttributes/phoneHome">
						<div class="floatleft full marginbottom">
							<div class="floatleft" style="width: 120px;">
								<p class="nomargin"><xsl:value-of select="$user.phoneHome" />:</p>
							</div>
							<div class="floatleft">
								<xsl:value-of select="communityUserAttributes/phoneHome" />
							</div>
						</div>
					</xsl:if>
					
					<xsl:if test="communityUserAttributes/phoneMobile">
						<div class="floatleft full marginbottom">
							<div class="floatleft" style="width: 120px;">
								<p class="nomargin"><xsl:value-of select="$user.phoneMobile" />:</p>
							</div>
							<div class="floatleft">
								<xsl:value-of select="communityUserAttributes/phoneMobile" />
							</div>
						</div>
					</xsl:if>
					
					<xsl:if test="communityUserAttributes/phoneWork">
						<div class="floatleft full marginbottom">
							<div class="floatleft" style="width: 120px;">
								<p class="nomargin"><xsl:value-of select="$user.phoneWork" />:</p>
							</div>
							<div class="floatleft">
								<xsl:value-of select="communityUserAttributes/phoneWork" />
							</div>
						</div>
					</xsl:if>
				
				</div>
				
			</div>
		
		</xsl:if>
		
	</xsl:template>

	<xsl:template name="createLinks">
		
		<div class="floatright marginleft">
			<a href="javascript:void(0);" onclick="checkAll();" title="{$i18n.CheckAll}"><xsl:value-of select="$i18n.CheckAll" /></a><xsl:text>&#x20;|&#x20;</xsl:text>
			<a href="javascript:void(0);" onclick="unCheckAll();" title="{$i18n.UnCheckAll}"><xsl:value-of select="$i18n.UnCheckAll" /></a>
		</div>
		
		<div class="send-email-link floatright hidden">
			<a href="javascript:void(0);" onclick="sendEmail();" title="{$i18n.SendEmailsToMembers}"><img class="alignmiddle" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/mail_send.png" /><xsl:text>&#x20;</xsl:text></a>
			<a href="javascript:void(0);" onclick="sendEmail();" title="{$i18n.SendEmailsToMembers}"><xsl:value-of select="$i18n.SendEmailsToMembers" /></a>
			<xsl:text>&#x20;|&#x20;</xsl:text>
		</div>
		
	</xsl:template>

</xsl:stylesheet> 