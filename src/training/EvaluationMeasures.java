package training;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Vector;

import org.json.JSONObject;

public class EvaluationMeasures {
	Vector<Integer> results;
	public EvaluationMeasures(Vector<Integer> results){
		this.results = results;
	}
	
	public void run(){
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("dataset/TEST/TEST.txt"));
			HashMap<String, Integer> wordToIndex = new HashMap<String, Integer>();
		
			String line;
			while((line = reader.readLine()) != null){
				results.add(Integer.parseInt(line));
			}
			reader.close();
			
			
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}			
	}
}
