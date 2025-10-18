package hu.ekke.receptmegoszto.service;

import hu.ekke.receptmegoszto.principal.MyUserDetails;
import hu.ekke.receptmegoszto.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    private UserDetailsRepository userDetailsRepository;

    @Autowired
    public MyUserDetailService(UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userDetailsRepository.findByUserName(s)
                .map(MyUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User name not found: " + s));
    }
}
