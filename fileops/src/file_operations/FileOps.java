package file_operations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static java.nio.file.StandardCopyOption.*;
import string_operations.StrOps;

public class FileOps {
	
	
	public static String createFolder(String location, String name){
		
		try{
			File f = new File(location + "\\" + name);
			f.mkdir();
			/*if(!f.mkdir()){
				System.out.println("Failed to create directory " + location + "\\" + name);
				System.exit(-1);
			}*/
		} catch(Exception e){
			System.out.println("Failed to create directory " + location + "\\" + name);
			System.exit(-1);
		}
		
		return location + "\\" + name;
	}
	
	public static void copyFiles(ArrayList<String> files, String destination){
		try{
			for(int ii = 0; ii < files.size(); ii++){
				String file_name = StrOps.getDilineatedSubstring(files.get(ii), "\\", 0, true);
				Files.copy(Paths.get(files.get(ii)), Paths.get(destination + "\\" + file_name), REPLACE_EXISTING);
			}
		} catch(Exception e){
			System.out.println("Failed to copy files.");
			System.exit(-1);
		}
	}
	
	/**
	 * Performs overwrite copy of a file from its current location to the destination.
	 * @param file - full path to the file
	 * @param destination - path to destination directory
	 * @return returns true if success, false if failure
	 */
	public static boolean copyFile(String file, String destination){
		try{
			ArrayList<String> path = breakFilePath(file);
			if(path.size() > 0){
				String filename = path.get(path.size() -1);
				String dest_path = destination + "\\" + filename;
				Files.copy(Paths.get(file), Paths.get(dest_path), REPLACE_EXISTING);
			}
		} catch(IOException e){
			System.out.println("[ERROR] cantrip.file_operations.fileops.copyFile error:: IOException when trying to copy file");
			return false;
		}
		return true;
	}
	
	public static void writeToFile(String filepath, String str, boolean append){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(filepath, append));
			bw.write(str);
			bw.close();
		} catch(IOException e){
			System.out.println("Failed to write to file " + filepath);
			System.exit(-1);
		}
	}

	public static void writeToFile(String filepath, ArrayList<String> strings, boolean append){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(filepath, append));
			for(int ii = 0; ii < strings.size(); ii++)
				bw.write(strings.get(ii));
			bw.close();
		} catch(IOException e){
			System.out.println("Failed to write to file " + filepath);
			System.exit(-1);
		}
	}
	
	
	public static void moveFile(String start_location, String final_location){
		try{
			Files.move(Paths.get(start_location), Paths.get(final_location), REPLACE_EXISTING);
		} catch(IOException e){
			System.out.println("Error moving " + start_location + " to " + final_location);
		}
	}
	
	public static void deleteFile(String file_location){
		try{
			Files.delete(Paths.get(file_location));
		}catch(IOException e){
			System.out.println("Error deleting " + file_location);
		}
	}
	
	public static String renameFile(String location, String new_name, boolean keep_extension){
		String final_path = "";
		try{
			String old_name = StrOps.getDilineatedSubstring(location, "\\", 0, true);
			String path = "";
			if(StrOps.findPattern(location, "\\") != -1){
				int index = location.length() - old_name.length();
				path = location.substring(0,index);
			}
			final_path = path;
			if(!keep_extension)
				final_path += new_name;
			else{
				String extension = StrOps.getDilineatedSubstring(old_name, ".", 1, false);
				final_path += new_name;
				final_path += ".";
				final_path += extension;
			}
			Files.move(Paths.get(location), Paths.get(final_path), REPLACE_EXISTING);
		} catch(IOException e){
			System.out.println("Error renaming " + location + " to " + new_name);
		}
		return final_path;
	}
	
	public static ArrayList<String> getAllPathsInDirectory(String path){
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> files = new ArrayList<String>();
		for(int ii = 0; ii < listOfFiles.length; ii++){
			files.add(listOfFiles[ii].getAbsolutePath());
		}
		return files;
	}
	
	public static ArrayList<String> getAllFilesInDirectory(String path){
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> files = new ArrayList<String>();
		for(int ii = 0; ii < listOfFiles.length; ii++){
			if(listOfFiles[ii].isFile())
				files.add(listOfFiles[ii].getAbsolutePath());
		}
		return files;
	}
	
	public static ArrayList<String> getFilesRecursive(String path){
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> files = new ArrayList<String>();
		for(int ii = 0; ii < listOfFiles.length; ii++){
			if(listOfFiles[ii].isFile())
				files.add(listOfFiles[ii].getAbsolutePath());
		}
		for(int ii = 0; ii < listOfFiles.length; ii++){
			if(listOfFiles[ii].isDirectory()){
				ArrayList<String> tempFiles = getFilesRecursive(listOfFiles[ii].getAbsolutePath());
				for(int jj = 0; jj < tempFiles.size(); jj++)
					files.add(tempFiles.get(jj));
			}
		}
		
		return files;
	}

	/**
	 * Returns all files matching a specific pattern. Requires StrOps Library.
	 * @param path - starting path to being searching from
	 * @param pattern - pattern to match for files. * allowed
	 * @return list of files that are found recursively from the path that match the pattern
	 */
	public static ArrayList<String> getFilesRecursive(String path, String pattern){
		ArrayList<String> files = getFilesRecursive(path);
		for(int ii = files.size()-1; ii >=0; ii--){
			if(!StrOps.patternMatch(files.get(ii), pattern))
				files.remove(ii);
		}
		return files;
	}
	
	public static boolean fileExists(String file){
		File f = new File(file);
		if(f.exists() && !f.isDirectory())
			return true;
		return false;
	}
	
	public static boolean directoryExists(String dir){
		File f = new File(dir);
		if(f.exists() && f.isDirectory())
			return true;
		return false;
	}
	
	public static long getLastModifiedOfFile(String file){
		File f = new File(file);
		return f.lastModified();
	}

	public static ArrayList<String> loadFileIntoArrayList(String filename) throws IOException{
		ArrayList<String> list = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = br.readLine();
		while(line != null){
			list.add(line);
			line = br.readLine();
		}
		br.close();
		return list;
	}

	
	public static ArrayList<String> breakFilePath(String path){
		ArrayList<String> b_path = new ArrayList<String>();
		String prev_instance = null, cur_instance = path;
		while(prev_instance == null || !prev_instance.equals(cur_instance)){
			if(cur_instance != null && prev_instance != null)
				b_path.add(cur_instance);
			prev_instance = cur_instance;
			cur_instance = StrOps.getDilineatedSubstring(path, "\\", b_path.size(), false);
		}
		return b_path;
	}
	
	public static String buildFilePath(ArrayList<String> b_path){
		String path = "";
		for(int ii = 0; ii < b_path.size(); ii++){
			path += b_path.get(ii);
			if(ii != b_path.size()-1)
				path += "\\";
		}
		return path;
	}
	
	public static ArrayList<String> findFiles(String pattern, String start_directory, boolean recurse){
		ArrayList<String> files = null;
		if(recurse)
			files = getFilesRecursive(start_directory);
		else
			files = getAllFilesInDirectory(start_directory);
		
		for(int ii = files.size()-1; ii >= 0; ii--){
			if(!StrOps.patternMatch(files.get(ii), pattern))
				files.remove(ii);
		}
		
		return files;
	}


}
