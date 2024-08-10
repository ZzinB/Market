package com.example.Wanted.Market.API.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFormDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Pattern(regexp = "^[a-zA-Z가-힣]{2,}$", message = "이름은 2글자 이상의 한글 또는 영문이어야 합니다.")
    private String name;

    @Email(message = "유효한 이메일 형식을 입력해주세요.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 최소 8자, 대문자, 소문자, 숫자 및 특수문자를 포함해야 합니다.")
    private String password;

    @NotBlank(message = "우편번호는 필수 입력 값입니다.")
    private String postcode;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String address;

    @NotBlank(message = "상세 주소는 필수 입력 값입니다.")
    private String detailAddress;

    private String extraAddress;

    @Pattern(regexp = "^(\\d{3})-(\\d{3,4})-(\\d{4})$", message = "휴대폰 번호는 000-0000-0000 형식이어야 합니다.")
    private String phoneNumber;
}
