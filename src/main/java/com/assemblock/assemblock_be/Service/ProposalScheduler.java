package com.assemblock.assemblock_be.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProposalScheduler {

    private final ProposalService proposalService;

    @Scheduled(cron = "0 0 0 * * *")
    public void autoRefuseTask() {
        proposalService.autoRefuseOverdueTargets();
    }
}