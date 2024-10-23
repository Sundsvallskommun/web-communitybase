<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

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
		/jquery/jquery.js?v=1
	</xsl:variable>

	<xsl:template match="Document">
		
		<script>
			$(function() {
				$("#resume-toggle").on("change", function() {
					$("#hour, #notification-types, #sections").toggle($(this).is(":checked"));
				});
			});
		</script>
		
		<div id="EmailResumeModule" class="contentitem">
			<section>
				<header>
					<h1><xsl:value-of select="$i18n.ResumeSettings.Title"/></h1>
				</header>
			
				<xsl:choose>
					<xsl:when test="UnsupportedAttributeHandler">
						<article>
							<p><xsl:value-of select="$i18n.UnsupportedAttributeHandler"/></p>
						</article>
					</xsl:when>
					<xsl:otherwise>
						<form method="post" action="{CurrentURI}">
							<article>
								<div class="bigmarginbottom">
									<h2><xsl:value-of select="$i18n.Resume"/></h2>
	
									<p class="description">
										<xsl:value-of select="$i18n.ResumeToggleDescription"/>
									</p>
									
									<div class="checkbox-inline">
										<label>
											<input type="checkbox" name="resume" id="resume-toggle" data-size="normal" data-onstyle="success" data-offstyle="secondary" data-toggle="toggle" data-on="{$i18n.On}" data-off="{$i18n.Off}">
												<xsl:if test="Hour">
													<xsl:attribute name="checked">checked</xsl:attribute>
												</xsl:if>
											</input>
											
											<span id="hour">
												<xsl:if test="not(Hour)">
													<xsl:attribute name="style">display: none;</xsl:attribute>
												</xsl:if>
												
												<xsl:call-template name="createDropdown">
													<xsl:with-param name="name" select="'resumeHour'"/>
													<xsl:with-param name="element" select="exsl:node-set($hours)/option"/>
													<xsl:with-param name="labelElementName" select="'name'" />
													<xsl:with-param name="valueElementName" select="'value'" />
													<xsl:with-param name="class" select="'form-control'" />
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
											</span>
										</label>
									</div>
								</div>
									
								<xsl:if test="ModuleNotifications">
									<div id="notification-types" class="bigmarginbottom">
										<xsl:if test="not(Hour)">
											<xsl:attribute name="style">display: none;</xsl:attribute>
										</xsl:if>
	
										<h2><xsl:value-of select="$i18n.SelectNotifications"/></h2>
		
										<p class="description">
											<xsl:value-of select="$i18n.SelectNotificationTypes"/>
										</p>
										
										<xsl:apply-templates select="ModuleNotifications/ModuleNotification"/>
									</div>								
								</xsl:if>
								
								<xsl:if test="Sections">
									<div id="sections" class="bigmarginbottom">
										<xsl:if test="not(Hour)">
											<xsl:attribute name="style">display: none;</xsl:attribute>
										</xsl:if>
	
										<h2><xsl:value-of select="$i18n.SelectSections"/></h2>
		
										<p class="description">
											<xsl:value-of select="$i18n.SelectSectionsDescripton"/>
										</p>
										
										<xsl:apply-templates select="Sections/section"/>
									</div>
								</xsl:if>
							</article>
	
							<footer class="clearfix">
								<button type="submit" class="btn btn-success float-right">
									<i class="icons icon-check"></i>
									<span><xsl:value-of select="$i18n.SaveChanges"/></span>
								</button>
								
								<xsl:if test="RedirectURI">
									<a class="btn btn-danger cancel-btn bigmarginright float-right" href="{RedirectURI}">
										<i class="icons icon-ban"></i>
										<span><xsl:value-of select="$i18n.Cancel" /></span>
									</a>
								</xsl:if>
							</footer>
						</form>
					</xsl:otherwise>
				</xsl:choose>
			</section>
		</div>
		
	</xsl:template>
	
	<xsl:template match="ModuleNotification">
	
		<div>
			<input type="checkbox" class="checkbox" id="module{id}" name="module{id}" value="true">
				<xsl:if test="Checked = 'true'">
					<xsl:attribute name="checked">true</xsl:attribute>
				</xsl:if>
			</input>
			
			<label for="module{id}">
				<em tabindex="0"/>
			</label>
			
			<label for="module{id}">
				<xsl:value-of select="name"/>
			</label>
		</div>
	
	</xsl:template>
	
	<xsl:template match="section">
	
		<div>
			<input type="checkbox" class="checkbox" name="section{sectionID}" id="section{sectionID}" value="true">
				<xsl:if test="Checked = 'true'">
					<xsl:attribute name="checked">true</xsl:attribute>
				</xsl:if>
			</input>
			
			<label for="section{sectionID}">
				<em tabindex="0"/>
			</label>
			
			<label for="section{sectionID}">
				<xsl:value-of select="name"/>
			</label>
		</div>
	
	</xsl:template>	
	
</xsl:stylesheet>