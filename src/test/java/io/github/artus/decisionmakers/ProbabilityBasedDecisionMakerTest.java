package io.github.artus.decisionmakers;

import io.github.artus.decisionmakers.ProbabilityBasedDecisionMaker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProbabilityBasedDecisionMakerTest {

    @Test
    void creating_RandomDecisionMaker_without_parameters_will_set_probability_to_default_value() {
        ProbabilityBasedDecisionMaker probabilityBasedDecisionMaker = new ProbabilityBasedDecisionMaker();
        assertEquals(ProbabilityBasedDecisionMaker.DEFAULT_PROBABILITY, probabilityBasedDecisionMaker.getProbability());
        assertNotNull(probabilityBasedDecisionMaker.getRandomNumberGenerator());
    }

    @Test
    void creating_RandomDecisionMaker_without_parameters_will_correctly_set_RandomNumberGenerator() {
        ProbabilityBasedDecisionMaker probabilityBasedDecisionMaker = new ProbabilityBasedDecisionMaker();
        assertNotNull(probabilityBasedDecisionMaker.getRandomNumberGenerator());
    }

    @Test
    void setProbability_throws_Exception_when_probability_is_smaller_than_0_of_greater_than_1() {

        ProbabilityBasedDecisionMaker probabilityBasedDecisionMaker = new ProbabilityBasedDecisionMaker();

        double probabilityTooSmall = -0.1;
        double probabilityTooBig = 1.1;

        assertThrows(IllegalArgumentException.class, () -> probabilityBasedDecisionMaker.setProbability(probabilityTooSmall));
        assertThrows(IllegalArgumentException.class, () -> probabilityBasedDecisionMaker.setProbability(probabilityTooBig));
    }

    @Test
    void decide_will_return_boolean_based_on_probability() {
        ProbabilityBasedDecisionMaker probabilityBasedDecisionMaker = new ProbabilityBasedDecisionMaker(0);

        for (int i = 0; i < 1000000; i++) {
            assertFalse(probabilityBasedDecisionMaker.decide());
        }

        probabilityBasedDecisionMaker.setProbability(1);

        for (int i = 0; i < 1000000; i++) {
            assertTrue(probabilityBasedDecisionMaker.decide());
        }

        int count = 0;
        int positives = 0;

        probabilityBasedDecisionMaker.setProbability(0.5);

        for (int i = 0; i < 1000000; i++) {
            if (probabilityBasedDecisionMaker.decide()) {
                positives++;
            }
            count++;
        }

        double averageProbability = (double) positives / (double) count;

        assertTrue(averageProbability <= 0.51);
        assertTrue(averageProbability >= 0.49);
    }
}