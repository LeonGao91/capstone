package dataAnalyticsModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * This class is used to test the model. Input data are txVrefHigh and
 * txVrefLow.
 * 
 * @author Yan 04/24/2015
 * @version 2.0
 */
public class DataAnalyticsModel {
	/**
	 * @param args
	 *            no command line arguments required
	 */
	public static void main(String[] args) {
		// input files
		String fileName1 = "margins-high.csv";
		TestLane[][][] lanes1;
		TestMarginsDDR margins1;
		TestRepeat[] repeats1 = new TestRepeat[5];
		TestSystem[] systems1 = new TestSystem[5];
		TestDirection[] directions = new TestDirection[2];
		Test test;
		String fileName2 = "margins-low.csv";
		TestLane[][][] lanes2;
		TestMarginsDDR margins2;
		TestRepeat[] repeats2 = new TestRepeat[5];
		TestSystem[] systems2 = new TestSystem[5];

		// read in data and construct test objects
		try {
			File file1 = new File(fileName1);
			FileInputStream fis1 = new FileInputStream(file1);
			BufferedReader br1 = new BufferedReader(new InputStreamReader(fis1));
			String line1 = null;

			File file2 = new File(fileName2);
			FileInputStream fis2 = new FileInputStream(file2);
			BufferedReader br2 = new BufferedReader(new InputStreamReader(fis2));
			String line2 = null;
			systems1 = new TestSystem[5];
			systems2 = new TestSystem[5];
			for (int i = 0; i < 5; i++) {
				repeats1 = new TestRepeat[5];
				repeats2 = new TestRepeat[5];
				for (int j = 0; j < 5; j++) {
					lanes1 = new TestLane[1][1][64];
					lanes2 = new TestLane[1][1][64];
					if ((line1 = br1.readLine()) != null
							&& (line2 = br2.readLine()) != null) {
						String[] numbers1 = line1.split(",");
						String[] numbers2 = line2.split(",");
						for (int k = 0; k < 64; k++) {
							// construct lanes
							lanes1[0][0][k] = new TestLane(
									Integer.parseInt(numbers1[k]),
									TestLane.VALID, "channel0", "rank0", "lane"
											+ k);
							lanes2[0][0][k] = new TestLane(
									Integer.parseInt(numbers2[k]),
									TestLane.VALID, "channel0", "rank0", "lane"
											+ k);
						}
					}
					// construct margins
					margins1 = new TestMarginsDDR(lanes1);
					margins2 = new TestMarginsDDR(lanes2);
					// construct repeats
					repeats1[j] = new TestRepeat(margins1, "repeat" + j);
					repeats2[j] = new TestRepeat(margins2, "repeat" + j);
				}
				// construct systems
				systems1[i] = new TestSystem(repeats1, "system" + i);
				systems2[i] = new TestSystem(repeats2, "system" + i);
			}
			// construct directions
			directions[0] = new TestDirection(systems1, "txVrefHigh");
			directions[1] = new TestDirection(systems2, "txVrefLow");
			test = new Test(directions, "testClient", "testProduct");
			br1.close();
			br2.close();
			System.out.println("Health: " + test.getHealth());
			System.out.println("Trust: " + test.getTrust());
			// output results to xml files
//			ResultExporter.output(test);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
