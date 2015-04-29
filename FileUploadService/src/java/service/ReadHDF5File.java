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

public class ReadHDF5File {

    static long[] dims = {1};
    static String[] directions = {"rxDqLeft", "rxDqRight", "rxVrefHigh", "rxVrefLow", "txDqLeft", "txDqRight", "txVrefHigh", "txVrefLow"};

    static {
//        try {
////            addDir(FileUploadService.properties.getProperty("dyLinkPath"));
//        } catch (IOException ex) {
//            Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public static FileInformation getFileInformation(String path) {

        FileInformation fileInfo = new FileInformation();

        String fileName = path.substring(path.lastIndexOf('/') + 1);

        fileInfo.shortFileName = fileName;
        fileInfo.customerID = fileName.substring(0, fileName.indexOf('-'));

        fileName = fileName.substring(fileName.indexOf('-') + 1);
        fileInfo.Interface = fileName.substring(0, fileName.indexOf('-'));

        fileName = fileName.substring(fileName.indexOf('-') + 1);
        fileInfo.timestamp = fileName.substring(0, fileName.indexOf('.'));

        FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
        FileFormat testFile = null;

        try {
            testFile = fileFormat.createInstance(path, FileFormat.READ);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (testFile != null) {
            try {
                testFile.open();
                Group root = (Group) ((javax.swing.tree.DefaultMutableTreeNode) testFile.getRootNode()).getUserObject();

                List attrList = root.getMetadata();
                Attribute attr = null;

                for (int i = 0; i < attrList.size(); i++) {
                    attr = (Attribute) attrList.get(i);

                    if (attr.getName().equals("Platform Type")) {
                        fileInfo.platformType = "";
                        String[] temp = (String[]) attr.getValue();
                        for (int j = 0; j < temp.length; j++) {
                            fileInfo.platformType += temp[j];
                        }
                    }

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
        }

        if (fileInfo.platformID != null) {
            fileInfo.platformID = fileInfo.platformID.replace(':', '_');
        }

        try {
            testFile.close();
        } catch (Exception ex) {
            Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
        }

        return fileInfo;
    }

    /**
     * copy a group recursively from one file to another file
     *
     * @param ff
     * @param tf
     * @param fg
     * @param tg
     */
    private static void copy(FileFormat ff, FileFormat tf, Group fg, Group tg) {
        for (HObject object : fg.getMemberList()) {
            if (object instanceof Dataset) {
                System.out.println("dataset:" + object);
                Dataset dataset = (Dataset) object;
                Datatype datatype = dataset.getDatatype();

                Datatype totype = null;
                Dataset toset = null;
                try {
                    totype = tf.createDatatype(datatype.getDatatypeClass(), datatype.getDatatypeSize(), datatype.getDatatypeOrder(), datatype.getDatatypeSign());
                } catch (Exception ex) {
                    Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    System.out.println(dataset.getName());
                    toset = tf.createScalarDS(dataset.getName(), tg, totype, dims, dataset.getMaxDims(), dataset.getChunkSize(), 0, dataset.read());
                } catch (Exception ex) {
                    Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            if (object instanceof Group) {
                System.out.println("group:" + object);
                Group fGroup = (Group) object;

                try {
                    Group tGroup = tf.createGroup(object.getName(), tg);
                    copy(ff, tf, fGroup, tGroup);
                } catch (Exception ex) {
                    Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    static void combineBaseFile(String file, String baseFile) {

        //get single file root
        FileInformation fileInfo = new FileInformation();

        FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
        FileFormat singleFileFormat = null;
        FileFormat baseFileFormat = null;
        Group fileRoot = null;
        Group baseRoot = null;

        try {
            singleFileFormat = fileFormat.createInstance(file, FileFormat.READ);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        Group newGroup = null;
        try {
            newGroup = baseFileFormat.createGroup(new File(file).getName(), baseRoot);
        } catch (Exception ex) {
            Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
        }

        copy(singleFileFormat, baseFileFormat, fileRoot, newGroup);

        try {
            singleFileFormat.close();
            baseFileFormat.close();
        } catch (Exception ex) {
            Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    static void combineDataFile(String file, String baseFile, FileInformation fileInfo) {

        FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
        FileFormat singleFileFormat = null;
        FileFormat baseFileFormat = null;
        Group fileRoot = null;
        Group baseRoot = null;

        try {
            singleFileFormat = fileFormat.createInstance(file, FileFormat.READ);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        Map<String, Group> groups = new HashMap<String, Group>();

        //get 8 direction group
        if (baseRoot.getMemberList().size() < directions.length) {
            for (String direction : directions) {
                try {
                    Group dirGroup = baseFileFormat.createGroup(direction, baseRoot);
                    groups.put(direction, dirGroup);
                } catch (Exception ex) {
                    Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
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

        copyData(singleFileFormat, baseFileFormat, fileRoot, groups);

        try {
            singleFileFormat.close();
            baseFileFormat.close();
        } catch (Exception ex) {
            Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void copyData(FileFormat ff, FileFormat tf, Group fg, Map<String, Group> tgs) {
        for (HObject object : fg.getMemberList()) {

            if (object instanceof Group) {
                Group fGroup = (Group) object;

                if (object.getName().startsWith("socketList")) {
                    copyData(ff, tf, fGroup, tgs);
                    break;
                } else if (object.getName().startsWith("socket_")) {
                    copyData(ff, tf, fGroup, tgs);
                    break;
                } else if (object.getName().startsWith("channel_")) {
                    //create channel group
                    Map<String, Group> channels = new HashMap<String, Group>();

                    for (String dir : tgs.keySet()) {

                        Group repeat = tgs.get(dir);

                        try {
                            Group newGroup = tf.createGroup(object.getName(), repeat);
                            channels.put(dir, newGroup);
                        } catch (Exception ex) {
                            Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    copyData(ff, tf, fGroup, channels);

                } else if (object.getName().startsWith("dimm_")) {
                    copyData(ff, tf, fGroup, tgs);
                    break;
                } else if (object.getName().startsWith("rank_")) {

                    //create rank group
                    Map<String, Group> ranks = new HashMap<String, Group>();

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

                } else if (object.getName().equals("dqMargin")) {
                    copyData(ff, tf, fGroup, tgs);
                    break;
                } else if (object.getName().startsWith("dqMargin_")) {

                    //copy data
                    for (HObject dir : fGroup.getMemberList()) {
                        if (tgs.containsKey(dir.getName())) {

                            Dataset dataset = (Dataset) dir;
                            Datatype datatype = dataset.getDatatype();

                            Datatype totype = null;
                            Dataset toset = null;
                            try {
                                totype = tf.createDatatype(datatype.getDatatypeClass(), datatype.getDatatypeSize(), datatype.getDatatypeOrder(), datatype.getDatatypeSign());
                            } catch (Exception ex) {
                                Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                System.out.println(dataset.getName());
                                toset = tf.createScalarDS(object.getName(), tgs.get(dir.getName()), totype, dims, dataset.getMaxDims(), dataset.getChunkSize(), 0, dataset.read());
                            } catch (Exception ex) {
                                Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            }

        }
    }
    
    public static Test getResult(String datafile, String customerID, String productID) {

        FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
        FileFormat singleFileFormat = null;
        Group fileRoot = null;

        try {
            singleFileFormat = fileFormat.createInstance(datafile, FileFormat.READ);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (singleFileFormat != null) {
            try {
                singleFileFormat.open();
                fileRoot = (Group) ((javax.swing.tree.DefaultMutableTreeNode) singleFileFormat.getRootNode()).getUserObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Test test = getTest(fileRoot, customerID, productID);
        
        System.out.println(test.toString());

        try {
            singleFileFormat.close();
        } catch (Exception ex) {
            Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return test;
    }
    
    public static Test getTest(Group root, String customerID, String productID) {
        List<HObject> directions = root.getMemberList();
        TestDirection[] testdirections = new TestDirection[directions.size()];
        
        for (int i = 0; i < directions.size(); i++) {
            testdirections[i] = getDirection((Group)directions.get(i));
        }
        
        return new Test(testdirections, customerID, productID);
    }
    
    private static TestDirection getDirection(Group direction) {
        List<HObject> systems = direction.getMemberList();
        TestSystem[] testsystems = new TestSystem[systems.size()];
        
        for (int i = 0; i < systems.size(); i++) {
            testsystems[i] = getSystem((Group)systems.get(i));
        }
        
        return new TestDirection(testsystems, direction.getName());
    }
    
    private static TestSystem getSystem(Group system) {
        List<HObject> repeats = system.getMemberList();
        TestRepeat[] testrepeats = new TestRepeat[repeats.size()];
        
        for (int i = 0; i < repeats.size(); i++) {
            TestLane[][][] repeat = getRepeat((Group)repeats.get(i));
            TestMarginsDDR margins = new TestMarginsDDR(repeat);
            testrepeats[i] = new TestRepeat(margins, ((Group)repeats.get(i)).getName());
        }
        
        return new TestSystem(testrepeats, system.getName());
    }
    
    
    public static TestLane[][][] getRepeat(Group repeatg) {
        TestLane[][][] repeat = new TestLane[repeatg.getMemberList().size()][][];
        List<HObject> channels = repeatg.getMemberList();
        
        for (int i = 0; i < channels.size(); i++) {
            repeat[i] = getChannel((Group)channels.get(i));
        }
        
        return repeat;
    }
    
    public static TestLane[][] getChannel(Group channelg) {
        TestLane[][] channel = new TestLane[channelg.getMemberList().size()][];
        
        List<HObject> ranks = channelg.getMemberList();
        
        for (int i = 0; i < ranks.size(); i++) {
            if (ranks.get(i) instanceof Group) {
                channel[i] = getRank((Group)ranks.get(i), channelg.getName());
            } else {
                System.out.println(ranks.get(i).getName());
            }
            
        }
        
        return channel;
    }
    
    public static TestLane[] getRank(Group rankg, String channelID) {
        
        TestLane[] rank = new TestLane[rankg.getMemberList().size()];
        
        List<HObject> lanes = rankg.getMemberList();
        
        for (int i = 0; i < lanes.size(); i++) {
            //set value
            rank[i] = getValue((Dataset)lanes.get(i), channelID, rankg.getName());
        }
        
        return rank;
    }
    
    public static TestLane getValue(Dataset data, String channelID, String rankID) {
        byte[] value = null;
        try {
            value = (byte[])data.read();
        } catch (Exception ex) {
            Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OutOfMemoryError ex) {
            Logger.getLogger(ReadHDF5File.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new TestLane(value[0], true, channelID, rankID, data.getName());
    }

    /**
     * add dynamic lib
     *
     * @param s
     * @throws IOException
     */
    private static void addDir(String s) throws IOException {
        try {
            Field field = ClassLoader.class.getDeclaredField("usr_paths");
            field.setAccessible(true);
            String[] paths = (String[]) field.get(null);
            for (int i = 0; i < paths.length; i++) {
                if (s.equals(paths[i])) {
                    return;
                }
            }
            String[] tmp = new String[paths.length + 1];
            System.arraycopy(paths, 0, tmp, 0, paths.length);
            tmp[paths.length] = s;
            field.set(null, tmp);
            System.setProperty("java.library.path", System.getProperty("java.library.path") + File.pathSeparator + s);
        } catch (IllegalAccessException e) {
            throw new IOException("Failed to get permissions to set library path");
        } catch (NoSuchFieldException e) {
            throw new IOException("Failed to get field handle to set library path");
        }
    }

}
