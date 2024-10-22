<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output encoding="ISO-8859-1" method="html" version="4.0"/>

	<xsl:template match="Absence" mode="show">
		
		<xsl:param name="mode" select="'Public'" />
		
		<div class="floatleft full bigmarginbottom">
			<div class="floatleft twenty"><b><xsl:value-of select="$i18n.Children" />:</b></div>
			<div class="floatleft eighty">
				<xsl:value-of select="name" />
					<div class="floatright">
						<xsl:call-template name="createUpdateDeleteLinks">
							<xsl:with-param name="mode" select="$mode" />
						</xsl:call-template>
					</div>
			</div>
		</div>
		<div class="floatleft full bigmarginbottom">
			<div class="floatleft twenty"><b><xsl:value-of select="$i18n.Comment" />:</b></div>
			<div class="floatleft eighty">
				<xsl:value-of select="comment" />
			</div>
		</div>
		<div class="floatleft full bigmarginbottom">
			<div class="floatleft twenty"><b><xsl:value-of select="$i18n.Date" />:</b></div>
			<div class="floatleft eighty">
				<xsl:value-of select="startDate" />
				<xsl:if test="daysBetween > 1">
					<xsl:text>&#x20;-&#x20;</xsl:text>
					<xsl:value-of select="endDate" />
				</xsl:if>
			</div>
		</div>
		<div class="floatleft full bigmarginbottom">
			<div class="floatleft twenty"><b><xsl:value-of select="$i18n.Time" />:</b></div>
			<div class="floatleft eighty">
				<xsl:call-template name="getFormattedTime">
					<xsl:with-param name="daysBetween" select="daysBetween" />
					<xsl:with-param name="startTime" select="startTime" />
					<xsl:with-param name="endTime" select="endTime" />
				</xsl:call-template>
			</div>
		</div>
		<div class="floatleft full bigmarginbottom">
			<div class="floatleft twenty"><b><xsl:value-of select="$i18n.Added" />:</b></div>
			<div class="floatleft eighty">
				<xsl:call-template name="createPostedByUserText">
					<xsl:with-param name="date" select="posted" />
					<xsl:with-param name="user" select="poster/user" />
				</xsl:call-template>
			</div>
		</div>
		<xsl:if test="updated">
			<div class="floatleft full bigmarginbottom">
				<div class="floatleft twenty"><b><xsl:value-of select="$i18n.Updated" />:</b></div>
				<div class="floatleft eighty">
					<xsl:call-template name="createPostedByUserText">
						<xsl:with-param name="date" select="updated" />
						<xsl:with-param name="user" select="editor/user" />
					</xsl:call-template>
				</div>
			</div>
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template name="AbsenceForm">
	
		<xsl:param name="absence" select="null" />
	
		<xsl:variable name="oneDay">
			<xsl:choose>
				<xsl:when test="$absence and $absence/startDate = $absence/endDate">true</xsl:when>
				<xsl:when test="$absence">false</xsl:when>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:variable name="wholeDay">
			<xsl:choose>
				<xsl:when test="($oneDay = 'true' and $absence/startTime = $absence/endTime) or $oneDay = 'false'">true</xsl:when>
				<xsl:when test="$absence">false</xsl:when>
			</xsl:choose>
		</xsl:variable>
		
		<div class="floatleft full bigmarginbottom">
			<label for="name"><xsl:value-of select="$i18n.Name" />:</label><br />
			<xsl:call-template name="createTextField">
				<xsl:with-param name="id" select="'name'"/>
				<xsl:with-param name="name" select="'name'"/>
				<xsl:with-param name="title" select="$i18n.Name"/>
				<xsl:with-param name="element" select="$absence" />
				<xsl:with-param name="size" select="'55'" />
			</xsl:call-template>
		</div>
		
		<div class="floatleft full bigmarginbottom">
			<label for="comment"><xsl:value-of select="$i18n.Comment" />:</label>
			<xsl:call-template name="createTextArea">
				<xsl:with-param name="id" select="'comment'"/>
				<xsl:with-param name="name" select="'comment'"/>
				<xsl:with-param name="title" select="$i18n.Comment"/>
				<xsl:with-param name="element" select="$absence" />
				<xsl:with-param name="element" select="$absence" />
				<xsl:with-param name="width" select="'99%'" />
				<xsl:with-param name="rows" select="'3'" />
			</xsl:call-template>
		</div>
	
		<div class="floatleft full bigmarginbottom">
			<div class="floatleft ten"><xsl:value-of select="$i18n.Period" />:</div>
			<div class="floatleft eighty">
				<xsl:call-template name="createRadio">
					<xsl:with-param name="id" select="'oneDay'" />
					<xsl:with-param name="name" select="'period'" />
					<xsl:with-param name="value" select="'oneday'" />
					<xsl:with-param name="checked">
						<xsl:if test="$oneDay = 'true'">true</xsl:if>
					</xsl:with-param>
				</xsl:call-template>
				<label for="oneDay"><xsl:value-of select="$i18n.OneDay" /></label><br/>
				<xsl:call-template name="createRadio">
					<xsl:with-param name="id" select="'severalDays'" />
					<xsl:with-param name="name" select="'period'" />
					<xsl:with-param name="value" select="'severaldays'" />
					<xsl:with-param name="checked">
						<xsl:if test="$oneDay = 'false'">true</xsl:if>
					</xsl:with-param>
				</xsl:call-template>
				<label for="severalDays"><xsl:value-of select="$i18n.SeveralDays" /></label>
			</div>
		</div>
	
		<div class="floatleft full bigmarginbottom">

			<div id="oneDayForm" class="floatleft full hidden">

				<div class="floatleft full bigmarginbottom">				
					<div class="floatleft ten"><label for="startDate"><xsl:value-of select="$i18n.Date" />:</label></div>
					<div class="floatleft eighty">
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="'date'"/>
							<xsl:with-param name="name" select="'date'"/>
							<xsl:with-param name="title" select="$i18n.Date"/>
							<xsl:with-param name="size" select="'10'" />
							<xsl:with-param name="class" select="'dateformat-Y-ds-m-ds-d show-weeks fill-grid no-animation disable-drag range-low-0-day'" />
							<xsl:with-param name="value">
								<xsl:if test="$oneDay = 'true'"><xsl:value-of select="$absence/startDate" /></xsl:if>
							</xsl:with-param>
						</xsl:call-template>
					</div>
				</div>
				<div class="floatleft full bigmarginbottom">
					<div class="floatleft ten"><xsl:value-of select="$i18n.Time" />:</div>
					<div class="floatleft eighty">
						<xsl:call-template name="createRadio">
							<xsl:with-param name="id" select="'wholeDay'" />
							<xsl:with-param name="name" select="'time'" />
							<xsl:with-param name="value" select="'wholeday'" />
							<xsl:with-param name="checked">
								<xsl:if test="$wholeDay = 'true' or not($absence)">true</xsl:if>
							</xsl:with-param>
						</xsl:call-template>
						<label for="wholeDay"><xsl:value-of select="$i18n.WholeDay" /></label><br/>
						<xsl:call-template name="createRadio">
							<xsl:with-param name="id" select="'partOfDay'" />
							<xsl:with-param name="name" select="'time'" />
							<xsl:with-param name="value" select="'partofday'" />
							<xsl:with-param name="checked">
								<xsl:if test="$wholeDay = 'false'">true</xsl:if>
							</xsl:with-param>
						</xsl:call-template>
						<label for="partOfDay"><xsl:value-of select="$i18n.PartOfDay" /><xsl:text>&#x20;</xsl:text></label>
						<label for="startTime"><xsl:value-of select="$i18n.from" /><xsl:text>&#x20;</xsl:text></label>
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="'startTime'"/>
							<xsl:with-param name="name" select="'startTime'"/>
							<xsl:with-param name="title" select="$i18n.StartTime"/>
							<xsl:with-param name="element" select="$absence" />
							<xsl:with-param name="size" select="'5'" />
							<xsl:with-param name="disabled" select="'true'" />
							<xsl:with-param name="value">
								<xsl:if test="$wholeDay = 'false'"><xsl:value-of select="$absence/startTime" /></xsl:if>
							</xsl:with-param>
						</xsl:call-template>
						<xsl:text>&#x20;</xsl:text>
						<label for="endTime"><xsl:value-of select="$i18n.to" /><xsl:text>&#x20;</xsl:text></label>
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="'endTime'"/>
							<xsl:with-param name="name" select="'endTime'"/>
							<xsl:with-param name="title" select="$i18n.EndTime"/>
							<xsl:with-param name="element" select="$absence" />
							<xsl:with-param name="size" select="'5'" />
							<xsl:with-param name="disabled" select="'true'" />
							<xsl:with-param name="value">
								<xsl:if test="$wholeDay = 'false'"><xsl:value-of select="$absence/endTime" /></xsl:if>
							</xsl:with-param>
						</xsl:call-template>
					</div>
				</div>
			</div>
			
			<div id="severalDaysForm" class="floatleft full hidden">
				
				<div class="floatleft full bigmarginbottom">
					<div class="floatleft ten"><label for="startDate"><xsl:value-of select="$i18n.From" />:</label></div>
					<div class="floatleft eighty">
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="'startDate'"/>
							<xsl:with-param name="name" select="'startDate'"/>
							<xsl:with-param name="title" select="$i18n.StartDate"/>
							<xsl:with-param name="element" select="$absence" />
							<xsl:with-param name="size" select="'10'" />
							<xsl:with-param name="class" select="'dateformat-Y-ds-m-ds-d show-weeks fill-grid no-animation disable-drag range-low-0-day'" />
							<xsl:with-param name="value">
								<xsl:if test="$oneDay = 'false'">
									<xsl:value-of select="$absence/startDate" />
								</xsl:if>
							</xsl:with-param>
						</xsl:call-template>
					</div>
				</div>
				
				<div class="floatleft full bigmarginbottom">
					<div class="floatleft ten"><label for="endDate"><xsl:value-of select="$i18n.To" />:</label></div>
					<div class="floatleft eighty">
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="'endDate'"/>
							<xsl:with-param name="name" select="'endDate'"/>
							<xsl:with-param name="title" select="$i18n.EndDate"/>
							<xsl:with-param name="element" select="$absence" />
							<xsl:with-param name="size" select="'10'" />
							<xsl:with-param name="class" select="'dateformat-Y-ds-m-ds-d show-weeks fill-grid no-animation disable-drag range-low-0-day'" />
							<xsl:with-param name="value">
								<xsl:if test="$oneDay = 'false'">
									<xsl:value-of select="$absence/endDate" />
								</xsl:if>
							</xsl:with-param>
						</xsl:call-template>
					</div>
				</div>
			
			</div>
			
		</div>
	
	</xsl:template>

	<xsl:template name="createUpdateDeleteLinks">
		
		<xsl:param name="mode" />
		<xsl:param name="isClosed" select="closed" />
		
		<xsl:choose>
			<xsl:when test="$isClosed = 'true' and $mode = 'Public'">
				<img src="{$moduleImagePath}/edit_disabled.png" title="{$i18n.UpdateAbsenceDisabled}" />
				<img src="{$moduleImagePath}/delete_disabled.png" title="{$i18n.DeleteAbsenceDisabled}" />
			</xsl:when>
			<xsl:otherwise>
				<a href="{$modulePath}/updateabsence/{absenceID}" title="{$i18n.UpdateAbsence}"><img src="{$moduleImagePath}/edit.png" /></a>
				<a href="{$modulePath}/deleteabsence/{absenceID}" title="{$i18n.DeleteAbsence}" onclick="return confirmDelete('{$i18n.DeleteAbsenceConfirm}?')"><img src="{$moduleImagePath}/delete.png" /></a>
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>

	<xsl:template name="getFormattedTime">
		
		<xsl:param name="daysBetween" />
		<xsl:param name="startTime" />
		<xsl:param name="endTime" />
		
		<xsl:choose>
			<xsl:when test="$daysBetween and $startTime = $endTime">
				<xsl:value-of select="$daysBetween" /><xsl:text>&#x20;</xsl:text>
				<xsl:choose>
					<xsl:when test="$daysBetween = 1">
						<xsl:value-of select="$i18n.day" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.days" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$startTime" /><xsl:text>&#x20;-&#x20;</xsl:text><xsl:value-of select="$endTime" />
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	
	<xsl:template match="validationError">
		<xsl:if test="fieldName and validationErrorType">
			<p class="error">
				<xsl:choose>
					<xsl:when test="validationErrorType='RequiredField'">
						<xsl:text><xsl:value-of select="$i18n.validationError.RequiredField"/></xsl:text>
					</xsl:when>
					<xsl:when test="validationErrorType='InvalidFormat'">
						<xsl:text><xsl:value-of select="$i18n.validationError.InvalidFormat"/></xsl:text>
					</xsl:when>
					<xsl:when test="validationErrorType='TooShort'">
						<xsl:text><xsl:value-of select="$i18n.validationError.TooShort" /></xsl:text>
					</xsl:when>
					<xsl:when test="validationErrorType='TooLong'">
						<xsl:text><xsl:value-of select="$i18n.validationError.TooLong" /></xsl:text>
					</xsl:when>									
					<xsl:when test="validationErrorType='Other'">
						<xsl:text><xsl:value-of select="$i18n.validationError.Other" /></xsl:text>
					</xsl:when>	
					<xsl:otherwise>
						<xsl:text><xsl:value-of select="$i18n.validationError.unknownValidationErrorType"/></xsl:text>
					</xsl:otherwise>					
				</xsl:choose>
				
				<xsl:text>&#x20;</xsl:text>
				
				<xsl:choose>
					<xsl:when test="fieldName = 'name'">
						<xsl:value-of select="$i18n.name"/>
					</xsl:when>
					<xsl:when test="fieldName = 'comment'">
						<xsl:value-of select="$i18n.comment"/>
					</xsl:when>
					<xsl:when test="fieldName = 'date'">
						<xsl:value-of select="$i18n.date"/>
					</xsl:when>
					<xsl:when test="fieldName = 'startDate'">
						<xsl:value-of select="$i18n.from"/>
					</xsl:when>
					<xsl:when test="fieldName = 'endDate'">
						<xsl:value-of select="$i18n.to"/>
					</xsl:when>
					<xsl:when test="fieldName = 'startTime'">
						<xsl:value-of select="$i18n.startTime"/>
					</xsl:when>
					<xsl:when test="fieldName = 'endTime'">
						<xsl:value-of select="$i18n.endTime"/>
					</xsl:when>
					<xsl:when test="fieldName = 'time'">
						<xsl:value-of select="$i18n.time"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="fieldName"/>
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:text>!</xsl:text>
			</p>
		</xsl:if>
	
		<xsl:if test="messageKey">
			<p class="error">
				<xsl:choose>
					<xsl:when test="messageKey='EndTimeBeforeStartTime'">
						<xsl:value-of select="$i18n.validationError.EndTimeBeforeStartTime" />!
					</xsl:when>
					<xsl:when test="messageKey='DaysBetweenToSmall'">
						<xsl:value-of select="$i18n.validationError.DaysBetweenToSmall" />!
					</xsl:when>
					<xsl:when test="messageKey='TimeBeforeLastReportTime'">
						<xsl:value-of select="$i18n.validationError.TimeBeforeLastReportTime" />!<xsl:text>&#x20;</xsl:text><xsl:value-of select="$i18n.LastReportHourPart1" /><xsl:text>&#x20;</xsl:text><xsl:value-of select="/Document/lastReportHour" />:00!
					</xsl:when>
					<xsl:when test="messageKey='NoGroup'">
						<xsl:value-of select="$i18n.validationError.NoGroup" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.validationError.unknownMessageKey" />!
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>
		
	</xsl:template>
	
</xsl:stylesheet>
