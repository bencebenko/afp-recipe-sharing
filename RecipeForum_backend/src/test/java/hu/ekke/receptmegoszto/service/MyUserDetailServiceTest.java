package hu.ekke.receptmegoszto.service;

import hu.ekke.receptmegoszto.domain.RecipeUser;
import hu.ekke.receptmegoszto.principal.MyUserDetails;
import hu.ekke.receptmegoszto.repository.UserDetailsRepository;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MyUserDetailServiceTest {

    Mockery context = new Mockery();

    @Test
    void loadUserByUsername_ReturnsMyUserDetails_WhenUserExists() {
        UserDetailsRepository mockRepo = context.mock(UserDetailsRepository.class);
        MyUserDetailService service = new MyUserDetailService(mockRepo);

        RecipeUser mockUser = new RecipeUser();
        mockUser.setUserName("david");

        context.checking(new Expectations() {{
            oneOf(mockRepo).findByUserName("david");
            will(returnValue(Optional.of(mockUser)));
        }});

        var result = service.loadUserByUsername("david");

        assertTrue(result instanceof MyUserDetails);
        assertEquals("david", ((MyUserDetails) result).getUsername());
        context.assertIsSatisfied();
    }

    @Test
    void loadUserByUsername_ThrowsException_WhenUserNotFound() {
        UserDetailsRepository mockRepo = context.mock(UserDetailsRepository.class);
        MyUserDetailService service = new MyUserDetailService(mockRepo);

        context.checking(new Expectations() {{
            oneOf(mockRepo).findByUserName("missing");
            will(returnValue(Optional.empty()));
        }});

        UsernameNotFoundException ex = assertThrows(
                UsernameNotFoundException.class,
                () -> service.loadUserByUsername("missing")
        );

        assertTrue(ex.getMessage().contains("User name not found"));
        context.assertIsSatisfied();
    }
}
