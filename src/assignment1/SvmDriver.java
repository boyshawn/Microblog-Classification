package assignment1;

import java.io.IOException;

/* This is the sample code 
 * for using the SVM in java
 */

public class SvmDriver {
	public static void main(String argv[]) throws IOException{
		
		/* Example for Yongwei & Francis
		 * 
		 * Below code reads in a data file and outputs a model called filename.model in the same folder
		 * */
		svm_train t = new svm_train();
		String arr[] = {"data\\apple_p(basic).txt"};
		t.run(arr);
		
		/* To use the model generated above to 'predict' data,
		 *  look at testing/Classifier.java (same as in assignment 1) */
		
	}
}
