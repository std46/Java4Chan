import org.fourchan.Java4Chan;
import org.fourchan.java4chan.Board;
import org.fourchan.java4chan.Thread;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Java4ChanTest {

    @Test
    @DisplayName("find /biz")
    void findBoard() {
        Java4Chan chan = new Java4Chan();
        Board biz = chan.findBoard("biz");
        Assertions.assertNotNull(biz);
    }

    @Test
    @DisplayName("get threads from /biz")
    void findThreadsForBoard() {
        Java4Chan chan = new Java4Chan();
        Board biz = chan.findBoard("biz");
        List<Thread> allThreads = biz.fetchAllThreads();
        Assertions.assertNotNull(allThreads);
    }
}
