package com.exemplo.controller;

import com.exemplo.dto.CurrencyDto;
import com.exemplo.models.Currency;
import com.exemplo.repositories.CurrencyRepository;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/currencies")
public class CurrencyController {

    @Autowired
    private CurrencyRepository currencies;

    @GetMapping()
    public List<CurrencyDto> list() {
        List<CurrencyDto> list = new ArrayList<>();
        
        currencies.findAll().forEach(c -> {
            list.add(new CurrencyDto(c));
        });
        
        return list;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyDto> get(@PathVariable int id) {
        Currency c = currencies.findById(id).orElse(null);
        CurrencyDto dto;
        if (c == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        dto = new CurrencyDto(c);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<Currency> post(@RequestBody CurrencyDto currency) {
        
        if (currency == null) {
            System.out.println("[CurrencyController - POST] Currency invalida");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Currency cur = currency.toEntity();
        currencies.save(cur);
        
        return new ResponseEntity<>(cur, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error message")
    public void handleError() {
        System.out.println("Erro!");
    }

}
