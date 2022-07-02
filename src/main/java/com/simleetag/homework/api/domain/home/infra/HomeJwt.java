package com.simleetag.homework.api.domain.home.infra;

import java.util.Date;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "home")
@ConstructorBinding
public class HomeJwt {

    private final Algorithm algorithm;
    private final long expiration;

    public HomeJwt(String secret, long expiration) {
        this.expiration = expiration;
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String createHomeworkToken(Long id) {
        return JWT.create()
                  .withClaim("homeId", id)
                  .withIssuedAt(new Date())
                  .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                  .sign(algorithm);
    }

    public Long parseClaimsAsHomeId(String homeworkToken) {
        final JWTVerifier verifier = JWT.require(algorithm).build();
        final Map<String, Claim> claims = verifier.verify(homeworkToken).getClaims();
        return claims.get("homeId").as(Long.class);
    }
}
