package assignment1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Controller {
	private static final String VECTOR_DBS1 = "outputdata/DBS1_vector.txt";
	private static final String VECTOR_DBS2	 = "outputdata/DBS2_vector.txt";
	private static final String VECTOR_NUS1 = "outputdata/NUS1_vector.txt";
	private static final String VECTOR_NUS2 = "outputdata/NUS2_vector.txt";
	private static final String VECTOR_STARHUB = "outputdata/Starhub_vector.txt";
	
	private static String CLASSIFIER_DBS1;
	private static String CLASSIFIER_DBS2;
	private static String CLASSIFIER_NUS1;
	private static String CLASSIFIER_NUS2;
	private static String CLASSIFIER_STARHUB;
	
	private static final String TEST_RESULT_DBS1 = "GENERATED-RESULT/Result_DBS1.txt";
	private static final String TEST_RESULT_DBS2	 = "GENERATED-RESULT/Result_DBS2.txt";
	private static final String TEST_RESULT_NUS1 = "GENERATED-RESULT/Result_NUS1.txt";
	private static final String TEST_RESULT_NUS2 = "GENERATED-RESULT/Result_NUS2.txt";
	private static final String TEST_RESULT_STARHUB = "GENERATED-RESULT/Result_STARHUB.txt";
	
	private static final String GROUNDTRUTH_DBS1 = "TEST/Groundtruth_DBS1.txt";
	private static final String GROUNDTRUTH_DBS2	 = "TEST/Groundtruth_DBS2.txt";
	private static final String GROUNDTRUTH_NUS1 = "TEST/Groundtruth_NUS1.txt";
	private static final String GROUNDTRUTH_NUS2 = "TEST/Groundtruth_NUS2.txt";
	private static final String GROUNDTRUTH_STARHUB = "TEST/Groundtruth_STARHUB.txt";
	
	private void generateVector(String testDataFile){
		//Generation of Vector
		//DBS1
		Indexer indexer = new Indexer(testDataFile, VECTOR_DBS1, "DBS1");
		indexer.run();
		
		//DBS2
		indexer = new Indexer(testDataFile, VECTOR_DBS2, "DBS2");
		indexer.run();
		
		//NUS1
		indexer = new Indexer(testDataFile, VECTOR_NUS1, "NUS1");
		indexer.run();
		
		//NUS2
		indexer = new Indexer(testDataFile, VECTOR_NUS2, "NUS2");
		indexer.run();
		
		//Starhub
		indexer = new Indexer(testDataFile, VECTOR_STARHUB, "Starhub");
		indexer.run();
	}
	
	public void generateTestResult(){
		//FIXME: to check with michael if the classifier will generate a completed SVM file
		Classifier.generateTestResult(CLASSIFIER_DBS1, VECTOR_DBS1, TEST_RESULT_DBS1);
		Classifier.generateTestResult(CLASSIFIER_DBS2, VECTOR_DBS2, TEST_RESULT_DBS2);
		Classifier.generateTestResult(CLASSIFIER_NUS1, VECTOR_NUS1, TEST_RESULT_NUS1);
		Classifier.generateTestResult(CLASSIFIER_NUS2, VECTOR_NUS2, TEST_RESULT_NUS2);
		Classifier.generateTestResult(CLASSIFIER_STARHUB, VECTOR_STARHUB, TEST_RESULT_STARHUB);
	}
	
	private double[] calculateResult(String generatedResultFile, String groundTruthFile){
		final int BETA = 1;
		
		ArrayList<Integer> generatedResult = buildResult(generatedResultFile);
		ArrayList<Integer> groundTruthResult = buildResult(groundTruthFile);
		
		int retrievedRelevant = 0;
		int relevantTotal = 0;
		int retrievedTotal = 0;
		
		//FIX ME: Assume that both the array size is the same
		for(int i = 0; i < generatedResult.size(); i++){
			int generated = generatedResult.get(i);
			int groundtruth = groundTruthResult.get(i);
			
			if(generated == 1){
				retrievedTotal++;
			}
			
			if(groundtruth == 1){
				relevantTotal++;
			}
			
			if(generated == 1 && groundtruth == 1){
				retrievedRelevant++;
			}
		}
		
		double precision = retrievedRelevant / retrievedTotal;	//a = (a+b)
		double recall = retrievedRelevant / relevantTotal;		//a = (a+c)
		
		double f1 = ((BETA + 1) * precision * recall)
				/ (BETA * precision + recall);
		
		double[] resultSet = {precision, recall, f1}; 
		return resultSet;
	}
	
	private ArrayList<Integer> buildResult(String fileName){
		File file = new File(fileName);
		BufferedReader reader = null;
		ArrayList<Integer> answerList = new ArrayList<>();
		
		try {
			reader = new BufferedReader(new FileReader(file));
			String answer = reader.readLine();
		
			while(answer != null){
				answerList.add(Integer.getInteger(answer));
				answer = reader.readLine();
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		return answerList;
	}
	
	public void printToEvaluationFile() throws IOException{
		double[] resultSet;
		final String evaluationFileName = "GENERATED-RESULT/evaluation.txt";
		BufferedWriter writer = null;
		
		writer= new BufferedWriter(new FileWriter(evaluationFileName));
		
		//Start writing
		writer.write("\tPrecision\tRecall\tf1\n");
		
		oneLinerResultWrite(writer, TEST_RESULT_DBS1, GROUNDTRUTH_DBS1, "DBS1");
		oneLinerResultWrite(writer, TEST_RESULT_DBS2, GROUNDTRUTH_DBS2, "DBS2");
		oneLinerResultWrite(writer, TEST_RESULT_NUS1, GROUNDTRUTH_NUS1, "NUS1");
		oneLinerResultWrite(writer, TEST_RESULT_NUS2, GROUNDTRUTH_NUS2, "NUS2");
		oneLinerResultWrite(writer, TEST_RESULT_STARHUB, GROUNDTRUTH_STARHUB, "Star");
		
		writer.close();
	}
	
	private void oneLinerResultWrite(BufferedWriter writer,
			String generatedResultFile, String groundTruthFile, String keyword) {
		
		double[] resultSet;
		resultSet = calculateResult(generatedResultFile, groundTruthFile);
		
		try {
			writer.write(keyword + "\t");
			writer.write(resultSet[0] + "\t", 0, 7);
			writer.write(resultSet[1] + "\t", 0, 7);
			writer.write(resultSet[1] + "\t", 0, 7);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args){
		final String testDataFile = "TEST/TEST.txt"; 
		
		Controller controller = new Controller();
		controller.generateVector(testDataFile);
		controller.generateTestResult();
		
		try {
			controller.printToEvaluationFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}