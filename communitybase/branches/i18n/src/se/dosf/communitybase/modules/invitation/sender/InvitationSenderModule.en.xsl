<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="InvitationSenderModuleTemplates.xsl" />
	
	<xsl:variable name="newInvitationSubject">Invitation to Parents' Meeting</xsl:variable>
	<xsl:variable name="updatedInvitationSubject">Updated invitation to Parents' Meeting!</xsl:variable>
	<xsl:variable name="resentInvitationSubject">Invitation to Parents' Meeting!</xsl:variable>
	<xsl:variable name="expiredInvitationSubject">Your invitation to Parents' Meeting has been deleted!</xsl:variable>
	<xsl:variable name="recalledInvitationSubject">Your invitation to Parents' Meeting has been deleted!</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="invitation.about.part1" select="'If you want to know more about the thoughts behind Parents Meeting, go to:'" />
	<xsl:variable name="invitation.about.part2" select="'Parents Meeting'" />
	<xsl:variable name="invitation.systemadmin" select="'System administrator'" />
	<xsl:variable name="invitation.information" select="'Parents Meeting aims to improve, deepen and simplify the everyday communication between homes and preschools. To activate your account on Parents Meeting, please click on this link'" />
	
	<xsl:variable name="invitation.recalled.header" select="'Your invitation to Parents Meeting has been deleted'" />
	<xsl:variable name="invitation.recalled.customlink" select="'Link to'" />
	
	<xsl:variable name="invitation.update.header" select="'Your invitation to Parents Meeting has been updated, and you have been given the following role(s)'" />
	
	<xsl:variable name="invitation.new.header" select="'You have been invited to Parents Meeting and have been give the following role(s)'" />
	<xsl:variable name="invitation.new.expires" select="'This invitation is valid until'" />
	
	<xsl:variable name="invitation.school.resource" select="'Resource person in'" />
	
	<xsl:variable name="invitation.group.admin" select="'Administrator for'" />
	<xsl:variable name="invitation.group.publisher" select="'Educationalist for'" />
	<xsl:variable name="invitation.group.member" select="'Parent to pupil in'" />
	<xsl:variable name="invitation.group.on" select="'on'" />			

</xsl:stylesheet>
