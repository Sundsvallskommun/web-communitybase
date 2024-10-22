<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />
	
	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js?v=1
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/searchmodule.js?v=2
	</xsl:variable>
	
	<xsl:template match="Document">
		
		<xsl:choose>
			<xsl:when test="Search">
				<div id="SearchModule" class="container">
			
					<xsl:apply-templates select="Search" />
					
				</div>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="PluginSearch"/>
				<xsl:apply-templates select="PluginJson"/>
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	
	<xsl:template match="PluginSearch">
		
		<xsl:apply-templates select="SectionHits" mode="search"/>
		<xsl:apply-templates select="TagHits" mode="search"/>
		<xsl:apply-templates select="ContentHits" mode="search"/>
		<xsl:apply-templates select="UserHits" mode="search"/>
		
	</xsl:template>
	
	<xsl:template match="PluginJson">
		
		<xsl:apply-templates select="SectionHits" mode="json"/>
		<xsl:apply-templates select="TagHits" mode="json"/>
		<xsl:apply-templates select="ContentHits" mode="json"/>
		<xsl:apply-templates select="UserHits" mode="json"/>
		
	</xsl:template>
	
	
	
	<xsl:template match="SectionHits" mode="search">
		
		<div class="col-lg-6">
			<xsl:variable name="sectionCount" select="count(SectionHit)"/>
			
			<div class="contentitem">
				<section>
					<header class="d-flex">
						<h2><xsl:value-of select="../PluginName"/></h2>
						
						<span class="ml-auto">
							<xsl:value-of select="$i18n.Showing"/>
							<xsl:text>&#160;</xsl:text>
							
							<span class="show-more-text">
								<xsl:choose>
									<xsl:when test="$sectionCount > 5">
										<xsl:text>5</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$sectionCount"/>
									</xsl:otherwise>
								</xsl:choose>
							</span>
							
							<span class="show-less-text" style="display: none;">
								<xsl:value-of select="$sectionCount"/>
							</span>
							
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$i18n.of"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$sectionCount"/>
						</span>
					</header>
					
					<div class="section-content">
						<ul class="no-styles">
							<xsl:apply-templates select="SectionHit" mode="search"/>
						</ul>
					</div>
					
					<xsl:if test="$sectionCount > 5">
						<footer>
							<a class="show-more" href="#">
								<xsl:value-of select="$i18n.ShowMoreSections"/>
							</a>
						
							<a class="show-less" style="display: none;" href="#">
								<xsl:value-of select="$i18n.ShowLessSections"/>
							</a>
						</footer>
					</xsl:if>
				</section>
			</div>
		</div>
		
	</xsl:template>
	
	<xsl:template match="SectionHit" mode="search">
	
		<li class="mb-1 d-flex">
			<xsl:if test="position() > 5">
				<xsl:attribute name="style">display: none!important;</xsl:attribute>
				<xsl:attribute name="class">mb-1 d-flex toggle-item</xsl:attribute>
			</xsl:if>
		
			<figure class="group medium mr-2" aria-hidden="true">
				<xsl:if test="../../SectionLogoURI">
					<img alt="{Name}" src="{../../SectionLogoURI}/sectionlogo/{SectionID}"/>
				</xsl:if>
			</figure>
			
			<div>
				<a href="{../../ContextPath}{FullAlias}">
					<xsl:value-of select="Name"/>
				</a>
				
				<xsl:if test="Fragment">
					<div class="small">
						<xsl:value-of select="Fragment" disable-output-escaping="yes"/>
					</div>
				</xsl:if>
				
				<xsl:if test="MemberCount or AccessMode != 'OPEN'">
					<div class="inline-box">
						<xsl:if test="AccessMode and AccessMode != 'OPEN'">
							<div>
								<xsl:choose>
									<xsl:when test="AccessMode = 'CLOSED'">
										<span aria-label="{$i18n.SecrecyLevel}: {$i18n.Closed}">
											<i class="icons icon-lock" aria-hidden="true"/>
										</span>
									</xsl:when>
									<xsl:when test="AccessMode = 'HIDDEN'">
										<span aria-label="{$i18n.SecrecyLevel}: {$i18n.Hidden}">
											<i class="icons icon-eye-slash" aria-hidden="true"/>
										</span>
									</xsl:when>
									<xsl:otherwise>
										<span aria-label="{$i18n.SecrecyLevel}: {$i18n.Open}">
											<i class="icons icon-eye" aria-hidden="true"/>
										</span>
									</xsl:otherwise>
								</xsl:choose>
							</div>
						</xsl:if>
					
						<xsl:if test="MemberCount">
							<div>
								<xsl:value-of select="MemberCount"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$i18n.members"/>
							</div>
						</xsl:if>					
					</div>
				</xsl:if>
			</div>
		</li>
	
	</xsl:template>
	
	<xsl:template match="SectionHits" mode="json">
		
		<div class="col-lg-6">
			<div class="contentitem">
				<section>
					<header>
						<h2><xsl:value-of select="../PluginName"/></h2>
					</header>
					
					<div class="section-content">
						<ul class="no-styles">
							<xsl:apply-templates select="SectionHit" mode="json"/>
						</ul>
					</div>
				</section>
			</div>
		</div>
		
	</xsl:template>
	
	<xsl:template match="SectionHit" mode="json">
		
		<li class="d-flex mb-1">
			<figure class="group medium mr-2" aria-hidden="true">
				<img alt="{Name}" src="{../../SectionLogoURI}/sectionlogo/{SectionID}"/>
			</figure>
			
			<div>
				<div>
					<a href="{../../ContextPath}{FullAlias}">
						<xsl:value-of select="Name"/>
					</a>
				</div>
				
				<xsl:if test="MemberCount or AccessMode != 'OPEN'">
					<div class="inline-box">
						<xsl:if test="AccessMode and AccessMode != 'OPEN'">
							<div>
								<xsl:choose>
									<xsl:when test="AccessMode = 'CLOSED'">
										<span aria-label="{$i18n.SecrecyLevel}: {$i18n.Closed}">
											<i class="icons icon-lock" aria-hidden="true"/>
										</span>
									</xsl:when>
									<xsl:when test="AccessMode = 'HIDDEN'">
										<span aria-label="{$i18n.SecrecyLevel}: {$i18n.Hidden}">
											<i class="icons icon-eye-slash" aria-hidden="true"/>
										</span>
									</xsl:when>
									<xsl:otherwise>
										<span aria-label="{$i18n.SecrecyLevel}: {$i18n.Open}">
											<i class="icons icon-eye" aria-hidden="true"/>
										</span>
									</xsl:otherwise>
								</xsl:choose>
							</div>
						</xsl:if>
					
						<xsl:if test="MemberCount">
							<div>
								<xsl:value-of select="MemberCount"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$i18n.members"/>
							</div>
						</xsl:if>					
					</div>
				</xsl:if>
			</div>
		</li>
		
	</xsl:template>

	
	
	<xsl:template match="TagHits" mode="search">
	
		<div class="col-lg-6">
			<xsl:variable name="tagCount" select="count(TagHit)"/>
			
			<div class="contentitem">
				<section>
					<header class="d-flex">
						<h2><xsl:value-of select="../PluginName"/></h2>
						
						<span class="ml-auto">
							<xsl:value-of select="$i18n.Showing"/>
							<xsl:text>&#160;</xsl:text>
							
							<span class="show-more-text">
								<xsl:choose>
									<xsl:when test="$tagCount > 5">
										<xsl:text>5</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$tagCount"/>
									</xsl:otherwise>
								</xsl:choose>
							</span>
							
							<span class="show-less-text" style="display: none;">
								<xsl:value-of select="$tagCount"/>
							</span>
							
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$i18n.of"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$tagCount"/>
						</span>
					</header>
					
					<div class="section-content">
						<ul class="no-styles">
							<xsl:apply-templates select="TagHit" mode="search"/>
						</ul>
					</div>
					
					<xsl:if test="$tagCount > 5">
						<footer>
							<a class="show-more" href="#">
								<xsl:value-of select="$i18n.ShowMoreTags"/>
							</a>
							
							<a class="show-less" style="display: none;" href="#">
								<xsl:value-of select="$i18n.ShowLessTags"/>
							</a>
						</footer>
					</xsl:if>
				</section>
			</div>
		</div>
		
	</xsl:template>
	
	<xsl:template match="TagHit" mode="search">
	
		<li class="mb-1">
			<xsl:if test="position() > 5">
				<xsl:attribute name="style">display: none!important;</xsl:attribute>
				<xsl:attribute name="class">mb-1 toggle-item</xsl:attribute>
			</xsl:if>
			
			<i class="icons icon-tag mr-2" aria-hidden="true"/>
		
			<a href="{../../SearchModuleURI}?t=tag&amp;q={.}">
				<xsl:text>#</xsl:text>
				<xsl:value-of select="."/>
			</a>
		</li>	
	
	</xsl:template>
	
	<xsl:template match="TagHits" mode="json">
		
		<div class="col-lg-6">
			<div class="contentitem">
				<section>
					<header>
						<h2><xsl:value-of select="../PluginName"/></h2>
					</header>
					
					<div class="section-content">
						<ul class="no-styles">
							<xsl:apply-templates select="TagHit" mode="json"/>
						</ul>
					</div>
				</section>
			</div>
		</div>
		
	</xsl:template>
	
	<xsl:template match="TagHit" mode="json">
		
		<li class="mb-1">
			<i class="icons icon-tag mr-2" aria-hidden="true"/>
			
			<a href="{../../SearchModuleURI}?t=tag&amp;q={.}">
				<xsl:text>#</xsl:text>
				<xsl:value-of select="."/>
			</a>
		</li>
	
	</xsl:template>
	
	
	
	
	<xsl:template match="ContentHits" mode="search">
		
		<div class="col-lg-6">
			<xsl:variable name="hitCount" select="count(ContentHit)"/>
			
			<div class="contentitem">
				<section>
					<header class="d-flex">
						<h2><xsl:value-of select="../PluginName"/></h2>
						
						<span class="ml-auto">
							<xsl:value-of select="$i18n.Showing"/>
							<xsl:text>&#160;</xsl:text>
							
							<span class="show-more-text">
								<xsl:choose>
									<xsl:when test="$hitCount > 5">
										<xsl:text>5</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$hitCount"/>
									</xsl:otherwise>
								</xsl:choose>
							</span>
							
							<span class="show-less-text" style="display: none;">
								<xsl:value-of select="$hitCount"/>
							</span>
							
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$i18n.of"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$hitCount"/>
						</span>
					</header>
					
					<div class="section-content">
						<ul class="no-styles">
							<xsl:apply-templates select="ContentHit" mode="search"/>
						</ul>
					</div>
					
					<xsl:if test="$hitCount > 5">
						<footer>
							<a class="show-more" href="#">
								<xsl:value-of select="$i18n.ShowMore"/>
							</a>
						
							<a class="show-less" style="display: none;" href="#">
								<xsl:value-of select="$i18n.ShowLess"/>
							</a>
						</footer>
					</xsl:if>
				</section>
			</div>
		</div>
		
	</xsl:template>
	
	<xsl:template match="ContentHit" mode="search">
	
		<xsl:variable name="iconName">
			<xsl:choose>
				<xsl:when test="../../Alias = 'file'">
					<xsl:text>file</xsl:text>
				</xsl:when>
				<xsl:when test="../../Alias = 'task'">
					<xsl:text>check</xsl:text>
				</xsl:when>
				<xsl:when test="../../Alias = 'post'">
					<xsl:text>comment</xsl:text>
				</xsl:when>
				<xsl:when test="../../Alias = 'page'">
					<xsl:text>page</xsl:text>
				</xsl:when>
				<xsl:when test="../../Alias = 'calendar'">
					<xsl:text>calendar</xsl:text>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		
		<li class="mb-1">
			<xsl:if test="position() > 5">
				<xsl:attribute name="style">display: none!important;</xsl:attribute>
				<xsl:attribute name="class">mb-1 toggle-item</xsl:attribute>
			</xsl:if>
			
			<div>
				<i class="icons icon-{$iconName} mr-2" aria-hidden="true"/>
				
				<a href="{../../ContextPath}{SectionAlias}{Alias}">
					<span>
						<xsl:value-of select="Title"/>
					</span>
					
					<i class="icons icon-chevron-right" aria-hidden="true"/>
					
					<span>
						<xsl:value-of select="SectionName"/>
					</span>
				</a>
			</div>
				
			<xsl:if test="Fragment">
				<div class="small">
					<xsl:value-of select="Fragment" disable-output-escaping="yes"/>
				</div>
			</xsl:if>
			
			<xsl:if test="InfoLine">
				<div class="inline-box">
					<div><xsl:value-of select="InfoLine"/></div>
				</div>
			</xsl:if>
		</li>
	
	</xsl:template>
	
	<xsl:template match="ContentHits" mode="json">
		
		<div class="col-lg-6">
			<div class="contentitem">
				<section>
					<header>
						<h2><xsl:value-of select="../PluginName"/></h2>
					</header>
					
					<div class="section-content">
						<ul class="no-styles">
							<xsl:apply-templates select="ContentHit" mode="json"/>
						</ul>
					</div>
				</section>
			</div>
		</div>
		
	</xsl:template>
	
	<xsl:template match="ContentHit" mode="json">
	
		<xsl:variable name="iconName">
			<xsl:choose>
				<xsl:when test="../../Alias = 'file'">
					<xsl:text>file</xsl:text>
				</xsl:when>
				<xsl:when test="../../Alias = 'task'">
					<xsl:text>check</xsl:text>
				</xsl:when>
				<xsl:when test="../../Alias = 'post'">
					<xsl:text>comment</xsl:text>
				</xsl:when>
				<xsl:when test="../../Alias = 'page'">
					<xsl:text>page</xsl:text>
				</xsl:when>
				<xsl:when test="../../Alias = 'calendar'">
					<xsl:text>calendar</xsl:text>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		
		<li class="mb-1">
			<div>
				<i class="icons icon-{$iconName} mr-2" aria-hidden="true"/>
				
				<a href="{../../ContextPath}{SectionAlias}{Alias}">
					
					<span>
						<xsl:value-of select="Title"/>
					</span>
					
					<i class="icons icon-chevron-right" aria-hidden="true"/>
					
					<span>
						<xsl:value-of select="SectionName"/>
					</span>
				</a>
			</div>
				
			<xsl:if test="Fragment">
				<div class="small">
					<xsl:value-of select="Fragment" disable-output-escaping="yes"/>
				</div>
			</xsl:if>
			
			<xsl:if test="InfoLine">
				<div class="inline-box">
					<div><xsl:value-of select="InfoLine"/></div>
				</div>
			</xsl:if>
		</li>
	
	</xsl:template>
	
	
	
	<xsl:template match="UserHits" mode="search">
	
		<div class="col-lg-6">
			<xsl:variable name="userCount" select="count(UserHit)"/>
			
			<div class="contentitem">
				<section>
					<header class="d-flex">
						<h2><xsl:value-of select="../PluginName"/></h2>
						
						<span class="ml-auto">
							<xsl:value-of select="$i18n.Showing"/>
							<xsl:text>&#160;</xsl:text>
							
							<span class="show-more-text">
								<xsl:choose>
									<xsl:when test="$userCount > 5">
										<xsl:text>5</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$userCount"/>
									</xsl:otherwise>
								</xsl:choose>
							</span>
							
							<span class="show-less-text" style="display: none;">
								<xsl:value-of select="$userCount"/>
							</span>
							
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$i18n.of"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$userCount"/>
						</span>
					</header>
					
					<div class="section-content">
						<ul class="no-styles">
							<xsl:apply-templates select="UserHit" mode="search"/>
						</ul>
					</div>
					
					<xsl:if test="$userCount > 5">
						<footer>
							<a href="#" class="show-more">
								<xsl:value-of select="$i18n.ShowMoreUsers"/>
							</a>
							
							<a href="#" class="show-less" style="display: none;">
								<xsl:value-of select="$i18n.ShowLessUsers"/>
							</a>
						</footer>
					</xsl:if>
				</section>
			</div>
		</div>
		
	</xsl:template>
	
	<xsl:template match="UserHit" mode="search">
	
		<li class="mb-1 d-flex">
			<xsl:if test="position() > 5">
				<xsl:attribute name="style">display: none!important;</xsl:attribute>
				<xsl:attribute name="class">mb-1 d-flex toggle-item</xsl:attribute>
			</xsl:if>
		
			<figure class="profile medium mr-2" aria-hidden="true">
				<xsl:if test="../../ProfileImageAlias">
					<img alt="{FullName}" src="{../../ContextPath}{../../ProfileImageAlias}/{UserID}" />
				</xsl:if>
			</figure>
			
			<div>
				<div>
					<xsl:choose>
						<xsl:when test="../../ShowProfileAlias">
							<a href="{../../ContextPath}{../../ShowProfileAlias}/{UserID}">
								<xsl:value-of select="FullName"/>
							</a>					
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="FullName"/>
						</xsl:otherwise>
					</xsl:choose>
				</div>
				
				<xsl:if test="IsExternal = 'true' or Organization or Attributes/Attribute">
					<div class="inline-box">
						<xsl:if test="IsExternal = 'true'">
							<div><xsl:value-of select="$i18n.IsExternal"/></div>
						</xsl:if>
						
						<xsl:if test="Organization">
							<div><xsl:value-of select="Organization"/></div>
						</xsl:if>
						
						<xsl:apply-templates select="Attributes/Attribute" mode="div"/>
					</div>
				</xsl:if>
			</div>
		</li>	
	
	</xsl:template>
	
	<xsl:template match="UserHits" mode="json">
		
		<div class="col-lg-6">
			<div class="contentitem">
				<section>
					<header>
						<h2><xsl:value-of select="../PluginName"/></h2>
					</header>
					
					<div class="section-content">
						<ul class="no-styles">
							<xsl:apply-templates select="UserHit" mode="json"/>
						</ul>
					</div>
				</section>
			</div>
		</div>
		
	</xsl:template>
	
	<xsl:template match="UserHit" mode="json">
	
		<li class="mb-1 d-flex">
			<figure class="profile medium mr-2" aria-hidden="true">
				<xsl:if test="../../ProfileImageAlias">
					<img alt="{FullName}" src="{../../ContextPath}{../../ProfileImageAlias}/{UserID}" />
				</xsl:if>
			</figure>
			
			<div>
				<div>
					<xsl:choose>
						<xsl:when test="../../ShowProfileAlias">
							<a href="{../../ContextPath}{../../ShowProfileAlias}/{UserID}">
								<xsl:value-of select="FullName"/>
							</a>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="FullName"/>
						</xsl:otherwise>
					</xsl:choose>
				</div>
				
				<xsl:if test="IsExternal = 'true' or Organization or Attributes/Attribute">
					<div class="inline-box">
						<xsl:if test="IsExternal = 'true'">
							<div><xsl:value-of select="$i18n.IsExternal"/></div>
						</xsl:if>
						
						<xsl:if test="Organization">
							<div><xsl:value-of select="Organization"/></div>
						</xsl:if>
						
						<xsl:apply-templates select="Attributes/Attribute" mode="div"/>
					</div>
				</xsl:if>
			</div>
		</li>	
	
	</xsl:template>
	
	<xsl:template match="Attribute" mode="div">
		
		<div><xsl:value-of select="."/></div>
		
	</xsl:template>
	
	
	
	<xsl:template match="Search">
	
		<div class="contentitem xl-margin-bottom">
			<form id="search" method="post" action="{/Document/requestinfo/uri}">
				<div class="row" id="searchwrapper">
					<div class="col-12 col-lg-6 mb-2">
						<div class="form-group">
							<label for="query"><xsl:value-of select="$i18n.SearchWord"/></label>
							
							<xsl:call-template name="createTextField">
								<xsl:with-param name="name" select="'q'"/>
								<xsl:with-param name="id" select="'query'"/>
								<xsl:with-param name="class" select="'form-control'"/>
								<xsl:with-param name="placeholder" select="$i18n.Search"/>
								<xsl:with-param name="width" select="''"/>
								<xsl:with-param name="value" select="Query"/>
							</xsl:call-template>
						</div>
					</div>
					
					<div class="col-12 col-lg-4 mb-2">
						<div class="form-group">
							<label for="typeSelector"><xsl:value-of select="$i18n.FilterResult"/></label>
							
							<xsl:call-template name="createDropdown">
								<xsl:with-param name="id" select="'typeSelector'"/>
								<xsl:with-param name="name" select="'t'"/>
								<xsl:with-param name="element" select="SearchTypes/SearchType"/>
								<xsl:with-param name="labelElementName" select="'name'" />
								<xsl:with-param name="valueElementName" select="'type'" />
								<xsl:with-param name="addEmptyOption" select="$i18n.AllResults"/>
								<xsl:with-param name="aria-label" select="$i18n.TypeOfResult"/>
								<xsl:with-param name="selectedValue" select="Type" />					
								<xsl:with-param name="class" select="'form-control'"/>
							</xsl:call-template>
						</div>
					</div>
					
					<div class="col-12 col-lg-2">
						<div class="hidden-md-down mb-2">&#160;</div>
						
						<button class="btn btn-success" type="submit">
							<i class="icons icon-search" aria-hidden="true"/>
							
							<span><xsl:value-of select="$i18n.Search"/></span>
						</button>
					</div>
				</div>
			</form>
		</div>
		
		<xsl:if test="Query and not(ViewFragment)">
			<div class="contentitem">
				<xsl:value-of select="$i18n.NoHits"/>
			</div>
		</xsl:if>
		
		<xsl:if test="ViewFragment">
			<div class="row">
				<xsl:apply-templates select="ViewFragment"/>
			</div>
		</xsl:if>
	
	</xsl:template>
	
	<xsl:template match="ViewFragment">
	
		<xsl:value-of select="." disable-output-escaping="yes"/>
		
	</xsl:template>
	
</xsl:stylesheet>