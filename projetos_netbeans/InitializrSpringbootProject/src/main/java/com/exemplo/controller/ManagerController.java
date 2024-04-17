
package com.exemplo.controller;

import com.exemplo.dto.ManagerDto;
import com.exemplo.models.Client;
import com.exemplo.models.Manager;
import com.exemplo.repositories.ClientRepository;
import com.exemplo.repositories.ManagerRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author tiago
 */
@RestController
@RequestMapping("/managers")
public class ManagerController {
    
    @Autowired
    private ManagerRepository managers;
    
    @Autowired
    private ClientRepository clients;
    
    @GetMapping()
    public List<ManagerDto> list() {
        List<ManagerDto> list = new ArrayList<>();
        managers.findAll().forEach(m -> {list.add(new ManagerDto(m));});
        return list;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ManagerDto> get(@PathVariable int id) {
        Manager m = managers.findById(id).orElse(null);
        ManagerDto dto;
        if (m == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        dto = new ManagerDto(m);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    
    @GetMapping("/best-manager")
    public ResponseEntity<ManagerDto> getBest() {

        List<Manager> list = managers.findByMaxPayments();
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Manager m = list.get(0);
        if (m == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        ManagerDto managerDto = new ManagerDto(m);
        return new ResponseEntity<>(managerDto, HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<Manager> post(@RequestBody ManagerDto manager) {
        
        if (manager == null) {
            System.out.println("[ManagerController - POST] Manager invalido");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Manager man = manager.toEntity();
        managers.save(man);
        
        return new ResponseEntity<>(man, HttpStatus.OK);
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error message")
    public void handleError() {
        System.out.println("Erro!");
    }
}
