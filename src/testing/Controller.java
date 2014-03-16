package testing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import model.Tweets;

import training.CHIStatSelecter;

public class Controller {
	private static final int VECTOR_SIZE = 200;
	
//	private static final String VECTOR_DBS1 = "Test_Vector/testvector(text+geoposition+social)DBS1.txt";
//	private static final String VECTOR_DBS2	 = "Test_Vector/testvector(text+geoposition+social)DBS2.txt";
//	private static final String VECTOR_NUS1 = "Test_Vector/testvector(text+geoposition+social)NUS1.txt";
//	private static final String VECTOR_NUS2 = "Test_Vector/testvector(text+geoposition+social)NUS2.txt";
//	private static final String VECTOR_STARHUB = "Test_Vector/testvector(text+geoposition+social)Starhub.txt";
//	
//	private static String CLASSIFIER_DBS1 = "models/vector(text+geoposition+social)DBS1.txt.model";
//	private static String CLASSIFIER_DBS2 = "models/vector(text+geoposition+social)DBS2.txt.model";
//	private static String CLASSIFIER_NUS1 = "models/vector(text+geoposition+social)NUS1.txt.model";
//	private static String CLASSIFIER_NUS2 = "models/vector(text+geoposition+social)NUS2.txt.model";
//	private static String CLASSIFIER_STARHUB = "models/vector(text+geoposition+social)Starhub.txt.model";
//	
//	private static final String TEST_RESULT_DBS1 = "GENERATED-RESULT/Result_DBS1.txt";
//	private static final String TEST_RESULT_DBS2	 = "GENERATED-RESULT/Result_DBS2.txt";
//	private static final String TEST_RESULT_NUS1 = "GENERATED-RESULT/Result_NUS1.txt";
//	private static final String TEST_RESULT_NUS2 = "GENERATED-RESULT/Result_NUS2.txt";
//	private static final String TEST_RESULT_STARHUB = "GENERATED-RESULT/Result_STARHUB.txt";
	
	private static final String VECTOR_APPLE = "Test_Vector/test/testvector(text)apple.txt";
	private static final String VECTOR_GOOGLE	 = "Test_Vector/test/testvector(text)google.txt";
	private static final String VECTOR_MICROSOFT = "Test_Vector/test/testvector(text)microsoft.txt";
	private static final String VECTOR_TWITTER = "Test_Vector/test/testvector(text)twitter.txt";
	
	private static String CLASSIFIER_APPLE = "models/vector(text+geoposition+social)apple.txt.model";
	private static String CLASSIFIER_GOOGLE = "models/vector(text+geoposition+social)google.txt.model";
	private static String CLASSIFIER_MICROSOFT = "models/vector(text+geoposition+social)microsoft.txt.model";
	private static String CLASSIFIER_TWITTER = "models/vector(text+geoposition+social)twitter.txt.model";
	
	private static final String TEST_RESULT_APPLE = "GENERATED-RESULT/Result_apple.txt";
	private static final String TEST_RESULT_GOOGLE	 = "GENERATED-RESULT/Result_google.txt";
	private static final String TEST_RESULT_MICROSOFT = "GENERATED-RESULT/Result_microsoft.txt";
	private static final String TEST_RESULT_TWITTER = "GENERATED-RESULT/Result_twitter.txt";
	
	private static final String GROUNDTRUTH_DBS1 = "TEST/Groundtruth_DBS1.txt";
	private static final String GROUNDTRUTH_DBS2	 = "TEST/Groundtruth_DBS2.txt";
	private static final String GROUNDTRUTH_NUS1 = "TEST/Groundtruth_NUS1.txt";
	private static final String GROUNDTRUTH_NUS2 = "TEST/Groundtruth_NUS2.txt";
	private static final String GROUNDTRUTH_STARHUB = "TEST/Groundtruth_STARHUB.txt";
	
	static CHIStatSelecter css;
	
	static final Vector<Integer> testClass = new Vector<Integer>();
	
	private static final int APPLE = 0;
	private static final int GOOGLE = 1;
	private static final int MICROSOFT = 2;
	private static final int TWITTER = 3;
	
