package org.programmers.cocktail.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
public class TotalHitsLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long totalHits;

    @Column(nullable = false, unique = true)
    private LocalDateTime recordedAt;

    public TotalHitsLog(Long totalHits, LocalDateTime recordedAt) {
        this.totalHits = totalHits;
        this.recordedAt = recordedAt;
    }

    protected TotalHitsLog() {}

}
