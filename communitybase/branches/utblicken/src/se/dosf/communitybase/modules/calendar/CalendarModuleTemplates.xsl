<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:include href="classpath://se/dosf/communitybase/utils/fckeditor/FCKEditor.xsl" />

	<xsl:template match="document">
	
		<div class="contentitem">
		
			<div class="normal">	
				<h1>
					<xsl:value-of select="/document/module/name" />
					
					<xsl:text>&#x20;</xsl:text> 
					
					<xsl:value-of select="$document.header" />: 
					
					<xsl:text>&#x20;</xsl:text> 
					
					<xsl:value-of select="group/name" />
				</h1>
			</div>
			
			<xsl:apply-templates select="calendarmodule" />
			<xsl:apply-templates select="addPost" />
			<xsl:apply-templates select="updatePost" />
			<xsl:apply-templates select="showPost" />
			<xsl:apply-templates select="showReadReceipt" />
			
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
			isAdmin = <xsl:value-of select="../isAdmin" />;
		</script>
		
		<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/{$calendar.script.language}" />
		<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/Calendar.js" />
		
		<xsl:if test="/document/isAdmin = 'true'">
			<p class="info"><img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/information.png" /><xsl:text>&#160;</xsl:text><xsl:value-of select="$calendarmodule.information" /><xsl:text>&#x20;"</xsl:text><xsl:value-of select="$calendarmodule.addlink.title" /><xsl:text>"</xsl:text></p>
		</xsl:if>
		
		<div id="jMonthCalendar" style="display: inline" />

		<xsl:if test="/document/isAdmin = 'true'">
 			<div style="clear: both;" />
			<div class="addGalleries" style="margin-top: 10px">
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
			<script type="text/javascript" language="javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/datepicker/js/lang/se.js" />        
			<script type="text/javascript" language="javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/datepicker/js/datepicker.js" />
       		<link href="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/datepicker/css/datepicker.css" rel="stylesheet" type="text/css" />
                         
            <div class="divNormal">
				<h1 class="normalTableHeading" colspan="4" align="left">
					<xsl:value-of select="$addPost.header" />
				</h1>
				<xsl:apply-templates select="validationException/validationError"/>
				<p>
					<div>
						<div class="floatleft">
							<xsl:value-of select="$post.date" />:
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
							<xsl:value-of select="$post.starttime" />:
							<br />
							<input type="text" id="starttime" name="starttime" size="5" maxlength="5" value="09:00" >
								<xsl:if test="requestparameters/parameter[name = 'starttime']">
									<xsl:attribute name="value"><xsl:value-of select="requestparameters/parameter[name = 'starttime']/value" /></xsl:attribute>
								</xsl:if>
							</input>
						</div>
						<div class="bigmarginleft floatleft">
							<xsl:value-of select="$post.endtime" />:
							<br />
							<input type="text" id="endtime" name="endtime" size="5" maxlength="5" value="10:00" >
								<xsl:if test="requestparameters/parameter[name = 'endtime']">
									<xsl:attribute name="value"><xsl:value-of select="requestparameters/parameter[name = 'endtime']/value" /></xsl:attribute>
								</xsl:if>
							</input>
							(<xsl:value-of select="$post.time.description" />)
						</div>
					</div>
					<div style="clear: both;" />
				</p>
				<p>
					<xsl:value-of select="$post.showcalendarpost" />:
					<br />
					<select name="posttype" width="150" style="width: 150px">
						<option value="GROUP">
							<xsl:if test="requestparameters/parameter[name = 'posttype']/value = 'GROUP'">
								<xsl:attribute name="selected" />
							</xsl:if>
							<xsl:value-of select="$post.showcalendarpost.group" />
						</option>
						<option value="SCHOOL">
							<xsl:if test="requestparameters/parameter[name = 'posttype']/value = 'SCHOOL'">
								<xsl:attribute name="selected" />
							</xsl:if>
							<xsl:value-of select="$post.showcalendarpost.school" />
						</option>
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
				<div style="clear: both;" />
				<p>
					<xsl:value-of select="$post.name" />:
					<br />
					<input type="text" name="title" size="72" value="{requestparameters/parameter[name='title']/value}"/>
				</p>
				<p>
					<xsl:value-of select="$post.description" />:
					<br/>
					<textarea class="fckeditor" name="description" rows="14" cols="54">
						<xsl:call-template name="replace-string">
							<xsl:with-param name="text" select="requestparameters/parameter[name='description']/value"/>
							<xsl:with-param name="from" select="'&#13;'"/>
							<xsl:with-param name="to" select="''"/>
						</xsl:call-template>
					</textarea>
					
					<xsl:call-template name="initializeFckEditor" />
					
				</p>	
							
				<div class="floatright">
					<input type="submit" value="{$addPost.submit}" />
					
					<input type="button" onclick="window.location = '{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/showMonth/{year}/{month}'" value="{$post.back}" />		
				</div>
			</div>
		</form>
		
	</xsl:template>
	
	<xsl:template match="updatePost">
	
		<form name="updatePost" method="post" action="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/updatePost/{calendarPost/id}">
                      
            <script type="text/javascript" language="javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/jMaskedinput-1.2.2/jquery.maskedinput-1.2.2.min.js" />
            <script type="text/javascript" language="javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/jMaskedinput-1.2.2/dateTimeMaskedInput.js" />
			<script type="text/javascript" language="javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/datepicker/js/lang/se.js" />        
			<script type="text/javascript" language="javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/datepicker/js/datepicker.js" />
       		<link href="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/datepicker/css/datepicker.css" rel="stylesheet" type="text/css" />
                        
            <div class="divNormal">
            	<h1 class="normalTableHeading" colspan="4" align="left">
					<xsl:value-of select="$updatePost.header" /><xsl:text> "</xsl:text><xsl:value-of select="calendarPost/title" />"
				</h1>
				<xsl:apply-templates select="validationException/validationError"/>
				<p>
					<div>
						<div class="floatleft">
							<xsl:value-of select="$post.date" />:
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
							<xsl:value-of select="$post.starttime" />:
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
							<xsl:value-of select="$post.endtime" />:
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
					<div style="clear: both;" />
				</p>
				<p>
					<xsl:value-of select="$post.showcalendarpost" />:
					<br />
					<select name="posttype" width="150" style="width: 150px">
						<option value="GROUP">
							<xsl:choose>
								<xsl:when test="requestparameters/parameter[name = 'posttype']/value = 'GROUP'">
									<xsl:attribute name="selected" />
								</xsl:when>
								<xsl:when test="calendarPost/groups/group">
									<xsl:attribute name="selected" />
								</xsl:when>
							</xsl:choose>
							<xsl:value-of select="$post.showcalendarpost.group" />
						</option>
						<option value="SCHOOL">
							<xsl:choose>
								<xsl:when test="requestparameters/parameter[name = 'posttype']/value = 'SCHOOL'">
									<xsl:attribute name="selected" />
								</xsl:when>
								<xsl:when test="calendarPost/schools/school">
									<xsl:attribute name="selected" />
								</xsl:when>
							</xsl:choose>
							<xsl:value-of select="$post.showcalendarpost.school" />
						</option>
						<xsl:if test="../isSysAdmin = 'true'">
							<option value="GLOBAL">
								<xsl:choose>
									<xsl:when test="requestparameters/parameter[name = 'posttype']/value = 'GLOBAL'">
										<xsl:attribute name="selected" />
									</xsl:when>
									<xsl:when test="calendarPost/global">
										<xsl:attribute name="selected" />
									</xsl:when>
								</xsl:choose>
								<xsl:value-of select="$post.showcalendarpost.all" />
							</option>
						</xsl:if>
					</select>
				</p>
				<div style="clear: both;" />
				<p>
					<xsl:value-of select="$post.name" />:
					<br />
					<input type="text" name="title" size="72">
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
					<xsl:value-of select="$post.description" />:
					<br/>
					<textarea class="fckeditor" name="description" rows="14" cols="54">
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
					<input type="submit" value="{$updatePost.submit}" />
					
					<input type="button" onclick="window.location = '{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/showPost/{calendarPost/id}'" value="{$post.back}" />		
				</div>
			</div>
		</form>

	</xsl:template>
	
	<xsl:template match="showPost">
		
		<script type="text/javascript" language="Javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/confirmDelete.js" />
		
		<div class="noBorderDiv">
			<div class="divNormal">
				<h1 class="normalTableHeading" colspan="4" align="left">
					<xsl:value-of select="substring(calendarPost/date,1,4)" /> - <xsl:value-of select="calendarPost/datetime/monthtext" />
				</h1>
			
				<table class="calendarday">
					<tr>
						<xsl:variable name="dayOfWeek" select="substring-before(substring(calendarPost/date,9,10),' ')" />
						<th colspan="3" align="left"><xsl:value-of select="calendarPost/datetime/daytext" /> 
						<xsl:text>&#x20;</xsl:text>
						<xsl:value-of select="$showPost.date.text" />
						<xsl:text>&#x20;</xsl:text>
						<xsl:value-of select="calendarPost/datetime/day" /> 
						(<xsl:value-of select="$showPost.date.week" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="calendarPost/datetime/week" />)</th>
					</tr>
					<tr>
						<td width="90px"><xsl:value-of select="$showPost.time" />:</td>
						<td><xsl:value-of select="$showPost.title" />:</td>
						<td width="150px"><xsl:value-of select="$showPost.group" />:</td>
					</tr>
					<tr class="calendarRow">
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
								<xsl:when test="calendarPost/groups/group[name = /document/group/name]">
									<xsl:value-of select="calendarPost/groups/group[name = /document/group/name]/name" />
								</xsl:when>
								<xsl:when test="calendarPost/schools/school[name = /document/group/school/name]">
									<xsl:value-of select="calendarPost/schools/school[name = /document/group/school/name]/name" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$showPost.all" />
								</xsl:otherwise>
							</xsl:choose>
							
						</td>
						<xsl:if test="/document/isAdmin = 'true'">
							<td width="5px">
								<a href="{/document/requestinfo/currentURI}/{/document/module/alias}/{/document/group/groupID}/showReadReceipt/{calendarPost/id}" Title="{$showPost.showreadreceipt.title}">
				 					<img src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/pics/chart.png" alt="" title="{$showPost.showreadreceipt.title}" />
				 				</a>
							</td>
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
						<td colspan="5">
							<xsl:value-of select="calendarPost/details" disable-output-escaping="yes" />
						</td>
					</tr>
					<tr class="calendarRow">
							<td colspan="6" align="left">
								<p class="addedby">
									<i>
										<xsl:value-of select="$showPost.postedby" />: 
										<xsl:choose>
											<xsl:when test="postedUser/user">
												<xsl:value-of select="postedUser/user/firstname" /><xsl:text> </xsl:text><xsl:value-of select="postedUser/user/lastname" />
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="$showPost.deleteduser" />
											</xsl:otherwise>
										</xsl:choose>
										, <xsl:value-of select="calendarPost/fullposted" />
									</i>
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
		
		<div class="noBorderDiv">
			<div class="divNormal">
				<h1 class="normalTableHeading" colspan="4" align="left">
					<xsl:value-of select="$showReadReceipt.header" /><xsl:text>&#x20;"</xsl:text><xsl:value-of select="calendarPost/title" />"
				</h1>
				<p><xsl:value-of select="$showReadReceipt.summary.part1" /><xsl:text>&#x20;</xsl:text><b><xsl:value-of select="count(receipt)" /></b><xsl:text>&#x20;</xsl:text><xsl:value-of select="$showReadReceipt.summary.part2" />.</p>
				<div class="floatleft full bigmarginbottom">
					<div class="half floatleft marginleft">
						<b><xsl:value-of select="$showReadReceipt.name" />:</b>
					</div>
					<div style="width: 30%" class="floatleft marginleft">
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
				<div style="width: 30%" class="floatleft">
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
					<xsl:otherwise>
						<xsl:value-of select="$validationError.unknownMessageKey" />!
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="replace-string">

            <xsl:param name="text" />
            <xsl:param name="from" />
            <xsl:param name="to" />

            <xsl:choose>
                  <xsl:when test="contains($text, $from)">
                        <xsl:variable name="before" select="substring-before($text, $from)" />
                        <xsl:variable name="after" select="substring-after($text, $from)" />
                        <xsl:variable name="prefix" select="concat($before, $to)" />
                        
                        <xsl:value-of select="$before" />
                        <xsl:value-of select="$to" />

                        <xsl:call-template name="replace-string">
                              <xsl:with-param name="text" select="$after" />
                              <xsl:with-param name="from" select="$from" />
                              <xsl:with-param name="to" select="$to" />
                        </xsl:call-template>
                  </xsl:when>

                  <xsl:otherwise>
                        <xsl:value-of select="$text" />
                  </xsl:otherwise>
            </xsl:choose>
            
      </xsl:template>
	
</xsl:stylesheet>