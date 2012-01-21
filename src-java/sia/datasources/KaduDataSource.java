package sia.datasources;

import java.io.File;
import java.io.FilenameFilter;

import sia.utils.ParserFactory;

public class KaduDataSource extends DataSource {

	public KaduDataSource() {
		extensions = new String[] { "" };
		descriptions = new String[][] { new String[] { "Kadu directory", "Default /home/user/.kadu/" } }; // TODO [Aga] windows
		passwordDescriptions = null;
		parser = new ParserFactory("KaduParser").create();
	}
	
	@Override
	public String validateFiles(String[] files) {
		if (files == null || files.length != 1 || files[0] == null || !new File(files[0]).isDirectory()) {
			return "You have to choose one directory.";
		} else if(!new File(files[0]+File.separator+"history/history.db").exists()) {
			return "The directory has not history/history.db file. It is not proper kadu history directory.";
		} else if(new File(files[0]).list(new FilenameFilter() {
			
			@Override
			public boolean accept(File arg0, String arg1) {
				return arg1.endsWith("xml");
			}
		}).length ==0) {
			return "The directory has not XML config file. It is not proper kadu history directory.";
		}
		return null;
	}

	@Override
	public String validatePasswords(String[] passwords) {
		return null;
	}

	@Override
	public String validateUid(String uid) {
		return null;
	}

}
