package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.ProposalCreateRequestDto;
import com.assemblock.assemblock_be.Dto.ProposalResponseDto;
import com.assemblock.assemblock_be.Dto.ProposalListDto;
import com.assemblock.assemblock_be.Dto.ProposalTargetUpdateRequestDto;
//import com.assemblock.assemblock_be.Entity.Proposal;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Service.ProposalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/proposals")
public class ProposalController {

    private final ProposalService proposalService;

    @PostMapping
    public ResponseEntity<ProposalResponseDto> createProposal(
            @AuthenticationPrincipal User user,
            @RequestBody ProposalCreateRequestDto requestDto
    ) {
        ProposalResponseDto response = proposalService.createProposal(user.getId(), requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<List<ProposalListDto>> getMyProposals(
            @AuthenticationPrincipal User user
    ) {
        List<ProposalListDto> proposals = proposalService.getMyProposals(user.getId());
        return ResponseEntity.ok(proposals);
    }

    @GetMapping("/{proposalId}")
    public ResponseEntity<ProposalResponseDto> getProposal(@PathVariable Long proposalId) {
        ProposalResponseDto response = proposalService.getProposalDetail(proposalId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{proposalId}")
    public ResponseEntity<Void> delete(@PathVariable Long proposalId) {
        proposalService.delete(proposalId);
        return ResponseEntity.ok().build();
    }
/*
    @GetMapping("/all")
    public ResponseEntity<List<Proposal>> getAllProposals() {
        List<Proposal> proposals = proposalService.findAll();
        return ResponseEntity.ok(proposals);
    }

 */

    @PatchMapping("/{proposalId}/response")
    public ResponseEntity<ProposalResponseDto> respondToProposal(
            @AuthenticationPrincipal User user,
            @PathVariable Long proposalId,
            @RequestBody ProposalTargetUpdateRequestDto request
    ) {
        ProposalResponseDto response = proposalService.respondToProposal(user.getId(), proposalId, request);

        return ResponseEntity.ok(response);
    }
}