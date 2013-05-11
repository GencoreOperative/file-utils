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

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import static org.junit.Assert.*;

/**
 * A collection of integration tests for the JarBuilder.
 *
 * @author Robert Wapshott
 */
public class JarBuilderTest {
    @Test
    public void shouldCreateAValidJar() {
        assertTrue(JarBuilder.createJar(randomName() + ".jar").build().delete());
    }

    @Test
    public void shouldAppendJarExtensionToNameIfOmitted() {
        File jar = null;
        try {
            String name = randomName();
            jar = JarBuilder.createJar(name).build();
            assertEquals(name + JarBuilder.JAR_EXTENSION, jar.getName());
        } finally {
            assertTrue(jar.delete());
        }
    }

    @Test
    public void shouldAddFilesToJar() throws IOException {
        String fileName = "badger";
        ByteArrayInputStream contents = new ByteArrayInputStream(fileName.getBytes());
        File jar = JarBuilder.createJar(randomName())
                .addFile(fileName, contents)
                .build();
        JarFile jarFile = new JarFile(jar);

        assertNotNull(jarFile.getEntry(fileName));
        assertTrue(jar.delete());
    }

    @Test
    public void shouldAddDirectory() throws IOException {
        File jar = JarBuilder.createJar(randomName()).addDirectory("badger").build();
        JarFile jarFile = new JarFile(jar);
        ZipEntry entry = jarFile.getEntry("badger/");
        assertNotNull(entry);
        assertTrue(jar.delete());
    }

    @Test
    public void shouldAllowMultipleDirectoriesWithTheSameName() throws IOException {
        File jar = JarBuilder.createJar(randomName())
                .addDirectory("badger")
                .addDirectory("badger")
                .build();
        JarFile jarFile = new JarFile(jar);
        ZipEntry entry = jarFile.getEntry("badger/");
        assertNotNull(entry);
        assertTrue(jar.delete());
    }


    @Test
    public void shouldAddManifest() throws IOException {
        String manifest = JarBuilder.createManifest().addLine("badger").build();
        File jar = JarBuilder.createJar(randomName()).withManifest(manifest).build();
        JarFile jarFile = new JarFile(jar);
        assertNotNull(jarFile.getEntry("META-INF/MANIFEST.MF"));
        assertTrue(jar.delete());
    }

    @Test
    public void shouldCompressDataStored() {
        ByteArrayInputStream contents = new ByteArrayInputStream("aaaaaaaaaaaaaaaaaaaa".getBytes());

        File uncompressed = JarBuilder.createJar(randomName())
                .addFile("badger", contents)
                .build();

        File compressed = JarBuilder.createJar(randomName())
                .withCompression()
                .addFile("badger", contents)
                .build();

        assertTrue(compressed.length() < uncompressed.length());
        assertTrue(uncompressed.delete());
        assertTrue(compressed.delete());
    }


    private static String randomName() {
        return RandomStringUtils.randomAlphabetic(5);
    }
}
