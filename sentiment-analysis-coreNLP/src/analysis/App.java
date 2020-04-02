package analysis;


import java.io.IOException;

public class App {


	public static void main(String[] args) throws IOException {
		
		SentimentAnalyzer Analyzer = new SentimentAnalyzer();
		
		
		String inputCorpusPath = "../corpus-test";
		
		
		// ATTENTION : Bien mettre les "/" avant et après les chemins
		// NOTE : Les chemins sont déjà configurés pour le test ( sur un dossier de 5 fichiers des discours de Barack Obama ) 
		
		String outputFolderPathT1 = "../output-test-classif-1/";
		Analyzer.classifyFromFolderType1(inputCorpusPath, outputFolderPathT1);
		
		String outputFolderPathT2 = "../output-test-classif-2/";
		Analyzer.classifyFromFolderType2(inputCorpusPath, outputFolderPathT2);
		
		String outputFolderPathT3 = "../output-test-classif-3/";
		Analyzer.classifyFromFolderType3(inputCorpusPath, outputFolderPathT3);
	  
	}

}
