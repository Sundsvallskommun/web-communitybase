<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="RegistrationModuleTemplates.xsl" />
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="registered.header" select="'Rekisteröinti on valmis'" />
	<xsl:variable name="registered.text" select="'Rekisteröintisi on nyt valmis, ja voit kirjautua sisään Vanhempainkokoukseen. Klikkaa linkkiä &quot;Kirjaudu&quot; oikeasta ylänurkasta'" />
	
	<xsl:variable name="register.header" select="'Tervetuloa Vanhempainkokoukseen'" />	
	<xsl:variable name="register.text" select="'Sinut on kutsuttu Vanhempainkokoukseen, verkkopalveluun jossa saat tietoa lapsesi koulusta kellon ympäri, vuoden jokaisena päivänä. Palvelun käyttämiseksi sinun tulee rekisteröityä alta löytyvällä lomakkeella'" />
	<xsl:variable name="register.rules.header" select="'Käyttösäännöt'" />

	<!-- Important to hold the format in this variable -->
	<xsl:variable name="register.rules.text">
Vanhempainkokouksen käyttösäännöt

Et saa häpäistä, herjata, häiritä, syrjiä, vainota tai uhata tai muuten loukata muiden oikeuksia.
Noudata yhteistä netikettiä. Keskusteluviestit jotka voidaan tulkita loukkaavaksi, poistetaan henkilökunnan toimesta.
Arkaluonteisia tietoja ei tule julkaista.
Esikoulun henkilöstön on toimittava yleisen järjen mukaisesti ja muistaa, että Vanhempainkokoukseen ei tule lisätä mitään kuvia tai tietoja jotka voidaan tulkita loukkaaviksi.

Tekijänoikeudet
Käyttäjä voi lähettää palveluun ainoastaan materiaalia, johon hänellä itsellään on tekijänoikeudet tai on saanut levitysluvan tekijänoikeuden haltijalta. Tekijänoikeuslaki on laaja, ja koskee lähes kaikkia tekstejä, lehtiartikkeleita, kuvia, piirroksia, tietokoneohjelmia ja musiikkia sekä monia muita asioita. Jos lähetät materiaalia johon sinulla ei ole kopiointilupaa, rikot tekijänoikeutta ja joudut kantamaan vastuun sekä seuraamukset. Käyttäjä saa käyttää Vanhempainkokouksessa esiintyvää materiaalia vain yksityisiin tarkoituksiin. Vanhempainkokouksessa esiintyvän materiaalin jäljennys, kopiointi, myynti tai muutoin luvaton käyttö on kielletty.

Sähköposti
Sähköpostiviestit joita järjestelmä lähettää, ovat henkilökohtaisia. Huomaa, että vastaanottaja on yksityinen ja viesti voidaan edelleen lähettää muille henkilöille.

Henkilökohtaiset tiedot
Henkilötietoja käsitellään noudattaen henkilötietolakia (SFS nro 1998:204). Tallennettuja tietoja käytetään hallinnollisiin tarkoituksiin. Sinulla on oikeus saada selville sinusta tallennetut tiedot ja pyytää niihin korjausta.
	

	</xsl:variable>
	
	<xsl:variable name="register.confirm" select="'Olen lukenut käyttösäännöt ja suostun noudattamaan niitä'" />
	<xsl:variable name="register.information" select="'Kirjautuaksesi Vanhempainkokoukseen, sinun tulee valita salasana. Sinun tulee myös antaa etu- ja sukunimesi, muut kentät ovat vapaaehtoisia'" />
	<xsl:variable name="register.email" select="'Käyttäjätunnus on sama kuin sähköpostiosoite'" />
	<xsl:variable name="register.firstname" select="'Etunimi'" />
	<xsl:variable name="register.lastname" select="'Sukunimi'" />
	<xsl:variable name="register.password" select="'Salasana (vähintään 6 merkkiä)'" />
	<xsl:variable name="register.confirmpassword" select="'Varmista salasana'" />
	<xsl:variable name="register.phonehome" select="'Puhelin (koti)'" />
	<xsl:variable name="register.phonemobile" select="'Puhelin (matka)'" />
	<xsl:variable name="register.phonework" select="'Puhelin (työ)'" />
	<xsl:variable name="register.submit" select="'Luo käyttäjätunnus'" />
	
	
	<xsl:variable name="validationError.RequiredField" select="'Kenttä on pakollinen'" />
	<xsl:variable name="validationError.InvalidFormat" select="'Kentän sisältö ei vastaa vaadittua muotoa'" />
	<xsl:variable name="validationError.TooLong" select="'Kentän sisältö on liian pitkä'" />
	<xsl:variable name="validationError.TooShort" select="'Kentän sisältö on liian lyhyt'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'Tuntematon virhe kentässä'" />
	
	<xsl:variable name="validationError.field.firstname" select="'etunimi'" />
	<xsl:variable name="validationError.field.lastname" select="'sukunimi'" />
	<xsl:variable name="validationError.field.password" select="'salasana'" />
	<xsl:variable name="validationError.field.commentText" select="'kommentti'" />
	<xsl:variable name="validationError.field.url" select="'polku'" />
	<xsl:variable name="validationError.field.text" select="'sisältö'" />
	
	<xsl:variable name="validationError.messageKey.license" select="'Et ole hyväksynyt käyttösääntöjä'" />
	<xsl:variable name="validationError.messageKey.passwordsDontMatch" select="'Salasanat eivät vastaa'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'Tuntematon virhe'" />				

</xsl:stylesheet>
