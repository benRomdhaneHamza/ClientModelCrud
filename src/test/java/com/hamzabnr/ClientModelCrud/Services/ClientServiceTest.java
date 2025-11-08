package com.hamzabnr.ClientModelCrud.Services;

import com.hamzabnr.ClientModelCrud.Models.ClientModel;
import com.hamzabnr.ClientModelCrud.Repositories.ClientRepository;
import com.hamzabnr.ClientModelCrud.dto.ClientDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

  @Mock
  private ClientRepository clientRepository;

  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  ClientService clientService;

  private ClientModel c1;
  private ClientModel c2;

  @BeforeEach
  void setup() {
    c1 = new ClientModel(1L, "alice@example.com", "Alice");
    c2 = new ClientModel(2L, "bob@example.com", "Bob");
  }

  @Test
  void shouldGetAllClients() {
    // Arrange: repository returns two clients
    when(clientRepository.findAll()).thenReturn(List.of(c1, c2));

    // Arrange: modelMapper.map(...) should convert ClientModel -> ClientDTO
    // Use a generic Answer so we don't need to stub per-instance mapping
    when(modelMapper.map(any(ClientModel.class), eq(ClientDTO.class)))
        .thenAnswer(invocation -> {
          ClientModel src = invocation.getArgument(0);
          return new ClientDTO(src.getName(), src.getEmail());
        });

    // Act
    List<ClientDTO> result = clientService.getClientsFromDB();

    // Assert
    assertThat(result).isNotNull();
    assertThat(result).hasSize(2);

    assertThat(result.get(0).getName()).isEqualTo("Alice");
    assertThat(result.get(0).getEmail()).isEqualTo("alice@example.com");

    assertThat(result.get(1).getName()).isEqualTo("Bob");
    assertThat(result.get(1).getEmail()).isEqualTo("bob@example.com");

  }

}
