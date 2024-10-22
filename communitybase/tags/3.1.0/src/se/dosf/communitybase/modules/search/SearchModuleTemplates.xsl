<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:exsl="http://exslt.org/common">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:include href="classpath://se/dosf/communitybase/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/searchmodule.js
	</xsl:variable>
	
	<!-- TODO generate dynamically -->
	
	<xsl:variable name="types">
		<option>
			<name><xsl:value-of select="$i18n.Users"/></name>
			<value>users</value>
		</option>
		<option>
			<name><xsl:value-of select="$i18n.Sections"/></name>
			<value>sections</value>
		</option>
		<option>
			<name><xsl:value-of select="$i18n.Files"/></name>
			<value>file</value>
		</option>
		<option>
			<name><xsl:value-of select="$i18n.Tasks"/></name>
			<value>task</value>
		</option>
		<option>
			<name><xsl:value-of select="$i18n.Posts"/></name>
			<value>post</value>
		</option>
		<option>
			<name><xsl:value-of select="$i18n.Calendar"/></name>
			<value>calendar</value>
		</option>
		<option>
			<name><xsl:value-of select="$i18n.Pages"/></name>
			<value>page</value>
		</option>
		<option>
			<name><xsl:value-of select="$i18n.Tags"/></name>
			<value>tags</value>
		</option>
		<option>
			<name><xsl:value-of select="$i18n.TaggedContent"/></name>
			<value>tag</value>
		</option>	
	</xsl:variable>	
	
	<xsl:template match="Document">
		
		<div id="SearchModule" class="contentitem of-module">
			
			<xsl:apply-templates select="Search" />
			
		</div>
					
	</xsl:template>
	
	<xsl:template match="Search">
	
		<div class="of-block" id="searchwrapper">
			
			<form id="search" method="get" action="{/Document/requestinfo/uri}">
			
				<div class="of-inner-padded-trl of-inner-padded-b-half">
					<div class="of-right">
						<button class="of-btn of-btn-gronsta">
							<xsl:value-of select="$i18n.Search"/>
						</button>
					</div>
					<div class="of-overflow">
						<input class="of-searchfield of-no-margin" type="text" name="q" value="{Query}"/>
						<a class="of-close of-icon of-icon-only" href="#">
							<i>
								<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 512 512">
									<use xlink:href="#close"/>
								</svg>
							</i>
							<span><xsl:value-of select="$i18n.Close"/></span>
						</a>
					</div>
				</div>
				<div>
					<div class="of-inner-padded-rl">
						
						<xsl:call-template name="createOFDropdown">
							<xsl:with-param name="id" select="'typeSelector'"/>
							<xsl:with-param name="name" select="'t'"/>
							<xsl:with-param name="element" select="exsl:node-set($types)/option"/>
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="valueElementName" select="'value'" />
							<xsl:with-param name="addEmptyOption" select="$i18n.AllResults"/>
							<xsl:with-param name="selectedValue" select="Type" />					
						</xsl:call-template>
											
						<!--
						
						TODO not implemented yet
						
						<select data-of-select="inline">
							<option>Alla Samarbetsrum</option>
							<optgroup label="Favoriter">
								<option value="0">Jimmy samarbetsrum</option>
								<option value="1">Interna todos</option>
							</optgroup>
							<optgroup label="Mina samarbetsrum">
								<option value="2">RIGES - Upphandling för informationssäkerhet</option>
								<option value="3">Arbetsmiljö och hälsa</option>
								<option value="4">Barn- och utbildningsförvaltningen</option>
							</optgroup>
							<optgroup label="Öppna samarbetsrum">
								<option value="5">Intressegruppen för IT-drift</option>
							</optgroup>
						</select> 
						-->
					</div>
				</div>
			
			</form>
			
			<xsl:if test="Query and not(UserHits) and not(SectionHits) and not(ContentHits) and not(TagHits)">
			
				<div class="of-inner-padded">
					<h4><xsl:value-of select="$i18n.NoHits"/></h4>
				</div>
			
			</xsl:if>
			
			<article>
			
				<xsl:if test="UserHits">
				
					<xsl:variable name="userCount" select="count(UserHits/user)"/>
				
					<div class="of-inner-padded-trl">
						<span class="of-results">
							
							<xsl:value-of select="$i18n.Showing"/>
							<xsl:text>&#160;</xsl:text>
							
							<xsl:choose>
								<xsl:when test="$userCount > 4">
									<xsl:text>4</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$userCount"/>
								</xsl:otherwise>
							</xsl:choose>
							
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$i18n.of"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$userCount"/>
							
						</span>
						<h3><xsl:value-of select="$i18n.Users"/></h3>
					</div>				
				
					<div>
						<ul class="of-row-list of-inner-padded-rl of-inner-padded-t-half">
							<xsl:apply-templates select="UserHits/user"/>
						</ul>
						
						<xsl:if test="$userCount > 4">
							<div class="of-inner-padded-bl" id="show-more-user" onclick="return show('user')">
								<a class="of-btn of-btn-vattjom of-btn-outline of-btn-inline" href="#">
									<xsl:value-of select="$i18n.ShowMoreUsers"/>
								</a>
							</div>
							
							<div class="of-inner-padded-bl" id="show-less-user" style="display:none;" onclick="return hide('user')">
								<a class="of-btn of-btn-vattjom of-btn-outline of-btn-inline" href="#">
									<xsl:value-of select="$i18n.ShowLessUsers"/>
								</a>
							</div>													
						</xsl:if>
					</div>
				
				</xsl:if>
			
				<xsl:if test="SectionHits">
				
					<xsl:variable name="sectionCount" select="count(SectionHits/SectionHit)"/>
				
					<div class="of-inner-padded-trl">
						<span class="of-results">
							
							<xsl:value-of select="$i18n.Showing"/>
							<xsl:text>&#160;</xsl:text>
							
							<xsl:choose>
								<xsl:when test="$sectionCount > 4">
									<xsl:text>4</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$sectionCount"/>
								</xsl:otherwise>
							</xsl:choose>
							
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$i18n.of"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$sectionCount"/>
							
						</span>
						<h3><xsl:value-of select="$i18n.Sections"/></h3>
					</div>				
				
					<div>
						<ul class="of-row-list of-inner-padded-rl of-inner-padded-t-half">
							<xsl:apply-templates select="SectionHits/SectionHit"/>
						</ul>
						
						<xsl:if test="$sectionCount > 4">
							<div class="of-inner-padded-bl" id="show-more-section" onclick="return show('section')">
								<a class="of-btn of-btn-vattjom of-btn-outline of-btn-inline" href="#">
									<xsl:value-of select="$i18n.ShowMoreSections"/>
								</a>
							</div>
							
							<div class="of-inner-padded-bl" id="show-less-section" style="display:none;" onclick="return hide('section')">
								<a class="of-btn of-btn-vattjom of-btn-outline of-btn-inline" href="#">
									<xsl:value-of select="$i18n.ShowLessSections"/>
								</a>
							</div>													
						</xsl:if>
					</div>					
				
				</xsl:if>
			
				<xsl:call-template name="appendContentHits">
					<xsl:with-param name="type" select="'file'"/>
					<xsl:with-param name="name" select="$i18n.Files"/>
					<xsl:with-param name="nameLowerCase" select="$i18n.filer"/>
					<xsl:with-param name="iconName" select="'file'"/>
				</xsl:call-template>
			
				<xsl:call-template name="appendContentHits">
					<xsl:with-param name="type" select="'task'"/>
					<xsl:with-param name="name" select="$i18n.Tasks"/>
					<xsl:with-param name="nameLowerCase" select="$i18n.tasks"/>
					<xsl:with-param name="iconName" select="'checkmark'"/>
				</xsl:call-template>
				
				<xsl:call-template name="appendContentHits">
					<xsl:with-param name="type" select="'post'"/>
					<xsl:with-param name="name" select="$i18n.Posts"/>
					<xsl:with-param name="nameLowerCase" select="$i18n.posts"/>
					<xsl:with-param name="iconName" select="'posts'"/>
				</xsl:call-template>
				
				<xsl:call-template name="appendContentHits">
					<xsl:with-param name="type" select="'page'"/>
					<xsl:with-param name="name" select="$i18n.Pages"/>
					<xsl:with-param name="nameLowerCase" select="$i18n.pages"/>
					<xsl:with-param name="iconName" select="'file'"/>
				</xsl:call-template>
				
				<xsl:call-template name="appendContentHits">
					<xsl:with-param name="type" select="'calendar'"/>
					<xsl:with-param name="name" select="$i18n.Calendar"/>
					<xsl:with-param name="nameLowerCase" select="$i18n.calendarPosts"/>
					<xsl:with-param name="iconName" select="'calendar'"/>
				</xsl:call-template>							
			
				<xsl:if test="TagHits">
				
					<xsl:variable name="tagCount" select="count(TagHits/Tag)"/>
				
					<div class="of-inner-padded-trl">
						<span class="of-results">
							
							<xsl:value-of select="$i18n.Showing"/>
							<xsl:text>&#160;</xsl:text>
							
							<xsl:choose>
								<xsl:when test="$tagCount > 4">
									<xsl:text>4</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$tagCount"/>
								</xsl:otherwise>
							</xsl:choose>
							
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$i18n.of"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$tagCount"/>
							
						</span>
						<h3><xsl:value-of select="$i18n.Tags"/></h3>
					</div>				
				
					<div>
						<ul class="of-row-list of-inner-padded-rl of-inner-padded-t-half">
							<xsl:apply-templates select="TagHits/Tag"/>
						</ul>
						
						<xsl:if test="$tagCount > 4">
							<div class="of-inner-padded-bl" id="show-more-tag" onclick="return show('tag')">
								<a class="of-btn of-btn-vattjom of-btn-outline of-btn-inline" href="#">
									<xsl:value-of select="$i18n.ShowMoreTags"/>
								</a>
							</div>
							
							<div class="of-inner-padded-bl" id="show-less-tag"  style="display:none;" onclick="return hide('tag')">
								<a class="of-btn of-btn-vattjom of-btn-outline of-btn-inline" href="#">
									<xsl:value-of select="$i18n.ShowLessTags"/>
								</a>
							</div>													
						</xsl:if>
					</div>					
				
				</xsl:if>			
			
			</article>
		</div>			
	
	</xsl:template>
	
	<xsl:template match="user">
	
		<li>
			<xsl:if test="position() > 4">
				<xsl:attribute name="style">display:none;</xsl:attribute>
				<xsl:attribute name="class">toggle-user</xsl:attribute>
			</xsl:if>
		
			<figure class="of-profile of-figure-lg">
				
				<xsl:if test="/Document/ProfileImageAlias">
					<img alt="{firstname} {lastname}" src="{/Document/requestinfo/contextpath}{/Document/ProfileImageAlias}/{userID}" />
				</xsl:if>
				
			</figure>
			<article>
				<xsl:choose>
					<xsl:when test="/Document/ShowProfileAlias">
					
						<a href="{/Document/requestinfo/contextpath}{/Document/ShowProfileAlias}/{userID}">
							<h4><xsl:value-of select="firstname" /><xsl:text>&#160;</xsl:text><xsl:value-of select="lastname" /></h4>
						</a>					
					
					</xsl:when>
					<xsl:otherwise>
					
						<h4><xsl:value-of select="firstname" /><xsl:text>&#160;</xsl:text><xsl:value-of select="lastname" /></h4>
					
					</xsl:otherwise>
				</xsl:choose>

				<xsl:if test="Attributes/Attribute[Name = 'description']">
					<p><xsl:value-of select="Attributes/Attribute[Name = 'description']/Value"/></p>
				</xsl:if>

				<xsl:if test="Attributes/Attribute[Name = 'organization'] or Attributes/Attribute[Name = 'phone']">
				
					<ul class="of-meta-line">
					
						<xsl:if test="Attributes/Attribute[Name = 'organization']">
							<li>
								<xsl:value-of select="Attributes/Attribute[Name = 'organization']/Value"/>
							</li>
						</xsl:if>
					
						<xsl:if test="Attributes/Attribute[Name = 'phone']">
							<li>
								<xsl:value-of select="Attributes/Attribute[Name = 'phone']/Value"/>
							</li>
						</xsl:if>					
					
					</ul>
				
				</xsl:if>

			</article>
		</li>	
	
	</xsl:template>
	
	<xsl:template match="SectionHit">
	
		<li>
			<xsl:if test="position() > 4">
				<xsl:attribute name="style">display:none;</xsl:attribute>
				<xsl:attribute name="class">toggle-section</xsl:attribute>
			</xsl:if>
		
			<figure class="of-badge-vattjom of-room of-figure-lg">
				<xsl:if test="/Document/SectionLogoURI">
					<img alt="{Name}" src="{/Document/SectionLogoURI}/sectionlogo/{SectionID}" />
				</xsl:if>
			</figure>
			<article>
				<div>
					<a href="{/Document/requestinfo/contextpath}{FullAlias}">
						<h2>
							<xsl:value-of select="Name"/>
						</h2>
					</a>
				</div>
				
				<xsl:if test="Fragment">
					<p>
						<xsl:value-of select="Fragment" disable-output-escaping="yes"/>
					</p>
				</xsl:if>
				
				<xsl:if test="MemberCount or AccessMode != 'OPEN'">
				
					<ul class="of-meta-line">
					
						<xsl:if test="AccessMode and AccessMode != 'OPEN'">
							<li class="of-icon">
								<xsl:choose>
									<xsl:when test="AccessMode = 'CLOSED'">
										<i>
											<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 512 512">
												<use xlink:href="#lock"/>
											</svg>
										</i>
										
										<span><xsl:value-of select="$i18n.Closed"/></span>									
									</xsl:when>
									<xsl:when test="AccessMode = 'HIDDEN'">
										<i>
											<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 512 512">
												<use xlink:href="#hidden"/>
											</svg>
										</i>
										
										<span><xsl:value-of select="$i18n.Hidden"/></span>										
									</xsl:when>
								</xsl:choose>
							</li>
						</xsl:if>
					
						<xsl:if test="MemberCount">
							<li>
								<xsl:value-of select="MemberCount"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$i18n.members"/>
							</li>
						</xsl:if>					
					
					</ul>
				
				</xsl:if>
				
			</article>
		</li>	
	
	</xsl:template>
	
	<xsl:template name="appendContentHits">
	
		<xsl:param name="type"/>
		<xsl:param name="name"/>
		<xsl:param name="nameLowerCase"/>
		<xsl:param name="iconName"/>
	
		<xsl:variable name="hitCount" select="count(ContentHits/ContentHit[Type=$type])"/>
	
		<xsl:if test="$hitCount > 0">
			
				<div class="of-inner-padded-trl">
					<span class="of-results">
						
						<xsl:value-of select="$i18n.Showing"/>
						<xsl:text>&#160;</xsl:text>
						
						<xsl:choose>
							<xsl:when test="$hitCount > 4">
								<xsl:text>4</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$hitCount"/>
							</xsl:otherwise>
						</xsl:choose>
						
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$i18n.of"/>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$hitCount"/>
						
					</span>
					<h3><xsl:value-of select="$name"/></h3>
				</div>				
			
				<div>
					<ul class="of-row-list of-inner-padded-rl of-inner-padded-t-half">
						<xsl:apply-templates select="ContentHits/ContentHit[Type=$type]">
							<xsl:with-param name="iconName" select="$iconName"/>
						</xsl:apply-templates>
					</ul>	
				
					
					<xsl:if test="$hitCount > 4">
						<div class="of-inner-padded-bl" id="show-more-{$type}" onclick="return show('{$type}')">
							<a class="of-btn of-btn-vattjom of-btn-outline of-btn-inline" href="#">
								<xsl:value-of select="$i18n.ShowMore"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$nameLowerCase"/>
							</a>
						</div>
						
						<div class="of-inner-padded-bl" id="show-less-{$type}" style="display:none;" onclick="return hide('{$type}')">
							<a class="of-btn of-btn-vattjom of-btn-outline of-btn-inline" href="#">
								<xsl:value-of select="$i18n.ShowLess"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$nameLowerCase"/>								
							</a>
						</div>													
					</xsl:if>
				</div>					
			
			</xsl:if>			
	
	</xsl:template>
		
	<xsl:template match="ContentHit">
	
		<xsl:param name="iconName"/>
		
		<li>
		
			<xsl:if test="position() > 4">
				<xsl:attribute name="style">display:none;</xsl:attribute>
				<xsl:attribute name="class">toggle-<xsl:value-of select="Type"/></xsl:attribute>
			</xsl:if>		
			<article>
				<a href="{/Document/requestinfo/contextpath}{SectionAlias}{Alias}">
					<h4 class="of-icon">
					
						<i>
							<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 512 512">
								<use xlink:href="#{$iconName}"/>
							</svg>
						</i>
							
						<xsl:text>&#160;</xsl:text>
								
						<span>
							<xsl:value-of select="Title"/>
							<xsl:text>&#160;</xsl:text>
							<i class="of-inline-arrow">
								<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 512 512">
									<use xlink:href="#arrow-right"/>
								</svg>
							</i>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="SectionName"/>
						</span>
					</h4>
				</a>
				
				<xsl:if test="Fragment">
					<p>
						<xsl:value-of select="Fragment" disable-output-escaping="yes"/>
					</p>
				</xsl:if>
				
				<xsl:if test="InfoLine">
				
					<ul class="of-meta-line">
						<li><xsl:value-of select="InfoLine"/></li>
					</ul>
				
				</xsl:if>
				
			</article>
		</li>
	
	</xsl:template>
	
	<xsl:template match="Tag">
	
		<li>
			<xsl:if test="position() > 4">
				<xsl:attribute name="style">display:none;</xsl:attribute>
				<xsl:attribute name="class">toggle-tag</xsl:attribute>
			</xsl:if>			
		
			<a href="{/Document/requestinfo/uri}?t=tag&amp;q={.}">
				<h4>
					<xsl:text>#</xsl:text>
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="."/>
				</h4>
			</a>
		</li>	
	
	</xsl:template>
		
</xsl:stylesheet>