package hearts;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {

    // represents AI choosing random cards
    public final static int RANDOM = 0;
    // AI always aims to lose the trick
    public final static int TRICK = 1;

    int[] strategies;
    
	
	int players;
	ArrayList<Hand> hands;
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
		strategies = new int[4];
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
		hands = new ArrayList<Hand>();
		for (int player = 0; player < 4; player++) {
			hands.add(new Hand());
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
		//sort hands
		for (Hand h : hands) {
			h.sort();
		}
		// computer players
		// print out initial hands
		for (Hand h : hands) {
		    h.print();
		}
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		Scanner scanner = new Scanner(System.in);
		// choose a player number to represent he user
		// TEMPORARILY REMOVE USER INPUT int userNum = 0;
		int userNum = 17;
		game.strategies[0] = TRICK;
		game.strategies[1] = TRICK;
		game.strategies[2] = TRICK;
		game.strategies[3] = TRICK;


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
			Card playedCard = null;

			if (playerNum == userNum) {
			    // if it is the user's turn, show them their hand and let them pick a card
			    System.out.println("This is your hand: ");
			    game.printHand(userNum);

			    System.out.print("Pick a card to play: ");
			    playedCard = game.hands.get(userNum).get(scanner.nextInt());
			    System.out.println("You played the " + playedCard);
			    game.hands.get(userNum).remove(playedCard);
			   
			} else {
			    // otherwise the computer is taking a move
			    // so run the method that lets them choose a card
			    playedCard = game.playCard(playerNum, trick, heartsPlayed, firstTrick, game.strategies[playerNum]);
			    System.out.println("Player " + playerNum + " played the " + playedCard);

			}

			//TODO: is there a better place to put this?
			// if the suit of the card played is hearts, then hearts have been played
			if (playedCard.getSuit() == Card.HEARTS) { heartsPlayed = true; }
			firstTrick = false;
			trick.add(playedCard);

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

		    if (winner == userNum) { System.out.println("You won this trick"); }
		    else {System.out.println("Player " + winner + " won this trick"); }
		    leader = winner;

		}

		
	}

    public void printHand(int playerNum) {
	// todo: change the 13 to something less concrete
	for(int i = 0; i < hands.get(playerNum).size(); i++) {
	    System.out.println(i + ". " + hands.get(playerNum).get(i));
	}
    }

    public Card playCard(int playerNum, ArrayList<Card> trick, boolean heartsPlayed, boolean firstTrick, int strategy) {
	Hand hand = hands.get(playerNum);
	if (firstTrick) {
	    // play the two of clubs
	    for (int i = 0; i < hand.size(); i++) {
			Card c = hand.get(i);
			if (c.getSuit() == Card.CLUBS && c.getVal() == 2) {
			    return c;
			}
	    }
	}

	Boolean hasLeadingSuit = false;
	
	ArrayList<Card> mylist = new ArrayList<Card>();
	if (trick.size() > 0) {
	    // if the player has a card in the leading suit, must play that card
	    // compile a list of cards in the player's hand of the leading suit
	    int leadingSuit = trick.get(0).getSuit();
	    mylist = hand.cardsInSuit(leadingSuit);
	    if (mylist.size() > 0) {
	    	hasLeadingSuit = true;
	    } else {
	    	mylist = hand.getList();
	    }
	} else {
		mylist = hand.getList();
	    // this player is leading the trick
	    // can only lead with hearts if hearts have already been played
	    if (!heartsPlayed) {
	    	mylist = new ArrayList<Card>();
	    	mylist.addAll(hand.cardsInSuit(Card.SPADES));
	    	mylist.addAll(hand.cardsInSuit(Card.CLUBS));
	    	mylist.addAll(hand.cardsInSuit(Card.SPADES));
		
	    	// 	if there are no valid cards, that means the user only has hearts left
	    	// so we set valid cards back to the entire hand
	    	if (mylist.size() == 0) { mylist = hand.getList(); }
	    }
	}

	// We've compiled a list of valid to choose from
	Random r = new Random();
	Card card;
	// case when the hand has cards of the leading suit
	if (hasLeadingSuit) {
	    if (strategy == this.RANDOM) {
		card = mylist.get(r.nextInt(mylist.size()));
	    } else { // if strategy == TRICK
		// find the lowest value card
		card = mylist.get(0);
		for (int i = 1; i < mylist.size(); i++) {
		    Card other = mylist.get(i);
		    if (other.getVal() < card.getVal()) { card = other; }
		}
	    }
	} else {
	    if (strategy == this.RANDOM) {
		card = mylist.get(r.nextInt(mylist.size()));
	    } else { // if strategy == TRICK
		card = mylist.get(0);
		for (int i = 1; i < mylist.size(); i++) {
		    Card other = mylist.get(i);
		    if (other.getVal() > card.getVal()) { card = other; }
		}
	    }
	}
	hand.remove(card);
	return card;
    }

}
