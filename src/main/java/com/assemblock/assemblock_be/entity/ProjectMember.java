package com.assemblock.assemblock_be.entity;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "project_members")
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "proposal_id", nullable = false)
    private Proposal proposal;

    @ManyToOne
    @JoinColumn(name = "proposer_id", nullable = false)
    private User proposer;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 실제 팀원

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", nullable = false)
    private MemberRole memberRole;

    @Column(name = "is_proposer", nullable = false)
    private boolean isProposer = false;
}
