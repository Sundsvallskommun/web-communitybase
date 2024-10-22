package se.dosf.communitybase.modules.pictureGallery.cruds;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.cruds.IntegerBasedCommunityBaseCRUD;
import se.dosf.communitybase.modules.pictureGallery.PictureGalleryModule;
import se.dosf.communitybase.modules.pictureGallery.beans.Comment;
import se.dosf.communitybase.modules.pictureGallery.beans.Picture;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.URIParser;

public class CommentCRUD extends IntegerBasedCommunityBaseCRUD<Comment, PictureGalleryModule> {

	protected final AnnotatedDAOWrapper<Picture, Integer> pictureDAO;

	public CommentCRUD(AnnotatedDAOWrapper<Comment, Integer> commentCRUDDAO, BeanRequestPopulator<Comment> populator, String typeElementName, String typeLogName, String listMethodAlias, PictureGalleryModule pictureGalleryModule, AnnotatedDAOWrapper<Picture, Integer> pictureDAO) {

		super(commentCRUDDAO, populator, typeElementName, typeLogName, listMethodAlias, pictureGalleryModule);

		this.pictureDAO = pictureDAO;
	}

	@Override
	protected void checkAddAccess(CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		Picture picture;

		if((picture = pictureDAO.get(NumberUtils.toInt(uriParser.get(3)))) == null) {
			throw new URINotFoundException(req.getRequestURI());
		}

		if(!callback.getAllowsComments() || !this.callback.checkAccess(picture, (CommunityGroup) req.getAttribute("group"))) {
			throw new AccessDeniedException("Denying user access to add comment to picture " + picture);
		}

		req.setAttribute("picture", picture);
	}

	@Override
	protected void checkUpdateAccess(Comment bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		if(!this.callback.hasUpdateAccess(bean, user)) {
			throw new AccessDeniedException("Denying user access to update comment " + bean);
		}
	}

	@Override
	protected void checkDeleteAccess(Comment bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		if(!this.callback.hasUpdateAccess(bean, user)) {
			throw new AccessDeniedException("Denying user access to delete comment " + bean);
		}
	}

	@Override
	protected Comment populateFromAddRequest(HttpServletRequest req, CommunityUser user, URIParser uriParser) throws ValidationException, Exception {

		Comment comment = super.populateFromAddRequest(req, user, uriParser);

		comment.setPicture((Picture) req.getAttribute("picture"));

		comment.setPoster(user);

		return comment;
	}

	@Override
	protected void redirectToListMethod(HttpServletRequest req, HttpServletResponse res, Comment bean) throws Exception {
		res.sendRedirect(req.getContextPath() + callback.getFullAlias((CommunityGroup) req.getAttribute("group")) + "/showPicture/" + bean.getPicture().getPictureID());
	}
}