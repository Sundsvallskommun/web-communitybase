<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="InvitationSenderModuleTemplates.xsl" />
	
	<xsl:variable name="newInvitationSubject">Kutsu Vanhempainkokoukseen!</xsl:variable>
	<xsl:variable name="updatedInvitationSubject">P�ivitetty kutsu Vanhempainkokoukseen!</xsl:variable>
	<xsl:variable name="resentInvitationSubject">Kutsu Vanhempainkokoukseen!</xsl:variable>
	<xsl:variable name="expiredInvitationSubject">Kutsusi Vanhempainkokoukseen on vanhentunut!</xsl:variable>
	<xsl:variable name="recalledInvitationSubject">Kutsusi Vanhempainkokoukseen on poistettu!</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="invitation.about.part1" select="'Jos haluat tiet�� lis�� Vanhempainkokouksen toiminnasta, voit lukea niist� osoitteessa'" />
	<xsl:variable name="invitation.about.part2" select="'Vanhempainkokous'" />
	<xsl:variable name="invitation.systemadmin" select="'J�rjestelm�n yll�pit�j�'" />
	<xsl:variable name="invitation.information" select="'Vanhempainkokous on sivusto joka helpottaa p�ivitt�ist� yhteydenpitoa kodin ja koulun v�lill�. Aktivoidaksesi k�ytt�j�tunnuksesi Vanhempainkokouksessa, klikkaa t�t� linkki�'" />
	
	<xsl:variable name="invitation.recalled.header" select="'Kutsusi Vanhempainkokoukseen on poistettu'" />
	<xsl:variable name="invitation.recalled.customlink" select="'Linkki'" />
	
	<xsl:variable name="invitation.update.header" select="'Kutsusi Vanhempainkokoukseen on p�ivitetty, ja sinulla on seuraava(t) rooli(t)'" />
	
	<xsl:variable name="invitation.new.header" select="'Sinut on kutsuttu Vanhempainkokoukseen, ja olet saanut seuraava(t) rooli(t)'" />
	<xsl:variable name="invitation.new.expires" select="'T�m� kutsu on voimassa m��r�ajan'" />
	
	<xsl:variable name="invitation.school.resource" select="'Yhteyshenkil�'" />
	
	<xsl:variable name="invitation.group.admin" select="'Yll�pit�j� ryhm�lle'" />
	<xsl:variable name="invitation.group.publisher" select="'Pedagogi ryhm�lle'" />
	<xsl:variable name="invitation.group.member" select="'Koululaisen vanhempi'" />
	<xsl:variable name="invitation.group.on" select="''" />

</xsl:stylesheet>
