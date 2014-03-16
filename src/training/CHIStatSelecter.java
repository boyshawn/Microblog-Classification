package training;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import model.Tweets;

import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similar.MoreLikeThis;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.util.packed.PackedInts.Reader;

import training.Indexer;

public class CHIStatSelecter {
	Indexer indexNUS1, indexNUS2, indexDBS1, indexDBS2, indexStarhub,
			indexNUS1_gp, indexNUS2_gp, indexDBS1_gp, indexDBS2_gp, indexStarhub_gp;
	int catSize, vectorSize, negativeTermSize, negSize1, negSize2, negSize3, negSize4;
	Map<String, Integer> NUS1_df, NUS2_df, DBS1_df, DBS2_df, Starhub_df;
	List<String> NUS1_tt, NUS2_tt, DBS1_tt, DBS2_tt, Starhub_tt;
	List<String> NUS1_CHItt, NUS2_CHItt, DBS1_CHItt, DBS2_CHItt, Starhub_CHItt;
	Map<String, Double> NUS1_CHI, NUS2_CHI, DBS1_CHI, DBS2_CHI, Starhub_CHI;
	int NUS1_docNum, NUS2_docNum, DBS1_docNum, DBS2_docNum, Starhub_docNum, N; 
	int[] negSizeArray;
	Map<String, Vector<String>> topNEGStore, similarNEGStore, randomNEGStore;
	String[] typeArray = new String[]{"apple", "google", "microsoft", "twitter"}; 
	Map<String, List<List<String>>> topTermListMap;
	Map<String, Vector<Vector<Integer>>> vectorStore;
	Map<String, List<List<Integer>>> negTermStore;
	
	public CHIStatSelecter(){
		N = 3500; 
		catSize = 4;
		vectorSize = 350;
		negativeTermSize = 350;
		vectorStore = new HashMap<String, Vector<Vector<Integer>>>();
		topNEGStore = new HashMap<String, Vector<String>>();
		similarNEGStore = new HashMap<String, Vector<String>>();
		randomNEGStore = new HashMap<String, Vector<String>>();
		topTermListMap = new HashMap<String, List<List<String>>>();
		negTermStore = new HashMap<String, List<List<Integer>>>();
	}
	
	

	public void run(){
		String type;
		
		type = "text";
		generateTextVector(type, Arrays.asList(new Indexer("TRAIN/apple.txt", "apple", vectorSize, type),
										new Indexer("TRAIN/google.txt", "google", vectorSize, type),
										new Indexer("TRAIN/microsoft.txt", "microsoft", vectorSize, type),
										new Indexer("TRAIN/twitter.txt", "twitter", vectorSize, type)
							), //Array of negative index name 
							//Arrays.asList("("+type+")_NEG_NUS1", "("+type+")_NEG_NUS2", "("+type+")_NEG_DBS1", "("+type+")_NEG_DBS2", "("+type+")_NEG_Starhub"));
							Arrays.asList("("+type+")_apple", "("+type+")_google", "("+type+")_microsoft", "("+type+")_twitter"));
		
		saveTopTerms(topTermListMap.get("ttltext"));
		
//		generateTestIndex("text", topTermListMap.get("ttltext"), topTermListMap.get("chittltext"));
//		generateTestIndex("geoposition", topTermListMap.get("chittlgeoposition"), topTermListMap.get("chittlgeoposition"));
//		generateTestSocial();
//		
//		//combine test basic
//		generateCombined("+1", "test_text_", "test_geoposition_", "Test_Vector/", "testvector(text+geoposition)", false);
//
//		//combine test CHI
//		generateCombined("+1", "testchi_text_", "testchi_geoposition_", "Test_Vector/", "testvectorCHI(text+geoposition)", false);
//
//		//generate single social test
//		generateCombined("+1", "test_social_", "Test_Vector/", "testvector(social)", false);
//		
//		//combine social test
//		generateCombined("+1", "test_text_", "test_social_", "Test_Vector/", "testvector(text+social)", false);
//		generateCombined("+1", "testchi_text_", "test_social_", "Test_Vector/", "testvectorCHI(text+social)", false);
//		generateCombined("+1", "test_text_", "test_geoposition_", "test_social_", "Test_Vector/", "testvector(text+geoposition+social)", false);
//		generateCombined("+1", "testchi_text_", "testchi_geoposition_", "test_social_", "Test_Vector/", "testvectorCHI(text+geoposition+social)", false);
//		
//		//combine basic and geoposition
//		generateCombined("+1", "text_", "geoposition_", "Training_Vector/", "TRAINING_VECTOR(text+geoposition)_", false);	
//		generateCombined("-1", "neg_text_", "neg_geoposition_", "Training_Vector/", "TRAINING_VECTOR(text+geoposition)_", true);	
//
//		//combine CHI basic and geoposition
//		generateCombined("+1", "CHItext_", "CHIgeoposition_", "Training_Vector/", "TRAINING_VECTOR_CHI(text+geoposition)_", false);	
//		generateCombined("-1", "neg_CHItext_", "neg_CHIgeoposition_", "Training_Vector/", "TRAINING_VECTOR_CHI(text+geoposition)_", true);	
//
//		//combine basic and social
//		generateCombined("+1", "text_", "social_", "Training_Vector/", "TRAINING_VECTOR(text+social)_", false);	
//		generateCombined("-1", "neg_text_", "negsocial_", "Training_Vector/", "TRAINING_VECTOR(text+social)_", true);
//		
//		//combine CHI basic and social
//		generateCombined("+1", "CHItext_", "social_", "Training_Vector/", "TRAINING_VECTOR_CHI(text+social)_", false);	
//		generateCombined("-1", "neg_CHItext_", "negsocial_", "Training_Vector/", "TRAINING_VECTOR_CHI(text+social)_", true);	
//
//		//combine CHI basic, geoposition and social 
//		generateCombined("+1", "CHItext_", "CHIgeoposition_", "social_", "Training_Vector/", "TRAINING_VECTOR_CHI(text+geoposition+social)_", false);	
//		generateCombined("-1", "neg_CHItext_", "neg_CHIgeoposition_", "negsocial_", "Training_Vector/", "TRAINING_VECTOR_CHI(text+geoposition+social)_", true);	
//		
//		//combine CHI basic, geoposition and social 
//		generateCombined("+1", "text_", "geoposition_", "social_", "Training_Vector/", "TRAINING_VECTOR(text+geoposition+social)_", false);	
//		generateCombined("-1", "neg_text_", "neg_geoposition_", "negsocial_", "Training_Vector/", "TRAINING_VECTOR(text+geoposition+social)_", true);	

	}
	
