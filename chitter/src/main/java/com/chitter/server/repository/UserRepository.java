package com.chitter.server.repository;

import com.chitter.server.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    Boolean existingEmailCheck(String email);

    Boolean existingUsernameCheck(String username);

}
