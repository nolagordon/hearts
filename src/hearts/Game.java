package hearts;

import java.util.ArrayList;
import java.util.Random;

public class Game {
	
	int players;
	ArrayList<ArrayList<Card>> hands;
	
	/*
	public Game(int players) {
		if (players >=3 && players <= 5) {
			this.players = players;
			deal();
		} else {
			System.out.println("Unsuitable number of players");
		}
		
	}
	*/
	
	public Game() {
		this.players = 4;
		deal();
	}
	
	// deal 13 cards to each of 4 players
	private void deal() {
		// populate deck of cards
		Random rand = new Random();
		ArrayList<Card> deck = new ArrayList<Card>();
		for (int s = 0; s < 4; s++) {
			for (int v = 1; v <= 13; v++) {
				deck.add(new Card(s,v));
			}
		}
		// deal cards
		hands = new ArrayList<ArrayList<Card>>();
		for (int player = 0; player < 4; player++) {
			hands.add(new ArrayList<Card>());
			for (int i = 0; i < 13; i++) {
				hands.get(player).add(deck.get(rand.nextInt(deck.size())));
			}
		}
		
	}
	
	public static void main(String[] args) {
		Game game = new Game();
	}
	
}
