package com.hamzabnr.ClientModelCrud.Services;

import ch.qos.logback.core.net.server.Client;
import com.hamzabnr.ClientModelCrud.Exceptions.ClientNotFoundException;
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
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
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
    // Arrange: modelMapper.map(...) should convert ClientModel -> ClientDTO
    // Use a generic Answer so we don't need to stub per-instance mapping
    when(modelMapper.map(any(ClientModel.class), eq(ClientDTO.class)))
        .thenAnswer(invocation -> {
          ClientModel src = invocation.getArgument(0);
          return new ClientDTO(src.getName(), src.getEmail());
        });

    // Arrange: repository returns two clients
    when(clientRepository.findAll()).thenReturn(List.of(c1, c2));

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

  @Test
  void shouldGetClientById() {
    // Arrange: modelMapper.map(...) should convert ClientModel -> ClientDTO
    // Use a generic Answer so we don't need to stub per-instance mapping
    when(modelMapper.map(any(ClientModel.class), eq(ClientDTO.class)))
        .thenAnswer(invocation -> {
          ClientModel src = invocation.getArgument(0);
          return new ClientDTO(src.getName(), src.getEmail());
        });
    // Arrange: repository should return client c2 when searched by id: 2L
    when((clientRepository.findById(2L))).thenReturn(Optional.ofNullable(c2));

    // Act
    ClientDTO result = clientService.getByIdFromDB(2L);

    // Assert
    assertThat(result).isNotNull();

    assertThat(result.getName()).isEqualTo("Bob");
    assertThat(result.getEmail()).isEqualTo("bob@example.com");
  }

  @Test
  void shouldAddClient() {
    // Arrange: modelMapper.map(...) should convert ClientModel -> ClientDTO
    // Use a generic Answer so we don't need to stub per-instance mapping
    when(modelMapper.map(any(ClientModel.class), eq(ClientDTO.class)))
        .thenAnswer(invocation -> {
          ClientModel src = invocation.getArgument(0);
          return new ClientDTO(src.getName(), src.getEmail());
        });
    // Arrange repository to return saved client
    when(clientRepository.save(any(ClientModel.class))).thenReturn(c1);

    // mocking reverse mapping now, ClientDTO -> ClientModel
    when(modelMapper.map(any(ClientDTO.class), eq(ClientModel.class)))
        .thenAnswer(invocation -> {
          ClientDTO src = invocation.getArgument(0);
          return new ClientModel(src.getEmail(), src.getName());
        });

    // Act
    // at this step i had to mock the mapping from ClientDTO -> ClientModel
    ClientDTO result = clientService.addClientToDB(new ClientDTO(c1.getName(), c1.getEmail()));

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("Alice");
    assertThat(result.getEmail()).isEqualTo("alice@example.com");
  }

  @Test
  void shouldUpdateClient() {
    // Arrange: modelMapper.map(...) should convert ClientModel -> ClientDTO
    // Use a generic Answer so we don't need to stub per-instance mapping
    when(modelMapper.map(any(ClientModel.class), eq(ClientDTO.class)))
        .thenAnswer(invocation -> {
          ClientModel src = invocation.getArgument(0);
          return new ClientDTO(src.getName(), src.getEmail());
        });
    // Arrange repository to return existing client
    when(clientRepository.findById(2L)).thenReturn(Optional.ofNullable(c2));
    // this will return the saved object as it is
    when(clientRepository.save(any(ClientModel.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    ClientDTO result = clientService.updateClientInDB(c2.getId(), new ClientDTO("modified" + c2.getName(), "modified" + c2.getEmail()));

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getEmail()).isEqualTo("modifiedbob@example.com");
    assertThat(result.getName()).isEqualTo("modifiedBob");

    verify(clientRepository, times(1)).save(any(ClientModel.class));
  }

  @Test
  void shouldThrowWhenClientNotFound() {
    // Arrange
    Long id = 99L;
    ClientDTO updates = new ClientDTO("Does not matter", "new@email.com");
    when(clientRepository.findById(id)).thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() -> clientService.updateClientInDB(id, updates))
        .isInstanceOf(ClientNotFoundException.class);
  }

}
