package hearts;

public class Card {
	public final int HEARTS = 0;
	public final int DIAMONDS = 1;
	public final int SPADES = 2;
	public final int CLUBS = 3;
	
	int suit;
	int val;
	
	public Card(int suit, int val) {
		this.suit = suit;
		this.val = val;
	}
	
	public int getSuit() {
		return suit;
	}
	
	public int getVal() {
		return val;
	}
	
}
