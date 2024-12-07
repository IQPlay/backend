package fr.parisnanterre.iqplay.entity.api;

public interface IUser {

    String email();
    String username();
    String password();

    void password(String password);
    void username(String username);
    void email(String email);
}
