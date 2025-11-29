package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Entity.Proposal;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Repository.ProposalRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProposalService {

    private final ProposalRepository proposalRepository;

    public Proposal create(Proposal proposal) {
        return proposalRepository.save(proposal);
    }

    public Proposal findById(Long id) {
        return proposalRepository.findById(id).orElse(null);
    }

    public List<Proposal> findAll() {
        return proposalRepository.findAll();
    }

    public void delete(Long id) {
        proposalRepository.deleteById(id);
    }

    public Proposal sendProposal(Long proposerId, String discordId,
                                 LocalDate start, LocalDate end,
                                 String title, String memo) {

        Proposal p = new Proposal();
        p.setProposer(new User(proposerId));
        p.setDiscordId(discordId);
        p.setRecruitStartDate(start);
        p.setRecruitEndDate(end);
        p.setProjectTitle(title);
        p.setProjectMemo(memo);

        return proposalRepository.save(p);
    }
}
