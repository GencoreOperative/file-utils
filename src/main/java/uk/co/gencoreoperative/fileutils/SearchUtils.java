package uk.co.gencoreoperative.fileutils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Provides a collection of mechanisms for searching a file system.
 * 
 * @author rwapshott
 */
public class SearchUtils {
	
	/**
	 * Iterates over the contents of the folder in a top down way.
	 * 
	 * This approach is more suitable for folder copy operations.
	 * 
	 * @param folder Non null, must be a folder.
	 * @param action Non null.
	 */
	public static void forEachFile(File folder, Consumer<File> action) {
        try (Stream<Path> walk = Files.walk(folder.toPath(), FileVisitOption.FOLLOW_LINKS)){
			walk.map(Path::toFile)
					.filter(File::isFile)
					.forEach(action);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
	public static Collection<File> search(File startFolder, final Predicate<File> search) {
		final List<File> results = new LinkedList<File>();
		forEachFile(startFolder, file -> {
            if (search.test(file)) {
                results.add(file);
            }
        });
		return results;
	}

	public static void main(String [] args) {
		forEachFile(new File("."), file -> System.out.println(file.getPath()));
	}
}
