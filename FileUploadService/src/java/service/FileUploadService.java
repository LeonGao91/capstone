/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author wangjerome
 */
@WebService(serviceName = "FileUploadService")
public class FileUploadService {

    public static final Properties properties = new Properties();

    static {
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
        
        System.err.println("xxxxxxxxxxxxxxxxxxx");
        
        new Thread(new FolderMonitor(new File(properties.getProperty("uploadPath")).toPath())).start();
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "uploadFile")
    @Oneway
    public void uploadFile(@WebParam(name = "path") String path) {
        System.err.println("FileUploadService: get file " + path);
        FileInformation fileInfo = ReadHDF5File.getFileInformation(path);

        String targetPath = properties.getProperty("mainFolder") + "/" + fileInfo.customerID + "/"
                + fileInfo.platformType + "/" + fileInfo.Interface + "/"
                + fileInfo.platformID + "/" + fileInfo.shortFileName;

        File file = new File(targetPath);

        System.err.println("FileUploadService: target file " + targetPath);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try {
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(FileUploadService.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.err.println("FileUploadService: file constructed");

        try {
            Files.move(new File(path).toPath(), file.toPath(), REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(FileUploadService.class.getName()).log(Level.SEVERE, null, ex);
        }

        String baseFileName = properties.getProperty("mainFolder") + "/" + fileInfo.customerID + "/"
                + fileInfo.platformType + "/" + fileInfo.Interface + "/"
                + fileInfo.platformID + "/" + "base_file.h5";

        String dataFileName = properties.getProperty("mainFolder") + "/" + fileInfo.customerID + "/"
                + fileInfo.platformType + "/" + fileInfo.Interface + "/"
                + "data_file.h5";

        ReadHDF5File.combineBaseFile(file.getAbsolutePath(), baseFileName);
        ReadHDF5File.combineDataFile(file.getAbsolutePath(), dataFileName, fileInfo);

        Test test = ReadHDF5File.getResult(dataFileName, fileInfo.customerID, fileInfo.platformID);
        
        ResultExporter.output(test, properties.getProperty("mainFolder"));
    }


    /**
     * Web service operation
     */
    @WebMethod(operationName = "getSummary")
    public String getSummary(@WebParam(name = "file") String customerID) {
        String xml = "";
        File f = new File(properties.getProperty("mainFolder") + "/" + customerID + "/summary.xml");
        try {
            Scanner s = new Scanner(f);
            while(s.hasNextLine()){
                xml = xml.concat(s.nextLine());
            }
        } catch (FileNotFoundException ex) {
            System.out.println("file not found");
            return "";
        }
        return xml;

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getDetail")
    public String getDetail(@WebParam(name = "customerID") String customerID, @WebParam(name = "file") String file) {
        System.out.println(file);
        
        String xml = "";
        File f = new File(properties.getProperty("mainFolder") + "/" + customerID + "/result/" + file);
        
        if (!f.exists()) {
            return "";
        }
        
        try {
            Scanner s = new Scanner(f);
            while(s.hasNextLine()){
                String str = s.nextLine();
                System.out.println(str);
                xml = xml.concat(str);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("file not found");
        }
        
        return xml;
    }

}
