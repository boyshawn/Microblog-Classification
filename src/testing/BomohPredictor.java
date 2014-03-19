package testing;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import cmu.arktweetnlp.Tagger;
import cmu.arktweetnlp.Tagger.TaggedToken;

public class BomohPredictor {
	
	HashMap<String, HashMap<String,String>> basicLex, learnLex;
	Tagger tagger;
	ArrayList<Integer> prediction, actual;
	
	public BomohPredictor(String fn, String fn1, String fn2) throws IOException, JSONException{
		HashMap<String, HashMap<String,String>> mpqaLex = readLexiconFromMPQA(fn);
		basicLex = mpqaLex;
		learnLex = learn(fn1, fn2);
		
		String modelFilename = "/cmu/arktweetnlp/model.20120919";
		tagger = new Tagger();
		tagger.loadModel(modelFilename);
		
		prediction = new ArrayList<Integer>();
		actual = new ArrayList<Integer>();
	}
	
	public void coconutDropFromSky(){
		System.out.println("A coconut has dropped onto your lap! Bomoh boleh! Coconut sedap!");
	}
	
	private void train(String fntweets, String fnlabel, String ofn) throws IOException, JSONException {
		// 1. read in training tweets & build vectors
		ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
		String line;
		BufferedReader br;
		br = new BufferedReader(new FileReader(fntweets));
		while((line = br.readLine()) != null){
			JSONObject tweet = new JSONObject(line);
			String text = tweet.getString("text");
			res.add(buildvector(text));
		}
		br.close();
		
		// 2. read labels
		ArrayList<Integer> act = new ArrayList<Integer>();
		String[] arr;
		String real;
		int r;
		br = new BufferedReader(new FileReader(fnlabel));
		while((line = br.readLine()) != null){
			arr = line.split(",");
			real = arr[1];
			if (real.equalsIgnoreCase("\"positive\"")) r=0;
			else if (real.equalsIgnoreCase("\"negative\"")) r=1;
			else r=2;
			act.add(r);
		}
		br.close();
		
		// 3. Combine gold standard + result vector. Write to file
		if (act.size() != res.size()){ 
			System.out.println("Size mismatch in BomohPredictor.train() !"); return;}
		PrintWriter tw = new PrintWriter(new FileWriter(ofn));
		for (int i=0; i<act.size(); i++){ 
			line = Integer.toString(act.get(i)) + " " + stringify(res.get(i));
			tw.println(line);
		}
		tw.close();
		
//		// 4. Trains SVM and outputs <ofn>.model in same DIR
//		PrintStream original = new PrintStream(System.out);
//		System.setOut(new PrintStream(new File(ofn+".scale")));
//		String argc[] = {"-s", "data\\TRAIN\\range", ofn};
//		br = new BufferedReader(new InputStreamReader(System.in));
//		svm_scale.main(argc);
//		System.setOut(original);
		
//		String argv[] = {"-s", "0", "-t", "0", ofn+".scale"};
		
		svm_train t = new svm_train();
		String argv[] = {"-s", "0", "-t", "2", ofn};
		t.run(argv);
	}	
	
	private String stringify(ArrayList<String> arrayList) { // Flattens into printable form
		StringBuilder sb = new StringBuilder();
		
		int index = 1;
		for (String s : arrayList){
			sb.append(index);
			sb.append(":");
			sb.append(s);
			sb.append(" ");
			++index;
		}

		return sb.toString();
	}

	private ArrayList<String> buildvector(String text) { // input = tweet's text
		List<TaggedToken> taggedTokens = tagger.tokenizeAndTag(text);
		int pos, neg, neu;
		int lpos, lneg, lneu;
		pos = neg = neu = 0;
		lpos = lneg = lneu = 0;
		String word;
		HashMap<String,String> entry;
		
		for (TaggedToken t : taggedTokens){
			word = t.token.toLowerCase();
			if (basicLex.containsKey(word)){
				 String polarity = basicLex.get(word).get("priorpolarity");
				 if (polarity.equalsIgnoreCase("positive")) pos++;
				 if (polarity.equalsIgnoreCase("negative")) neg++;
				 if (polarity.equalsIgnoreCase("neutral")) neu++;
			 }
			
			if (learnLex.containsKey(word)){
				entry = learnLex.get(word);
				if (!entry.containsKey("ambiguous")){
					String polarity = entry.get("priorpolarity");
					if (polarity.equalsIgnoreCase("positive")) lpos++;
					if (polarity.equalsIgnoreCase("negative")) lneg++;
					if (polarity.equalsIgnoreCase("neutral")) lneu++;
				}
			}
		}
		
		// Hack
		if (pos > 0 || neg > 0 ) neu = 0;
		if (lpos > 0 || lneg > 0 ) lneu = 0;
		
		// build vector
		/*
		 * 1:pos 2:neg 3:neu
		 * */
		ArrayList<String> result = new ArrayList<String>();
		result.add(Integer.toString(pos));
		result.add(Integer.toString(neg));
		result.add(Integer.toString(neu));
		
		result.add(Integer.toString(lpos));
		result.add(Integer.toString(lneg));
		result.add(Integer.toString(lneu));
		
		return result;
	}

