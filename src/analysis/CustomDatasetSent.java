package analysis;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

public class CustomDatasetSent {

	protected ArrayList<CustomSentenceSent> customSentences;
	
	public CustomDatasetSent(ArrayList<CustomSentenceSent> data) {
		this.customSentences = data;
	}
	
	public CustomDatasetSent() {
		this.customSentences = new ArrayList<CustomSentenceSent>();
	}
	
	
	/**
	 * Ajoute le contenu d'un CustomDatasetSent dans le CustomDataset courant
	 * @param dataset
	 * 			Le CustomDatasetSent à ajouter
	 */
	public void concat(CustomDatasetSent dataset) {
		this.customSentences.addAll(dataset.getCustomSentences());
	}
	
	@Override
	public String toString() {
		return customSentences.toString();
	}
	
	public void add(CustomSentenceSent customSent) {
		this.customSentences.add(customSent);
	}
	
	public ArrayList<CustomSentenceSent> getCustomSentences() {
		return customSentences;
	}
	
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
	
	public String getLabel() {
		HashMap<String, Double> sentValues = this.getMeanSent();
		Entry<String, Double> maxVal = Collections.max( sentValues.entrySet(), (Entry<String, Double> val1, Entry<String, Double> val2) -> val1.getValue().compareTo(val2.getValue() ) );
		return maxVal.getKey();
	}

	
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
	
	public HashMap<String, ArrayList<CustomSentenceSent>> getClustersSorted() {
		HashMap<String, ArrayList<CustomSentenceSent>> clusters = this.getClusters();
		clusters.get("VP").sort(CustomSentenceSent.VeryPositiveComparator);
		clusters.get("P").sort(CustomSentenceSent.PositiveComparator);
		clusters.get("N").sort(CustomSentenceSent.NegativeComparator);
		clusters.get("VN").sort(CustomSentenceSent.VeryNegativeComparator);
		return clusters;
	}
	


}
