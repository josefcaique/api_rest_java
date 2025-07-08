package com.josef.api_rest.integrationtests.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class AccountCredentialsDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String fullname;

    public AccountCredentialsDTO(){}

    public AccountCredentialsDTO(String username, String password, String fullname) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
    }
    private AccountCredentialsDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static AccountCredentialsDTO createAccountCredentialsDTO(String username, String password) {
        return new AccountCredentialsDTO(username, password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountCredentialsDTO that = (AccountCredentialsDTO) o;
        return Objects.equals(username,
                that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }
}
