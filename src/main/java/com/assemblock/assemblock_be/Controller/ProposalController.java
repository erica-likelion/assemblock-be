package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.*;
import com.assemblock.assemblock_be.Entity.Proposal;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Service.ProposalService;
import com.assemblock.assemblock_be.Service.ProposalTargetService;
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
    private final ProposalTargetService proposalTargetService;

    // 1. 제안 생성
    @PostMapping
    public ResponseEntity<Void> createProposal(@RequestBody ProposalCreateRequestDto requestDto) {
        proposalService.createProposal(requestDto);
        return ResponseEntity.ok().build();
    }

    // 2. 내가 쓴 제안서 조회
    @GetMapping("/me")
    public ResponseEntity<List<Proposal>> getMyProposals(
            @AuthenticationPrincipal User user
    ) {
        List<Proposal> proposals = proposalService.getMyProposals(user.getId());
        return ResponseEntity.ok(proposals);
    }

    // 3. 제안 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProposalResponseDto> getProposal(@PathVariable Long id) {
        ProposalResponseDto response = proposalService.getProposalDetail(id);
        return ResponseEntity.ok(response);
    }

    // 4. 제안 타겟 상태 수정
    @PatchMapping("/{proposalId}/targets/{blockId}")
    public ResponseEntity<Void> updateTargetStatus(
            @PathVariable Long proposalId,
            @PathVariable Long blockId,
            @RequestBody ProposalTargetUpdateRequestDto requestDto
    ) {
        proposalTargetService.updateResponseStatus(proposalId, blockId, requestDto);
        return ResponseEntity.ok().build();
    }

    // 5. 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        proposalService.delete(id);
        return ResponseEntity.ok().build();
    }
}