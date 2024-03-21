package com.rollcall.server.dto;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {

    private UUID id;

    @NotEmpty(message = "Username cannot be null")
    private String userName;

    @NotEmpty(message = "Name cannot be null")
    private String name;

    @NotEmpty(message = "Password cannot be null")
    @Size(min = 6, max = 20, message = "Password must be between 6 to 20")
    private String password;

    // @Pattern(regexp = "\'^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$\'gm")
    // @NotEmpty(message = "Email cannot be null")
    @Email(message = "Invalid email")
    private String email;

    // @Size(min = 7)
    private long phone;

    @NotEmpty(message = "Profession cannot be null")
    private String profession;

    @NotEmpty(message = "Date of birth cannot be null")
    private String dob;
}
