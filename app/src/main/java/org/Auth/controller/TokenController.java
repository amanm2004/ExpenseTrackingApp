package org.Auth.controller;

import org.Auth.Service.JwtService;
import org.Auth.Service.RefreshTokenService;
import org.Auth.entities.RefreshToken;
import org.Auth.request.AuthReqDto;
import org.Auth.request.RefreshTokenReqDto;
import org.Auth.response.JwtResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class TokenController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("auth/v1/login")
    public ResponseEntity authenticateAndGetToken(@RequestBody AuthReqDto authReqDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authReqDto.getUsername(),authReqDto.getPassword()));
           if (authentication.isAuthenticated()){
               RefreshToken refreshToken = refreshTokenService.createReferenceToken(authReqDto.getUsername());
               return new ResponseEntity<>(JwtResponseDto.builder().accessToken(jwtService.GenerateToken(authReqDto.getUsername()))
                       .token(refreshToken.getToken())
                       .build(), HttpStatus.OK
               );
           }else {
               return new ResponseEntity<>("Exception in user service",HttpStatus.INTERNAL_SERVER_ERROR);
           }
    }
    @PostMapping("auth/v1/refreshToken")
    private JwtResponseDto refreshToken(@RequestBody RefreshTokenReqDto refreshTokenReqDto){
        return refreshTokenService.findByToken(refreshTokenReqDto.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.GenerateToken(userInfo.getUsername());
                    return JwtResponseDto.builder().accessToken(accessToken)
                            .token(refreshTokenReqDto.getToken())
                            .build();
                }).orElseThrow(()-> new RuntimeException("Refresh token is not in DB..!"));
    }

}
