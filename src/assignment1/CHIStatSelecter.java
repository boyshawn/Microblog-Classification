package assignment1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import assignment1.Indexer;

public class CHIStatSelecter {
	Indexer indexNUS1, indexNUS2, indexDBS1, indexDBS2, indexStarhub;
	int catSize;
	Map<String, Integer> NUS1_df, NUS2_df, DBS1_df, DBS2_df, Starhub_df;
	List<String> NUS1_tt, NUS2_tt, DBS1_tt, DBS2_tt, Starhub_tt;
	List<String> NUS1_CHItt, NUS2_CHItt, DBS1_CHItt, DBS2_CHItt, Starhub_CHItt;
	Map<String, Double> NUS1_CHI, NUS2_CHI, DBS1_CHI, DBS2_CHI, Starhub_CHI;
	int NUS1_docNum, NUS2_docNum, DBS1_docNum, DBS2_docNum, Starhub_docNum, N; 
	public CHIStatSelecter(){
	}
	
	public void run(){
		// DBS1 = 1000
		// DBS2 = 200
		// NUS1 = 1000
		// NUS2 = 300
		// Starhub = 1000
		
		catSize = 5;
		indexNUS1 = new Indexer("dataset/NUS1.txt", "outputdata/NUS1_textualWords.txt", "NUS1");
		indexNUS2 = new Indexer("dataset/NUS2.txt", "outputdata/NUS2_textualWords.txt", "NUS2");
		indexDBS1 = new Indexer("dataset/DBS1.txt", "outputdata/DBS1_textualWords.txt", "DBS1");
		indexDBS2 = new Indexer("dataset/DBS2.txt", "outputdata/DBS2_textualWords.txt", "DBS2");
		indexStarhub = new Indexer("dataset/starhub.txt", "outputdata/starhub_textualWords.txt", "Starhub");
		
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
		int A,B,C,D;
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
			
			//check for correct doc sum
			int NTemp = A+B+C+D;
//			if(A>38)
//			System.out.println(((NUS1_docNum + NUS2_docNum + DBS1_docNum + DBS2_docNum + Starhub_docNum)== A+B+C+D )+ 
//			" total:" + NTemp + " N:" + N + " W:" + W + " A:" + A + " B:" + B + " C:" + C + " D:" + D + " CHI:" + chi);
		}
		sorted_map.putAll(CHI_MAP);
		return sorted_map;
	}

	private int getNumDocNotInC(List<Integer> residueDocNum) {
		int count = 0;
		for(Integer num: residueDocNum){
			count += num;
		}
		return count;
	}

	private int computD(String W, List<Map<String, Integer>> list, int B) {
		int WCount = 0;
		for(int i=0; i<list.size(); i++){
			System.out.println("list size: " +list.size());
			Map<String, Integer> tempCAT = list.get(i);
			WCount += tempCAT.get(W);
		}
		return WCount - B;
	}

	private int computB(String W, List<Map<String, Integer>> list) {
		int WCount = 0;
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
