package com.example.frontmicroservice.constant;

public enum ProblemEnum {

    USERNAME_NOT_AVAILABLE("Username not available"),
    SOMETHING_WENT_WRONG("Something went wrong"),
    ENTER_NAME_AND_PASSWORD("Enter name and password");
    private String description;
    private ProblemEnum(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
