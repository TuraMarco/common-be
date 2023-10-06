package it.turamarco.commonbe.security.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Locator;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import it.turamarco.commonbe.security.service.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtils {
	@Value("${security.app.jwtSecret}")
	private String jwtSecret;

	@Value("${security.app.jwtExpirationMs}")
	private int jwtExpirationMs;

	@Value("${security.app.jwtCookieName}")
	private String jwtCookie;

	public String getJwtFromCookies(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, jwtCookie);
		if (cookie != null) {
			return cookie.getValue();
		} else {
			return null;
		}
	}

	public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
		String jwt = generateTokenFromUsername(userPrincipal.getUsername());
		return ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
	}

	public ResponseCookie getCleanJwtCookie() {
		return ResponseCookie.from(jwtCookie, null).path("/api").build();
	}

	private Locator<Key> keyLocator() {
		return new Locator<Key>() {
			@Override
			public Key locate(Header header) {
				return key();
			}
		};
	}

	private Key key() {
		// TODO: rinforzare la chiave perchè debole
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().keyLocator(keyLocator()).build().parseSignedClaims(token).getPayload().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().keyLocator(keyLocator()).build().parse(authToken);
			return true;
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

	public String generateTokenFromUsername(String username) {
		return Jwts.builder().subject(username).issuedAt(new Date())
				.expiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))).compact();
	}
}
