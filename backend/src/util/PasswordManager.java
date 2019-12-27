package util;

import com.lambdaworks.crypto.SCryptUtil;

import java.time.Duration;
import java.time.Instant;

public class PasswordManager {
    public static String hashPassword(String password) {
        Instant beginHash = Instant.now();
        String generatedSecuredPasswordHash = SCryptUtil.scrypt(password, 16, 16, 16);
        Instant endHash = Instant.now();
        System.out.println(String.format(
                "Process took %f s",
                Duration.between(beginHash, endHash).toMillis() / 1024.0
        ));
        return generatedSecuredPasswordHash;
    }

    public static boolean verifyPassword(String hashedPassowrd, String password) {
        return SCryptUtil.check(password, hashedPassowrd);
    }
}
