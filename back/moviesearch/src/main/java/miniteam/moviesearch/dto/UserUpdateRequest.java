package miniteam.moviesearch.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String password;
    private String email;
}
