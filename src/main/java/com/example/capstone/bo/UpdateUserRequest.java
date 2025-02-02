package com.example.capstone.bo;
import java.time.LocalDate;

public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String bankAccountUsername;
    private String subscription;
    private String profilePic;
    private String gender;
    private LocalDate dateOfBirth;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getBankAccountUsername() { return bankAccountUsername; }
    public void setBankAccountUsername(String bankAccountUsername) { this.bankAccountUsername = bankAccountUsername; }

    public String getSubscription() { return subscription; }
    public void setSubscription(String subscription) { this.subscription = subscription; }

    public String getProfilePic() { return profilePic; }
    public void setProfilePic(String profilePic) { this.profilePic = profilePic; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
}
