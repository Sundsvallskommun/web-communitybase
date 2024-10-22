<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>
	
	<xsl:include href="classpath://se/unlogic/hierarchy/core/utils/xsl/CKEditor.xsl" />
	
	<xsl:variable name="globalscripts">
		/jquery/jquery.js
		/jquery/jquery-migrate.js
		/ckeditor/ckeditor.js
		/ckeditor/adapters/jquery.js
	</xsl:variable>

	<xsl:template name="initializeFckEditor">
		
		<xsl:param name="toolbar" select="null" />
		<xsl:param name="editorHeight" select="'370'" />
		
		<xsl:call-template name="initializeFCKEditor">
			<xsl:with-param name="editorHeight" select="$editorHeight"/>
			<xsl:with-param name="contentsCss" select="/document/cssPath" />
			<xsl:with-param name="editorContainerClass" select="'fckeditor'" />
			<xsl:with-param name="contextPath" select="/document/requestinfo/contextpath" />
			<xsl:with-param name="customConfig">
				<xsl:value-of select="/document/requestinfo/contextpath"/><xsl:text>/static/f/</xsl:text><xsl:value-of select="/document/module/sectionID"/><xsl:text>/</xsl:text><xsl:value-of select="/document/module/moduleID"/><xsl:text>/utils/ckeditor/config.js</xsl:text>
			</xsl:with-param>
			<xsl:with-param name="toolbar" select="$toolbar" />
		</xsl:call-template>
	
	</xsl:template>
	
	<xsl:template name="initializeNewFckEditor">
		
		<xsl:param name="toolbar" select="null" />
		<xsl:param name="editorHeight" select="'370'" />
		
		<xsl:call-template name="initializeFCKEditor">
			<xsl:with-param name="editorHeight" select="$editorHeight"/>
			<xsl:with-param name="contentsCss" select="/Document/cssPath" />
			<xsl:with-param name="editorContainerClass" select="'fckeditor'" />
			<xsl:with-param name="contextPath" select="/Document/requestinfo/contextpath" />
			<xsl:with-param name="customConfig">
				<xsl:value-of select="/Document/requestinfo/contextpath"/><xsl:text>/static/f/</xsl:text><xsl:value-of select="/Document/module/sectionID"/><xsl:text>/</xsl:text><xsl:value-of select="/Document/module/moduleID"/><xsl:text>/utils/ckeditor/config.js</xsl:text>
			</xsl:with-param>
			<xsl:with-param name="toolbar" select="$toolbar" />
		</xsl:call-template>
		
	</xsl:template>
	
</xsl:stylesheet>