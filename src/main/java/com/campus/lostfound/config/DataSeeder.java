package com.campus.lostfound.config;

import com.campus.lostfound.model.Item;
import com.campus.lostfound.model.User;
import com.campus.lostfound.repository.ItemRepository;
import com.campus.lostfound.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create admin user
        if (!userRepository.existsByEmail("admin@campus.edu")) {
            User admin = User.builder()
                    .fullName("Campus Admin")
                    .email("admin@campus.edu")
                    .password(passwordEncoder.encode("admin123"))
                    .role(User.Role.ADMIN)
                    .isActive(true)
                    .studentId("ADMIN001")
                    .phoneNumber("9999999999")
                    .build();
            userRepository.save(admin);
        }

        // Create sample student
        if (!userRepository.existsByEmail("student@campus.edu")) {
            User student = User.builder()
                    .fullName("Arjun Sharma")
                    .email("student@campus.edu")
                    .password(passwordEncoder.encode("student123"))
                    .role(User.Role.USER)
                    .isActive(true)
                    .studentId("CS2021001")
                    .phoneNumber("9876543210")
                    .build();
            userRepository.save(student);

            User user2 = User.builder()
                    .fullName("Priya Reddy")
                    .email("priya@campus.edu")
                    .password(passwordEncoder.encode("student123"))
                    .role(User.Role.USER)
                    .isActive(true)
                    .studentId("EC2022042")
                    .phoneNumber("8765432109")
                    .build();
            userRepository.save(user2);

            // Seed sample items
            User s = userRepository.findByEmail("student@campus.edu").get();
            User p = userRepository.findByEmail("priya@campus.edu").get();

            itemRepository.save(Item.builder()
                    .title("Black Samsung Galaxy Earbuds")
                    .description("Lost my Samsung Galaxy Buds Pro near the library. They are in a black case with my name sticker on them.")
                    .type(Item.ItemType.LOST)
                    .category(Item.Category.ELECTRONICS)
                    .location("Main Library, 2nd Floor")
                    .status(Item.ItemStatus.ACTIVE)
                    .dateLostOrFound(LocalDateTime.now().minusDays(2))
                    .contactInfo("arjun@campus.edu / 9876543210")
                    .reportedBy(s)
                    .build());

            itemRepository.save(Item.builder()
                    .title("Blue Jansport Backpack")
                    .description("Found a blue backpack near the cafeteria. Contains some books and a water bottle.")
                    .type(Item.ItemType.FOUND)
                    .category(Item.Category.BAGS)
                    .location("Main Cafeteria Entrance")
                    .status(Item.ItemStatus.ACTIVE)
                    .dateLostOrFound(LocalDateTime.now().minusDays(1))
                    .contactInfo("priya@campus.edu")
                    .reportedBy(p)
                    .build());

            itemRepository.save(Item.builder()
                    .title("Student ID Card - Rohit Verma")
                    .description("Found a student ID card on the ground near Block B entrance.")
                    .type(Item.ItemType.FOUND)
                    .category(Item.Category.DOCUMENTS)
                    .location("Academic Block B, Ground Floor")
                    .status(Item.ItemStatus.ACTIVE)
                    .dateLostOrFound(LocalDateTime.now().minusHours(5))
                    .contactInfo("security@campus.edu")
                    .reportedBy(p)
                    .build());

            itemRepository.save(Item.builder()
                    .title("Silver Apple Watch Series 8")
                    .description("Lost my Apple Watch at the sports ground during football practice.")
                    .type(Item.ItemType.LOST)
                    .category(Item.Category.ACCESSORIES)
                    .location("Sports Ground / Football Field")
                    .status(Item.ItemStatus.ACTIVE)
                    .dateLostOrFound(LocalDateTime.now().minusDays(3))
                    .contactInfo("arjun@campus.edu")
                    .reportedBy(s)
                    .build());

            itemRepository.save(Item.builder()
                    .title("Data Structures Textbook")
                    .description("Found a textbook 'Introduction to Algorithms' with some handwritten notes inside.")
                    .type(Item.ItemType.FOUND)
                    .category(Item.Category.BOOKS)
                    .location("Reading Room, Table 12")
                    .status(Item.ItemStatus.ACTIVE)
                    .dateLostOrFound(LocalDateTime.now().minusHours(8))
                    .contactInfo("priya@campus.edu")
                    .reportedBy(p)
                    .build());
        }

        System.out.println("✅ Data seeded successfully!");
        System.out.println("👤 Admin: admin@campus.edu / admin123");
        System.out.println("👤 Student: student@campus.edu / student123");
    }
}
