<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common" exclude-result-prefixes="exsl">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	
	<xsl:variable name="globalscripts">
		/jquery/jquery.js?v=1
	</xsl:variable>
	
	<xsl:variable name="scripts">
		/js/emailresumesettings.js
	</xsl:variable>

	<xsl:variable name="hours">
		<option>
			<name>00:00</name>
			<value>0</value>
		</option>
		<option>
			<name>01:00</name>
			<value>1</value>
		</option>
		<option>
			<name>02:00</name>
			<value>2</value>
		</option>
		<option>
			<name>03:00</name>
			<value>3</value>
		</option>
		<option>
			<name>04:00</name>
			<value>4</value>
		</option>
		<option>
			<name>05:00</name>
			<value>5</value>
		</option>
		<option>
			<name>06:00</name>
			<value>6</value>
		</option>
		<option>
			<name>07:00</name>
			<value>7</value>
		</option>
		<option>
			<name>08:00</name>
			<value>8</value>
		</option>
		<option>
			<name>09:00</name>
			<value>9</value>
		</option>
		<option>
			<name>10:00</name>
			<value>10</value>
		</option>
		<option>
			<name>11:00</name>
			<value>11</value>
		</option>
		<option>
			<name>12:00</name>
			<value>12</value>
		</option>
		<option>
			<name>13:00</name>
			<value>13</value>
		</option>
		<option>
			<name>14:00</name>
			<value>14</value>
		</option>
		<option>
			<name>15:00</name>
			<value>15</value>
		</option>
		<option>
			<name>16:00</name>
			<value>16</value>
		</option>
		<option>
			<name>17:00</name>
			<value>17</value>
		</option>
		<option>
			<name>18:00</name>
			<value>18</value>
		</option>
		<option>
			<name>19:00</name>
			<value>19</value>
		</option>
		<option>
			<name>20:00</name>
			<value>20</value>
		</option>
		<option>
			<name>21:00</name>
			<value>21</value>
		</option>
		<option>
			<name>22:00</name>
			<value>22</value>
		</option>
		<option>
			<name>23:00</name>
			<value>23</value>
		</option>
	</xsl:variable>

	<xsl:template match="Document">
		
		<div id="EmailResumeModule" class="contentitem">
			<section>
				<header>
					<h1><xsl:value-of select="$i18n.ResumeSettings.Title"/></h1>
				</header>
			
				<xsl:choose>
					<xsl:when test="UnsupportedAttributeHandler">
						<div class="section-content">
							<p><xsl:value-of select="$i18n.UnsupportedAttributeHandler"/></p>
						</div>
					</xsl:when>
					<xsl:otherwise>
						<form method="post" action="{CurrentURI}">
							<div class="section-content">
								<div class="bigmarginbottom">
									<p>
										<xsl:value-of select="$i18n.ResumeToggleDescription"/>
									</p>
									
									<div class="form-check">
										<xsl:call-template name="createCheckbox">
											<xsl:with-param name="id" select="'resume'"/>
											<xsl:with-param name="name" select="'resume'"/>
											<xsl:with-param name="class" select="'form-check-input'"/>
											<xsl:with-param name="aria-controls" select="'resumeWrapper'"/>
											<xsl:with-param name="checked">
												<xsl:if test="Hour">
													<xsl:text>true</xsl:text>
												</xsl:if>
											</xsl:with-param>
										</xsl:call-template>
										
										<label class="form-check-label" for="resume"><xsl:value-of select="$i18n.ActiveEmailResume"/></label>
									</div>
								</div>
								
								<div id="resumeWrapper">
									<xsl:if test="not(Hour)">
										<xsl:attribute name="style">display: none;</xsl:attribute>
									</xsl:if>
									
									<div class="form-group">
										<label for="resumeHour"><xsl:value-of select="$i18n.ResumeHour"/></label>
										
										<xsl:call-template name="createDropdown">
											<xsl:with-param name="name" select="'resumeHour'"/>
											<xsl:with-param name="id" select="'resumeHour'"/>
											<xsl:with-param name="element" select="exsl:node-set($hours)/option"/>
											<xsl:with-param name="labelElementName" select="'name'" />
											<xsl:with-param name="valueElementName" select="'value'" />
											<xsl:with-param name="class" select="'form-control col-md-4'" />
											<xsl:with-param name="selectedValue">
												<xsl:choose>
													<xsl:when test="Hour">
														<xsl:value-of select="Hour" />
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="DefaultHour" />
													</xsl:otherwise>
												</xsl:choose>
											</xsl:with-param>
										</xsl:call-template>
									</div>
									
									<xsl:if test="ModuleNotifications">
										<fieldset class="form-group mb-3">
											<legend><xsl:value-of select="$i18n.SelectNotificationTypes"/></legend>
											
											<xsl:apply-templates select="ModuleNotifications/ModuleNotification"/>
										</fieldset>
									</xsl:if>
									
									<xsl:if test="Sections">
										<fieldset class="form-group">
											<legend><xsl:value-of select="$i18n.SelectSectionsDescripton"/></legend>
											
											<xsl:apply-templates select="Sections/section"/>
										</fieldset>
									</xsl:if>
								</div>
							</div>
	
							<footer class="d-flex">
								<div class="ml-auto">
									<xsl:if test="RedirectURI">
										<a class="btn btn-danger bigmarginright" href="{RedirectURI}">
											<i class="icons icon-ban" aria-hidden="true"/>
											
											<span><xsl:value-of select="$i18n.Cancel" /></span>
										</a>
									</xsl:if>
									
									<button type="submit" class="btn btn-success">
										<i class="icons icon-check" aria-hidden="true"/>
										
										<span><xsl:value-of select="$i18n.SaveChanges"/></span>
									</button>
								</div>
							</footer>
						</form>
					</xsl:otherwise>
				</xsl:choose>
			</section>
		</div>
		
	</xsl:template>
	
	<xsl:template match="ModuleNotification">
		
		<xsl:variable name="name">
			<xsl:text>module</xsl:text>
			<xsl:value-of select="id"/>
		</xsl:variable>
		
		<div class="form-check">
			<xsl:call-template name="createCheckbox">
				<xsl:with-param name="id" select="$name"/>
				<xsl:with-param name="name" select="$name"/>
				<xsl:with-param name="class" select="'form-check-input'"/>
				<xsl:with-param name="checked">
					<xsl:if test="Checked = 'true'">
						<xsl:text>true</xsl:text>
					</xsl:if>
				</xsl:with-param>
			</xsl:call-template>
			
			<label class="form-check-label" for="{$name}"><xsl:value-of select="name"/></label>
		</div>
	
	</xsl:template>
	
	<xsl:template match="section">
		
		<xsl:variable name="name">
			<xsl:text>section</xsl:text>
			<xsl:value-of select="sectionID"/>
		</xsl:variable>
		
		<div class="form-check">
			<xsl:call-template name="createCheckbox">
				<xsl:with-param name="id" select="$name"/>
				<xsl:with-param name="name" select="$name"/>
				<xsl:with-param name="class" select="'form-check-input'"/>
				<xsl:with-param name="checked">
					<xsl:if test="Checked = 'true'">
						<xsl:text>true</xsl:text>
					</xsl:if>
				</xsl:with-param>
			</xsl:call-template>
			
			<label class="form-check-label" for="{$name}"><xsl:value-of select="name"/></label>
		</div>
	
	</xsl:template>	
	
</xsl:stylesheet>