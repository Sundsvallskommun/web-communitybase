package se.dosf.communitybase.modules.favourites;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import se.dosf.communitybase.beans.SectionFavourite;
import se.dosf.communitybase.dao.CBDAOFactory;
import se.dosf.communitybase.enums.SectionAccessMode;
import se.dosf.communitybase.events.CBMemberRemovedEvent;
import se.dosf.communitybase.events.CBSectionPreDeleteEvent;
import se.unlogic.hierarchy.core.annotations.EventListener;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.SimpleSectionDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.URIParser;


public class SectionFavouriteModule extends AnnotatedForegroundModule implements SectionFavouriteProvider {

	private AnnotatedDAO<SectionFavourite> sectionFavouriteDAO;
	
	protected QueryParameterFactory<SectionFavourite, Integer> sectionIDParamFactory;
	
	protected QueryParameterFactory<SectionFavourite, Integer> userIDParamFactory;
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);
		
		if (!systemInterface.getInstanceHandler().addInstance(SectionFavouriteProvider.class, this)) {

			log.warn("Unable to register module " + moduleDescriptor + " in instance handler, another module is already registered for class " + SectionFavouriteProvider.class.getName());
		}
		
	}
	
	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		CBDAOFactory daoFactory = new CBDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());

		sectionFavouriteDAO = daoFactory.getSectionFavouriteDAO();

		sectionIDParamFactory = sectionFavouriteDAO.getParamFactory("sectionID", Integer.class);
		userIDParamFactory = sectionFavouriteDAO.getParamFactory("userID", Integer.class);
	}
	
	@WebPublic(alias = "togglefavourite")
	public ForegroundModuleResponse toggleFavourite(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws NumberFormatException, SQLException, IOException {

		SectionInterface sectionInterface;

		JsonObject jsonObject = new JsonObject(1);

		Integer sectionID = uriParser.getInt(2);
		
		if (sectionID != null && (sectionInterface = systemInterface.getSectionInterface(sectionID)) != null) {

			SectionFavourite sectionFavourite = getSectionFavourite(sectionID, user.getUserID());

			if(sectionFavourite != null) {
				
				log.info("User " + user + " deleting section " + sectionInterface.getSectionDescriptor() + " from its favourites");
				
				sectionFavouriteDAO.delete(sectionFavourite);
				
				jsonObject.putField("DeleteSuccess", "true");

			} else {
				
				log.info("User " + user + " adding section " + sectionInterface.getSectionDescriptor() + " to its favourites");
				
				sectionFavouriteDAO.add(new SectionFavourite(sectionID, user.getUserID()));
				
				jsonObject.putField("AddSuccess", "true");
			}
			
		} else {

			jsonObject.putField("SectionNotFound", sectionID);

		}

		HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);

		return null;
		
	}
	
	@Override
	public List<Integer> getUserSectionFavourites(Integer userID) throws SQLException {

		HighLevelQuery<SectionFavourite> query = new HighLevelQuery<SectionFavourite>();
		
		query.addParameter(userIDParamFactory.getParameter(userID));
		
		List<SectionFavourite> sectionFavourites = sectionFavouriteDAO.getAll(query);
		
		if(sectionFavourites != null) {
			
			List<Integer> sectionIDs = new ArrayList<Integer>(sectionFavourites.size());
			
			for(SectionFavourite favourite : sectionFavourites) {
				
				sectionIDs.add(favourite.getSectionID());
				
			}
			
			return sectionIDs;
		}
		
		return null;
	}

	@Override
	public boolean addSectionFavourite(Integer sectionID, Integer userID) throws SQLException {

		SectionInterface sectionInterface = systemInterface.getSectionInterface(sectionID);
		
		if(sectionInterface != null) {
		
			SectionFavourite favourite = new SectionFavourite();
		
			favourite.setSectionID(sectionID);
			favourite.setUserID(userID);
			
			sectionFavouriteDAO.add(favourite);
		
			return true;
		}
	
		return false;
	}

	@Override
	public boolean deleteSectionFavourite(Integer sectionID, Integer userID) throws SQLException {

		SectionFavourite sectionFavourite = getSectionFavourite(sectionID, userID);
		
		if(sectionFavourite != null) {
		
			sectionFavouriteDAO.delete(sectionFavourite);
		
			return true;
		}
	
		return false;
		
	}

	@Override
	public String getToggleFavouriteAlias() {

		return getFullAlias() + "/togglefavourite";
	}

	private SectionFavourite getSectionFavourite(Integer sectionID, Integer userID) throws SQLException {
		
		HighLevelQuery<SectionFavourite> query = new HighLevelQuery<SectionFavourite>();
	
		query.addParameter(sectionIDParamFactory.getParameter(sectionID));
		query.addParameter(userIDParamFactory.getParameter(userID));
		
		return sectionFavouriteDAO.get(query);
		
	}
	
	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(SectionFavouriteProvider.class, this);

		super.unload();
	}
	
	@EventListener(channel = SimpleSectionDescriptor.class)
	public void processEvent(CBSectionPreDeleteEvent event, EventSource source) {

		HighLevelQuery<SectionFavourite> query = new HighLevelQuery<SectionFavourite>(sectionIDParamFactory.getParameter(event.getSectionID()));

		try {
			sectionFavouriteDAO.delete(query);
		} catch (SQLException e) {

			log.warn("Unable to delete favorite section links to section marked for deletion", e);
		}
	}

	@EventListener(channel = SimpleSectionDescriptor.class)
	public void processEvent(CBMemberRemovedEvent event, EventSource source) {

		if (SectionAccessMode.CLOSED.equals(event.getSectionAccessMode()) || SectionAccessMode.HIDDEN.equals(event.getSectionAccessMode())) {
			
			HighLevelQuery<SectionFavourite> query = new HighLevelQuery<SectionFavourite>();
	
			query.addParameter(sectionIDParamFactory.getParameter(event.getSectionID()));
			query.addParameter(userIDParamFactory.getParameter(event.getUserID()));
	
			try {
				sectionFavouriteDAO.delete(query);
			} catch (SQLException e) {
	
				log.warn("Unable to delete favorite section links for user (" + event.getUserID() + ") removed from section (" + event.getSectionID() + ")", e);
			}
		}
	}

}
