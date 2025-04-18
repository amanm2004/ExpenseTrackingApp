package org.Auth.Service;


import org.Auth.entities.RefreshToken;
import org.Auth.entities.UserInfo;
import org.Auth.repository.RefreshTokenRepo;
import org.Auth.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepo refreshTokenRepo;

    @Autowired
    UserRepo userRepo;

    public RefreshToken createReferenceToken(String username){
        UserInfo userInfoExtracted = userRepo.findByUserName(username);
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userInfoExtracted)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))
                .build();
        return refreshTokenRepo.save(refreshToken);
    }
    public RefreshToken verifyExpiration(RefreshToken token){
        if (token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepo.delete(token);
            throw new RuntimeException(token.getToken()+"Refresh token is expired. Please make a new login");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepo.findByToken(token);
    }

}
