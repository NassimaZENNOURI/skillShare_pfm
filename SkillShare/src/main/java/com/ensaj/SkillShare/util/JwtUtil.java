package com.ensaj.SkillShare.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    // Clé secrète encodée en Base64 (256 bits minimum, conforme à HS256)
    private final String base64Secret = "wq5rdGt3T1JFcGhyQ3JFTnpXMUZvR2FPQU5QQmZnYWs=";
    private final SecretKey key;
    private final long jwtExpiration = 1000 * 60 * 60 * 10;

    public JwtUtil() {
        // Décoder la clé Base64 et vérifier sa taille
        byte[] decodedKey = Base64.getDecoder().decode(base64Secret);
        if (decodedKey.length < 32) { // 256 bits = 32 octets
            throw new IllegalArgumentException("La clé doit avoir une taille d'au moins 256 bits.");
        }
        this.key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
    }

    // Cette méthode crée un JWT (jeton)
    public String generateToken(String nom, String prenom, String email, int idUser) {
        return Jwts.builder()
                .setSubject(email)
                .claim("idUser", idUser)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extrait l'email du token
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // Extrait l'ID utilisateur du token
    public int extractUserId(String token) {
        Claims claims = extractClaims(token);
        return Integer.valueOf(claims.get("idUser").toString());
    }

    // Vérifie si la date d'expiration du jeton n'est pas dépassée
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // Décode le jeton avec la clé secrète
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
