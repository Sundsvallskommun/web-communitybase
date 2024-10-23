package se.dosf.communitybase.modules.favourites;

import java.sql.SQLException;
import java.util.List;


public interface SectionFavouriteProvider {

	public List<Integer> getUserSectionFavourites(Integer userID) throws SQLException;

	public boolean addSectionFavourite(Integer sectionID, Integer userID) throws SQLException;
	
	public boolean deleteSectionFavourite(Integer sectionID, Integer userID) throws SQLException;
	
	public String getToggleFavouriteAlias();
	
}
