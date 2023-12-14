package mp.sitili.modules.jwt.adapters;

import mp.sitili.modules.jwt.entities.JwtRequest;
import mp.sitili.modules.jwt.entities.JwtResponse;
import mp.sitili.modules.jwt.use_cases.service.JwtService;
import mp.sitili.modules.user.entities.User;
import mp.sitili.modules.user.use_cases.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class JwtController {

    private final JwtService jwtService;

    private final UserService userService;

    public JwtController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping({"/authenticate"})
    public ResponseEntity<JwtResponse> createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        JwtResponse validador = jwtService.createJwtToken(jwtRequest);
        if(validador.getUser().getStatus()){
            return new ResponseEntity<>(validador, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    @PostMapping({"/registerNewUser"})
    public ResponseEntity<JwtResponse> registerNewUser(@RequestBody Map<String, Object> productData) throws Exception {
        if(!productData.isEmpty()){
            String email = (String) productData.get("email");
            String password = (String) productData.get("password");
            String first_name = (String) productData.get("first_name");
            String last_name = (String) productData.get("last_name");
            Integer role = (Integer) productData.get("role");

            User user = userService.registerNewUser(email, password, first_name, last_name, role);
            if(user != null){
                JwtResponse validador = jwtService.createJwtToken(new JwtRequest(email, password));
                if(validador != null){
                    return new ResponseEntity<>(validador, HttpStatus.OK);
                }else{
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            }else{
                return new ResponseEntity<>(HttpStatus.SEE_OTHER);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}
