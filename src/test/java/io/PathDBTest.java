package io;

import org.junit.Assert;
import org.junit.Test;

/**
 * This is the test file for PathDB
 *
 * @author Zitong Wei
 * @version 1.0
 */
public class PathDBTest {
    @Test
    public void testGetPath() {
        PathDB.addPath("/path/to/test");
        Assert.assertEquals("/path/to/test", PathDB.getPath("test"));
    }

    @Test
    public void testDeletePath() {
        PathDB.addPath("Test");
        Assert.assertEquals("Test", PathDB.getPath("Test"));
        PathDB.delete("Test");
        Assert.assertNull(PathDB.getPath("Test"));
    }

}
