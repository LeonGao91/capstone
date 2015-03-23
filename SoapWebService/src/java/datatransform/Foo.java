/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datatransform;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;

/**
 *
 * @author wangjerome
 */
public class Foo {
    public static void main(String[] args) {
        H5_CreateFile.CreateFile();
    }
}



class H5_CreateFile {
	static final String FILENAME = "H5_CreateFile.h5";

	public static void CreateFile() {
		int file_id = -1;

		// Create a new file using default properties.
		try {
			file_id = H5.H5Fcreate(FILENAME, HDF5Constants.H5F_ACC_TRUNC,
					HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Close the file.
		try {
			if (file_id >= 0)
				H5.H5Fclose(file_id);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		H5_CreateFile.CreateFile();
	}

}
