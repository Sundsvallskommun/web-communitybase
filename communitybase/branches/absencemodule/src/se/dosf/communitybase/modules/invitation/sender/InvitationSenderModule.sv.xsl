<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="InvitationSenderModuleTemplates.xsl" />
	
	<xsl:variable name="newInvitationSubject">Inbjudan till F�r�ldram�tet!</xsl:variable>
	<xsl:variable name="updatedInvitationSubject">Uppdaterad inbjudan till F�r�ldram�tet!</xsl:variable>
	<xsl:variable name="resentInvitationSubject">Inbjudan till F�r�ldram�tet!</xsl:variable>
	<xsl:variable name="expiredInvitationSubject">Din inbjudan till F�r�ldram�tet har tagits bort!</xsl:variable>
	<xsl:variable name="recalledInvitationSubject">Din inbjudan till F�r�ldram�tet har tagits bort!</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="invitation.about.part1" select="'Vill du veta mer om tankarna bakom F�r�ldram�tet kan du l�sa om dem p�'" />
	<xsl:variable name="invitation.about.part2" select="'F�r�ldram�tet'" />
	<xsl:variable name="invitation.systemadmin" select="'Systemadministrat�r'" />
	<xsl:variable name="invitation.information" select="'F�r�ldram�tet �r en webbplats som g�r vardagskommunikationen mellan skola och hem l�ttare. F�r att aktivera ditt anv�ndarkonto p� F�r�ldram�tet klickar du p� den h�r l�nken'" />
	
	<xsl:variable name="invitation.recalled.header" select="'Din inbjudan till F�r�ldram�tet har tagits bort'" />
	<xsl:variable name="invitation.recalled.customlink" select="'L�nk till'" />
	
	<xsl:variable name="invitation.update.header" select="'Din inbjudan till F�r�ldram�tet har uppdaterats och du har f�tt f�ljande roll(er)'" />
	
	<xsl:variable name="invitation.new.header" select="'Du har blivit inbjuden till F�r�ldram�tet och f�tt f�ljande roll(er)'" />
	<xsl:variable name="invitation.new.expires" select="'Denna inbjudan �r giltig t.o.m'" />
	
	<xsl:variable name="invitation.school.resource" select="'Resursperson p�'" />
	
	<xsl:variable name="invitation.group.admin" select="'Administrat�r f�r'" />
	<xsl:variable name="invitation.group.publisher" select="'Pedagog f�r'" />
	<xsl:variable name="invitation.group.member" select="'F�r�lder till elev i'" />
	<xsl:variable name="invitation.group.on" select="'p�'" />			

</xsl:stylesheet>