	private void saveTopTerms(List<List<String>> list) {
		try{
			for(int i=0; i<list.size(); i++){
				PrintWriter tw = new PrintWriter(new FileWriter("SAVED/ttltext"+i));
				List<String> tl = list.get(i);
				for(String term:tl){
					tw.println(term);
				}
				tw.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void loadTrainingFeature() {
		try{
			List<List<String>> alltl = new ArrayList<List<String>>();
			for(int i=0; i<4; i++){
				BufferedReader reader = new BufferedReader(new FileReader("SAVED/ttltext"+i));
				List<String> tl = new ArrayList<String>();
				String line;
				while((line = reader.readLine()) != null){
					tl.add(line);
				}
				alltl.add(tl);
				System.out.println(i+": "+tl.size());
				reader.close();
			}
			topTermListMap.put("ttltext", alltl);
		}catch(Exception e){
		}
		
	}

	public void generateTestIndex() {
		generateTestIndex("text", topTermListMap.get("ttltext"), topTermListMap.get("chittltext"));
	}

	private void generateTestSocial() {
		Indexer testindex = new Indexer();
		Map<String, Vector<Vector<Integer>>> tempMap = testindex.generateSocialFeatureForTesting(new File("TEST/test.txt"));
		System.out.println("TEST SOCIAL " +tempMap.keySet());
		for(int i=0; i<typeArray.length; i++){
			String key = typeArray[i];
			if(key.equalsIgnoreCase("Starhub")) key="STARHUB";
			Vector<Vector<Integer>> tempV = tempMap.get(key);
			//System.out.println("TEST SOCIAL V: "+key+" " +tempV);
			vectorStore.put("test_social_"+typeArray[i], tempV);
		}
	}

	private void generateSocailFeature(String type, List<Indexer> indexerList, List<String> negativeFileNameList) {
		System.out.println("Start social feature");
		Map<String, Vector<Vector<Integer>>> tempMap;
		Vector<Vector<Integer>> mainVector;
		//run index
		for(int i=0; i<indexerList.size(); i++){
			Indexer index = indexerList.get(i);
			//index.run();
			tempMap = index.generateSocaialFeature(typeArray[i]);
			
			//add main to vectorStore
			mainVector = tempMap.get(typeArray[i]);
			
			//generate remaining list
			List<Vector<Vector<Integer>>> remainList = new ArrayList<Vector<Vector<Integer>>>();
			System.out.println("social: temp map" + tempMap.keySet());
			for(int j=0; j<indexerList.size(); j++){
				if(i==j) continue;
				remainList.add(tempMap.get(typeArray[j]));
				System.out.println("social:remain list " + tempMap.get(typeArray[j]).size());
			}
						
			//get negative doc list
			List<List<Integer>> negList = negTermStore.get(typeArray[i]+"_");
			
			//start appending negative terms
			Vector<Vector<Integer>> negVector = new Vector<Vector<Integer>>();
			for(int j=0; j<negList.size(); j++){
				Vector<Vector<Integer>> remainListBasedOnType = remainList.get(j);
				System.out.println("social:append neg " + remainListBasedOnType.size());
				List<Integer> negDocNumList = negList.get(j);
				for(int k=0; k<negDocNumList.size(); k++){
					int docNum = negDocNumList.get(k);
					Vector<Integer> featureVector = remainListBasedOnType.get(docNum);
					negVector.add(featureVector);
				}
			}
			
			//add main to Vector Store
			vectorStore.put("social_"+typeArray[i], mainVector);
			
			//add neg to Vector Store
			vectorStore.put("negsocial_"+typeArray[i], negVector);
			
			try{
				BufferedWriter writer = new BufferedWriter(new FileWriter("Training_Vector/TRAINING_VECTOR(social)"+typeArray[i]+".txt"));
				for(int j=0; j<mainVector.size(); j++){
					StringBuilder vectorBuf = new StringBuilder();
					Vector<Integer> vectorValueStore = mainVector.get(j);
					for(int k=0; k<vectorValueStore.size() && k<vectorSize; k++){
						vectorBuf.append((k+1) + ":" + vectorValueStore.get(k) + " ");
					}
					writer.write("+1"+" "+vectorBuf.toString()+"\n");
				}
				
				for(int j=0; j<negVector.size(); j++){
					StringBuilder vectorBuf = new StringBuilder();
					Vector<Integer> vectorValueStore = negVector.get(j);
					for(int k=0; k<vectorValueStore.size() && k<vectorSize; k++){
						vectorBuf.append((k+1) + ":" + vectorValueStore.get(k) + " ");
					}
					writer.write("-1"+" "+vectorBuf.toString()+"\n");
				}

				writer.close();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	//to debug
	private void generateCombined(String typeSign, List<String> asList,
			String folder, String name, boolean append) {
		try {
			for(int i=0; i<typeArray.length; i++){
				BufferedWriter writer = new BufferedWriter(new FileWriter(folder+name+typeArray[i]+".txt", append));
				List<Vector<Vector<Integer>>> pts = new ArrayList<Vector<Vector<Integer>>>();
				//generate list of pts
				for(int j=0; j<asList.size(); j++){
					String key = asList.get(j);
					pts.add(vectorStore.get(key+typeArray[i]));
				}
				
				int numofrow = pts.get(0).size();
				for(int j=0; j<numofrow; j++){
					String appendString = typeSign;
					//for each pt, append row j
					int count =1;
					for(int k=0; k<pts.size(); k++){
						Vector<Vector<Integer>> pt =pts.get(k);
						Vector<Integer> pt_row = pt.get(j);
						System.out.println(j+ " " + k );
						//for each col in row in pt
						for(int l=0; l<pt_row.size(); l++){
							appendString += " "+ count++ +":"+pt.get(l);
						}
					}
					writer.write(appendString);
				}				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void generateCombined(String typeSign, String key1, String key2, String folder, String name, boolean append) {
		
		try {
			
			for(int i=0; i<typeArray.length; i++){
				BufferedWriter writer = new BufferedWriter(new FileWriter(folder+name+typeArray[i]+".txt", append));
				Vector<Vector<Integer>> pt1 = vectorStore.get(key1+typeArray[i]);
				Vector<Vector<Integer>> pt2 = vectorStore.get(key2+typeArray[i]);
				for(int j=0; j<pt1.size(); j++){
					int count = 1;
					Vector<Integer> values1 = pt1.get(j);
					Vector<Integer> values2 = pt2.get(j);
					
					//pt1.get(j).addAll(pt2.get(j));
					String appendString = typeSign;
					for(int k=0;k<values1.size();k++){
						appendString += " "+count+++":"+values1.get(k);
					}
					for(int k=0;k<values2.size();k++){
						appendString += " "+count+++":"+values2.get(k);
					}
					writer.write(appendString+"\n");
				}
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
private void generateCombined(String typeSign,  String key1, String folder, String name, boolean append) {
		
		try {
			
			for(int i=0; i<typeArray.length; i++){
				BufferedWriter writer = new BufferedWriter(new FileWriter(folder+name+typeArray[i]+".txt", append));
				Vector<Vector<Integer>> pt1 = vectorStore.get(key1+typeArray[i]);
				for(int j=0; j<pt1.size(); j++){
					int count = 1;
					Vector<Integer> values1 = pt1.get(j);
					
					//pt1.get(j).addAll(pt2.get(j));
					String appendString = typeSign;
					for(int k=0;k<values1.size();k++){
						appendString += " "+count+++":"+values1.get(k);
					}
					writer.write(appendString+"\n");
				}
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
private void generateCombined(String typeSign, String key1, String key2, String key3, String folder, String name, boolean append) {
		
		try {
			
			for(int i=0; i<typeArray.length; i++){
				BufferedWriter writer = new BufferedWriter(new FileWriter(folder+name+typeArray[i]+".txt", append));
				Vector<Vector<Integer>> pt1 = vectorStore.get(key1+typeArray[i]);
				Vector<Vector<Integer>> pt2 = vectorStore.get(key2+typeArray[i]);
				Vector<Vector<Integer>> pt3 = vectorStore.get(key3+typeArray[i]);
				for(int j=0; j<pt1.size(); j++){
					int count = 1;
					Vector<Integer> values1 = pt1.get(j);
					Vector<Integer> values2 = pt2.get(j);
					Vector<Integer> values3 = pt3.get(j);
					
					//pt1.get(j).addAll(pt2.get(j));
					String appendString = typeSign;
					for(int k=0;k<values1.size();k++){
						appendString += " "+count+++":"+values1.get(k);
					}
					for(int k=0;k<values2.size();k++){
						appendString += " "+count+++":"+values2.get(k);
					}
					
					for(int k=0;k<values3.size();k++){
						appendString += " "+count+++":"+values3.get(k);
					}
					writer.write(appendString+"\n");
				}
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void generateTestIndex(String field, List<List<String>> ttList, List<List<String>> chittList) {
		System.out.println("TEST");
		Vector<Vector<Integer>> vStore = new Vector<Vector<Integer>>();
		Indexer testIndex = new Indexer("TEST/test.txt", "TEST", vectorSize, field);
		testIndex.run();
		IndexReader tesReader = testIndex.getReader();
				
		System.out.println("TEST"+ttList.size() + " " + testIndex.getNumDocs());
		try{
			for(int i=0; i<ttList.size(); i++){
				BufferedWriter writer = new BufferedWriter(new FileWriter("Test_Vector/class/testvector("+field+")"+typeArray[i]+".txt"));
				List<String> tt = ttList.get(i);
				vStore = new Vector<Vector<Integer>>();
				for(int j=0; j<testIndex.getNumDocs(); j++){
					StringBuilder vectorBuf = new StringBuilder();
					TermFreqVector tfv = tesReader.getTermFreqVector(j, field);
					String[] textArray = (tfv==null)?new String[0]:tfv.getTerms();
					Vector<Integer> vectorValueStore = new Vector<Integer>();
					for(int k=0; k<tt.size() && k<vectorSize; k++){
						vectorBuf.append((k+1) + ":");
		        		boolean exist = false;
		        		for(String text: textArray){
		        			if(tt.get(k).equalsIgnoreCase(text)){
		        				exist = true;
		        				break;
		        			}
		        		}
		        		if(exist){ 
		        			vectorValueStore.add(1);
		        			vectorBuf.append("1 ");
		        		}
		        		else{ 
		        			vectorValueStore.add(0);
		        			vectorBuf.append("0 ");
		        		}
					}
					vStore.add(vectorValueStore);
					writer.write("+1"+" "+vectorBuf.toString()+"\n");
				}
				writer.close();
				vectorStore.put("test_"+field+"_"+typeArray[i], vStore);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void generateTextVector(String field, List<Indexer> indexerList, List<String> negativeFileNameList) {
		
		List<Map<String, Integer>> dfMapList = new ArrayList<Map<String, Integer>>();
		List<List<String>> ttList = new ArrayList<List<String>>();
		List<Integer> docNumList = new ArrayList<Integer>();
		List<Map<String, Double>> chiMapList = new ArrayList<Map<String, Double>>();
		List<List<String>> chiTtList = new ArrayList<List<String>>();
		
		//run index
		for(int i=0; i<indexerList.size(); i++){
			Indexer index = indexerList.get(i);
			index.run();
			dfMapList.add(index.getFrequencyMap());
			ttList.add(index.getTopTerms());
			docNumList.add(index.getNumDocs());
		}
		
		topTermListMap.put("ttl"+field, ttList);
		
		for(int i=0; i<indexerList.size(); i++){
			Indexer index = indexerList.get(i);
			vectorStore.put(field+"_"+typeArray[i], index.generateTermVectors());
		}
		
		//generate chi stat & negative terms
		for(int i=0; i<indexerList.size(); i++){
			List<Map<String, Integer>> residueDfMapList = new ArrayList<Map<String, Integer>>(dfMapList);
			List<Integer> residueDocNumList = new ArrayList<Integer>(docNumList);
			List<List<String>> residueTtList = new ArrayList<List<String>>(ttList);
			List<Indexer> residueIndexList = new ArrayList<Indexer>(indexerList);
			residueDfMapList.remove(i);
			residueDocNumList.remove(i);
			residueTtList.remove(i);
			residueIndexList.remove(i);
			generateNegativeTerms(i, field, dfMapList.get(i), ttList.get(i), indexerList.get(i), negativeFileNameList.get(i), residueDfMapList, residueTtList, residueIndexList, "");
			chiMapList.add(generatCHI(dfMapList.get(i), ttList.get(i), docNumList.get(i), residueDfMapList, residueDocNumList));
			chiTtList.add(generateList(chiMapList.get(i)));
			
			//user chiTT to generate terms
			//System.out.println("CHITT SIZE: "+chiTtList.get(i).get(1));
			Vector<Vector<Integer>> chiList = indexerList.get(i).generateTermVectorsWithTopTerms(chiTtList.get(i), "CHI");
			vectorStore.put("CHI"+field+"_"+typeArray[i], chiList); 
			generateNegativeTerms(i, field, dfMapList.get(i), chiTtList.get(i), indexerList.get(i), negativeFileNameList.get(i), residueDfMapList, residueTtList, residueIndexList, "_CHI");
		}
		topTermListMap.put("chittl"+field, chiTtList);
	}

	private void generateTextVector() {
		indexNUS1 = new Indexer("TRAIN/NUS1.txt", "NUS1 ", vectorSize, "text");
		indexNUS2 = new Indexer("TRAIN/NUS2.txt", "NUS2", vectorSize, "text");
		indexDBS1 = new Indexer("TRAIN/DBS1.txt", "DBS1", vectorSize, "text");
		indexDBS2 = new Indexer("TRAIN/DBS2.txt", "DBS2", vectorSize, "text");
		indexStarhub = new Indexer("TRAIN/starhub.txt", "Starhub", vectorSize, "text");
		
		indexNUS1.run();
		indexNUS2.run();
		indexDBS1.run();
		indexDBS2.run();
		indexStarhub.run();
		
		NUS1_df = indexNUS1.getFrequencyMap();
		NUS2_df = indexNUS2.getFrequencyMap();
		DBS1_df = indexDBS1.getFrequencyMap();
		DBS2_df = indexDBS2.getFrequencyMap();
		Starhub_df = indexStarhub.getFrequencyMap(); 
		
		NUS1_tt = indexNUS1.getTopTerms();
		NUS2_tt = indexNUS2.getTopTerms();
		DBS1_tt = indexDBS1.getTopTerms();
		DBS2_tt = indexDBS2.getTopTerms();
		Starhub_tt = indexStarhub.getTopTerms();
		
		NUS1_docNum = indexNUS1.getNumDocs();
		NUS2_docNum = indexNUS2.getNumDocs();
		DBS1_docNum = indexDBS1.getNumDocs();
		DBS2_docNum = indexDBS2.getNumDocs();
		Starhub_docNum = indexStarhub.getNumDocs();
		N = NUS1_docNum + NUS2_docNum + DBS1_docNum + DBS2_docNum + Starhub_docNum;
		
		NUS1_CHI = generatCHI(NUS1_df, NUS1_tt, NUS1_docNum, Arrays.asList(NUS2_df, DBS1_df, DBS2_df, Starhub_df), 
				 Arrays.asList(NUS2_docNum, DBS1_docNum, DBS2_docNum, Starhub_docNum));
		NUS2_CHI = generatCHI(NUS2_df, NUS2_tt, NUS2_docNum, Arrays.asList(NUS1_df, DBS1_df, DBS2_df, Starhub_df), 
				 Arrays.asList(NUS1_docNum, DBS1_docNum, DBS2_docNum, Starhub_docNum));
		DBS1_CHI = generatCHI(DBS1_df, DBS1_tt, DBS1_docNum, Arrays.asList(NUS1_df, NUS2_df, DBS2_df, Starhub_df), 
				 Arrays.asList(NUS1_docNum, NUS2_docNum, DBS2_docNum, Starhub_docNum));
		DBS2_CHI = generatCHI(DBS2_df, DBS2_tt, DBS2_docNum, Arrays.asList(NUS1_df, NUS2_df, DBS1_df, Starhub_df), 
				 Arrays.asList(NUS1_docNum, NUS2_docNum, DBS1_docNum, Starhub_docNum));
		Starhub_CHI = generatCHI(Starhub_df, Starhub_tt, Starhub_docNum, Arrays.asList(NUS1_df, NUS2_df, DBS1_df, DBS2_df), 
				 Arrays.asList(NUS1_docNum, NUS2_docNum, DBS1_docNum, DBS2_docNum));
		
		NUS1_CHItt = generateList(NUS1_CHI);
		NUS2_CHItt = generateList(NUS2_CHI);
		DBS1_CHItt = generateList(DBS1_CHI);
		DBS2_CHItt = generateList(DBS2_CHI);
		Starhub_CHItt = generateList(Starhub_CHI);
		
		printTerms(NUS1_CHItt, NUS1_CHI);
		
		indexNUS1.generateTermVectors();
		indexNUS2.generateTermVectors();
		indexDBS1.generateTermVectors();
		indexDBS2.generateTermVectors();
		indexStarhub.generateTermVectors();
		
		generateNegativeTerms(0, "text", NUS1_df, NUS1_tt, indexNUS1, "NUS1_NEG", Arrays.asList(NUS2_df, DBS1_df, DBS2_df, Starhub_df), 
				 Arrays.asList(NUS2_tt, DBS1_tt, DBS2_tt, Starhub_tt));
		generateNegativeTerms(1, "text", NUS2_df, NUS2_tt, indexNUS2, "NUS2_NEG", Arrays.asList(NUS1_df, DBS1_df, DBS2_df, Starhub_df), 
				 Arrays.asList(NUS1_tt, DBS1_tt, DBS2_tt, Starhub_tt));
		generateNegativeTerms(2, "text", DBS1_df, DBS1_tt, indexDBS1, "DBS1_NEG", Arrays.asList(NUS1_df, NUS2_df, DBS2_df, Starhub_df), 
				 Arrays.asList(NUS1_tt, NUS2_tt, DBS2_tt, Starhub_tt));
		generateNegativeTerms(3, "text", DBS2_df, DBS2_tt, indexDBS2, "DBS2_NEG", Arrays.asList(NUS1_df, NUS2_df, DBS1_df, Starhub_df), 
				 Arrays.asList(NUS1_tt, NUS2_tt, DBS1_tt, Starhub_tt));
		generateNegativeTerms(4, "text", Starhub_df, Starhub_tt, indexStarhub, "Starhub_NEG", Arrays.asList(NUS1_df, NUS2_df, DBS1_df, DBS2_df), 
				 Arrays.asList(NUS1_tt, NUS2_tt, DBS1_tt, DBS2_tt));
	}
	
	//not used
	private void generateNegativeTerms(int negType, String type, Map<String, Integer> fm, List<String> tt, Indexer index, String index_name,
			List<Map<String, Integer>> fm_list, List<List<String>> tt_lists) {
		String textVectorFileName = "Training_Vector_NEG/TRAINING_VECTOR_"+index_name+".txt";
		Map<String, Double> negTermMap = new HashMap<String, Double>();
		ValueComparator bvc =  new ValueComparator(negTermMap);
        TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);
        
        int minNegSize = negativeTermSize;
        for(List<String> ttList: tt_lists){
        	if(ttList.size() < minNegSize) minNegSize = ttList.size(); 
        }
        
//        // generate negative vector based on df
//        negTermMap = mostFrequentNegativeTerms(fm_list, tt_lists, minNegSize);
//        topNEGStore.put(type, index.generateVectors("-1", "Training_Vector_NEG/TRAINING_VECTOR_"+index_name+".txt", negTermMap, minNegSize));
//	
//        // generate based on similar terms to positive
//        negTermMap = similarNegativeTerms(fm, tt, fm_list, tt_lists, minNegSize);
//        similarNEGStore.put(type, index.generateVectors("-1", "Training_Vector_NEG/SIMILAR_TRAINING_VECTOR_"+index_name+".txt", negTermMap, minNegSize));
//	
//        // generate negative vector based on random
//        negTermMap = randomNegativeTerms(fm_list, tt_lists, minNegSize);
//        randomNEGStore.put(type, index.generateVectors("-1", "Training_Vector_NEG/RANDOM_TRAINING_VECTOR_"+index_name+".txt", negTermMap, minNegSize));
        
	}
	
	private void generateNegativeTerms(int negType, String field, Map<String, Integer> fm, List<String> tt, Indexer index, String index_name,
			List<Map<String, Integer>> fm_list, List<List<String>> tt_lists, List<Indexer> indexer_list, String chi) {
		//String textVectorFileName = "Training_Vector_NEG/TRAINING_VECTOR_"+index_name+".txt";
//		String textVectorFileName = "Training_Vector/TRAINING_VECTOR_"+index_name+".txt";
		Map<String, Double> negTermMap = new HashMap<String, Double>();
		ValueComparator bvc =  new ValueComparator(negTermMap);
        TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);
        
        int minNegSize = negativeTermSize;
        for(List<String> ttList: tt_lists){
        	if(ttList.size() < minNegSize) minNegSize = ttList.size(); 
        }
        //negative document List
        // apple = 621
 		// google = 271
 		// microsoft = 535
 		// twitter = 412  
        
        Vector<Vector<Integer>> tweetVectors ;
        List<List<Integer>> negDocList = new ArrayList<List<Integer>>();
        if(negTermStore.containsKey(typeArray[negType]+"_")){
        	negDocList = negTermStore.get(typeArray[negType]+"_");
        }
        else{
	        
	        switch(negType){
	        case 0:
//	        	negDocList.add(getSimilarIntList(300, 1000, index, indexer_list.get(1)));
//	        	negDocList.add(getSimilarIntList(100, 200, index, indexer_list.get(2)));
//	        	negDocList.add(getSimilarIntList(300, 1000, index, indexer_list.get(3)));
	        	negDocList.add(getRandomIntList(121, 271));
	        	negDocList.add(getRandomIntList(300, 535));
	        	negDocList.add(getRandomIntList(200, 412));
	       
	//        	System.out.println(indexer_list.get(0).getNumDocs() + " " + negDocList.get(0).size()+" 200 ////");
	//        	System.out.println(indexer_list.get(1).getNumDocs() + " " + negDocList.get(1).size()+" 350 ////");
	//        	System.out.println(indexer_list.get(2).getNumDocs() + " " + negDocList.get(2).size()+" 100 ////");
	//        	System.out.println(indexer_list.get(3).getNumDocs() + " " + negDocList.get(3).size()+" 350 ////");
	        	break;
	        case 1:
	        	negDocList.add(getRandomIntList(100, 621));
	        	negDocList.add(getRandomIntList(100, 535));
	        	negDocList.add(getRandomIntList(100, 412));
	        	break;
	        case 2:
	        	negDocList.add(getRandomIntList(235, 621));	   
		        negDocList.add(getRandomIntList(100, 271));
	        	negDocList.add(getRandomIntList(200, 412));
		    	break;
	        case 3:
	        	negDocList.add(getRandomIntList(200, 621));	   
		        negDocList.add(getRandomIntList(100, 271));
	        	negDocList.add(getRandomIntList(112, 535));
	        	break;
	        }
        }
        //for social feature
        negTermStore.put(typeArray[negType]+"_"+chi, negDocList);
        
        tweetVectors = printNegativeVector(field, tt, indexer_list, negDocList, minNegSize, index_name, chi);
        vectorStore.put("neg_"+chi.replace("_", "")+field+"_"+typeArray[negType], tweetVectors);
        
//        // generate negative vector based on df
//        negTermMap = mostFrequentNegativeTerms(fm_list, tt_lists, minNegSize);
//        topNEGStore.put(type, index.generateVectors("-1", "Training_Vector_NEG/TRAINING_VECTOR_"+index_name+".txt", negTermMap, minNegSize));
//	
//        // generate based on similar terms to positive
//        negTermMap = similarNegativeTerms(fm, tt, fm_list, tt_lists, minNegSize);
//        similarNEGStore.put(type, index.generateVectors("-1", "Training_Vector_NEG/SIMILAR_TRAINING_VECTOR_"+index_name+".txt", negTermMap, minNegSize));
//	
//        // generate negative vector based on random
//        negTermMap = randomNegativeTerms(fm_list, tt_lists, minNegSize);
//        randomNEGStore.put(type, index.generateVectors("-1", "Training_Vector_NEG/RANDOM_TRAINING_VECTOR_"+index_name+".txt", negTermMap, minNegSize));
        	
	}
	
	private List<Integer> getSimilarIntList(int want, int has, Indexer mainIndex,
			Indexer getFromIndex) {
		System.out.println("ScoreDoc txl: enter");
		List<Integer> resultsIntArray = new ArrayList<Integer>();
		Map<Integer, Integer> counterMap = new HashMap<Integer, Integer>();
		int check = 0;
		try{
			IndexSearcher getFromSearcher = getFromIndex.getSearcher();
			IndexReader mainReader = mainIndex.getReader();
			IndexReader getFromReader = getFromSearcher.getIndexReader();
			
			MoreLikeThis mlt = new MoreLikeThis(getFromReader); // Pass the index reader
			mlt.setFieldNames(new String[] {"text"}); // specify the fields for similiarity

			for(int j=0; j<mainReader.numDocs(); j++){
				check=j;
				// compareDoc = mainReader.document(j);
//				Query query = mlt.like(j); // Pass the doc id 
				
				TermFreqVector tfv = mainReader.getTermFreqVector(j, "text");
	        	String[] textArray = tfv.getTerms();
	        	String queryString ="";
	        	for(String tString:textArray){
	        		queryString += tString+" ";
	        	}
	        	System.out.println("ScoreDoc queryString: " + queryString);
				if(textArray!=null){
					Query query = new QueryParser(Version.LUCENE_CURRENT, "text", new StandardAnalyzer(Version.LUCENE_36)).parse(queryString);
					TopDocs similarDocs = getFromSearcher.search(query, want); // Use the searcher
	//				System.out.println("ScoreDoc Q: "+j+" "+ similarDocs.scoreDocs.length);
					for(int i=0; i<similarDocs.scoreDocs.length; i++){
						ScoreDoc scoreDoc = similarDocs.scoreDocs[i];
	//					System.out.println("  ScoreDoc: "+scoreDoc.doc);
						int scoreKey = scoreDoc.doc;
						if(counterMap.containsKey(scoreKey)){
							counterMap.put(scoreKey, (counterMap.get(scoreKey)+1));
						}else{
							counterMap.put(scoreKey, 1);
						}
					}
				}
			}
			IntValueComparator bvc =  new IntValueComparator(counterMap);
	        TreeMap<Integer,Integer> sorted_map = new TreeMap<Integer,Integer>(bvc);
	        sorted_map.putAll(counterMap);
	        Integer[] tempArray = sorted_map.keySet().toArray(new Integer[sorted_map.keySet().size()]);
	        resultsIntArray = Arrays.asList(tempArray).subList(0, want); 	
	        System.out.println(resultsIntArray);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("check: "+check);
		}
		System.out.println("exit "+ resultsIntArray.size());
		return resultsIntArray;
	}

	private Vector<Vector<Integer>> printNegativeVector(String type, List<String> tt,
			List<Indexer> indexer_list, List<List<Integer>> negDocList, int minNegSize, String index_name, String chi) {
		Vector<Vector<Integer>> tweetVectors = new Vector<Vector<Integer>>();
		//String textVectorFileName = "Training_Vector_NEG/TRAINING_VECTOR"+index_name+".txt";
		String textVectorFileName = "Training_Vector/TRAINING_VECTOR"+chi+index_name+".txt";
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(textVectorFileName, true));
			int count=0;
	    	for(int i=0; i<negDocList.size(); i++){
	    		Indexer tempIndex = indexer_list.get(i);
	    		IndexReader reader = tempIndex.getReader();
	    		List<Integer> numberList = negDocList.get(i);
	    		for(int k=0; k<numberList.size(); k++){
	    			StringBuilder vectorBuf = new StringBuilder();
	    			//System.out.println("Value: "+ numberList.get(k) + " " + tempIndex.getNumDocs() + " "+i + " " +k+" "+ count++);
		        	TermFreqVector tfv = reader.getTermFreqVector(numberList.get(k), type);
		        	String[] textArray = (tfv==null)?new String[0]:tfv.getTerms();
		        	Vector<Integer> vectorValueStore = new Vector<Integer>();
		        	//TEST
		        	//System.out.println(textArray.length+ " " +Arrays.toString(textArray));
		        	//indicate how many terms in vector
		        	//for(int j=0; j<minNegSize; j++){
		        	for(int j=0; j<tt.size() && j<vectorSize; j++){
		        		vectorBuf.append((j+1) + ":");
		        		boolean exist = false;
		        		//if(chi.equalsIgnoreCase("_CHI")) System.out.println("In CHI: "+i+" "+k+" "+j);
		        		for(String text: textArray){
		        			if(tt.get(j).equalsIgnoreCase(text)){
		        				exist = true;
		        				break;
		        			}
		        		}
		        		if(exist){ 
		        			vectorValueStore.add(1);
		        			vectorBuf.append("1 ");
		        		}
		        		else{ 
		        			vectorValueStore.add(0);
		        			vectorBuf.append("0 ");
		        		}
		        	}
		        	tweetVectors.add(vectorValueStore);
		        	//if(chi.equalsIgnoreCase("_CHI")) System.out.println("In CHI 2: "+textVectorFileName+" "+vectorBuf.toString());
		        	writer.write("-1"+" "+vectorBuf.toString()+"\n");
	    		}
	    	}
	    	writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return tweetVectors;
	}

	private List<Integer> getRandomIntList(int want, int has) {	
		List<Integer> randomIntArray = new ArrayList<Integer>();
		for(int i=0;i<has;i++){
			randomIntArray.add(i);  // list contains: [0,1,2,3,4,5,6,7,8,9]
			}
		Collections.shuffle(randomIntArray);
		return randomIntArray.subList(0, want);
	}

	private List<Integer> getIntList(int size) {
		List<Integer> intArr = new ArrayList<Integer>();
		for(int i=0; i<size; i++){
			intArr.add(i);
		}
		return intArr;
	}

	private Map<String, Double> randomNegativeTerms(
			List<Map<String, Integer>> fm_list, List<List<String>> tt_lists, int minNegSize) {
		Map<String, Double> negTermMap = new HashMap<String, Double>();
		// generatate random set 
		List<List<Integer>> randomList = new ArrayList<List<Integer>>();
		for(int k=0; k<tt_lists.size(); k++){
			List<Integer> randomIntArray = new ArrayList<Integer>();
			for(int i=0;i<tt_lists.get(k).size();i++){
				randomIntArray.add(i);  // list contains: [0,1,2,3,4,5,6,7,8,9]
				}
			Collections.shuffle(randomIntArray);
			randomList.add(randomIntArray);
		}
		//keep adding, till reaches negative vector size
        for(int i=0; negTermMap.size()<minNegSize; i++ ){
        	List<String> termList = tt_lists.get(i%tt_lists.size());
        	List<Integer> randomIntArray = randomList.get(i%tt_lists.size());
        	//System.out.println(randomIntArray.size()+" "+i+" "+randomIntArray.get(i));
        	//randomIntArray.get(i);
        	String termToAdd = termList.get(randomIntArray.get(i));
        	//String termToAdd = termList.get(randomIntArray.get(i/tt_lists.size()));
        	if(negTermMap.containsKey(termToAdd)){
        		continue;
        	}
        	negTermMap.put(termToAdd, (double)fm_list.get(i%tt_lists.size()).get(termToAdd));
        }
        return negTermMap;
	}

	private Map<String, Double> similarNegativeTerms(Map<String, Integer> fm,
			List<String> tt, List<Map<String, Integer>> fm_list,
			List<List<String>> tt_lists, int minNegSize) {
		Map<String, Double> negTermMap = new HashMap<String, Double>();
		//add similar terms first
		for(int i=0; (i<tt.size() && negTermMap.size()<minNegSize); i++){
			String termToAdd = tt.get(i);
			for(Map<String, Integer> freqMap: fm_list){
				if(freqMap.containsKey(termToAdd)){
					negTermMap.put(termToAdd, (double)freqMap.get(termToAdd));
					break;
				}
			}
		}
		//keep adding, till reaches negative vector size
        for(int i=0; negTermMap.size()<minNegSize; i++ ){
        	List<String> termList = tt_lists.get(i%tt_lists.size());
        	String termToAdd = termList.get(i/tt_lists.size());
        	if(negTermMap.containsKey(termToAdd)){
        		continue;
        	}
        	negTermMap.put(termToAdd, (double)fm_list.get(i%tt_lists.size()).get(termToAdd));
        }
        return negTermMap;
	}

	private Map<String, Double> mostFrequentNegativeTerms(List<Map<String, Integer>> fm_list, List<List<String>> tt_lists, int minNegSize) {
		Map<String, Double> negTermMap = new HashMap<String, Double>();
		//keep adding, till reaches negative vector size
        for(int i=0; negTermMap.size()<minNegSize; i++ ){
        	List<String> termList = tt_lists.get(i%tt_lists.size());
        	String termToAdd = termList.get(i/tt_lists.size());
        	if(negTermMap.containsKey(termToAdd)){
        		continue;
        	}
        	negTermMap.put(termToAdd, (double)fm_list.get(i%tt_lists.size()).get(termToAdd));
        }
        return negTermMap;
	}

	private void generateTrainingVectors() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("NUS_trainingVector.txt"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void printTerms(List<String> topTerms, Map<String, Double> frequencyMap) {
		StringBuilder termBuf = new StringBuilder();
        for (String topTerm : topTerms) {
          termBuf.append(topTerm).
            append("(").
            append(frequencyMap.get(topTerm)).
            append(");");
        }
        System.out.println(">>> top terms: " + termBuf.toString());
	}

	private List<String> generateList(Map<String, Double> map) {
		List<String> termlist = new ArrayList<String>();
		for (Map.Entry<String, Double> entry : map.entrySet())
		{
			termlist.add(entry.getKey());
		    //System.out.println(entry.getKey() + "/" + entry.getValue());
		}
		return termlist;
	}

	private Map<String, Double> generatCHI(Map<String, Integer> CAT_df, List<String> CAT_tt, 
		int CAT_docNum, List<Map<String, Integer>> list, List<Integer> residueDocNum) {
		Map<String, Double> CHI_MAP = new HashMap<String, Double>();
		ValueComparator bvc =  new ValueComparator(CHI_MAP);
        TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);
		double A,B,C,D;
		// A = number of documents in C containing W
		// B = number of documents not in C containing W
		// C = number of documents in C not containing W
		// D = number of documents not in C not containing W
		
		// compute number of documents not in C
		int numDocNotInC = getNumDocNotInC(residueDocNum);
		for(int i=0; i<CAT_tt.size(); i++){
			String W = CAT_tt.get(i);
			// A = number of documents in C containing W
			A = CAT_df.get(W);
			// B = number of documents not in C containing W
			B = computB(W, list);
			// C = number of documents in C not containing W
			C = CAT_docNum - A;
			// D = number of documents not in C not containing W
			D = numDocNotInC - B;
			
			//for each term, generate CHI values using ABCD
			double chi = (((double)N*((A*D)-(C*B)))/((A+C)*(B+D)*(A+B)*(C+D))); 
			CHI_MAP.put(W, chi);
			//System.out.println(W+": "+chi);
			//check for correct doc sum
//			double NTemp = A+B+C+D;
//			if(A>38)
//			System.out.println(((NUS1_docNum + NUS2_docNum + DBS1_docNum + DBS2_docNum + Starhub_docNum)== A+B+C+D )+ 
//			" total:" + NTemp + " N:" + N + " W:" + W + " A:" + A + " B:" + B + " C:" + C + " D:" + D + " CHI:" + chi);
		}
		sorted_map.putAll(CHI_MAP);
		//System.out.println("MapSize: "+sorted_map.size());
		return sorted_map;
	}

	private int getNumDocNotInC(List<Integer> residueDocNum) {
		int count = 0;
		for(Integer num: residueDocNum){
			count += num;
		}
		return count;
	}

	private double computD(String W, List<Map<String, Integer>> list, int B) {
		double WCount = 0;
		for(int i=0; i<list.size(); i++){
			System.out.println("list size: " +list.size());
			Map<String, Integer> tempCAT = list.get(i);
			WCount += tempCAT.get(W);
		}
		return WCount - B;
	}

	private double computB(String W, List<Map<String, Integer>> list) {
		double WCount = 0;
		for(int i=0; i<list.size(); i++){
			Map<String, Integer> tempCAT = list.get(i);
			if(tempCAT.containsKey(W))
				WCount += tempCAT.get(W);
		} 
		return WCount;
	}
}

class ValueComparator implements Comparator<String> {

    Map<String, Double> base;
    public ValueComparator(Map<String, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}

class IntValueComparator implements Comparator<Integer> {

    Map<Integer, Integer> base;
    public IntValueComparator(Map<Integer, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(Integer a, Integer b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
