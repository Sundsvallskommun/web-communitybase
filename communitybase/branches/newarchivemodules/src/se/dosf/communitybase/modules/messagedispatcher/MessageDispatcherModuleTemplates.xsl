<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />
	
	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	<xsl:include href="classpath://se/dosf/communitybase/utils/ckeditor/CKEditor.xsl" />
	
	<xsl:variable name="scripts">
		/utils/ckeditor/ckeditor.js
		/js/jquery.tablesorter.min.js
		/js/init.tablesorter.js
		/utils/treeview/jquery.cookie.js
		/utils/treeview/jquery.jstree.custom.js
		/utils/treeview/communitybase.treeview.js
		/utils/js/communitybase.common.js
	</xsl:variable>	
	
	<xsl:variable name="links">
		/css/tablesorter/tablesorter.css
		/utils/treeview/themes/communitybase/style.css
	</xsl:variable>
	
	<xsl:template match="document">

		<div class="contentitem">

			<h1>
				<xsl:value-of select="/document/module/name" /><xsl:text>&#x20;</xsl:text>
			</h1>

			<xsl:apply-templates select="WriteMessage" />
			<xsl:apply-templates select="PreviewMessage" />
		</div>
	</xsl:template>

	<xsl:template match="WriteMessage">
	
		<script type="text/javascript">
			$(document).ready(function() {
				
				var sendEmail = $("#send-email");
				var sendEmailBox = $("#email-text-box");
				
				sendEmailBox.toggle(sendEmail.prop("checked"));
				
				sendEmail.click(function(){
					sendEmailBox.toggle($(this).prop("checked"));
				});
				
				
				var sendSMS = $("#send-sms");
				var sendSMSBox = $("#sms-text-box");

				sendSMSBox.toggle(sendSMS.prop("checked"));
				
				sendSMS.click(function(){
					sendSMSBox.toggle($(this).prop("checked"));
				});
				
			});
		</script>
		
		<xsl:value-of select="WelcomeMessage" disable-output-escaping="yes"/>
		
		<xsl:apply-templates select="validationException/validationError"/>
		
		<xsl:if test="UserCount or SMSUserCount">
			<div class="content-box bigmarginbottom">
			
				<h1 class="header">
					<span><xsl:value-of select="$i18n.SentMessage.Header" /></span>
				</h1>
			
				<div class="content">
					
					<div class="floatleft full marginbottom">
						
						<xsl:value-of select="$i18n.SentMessage.DescriptionPre"/>
						
						<xsl:if test="UserCount">
							<strong>
								<xsl:value-of select="UserCount"/>
							</strong>
							
							<xsl:value-of select="$i18n.SentMessage.DescriptionEmail" />
						</xsl:if>
						
						<xsl:if test="UserCount and SMSUserCount">
							<xsl:value-of select="$i18n.SentMessage.DescriptionAnd"/>
						</xsl:if>
						
						<xsl:if test="SMSUserCount">
							<strong>
								<xsl:value-of select="SMSUserCount"/>
							</strong>
							
							<xsl:value-of select="$i18n.SentMessage.DescriptionSMS"/>
						</xsl:if>
					
						<xsl:value-of select="$i18n.SentMessage.DescriptionPost"/>
						
					</div>
					
					<xsl:if test="SMSUserCount">
						<div class="floatleft full">
							<xsl:value-of select="$i18n.SentMessage.Description2SMS"/>
						</div>
					</xsl:if>
					
				</div>
					
			</div>
		</xsl:if>
		
		<div class="content-box">
		
			<h1 class="header">
				<span><xsl:value-of select="$i18n.WriteMessage.Header" /></span>
			</h1>
		
			<div class="content">
				<form method="POST" action="{/document/requestinfo/uri}">
				
					<div class="floatleft full bigmarginbottom">
						<strong>
							<xsl:value-of select="$i18n.WriteMessage.Format" />
						</strong>
					</div>
				
					<div class="floatleft full">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="id" select="'send-email'" />
							<xsl:with-param name="name" select="'send-email'" />
							<xsl:with-param name="class" select="'floatleft'" />
						</xsl:call-template>
						<label for="send-email" class="floatleft">
							<xsl:value-of select="$i18n.WriteMessage.SendEmail" />
						</label>
					</div>
					
					<div class="floatleft full bigmarginbottom">
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="id" select="'send-sms'" />
							<xsl:with-param name="name" select="'send-sms'" />
							<xsl:with-param name="class" select="'floatleft'" />
						</xsl:call-template>
						<label for="send-sms" class="floatleft">
							<xsl:value-of select="$i18n.WriteMessage.SendSMS" />
						</label>
					</div>
					
					<div class="floatleft full bigmarginbottom">
						<strong>
							<xsl:value-of select="$i18n.WriteMessage.FilterRoles" />
						</strong>
					</div>
					
					<div class="floatleft full">
						<div class="floatleft marginright">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="id" select="'role-member'" />
								<xsl:with-param name="name" select="'role-member'" />
								<xsl:with-param name="class" select="'floatleft'" />
							</xsl:call-template>
							<label for="role-member" class="floatleft">
								<xsl:value-of select="$i18n.User.Members" />
							</label>
						</div>
						
						<div class="floatleft full bigmarginbottom">
							<xsl:call-template name="createCheckbox">
								<xsl:with-param name="id" select="'role-publisher'" />
								<xsl:with-param name="name" select="'role-publisher'" />
								<xsl:with-param name="class" select="'floatleft'" />
							</xsl:call-template>
							<label for="role-publisher" class="floatleft">
								<xsl:value-of select="$i18n.User.Publishers" />
							</label>
						</div>
					</div>
					
					<label for="receivers">
						<strong>
							<xsl:value-of select="$i18n.WriteMessage.Recipients" />:
						</strong>
					</label>
					
					<div class="content-box-no-header">
						<xsl:call-template name="createTreeview">
							<xsl:with-param name="id" select="'messagedispatcher-communitybase-treeview'" />
							<xsl:with-param name="schools" select="schools" />
							<xsl:with-param name="requestparameters" select="requestparameters" />
							<xsl:with-param name="globalAccess" select="/document/isSysAdmin" />
						</xsl:call-template>
					
						<xsl:if test="not(schools/school)">
							<p><xsl:value-of select="$i18n.WriteMessage.noaccess" /></p>
						</xsl:if>
					</div>
					
					<div class="floatleft full" id="email-text-box">
						<fieldset class="border-radius-small lightbackground">
							<legend><xsl:value-of select="$i18n.WriteMessage.Email"/></legend>
						
							<div class="floatleft full bigmarginbottom">
								<label for="email-subject" class="floatleft full">
									<xsl:value-of select="$i18n.WriteMessage.EmailSubject" />
								</label>
								<div class="floatleft full">
									<xsl:call-template name="createTextField">
										<xsl:with-param name="id" select="'email-subject'" />
										<xsl:with-param name="name" select="'email-subject'" />
									</xsl:call-template>
								</div>
							</div>
							
							<label for="email-text" class="floatleft full">
								<xsl:value-of select="$i18n.WriteMessage.EmailText" />
							</label>
							
							<div class="floatleft full">
								
								<textarea class="fckeditor" name="email-text" rows="14" cols="54">
									<xsl:call-template name="replace-string">
										<xsl:with-param name="text" select="requestparameters/parameter[name='email-text']/value"/>
										<xsl:with-param name="from" select="'&#13;'"/>
										<xsl:with-param name="to" select="''"/>
									</xsl:call-template>
								</textarea>
								
								<xsl:call-template name="initializeFckEditor" />
							</div>
						</fieldset>
					</div>
					
					<div class="floatleft full" id="sms-text-box">
						<fieldset class="border-radius-small lightbackground">
							<legend><xsl:value-of select="$i18n.WriteMessage.SMS"/></legend>
						
							<div class="floatleft full">
								<xsl:call-template name="createTextArea">
									<xsl:with-param name="id" select="'sms-text'" />
									<xsl:with-param name="name" select="'sms-text'" />
									<xsl:with-param name="maxlength" select="'253'" />
									<xsl:with-param name="rows" select="5"/>
									<xsl:with-param name="class" select="'border-box'"/>
								</xsl:call-template>
							</div>
						</fieldset>
					</div>
					
					<div class="floatright">
						<input type="submit" value="{$i18n.Preview}" />
					</div>
					
				</form>
			</div>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="PreviewMessage">
	
		<xsl:apply-templates select="validationException/validationError"/>
		
		<div class="content-box">
		
			<h1 class="header">
				<span><xsl:value-of select="$i18n.PreviewMessage.Header" /></span>
			</h1>
		
			<div class="content">
				<form method="POST" action="{/document/requestinfo/contextpath}/{/document/module/alias}/{/document/groupID}/send">
				
					<div class="floatleft full bigmarginbottom">
						<xsl:value-of select="$i18n.PreviewMessage.Description"/>
					</div>
					
					<div class="floatleft full bigmarginbottom">
						<xsl:value-of select="$i18n.PreviewMessage.SendingTo"/>
						
						<xsl:if test="DispatchMessage/sendToMembers and not(DispatchMessage/sendToPublishers)">
							<xsl:value-of select="$i18n.PreviewMessage.SendingToMembers"/>
						</xsl:if>
						
						<xsl:if test="not(DispatchMessage/sendToMembers) and DispatchMessage/sendToPublishers">
							<xsl:value-of select="$i18n.PreviewMessage.SendingToPublishers"/>
						</xsl:if>
						
						<xsl:if test="DispatchMessage/sendToMembers and DispatchMessage/sendToPublishers">
							<xsl:value-of select="$i18n.PreviewMessage.SendingToMembersAndPublishers"/>
						</xsl:if>
					</div>
					
					<div class="floatleft full bigmarginbottom">
						<xsl:value-of select="$i18n.PreviewMessage.RecipientsPre"/>
						
						<strong>
							<xsl:value-of select="UserCount"/>
						</strong>
						
						<xsl:value-of select="$i18n.PreviewMessage.RecipientsPost"/>
						
						<div class="content-box-no-header">
							<xsl:call-template name="createTreeview">
								<xsl:with-param name="id" select="'messagedispatcher-communitybase-treeview'" />
								<xsl:with-param name="schools" select="DispatchMessage/schools" />
								<xsl:with-param name="requestparameters" select="requestparameters" />
								<xsl:with-param name="globalAccess" select="/document/isSysAdmin" />
								<xsl:with-param name="createCheckboxes" select="'false'" />
							</xsl:call-template>
						</div>
						
					</div>
					
					<xsl:if test="DispatchMessage/sendEmail">
					
						<div class="floatleft full">
							<fieldset class="border-radius-small lightbackground">
								<legend><xsl:value-of select="$i18n.WriteMessage.Email"/></legend>
								
								<div class="floatleft full bigmarginbottom">
									<strong>
										<xsl:value-of select="$i18n.WriteMessage.EmailSubject" />
									</strong>
									
									<xsl:text>:&#x20;</xsl:text>
									
									<xsl:value-of select="DispatchMessage/emailSubject"/>
								</div>

								<div class="floatleft full">
									<strong>
										<xsl:value-of select="$i18n.WriteMessage.EmailText" />
										<xsl:text>:</xsl:text>
									</strong>
								</div>
								
								<div class="floatleft full bigmarginbottom">
									<xsl:value-of select="DispatchMessage/emailText" disable-output-escaping="yes"/>
								</div>
							</fieldset>
						</div>

					</xsl:if>
					
					<xsl:if test="DispatchMessage/sendSMS">
					
						<div class="floatleft full">
							<fieldset class="border-radius-small lightbackground">
								<legend><xsl:value-of select="$i18n.WriteMessage.SMS" /></legend>
							
								<div class="floatleft full bigmarginbottom">
									<xsl:variable name="text">
										<xsl:call-template name="replace-string">
											<xsl:with-param name="text" select="DispatchMessage/SMSText" />
											<xsl:with-param name="from" select="'&#13;'" />
											<xsl:with-param name="to" select="'&lt;br&gt;'" />
										</xsl:call-template>
									</xsl:variable>
									
									<xsl:value-of select="$text" disable-output-escaping="yes"/>
								</div>
								
							</fieldset>
						</div>
					
					</xsl:if>
					
					<xsl:for-each select="requestparameters/parameter">
						<xsl:for-each select="value">
						
							<xsl:call-template name="createHiddenField">
								<xsl:with-param name="name" select="../name" />
								<xsl:with-param name="value" select="." />
							</xsl:call-template>
							
						</xsl:for-each>
					</xsl:for-each>
					
					<div class="floatright">
						<input type="submit" name="backToWrite" value="{$i18n.Change}" onclick="this.form.action='{/document/requestinfo/uri}'" class="marginright" />
						<input type="submit" value="{$i18n.Send}" />
					</div>
					
				</form>
			</div>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="user" mode="debug">
		<tr>
			<td>
				
				<a href="{/document/requestinfo/contextpath}{/document/section/fullAlias}/administration/useradmin/show/{userID}" title="user.showOrChangeUser.title {firstname} {lastname}">
					<img class="alignmiddle" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/user.png" />
					<xsl:text>&#x20;</xsl:text>
				</a>
				<a href="{/document/requestinfo/contextpath}{/document/section/fullAlias}/administration/useradmin/show/{userID}" title="user.showOrChangeUser.title {firstname} {lastname}">

					<xsl:value-of select="firstname" />

					<xsl:text>&#x20;</xsl:text>

					<xsl:value-of select="lastname" />

					<xsl:text>&#x20;</xsl:text>

					<xsl:if
						test="communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation/Comment != ''">
						(<xsl:value-of select="communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation/Comment" />)
					</xsl:if>
				</a>
				
			</td>		
		</tr>
	</xsl:template>
	
	<xsl:template match="validationError">
		<xsl:if test="fieldName and validationErrorType">
			<p class="error">
				<xsl:choose>
					<xsl:when test="validationErrorType='RequiredField'">
						<xsl:value-of select="$i18n.ValidationError.RequiredField" />
					</xsl:when>
					<xsl:when test="validationErrorType='InvalidFormat'">
						<xsl:value-of select="$i18n.ValidationError.InvalidFormat" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooLong'">
						<xsl:value-of select="$i18n.ValidationError.TooLong" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooShort'">
						<xsl:value-of select="$i18n.ValidationError.TooShort" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.ValidationError.UnknownValidationErrorType" />
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:choose>
					<xsl:when test="fieldName = 'MessageType'">
						<xsl:value-of select="$i18n.ValidationError.Field.MessageType" />! 
					</xsl:when>
					<xsl:when test="fieldName = 'emailSubject'">
						<xsl:value-of select="$i18n.ValidationError.Field.emailSubject" />!
					</xsl:when>
					<xsl:when test="fieldName = 'emailText'">
						<xsl:value-of select="$i18n.ValidationError.Field.emailText" />!
					</xsl:when>
					<xsl:when test="fieldName = 'smsText'">
						<xsl:value-of select="$i18n.ValidationError.Field.smsText" />!
					</xsl:when>
					<xsl:when test="fieldName = 'recipient'">
						<xsl:value-of select="$i18n.ValidationError.Field.Recipients" />!
					</xsl:when>
					<xsl:when test="fieldName = 'memberType'">
						<xsl:value-of select="$i18n.ValidationError.Field.MemberType" />!
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="fieldName"/>
					</xsl:otherwise>
				</xsl:choose>	
			</p>
		</xsl:if>
	
		<xsl:if test="messageKey">
			<p class="error">
				<xsl:choose>
					<xsl:when test="messageKey='NoUserChoosen'">
						<xsl:value-of select="$i18n.ValidationError.MessageKey.NoUserChoosen" />!
					</xsl:when>	
					<xsl:otherwise>
						<xsl:value-of select="$i18n.ValidationError.UnknownFault" />!
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
	</xsl:template>	

</xsl:stylesheet>