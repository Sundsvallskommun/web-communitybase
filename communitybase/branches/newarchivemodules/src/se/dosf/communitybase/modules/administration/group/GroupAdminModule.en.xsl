<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="GroupAdminModuleTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.administration.by" select="'by'" />
	
	<xsl:variable name="user.update.header" select="'Update user'" />
	<xsl:variable name="user.update.authorize" select="'User level'" />
	<xsl:variable name="user.update.parent" select="'Parent'" />
	<xsl:variable name="user.update.educationalist" select="'Educationalist'" />
	<xsl:variable name="user.update.admin" select="'Administrator'" />
	<xsl:variable name="user.update.comment.member" select="'First and last name of the child(ren)'" />
	<xsl:variable name="user.update.comment.admin" select="'Comment'" />
	<xsl:variable name="user.update.email" select="'Email'" />
	<xsl:variable name="user.update.phonehome" select="'Phone home'" />
	<xsl:variable name="user.update.mobilephone" select="'Phone mobile'" />
	<xsl:variable name="user.update.phonework" select="'Phone work'" />
	<xsl:variable name="user.update.submit" select="'Save changes'" />
	
	<xsl:variable name="users.move.header" select="'Move users'" />
	<xsl:variable name="users.move.text.part1" select="'You have choosen to move'" />
	<xsl:variable name="users.move.text.part2" select="'users.'" />
	<xsl:variable name="users.move.description" select="'Click on the group which users should be moved to.'" />
	<xsl:variable name="users.copy.header" select="'Copy users'" />
	<xsl:variable name="users.copy.text.part1" select="'You have choosen to copy'" />
	<xsl:variable name="users.copy.text.part2" select="'users.'" />
	<xsl:variable name="users.copy.description" select="'Click on the group which users should be copied to.'" />
	<xsl:variable name="schools.tree.title" select="'Preschools'" />
	<xsl:variable name="schools.tree.expandAll" select="'Expand all'" />
	<xsl:variable name="schools.tree.collapseAll" select="'Collapse all'" />
	
	<xsl:variable name="groupInfoElement.users.header" select="'Users'" />
	<xsl:variable name="groupInfoElement.users.nousers" select="'There are no users in this group'" />
	<xsl:variable name="groupInfoElement.users.moveusers" select="'Move marked users'" />
	<xsl:variable name="groupInfoElement.users.copyusers" select="'Copy marked users'" />
	<xsl:variable name="groupInfoElement.invitedusers.header" select="'Invited users'" />
	<xsl:variable name="groupInfoElement.invitedusers.noinvited" select="'There are no invited users in this group'" />
	<xsl:variable name="groupInfoElement.adduser.header" select="'Add user'" />
	<xsl:variable name="groupInfoElement.adduser.email" select="'Email'" />
	<xsl:variable name="groupInfoElement.adduser.role" select="'Role'" />
	<xsl:variable name="groupInfoElement.adduser.admin.comment" select="'Comment'" />
	<xsl:variable name="groupInfoElement.adduser.member.comment" select="'First and last name of the child(ren)'" />
	<xsl:variable name="groupInfoElement.adduser.parent" select="'Parent'" />
	<xsl:variable name="groupInfoElement.adduser.educationalist" select="'Educationalist'" />
	<xsl:variable name="groupInfoElement.adduser.admin" select="'Administrator'" />
	<xsl:variable name="groupInfoElement.adduser.submit" select="'Add'" />
	<xsl:variable name="groupInfoElement.modules.header" select="'Modules'" />
	
	<xsl:variable name="user.showOrChangeUser.title" select="'Show/edit user'" />
	<xsl:variable name="user.delete.title" select="'Delete user'" />
	<xsl:variable name="user.member" select="'Parent'" />
	<xsl:variable name="user.educationalist" select="'Educationalist'" />
	<xsl:variable name="user.admin" select="'Administrator'" />
	<xsl:variable name="user.unknown" select="'Unknown user level'" />
	
	<xsl:variable name="invitation.expires" select="'Valid until'" />
	<xsl:variable name="invitation.member" select="'Parent'" />
	<xsl:variable name="invitation.educationalist" select="'Educationalist'" />
	<xsl:variable name="invitation.admin" select="'Administrator'" />
	<xsl:variable name="invitation.unknown" select="'Unknown user level'" />
	<xsl:variable name="invitation.sendnew.title" select="'Resend invite to'" />
	<xsl:variable name="invitation.delete.title" select="'Delete invite'" />

	<xsl:variable name="modules.disable.title" select="'Disable module'" />
	<xsl:variable name="modules.enable.title" select="'Activate module'" />
	
	<xsl:variable name="validationError.RequiredField" select="'You have to fill in the field'" />
	<xsl:variable name="validationError.InvalidFormat" select="'The field has an invalid value'" />
	<xsl:variable name="validationError.Other" select="'The path is invalid, please edit the field'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'The field has an unknown error'" />
	<xsl:variable name="validationError.field.email" select="'email'" />
	<xsl:variable name="validationError.field.access" select="'user level'" />
	<xsl:variable name="validationError.messageKey.DeleteFailedUserNotFound" select="'The user you are trying to delete could not be found'" />
	<xsl:variable name="validationError.messageKey.UpdateFailedUserNotFound" select="'The user you are trying to update could not be found'" />
	<xsl:variable name="validationError.messageKey.NoUserChoosen" select="'You need to choose at least one user to move/copy'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'An unknown error has occured'" />				

</xsl:stylesheet>
