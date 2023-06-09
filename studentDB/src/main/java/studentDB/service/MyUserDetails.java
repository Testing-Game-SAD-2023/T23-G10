package studentDB.service;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import studentDB.entity.Student;

@SuppressWarnings("serial")
public class MyUserDetails implements UserDetails {

	private String username;
	private String password;
	private Long id;
	private Boolean enabled;
	private List<GrantedAuthority> authorities;

	public MyUserDetails() {}
	
	public MyUserDetails(Student student) {
		this.username = student.getEmail();
		this.password = student.getPassword();
		this.enabled = student.isEnabled();
		this.id = student.getId();
		this.authorities = new ArrayList<GrantedAuthority>();
		this.authorities.add(new SimpleGrantedAuthority("ROLE_" + student.getRole()));
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}
	
	public Long getId() {
		return id;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
