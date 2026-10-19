package za.co.quickchat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    private final Login login = new Login();

    @Test
    public void testValidUsername() {
        assertTrue(login.checkUserName("ab_cd"));
    }

    @Test
    public void testInvalidUsername() {
        assertFalse(login.checkUserName("abcdef"));
        assertFalse(login.checkUserName("abcde"));
    }

    @Test
    public void testValidPassword() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testInvalidPassword() {
        assertFalse(login.checkPasswordComplexity("password"));
        assertFalse(login.checkPasswordComplexity("short1!"));
    }

    @Test
    public void testValidCellPhoneNumber() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testInvalidCellPhoneNumber() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    @Test
    public void testSuccessfulLogin() {
        assertTrue(login.loginUser(
                "ab_cd", "Chat@123", "ab_cd", "Chat@123"
        ));
    }

    @Test
    public void testFailedLogin() {
        assertFalse(login.loginUser(
                "ab_cd", "Chat@123", "wrong", "Chat@123"
        ));
    }
}
