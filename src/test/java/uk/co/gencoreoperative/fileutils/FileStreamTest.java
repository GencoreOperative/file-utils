package uk.co.gencoreoperative.fileutils;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Demonstrate the behaviour of the FileStream utility class.
 * 
 * @author rwapshott
 */
public class FileStreamTest {
	private static final String test = 
		"Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
    private static final String test2 =
            "Lorem ipsum \ndolor sit amet, \nconsectetur adipiscing \nelit.";
	@Test
	public void shouldCopyAllDataBetweenTwoStreams() {
		// Given
		ByteArrayInputStream in = new ByteArrayInputStream(test.getBytes());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		// When
		FileStream.copyStream(in, out);
		
		// Then
		assertEquals(new String(out.toByteArray()), test);
	}
	
	@Test
	public void shouldNotCloseStreamsInNonCloseMode() throws IOException {
		// Given
		ByteArrayInputStream in = mock(ByteArrayInputStream.class);
		when(in.read((byte[]) any())).thenReturn(-1);
		ByteArrayOutputStream out = mock(ByteArrayOutputStream.class);
		
		// When
		FileStream.copyStreamWithoutClosingStreams(in, out);
		
		// Then
		verify(in, times(0)).close();
		verify(out, times(0)).close();
	}
	
	@Test
	public void shouldCloseStreamsInCloseMode() throws IOException {
		// Given
		ByteArrayInputStream in = mock(ByteArrayInputStream.class);
		when(in.read((byte[]) any())).thenReturn(-1);
		ByteArrayOutputStream out = mock(ByteArrayOutputStream.class);
		
		// When
		FileStream.copyStream(in, out);
		
		// Then
		verify(in, times(1)).close();
		verify(out, times(1)).close();
	}

    @Test
    public void shouldReadLinesFromStream() {
        // Given
        ByteArrayInputStream in = new ByteArrayInputStream(test2.getBytes());
        // When
        List<String> result = FileStream.readLinesToList(in);
        // Then
        assertNotNull(result);
        assertEquals(4, result.size());
    }
}
