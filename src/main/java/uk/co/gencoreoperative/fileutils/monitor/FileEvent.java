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

import java.io.File;

/**
 * Models a FileEvent which is used to signal this change to users of the FolderMonitor.
 *
 * @author Robert Wapshott
 */
public class FileEvent {
    public enum Type {
        ADD,
        CHANGED,
        DELETED
    }

    private File path;
    private Type type;

    public FileEvent(File path, Type type) {
        this.path = path;
        this.type = type;
    }

    public File getPath() {
        return path;
    }

    public Type getType() {
        return type;
    }

    public static FileEvent fileAdded(File path) {
        return new FileEvent(path, Type.ADD);
    }

    public static FileEvent fileChanged(File path) {
        return new FileEvent(path, Type.CHANGED);
    }

    public static FileEvent fileDeleted(File path) {
        return new FileEvent(path, Type.DELETED);
    }
}
