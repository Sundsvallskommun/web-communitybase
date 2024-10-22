<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="ForumModuleTemplates.xsl" />
	
	<xsl:variable name="addForumBreadcrumb">L�gg till forum</xsl:variable>
	<xsl:variable name="updateForumBreadcrumb">Redigera forum </xsl:variable>
	<xsl:variable name="updateThreadBreadcrumb">Redigera tr�den </xsl:variable>
	<xsl:variable name="updateForumPostBreadcrumb">Redigera forumpost</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'f�r'" />
	<xsl:variable name="communitymodule.noforum" select="'Inga forum f�r'" />
	<xsl:variable name="communitymodule.noforums" select="'Det finns f�r n�rvarande inga forum f�r'" />
	
	<xsl:variable name="community.delete.confirm" select="'�r du s�ker p� att du vill ta bort forumet och allt dess inneh�ll'" />
	<xsl:variable name="community.delete.title" select="'Ta bort forum'" />
	<xsl:variable name="community.update.title" select="'Redigera forum'" />
	
	<xsl:variable name="forumref.addForum" select="'L�gg till nytt forum'" />
	
	<xsl:variable name="updateCommunity.header" select="'Redigera forum'" />
	<xsl:variable name="updateCommunity.name" select="'Namn'" />
	<xsl:variable name="updateCommunity.description" select="'Beskrivning'" />
	<xsl:variable name="updateCommunity.submit" select="'Spara �ndringar'" />
	
	<xsl:variable name="addCommunity.header" select="'L�gg till forum'" />
	<xsl:variable name="addCommunity.name" select="'Namn'" />
	<xsl:variable name="addCommunity.description" select="'Beskrivning'" />
	<xsl:variable name="addCommunity.submit" select="'L�gg till'" />
	
	<xsl:variable name="showCommunityThreads.noThreads" select="'Det finns inga diskussioner i det h�r forumet'" />
	<xsl:variable name="showCommunityThreads.addThread" select="'L�gg till ny diskussion'" />
	
	<xsl:variable name="thread.delete.confirm.part1" select="'�r du s�ker p� att du vill ta bort diskussionen'" />
	<xsl:variable name="thread.delete.confirm.part2" select="'Den har'" />
	<xsl:variable name="thread.delete.confirm.part3" select="'svar'" />
	<xsl:variable name="thread.delete.title" select="'Ta bort diskussion'" />
	<xsl:variable name="thread.update.title" select="'Redigera diskussion'" />
	<xsl:variable name="thread.postedBy" select="'Inlagd av'" />
	<xsl:variable name="thread.deletedUser" select="'Borttagen anv�ndare'" />
	<xsl:variable name="thread.time" select="'klockan'" />
	<xsl:variable name="thread.lastchanged" select="'Senast �ndrad'" />
	<xsl:variable name="thread.answers" select="'Antal svar'" />
	
	<xsl:variable name="addCommunityThread.header" select="'L�gg till ny diskussion i forum'" />
	<xsl:variable name="addCommunityThread.subject" select="'Rubrik'" />
	<xsl:variable name="addCommunityThread.text" select="'Text'" />
	<xsl:variable name="addCommunityThread.submit" select="'L�gg till ny diskussion'" />
	
	<xsl:variable name="updateCommunityThread.header" select="'Redigera diskussion'" />
	<xsl:variable name="updateCommunityThread.subject" select="'Rubrik'" />
	<xsl:variable name="updateCommunityThread.text" select="'Text'" />
	<xsl:variable name="updateCommunityThread.submit" select="'Spara �ndringar'" />
	
	<xsl:variable name="showCommunityPosts.delete.confirm.part1" select="'�r du s�ker p� att du vill ta bort diskussionen'" />
	<xsl:variable name="showCommunityPosts.delete.confirm.part2" select="'och alla svar'" />
	<xsl:variable name="showCommunityPosts.delete.title" select="'Ta bort posten'" />
	<xsl:variable name="showCommunityPosts.update.title" select="'Redigera posten'" />
	<xsl:variable name="showCommunityPosts.answer" select="'Svara p� inl�gget'" />
	
	<xsl:variable name="post.delete.confirm" select="'�r du s�ker p� att du vill ta bort svaret'" />
	<xsl:variable name="post.delete.title" select="'Ta bort post'" />
	<xsl:variable name="post.update.title" select="'Redigera post'" />
		
	<xsl:variable name="updateCommunityPost.header" select="'�ndra inl�gget'" />
	<xsl:variable name="updateCommunityPost.submit" select="'Spara �ndringar'" />
		
	<xsl:variable name="validationError.RequiredField" select="'Du m�ste fylla i f�ltet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt v�rde p� f�ltet'" />
	<xsl:variable name="validation.tooShort" select="'F�r kort inneh�ll i f�ltet'" />
	<xsl:variable name="validation.tooLong" select="'F�r l�ngt inneh�ll i f�ltet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Ok�nt fel p� f�ltet'" />
	<xsl:variable name="validationError.field.name" select="'namn'" />
	<xsl:variable name="validationError.field.subject" select="'rubrik'" />
	<xsl:variable name="validationError.field.description" select="'beskrivning'" />
	<xsl:variable name="validationError.field.message" select="'text'" />
	<xsl:variable name="validationError.messageKey.InvalidContent" select="'Ogiltiga tecken i inneh�llet'" />
	<xsl:variable name="validationError.messageKey.NoGroup" select="'Du m�ste v�lja minst en grupp att publicera till'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett ok�nt fel har uppst�tt'" />				

</xsl:stylesheet>