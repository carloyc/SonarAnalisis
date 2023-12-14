package mp.sitili.modules.data_user.entities;

import mp.sitili.modules.user.entities.User;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "data_users")
public class DataUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "company", length = 100)
    private String company;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "phone", length = 100)
    private String phone;

    @Column(name = "register_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date registerDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "email")
    private User user;


    public DataUser() {
    }

    public DataUser(String company, String firstName, String lastName, String phone, User user) {
        this.company = company;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
