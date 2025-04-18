package org.Auth.controller;

import lombok.AllArgsConstructor;
import org.Auth.Service.JwtService;
import org.Auth.Service.RefreshTokenService;
import org.Auth.Service.UserDetailsServiceImpl;
import org.Auth.entities.RefreshToken;
import org.Auth.model.UserInfoDto;
import org.Auth.response.JwtResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/auth/v1/signup")
    public ResponseEntity signup(@RequestBody UserInfoDto userInfoDto) {
        try {
            Boolean isSignedUp = userDetailsService.signupUser(userInfoDto);
            if (Boolean.FALSE.equals(isSignedUp)) {
                return new ResponseEntity<>("User already exits", HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken = refreshTokenService.createReferenceToken(userInfoDto.getUsername());
            String jwtToken = jwtService.GenerateToken(userInfoDto.getUsername());
            return new ResponseEntity<>(JwtResponseDto.builder().accessToken(jwtToken)
                    .token(refreshToken.getToken()).build(), HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>("Exception is user service",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

