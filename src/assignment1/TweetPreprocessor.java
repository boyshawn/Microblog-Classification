package assignment1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class TweetPreprocessor {
	static Logger logger = Logger.getLogger(TweetPreprocessor.class); 
	private static final String STOPWORD_LIST_FILE = "src/assignment1/stopwords.txt";
	private static final String TWITTER_DICTIONARY = "src/assignment1/emnlp_dict.txt";
	
	private ArrayList<String> stopWordList;
	private HashMap<String, Integer> dictionary;
	private HashMap<String, String> twitterDictionary;
	
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

	private HashMap<String, Integer> textExtraction(BufferedReader reader){
		String inputLine = null;
		final int NEW_WORD_COUNT = 1;
		HashMap<String, Integer> dictionary = new HashMap<String, Integer>();
		
		try {
			inputLine = reader.readLine();

			while(inputLine != null){
				JSONObject tweet = new JSONObject(inputLine);
				String tweetText = tweet.getString("text");
				
				String[] tweetTokens = tweetText.toLowerCase().split("\\s+");
				
				for(int i = 0; i < tweetTokens.length; i++){
					if(!stopWordList.contains(tweetTokens[i])){
						String word = vocabularyNormalisation(tweetTokens[i]);
						word = stemming(word);
						
						if(dictionary.get(word) == null){	//New Word
							dictionary.put(word, NEW_WORD_COUNT);
						}
						else{	//Old word
							dictionary.put(word, dictionary.get(word) + 1);
						}
					}
				}

				inputLine = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e){
			e.printStackTrace();
		}
		
		return dictionary;
	}

	private void generateStopWordsList(){
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(STOPWORD_LIST_FILE));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String stopWord = null;
		this.stopWordList = new ArrayList<>();

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
		HashMap<String, String> twitterDictionary = new HashMap<>();
		try {
			line = reader.readLine();
			
			while(line != null){
				String[] wordPair = line.split("\t");
				
				if(wordPair.length != 2){
					twitterDictionary.put(wordPair[0], wordPair[1]);
				}
				
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.twitterDictionary = twitterDictionary;
	}
	
	private void printDictionary(){
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
	}

	public static void main(String[] args){
		final String trainingFilePath = "TRAIN/DBS1.txt";
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
