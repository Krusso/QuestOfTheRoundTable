package com.qotrt.util;

import java.util.List;

import com.qotrt.gameplayer.Player;

public class PlayerUtil {

	public static int[] playersToIDs(List<Player> players) {
		return players.stream().mapToInt(i -> i.getID()).toArray();
	}
	
}
