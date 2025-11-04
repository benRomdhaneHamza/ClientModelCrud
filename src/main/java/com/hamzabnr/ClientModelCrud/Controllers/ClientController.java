package com.hamzabnr.ClientModelCrud.Controllers;

import com.hamzabnr.ClientModelCrud.Models.ClientModel;
import com.hamzabnr.ClientModelCrud.Services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

  @Autowired
  ClientService clientService;

  @GetMapping
  public List<ClientModel> getClients() {
    // return clientService.getAllClients();
    return clientService.getClientsFromDB();
  }

  @GetMapping("/{id}")
  public ClientModel getClientById(@PathVariable Long id) {
    // return clientService.clientById(id);
    return clientService.getByIdFromDB(id);
  }

  @PostMapping
  public String addClient(@RequestBody ClientModel client) {
    // clientService.addClient(client);
    // return "new client " + client.getId() + " added successfully";
    ClientModel addedClient = clientService.addClientToDB(client);
    return "new client " + addedClient.getId() + " added successfully";
  }

  @PostMapping("/bulk")
  public List<String> addClientBulk(@RequestBody List<ClientModel> clients) {
    List<String> addedClientsIds = new ArrayList<>();
    clients.forEach(cl -> {
      ClientModel newClient = clientService.addClientToDB(cl);
      addedClientsIds.add("new client " + newClient.getId() + " added successfully");
    });
    return addedClientsIds;
  }

  @PutMapping("/{id}")
  public ClientModel updateClient(@PathVariable Long id, @RequestBody ClientModel updates) {
    // return clientService.updateClient(id, updates);
    return clientService.updateClientInDB(id, updates);
  }

  @DeleteMapping("/{id}")
  public String deleteClient(@PathVariable Long id) {
    // clientService.deleteClient(id);
    clientService.deleteClientFromDB(id);
    return "client " + id + " deleted successfully";
  }

}
