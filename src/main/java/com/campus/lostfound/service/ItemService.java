package com.campus.lostfound.service;

import com.campus.lostfound.dto.DTOs.*;
import com.campus.lostfound.model.Item;
import com.campus.lostfound.model.User;
import com.campus.lostfound.repository.ItemRepository;
import com.campus.lostfound.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    public ItemResponse createItem(ItemRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Item item = Item.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .type(request.getType())
                .category(request.getCategory())
                .location(request.getLocation())
                .imageUrl(request.getImageUrl())
                .dateLostOrFound(request.getDateLostOrFound())
                .contactInfo(request.getContactInfo())
                .status(Item.ItemStatus.ACTIVE)
                .reportedBy(user)
                .build();

        return toResponse(itemRepository.save(item));
    }

    public List<ItemResponse> getAllItems() {
        return itemRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ItemResponse> getPublicItems() {
        return itemRepository.findAll().stream()
                .filter(i -> i.getStatus() == Item.ItemStatus.ACTIVE)
                .map(this::toResponse).collect(Collectors.toList());
    }

    public List<ItemResponse> getItemsByType(Item.ItemType type) {
        return itemRepository.findActiveByType(type).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ItemResponse> searchItems(String keyword) {
        return itemRepository.searchByKeyword(keyword).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ItemResponse> getMyItems(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return itemRepository.findByReportedBy(user).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ItemResponse getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        return toResponse(item);
    }

    public ItemResponse claimItem(Long itemId, ClaimRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setStatus(Item.ItemStatus.CLAIMED);
        item.setClaimedBy(user);
        item.setClaimDescription(request.getClaimDescription());
        item.setClaimedAt(LocalDateTime.now());

        return toResponse(itemRepository.save(item));
    }

    public ItemResponse updateItemStatus(Long itemId, Item.ItemStatus status) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        item.setStatus(status);
        // If a claim is rejected, clear the claim data so the item
        // goes back to ACTIVE and can be claimed by someone else
        if (status == Item.ItemStatus.ACTIVE) {
            item.setClaimedBy(null);
            item.setClaimDescription(null);
            item.setClaimedAt(null);
        }
        return toResponse(itemRepository.save(item));
    }

    public ItemResponse rejectClaim(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        item.setStatus(Item.ItemStatus.ACTIVE);
        item.setClaimedBy(null);
        item.setClaimDescription(null);
        item.setClaimedAt(null);
        item.setFoundBy(null);
        item.setFoundLocation(null);
        item.setFoundNotes(null);
        item.setFoundReportedAt(null);
        return toResponse(itemRepository.save(item));
    }

    public ItemResponse reportFound(Long itemId, com.campus.lostfound.dto.DTOs.FoundReportRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        if (item.getType() != Item.ItemType.LOST) {
            throw new RuntimeException("This item is not marked as lost");
        }
        if (item.getStatus() != Item.ItemStatus.ACTIVE) {
            throw new RuntimeException("This item is not active");
        }
        item.setStatus(Item.ItemStatus.FOUND_REPORTED);
        item.setFoundBy(user);
        item.setFoundLocation(request.getFoundLocation());
        item.setFoundNotes(request.getFoundNotes());
        item.setFoundReportedAt(java.time.LocalDateTime.now());
        return toResponse(itemRepository.save(item));
    }

    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    public DashboardStats getStats() {
        return DashboardStats.builder()
                .totalItems(itemRepository.count())
                .lostItems(itemRepository.countByType(Item.ItemType.LOST))
                .foundItems(itemRepository.countByType(Item.ItemType.FOUND))
                .resolvedItems(itemRepository.countByStatus(Item.ItemStatus.RESOLVED))
                .totalUsers(userRepository.count())
                .activeItems(itemRepository.countByStatus(Item.ItemStatus.ACTIVE))
                .claimedItems(itemRepository.countByStatus(Item.ItemStatus.CLAIMED))
                .build();
    }

    private ItemResponse toResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .type(item.getType())
                .status(item.getStatus())
                .category(item.getCategory())
                .location(item.getLocation())
                .imageUrl(item.getImageUrl())
                .dateLostOrFound(item.getDateLostOrFound())
                .createdAt(item.getCreatedAt())
                .contactInfo(item.getContactInfo())
                .reportedByName(item.getReportedBy() != null ? item.getReportedBy().getFullName() : null)
                .reportedByEmail(item.getReportedBy() != null ? item.getReportedBy().getEmail() : null)
                .reportedById(item.getReportedBy() != null ? item.getReportedBy().getId() : null)
                .claimedByName(item.getClaimedBy() != null ? item.getClaimedBy().getFullName() : null)
                .claimedByEmail(item.getClaimedBy() != null ? item.getClaimedBy().getEmail() : null)
                .claimDescription(item.getClaimDescription())
                .claimedAt(item.getClaimedAt())
                .foundByName(item.getFoundBy() != null ? item.getFoundBy().getFullName() : null)
                .foundByEmail(item.getFoundBy() != null ? item.getFoundBy().getEmail() : null)
                .foundLocation(item.getFoundLocation())
                .foundNotes(item.getFoundNotes())
                .foundReportedAt(item.getFoundReportedAt())
                .build();
    }
}

    // Kept at bottom — approveClaim just resolves the item
    // (already covered by updateItemStatus RESOLVED, but named explicitly for clarity)
