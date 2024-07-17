package com.example.startingclubbackend.service.role;

import com.example.startingclubbackend.model.Role;

import javax.management.relation.RoleInfoNotFoundException;

public interface RoleService {
    Role fetchRoleByName(final String roleName);
}
