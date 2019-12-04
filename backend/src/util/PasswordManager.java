package util;

import com.lambdaworks.crypto.SCryptUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Advanced;
import de.mkammerer.argon2.Argon2Factory;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PasswordManager {
    private static Argon2 ARGON2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    public static String hashPassword(char[] password) {
        String encoded = null;
        try {
            Instant beginHash = Instant.now();
            // Compute and return the hash
            // 1 iterations, 64 MiB of memory, and 8 degrees of parallelism ( 1 CPU core #
            // change for more cores )
            encoded = ARGON2.hash(1, 65536, 8, password);
            Instant endHash = Instant.now();
            System.out.println(String.format(
                    "Process took %f s",
                    Duration.between(beginHash, endHash).toMillis() / 1024.0
            ));
        } catch (Exception ex) {
            Logger.getLogger(PasswordManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ARGON2.wipeArray(password);
        }
        return encoded;
    }

    public static boolean verifyPassword(String hashedPassword, char[] password) {
        try {
            return ARGON2.verify(hashedPassword, password);
        } finally {
            ARGON2.wipeArray(password);
        }
    }

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

    public static boolean verifyPassword(String hashedPassword, String password) {
        return SCryptUtil.check(password, hashedPassword);
    }
}
