<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:variable name="scripts">
		/js/notificationbgmodule.js
	</xsl:variable>

	<xsl:template match="Document">
		
			<script>
				var notificationHandlerUrl = '<xsl:value-of select="NotificationHandlerURL"/>';
			</script>
		
			<div class="samarbetsrum-header-section samarbetsrum-notifications">
				<a data-toggle="of-notification-menu" href="#" class="of-icon of-icon-lg of-badge-rodon" id="notification-symbol" data-of-no-tooltip="true" title="{$i18n.NotificationMenuTitle}">
				
					<xsl:if test="UnreadCount">
						<xsl:attribute name="data-of-badge"><xsl:value-of select="UnreadCount"/></xsl:attribute>
					</xsl:if>
					
					<i><svg viewBox="0 0 512 512" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns="http://www.w3.org/2000/svg"><use xlink:href="#notification"/></svg></i>
					<span class="of-hide-from-md"><xsl:value-of select="$i18n.NotificationsShort"/></span>
				</a>
				<nav class="of-notification-menu">
					<div>
						<h4><xsl:value-of select="$i18n.Notifications"/></h4>
					</div>
					<div class="article">
						<ul class="of-notifications-list" id="notifications-list">
							
							<li id="notifications-loading" class="notification-template">
								<a>
									<div class="article">
										<p><xsl:value-of select="$i18n.LoadingNotifications"/></p>
									</div>
								</a>
							</li>
							
							<li id="no-notifications" style="display:none;" class="notification-template">
								<a>
									<div class="article">
										<p><xsl:value-of select="$i18n.NoNotifications"/></p>
									</div>
								</a>
							</li>							
							
						</ul>
					</div>
					<div class="footer">
						<a class="of-block-link" href="{NotificationHandlerURL}"><xsl:value-of select="$i18n.ShowAllNotifications"/></a>
					</div>						
				</nav>				
			</div>
		
	</xsl:template>
		
</xsl:stylesheet>