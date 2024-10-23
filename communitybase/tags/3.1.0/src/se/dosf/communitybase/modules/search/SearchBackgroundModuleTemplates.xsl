<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:variable name="globalscripts">
		/jquery/jquery.js
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/searchbackgroundmodule.js
	</xsl:variable>
	
	<xsl:template match="Document">
		
			<div class="samarbetsrum-header-section samarbetsrum-search">
				
				<script type="text/javascript">
					searchModuleURI = '<xsl:value-of select="SearchModuleURI" />';
					sectionLogoURI = '<xsl:value-of select="SectionLogoURI" />';
					contextPath = '<xsl:value-of select="ContextPath" />';
					i18nSearchModule = {
						"SHOW_ALL_RESULT": '<xsl:value-of select="$i18n.ShowAllSearchResult" />',
						"EXTENDED_SEARCH": '<xsl:value-of select="$i18n.ExtendedSearchResult" />'
					};
				</script>
				
				<div class="of-search of-search-nested-results of-hide-to-lg in focus has-results">
				
					<input id="search-object" type="search" placeholder="{$i18n.SearchPlaceholder}" name="search" class="of-searchfield" />
					
					<div class="of-search-results">
						<a href="#" class="of-close of-icon of-icon-only">
							<i><svg viewBox="0 0 512 512" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns="http://www.w3.org/2000/svg"><use xlink:href="#close"/></svg></i>
							<span>Stäng</span>
						</a>
						<article>
						</article>
						<div id="showAllLink" class="footer of-hidden">
							<a data-hrefbase="{SearchModuleURI}" class="of-block-link"><xsl:value-of select="$i18n.ShowAllSearchResult" /></a>
						</div>
					</div>
					<div class="of-text-center of-inner-padded-t-half">
						<a href="#" class="close-search of-btn of-btn-inline of-btn-outline of-btn-xs of-hide-from-lg">Stäng</a>
					</div>
				</div>

				<a data-toggle="of-search" href="#" class="of-icon of-icon-lg of-hide-from-lg">
					<i><svg viewBox="0 0 512 512" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns="http://www.w3.org/2000/svg"><use xlink:href="#search"/></svg></i>
					<span>Sök</span>
				</a>
			</div>
		
			<div class="hidden">
			
				<div id="search-users-header">
					<h4>Personer</h4>
				</div>
				
				<li id="search-user-template">
					<a href="#">
						<figure class="of-profile of-figure-sm">
							<img alt="" data-srcbase="{ProfileImageURI}"/>
						</figure>
						<span></span>
						<!-- <ul class="of-meta-line">

						</ul> -->
					</a>
				</li>			
			
				<div id="search-sections-header">
					<h4>Samarbetsrum</h4>
				</div>		
			
				<li id="search-section-template">
					<a>
						<figure class="of-room of-figure-xs">
							<img alt="" data-srcbase="{SectionLogoURI}"/>
						</figure>
						<span></span>
					</a>
				</li>			
			
				<div id="search-tags-header">
					<h4>Taggar</h4>
				</div>			
			
				<li id="search-tags-template">
					<a data-srcbase="{SearchModuleURI}">
						<span></span>
					</a>
				</li>			
			
			</div>
		
	</xsl:template>
	
	<xsl:template match="user">
		
		<xsl:variable name="fullName">
			<xsl:value-of select="firstname" /><xsl:text>&#160;</xsl:text><xsl:value-of select="lastname" />
		</xsl:variable>
		
		<li>
			<a class="of-profile-link" href="{/Document/ShowProfileURI}/{userID}">
				<figure class="of-profile of-figure-sm">
					<xsl:if test="/Document/ProfileImageURI">
						<img alt="{$fullName}" src="{/Document/ProfileImageURI}/{userID}" data-of-tooltip="{$fullName}" />
					</xsl:if>
				</figure>
			</a>
		</li>

	</xsl:template>
	
</xsl:stylesheet>