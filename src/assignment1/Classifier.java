package assignment1;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import libsvm.*;

public class Classifier {
	public Classifier(String trainedClassifierFile) throws IOException{
		
	}
	
	public static void generateTestResult(String classifier, String inputFile, String outputFile) throws IOException{
		
		svm_model model = svm.svm_load_model(classifier);
		String result = "";
		
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		try {
			String line = br.readLine();
			svm_node[] x = new svm_node[510];
			
			while (line != null){
				// Create test vector
				String[] parts = line.split(" ");
				for (int i=1; i<parts.length; i++){
					if (!parts[i].contains(":")){
						System.out.print("Error! Vector does not contain ':' separator!");
						return;
					}
					String[] pt = parts[i].split(":");
					x[i-1].index = Integer.valueOf(pt[0]);
					x[i-1].value = Double.valueOf(pt[1]);
				}
				
				double pr = svm.svm_predict(model, x);
				result.concat(String.valueOf(pr) + "\n");
				
				line = br.readLine();
			}
			
		} finally {
			br.close();
		}
		
		// Write to outputFile
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
		try{
			bw.write(result);
		} finally {
			bw.close();
		}
		
		return;
	}
}
