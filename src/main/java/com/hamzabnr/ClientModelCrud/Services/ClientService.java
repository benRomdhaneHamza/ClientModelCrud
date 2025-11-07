package com.hamzabnr.ClientModelCrud.Services;

import com.hamzabnr.ClientModelCrud.Config.ModelMapperConfig;
import com.hamzabnr.ClientModelCrud.Exceptions.ClientNotFoundException;
import com.hamzabnr.ClientModelCrud.Models.ClientModel;
import com.hamzabnr.ClientModelCrud.Repositories.ClientRepository;
import com.hamzabnr.ClientModelCrud.dto.ClientDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
  private final ModelMapper modelMapper;

  public ClientService(ClientRepository clientRepository, ModelMapper modelMapper) {
    this.clientRepository = clientRepository;
    this.modelMapper = modelMapper;
  }

  public ClientDTO addClientToDB(ClientDTO client) {
    ClientModel savedClient = clientRepository.save(modelMapper.map(client, ClientModel.class));
    // return new ClientDTO(savedClient.getName(), savedClient.getEmail());
    // using a ModelMapper to map ClientModel to ClientDTO
    return modelMapper.map(savedClient, ClientDTO.class);
  }

  public List<ClientDTO> getClientsFromDB() {
//    List<ClientModel> clients = clientRepository.findAll();
//    List<ClientDTO> clsDTO = new ArrayList<>();
//    clients.forEach(cl -> {
//      clsDTO.add(new ClientDTO(cl.getName(), cl.getEmail()));
//    });
//    return clsDTO;

    // using a ModelMapper to map ClientModel to ClientDTO
    return clientRepository.findAll()
        .stream()
        .map(cl -> modelMapper.map(cl, ClientDTO.class))
        .toList();
  }

  public ClientDTO getByIdFromDB(Long id) {
    ClientModel client = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
    // return new ClientDTO(client.getName(), client.getEmail());
    // using a ModelMapper to map ClientModel to ClientDTO
    return modelMapper.map(client, ClientDTO.class);
  }

  public void deleteClientFromDB(Long id) {
    clientRepository.deleteById(id);
  }

  public ClientDTO updateClientInDB(Long id, ClientDTO client) {
    ClientModel existingClient = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
    if (client.getName() != null) {
      existingClient.setName(client.getName());
    }
    if (client.getEmail() != null) {
      existingClient.setEmail(client.getEmail());
    }
    clientRepository.save(existingClient);
    return modelMapper.map(existingClient, ClientDTO.class);
  }
}

