<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"/>

	<xsl:template match="document">
		<xsl:apply-templates select="newInvitation/invitation" mode="new"/>
		<xsl:apply-templates select="updatedInvitation/invitation" mode="update"/>
		<xsl:apply-templates select="resendInvitation/invitation" mode="new"/>
		<xsl:apply-templates select="expiredInvitation/invitation" mode="recalled"/>
		<xsl:apply-templates select="recalledInvitation/invitation" mode="recalled"/>
	</xsl:template>
		
	<xsl:template match="invitation" mode="recalled">
		<html>
			<body>
				<table class="normal">
					<tr>
						<th align="left"><xsl:value-of select="$invitation.recalled.header" />!</th>
					</tr>	
					<tr>
						<td>
							<xsl:value-of select="$invitation.about.part1" /><xsl:text>&#x20;</xsl:text><a href="{/document/systemURL}"><xsl:value-of select="$invitation.about.part2" /></a>
						</td>
					</tr>
					<tr>
						<td>
							<xsl:value-of select="$invitation.recalled.customlink" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="/document/municipalityName" /><xsl:text>&#x20;</xsl:text>
							<a href="{/document/municipalityURL}"><xsl:value-of select="/document/municipalityURL" /></a>
						</td>
					</tr>											
				</table>				
			</body>
		</html>
	</xsl:template>		
	
	<xsl:template match="invitation" mode="update">
		<html>
			<body>
				<table class="normal">
					<tr>
						<th align="left"><xsl:value-of select="$invitation.update.header" />:</th>
					</tr>
					<tr>
						<td>
							<br/>
						</td>
					</tr>					
					<xsl:if test="admin">
						<tr>
							<td>-<xsl:value-of select="$invitation.systemadmin" /></td>
						</tr>					
					</xsl:if>
					
					<xsl:apply-templates select="schools/school"/>
					<xsl:apply-templates select="groups/group"/>

					<tr>
						<td>
							<br/>
							<xsl:value-of select="$invitation.information" />:
							<xsl:text>&#x20;</xsl:text>							
							<a href="{/document/registrationURL}/{invitationID}/{linkID}">
								<xsl:value-of select="/document/registrationURL"/>/<xsl:value-of select="invitationID"/>/<xsl:value-of select="linkID"/>
							</a>
						</td>
					</tr>	
					<tr>
						<td>
							<br/>
							<xsl:value-of select="invitation.about.part1" /><xsl:text>&#x20;</xsl:text><a href="{/document/systemURL}"><xsl:value-of select="invitation.about.part2" /></a>
						</td>
					</tr>					
				</table>				
			</body>
		</html>
	</xsl:template>	
	
	<xsl:template match="invitation" mode="new">
		
		<html>
			<body>
				<table class="normal">
					<tr>
						<th align="left"><xsl:value-of select="$invitation.new.header" />:</th>
					</tr>
					<tr>
						<td>
							<br/>
						</td>
					</tr>						
					<xsl:if test="admin">
						<tr>
							<td>-<xsl:value-of select="$invitation.systemadmin" /></td>
						</tr>					
					</xsl:if>
					
					<xsl:apply-templates select="schools/school" mode="invitation"/>
					<xsl:apply-templates select="groups/group"/>

					<tr>
						<td>
							<br/>
							<xsl:value-of select="$invitation.information" />:
							<xsl:text>&#x20;</xsl:text>							
							<a href="{/document/registrationURL}/{invitationID}/{linkID}">
								<xsl:value-of select="/document/registrationURL"/>/<xsl:value-of select="invitationID"/>/<xsl:value-of select="linkID"/>
							</a>
						</td>
					</tr>
					<tr>
						<td>
							<br/>
							<xsl:value-of select="$invitation.new.expires" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="expires"/>.
						</td>
					</tr>						
					<tr>
						<td>
							<br/>
							<xsl:value-of select="$invitation.about.part1" /><xsl:text>&#x20;</xsl:text><a href="{/document/systemURL}"><xsl:value-of select="$invitation.about.part2" /></a>
						</td>
					</tr>					
				</table>				
			</body>
		</html>		
	</xsl:template>
	
	<xsl:template match="school" mode="invitation">
		<tr>
			<td>
				<xsl:text>-</xsl:text><xsl:value-of select="$invitation.school.resource" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="name"/>
			</td>
		</tr>
	</xsl:template>
	
	<xsl:template match="group">
		<tr>
			<td>
				<xsl:if test="GroupRelation/GroupAccessLevel='ADMIN'">
					-<xsl:value-of select="$invitation.group.admin" />
				</xsl:if>
				<xsl:if test="GroupRelation/GroupAccessLevel='PUBLISHER'">
					-<xsl:value-of select="$invitation.group.publisher" />
				</xsl:if>
				<xsl:if test="GroupRelation/GroupAccessLevel='MEMBER'">
					-<xsl:value-of select="$invitation.group.member" />
				</xsl:if>
												
				<xsl:text>&#x20;</xsl:text>
				<xsl:value-of select="name"/><xsl:text>&#x20;</xsl:text><xsl:value-of select="$invitation.group.on" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="school/name"/>
			</td>
		</tr>
	</xsl:template>						
</xsl:stylesheet>