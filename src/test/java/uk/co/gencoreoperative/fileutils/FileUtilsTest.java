package uk.co.gencoreoperative.fileutils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileUtilsTest {
    @Test
    public void shouldSignalFileAsDeleted() throws IOException {
        Path tempFile = Files.createTempFile("test", ".tmp");
        File file = tempFile.toFile();
        assertTrue(Files.exists(tempFile));
        FileUtils.delete(file);
        assertFalse(Files.exists(tempFile));
    }

}
