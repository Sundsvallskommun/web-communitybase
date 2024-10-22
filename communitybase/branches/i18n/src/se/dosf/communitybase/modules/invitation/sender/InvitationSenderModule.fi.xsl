<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="InvitationSenderModuleTemplates.xsl" />
	
	<xsl:variable name="newInvitationSubject">Kutsu Vanhempainkokoukseen!</xsl:variable>
	<xsl:variable name="updatedInvitationSubject">Päivitetty kutsu Vanhempainkokoukseen!</xsl:variable>
	<xsl:variable name="resentInvitationSubject">Kutsu Vanhempainkokoukseen!</xsl:variable>
	<xsl:variable name="expiredInvitationSubject">Kutsusi Vanhempainkokoukseen on vanhentunut!</xsl:variable>
	<xsl:variable name="recalledInvitationSubject">Kutsusi Vanhempainkokoukseen on poistettu!</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="invitation.about.part1" select="'Jos haluat tietää lisää Vanhempainkokouksen toiminnasta, voit lukea niistä osoitteessa'" />
	<xsl:variable name="invitation.about.part2" select="'Vanhempainkokous'" />
	<xsl:variable name="invitation.systemadmin" select="'Järjestelmän ylläpitäjä'" />
	<xsl:variable name="invitation.information" select="'Vanhempainkokous on sivusto joka helpottaa päivittäistä yhteydenpitoa kodin ja koulun välillä. Aktivoidaksesi käyttäjätunnuksesi Vanhempainkokouksessa, klikkaa tätä linkkiä'" />
	
	<xsl:variable name="invitation.recalled.header" select="'Kutsusi Vanhempainkokoukseen on poistettu'" />
	<xsl:variable name="invitation.recalled.customlink" select="'Linkki'" />
	
	<xsl:variable name="invitation.update.header" select="'Kutsusi Vanhempainkokoukseen on päivitetty, ja sinulla on seuraava(t) rooli(t)'" />
	
	<xsl:variable name="invitation.new.header" select="'Sinut on kutsuttu Vanhempainkokoukseen, ja olet saanut seuraava(t) rooli(t)'" />
	<xsl:variable name="invitation.new.expires" select="'Tämä kutsu on voimassa määräajan'" />
	
	<xsl:variable name="invitation.school.resource" select="'Yhteyshenkilö'" />
	
	<xsl:variable name="invitation.group.admin" select="'Ylläpitäjä ryhmälle'" />
	<xsl:variable name="invitation.group.publisher" select="'Pedagogi ryhmälle'" />
	<xsl:variable name="invitation.group.member" select="'Koululaisen vanhempi'" />
	<xsl:variable name="invitation.group.on" select="''" />

</xsl:stylesheet>
