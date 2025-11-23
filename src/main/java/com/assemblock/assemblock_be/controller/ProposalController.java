package com.assemblock.assemblock_be.controller;

import com.assemblock.assemblock_be.entity.Proposal;
import com.assemblock.assemblock_be.service.ProposalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/proposals")
public class ProposalController {

    private final ProposalService proposalService;

    // 1) 제안 생성
    @PostMapping
    public Proposal sendProposal(
            @RequestParam Long proposerId,
            @RequestParam String discordId,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String title,
            @RequestParam String memo
    ) {
        return proposalService.sendProposal(
                proposerId,
                discordId,
                LocalDate.parse(startDate),
                LocalDate.parse(endDate),
                title,
                memo
        );
    }

    // 2) 제안 조회
    @GetMapping("/{id}")
    public Proposal findProposal(@PathVariable Long id) {
        return proposalService.findById(id);
    }

    // 3) 전체 제안 조회
    @GetMapping
    public List<Proposal> findAll() {
        return proposalService.findAll();
    }

    // 4) 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        proposalService.delete(id);
    }
}
