package za.co.quickchat;

public class Login {

    // Check whether the username is valid
    public boolean checkUserName(String username) {
        return username != null
                && username.length() <= 5
                && username.contains("_");
    }

    // Check whether the password meets the complexity rules
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;

        for (int i = 0; i < password.length(); i++) {
            char character = password.charAt(i);

            if (Character.isUpperCase(character)) {
                hasUppercase = true;
            }

            if (Character.isDigit(character)) {
                hasNumber = true;
            }

            if (!Character.isLetterOrDigit(character)) {
                hasSpecialCharacter = true;
            }
        }

        return hasUppercase && hasNumber && hasSpecialCharacter;
    }

    // Check whether the cellphone number uses an international code
    public boolean checkCellPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }

        return phoneNumber.matches("^\\+[1-9][0-9]{7,14}$");
    }

    // Register a user and return the registration result
    public String registerUser(String username, String password,
                               String phoneNumber) {

        if (!checkUserName(username)) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }

        if (!checkCellPhoneNumber(phoneNumber)) {
            return "Cell phone number incorrectly formatted or does not contain an international code.";
        }

        return "User registered successfully.";
    }

    // Check login details
    public boolean loginUser(String registeredUsername,
                             String registeredPassword,
                             String enteredUsername,
                             String enteredPassword) {

        return registeredUsername != null
                && registeredPassword != null
                && registeredUsername.equals(enteredUsername)
                && registeredPassword.equals(enteredPassword);
    }

    // Return the login status message
    public String returnLoginStatus(boolean loginSuccessful,
                                    String firstName,
                                    String lastName) {

        if (loginSuccessful) {
            return "Welcome " + firstName + ", "
                    + lastName + " it is great to see you again.";
        }

        return "Username or password incorrect, please try again.";
    }
}

