package com.simleetag.homework.domain.oauth;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "jwt")
@ConstructorBinding
public class OAuthJwt {

    private final Algorithm algorithm;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public OAuthJwt(String secret, long accessTokenExpiration, long refreshTokenExpiration) {
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        algorithm = Algorithm.HMAC256(secret);
    }

    public String createWithUserId(Long id) {
        return JWT.create()
                  .withClaim("id", id)
                  .withIssuedAt(new Date())
                  .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpiration))
                  .sign(algorithm);
    }

    public <T> T parseClaims(String token, String claimName, Class<T> claimClass) {
        return JWT.decode(token)
                  .getClaims()
                  .get(claimName)
                  .as(claimClass);
    }
}
