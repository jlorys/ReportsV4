package reports.reports.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import reports.reports.domain.AppUser;
import reports.reports.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private AppUserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieve an account depending on its login this method is not case sensitive.
     *
     * @param username the user's username
     * @return a Spring Security userdetails object that matches the username
     * @throws UsernameNotFoundException when the user could not be found
     */
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new UsernameNotFoundException("Empty username");
        }
        log.debug("Security verification for user '{}'", username);

        AppUser account = userRepository.getByUserName(username);

        if (account == null) {
            log.info("User {} could not be found", username);
            throw new UsernameNotFoundException("user " + username + " could not be found");
        }

        Collection<GrantedAuthority> grantedAuthorities = toGrantedAuthorities(account.getRoleNames());
        String password = account.getPassword();
        return new UserWithId(username, password, grantedAuthorities, account.isEnabled(), account.getId());
    }

    private Collection<GrantedAuthority> toGrantedAuthorities(List<String> roles) {
        List<GrantedAuthority> result = new ArrayList<>();
        for (String role : roles) {
            result.add(new SimpleGrantedAuthority(role));
        }
        return result;
    }
}