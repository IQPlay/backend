package fr.parisnanterre.iqplay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.parisnanterre.iqplay.entity.api.IUser;

public class PlayerRegistrationRequestDto implements IUser {

    @JsonProperty
    private String email;

    @JsonProperty
    private String username;

    @JsonProperty
    private String password;

    // No-args constructor (mandatory for Spring)
    public PlayerRegistrationRequestDto() {}

    @Override
    public String email() {
        return email;
    }

    @Override
    public void email(String email) { this.email = email; }

    @Override
    public String username() { return username; }

    @Override
    public void username(String username) { this.username = username; }

    @Override
    public String password() { return password; }

    @Override
    public void password(String password) { this.password = password; }

}
