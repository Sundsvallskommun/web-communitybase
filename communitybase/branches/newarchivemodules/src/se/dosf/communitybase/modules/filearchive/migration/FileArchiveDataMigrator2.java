package se.dosf.communitybase.modules.filearchive.migration;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import se.dosf.communitybase.modules.filearchive.FileArchiveModule;
import se.dosf.communitybase.utils.CommunityBaseAnnotatedDAOFactory;
import se.dosf.communitybase.utils.DBtoDiskDataMigrator;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.io.FileUtils;

/** Migrates file data from DB to disk storage.
 * 
 * @author exuvo */
public class FileArchiveDataMigrator2 extends DBtoDiskDataMigrator<FileLegacy> {

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		CommunityBaseAnnotatedDAOFactory daoFactory = new CommunityBaseAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler(), null);
		AnnotatedDAO<FileLegacy> fileDAO2 = daoFactory.getDAO(FileLegacy.class);
		dataDAO = new AnnotatedDAOWrapper<FileLegacy, Integer>(fileDAO2, "fileID", Integer.class);
	}

	@Override
	protected String getSelectIDQuery() {

		return "SELECT fileID FROM filearchive_files ORDER BY fileID ASC";
	}

	@Override
	protected String getDataName() {

		return "File";
	}

	@Override
	protected String getDataNames() {

		return "files";
	}

	@Override
	public void writeToDisk(FileLegacy file2) throws IOException, SQLException {

		File file = new File(FileArchiveModule.getFilePath(filestore, file2.getFileID()));
		FileUtils.createMissingDirectories(file);
		FileUtils.writeFile(file, file2.getFile().getBinaryStream(), true);
	}

	@Override
	protected Class<FileArchiveDataMigrator2> getClass2() {

		return FileArchiveDataMigrator2.class;
	}

	@Override
	protected XMLDBScriptProvider getScriptProvider() throws SAXException, IOException, ParserConfigurationException {

		return new XMLDBScriptProvider(FileArchiveModule.class.getResourceAsStream("MySQL DB Script.xml"));
	}

	@Override
	protected int getFromVersion() {

		return 3;
	}

	@Override
	protected int getToVersion() {

		return 4;
	}

	@Override
	protected String getTableGroupName() {

		return FileArchiveModule.class.getPackage().getName();
	}
}
