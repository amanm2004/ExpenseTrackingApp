package org.Auth.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.Auth.entities.UserInfo;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDto extends UserInfo {

    public String userName;

    public String lastName;

    public Long phoneNumber;

    public String email;

}
