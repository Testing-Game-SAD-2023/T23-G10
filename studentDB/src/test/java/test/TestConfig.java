package test;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SuppressWarnings("deprecation")
@TestConfiguration
public class TestConfig {

	@Bean 
    public PasswordEncoder passwordEncoder() {
       return NoOpPasswordEncoder.getInstance() ;
    }
}
