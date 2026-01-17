package iuh.fit.chat.component;

import iuh.fit.chat.auth.AuthService;
import iuh.fit.chat.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component("userDetailsService")
@RequiredArgsConstructor
public class UserDetailCustom implements UserDetailsService {
    private final List<LoginResponse> users = AuthService.users;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginResponse user = this.users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);

        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
