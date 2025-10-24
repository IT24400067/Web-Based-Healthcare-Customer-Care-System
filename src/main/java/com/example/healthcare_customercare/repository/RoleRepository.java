package com.example.healthcare_customercare.repository;

import com.example.healthcare_customercare.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    // Find role by name
    Optional<Role> findByRoleName(String roleName);
    
    // Find all roles ordered by name
    List<Role> findAllByOrderByRoleNameAsc();
    
    // Check if role name exists (excluding current role for updates)
    @Query("SELECT COUNT(r) > 0 FROM Role r WHERE r.roleName = :roleName AND r.roleId != :roleId")
    boolean existsByRoleNameAndRoleIdNot(String roleName, Long roleId);
    
    // Check if role name exists
    boolean existsByRoleName(String roleName);
    
    // Find roles by name containing (case insensitive)
    List<Role> findByRoleNameContainingIgnoreCase(String roleName);
    
    // Count total roles
    long count();
}
