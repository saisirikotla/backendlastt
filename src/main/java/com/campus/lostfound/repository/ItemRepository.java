package com.campus.lostfound.repository;

import com.campus.lostfound.model.Item;
import com.campus.lostfound.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByType(Item.ItemType type);
    List<Item> findByStatus(Item.ItemStatus status);
    List<Item> findByTypeAndStatus(Item.ItemType type, Item.ItemStatus status);
    List<Item> findByReportedBy(User user);
    List<Item> findByCategory(Item.Category category);
    long countByType(Item.ItemType type);
    long countByStatus(Item.ItemStatus status);

    @Query("SELECT i FROM Item i WHERE " +
           "(LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.location) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Item> searchByKeyword(String keyword);

    @Query("SELECT i FROM Item i WHERE i.type = :type AND i.status = 'ACTIVE' ORDER BY i.createdAt DESC")
    List<Item> findActiveByType(Item.ItemType type);
}
