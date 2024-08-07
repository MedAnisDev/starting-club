package com.example.startingclubbackend.service.role;

import com.example.startingclubbackend.exceptions.custom.ResourceNotFoundException;
import com.example.startingclubbackend.model.role.Role;
import com.example.startingclubbackend.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository ;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role fetchRoleByName(String roleName) {
        return roleRepository.fetchRoleByName(roleName)
                .orElseThrow(() ->  new ResourceNotFoundException("The role could not be found."));
    }
}
