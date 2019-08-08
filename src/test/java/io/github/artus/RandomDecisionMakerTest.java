package io.github.artus;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomDecisionMakerTest {

    @Test
    void creating_RandomDecisionMaker_without_parameters_will_set_probability_to_default_value() {
        RandomDecisionMaker randomDecisionMaker = new RandomDecisionMaker();
        assertEquals(RandomDecisionMaker.DEFAULT_PROBABILITY, randomDecisionMaker.getProbability());
        assertNotNull(randomDecisionMaker.getRandomNumberGenerator());
    }

    @Test
    void creating_RandomDecisionMaker_without_parameters_will_correctly_set_RandomNumberGenerator() {
        RandomDecisionMaker randomDecisionMaker = new RandomDecisionMaker();
        assertNotNull(randomDecisionMaker.getRandomNumberGenerator());
    }

    @Test
    void setProbability_throws_Exception_when_probability_is_smaller_than_0_of_greater_than_1() {

        RandomDecisionMaker randomDecisionMaker = new RandomDecisionMaker();

        double probabilityTooSmall = -0.1;
        double probabilityTooBig = 1.1;

        assertThrows(IllegalArgumentException.class, () -> randomDecisionMaker.setProbability(probabilityTooSmall));
        assertThrows(IllegalArgumentException.class, () -> randomDecisionMaker.setProbability(probabilityTooBig));
    }

    @Test
    void decide_will_return_boolean_based_on_probability() {
        RandomDecisionMaker randomDecisionMaker = new RandomDecisionMaker(0);

        for (int i = 0; i < 1000000; i++) {
            assertFalse(randomDecisionMaker.decide());
        }

        randomDecisionMaker.setProbability(1);

        for (int i = 0; i < 1000000; i++) {
            assertTrue(randomDecisionMaker.decide());
        }

        int count = 0;
        int positives = 0;

        randomDecisionMaker.setProbability(0.5);

        for (int i = 0; i < 1000000; i++) {
            if (randomDecisionMaker.decide()) {
                positives++;
            }
            count++;
        }

        double averageProbability = (double) positives / (double) count;

        assertTrue(averageProbability <= 0.51);
        assertTrue(averageProbability >= 0.49);
    }
}