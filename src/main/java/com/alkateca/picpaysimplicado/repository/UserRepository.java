package com.alkateca.picpaysimplicado.repository;

import com.alkateca.picpaysimplicado.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
