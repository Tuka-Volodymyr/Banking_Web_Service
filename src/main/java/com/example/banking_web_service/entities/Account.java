package com.example.banking_web_service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String fullName;
    @Email
    @Pattern(regexp = ".+\\.com")
    private String email;
    @Size(min = 4)
    private String password;
    @JsonIgnore
    private boolean lockStatus;
    @JsonIgnore
    private int failedAttempts;
    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles=new ArrayList<>();
}
