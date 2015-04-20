package dataAnalyticsModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class DataAnalyticsModel {
	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {

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
		
		try {
			File file1 = new File(fileName1);
			FileInputStream fis1 = new FileInputStream(file1);
			BufferedReader br1 = new BufferedReader(new InputStreamReader(fis1));
			String line1 = null;
			
			File file2 = new File(fileName2);
			FileInputStream fis2 = new FileInputStream(file2);
			BufferedReader br2 = new BufferedReader(new InputStreamReader(fis2));
			String line2 = null;
			// while ((line = br.readLine()) != null) {
			// System.out.println(line);
			systems1 = new TestSystem[5];
			systems2 = new TestSystem[5];
			for (int i = 0; i < 5; i++) {
				repeats1 = new TestRepeat[5];
				repeats2 = new TestRepeat[5];
				for (int j = 0; j < 5; j++) {
					lanes1 = new TestLane[1][1][64];
					lanes2 = new TestLane[1][1][64];
					// System.out.println(numbers.length); // TODO
					if ((line1 = br1.readLine()) != null && (line2 = br2.readLine()) != null) {
						//System.out.println(line);
						String[] numbers1 = line1.split(",");
						String[] numbers2 = line2.split(",");
						for (int k = 0; k < 64; k++) {
							// System.out.println("i: " + i + " j: " + j +
							// " k: " + k);
							// System.out.println(numbers[k]);
							lanes1[0][0][k] = new TestLane(
									Integer.parseInt(numbers1[k]));
							// System.out.println(lanes[0][0][k].toString());
							lanes2[0][0][k] = new TestLane(
									Integer.parseInt(numbers2[k]));
						}
					}
					margins1 = new TestMarginsDDR(lanes1);
					margins2 = new TestMarginsDDR(lanes2);
					//System.out.println(margins.toString());
					repeats1[j] = new TestRepeat(margins1);
					repeats2[j] = new TestRepeat(margins2);
				}
				systems1[i] = new TestSystem(repeats1);
				systems2[i] = new TestSystem(repeats2);
			}
			// }
			directions[0] = new TestDirection(systems1);
			directions[1] = new TestDirection(systems2);
			test = new Test(directions);
			br1.close();
			br2.close();
			System.out.println("test");
			System.out.println(test.toString());
			System.out.println("Health: " + test.getHealth());
			System.out.println("Trust: " + test.getTrust());
			//System.out.println(test.getHealth());
			//System.out.println(test.getTrust());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
