package org.example.model;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String id;
    private String name;
    private String email;
    private String password;
    private String role;
    private String address;


}
