package myPackage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfiguration {
	
	 @Autowired
	 MyUserDetailsService userDetailsService;
	
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
				.antMatchers("/register","/registration_success","/verify").permitAll()
				.antMatchers("/user").authenticated()
				.anyRequest().hasRole("ADMIN")
				.and()
				.formLogin().loginPage("/login").permitAll();
		
		http.csrf().disable();
		
		//Necessario per visualizzare console h2
		http.headers().frameOptions().sameOrigin(); 
		
		return http.build();
	}
}
