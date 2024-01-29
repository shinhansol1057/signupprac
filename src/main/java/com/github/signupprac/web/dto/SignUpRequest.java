package com.github.signupprac.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    private String name;
    private String nickName;
    private String email;
    private String password;
    private String phoneNum;
    private String gender;
    private String profileImg;
}
