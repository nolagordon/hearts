//package hearts;

import java.util.ArrayList;
import java.util.Random;

public class Game {
	
	int players;
	ArrayList<ArrayList<Card>> hands;
    int twoOfClubs;
	
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
			for (int v = 2; v <= 14; v++) {
				deck.add(new Card(s,v));
			}
		}
		// deal cards
		hands = new ArrayList<ArrayList<Card>>();
		for (int player = 0; player < 4; player++) {
			hands.add(new ArrayList<Card>());
			for (int i = 0; i < 13; i++) {
			    int cardIndex = rand.nextInt(deck.size());
			    Card cardToAdd = deck.remove(cardIndex);
			    
			    // if a player gets the 2 of clubs, take note
			    // they will start the first trick of the game
			    if ((cardToAdd.getSuit() == Card.CLUBS) && (cardToAdd.getVal() == 2)) {
				twoOfClubs = player;
			    }
			    hands.get(player).add(cardToAdd);
			}
		}
		
	}
	
	public static void main(String[] args) {
		Game game = new Game();

		/*
		Scanner scanner = new Scanner(System.in);
		
		// while the game is not over, let the user make a move, then let AI move
		System.out.println("This is your hand: ");
		game.printHand(0);

		System.out.print("Pick a card to play: ");
		Card played = hands.get(0).get(scanner.nextInt());*/

		// computer players
		// print out initial hands
		for (int i = 0; i < 4; i++) {
		    System.out.println(game.hands.get(i));
		}

		// play 5 tricks
		// stores whether or not a heart has been played
		boolean heartsPlayed = false;
		boolean firstTrick = true;
		int leader = game.twoOfClubs;
		for (int i = 0; i < 13; i++) {
		    System.out.println("Trick " + (i+1) + ":");
		    ArrayList<Card> trick = new ArrayList<Card>();

		    // let each player take their turn during the trick
		    for (int j = 0; j < game.players; j++) {
			// calculate which player goes next in the trick
			int playerNum = (leader + j) % game.players;
			Card next = game.playCard(playerNum, trick, heartsPlayed, firstTrick);

			// if the suit of the card played is hearts, then hearts have been played
			if (next.getSuit() == Card.HEARTS) { heartsPlayed = true; }
			firstTrick = false;
			System.out.println("Player " + playerNum + " played the " + next);
			trick.add(next);
		    }

		    // figure out the highest card of the leading suit in the trick
		    // to determine who won the trick
		    int leadingSuit = trick.get(0).getSuit();
		    int winner = leader;
		    Card winningCard = trick.get(0);
		    for (int j = 1; j < game.players; j++) {
			Card card = trick.get(j);

			// the player with the highest value card in the leading suit wins the trick
			if (card.getSuit() == leadingSuit && card.getVal() > winningCard.getVal()) {
			    winner = (j + leader) % game.players;
			    winningCard = card;
			}
		    }

		    System.out.println("Player " + winner + " won this trick");
		    leader = winner;

		}

		
	}

    public void printHand(int playerNum) {
	// todo: change the 13 to something less concrete
	for(int i = 0; i < hands.get(playerNum).size(); i++) {
	    System.out.println(i + ". " + hands.get(playerNum).get(i));
	}
    }

    public Card playCard(int playerNum, ArrayList<Card> trick, boolean heartsPlayed, boolean firstTrick) {
	ArrayList<Card> hand = hands.get(playerNum);
	if (firstTrick) {
	    // play the two of clubs
	    for (int i = 0; i < hand.size(); i++) {
		Card c = hand.get(i);
		if (c.getSuit() == Card.CLUBS && c.getVal() == 2) {
		    return c;
		}
	    }
	}

	ArrayList<Card> validCards = hand;

	ArrayList<Card> leadingSuitCards = new ArrayList<Card>();
	if (trick.size() > 0) {
	    // if the player has a card in the leading suit, must play that card
	    // compile a list of cards in the player's hand of the leading suit
	    int leadingSuit = trick.get(0).getSuit();

	    for (int i = 0; i < hand.size(); i++) {
		if (hand.get(i).getSuit() == leadingSuit) {
		    leadingSuitCards.add(hand.get(i));
		}
	    }
	} else {
	    // this player is leading the trick
	    // can only lead with hearts if hearts have already been played
	    if (!heartsPlayed) {
		validCards = new ArrayList<Card>();
		for (int i = 0; i < hand.size(); i++) {
		    if (hand.get(i).getSuit() != hand.get(i).HEARTS) {
			validCards.add(hand.get(i));
		    }
		}
	    }
	}

	Random r = new Random();
	Card card;
	if (leadingSuitCards.size() > 0) {
	    card = leadingSuitCards.get(r.nextInt(leadingSuitCards.size()));
	} else {
	    card = validCards.get(r.nextInt(validCards.size()));
	}
	hand.remove(card);
	return card;
    }
	
}
