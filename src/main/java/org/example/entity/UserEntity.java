package org.example.entity;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    private String id;
    private String name;
    private String email;
    private String password;
    private String role;
    private String address;


}
