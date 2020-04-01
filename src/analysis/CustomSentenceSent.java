package analysis;


import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

public class CustomSentenceSent{
	
	public String getText() {
		return text;
	}

	public HashMap<String, Double> getSentValues() {
		return sentValues;
	}

	protected String text;
	protected HashMap<String,Double> sentValues;
	
	public CustomSentenceSent(String text, HashMap<String, Double> data) {
		this.text = text;
		this.sentValues = data;
	}
	
	public Entry<String, Double> maxValue() {
		Entry<String, Double> maxVal = Collections.max( this.sentValues.entrySet(), (Entry<String, Double> val1, Entry<String, Double> val2) -> val1.getValue().compareTo(val2.getValue() ) );
		return maxVal;
	}
	
	public String getLabel() {
		Entry<String, Double> maxVal = this.maxValue();
		return maxVal.getKey();
	}

	@Override
	public String toString() {
		return String.format("[\"%s\", %s]", this.text, this.sentValues.toString());
	}

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
