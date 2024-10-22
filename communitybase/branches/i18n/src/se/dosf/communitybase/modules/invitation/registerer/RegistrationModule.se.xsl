<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="RegistrationModuleTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="registered.header" select="'Registreringen slutf�rd'" />
	<xsl:variable name="registered.text" select="'Din registrering �r nu slutf�rd och du kan nu logga in i F�r�ldram�tet genom att klicka p� &quot;Logga in&quot; l�nken som du finner i �vre h�gra h�rnet p� sidan'" />
	
	<xsl:variable name="register.header" select="'V�lkommen till F�r�ldram�tet'" />	
	<xsl:variable name="register.text" select="'Du har blivit inbjuden till F�r�ldram�tet, en internetportal d�r du f�r tillg�ng till information om dina barns skolg�ng dygnet runt, �ret runt. F�r att kunna anv�nda denna tj�nst m�ste du f�rst registrera dig, via formul�ret nedan'" />	
	<xsl:variable name="register.rules.header" select="'F�rh�llningsregler'" />

	<!-- Important to hold the format in this variable -->
	<xsl:variable name="register.rules.text">
	
<!-- F�rh�llningsregler f�r F�r�ldram�tet Du f�r inte f�rtala, sm�da, trakassera, 
diskriminera, f�rf�lja eller hota andra eller p� n�got annat s�tt kr�nka andras 
r�ttigheter. Iakttag vanlig n�tetikett. Inl�gg i diskussionerna som kan tolkas 
st�tande tas bort av f�rskolepersonalen. Ingen k�nslig information f�r 
publiceras. F�rskolepersonalen skall agera med varsamhet och t�nka p� att 
inga bilder eller information som kan vara st�tande l�ggs in p� F�r�ldram�tet.
	
Upphovsr�tt Du f�r endast skicka in material till systemet, som Du sj�lv �ger 
upphovsr�tten till, eller har tillst�nd att distribuera vidare. S�dant 
tillst�nd kan bara ges av upphovsr�ttsinnehavaren. Upphovsr�ttslagen �r 
mycket omfattande och g�ller i princip alla typer av texter, 
tidningsartiklar, bilder, teckningar, dataprogram och musik; samt 
�tskilligt annat. Om Du skulle skicka in material som annan har 
upphovsr�tten till riskerar Du att bryta mot upphovsr�ttslagen 
och drabbas av b�de ers�ttningsskyldighet och straff. Du f�r endast anv�nda 
material fr�n F�r�ldram�tet f�r privat bruk Du f�r inte m�ngfaldiga, kopiera, 
s�lja eller p� annat s�tt utnyttja materialet fr�n F�r�ldram�tet utan tillst�nd 
fr�n den som innehar upphovsr�tten till materialet.
	
Elektronisk post som endast s�nds till namngivna abonnenter i systemet, 
�r privat. Notera dock att mottagaren av privat post kan komma att s�nda 
posten vidare till andra personer eller till exempel anv�nda posten som 
inl�gg i konferenser.
	
Personuppgifter Personuppgifter som registrerar behandlas i enlighet med 
Personuppgiftslagen (SFS nr 1998:204). Informationen lagras hj�lp av IT f�r 
administrativa �ndam�l. Du har r�tt att beg�ra utdrag och r�ttelser.
Personuppgiftsansvarig �r Barn- och utbildningsn�mnden, 
Sundsvalls kommun, 851 85 Sundsvall -->

	</xsl:variable>
	
	<xsl:variable name="register.confirm" select="'Jag har tagit del av f�rh�llningsreglerna och godk�nner dessa'" />
	<xsl:variable name="register.information" select="'F�r att kunna logga in i f�r�ldram�tet m�ste du v�lja ett l�senord. Du m�ste �ven fylla i f�r- och efternamn, �vriga f�lt �r valfria'" />
	<xsl:variable name="register.email" select="'Ditt anv�ndarnamn �r samma som din E-postadress'" />
	<xsl:variable name="register.firstname" select="'F�rnamn'" />
	<xsl:variable name="register.lastname" select="'Efternamn'" />
	<xsl:variable name="register.password" select="'L�senord (minst 6 tecken)'" />
	<xsl:variable name="register.confirmpassword" select="'Bekr�fta l�senord'" />
	<xsl:variable name="register.phonehome" select="'Telefon hem'" />
	<xsl:variable name="register.phonemobile" select="'Telefon mobil'" />
	<xsl:variable name="register.phonework" select="'Telefon arbete'" />
	<xsl:variable name="register.submit" select="'Skapa konto'" />
	
	
	<xsl:variable name="validationError.RequiredField" select="'Du m�ste fylla i f�ltet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt format p� f�ltet'" />
	<xsl:variable name="validationError.TooLong" select="'F�r l�ngt inneh�ll i f�ltet'" />
	<xsl:variable name="validationError.TooShort" select="'F�r kort inneh�ll i f�ltet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Ok�nt fel p� f�ltet'" />
	
	<xsl:variable name="validationError.field.firstname" select="'f�rnamn'" />
	<xsl:variable name="validationError.field.lastname" select="'efternamn'" />
	<xsl:variable name="validationError.field.password" select="'l�senord'" />
	<xsl:variable name="validationError.field.commentText" select="'kommentar'" />
	<xsl:variable name="validationError.field.url" select="'s�kv�g'" />
	<xsl:variable name="validationError.field.text" select="'inneh�ll'" />
	
	<xsl:variable name="validationError.messageKey.license" select="'Du har inte godk�nt f�rh�llningsreglerna'" />
	<xsl:variable name="validationError.messageKey.passwordsDontMatch" select="'L�senorden �verenst�mmer inte'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett ok�nt fel har uppst�tt'" />				

</xsl:stylesheet>