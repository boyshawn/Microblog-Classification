package testing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;

public class CombineVector extends Thread{

	String optputName; 
	String name1; 
	String name2;
	public CombineVector(String optputName, String name1, String name2) {
		this.optputName = optputName;
		this.name1 = name1;
		this.name2 = name2;
	}

	public void run(){
		try{
			PrintWriter tw = new PrintWriter(new FileWriter(optputName));
			Vector<BufferedReader> brV = new Vector<BufferedReader>();
			
			BufferedReader readertemp = new BufferedReader(new FileReader(name1));
			String line = readertemp.readLine();
			readertemp.close();
			String[] temparr=line.split(" ");
			String last = temparr[temparr.length-1];
			int val = Integer.parseInt(last.split(":")[0]) + 1;
			
			BufferedReader reader1 = new BufferedReader(new FileReader(name1));	
			BufferedReader reader2 = new BufferedReader(new FileReader(name2));	
			String line1, line2;
			while(((line1 = reader1.readLine()) != null) && ((line2=reader2.readLine())!=null)){
				String[] newfeature = line2.split(" ");
				int tempval = val;
				for(int i=0; i<newfeature.length; i++){
					line1 += " "+ tempval++ +":"+ newfeature[i];
				}
				tw.println(line1);
			}
			reader1.close();
			reader2.close();
			
			//build feature
			//for positive
			tw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
