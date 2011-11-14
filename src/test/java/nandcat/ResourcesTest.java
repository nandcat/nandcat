package nandcat;

import java.util.Locale;
import java.util.ResourceBundle;
import org.junit.Test;

public class ResourcesTest {

    @Test
    public void test() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("myProject.label", Locale.FRENCH);

        String name = resourceBundle.getString("NAME");
        String password = resourceBundle.getString("PASSWORD");

        System.out.println(name);
        System.out.println(password);
    }

}
