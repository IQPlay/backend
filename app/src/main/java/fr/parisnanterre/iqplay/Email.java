package fr.parisnanterre.iqplay;

public record Email(String email) {
    public Email {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }
}

