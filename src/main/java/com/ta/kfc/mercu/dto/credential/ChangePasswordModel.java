package com.ta.kfc.mercu.dto.credential;

public class ChangePasswordModel {
    private String newPassword;
    private String passwordConfirmation;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public boolean isValid() {
        return !newPassword.isEmpty() && newPassword.equals(passwordConfirmation);
    }

}
