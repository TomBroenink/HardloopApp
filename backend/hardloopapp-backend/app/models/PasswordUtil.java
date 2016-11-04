package models;

import org.jasypt.util.password.StrongPasswordEncryptor;

/**
 * Created by Henderikus on 2-11-2016.
 */
public class PasswordUtil {

    private static PasswordUtil passwordUtil = null;
    private static StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();

    private PasswordUtil(){

    }

    public static PasswordUtil getInstance(){
        if(passwordUtil == null){
            passwordUtil = new PasswordUtil();
        }
        return passwordUtil;
    }

    /**
     * Encrypt password using Jasypt library
     * This library uses SHA-256 encryption for password encryption
     * Password will be encrypted using 100000 iterations and comes with 16 bit random salt
     * @param userPassword
     * @return
     */
    public String encryptPassword(String userPassword){
        return encryptor.encryptPassword(userPassword);
    }

    /**
     * check if password is valid
     * @param inputPassword
     * @param encryptedPassword
     * @return true when valid, false when invalid
     */
    public boolean validatePassword(String inputPassword, String encryptedPassword){
        boolean validPassword = false;

        if(encryptor.checkPassword(inputPassword, encryptedPassword)){
            validPassword = true;
        }

        return validPassword;
    }
}
