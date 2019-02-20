package keyword;

import org.junit.Assert;
import org.junit.Test;
import java.awt.*;

/**
 * This is the test file for KeywordDB
 *
 * @author Binbin Yan
 * @version 1.0
 */

public class KeywordDBTest {
    @Test
    public void testJavaColor(){

        Color red = Color.red;
        Color blue = Color.blue;
        Color purple = Color.magenta;

        KeywordDB test = new KeywordDB("Java");

        Color output1 = test.matchColor("import");
        Color output2 = test.matchColor("void");
        Color output3 = test.matchColor("enum");

        Assert.assertTrue(red == output1);
        Assert.assertTrue(blue == output2);
        Assert.assertTrue(purple == output3);

    }

    @Test
    public void testCColor(){

        Color red = Color.red;
        Color blue = Color.blue;
        Color purple = Color.magenta;

        KeywordDB test = new KeywordDB("C");

        Color output1 = test.matchColor("#include");
        Color output2 = test.matchColor("void");
        Color output3 = test.matchColor("enum");

        Assert.assertTrue(red == output1);
        Assert.assertTrue(blue == output2);
        Assert.assertTrue(purple == output3);
    }
}
