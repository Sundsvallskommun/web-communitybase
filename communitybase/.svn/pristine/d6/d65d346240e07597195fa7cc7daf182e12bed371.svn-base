<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="CombinedCalendarModuleTemplates.xsl" />
	<xsl:include href="Common.sv.xsl"/>
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.en.xsl" />
	
	<xsl:variable name="updatePostBreadCrumb">Edit calendar entry</xsl:variable>
	<xsl:variable name="readReceiptBreadCrumb">Read receipt</xsl:variable>
	<xsl:variable name="addPostBreadCrumb">Add calendar entry</xsl:variable>
	
	<!-- Naming template.mode.field.type -->
	
	<xsl:variable name="document.header" select="'for'" />
	<xsl:variable name="calendar.script.language" select="'CalendarLanguage.en.js'" />
	
	<xsl:variable name="calendarmodule.information" select="'To add a calendar entry, double-click on the desired day or click on the link'" />
	<xsl:variable name="calendarmodule.addlink.title" select="'Add calendar entry'" />
	
	<xsl:variable name="addPost.header" select="'Add calendar entry'" />
	<xsl:variable name="addPost.submit" select="'Add'" />
	<xsl:variable name="updatePost.header" select="'Edit calendar entry'" />
	<xsl:variable name="updatePost.submit" select="'Save changes'" />
	<xsl:variable name="post.date" select="'Date'" />
	<xsl:variable name="post.starttime" select="'Starting time'" />
	<xsl:variable name="post.endtime" select="'End time'" />
	<xsl:variable name="post.time.description" select="'Starting time and end time are not mandatory'" />
	<xsl:variable name="post.publish" select="'Publish calender entry to'" />
	<xsl:variable name="post.noaccess" select="'You have not got access to any preschool'" />
	<xsl:variable name="post.name" select="'Name'" />
	<xsl:variable name="post.description" select="'Description'" />
	<xsl:variable name="post.back" select="'Back'" />
	<xsl:variable name="post.sendReminder" select="'Send a reminder the day before'" />
	<xsl:variable name="post.reminderIsSent" select="'A reminder about the event is sent a day before'" />

	<xsl:variable name="schools.tree.title" select="'Preschools'" />

	<xsl:variable name="showPost.date.text" select="'the'" />
	<xsl:variable name="showPost.date.week" select="'week'" />
	<xsl:variable name="showPost.time" select="'Time'" />
	<xsl:variable name="showPost.title" select="'Title'" />
	<xsl:variable name="showPost.group" select="'Group'" />
	<xsl:variable name="showPost.all" select="'All preschools'" />
	<xsl:variable name="showPost.publishedTo" select="'Shown for'" />
	<xsl:variable name="showPost.showreadreceipt.title" select="'Show read receipt for calendar entry'" />
	<xsl:variable name="showPost.changepost.title" select="'Change calendar entry'" />
	<xsl:variable name="showPost.delete.confirm" select="'Are you sure you want to delete the calendar entry'" />
	<xsl:variable name="showPost.delete.title" select="'Delete calendar entry'" />
	<xsl:variable name="showPost.postedby" select="'Submitted by'" />
	<xsl:variable name="showPost.deleteduser" select="'Deleted user'" />
	
	<xsl:variable name="showReadReceipt.header" select="'Read receipt for the calendar entry'" />
	<xsl:variable name="showReadReceipt.summary.part1" select="'A total of'" />
	<xsl:variable name="showReadReceipt.summary.part2" select="'users have read the newsletter, of which'" />
	<xsl:variable name="showReadReceipt.summary.part3" select="'are hidden'" />
	<xsl:variable name="showReadReceipt.name" select="'Name'" />
	<xsl:variable name="showReadReceipt.firstread" select="'First read'" />
	<xsl:variable name="showReadReceipt.lastread" select="'Last read'" />
	<xsl:variable name="showReadReceipt.noreceipt" select="'No users have read the calendar entry'" />
	<xsl:variable name="showReadReceipt.back" select="'Back'" />
	
	<xsl:variable name="validationError.RequiredField" select="'You have to fill in the field'" />
	<xsl:variable name="validationError.InvalidFormat" select="'The value of the field is incorrect'" />
	<xsl:variable name="validationError.TooLong" select="'The fieldvalue is too big'" />
	<xsl:variable name="validationError.TooShort" select="'The fieldvalue is too short'" />
	<xsl:variable name="validationError.unknownValidationErrorType" select="'The field has an unknown error'" />
	<xsl:variable name="validationError.field.name" select="'name'" />
	<xsl:variable name="validationError.field.date" select="'date'" />
	<xsl:variable name="validationError.field.description" select="'description'" />
	<xsl:variable name="validationError.messageKey.InvalidContent" select="'Invalid characters in the content'" />
	<xsl:variable name="validationError.messageKey.NoGroup" select="'You have to select atleast one group to publish to'" />
	<xsl:variable name="validationError.unknownMessageKey" select="'An unknown error has occured'" />				

</xsl:stylesheet>
