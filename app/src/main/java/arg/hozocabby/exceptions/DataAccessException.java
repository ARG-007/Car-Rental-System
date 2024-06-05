package arg.hozocabby.exceptions;

public class DataAccessException extends Exception{
    public DataAccessException(String message){
        super(message);
    }

    public DataAccessException(String message, Exception ex){
        super(message, ex);
    }

    public DataAccessException(Exception ex){
        super(ex);
    }

}
