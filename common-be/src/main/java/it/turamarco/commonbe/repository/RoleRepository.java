package it.turamarco.commonbe.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.turamarco.commonbe.model.ERole;
import it.turamarco.commonbe.model.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
	Optional<Role> findByName(ERole name);
}