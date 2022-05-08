package com.simleetag.homework.domain.oauth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.ECDSAKeyProvider;
import com.simleetag.homework.dto.AccessTokenResponse;
import com.simleetag.homework.dto.UserInformationResponse;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class AppleOAuthProvider extends AbstractOAuthProvider {

    @Autowired
    private OAuthJwt oauthJwt;

    @Value("${oauth.attribute.apple.team-id}")
    private String TEAM_ID;

    @Value("${oauth.attribute.apple.client-id}")
    private String CLIENT_ID;

    @Value("${oauth.attribute.apple.key-path}")
    private String KEY_PATH;

    @Value("${oauth.attribute.apple.uri}")
    private String APPLE_URI;

    @Value("${oauth.attribute.apple.client-secret-expiration}")
    private Integer APPLE_CLIENT_SECRET_EXPIRATION;

    public AppleOAuthProvider() {
        this(new OAuthAttributes());
    }

    public AppleOAuthProvider(OAuthAttributes oauthAttributes) {
        super(oauthAttributes);
    }

    @Override
    public AccessTokenResponse requestAccessToken(String code) throws IOException {
        return WebClient.create()
                .post()
                .uri(oauthAttributes.getTokenUri())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(createRequestBodyOfToken(code))
                .retrieve()
                .bodyToMono(AccessTokenResponse.class)
                .block();
    }

    private MultiValueMap<String, String> createRequestBodyOfToken(String code) throws IOException {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", oauthAttributes.getClientId());
        formData.add("client_secret", makeClientSecret());
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", oauthAttributes.getRedirectUri());
        return formData;
    }

    private String makeClientSecret() throws IOException {
        Date expirationDate = Date.from(LocalDateTime.now().plusDays(APPLE_CLIENT_SECRET_EXPIRATION).atZone(ZoneId.systemDefault()).toInstant());
        Map<String, Object> headerMap = new HashMap<>();
        return JWT.create()
                .withHeader(headerMap)
                .withIssuer(TEAM_ID)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(expirationDate)
                .withAudience(APPLE_URI)
                .withSubject(CLIENT_ID)
                .sign(Algorithm.ECDSA256((ECDSAKeyProvider) getPrivateKey()));
    }

    private PrivateKey getPrivateKey() throws IOException {
        ClassPathResource resource = new ClassPathResource(KEY_PATH);
        String privateKey = new String(Files.readAllBytes(Paths.get(resource.getURI())));
        Reader pemReader = new StringReader(privateKey);
        PEMParser pemParser = new PEMParser(pemReader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        return converter.getPrivateKey(object);
    }

    @Override
    public UserInformationResponse requestUserInformation(AccessTokenResponse accessTokenResponse) {
        Long oauthId = oauthJwt.parseClaims(accessTokenResponse.getAccessToken(), "id", Long.class);
        return new UserInformationResponse(oauthId);
    }
}
