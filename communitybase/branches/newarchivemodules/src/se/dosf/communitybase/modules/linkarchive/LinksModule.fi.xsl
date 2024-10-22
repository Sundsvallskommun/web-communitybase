<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="LinkArchiveModuleTemplates.xsl" />
	
	<xsl:variable name="newLinkText">Uusi linkki: </xsl:variable>
	<xsl:variable name="addGroupLinkBreadcrumb">Lisää linkki ryhmään</xsl:variable>
	<xsl:variable name="addSchoolLinkBreadcrumb">Lisää linkki kouluun</xsl:variable>
	<xsl:variable name="addGlobalLinkBreadcrumb">Lisää yleinen linkki</xsl:variable>
	<xsl:variable name="updateLinkBreadcrumb">Päivitä linkki</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'kohteelle'" />
	<xsl:variable name="linksmodule.nolinks1" select="'Ei linkkejä kohteelle'" />
	<xsl:variable name="linksmodule.nolinks2" select="'Linkkejä ei ole tällä hetkellä kohteelle'" />	
	<xsl:variable name="linksmodule.nomunicipalitylinks1" select="'Ei kunnallisia linkkejä'" />
	<xsl:variable name="linksmodule.nomunicipalitylinks2" select="'Tällä hetkellä ei ole kunnallisia linkkejä'" />
	
	<xsl:variable name="grouplinkRef.add" select="'Lisää uusi ryhmälinkki'" />
	<xsl:variable name="schoollinkRef.add" select="'Lisää uusi koululinkki'" />
	<xsl:variable name="globallinkRef.add" select="'Lisää uusi yleinen linkki'" />
		
	<xsl:variable name="groupLinks.nolinks" select="'Tällä hetkellä ei ole ryhmälinkkejä'" />
	
	<xsl:variable name="schoolLinks.header" select="'Linkit koululle'" />
	<xsl:variable name="schoolLinks.nolinks" select="'Tällä hetkellä ei ole koululinkkejä'" />
	
	<xsl:variable name="globalLinks.header" select="'Yleiset linkit'" />
	<xsl:variable name="globalLinks.nolinks" select="'Tällä hetkellä ei ole yleisiä linkkejä'" />
	
	<xsl:variable name="links.delete.confirm" select="'Haluatko varmasti poistaa linkin'" />
	<xsl:variable name="links.delete.title" select="'Poista linkki'" />
	<xsl:variable name="links.update.title" select="'Muokkaa linkkiä'" />
	
	<xsl:variable name="link.address" select="'Osoite'" />
	<xsl:variable name="link.description" select="'Kuvaus'" />
	<xsl:variable name="link.postedBy" select="'Lähettänyt'" />
	<xsl:variable name="link.deletedUser" select="'Poistettu käyttäjä'" />
	<xsl:variable name="link.submit" select="'Lisää linkki'" />
	
	<xsl:variable name="updateLink.header" select="'Päivitä linkkejä'" />
	<xsl:variable name="updateLink.submit" select="'Tallenna muutokset'" />
	
	<xsl:variable name="addGroupLink.header" select="'Lisää linkki ryhmään'" />
		
	<xsl:variable name="addSchoolLink.header" select="'Lisää linkki kouluun'" />
		
	<xsl:variable name="addGlobalLink.header" select="'Lisää yleinen linkki'" />
			
	<xsl:variable name="validationError.RequiredField" select="'Kenttä on pakollinen'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Osoitteen täytyy alkaa http://, https:// tai ftp://'" />
	<xsl:variable name="validationError.TooLong" select="'Kentän sisältö on liian pitkä'" />
	<xsl:variable name="validationError.TooShort" select="'Kentän sisältö on liian lyhyt'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Tuntematon virhe kentässä'" />
	
	<xsl:variable name="validationError.field.description" select="'kuvaus'" />
	<xsl:variable name="validationError.field.url" select="'osoite'" />

</xsl:stylesheet>
