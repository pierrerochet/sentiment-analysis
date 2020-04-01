package analysis;


import java.io.IOException;

public class App {


	public static void main(String[] args) throws IOException {
		
		SentimentAnalyzer Analyzer = new SentimentAnalyzer();
		
		String inputCorpusPath = "/home/rochet/corpora/dossier_test/obama-texts/";
		
//		String outputFolderPathT1 = "/home/rochet/corpora/obama-texts-classif-1/";
//		Analyzer.classifyFromFolderType1(inputCorpusPath, outputFolderPathT1);
//		
		String outputFolderPathT2 = "/home/rochet/corpora/obama-texts-classif-2/";
		Analyzer.classifyFromFolderType2(inputCorpusPath, outputFolderPathT2);
		
//		String outputFolderPathT3 = "/home/rochet/corpora/obama-texts-classif-3/";
//		Analyzer.classifyFromFolderType3(inputCorpusPath, outputFolderPathT3);
	
	}

}
