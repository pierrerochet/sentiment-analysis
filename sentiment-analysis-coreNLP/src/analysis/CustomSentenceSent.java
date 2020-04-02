package analysis;


import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;


/**
 * Une phrase avec les valeurs des sentiments associés
 * 
 * @author rochet
 *
 */
public class CustomSentenceSent{
	/**
	 * Retourne le texte de la phrase contenue dans le CustomSentenceSent
	 * 
	 * @return Le texte de la phrase.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Retourne les valeurs des sentiments à la phrase contenue dans le CustomSentenceSent.
	 * 
	 * @return Les valeurs des sentiments.
	 */
	public HashMap<String, Double> getSentValues() {
		return sentValues;
	}

	/**
	 * Le text de la phrase.
	 */
	protected String text;
	
	/**
	 * Les valeurs des sentiments.
	 */
	protected HashMap<String,Double> sentValues;
	
	/**
	 * Constructeur qui crée une CustomSentenceSent
	 * avec le texte du phrases et les valeurs des sentiments associés.
	 * 
	 * @param text
	 * 			Le texte de la phrase.
	 * @param data
	 * 			Les valeurs des sentiments associés.
	 */
	public CustomSentenceSent(String text, HashMap<String, Double> data) {
		this.text = text;
		this.sentValues = data;
	}
	
	/**
	 * Renvoie le sentiment et la valeur associée du sentiment le plus présent.
	 * 
	 * @return
	 * 			Le sentiment et sa valeur associée.
	 */
	public Entry<String, Double> maxValue() {
		Entry<String, Double> maxVal = Collections.max( this.sentValues.entrySet(), (Entry<String, Double> val1, Entry<String, Double> val2) -> val1.getValue().compareTo(val2.getValue() ) );
		return maxVal;
	}
	
	/**
	 * Renvoie le label associé à la phrase.
	 * 
	 * @return
	 * 		Le label : VP, P, N ou VN.
	 */
	public String getLabel() {
		Entry<String, Double> maxVal = this.maxValue();
		return maxVal.getKey();
	}
	
	/**
	 * Affiche une représentation de l'objet CustomSentenceSent.
	 */
	@Override
	public String toString() {
		return String.format("[\"%s\", %s]", this.text, this.sentValues.toString());
	}

	/**
	 * Permet de comparer deux CustomSentenceSent sur la valaur du sentiment VP.
	 */
	public static Comparator<CustomSentenceSent> VeryPositiveComparator = new Comparator<CustomSentenceSent>() {
		@Override
		public int compare(CustomSentenceSent s1, CustomSentenceSent s2) {
			double N1 = s1.getSentValues().get("VP");
			double N2 = s2.getSentValues().get("VP");
			if (N1 < N2) return 1;
			if (N1 > N2) return -1;
			else return 0;
		}
	};
	
	/**
	 * Permet de comparer deux CustomSentenceSent sur la valaur du sentiment P.
	 */
	public static Comparator<CustomSentenceSent> PositiveComparator = new Comparator<CustomSentenceSent>() {
		@Override
		public int compare(CustomSentenceSent s1, CustomSentenceSent s2) {
			double N1 = s1.getSentValues().get("P");
			double N2 = s2.getSentValues().get("P");
			if (N1 < N2) return 1;
			if (N1 > N2) return -1;
			else return 0;
		}
	};
	
	
	/**
	 * Permet de comparer deux CustomSentenceSent sur la valaur du sentiment N.
	 */
	public static Comparator<CustomSentenceSent> NegativeComparator = new Comparator<CustomSentenceSent>() {
		@Override
		public int compare(CustomSentenceSent s1, CustomSentenceSent s2) {
			double N1 = s1.getSentValues().get("N");
			double N2 = s2.getSentValues().get("N");
			if (N1 < N2) return 1;
			if (N1 > N2) return -1;
			else return 0;
		}
	};
	
	/**
	 * Permet de comparer deux CustomSentenceSent sur la valaur du sentiment VN.
	 */
	public static Comparator<CustomSentenceSent> VeryNegativeComparator = new Comparator<CustomSentenceSent>() {
		@Override
		public int compare(CustomSentenceSent s1, CustomSentenceSent s2) {
			double N1 = s1.getSentValues().get("VN");
			double N2 = s2.getSentValues().get("VN");
			if (N1 < N2) return 1;
			if (N1 > N2) return -1;
			else return 0;
		}
	};

}
