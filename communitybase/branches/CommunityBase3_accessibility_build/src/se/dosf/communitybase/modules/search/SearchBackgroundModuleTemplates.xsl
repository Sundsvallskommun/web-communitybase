<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:variable name="globalscripts">
		/jquery/jquery.js?v=1
	</xsl:variable>

	<xsl:variable name="scripts">
		/js/searchbackgroundmodule.js?v=3
	</xsl:variable>
	
	<xsl:template match="Document">
		
		<div class="site-search-container" id="site-search" data-searchmoduleuri="{SearchModuleURI}" data-contextpath="{ContextPath}">
			<div class="search-wrapper">
				<div class="input-group d-flex align-items-center">
					<div class="input-group-prepend">
						<label class="input-group-text mb-0" for="searchBar"><xsl:value-of select="$i18n.Search"/></label>
					</div>
					<input id="searchBar" data-hrefbase="{SearchModuleURI}" type="search" name="search" class="form-control search-input" aria-autocomplete="list" data-controls="search-result-container-std search-result-container"/>
				</div>
				
				<div class="search-result-container container-fluid" style="display: none;" aria-live="polite" id="search-result-container">
					<div class="close-bar">
						<a href="#" class="close-search">
							<i class="icons icon-close" aria-hidden="true"/>
							
							<span><xsl:value-of select="$i18n.Close"/></span>
						</a>
					</div>
					
					<div class="container">
						<div class="search-filters">
							<a href="#" class="filter active" data-type="">
								<xsl:value-of select="$i18n.TopResults"/>
							</a>
							
							<xsl:apply-templates select="SearchTypes/SearchType"/>
						</div>
						
						<div class="row search-result">
						</div>
					</div>
					
					<div class="show-all-bar">
						<a class="showAllLink" href="#"><xsl:value-of select="$i18n.ShowAllSearchResult"/></a>
					</div>
				</div>
			</div>
			
			<div class="hidden">
				<div class="search-no-hits-template">
					<div class="col-lg-12">
						<div class="contentitem">
							<section>
								<div class="section-content">
									<xsl:value-of select="$i18n.NoHitsClickBelow"/>
								</div>
							</section>
						</div>
					</div>
				</div>
			</div>
		</div>
		
	</xsl:template>
	
	<xsl:template match="SearchType">
		
		<a href="#" data-type="{type}" class="filter">
			<xsl:value-of select="name"/>
		</a>
		
	</xsl:template>
	
</xsl:stylesheet>