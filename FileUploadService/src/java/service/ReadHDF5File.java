package service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.H5File;
import dataAnalyticsModel.*;
import static service.FileUploadService.properties;

/**
 * This class is used to read HDF5 file for a particular HDF5 file of platform
 * DRAM, it is very platform specific, and this is the main part to change for
 * other platforms
 *
 * @author wangjerome
 */
public class ReadHDF5File {

    // a helper variable used in HDF5 file operation
    // indicate the dimention of the data set is 1 * 1
    static private long[] dims = {1};

    // 8 direction names of the data, it should match the name in HDF5 file
    static private String[] directions = {"rxDqLeft", "rxDqRight", "rxVrefHigh",
        "rxVrefLow", "txDqLeft", "txDqRight", "txVrefHigh", "txVrefLow"};
    
    // check if the file is dqMarginEnabled
    private static boolean dqMarginEnabled = true;

    /**
     * get file information of a particular HDF5 file of DRAM platform
     *
     * @param path path to target file
     * @return file information concerning the file
     */
    public static FileInformation getFileInformation(String path) {

        FileInformation fileInfo = new FileInformation();

        //short version of the file name
        String fileName = path.substring(path.lastIndexOf('/') + 1);

        fileInfo.shortFileName = fileName;

        //file name contains information of customerID, interface and timestamp
        //they are concatenated by "-"
        fileInfo.customerID = fileName.substring(0, fileName.indexOf('-'));

        fileName = fileName.substring(fileName.indexOf('-') + 1);
        fileInfo.Interface = fileName.substring(0, fileName.indexOf('-'));

        fileName = fileName.substring(fileName.indexOf('-') + 1);
        fileInfo.timestamp = fileName.substring(0, fileName.indexOf('.'));

        //open the HDF5 file
        FileFormat fileFormat = FileFormat.getFileFormat(
                FileFormat.FILE_TYPE_HDF5);
        FileFormat testFile = null;
        try {
            testFile = fileFormat.createInstance(path, FileFormat.READ);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //get attributes of the root group
            testFile.open();
            Group root
                    = (Group) ((javax.swing.tree.DefaultMutableTreeNode) testFile.getRootNode()).getUserObject();
            List attrList = root.getMetadata();

            Attribute attr = null;

            //go through all the attributes
            for (int i = 0; i < attrList.size(); i++) {
                attr = (Attribute) attrList.get(i);

                // platform type
                if (attr.getName().equals("Platform Type")) {
                    fileInfo.platformType = "";
                    // the value is a string array of size 1
                    String[] temp = (String[]) attr.getValue();
                    for (int j = 0; j < temp.length; j++) {
                        fileInfo.platformType += temp[j];
                    }
                }

                //target platform ID
                if (attr.getName().equals("Target Platform ID")) {
                    fileInfo.platformID = "";
                    String[] temp = (String[]) attr.getValue();
                    for (int j = 0; j < temp.length; j++) {
                        fileInfo.platformID += temp[j];
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // replace ":" in platform ID to "_" since files in Linux cannot
        // have charactor ":"
        if (fileInfo.platformID != null) {
            fileInfo.platformID = fileInfo.platformID.replace(':', '_');
        }

        //close file
        try {
            testFile.close();
        } catch (Exception ex) {
            Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE,
                    null, ex);
        }

        return fileInfo;
    }

    /**
     * used to combine the file into the combined base file
     *
     * @param file single HDF5 file
     * @param baseFile combined file of all the single HDF5 of the same system
     */
    static public void combineBaseFile(String file, String baseFile) {

        FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
        FileFormat singleFileFormat = null;
        FileFormat baseFileFormat = null;
        Group fileRoot = null;
        Group baseRoot = null;

        // get the single file
        try {
            singleFileFormat = fileFormat.createInstance(file, FileFormat.READ);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // open the file and get the root group
        if (singleFileFormat != null) {
            try {
                singleFileFormat.open();
                fileRoot = (Group) ((javax.swing.tree.DefaultMutableTreeNode) singleFileFormat.getRootNode()).getUserObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // get base file root
        // if there is a base file already
        if (new File(baseFile).exists()) {
            try {
                baseFileFormat
                        = fileFormat.createInstance(baseFile, FileFormat.WRITE);

                // if this is not set up properly, limited number of group
                // can be loaded
                baseFileFormat.setMaxMembers(999999999);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (baseFileFormat != null) {
                try {
                    baseFileFormat.open();
                    baseRoot = (Group) ((javax.swing.tree.DefaultMutableTreeNode) baseFileFormat.getRootNode()).getUserObject();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {

            // if there is not base file yet, create the file
            try {
                baseFileFormat
                        = fileFormat.createFile(baseFile, FileFormat.FILE_CREATE_DELETE);
                baseFileFormat.setMaxMembers(999999999);
            } catch (Exception ex) {
                Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (baseFileFormat != null) {
                try {
                    baseFileFormat.open();
                } catch (Exception ex) {
                    Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
                }
                baseRoot = (Group) ((javax.swing.tree.DefaultMutableTreeNode) baseFileFormat.getRootNode()).getUserObject();
            }

        }

        // create a group using the single file name
        Group newGroup = null;
        try {
            newGroup
                    = baseFileFormat.createGroup(new File(file).getName(), baseRoot);
        } catch (Exception ex) {
            Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
        }

        // copy the whole file from single file to base file
        copy(singleFileFormat, baseFileFormat, fileRoot, newGroup);

        // close files
        try {
            singleFileFormat.close();
            baseFileFormat.close();
        } catch (Exception ex) {
            Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Used to combine useful data from files of the same platform
     *
     * @param file single file path
     * @param baseFile combined data file path
     * @param fileInfo information of the single file
     */
    static public void combineDataFile(String file, String baseFile,
            FileInformation fileInfo) {

        FileFormat fileFormat
                = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
        FileFormat singleFileFormat = null;
        FileFormat baseFileFormat = null;
        Group fileRoot = null;
        Group baseRoot = null;

        // get the single file
        try {
            singleFileFormat = fileFormat.createInstance(file, FileFormat.READ);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // open the file
        if (singleFileFormat != null) {
            try {
                singleFileFormat.open();
                fileRoot = (Group) ((javax.swing.tree.DefaultMutableTreeNode) singleFileFormat.getRootNode()).getUserObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //get base file root
        if (new File(baseFile).exists()) {
            try {
                baseFileFormat = fileFormat.createInstance(baseFile, FileFormat.WRITE);
                baseFileFormat.setMaxMembers(999999999);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (baseFileFormat != null) {
                try {
                    baseFileFormat.open();
                    baseRoot = (Group) ((javax.swing.tree.DefaultMutableTreeNode) baseFileFormat.getRootNode()).getUserObject();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                baseFileFormat = fileFormat.createFile(baseFile, FileFormat.FILE_CREATE_DELETE);
                baseFileFormat.setMaxMembers(999999999);
            } catch (Exception ex) {
                Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (baseFileFormat != null) {
                try {
                    baseFileFormat.open();
                } catch (Exception ex) {
                    Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
                }
                baseRoot = (Group) ((javax.swing.tree.DefaultMutableTreeNode) baseFileFormat.getRootNode()).getUserObject();
            }

        }

        // key is the direction name, value is the current group of combined
        // data file, as the copy goes deep, new group will change and be 
        // updated in the map
        Map<String, Group> groups = new HashMap<String, Group>();

        // if there is no direction groups yet in the root group of combined 
        // data file, create the 8 direction group
        if (baseRoot.getMemberList().size() < directions.length) {

            for (String direction : directions) {
                try {
                    Group dirGroup
                            = baseFileFormat.createGroup(direction, baseRoot);
                    groups.put(direction, dirGroup);
                } catch (Exception ex) {
                    Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            // if there is already direction group, get the group
            for (HObject group : baseRoot.getMemberList()) {
                groups.put(group.getName(), (Group) group);
            }
        }

        //create system group
        outer:
        for (String dir : groups.keySet()) {
            Group g = groups.get(dir);

            //check if the system already exist
            for (HObject system : g.getMemberList()) {
                if (system.getName().equals(fileInfo.platformID)) {
                    groups.put(dir, (Group) system);
                    continue outer;
                }

            }

            //not exist, create system group
            try {
                Group newGroup = baseFileFormat.createGroup(fileInfo.platformID, g);
                groups.put(dir, newGroup);
            } catch (Exception ex) {
                Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //create repeat groups
        for (String dir : groups.keySet()) {

            Group system = groups.get(dir);

            try {
                Group newGroup = baseFileFormat.createGroup(fileInfo.timestamp, system);
                groups.put(dir, newGroup);
            } catch (Exception ex) {
                Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // selective copy data from single file to combined data file
        copyData(singleFileFormat, baseFileFormat, fileRoot, groups);

        try {
            singleFileFormat.close();
            baseFileFormat.close();
        } catch (Exception ex) {
            Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * copy a group recursively from one file to another file
     *
     * @param ff file to copy from
     * @param tf file to copy to
     * @param fg file group to copy from
     * @param tg file group to copy to
     */
    private static void copy(FileFormat ff, FileFormat tf, Group fg, Group tg) {

        // go through all the members of the group
        for (HObject object : fg.getMemberList()) {

            //if it is dataset
            if (object instanceof Dataset) {

                // data set of copy from
                Dataset dataset = (Dataset) object;
                // data type of the from data set
                Datatype datatype = dataset.getDatatype();

                // data type to copy to
                Datatype totype = null;
                //  data set to copy to
                Dataset toset = null;

                // create data type
                try {
                    totype = tf.createDatatype(datatype.getDatatypeClass(),
                            datatype.getDatatypeSize(),
                            datatype.getDatatypeOrder(),
                            datatype.getDatatypeSign());
                } catch (Exception ex) {
                    Logger.getLogger(ReadHDF5File.class.getName())
                            .log(Level.SEVERE, null, ex);
                }

                // create data type
                try {
                    toset = tf.createScalarDS(dataset.getName(), tg, totype,
                            dims, dataset.getMaxDims(), dataset.getChunkSize(),
                            0, dataset.read());
                } catch (Exception ex) {
                    Logger.getLogger(ReadHDF5File.class.getName())
                            .log(Level.SEVERE, null, ex);
                }

            }

            // if the member is another group
            if (object instanceof Group) {

                // group to copy from
                Group fGroup = (Group) object;

                try {
                    // create new group in the group to copy to
                    Group tGroup = tf.createGroup(object.getName(), tg);

                    // recursively run the method
                    copy(ff, tf, fGroup, tGroup);
                } catch (Exception ex) {
                    Logger.getLogger(ReadHDF5File.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    /**
     * recursively copy data from single file to combined file
     *
     * @param ff file to copy from
     * @param tf combined data file to copy to
     * @param fg group to copy from
     * @param tgs groups to copy to (groups of tf)
     */
    private static void copyData(FileFormat ff, FileFormat tf, Group fg, Map<String, Group> tgs) {

        // go through all the members of the group
        for (HObject object : fg.getMemberList()) {

            // for a group
            if (object instanceof Group) {

                // cast it to group
                Group fGroup = (Group) object;

                if (object.getName().startsWith("socketList")) { // for socketList group, go deep
                    copyData(ff, tf, fGroup, tgs);
                    break;
                } else if (object.getName().startsWith("socket_")) { // socket group, go deep
                    copyData(ff, tf, fGroup, tgs);
                    break;
                } else if (object.getName().startsWith("channel_")) { // channel group, create the group

                    //create channel group for each direction group
                    Map<String, Group> channels = new HashMap<String, Group>();

                    for (String dir : tgs.keySet()) {

                        Group repeat = tgs.get(dir);

                        try {
                            Group newGroup = tf.createGroup(object.getName(), repeat);
                            channels.put(dir, newGroup);
                        } catch (Exception ex) {
                            System.err.println("path:" + repeat.getName() + "object:" + object.getName());
                            Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    copyData(ff, tf, fGroup, channels);

                } else if (object.getName().startsWith("dimm_")) { // dimm group, go deep
                    copyData(ff, tf, fGroup, tgs);
                    break;
                } else if (object.getName().startsWith("rank_")) { // rank group, go deep

                    //create rank group for each channel
                    Map<String, Group> ranks = new HashMap<String, Group>();

                    //check if dq margin is enabled
                    dqMarginEnabled = checkdqMarginEnabled(object);
                    
                    System.out.println("dqMarginEnabled " + dqMarginEnabled);

                    for (String dir : tgs.keySet()) {

                        Group channel = tgs.get(dir);
                        try {
                            Group newGroup = tf.createGroup(object.getName(), channel);
                            ranks.put(dir, newGroup);
                        } catch (Exception ex) {
                            Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    copyData(ff, tf, fGroup, ranks);

                } else if (object.getName().equals("dqMargin") && dqMarginEnabled) { // dqMargin group, go deep
                    copyData(ff, tf, fGroup, tgs);
                    break;
                } else if (object.getName().equals("rankMargin") && !dqMarginEnabled) {
                    
                    for (HObject data : fGroup.getMemberList()) {
                        if (tgs.containsKey(data.getName())) {

                            Dataset dataset = (Dataset) data;
                            Datatype datatype = dataset.getDatatype();

                            // create data set under the group
                            Datatype totype = null;
                            Dataset toset = null;
                            try {
                                totype = tf.createDatatype(datatype.getDatatypeClass(),
                                        datatype.getDatatypeSize(),
                                        datatype.getDatatypeOrder(),
                                        datatype.getDatatypeSign());
                            } catch (Exception ex) {
                                Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                toset = tf.createScalarDS(object.getName(),
                                        tgs.get(data.getName()), totype, dims,
                                        dataset.getMaxDims(),
                                        dataset.getChunkSize(), 0,
                                        dataset.read());
                            } catch (Exception ex) {
                                Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    
                } else if (object.getName().startsWith("dqMargin_") && dqMarginEnabled) { // dqMargin_ group, copy data

                    //copy the 8 datas of 8 directions
                    for (HObject dir : fGroup.getMemberList()) {
                        if (tgs.containsKey(dir.getName())) {

                            Dataset dataset = (Dataset) dir;
                            Datatype datatype = dataset.getDatatype();

                            // create data set under the group
                            Datatype totype = null;
                            Dataset toset = null;
                            try {
                                totype = tf.createDatatype(datatype.getDatatypeClass(),
                                        datatype.getDatatypeSize(),
                                        datatype.getDatatypeOrder(),
                                        datatype.getDatatypeSign());
                            } catch (Exception ex) {
                                Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                toset = tf.createScalarDS(object.getName(),
                                        tgs.get(dir.getName()), totype, dims,
                                        dataset.getMaxDims(),
                                        dataset.getChunkSize(), 0,
                                        dataset.read());
                            } catch (Exception ex) {
                                Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            }

        }
    }

    /**
     * create result object from the combined data file
     *
     * @param datafile combined data file path
     * @param customerID customer id
     * @param productID product id
     * @param mainFolder main folder path
     * @return finished Test result
     */
    public static Test getResult(String datafile, String customerID,
            String productID, String mainFolder) {

        FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
        FileFormat singleFileFormat = null;

        // open the combined data file
        Group fileRoot = null;
        try {
            singleFileFormat = fileFormat.createInstance(datafile, FileFormat.READ);
            singleFileFormat.setMaxMembers(999999999);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // get the root group
        if (singleFileFormat != null) {
            try {
                singleFileFormat.open();
                fileRoot = (Group) ((javax.swing.tree.DefaultMutableTreeNode) singleFileFormat.getRootNode()).getUserObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // get the test result from combined data file
        Test test = getTest(fileRoot, customerID, productID, mainFolder);

        // close the file
        try {
            singleFileFormat.close();
        } catch (Exception ex) {
            Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
        }

        return test;
    }

    /**
     * get the test result from combined data file
     *
     * @param root root group
     * @param customerID customer id
     * @param productID product id
     * @param mainFolder main folder path
     * @return test result
     */
    private static Test getTest(Group root, String customerID, String productID, String mainFolder) {
        List<HObject> directions = root.getMemberList();

        TestDirection[] testdirections = new TestDirection[directions.size()];

        // for each direction group, build a test direction object
        for (int i = 0; i < directions.size(); i++) {
            testdirections[i] = getDirection((Group) directions.get(i));
        }

        // create test based on the TestDirecition array
        return new Test(testdirections, customerID, productID, mainFolder);
    }

    /**
     * Build TestDirection object from the direction group
     *
     * @param direction direction group
     * @return TestDirection object
     */
    private static TestDirection getDirection(Group direction) {
        List<HObject> systems = direction.getMemberList();

        // each TestDirection is composed of several TestSystem,
        TestSystem[] testsystems = new TestSystem[systems.size()];

        // get TestSystem array from the subgroups
        try {
            for (int i = 0; i < systems.size(); i++) {
                testsystems[i] = getSystem((Group) systems.get(i));
            }
        } catch (Exception e) {
            System.out.println(direction.getName());
        }

        // build the TestDirection from the systems array
        return new TestDirection(testsystems, direction.getName());
    }

    /**
     * Build TestSystem object from the system group
     *
     * @param system system group
     * @return TestSystem object
     */
    private static TestSystem getSystem(Group system) {
        List<HObject> repeats = system.getMemberList();

        // each TestSysem is composed of several TestRepeat
        TestRepeat[] testrepeats = new TestRepeat[repeats.size()];

        // build repeat groups, and put them into an TestRepeat array
        for (int i = 0; i < repeats.size(); i++) {
            try {
                TestLane[][][] repeat = getRepeat((Group) repeats.get(i));
                TestMarginsDDR margins = new TestMarginsDDR(repeat);
                testrepeats[i] = new TestRepeat(margins, ((Group) repeats.get(i)).getName());
            } catch (Exception e) {
                System.err.println("xxxxxxxxxx " + system.getName()
                        + " xxxxxxxxxx" + repeats.get(i).getName());
            }
        }

        return new TestSystem(testrepeats, system.getName());
    }

    /**
     * build three-dimension TestLane array from repeat group
     *
     * @param repeatg repeat group
     * @return three-dimension TestLane array
     */
    public static TestLane[][][] getRepeat(Group repeatg) {

        // the dimension size is number of channels
        TestLane[][][] repeat = new TestLane[repeatg.getMemberList().size()][][];
        List<HObject> channels = repeatg.getMemberList();

        // go through each channel to get a 2-dimensional array, and put into the result
        for (int i = 0; i < channels.size(); i++) {
            repeat[i] = getChannel((Group) channels.get(i));
        }

        return repeat;
    }

    /**
     * build two-dimension TestLane array from channel group
     *
     * @param channelg channel group
     * @return two-dimension TestLane array
     */
    public static TestLane[][] getChannel(Group channelg) {
        // the dimension size is number of ranks
        TestLane[][] channel = new TestLane[channelg.getMemberList().size()][];

        List<HObject> ranks = channelg.getMemberList();

        // go through each rank to get an array, and put into the result
        for (int i = 0; i < ranks.size(); i++) {
            if (ranks.get(i) instanceof Group) {
                channel[i] = getRank((Group) ranks.get(i), channelg.getName());
            }
        }

        return channel;
    }

    /**
     * build a TestLane array from rank group
     *
     * @param rankg rank group
     * @param channelID channel id (channel group name)
     * @return an array of TestLane
     */
    public static TestLane[] getRank(Group rankg, String channelID) {
        // the dimension size is number of lanes
        TestLane[] rank = new TestLane[rankg.getMemberList().size()];

        List<HObject> lanes = rankg.getMemberList();

        for (int i = 0; i < lanes.size(); i++) {
            //set value
            rank[i] = getValue((Dataset) lanes.get(i), channelID, rankg.getName());
        }

        return rank;
    }

    /**
     * get TestLane based on lane dataset
     *
     * @param data dataset to read from
     * @param channelID channel id
     * @param rankID rank id
     * @return TestLane object
     */
    public static TestLane getValue(Dataset data, String channelID, String rankID) {

        // read the value as byte array
        byte[] value = null;

        try {
            value = (byte[]) data.read();
        } catch (Exception ex) {
            Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OutOfMemoryError ex) {
            Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
        }

        // build the test lane object
        return new TestLane(value[0], true, channelID, rankID, data.getName());
    }

    /**
     * This method is used to check if the dq margin enabled flag is on or off
     * by checking the rank group
     *
     * @param object rank group
     * @return if the dqMarginEnable dataset is 1, if 1 return true, 0 return
     * false
     */
    private static boolean checkdqMarginEnabled(HObject object) {
        Group rank = (Group) object;

        for (HObject o : rank.getMemberList()) {
            if (o.getName().equals("dqMarginEnabled")) {
                
                // read the value as byte array
                byte[] value = null;

                try {
                    value = (byte[]) ((Dataset)o).read();
                } catch (Exception ex) {
                    Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
                } catch (OutOfMemoryError ex) {
                    Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                // if value is 1, return true
                return value[0] == 1;
            }
        }
        
        return false;

    }

}
