<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:template name="createOFDropdown">
		<xsl:param name="id" select="null"/>
		<xsl:param name="name" select="null"/>
		<xsl:param name="simple" select="null"/>
		<xsl:param name="class" select="null"/>
		<xsl:param name="rel" select="null"/>
		<xsl:param name="title" select="''"/>
		<xsl:param name="disabled" select="null"/>
		<xsl:param name="valueElementName" select="null"/>
		<xsl:param name="labelElementName" select="null"/>
		<xsl:param name="labelElementName2" select="null"/>
		<xsl:param name="element" />
		<xsl:param name="selectedValue" select="''" />
		<xsl:param name="width" select="null"/>
		<xsl:param name="requestparameters" select="requestparameters"/>
		<xsl:param name="addEmptyOption" select="null"/>
		<xsl:param name="showInline" select="true()"/>
		
		<select>
			
			<xsl:attribute name="data-of-select">
				<xsl:if test="$showInline = true()">inline</xsl:if>
			</xsl:attribute>
		
			<xsl:if test="$class">
				<xsl:attribute name="class">
					<xsl:value-of select="$class"/> 
				</xsl:attribute>
			</xsl:if>
			
			<xsl:if test="$rel">
				<xsl:attribute name="rel">
					<xsl:value-of select="$rel"/> 
				</xsl:attribute>
			</xsl:if>
		
			<xsl:if test="$name">
				<xsl:attribute name="name">
					<xsl:value-of select="$name"/> 
				</xsl:attribute>
			</xsl:if>
			
			<xsl:if test="$id">
				<xsl:attribute name="id">
					<xsl:value-of select="$id"/>
				</xsl:attribute>
			</xsl:if>
			
			<xsl:if test="$disabled">
				<xsl:attribute name="disabled">
					<xsl:value-of select="'true'"/>
				</xsl:attribute>
			</xsl:if>
				
			<xsl:if test="$width">
				<xsl:attribute name="style">
					<xsl:value-of select="concat('width: ',$width)"/>
				</xsl:attribute>
			</xsl:if>
			
			<xsl:if test="$addEmptyOption">
				<option value=""><xsl:value-of select="$addEmptyOption"/></option>
			</xsl:if>
			
			<xsl:choose>
				<xsl:when test="$simple">
				
					<xsl:for-each select="$element">
						<option value="{.}">
							<xsl:choose>
								<xsl:when test="$requestparameters">
									<xsl:if test="$requestparameters/parameter[name=$name]/value = .">
										<xsl:attribute name="selected"/>
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<xsl:if test=". = $selectedValue">
										<xsl:attribute name="selected" />
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>
							
							<xsl:apply-templates select="."/>
						</option>
					</xsl:for-each>
										
				</xsl:when>
				<xsl:otherwise>
				
					<xsl:variable name="paramValue" select="$requestparameters/parameter[name=$name]/value"/>
				
					<xsl:for-each select="$element">
						<option value="{*[name()=$valueElementName]}">
							<xsl:choose>
								<xsl:when test="$requestparameters">
									<xsl:if test="$paramValue = *[local-name()=$valueElementName]">
										<xsl:attribute name="selected"/>
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<xsl:if test="*[name()=$valueElementName] = $selectedValue">
										<xsl:attribute name="selected" />
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:value-of select="*[name()=$labelElementName]" />
							
							<xsl:if test="$labelElementName2">
							
								<xsl:text>&#x20;</xsl:text>
								<xsl:value-of select="*[name()=$labelElementName2]" />
							</xsl:if>
							
						</option>
					</xsl:for-each>
					
				</xsl:otherwise>
			</xsl:choose>			
								
			<xsl:if test="$title != ''">			
				<xsl:attribute name="title" >
					<xsl:value-of select="$title"/>
				</xsl:attribute>
			</xsl:if>
			
		</select>
		
	</xsl:template>

</xsl:stylesheet>