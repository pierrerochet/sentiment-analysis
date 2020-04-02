package analysis;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.ejml.simple.SimpleMatrix;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;


/**
 * 
 * L'analyseur de sentiment qui
 * repose sur le module StanfordCoreNLP
 * 
 * @author rochet
 * 
 */
public class SentimentAnalyzer {
	
	/**
	 * Les propriétés de l'analyseur Stanford.
	 */
	protected Properties props;
	
	/**
	 * L'analyseur Stanford.
	 */
	protected StanfordCoreNLP pipeline;
	
	/**
	 * Constructeur qui instancie l'analyseur Stanford avec les propriétés.
	 */
	public SentimentAnalyzer() {
		this.props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
		this.pipeline = new StanfordCoreNLP(props);
	}
	
	/**
	 * Analyse les phrases contenues dans un fichier.
	 * 
	 * @param pathFile
	 * 				Le chemin du fichier à analyser.
	 * @return
	 * 				Renvoie un CustomDatasetSent contenant les phrases analysées
	 * 				et les valeurs associées à chaque sentiment.
	 * @throws IOException
	 */
	public CustomDatasetSent analyzeFromFile(String pathFile) throws IOException {
		
		List<String> lines = Files.readAllLines(Paths.get(pathFile), StandardCharsets.UTF_8);
        CustomDatasetSent data = this.analyzeFromList(lines);
        return data;
	}
	

	/**
	 * <p>Analyse les phrases contenues dans une liste contenant des chaînes de caractèrtes.</p>
	 * 
	 * @param sentencesList
	 * 				La liste contenant les phrases à analyser.
	 * @return
	 * 				Renvoie un CustomDatasetSent contenant les phrases analysées
	 * 				et les valeurs associées à chaque sentiment.
	 */
	public CustomDatasetSent analyzeFromList(List<String> sentencesList) {
		
        CustomDatasetSent data = new CustomDatasetSent();
        
        for (String line: sentencesList) {
        
	        Annotation annotation = pipeline.process(line);
	      
	        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
				
				Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
				SimpleMatrix sm = RNNCoreAnnotations.getPredictions(tree);
				
				HashMap<String, Double> sentValues = new HashMap<String, Double>();
				sentValues.put("VN", sm.get(0));
				sentValues.put("N", sm.get(1));
				sentValues.put("P", sm.get(2));
				sentValues.put("VP", sm.get(3));
				
		
				CustomSentenceSent customSentence = new CustomSentenceSent(sentence.toString(), sentValues);
				data.add(customSentence);
				
		   }
		}
        
