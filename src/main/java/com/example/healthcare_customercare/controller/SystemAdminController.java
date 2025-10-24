package com.example.healthcare_customercare.controller;

import com.example.healthcare_customercare.entity.Role;
import com.example.healthcare_customercare.entity.User;
import com.example.healthcare_customercare.service.RoleService;
import com.example.healthcare_customercare.service.SystemAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/system-admin")
public class SystemAdminController {

    @Autowired
    private SystemAdminService systemAdminService;
    
    @Autowired
    private RoleService roleService;

    // Display the system admin dashboard
    @GetMapping("/dashboard")
    public String showDashboard(@RequestParam String email, Model model) {
        try {
            User admin = systemAdminService.findUserByEmail(email);
            if (admin != null && admin.isAdmin()) {
                model.addAttribute("admin", admin);
                return "System-Admin/system-admin";
            } else {
                return "redirect:/login?error=access_denied";
            }
        } catch (Exception e) {
            return "redirect:/login?error=user_not_found";
        }
    }

    // Edit user page
    @GetMapping("/edit-user/{email}")
    public String editUser(@PathVariable String email, @RequestParam String adminEmail, Model model) {
        try {
            // Verify admin access
            User admin = systemAdminService.findUserByEmail(adminEmail);
            if (admin == null || !admin.isAdmin()) {
                return "redirect:/login?error=access_denied";
            }
            
            // Check if the user exists
            User user = systemAdminService.findUserByEmail(email);
            if (user == null) {
                return "redirect:/system-admin/dashboard?email=" + adminEmail + "&error=user_not_found";
            }
            
            model.addAttribute("userEmail", email);
            model.addAttribute("adminEmail", adminEmail);
            return "System-Admin/edit-user";
        } catch (Exception e) {
            return "redirect:/system-admin/dashboard?email=" + adminEmail + "&error=edit_error";
        }
    }

    // API endpoint to get all users
    @GetMapping("/api/admin/users")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> users = systemAdminService.getAllUsers();
            response.put("success", true);
            response.put("users", users);
            response.put("count", users.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving users: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // API endpoint to get user statistics
    @GetMapping("/api/admin/user-statistics")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUserStatistics() {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Long> stats = systemAdminService.getUserStatistics();
            response.put("success", true);
            response.put("totalUsers", stats.get("totalUsers"));
            response.put("activeUsers", stats.get("activeUsers"));
            response.put("staffMembers", stats.get("staffMembers"));
            response.put("customers", stats.get("customers"));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving user statistics: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // API endpoint to create a new user
    @PostMapping("/api/admin/users")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody Map<String, String> userData) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate required fields
            if (userData.get("firstName") == null || userData.get("firstName").trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "First name is required");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (userData.get("lastName") == null || userData.get("lastName").trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Last name is required");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (userData.get("email") == null || userData.get("email").trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Email is required");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (userData.get("password") == null || userData.get("password").trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Password is required");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (userData.get("role") == null || userData.get("role").trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Role is required");
                return ResponseEntity.badRequest().body(response);
            }

            // Check if user already exists
            User existingUser = systemAdminService.findUserByEmail(userData.get("email"));
            if (existingUser != null) {
                response.put("success", false);
                response.put("message", "User with this email already exists");
                return ResponseEntity.badRequest().body(response);
            }

            // Create new user
            User newUser = new User(
                userData.get("firstName"),
                userData.get("lastName"),
                userData.get("email"),
                userData.get("phoneNumber"),
                userData.get("password"),
                userData.get("role")
            );

