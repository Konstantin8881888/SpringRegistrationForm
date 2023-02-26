package com.example.auth.bean;

import com.example.auth.entity.AppUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse extends BaseResponse
{
    private final String name;
    private final String email;
    private final boolean enabled;

    public UserResponse(AppUser appUser)
    {
        this.name = appUser.getUsername();
        this.email = appUser.getEmail();
        this.enabled = appUser.isEnabled();
    }
}
