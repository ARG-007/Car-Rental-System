package arg.hozocabby.exceptions;

public enum ExceptionCode {

    // Account
    ACCOUNT_EXISTS(1, "Account Already Exists"),
    ACCOUNT_NOT_EXISTS(2, "Account Do Not Exists"),
    EMPTY_ACCOUNT_IDENTIFIER(3, "Account Identifier Is Empty"),
    ACCOUNT_HAS_NO_ACCESS(4, "Account Does Not Have Access"),

    // Password
    EMPTY_PASSWORD(5, "Empty Password"),
    INCORRECT_PASSWORD(6, "Incorrect Password"),
    INVALID_PASSWORD(7, "Invalid Password");

    // Database


    private final int code;
    private final String description;
    private final String stringRepresentation;


    ExceptionCode(int code, String description){
        this.code = code;
        this.description = description;

        stringRepresentation = code +" : "+description;
    }

    public int getCode(){
        return code;
    }

    public String getDescription(){
        return description;
    }

    public String toString(){
        return stringRepresentation;
    }
}
