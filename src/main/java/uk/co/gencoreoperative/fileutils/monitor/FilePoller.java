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
package uk.co.gencoreoperative.fileutils.monitor;

import uk.co.gencoreoperative.fileutils.SearchUtils;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * FilePoller is responsible for keeping track of files in a folder. If a file changes the
 * FilePoller needs to be able to determine this.
 *
 * This implementation will maintain some in-memory maps which will track the file size and
 * last modified timestamps. These will allow the FilePoller to determine whether the file
 * has changed between successive calls.
 */
public class FilePoller {
    private Map<String, Long> timestamps = new HashMap<String, Long>();
    private Map<String, Long> sizes = new HashMap<String, Long>();
    private boolean firstPoll = true;

    private File folder;

    public FilePoller(File folder) {
        this.folder = folder;
    }

    private boolean isFileNew(String path) {
        return timestamps.containsKey(path);
    }

    private boolean isTimestampChanged(String path, long timestamp) {
        long result = timestamps.put(path, timestamp);
        return result != timestamp;
    }

    private boolean isFileSizeChanged(String path, long size) {
        long result = sizes.put(path, size);
        return result != size;
    }

    public List<FileEvent> poll() {
        final List<FileEvent> events = new LinkedList<FileEvent>();
        final Set<String> deletions = new HashSet<String>(timestamps.keySet());

        SearchUtils.Action action = new SearchUtils.Action(){
            @Override
            public void perform(File file) {
                if (file.isDirectory()) return;
                String path = file.getPath();

                // Process additions
                if (isFileNew(path)) {
                    events.add(FileEvent.fileAdded(file));
                    return;
                }

                // Process deletions
                deletions.remove(path);

                // Process changed
                boolean timestampFlag = isTimestampChanged(path, file.lastModified());
                boolean sizeFlag = isFileSizeChanged(path, file.length());
                if (timestampFlag || sizeFlag) {
                    events.add(FileEvent.fileChanged(file));
                }
            }
        };
        SearchUtils.iterateTopDown(folder, action);

        for (String path : deletions) {
            events.add(FileEvent.fileDeleted(new File(path)));
        }

        if (firstPoll) {
            firstPoll = false;
            return Collections.emptyList();
        }

        return events;
    }
}
