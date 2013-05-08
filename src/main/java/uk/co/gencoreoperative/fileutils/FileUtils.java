package uk.co.gencoreoperative.fileutils;

import uk.co.gencoreoperative.fileutils.SearchUtils.Action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FileUtils {
	private final SearchUtils searchUtils;
	public FileUtils() {
		searchUtils = new SearchUtils();
	}
	
	public FileUtils(SearchUtils search) {
		this.searchUtils = search;
	}
	
	public void copy(File source, File target) {
		if (source == null) throw new IllegalArgumentException("source");
		if (target == null) throw new IllegalArgumentException("target");
		if (!source.exists()) throw new IllegalStateException("source doesn't exist");
		
		if (target.isDirectory()) {
			removeFolder(target);
		}
		
		try {
			FileInputStream fin = new FileInputStream(source);
			FileOutputStream fout = new FileOutputStream(target, false);
			StreamUtils.copyStream(fin, fout);
			// Finally set Last Modified timestamp
			target.setLastModified(source.lastModified());
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

    /**
     * Remove all files within a folder and the folder its self.
     * @param folder Non folder to remove files from.
     */
	public void removeFolder(File folder) {
		if (folder == null) throw new IllegalArgumentException("folder");
		if (!folder.exists()) return;
		if (folder.isFile() && !folder.delete()) {
			throw new IllegalStateException("Could not delete " + folder.getPath());
		}
		
		SearchUtils.iterateBottomUp(folder, new Action(){
			@Override
			public void perform(File file) {
				if (!file.delete()) {
					throw new IllegalStateException("Could not delete " + file.getPath());
				}
			}
		});
	}
    
    public void copyFolder(final File source, final File target) {
		if (source == null) throw new IllegalArgumentException("source");
		if (target == null) throw new IllegalArgumentException("target");
		if (!source.isDirectory()) throw new IllegalArgumentException("source is not folder");
		
		// Prepare the source path, needed later
		String temp = source.getAbsolutePath();
		if (!temp.endsWith(File.separator)) {
			temp += File.separator;
		}
		final String sourcePath = temp;
		
		
		// Build a map of each File to its relative path
		final Map<String, File> pathMap = new HashMap<String, File>();
		searchUtils.iterateTopDown(source, new Action(){
			@Override
			public void perform(File file) {
				if (file.equals(source)) return;
				String path = file.getAbsolutePath();
				path = path.substring(sourcePath.length(), path.length());
				pathMap.put(path, file);
			}
		});
		
		/**
		 * Iterate over the map of files, copying source to target.
		 */
		for (String path : pathMap.keySet()) {
			File s = pathMap.get(path);
			File t = new File(target.getAbsoluteFile() + File.separator + path);
			File tP = t.getParentFile();
			
			if (!tP.exists() && !tP.mkdirs()) {
				throw new IllegalStateException("Couldn't create " + tP.getPath());
			}
			
			if (s.isFile()) {
				copy(s, t);
			} else if (s.isDirectory()) {
				t.mkdir();
			}
		}
	}
	
    /**
     * Given a file path, split the folders and file name into an array.
     * 
     * @param path The file path to split.
     * @return An array of zero or more elements.
     */
    public String[] splitFilePathElements(String path) {
        String sep = File.separator;
        if (sep.equals("\\")) {
            sep = "\\\\";
        }
        return path.split(sep);
    }
    
    /**
     * In order to not use file separators in source code, the caller can
     * instead refer to file paths as String arrays. These can then be
     * converted to using the File separator at runtime.
     * 
     * @param parts An array describing a file/folders path.
     * 
     * @return A string with operating system specific file separators
     * inserted inbetween the file name parts.
     */
    public String assemblePathFromParts(String[] parts) {
    	if (parts.length == 0) return "";
    	String s = "";
    	for (String p : parts) {
    		s += p + File.separator;
    	}
    	s = s.substring(0, s.length() - File.separator.length());
    	return s;
    }
    
    /**
     * Lists the contents of a folder and ensuring that the result of the 
     * operation is clear to the caller.
     * 
     * @param folder Non null folder to list. Must not be a file.
     * 
     * @return A non null, possibly empty List of files in the folder.
     * 
     * @throws PermissionDeniedException If the user was not allowed to access the folder.
     */
    public static List<File> listFolder(File folder) throws PermissionDeniedException {
    	if (folder == null) {
    		throw new IllegalArgumentException("folder cannot be null");
    	}
    	if (!folder.isDirectory()) {
    		throw new IllegalStateException(folder.getPath() + " was not a folder");
    	}
    	File[] files = folder.listFiles();
    	if (files == null) {
    		throw new PermissionDeniedException(folder);
    	}
    	return Arrays.asList(files);
    }

    /**
     * Delete the file and throw an exception if the file could not be deleted.
     *
     * @param file A file to be deleted.
     * @throws IOException If the file could not be deleted.
     */
    public static void delete(File file) throws IOException {
        if (file.isDirectory()) {
            throw new IllegalArgumentException(file.getPath() + " is not a file");
        }
        if (!file.delete()) {
            throw new IOException("Failed to delete " + file.getPath());
        }
    }
}
