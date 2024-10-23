<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="InvitationSenderModuleTemplates.xsl" />
	
	<xsl:variable name="newInvitationSubject">Inbjudan till Föräldramötet!</xsl:variable>
	<xsl:variable name="updatedInvitationSubject">Uppdaterad inbjudan till Föräldramötet!</xsl:variable>
	<xsl:variable name="resentInvitationSubject">Inbjudan till Föräldramötet!</xsl:variable>
	<xsl:variable name="expiredInvitationSubject">Din inbjudan till Föräldramötet har tagits bort!</xsl:variable>
	<xsl:variable name="recalledInvitationSubject">Din inbjudan till Föräldramötet har tagits bort!</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="invitation.about.part1" select="'Vill du veta mer om tankarna bakom Föräldramötet kan du läsa om dem på'" />
	<xsl:variable name="invitation.about.part2" select="'Föräldramötet'" />
	<xsl:variable name="invitation.systemadmin" select="'Systemadministratör'" />
	<xsl:variable name="invitation.information" select="'Föräldramötet är en webbplats som gör vardagskommunikationen mellan skola och hem lättare. För att aktivera ditt användarkonto på Föräldramötet klickar du på den här länken'" />
	
	<xsl:variable name="invitation.recalled.header" select="'Din inbjudan till Föräldramötet har tagits bort'" />
	<xsl:variable name="invitation.recalled.customlink" select="'Länk till'" />
	
	<xsl:variable name="invitation.update.header" select="'Din inbjudan till Föräldramötet har uppdaterats och du har fått följande roll(er)'" />
	
	<xsl:variable name="invitation.new.header" select="'Du har blivit inbjuden till Föräldramötet och fått följande roll(er)'" />
	<xsl:variable name="invitation.new.expires" select="'Denna inbjudan är giltig t.o.m'" />
	
	<xsl:variable name="invitation.school.resource" select="'Resursperson på'" />
	
	<xsl:variable name="invitation.group.admin" select="'Administratör för'" />
	<xsl:variable name="invitation.group.publisher" select="'Pedagog för'" />
	<xsl:variable name="invitation.group.member" select="'Förälder till elev i'" />
	<xsl:variable name="invitation.group.on" select="'på'" />			

</xsl:stylesheet>