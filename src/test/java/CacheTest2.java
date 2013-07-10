import com.mgerasymchuk.cache.TemporaryCache2;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.mgerasymchuk.cache.utils.CustomClock;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Maksym
 * Date: 15.05.13
 * Time: 23:50
 * To change this template use File | Settings | File Templates.
 */
@RunWith(JUnit4.class)
public class CacheTest2 {

    CustomClock clockMock = new CustomClock();

    @Test
    public void testSizeOperation() {
        TemporaryCache2<Integer, String> cache = new TemporaryCache2<Integer, String>(clockMock);
        cache.put(0, "0", 3600000);
        cache.put(1, "1", 1000);
        cache.put(3, "27", 3000);

        TestCase.assertEquals(3, cache.size());
        clockMock.plusMilliseconds(1500);
        TestCase.assertEquals(2, cache.size());
        clockMock.plusMilliseconds(4000);
        TestCase.assertEquals(1, cache.size());
        clockMock.plusMilliseconds(3600001);
        TestCase.assertTrue(cache.isEmpty());
    }

    @Test
    public void testAddOperation() {
        TemporaryCache2<Integer, String> cache = new TemporaryCache2<Integer, String>(clockMock);
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "0");
        map.put(1, "1");
        map.put(3, "27");
        cache.putAll(map);
        cache.put(4, "4", 20000);

        clockMock.plusMilliseconds(1500);
        TestCase.assertEquals(4, cache.size());
        clockMock.plusMilliseconds(cache.getDefaultExpirationPeriod() + 1);
        TestCase.assertEquals(1, cache.size());
        TestCase.assertEquals("4", cache.get(4));
        clockMock.plusMilliseconds(21000);
        TestCase.assertTrue(cache.isEmpty());
    }

    @Test
    public void testRemoveOperation() {
        TemporaryCache2<Integer, String> cache = new TemporaryCache2<Integer, String>(clockMock);
        cache.put(0, "0", 3600000);
        cache.put(1, "1", 1000);
        cache.put(3, "27", 3000);

        clockMock.plusMilliseconds(1500);
        TestCase.assertEquals(null, cache.remove(1));
        TestCase.assertEquals("27", cache.remove(3));
        clockMock.plusMilliseconds(3590000);
        TestCase.assertEquals("0", cache.get(0));
        clockMock.plusMilliseconds(3600001);
        TestCase.assertTrue(cache.isEmpty());
    }
}
