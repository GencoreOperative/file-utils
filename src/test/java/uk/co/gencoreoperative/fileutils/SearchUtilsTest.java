package uk.co.gencoreoperative.fileutils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static uk.co.gencoreoperative.fileutils.SearchUtils.forEachFile;
import static uk.co.gencoreoperative.fileutils.SearchUtils.search;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class SearchUtilsTest {
//	private Set<String> files;
//	private Map<String, Set<String>> folders;
//
//	@Before
//	public void setup() {
//		files = new HashSet<String>();
//		folders = new HashMap<String, Set<String>>();
//	}
//
//	@Test
//	public void testMockFileIsFile() {
//		file(null, "a");
//		File test = mockFile("a");
//		assertEquals(true, test.isFile());
//	}
//
//	@Test
//	public void testMockFileIsFolder() {
//		folder(null, "a");
//		File test = mockFile("a");
//		assertEquals(true, test.isDirectory());
//	}
//
//	@Test
//	public void searchForFilesShouldFindFiles() {
//		folder(null, "a");
//		folder("a", "b");
//		file("b", "e");
//		folder("a", "c");
//		file("c", "d");
//		File test = mockFile(null);
//		Collection<File> results = search(test, new Search(){
//			@Override
//			public boolean include(File file) {
//				return file.isFile();
//			}
//		});
//		assertEquals(2, results.size());
//		assertEquals(true, fileNames(results).contains("e"));
//		assertEquals(true, fileNames(results).contains("d"));
//	}
//
//	@Test
//	public void testIterateBottomUpStartAtRightPlace() {
//		folder(null, "a");
//		folder("a", "b");
//		file("b", "e");
//		folder("a", "c");
//		file("c", "d");
//		File test = mockFile("a");
//		final List<String> names = new LinkedList<String>();
//		iterateBottomUp(test, new Action(){
//			@Override
//			public void perform(File file) {
//				names.add(file.getName());
//			}
//		});
//		assertEquals(5, names.size());
//		assertEquals("e", names.get(0));
//		assertEquals("b", names.get(1));
//		assertEquals("d", names.get(2));
//		assertEquals("c", names.get(3));
//		assertEquals("a", names.get(4));
//	}
//
//	@Test
//	public void testIterateTopDownStartAtRightPlace() {
//		folder(null, "a");
//		folder("a", "b");
//		file("b", "e");
//		folder("a", "c");
//		file("c", "d");
//		File test = mockFile("a");
//		final List<String> names = new LinkedList<String>();
//		forEachFile(test, new Action(){
//			@Override
//			public void perform(File file) {
//				names.add(file.getName());
//			}
//		});
//		assertEquals(5, names.size());
//		assertEquals("a", names.get(0));
//		assertEquals("b", names.get(1));
//		assertEquals("c", names.get(2));
//		assertEquals("e", names.get(3));
//		assertEquals("d", names.get(4));
//	}
//
//	/**
//	 * Create a Mock File which will behave like a file system file
//	 * but will use the Map/Set data stored in this class to provide
//	 * answers to queries.
//	 *
//	 * @param path The path in the folders structure to start from.
//	 *
//	 * @return A mock File.
//	 */
//	private File mockFile(String path) {
//		File f = mock(File.class);
//		stub(f.getName()).toReturn(path);
//
//		// Boolean methods
//		stub(f.isDirectory()).toReturn(!files.contains(path));
//		stub(f.isFile()).toReturn(files.contains(path));
//
//		// ListFiles method
//		List<File> filesList = new LinkedList<File>();
//		for (String s : folders.get(path)) {
//			filesList.add(mockFile(s));
//		}
//		stub(f.listFiles()).toReturn(filesList.toArray(new File[0]));
//		return f;
//	}
//
//	/**
//	 * Mocks the relation between parent and file.
//	 */
//	private void file(String parent, String file) {
//		folder(parent, file);
//		assertEquals(true, files.add(file));
//	}
//
//	/**
//	 * Mocks the relationship between parent and folder.
//	 */
//	private void folder(String parentFile, String file) {
//		Set<String> folderContents = folders.get(parentFile);
//		if (folderContents == null) {
//			folderContents = new HashSet<String>();
//			folders.put(parentFile, folderContents);
//		}
//		// If we are adding to the set for the first time
//		if (folderContents.add(file)) {
//			folders.put(file, new HashSet<String>());
//		}
//	}
//
//	private static Set<String> fileNames(Collection<File> files) {
//		Set<String> names = new HashSet<String>();
//		for (File f : files) {
//			names.add(f.getName());
//		}
//		return names;
//	}
}
