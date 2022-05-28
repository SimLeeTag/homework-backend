package com.simleetag.homework.api.domain.oauth.infra;

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

    public OAuthJwt(String secret, long accessTokenExpiration) {
        this.accessTokenExpiration = accessTokenExpiration;
        algorithm = Algorithm.HMAC256(secret);
    }

    public String createAccessToken(Long id) {
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
