<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include
		href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl" />

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/templateChoosing.js
	</xsl:variable>

	<xsl:template match="Document">

		<div id="SchoolMenuAdminModule" class="contentitem">

			<xsl:apply-templates select="ListSchoolMenus" />
			<xsl:apply-templates select="ShowSchoolMenu" />
			<xsl:apply-templates select="AddSchoolMenu" />
			<xsl:apply-templates select="UpdateSchoolMenu" />
		</div>

	</xsl:template>

	<xsl:template match="ListSchoolMenus">

		<xsl:call-template name="ShowSchoolMenu" />

	</xsl:template>

	<xsl:template match="ShowSchoolMenu" name="ShowSchoolMenu">

		<h1>
			<xsl:value-of select="$i18n.ShowSchoolMenu.title" />
			<xsl:value-of select="Week" />
		</h1>

		<div class="floatleft full">
			<div class="content-box">
				<h1 class="header">

					<div class="floatleft thirty">
						<a
							href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/{/Document/group/groupID}/show/{Year}/{Week - 1}"
							title="{$i18n.ShowSchoolMenu.PreviousWeek}">
							<img class="alignmiddle"
								src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/arrow_left.png" />
						</a>
						<xsl:text> </xsl:text>
						<a
							href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/{/Document/group/groupID}/show/{Year}/{Week - 1}"
							title="{$i18n.ShowSchoolMenu.PreviousWeek}">
							<xsl:value-of select="$i18n.ShowSchoolMenu.PreviousWeek" />
						</a>
					</div>

					<div class="floatleft forty text-align-center">
						<xsl:value-of select="concat($i18n.WeekShort, Week, ' ', Year)" />
					</div>

					<div class="floatleft thirty text-align-right">
						<a
							href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/{/Document/group/groupID}/show/{Year}/{Week + 1}"
							title="{$i18n.ShowSchoolMenu.NextWeek}">
							<xsl:value-of select="$i18n.ShowSchoolMenu.NextWeek" />
						</a>
						<xsl:text> </xsl:text>
						<a
							href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/{/Document/group/groupID}/show/{Year}/{Week + 1}"
							title="{$i18n.ShowSchoolMenu.NextWeek}">
							<img class="alignmiddle"
								src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/arrow_right.png" />
						</a>
					</div>

				</h1>

				<div class="content">

					<xsl:choose>
						<xsl:when test="SchoolMenu">

							<xsl:apply-templates select="SchoolMenu" />

							<xsl:if test="Admin">
								<div class="floatright">
									<a
										href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/{/Document/group/groupID}/update/{SchoolMenu/schoolMenuID}"
										title="{$i18n.ShowSchoolMenu.Update}">
										<xsl:value-of select="$i18n.ShowSchoolMenu.Update" />
										<img class="aligntop marginleft"
											src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/plate_edit.png" />
									</a>
								</div>
							</xsl:if>

						</xsl:when>
						<xsl:otherwise>

							<div class="floatleft full">
								<xsl:value-of select="$i18n.ShowSchoolMenu.NoMenu" />
							</div>

							<xsl:if test="Admin">
								<div class="floatright">
									<a
										href="{/Document/requestinfo/currentURI}/{/Document/module/alias}/{/Document/group/groupID}/add/{Year}/{Week}"
										title="{$i18n.ShowSchoolMenu.Add}">
										<xsl:value-of select="$i18n.ShowSchoolMenu.Add" />
										<img class="aligntop marginleft"
											src="{/Document/requestinfo/contextpath}/static/f/{/Document/module/sectionID}/{/Document/module/moduleID}/pics/plate_add.png" />
									</a>
								</div>
							</xsl:if>

						</xsl:otherwise>
					</xsl:choose>

				</div>

			</div>

		</div>

	</xsl:template>

	<xsl:template match="SchoolMenu">

		<div class="floatleft full bigmargintop bigmarginleft">
			<div class="floatleft twenty bold">
				<xsl:value-of select="concat($i18n.Monday, ': ')" />
			</div>
			<div class="floatleft fifty">
				<xsl:value-of select="monday" />
			</div>
		</div>

		<div class="floatleft full margintop bigmarginleft">
			<div class="floatleft twenty bold">
				<xsl:value-of select="concat($i18n.Tuesday, ': ')" />
			</div>
			<div class="floatleft fifty">
				<xsl:value-of select="tuesday" />
			</div>
		</div>

		<div class="floatleft full margintop bigmarginleft">
			<div class="floatleft twenty bold">
				<xsl:value-of select="concat($i18n.Wednesday, ': ')" />
			</div>
			<div class="floatleft fifty">
				<xsl:value-of select="wednesday" />
			</div>
		</div>

		<div class="floatleft full margintop bigmarginleft">
			<div class="floatleft twenty bold">
				<xsl:value-of select="concat($i18n.Thursday, ': ')" />
			</div>
			<div class="floatleft fifty">
				<xsl:value-of select="thursday" />
			</div>
		</div>

		<div class="floatleft full margintop bigmarginleft">
			<div class="floatleft twenty bold">
				<xsl:value-of select="concat($i18n.Friday, ': ')" />
			</div>
			<div class="floatleft fifty">
				<xsl:value-of select="friday" />
			</div>
		</div>

		<xsl:if test="saturday  != '' ">
			<div class="floatleft full margintop bigmarginleft">
				<div class="floatleft twenty bold">
					<xsl:value-of select="concat($i18n.Saturday, ': ')" />
				</div>
				<div class="floatleft fifty">
					<xsl:value-of select="saturday" />
				</div>
			</div>
		</xsl:if>

		<xsl:if test="sunday  != '' ">
			<div class="floatleft full margintop bigmarginleft">
				<div class="floatleft twenty bold">
					<xsl:value-of select="concat($i18n.Sunday, ': ')" />
				</div>
				<div class="floatleft fifty">
					<xsl:value-of select="sunday" />
				</div>
			</div>
		</xsl:if>

	</xsl:template>

	<xsl:template match="AddSchoolMenu">

		<h1>
			<xsl:value-of select="$i18n.AddSchoolMenu.title" />
			<xsl:value-of select="Week" />
		</h1>

		<xsl:choose>
			<xsl:when test="InvalidDate">
				<xsl:value-of select="$i18n.AddSchoolMenu.InvalidDate" />
			</xsl:when>
			<xsl:otherwise>

				<form method="post" action="{/Document/requestinfo/uri}">

					<xsl:apply-templates select="validationException/validationError" />

					<xsl:call-template name="createHiddenField">
						<xsl:with-param name="id" select="'week'" />
						<xsl:with-param name="name" select="'week'" />
						<xsl:with-param name="value" select="Week" />
					</xsl:call-template>

					<xsl:call-template name="createHiddenField">
						<xsl:with-param name="id" select="'year'" />
						<xsl:with-param name="name" select="'year'" />
						<xsl:with-param name="value" select="Year" />
					</xsl:call-template>

					<xsl:choose>
						<xsl:when test="WeekMenus">

							<div class="floatleft marginbottom">
								<xsl:value-of select="$i18n.AddSchoolMenu.UsingTemplate" />
							</div>

							<div class="floatleft full bigmarginbottom">
								<label for="template" class="floatleft full">
									<xsl:value-of select="$i18n.TemplateOffset" />
								</label>
								<div class="floatleft full">
									<xsl:call-template name="createDropdown">
										<xsl:with-param name="id" select="'offset'" />
										<xsl:with-param name="name" select="'offset'" />
										<xsl:with-param name="element" select="WeekMenus/WeekMenu" />
										<xsl:with-param name="valueElementName" select="'week'" />
										<xsl:with-param name="labelElementName" select="'week'" />
										<xsl:with-param name="selectedValue" select="SchoolTemplateOffset + 1" />
									</xsl:call-template>
								</div>
							</div>

							<div class="hidden">
								<div id="WeekMenu_Prev">
									<xsl:value-of select="WeekMenu/week" />
								</div>
								<div id="WeekMenu_DefaultOffset">
									<xsl:value-of select="DefaultOffset" />
								</div>
								<div id="WeekMenu_Weeks">
									<xsl:value-of select="count(WeekMenus/WeekMenu)" />
								</div>
								<div id="WeekMenu_WeekPrefix">
									<xsl:value-of select="$i18n.AddSchoolMenu.OffsetWeekPrefix" />
								</div>
								<div id="WeekMenu_OverwriteChanges">
									<xsl:value-of select="$i18n.AddSchoolMenu.ConfirmOverwriteChanges" />
								</div>
								
								<xsl:for-each select="WeekMenus/WeekMenu">
									<div id="WeekMenu{week}-monday">
										<xsl:value-of select="monday" />
									</div>
									<div id="WeekMenu{week}-tuesday">
										<xsl:value-of select="tuesday" />
									</div>
									<div id="WeekMenu{week}-wednesday">
										<xsl:value-of select="wednesday" />
									</div>
									<div id="WeekMenu{week}-thursday">
										<xsl:value-of select="thursday" />
									</div>
									<div id="WeekMenu{week}-friday">
										<xsl:value-of select="friday" />
									</div>
									<div id="WeekMenu{week}-saturday">
										<xsl:value-of select="saturday" />
									</div>
									<div id="WeekMenu{week}-sunday">
										<xsl:value-of select="sunday" />
									</div>
								</xsl:for-each>
							</div>


						</xsl:when>
						<xsl:otherwise>

							<p>
								<xsl:value-of select="$i18n.AddSchoolMenu.NoTemplate" />
							</p>

						</xsl:otherwise>
					</xsl:choose>

					<xsl:call-template name="UpdateSchoolMenuForm">
						<xsl:with-param name="monday" select="WeekMenu/monday" />
						<xsl:with-param name="tuesday" select="WeekMenu/tuesday" />
						<xsl:with-param name="wednesday" select="WeekMenu/wednesday" />
						<xsl:with-param name="thursday" select="WeekMenu/thursday" />
						<xsl:with-param name="friday" select="WeekMenu/friday" />
						<xsl:with-param name="saturday" select="WeekMenu/saturday" />
						<xsl:with-param name="sunday" select="WeekMenu/sunday" />
					</xsl:call-template>

					<div class="floatright">
						<input type="submit" value="{$i18n.Add}" />
					</div>

				</form>

			</xsl:otherwise>
		</xsl:choose>

	</xsl:template>

	<xsl:template match="UpdateSchoolMenu">

		<h1>
			<xsl:value-of select="$i18n.UpdateSchoolMenu.title" />
			<xsl:value-of select="Week" />
		</h1>

		<form method="post" action="{/Document/requestinfo/uri}">

			<xsl:apply-templates select="validationException/validationError" />

			<xsl:call-template name="UpdateSchoolMenuForm">
				<xsl:with-param name="monday" select="SchoolMenu/monday" />
				<xsl:with-param name="tuesday" select="SchoolMenu/tuesday" />
				<xsl:with-param name="wednesday" select="SchoolMenu/wednesday" />
				<xsl:with-param name="thursday" select="SchoolMenu/thursday" />
				<xsl:with-param name="friday" select="SchoolMenu/friday" />
				<xsl:with-param name="saturday" select="SchoolMenu/saturday" />
				<xsl:with-param name="sunday" select="SchoolMenu/sunday" />
			</xsl:call-template>

			<div class="floatright">
				<input type="submit" value="{$i18n.SaveChanges}" />
			</div>

		</form>

	</xsl:template>

	<xsl:template name="UpdateSchoolMenuForm">
		<xsl:param name="monday" />
		<xsl:param name="tuesday" />
		<xsl:param name="wednesday" />
		<xsl:param name="thursday" />
		<xsl:param name="friday" />
		<xsl:param name="saturday" />
		<xsl:param name="sunday" />

		<div class="floatleft full bigmarginbottom">
			<label for="monday-{week}" class="floatleft full">
				<xsl:value-of select="$i18n.Monday" />
			</label>
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'monday'" />
					<xsl:with-param name="name" select="'monday'" />
					<xsl:with-param name="maxlength" select="'255'" />
					<xsl:with-param name="value" select="$monday" />
				</xsl:call-template>
			</div>
		</div>

		<div class="floatleft full bigmarginbottom">
			<label for="tuesday-{week}" class="floatleft full">
				<xsl:value-of select="$i18n.Tuesday" />
			</label>
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'tuesday'" />
					<xsl:with-param name="name" select="'tuesday'" />
					<xsl:with-param name="maxlength" select="'255'" />
					<xsl:with-param name="value" select="$tuesday" />
				</xsl:call-template>
			</div>
		</div>

		<div class="floatleft full bigmarginbottom">
			<label for="wednesday-{week}" class="floatleft full">
				<xsl:value-of select="$i18n.Wednesday" />
			</label>
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'wednesday'" />
					<xsl:with-param name="name" select="'wednesday'" />
					<xsl:with-param name="maxlength" select="'255'" />
					<xsl:with-param name="value" select="$wednesday" />
				</xsl:call-template>
			</div>
		</div>

		<div class="floatleft full bigmarginbottom">
			<label for="thursday-{week}" class="floatleft full">
				<xsl:value-of select="$i18n.Thursday" />
			</label>
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'thursday'" />
					<xsl:with-param name="name" select="'thursday'" />
					<xsl:with-param name="maxlength" select="'255'" />
					<xsl:with-param name="value" select="$thursday" />
				</xsl:call-template>
			</div>
		</div>

		<div class="floatleft full bigmarginbottom">
			<label for="friday-{week}" class="floatleft full">
				<xsl:value-of select="$i18n.Friday" />
			</label>
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'friday'" />
					<xsl:with-param name="name" select="'friday'" />
					<xsl:with-param name="maxlength" select="'255'" />
					<xsl:with-param name="value" select="$friday" />
				</xsl:call-template>
			</div>
		</div>

		<div class="floatleft full bigmarginbottom">
			<label for="saturday-{week}" class="floatleft full">
				<xsl:value-of select="$i18n.Saturday" />
			</label>
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'saturday'" />
					<xsl:with-param name="name" select="'saturday'" />
					<xsl:with-param name="maxlength" select="'255'" />
					<xsl:with-param name="value" select="$saturday" />
				</xsl:call-template>
			</div>
		</div>

		<div class="floatleft full bigmarginbottom">
			<label for="sunday-{week}" class="floatleft full">
				<xsl:value-of select="$i18n.Sunday" />
			</label>
			<div class="floatleft full">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="'sunday'" />
					<xsl:with-param name="name" select="'sunday'" />
					<xsl:with-param name="maxlength" select="'255'" />
					<xsl:with-param name="value" select="$sunday" />
				</xsl:call-template>
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

				<!-- <xsl:choose> -->
				<!-- <xsl:otherwise> -->
				<xsl:value-of select="fieldName" />
				<!-- </xsl:otherwise> -->
				<!-- </xsl:choose> -->

				<xsl:text>!</xsl:text>
			</p>
		</xsl:if>

		<xsl:if test="messageKey">
			<p class="error">
				<!-- <xsl:choose> -->
				<!-- <xsl:otherwise> -->
				<xsl:value-of select="$i18n.ValidationError.UnknownFault" />
				<!-- </xsl:otherwise> -->
				<!-- </xsl:choose> -->
			</p>
		</xsl:if>

	</xsl:template>

</xsl:stylesheet>

