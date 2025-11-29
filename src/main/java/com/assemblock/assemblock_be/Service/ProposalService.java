package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Entity.Proposal;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Repository.ProposalRepository;
import com.assemblock.assemblock_be.Repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProposalService {
    private final ProposalRepository proposalRepository;
    private final UserRepository userRepository;

    @Transactional
    public Proposal create(Proposal proposal) {
        return proposalRepository.save(proposal);
    }

    @Transactional(readOnly = true)
    public Proposal findById(Long id) {
        return proposalRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Proposal> findAll() {
        return proposalRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        proposalRepository.deleteById(id);
    }

    @Transactional
    public Proposal sendProposal(Long proposerId, String discordId,
                                 LocalDate start, LocalDate end,
                                 String title, String memo) {

        User proposer = userRepository.findById(proposerId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        Proposal p = new Proposal();
        p.setProposer(proposer);
        p.setDiscordId(discordId);
        p.setRecruitStartDate(start);
        p.setRecruitEndDate(end);
        p.setProjectTitle(title);
        p.setProjectMemo(memo);

        return proposalRepository.save(p);
    }
}