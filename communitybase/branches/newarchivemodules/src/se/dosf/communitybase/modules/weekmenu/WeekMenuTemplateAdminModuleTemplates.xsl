<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl" />

	<!-- <xsl:variable name="globalscripts"> -->
	<!-- /jquery/jquery.js -->
	<!-- </xsl:variable> -->

	<!-- <xsl:variable name="scripts"> -->
	<!-- /js/jquery.tablesorter.min.js -->
	<!-- /js/init.tablesorter.js -->
	<!-- </xsl:variable> -->

	<!-- <xsl:variable name="links"> -->
	<!-- /css/tablesorter/tablesorter.css -->
	<!-- </xsl:variable> -->

	<xsl:template match="Document">

		<div id="WeekMenuTemplateAdminModule" class="contentitem">

			<xsl:apply-templates select="ListWeekMenuTemplates" />
			<xsl:apply-templates select="AddWeekMenuTemplate" />
			<xsl:apply-templates select="UpdateWeekMenuTemplate" />
		</div>

	</xsl:template>

	<xsl:template match="ListWeekMenuTemplates">

		<h1>
			<xsl:value-of select="$i18n.ListWeekMenuTemplates.title" />
		</h1>

		<table class="full border">
			<thead class="sortable">
				<tr>
					<th>
						<xsl:value-of select="$i18n.Name" />
					</th>
					<th>
						<xsl:value-of select="$i18n.WeeksCount" />
					</th>
					<th>
						<xsl:value-of select="$i18n.Start" />
					</th>
					<th>
						<xsl:value-of select="$i18n.End" />
					</th>
					<th width="33px" class="no-sort" />
				</tr>
			</thead>

			<tbody>

				<xsl:choose>
					<xsl:when test="WeekMenuTemplates">

						<xsl:apply-templates select="WeekMenuTemplates/WeekMenuTemplate"
							mode="list" />

					</xsl:when>
					<xsl:otherwise>

						<tr>
							<td colspan="5">
								<p>
									<xsl:value-of
										select="$i18n.ListWeekMenuTemplates.NoWeekMenuTemplatesFound" />
								</p>
							</td>
						</tr>

					</xsl:otherwise>
				</xsl:choose>

			</tbody>
		</table>

		<div class="floatright">
			<a href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/add"
				title="{$i18n.ListWeekMenuTemplates.Add}">
				<img class="aligntop marginright"
					src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/add.png" />
				<xsl:value-of select="$i18n.ListWeekMenuTemplates.Add" />
			</a>
		</div>

	</xsl:template>

	<xsl:template match="WeekMenuTemplate" mode="list">

		<tr>
			<td>
				<a
					href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/update/{weekMenuTemplateID}"
					title="{$i18n.Update}: {name}">
					<xsl:value-of select="name" />
				</a>
			</td>
			<td>
				<a
					href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/update/{weekMenuTemplateID}"
					title="{$i18n.Update}: {name}">
					<xsl:value-of select="weeks" />
				</a>
			</td>
			<td>
				<a
					href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/update/{weekMenuTemplateID}"
					title="{$i18n.Update}: {name}">
					<xsl:value-of select="startYear" />
					<xsl:value-of select="' V'" />
					<xsl:value-of select="startWeek" />
				</a>
			</td>
			<td>
				<a
					href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/update/{weekMenuTemplateID}"
					title="{$i18n.Update}: {name}">
					<xsl:value-of select="endYear" />
					<xsl:value-of select="' V'" />
					<xsl:value-of select="endWeek" />
				</a>
			</td>
			<td>
				<a
					href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/update/{weekMenuTemplateID}"
					title="{$i18n.Update}: {name}">
					<img class="alignbottom"
						src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/edit.png" />
				</a>
				<a
					href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/delete/{weekMenuTemplateID}"
					onclick="return confirm('{$i18n.Delete}: {name}?')" title="{$i18n.Delete}: {name}">
					<img class="alignbottom"
						src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/delete.png" />
				</a>
			</td>
		</tr>

	</xsl:template>

	<xsl:template match="AddWeekMenuTemplate">

		<h1>
			<xsl:value-of select="$i18n.AddWeekMenuTemplate.title" />
		</h1>

		<form method="post" action="{/Document/requestinfo/uri}">

			<xsl:apply-templates select="validationException/validationError" />

			<div class="floatleft full bigmarginbottom">
				<label for="name" class="floatleft full">
					<xsl:value-of select="$i18n.Name" />
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'name'" />
						<xsl:with-param name="name" select="'name'" />
					</xsl:call-template>
				</div>
			</div>

			<div class="floatleft full bigmarginbottom">
				<label for="weeks" class="floatleft full">
					<xsl:value-of select="$i18n.RollingWeeks" />
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'weeks'" />
						<xsl:with-param name="name" select="'weeks'" />
						<xsl:with-param name="value" select="'10'" />
					</xsl:call-template>
				</div>
			</div>

			<div class="floatleft full bigmarginbottom">
				<label for="startYear" class="floatleft full">
					<xsl:value-of select="$i18n.StartYear" />
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'startYear'" />
						<xsl:with-param name="name" select="'startYear'" />
						<xsl:with-param name="value" select="CurrentYear" />
					</xsl:call-template>
				</div>
			</div>

			<div class="floatleft full bigmarginbottom">
				<label for="startWeek" class="floatleft full">
					<xsl:value-of select="$i18n.StartWeek" />
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'startWeek'" />
						<xsl:with-param name="name" select="'startWeek'" />
					</xsl:call-template>
				</div>
			</div>

			<div class="floatleft full bigmarginbottom">
				<label for="endYear" class="floatleft full">
					<xsl:value-of select="$i18n.EndYear" />
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'endYear'" />
						<xsl:with-param name="name" select="'endYear'" />
						<xsl:with-param name="value" select="CurrentYear" />
					</xsl:call-template>
				</div>
			</div>

			<div class="floatleft full bigmarginbottom">
				<label for="endWeek" class="floatleft full">
					<xsl:value-of select="$i18n.EndWeek" />
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'endWeek'" />
						<xsl:with-param name="name" select="'endWeek'" />
					</xsl:call-template>
				</div>
			</div>

			<div class="floatright">
				<input type="submit" value="{$i18n.Add}" />
			</div>

		</form>

	</xsl:template>

	<xsl:template match="UpdateWeekMenuTemplate">

		<h1>
			<xsl:value-of select="$i18n.UpdateWeekMenuTemplate.title" />
		</h1>

		<form method="post" action="{/Document/requestinfo/uri}">

			<xsl:apply-templates select="validationException/validationError" />

			<div class="floatleft full bigmarginbottom">
				<label for="name" class="floatleft full">
					<xsl:value-of select="$i18n.Name" />
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'name'" />
						<xsl:with-param name="name" select="'name'" />
						<xsl:with-param name="element" select="WeekMenuTemplate" />
					</xsl:call-template>
				</div>
			</div>

			<div class="floatleft full bigmarginbottom">
				<label for="weeks" class="floatleft full">
					<xsl:value-of select="$i18n.RollingWeeks" />
				</label>

				<div class="floatleft full">
					<xsl:call-template name="createHiddenField">
						<xsl:with-param name="id" select="'weeks'" />
						<xsl:with-param name="name" select="'weeks'" />
						<xsl:with-param name="value" select="WeekMenuTemplate/weeks" />
					</xsl:call-template>
					<xsl:value-of select="WeekMenuTemplate/weeks" />
				</div>
			</div>

			<div class="floatleft full bigmarginbottom">
				<label for="startYear" class="floatleft full">
					<xsl:value-of select="$i18n.StartYear" />
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'startYear'" />
						<xsl:with-param name="name" select="'startYear'" />
						<xsl:with-param name="element" select="WeekMenuTemplate" />
					</xsl:call-template>
				</div>
			</div>

			<div class="floatleft full bigmarginbottom">
				<label for="startWeek" class="floatleft full">
					<xsl:value-of select="$i18n.StartWeek" />
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'startWeek'" />
						<xsl:with-param name="name" select="'startWeek'" />
						<xsl:with-param name="element" select="WeekMenuTemplate" />
					</xsl:call-template>
				</div>
			</div>

			<div class="floatleft full bigmarginbottom">
				<label for="endYear" class="floatleft full">
					<xsl:value-of select="$i18n.EndYear" />
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'endYear'" />
						<xsl:with-param name="name" select="'endYear'" />
						<xsl:with-param name="element" select="WeekMenuTemplate" />
					</xsl:call-template>
				</div>
			</div>

			<div class="floatleft full bigmarginbottom">
				<label for="endWeek" class="floatleft full">
					<xsl:value-of select="$i18n.EndWeek" />
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'endWeek'" />
						<xsl:with-param name="name" select="'endWeek'" />
						<xsl:with-param name="element" select="WeekMenuTemplate" />
					</xsl:call-template>
				</div>
			</div>

			<xsl:apply-templates select="WeekMenuTemplate/menus/WeekMenu" />

			<div class="floatright">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>

		</form>

	</xsl:template>

	<xsl:template match="WeekMenu">

		<div>

			<h2>
				<xsl:value-of select="$i18n.Week" />
				<xsl:value-of select="': '" />
				<xsl:value-of select="week" />
			</h2>

			<div class="floatleft full bigmarginbottom">
				<label for="{concat('monday-', week)}" class="floatleft full">
					<xsl:value-of select="$i18n.Monday" />
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="concat('monday-', week)" />
						<xsl:with-param name="name" select="concat('monday-', week)" />
						<xsl:with-param name="maxlength" select="'255'" />
						<xsl:with-param name="value" select="monday" />
					</xsl:call-template>
				</div>
			</div>

			<div class="floatleft full bigmarginbottom">
				<label for="{concat('tuesday-', week)}" class="floatleft full">
					<xsl:value-of select="$i18n.Tuesday" />
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="concat('tuesday-', week)" />
						<xsl:with-param name="name" select="concat('tuesday-', week)" />
						<xsl:with-param name="maxlength" select="'255'" />
						<xsl:with-param name="value" select="tuesday" />
					</xsl:call-template>
				</div>
			</div>

			<div class="floatleft full bigmarginbottom">
				<label for="{concat('wednesday-', week)}" class="floatleft full">
					<xsl:value-of select="$i18n.Wednesday" />
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="concat('wednesday-', week)" />
						<xsl:with-param name="name" select="concat('wednesday-', week)" />
						<xsl:with-param name="maxlength" select="'255'" />
						<xsl:with-param name="value" select="wednesday" />
					</xsl:call-template>
				</div>
			</div>

			<div class="floatleft full bigmarginbottom">
				<label for="{concat('thursday-', week)}" class="floatleft full">
					<xsl:value-of select="$i18n.Thursday" />
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="concat('thursday-', week)" />
						<xsl:with-param name="name" select="concat('thursday-', week)" />
						<xsl:with-param name="maxlength" select="'255'" />
						<xsl:with-param name="value" select="thursday" />
					</xsl:call-template>
				</div>
			</div>

			<div class="floatleft full bigmarginbottom">
				<label for="{concat('friday-', week)}" class="floatleft full">
					<xsl:value-of select="$i18n.Friday" />
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="concat('friday-', week)" />
						<xsl:with-param name="name" select="concat('friday-', week)" />
						<xsl:with-param name="maxlength" select="'255'" />
						<xsl:with-param name="value" select="friday" />
					</xsl:call-template>
				</div>
			</div>

			<div class="floatleft full bigmarginbottom">
				<label for="{concat('saturday-', week)}" class="floatleft full">
					<xsl:value-of select="$i18n.Saturday" />
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="concat('saturday-', week)" />
						<xsl:with-param name="name" select="concat('saturday-', week)" />
						<xsl:with-param name="maxlength" select="'255'" />
						<xsl:with-param name="value" select="saturday" />
					</xsl:call-template>
				</div>
			</div>

			<div class="floatleft full bigmarginbottom">
				<label for="{concat('sunday-', week)}" class="floatleft full">
					<xsl:value-of select="$i18n.Sunday" />
				</label>
				<div class="floatleft full">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="concat('sunday-', week)" />
						<xsl:with-param name="name" select="concat('sunday-', week)" />
						<xsl:with-param name="maxlength" select="'255'" />
						<xsl:with-param name="value" select="sunday" />
					</xsl:call-template>
				</div>
			</div>

		</div>

	</xsl:template>

	<xsl:template match="validationError">

		<xsl:if test="fieldName and validationErrorType and not(messageKey)">
			<p class="error">
				<xsl:choose>
					<xsl:when test="validationErrorType='RequiredField'">
						<xsl:value-of select="$i18n.ValidationError.RequiredField" />
					</xsl:when>
					<xsl:when test="validationErrorType='InvalidFormat'">
						<xsl:value-of select="$i18n.ValidationError.InvalidFormat" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooShort'">
						<xsl:value-of select="$i18n.ValidationError.TooShort" />
					</xsl:when>
					<xsl:when test="validationErrorType='TooLong'">
						<xsl:value-of select="$i18n.ValidationError.TooLong" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$i18n.ValidationError.UnknownValidationErrorType" />
					</xsl:otherwise>
				</xsl:choose>

				<xsl:text>&#x20;</xsl:text>

				<xsl:choose>
					<xsl:when test="fieldName = 'name'">
						<xsl:value-of select="$i18n.Name" />
					</xsl:when>
					<xsl:when test="fieldName = 'weeks'">
						<xsl:value-of select="$i18n.RollingWeeks" />
					</xsl:when>
					<xsl:when test="fieldName = 'startWeek'">
						<xsl:value-of select="$i18n.StartWeek" />
					</xsl:when>
					<xsl:when test="fieldName = 'endWeek'">
						<xsl:value-of select="$i18n.EndWeek" />
					</xsl:when>
					<xsl:when test="fieldName = 'startYear'">
						<xsl:value-of select="$i18n.StartYear" />
					</xsl:when>
					<xsl:when test="fieldName = 'endYear'">
						<xsl:value-of select="$i18n.EndYear" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="fieldName" />
					</xsl:otherwise>
				</xsl:choose>

				<xsl:text>!</xsl:text>
			</p>
		</xsl:if>

		<xsl:if test="messageKey">
			<p class="error">
				<xsl:choose>
					<xsl:when test="messageKey='StartDateIsInThePast'">
						<xsl:value-of select="$i18n.ValidationError.StartDateIsInThePast" />
					</xsl:when>
					<xsl:when test="messageKey='EndDateIsInThePast'">
						<xsl:value-of select="$i18n.ValidationError.EndDateIsInThePast" />
					</xsl:when>
					<xsl:when test="messageKey='StartIsAfterEnd'">
						<xsl:value-of select="$i18n.ValidationError.StartIsAfterEnd" />
					</xsl:when>
					<xsl:when test="messageKey='InvalidWeek'">
					
						<xsl:value-of select="$i18n.ValidationError.OutOfRangeWeek" />
						
						<xsl:choose>
							<xsl:when test="fieldName = 'weeks'">
								<xsl:value-of select="$i18n.RollingWeeks" />
							</xsl:when>
							<xsl:when test="fieldName = 'startWeek'">
								<xsl:value-of select="$i18n.StartWeek" />
							</xsl:when>
							<xsl:when test="fieldName = 'endWeek'">
								<xsl:value-of select="$i18n.EndWeek" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="fieldName" />
							</xsl:otherwise>
						</xsl:choose>
						
						<xsl:value-of select="$i18n.ValidationError.OutOfRangeWeekBetween" />
						<xsl:value-of select="minimum" />
						<xsl:value-of select="$i18n.ValidationError.OutOfRangeWeekBetweenAnd" />
						<xsl:value-of select="maximum" />
						
					</xsl:when>
					<xsl:when test="messageKey='StartDateOverlapsWithOther'">
						<xsl:value-of select="$i18n.ValidationError.StartDateOverlapsWithOther" />
						<xsl:text>: </xsl:text>
						<xsl:value-of select="collider"/>
					</xsl:when>
					<xsl:when test="messageKey='EndDateOverlapsWithOther'">
						<xsl:value-of select="$i18n.ValidationError.EndDateOverlapsWithOther" />
						<xsl:text>: </xsl:text>
						<xsl:value-of select="collider"/>
					</xsl:when>
					<xsl:when test="messageKey='DateContainsOther'">
						<xsl:value-of select="$i18n.ValidationError.DateContainsOther" />
						<xsl:text>: </xsl:text>
						<xsl:value-of select="collider"/>
					</xsl:when>

					<xsl:otherwise>
						<xsl:value-of select="$i18n.ValidationError.UnknownFault" />
					</xsl:otherwise>
				</xsl:choose>
			</p>
		</xsl:if>

	</xsl:template>

</xsl:stylesheet>

