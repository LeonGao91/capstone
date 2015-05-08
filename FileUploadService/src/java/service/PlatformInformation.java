package service;

/**
 * This class is used contain data file information of combined data file
 * @author wangjerome
 */
public class PlatformInformation {
    
    // data file path
    String datafile;
    
    String customerID;
    String productType;
    
    // main folder path
    String mainFolder;
    
    // last modified time, get by System.currentTimeMillis()
    long timeUploaded;
}