            User createdUser = systemAdminService.createUser(newUser);
            response.put("success", true);
            response.put("message", "User created successfully");
            response.put("user", createdUser);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating user: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // API endpoint to get a specific user
    @GetMapping("/api/admin/users/{email}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = systemAdminService.findUserByEmail(email);
            if (user != null) {
                response.put("success", true);
                response.put("user", user);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving user: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // API endpoint to update a user
    @PutMapping("/api/admin/users/{email}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable String email, @RequestBody Map<String, String> userData) {
        Map<String, Object> response = new HashMap<>();
        try {
            User existingUser = systemAdminService.findUserByEmail(email);
            if (existingUser == null) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.notFound().build();
            }

            // Update user fields
            if (userData.get("firstName") != null) {
                existingUser.setFirstName(userData.get("firstName"));
            }
            if (userData.get("lastName") != null) {
                existingUser.setLastName(userData.get("lastName"));
            }
            if (userData.get("phoneNumber") != null) {
                existingUser.setPhoneNumber(userData.get("phoneNumber"));
            }
            if (userData.get("password") != null && !userData.get("password").trim().isEmpty()) {
                existingUser.setPassword(userData.get("password"));
            }
            if (userData.get("role") != null) {
                existingUser.setRole(userData.get("role"));
            }

            User updatedUser = systemAdminService.updateUser(existingUser);
            response.put("success", true);
            response.put("message", "User updated successfully");
            response.put("user", updatedUser);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating user: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // API endpoint to delete a user
    @DeleteMapping("/api/admin/users/{email}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            User existingUser = systemAdminService.findUserByEmail(email);
            if (existingUser == null) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.notFound().build();
            }

            // Prevent deletion of admin users
            if (existingUser.isAdmin()) {
                response.put("success", false);
                response.put("message", "Cannot delete admin users");
                return ResponseEntity.badRequest().body(response);
            }

            boolean deleted = systemAdminService.deleteUser(email);
            if (deleted) {
                response.put("success", true);
                response.put("message", "User deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to delete user");
                return ResponseEntity.status(500).body(response);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting user: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // API endpoint to get users by role
    @GetMapping("/api/admin/users/by-role/{role}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUsersByRole(@PathVariable String role) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> users = systemAdminService.getUsersByRole(role);
            response.put("success", true);
            response.put("users", users);
            response.put("count", users.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving users by role: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // API endpoint to search users
    @GetMapping("/api/admin/users/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchUsers(@RequestParam String query) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> users = systemAdminService.searchUsers(query);
            response.put("success", true);
            response.put("users", users);
            response.put("count", users.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error searching users: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // Test endpoint to verify admin login
    @GetMapping("/test-admin-login")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> testAdminLogin(@RequestParam String email, @RequestParam String password) {
        Map<String, Object> response = new HashMap<>();
        try {
            User admin = systemAdminService.findUserByEmail(email);
            if (admin != null && admin.isAdmin()) {
                response.put("success", true);
                response.put("message", "Admin user found");
                response.put("user", Map.of(
                    "email", admin.getEmail(),
                    "fullName", admin.getFullName(),
                    "role", admin.getRole()
                ));
            } else {
                response.put("success", false);
                response.put("message", "Admin user not found or not admin role");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // ========== ROLE MANAGEMENT ENDPOINTS ==========

    // API endpoint to get all roles
    @GetMapping("/api/admin/roles")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAllRoles() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Role> roles = roleService.getAllRoles();
            response.put("success", true);
            response.put("roles", roles);
            response.put("count", roles.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving roles: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // API endpoint to get all roles (including inactive)
    @GetMapping("/api/admin/roles/all")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAllRolesIncludingInactive() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Role> roles = roleService.getAllRoles();
            response.put("success", true);
            response.put("roles", roles);
            response.put("count", roles.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving roles: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // API endpoint to get a specific role by ID
    @GetMapping("/api/admin/roles/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getRole(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Role> role = roleService.getRoleById(id);
            if (role.isPresent()) {
                response.put("success", true);
                response.put("role", role.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Role not found");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving role: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // API endpoint to create a new role
    @PostMapping("/api/admin/roles")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createRole(@RequestBody Map<String, String> roleData) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate required fields
            if (roleData.get("roleName") == null || roleData.get("roleName").trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Role name is required");
                return ResponseEntity.badRequest().body(response);
            }

            // Check if role name already exists
            if (roleService.roleNameExists(roleData.get("roleName"))) {
                response.put("success", false);
                response.put("message", "Role with this name already exists");
                return ResponseEntity.badRequest().body(response);
            }

            // Create new role
            Role newRole = new Role(roleData.get("roleName"));

            Role createdRole = roleService.createRole(newRole);
            response.put("success", true);
            response.put("message", "Role created successfully");
            response.put("role", createdRole);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating role: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // API endpoint to update a role
    @PutMapping("/api/admin/roles/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateRole(@PathVariable Long id, @RequestBody Map<String, String> roleData) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Role> existingRoleOpt = roleService.getRoleById(id);
            if (existingRoleOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Role not found");
                return ResponseEntity.notFound().build();
            }

            Role existingRole = existingRoleOpt.get();
            
            // Check if role name already exists (excluding current role)
            if (!existingRole.getRoleName().equals(roleData.get("roleName")) && 
                roleService.roleNameExistsExcluding(roleData.get("roleName"), id)) {
                response.put("success", false);
                response.put("message", "Role with this name already exists");
                return ResponseEntity.badRequest().body(response);
            }

            // Update role name
            if (roleData.get("roleName") != null) {
                existingRole.setRoleName(roleData.get("roleName"));
            }

            Role updatedRole = roleService.updateRole(id, existingRole);
            response.put("success", true);
            response.put("message", "Role updated successfully");
            response.put("role", updatedRole);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating role: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // API endpoint to delete a role (soft delete)
    @DeleteMapping("/api/admin/roles/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteRole(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Role> existingRole = roleService.getRoleById(id);
            if (existingRole.isEmpty()) {
                response.put("success", false);
                response.put("message", "Role not found");
                return ResponseEntity.notFound().build();
            }

            boolean deleted = roleService.deleteRole(id);
            if (deleted) {
                response.put("success", true);
                response.put("message", "Role deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to delete role");
                return ResponseEntity.status(500).body(response);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting role: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // API endpoint to search roles
    @GetMapping("/api/admin/roles/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchRoles(@RequestParam String query) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Role> roles = roleService.searchRolesByName(query);
            response.put("success", true);
            response.put("roles", roles);
            response.put("count", roles.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error searching roles: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // API endpoint to get role statistics
    @GetMapping("/api/admin/role-statistics")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getRoleStatistics() {
        Map<String, Object> response = new HashMap<>();
        try {
            long totalRolesCount = roleService.countRoles();
            
            response.put("success", true);
            response.put("totalRoles", totalRolesCount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving role statistics: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
