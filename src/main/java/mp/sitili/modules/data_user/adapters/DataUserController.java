package mp.sitili.modules.data_user.adapters;

import mp.sitili.modules.data_user.entities.DataUser;
import mp.sitili.modules.data_user.use_cases.dto.DataUdto;
import mp.sitili.modules.data_user.use_cases.dto.DataUserDTO;
import mp.sitili.modules.data_user.use_cases.dto.UsuariosxMesDTO;
import mp.sitili.modules.data_user.use_cases.methods.DataUserRepository;
import mp.sitili.modules.data_user.use_cases.service.DataUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dataUser")
public class DataUserController {

    private final DataUserRepository dataUserRepository;

    private final DataUserService dataUserService;

    public DataUserController(DataUserRepository dataUserRepository, DataUserService dataUserService) {
        this.dataUserRepository = dataUserRepository;
        this.dataUserService = dataUserService;
    }


    @GetMapping("/list")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<DataUserDTO>> datosUsuarios() {
        List<DataUserDTO> usuarios = dataUserRepository.findAllDataUsers();

        if(usuarios != null){
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(usuarios, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listu")
    @PreAuthorize("hasRole('Admin') or hasRole('Seller') or hasRole('User')")
    public ResponseEntity<DataUserDTO> datosPorUsuario() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        DataUserDTO usuarios = dataUserRepository.findAllDataUser(userEmail);

        if(usuarios != null){
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(usuarios, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> actualizarPorUsuario(@RequestBody DataUdto dataUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        DataUserDTO usuarios = dataUserRepository.findAllDataUser(userEmail);

        boolean revision = dataUserService.update("Sin Compañia", dataUser.getFirstName(),
                dataUser.getLastName(), dataUser.getPhone(),
                usuarios.getId(), userEmail);

        if(usuarios.getId() != null && usuarios.getId() != 0){
            if(revision){
                return new ResponseEntity<>("Usuario actualizado", HttpStatus.OK);
            }else {
                return new ResponseEntity<>("Error al actualizar datos", HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>("No existen datos de ususario", HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/updateSeller")
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<String> actualizarPorSeller(@RequestBody DataUdto dataUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        DataUserDTO usuarios = dataUserRepository.findAllDataUser(userEmail);

        boolean revision = dataUserService.update(dataUser.getCompany(), dataUser.getFirstName(),
                dataUser.getLastName(), dataUser.getPhone(),
                usuarios.getId(), userEmail);

        if(usuarios.getId() != null && usuarios.getId() != 0){
            if(revision){
                return new ResponseEntity<>("Vendedor actualizado", HttpStatus.OK);
            }else {
                return new ResponseEntity<>("Error al actualizar datos", HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>("No existen datos de vendedor", HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/updateCompany")
    @PreAuthorize("hasRole('Admin') or hasRole('Seller')")
    public ResponseEntity<String> actualizarCompany(@RequestBody DataUdto dataUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        boolean revision = dataUserService.setCompany(userEmail, dataUser.getCompany(), dataUser.getPhone());

        if(revision){
            return new ResponseEntity<>("Compañia asociar correctamente", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Error al asociar compañia", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/usuTot")
    @PreAuthorize("hasRole('Root') or hasRole('Admin')")
    public ResponseEntity<List<UsuariosxMesDTO>> usuariosPorMes() {
        List<UsuariosxMesDTO> usuarios = dataUserService.usuXmes();

        if(usuarios != null){
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(usuarios, HttpStatus.NO_CONTENT);
        }
    }

}
