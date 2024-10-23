<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>
	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.xsl"/>

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

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/resumesettingsmodule.js
	</xsl:variable>
	
	<xsl:variable name="links">
		/css/resumesettingsmodule.css
	</xsl:variable>

	<xsl:template match="Document">
		
		<div id="EmailResumeModule" class="contentitem of-module">
			
			<div class="of-block">
				<header class="of-inner-padded-trl">
					<h1><xsl:value-of select="$i18n.ResumeSettings.Title"/></h1>
				</header>
			
				<xsl:choose>
					<xsl:when test="UnsupportedAttributeHandler">
						
						<p><xsl:value-of select="$i18n.UnsupportedAttributeHandler"/></p>
						
					</xsl:when>
					<xsl:otherwise>
					
						<form method="post" action="{CurrentURI}">
	
							<article class="of-inner-padded">
					
								<div>
								
									<h3>Resumé</h3>
	
									<label class="of-block-label">
										<span class="description"><xsl:value-of select="$i18n.ResumeToggleDescription"/></span>
									</label>
									
									<label class="of-checkbox-label of-left bigmarginright">
										<select name="resume" data-of-select="toggle" id="resume-toggle">
											<option value="0"><xsl:value-of select="$i18n.Off"/></option>
											<option value="1">
											
												<xsl:if test="Hour">
													<xsl:attribute name="selected">true</xsl:attribute>
												</xsl:if>
											
												<xsl:value-of select="$i18n.On"/>
											</option>
										</select>
									</label>								
								
									<label class="of-checkbox-label of-left" id="hour">
										
										<xsl:if test="not(Hour)">
											<xsl:attribute name="style">display: none;</xsl:attribute>
										</xsl:if>
										
										<xsl:call-template name="createOFDropdown">
											<xsl:with-param name="name" select="'resumeHour'"/>
											<xsl:with-param name="element" select="exsl:node-set($hours)/option"/>
											<xsl:with-param name="labelElementName" select="'name'" />
											<xsl:with-param name="valueElementName" select="'value'" />
											
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
																		
									</label>								
								
								</div>
									
								<xsl:if test="ModuleNotifications">
								
									<div class="of-inner-padded-t clearboth" id="notification-types">
	
										<xsl:if test="not(Hour)">
											<xsl:attribute name="style">display: none;</xsl:attribute>
										</xsl:if>
	
										<h3><xsl:value-of select="$i18n.SelectNotifications"/></h3>
		
										<label class="of-block-label">
											<span class="description"><xsl:value-of select="$i18n.SelectNotificationTypes"/></span>
										</label>
										
										<xsl:apply-templates select="ModuleNotifications/ModuleNotification"/>
									
									</div>								
								
								</xsl:if>
								
								<xsl:if test="Sections">
								
									<div class="of-inner-padded-t clearboth" id="sections">
	
										<xsl:if test="not(Hour)">
											<xsl:attribute name="style">display: none;</xsl:attribute>
										</xsl:if>
	
										<h3><xsl:value-of select="$i18n.SelectSections"/></h3>
		
										<label class="of-block-label">
											<span class="description"><xsl:value-of select="$i18n.SelectSectionsDescripton"/></span>
										</label>
										
										<xsl:apply-templates select="Sections/section"/>
									
									</div>										
								
								</xsl:if>
								
							</article>
	
							<footer class="of-no-bg of-text-right of-inner-padded">
								<button type="submit" class="of-btn of-btn-inline of-btn-gronsta">Spara ändringar</button>
								
								<xsl:if test="RedirectURI">
									<span class="of-btn-link">
										<xsl:text> </xsl:text>
										<xsl:value-of select="$i18n.or"/>
										<xsl:text> </xsl:text>
										<a href="{RedirectURI}">
											<xsl:value-of select="$i18n.cancel"/>
										</a>
									</span>								
								</xsl:if>
							</footer>
	
						</form>										
					
					</xsl:otherwise>
				</xsl:choose>
			
			</div>
		</div>
		
	</xsl:template>
	
	<xsl:template match="ModuleNotification">
	
		<label class="of-checkbox-label">
			<input type="checkbox" name="module{id}" value="true">
				<xsl:if test="Checked = 'true'">
					<xsl:attribute name="checked">true</xsl:attribute>
				</xsl:if>
			</input>
			<em class="of-checkbox" tabindex="0"/>
			<span>
				<xsl:value-of select="name"/>
			</span>
		</label>	
	
	</xsl:template>
	
	<xsl:template match="section">
	
		<label class="of-checkbox-label">
			<input type="checkbox" name="section{sectionID}" value="true">
				<xsl:if test="Checked = 'true'">
					<xsl:attribute name="checked">true</xsl:attribute>
				</xsl:if>
			</input>
			<em class="of-checkbox" tabindex="0"/>
			<span>
				<xsl:value-of select="name"/>
			</span>
		</label>	
	
	</xsl:template>	
	
</xsl:stylesheet>