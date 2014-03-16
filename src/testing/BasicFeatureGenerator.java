package testing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;

import org.json.JSONObject;

import model.Tweet;
import model.Tweets;

public class BasicFeatureGenerator extends Thread{
	
	private static final String DICTIONARY = "Dictionary/basic.txt";
	
	Vector<Tweet> pos; 
	Vector<Tweet> neg; 
	Vector<Tweet> neg2; 
	String filename;
	
	public BasicFeatureGenerator(Vector<Tweet> test, String filename) {
		Vector<String> dictionary = new Vector<String>();

		try{
			// Read dictionary
			BufferedReader reader = new BufferedReader(new FileReader(DICTIONARY));	
			String line;
			while((line = reader.readLine()) != null){
				dictionary.add(line);				
			}
			reader.close();
			
			System.out.println(dictionary.size());
			System.out.println(test.size());
			
			//build feature
			//for positive
			PrintWriter tw = new PrintWriter(new FileWriter(filename));
			for(int i=0; i<test.size(); i++){
				String text = test.get(i).text();
				String append = "+1 ";
				
				String[] tta = text.split(" ");
				for(int j=0; j<dictionary.size(); j++){
					int exist = 0;
					String dictTerm = dictionary.get(j);
					for(int k=0; k<tta.length; k++){
						if(text.contains(dictionary.get(j))){
							exist=1;
							break;
						}
					}
					append += " "+(j+1)+":"+exist;
				}
					
//				for(int j=0; j<dictionary.size(); j++){
//					int exist = 0;
//					if(text.contains(dictionary.get(j))) exist =1;
//					append += " "+(j+1)+":"+exist;
//				}
				tw.println(append);
			}
			
			tw.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}

	public BasicFeatureGenerator(Vector<Tweet> pos, Vector<Tweet> neg, Vector<Tweet> neg2, String filename) {
		this.pos = pos;
		this.neg = neg;
		this.neg2 = neg2;
		this.filename = filename;
	}
	
	public void run(){
		Vector<String> dictionary = new Vector<String>();

		try{
			// Read dictionary
			BufferedReader reader = new BufferedReader(new FileReader(DICTIONARY));	
			String line;
			while((line = reader.readLine()) != null){
				dictionary.add(line);				
			}
			reader.close();
			
			System.out.println(dictionary.size());
			System.out.println(pos.size());
			System.out.println(neg.size());
			System.out.println(neg2.size());
			
			//build feature
			//for positive
			PrintWriter tw = new PrintWriter(new FileWriter(filename));
			for(int i=0; i<pos.size(); i++){
				String text = pos.get(i).text();
				String append = "+1 ";
				
				String[] tta = text.split(" ");
				for(int j=0; j<dictionary.size(); j++){
					int exist = 0;
					String dictTerm = dictionary.get(j);
					for(int k=0; k<tta.length; k++){
						if(text.equalsIgnoreCase(dictTerm)){
							exist=1;
							break;
						}
					}
					append += " "+(j+1)+":"+exist;
				}
					
//				for(int j=0; j<dictionary.size(); j++){
//					int exist = 0;
//					if(text.contains(dictionary.get(j))) exist =1;
//					append += " "+(j+1)+":"+exist;
//				}
				tw.println(append);
			}
			
			//for negative
			for(int i=0; i<neg.size(); i++){
				String text = neg.get(i).text();
				String append = "-1 ";
				
				String[] tta = text.split(" ");
				for(int j=0; j<dictionary.size(); j++){
					int exist = 0;
					String dictTerm = dictionary.get(j);
					for(int k=0; k<tta.length; k++){
						if(text.equalsIgnoreCase(dictTerm)){
							exist=1;
							break;
						}
					}
					append += " "+(j+1)+":"+exist;
				}
				
//				for(int j=0; j<dictionary.size(); j++){
//					int exist = 0;
//					if(text.contains(dictionary.get(j))) exist =1;
//					append += " "+(j+1)+":"+exist;
//				}
				tw.println(append);
			}
			
			//for neutral
			for(int i=0; i<neg2.size(); i++){
				String text = neg2.get(i).text();
				String append = "-1 ";
				
				String[] tta = text.split(" ");
				for(int j=0; j<dictionary.size(); j++){
					int exist = 0;
					String dictTerm = dictionary.get(j);
					for(int k=0; k<tta.length; k++){
						if(text.equalsIgnoreCase(dictTerm)){
							exist=1;
							break;
						}
					}
					append += " "+(j+1)+":"+exist;
				}
				
//				for(int j=0; j<dictionary.size(); j++){
//					int exist = 0;
//					if(text.contains(dictionary.get(j))) exist =1;
//					append += " "+(j+1)+":"+exist;
//				}
				tw.println(append);
			}
			
			tw.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
