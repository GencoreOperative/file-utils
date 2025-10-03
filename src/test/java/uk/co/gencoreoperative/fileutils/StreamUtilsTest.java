package uk.co.gencoreoperative.fileutils;

import org.junit.Test;
import uk.co.gencoreoperative.FileUtilsConstants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author rwapshott
 */
public class StreamUtilsTest {
//	private static final String test =
//		"Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
//    private static final String test2 =
//            "Lorem ipsum \ndolor sit amet, \nconsectetur adipiscing \nelit.";
//	@Test
//	public void shouldCopyAllDataBetweenTwoStreams() {
//		// Given
//		ByteArrayInputStream in = new ByteArrayInputStream(test.getBytes());
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//		// When
//		StreamUtils.copyStream(in, out);
//
//		// Then
//		assertEquals(new String(out.toByteArray()), test);
//	}
//
//	@Test
//	public void shouldNotCloseStreamsInNonCloseMode() throws IOException {
//		// Given
//		ByteArrayInputStream in = mock(ByteArrayInputStream.class);
//		when(in.read((byte[]) any())).thenReturn(-1);
//		ByteArrayOutputStream out = mock(ByteArrayOutputStream.class);
//
//		// When
//		StreamUtils.copyStreamWithoutClosingStreams(in, out);
//
//		// Then
//		verify(in, times(0)).close();
//		verify(out, times(0)).close();
//	}
//
//	@Test
//	public void shouldCloseStreamsInCloseMode() throws IOException {
//		// Given
//		ByteArrayInputStream in = mock(ByteArrayInputStream.class);
//		when(in.read((byte[]) any())).thenReturn(-1);
//		ByteArrayOutputStream out = mock(ByteArrayOutputStream.class);
//
//		// When
//		StreamUtils.copyStream(in, out);
//
//		// Then
//		verify(in, times(1)).close();
//		verify(out, times(1)).close();
//	}
//
//    @Test
//    public void shouldReadLinesFromStream() {
//        // Given
//        ByteArrayInputStream in = new ByteArrayInputStream(test2.getBytes());
//        // When
//        List<String> result = StreamUtils.readLinesToList(in);
//        // Then
//        assertNotNull(result);
//        assertEquals(4, result.size());
//    }
//
//    @Test
//    public void shouldTrimLastCharacterFromLine() {
//		// Given
//		ByteArrayInputStream in = new ByteArrayInputStream(test2.getBytes());
//		// When
//		String result = StreamUtils.readLines(in);
//		// Then
//		assertFalse(result.endsWith(FileUtilsConstants.NEW_LINE));
//	}
//
//    @Test
//    public void shouldIterateOverAllLines() {
//        // Given
//        ByteArrayInputStream in = new ByteArrayInputStream(test2.getBytes());
//        Iterator<String> iterator = StreamUtils.getLineIterator(in);
//        List<String> results = new ArrayList<String>();
//
//        // When
//        for (String line = iterator.next(); line != null; line = iterator.next()) {
//            results.add(line);
//        }
//
//        // Then
//        assertEquals(4, results.size());
//    }
//
//    @Test
//    public void shouldCloseInputStreamWhenComplete() throws IOException {
//        // Given
//        InputStream in = spy(new ByteArrayInputStream("".getBytes()));
//        Iterator<String> iterator = StreamUtils.getLineIterator(in);
//
//        // When
//        iterator.next();
//
//        // Then
//        verify(in).close();
//    }

}
