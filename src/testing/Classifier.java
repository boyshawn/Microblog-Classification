package testing;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import libsvm.*;

public class Classifier {
	public Classifier(String trainedClassifierFile) throws IOException{
		
	}
	
	public static String generateModel(String inputFilePath) throws IOException{
		svm_train t = new svm_train();
		String arr[] = {inputFilePath};
		t.run(arr);
		
		return t.model_file_name;
	}
	
	public static void generateTestResult(String classifier, String inputFile, String outputFile) throws IOException{
		
		svm_model model = svm.svm_load_model(classifier);
		String result = "";
		
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		try {
			String line = br.readLine();
			svm_node[] x = new svm_node[15000];
			for (int i=0; i<x.length; i++){
				x[i] = new svm_node();
			}
			int count=0;
			while (line != null){
				count++;
				// Create test vector
				line = line.replace("  ", " ");
				String[] parts = line.split(" ");
				for (int i=1; i<parts.length; i++){
					if (!parts[i].contains(":")){
						System.out.print("Error! Vector does not contain ':' separator! count:"+count+" col:"+i+" "+parts[i]);
						return;
					}
					String[] pt = parts[i].split(":");
					x[i-1].index = Integer.valueOf(pt[0]);
					x[i-1].value = Double.valueOf(pt[1]);
				}
				
				Double pr = svm.svm_predict(model, x);
//				System.out.println("printing pr:");
//				System.out.println(pr);
//				result.concat(String.valueOf(pr) + "\n");
				result = result + String.valueOf(pr.intValue()) + "\n";
//				System.out.println("printing valueof pr:");
//				System.out.println(String.valueOf(pr));
//				System.out.println("printing result:");
//				System.out.println(result);
				line = br.readLine();
			}
			
		} finally {
			br.close();
		}
		
		// Write to outputFile
		System.out.println("Result is:");
		System.out.println(result);
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
		try{
			bw.write(result);
		} finally {
			bw.close();
		}
		
		return;
	}
	
	public static void main(String[] args){
		
		String VECTOR_DBS1 = "Test_Vector/vector(text+geoposition+social)DBS1.txt";
		String CLASSIFIER_DBS1 = "models/vector(text+geoposition+social)DBS1.txt.model";
		String TEST_RESULT_DBS1 = "GENERATED-RESULT/Result_DBS1.txt";
		
		try {
			generateTestResult(CLASSIFIER_DBS1, VECTOR_DBS1, TEST_RESULT_DBS1);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}
