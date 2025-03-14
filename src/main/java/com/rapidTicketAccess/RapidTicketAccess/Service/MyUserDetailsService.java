package com.rapidTicketAccess.RapidTicketAccess.Service;

import com.rapidTicketAccess.RapidTicketAccess.Model.UserPrincipal;
import com.rapidTicketAccess.RapidTicketAccess.Model.Users;
import com.rapidTicketAccess.RapidTicketAccess.Repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private UsersRepository usersRepository;

    MyUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByUsername(username);
        if (user == null){
            System.out.println("user not found");
            throw new UsernameNotFoundException("user not found");
        }

        return new UserPrincipal(user);
    }
}
