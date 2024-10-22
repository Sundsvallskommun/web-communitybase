<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:variable name="scripts">
		/js/notificationbgmodule.js
	</xsl:variable>

	<xsl:template match="Document">
		
		<div class="btn-group">
			<button type="button" id="notificationmenu" data-url="{NotificationHandlerURL}" data-autorefresh="{AutoRefresh}" data-interval="{RefreshInterval}" class="btn btn-sm dropdown-toggle no-caret" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" aria-label="{$i18n.NotificationMenuTitle}">
				<i class="icons icon-bell big" aria-hidden="true"/>
				
				<span id="notificationbadge">
					<xsl:if test="UnreadCount = 0">
						<xsl:attribute name="class">hidden</xsl:attribute>
					</xsl:if>
					
					<xsl:value-of select="UnreadCount"/>
				</span>
			</button>
			
			<ul class="dropdown-menu">
				<li>
					<p class="h6 dropdown-header"><xsl:value-of select="$i18n.Notifications"/></p>
				</li>
				
				<li aria-hidden="true">
					<hr class="dropdown-divider no-margin-top no-margin-bottom"/>
				</li>
				
				<li>
					<div id="notifications-list"></div>
				</li>
				
				<li>
					<a class="dropdown-item" id="notifications-loading" href="#"><xsl:value-of select="$i18n.LoadingNotifications"/></a>
				</li>
				
				<li>
					<div class="dropdown-item italic" style="display: none;" id="no-notifications"><xsl:value-of select="$i18n.NoNotifications"/></div>
				</li>
				
				<li aria-hidden="true">
					<hr class="dropdown-divider no-margin-top no-margin-bottom"/>
				</li>
				
				<li>
					<a class="dropdown-item bigpaddingtop bigpaddingbottom text-align-center" href="{NotificationHandlerURL}"><xsl:value-of select="$i18n.ShowAllNotifications"/></a>
				</li>
			</ul>
		</div>
		
	</xsl:template>
		
</xsl:stylesheet>