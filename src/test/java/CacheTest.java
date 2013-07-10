import com.mgerasymchuk.cache.TemporaryCache;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.mgerasymchuk.cache.utils.CustomClock;
import com.mgerasymchuk.cache.utils.TimeUtils;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Maksym
 * Date: 17.06.13
 * Time: 22:06
 * To change this template use File | Settings | File Templates.
 */
@RunWith(JUnit4.class)
public class CacheTest {
    CustomClock clockMock = new CustomClock();

    TemporaryCache<Integer, String> cache = new TemporaryCache<Integer, String>(20000, clockMock);

    @Test
    public void testSizeOperation() {
        cache.clear();

        cache.put(0, "0");
        cache.put(1, "1");

        TestCase.assertEquals(2, cache.size());
        clockMock.setTime(TimeUtils.getRelativeTime(15000));
        cache.put(3, "27");
        TestCase.assertEquals(3, cache.size());
        clockMock.setTime(TimeUtils.getRelativeTime(20001));
        TestCase.assertEquals(1, cache.size());
        clockMock.setTime(TimeUtils.getRelativeTime(35001));
        TestCase.assertTrue(cache.isEmpty());
    }

    @Test
    public void testAddOperation() {
        cache.clear();
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "0");
        map.put(1, "1");
        map.put(3, "27");
        cache.putAll(map);

        clockMock.setTime(TimeUtils.getRelativeTime(10000));
        TestCase.assertEquals(3, cache.size());
        cache.put(4, "4");
        clockMock.setTime(TimeUtils.getRelativeTime(20000));
        TestCase.assertEquals(1, cache.size());
        TestCase.assertEquals("4", cache.get(4));
        clockMock.setTime(TimeUtils.getRelativeTime(30000));
        TestCase.assertTrue(cache.isEmpty());
    }

    @Test
    public void testRemoveOperation() {
        cache.clear();
        cache.put(0, "0");
        cache.put(1, "1");

        clockMock.setTime(TimeUtils.getRelativeTime(21000));
        cache.put(3, "27");
        cache.put(4, "32");
        TestCase.assertEquals(null, cache.remove(1));
        TestCase.assertEquals("27", cache.remove(3));
        clockMock.setTime(TimeUtils.getRelativeTime(39000));
        TestCase.assertEquals("32", cache.get(4));
        TestCase.assertEquals(1, cache.size());
        clockMock.plusMilliseconds(40000);
        TestCase.assertTrue(cache.isEmpty());
    }

}
