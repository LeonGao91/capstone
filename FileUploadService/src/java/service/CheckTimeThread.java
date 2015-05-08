package service;

import dataAnalyticsModel.ResultExporter;
import dataAnalyticsModel.Test;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static service.FileUploadService.properties;

/**
 * This thread is used to check if there is any pending data file has not been
 * tested, it will periodically check and if there is, it will run a test
 * against the data file
 *
 * @author wangjerome
 */
public class CheckTimeThread implements Runnable {

    //this hash map is shared between the service and this thread
    //when a file comes, it will put its map into this map
    private ConcurrentHashMap<String, PlatformInformation> map;

    /**
     * construct thread with a map, this thread will periodically check any new
     * item into this map, and run test against them
     *
     * @param map key is the combined data file path, value is platform
     * information, check the PlatformInformation class
     */
    CheckTimeThread(ConcurrentHashMap<String, PlatformInformation> map) {
        this.map = map;
    }

    /**
     * periodically check and if there is new items in the map
     */
    @Override
    public void run() {

        while (true) {

            try {
                //for each loop, wait 30 seconds, the value should be adjusted
                //or read from  properties file
                Thread.sleep(30000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CheckTimeThread.class.getName())
                        .log(Level.SEVERE, null, ex);
            }

            //go through items in the map
            for (String dataFileName : map.keySet()) {

                //if it has been more than 60 minutes since last upload
                //of a particular platform data file
                if (System.currentTimeMillis()
                        - map.get(dataFileName).timeUploaded > 60000) {

                    PlatformInformation pi = map.get(dataFileName);

                    //run test against the combinded data file
                    Test test = ReadHDF5File.
                            getResult(pi.datafile, pi.customerID,
                                    pi.productType, pi.mainFolder);

                    //output the result
                    ResultExporter.output(test, pi.mainFolder);

                    map.remove(dataFileName);
                    System.out.println("******* Test build " 
                            + dataFileName + "********");
                    
                    //do one test generate for one time
                    break;
                }

            }
        }

    }

}
