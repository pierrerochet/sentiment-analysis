package analysis;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;


/**
 * Un Dataset personalisé pour manipuler des CustomSentenceSent.
 * Il facilite notamment les opérations de trie des phrases d'un corpus.
 * 
 * @author rochet
 *
 */
public class CustomDatasetSent {
	
	/**
	 * La liste des CustomSentencesSent.
	 */
	protected ArrayList<CustomSentenceSent> customSentences;
	
	/**
	 * Constructeur qui crée un CustomDatasetSent avec une liste de CustomSentenceSent.
	 * @param data
	 * 			La liste des CustomSentenceSent à utiliser pour remplir le dataset
	 */
	public CustomDatasetSent(ArrayList<CustomSentenceSent> data) {
		this.customSentences = data;
	}
	
	/**
	 * Constructeur qui crée un CustomDatasetSent vide.
	 */
	public CustomDatasetSent() {
		this.customSentences = new ArrayList<CustomSentenceSent>();
	}
	
	
	/**
	 * Ajoute le contenu d'un CustomDatasetSent dans ce CustomDatasetSent.
	 * 
	 * @param dataset
	 * 			Le CustomDatasetSent à ajouter
	 */
	public void concat(CustomDatasetSent dataset) {
		this.customSentences.addAll(dataset.getCustomSentences());
	}
	
	/**
	 * Affiche une représentation du CustomDatasetSent
	 */
	@Override
	public String toString() {
		return customSentences.toString();
	}
	
	/**
	 * Ajoute une CustomSentenceSent au CustomDatasetSent.
	 * 
	 * @param customSent
	 * 			La CustomSentenceSent à ajouter
	 */
	public void add(CustomSentenceSent customSent) {
		this.customSentences.add(customSent);
	}
	
	/**
	 * Renvoie la liste des CustomSentenceSent contenue dans le CustomDatasetSent.
	 * 
	 * @return
	 * 		La liste des CustomSentenceSent contenue dans le CustomDatasetSent.
	 */
	public ArrayList<CustomSentenceSent> getCustomSentences() {
		return customSentences;
	}
	
	/**
	 * Calcule les moyennes des sentiments de l'ensemble 
	 * des CustomSentenceSent contenues dans le CustomSentenceSent.
	 * 
	 * @return
	 * 		Les sentiments et les valeurs moyennes associées
	 */
	public HashMap<String, Double> getMeanSent() {
		
		double VP = 0;
		double P = 0;
		double N = 0;
		double VN = 0;
		for (CustomSentenceSent sent: this.customSentences) {
			HashMap<String, Double> sentValues = sent.getSentValues();
			VP = VP + sentValues.get("VP");
			P = P + sentValues.get("P");
			N = N + sentValues.get("N");
			VN = VN + sentValues.get("VN");
		}
		double total = this.customSentences.size();
		VP = VP / total;
		P = P / total;
		N = N / total;
		VN = VN / total;
		HashMap<String, Double> meanValues = new HashMap<String, Double>();
		meanValues.put("VP", VP);
		meanValues.put("P", P);
		meanValues.put("N", N);
		meanValues.put("VN", VN);
		return meanValues;
		
	}
	
	/**
	 * Calcule le sentiment mojoritaire de l'ensemble des CustomSentenceSent 
	 * contenues dans le CustomDatasetSent.
	 * 
	 * Appelle la méthode getMeanSent puis renvoie le label avec la moyenne la plus élevée.
	 * 
	 * @return
	 * 		La sentiment majoritaire : VP, P, N ou VN.
	 */
	public String getLabel() {
		HashMap<String, Double> sentValues = this.getMeanSent();
		Entry<String, Double> maxVal = Collections.max( sentValues.entrySet(), (Entry<String, Double> val1, Entry<String, Double> val2) -> val1.getValue().compareTo(val2.getValue() ) );
		return maxVal.getKey();
	}

	/**
	 * Classe les CustomSentenceSent contenues dans le CustomDatasetSent 
	 * en fonction de leur sentiment majoritaire.
	 * 
	 * @return
	 * 			Les CustomSentenceSent triées dans les classes : VP, P, N, VN.
	 */
	public HashMap<String, ArrayList<CustomSentenceSent>> getClusters() {
		HashMap<String, ArrayList<CustomSentenceSent>> clusters = new HashMap<String, ArrayList<CustomSentenceSent>>();
		clusters.put("VP", new ArrayList<CustomSentenceSent>());
		clusters.put("P", new ArrayList<CustomSentenceSent>());
		clusters.put("N", new ArrayList<CustomSentenceSent>());
		clusters.put("VN", new ArrayList<CustomSentenceSent>());
		for (CustomSentenceSent sent: this.customSentences) {
			String cluster = sent.maxValue().getKey();
			clusters.get(cluster).add(sent);
		}
		return clusters;
	}
	
	
	/**
	 * Classe les CustomSentenceSent contenues dans le CustomDatasetSent 
	 * en fonction de leur sentiment majoritaire. 
	 * Chaque classe est ensuite triée par ordre décroissant 
	 * de la valeur du sentiment de la classe.
	 * 
	 * @return
	 * 			Les CustomSentenceSent classées par sentiment puis triées par ordre décroisant.
	 */
	public HashMap<String, ArrayList<CustomSentenceSent>> getClustersSorted() {
		HashMap<String, ArrayList<CustomSentenceSent>> clusters = this.getClusters();
		clusters.get("VP").sort(CustomSentenceSent.VeryPositiveComparator);
		clusters.get("P").sort(CustomSentenceSent.PositiveComparator);
		clusters.get("N").sort(CustomSentenceSent.NegativeComparator);
		clusters.get("VN").sort(CustomSentenceSent.VeryNegativeComparator);
		return clusters;
	}
	


}
