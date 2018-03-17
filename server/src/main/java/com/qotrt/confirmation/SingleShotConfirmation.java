package com.qotrt.confirmation;

import com.qotrt.gameplayer.Player;

public class SingleShotConfirmation extends Confirmation {

	public SingleShotConfirmation(String eventName, String acceptEventName, String declineEventName) {
		super(eventName, acceptEventName, declineEventName);
	}

	@Override
	public void accept(Player player, String attempt, String success, String failure) {
		System.out.println(attempt);
		if(backingInt > 0) {
			backingInt = 0;
			System.out.println(success);
			accepted.add(player);
			if(acceptEventName != null) {
				fireEvent(acceptEventName, null, player);
			}
			checkIfCanOpenLatch(cdl, backingInt);
		} else {
			System.out.println(failure);
		}
	}

}
