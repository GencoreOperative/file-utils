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
