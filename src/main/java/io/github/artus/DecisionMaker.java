package io.github.artus;

public interface DecisionMaker {
    void setProbability(double probability);
    boolean decide();

}
