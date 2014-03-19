package testing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import cmu.arktweetnlp.Tagger;
import cmu.arktweetnlp.Tagger.TaggedToken;

public class BomohPredictor {
	
	ArrayList<Integer> prediction = new ArrayList<Integer>();
	ArrayList<Integer> mprediction = new ArrayList<Integer>();
	ArrayList<Integer> actual = new ArrayList<Integer>();
	ArrayList<String> tweetstore = new ArrayList<String>();

	private void run(String testDataFile) throws IOException, JSONException{
		
		//////////////////////////////////////////////
		// 1. Read in adjectives from general inquirer
		
		HashMap<String, Integer> posLex = readLexiconFromGI("data\\GI_positive.txt", 0); //0
		HashMap<String, Integer> negLex = readLexiconFromGI("data\\GI_negative.txt", 1); //1
		HashMap<String, Integer> neuLex = readLexiconFromGI("data\\GI_neutral.txt", 2); //2
		
		// 1.1 Get MPQA
		HashMap<String, HashMap<String,String>> mpqaLex = readLexiconFromMPQA("data\\subjclueslen1-HLTEMNLP05.tff");
		
		// 1.2 Learn from training data
		HashMap<String, HashMap<String,String>> learnLex = learn("TRAIN\\tweets_train.txt", "TRAIN\\label_train.txt");

		learnLex.putAll(mpqaLex);
		mpqaLex = learnLex;
		
		//System.out.println(mpqaLex.get("abase").get("priorpolarity"));
		
		/////////////////////////////
		// 2. read in training tweets
		
		String modelFilename = "/cmu/arktweetnlp/model.20120919";
		List<TaggedToken> taggedTokens;
		Tagger tagger = new Tagger();
		tagger.loadModel(modelFilename);
		 
		BufferedReader br;
		String line, word, mword;
		int pos, neg, neu, pred;
		int mpos, mneg, mneu, mpred;
		br = new BufferedReader(new FileReader(testDataFile));
		while((line = br.readLine()) != null){
			
			pos = neg = neu = 0;
			mpos = mneg = mneu = 0;
			JSONObject tweet = new JSONObject(line);
			String text = tweet.getString("text");
			tweetstore.add(text);
			
			
			taggedTokens = tagger.tokenizeAndTag(text);
			for (TaggedToken t : taggedTokens){	
				if (t.tag.equalsIgnoreCase("#")){ t.token = t.token.substring(1); } // remove hashtag at front
				word = t.token.toUpperCase();
				mword = t.token.toLowerCase();
				
				 if (posLex.containsKey(word)) pos++;
				 if (negLex.containsKey(word)) neg++;
				 if (neuLex.containsKey(word)) neu++;
				 
				 if (mpqaLex.containsKey(mword)){
					 String polarity = mpqaLex.get(mword).get("priorpolarity");
					 if (polarity.equalsIgnoreCase("positive")) mpos++;
					 if (polarity.equalsIgnoreCase("negative")) mneg++;
					 if (polarity.equalsIgnoreCase("neutral")) mneu++;
				 }
			}
			
			/*
			 * Scoring:
			 * 	pos = pos > neg
			 *	neg = neg > pos
			 *	neu = no pos no neg or pos==neg
			 * */
			
			if (pos > neg) pred=0;
			else if (neg > pos) pred=1;
			else pred=2;
			prediction.add(pred);
			
			if (mpos > mneg) mpred=0;
			else if (mneg > mpos) mpred=1;
			else mpred=2;
			mprediction.add(mpred);
			
			
			//System.out.println(pos + " " + neg + " " + neu + " " + pred + " " + text);
		}
		 
		br.close();
	}
	
	private void coconutDropFromSky(String fn) throws IOException{
		BufferedReader br;
		String line;
		String curr[];
		ArrayList<ArrayList<String>> gold = new ArrayList<ArrayList<String>>(); 

		br = new BufferedReader(new FileReader(fn));
		while((line = br.readLine()) != null){
			curr = line.split(",");
			
			gold.add(new ArrayList<String>());
			
		}
		br.close();

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
					t.token = t.token.substring(1); // remove hashtag at front
					newHashtags.add(t.token);
				}
				if (t.tag.equalsIgnoreCase("A")){
					newHashtags.add(t.token);
				}
				if (t.tag.equalsIgnoreCase("N")){
					newHashtags.add(t.token);
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
				
				hm.put(newTokens.get(j), entry);
			}
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
		BomohPredictor bomoh = new BomohPredictor();
		bomoh.run("TEST\\tweets_test.txt");
		
		//bomoh.score("TEST\\label_test.txt", bomoh.prediction);
		bomoh.score("TEST\\label_test.txt", bomoh.mprediction);
		
		//bomoh.printPrediction();
		
//		for (int i=0; i<bomoh.prediction.size(); i++){
//			System.out.print(bomoh.prediction.get(i) + "    ");
//			System.out.println(bomoh.tweetstore.get(i));
//		}
	}

}
