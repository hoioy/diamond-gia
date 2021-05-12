package com.hoioy.diamond.oauth2.support.pkce;


import org.apache.commons.codec.binary.Base64;
import org.springframework.security.crypto.codec.Utf8;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// code verifier转化为code challenge 的算法 枚举
public enum CodeChallengeMethod {
    S256 {
        @Override
        public String transform(String codeVerifier) {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalArgumentException("No such algorithm [SHA-256]");
            }
            byte[] sha256 = md.digest(Utf8.encode(codeVerifier));
            return Base64.encodeBase64URLSafeString(sha256);
        }
    },
    PLAIN {
        @Override
        public String transform(String codeVerifier) {
            return codeVerifier;
        }
    },
    NONE {
        @Override
        public String transform(String codeVerifier) {
            throw new UnsupportedOperationException();
        }
    };

    public abstract String transform(String codeVerifier);
}