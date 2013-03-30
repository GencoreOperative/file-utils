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
 * FilePoller is responsible for keeping track of files in a folder. If a file changes the caller
 * will be notified by the Collection of FileEvents returned between successive calls to the poll
 * function.
 *
 * Note: This implementation will maintain in-memory maps which will track the file size and
 * last modified timestamps. These will allow the FilePoller to determine whether the file
 * has changed between successive calls. This however does mean that the implementation is
 * limited to a reasonable number of files.
 *
 * Note: Sizing has not yet been determined.
 *
 * @author Robert Wapshott
 */
public class FilePoller {
    private Map<String, Long> timestamps = new HashMap<String, Long>();
    private Map<String, Long> sizes = new HashMap<String, Long>();
    private boolean firstPoll = true;

    private File folder;

    public FilePoller(File folder) {
        if (!folder.isDirectory()) throw new IllegalStateException("must be a folder");
        this.folder = folder;
    }

    /**
     * @return Return all paths that are currently being tracked.
     */
    private Set<String> getAllFilePaths() {
        return timestamps.keySet();
    }

    /**
     * Checks if the file has already been added, and if not, adds the file to the maps.
     *
     * @param file File to check.
     * @return True if the file is not currently tracked by the FilePoller.
     */
    private boolean isFileNew(File file) {
        String path = file.getPath();

        if (!timestamps.containsKey(path)) {
            addPath(file);
            return true;
        }
        return false;
    }

    /**
     *
     * @param file
     * @return
     */
    private boolean isFileChanged(File file) {
        String path = file.getPath();
        long timestamp = file.lastModified();
        long size = file.length();

        boolean changed = false;
        if (timestamps.put(path, timestamp) != timestamp) {
            changed = true;
        }

        if (sizes.put(path, size) != size) {
            changed = true;
        }

        return changed;
    }

    private void addPath(File file) {
        long lastModified = file.lastModified();
        long size = file.length();
        String path = file.getPath();
        timestamps.put(path, lastModified);
        sizes.put(path, size);
    }

    private void removePath(File file) {
        String path = file.getPath();
        timestamps.remove(path);
        sizes.remove(path);
    }

    /**
     * Perform a poll of the files that are covered by this FilePoller.
     *
     * If this is the first poll, then no changes will be reported. Any subsequent polls
     * will indicate the changes that have taken place in the folder and sub folders
     * of the file system being monitored.
     *
     * @return A non null collection of FileEvents indicating changes that have taken place.
     */
    public List<FileEvent> poll() {
        final List<FileEvent> events = new LinkedList<FileEvent>();
        final Set<String> deletions = new HashSet<String>(getAllFilePaths());

        SearchUtils.Action action = new SearchUtils.Action(){
            @Override
            public void perform(File file) {
                if (file.isDirectory()) return;
                String path = file.getPath();

                // Process additions
                if (isFileNew(file)) {
                    events.add(FileEvent.fileAdded(file));
                    return;
                }

                // The file exists, we don't need to track its deletion
                deletions.remove(path);

                // Process changed
                if (isFileChanged(file)) {
                    events.add(FileEvent.fileChanged(file));
                }
            }
        };
        SearchUtils.iterateTopDown(folder, action);

        for (String path : deletions) {
            File file = new File(path);
            removePath(file);
            events.add(FileEvent.fileDeleted(file));
        }

        if (firstPoll) {
            firstPoll = false;
            return Collections.emptyList();
        }

        return events;
    }
}
