package com.campus.lostfound.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private ItemType type;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String location;

    @Column(length = 5000000)
    private String imageUrl;

    private LocalDateTime dateLostOrFound;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String contactInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by")
    private User reportedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claimed_by")
    private User claimedBy;

    private String claimDescription;

    private LocalDateTime claimedAt;

    // For LOST items that have been found by someone
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "found_by")
    private User foundBy;

    private String foundLocation;

    private String foundNotes;

    private LocalDateTime foundReportedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = ItemStatus.ACTIVE;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum ItemType {
        LOST, FOUND
    }

    public enum ItemStatus {
        ACTIVE, CLAIMED, RESOLVED, REJECTED, FOUND_REPORTED
    }

    public enum Category {
        ELECTRONICS, BOOKS, CLOTHING, ACCESSORIES, DOCUMENTS, KEYS, BAGS, SPORTS, OTHERS
    }
}
