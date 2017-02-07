package farm.chaos.ppfax.utils;

import java.util.Random;

public class RandomBean {
    private static final Random RANDOM = new Random();
    private static int ctr = 1000;

    public String getNextId() {
        return String.format("%04d%04d", Math.abs(RANDOM.nextInt()) % 10000, ctr++);
    }
}
