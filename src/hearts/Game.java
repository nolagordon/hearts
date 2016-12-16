package hearts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import rl.GameState;

public class Game {

    int currentPlayer;
	int players;
	ArrayList<Hand> hands;
	ArrayList<Card[]> tricks;
    HeartsTransition[] mctsTransitions;
    ArrayList<int[]> cardPlayers;

    // record the history of game states for each player
    ArrayList<ArrayList<GameState>> history;

    // store the trick winners and number of hearts in each trick
    // to allow easy undoing of moves in the unplayCard method
    int[] trickHearts;
    int[] trickWinners;

    // HeartsTransition lastTransition;
    int[] scores;
    int twoOfClubs;
    int turn;
    int leadingSuit;
    boolean heartsPlayed;
	
	public Game() {
	    turn = 0;
		this.players = 4;
		scores = new int[4];

		history = new ArrayList<ArrayList<GameState>>();
		for (int i = 0; i < players; i++) {
		    history.add(new ArrayList<GameState>());
		}
		tricks = new ArrayList<Card[]>();
		cardPlayers = new ArrayList<int[]>();
		trickHearts = new int[13];
		trickWinners = new int[13];
		mctsTransitions = new HeartsTransition[13];

		leadingSuit = Card.CLUBS;
		for (int i = 0; i < 13; i++) {
		    tricks.add(new Card[4]);
		    cardPlayers.add(new int[4]);
		    for (int j = 0; j < players; j++) {
			history.get(j).add(null);
		    }
		}

		deal();
		
		currentPlayer = twoOfClubs;
		// stores whether or not a heart has been played
		heartsPlayed = false;
	}
		
