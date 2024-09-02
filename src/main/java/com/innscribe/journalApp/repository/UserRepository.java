package com.innscribe.journalApp.repository;

import com.innscribe.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    // Find user by username
    User findByUsername(String username);
}
