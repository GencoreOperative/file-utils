package uk.co.gencoreoperative.fileutils;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * A collection of integration tests for the JarBuilder.
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
        return UUID.randomUUID().toString();
    }
}
