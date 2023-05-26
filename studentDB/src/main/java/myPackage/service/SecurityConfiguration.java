package myPackage.service;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfiguration {
	
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
	@Bean
	public BCryptPasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http.authorizeRequests()
				//Public
				.antMatchers("/register","/verify","/forgot-password",
						"/reset-password","/login","/images/**").permitAll()
				
				//User
				.antMatchers("/login-success").authenticated()
				.antMatchers("/user/{userId}")
					.access("(hasRole('USER') or hasRole('ADMIN')) and @securityConfiguration.hasUserId(authentication,#userId)")
				
				//Admin
				.anyRequest().hasRole("ADMIN")
				.and()
				.formLogin().loginPage("/login").permitAll()
				.defaultSuccessUrl("/login-success")
				.and().logout().permitAll().logoutSuccessUrl("/login?logout");
		
		http.csrf().disable();
		
		//Necessario per visualizzare console h2
		http.headers().frameOptions().sameOrigin(); 
		
		return http.build();
	}
	
	public boolean hasUserId(Authentication authentication, Long userId) {
        MyUserDetails principal = (MyUserDetails) authentication.getPrincipal();
        return principal.getId() == userId;
    }
}
