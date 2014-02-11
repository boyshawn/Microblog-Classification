/*
 * Indexer.java
 *
 * Created on 6 March 2006, 13:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package assignment1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.comparators.ReverseComparator;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.json.JSONObject;


public class Indexer {

    private IndexWriter indexWriter = null;
    private float topTermCutoff;
    public IndexSearcher searcher = null;
    private Set<String> STOP_WORDS = null;
    String inputFileName, outputFileName, textVectorFileName;
    private Map<String,Integer> frequencyMap;
    private List<String> topTerms ;
	private String indexDirectory;
	private int numDocs, vectorSize;
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    	// The path of the input file where the original tweets are stored in json format
		inputFileName = "dataset/nus/nus_pos.txt";
		
		// The path of the output file to save the result
		outputFileName = "outputdata/nus_pos_textualWords.txt";
		
    }
    
    public Indexer(String ifn, String ofn, String index_name, int vectorSize) {
    	// The path of the input file where the original tweets are stored in json format
		inputFileName = ifn;
		// The path of the output file to save the result
		outputFileName = ofn;
		indexDirectory = "index-directory_"+index_name;
		textVectorFileName = "Training_Vector/TRAINING_VECTOR_"+index_name+".txt";
		this.vectorSize = vectorSize;
    }
    
    public void run(){
		try{
			getIndexWriter(true);
			// Reader to read the input file
			BufferedReader reader = new BufferedReader(new FileReader(inputFileName));			
			// Wrimter to write the result file
			BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
		    
			// This HashMap save the mapping relationship between a textual word and its index number
			HashMap<String, Integer> wordToIndex = new HashMap<String, Integer>();
			
			String line;
			while((line = reader.readLine()) != null){
				
				// Convert the input String line to JSONObject
				JSONObject tweet = new JSONObject(line);
				
				// Add tweet to indexer
				indexTweet(tweet);
				
				//System.out.println(tweet.get("text"));
				
				// Get the "text" field, which saves the textual content of this tweet
				// Other fields could be fetched similarly
				String text = tweet.getString("text");
				
				// Split this sentence into words
				// Here, the sentence is split only with blank space
				// You may consider other factors, e.g., punctuation, word root, URL, mentions(@someone)...
				String[] words = text.toLowerCase().split("\\s+");
				
				for(int i=0; i<words.length; i++){
					
					// Map a textual word to an integer index number
					if(!wordToIndex.containsKey(words[i])){
						wordToIndex.put(words[i], wordToIndex.size());
					}
					
					// Output the mapped words to file
					writer.write(wordToIndex.get(words[i])+" ");
				}
				
				writer.write("\n");
			}
			reader.close();
			writer.close();
			closeIndexWriter();
			
			//print top terms
			String[] topTermArray = computeTopTermQuery();
			FSDirectory idx = FSDirectory.open(new File(indexDirectory));
			// have to set similarity on both IndexWriter and IndexSearcher
			searcher = new IndexSearcher(idx);
			IndexReader reader1 = searcher.getIndexReader();
			for (int i=0; i<reader1.maxDoc(); i++) {
			    if (reader1.isDeleted(i))
			        continue;
			    Document doc = reader1.document(i);
			    //System.out.println(doc.get("text"));
			    // do something with docId here...
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
    }
    
    private void indexTweet(JSONObject tweet){
    	try{
	    	indexWriter = getIndexWriter(false);
	        Document doc = new Document();
	        doc.add(new Field("text", removeStopWords((String)tweet.get("text")), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
	        indexWriter.addDocument(doc);
	        System.out.println("s "+doc.get("text"));
    	}catch(Exception e){
    	}
	}
    
	public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null&&create) {
            
    		final File docDir = new File(indexDirectory);

            if (docDir.exists()) {
                System.out.println("Index at '" + docDir.getAbsolutePath() + "' is already built (delete first!)");
                System.exit(1);
            }
        	
        	FSDirectory idx = FSDirectory.open(new File(indexDirectory));
        	
    		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(
    				Version.LUCENE_CURRENT, new StandardAnalyzer(Version.LUCENE_CURRENT, getStopwords()));

    		 indexWriter = new IndexWriter(idx, indexWriterConfig);
            
        }
        return indexWriter;
   }    
   
    private Set<?> getStopwords() {
    	if (STOP_WORDS != null) return STOP_WORDS; 
    	STOP_WORDS = new HashSet<String>();
    	try {	 
    		BufferedReader br = new BufferedReader(new FileReader("stopwords.txt"));
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				STOP_WORDS.add(sCurrentLine);
				System.out.println(sCurrentLine);
			}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}     	
		return null;
	}

	public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
   }
        
    public void rebuildIndexes() throws IOException {
          getIndexWriter(true);
          closeIndexWriter();
     }    
    
    private String[] computeTopTermQuery() throws Exception {
    	FSDirectory idx = FSDirectory.open(new File(indexDirectory));
		// have to set similarity on both IndexWriter and IndexSearcher
		searcher = new IndexSearcher(idx);
    	
        frequencyMap = new HashMap<String,Integer>();
        List<String> termlist = new ArrayList<String>();
        IndexReader reader = searcher.getIndexReader();
//        IndexReader reader = IndexReader.open(ramdir);
        TermEnum terms = reader.terms();
        while (terms.next()) {
          Term term = terms.term();
          String termText = term.text();
          int frequency = reader.docFreq(term);
          frequencyMap.put(termText, frequency);
          termlist.add(termText);
        }
        numDocs = reader.numDocs();
        reader.close();
        // sort the term map by frequency descending
        Collections.sort(termlist, new ReverseComparator<String>(
          new ByValueComparator(frequencyMap)));
        // retrieve the top terms based on topTermCutoff
        topTerms = new ArrayList<String>();
        float topFreq = -1.0F;
        for (String term : termlist) {
          if (topFreq < 0.0F) {
            // first term, capture the value
            topFreq = (float) frequencyMap.get(term);
            topTerms.add(term);
          } else {
            // not the first term, compute the ratio and discard if below
            // topTermCutoff score
            float ratio = (float) ((float) frequencyMap.get(term) / topFreq);
            if (ratio >= topTermCutoff) {
              topTerms.add(term);
            } else {
              break;
            }
          }
        }
        StringBuilder termBuf = new StringBuilder();
        for (String topTerm : topTerms) {
          termBuf.append(topTerm).
            append("(").
            append(frequencyMap.get(topTerm)).
            append(");");
        }
        System.out.println(">>> top terms: " + termBuf.toString());
        return topTerms.toArray(new String[topTerms.size()]);
        //return null;
      }
  
    public void setTopTermCutoff(float topTermCutoff) {
        if (topTermCutoff < 0.0F || topTermCutoff > 1.0F) {
          throw new IllegalArgumentException(
            "Invalid value: 0.0F <= topTermCutoff <= 1.0F");
        }
        this.topTermCutoff = topTermCutoff;
      }
    
    public String removeStopWords(String string) throws IOException 
    {
    	string = string.toLowerCase();
        //StandardAnalyzer ana = new StandardAnalyzer(Version.LUCENE_CURRENT);
        TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_CURRENT,new StringReader(string));
        StringBuilder sb = new StringBuilder();
        tokenStream = new StopFilter(Version.LUCENE_CURRENT, tokenStream, getStopwords());
        tokenStream = new PorterStemFilter(tokenStream);
        CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
        while (tokenStream.incrementToken()) 
        {
            if (sb.length() > 0) 
            {
                sb.append(" ");
            }
            sb.append(token.toString());
        }
        return sb.toString();
    }
    
    public List<String> getTopTerms(){
    	return topTerms;
    }
    
    public Map<String, Integer> getFrequencyMap(){
    	return frequencyMap;
    }
    
    public int getNumDocs(){
    	return numDocs;
    }
    
    public void generateTermVectors(){
    	generateVectors("+1", textVectorFileName, topTerms.toArray(new String[topTerms.size()]), vectorSize);
	
//    	FSDirectory idx;
//		try {
//			BufferedWriter writer = new BufferedWriter(new FileWriter(textVectorFileName));
//			idx = FSDirectory.open(new File(indexDirectory));
//			searcher = new IndexSearcher(idx); 	
//	        IndexReader reader = searcher.getIndexReader();
//	        for(int i=0; i<reader.numDocs(); i++){
//	        	Document doc = reader.document(i);
//	        	String docId = doc.get("docId");
//	        	
//	        	StringBuilder vectorBuf = new StringBuilder();
//	        	vectorBuf.append("+1 ");
//	        	TermFreqVector tfv = reader.getTermFreqVector(i, "text");
//	        	String[] textArray = tfv.getTerms();
//	        	System.out.println(textArray.length+ " " +Arrays.toString(textArray));
//	        	//indicate how many terms in vector
//	        	for(int j=0; j<vectorSize; j++){
//	        		vectorBuf.append((j+1) + ":");
//	        		boolean exist = false;
//	        		for(String text: textArray){
//	        			if(topTerms.get(j).equalsIgnoreCase(text)){
//	        				exist = true;
//	        				break;
//	        			}
//	        		}
//	        		if(exist) vectorBuf.append("1 ");
//	        		else vectorBuf.append("0 ");
//	        	}
//	        	writer.write(vectorBuf.toString()+"\n");
//	        }
//	        writer.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
    }
    
    public void generateVectors(String string, String vectorFileName, String[] termVector, int vectorSize){
    	FSDirectory idx;
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(vectorFileName));
			idx = FSDirectory.open(new File(indexDirectory));
			searcher = new IndexSearcher(idx); 	
	        IndexReader reader = searcher.getIndexReader();
	        for(int i=0; i<reader.numDocs(); i++){
	        	Document doc = reader.document(i);
	        	String docId = doc.get("docId");
	        	
	        	StringBuilder vectorBuf = new StringBuilder();
	        	vectorBuf.append(string+" ");
	        	TermFreqVector tfv = reader.getTermFreqVector(i, "text");
	        	String[] textArray = tfv.getTerms();
	        	System.out.println(textArray.length+ " " +Arrays.toString(textArray));
	        	//indicate how many terms in vector
	        	for(int j=0; j<vectorSize; j++){
	        		vectorBuf.append((j+1) + ":");
	        		boolean exist = false;
	        		for(String text: textArray){
	        			if(termVector[j].equalsIgnoreCase(text)){
	        				exist = true;
	        				break;
	        			}
	        		}
	        		if(exist) vectorBuf.append("1 ");
	        		else vectorBuf.append("0 ");
	        	}
	        	writer.write(vectorBuf.toString()+"\n");
	        }
	        writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	public void generateVectors(String type, String vectorFileName,
			Map<String, Double> negTermMap, int negativeTermSize) {
		String[] termVector = negTermMap.keySet().toArray(new String[negTermMap.keySet().size()]);
		generateVectors(type, vectorFileName, termVector, negativeTermSize);
	}
}
