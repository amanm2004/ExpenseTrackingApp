package org.Auth.repository;

import org.Auth.entities.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<UserInfo,Long> {

    public UserInfo findByUserName(String username);
}
