package com.assemblock.assemblock_be.service;

import com.assemblock.assemblock_be.entity.ProposalTarget;
import com.assemblock.assemblock_be.entity.ProposalTargetId;
import com.assemblock.assemblock_be.repository.ProposalTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProposalTargetService {

    private final ProposalTargetRepository proposalTargetRepository;

    public ProposalTarget create(ProposalTarget proposalTarget) {
        return proposalTargetRepository.save(proposalTarget);
    }

    public ProposalTarget findOne(ProposalTargetId id) {
        return proposalTargetRepository.findById(id).orElse(null);
    }

    public List<ProposalTarget> findAll() {
        return proposalTargetRepository.findAll();
    }

    public void delete(ProposalTargetId id) {
        proposalTargetRepository.deleteById(id);
    }
}
