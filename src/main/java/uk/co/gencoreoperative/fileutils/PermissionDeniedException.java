package uk.co.gencoreoperative.fileutils;

import java.io.File;
import java.io.IOException;

/**
 * Indicates that access to a given Folder was not allowed.
 * 
 * @author rwapshott
 */
public class PermissionDeniedException extends IOException {
	private static final long serialVersionUID = 1L;
	
	public static final String ERROR = "Permission Denied: ";
	private final File folder;
	
	public PermissionDeniedException(File folder) {
		super(ERROR + folder.getPath());
		this.folder = folder;
	}
	
	/**
	 * @return The folder that could not be accessed.
	 */
	public File getFolder() {
		return folder;
	}
}