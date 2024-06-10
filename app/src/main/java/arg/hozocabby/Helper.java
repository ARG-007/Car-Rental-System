package arg.hozocabby;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Helper {
    private static final ClassLoader classLoader = Helper.class.getClassLoader();

    public static InputStream getResourceAsStream(String resource){
        return classLoader.getResourceAsStream(resource);
    }

    public static URL getResource(String resource){
        return classLoader.getResource(resource);
    }

    public static Properties getPropertiesFromResource(String resource) throws IOException{
        Properties props = new Properties();
        InputStream is = getResourceAsStream(resource);

        if(is == null)
            throw new IOException("File Not Found");

        if(resource.toLowerCase().contains(".xml")) {
            props.loadFromXML(is);
        }
        else {
            props.load(is);
        }
        return props;
    }

    public static List<String> getFileContent(String path) {

        try {
            return Files.readAllLines(Paths.get(getResource(path).toURI()));
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace(System.err);
        }
        return new ArrayList<>();
    }
}
