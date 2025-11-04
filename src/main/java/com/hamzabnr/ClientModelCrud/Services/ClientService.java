package com.hamzabnr.ClientModelCrud.Services;

import com.hamzabnr.ClientModelCrud.Exceptions.ClientNotFoundException;
import com.hamzabnr.ClientModelCrud.Models.ClientModel;
import com.hamzabnr.ClientModelCrud.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Service
public class ClientService {

  // In-memory list to simulate a database
  private final List<ClientModel> clientsList = new ArrayList<>(List.of(
      new ClientModel(1L, "name client 1", "email client 1"),
      new ClientModel(2L, "name client 2", "email client 2")
  ));

  public List<ClientModel> getAllClients() {
    return clientsList;
  }

  public ClientModel clientById(Long id) {
    ClientModel client = clientsList.stream().filter(cl -> cl.getId().equals(id)).
        findFirst().orElseThrow(() -> new ClientNotFoundException(id));;
    return client;
  }

  public void addClient(ClientModel client) {
    clientsList.add(client);
  }

  public ClientModel updateClient(Long id, ClientModel updates) {
    ClientModel client = clientById(id);
    if (client != null) {
      if (updates.getName() != null) {
        client.setName(updates.getName());
      }
      if (updates.getEmail() != null) {
        client.setEmail(updates.getEmail());
      }
      return client;
    }
    return null;
  }

  public void deleteClient(Long id) {
    clientsList.removeIf(cl -> cl.getId().equals(id));
  }


  // Using database instead of in-memory list
  private final ClientRepository clientRepository;

  public ClientService(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  public ClientModel addClientToDB(ClientModel client) {
    return clientRepository.save(client);
  }

  public List<ClientModel> getClientsFromDB() {
    return clientRepository.findAll();
  }

  public ClientModel getByIdFromDB(Long id) {
    return clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
  }

  public void deleteClientFromDB(Long id) {
    clientRepository.deleteById(id);
  }

  public ClientModel updateClientInDB(Long id, ClientModel client) {
    ClientModel existingClient = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
    if (client.getName() != null) {
      existingClient.setName(client.getName());
    }
    if (client.getEmail() != null) {
      existingClient.setEmail(client.getEmail());
    }
    clientRepository.save(existingClient);
    return existingClient;
  }
}

