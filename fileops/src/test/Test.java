package test;

import java.util.ArrayList;

import string_operations.StrOps;
import file_operations.FileOps;

public class Test {
	public static void main(String[] args){
		String start_directory = "C:\\Users\\mikhail.andreev\\Dropbox\\Programming\\Java\\Libraries";
		String pattern = "*.jar";
		String archiveDirectory = "C:\\Users\\mikhail.andreev\\Dropbox\\Programming\\Java\\Libraries\\All Archives";
		boolean recurse = true;
		
		ArrayList<String> files = FileOps.findFiles(pattern, start_directory, recurse);
		for(int ii = 0; ii < files.size(); ii++){
			//first ignore any files already in the archive directory
			ArrayList<String> part = FileOps.breakFilePath(files.get(ii));
			String filename = part.get(part.size()-1);
			part.remove(part.size()-1);
			if(!archiveDirectory.equals(FileOps.buildFilePath(part))){
				String archive_path = archiveDirectory + "\\" + filename;
				if(FileOps.fileExists(archive_path)){
					long data_mod = FileOps.getLastModifiedOfFile(archive_path);
					long data_mod_new = FileOps.getLastModifiedOfFile(files.get(ii));
					if(data_mod_new > data_mod){
						
					}
				}
			}
			
		}
		
		//StrOps.printArrayList(files);
	}
}
