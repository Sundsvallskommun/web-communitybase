<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="ForumModuleTemplates.xsl" />
	
	<xsl:variable name="addForumBreadcrumb">Lägg till forum</xsl:variable>
	<xsl:variable name="updateForumBreadcrumb">Redigera forum </xsl:variable>
	<xsl:variable name="updateThreadBreadcrumb">Redigera tråden </xsl:variable>
	<xsl:variable name="updateForumPostBreadcrumb">Redigera forumpost</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'för'" />
	<xsl:variable name="communitymodule.noforum" select="'Inga forum för'" />
	<xsl:variable name="communitymodule.noforums" select="'Det finns för närvarande inga forum för'" />
	
	<xsl:variable name="community.delete.confirm" select="'Är du säker på att du vill ta bort forumet och allt dess innehåll'" />
	<xsl:variable name="community.delete.title" select="'Ta bort forum'" />
	<xsl:variable name="community.update.title" select="'Redigera forum'" />
	
	<xsl:variable name="forumref.addForum" select="'Lägg till nytt forum'" />
	
	<xsl:variable name="updateCommunity.header" select="'Redigera forum'" />
	<xsl:variable name="updateCommunity.name" select="'Namn'" />
	<xsl:variable name="updateCommunity.description" select="'Beskrivning'" />
	<xsl:variable name="updateCommunity.submit" select="'Spara ändringar'" />
	
	<xsl:variable name="addCommunity.header" select="'Lägg till forum'" />
	<xsl:variable name="addCommunity.name" select="'Namn'" />
	<xsl:variable name="addCommunity.description" select="'Beskrivning'" />
	<xsl:variable name="addCommunity.submit" select="'Lägg till'" />
	
	<xsl:variable name="showCommunityThreads.noThreads" select="'Det finns inga diskussioner i det här forumet'" />
	<xsl:variable name="showCommunityThreads.addThread" select="'Lägg till ny diskussion'" />
	
	<xsl:variable name="thread.delete.confirm.part1" select="'Är du säker på att du vill ta bort diskussionen'" />
	<xsl:variable name="thread.delete.confirm.part2" select="'Den har'" />
	<xsl:variable name="thread.delete.confirm.part3" select="'svar'" />
	<xsl:variable name="thread.delete.title" select="'Ta bort diskussion'" />
	<xsl:variable name="thread.update.title" select="'Redigera diskussion'" />
	<xsl:variable name="thread.postedBy" select="'Inlagd av'" />
	<xsl:variable name="thread.deletedUser" select="'Borttagen användare'" />
	<xsl:variable name="thread.time" select="'klockan'" />
	<xsl:variable name="thread.lastchanged" select="'Senast ändrad'" />
	<xsl:variable name="thread.answers" select="'Antal svar'" />
	
	<xsl:variable name="addCommunityThread.header" select="'Lägg till ny diskussion i forum'" />
	<xsl:variable name="addCommunityThread.subject" select="'Rubrik'" />
	<xsl:variable name="addCommunityThread.text" select="'Text'" />
	<xsl:variable name="addCommunityThread.submit" select="'Lägg till ny diskussion'" />
	
	<xsl:variable name="updateCommunityThread.header" select="'Redigera diskussion'" />
	<xsl:variable name="updateCommunityThread.subject" select="'Rubrik'" />
	<xsl:variable name="updateCommunityThread.text" select="'Text'" />
	<xsl:variable name="updateCommunityThread.submit" select="'Spara ändringar'" />
	
	<xsl:variable name="showCommunityPosts.delete.confirm.part1" select="'Är du säker på att du vill ta bort diskussionen'" />
	<xsl:variable name="showCommunityPosts.delete.confirm.part2" select="'och alla svar'" />
	<xsl:variable name="showCommunityPosts.delete.title" select="'Ta bort posten'" />
	<xsl:variable name="showCommunityPosts.update.title" select="'Redigera posten'" />
	<xsl:variable name="showCommunityPosts.answer" select="'Svara på inlägget'" />
	
	<xsl:variable name="post.delete.confirm" select="'Är du säker på att du vill ta bort svaret'" />
	<xsl:variable name="post.delete.title" select="'Ta bort post'" />
	<xsl:variable name="post.update.title" select="'Redigera post'" />
		
	<xsl:variable name="updateCommunityPost.header" select="'Ändra inlägget'" />
	<xsl:variable name="updateCommunityPost.submit" select="'Spara ändringar'" />
		
	<xsl:variable name="validationError.RequiredField" select="'Du måste fylla i fältet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt värde på fältet'" />
	<xsl:variable name="validation.tooShort" select="'För kort innehåll i fältet'" />
	<xsl:variable name="validation.tooLong" select="'För långt innehåll i fältet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Okänt fel på fältet'" />
	<xsl:variable name="validationError.field.name" select="'namn'" />
	<xsl:variable name="validationError.field.subject" select="'rubrik'" />
	<xsl:variable name="validationError.field.description" select="'beskrivning'" />
	<xsl:variable name="validationError.field.message" select="'text'" />
	<xsl:variable name="validationError.messageKey.InvalidContent" select="'Ogiltiga tecken i innehållet'" />
	<xsl:variable name="validationError.messageKey.NoGroup" select="'Du måste välja minst en grupp att publicera till'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett okänt fel har uppstått'" />				

</xsl:stylesheet>