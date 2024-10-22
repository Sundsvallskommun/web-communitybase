<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />
	
	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl"/>

	<xsl:variable name="globalscripts">
		/jquery/jquery.js?v=1
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/searchmodule.js?v=1
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
					
					<article>
						<xsl:apply-templates select="SectionHit" mode="search"/>
					</article>
					
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
	
		<div class="marginbottom d-flex">
			<xsl:if test="position() > 5">
				<xsl:attribute name="style">display: none!important;</xsl:attribute>
				<xsl:attribute name="class">marginbottom d-flex toggle-item</xsl:attribute>
			</xsl:if>
		
			<figure class="group medium bigmarginright">
				<xsl:if test="../../SectionLogoURI">
					<img alt="{Name}" src="{../../SectionLogoURI}/sectionlogo/{SectionID}" />
				</xsl:if>
			</figure>
			
			<div>
				<a href="{../../ContextPath}{FullAlias}">
					<xsl:value-of select="Name"/>
				</a>
				
				<xsl:if test="Fragment">
					<br/>
					<small>
						<xsl:value-of select="Fragment" disable-output-escaping="yes"/>
					</small>
				</xsl:if>
				
				<xsl:if test="MemberCount or AccessMode != 'OPEN'">
					<ul class="inline">
						<xsl:if test="AccessMode and AccessMode != 'OPEN'">
							<li>
								<xsl:choose>
									<xsl:when test="AccessMode = 'CLOSED'">
										<i class="icons icon-lock"/>
									</xsl:when>
									<xsl:when test="AccessMode = 'HIDDEN'">
										<i class="icons icon-eye-slash"/>
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
			</div>
		</div>
	
	</xsl:template>
	
	<xsl:template match="SectionHits" mode="json">
		
		<div class="col-lg-6">
			<div class="contentitem">
				<section>
					<header>
						<h2><xsl:value-of select="../PluginName"/></h2>
					</header>
					<article>
						<xsl:apply-templates select="SectionHit" mode="json"/>
					</article>
				</section>
			</div>
		</div>
		
	</xsl:template>
	
	<xsl:template match="SectionHit" mode="json">
		
		<a href="{../../ContextPath}{FullAlias}" class="d-flex marginbottom">
			<figure class="group small bigmarginright">
				<img alt="{Name}" src="{../../SectionLogoURI}/sectionlogo/{SectionID}"/>
			</figure>
			
			<div>
				<span><xsl:value-of select="Name"/></span>
			</div>
		</a>
		
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
					
					<article>
						<xsl:apply-templates select="TagHit" mode="search"/>
					</article>
					
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
	
		<div class="marginbottom">
			<xsl:if test="position() > 5">
				<xsl:attribute name="style">display: none!important;</xsl:attribute>
				<xsl:attribute name="class">marginbottom toggle-item</xsl:attribute>
			</xsl:if>			
		
			<a href="{../../SearchModuleURI}?t=tag&amp;q={.}">
				<xsl:text>#</xsl:text>
				<xsl:value-of select="."/>
			</a>
		</div>	
	
	</xsl:template>
	
	<xsl:template match="TagHits" mode="json">
		
		<div class="col-lg-6">
			<div class="contentitem">
				<section>
					<header>
						<h2><xsl:value-of select="../PluginName"/></h2>
					</header>
					<article>
						<xsl:apply-templates select="TagHit" mode="json"/>
					</article>
				</section>
			</div>
		</div>
		
	</xsl:template>
	
	<xsl:template match="TagHit" mode="json">
	
		<a href="{../../SearchModuleURI}?t=tag&amp;q={.}" class="d-flex marginbottom">
			<xsl:text>#</xsl:text>
			<xsl:value-of select="."/>
		</a>
	
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
					
					<article>
						<xsl:apply-templates select="ContentHit" mode="search"/>
					</article>
					
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
		
		<div class="marginbottom">
			<xsl:if test="position() > 5">
				<xsl:attribute name="style">display: none!important;</xsl:attribute>
				<xsl:attribute name="class">marginbottom toggle-item</xsl:attribute>
			</xsl:if>
			
			<div>
				<a href="{../../ContextPath}{SectionAlias}{Alias}">
					<i class="icons icon-{$iconName}"/>
					
					<span>
						<xsl:value-of select="Title"/>
					</span>
					
					<i class="icons icon-chevron-right"/>
					
					<span>
						<xsl:value-of select="SectionName"/>
					</span>
				</a>
			</div>
				
			<xsl:if test="Fragment">
				<div>
					<small>
						<xsl:value-of select="Fragment" disable-output-escaping="yes"/>
					</small>
				</div>
			</xsl:if>
			
			<xsl:if test="InfoLine">
				<ul class="inline">
					<li><xsl:value-of select="InfoLine"/></li>
				</ul>
			</xsl:if>
		</div>
	
	</xsl:template>
	
	<xsl:template match="ContentHits" mode="json">
		
		<div class="col-lg-6">
			<div class="contentitem">
				<section>
					<header>
						<h2><xsl:value-of select="../PluginName"/></h2>
					</header>
					<article>
						<xsl:apply-templates select="ContentHit" mode="json"/>
					</article>
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
		
		<div class="marginbottom">
			<div>
				<a href="{../../ContextPath}{SectionAlias}{Alias}">
					<i class="icons icon-{$iconName}"/>
					
					<span>
						<xsl:value-of select="Title"/>
					</span>
					
					<i class="icons icon-chevron-right"/>
					
					<span>
						<xsl:value-of select="SectionName"/>
					</span>
				</a>
			</div>
				
			<xsl:if test="Fragment">
				<div>
					<small>
						<xsl:value-of select="Fragment" disable-output-escaping="yes"/>
					</small>
				</div>
			</xsl:if>
			
			<xsl:if test="InfoLine">
				<ul class="inline">
					<li><xsl:value-of select="InfoLine"/></li>
				</ul>
			</xsl:if>
		</div>
	
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
					
					<article>
						<xsl:apply-templates select="UserHit" mode="search"/>
					</article>
					
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
	
		<div class="marginbottom d-flex">
			<xsl:if test="position() > 5">
				<xsl:attribute name="style">display: none!important;</xsl:attribute>
				<xsl:attribute name="class">marginbottom d-flex toggle-item</xsl:attribute>
			</xsl:if>
		
			<figure class="profile medium bigmarginright">
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
				
				<div>
					<ul class="inline">
						<xsl:if test="IsExternal = 'true'">
							<li><xsl:value-of select="$i18n.IsExternal"/></li>
						</xsl:if>
						
						<xsl:if test="Organization">
							<li><xsl:value-of select="Organization"/></li>
						</xsl:if>
						
						<xsl:apply-templates select="Attributes/Attribute" mode="li"/>
					</ul>
				</div>
			</div>
		</div>	
	
	</xsl:template>
	
	<xsl:template match="UserHits" mode="json">
		
		<div class="col-lg-6">
			<div class="contentitem">
				<section>
					<header>
						<h2><xsl:value-of select="../PluginName"/></h2>
					</header>
					<article>
						<xsl:apply-templates select="UserHit" mode="json"/>
					</article>
				</section>
			</div>
		</div>
		
	</xsl:template>
	
	<xsl:template match="UserHit" mode="json">
	
		<div class="marginbottom d-flex">
			<figure class="profile medium bigmarginright">
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
				
				<div>
					<ul class="inline">
						<xsl:if test="IsExternal = 'true'">
							<li><xsl:value-of select="$i18n.IsExternal"/></li>
						</xsl:if>
						
						<xsl:if test="Organization">
							<li><xsl:value-of select="Organization"/></li>
						</xsl:if>
						
						<xsl:apply-templates select="Attributes/Attribute" mode="li"/>
					</ul>
				</div>
			</div>
		</div>	
	
	</xsl:template>
	
	<xsl:template match="Attribute" mode="li">
		
		<li><xsl:value-of select="."/></li>
		
	</xsl:template>
	
	
	
	<xsl:template match="Search">
	
		<div class="contentitem xl-margin-bottom">
			<form id="search" method="post" action="{/Document/requestinfo/uri}">
				<div class="row" id="searchwrapper">
					<div class="col-lg-8 bigmarginbottom">
						<div class="input-group">
							<xsl:call-template name="createTextField">
								<xsl:with-param name="name" select="'q'"/>
								<xsl:with-param name="class" select="'form-control'"/>
								<xsl:with-param name="placeholder" select="$i18n.Search"/>
								<xsl:with-param name="width" select="''"/>
								<xsl:with-param name="value" select="Query"/>
							</xsl:call-template>
							
							<span class="input-group-btn">
								<button class="btn btn-success" type="submit">
									<i class="icons icon-search"/>
									<span><xsl:value-of select="$i18n.Search"/></span>
								</button>
							</span>
						</div>
					</div>
					<div class="col-lg-4">
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id" select="'typeSelector'"/>
							<xsl:with-param name="name" select="'t'"/>
							<xsl:with-param name="element" select="SearchTypes/SearchType"/>
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="valueElementName" select="'type'" />
							<xsl:with-param name="addEmptyOption" select="$i18n.AllResults"/>
							<xsl:with-param name="selectedValue" select="Type" />					
							<xsl:with-param name="class" select="'form-control'"/>
						</xsl:call-template>
					</div>
				</div>
			</form>
		</div>
		
		<xsl:if test="Query and not(ViewFragment)">
			<div class="contentitem italic">
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