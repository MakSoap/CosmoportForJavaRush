package com.space.service;

import com.space.model.Ship;

import java.util.List;

public interface ShipService {

    List<Ship> getAllShip();
    int getShipsCount();
    Ship createShip(Ship ship);
    Ship getShip(int id);
    Ship updateShip(Ship ship);
    void deleteShip(Long id);

}