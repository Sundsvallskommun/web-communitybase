<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:variable name="scripts">
		/js/notificationbgmodule.js
	</xsl:variable>

	<xsl:template match="Document">
		
		<div class="btn-group">
			<button type="button" id="notificationmenu" data-url="{NotificationHandlerURL}" data-autorefresh="{AutoRefresh}" data-interval="{RefreshInterval}" class="btn btn-sm dropdown-toggle no-caret" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" title="{$i18n.NotificationMenuTitle}">
				<i class="icons icon-bell big"></i>
				
				<span id="notificationbadge">
					<xsl:if test="UnreadCount = 0">
						<xsl:attribute name="class">hidden</xsl:attribute>
					</xsl:if>
					
					<xsl:value-of select="UnreadCount"/>
				</span>
			</button>
			
			<div class="dropdown-menu">
				<h6 class="dropdown-header"><xsl:value-of select="$i18n.Notifications"/></h6>
				<hr class="no-margin-top no-margin-bottom"/>
				<div id="notifications-list"></div>
				<a class="dropdown-item" id="notifications-loading" href="#"><xsl:value-of select="$i18n.LoadingNotifications"/></a>
				<div class="dropdown-item italic" style="display: none;" id="no-notifications"><xsl:value-of select="$i18n.NoNotifications"/></div>
				<hr class="no-margin-top no-margin-bottom"/>
				<a class="dropdown-item bigpaddingtop bigpaddingbottom text-align-center" href="{NotificationHandlerURL}"><xsl:value-of select="$i18n.ShowAllNotifications"/></a>
			</div>
		</div>
		
	</xsl:template>
		
</xsl:stylesheet>