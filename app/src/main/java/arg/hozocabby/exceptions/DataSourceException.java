package arg.hozocabby.exceptions;

public class DataSourceException extends Exception{
    public DataSourceException(String message) {
        super(message);
    }

    public DataSourceException(String message, Exception ex){
        super(message, ex);
    }
}
