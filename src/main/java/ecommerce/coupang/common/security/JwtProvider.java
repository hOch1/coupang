package ecommerce.coupang.common.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtProvider {

	private final String base64Secret;

	public JwtProvider(@Value("${jwt.secret}") String secret) {
		this.base64Secret = Encoders.BASE64.encode(secret.getBytes(StandardCharsets.UTF_8));
	}

	public String createToken(String email) {
		Date now = new Date();
		// Date expiration = new Date(now.getTime() + 30 * 60 * 1000);
		Date expiration = new Date(Long.MAX_VALUE); // 테스트 - 만료없음

		String token = Jwts.builder()
			.setSubject(email)
			.setIssuedAt(now)
			.setExpiration(expiration)
			.signWith(getKeyFromBase64EncodedKey(base64Secret))
			.compact();

		return token;
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(getKeyFromBase64EncodedKey(base64Secret))
				.build()
				.parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.warn("잘못된 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			log.warn("만료된 JWT 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			log.warn("지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			log.warn("JWT 토큰이 잘못되었습니다.");
		}

		return false;
	}

	public String getTokenByMemberEmail(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(getKeyFromBase64EncodedKey(base64Secret))
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
	}

	public Key getKeyFromBase64EncodedKey(String base64Secret) {
		byte[] bytes = Decoders.BASE64.decode(base64Secret);

		return Keys.hmacShaKeyFor(bytes);
	}
}
