package se.dosf.communitybase.modules.userprofile.events;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import se.unlogic.hierarchy.core.beans.User;

public class UserUpdatedEvent implements Serializable {

	private static final long serialVersionUID = 8377840249582794714L;

	private User user;

	private BufferedImage image;

	private boolean imageDeleted;

	public User getUser() {

		return user;
	}

	public void setUser(User user) {

		this.user = user;
	}

	public BufferedImage getImage() {

		return image;
	}

	public void setImage(BufferedImage image) {

		this.image = image;
	}

	public boolean isImageDeleted() {

		return imageDeleted;
	}

	public void setImageDeleted(boolean imageDeleted) {

		this.imageDeleted = imageDeleted;
	}
}