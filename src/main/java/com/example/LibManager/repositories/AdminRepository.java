package com.example.LibManager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.LibManager.models.Admin;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, String> {
    Optional<Admin> findByEmail(String email);
}
