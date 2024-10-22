<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="LinksModuleTemplates.xsl" />
	
	<xsl:variable name="newLinkText">Ny länk: </xsl:variable>
	<xsl:variable name="addGroupLinkBreadcrumb">Lägg till grupplänk</xsl:variable>
	<xsl:variable name="addSchoolLinkBreadcrumb">Lägg till skollänk</xsl:variable>
	<xsl:variable name="addGlobalLinkBreadcrumb">Lägg till globallänk</xsl:variable>
	<xsl:variable name="updateLinkBreadcrumb">Ändra länk</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'för'" />
	<xsl:variable name="linksmodule.nolinks1" select="'Inga länkar för'" />
	<xsl:variable name="linksmodule.nolinks2" select="'Det finns för närvarande inga länkar för'" />	
	<xsl:variable name="linksmodule.nomunicipalitylinks1" select="'Inga kommunala länkar'" />
	<xsl:variable name="linksmodule.nomunicipalitylinks2" select="'Det finns för närvarande inga kommunala länkar'" />
	
	<xsl:variable name="grouplinkRef.add" select="'Lägg till ny grupplänk'" />
	<xsl:variable name="schoollinkRef.add" select="'Lägg till ny skollänk'" />
	<xsl:variable name="globallinkRef.add" select="'Lägg till ny globallänk'" />
		
	<xsl:variable name="groupLinks.nolinks" select="'Det finns för närvarande inga grupplänkar'" />
	
	<xsl:variable name="schoolLinks.header" select="'Länkar för'" />
	<xsl:variable name="schoolLinks.nolinks" select="'Det finns för närvarande inga skollänkar'" />
	
	<xsl:variable name="globalLinks.header" select="'Kommunala länkar'" />
	<xsl:variable name="globalLinks.nolinks" select="'Det finns för närvarande inga kommunala länkar'" />
	
	<xsl:variable name="links.delete.confirm" select="'Är du säker på att du vill ta bort länken'" />
	<xsl:variable name="links.delete.title" select="'Ta bort länk'" />
	<xsl:variable name="links.update.title" select="'Ändra länk'" />
	
	<xsl:variable name="link.address" select="'Adress'" />
	<xsl:variable name="link.description" select="'Beskrivning'" />
	<xsl:variable name="link.postedBy" select="'Inlagd av'" />
	<xsl:variable name="link.deletedUser" select="'Borttagen användare'" />
	<xsl:variable name="link.submit" select="'Lägg till länk'" />
	
	<xsl:variable name="updateLink.header" select="'Ändra länk'" />
	<xsl:variable name="updateLink.submit" select="'Spara ändringar'" />
	
	<xsl:variable name="addGroupLink.header" select="'Lägg till länk för gruppen'" />
		
	<xsl:variable name="addSchoolLink.header" select="'Lägg till länk för skolan'" />
		
	<xsl:variable name="addGlobalLink.header" select="'Lägg till en Kommunallänk'" />
			
	<xsl:variable name="validationError.RequiredField" select="'Du måste fylla i fältet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Adressen måste börja med http://, https:// eller ftp://'" />
	<xsl:variable name="validationError.TooLong" select="'För långt innehåll i fältet'" />
	<xsl:variable name="validationError.TooShort" select="'För kort innehåll i fältet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Okänt fel på fältet'" />
	
	<xsl:variable name="validationError.field.description" select="'beskrivning'" />
	<xsl:variable name="validationError.field.url" select="'adress'" />

</xsl:stylesheet>