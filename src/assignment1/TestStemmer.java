package assignment1;

public class TestStemmer {

	public static void main(String[] args) {
		System.out.println("Testing Porter Stemming now...");
		
		String stemWord = "Kicking";
		
		Stemmer stemmer = new Stemmer();
		
		stemmer.add(stemWord.toCharArray(), stemWord.length());
		stemmer.stem();
		
		System.out.println(stemWord + " has been stemmed to " + stemmer.toString()); 
	}

}
