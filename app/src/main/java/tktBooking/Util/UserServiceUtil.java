package tktBooking.Util;

import org.mindrot.jbcrypt.BCrypt;

public class UserServiceUtil {
     public static String hashPassword(String plainPassword) {
        System.out.println(BCrypt.hashpw(plainPassword, BCrypt.gensalt()));
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }



    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

}

