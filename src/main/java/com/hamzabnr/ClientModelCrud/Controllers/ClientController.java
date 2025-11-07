package com.hamzabnr.ClientModelCrud.Controllers;

import com.hamzabnr.ClientModelCrud.Models.ClientModel;
import com.hamzabnr.ClientModelCrud.Services.ClientService;
import com.hamzabnr.ClientModelCrud.dto.ClientDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clients")
public class ClientController {

  @Autowired
  ClientService clientService;

  @GetMapping
  public ResponseEntity<Map<String, Object>> getClients() {
    // return clientService.getAllClients();
    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("clients", clientService.getClientsFromDB());
    return ResponseEntity.status(HttpStatus.OK).body(body);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
    // return clientService.clientById(id);
    ClientDTO client = clientService.getByIdFromDB(id);
    return ResponseEntity.status(HttpStatus.OK).body(client);
  }

  @PostMapping
  public ResponseEntity<ClientDTO> addClient(@Valid @RequestBody ClientDTO client) {
    // clientService.addClient(client);
    // return "new client " + client.getId() + " added successfully";

    return ResponseEntity.status(HttpStatus.CREATED).body(clientService.addClientToDB(client));
  }

  @PostMapping("/bulk")
  public ResponseEntity<Map<String, Object>> addClientBulk(@Valid @RequestBody List<ClientDTO> clients) {
    List<String> addedClientsIds = new ArrayList<>();
    clients.forEach(cl -> {
      ClientDTO savedClient = clientService.addClientToDB(cl);
      addedClientsIds.add("new client " + savedClient.getName() + " added successfully");
    });

    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", LocalDateTime.now().toString());
    body.put("addedClients",  addedClientsIds);
    return ResponseEntity.status(HttpStatus.CREATED).body(body);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @Valid @RequestBody ClientDTO updates) {
    // return clientService.updateClient(id, updates);
    ClientDTO updatedClient = clientService.updateClientInDB(id, updates);

    return ResponseEntity.status(HttpStatus.OK).body(updatedClient);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteClient(@PathVariable Long id) {
    // clientService.deleteClient(id);
    clientService.deleteClientFromDB(id);
    return ResponseEntity.status(HttpStatus.OK).body("client " + id + " deleted successfully");
  }

}
