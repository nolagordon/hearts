package hearts;

import java.util.ArrayList;

public class Hand {
	private ArrayList<Card> cards;
	
	public Hand() {
		 cards = new ArrayList<Card>();
	}
	
	public void sort() {
		    ArrayList<Card> hearts = new ArrayList<Card>();
		    ArrayList<Card> diamonds = new ArrayList<Card>();
		    ArrayList<Card> clubs = new ArrayList<Card>();
		    ArrayList<Card> spades = new ArrayList<Card>();

		    for (int j = 0; j < cards.size(); j++) {
			Card curCard = cards.get(j);
			if (curCard.getSuit() == Card.HEARTS) { hearts.add(curCard); }
			else if (curCard.getSuit() == Card.DIAMONDS) { diamonds.add(curCard); }
			else if (curCard.getSuit() == Card.CLUBS) { clubs.add(curCard); }
			else { spades.add(curCard); }
		    }

		    for (int j = 0; j < hearts.size(); j++) {
			cards.set(j, hearts.get(j));
		    }

		    for (int j = 0; j < diamonds.size(); j++) {
			cards.set(j + hearts.size(), diamonds.get(j));
		    }

		    for (int j = 0; j < clubs.size(); j++) {
			cards.set(j + hearts.size() + diamonds.size(), clubs.get(j));
		    }

		    for (int j = 0; j < spades.size(); j++) {
			cards.set(j + hearts.size() + diamonds.size() + clubs.size(), spades.get(j));
		    }
	}
	
	public void print() {
		for (int i = 0; i < cards.size(); i++) {
			System.out.println(i + ". >" + cards.get(i));
		}
	}
	
	public void add(Card c) {
		cards.add(c);
	}
	
	public Card remove(Card c) {
		cards.remove(c);
		return c;
	}
	
	public Card get(int index) {
		return cards.get(index);
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
	
}
