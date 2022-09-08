package com.simleetag.homework.api.domain.user.oauth;

import java.util.Date;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.simleetag.homework.api.config.AppEnvironment;

import org.springframework.stereotype.Component;

@Component
public class OAuthJwt {
    private final Algorithm algorithm;

    private final long accessTokenExpiration;

    public OAuthJwt(AppEnvironment appEnvironment) {
        final AppEnvironment.Jwt.OauthAttributes oauthAttributes = appEnvironment.getJwt().oauthAttributes();
        this.accessTokenExpiration = oauthAttributes.accessTokenExpiration();
        this.algorithm = Algorithm.HMAC256(oauthAttributes.secret());
    }

    public String createHomeworkToken(Long id) {
        return JWT.create()
                  .withClaim("userId", id)
                  .withIssuedAt(new Date())
                  .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpiration))
                  .sign(algorithm);
    }

    public <T> T parseClaims(String token, String claimName, Class<T> claimClass) {
        return parseClaims(token)
                .get(claimName)
                .as(claimClass);
    }

    private Map<String, Claim> parseClaims(String token) {
        return JWT.decode(token)
                  .getClaims();
    }

    public Long parseClaimsAsUserId(String homeworkToken) {
        final JWTVerifier verifier = JWT.require(algorithm).build();
        final Map<String, Claim> claims = verifier.verify(homeworkToken).getClaims();
        return claims.get("userId").as(Long.class);
    }
}
