package mp.sitili.modules.user.use_cases.service;

import mp.sitili.modules.data_user.use_cases.methods.DataUserRepository;
import mp.sitili.modules.resetPassword.use_cases.service.PasswordResetTokenService;
import mp.sitili.modules.role.entities.Role;
import mp.sitili.modules.role.use_cases.methods.RoleRepository;
import mp.sitili.modules.user.entities.User;
import mp.sitili.modules.user.use_cases.dto.SelectVendedorDTO;
import mp.sitili.modules.user.use_cases.dto.ValidSellerDTO;
import mp.sitili.modules.user.use_cases.methods.UserRepository;
import mp.sitili.modules.user.use_cases.repository.IUserRepository;
import mp.sitili.utils.email.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class UserService implements IUserRepository {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final DataUserRepository dataUserRepository;

    private final PasswordResetTokenService tokenService;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, EmailService emailService, DataUserRepository dataUserRepository, PasswordResetTokenService tokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.dataUserRepository = dataUserRepository;
        this.tokenService = tokenService;
    }

    public void initRoleAndUser() {

        // Crear roles
        Role rootRole = new Role();
        rootRole.setRoleName("Root");
        rootRole.setRoleDescription("Root role");
        roleRepository.save(rootRole);

        Role adminRole = new Role();
        adminRole.setRoleName("Admin");
        adminRole.setRoleDescription("Admin role");
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("User");
        userRole.setRoleDescription("User role");
        roleRepository.save(userRole);

        Role vendedorRole = new Role();
        vendedorRole.setRoleName("Seller");
        vendedorRole.setRoleDescription("Seller role");
        roleRepository.save(vendedorRole);

        User rootUser = new User();
        rootUser.setEmail("root@root");
        rootUser.setPassword(getEncodedPassword("root"));
        rootUser.setStatus(true);
        Set<Role> rootRoles = new HashSet<>();
        rootRoles.add(adminRole);
        rootUser.setRole(rootRoles);
        userRepository.save(rootUser);

        User adminUser = new User();
        adminUser.setEmail("admin@admin");
        adminUser.setPassword(getEncodedPassword("root"));
        adminUser.setStatus(true);
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        userRepository.save(adminUser);

        User userUser = new User();
        userUser.setEmail("user@user");
        userUser.setPassword(getEncodedPassword("root"));
        userUser.setStatus(true);
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        userUser.setRole(userRoles);
        userRepository.save(userUser);

        User vendedorUser = new User();
        vendedorUser.setEmail("vendedor@vendedor");
        vendedorUser.setPassword(getEncodedPassword("root"));
        vendedorUser.setStatus(true);
        Set<Role> vendedorRoles = new HashSet<>();
        vendedorRoles.add(vendedorRole);
        vendedorUser.setRole(vendedorRoles);
        userRepository.save(vendedorUser);

        User vendedorUser1 = new User();
        vendedorUser1.setEmail("vend@vend");
        vendedorUser1.setPassword(getEncodedPassword("root"));
        vendedorUser1.setStatus(true);
        Set<Role> vendedorRoles1 = new HashSet<>();
        vendedorRoles1.add(vendedorRole);
        vendedorUser1.setRole(vendedorRoles1);
        userRepository.save(vendedorUser1);

        // Asociar datos a usuarios
        dataUserRepository.asociarUserData("root@root", "Carlo", "Yael");
        dataUserRepository.asociarUserData("admin@admin", "Daniel", "Wong");
        dataUserRepository.asociarUserData("user@user", "Diego", "Garcia");
        dataUserRepository.asociarUserData("vendedor@vendedor", "Hector", "Marquina");
        dataUserRepository.asociarUserData("vend@vend", "wongsito", "wongsito");

}

    public User registerNewUser(String email, String password, String first_name, String last_name, Integer rol) {
        Role role;
        Set<Role> userRoles = new HashSet<>();
        User usuario = null;
        User user = null;
        Optional<Role> optionalRole;

        switch (rol) {
            case 1:
                optionalRole = roleRepository.findById("Root");
                if (optionalRole.isPresent()){
                    role = optionalRole.get();
                    userRoles.add(role);
                    usuario = new User(email,
                            passwordEncoder.encode(password),
                            true,
                            userRoles);
                }
                break;
            case 2:
                optionalRole = roleRepository.findById("Admin");
                if (optionalRole.isPresent()){
                    role = optionalRole.get();
                    userRoles.add(role);
                    usuario = new User(email,
                            passwordEncoder.encode(password),
                            true,
                            userRoles);
                }
                break;
            case 3:
                optionalRole = roleRepository.findById("Seller");
                if (optionalRole.isPresent()){
                    role = optionalRole.get();
                    userRoles.add(role);
                    usuario = new User(email,
                            passwordEncoder.encode(password),
                            false,
                            userRoles);
                }
                break;
            case 4:
                optionalRole = roleRepository.findById("User");
                if (optionalRole.isPresent()){
                    role = optionalRole.get();
                    userRoles.add(role);
                    usuario = new User(email,
                            passwordEncoder.encode(password),
                            true,
                            userRoles);
                }
                break;
            default:

        }

        Optional<User> validador = userRepository.findById(email);

        if(validador.isEmpty()){
            assert usuario != null;
            user = userRepository.save(usuario);
            switch (rol){
                case 1:
                    sendEmail(user.getEmail(), "Bienvenido Super Admin", "Te registraron como SuperAdmin de SITILI");
                    break;
                case 2:
                    sendEmail(user.getEmail(),  "Bienvenido Admin", "Te registraron como administrador de SITILI");
                    break;
                case 3:
                    sendEmail(user.getEmail(), "Bienvenido Seller", "Te registraste como vendedor de SITILI, espera a que un administrador te acepte en la plataforma");
                    break;
                case 4:
                    sendEmail(user.getEmail(), "Bienvenido User", "Te registraste como cliente de SITILI, podras ver y adquirir diversos productos en nuestra plataforma");
                    break;
                default:
            }
            dataUserRepository.asociarUserData(user.getEmail(), first_name, last_name);
        }else{
            return user;
        }

        return user;

    }

    @Override
    public String sendEmail(String username, String title, String bob) {
        return emailService.sendMail(username, title, bob);
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public List<SelectVendedorDTO> findSellers(){
        return userRepository.findSellers();
    }


    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenService.deleteToken(user);
        emailService.sendPasswordChangeConfirmation(user.getEmail());
    }
  
    @Override
    public ValidSellerDTO validateCompany(String user_id){
        return userRepository.validateCompany(user_id);
    }

}