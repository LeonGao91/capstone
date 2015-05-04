/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dataAnalyticsModel.ResultExporter;
import dataAnalyticsModel.Test;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static service.FileUploadService.properties;

/**
 *
 * @author wangjerome
 */
public class CheckTimeThread implements Runnable {

    ConcurrentHashMap<String, PlatformInformation> map;

    public CheckTimeThread(ConcurrentHashMap<String, PlatformInformation> map) {
        this.map = map;
    }

    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CheckTimeThread.class.getName()).log(Level.SEVERE, null, ex);
            }

            for (String dataFileName : map.keySet()) {
                if (System.currentTimeMillis() - map.get(dataFileName).timeUploaded > 60000) {
                    PlatformInformation pi = map.get(dataFileName);
                    Test test = ReadHDF5File.getResult(pi.datafile, pi.customerID, pi.productType, pi.mainFolder);
                    System.out.println(test.toString());
                    ResultExporter.output(test, pi.mainFolder);
                    map.remove(dataFileName);
                    System.out.println("*******Constructed " + dataFileName);
                    break;
                }
                
            }
            
            System.out.println("**********No test object constructed");
            
        }

    }

}
