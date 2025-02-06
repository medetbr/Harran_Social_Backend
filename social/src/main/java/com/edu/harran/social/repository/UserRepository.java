package com.edu.harran.social.repository;

import com.edu.harran.social.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String eMail);

    User findUserById(Long userId2);

    User findUserByUserId(String userId);
}
