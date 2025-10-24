package com.example.healthcare_customercare.service;

import com.example.healthcare_customercare.entity.Role;
import com.example.healthcare_customercare.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    
    @Autowired
    private RoleRepository roleRepository;
    
    // Get all roles
    public List<Role> getAllRoles() {
        return roleRepository.findAllByOrderByRoleNameAsc();
    }
    
    // Get role by ID
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }
    
    // Get role by name
    public Optional<Role> getRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }
    
    // Create new role
    public Role createRole(Role role) {
        // Validate role name uniqueness
        if (roleRepository.existsByRoleName(role.getRoleName())) {
            throw new IllegalArgumentException("Role with name '" + role.getRoleName() + "' already exists");
        }
        
        return roleRepository.save(role);
    }
    
    // Update existing role
    public Role updateRole(Long id, Role updatedRole) {
        Optional<Role> existingRoleOpt = roleRepository.findById(id);
        if (existingRoleOpt.isEmpty()) {
            throw new IllegalArgumentException("Role with ID " + id + " not found");
        }
        
        Role existingRole = existingRoleOpt.get();
        
        // Validate role name uniqueness (excluding current role)
        if (!existingRole.getRoleName().equals(updatedRole.getRoleName()) && 
            roleRepository.existsByRoleNameAndRoleIdNot(updatedRole.getRoleName(), id)) {
            throw new IllegalArgumentException("Role with name '" + updatedRole.getRoleName() + "' already exists");
        }
        
        // Update role name
        existingRole.setRoleName(updatedRole.getRoleName());
        
        return roleRepository.save(existingRole);
    }
    
    // Delete role (hard delete)
    public boolean deleteRole(Long id) {
        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Search roles by name
    public List<Role> searchRolesByName(String roleName) {
        return roleRepository.findByRoleNameContainingIgnoreCase(roleName);
    }
    
    // Count total roles
    public long countRoles() {
        return roleRepository.count();
    }
    
    // Check if role name exists
    public boolean roleNameExists(String roleName) {
        return roleRepository.existsByRoleName(roleName);
    }
    
    // Check if role name exists excluding current role
    public boolean roleNameExistsExcluding(String roleName, Long excludeId) {
        return roleRepository.existsByRoleNameAndRoleIdNot(roleName, excludeId);
    }
}
