<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="LinkArchiveModuleTemplates.xsl" />
	
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.sv.xsl" />
	
	<!-- Bread crumbs -->
	<xsl:variable name="newLinkText">Ny l�nk: </xsl:variable>
	<xsl:variable name="addLinkBreadcrumb">L�gg till l�nk</xsl:variable>
	<xsl:variable name="updateLinkBreadcrumb">�ndra l�nk</xsl:variable>
	
	<!--  -->
	<xsl:variable name="i18n.link.newNotification">Ny l�nk sedan du bes�kte l�nkarkivet senast</xsl:variable>
	<xsl:variable name="i18n.link.publishTo">G�r l�nken �tkomlig f�r</xsl:variable>
	<xsl:variable name="i18n.noaccess">Du har inte r�ttighet till n�gon f�rskola</xsl:variable>
	
	<!-- i18n translations -->
	<xsl:variable name="i18n.for">f�r</xsl:variable>
	<xsl:variable name="i18n.AllLinks">Alla l�nkar</xsl:variable>
	<xsl:variable name="i18n.NoLinks">Det finns f�r n�rvarande inga l�nkar h�r</xsl:variable>
	<xsl:variable name="i18n.DeleteLink">Ta bort l�nk</xsl:variable>
	<xsl:variable name="i18n.AddLink">L�gg till l�nk</xsl:variable>
	<xsl:variable name="i18n.EditLink">�ndra l�nk</xsl:variable>
	<xsl:variable name="i18n.AddedBy">Inlagd av</xsl:variable>
	<xsl:variable name="i18n.atTime">klockan</xsl:variable>
	<!-- 
	<xsl:variable name="i18n.RemovedUser">Borttagen anv�ndare</xsl:variable>
	 -->
	<xsl:variable name="i18n.Description">Beskrivning</xsl:variable>
	<xsl:variable name="i18n.Posted">Datum</xsl:variable>
	<xsl:variable name="i18n.URL">Adress</xsl:variable>
	<xsl:variable name="i18n.AlphaToOmega">A till �</xsl:variable>
	<xsl:variable name="i18n.OmegaToAlpha">� till A</xsl:variable>
	<xsl:variable name="i18n.NewestFirst">Senast f�rst</xsl:variable>
	<xsl:variable name="i18n.OldestFirst">�ldst f�rst</xsl:variable>
	<xsl:variable name="i18n.SaveChanges">Spara �ndringar</xsl:variable>
	<xsl:variable name="i18n.OrderBy">Sortera p�</xsl:variable>
	<xsl:variable name="i18n.ShownFor">Visas f�r</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	<!-- 
	<xsl:variable name="document.header" select="'f�r'" />
	<xsl:variable name="linksmodule.nolinks1" select="'Inga l�nkar f�r'" />
	<xsl:variable name="linksmodule.nolinks2" select="'Det finns f�r n�rvarande inga l�nkar f�r'" />	
	<xsl:variable name="linksmodule.nomunicipalitylinks1" select="'Inga kommunala l�nkar'" />
	<xsl:variable name="linksmodule.nomunicipalitylinks2" select="'Det finns f�r n�rvarande inga kommunala l�nkar'" />
	
	<xsl:variable name="grouplinkRef.add" select="'L�gg till ny gruppl�nk'" />
	<xsl:variable name="schoollinkRef.add" select="'L�gg till ny skoll�nk'" />
	<xsl:variable name="globallinkRef.add" select="'L�gg till ny globall�nk'" />
		
	<xsl:variable name="groupLinks.nolinks" select="'Det finns f�r n�rvarande inga gruppl�nkar'" />
	
	<xsl:variable name="schoolLinks.header" select="'L�nkar f�r'" />
	<xsl:variable name="schoolLinks.nolinks" select="'Det finns f�r n�rvarande inga skoll�nkar'" />
	
	<xsl:variable name="globalLinks.header" select="'Kommunala l�nkar'" />
	<xsl:variable name="globalLinks.nolinks" select="'Det finns f�r n�rvarande inga kommunala l�nkar'" />
	
	<xsl:variable name="links.delete.confirm" select="'�r du s�ker p� att du vill ta bort l�nken'" />
	<xsl:variable name="links.delete.title" select="'Ta bort l�nk'" />
	<xsl:variable name="links.update.title" select="'�ndra l�nk'" />
	
	<xsl:variable name="link.address" select="'Adress'" />
	<xsl:variable name="link.description" select="'Beskrivning'" />
	<xsl:variable name="link.postedBy" select="'Inlagd av'" />
	<xsl:variable name="link.deletedUser" select="'Borttagen anv�ndare'" />
	<xsl:variable name="link.submit" select="'L�gg till l�nk'" />
	
	<xsl:variable name="updateLink.header" select="'�ndra l�nk'" />
	<xsl:variable name="updateLink.submit" select="'Spara �ndringar'" />
	
	<xsl:variable name="addGroupLink.header" select="'L�gg till l�nk f�r gruppen'" />
		
	<xsl:variable name="addSchoolLink.header" select="'L�gg till l�nk f�r skolan'" />
		
	<xsl:variable name="addGlobalLink.header" select="'L�gg till en Kommunall�nk'" />
			 -->
	<xsl:variable name="validationError.RequiredField" select="'Du m�ste fylla i f�ltet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Adressen m�ste b�rja med http://, https:// eller ftp://'" />
	<xsl:variable name="validationError.TooLong" select="'F�r l�ngt inneh�ll i f�ltet'" />
	<xsl:variable name="validationError.TooShort" select="'F�r kort inneh�ll i f�ltet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Ok�nt fel p� f�ltet'" />
	
	<xsl:variable name="validationError.field.description" select="'beskrivning'" />
	<xsl:variable name="validationError.field.url" select="'adress'" />
	
	<xsl:variable name="validationError.messageKey.NoGroup" select="'Du m�ste v�lja minst en grupp att publicera till'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett ok�nt fel har uppst�tt'" />

</xsl:stylesheet>