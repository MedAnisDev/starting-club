package com.example.startingclubbackend.service.role;

import com.example.startingclubbackend.model.role.Role;

public interface RoleService {
    Role fetchRoleByName(final String roleName);
}
