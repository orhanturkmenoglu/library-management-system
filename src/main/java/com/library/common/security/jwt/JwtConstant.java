package com.library.common.security.jwt;

public class JwtConstant {

    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_SECRET="YourSecretKeyForJWTSigningAndValidation12345";
    public static final long JWT_EXPIRATION_MS = 86400000; // 1
    public static final String PREFIX = "Bearer ";


}
