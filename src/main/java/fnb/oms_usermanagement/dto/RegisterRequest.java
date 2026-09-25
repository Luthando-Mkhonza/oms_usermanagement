package fnb.oms_usermanagement.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String firstName;
    private String surname;
    private String email;
    private String password;
}
