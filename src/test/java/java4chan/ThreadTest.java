package java4chan;

import org.fourchan.java4chan.Thread;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ThreadTest {

    @Test
    @DisplayName("expired thread")
    void checkExpiredThread() {
        Thread t = Thread.fetch("biz", 1234);
        Assertions.assertTrue(t.is404());
    }
}
