package models;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Henderikus on 2-11-2016.
 */
public class PasswordUtilTest extends TestCase {

    private PasswordUtil passwordUtil;
    private String encryptedPassword;

    @Before
    public void setUp() throws Exception {
        this.passwordUtil = PasswordUtil.getInstance();
        this.encryptedPassword = passwordUtil.encryptPassword("secret");
        System.out.println("encryption: " + this.encryptedPassword);

    }

    @Test
    public void testEncryptedPasswordTrue() throws Exception {
        assertTrue(passwordUtil.validatePassword("secret", this.encryptedPassword));
        assertFalse(passwordUtil.validatePassword("fout", this.encryptedPassword));
    }

    @Test
    public void testValidatePasswordFalse() throws Exception {
        assertFalse(passwordUtil.validatePassword("fout", "a65UoYldaXjBAJr+ArIhiusHXf+yG1PAhZeQ4BXwWjnhOuRH/EK3S37uHhQ9wwFp"));
        assertTrue(passwordUtil.validatePassword("secret", "a65UoYldaXjBAJr+ArIhiusHXf+yG1PAhZeQ4BXwWjnhOuRH/EK3S37uHhQ9wwFp"));
    }

}