	private void run(String fnmodel, String fntweets, String fnres) throws IOException, JSONException{
		// 1. Read in testing tweets
		ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
		String line;
		BufferedReader br;
		br = new BufferedReader(new FileReader(fntweets));
		while((line = br.readLine()) != null){
			JSONObject tweet = new JSONObject(line);
			String text = tweet.getString("text");
			res.add(buildvector(text));
		}
		br.close();
		
		// 2. Write tweets in vector form to file
		String ofn = fntweets + ".outvector";
		PrintWriter tw = new PrintWriter(new FileWriter(ofn));
		for (int i=0; i<res.size(); i++){
			tw.println(stringify(res.get(i)));
		}
		tw.close();
		
		// 3. Classify & read results back into Bomoh
		//Classifier.generateTestResult(fnmodel, ofn, fnres);
//		System.out.println(ofn);
//		PrintStream original = new PrintStream(System.out);
//		System.setOut(new PrintStream(new File(ofn+".scale")));
//		String argz[] = {"-r", "data\\TRAIN\\range", ofn};
//		br = new BufferedReader(new InputStreamReader(System.in));
//		svm_scale.main(argz);
//		System.setOut(original);
		
//		svm_predict t = new svm_predict();
//		String args[] = {ofn+".scale", fnmodel, fnres};
		String args[] = {ofn, fnmodel, fnres}; 
		svm_predict.main(args);
		Double x;
		br = new BufferedReader(new FileReader(fnres));
		while((line = br.readLine()) != null){
			x = Double.parseDouble(line);
			prediction.add(x.intValue());
		}
		br.close();
	}
	
	private void old_run(String testDataFile) throws IOException, JSONException{
		
		//////////////////////////////////////////////
		// 1. Read in adjectives from general inquirer
		
		HashMap<String, Integer> posLex = readLexiconFromGI("data\\GI_positive.txt", 0); //0
		HashMap<String, Integer> negLex = readLexiconFromGI("data\\GI_negative.txt", 1); //1
		HashMap<String, Integer> neuLex = readLexiconFromGI("data\\GI_neutral.txt", 2); //2
		
		// 1.1 Get MPQA
		HashMap<String, HashMap<String,String>> mpqaLex = readLexiconFromMPQA("data\\subjclueslen1-HLTEMNLP05.tff");
		
		// 1.2 Learn from training data
		HashMap<String, HashMap<String,String>> learnLex = learn("TRAIN\\tweets_train.txt", "TRAIN\\label_train.txt");
		//learnLex.putAll(mpqaLex);
		//mpqaLex = learnLex;
		
		//System.out.println(mpqaLex.get("abase").get("priorpolarity"));
	}

	private HashMap<String, Integer> readLexiconFromGI(String fn, Integer x) throws IOException{
		BufferedReader br;
		String line;
		HashMap<String, Integer> Lexicon = new HashMap<String, Integer>();

		br = new BufferedReader(new FileReader(fn));
		while((line = br.readLine()) != null){
			Lexicon.put(line, x);
		}
		br.close();
		
		return Lexicon;
	}
	
	HashMap<String, HashMap<String, String>> readLexiconFromMPQA(String fn) throws IOException{
		BufferedReader br;
		String line, key="";
		String[] parts, values;
		int i;
		HashMap<String, HashMap<String, String>> dic = new HashMap<String, HashMap<String, String>>();
		HashMap<String,String> entry;

		// type=weaksubj len=1 word1=abandoned pos1=adj stemmed1=n priorpolarity=negative
		br = new BufferedReader(new FileReader(fn));
		while((line = br.readLine()) != null){ 
			entry = new HashMap<String,String>();
			parts = line.split(" ");
			if (parts.length==6){
				for (i=0; i<6; i++){
					values = parts[i].split("=");
					entry.put(values[0], values[1]);
					if (i==2){ // use 'word1' as key
						key = values[1];
					}
				}
			}
			
			dic.put(key, entry);
		}
		br.close();
		
		return dic;
	}
	
