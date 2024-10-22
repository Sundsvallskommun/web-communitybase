<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	<xsl:include href="classpath://se/dosf/communitybase/utils/ckeditor/CKEditor.xsl" />

	<xsl:variable name="scripts">
		/utils/treeview/jquery.cookie.js
		/utils/treeview/jquery.jstree.custom.js
		/utils/treeview/communitybase.treeview.js
		/utils/ckeditor/ckeditor.js
		/utils/js/confirmDelete.js
		/utils/js/datepicker/datepicker.js
		/utils/js/communitybase.common.js
	</xsl:variable>	
	
	<xsl:variable name="links">
		/utils/js/datepicker/css/datepicker.css
		/utils/treeview/themes/communitybase/style.css
	</xsl:variable>
	
	<xsl:variable name="moduleImagePath"><xsl:value-of select="/document/requestinfo/contextpath" />/static/f/<xsl:value-of select="/document/module/sectionID" />/<xsl:value-of select="/document/module/moduleID" />/pics</xsl:variable>

	<xsl:template match="document">
	
		<div class="contentitem">
		
			<h1>
				<xsl:value-of select="/document/module/name" />
				
				<xsl:text>&#x20;</xsl:text> 
				
				<xsl:value-of select="$document.header" />: 
				
				<xsl:text>&#x20;</xsl:text> 
				
				<xsl:value-of select="group/name" />
			</h1>
			
			<xsl:apply-templates select="calendarmodule" />
			<xsl:apply-templates select="addPost" />
			<xsl:apply-templates select="updatePost" />
			<xsl:apply-templates select="showPost" />
			<xsl:apply-templates select="showReadReceipt" />
			<xsl:apply-templates select="GlobalResume" />
			
		</div>	
		
	</xsl:template>
	
	<xsl:template match="calendarmodule">
		
		<link rel="stylesheet" href="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/css/calendarcore.css" type="text/css" /> 
		<script src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/jMonthCalendar-1.2.2/jMonthCalendar.min.js" type="text/javascript" />
		
		<script type="text/javascript">
			<!-- set url used to request events -->
			getEventsUrl = "<xsl:value-of select="/document/requestinfo/currentURI" />/<xsl:value-of select="/document/module/alias" />/<xsl:value-of select="/document/group/groupID" />/getPosts";
			addPostUrl = "<xsl:value-of select="/document/requestinfo/currentURI" />/<xsl:value-of select="/document/module/alias" />/<xsl:value-of select="/document/group/groupID" />/addPost";
			startMonth = <xsl:value-of select="startMonth" />;
			isAdmin = <xsl:value-of select="../isGroupAdmin" />;
			imagePath = "<xsl:value-of select="$moduleImagePath" />";
			allSchools = "<xsl:value-of select="$i18n.AllSchools" />";
		</script>
		
		<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/{$calendar.script.language}" />
		<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/Calendar.js" />
		
		<xsl:if test="/document/isGroupAdmin = 'true'">
			<p class="info"><img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/information.png" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$calendarmodule.information" /><xsl:text>&#x20;"</xsl:text><xsl:value-of select="$calendarmodule.addlink.title" /><xsl:text>"</xsl:text></p>
		</xsl:if>
		
		<div id="jMonthCalendar" style="display: inline" />

		<xsl:if test="/document/isGroupAdmin = 'true'">
 			<div class="clearboth" />
			<div class="text-align-right floatright bigmargintop">
				<a id="addPostLink" href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addPost">
					<xsl:value-of select="$calendarmodule.addlink.title" />
				</a>	
			</div>
		</xsl:if>

	</xsl:template>
	
	<xsl:template match="addPost">
	
		<form name="addPost" method="post" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/addPost">
                       
            <script type="text/javascript" language="javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/jMaskedinput-1.2.2/jquery.maskedinput-1.2.2.min.js" />
            <script type="text/javascript" language="javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/jMaskedinput-1.2.2/dateTimeMaskedInput.js" />
                         
            <div class="content-box">
				<h1 class="header">
					<span><xsl:value-of select="$addPost.header" /></span>
				</h1>
				
				<div class="content">
				
					<xsl:apply-templates select="validationException/validationError"/>
					
					<p>
						<div>
							<div class="floatleft">
								<label for="startdate"><xsl:value-of select="$post.date" />:</label>
								<br />
								<input type="text" size="10" class="dateformat-Y-ds-m-ds-d show-weeks fill-grid no-animation disable-drag" id="startdate" name="startdate" >
									<xsl:attribute name="value">
										<xsl:choose>
											<xsl:when test="requestparameters/parameter[name = 'startdate']">
												<xsl:value-of select="requestparameters/parameter[name = 'startdate']/value" />
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="date"	/>			 
											</xsl:otherwise>
										</xsl:choose>
									</xsl:attribute>
								</input>
							</div>
							<div class="bigmarginleft floatleft">
								<label for="starttime"><xsl:value-of select="$post.starttime" />:</label>
								<br />
								<input type="text" id="starttime" name="starttime" size="5" maxlength="5" value="09:00" >
									<xsl:if test="requestparameters/parameter[name = 'starttime']">
										<xsl:attribute name="value"><xsl:value-of select="requestparameters/parameter[name = 'starttime']/value" /></xsl:attribute>
									</xsl:if>
								</input>
							</div>
							<div class="bigmarginleft floatleft">
								<label for="endtime"><xsl:value-of select="$post.endtime" />:</label>
								<br />
								<input type="text" id="endtime" name="endtime" size="5" maxlength="5" value="10:00" >
									<xsl:if test="requestparameters/parameter[name = 'endtime']">
										<xsl:attribute name="value"><xsl:value-of select="requestparameters/parameter[name = 'endtime']/value" /></xsl:attribute>
									</xsl:if>
								</input>
								(<xsl:value-of select="$post.time.description" />)
							</div>
						</div>
						<div class="clearboth" />
					</p>
					<p>
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="id" select="'sendReminder'" />
							<xsl:with-param name="name" select="'sendReminder'" />
							<xsl:with-param name="value" select="'true'" />
							<xsl:with-param name="element" select="calendarPost" />
							<xsl:with-param name="class" select="'vertical-align-middle'" />
						</xsl:call-template>
						<xsl:text>&#160;</xsl:text>
						<label for="sendReminder">
							<img src="{$moduleImagePath}/bell.png" class="vertical-align-middle" alt="" />
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$post.sendReminder" />
						</label>
					</p>
					<p>
						<label for="posttype"><xsl:value-of select="$post.showcalendarpost" />:</label>
						<br />
						<select id="posttype" name="posttype" width="150" style="width: 150px">
							<option value="GROUP">
								<xsl:if test="requestparameters/parameter[name = 'posttype']/value = 'GROUP'">
									<xsl:attribute name="selected" />
								</xsl:if>
								<xsl:value-of select="$post.showcalendarpost.group" />
							</option>
							<xsl:if test="../isSchoolAdmin = 'true'">
								<option value="SCHOOL">
									<xsl:if test="requestparameters/parameter[name = 'posttype']/value = 'SCHOOL'">
										<xsl:attribute name="selected" />
									</xsl:if>
									<xsl:value-of select="$post.showcalendarpost.school" />
								</option>
							</xsl:if>
							<xsl:if test="../isSysAdmin = 'true'">
								<option value="GLOBAL">
									<xsl:if test="requestparameters/parameter[name = 'posttype']/value = 'GLOBAL'">
										<xsl:attribute name="selected" />
									</xsl:if>
									<xsl:value-of select="$post.showcalendarpost.all" />
								</option>
							</xsl:if>
						</select>
					</p>
					<div class="" />
					<p>
						<label for="title"><xsl:value-of select="$post.name" />:</label>
						<br />
						<input type="text" id="title" name="title" size="72" value="{requestparameters/parameter[name='title']/value}"/>
					</p>
					<p>
						<label for="description"><xsl:value-of select="$post.description" />:</label>
						<br/>
						<textarea class="fckeditor" id="description" name="description" rows="14" cols="54">
							<xsl:call-template name="replace-string">
								<xsl:with-param name="text" select="requestparameters/parameter[name='description']/value"/>
								<xsl:with-param name="from" select="'&#13;'"/>
								<xsl:with-param name="to" select="''"/>
							</xsl:call-template>
						</textarea>
						
						<xsl:call-template name="initializeFckEditor" />
						
					</p>	
								
					<div class="floatright">
						<input type="button" onclick="window.location = '{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/showMonth/{year}/{month}'" value="{$post.back}" />
						<input type="submit" class="marginleft" value="{$addPost.submit}" />	
					</div>
					
				</div>
			
			</div>
			
		</form>
		
	</xsl:template>
	
	<xsl:template match="updatePost">
	
		<form name="updatePost" method="post" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/updatePost/{calendarPost/id}">
                      
            <script type="text/javascript" language="javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/jMaskedinput-1.2.2/jquery.maskedinput-1.2.2.min.js" />
            <script type="text/javascript" language="javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/jMaskedinput-1.2.2/dateTimeMaskedInput.js" />
                        
            <div class="content-box">
            	<h1 class="header">
					<span><xsl:value-of select="$updatePost.header" /><xsl:text>: </xsl:text><xsl:value-of select="calendarPost/title" /></span>
				</h1>
				
				<div class="content">
				
					<xsl:apply-templates select="validationException/validationError"/>
					
					<p>
						<div>
							<div class="floatleft">
								<label for="startdate"><xsl:value-of select="$post.date" />:</label>
								<br />
								<input type="text" size="10" class="dateformat-Y-ds-m-ds-d show-weeks fill-grid no-animation disable-drag" id="startdate" name="startdate" >
									<xsl:attribute name="value">
										<xsl:choose>
											<xsl:when test="requestparameters/parameter[name = 'startdate']">
												<xsl:value-of select="requestparameters/parameter[name = 'startdate']/value" />
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="substring-before(calendarPost/date,' ')"	/>			 
											</xsl:otherwise>
										</xsl:choose>
									</xsl:attribute>
								</input>
							</div>
							<div class="bigmarginleft floatleft">
								<label for="starttime"><xsl:value-of select="$post.starttime" />:</label>
								<br />
								<input type="text" id="starttime" name="starttime" size="5" maxlength="5">
									<xsl:attribute name="value">
										<xsl:choose>
											<xsl:when test="requestparameters/parameter[name = 'starttime']">
												<xsl:value-of select="requestparameters/parameter[name = 'starttime']/value" />
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="calendarPost/startTime" />			 
											</xsl:otherwise>
										</xsl:choose>
									</xsl:attribute>
								</input>
							</div>
							<div class="bigmarginleft floatleft">
								<label for="endtime"><xsl:value-of select="$post.endtime" />:</label>
								<br />
								<input type="text" id="endtime" name="endtime" size="5" maxlength="5" >
									<xsl:attribute name="value">
										<xsl:choose>
											<xsl:when test="requestparameters/parameter[name = 'endtime']">
												<xsl:value-of select="requestparameters/parameter[name = 'endtime']/value" />
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="calendarPost/endTime" />			 
											</xsl:otherwise>
										</xsl:choose>
									</xsl:attribute>
								</input>
								(<xsl:value-of select="$post.time.description" />)
							</div>
						</div>
						
						<div class="clearboth" />
						
					</p>
					<p>
						<xsl:call-template name="createCheckbox">
							<xsl:with-param name="id" select="'sendReminder'" />
							<xsl:with-param name="name" select="'sendReminder'" />
							<xsl:with-param name="value" select="'true'" />
							<xsl:with-param name="element" select="calendarPost" />
							<xsl:with-param name="class" select="'vertical-align-middle'" />
						</xsl:call-template>
						<xsl:text>&#160;</xsl:text>
						<label for="sendReminder">
							<img src="{$moduleImagePath}/bell.png" class="vertical-align-middle" alt="" />
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$post.sendReminder" />
						</label>
					</p>
					<p>
						<label for="publish"><xsl:value-of select="$post.publish" />:</label>				
						
						<div class="content-box-no-header">
					
							<xsl:call-template name="createTreeview">
								<xsl:with-param name="id" select="'calendar-communitybase-treeview'" />
								<xsl:with-param name="element" select="calendarPost" />
								<xsl:with-param name="schools" select="schools" />
								<xsl:with-param name="requestparameters" select="requestparameters" />
								<xsl:with-param name="globalAccess" select="../isSysAdmin" />
							</xsl:call-template>
						
							<xsl:if test="not(schools/school)">
								<p><xsl:value-of select="$post.noaccess" /></p>
							</xsl:if>
							
						</div>
					</p>
					<div class="clearboth" />
					<p>
						<label for="title"><xsl:value-of select="$post.name" />:</label>
						<br />
						<input type="text" id="title" name="title" size="72">
							<xsl:attribute name="value">
								<xsl:choose>
									<xsl:when test="requestparameters/parameter[name = 'title']">
										<xsl:value-of select="requestparameters/parameter[name = 'title']/value" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="calendarPost/title" />			 
									</xsl:otherwise>
								</xsl:choose>
							</xsl:attribute>
						</input>
					</p>
					<p>
						<label for="description"><xsl:value-of select="$post.description" />:</label>
						<br/>
						<textarea class="fckeditor" id="description" name="description" rows="14" cols="54">
							<xsl:call-template name="replace-string">
								<xsl:with-param name="text">
									<xsl:choose>
										<xsl:when test="requestparameters/parameter[name = 'description']">
											<xsl:value-of select="requestparameters/parameter[name='description']/value"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="calendarPost/details"/>		 
										</xsl:otherwise>
									</xsl:choose>
								</xsl:with-param>
								<xsl:with-param name="from" select="'&#13;'"/>
								<xsl:with-param name="to" select="''"/>
								
							</xsl:call-template>
						</textarea>
						
						<xsl:call-template name="initializeFckEditor" />
						
					</p>				
					<div class="floatright">
						<input type="button" onclick="window.location = '{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/showPost/{calendarPost/id}'" value="{$post.back}" />
						<input type="submit" class="marginleft" value="{$updatePost.submit}" />
					</div>
				
				</div>
				
			</div>
		</form>

	</xsl:template>
	
	<xsl:template match="showPost">
		
		<div class="content-box">
			
			<h1 class="header">
				<span><xsl:value-of select="substring(calendarPost/date,1,4)" /> - <xsl:value-of select="calendarPost/datetime/monthtext" /></span>
			</h1>
		
			<div class="content">
		
				<table class="full border marginbottom">
					
					<tr>
						<xsl:variable name="dayOfWeek" select="substring-before(substring(calendarPost/date,9,10),' ')" />
						<th colspan="5" align="left"><xsl:value-of select="calendarPost/datetime/daytext" /> 
						<xsl:text>&#x20;</xsl:text>
						<xsl:value-of select="$showPost.date.text" />
						<xsl:text>&#x20;</xsl:text>
						<xsl:value-of select="calendarPost/datetime/day" /> 
						(<xsl:value-of select="$showPost.date.week" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="calendarPost/datetime/week" />)</th>
						<th align="right">
							<xsl:if test="calendarPost/sendReminder">
								<img src="{$moduleImagePath}/bell.png" class="vertical-align-middle" title="{$post.reminderIsSent}" alt="" />
							</xsl:if>
						</th>
					</tr>
					<tr>
						<td width="90px"><xsl:value-of select="$showPost.time" />:</td>
						<td><xsl:value-of select="$showPost.title" />:</td>
						<td width="200px"><xsl:value-of select="$showPost.publishedTo" />:</td>
						<td colspan="3"/>
					</tr>
					<tr class="lightbackground">
						<td align="left">
							<xsl:value-of select="calendarPost/startTime" />
							<xsl:if test="calendarPost/endTime">
								<xsl:text> - </xsl:text><xsl:value-of select="calendarPost/endTime" />
							</xsl:if>
						</td>
						<td align="left">
							<xsl:value-of select="calendarPost/title" />
						</td>
						<td align="left">
						
							<xsl:choose>
								<xsl:when test="not(hasReceiptAccess) and not(hasUpdateAccess)">
									<xsl:attribute name="colspan">4</xsl:attribute>
								</xsl:when>
								<xsl:when test="not(hasReceiptAccess)">
									<xsl:attribute name="colspan">2</xsl:attribute>
								</xsl:when>
								<xsl:when test="not(hasUpdateAccess)">
									<xsl:attribute name="colspan">3</xsl:attribute>
								</xsl:when>
							</xsl:choose>
						
							<xsl:call-template name="createPublishingInformation">
								<xsl:with-param name="id" select="calendarPost/id" />
								<xsl:with-param name="element" select="calendarPost" />
								<xsl:with-param name="moduleImagePath" select="$moduleImagePath" />
							</xsl:call-template>
						</td>
						
						<xsl:if test="hasReceiptAccess">
							<td width="5px">
								<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/showReadReceipt/{calendarPost/id}" Title="{$showPost.showreadreceipt.title}">
				 					<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/chart.png" alt="" title="{$showPost.showreadreceipt.title}" />
				 				</a>
							</td>
						</xsl:if>
						
						<xsl:if test="hasUpdateAccess">
							<td width="5px">
								<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/updatePost/{calendarPost/id}" Title="{$showPost.changepost.title}">
									<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/edit.png" title="{$showPost.changepost.title}" />
								</a>	
							</td>
							<td width="5px">
								<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/deletePost/{calendarPost/id}" onclick="return confirmDelete('{$showPost.delete.confirm}?')">
									<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/delete.png" title="{$showPost.delete.title}" />
								</a>
							</td>
						</xsl:if>
					</tr>
					<tr />
					<tr>
						<td colspan="6">
							<xsl:value-of select="calendarPost/details" disable-output-escaping="yes" />
						</td>
					</tr>
					<tr class="lightbackground">
						<td colspan="6" align="left">
							<p class="addedBy">
								<xsl:value-of select="$showPost.postedby" />: 
								<xsl:choose>
									<xsl:when test="postedUser/user">
										<xsl:value-of select="postedUser/user/firstname" /><xsl:text> </xsl:text><xsl:value-of select="postedUser/user/lastname" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$showPost.deleteduser" />
									</xsl:otherwise>
								</xsl:choose>, <xsl:value-of select="calendarPost/fullDate" />
							</p>
						</td>
					</tr>
					
				</table>
				
				<div class="floatright">
					<input type="button" onclick="window.location = '{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/showMonth/{substring-before(calendarPost/date,'-')}/{calendarPost/datetime/month}'" value="{$post.back}" />
				</div>
			
			</div>
			
		</div>		
	
	</xsl:template>
	
	<xsl:template match="showReadReceipt">
		
		<div class="content-box">
			
			<h1 class="header">
				<span><xsl:value-of select="$showReadReceipt.header" /><xsl:text>&#x20;"</xsl:text><xsl:value-of select="calendarPost/title" />"</span>
			</h1>
			
			<div class="content">
			
				<p><xsl:value-of select="$showReadReceipt.summary.part1" /><xsl:text>&#x20;</xsl:text><b><xsl:value-of select="count(receipt) + HiddenReceipts" /></b><xsl:text>&#x20;</xsl:text><xsl:value-of select="$showReadReceipt.summary.part2" /><xsl:text>&#x20;</xsl:text><b><xsl:value-of select="HiddenReceipts" /></b><xsl:text>&#x20;</xsl:text><xsl:value-of select="$showReadReceipt.summary.part3" />.</p>
				
				<div class="floatleft full marginbottom">
					
					<div class="half floatleft marginleft">
						<b><xsl:value-of select="$showReadReceipt.name" />:</b>
					</div>
					<div class="floatleft thirty">
						<b><xsl:value-of select="$showReadReceipt.firstread" />:</b>
					</div>
					<div class="floatleft">
						<b><xsl:value-of select="$showReadReceipt.lastread" />:</b>
					</div>
					
				</div>
				<xsl:choose>
					<xsl:when test="receipt">
						<xsl:apply-templates select="receipt" />
					</xsl:when>
					<xsl:otherwise>
						<div class="floatleft full marginbottom border">
							<div class="floatleft full margintop marginbottom marginleft">
								<xsl:value-of select="$showReadReceipt.noreceipt" />
							</div>
						</div>
					</xsl:otherwise>
				</xsl:choose>
				<div class="floatright bigmargintop">
					<input type="button" onclick="window.location = '{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/showPost/{calendarPost/id}'" value="{$showReadReceipt.back}" />
				</div>
			
			</div>

		</div>

	</xsl:template>
	
	<xsl:template match="receipt">
		
		<div class="floatleft full marginbottom border">
		
			<div class="floatleft full margintop marginbottom marginleft">
			
				<div class="half floatleft">
					<xsl:value-of select="user/firstname" /><xsl:text> </xsl:text><xsl:value-of select="user/lastname" />
					<xsl:if test="user/communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation/Comment != ''">
						<xsl:text> (</xsl:text><xsl:value-of select="user/communityUserAttributes/groups/group[groupID = /document/group/groupID]/GroupRelation/Comment" /><xsl:text>)</xsl:text>
					</xsl:if>
				</div>
				<div class="floatleft thirty">
					<xsl:value-of select="firstReadTime" />
				</div>
				<div class="floatleft">
					<xsl:value-of select="lastReadTime" />
				</div>
				
			</div>
			
		</div>
		
	</xsl:template>
	
	<xsl:template match="validationError">
		<xsl:if test="fieldName and validationErrorType">
			<p class="error">
				<xsl:choose>
					<xsl:when test="validationErrorType='RequiredField'">
						<xsl:value-of select="$validationError.RequiredField" />
					</xsl:when>
					<xsl:when test="validationErrorType='InvalidFormat'">
						<xsl:value-of select="$validationError.InvalidFormat" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooLong'">
						<xsl:value-of select="$validationError.TooLong" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooShort'">
						<xsl:value-of select="$validationError.TooShort" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$validationError.unknownValidationErrorType" />
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:choose>
					<xsl:when test="fieldName = 'title'">
						<xsl:value-of select="$validationError.field.name" />!
					</xsl:when>
					<xsl:when test="fieldName = 'description'">
						<xsl:value-of select="$validationError.field.description" />!
					</xsl:when>
					<xsl:when test="fieldName = 'startdate'">
						<xsl:value-of select="$validationError.field.date" />!
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
					<xsl:when test="messageKey='InvalidContent'">
						<xsl:value-of select="$validationError.messageKey.InvalidContent" />!
					</xsl:when>
					<xsl:when test="messageKey='NoGroup'">
						<xsl:value-of select="$validationError.messageKey.NoGroup" /> 
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$validationError.unknownMessageKey" />!
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
	</xsl:template>
	
</xsl:stylesheet>