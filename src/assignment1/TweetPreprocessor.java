package assignment1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

public class TweetPreprocessor { 
	private static final String STOPWORD_LIST_FILE = "src/assignment1/stopwords.txt";
	private static final String TWITTER_DICTIONARY = "src/assignment1/emnlp_dict.txt";
	private static final boolean NORMALISATION_FLAG = true;
	private static final boolean STEMMING_FLAG = true;
	private static final boolean HASHTAG_SELECTION_FLAG = true;
	private static final int FEATURE_SIZE = 500;
	
	private ArrayList<String> stopWordList;
	private TreeMap<String, Integer> dictionary;
	public TreeMap<String, String> twitterDictionary;
	private List<Set<String>> processedTweets = new ArrayList<Set<String>>();
	private List<Integer[]> tweetVector;
	private List<String> selectedFeature = new ArrayList<String>();
	
	private void preProcess(String trainingFilePath) throws FileNotFoundException{
		if(trainingFilePath == null){
			throw new NullPointerException();
		}

		File trainingFile = new File(trainingFilePath);

		if(!trainingFile.exists() || !trainingFile.isFile()){
			throw new FileNotFoundException();
		}

		BufferedReader reader = new BufferedReader(new FileReader(trainingFile));
		this.dictionary = textExtraction(reader);
	}

	private TreeMap<String, Integer> textExtraction(BufferedReader reader){
		String inputLine = null;
		final int NEW_WORD_COUNT = 1;
		TreeMap<String, Integer> dictionary = new TreeMap<String, Integer>();
		
		try {
			inputLine = reader.readLine();

			while(inputLine != null){
				JSONObject tweet = new JSONObject(inputLine);
				String tweetText = tweet.getString("text");
				
				String[] tweetTokens = tweetText.toLowerCase().split("[^a-zA-Z0-9#@]+");
				
				Set<String> processedTweet = null;
				for(int i = 0; i < tweetTokens.length; i++){
					processedTweet = new HashSet<String>();
					String word = tweetTokens[i];

					if(NORMALISATION_FLAG){
						word = vocabularyNormalisation(word);
					}
					
					if(!shouldBeRemoved(word)){
						
						if(STEMMING_FLAG){
							word = stemming(word);
						}
						
						processedTweet.add(word);
						
						if(dictionary.get(word) == null){	//New Word
							dictionary.put(word, NEW_WORD_COUNT);
						}
						else{	//Old word
							dictionary.put(word, dictionary.get(word) + 1);
						}
					}
				}
				
				this.processedTweets.add(processedTweet);
				inputLine = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e){
			e.printStackTrace();
		}
		
		return dictionary;
	}

	private boolean shouldBeRemoved(String word){
		if(this.stopWordList.contains(word)){
			return true;
		}
		
		//Discard twitter mentioned and web link
		if(	word.startsWith("@") ||	
			word.startsWith("http")){
			return true;
		}
		return false;
	}
	
	private void generateStopWordsList(){
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(STOPWORD_LIST_FILE));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String stopWord = null;
		this.stopWordList = new ArrayList<String>();

		try {
			stopWord = reader.readLine();

			while(stopWord != null){
				this.stopWordList.add(stopWord);
				stopWord = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Porter Stemming is implemented here
	private String stemming(String word){
		if(word.startsWith("#")){
			return word;
		}
		
		Stemmer stemmer = new Stemmer();
		stemmer.add(word.toCharArray(), word.length());
		stemmer.stem();
		
		return stemmer.toString();
	}
	
	//TODO
	private String vocabularyNormalisation(String word){
		if(twitterDictionary.containsKey(word)){
			return twitterDictionary.get(word);
		}
		else{
			return word;
		}
	}
	
	private void generateTweetVocabulary(){
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(TWITTER_DICTIONARY));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String line = null;
		TreeMap<String, String> twitterDictionary = new TreeMap<String, String>();
		try {
			line = reader.readLine();
			
			while(line != null){
				String[] wordPair = line.split("\t");
				
				if(wordPair.length == 2){
					//System.out.println("Putting " + wordPair[0] + " and " + wordPair[1] + " into the dictionary.");
					twitterDictionary.put(wordPair[0], wordPair[1]);
				}
				
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.twitterDictionary = twitterDictionary;
	}
	
	public void printDictionary(){
		BufferedWriter writer = null;
		
		try {
			writer= new BufferedWriter(new FileWriter("dictionary.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			writer.write("Size of dictionary: " + this.dictionary.size() + "\n");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Set<String> tokenList = this.dictionary.keySet();
		Iterator<String> tokenIter = tokenList.iterator();
		
		while(tokenIter.hasNext()){
			try {
				String key = tokenIter.next();
				writer.write(key + ": " + dictionary.get(key));
				writer.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void featureSelection(){
		TreeMap <String, Integer> dictionary = new TreeMap<String, Integer>();
		dictionary.putAll(this.dictionary);
		Iterator<String> dictionaryIter = dictionary.keySet().iterator();
		
		while(dictionaryIter.hasNext()){
			String word = dictionaryIter.next();
			if(word.startsWith("@") && HASHTAG_SELECTION_FLAG){
				this.selectedFeature.add(word);
				dictionary.remove(word);
			}
		}
		
		//int remainingFeature 
	}
	
	private TreeMap<String,Integer> sortMapByValues(TreeMap<String, Integer> map){
		ValueComparator byValueComparator =  new ValueComparator(map);
        TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(byValueComparator);
        return sorted_map;
	}
	
	public static void main(String[] args){
		final String trainingFilePath = "TRAIN/NUS1.txt";
		TweetPreprocessor tweetPreprocessor = new TweetPreprocessor();

		tweetPreprocessor.generateStopWordsList();
		tweetPreprocessor.generateTweetVocabulary();

		try {
			tweetPreprocessor.preProcess(trainingFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		tweetPreprocessor.printDictionary();

		System.out.println("Successful run.");
	}
}

class ValueComparator implements Comparator<String> {

    Map<String, Integer> base;
    public ValueComparator(TreeMap<String, Integer> map) {
        this.base = map;
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