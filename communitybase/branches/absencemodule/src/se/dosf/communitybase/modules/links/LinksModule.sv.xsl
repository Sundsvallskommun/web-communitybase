<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="LinksModuleTemplates.xsl" />
	
	<xsl:variable name="newLinkText">Ny l�nk: </xsl:variable>
	<xsl:variable name="addGroupLinkBreadcrumb">L�gg till gruppl�nk</xsl:variable>
	<xsl:variable name="addSchoolLinkBreadcrumb">L�gg till skoll�nk</xsl:variable>
	<xsl:variable name="addGlobalLinkBreadcrumb">L�gg till globall�nk</xsl:variable>
	<xsl:variable name="updateLinkBreadcrumb">�ndra l�nk</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
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
			
	<xsl:variable name="validationError.RequiredField" select="'Du m�ste fylla i f�ltet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Adressen m�ste b�rja med http://, https:// eller ftp://'" />
	<xsl:variable name="validationError.TooLong" select="'F�r l�ngt inneh�ll i f�ltet'" />
	<xsl:variable name="validationError.TooShort" select="'F�r kort inneh�ll i f�ltet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Ok�nt fel p� f�ltet'" />
	
	<xsl:variable name="validationError.field.description" select="'beskrivning'" />
	<xsl:variable name="validationError.field.url" select="'adress'" />

</xsl:stylesheet>