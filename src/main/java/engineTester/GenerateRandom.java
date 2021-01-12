package engineTester;

import java.util.Random;

public class GenerateRandom {

    private Random random;

    public GenerateRandom(Random random) {
        this.random = random;
    }

    public float generateRandomX(){
        return random.nextFloat() * 800 - 400;
    }

    public float generateRandomY(){
        return random.nextFloat() * 800 - 400;
    }

    public float generateRandomZ(){
        return random.nextFloat() * -600;
    }
}
