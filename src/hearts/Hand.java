package hearts;

import java.util.ArrayList;
import java.util.Collections;

public class Hand {
	private ArrayList<Card> cards;
	
	public Hand() {
		 cards = new ArrayList<Card>();
	}
	
	public void sort() {
		Collections.sort(cards, Card.cardComparator());
	}
	
	public void print() {
		for (int i = 0; i < cards.size(); i++) {
			System.out.println(i + ". " + cards.get(i));
		}
	}
	
	public void add(Card c) {
		cards.add(c);
	}
	
	public Card remove(Card c) {
	    //System.out.println("card to remove is " + c);
	    //System.out.println("card list is " + cards);
	    Card toRemove = null;
	    for (Card card: cards) {
		if (card.getSuit() == c.getSuit() && card.getVal() == c.getVal()) {
		    toRemove = card;
		}
	    }

	    if (toRemove == null) {
		System.out.println("Error: removing null from card list");
		System.exit(0);
	    } 
	    cards.remove(toRemove);
	    return toRemove;
	}   
	
	public Card get(int index) {
		return cards.get(index);
	}
	
	public boolean contains(Card c) {
		for (Card card : cards) {
			if (card.equals(c)) {
				return true;
			}
		}
		return false;
	}
	
	public int size() {
		return cards.size();
	}
	
	public ArrayList<Card> getList() {
		return cards;
	}
	
	public ArrayList<Card> cardsInSuit(int suit) {
		ArrayList<Card> inSuit = new ArrayList<Card>();
		for (Card c : cards) {
			if (c.getSuit() == suit) {
				inSuit.add(c);
			}
		}
		return inSuit;
	}

    public Hand clone() {
	Hand clone = new Hand();
	for (Card c : cards) {
	    clone.add(c);
	}
	return clone;
    }
	
}
