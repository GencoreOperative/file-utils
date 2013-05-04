package uk.co.gencoreoperative.fileutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * A collection of static operations for working with streams.
 * 
 * @author rwapshott
 */
public class FileStream {
    /**
     * Read the contents of a stream and convert it into lines which are placed in a String.
     *
     * @param in Non null stream to read. Will not be closed once complete.
     *
     * @return A non null but possibly empty string.
     */
    public static String readLines(InputStream in) {
        String r = "";
        String nl = "\n";

        for (String line : readLinesToList(in)) {
            r += line + nl;
        }

        if (!r.isEmpty()) {
            r.substring(0, r.length() - nl.length());
        }

        return r;
    }

    /**
     * Reads an InputStream and converts it into a list of Strings representing the lines of text
     * within the stream.
     *
     * @param in Non null stream to read. Will not be closed once complete.
     *
     * @return A non null, but possibly empty list.
     */
    public static List<String> readLinesToList(InputStream in) {
        List<String> r = new LinkedList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String data;
        try {
            while ((data = reader.readLine()) != null) {
                r.add(data);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return r;
    }

	/**
	 * Copy the contents of one stream to another in an efficient way. This
	 * call will block until the entire stream has been copied. The input and
	 * output streams will be closed once the operation completes.
	 * 
	 * @param in Non null input stream to copy.
	 * 
	 * @param out Non null output stream to write the input data to.
	 * 
	 * @throws IllegalStateException If there was any IO error whilst copying the
	 * data, then this runtime exception will be thrown.
	 */
	public static void copyStream(InputStream in, OutputStream out) {
		if (in == null) throw new IllegalArgumentException("in");
		if (out == null) throw new IllegalArgumentException("out");
		
		copyStreamWithoutClosingStreams(in, out);
		try {
			in.close();
			out.flush();
			out.close();
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}
	
	/**
	 * Copy the contents of one stream to another in an efficient way. This
	 * call will block until the entire stream has been copied. The input and
	 * output streams will not be closed once the operation completes. It will
	 * be up to the caller to manage this stage of the process.
	 * 
	 * @param in Non null input stream to copy.
	 * 
	 * @param out Non null output stream to write the input data to.
	 * 
	 * @throws IllegalStateException If there was any IO error whilst copying the
	 * data, then this runtime exception will be thrown.
	 */
	public static void copyStreamWithoutClosingStreams(InputStream in, OutputStream out) {
		if (in == null) throw new IllegalArgumentException("in");
		if (out == null) throw new IllegalArgumentException("out");
		
		try {
			byte[] buf = new byte[8000];
			int read = 0;
			while (read != -1) {
				read = in.read(buf);
				if (read == -1) continue;
				out.write(buf, 0, read);
			}
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}
}
