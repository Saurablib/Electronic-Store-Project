package com.lcwd.electronic.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lcwd.electronic.store.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

}
