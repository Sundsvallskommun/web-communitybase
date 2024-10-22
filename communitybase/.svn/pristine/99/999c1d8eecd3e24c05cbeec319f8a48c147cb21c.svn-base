<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="RegistrationModuleTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="registered.header" select="'Registreringen slutförd'" />
	<xsl:variable name="registered.text" select="'Din registrering är nu slutförd och du kan nu logga in i Föräldramötet genom att klicka på &quot;Logga in&quot; länken som du finner i övre högra hörnet på sidan'" />
	
	<xsl:variable name="register.header" select="'Välkommen till Föräldramötet'" />	
	<xsl:variable name="register.text" select="'Du har blivit inbjuden till Föräldramötet, en internetportal där du får tillgång till information om dina barns skolgång dygnet runt, året runt. För att kunna använda denna tjänst måste du först registrera dig, via formuläret nedan'" />	
	<xsl:variable name="register.rules.header" select="'Förhållningsregler'" />

	<!-- Important to hold the format in this variable -->
	<xsl:variable name="register.rules.text">
	
<!-- Förhållningsregler för Föräldramötet Du får inte förtala, smäda, trakassera, 
diskriminera, förfölja eller hota andra eller på något annat sätt kränka andras 
rättigheter. Iakttag vanlig nätetikett. Inlägg i diskussionerna som kan tolkas 
stötande tas bort av förskolepersonalen. Ingen känslig information får 
publiceras. Förskolepersonalen skall agera med varsamhet och tänka på att 
inga bilder eller information som kan vara stötande läggs in på Föräldramötet.
	
Upphovsrätt Du får endast skicka in material till systemet, som Du själv äger 
upphovsrätten till, eller har tillstånd att distribuera vidare. Sådant 
tillstånd kan bara ges av upphovsrättsinnehavaren. Upphovsrättslagen är 
mycket omfattande och gäller i princip alla typer av texter, 
tidningsartiklar, bilder, teckningar, dataprogram och musik; samt 
åtskilligt annat. Om Du skulle skicka in material som annan har 
upphovsrätten till riskerar Du att bryta mot upphovsrättslagen 
och drabbas av både ersättningsskyldighet och straff. Du får endast använda 
material från Föräldramötet för privat bruk Du får inte mångfaldiga, kopiera, 
sälja eller på annat sätt utnyttja materialet från Föräldramötet utan tillstånd 
från den som innehar upphovsrätten till materialet.
	
Elektronisk post som endast sänds till namngivna abonnenter i systemet, 
är privat. Notera dock att mottagaren av privat post kan komma att sända 
posten vidare till andra personer eller till exempel använda posten som 
inlägg i konferenser.
	
Personuppgifter Personuppgifter som registrerar behandlas i enlighet med 
Personuppgiftslagen (SFS nr 1998:204). Informationen lagras hjälp av IT för 
administrativa ändamål. Du har rätt att begära utdrag och rättelser.
Personuppgiftsansvarig är Barn- och utbildningsnämnden, 
Sundsvalls kommun, 851 85 Sundsvall -->

	</xsl:variable>
	
	<xsl:variable name="register.confirm" select="'Jag har tagit del av förhållningsreglerna och godkänner dessa'" />
	<xsl:variable name="register.information" select="'För att kunna logga in i föräldramötet måste du välja ett lösenord. Du måste även fylla i för- och efternamn, övriga fält är valfria'" />
	<xsl:variable name="register.email" select="'Ditt användarnamn är samma som din E-postadress'" />
	<xsl:variable name="register.firstname" select="'Förnamn'" />
	<xsl:variable name="register.lastname" select="'Efternamn'" />
	<xsl:variable name="register.password" select="'Lösenord (minst 6 tecken)'" />
	<xsl:variable name="register.confirmpassword" select="'Bekräfta lösenord'" />
	<xsl:variable name="register.phonehome" select="'Telefon hem'" />
	<xsl:variable name="register.phonemobile" select="'Telefon mobil'" />
	<xsl:variable name="register.phonework" select="'Telefon arbete'" />
	<xsl:variable name="register.submit" select="'Skapa konto'" />
	
	
	<xsl:variable name="validationError.RequiredField" select="'Du måste fylla i fältet'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Felaktigt format på fältet'" />
	<xsl:variable name="validationError.TooLong" select="'För långt innehåll i fältet'" />
	<xsl:variable name="validationError.TooShort" select="'För kort innehåll i fältet'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Okänt fel på fältet'" />
	
	<xsl:variable name="validationError.field.firstname" select="'förnamn'" />
	<xsl:variable name="validationError.field.lastname" select="'efternamn'" />
	<xsl:variable name="validationError.field.password" select="'lösenord'" />
	<xsl:variable name="validationError.field.commentText" select="'kommentar'" />
	<xsl:variable name="validationError.field.url" select="'sökväg'" />
	<xsl:variable name="validationError.field.text" select="'innehåll'" />
	
	<xsl:variable name="validationError.messageKey.license" select="'Du har inte godkänt förhållningsreglerna'" />
	<xsl:variable name="validationError.messageKey.passwordsDontMatch" select="'Lösenorden överenstämmer inte'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Ett okänt fel har uppstått'" />				

</xsl:stylesheet>