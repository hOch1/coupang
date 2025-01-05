package ecommerce.coupang.common.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ecommerce.coupang.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {

	private final Member member;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + member.getRole());
		return List.of(authority);
	}

	@Override
	public String getPassword() {
		return this.member.getPassword();
	}

	@Override
	public String getUsername() {
		return this.member.getEmail();
	}
}
