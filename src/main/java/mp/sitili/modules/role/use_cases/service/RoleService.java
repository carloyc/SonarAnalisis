package mp.sitili.modules.role.use_cases.service;

import mp.sitili.modules.role.entities.Role;
import mp.sitili.modules.role.use_cases.methods.RoleRepository;
import mp.sitili.modules.role.use_cases.repository.IRoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements IRoleRepository {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role createNewRole(Role role) {
        return roleRepository.save(role);
    }
}
