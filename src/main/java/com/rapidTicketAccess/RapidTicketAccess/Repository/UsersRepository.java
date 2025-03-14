package com.rapidTicketAccess.RapidTicketAccess.Repository;

import com.rapidTicketAccess.RapidTicketAccess.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    Users findByUsername(String username);
}
