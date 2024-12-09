package fr.parisnanterre.iqplay.entity;

import fr.parisnanterre.iqplay.entity.api.IUser;
import fr.parisnanterre.iqplaylib.api.IPlayer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.*;

@Entity
public class Player implements IPlayer, IUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    public Player() { }

    public Player(String email, String username) {
        this.email = email;
        this.username = username;
    }

    @Override
    public Long id() { return this.id; }

    @Override
    public String email() { return email; }

    @Override
    public void email(String email) { this.email = email; }

    @Override
    public String username() { return username; }

    @Override
    public void username(String username) { this.username = username; }

    @Override
    public String password() { return password; }

    @Override
    public void password(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}

