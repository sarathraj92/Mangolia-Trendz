package com.mangoliatrendz.library.repository;

import com.mangoliatrendz.library.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {

 Admin findByEmail(String email);
}
