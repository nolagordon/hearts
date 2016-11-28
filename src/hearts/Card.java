//package hearts;

import java.util.HashMap;

public class Card {
	public static final int HEARTS = 0;
	public static final int DIAMONDS = 1;
	public static final int SPADES = 2;
	public static final int CLUBS = 3;
	
	int suit;
	int val;

    public HashMap<Integer,String> suitString = new HashMap<Integer,String>();

    public HashMap<Integer,String> valString = new HashMap<Integer,String>();
	
	public Card(int suit, int val) {
		this.suit = suit;
		this.val = val;

		suitString.put(0, "hearts");
		suitString.put(1, "diamonds");
		suitString.put(2, "spades");
		suitString.put(3, "clubs");
		
		valString.put(2,"two");
		valString.put(3,"three");
		valString.put(4,"four");
		valString.put(5,"five");
		valString.put(6,"six");
		valString.put(7,"seven");
		valString.put(8,"eight");
		valString.put(9,"nine");
		valString.put(10,"ten");
		valString.put(11,"jack");
		valString.put(12,"queen");
		valString.put(13,"king");
		valString.put(14,"ace");
		

	}
	
	public int getSuit() {
		return suit;
	}
	
	public int getVal() {
		return val;
	}

    public String toString() {
	return valString.get(val) + " of " + suitString.get(suit);
    }
	
}
