package com.space.controller;

import com.space.model.ShipType;
import com.space.model.Ship;
import com.space.service.ShipDataService;
import com.space.specification.ShipSpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ShipRestController {

    private ShipDataService shipService;

    @Autowired
    public ShipRestController(ShipDataService shipService) {
        this.shipService = shipService;
    }

    private int comparingByOrder(ShipOrder order, Ship firstShip, Ship secondShip) {
        int res = 0;
        if (order == null) {
            res = firstShip.id.compareTo(secondShip.id);
        } else
            switch (order) {
                default:
                case ID:
                    res = firstShip.id.compareTo(secondShip.id);
                    break;
                case SPEED:
                    res = firstShip.speed.compareTo(secondShip.speed);
                    break;
                case DATE:
                    res = firstShip.prodDate.compareTo(secondShip.prodDate);
                    break;
                case RATING:
                    res = firstShip.rating.compareTo(secondShip.rating);
                    break;
            }
        return res;
    }

    @GetMapping(
            value = "/rest/ships"
    )
    public ResponseEntity<List<Ship>> getAllShip(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "planet", required = false) String planet,
            @RequestParam(name = "shipType", required = false) ShipType shipType,
            @RequestParam(name = "after", required = false) Long after,
            @RequestParam(name = "before", required = false) Long before,
            @RequestParam(name = "isUsed", required = false) Boolean isUsed,
            @RequestParam(name = "minSpeed", required = false) Double minSpeed,
            @RequestParam(name = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(name = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(name = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(name = "minRating", required = false) Double minRating,
            @RequestParam(name = "maxRating", required = false) Double maxRating,
            @RequestParam(name = "order", required = false) ShipOrder order,
            @RequestParam(name = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "3", required = false) Integer pageSize
    ) {
        try {
            Specification<Ship> spec = ShipSpecificationBuilder.getSpec(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);

            List<Ship> ships = shipService.getAllShip(spec);
            List<Ship> filterShips = ships.stream()
                    .sorted((firstShip, secondShip) ->
                        comparingByOrder(order, firstShip, secondShip))
                    .skip(pageNumber * pageSize)
                    .limit(pageSize)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(filterShips, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(
            path = "/rest/ships/count",
            method = RequestMethod.GET
    )
    public int getShipsCount(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "planet", required = false) String planet,
            @RequestParam(name = "shipType", required = false) ShipType shipType,
            @RequestParam(name = "after", required = false) Long after,
            @RequestParam(name = "before", required = false) Long before,
            @RequestParam(name = "isUsed", required = false) Boolean isUsed,
            @RequestParam(name = "minSpeed", required = false) Double minSpeed,
            @RequestParam(name = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(name = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(name = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(name = "minRating", required = false) Double minRating,
            @RequestParam(name = "maxRating", required = false) Double maxRating
    ) {
        Specification<Ship> spec = ShipSpecificationBuilder.getSpec(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
        return shipService.getShipsCount(spec);
    }


    @RequestMapping(
            path = "/rest/ships",
            method = RequestMethod.POST
    )
    public ResponseEntity<Ship> createShip(@RequestBody Ship ship) {
        try {
            if (
                    ship.name == null ||
                            ship.planet == null ||
                            ship.shipType == null ||
                            ship.prodDate == null ||
                            ship.speed == null ||
                            ship.crewSize == null
            ) throw new IllegalArgumentException();

            if (!isValidShipData(ship))
                throw new IllegalArgumentException();

            if (ship.isUsed == null)
                ship.isUsed = false;

            return new ResponseEntity<>(shipService.createShip(ship), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(
            path = "/rest/ships/{id}",
            method = RequestMethod.GET
    )
    public ResponseEntity<Ship> getShip(@PathVariable String id) {
        try {
            if (isBadId(id)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            Ship ship = shipService.getShip(Integer.parseInt(id));
            if (ship == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            return new ResponseEntity<>(ship, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(
            path = "/rest/ships/{id}",
            method = RequestMethod.POST
    )
    public ResponseEntity<Ship> updateShip(@PathVariable String id, @RequestBody Ship ship) {
        if (isBadId(id)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (!isValidShipData(ship))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Ship shipDB = shipService.getShip(Integer.parseInt(id));
        if (shipDB == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (ship.name != null) {
            shipDB.name = ship.name;
        }
        if (ship.planet != null) {
            shipDB.planet = ship.planet;
        }
        if (ship.shipType != null) {
            shipDB.shipType = ship.shipType;
        }
        if (ship.prodDate != null) {
            shipDB.prodDate = ship.prodDate;
        }
        if (ship.isUsed != null) {
            shipDB.isUsed = ship.isUsed;
        }
        if (ship.speed != null) {
            shipDB.speed = ship.speed;
        }
        if (ship.crewSize != null) {
            shipDB.crewSize = ship.crewSize;
        }

        return new ResponseEntity<>(shipService.updateShip(shipDB), HttpStatus.OK);
    }

    @RequestMapping(
            path = "/rest/ships/{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity<?> deleteShip(@PathVariable String id) {
        if (isBadId(id)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Ship ship = shipService.getShip(Integer.parseInt(id));
        if (ship == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        shipService.deleteShip(Long.parseLong(id));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean isBadId(String id) {
        try {
            int i = Integer.parseInt(id);
            if (i <= 0)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }

    private boolean isValidShipData(Ship ship) {
        if (ship == null)
            return false;

        if (ship.name != null) {
            if ((ship.name.length() > 50) || ship.name.isEmpty())
                return false;
        }
        if (ship.planet != null) {
            if ((ship.planet.length() > 50) || ship.planet.isEmpty())
                return false;
        }
        if (ship.speed != null) {
            BigDecimal roundSpeed = new BigDecimal(ship.speed).setScale(2, RoundingMode.DOWN);
            if ((roundSpeed.compareTo(new BigDecimal(0.01)) < 0) || (roundSpeed.compareTo(new BigDecimal(0.99)) > 0))
                return false;
        }
        if (ship.crewSize != null) {
            if (!((ship.crewSize >= 1) && (ship.crewSize < 10000)))
                return false;
        }
        if (ship.prodDate != null) {
            if (ship.prodDate.getTime() < 0)
                return false;
            int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(ship.prodDate));
            if ( !((year >= 2800) && (year < 3020)) )
                return false;
        }
        return true;
    }

}
