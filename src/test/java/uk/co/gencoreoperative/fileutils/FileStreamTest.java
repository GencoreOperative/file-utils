package uk.co.gencoreoperative.fileutils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

/**
 * Demonstrate the behaviour of the FileStream utility class.
 * 
 * @author rwapshott
 */
public class FileStreamTest {
	private static final String test = 
		"Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
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
}