        return data;
	}
      

	/**
	 * <p>Lance une classification de type 3 sur tous les fichiers d'un répertoire.</p>
	 * <p>Classification de type 3 :</p>
	 * <p>Pour chaque fichier présent dans le répertoire un nouveau fichier est crée. 
	 * 	  Il contient la liste de toutes les phrases du fichier classées par sentiment.
	 * 	  Pour chaque sentiment les phrases sont classées par ordre décroissant de la valeur du sentiment.</p>
	 * 
	 * @see #classifyFromFolderType2
	 * @see #classifyFromFolderType3
	 * 
	 * @param inputCorpusPath
	 * 		Le chemin du repertoire contenant les fichiers à classer.
	 * @param outputFolderPath
	 * 		Le chemin du repertoire qui contiendra les fichiers classés.
	 * 		Si le repertoire n'existe pas alors il sera créé.
	 * @throws IOException
	 */
	public void classifyFromFolderType3(String inputCorpusPath, String outputFolderPath) throws IOException {
		
		System.out.println("Start classification : Type 3");
		
		File corpus = new File(inputCorpusPath);
		File[] listFiles = corpus.listFiles();
		
		File outputFolder = new File(outputFolderPath);
		if(!outputFolder.exists()) {
			outputFolder.mkdir();
		}
		
		for (File File : listFiles) {
			
			String pathFile = File.getPath();
			String fileName = File.getName();
			
			System.out.println("%% Process : " + fileName);
			
			ArrayList<String> resultArray = new ArrayList<String>();
			
			Path outputFile = Paths.get(outputFolderPath + fileName);
			
			CustomDatasetSent dataset = this.analyzeFromFile(pathFile);
			
			HashMap<String, ArrayList<CustomSentenceSent>> clusterMap = dataset.getClustersSorted();
			
			for (Entry<String,ArrayList<CustomSentenceSent>> cluster : clusterMap.entrySet()) {
				String clusterName = cluster.getKey();
				resultArray.add("### " + clusterName);
				for (CustomSentenceSent sent : cluster.getValue()) {
					resultArray.add(String.format("%s\t%.3f", sent.getText(), sent.getSentValues().get(clusterName) ) );
				}
				resultArray.add("\n");
			}

			Files.write(outputFile, resultArray, Charset.forName("UTF-8"));
		}
		
		System.out.println("Done.");
	}
	
	/**
	 * <p>Lance une classification de type 2 sur tous les fichiers d'un répertoire.</p>
	 * <p>Classification de type 2 :</p>
	 * <p>Toutes les phrases de tous les fichiers sont classées par sentiment 
	 * 	  dans des fichiers nommés respectivement :</p>
	 * 			<ul>
	 * 			<li>VP.txt</li>
	 * 			<li>P.txt</li>
	 * 			<li>N.txt</li>
	 * 			<li>VN.txt</li>
	 * 			</ul>
	 * <p>Dans les fichiers de sorties les phrases sont classées 
	 *    par ordre décroissement de la valeur du sentiment.<p>
	 * 
	 * @see #classifyFromFolderType1
	 * @see #classifyFromFolderType3
	 * 
	 * @param inputCorpusPath
	 * 		Le chemin du repertoire contenant les fichiers à classer.
	 * @param outputFolderPath
	 * 		Le chemin du repertoire qui contiendra les fichiers classés.
	 * 		Si le repertoire n'existe pas alors il sera créé.
	 * @throws IOException
	 */
	public void classifyFromFolderType2(String inputCorpusPath, String outputFolderPath) throws IOException {
		
		System.out.println("Start classification : Type 2");
		
		File corpus = new File(inputCorpusPath);
		File[] listFiles = corpus.listFiles();
		
		CustomDatasetSent datasetTotal = new CustomDatasetSent();
		
		for (File File : listFiles) {
			System.out.println("%% Process : " + File.getName());
			String pathFile = File.getPath();
			CustomDatasetSent dataset = this.analyzeFromFile(pathFile);
			datasetTotal.concat(dataset);
		}
		
		HashMap<String, ArrayList<CustomSentenceSent>> clusterMap = datasetTotal.getClustersSorted();
		
		File outputFolder = new File(outputFolderPath);
		if(!outputFolder.exists()) {
			outputFolder.mkdir();
		}
		
		for (Entry<String,ArrayList<CustomSentenceSent>> cluster : clusterMap.entrySet()) {
			ArrayList<String> result = new ArrayList<String>();
			String clusterName = cluster.getKey();
			for (CustomSentenceSent sent : cluster.getValue()) {
				result.add(String.format("%s\t%.3f", sent.getText(), sent.getSentValues().get(clusterName) ) );
			}
			Path outputPath = Paths.get(outputFolderPath + clusterName + ".txt");
			Files.write(outputPath, result, Charset.forName("UTF-8"));
		}
		
		System.out.println("Done.");
		
	}
	
	/***
	 * <p>Lance une classification de type 1 sur tous les fichiers d'un répertoire.
	 *    Classification de type 1 :</p>
	 * <p>Les fichiers sont classés par sentiment
	 * 	  dans des dossier nommés respectivement :</p>
	 * 			<ul>
	 * 			<li>VP</li>
	 * 			<li>P</li>
	 * 			<li>N</li>
	 * 			<li>VP</li>		
	 * 			</ul>
	 * 
	 * @see #classifyFromFolderType2
	 * @see #classifyFromFolderType3
	 * 
	 * @param inputCorpusPath
	 * 		Le chemin du repertoire contenant les fichiers à classer.
	 * @param outputFolderPath
	 * 		Le chemin du repertoire qui contiendra les fichiers classés.
	 * 		Si le repertoire n'existe pas alors il sera créé.
	 * @throws IOException
	 */
	public void classifyFromFolderType1(String inputCorpusPath, String outputFolderPath) throws IOException {
		
		System.out.println("Start classification : Type 1");
		
		File corpus = new File(inputCorpusPath);
		File[] listFiles = corpus.listFiles();
		
		File outputFolder = new File(outputFolderPath);
		if(!outputFolder.exists()) {
			outputFolder.mkdir();
		}
		
		ArrayList<String> foldersClass = new ArrayList<String>();
		foldersClass.add("VP");
		foldersClass.add("P");
		foldersClass.add("N");
		foldersClass.add("VN");

		for (String pathClass: foldersClass) {
			File classFile = new File(outputFolderPath + pathClass);
			if(!classFile.exists()) {
				classFile.mkdir();
			}
		}
		
		for (File File : listFiles) {
					
			String pathFile = File.getPath();
			String fileName = File.getName();
			
			System.out.println("%% Process : " + fileName);
			
			List<String> lines = Files.readAllLines(Paths.get(pathFile), StandardCharsets.UTF_8);
			CustomDatasetSent dataset = this.analyzeFromList(lines);
			String label = dataset.getLabel();
			
			switch(label) {
			case "VP": Files.write(Paths.get(outputFolder + "/VP/" + fileName), lines, Charset.forName("UTF-8")); break;
			case "P": Files.write(Paths.get(outputFolder + "/P/" + fileName), lines, Charset.forName("UTF-8")); break;
			case "N": Files.write(Paths.get(outputFolder + "/N/" + fileName), lines, Charset.forName("UTF-8")); break;
			case "VN": Files.write(Paths.get(outputFolder + "/VN/" + fileName), lines, Charset.forName("UTF-8")); break;
			}
			
		}
		
		System.out.println("Done.");
		
	}
	

}
