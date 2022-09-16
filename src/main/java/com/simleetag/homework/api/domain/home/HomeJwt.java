package com.simleetag.homework.api.domain.home;

import java.util.Date;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.simleetag.homework.api.config.AppEnvironment;

import org.springframework.stereotype.Component;

@Component
public class HomeJwt {
    private final Algorithm algorithm;

    private final long accessTokenExpiration;

    public HomeJwt(AppEnvironment appEnvironment) {
        final AppEnvironment.Jwt.HomeAttributes homeAttributes = appEnvironment.getJwt().homeAttributes();
        this.accessTokenExpiration = homeAttributes.accessTokenExpiration();
        this.algorithm = Algorithm.HMAC256(homeAttributes.secret());
    }

    public String createHomeworkToken(Long id) {
        return JWT.create()
                  .withClaim("homeId", id)
                  .withIssuedAt(new Date())
                  .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpiration))
                  .sign(algorithm);
    }

    public Long parseClaimsAsHomeId(String homeworkToken) {
        final JWTVerifier verifier = JWT.require(algorithm).build();
        final Map<String, Claim> claims = verifier.verify(homeworkToken).getClaims();
        return claims.get("homeId").as(Long.class);
    }
}
