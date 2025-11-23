package com.assemblock.assemblock_be.controller;

import com.assemblock.assemblock_be.entity.ProposalTarget;
import com.assemblock.assemblock_be.service.ProposalTargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/proposal-targets")
public class ProposalTargetController {

    private final ProposalTargetService proposalTargetService;

    // 특정 Proposal + Block 에 대한 제안 상태 변경
    @PostMapping("/respond")
    public ProposalTarget respondToProposal(
            @RequestParam Long proposalId,
            @RequestParam Long proposalBlockId,
            @RequestParam String status
    ) {
        return proposalTargetService.updateResponseStatus(proposalId, proposalBlockId, status);
    }
}
