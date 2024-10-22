package se.dosf.communitybase.modules.util;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import se.dosf.communitybase.modules.invitation.InvitationModule;
import se.dosf.communitybase.modules.invitation.beans.Invitation;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;

import it.sauronsoftware.cron4j.Scheduler;

public class InvitationCleanerModule extends AnnotatedForegroundModule implements Runnable {

	@InstanceManagerDependency(required = true)
	private InvitationModule invitationModule;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Start time expression", description = "Cron expression for when to clean unused invitations", required = true)
	protected String cronExp = "0 4 * * *";

	private Scheduler taskScheduler;

	@Override
	public void update(ForegroundModuleDescriptor descriptor, DataSource dataSource) throws Exception {

		super.update(descriptor, dataSource);

		stopTaskScheduler();

		initTaskScheduler();
	}

	@Override
	public void unload() throws Exception {

		stopTaskScheduler();

		super.unload();

		systemInterface.getInstanceHandler().removeInstance(InvitationCleanerModule.class, this);
	}

	@Override
	public void run() {

		if (this.systemInterface.getSystemStatus() == SystemStatus.STARTED) {
			log.info("Cleaning invitations with missing sections...");

			try {
				List<Invitation> invitations = invitationModule.getAllInvitations();

				if (CollectionUtils.isEmpty(invitations)) {
					log.info("No invitations found.");
					
					return;
				}

				for (Invitation invitation : invitations) {
					if (CollectionUtils.isEmpty(invitation.getSectionInvitations())) {
						log.info("Deleting invitation " + invitation + " due to it not having valid section invitations left.");
						
						invitationModule.removeInvitation(invitation);
					}
				}
			} catch (SQLException e) {
				log.error("Could not delete invitations", e);
			}
		}

	}

	private void stopTaskScheduler() {

		if (taskScheduler != null && taskScheduler.isStarted()) {
			taskScheduler.stop();
		}
	}

	private void initTaskScheduler() {

		taskScheduler = new Scheduler(systemInterface.getApplicationName() + " - " + moduleDescriptor.toString());

		taskScheduler.setDaemon(true);
		taskScheduler.schedule(cronExp, this);
		taskScheduler.start();
	}
}