	private void generateVector(String testDataFile){
		//Generation of Vector
		//DBS1
//		Indexer indexer = new Indexer(testDataFile, VECTOR_DBS1, "DBS1");
//		indexer.run();
//		
//		//DBS2
//		indexer = new Indexer(testDataFile, VECTOR_DBS2, "DBS2");
//		indexer.run();
//		
//		//NUS1
//		indexer = new Indexer(testDataFile, VECTOR_NUS1, "NUS1");
//		indexer.run();
//		
//		//NUS2
//		indexer = new Indexer(testDataFile, VECTOR_NUS2, "NUS2");
//		indexer.run();
//		
//		//Starhub
//		indexer = new Indexer(testDataFile, VECTOR_STARHUB, "Starhub");
//		indexer.run();
	}
	
	public static void generateTestResult(){
		//FIXME: to check with michael if the classifier will generate a completed SVM file
		try {
//			Classifier.generateTestResult(CLASSIFIER_DBS1, VECTOR_DBS1, TEST_RESULT_DBS1);
//			Classifier.generateTestResult(CLASSIFIER_DBS2, VECTOR_DBS2, TEST_RESULT_DBS2);
//			Classifier.generateTestResult(CLASSIFIER_NUS1, VECTOR_NUS1, TEST_RESULT_NUS1);
//			Classifier.generateTestResult(CLASSIFIER_NUS2, VECTOR_NUS2, TEST_RESULT_NUS2);
//			Classifier.generateTestResult(CLASSIFIER_STARHUB, VECTOR_STARHUB, TEST_RESULT_STARHUB);
			
			Classifier.generateTestResult(CLASSIFIER_APPLE, VECTOR_APPLE, TEST_RESULT_APPLE);
			Classifier.generateTestResult(CLASSIFIER_GOOGLE, VECTOR_GOOGLE, TEST_RESULT_GOOGLE);
			Classifier.generateTestResult(CLASSIFIER_MICROSOFT, VECTOR_MICROSOFT, TEST_RESULT_MICROSOFT);
			Classifier.generateTestResult(CLASSIFIER_TWITTER, VECTOR_TWITTER, TEST_RESULT_TWITTER);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
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
//		File file = new File(fileName);
//		BufferedReader reader = null;
//		ArrayList<Integer> answerList = new ArrayList<>();
//		
//		try {
//			reader = new BufferedReader(new FileReader(file));
//			String answer = reader.readLine();
//		
//			while(answer != null){
//				answerList.add(Integer.getInteger(answer));
//				answer = reader.readLine();
//			}
//			
//			reader.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//				
//		return answerList;
		return null;
	}
	
	public void printToEvaluationFile() throws IOException{
		double[] resultSet;
		final String evaluationFileName = "GENERATED-RESULT/evaluation.txt";
		BufferedWriter writer = null;
		
		writer= new BufferedWriter(new FileWriter(evaluationFileName));
		
		//Start writing
		writer.write("\tPrecision\tRecall\tf1\n");
		
//		oneLinerResultWrite(writer, TEST_RESULT_DBS1, GROUNDTRUTH_DBS1, "DBS1");
//		oneLinerResultWrite(writer, TEST_RESULT_DBS2, GROUNDTRUTH_DBS2, "DBS2");
//		oneLinerResultWrite(writer, TEST_RESULT_NUS1, GROUNDTRUTH_NUS1, "NUS1");
//		oneLinerResultWrite(writer, TEST_RESULT_NUS2, GROUNDTRUTH_NUS2, "NUS2");
//		oneLinerResultWrite(writer, TEST_RESULT_STARHUB, GROUNDTRUTH_STARHUB, "Star");
		
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
		
		//generate tweet classification
//		css = new CHIStatSelecter();
//		css.run();
		
		//generate feature for emotion
		generateTrainingFeature();
//		generateHarvestFeature();
					
		//combine feature
//		generateCombinedFeature();

		// for test tweets
		//generateClasssification vectors
//		css = new CHIStatSelecter();
//		css.loadTrainingFeature();
//		css.generateTestIndex();
//		
//		//run through classifier
//		generateTestResult();
//		// get classification for each test tweet
//		readTestTweetClassification();
//		classifySentimentDifferentClass();
		//depending on classification, use a different modal
				
//		generateTestResult();
		
	}

	private static void generateCombinedFeature() {
		//basic + social
		combineVector("Test_Vector/apple_p(basic+social).txt","Test_Vector/apple_p(basic).txt", "Test_Vector/social/apple_p(social).txt");
		combineVector("Test_Vector/apple_p(basic+social).txt","Test_Vector/apple_p(basic).txt", "Test_Vector/social/apple_p(social).txt");
		combineVector("Test_Vector/apple_p(basic+social).txt","Test_Vector/apple_p(basic).txt", "Test_Vector/social/apple_p(social).txt");

		combineVector("Test_Vector/google_p(basic+social).txt","Test_Vector/google_p(basic).txt", "Test_Vector/social/google_p(social).txt");
		combineVector("Test_Vector/google_p(basic+social).txt","Test_Vector/google_p(basic).txt", "Test_Vector/social/google_p(social).txt");
		combineVector("Test_Vector/google_p(basic+social).txt","Test_Vector/google_p(basic).txt", "Test_Vector/social/google_p(social).txt");

		combineVector("Test_Vector/microsoft_p(basic+social).txt","Test_Vector/microsoft_p(basic).txt", "Test_Vector/social/microsoft_p(social).txt");
		combineVector("Test_Vector/microsoft_p(basic+social).txt","Test_Vector/microsoft_p(basic).txt", "Test_Vector/social/microsoft_p(social).txt");
		combineVector("Test_Vector/microsoft_p(basic+social).txt","Test_Vector/microsoft_p(basic).txt", "Test_Vector/social/microsoft_p(social).txt");

		combineVector("Test_Vector/twitter_p(basic+social).txt","Test_Vector/twitter_p(basic).txt", "Test_Vector/social/twitter_p(social).txt");
		combineVector("Test_Vector/twitter_p(basic+social).txt","Test_Vector/twitter_p(basic).txt", "Test_Vector/social/twitter_p(social).txt");
		combineVector("Test_Vector/twitter_p(basic+social).txt","Test_Vector/twitter_p(basic).txt", "Test_Vector/social/twitter_p(social).txt");

		//basic + harvest
		combineVector("Test_Vector/apple_p(basic+harvest).txt","Test_Vector/apple_p(basic).txt", "Test_Vector/social/apple_p(harvest).txt");
		combineVector("Test_Vector/apple_p(basic+harvest).txt","Test_Vector/apple_p(basic).txt", "Test_Vector/social/apple_p(harvest).txt");
		combineVector("Test_Vector/apple_p(basic+harvest).txt","Test_Vector/apple_p(basic).txt", "Test_Vector/social/apple_p(harvest).txt");

		combineVector("Test_Vector/google_p(basic+harvest).txt","Test_Vector/google_p(basic).txt", "Test_Vector/social/google_p(harvest).txt");
		combineVector("Test_Vector/google_p(basic+harvest).txt","Test_Vector/google_p(basic).txt", "Test_Vector/social/google_p(harvest).txt");
		combineVector("Test_Vector/google_p(basic+harvest).txt","Test_Vector/google_p(basic).txt", "Test_Vector/social/google_p(harvest).txt");

		combineVector("Test_Vector/microsoft_p(basic+harvest).txt","Test_Vector/microsoft_p(basic).txt", "Test_Vector/social/microsoft_p(harvest).txt");
		combineVector("Test_Vector/microsoft_p(basic+harvest).txt","Test_Vector/microsoft_p(basic).txt", "Test_Vector/social/microsoft_p(harvest).txt");
		combineVector("Test_Vector/microsoft_p(basic+harvest).txt","Test_Vector/microsoft_p(basic).txt", "Test_Vector/social/microsoft_p(harvest).txt");

		combineVector("Test_Vector/twitter_p(basic+harvest).txt","Test_Vector/twitter_p(basic).txt", "Test_Vector/social/twitter_p(harvest).txt");
		combineVector("Test_Vector/twitter_p(basic+harvest).txt","Test_Vector/twitter_p(basic).txt", "Test_Vector/social/twitter_p(harvest).txt");
		combineVector("Test_Vector/twitter_p(basic+harvest).txt","Test_Vector/twitter_p(basic).txt", "Test_Vector/social/twitter_p(harvest).txt");
		
		//basic + harvest + social
		combineVector("Test_Vector/apple_p(basic+harvest+social).txt","Test_Vector/apple_p(basic+harvest).txt", "Test_Vector/social/apple_p(social).txt");
		combineVector("Test_Vector/apple_p(basic+harvest+social).txt","Test_Vector/apple_p(basic+harvest).txt", "Test_Vector/social/apple_p(social).txt");
		combineVector("Test_Vector/apple_p(basic+harvest+social).txt","Test_Vector/apple_p(basic+harvest).txt", "Test_Vector/social/apple_p(social).txt");

		combineVector("Test_Vector/google_p(basic+harvest+social).txt","Test_Vector/google_p(basic+harvest).txt", "Test_Vector/social/google_p(social).txt");
		combineVector("Test_Vector/google_p(basic+harvest+social).txt","Test_Vector/google_p(basic+harvest).txt", "Test_Vector/social/google_p(social).txt");
		combineVector("Test_Vector/google_p(basic+harvest+social).txt","Test_Vector/google_p(basic+harvest).txt", "Test_Vector/social/google_p(social).txt");

		combineVector("Test_Vector/microsoft_p(basic+harvest+social).txt","Test_Vector/microsoft_p(basic+harvest).txt", "Test_Vector/social/microsoft_p(social).txt");
		combineVector("Test_Vector/microsoft_p(basic+harvest+social).txt","Test_Vector/microsoft_p(basic+harvest).txt", "Test_Vector/social/microsoft_p(social).txt");
		combineVector("Test_Vector/microsoft_p(basic+harvest+social).txt","Test_Vector/microsoft_p(basic+harvest).txt", "Test_Vector/social/microsoft_p(social).txt");

		combineVector("Test_Vector/twitter_p(basic+harvest+social).txt","Test_Vector/twitter_p(basic+harvest).txt", "Test_Vector/social/twitter_p(social).txt");
		combineVector("Test_Vector/twitter_p(basic+harvest+social).txt","Test_Vector/twitter_p(basic+harvest).txt", "Test_Vector/social/twitter_p(social).txt");
		combineVector("Test_Vector/twitter_p(basic+harvest+social).txt","Test_Vector/twitter_p(basic+harvest).txt", "Test_Vector/social/twitter_p(social).txt");
				
	}

	private static void generateHarvestFeature() {
		System.out.println("Harveseting");
		System.out.println("apple - start");
		Tweets a_pTweet = new Tweets("Train/pos_apple.txt");
		Tweets a_ngTweet = new Tweets("Train/neg_apple.txt");
		Tweets a_nuTweet = new Tweets("Train/neu_apple.txt");
		HarvestFeatureGenerator a_p_hfg = new HarvestFeatureGenerator(a_pTweet.getTweets(), a_ngTweet.getTweets(), a_nuTweet.getTweets(), "Test_Vector/harvest/apple_p(harvest).txt");
		HarvestFeatureGenerator a_ng_hfg = new HarvestFeatureGenerator(a_ngTweet.getTweets(), a_pTweet.getTweets(), a_nuTweet.getTweets(), "Test_Vector/harvest/apple_ng(harvest).txt");
		HarvestFeatureGenerator a_nu_hfg = new HarvestFeatureGenerator(a_nuTweet.getTweets(), a_pTweet.getTweets(), a_ngTweet.getTweets(),"Test_Vector/harvest/apple_nu(harvest).txt");
		
		System.out.println("google - start");
		Tweets g_pTweet = new Tweets("Train/pos_google.txt");
		Tweets g_ngTweet = new Tweets("Train/neg_google.txt");
		Tweets g_nuTweet = new Tweets("Train/neu_google.txt");
		HarvestFeatureGenerator g_p_hfg = new HarvestFeatureGenerator(g_pTweet.getTweets(), g_ngTweet.getTweets(), g_nuTweet.getTweets(), "Test_Vector/harvest/google_p(harvest).txt");
		HarvestFeatureGenerator g_ng_hfg = new HarvestFeatureGenerator(g_ngTweet.getTweets(), g_pTweet.getTweets(), g_nuTweet.getTweets(), "Test_Vector/harvest/google_ng(harvest).txt");
		HarvestFeatureGenerator g_nu_hfg = new HarvestFeatureGenerator(g_nuTweet.getTweets(), g_pTweet.getTweets(), g_ngTweet.getTweets(),"Test_Vector/harvest/google_nu(harvest).txt");

		System.out.println("microsoft - start");
		Tweets m_pTweet = new Tweets("Train/pos_microsoft.txt");
		Tweets m_ngTweet = new Tweets("Train/neg_microsoft.txt");
		Tweets m_nuTweet = new Tweets("Train/neu_microsoft.txt");
		HarvestFeatureGenerator m_p_hfg = new HarvestFeatureGenerator(m_pTweet.getTweets(), m_ngTweet.getTweets(), m_nuTweet.getTweets(), "Test_Vector/harvest/microsoft(harvest).txt");
		HarvestFeatureGenerator m_ng_hfg = new HarvestFeatureGenerator(m_ngTweet.getTweets(), m_pTweet.getTweets(), m_nuTweet.getTweets(), "Test_Vector/harvest/microsoft_ng(harvest).txt");
		HarvestFeatureGenerator m_nu_hfg = new HarvestFeatureGenerator(m_nuTweet.getTweets(), m_pTweet.getTweets(), m_ngTweet.getTweets(),"Test_Vector/harvest/microsoft_nu(harvest).txt");
		
		System.out.println("twitter - start");
		Tweets t_pTweet = new Tweets("Train/pos_twitter.txt");
		Tweets t_ngTweet = new Tweets("Train/neg_twitter.txt");
		Tweets t_nuTweet = new Tweets("Train/neu_twitter.txt");
		HarvestFeatureGenerator t_p_hfg = new HarvestFeatureGenerator(t_pTweet.getTweets(), t_ngTweet.getTweets(), t_nuTweet.getTweets(), "Test_Vector/harvest/twitter(harvest).txt");
		HarvestFeatureGenerator t_ng_hfg = new HarvestFeatureGenerator(t_ngTweet.getTweets(), t_pTweet.getTweets(), t_nuTweet.getTweets(), "Test_Vector/harvest/twitter_ng(harvest).txt");
		HarvestFeatureGenerator t_nu_hfg = new HarvestFeatureGenerator(t_nuTweet.getTweets(), t_pTweet.getTweets(), t_ngTweet.getTweets(),"Test_Vector/harvest/twitter_nu(harvest).txt");

	}
	
	private static void classifySentimentDifferentClass() {
		//read in test tweets vectors
	}

	private static void readTestTweetClassification() {
		try{
			BufferedReader a_r = new BufferedReader(new FileReader(TEST_RESULT_APPLE));
			BufferedReader g_r = new BufferedReader(new FileReader(TEST_RESULT_GOOGLE));
			BufferedReader m_r = new BufferedReader(new FileReader(TEST_RESULT_MICROSOFT));
			BufferedReader t_r = new BufferedReader(new FileReader(TEST_RESULT_TWITTER));
			
			String val;
			
			Vector<Boolean> abool = new Vector<Boolean>();
			Vector<Boolean> gbool = new Vector<Boolean>();
			Vector<Boolean> mbool = new Vector<Boolean>();
			Vector<Boolean> tbool = new Vector<Boolean>();
		
			while((val = a_r.readLine() )!=null){
				abool.add(val.equals("1"));
			}
			while((val = g_r.readLine() )!=null){
				gbool.add(val.equals("1"));
			}
			while((val = m_r.readLine() )!=null){
				mbool.add(val.equals("1"));
			}
			while((val = t_r.readLine() )!=null){
				tbool.add(val.equals("1"));
			}
			
			a_r.close();
			g_r.close();
			m_r.close();
			t_r.close();
			
			for(int i=0; i<abool.size(); i++){
				if(abool.get(i)){
					testClass.add(APPLE);
				}else if(gbool.get(i)){
					testClass.add(GOOGLE);
				}else if(mbool.get(i)){
					testClass.add(MICROSOFT);
				}else if(tbool.get(i)){
					testClass.add(TWITTER);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	private static void combineVector(String optputName, String name1, String name2) {
		try{
			
			PrintWriter tw = new PrintWriter(new FileWriter(optputName));
			Vector<BufferedReader> brV = new Vector<BufferedReader>();
			
			BufferedReader readertemp = new BufferedReader(new FileReader(name1));
			String line = readertemp.readLine();
			readertemp.close();
			String[] temparr=line.split(" ");
			String last = temparr[temparr.length-1];
			int val = Integer.parseInt(last.split(":")[0]) + 1;
			
			BufferedReader reader1 = new BufferedReader(new FileReader(name1));	
			BufferedReader reader2 = new BufferedReader(new FileReader(name2));	
			String line1, line2;
			while(((line1 = reader1.readLine()) != null) && ((line2=reader2.readLine())!=null)){
				String[] newfeature = line2.split(" ");
				for(int i=0; i<newfeature.length; i++){
					line1 += " "+ val++ + newfeature[i];
				}
				tw.println(line1);
			}
			reader1.close();
			reader2.close();
			
			//build feature
			//for positive
			tw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void generateTrainingFeature() {
//		System.out.println("apple - start");
//		Tweets a_pTweet = new Tweets("Train/pos_apple.txt");
//		Tweets a_ngTweet = new Tweets("Train/neg_apple.txt");
//		Tweets a_nuTweet = new Tweets("Train/neu_apple.txt");
//		BasicFeatureGenerator a_p_bfg = new BasicFeatureGenerator(a_pTweet.getTweets(), a_ngTweet.getTweets(), a_nuTweet.getTweets(), "Test_Vector/apple_p(basic).txt");
//		BasicFeatureGenerator a_ng_bfg = new BasicFeatureGenerator(a_ngTweet.getTweets(), a_pTweet.getTweets(), a_nuTweet.getTweets(), "Test_Vector/apple_ng(basic).txt");
//		BasicFeatureGenerator a_nu_bfg = new BasicFeatureGenerator(a_nuTweet.getTweets(), a_pTweet.getTweets(), a_ngTweet.getTweets(),"Test_Vector/apple_nu(basic).txt");
//		
//		System.out.println("google - start");
//		Tweets g_pTweet = new Tweets("Train/pos_google.txt");
//		Tweets g_ngTweet = new Tweets("Train/neg_google.txt");
//		Tweets g_nuTweet = new Tweets("Train/neu_google.txt");
//		BasicFeatureGenerator g_p_bfg = new BasicFeatureGenerator(g_pTweet.getTweets(), g_ngTweet.getTweets(), g_nuTweet.getTweets(), "Test_Vector/google(basic).txt");
//		BasicFeatureGenerator g_ng_bfg = new BasicFeatureGenerator(g_ngTweet.getTweets(), g_pTweet.getTweets(), g_nuTweet.getTweets(), "Test_Vector/google_ng(basic).txt");
//		BasicFeatureGenerator g_nu_bfg = new BasicFeatureGenerator(g_nuTweet.getTweets(), g_pTweet.getTweets(), g_ngTweet.getTweets(),"Test_Vector/google_nu(basic).txt");
//		
//		System.out.println("microsoft - start");
//		Tweets m_pTweet = new Tweets("Train/pos_microsoft.txt");
//		Tweets m_ngTweet = new Tweets("Train/neg_microsoft.txt");
//		Tweets m_nuTweet = new Tweets("Train/neu_microsoft.txt");
//		BasicFeatureGenerator m_p_bfg = new BasicFeatureGenerator(m_pTweet.getTweets(), m_ngTweet.getTweets(), m_nuTweet.getTweets(), "Test_Vector/microsoft(basic).txt");
//		BasicFeatureGenerator m_ng_bfg = new BasicFeatureGenerator(m_ngTweet.getTweets(), m_pTweet.getTweets(), m_nuTweet.getTweets(), "Test_Vector/microsoft_ng(basic).txt");
//		BasicFeatureGenerator m_nu_bfg = new BasicFeatureGenerator(m_nuTweet.getTweets(), m_pTweet.getTweets(), m_ngTweet.getTweets(),"Test_Vector/microsoft_nu(basic).txt");
//		
		System.out.println("twitter - start");
		Tweets t_pTweet = new Tweets("Train/pos_twitter.txt");
		Tweets t_ngTweet = new Tweets("Train/neg_twitter.txt");
		Tweets t_nuTweet = new Tweets("Train/neu_twitter.txt");
		BasicFeatureGenerator t_p_bfg = new BasicFeatureGenerator(t_pTweet.getTweets(), t_ngTweet.getTweets(), t_nuTweet.getTweets(), "Test_Vector/twitter(basic).txt");
		BasicFeatureGenerator t_ng_bfg = new BasicFeatureGenerator(t_ngTweet.getTweets(), t_pTweet.getTweets(), t_nuTweet.getTweets(), "Test_Vector/twitter_ng(basic).txt");
		BasicFeatureGenerator t_nu_bfg = new BasicFeatureGenerator(t_nuTweet.getTweets(), t_pTweet.getTweets(), t_ngTweet.getTweets(),"Test_Vector/twitter_nu(basic).txt");
				
		t_p_bfg.start();
		t_ng_bfg.start();
		t_nu_bfg.start();
	}
}