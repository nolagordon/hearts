//package hearts;

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
			    int cardIndex = rand.nextInt(deck.size());
				hands.get(player).add(deck.remove(cardIndex));
			}
		}
		
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		
		// while the game is not over, let the user make a move, then let AI move
		System.out.println("This is your deck: ");
		game.printHand(0);
		
	}

    public void printHand(int playerNum) {
	// todo: change the 13 to something less concrete
	for(int i = 0; i < hands.get(playerNum).size(); i++) {
	    System.out.println(hands.get(playerNum).get(i));
	}
    }
	
}
