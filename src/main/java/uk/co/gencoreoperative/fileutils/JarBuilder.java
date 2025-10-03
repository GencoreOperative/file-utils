package uk.co.gencoreoperative.fileutils;

import uk.co.gencoreoperative.FileUtilsConstants;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.zip.ZipOutputStream;

/**
 * A basic fluent wrapper around the JDK classes for working with Jar files. This class will capture
 * the various nuances and details of this process and hopefully make it simpler.
 *
 * @author Robert Wapshott
 */
public class JarBuilder {

    public static final String META_INF = "META-INF";
    public static final String MANIFEST_MF = "MANIFEST.MF";
    public static final String MANIFEST = META_INF + "/" + MANIFEST_MF;

    public static final String JAR_EXTENSION = ".jar";

    private Set<String> directoriesAdded = new HashSet<String>();

    private ZipOutputStream zout;
    private String jarName;

    private JarBuilder(String name) {
        jarName = name;
        if (!jarName.endsWith(JAR_EXTENSION)) {
            jarName += JAR_EXTENSION;
        }

        try {
            zout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(jarName, false)));
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Could not create file " + name, e);
        }

        /**
         * Ensure the Jar has at least one entry. In this case the META-INF is appropriate.
         */
        addDirectoryInternal(META_INF);
    }

    /**
     * Signal the Jar file that it is to use compression for all subsequent entries added to the jar.
     * @return The JarBuilder instance.
     */
    public JarBuilder withCompression() {
        zout.setLevel(9);
        return this;
    }

    /**
     * @param directory Non null name of the folder.
     * @return The JarBuilder instance.
     */
    public JarBuilder addDirectory(String directory) {
        if (directory.startsWith(META_INF)) {
            throw new IllegalStateException(
                    "The manifest folder should not be created directly. " +
                    "Use the manifest functions for this purpose.");
        }
        return addDirectoryInternal(directory);
    }

    /**
     * @param directory Non null name of the folder.
     * @return The JarBuilder instance.
     */
    public JarBuilder addDirectoryInternal(String directory) {
        String name = directory;
        if (!directory.endsWith("/")) {
            name += "/";
        }

        if (!directoriesAdded.contains(directory)) {
            createEntry(name);
            closeEntry();
            directoriesAdded.add(directory);
        }
        return this;
    }

    /**
     * Add the file entry and copy the contents of the stream to the Jar file.
     *
     * Note: This is a blocking operation as the contents are read from the stream.
     *
     * @param filename The name of the file to add.
     * @param contents A stream to the contents of the file to append to the Jar.
     * @return The JarBuilder instance.
     */
    public JarBuilder addFile(String filename, InputStream contents) {
        createEntry(filename);
        StreamUtils.copyStreamWithoutClosingStreams(contents, zout);
        try {
            contents.close();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write data", e);
        }
        closeEntry();
        return this;
    }

    /**
     * Create an entry in the Jar file.
     * @param name The name determines the type of entry to create.
     */
    private void createEntry(String name) {
        try {
            JarEntry entry = new JarEntry(name);
            zout.putNextEntry(entry);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create entry", e);
        }
    }

    private void closeEntry() {
        try {
            zout.closeEntry();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to close Entry", e);
        }
    }

    /**
     * @param manifest The entire Manifest file text contents to add.
     * @return The JarBuilder instance.
     */
    public JarBuilder withManifest(String manifest) {
        addFile(MANIFEST, new ByteArrayInputStream(manifest.getBytes()));
        return this;
    }

    /**
     * Complete creation of the Jar file.
     * @return A non null Jar.
     */
    public File build() {
        try {
            zout.flush();
            zout.close();
            return new File(jarName);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }


    /**
     * Create a new instance of the JarBuilder.
     *
     * @param name The name of the Jar file to be created.
     *
     * @return A JarBuilder ready to be manipulated.
     */
    public static JarBuilder createJar(String name) {
        return new JarBuilder(name);
    }

    /**
     * Create a new instance of the ManfiestBuider.
     *
     * @return A ManfiestBuider ready to be populated.
     */
    public static ManifestBuilder createManifest() {
        return new ManifestBuilder();
    }

    /**
     * Fluent API for building the manifest of a Jar file.
     *
     * @author Robert Wapshott
     */
    public static class ManifestBuilder {
        private List<String> lines = new LinkedList<String>();
        public ManifestBuilder withMainClass(String name) {
            lines.add(MessageFormat.format("Main-Class: {0}", name));
            return this;
        }

        /**
         * @param line A generic line to add to the manifest.
         * @return The Manifest Builder.
         */
        public ManifestBuilder addLine(String line) {
            lines.add(line);
            return this;
        }

        /**
         * @return The assembled manifest text.
         */
        public String build() {
            String r = "";
            for (String line : lines) {
                r += line + FileUtilsConstants.NEW_LINE;
            }
            r += FileUtilsConstants.NEW_LINE;
            return r;
        }
    }
}
