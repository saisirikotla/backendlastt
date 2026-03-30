package com.campus.lostfound.controller;

import com.campus.lostfound.dto.DTOs.*;
import com.campus.lostfound.dto.DTOs.FoundReportRequest;
import com.campus.lostfound.model.Item;
import com.campus.lostfound.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/public")
    public ResponseEntity<List<ItemResponse>> getPublicItems() {
        return ResponseEntity.ok(itemService.getPublicItems());
    }

    @GetMapping("/public/search")
    public ResponseEntity<List<ItemResponse>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(itemService.searchItems(keyword));
    }

    @GetMapping("/public/type/{type}")
    public ResponseEntity<List<ItemResponse>> getByType(@PathVariable Item.ItemType type) {
        return ResponseEntity.ok(itemService.getItemsByType(type));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<ItemResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @PostMapping
    public ResponseEntity<ItemResponse> createItem(
            @RequestBody ItemRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(itemService.createItem(request, userDetails.getUsername()));
    }

    @GetMapping("/my-items")
    public ResponseEntity<List<ItemResponse>> getMyItems(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(itemService.getMyItems(userDetails.getUsername()));
    }

    @PostMapping("/{id}/report-found")
    public ResponseEntity<?> reportFound(
            @PathVariable Long id,
            @RequestBody FoundReportRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(itemService.reportFound(id, request, userDetails.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/claim")
    public ResponseEntity<ItemResponse> claimItem(
            @PathVariable Long id,
            @RequestBody ClaimRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(itemService.claimItem(id, request, userDetails.getUsername()));
    }
}
