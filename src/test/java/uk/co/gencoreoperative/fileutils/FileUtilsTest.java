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

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Robert Wapshott
 */
public class FileUtilsTest {
    @Test (expected = IOException.class)
    public void shouldThrowExceptionIfTheFileCouldNotBeDeleted() throws IOException {
        File mock = mock(File.class);
        given(mock.delete()).willReturn(false);
        FileUtils.delete(mock);
    }

    @Test
    public void shouldSignalFileAsDeleted() throws IOException {
        File mock = mock(File.class);
        given(mock.delete()).willReturn(true);
        FileUtils.delete(mock);
        verify(mock).delete();
    }

}
