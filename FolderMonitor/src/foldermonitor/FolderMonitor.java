/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldermonitor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

/**
 *
 * @author wangjerome
 */
public class FolderMonitor implements Runnable{

    //fold path to monitor
    private Path path;
 
    public FolderMonitor(Path path) {
        this.path = path;
    }
 
    // print the events, the affected file and send file name to service
    private void updateFile(WatchEvent<?> event) {
        WatchEvent.Kind<?> kind = event.kind();
        
        //create a new file
        if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
            Path pathCreated = (Path) event.context();
            System.out.println("Entry created:" + pathCreated);
            
            //send path name to service
            uploadFile(pathCreated.toAbsolutePath().toString());
            
        //delete a file
        } else if (kind.equals(StandardWatchEventKinds.ENTRY_DELETE)) {
            Path pathDeleted = (Path) event.context();
            System.out.println("Entry deleted:" + pathDeleted);
            
        //modify a file
        } else if (kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
            Path pathModified = (Path) event.context();
            System.out.println("Entry modified:" + pathModified);
        }
    }
 
    @Override
    public void run() {
        try {
            WatchService watchService = path.getFileSystem().newWatchService();
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
 
            // loop forever to watch directory
            while (true) {
                WatchKey watchKey;
                watchKey = watchService.take(); // this call is blocking until events are present
 
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
            System.out.println("interrupted. Goodbye");
            return;
        } catch (IOException ex) {
            ex.printStackTrace();  // don't do this in production code. Use a loggin framework
            return;
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        Path pathToWatch = FileSystems.getDefault().getPath("/Users/wangjerome/Desktop/foo");
        FolderMonitor dirWatcher = new FolderMonitor(pathToWatch);
        Thread dirWatcherThread = new Thread(dirWatcher);
        dirWatcherThread.start();
 
    }

    //service method reference
    private static void uploadFile(java.lang.String path) {
        service.UpdateFileService_Service service = new service.UpdateFileService_Service();
        service.UpdateFileService port = service.getUpdateFileServicePort();
        port.uploadFile(path);
    }


    
}
