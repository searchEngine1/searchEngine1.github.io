package il.ac.shenkar.model.dto;

import java.util.Comparator;

public class Word {
	private long id;
	private String word;
	private int hits;
	private StringBuilder docs;
	
	public Word() {
		super();
	}

	public Word(String word, String docId) {
		this.word = word;
		this.hits = 1;
		docs = new StringBuilder();
		docs.append(docId);
	}
	
	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	public int getHits() {
		return hits;
	}
	
	public void setHits(int hits) {
		this.hits = hits;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void addHitByOne() {
		hits++;
	}

	
	public void appendDoc(String docId)  {
		docs.append(",");
		docs.append(docId);
	}
	
	@Override
	public String toString() {
		return "[word=" + word + ", hits=" + hits + "]";
	}

	public StringBuilder getDocs() {
		return docs;
	}

	public void setDocs(StringBuilder docs) {
		this.docs = docs;
	}
	
	public static Comparator<Word> WordComparator = new Comparator<Word>() {

		public int compare(Word w1, Word w2) {

			String word1 = w1.getWord().toLowerCase();
			String word2 = w2.getWord().toLowerCase();

			// ascending order
			return word1.compareTo(word2);
		}

	};
	
	
	

}
