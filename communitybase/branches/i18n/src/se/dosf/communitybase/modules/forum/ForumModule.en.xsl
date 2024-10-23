<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="ForumModuleTemplates.xsl" />
	
	<xsl:variable name="addForumBreadcrumb">Add forum</xsl:variable>
	<xsl:variable name="updateForumBreadcrumb">Update forum </xsl:variable>
	<xsl:variable name="updateThreadBreadcrumb">Update thread </xsl:variable>
	<xsl:variable name="updateForumPostBreadcrumb">Update forum post</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'for'" />
	<xsl:variable name="communitymodule.noforum" select="'No forums for'" />
	<xsl:variable name="communitymodule.noforums" select="'At the moment there are no forums for'" />
	
	<xsl:variable name="community.delete.confirm" select="'Are you really sure you want to delete the forum and all of it´s content'" />
	<xsl:variable name="community.delete.title" select="'Delete forum'" />
	<xsl:variable name="community.update.title" select="'Edit forum'" />
	
	<xsl:variable name="forumref.addForum" select="'Add new forum'" />
	
	<xsl:variable name="updateCommunity.header" select="'Edit forum'" />
	<xsl:variable name="updateCommunity.name" select="'Name'" />
	<xsl:variable name="updateCommunity.description" select="'Description'" />
	<xsl:variable name="updateCommunity.submit" select="'Description'" />
	
	<xsl:variable name="addCommunity.header" select="'Add forum'" />
	<xsl:variable name="addCommunity.name" select="'Name'" />
	<xsl:variable name="addCommunity.description" select="'Description'" />
	<xsl:variable name="addCommunity.submit" select="'Add'" />
	
	<xsl:variable name="showCommunityThreads.noThreads" select="'There are no threads in this forum'" />
	<xsl:variable name="showCommunityThreads.addThread" select="'Add new thread'" />
	
	<xsl:variable name="thread.delete.confirm.part1" select="'Are you really sure you want to delete the thread'" />
	<xsl:variable name="thread.delete.confirm.part2" select="'It has'" />
	<xsl:variable name="thread.delete.confirm.part3" select="'answers'" />
	<xsl:variable name="thread.delete.title" select="'Delete thread'" />
	<xsl:variable name="thread.update.title" select="'Edit thread'" />
	<xsl:variable name="thread.postedBy" select="'Submitted by'" />
	<xsl:variable name="thread.deletedUser" select="'Deleted user'" />
	<xsl:variable name="thread.time" select="'at'" />
	<xsl:variable name="thread.lastchanged" select="'Last changed'" />
	<xsl:variable name="thread.answers" select="'Number of answers'" />
	
	<xsl:variable name="addCommunityThread.header" select="'Add new thread in the forum'" />
	<xsl:variable name="addCommunityThread.subject" select="'Subject'" />
	<xsl:variable name="addCommunityThread.text" select="'Text'" />
	<xsl:variable name="addCommunityThread.submit" select="'Add new thread'" />
	
	<xsl:variable name="updateCommunityThread.header" select="'Edit thread'" />
	<xsl:variable name="updateCommunityThread.subject" select="'Subject'" />
	<xsl:variable name="updateCommunityThread.text" select="'Text'" />
	<xsl:variable name="updateCommunityThread.submit" select="'Save changes'" />
	
	<xsl:variable name="showCommunityPosts.delete.confirm.part1" select="'Are you sure you want to delete the thread?'" />
	<xsl:variable name="showCommunityPosts.delete.confirm.part2" select="'and all replies'" />
	<xsl:variable name="showCommunityPosts.delete.title" select="'Delete post'" />
	<xsl:variable name="showCommunityPosts.update.title" select="'Edit post'" />
	<xsl:variable name="showCommunityPosts.answer" select="'Reply to thread'" />
	
	<xsl:variable name="post.delete.confirm" select="'Are you really sure you want to delete the post?'" />
	<xsl:variable name="post.delete.title" select="'Delete post'" />
	<xsl:variable name="post.update.title" select="'Edit post'" />
		
	<xsl:variable name="updateCommunityPost.header" select="'Edit post'" />
	<xsl:variable name="updateCommunityPost.submit" select="'Edit post'" />
		
	<xsl:variable name="validationError.RequiredField" select="'You have to fill in the field'" />
	<xsl:variable name="validationError.InvalidFormat" select="'The field has an incorrect value'" />
	<xsl:variable name="validation.tooShort" select="'The content in the field is too short'" />
	<xsl:variable name="validation.tooLong" select="'The content in the field is too long'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'The field has an unknown error'" />
	<xsl:variable name="validationError.field.name" select="'name'" />
	<xsl:variable name="validationError.field.subject" select="'subject'" />
	<xsl:variable name="validationError.field.description" select="'description'" />
	<xsl:variable name="validationError.field.message" select="'message'" />
	<xsl:variable name="validationError.messageKey.InvalidContent" select="'The content has invalid characters'" />
	<xsl:variable name="validationError.messageKey.NoGroup" select="'You have to select atleast one group to publish too'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'An unknown error has occured'" />				

</xsl:stylesheet>
