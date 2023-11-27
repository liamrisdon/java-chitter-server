package com.chitter.server.repository;

import com.chitter.server.model.ERole;
import com.chitter.server.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
