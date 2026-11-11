package com.webdev2;

/**
 * Interface representing an entity whose academic standing can be computed.
 */
public interface Gradable {
    /**
     * Computes and returns the academic standing of the entity.
     *
     * @return a String representing the standing (e.g., "Good Standing")
     */
    String computeStanding();
}
