<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:include href="MessageDispatcherModuleTemplates.xsl"/>
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.sv.xsl"/>
	
	<xsl:variable name="schools.tree.title" select="'Förskolor'"/>
	<xsl:variable name="schools.tree.expandAll" select="'Fäll ut alla'"/>
	<xsl:variable name="schools.tree.collapseAll" select="'Stäng alla'"/>
	
	<xsl:variable name="i18n.User.Members" select="'Föräldrar'"/>
	<xsl:variable name="i18n.User.Publishers" select="'Pedagoger'"/>
	
	<xsl:variable name="i18n.ValidationError.RequiredField">Du måste ange: </xsl:variable>
	<xsl:variable name="i18n.ValidationError.InvalidFormat">Ogiltigt format: </xsl:variable>
	<xsl:variable name="i18n.ValidationError.TooShort">För kort: </xsl:variable>
	<xsl:variable name="i18n.ValidationError.TooLong">För långt: </xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnknownValidationErrorType">Okänt validerings fel</xsl:variable>
	<xsl:variable name="i18n.ValidationError.UnknownFault">Ett okänt fel har uppstått</xsl:variable>
	<xsl:variable name="i18n.ValidationError.MessageKey.NoUserChoosen">Du har inte valt någon användare att flytta/kopiera</xsl:variable>
	<xsl:variable name="i18n.ValidationError.Field.emailText">E-post text</xsl:variable>
	<xsl:variable name="i18n.ValidationError.Field.emailSubject">E-post ämne</xsl:variable>
	<xsl:variable name="i18n.ValidationError.Field.MessageType">Skicka via e-post och/eller SMS</xsl:variable>
	<xsl:variable name="i18n.ValidationError.Field.smsText">SMS text</xsl:variable>
	<xsl:variable name="i18n.ValidationError.Field.Recipients">Minst en mottagargrupp med medlemmar av den roll du valt</xsl:variable>
	<xsl:variable name="i18n.ValidationError.Field.MemberType">Roller att skicka till</xsl:variable>
	
	<xsl:variable name="i18n.WriteMessage.Header">Nytt meddelande</xsl:variable>
	<xsl:variable name="i18n.WriteMessage.SendEmail">Skicka via e-post</xsl:variable>
	<xsl:variable name="i18n.WriteMessage.SendSMS">Skicka via SMS</xsl:variable>
	<xsl:variable name="i18n.WriteMessage.FilterRoles">Välj roller att skicka till:</xsl:variable>
	<xsl:variable name="i18n.WriteMessage.Recipients">Mottagare</xsl:variable>
	<xsl:variable name="i18n.WriteMessage.Email">E-post</xsl:variable>
	<xsl:variable name="i18n.WriteMessage.EmailSubject">Ämne</xsl:variable>
	<xsl:variable name="i18n.WriteMessage.EmailText">Meddelande</xsl:variable>
	<xsl:variable name="i18n.WriteMessage.SMS">SMS</xsl:variable>
	<xsl:variable name="i18n.WriteMessage.noaccess">'Du har inte rättighet till någon förskola'</xsl:variable>
	<xsl:variable name="i18n.Preview">Förhandsgranska</xsl:variable>

	<xsl:variable name="i18n.PreviewMessage.Header">Förhandsgranskning</xsl:variable>
	<xsl:variable name="i18n.PreviewMessage.Description">Kontrollera att allt ser korrekt ut och klicka på skicka när du är klar.</xsl:variable>
	<xsl:variable name="i18n.PreviewMessage.RecipientsPre">Valda mottagar grupper (</xsl:variable>
	<xsl:variable name="i18n.PreviewMessage.RecipientsPost"> användare)</xsl:variable>
	<xsl:variable name="i18n.PreviewMessage.SendingTo">Skickar till </xsl:variable>
	<xsl:variable name="i18n.PreviewMessage.SendingToMembers">föräldrar.</xsl:variable>
	<xsl:variable name="i18n.PreviewMessage.SendingToPublishers">pedagoger.</xsl:variable>
	<xsl:variable name="i18n.PreviewMessage.SendingToMembersAndPublishers">föräldrar och pedagoger.</xsl:variable>
	<xsl:variable name="i18n.Send">Skicka</xsl:variable>
	<xsl:variable name="i18n.Change">Ändra</xsl:variable>
	
	<xsl:variable name="i18n.SentMessage.Header">Meddelande skickat</xsl:variable>
	<xsl:variable name="i18n.SentMessage.DescriptionPre">Ditt meddelande har skickats till </xsl:variable>
	<xsl:variable name="i18n.SentMessage.DescriptionPost">.</xsl:variable>
	<xsl:variable name="i18n.SentMessage.DescriptionAnd"> och </xsl:variable>
	<xsl:variable name="i18n.SentMessage.DescriptionEmail"> användare via epost</xsl:variable>
	<xsl:variable name="i18n.SentMessage.DescriptionSMS"> användare via SMS</xsl:variable>
	<xsl:variable name="i18n.SentMessage.Description2SMS">SMS skickas endast till användare som har angivit mobiltelefonnummer.</xsl:variable>
	<xsl:variable name="i18n.WriteMessage.Format">Välj meddelande format:</xsl:variable>
</xsl:stylesheet>
