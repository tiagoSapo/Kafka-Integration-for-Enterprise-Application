
package com.exemplo.controller;

import com.exemplo.dto.BalanceDto;
import com.exemplo.dto.ClientDto;
import com.exemplo.dto.CreditsDto;
import com.exemplo.dto.PaymentsDto;
import com.exemplo.models.Client;
import com.exemplo.models.Manager;
import com.exemplo.repositories.ClientRepository;
import com.exemplo.repositories.ManagerRepository;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/clients")
public class ClientController {
    
    @Autowired
    private ClientRepository clients;
    
    @Autowired
    private ManagerRepository managers;
    
    @GetMapping()
    public ResponseEntity<List<ClientDto>> list() {
        
        List<ClientDto> list = new ArrayList<>();
        
        clients.findAll().forEach(c -> { list.add(new ClientDto(c)); });
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> get(@PathVariable int id) {
        Client cli = clients.findById(id).orElse(null);
        ClientDto c;
        if (cli == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        c = new ClientDto(cli);
        return new ResponseEntity<>(c, HttpStatus.OK);
    }
    
    @GetMapping("/{id}/bill")
    public ResponseEntity<CreditsDto> getBill(@PathVariable int id) {
        Client c = clients.findById(id).orElse(null);
        if (c == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        CreditsDto b = new CreditsDto(c.getId(), c.getCreditsMonth());
        return new ResponseEntity<>(b, HttpStatus.OK);
    }
    
    @GetMapping("/{id}/balance")
    public ResponseEntity<BalanceDto> getBalance(@PathVariable int id) {
        Client cli = clients.findById(id).orElse(null);
        if (cli == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        BalanceDto ba = new BalanceDto(cli.getId(), cli.getBalance());
        return new ResponseEntity<>(ba, HttpStatus.OK);
    }
    
    @GetMapping("/balances")
    public ResponseEntity<List<BalanceDto>> getBalances() {
        List<Client> list = clients.findAll();
        List<BalanceDto> balances = new ArrayList<>();
        
        list.forEach(c -> {balances.add(new BalanceDto(c.getId(), c.getBalance()));});
        return new ResponseEntity<>(balances, HttpStatus.OK);
    }
    
    @GetMapping("/{id}/credits")
    public ResponseEntity<CreditsDto> getCredit(@PathVariable int id) {
        
        Client cli = clients.findById(id).orElse(null);
        CreditsDto c;
        if (cli == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        c = new CreditsDto(cli.getId(), cli.getCredits());
        return new ResponseEntity<>(c, HttpStatus.OK);
    }
    
    @GetMapping("/credits")
    public ResponseEntity<List<CreditsDto>> getCredits() {
        
        List<Client> list = clients.findAll();
        List<CreditsDto> credits = new ArrayList<>();
        
        list.forEach(c -> {credits.add(new CreditsDto(c.getId(), c.getCredits()));});
        return new ResponseEntity<>(credits, HttpStatus.OK);
    }
    
    @GetMapping("/{id}/payments")
    public ResponseEntity<PaymentsDto> getPayment(@PathVariable int id) {
        
        Client cli = clients.findById(id).orElse(null);
        PaymentsDto payments;
        if (cli == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        payments = new PaymentsDto(cli.getId(), cli.getPayments());
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }
    
    @GetMapping("/payments")
    public ResponseEntity<List<PaymentsDto>> getPayments() {
        List<Client> list = clients.findAll();
        List<PaymentsDto> payments = new ArrayList<>();
        
        list.forEach(c -> {payments.add(new PaymentsDto(c.getId(), c.getPayments()));});
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }
    
    @GetMapping("/max-credits")
    public ResponseEntity<CreditsDto> getMaxCredits() {
        List<Client> list = clients.findByMaxCredits();
        
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Client c = list.get(0);
        CreditsDto credits = new CreditsDto(c.getId(), c.getCredits());
        
        return new ResponseEntity<>(credits, HttpStatus.OK);
    }

    
    @PostMapping
    public ResponseEntity<ClientDto> post(@RequestBody ClientDto client) {

        Manager man;
        
        if (client == null) {
            System.out.println("[ClientController - POST] Cliente com id invalido");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        man = managers.findById(client.getManager_id()).orElse(null);
        if (man == null) {
            System.out.println("[ClientController - POST] Manager com id invalido");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Client c = client.toEntity(man);
        c.setManager(man);
        clients.save(c);
        
        return new ResponseEntity<>(new ClientDto(c), HttpStatus.OK);
    }
    
}
