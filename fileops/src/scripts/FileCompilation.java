package scripts;

import java.util.ArrayList;

//import string_operations.StrOps;
import file_operations.FileOps;

public class FileCompilation {
	public static void main(String[] args){
		String start_directory = args[0];
		String pattern = args[1];
		String archiveDirectory = args[2];
		boolean recurse = Boolean.parseBoolean(args[3]);
	/*	
		String start_directory = "C:/Users/mikhail.andreev/Dropbox/Programming/Java/Libraries";
		String pattern = ".jar";
		String archiveDirectory = "C:/Users/mikhail.andreev/Dropbox/Programming/Java/Libraries/All Archives";
		boolean recurse = true;
		*/


		System.out.println(String.format("Running File Compilation\r\nParameters:\r\n\tstart_directory = %s\r\n\tpattern = \'%s\'\r\n\tarchiveDirectory = %s\r\n\trecurse = %b\r\n\t", start_directory, pattern, archiveDirectory, recurse));
		
		System.out.println("Getting Files");
		ArrayList<String> files = FileOps.findFiles(pattern, start_directory, recurse);
		//StrOps.printArrayList(files);
		
		
		for(int ii = 0; ii < files.size(); ii++){
			if(!inArchiveDirectory(files.get(ii), archiveDirectory) && isModifiedLast(files.get(ii), archiveDirectory)){
				System.out.println("New or updated file found: " + files.get(ii));
				System.out.print("Copying " + files.get(ii) + " to " + archiveDirectory + "...");
				if(!FileOps.copyFile(files.get(ii), archiveDirectory))
					System.out.println("\n[ERROR] Failed to copy " + files.get(ii) + " to " + archiveDirectory);
				else
					System.out.println("\tDone.");
			}
		}
		
		System.out.println("Script Complete.");
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
