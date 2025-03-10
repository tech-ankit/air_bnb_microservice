package com.user.bindings;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponseDto {

    private String userId;

    private String name;

    private String username;

    private String email;

    private String mobile;

    private String pazzwort;

    private String role;
}
