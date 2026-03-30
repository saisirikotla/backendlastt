package com.campus.lostfound.dto;

import com.campus.lostfound.model.Item;
import com.campus.lostfound.model.User;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

public class DTOs {

    // Auth DTOs
    @Data
    public static class RegisterRequest {
        private String fullName;
        private String email;
        private String password;
        private String phoneNumber;
        private String studentId;
    }

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthResponse {
        private String token;
        private String email;
        private String fullName;
        private String role;
        private Long userId;
    }

    // Item DTOs
    @Data
    public static class ItemRequest {
        private String title;
        private String description;
        private Item.ItemType type;
        private Item.Category category;
        private String location;
        private String imageUrl;
        private LocalDateTime dateLostOrFound;
        private String contactInfo;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemResponse {
        private Long id;
        private String title;
        private String description;
        private Item.ItemType type;
        private Item.ItemStatus status;
        private Item.Category category;
        private String location;
        private String imageUrl;
        private LocalDateTime dateLostOrFound;
        private LocalDateTime createdAt;
        private String contactInfo;
        private String reportedByName;
        private String reportedByEmail;
        private Long reportedById;
        private String claimedByName;
        private String claimedByEmail;
        private String claimDescription;
        private LocalDateTime claimedAt;
        private String foundByName;
        private String foundByEmail;
        private String foundLocation;
        private String foundNotes;
        private LocalDateTime foundReportedAt;
    }

    @Data
    public static class FoundReportRequest {
        private String foundLocation;
        private String foundNotes;
    }

    @Data
    public static class ClaimRequest {
        private String claimDescription;
    }

    // Stats DTO
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DashboardStats {
        private long totalItems;
        private long lostItems;
        private long foundItems;
        private long resolvedItems;
        private long totalUsers;
        private long activeItems;
        private long claimedItems;
    }

    // User response DTO
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResponse {
        private Long id;
        private String fullName;
        private String email;
        private String phoneNumber;
        private String studentId;
        private String role;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private int itemsReported;
    }
}
