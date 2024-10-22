<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="GlobalAdminModuleTemplates.xsl" />
	
	<!-- BreadCrumb variables -->
	
	<xsl:variable name="apos">'</xsl:variable>
	
	<xsl:variable name="renameScoolBreadCrumbTest">Rename school: </xsl:variable>
	<xsl:variable name="updateGroupBreadCrumbTest">Update group: </xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="renameSchool.header" select="'Rename the preschool'" />
	<xsl:variable name="renameSchool.name" select="'Name'" />
	<xsl:variable name="group.rename.email" select="'Email (optional)'" />
	<xsl:variable name="renameSchool.submit" select="'Save changes'" />
	
	<xsl:variable name="group.rename.header" select="'Update group'" />
	<xsl:variable name="group.rename.name" select="'Name'" />
	<xsl:variable name="group.rename.submit" select="'Save changes'" />
	
	<xsl:variable name="systemInfoElement.header" select="'Administrate preschools'" />
	<xsl:variable name="systemInfoElement.school.header" select="'Preschools'" />
	<xsl:variable name="systemInfoElement.school.noschool" select="'There are no preschools'" />
	<xsl:variable name="systemInfoElement.school.addschool" select="'Add preschool'" />
	<xsl:variable name="systemInfoElement.school.addschool.name" select="'Name'" />
	<xsl:variable name="systemInfoElement.admins.header" select="'Administrator'" />
	<xsl:variable name="systemInfoElement.admins.noadmins" select="'There are no administrators'" />
	<xsl:variable name="systemInfoElement.invitedadmins.header" select="'Invited adminstrators'" />
	<xsl:variable name="systemInfoElement.invitedadmins.noadmins" select="'There are no invited adminstrators'" />
	<xsl:variable name="systemInfoElement.addadmins.header" select="'Add administrator'" />
	<xsl:variable name="systemInfoElement.addadmins.email" select="'Email'" />
	<xsl:variable name="systemInfoElement.addadmins.language" select="'Language'" />
	<xsl:variable name="systemInfoElement.addadmins.submit" select="'Add'" />
	
	<xsl:variable name="schools.tree.title" select="'Preschools'" />
	<xsl:variable name="schools.tree.expandAll" select="'Expand all'" />
	<xsl:variable name="schools.tree.collapseAll" select="'Collapse all'" />
	
	<xsl:variable name="school.tree.changename.title" select="'Rename preschool'" />
	<xsl:variable name="school.tree.delete.title" select="'Delete preschool'" />
	
	<xsl:variable name="group.tree.changename.title" select="'Update group'" />
	<xsl:variable name="group.tree.delete.title" select="'Delete group'" />
	
	<xsl:variable name="user.admin.delete.title" select="'Delete user'" />
	
	<xsl:variable name="invitation.admin.expires" select="'Valid until'" />
	<xsl:variable name="invitation.admin.sendnew.title" select="'Send the invite again to'" />
	<xsl:variable name="invitation.admin.delete.title" select="'Delete the invite'" />
	
	<xsl:variable name="schoolInfoElement.header" select="'Administrate preschool'" />
	<xsl:variable name="schoolInfoElement.groups.header" select="'Groups'" />
	<xsl:variable name="schoolInfoElement.groups.nogroups" select="'There are no groups'" />
	<xsl:variable name="schoolInfoElement.addgroup.header" select="'Add group'" />
	<xsl:variable name="schoolInfoElement.addgroup.name" select="'Name'" />
	<xsl:variable name="schoolInfoElement.addgroup.submit" select="'Add'" />
	<xsl:variable name="schoolInfoElement.resources.header" select="'Resource persons'" />
	<xsl:variable name="schoolInfoElement.resources.noresources" select="'There are no resource persons'" />
	<xsl:variable name="schoolInfoElement.invitedresources.header" select="'Invited resource persons'" />
	<xsl:variable name="schoolInfoElement.invitedresources.noinvited" select="'There are no invited resource persons'" />
	<xsl:variable name="schoolInfoElement.addresource.header" select="'Add resource person'" />	
	<xsl:variable name="schoolInfoElement.addresource.email" select="'Email'" />
	<xsl:variable name="schoolInfoElement.addresource.submit" select="'Add'" />
	
	<xsl:variable name="group.changename.title" select="'Rename group'" />
	<xsl:variable name="group.delete.title" select="'Delete group'" />
	
	<xsl:variable name="user.schooladmin.delete.title" select="'Delete user'" />
	
	<xsl:variable name="invitation.schooladmin.expires" select="'Valid until'" />
	<xsl:variable name="invitation.schooladmin.sendnew.title" select="'Resend invite to'" />
	<xsl:variable name="invitation.schooladmin.delete.title" select="'Delete invite'" />
	
	<xsl:variable name="validationError.RequiredField" select="'You have to fill in the field'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Invalid value of the field'" />
	<xsl:variable name="validationError.Other" select="'The path is invalid, please edit the field'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'The field has an unknown error'" />
	<xsl:variable name="validationError.field.name" select="'name'" />
	<xsl:variable name="validationError.field.email" select="'email'" />
	<xsl:variable name="validationError.messageKey.DeleteFailedUserNotFound" select="'The user you are trying to remove could not be found'" />
	<xsl:variable name="validationError.messageKey.RenameFailedGroupNotFound" select="'The group whose name you are trying to change could not be found'"/>
	<xsl:variable name="validationError.messageKey.DeleteFailedGroupNotFound" select="'The group you are trying to delete could not be found'"/>
	<xsl:variable name="validationError.unknownMessageKey" select="'An unknown error has occured'" />			

</xsl:stylesheet>
