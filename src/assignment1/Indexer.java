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
import java.util.Vector;

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
    String inputFileName, textVectorFileName;
    private Map<String,Integer> frequencyMap, locationFrequencyMap;
    private Map<String,Map<String,Integer>> freqMapMap;
    private List<String> topTerms, topLocationTerms ;
    private Map<String, List<String>> topTermsMap;
	private String indexDirectory;
	private int numDocs, vectorSize;
	private String[] fieldList;
	private String field;
	private String index_name;
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    	// The path of the input file where the original tweets are stored in json format
		inputFileName = "dataset/nus/nus_pos.txt";
		
		// The path of the output file to save the result
//		outputFileName = "outputdata/nus_pos_textualWords.txt";
		
    }
    
    public Indexer(String ifn, String index_name, int vectorSize, String field) {
    	// The path of the input file where the original tweets are stored in json format
		inputFileName = ifn;
		// The path of the output file to save the result
		//outputFileName = ofn;
		this.index_name = index_name;
		indexDirectory = "index-directory/index-directory("+field+")_"+index_name;
		textVectorFileName = "Training_Vector/TRAINING_VECTOR("+field+")_"+index_name+".txt";
		this.vectorSize = vectorSize;
		this.field = field;
		
		fieldList = new String[]{"text", "geoposition"};
		frequencyMap = new HashMap<String,Integer>();
		
		topTerms = new ArrayList<String>();
    }
    
    public void run(){
		try{
			getIndexWriter(true);
			// Reader to read the input file
			BufferedReader reader = new BufferedReader(new FileReader(inputFileName));			
			// Wrimter to write the result file
			//BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
		    
			// This HashMap save the mapping relationship between a textual word and its index number
			HashMap<String, Integer> wordToIndex = new HashMap<String, Integer>();
			String line;
			while((line = reader.readLine()) != null){
				// Convert the input String line to JSONObject
				JSONObject tweet = new JSONObject(line);
				// Add tweet to indexer
				indexTweet(tweet);
			
			}
			reader.close();
			//writer.close();
			closeIndexWriter();
			
			//print top terms
			String[] topTermArray = computeTopTermQuery();
			//String[] topTermArray = computeTopFieldQuery();
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
	        String fieldValue = (tweet.isNull(field)?"": tweet.getString(field));
	        doc.add(new Field(field, removeStopWords((String)fieldValue), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
	        indexWriter.addDocument(doc);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}
    
	public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null&&create) {
                   	
    		final File docDir = new File(indexDirectory);

            if (docDir.exists()) {
            	File tempFile = new File("index-directory");
        		deleteFolder(tempFile);
            }

        	FSDirectory idx = FSDirectory.open(new File(indexDirectory));
        	
    		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(
    				Version.LUCENE_CURRENT, new StandardAnalyzer(Version.LUCENE_CURRENT, getStopwords()));

    		 indexWriter = new IndexWriter(idx, indexWriterConfig);
            
        }
        return indexWriter;
	}    
	
	public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}
   
    private Set<?> getStopwords() {
    	if (STOP_WORDS != null) return STOP_WORDS; 
    	STOP_WORDS = new HashSet<String>();
    	try {	 
    		BufferedReader br = new BufferedReader(new FileReader("stopwords.txt"));
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				STOP_WORDS.add(sCurrentLine);
				//System.out.println(sCurrentLine);
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
        System.out.println(">1>> top terms: " + termBuf.toString());
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
    	if(string.equalsIgnoreCase("string")) return string;
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
    	//return topTermsMap.get("text");
    }
    
    public Map<String, Integer> getFrequencyMap(){
    	return frequencyMap;
    	//return freqMapMap.get("text");
    }
    
    public int getNumDocs(){
    	return numDocs;
    }
    
    public Vector<String> generateTermVectors(){
    	int vSize = topTerms.size()>vectorSize ? vectorSize : topTerms.size();
    	return generateVectors("+1", textVectorFileName, topTerms.toArray(new String[topTerms.size()]), vSize);
    }
    
    public Vector<String> generateTermVectorsWithTopTerms(List<String> newTopTerms, String tt_type){
    	int vSize = newTopTerms.size()>vectorSize ? vectorSize : newTopTerms.size();
    	String newName = "Training_Vector/TRAINING_VECTOR_"+tt_type+"("+field+")_"+index_name+".txt";
    	
    	return generateVectors("+1", newName, newTopTerms.toArray(new String[newTopTerms.size()]), vSize);
    }
    
    public Vector<String> generateVectors(String type, String vectorFileName, String[] termVector, int vectorSize){
    	Vector<String> tweetVectors = new Vector<String>();
    	FSDirectory idx;
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(vectorFileName));
			idx = FSDirectory.open(new File(indexDirectory));
			searcher = new IndexSearcher(idx); 	
	        IndexReader reader = searcher.getIndexReader();
	        System.out.println("TOTAL NUM DOCS: "+reader.numDocs());
	        for(int i=0; i<reader.numDocs(); i++){     	
	        	StringBuilder vectorBuf = new StringBuilder();
	        	TermFreqVector tfv = reader.getTermFreqVector(i, field);
	        	
	        	
	        	String[] textArray = (tfv==null)?new String[0]:tfv.getTerms();
	        	//TEST
	        	//System.out.println(textArray.length+ " " +Arrays.toString(textArray));
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
	        	tweetVectors.add(vectorBuf.toString());
	        	writer.write(type+" "+vectorBuf.toString()+"\n");
	        }
	        writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tweetVectors;
    }
    
	public Vector<String> generateVectors(String type, String vectorFileName,
			Map<String, Double> negTermMap, int negativeTermSize) {
		String[] termVector = negTermMap.keySet().toArray(new String[negTermMap.keySet().size()]);
		return generateVectors(type, vectorFileName, termVector, negativeTermSize);
	}
}
