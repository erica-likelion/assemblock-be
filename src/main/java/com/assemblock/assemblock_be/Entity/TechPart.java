package com.assemblock.assemblock_be.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Tech_parts")
public class TechPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tech_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tech_name", nullable = false, unique = true)
    private TechName techName;

    @OneToMany(mappedBy = "techPart")
    private List<Block> blocks = new ArrayList<>();

    public enum TechName {
        Design, FrontEnd, BackEnd
    }
}