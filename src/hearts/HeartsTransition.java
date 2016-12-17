package hearts;

import fr.avianey.mcts4j.Transition;

/*
 * This file is part of minimax4j.
 * <https://github.com/avianey/minimax4j>
 *  
 * Copyright (C) 2012 Antoine Vianey
 * 
 * minimax4j is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * minimax4j is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with minimax4j. If not, see <http://www.gnu.org/licenses/lgpl.html>
 */

/**
 * A basic move implementation : who and where...
 * 
 * original code @author antoine vianey modified by @author ameliaarcher
 * and @author nola gordon
 */
public class HeartsTransition implements Transition {

	/** The player owning the move */
	private int player;

	/** Card to play */
	private Card card;

	public HeartsTransition(Card card, int player) {
		this.card = card;
		this.player = player;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

	// TODO: is this a good hashcode?
	@Override
	public int hashCode() {
		return (player >> 6) | (card.getSuit() >> 3) | card.getVal();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof HeartsTransition && ((HeartsTransition) o).player == player
				&& ((HeartsTransition) o).card == card;
	}

	public String toString() {
		return "HeartsTransition";
	}

}
