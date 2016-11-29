//package hearts;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

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
		Scanner scanner = new Scanner(System.in);
		// choose a player number to represent he user
		int userNum = 0;

		game.sortHands();

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
			    playedCard = game.playCard(playerNum, trick, heartsPlayed, firstTrick);
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

    // sorts each player's hand for readability
    public void sortHands() {
	for (int i = 0; i < hands.size(); i++) {
	    ArrayList<Card> hand = hands.get(i);
	    ArrayList<Card> hearts = new ArrayList<Card>();
	    ArrayList<Card> diamonds = new ArrayList<Card>();
	    ArrayList<Card> clubs = new ArrayList<Card>();
	    ArrayList<Card> spades = new ArrayList<Card>();

	    for (int j = 0; j < hand.size(); j++) {
		Card curCard = hand.get(j);
		if (curCard.getSuit() == Card.HEARTS) { hearts.add(curCard); }
		else if (curCard.getSuit() == Card.DIAMONDS) { diamonds.add(curCard); }
		else if (curCard.getSuit() == Card.CLUBS) { clubs.add(curCard); }
		else { spades.add(curCard); }
	    }

	    for (int j = 0; j < hearts.size(); j++) {
		hand.set(j, hearts.get(j));
	    }

	    for (int j = 0; j < diamonds.size(); j++) {
		hand.set(j + hearts.size(), diamonds.get(j));
	    }

	    for (int j = 0; j < clubs.size(); j++) {
		hand.set(j + hearts.size() + diamonds.size(), clubs.get(j));
	    }

	    for (int j = 0; j < spades.size(); j++) {
		hand.set(j + hearts.size() + diamonds.size() + clubs.size(), spades.get(j));
	    }

		   

	}
    }
	
}
