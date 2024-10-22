<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="ForumModuleTemplates.xsl" />
	
	<xsl:variable name="addForumBreadcrumb">Lisää keskustelualue</xsl:variable>
	<xsl:variable name="updateForumBreadcrumb">Muokkaa keskustelualuetta </xsl:variable>
	<xsl:variable name="updateThreadBreadcrumb">Muokkaa ketjua </xsl:variable>
	<xsl:variable name="updateForumPostBreadcrumb">Muokkaa viestiä</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'kohteelle'" />
	<xsl:variable name="communitymodule.noforum" select="'Ei keskustelualuetta'" />
	<xsl:variable name="communitymodule.noforums" select="'Keskustelualueita ei ole tällä hetkellä'" />
	
	<xsl:variable name="community.delete.confirm" select="'Haluatko varmasti poistaa keskustelualueen ja kaiken sen sisällön'" />
	<xsl:variable name="community.delete.title" select="'Poista keskustelualue'" />
	<xsl:variable name="community.update.title" select="'Muokkaa keskustelualuetta'" />
	
	<xsl:variable name="forumref.addForum" select="'Lisää keskustelualue'" />
	
	<xsl:variable name="updateCommunity.header" select="'Muokkaa keskustelualuetta'" />
	<xsl:variable name="updateCommunity.name" select="'Nimi'" />
	<xsl:variable name="updateCommunity.description" select="'Kuvaus'" />
	<xsl:variable name="updateCommunity.submit" select="'Kuvaus'" />
	
	<xsl:variable name="addCommunity.header" select="'Lisää keskustelualue'" />
	<xsl:variable name="addCommunity.name" select="'Nimi'" />
	<xsl:variable name="addCommunity.description" select="'Kuvaus'" />
	<xsl:variable name="addCommunity.submit" select="'Lisää'" />
	
	<xsl:variable name="showCommunityThreads.noThreads" select="'Keskustelualueella ei ole vielä ketjuja'" />
	<xsl:variable name="showCommunityThreads.addThread" select="'Luo uusi ketju'" />
	
	<xsl:variable name="thread.delete.confirm.part1" select="'Haluatko varmasti poistaa ketjun'" />
	<xsl:variable name="thread.delete.confirm.part2" select="'Ketjulla on'" />
	<xsl:variable name="thread.delete.confirm.part3" select="'vastausta'" />
	<xsl:variable name="thread.delete.title" select="'Poista ketju'" />
	<xsl:variable name="thread.update.title" select="'Muokkaa ketjua'" />
	<xsl:variable name="thread.postedBy" select="'Lähettänyt'" />
	<xsl:variable name="thread.deletedUser" select="'Poistettu käyttäjä'" />
	<xsl:variable name="thread.time" select="'kello'" />
	<xsl:variable name="thread.lastchanged" select="'Viimeksi muokattu'" />
	<xsl:variable name="thread.answers" select="'Vastauksia'" />
	
	<xsl:variable name="addCommunityThread.header" select="'Lisää uusi ketju alueelle'" />
	<xsl:variable name="addCommunityThread.subject" select="'Otsikko'" />
	<xsl:variable name="addCommunityThread.text" select="'Sisältö'" />
	<xsl:variable name="addCommunityThread.submit" select="'Lisää ketju'" />
	
	<xsl:variable name="updateCommunityThread.header" select="'Muokkaa ketjua'" />
	<xsl:variable name="updateCommunityThread.subject" select="'Otsikko'" />
	<xsl:variable name="updateCommunityThread.text" select="'Sisältö'" />
	<xsl:variable name="updateCommunityThread.submit" select="'Tallenna muutokset'" />
	
	<xsl:variable name="showCommunityPosts.delete.confirm.part1" select="'Haluatko varmasti poistaa ketjun'" />
	<xsl:variable name="showCommunityPosts.delete.confirm.part2" select="'ja kaikki vastaukset'" />
	<xsl:variable name="showCommunityPosts.delete.title" select="'Poista viesti'" />
	<xsl:variable name="showCommunityPosts.update.title" select="'Muokkaa viestiä'" />
	<xsl:variable name="showCommunityPosts.answer" select="'Vastaa viestiin'" />
	
	<xsl:variable name="post.delete.confirm" select="'Haluatko varmasti poistaa viestin'" />
	<xsl:variable name="post.delete.title" select="'Poista viesti'" />
	<xsl:variable name="post.update.title" select="'Muokkaa viestiä'" />
		
	<xsl:variable name="updateCommunityPost.header" select="'Muokkaa viestiä'" />
	<xsl:variable name="updateCommunityPost.submit" select="'Muokkaa viestiä'" />
		
	<xsl:variable name="validationError.RequiredField" select="'Kenttä on pakollinen'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Kentän sisältö ei vastaa vaadittua muotoa'" />
	<xsl:variable name="validation.tooShort" select="'Kentän sisältö on liian lyhyt'" />
	<xsl:variable name="validation.tooLong" select="'Kentän sisältö on liian pitkä'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Tuntematon virhe kentässä'" />
	<xsl:variable name="validationError.field.name" select="'nimi'" />
	<xsl:variable name="validationError.field.subject" select="'otsikko'" />
	<xsl:variable name="validationError.field.description" select="'kuvaus'" />
	<xsl:variable name="validationError.field.message" select="'teksti'" />
	<xsl:variable name="validationError.messageKey.InvalidContent" select="'Kentän sisältö on epäkelpo'" />
	<xsl:variable name="validationError.messageKey.NoGroup" select="'Sinun täytyy valita vähintään yksi ryhmä johon julkaista'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Tuntematon virhe'" />	

</xsl:stylesheet>
