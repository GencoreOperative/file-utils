/**
 * Copyright 2013 ForgeRock, Inc.
 *
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 */
package uk.co.gencoreoperative.fileutils;

import uk.co.gencoreoperative.Constants;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
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
    private ZipOutputStream zout;
    private String jarName;

    private JarBuilder(String name) {
        try {
            jarName = name;
            zout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(name, false)));
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Could not create file " + name, e);
        }
    }

    public JarBuilder addDirectory(String directory) {
        if (directory.startsWith(META_INF)) {
            throw new IllegalStateException("The manifest folder should not be created directly. " +
                    "Use the manifest functions");
        }

        String name = directory;
        if (!directory.endsWith("/")) {
            name += "/";
        }
        createEntry(name);
        return this;
    }

    public JarBuilder addFile(String filename, InputStream contents) {
        createEntry(filename);
        StreamUtils.copyStreamWithoutClosingStreams(contents, zout);
        try {
            contents.close();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write data", e);
        }
        return this;
    }

    private void createEntry(String name) {
        try {
            zout.putNextEntry(new JarEntry(name));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create entry", e);
        }
    }

    public JarBuilder withManifest(String manifest) {
        createEntry(META_INF + "/");
        addFile(META_INF + "/" + MANIFEST_MF, new ByteArrayInputStream(manifest.getBytes()));
        return this;
    }

    /**
     * Create the Jar and ensure it is ready to be used.
     * @return A non null Jar.
     */
    public File build() {
        try {
            zout.flush();
            zout.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return new File(jarName);
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
                r += line + Constants.NEW_LINE;
            }
            r += Constants.NEW_LINE;
            return r;
        }
    }
}
