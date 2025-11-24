package com.assemblock.assemblock_be.Entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProposalTargetId implements Serializable {
    private Long proposal;
    private Long proposalBlock;
}