	// deal 13 cards to each of 4 players
	private void deal() {
		// populate deck of cards; 
		Random rand = new Random();
		ArrayList<Card> cardPile = new ArrayList<Card>();
		for (int s = 0; s < players; s++) {
			for (int v = 2; v <= 14; v++) {
				cardPile.add(new Card(s,v));
			}
		}
		// deal cards
		hands = new ArrayList<Hand>();
		for (int player = 0; player < 4; player++) {
			hands.add(new Hand());
			for (int i = 0; i < 13; i++) {
			    int cardIndex = rand.nextInt(cardPile.size());
			    Card cardToAdd = cardPile.remove(cardIndex);
			    
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

    // for td learning: returns a list of possible states the game can transition to
    public Map<GameState, Card> getPossibleStates(int currentPlayer) {
	    Hand hand = hands.get(currentPlayer);
	    Map<GameState, Card> states = new HashMap<GameState, Card>();
	  
	    // if leading the trick, may or may not be able to use hearts
	    // but otherwise can choose any card
	    if (turn % players == 0) {
		// if hearts have not already been played, we can lead with non-heart card
	    	// otherwise falls through to adding all possible cards from hand
		if (!heartsPlayed) {
		    for (Card c: hand.getList()) {
			if (c.getSuit() != Card.HEARTS) {
			    Game clone = this.clone();
			    clone.playCard(new HeartsTransition(c, currentPlayer));
			    states.put(clone.getHistory(currentPlayer, (turn - 1) % players), c);
			}
		    }
		}
	    } else { // not leading the trick
		for (Card c1 : hand.getList()) {
		    // must play card in leading suit if in hand
		    if (c1.getSuit() == leadingSuit) {
			Game clone = this.clone();
			clone.playCard(new HeartsTransition(c1, currentPlayer));
			states.put(clone.getHistory(currentPlayer, (turn - 1) % players), c1);
		    }
		} 
	    }
	    // if hearts have already been played
	    if (states.isEmpty()) {
		for (Card c : hand.getList()) {
		    Game clone = this.clone();
		    clone.playCard(new HeartsTransition(c, currentPlayer));
		    states.put(clone.getHistory(currentPlayer, (turn - 1) % players), c);
		}
	    }
	    return states;
	
    }
	
	public Set<HeartsTransition> getPossibleMoves(int currentPlayer) {
	    //System.out.println("\n\n It's player " + currentPlayer + "'s turn, turn # " + turn);
	    //System.out.println("\n\n HANDS \n\n");
	    
	    //printHands();
	    Hand hand = hands.get(currentPlayer);
	    Set<HeartsTransition> moves = new HashSet<HeartsTransition>();
	  
	    // if leading the trick, may or may not be able to use hearts
	    // but otherwise can choose any card
	    if (turn % players == 0) {
			// if hearts have not already been played, we can lead with non-heart card
	    	// otherwise falls through to adding all possible cards from hand
			if (!heartsPlayed) {
			    for (Card c: hand.getList()) {
			    	if (c.getSuit() != Card.HEARTS) {
			    		moves.add(new HeartsTransition(c, currentPlayer));
			    	}
			    }
			}
	    } else { // not leading the trick
			for (Card c1 : hand.getList()) {
			    // must play card in leading suit if in hand
			    if (c1.getSuit() == leadingSuit) {
			    	moves.add(new HeartsTransition(c1, currentPlayer));
			    }
			} 
	    }
	    // if hearts have already b
	    if (moves.isEmpty()) {
			for (Card c : hand.getList()) {
			    moves.add(new HeartsTransition(c, currentPlayer));
			}
	    }
	    
	    if (moves.isEmpty()) {
		System.out.println("size of hand is " + hand.size());
		System.out.println("Error! Moves empty!");
		System.exit(0);
	    }

	    return moves;
	}
	
	// play card, return next player
	public int playCard(HeartsTransition transition) {
		Card card = transition.getCard();
		int playerNum = transition.getPlayer();
		//lastTransition = transition;
		mctsTransitions[turn/players] = transition;
		Card[] trick = tricks.get(turn/players);
		
		this.hands.get(playerNum).remove(card);
		trick[turn % players] = card;
		cardPlayers.get(turn/players)[turn % players] = playerNum;

		// if this is the first turn of the suit, set the leadingSuit to that card's suit
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
		    if (winningCard.getSuit() == Card.HEARTS) { 
			hearts++;
		    }

		    // the first player of the trick
		    int winner = cardPlayers.get(turn/players)[0];
		    for (int j = 1; j < this.players; j++) {
			Card c = trick[j];
			// count hearts
			if (c.getSuit() == Card.HEARTS) {
			    hearts++;
			}
			// the player with the highest value card in the leading suit wins the trick
			if (c.getSuit() == winningCard.getSuit() && c.getVal() > winningCard.getVal()) {
			    winner = (j + cardPlayers.get(turn/players)[0]) % this.players;
			    winningCard = c;
			}
		    }

		    // now we know the winner and number of hearts for this trick
		    // we can store them in trickHearts and trickWinners
		    // to make backtracking easier later on
		    trickWinners[turn/players] = winner;
		    trickHearts[turn/players] = hearts;

		    nextPlayer = winner;
		    scores[winner] += hearts;

		}
		turn++;
		// update the history
		GameState state = new GameState(scores, 
					    scores[currentPlayer], 
					    hands, 
					    hands.get(currentPlayer), 
					    turn == 51, 
					    false, 
					    false);
		ArrayList<GameState> playerHist = history.get(currentPlayer);
		playerHist.set(((turn-1) / players), state);

		currentPlayer = nextPlayer;
		return nextPlayer;
	}
	
	// undo playing of card, return player
	public int unplayCard(Card card, int playerNum) {
	    //assert we are undoing most recent move
	    //assert card == lastTransition.getCard() && playerNum == lastTransition.getPlayer();
	    
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
			int hearts = trickHearts[(turn - 1)/players]; //num points won in last trick
			int winner = trickWinners[(turn - 1)/players];
		
			scores[winner] -= hearts;
	    }
	    turn--;
	    return playerNum;
	}
    
	public int hasTwoOfClubs() {
	    return twoOfClubs;
	}

    public ArrayList<GameState> getHistory(int playerNum) {
	return history.get(playerNum);
    }

    public GameState getHistory(int playerNum, int trickNum) {
	return history.get(playerNum).get(trickNum);
    }

    public Card getTwoOfClubs() {
		for (Card c: hands.get(twoOfClubs).getList()) {
		    if (c.getSuit() == Card.CLUBS && c.getVal() == 2) {
		    	return c;
		    }
		}
		return null;
    }

    public ArrayList<int[]> getCardPlayers() {
    	return cardPlayers;
    }

    public int[] getWinners() {
    	return trickWinners;
    }
    
    public HeartsTransition[] getMctsTransitions() {
    	return mctsTransitions;
    }
    
    public int getCurrentPlayer() {
    	return currentPlayer;
    }

    public void printHands() {
	for (Hand h : hands) {
	    for (Card c : h.getList()) {
		System.out.println(c);
	    }
	    System.out.println("xxxxxxxxxxxxxxxx");
	}
    }

    public void next() {
		if ((turn+1) % players == 0) {
		    currentPlayer = trickWinners[(turn - 1)/players];
		} else {
		    currentPlayer = (currentPlayer + 1) % players; 
		}
    }
    
    public Game clone() {
    	Game clone = new Game();
    	clone.currentPlayer = this.currentPlayer;
    	clone.players = this.players;
        clone.twoOfClubs = this.twoOfClubs;
        clone.turn = this.turn;
        clone.leadingSuit = this.leadingSuit;

        clone.cardPlayers = new ArrayList<int[]>();
        clone.tricks = new ArrayList<Card[]>();
        for (int i = 0; i < 13; i++) {
        	clone.tricks.add(this.tricks.get(i).clone());
    		clone.cardPlayers.add(this.cardPlayers.get(i).clone());
    	}
        
    	clone.hands = new ArrayList<Hand>();
        for (Hand h : hands) {
	    clone.hands.add(h.clone());
        }

	clone.mctsTransitions = this.mctsTransitions.clone();
        clone.trickHearts = this.trickHearts.clone();
        clone.trickWinners = this.trickWinners.clone();
        clone.scores = this.scores.clone();
        
        clone.heartsPlayed = this.heartsPlayed;
    	return clone;
    }
    
}
