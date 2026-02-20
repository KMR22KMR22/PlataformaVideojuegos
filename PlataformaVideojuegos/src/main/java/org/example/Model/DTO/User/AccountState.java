package org.example.Model.DTO.User;

public enum AccountState {

    ACTIVE("active"),
    SUSPENDED("suspended"),
    BANNED("banned");

    private final String value;

    AccountState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
