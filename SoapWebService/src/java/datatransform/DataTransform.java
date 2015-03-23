/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatransform;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

/**
 *
 * @author wangjerome
 */
public class DataTransform {

    public int transform(String inputfile, String outputdb, String dsname, DataStructure ds) throws FileNotFoundException, HDF5LibraryException, Exception {
//        File file = new File("whereami.txt");
//        file.createNewFile();
        addDir("/Users/wangjerome/Desktop/foo");
        System.out.println("here");
        int sum = 0;
        int count = 0;
        Scanner sc = new Scanner(new File(inputfile));

        sc.nextLine();

        long[] dims = new long[ds.getParamNum()];

        for (int i = 0; i < dims.length; i++) {
            dims[i] = ds.getSize(i);
        }

        Object data = ds.getData();

        int[] params = new int[ds.getParamNum()];

        while (sc.hasNext()) {

            String s = sc.nextLine();
            String[] tokens = s.split(",");

            for (int i = 0; i < ds.getParamNum(); i++) {
                params[i] = ds.translate(i, tokens[i]);
                sum += params[i];
                count++;
            }

            ds.setData(data, Integer.parseInt(tokens[tokens.length - 1]), params);
        }

        HDF5Database db = new HDF5Database(true, outputdb);
        db.putData(dims, dsname, data);
        db.close();

        return sum/count;
    }

    private static void addDir(String s) throws IOException {
        try {
            // This enables the java.library.path to be modified at runtime
            // From a Sun engineer at http://forums.sun.com/thread.jspa?threadID=707176
            //
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

    private Object getData(String file, String dsname, DataStructure ds) throws FileNotFoundException, HDF5LibraryException, Exception {

        long[] dims = new long[ds.getParamNum()];

        for (int i = 0; i < dims.length; i++) {
            dims[i] = ds.getSize(i);
        }

        Object data = ds.getData();

        int[] params = new int[ds.getParamNum()];

        HDF5Database db = new HDF5Database(false, file);
        db.getData(dsname, data);
        db.close();

        return data;
    }

    public static void main(String[] args) throws Exception {
        DataTransform trans = new DataTransform();
        try {
            trans.transform("/Users/wangjerome/Desktop/random_part_1_rep_1.csv", "H5_CreateFile.h5", "dset", DataStructureFactory.getDataStructure());
//            trans.getData("H5_CreateFile.h5", "dset", DataStructureFactory.getDataStructure());
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        }
    }

}
