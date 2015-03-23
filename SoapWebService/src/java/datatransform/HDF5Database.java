/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatransform;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

/**
 *
 * @author wangjerome
 */
public class HDF5Database implements Database {

    private int fileId = -1;

    public HDF5Database(boolean newFile, String fileName) throws HDF5LibraryException {
        if (newFile) {
            fileId = createFile(fileName);
        } else {
            fileId = openFile(fileName);
        }
    }
    
    private int createFile(String fileName) throws HDF5LibraryException {
        
        System.out.println(System.getProperty("java.library.path"));
        System.out.println(System.getProperty("Test"));

        int file = H5.H5Fcreate(fileName, HDF5Constants.H5F_ACC_TRUNC,
                HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);

        return file;

    }

    private int openFile(String fileName) throws HDF5LibraryException {
        int file = H5.H5Fopen(fileName,HDF5Constants.H5F_ACC_RDONLY, HDF5Constants.H5P_DEFAULT);

        return file;

    }

    @Override
    public void putData(long[] dims, String name, Object data) throws Exception {

        int dataspace_id = -1;
        int dataset_id = -1;

        if (fileId < 0) {
            throw new Exception("No file opened");
        }

        // Create the data space for the dataset.
        dataspace_id = H5.H5Screate_simple(dims.length, dims, null);

        // Create the dataset.
        dataset_id = H5.H5Dcreate(fileId, "/" + name,
                HDF5Constants.H5T_STD_I32BE, dataspace_id,
                HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);

        H5.H5Dwrite(dataset_id, HDF5Constants.H5T_NATIVE_INT,
                HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
                HDF5Constants.H5P_DEFAULT, data);
        H5.H5Sclose(dataspace_id);
        H5.H5Dclose(dataset_id);

    }

    @Override
    public long[] getDataDim(String name) throws Exception {
        int dataspace_id = -1;
        int dataset_id = -1;

        if (fileId < 0) {
            throw new Exception("No file opened");
        }

        //open data set
        dataset_id = H5.H5Dopen(fileId, name, HDF5Constants.H5P_DEFAULT);

        // get the data space for the dataset.
        dataspace_id = H5.H5Dget_space(dataset_id);

        // get the dims number
        long[] result = new long[H5.H5Sget_simple_extent_ndims(dataspace_id)];

        // get dims
        H5.H5Sget_simple_extent_dims(dataspace_id, result, null);
        H5.H5Sclose(dataspace_id);
        H5.H5Dclose(dataset_id);

        return result;
    }

    @Override
    public void getData(String name, Object data) throws Exception {
        int dataset_id = -1;

        if (fileId < 0) {
            throw new Exception("No file opened");
        }

        //open data set
        dataset_id = H5.H5Dopen(fileId, name, HDF5Constants.H5P_DEFAULT);
        H5.H5Dread(dataset_id, HDF5Constants.H5T_NATIVE_INT,
                HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
                HDF5Constants.H5P_DEFAULT, data);

        H5.H5Dclose(dataset_id);
    }

    @Override
    public void close() throws HDF5LibraryException {

        if (fileId >= 0) {
            H5.H5Fclose(fileId);
        }

    }

}



