package arg.hozocabby.util;

@FunctionalInterface
interface Validator{
    boolean validate(String input);
}

public enum Validation implements Validator{
    FIELD_NOT_EMPTY("Field Cannot Be Empty", (s)->s.isEmpty() || s.isBlank()),
    NO_NUMERIC("Field Cannot Contain Numeric Characters", (s)->s.matches(".*\\d.*")),
    NO_SPECIAL_CHARS("Field Cannot Contain Special Characters", (s)->s.matches(".*[^a-zA-Z0-9].*")),
    INVALID_MOBILE("Mobile Number Has Invalid Format, Number must contain 10 digits with no space or other characters", (s)-> !(s.matches("\\d{10}"))),
    ONLY_NUMERIC("Field Can Only Contain Numerics",(s)->{
        try{
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }),
    INVALID_DOUBLE("Incorrect Floating Value", (s)->{
        try{
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    });

    private final Validator validator;
    private final String message;

    private Validation(String message, Validator validator) {
        this.message = message;
        this.validator = validator;
    }

    public String getMessage() {
        return message;
    }

    public boolean validate(String input) {
        return validator.validate(input);
    }
}
