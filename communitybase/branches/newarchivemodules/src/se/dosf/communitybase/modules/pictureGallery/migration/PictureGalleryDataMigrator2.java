package se.dosf.communitybase.modules.pictureGallery.migration;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import se.dosf.communitybase.modules.pictureGallery.PictureGalleryModule;
import se.dosf.communitybase.utils.CommunityBaseAnnotatedDAOFactory;
import se.dosf.communitybase.utils.DBtoDiskDataMigrator;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.io.FileUtils;

/** Migrates pictures image data from DB to disk storage.
 * 
 * @author exuvo */
public class PictureGalleryDataMigrator2 extends DBtoDiskDataMigrator<PictureLegacy> {

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		CommunityBaseAnnotatedDAOFactory daoFactory = new CommunityBaseAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler(), null);
		AnnotatedDAO<PictureLegacy> pictureDAO2 = daoFactory.getDAO(PictureLegacy.class);
		dataDAO = new AnnotatedDAOWrapper<PictureLegacy, Integer>(pictureDAO2, "pictureID", Integer.class);
	}

	@Override
	protected String getSelectIDQuery() {

		return "SELECT pictureID FROM picturegallery_pictures ORDER BY pictureID ASC";
	}

	@Override
	protected String getDataName() {

		return "Picture";
	}

	@Override
	protected String getDataNames() {

		return "pictures";
	}

	@Override
	public void writeToDisk(PictureLegacy picture) throws IOException, SQLException {

		File file = new File(PictureGalleryModule.getPictureFullFilePath(filestore, picture.getPictureID()));
		FileUtils.createMissingDirectories(file);
		FileUtils.writeFile(file, picture.getFull().getBinaryStream(), true);

		file = new File(PictureGalleryModule.getPictureMediumFilePath(filestore, picture.getPictureID()));
		FileUtils.createMissingDirectories(file);
		FileUtils.writeFile(file, picture.getMedium().getBinaryStream(), true);

		file = new File(PictureGalleryModule.getPictureSmallFilePath(filestore, picture.getPictureID()));
		FileUtils.createMissingDirectories(file);
		FileUtils.writeFile(file, picture.getSmall().getBinaryStream(), true);
	}

	@Override
	protected Class<PictureGalleryDataMigrator2> getClass2() {

		return PictureGalleryDataMigrator2.class;
	}

	@Override
	protected XMLDBScriptProvider getScriptProvider() throws SAXException, IOException, ParserConfigurationException {

		return new XMLDBScriptProvider(PictureGalleryModule.class.getResourceAsStream("MySQL DB Script.xml"));
	}

	@Override
	protected int getFromVersion() {

		return 5;
	}

	@Override
	protected int getToVersion() {

		return 6;
	}

	@Override
	protected String getTableGroupName() {

		return PictureGalleryModule.class.getPackage().getName();
	}

}
