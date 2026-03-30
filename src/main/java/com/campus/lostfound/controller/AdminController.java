package com.campus.lostfound.controller;

import com.campus.lostfound.dto.DTOs.*;
import com.campus.lostfound.model.Item;
import com.campus.lostfound.model.User;
import com.campus.lostfound.repository.UserRepository;
import com.campus.lostfound.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard/stats")
    public ResponseEntity<DashboardStats> getStats() {
        return ResponseEntity.ok(itemService.getStats());
    }

    @GetMapping("/items")
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @PatchMapping("/items/{id}/status")
    public ResponseEntity<ItemResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam Item.ItemStatus status) {
        return ResponseEntity.ok(itemService.updateItemStatus(id, status));
    }

    // Approve a claim or found report: marks item as RESOLVED
    @PatchMapping("/items/{id}/approve-claim")
    public ResponseEntity<ItemResponse> approveClaim(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.updateItemStatus(id, Item.ItemStatus.RESOLVED));
    }

    // Reject a claim or found report: resets item back to ACTIVE
    @PatchMapping("/items/{id}/reject-claim")
    public ResponseEntity<ItemResponse> rejectClaim(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.rejectClaim(id));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok("Item deleted");
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> response = users.stream().map(u ->
            UserResponse.builder()
                .id(u.getId())
                .fullName(u.getFullName())
                .email(u.getEmail())
                .phoneNumber(u.getPhoneNumber())
                .studentId(u.getStudentId())
                .role(u.getRole().name())
                .isActive(u.getIsActive())
                .createdAt(u.getCreatedAt())
                .itemsReported(u.getReportedItems() != null ? u.getReportedItems().size() : 0)
                .build()
        ).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/users/{id}/toggle")
    public ResponseEntity<?> toggleUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsActive(!user.getIsActive());
        userRepository.save(user);
        return ResponseEntity.ok("User status updated");
    }
}
