package org.example.Model.DTO.User;

public record UserProfileDTO(Long id, String userName, String avatar, String country, String registrationDate) {

    @Override
    public String toString() {
        return "UserProfileDTO{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", country='" + country + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                '}';
    }
}


