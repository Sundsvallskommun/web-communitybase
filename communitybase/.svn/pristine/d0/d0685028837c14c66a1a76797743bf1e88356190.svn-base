<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:template match="document">
		<div class="contentitem">
			<div class="normal">	
				<h1><xsl:value-of select="/document/module/name" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="$document.header.part1" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="/document/group/name" /> (<xsl:value-of select="count(Members/user)"/><xsl:text>&#x20;</xsl:text><xsl:value-of select="$document.header.part2" />.)</h1>
			</div>
			<xsl:apply-templates select="Members" />
		</div>
	</xsl:template>
	
	<xsl:template match="Members">
		
		<xsl:if test="user/communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation[GroupAccessLevel = 'ADMIN' or GroupAccessLevel = 'PUBLISHER']">
			<h2><xsl:value-of select="$Members.publishers" /></h2>
			<xsl:apply-templates select="user[communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation[GroupAccessLevel = 'ADMIN' or GroupAccessLevel = 'PUBLISHER']]"/>
		</xsl:if>
		
		<xsl:if test="user/communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation[GroupAccessLevel = 'MEMBER']">
			<h2><xsl:value-of select="$Members.members" /></h2>
			<xsl:apply-templates select="user[communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation[GroupAccessLevel = 'MEMBER']]"/>
		</xsl:if>
		
		<xsl:if test="not(user)">
			<h2><xsl:value-of select="$Members.nousers" /></h2>
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template match="user">
		<div class="divNormal">
			<div class="normalTableHeading floatleft" style="width: 58.5em;">
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
			<div class="noBorderDiv" style="padding: 2px 2px 2px 3px;">
				<div class="floatleft" style="width: 120px;">
					<p class="heading"><xsl:value-of select="$user.email" />:</p> 
				</div>
				<div class="floatleft">
					<a href="mailto:{email}"><xsl:value-of select="email" /></a>
				</div>
			</div>
			<xsl:if test="communityUserAttributes/phoneHome">
				<div class="noBorderDiv" style="padding: 2px 2px 2px 3px;">
					<div class="floatleft" style="width: 120px;">
						<p class="heading"><xsl:value-of select="$user.phoneHome" />:</p>
					</div>
					<div class="floatleft">
						<xsl:value-of select="communityUserAttributes/phoneHome" />
					</div>
				</div>
			</xsl:if>
			<xsl:if test="communityUserAttributes/phoneMobile">
				<div class="noBorderDiv" style="padding: 2px 2px 2px 3px;">
					<div class="floatleft" style="width: 120px;">
						<p class="heading"><xsl:value-of select="$user.phoneMobile" />:</p>
					</div>
					<div class="floatleft">
						<xsl:value-of select="communityUserAttributes/phoneMobile" />
					</div>
				</div>
			</xsl:if>
			<xsl:if test="communityUserAttributes/phoneWork">
				<div class="noBorderDiv" style="padding: 2px 2px 2px 3px;">
					<div class="floatleft" style="width: 120px;">
						<p class="heading"><xsl:value-of select="$user.phoneWork" />:</p>
					</div>
					<div class="floatleft">
						<xsl:value-of select="communityUserAttributes/phoneWork" />
					</div>
				</div>
			</xsl:if>
		</div>
	</xsl:template>

</xsl:stylesheet> 