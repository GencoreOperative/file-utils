package uk.co.gencoreoperative.fileutils.monitor;

/**
 * FileMonitor is reponsible for managing the polling process and providing the caller
 * with the option to customise the polling process.
 */
public class FolderMonitor {
//    private File folder;
//    private int duration = 5;
//    private boolean started = false;
//    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
//    private Lookup lookup;
//
//    public FolderMonitor monitor(File folder) {
//        this.folder = folder;
//        return this;
//    }
//
//    public FolderMonitor every(int seconds) {
//        duration = seconds;
//        return this;
//    }
//
//    /**
//     * Define the Lookup which will be used for signalling file changes.
//     * @param lookup Non null lookup.
//     * @return The FolderMonitor instance.
//     */
//    public FolderMonitor usingLookup(Lookup lookup) {
//        this.lookup = lookup;
//        return this;
//    }
//
//    public Lookup start() {
//        if (folder == null) {
//            throw new IllegalStateException("Folder must be assigned");
//        }
//
//        if (started) {
//            throw new IllegalStateException("Monitor is already started");
//        }
//
//        if (lookup == null) {
//            lookup = new Lookup();
//        }
//
//        Runnable poller = new Runnable() {
//            private FilePoller filePoller = new FilePoller(folder);
//            private Lookup.View<FileEvent> view = lookup.getView(FileEvent.class);
//            @Override
//            public void run() {
//                view.replaceAllWith(filePoller.poll());
//            }
//        };
//
//        executorService.scheduleAtFixedRate(poller, 0l, (long)duration, TimeUnit.SECONDS);
//        return lookup;
//    }
//
//    public void stop() {
//        executorService.shutdown();
//    }
}
