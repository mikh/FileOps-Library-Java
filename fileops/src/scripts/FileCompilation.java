package scripts;

import java.util.ArrayList;

//import string_operations.StrOps;
import file_operations.FileOps;

public class FileCompilation {
	public static void main(String[] args){
		/*
		String start_directory = args[0];
		String pattern = args[1];
		String archiveDirectory = args[2];
		boolean recurse = Boolean.parseBoolean(args[3]);
		boolean group = Boolean.parseBoolean(args[4]);
		String custom_group = args[5];
	*/
		String start_directory = "C:\\Users\\mikhail.andreev\\Dropbox\\Programming\\Java\\Libraries";
		String pattern = "*.jar";
		String archiveDirectory = "C:\\Users\\mikhail.andreev\\Dropbox\\Programming\\Java\\Libraries\\All Archives";
		boolean recurse = true;
		boolean group = true;
		String custom_group = "cantrip";
		/**/


		System.out.println(String.format("Running File Compilation\r\nParameters:\r\n\tstart_directory = %s\r\n\tpattern = \'%s\'\r\n\tarchiveDirectory = %s\r\n\trecurse = %b\r\n\tgroup = %b\r\n\tcustom_group = %s\r\n\t", start_directory, pattern, archiveDirectory, recurse, group, custom_group));
		
		System.out.println("Getting Files");
		ArrayList<String> files = FileOps.findFiles(pattern, start_directory, recurse);
		ArrayList<String> archiveSubfolders = new ArrayList<String>();
		
		
		for(int ii = 0; ii < files.size(); ii++){
			String directory = archiveDirectory;
			if(group)
				directory = getArchiveDirectory(files.get(ii), archiveDirectory, start_directory, custom_group, archiveSubfolders);
			
			if(!inArchiveDirectory(files.get(ii), directory) && isModifiedLast(files.get(ii), directory)){
				System.out.println("New or updated file found: " + files.get(ii));
				System.out.print("Copying " + files.get(ii) + " to " + directory + "...");
				if(!FileOps.copyFile(files.get(ii), directory))
					System.out.println("\n[ERROR] Failed to copy " + files.get(ii) + " to " + directory);
				else
					System.out.println("\tDone.");
			}
		}
		
		System.out.println("Script Complete.");
	}
	
	private static String getArchiveDirectory(String filepath, String archiveDirectory, String base_directory, String custom_group, ArrayList<String> archiveSubfolders){
		ArrayList<String> path = FileOps.breakFilePath(filepath);
		String name = path.get(path.size()-1);
		if(name.contains(custom_group)){
			int index = archiveSubfolders.indexOf(custom_group);
			if(index == -1){
				FileOps.createFolder(archiveDirectory, custom_group);
				archiveSubfolders.add(custom_group);
			}
			return archiveDirectory + "\\" + custom_group;
		} else{
			filepath = filepath.substring(base_directory.length() + 1);
			path = FileOps.breakFilePath(filepath);
			for(int ii = path.size()-2; ii >= 0; ii--){
				if(archiveSubfolders.contains(path.get(ii)))
					return archiveDirectory + "\\" + path.get(ii);
			}
			FileOps.createFolder(archiveDirectory, path.get(path.size()-2));
			archiveSubfolders.add(path.get(path.size()-2));
			return archiveDirectory + "\\" + path.get(path.size()-2);
		}
	}

	private static boolean inArchiveDirectory(String filename, String archiveDirectory){
		ArrayList<String> path = FileOps.breakFilePath(filename);
		if(path.size() > 0){
			path.remove(path.size()-1);
			if(archiveDirectory.equals(FileOps.buildFilePath(path)))
				return true;
		}
		return false;
	}

	private static boolean isModifiedLast(String filename, String archiveDirectory){
		ArrayList<String> path = FileOps.breakFilePath(filename);
		if(path.size() > 0){
			String file = path.get(path.size()-1);
			String archive_path = archiveDirectory + "\\" + file;
			if(FileOps.fileExists(archive_path)){
				long date_mod_file = FileOps.getLastModifiedOfFile(filename);
				long data_mod_archive = FileOps.getLastModifiedOfFile(archive_path);
				if(data_mod_archive >= date_mod_file)
					return false;
			}			
		}
		return true;
	}
}
