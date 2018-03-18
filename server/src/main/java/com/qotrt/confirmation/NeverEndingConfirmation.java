package com.qotrt.confirmation;

import com.qotrt.gameplayer.Player;

public class NeverEndingConfirmation extends Confirmation {

	public NeverEndingConfirmation(String eventName, String acceptEventName, String declineEventName) {
		super(eventName, acceptEventName, declineEventName);
	}

	@Override
	public boolean accept(Player player, String attempt, String success, String failure) {
		System.out.println(attempt);
		if(backingInt > 0) {
			System.out.println(success);
			accepted.clear();
			accepted.add(player);
			if(acceptEventName != null) {
				fireEvent(acceptEventName, null, player);
			}
			return true;
		} else {
			System.out.println(failure);
			return false;
		}
	}

}
