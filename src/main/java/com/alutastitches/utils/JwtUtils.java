package com.alutastitches.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;


@Component
public class JwtUtils {

    private Supplier<SecretKeySpec> getKey = () ->{
        Key key = Keys.hmacShaKeyFor("fad7e270e62ae5b3bd8655128a56be9919e4fefb9fd8a0209e87ab300d3c173ac3e216f0ca5a34cc997fa4b77e4753439e1534eb4d91dc9590d8a374ca5aa8a2"
                .getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(key.getEncoded(), key.getAlgorithm());

    };
        //Setting the expiration time to 15minutes after token issued time.
    private Supplier<Date> expirationTime = () ->
        Date.from(LocalDateTime.now().plusMinutes(15).atZone(ZoneId.systemDefault())
                .toInstant());

    //A Function to create Json Web Token (jwt)
public Function<UserDetails, String> createJwt = (userDetails) ->{
        Map<String, Object> claims = new HashMap<>();
    return Jwts.builder()
            .signWith(getKey.get())
            .claims(claims)
            .subject(userDetails.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(expirationTime.get())
            .compact();
};

            // A Function to extract Claims from the token
public <T> T extractClaims(String token, Function<Claims, T> claimsResolver ){
    Claims claims = Jwts.parser().verifyWith(getKey.get()).build().parseSignedClaims(token).getPayload();
    return claimsResolver.apply(claims);
}

            // Extract username from the token
public Function<String, String> extractUsername = (token)-> extractClaims(token, Claims::getSubject);

            //Extract Expiration time from the token
public Function<String, Date> extractExpirationTime = (token)-> extractClaims(token, Claims::getExpiration);

            //This Function check if the token has expired based on the extractedExpirationTime.
public Function<String, Boolean> isTokenExpired = (token) -> extractExpirationTime.apply(token).after(new Date(System.currentTimeMillis()));

            //This Function determine the validity of the token by checking if the token has expired
            // and if the extracted Username is equal to the Username provided by the user
public BiFunction<String, String, Boolean> isTokenValid = (token, username)-> isTokenExpired.apply(token) &&
        Objects.equals(extractUsername.apply(token),username);


}