	private HashMap<String, HashMap<String, String>> learn(String fn, String fn1) throws IOException, JSONException { // Learning hashtags
		String modelFilename = "/cmu/arktweetnlp/model.20120919";
		ArrayList<ArrayList<String>> store = new ArrayList<ArrayList<String>>();
		ArrayList<String> newHashtags = new ArrayList<String>();
		
		List<TaggedToken> taggedTokens;
		Tagger tagger = new Tagger();
		tagger.loadModel(modelFilename);
		 
		BufferedReader br;
		String line;
		br = new BufferedReader(new FileReader(fn));
		while((line = br.readLine()) != null){
			JSONObject tweet = new JSONObject(line);
			String text = tweet.getString("text");
			
			newHashtags = new ArrayList<String>();
			taggedTokens = tagger.tokenizeAndTag(text);
			for (TaggedToken t : taggedTokens){
				if (t.tag.equalsIgnoreCase("#")){ 
					//t.token = t.token.substring(1); // remove hashtag at front
					newHashtags.add(t.token.toLowerCase());
				}
				if (t.tag.equalsIgnoreCase("A")){
					newHashtags.add(t.token.toLowerCase());
				}
				if (t.tag.equalsIgnoreCase("N")){
					newHashtags.add(t.token.toLowerCase());
				}
			}
			store.add(newHashtags);
		}		
		
		int r;
		String real, arr[];
		ArrayList<Integer> gt = new ArrayList<Integer>();
		br = new BufferedReader(new FileReader(fn1));
		while((line = br.readLine()) != null){
			arr = line.split(",");
			real = arr[1];
			if (real.equalsIgnoreCase("\"positive\"")) r=0;
			else if (real.equalsIgnoreCase("\"negative\"")) r=1;
			else r=2;
			
			gt.add(r);
		}
		br.close();
		
		HashMap<String, HashMap<String, String>> hm = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> entry = new HashMap<String, String>();
		String carr[] = {"positive", "negative", "neutral"};
		
		for (int i=0; i<gt.size(); i++){
			ArrayList<String> newTokens = store.get(i);
			String polarity = carr[gt.get(i)];
			for (int j=0; j<newTokens.size(); j++){
				entry = new HashMap<String, String>();
				entry.put("word1", newTokens.get(j));
				entry.put("pos1", "hashtag");
				entry.put("priorpolarity", polarity);
				if (hm.containsKey(newTokens.get(j))){ // set this term as ambiguous if it already exists
					entry.put("ambiguous", "true");
				}
				hm.put(newTokens.get(j), entry);
			}
		}
		
		ArrayList<String> remv = new ArrayList<String>();
		for (String key : hm.keySet()){
			if (hm.get(key).containsKey("ambiguous"))
				remv.add(key);
		}
		for (String key: remv){
			hm.remove(key);
		}
		
		return hm;
	}
	
	
	private void score(String fn, ArrayList<Integer> currPrediction) throws IOException {
		BufferedReader br;
		String line, real, arr[];
		int r;
		actual = new ArrayList<Integer>();

		br = new BufferedReader(new FileReader(fn));
		while((line = br.readLine()) != null){
			arr = line.split(",");
			real = arr[1];
			if (real.equalsIgnoreCase("\"positive\"")) r=0;
			else if (real.equalsIgnoreCase("\"negative\"")) r=1;
			else r=2;
			
			actual.add(r);
		}
		br.close();
		
		// check size are the same
		if (actual.size() != currPrediction.size()){
			System.out.println("Predict & Actual size mismatch!");
			return;
		}
		
		/*
		 * Split into
		 * 	pos | neg+neu
		 *  neg | pos+neu
		 *  neu | pos+neg
		 * */
		
		int tp, tp_fp, tp_fn;
		double pre, rec, f1;
		
		for (int x=0; x<3; x++){ // 0/1/2 -> pos/neg/neu
			tp = tp_fp = tp_fn = 0;
			for (int i=0; i<actual.size();i++){
				if (actual.get(i) == currPrediction.get(i) && actual.get(i)==x) tp++;
				if (currPrediction.get(i) == x) tp_fp++;
				if (actual.get(i) == x) tp_fn++;
			}
			pre = (tp_fp > 0) ? (double)tp / tp_fp : 0;
			rec = (double)tp / tp_fn;
			f1 = (pre+rec != 0) ? 2 * (pre*rec) / (pre+rec) : 0; 
			
			// Display results
			String[] disp = {"positive", "negative", "neutral"};
			System.out.println("Case: " + disp[x]);
			System.out.println("Precision = " + pre);
			System.out.println("Recall = " + rec);
			System.out.println("F1 = " + f1);
			System.out.println();
		}
		
	}
	
	private void printPrediction() {
		String[] disp = {"positive", "negative", "neutral"};
		for (int i=0; i<prediction.size(); ++i){
			System.out.println("" + i + ": " + disp[prediction.get(i)]);
		}
	}
	
	public static void main(String[] args) throws IOException, JSONException{
		
		final String mpqa = "data\\subjclueslen1-HLTEMNLP05.tff";
		final String tweets_train = "data\\TRAIN\\tweets_train.txt";
		final String label_train = "data\\TRAIN\\label_train.txt";
		final String tweets_test = "data\\TEST\\tweets_test.txt";
		final String label_test = "data\\TEST\\label_test.txt";
		
		String outFileName = "data\\TRAIN\\train_vector";
		
		BomohPredictor bomoh = new BomohPredictor(mpqa, tweets_train, label_train);
		bomoh.train(tweets_train, label_train, outFileName);
		
		bomoh.run(outFileName + ".model", tweets_test, "data\\TEST\\result");
		bomoh.score(label_test, bomoh.prediction);
		
		//bomoh.printPrediction();
		//System.out.println("#TEXT".toLowerCase());
		System.out.println(bomoh.learnLex.size());
		
	}


}
