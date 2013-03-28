package uk.co.gencoreoperative.fileutils;

import static uk.co.gencoreoperative.fileutils.FileUtils.listFolder;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Provides a collection of mechanisms for searching a file system.
 * 
 * @author rwapshott
 */
public class SearchUtils {
	
	/**
	 * Iterates over the contents of the Folder performing the Action on each
	 * file and folder contained within the folder. The file/folders will be 
	 * processed depth first, so that the lowest file or folder in a particular
	 * folder will be processed first.
	 * 
	 * This is important for recursive delete operations for example.
	 * 
	 * @param folder Non null, must be a folder.
	 * @param action Non null.
	 */
	public static void iterateBottomUp(File folder, Action action) {
		if (folder == null) throw new IllegalArgumentException("folder");
		if (!folder.isDirectory()) {
			throw new IllegalArgumentException("folder did not exist");
		}
		if (action == null) throw new IllegalArgumentException("action");
		
		/**
		 * Search will provide us a means of iterating over the folder structure
		 * without needing recursion. For very large file systems, using recursion
		 * can blow the heap.
		 */
		List<File> search = new LinkedList<File>();
		/**
		 * Listed will help us keep track of which folders we have listed.
		 */
		Set<File> listed = new HashSet<File>();
		search.add(folder);
		
		while (!search.isEmpty()) {
			File f = search.remove(0);
			
			if (f.isFile()) {
				action.perform(f);
			} else if (f.isDirectory()) {
				/**
				 * Have we listed the contents of this folder? If we have
				 * then it will be in the listed set. If it is in that set
				 * then we know we have processed the child file/folders
				 * already and can safely process the current folder.
				 */
				if (listed.contains(f)) {
					listed.remove(f);
					action.perform(f);
				} else {
					search.add(0, f);
					try {
						search.addAll(0, listFolder(f));
					} catch (PermissionDeniedException  e) {
						// If we cannot access the folder, move on
					}
					listed.add(f);
				}
			}
		}
	}
	
	/**
	 * Iterates over the contents of the folder in a top down way.
	 * 
	 * This approach is more suitable for folder copy operations.
	 * 
	 * @param folder Non null, must be a folder.
	 * @param action Non null.
	 */
	public static void iterateTopDown(File folder, Action action) {
		if (folder == null) throw new IllegalArgumentException("folder");
		if (!folder.isDirectory()) {
			throw new IllegalArgumentException("folder did not exist");
		}
		if (action == null) throw new IllegalArgumentException("action");
		
		/**
		 * Search will provide us a means of iterating over the folder structure
		 * without needing recursion. For very large file systems, using recursion
		 * can blow the heap.
		 */
		List<File> search = new LinkedList<File>();
		search.add(folder);
		
		while (!search.isEmpty()) {
			File f = search.remove(0);
			action.perform(f);
			
			if (f.isDirectory()) {
				try {
					search.addAll(listFolder(f));
				} catch (PermissionDeniedException e) {
					// If we cannot access the folder, move on
				}
			}
		}
	}
	
	/**
	 * Search the file system starting with the folder specified and 
	 * iterate over all sub folders.
	 * 
	 * @param startFolder The folder to start the search from. Non null.
	 * 
	 * @param search The Search implementation which indicates whether
	 * a File should be included in the return results.
	 * 
	 * @return A collection of Files which the Search implementation 
	 * returned true for.
	 */
	public static Collection<File> search(File startFolder, final Search search) {
		final List<File> results = new LinkedList<File>();
		iterateTopDown(startFolder, new Action() {
			@Override
			public void perform(File file) {
				if (search.include(file)) results.add(file);
			}
		});
		return results;
	}
	
	/**
	 * Action to be performed during a Folder operation.
	 * 
	 * @author Robert Wapshott
	 */
	public static interface Action {
		public void perform(File file);
	}
	
	/**
	 * Search represents a simple interface to determine which files
	 * should be included in a file system search.
	 * 
	 * @author rwapshott
	 */
	public static interface Search {
		public boolean include(File file);
	}
	
	public static void main(String [] args) {
		iterateBottomUp(new File("."), new Action(){
			@Override
			public void perform(File file) {
				System.out.println(file.getPath());
			}
		});
	}
}
