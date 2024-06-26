package com.chess.api.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.chess.api.core.utils.KeyValuePair;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final String JWT_ISS = "www.custom-chess-api.com";

    private static final String JWT_SUB = "www.custom-chess-client.com";

    @Value("${jwt.secret}")
    private String secret;

    public final String createJWT(Date issueExp) {
        return createJWT(JWT_ISS, JWT_SUB, issueExp);
    }

    public final String createJWT(String issuer, String subject, Date issueExp) {
        Algorithm alg = Algorithm.HMAC256(this.secret);

        JWTCreator.Builder tokenBuilder = JWT.create()
                .withIssuer(issuer)
                .withSubject(subject)
                .withExpiresAt(issueExp);

        return tokenBuilder.sign(alg);
    }

    @SafeVarargs
    public final String createJWTWithClaims(Date issueExp, KeyValuePair<String, String>... claims) {
        return createJWTWithClaims(JWT_ISS, JWT_SUB, issueExp, claims);
    }

    @SafeVarargs
    public final String createJWTWithClaims(String issuer, String subject, Date issueExp,
            KeyValuePair<String, String>... claims) {
        Algorithm alg = Algorithm.HMAC256(this.secret);

        JWTCreator.Builder tokenBuilder = JWT.create()
                .withIssuer(issuer)
                .withSubject(subject)
                .withExpiresAt(issueExp);

        for (KeyValuePair<String, String> c : claims) {
            tokenBuilder.withClaim(c.key, c.value);
        }
        return tokenBuilder.sign(alg);
    }

    public String createRefreshToken() {
        byte[] bytes = new byte[32];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Math.floor(Math.random() * 128);
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private String hashString(String message) {
        return DigestUtils.sha256Hex(message);
    }
}
