package mp.sitili.modules.role.use_cases.repository;

import mp.sitili.modules.role.entities.Role;

public interface IRoleRepository {
    Role createNewRole(Role role);
}
