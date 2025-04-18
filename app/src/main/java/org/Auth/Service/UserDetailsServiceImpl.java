package org.Auth.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.Auth.entities.UserInfo;
import org.Auth.eventProducer.UserInfoProducer;
import org.Auth.model.UserInfoDto;
import org.Auth.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Component
@AllArgsConstructor
@Data
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private final UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserInfoProducer userInfoProducer;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user = userRepo.findByUserName(username);
        if (user==null){
            throw new UsernameNotFoundException("could not find user..!!");
        }
        return new CustomUserDetails(user);
    }
    public UserInfo checkIfUserAlreadyExists(UserInfoDto userInfoDto){
        return userRepo.findByUserName(userInfoDto.getUsername());
    }
    public boolean signupUser(UserInfoDto userInfoDto){
        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        if (Objects.nonNull((checkIfUserAlreadyExists(userInfoDto)))){
            return false;
        }
        String userId = UUID.randomUUID().toString();
        userRepo.save(new UserInfo(userId,userInfoDto.getUsername(),userInfoDto.getPassword(),new HashSet<>()));
        //push event to Queue 
        userInfoProducer.sendEventToKafka(userInfoDto);
        return true;
    }

}
