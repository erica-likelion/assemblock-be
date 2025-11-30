package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.ProposalTargetUpdateRequestDto;
import com.assemblock.assemblock_be.Service.ProposalTargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/proposal-targets")
public class ProposalTargetController {

    private final ProposalTargetService proposalTargetService;

    @PostMapping("/respond")
    public void respondToProposal(
                                   @RequestParam Long proposalId,
                                   @RequestParam Long proposalBlockId,
                                   @RequestParam String status
    ) {
        ProposalTargetUpdateRequestDto requestDto = ProposalTargetUpdateRequestDto.builder()
                .status(status)
                .build();

        proposalTargetService.updateResponseStatus(proposalId, proposalBlockId, requestDto);
    }
}