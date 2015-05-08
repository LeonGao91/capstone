package service;

import dataAnalyticsModel.ResultExporter;
import dataAnalyticsModel.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Used to upload service and return xml concerning test result
 * @author wangjerome
 */
@WebService(serviceName = "FileUploadService")
public class FileUploadService {

    /**
     * properties concerning directory paths
     */      
    public static final Properties properties = new Properties();
    
    // information of uploaded data file and its information, used to 
    // send message to CheckTimeThread, and the thread will decide when to
    // run test again which data
    private static final ConcurrentHashMap<String, PlatformInformation> map 
            = new ConcurrentHashMap<String, PlatformInformation>();

    // start the threads when class loaded
    static {
        
        // open the properties file
        try {
            File file = new File("setup.properties");
            FileInputStream fileInput = new FileInputStream(file);
            properties.load(fileInput);
            fileInput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // start folder monitor thread and check time thread
        try {
            new Thread(new FolderMonitor(new File(properties.getProperty("uploadPath")).toPath())).start();
            new Thread(new CheckTimeThread(map)).start();
        } catch(Exception e) {
            System.err.println("failed to find" );
            File file = new File(".");
            System.out.println(file.getAbsoluteFile());
        }
        
        
    }

    /**
     * When a file is uploaded into the specific folder, this method will be
     * called to move the file to right location and combine it with other files
     * into base file and data file
     * @param path 
     *          file path
     */
    public static void uploadFile(String path) {
        
        System.out.println("!!!!!!!! FileUploadService: get file " + path);
        
        // get the file information
        FileInformation fileInfo = ReadHDF5File.getFileInformation(path);

        // set up the path where this file should be move to
        String targetPath = properties.getProperty("mainFolder") + "/" + fileInfo.customerID + "/"
                + fileInfo.platformType + "/" + fileInfo.Interface + "/"
                + fileInfo.platformID + "/" + fileInfo.shortFileName;
        
        // if there is already such file, return
        if (new File(targetPath).exists()) {
            return;
        }

        
        File file = new File(targetPath);
        
        System.out.println("!!!!!!!! FileUploadService: target file " + targetPath);

        // copy the file
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(FileUploadService.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Files.move(new File(path).toPath(), file.toPath(), REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(FileUploadService.class.getName()).log(Level.SEVERE, null, ex);
        }

        // base file path
        String baseFileName = properties.getProperty("mainFolder") + "/" + fileInfo.customerID + "/"
                + fileInfo.platformType + "/" + fileInfo.Interface + "/"
                + fileInfo.platformID + "/" + "base_file.h5";

        // data file path
        String dataFileName = properties.getProperty("mainFolder") + "/" + fileInfo.customerID + "/"
                + fileInfo.platformType + "/" + fileInfo.Interface + "/"
                + "data_file.h5";

        // combine the file to base file
        ReadHDF5File.combineBaseFile(file.getAbsolutePath(), baseFileName);
        // combine the file to data file
        ReadHDF5File.combineDataFile(file.getAbsolutePath(), dataFileName, fileInfo);
        
        // update the map
        if (map.contains(dataFileName)) {
            map.get(dataFileName).timeUploaded = System.currentTimeMillis();
        } else {
            PlatformInformation pi = new PlatformInformation();
            pi.customerID = fileInfo.customerID;
            pi.datafile = dataFileName;
            pi.mainFolder = properties.getProperty("mainFolder");
            pi.productType = fileInfo.platformType;
            pi.timeUploaded = System.currentTimeMillis();
            map.put(dataFileName, pi);
        }
        
        System.out.println("\"!!!!!!!! FileUploadService: upload file finished");

    }

    /**
     * Web service operation, used to get summary file of a particular customer id
     * @param customerID
     *          customer id
     * @return 
     *          xml file contains test information of the customer
     */
    @WebMethod(operationName = "getSummary")
    public String getSummary(@WebParam(name = "customerID") String customerID) {
        
        // get the file
        File f = new File(properties.getProperty("mainFolder") + "/" + customerID + "/summary.xml");
        System.out.println("!!!!!!!!!! get file " + f.getAbsolutePath());
        
        StringBuilder xml = new StringBuilder();
        
        // append the file
        try {
            Scanner s = new Scanner(f);
            while(s.hasNextLine()){
                xml = xml.append(s.nextLine());
            }
            s.close();
        } catch (Exception ex) {
            System.out.println("file not found");
            return "";
        }
        
        return xml.toString();

    }

    /**
     * Web service operation, get detail test information about a particular test
     * @param customerID
     *          customer id
     * @param file
     *          file name
     * @return 
     *          xml file contains particular test information
     */
    @WebMethod(operationName = "getDetail")
    public String getDetail(@WebParam(name = "customerID") String customerID, @WebParam(name = "file") String file) {
        
        
        File f = new File(properties.getProperty("mainFolder") + "/" + customerID + "/result/" + file);
        System.out.println("!!!!!!!!!! get file " + f.getAbsolutePath());
   
        // if file not exist
        if (!f.exists()) {
            return "";
        }
        
        // copy all the file
        StringBuilder xml = new StringBuilder();
        
        try {
            Scanner s = new Scanner(f);
            while(s.hasNextLine()){
                String str = s.nextLine();
                xml = xml.append(str);
            }
        } catch (Exception ex) {
            System.out.println("file not found");
        }
        
        return xml.toString();
    }

}
