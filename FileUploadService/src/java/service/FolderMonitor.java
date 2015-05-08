package service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This thread is used to monitor a particular folder, if there is any file put
 * into the folder, it will send the name of the file to FileUploadService
 *
 * @author wangjerome
 */
public class FolderMonitor implements Runnable {

    //folder path to monitor
    private Path path;

    /**
     * This class is used to build a thread which will monitor a folder,
     * whenever a file is uploaded, it will send the name to the service
     *
     * @param path folder to monitor
     */
    public FolderMonitor(Path path) {
        this.path = path;
    }

    // send the uploaded file name to FileUploadService
    private void updateFile(WatchEvent<?> event) {

        // kind of file event
        WatchEvent.Kind<?> kind = event.kind();

        // create a new file
        if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
            Path pathCreated = (Path) event.context();
            System.out.println("Entry created:" + pathCreated);

            // send path name to service
            try {
                // wait for the whole file is already uploaded
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(FolderMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // send the file name to service
            FileUploadService.uploadFile(path + "/" + pathCreated);

        }
    }

    /**
     * start monitoring the folder, and check if there is any file event
     */
    @Override
    public void run() {
        
        try {
            // register to the path to monitor if there is any new event
            WatchService watchService = path.getFileSystem().newWatchService();
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY, 
                    StandardWatchEventKinds.ENTRY_DELETE);

            // loop forever to watch directory
            while (true) {
                WatchKey watchKey;
                // this call is blocking until events are present
                watchKey = watchService.take(); 

                // poll for file system events on the WatchKey
                for (final WatchEvent<?> event : watchKey.pollEvents()) {
                    updateFile(event);
                }
                
                // if the watched directed gets deleted, get out of run method
                if (!watchKey.reset()) {
                    System.out.println("No longer valid");
                    watchKey.cancel();
                    watchService.close();
                    break;
                }

            }

        } catch (InterruptedException ex) {
            System.out.println("interrupted");
            return;
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
    }

}
