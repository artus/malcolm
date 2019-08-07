package io.github.artus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Getter
@Setter(AccessLevel.PRIVATE)
public class RandomDecisionMaker implements DecisionMaker {

    public static final double DEFAULT_PROBABILITY = 0.5;

    private double probability;
    private Random randomNumberGenerator;

    public RandomDecisionMaker() {
        this(DEFAULT_PROBABILITY);
    }

    public RandomDecisionMaker(double probability) {
        this(probability, new Random());
    }

    public RandomDecisionMaker(double probability, Random randomNumberGenerator) {
        this.setProbability(probability);
        this.setRandomNumberGenerator(randomNumberGenerator);
    }

    public void setProbability(double probability) {
        if (probability < 0 || probability > 1)  {
            String errorMessage = String.format("Probability needs to be greater than or equal to 0 and lesser than or equal to 1. Supplied probability %f is invalid.", probability);
            throw new IllegalArgumentException(errorMessage);
        }
        this.probability = probability;
    }

    public boolean decide() {
        if (this.getProbability() == 0) return false;
        return this.getRandomNumberGenerator().nextDouble() <= this.getProbability();
    }
}
