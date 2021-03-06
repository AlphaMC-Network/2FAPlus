package me.egg82.tfaplus.auth.data;

import java.util.UUID;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import me.egg82.tfaplus.extended.ServiceKeys;

public class HOTPData extends AuthenticationData {
    private final long length;
    private final long counter;
    private final SecretKey key;

    public HOTPData(UUID uuid, long length, long counter) {
        super(uuid);

        if (length <= 0) {
            throw new IllegalArgumentException("length cannot be <= 0.");
        }
        if (counter < 0) {
            throw new IllegalArgumentException("counter cannot be < 0");
        }

        this.length = length;
        this.counter = counter;
        this.key = null;
    }

    public HOTPData(UUID uuid, long length, long counter, byte[] key) {
        super(uuid);

        if (length <= 0) {
            throw new IllegalArgumentException("length cannot be <= 0.");
        }
        if (counter < 0) {
            throw new IllegalArgumentException("counter cannot be < 0");
        }
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null.");
        }

        this.length = length;
        this.counter = counter;
        this.key = new SecretKeySpec(key, ServiceKeys.HOTP_ALGORITM);
    }

    public HOTPData(UUID uuid, long length, long counter, SecretKey key) {
        super(uuid);

        if (length <= 0) {
            throw new IllegalArgumentException("length cannot be <= 0.");
        }
        if (counter < 0) {
            throw new IllegalArgumentException("counter cannot be < 0");
        }
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null.");
        }

        this.length = length;
        this.counter = counter;
        this.key = key;
    }

    public long getLength() { return length; }

    public long getCounter() { return counter; }

    public SecretKey getKey() { return key; }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HOTPData)) return false;
        HOTPData hotpData = (HOTPData) o;
        return uuid.equals(hotpData.uuid);
    }
}
