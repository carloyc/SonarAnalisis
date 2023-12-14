package mp.sitili.modules.jwt.entities;

public class JwtRegister {
    private String email;
    private String password;
    private int role;

    public JwtRegister(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public JwtRegister() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
