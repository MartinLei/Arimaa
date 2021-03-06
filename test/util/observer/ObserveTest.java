package util.observer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ObserveTest {
    private boolean ping = false;
    private ObserverTest testObserver;
    private Observable testObservable;

    @BeforeEach
    void setUp() {
        testObserver = new ObserverTest();
        testObservable = new Observable();
        testObservable.addObserver(testObserver);
    }

    @Test
    void testNotify() {
        assertFalse(ping);
        testObservable.notifyObservers();
        assertTrue(ping);
    }

    @Test
    void testRemove() {
        assertFalse(ping);
        testObservable.removeObserver(testObserver);
        testObservable.notifyObservers();
        assertFalse(ping);
    }

    @Test
    void testRemoveAll() {
        assertFalse(ping);
        testObservable.removeAllObservers();
        testObservable.notifyObservers();
        assertFalse(ping);
    }

    class ObserverTest implements IObserver {
        @Override
        public void update(Event e) {
            ping = true;
        }

    }

}