package com.assemblock.assemblock_be.controller;

import com.assemblock.assemblock_be.Entity.Proposal;
import com.assemblock.assemblock_be.service.ProposalService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/proposals")
public class ProposalController {
    private final ProposalService proposalService;

    @PostMapping
    public Proposal sendProposal(@RequestBody ProposalRequestDto request) {
        return proposalService.sendProposal(
                request.getProposerId(),
                request.getDiscordId(),
                LocalDate.parse(request.getStartDate()),
                LocalDate.parse(request.getEndDate()),
                request.getTitle(),
                request.getMemo()
        );
    }

    @GetMapping("/{id}")
    public Proposal findProposal(@PathVariable Long id) {
        return proposalService.findById(id);
    }

    @GetMapping
    public List<Proposal> findAll() {
        return proposalService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        proposalService.delete(id);
    }

    @Data
    public static class ProposalRequestDto {
        private Long proposerId;
        private String discordId;
        private String startDate;
        private String endDate;
        private String title;
        private String memo;
    }
}