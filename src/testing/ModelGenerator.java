package testing;

import java.io.IOException;

public class ModelGenerator extends Thread{
	String inputFilePath;
	public ModelGenerator(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}

	public void run(){
		try {
			System.out.println("generating model: "+ inputFilePath);
			svm_train t = new svm_train();
			String arr[] = {inputFilePath};
			t.run(arr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		return t.model_file_name;
	}
}
