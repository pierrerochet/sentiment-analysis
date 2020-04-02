package analysis;


import java.io.IOException;

public class App {


	public static void main(String[] args) throws IOException {
		
		SentimentAnalyzer Analyzer = new SentimentAnalyzer();
		
		String inputCorpusPath = "/home/rochet/git/corpus-test";
		
		// ATTENTION : Bien mettre les "/" avant et après les chemins
		
		String outputFolderPathT1 = "/home/rochet/git/output-test-classif-1/";
		Analyzer.classifyFromFolderType1(inputCorpusPath, outputFolderPathT1);
		
//		String outputFolderPathT2 = "/home/rochet/git/output-test-classif-2/";
//		Analyzer.classifyFromFolderType2(inputCorpusPath, outputFolderPathT2);
		
//		String outputFolderPathT3 = "/home/rochet/git/output-test-classif-3/";
//		Analyzer.classifyFromFolderType3(inputCorpusPath, outputFolderPathT3);
	
	}

}
