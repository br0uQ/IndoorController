package de.jschmucker.indoorcontroller.controller;

import de.jschmucker.indoorcontroller.model.IndoorService;

/**
 * This interface can be implemented if the implementing class is connected to the IndoorService.
 * Created by jschmucker on 05.01.17.
 */
public interface IndoorServiceBound {
    /**
     * @return The IndoorService
     */
    IndoorService getIndoorService();
}
