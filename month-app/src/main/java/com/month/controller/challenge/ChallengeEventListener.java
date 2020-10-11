package com.month.controller.challenge;

import com.month.event.challenge.AllMembersEnteredEvent;
import com.month.service.challenge.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChallengeEventListener {

	private final ChallengeService challengeService;

	@EventListener
	public void autoStartChallenge(AllMembersEnteredEvent event) {
		challengeService.autoStartChallenge(event.getChallengePlanId());
	}

}
