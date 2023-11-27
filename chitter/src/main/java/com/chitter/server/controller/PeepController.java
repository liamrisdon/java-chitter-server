package com.chitter.server.controller;

import com.chitter.server.model.Peep;
import com.chitter.server.repository.PeepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/api/auth/")
public class PeepController {

    @Autowired
    PeepRepository peepRepository;

    @GetMapping
    public ResponseEntity<?> getAllPeeps() {
        try {
            List<Peep> peeps = new ArrayList<Peep>();
            peepRepository.findAll().forEach(peeps::add);

            if(peeps.isEmpty()) {
                return new ResponseEntity<>("No Peeps found", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(peeps, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> newPeep (@RequestBody Peep peep) {
        try {
            if(peep.getContent() == null) {
                return new ResponseEntity<>("Error: no peep content", HttpStatus.BAD_REQUEST);
            }
            Peep _peep = peepRepository.save(new Peep(peep.getUsername(), peep.getName(), peep.getContent(), peep.getDateCreated()));
            return new ResponseEntity<>(_peep, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
