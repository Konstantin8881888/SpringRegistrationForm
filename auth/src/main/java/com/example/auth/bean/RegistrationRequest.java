package com.example.auth.bean;

import lombok.*;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class RegistrationRequest
{
    private final String name;
    private final String email;
    private final String password;
    private final boolean checked;


}
