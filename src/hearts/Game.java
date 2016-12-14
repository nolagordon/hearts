package hearts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
//import java.util.Scanner;
import java.util.Set;

public class Game {

    // represents AI choosing random cards
    public final static int RANDOM = 0;
    // AI always aims to lose the trick
    public final static int TRICK = 1;

	int players;
	ArrayList<Hand> hands;
	ArrayList<Card[]> tricks;
	HeartsTransition lastTransition;
	int[] scores;
	int[] strategies;
    int twoOfClubs;
    //Scanner scanner;
    int turn;
    int leadingSuit;
    int userNum;
    boolean heartsPlayed;
	
	public Game() {
		this.players = 4;
		strategies = new int[4];
		scores = new int[4];
		tricks = new ArrayList<Card[]>();
		leadingSuit = Card.CLUBS;
		for (int i = 0; i < 13; i++) {
			tricks.add(new Card[4]);
		}
		deal();
		
		//scanner = new Scanner(System.in);
		// choose a player number to represent he user
		// TEMPORARILY REMOVE USER INPUT int userNum = 0;
		userNum = 17;

		// stores whether or not a heart has been played
		heartsPlayed = false;
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
		// print out initial hands
		/*for (Hand h : hands) {
		    h.print();
		}*/
	}
	
	//return player with lowest score, if tied picks higher player num
	public int lowestScorePlayer() {
		int lowestPlayer = 0;
		int lowestScore = scores[0];
		for (int i = 1; i < players; i++) {
			if (lowestScore > scores[i]) {
				lowestPlayer = i;
				lowestScore = scores[i];
			}
		}
		return lowestPlayer;
	}

	public ArrayList<Hand> getHands() {
		return hands;
	}
	
	public int getTurn() {
		return turn;
	}
	
	public ArrayList<Card[]> getTricks() {
		return tricks;
	}
	
	public int[] getScores() {
		return scores;
	}
	
	public Set<HeartsTransition> getPossibleMoves(int currentPlayer) {
		Hand hand = hands.get(currentPlayer);
    	Set<HeartsTransition> moves = new HashSet<HeartsTransition>();
		for (Card c1 : hand.getList()) {
			if (c1.getSuit() == leadingSuit) {
				moves.add(new HeartsTransition(c1, currentPlayer));
			}
		}
		if (moves.isEmpty()) {
			for (Card c2 : hand.getList()) {
				moves.add(new HeartsTransition(c2, currentPlayer));
			}
		}
		return moves;
	}
	
	// play card, return next player
	public int playCard(HeartsTransition transition) {
		Card card = transition.getCard();
		int playerNum = transition.getPlayer();
		lastTransition = transition;
		Card[] trick = tricks.get(turn/players);
		
	    this.hands.get(playerNum).remove(card);
		trick[turn % players] = card;
		if (turn % players == 0) {
			leadingSuit = trick[0].getSuit();
		}
		
		//TODO: is there a better place to put this?
		// if the suit of the card played is hearts, then hearts have been played
		if (card.getSuit() == Card.HEARTS) { heartsPlayed = true; }

		// calculate which player goes next in the trick
		int nextPlayer = (playerNum + 1) % players;

		// if last turn of trick, choose winner
		if (turn % players == players - 1) {
		    // figure out the highest card of the leading suit in the trick
		    // to determine who won the trick
		    int hearts = 0; // count hearts in trick
		    Card winningCard = trick[0];
		    int winner = 0;
		    for (int j = 1; j < this.players; j++) {
				Card c = trick[j];
				// count hearts
				if (c.getSuit() == Card.HEARTS) {
					hearts++;
				}
				// the player with the highest value card in the leading suit wins the trick
				if (c.getSuit() == leadingSuit && c.getVal() > winningCard.getVal()) {
				    winner = (j + playerNum) % this.players;
				    winningCard = c;
				}
		    }
		    nextPlayer = winner;
		    scores[winner] += hearts;
		    for (int s : scores) {
		    	System.out.print(s + " ");
		    }
		    System.out.println("Points subtracted");
		}
		turn++;
		return nextPlayer;
	}
	
	// undo playing of card, return player
	public int unplayCard(Card card, int playerNum) {
		//assert we are undoing most recent move
		assert card == lastTransition.getCard() && playerNum == lastTransition.getPlayer();
	    this.hands.get(playerNum).add(card);

		//TODO: is there a better place to put this?
		// if the suit of the card played is hearts, check whether was 1st heart played
	    heartsPlayed = false;	    
	    if (card.getSuit() == Card.HEARTS) {
	    	outerloop:
	    	for (Card[] trick : tricks) {
	    		for (Card c : trick) {
	    			if (c.getSuit() == Card.HEARTS) {
	    				heartsPlayed = true;
	    				break outerloop;
	    			}
	    		}
	    	}
	    }
	    
		// if first turn of trick
		if (turn % players == 0) {
		    // figure out who won last trick and undo points
			int hearts = 0; //num points won in last trick
			Card[] trick = tricks.get(turn/players - 1);
		    int leadingSuit = trick[0].getSuit();
		    Card winningCard = trick[0];
		    int winner = 0;
		    for (int j = 1; j < this.players; j++) {
				Card c = trick[j];
				if (c.getSuit() == Card.HEARTS) { hearts++; }
				// the player with the highest value card in the leading suit wins the trick
				if (c.getSuit() == leadingSuit && c.getVal() > winningCard.getVal()) {
				    winner = (j + playerNum) % this.players;
				    winningCard = c;
				}
		    }
		    scores[winner] -= hearts;
		    for (int s : scores) {
		    	System.out.print(s + " ");
		    }
		    System.out.println();
		}
	    turn--;
		return playerNum;
	}
	
	public int hasTwoOfClubs() {
		return twoOfClubs;
	}
}
