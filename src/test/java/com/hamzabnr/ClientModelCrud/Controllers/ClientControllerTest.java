package com.hamzabnr.ClientModelCrud.Controllers;

import com.hamzabnr.ClientModelCrud.Models.ClientModel;
import com.hamzabnr.ClientModelCrud.Repositories.ClientRepository;
import com.hamzabnr.ClientModelCrud.dto.ClientDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

// Tells Spring to use the test profile when running the test.
// Loads configuration files like application-test.properties
@ActiveProfiles("test")
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ClientControllerTest {

  @Autowired
  private ClientRepository clientRepository;

  @Autowired
  TestRestTemplate testRestTemplate;

  @BeforeEach
  void setup() {
    clientRepository.deleteAll();
    clientRepository.save(new ClientModel(null, "Alice", "alice@example.com"));
    clientRepository.save(new ClientModel(null, "Bob", "bob@example.com"));
    clientRepository.save(new ClientModel(null, "Marco", "marco@example.com"));
  }

  @Test
  public void shouldGetHealthCheck() {
    String result =  this.testRestTemplate.getForObject("/health", String.class);
    assert(result.equals("Api is running ..."));
  }

  @Test
  void shouldGetAllClients() {
    // Act
    ResponseEntity<Map> response = testRestTemplate.getForEntity("/clients", Map.class);

    // Assert status
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    // Body should not be null
    Map<String, Object> body = response.getBody();
    assertThat(body).isNotNull();

    // Extract "clients"
    List<Map<String, Object>> clients = (List<Map<String, Object>>) body.get("clients");

    // Now we can assert on the clients list
    assertThat(clients).hasSize(3);

    assertThat(clients.get(0).get("name")).isEqualTo("alice@example.com");
    assertThat(clients.get(0).get("email")).isEqualTo("Alice");

    assertThat(clients.get(1).get("name")).isEqualTo("bob@example.com");
    assertThat(clients.get(1).get("email")).isEqualTo("Bob");
  }

  @Test
  public void shouldGetClientById() {
    // Arrange: save a new client to get his id
    ClientModel savedClient = clientRepository.save(new ClientModel("testClient@mail.com", "testClient"));
    Long clientId = savedClient.getId();

    ResponseEntity<Map> response = testRestTemplate.getForEntity("/clients/" + clientId, Map.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    Map<String, Object> client = response.getBody();
    assertThat(client).isNotNull();
    assertThat(client.get("name")).isEqualTo("testClient");
    assertThat(client.get("email")).isEqualTo("testClient@mail.com");
  }

  @Test
  @Transactional
  public void shouldAddClient() {
    // Arrange
    ClientDTO toAddClient = new ClientDTO("hamza", "hamza@gmail.com");
    // Act
    ResponseEntity<Map> response = testRestTemplate.postForEntity("/clients", toAddClient, Map.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    Map<String, Object> client = response.getBody();
    System.out.println(client);
    assertThat(client).isNotNull();
    assertThat(client.get("name")).isEqualTo("hamza");
    assertThat(client.get("email")).isEqualTo("hamza@gmail.com");
  }

  @Test
  public void shouldUpdateClient() {
    // Arrange: save a new client to get his id
    ClientModel savedClient = clientRepository.save(new ClientModel("testClient@mail.com", "testClient"));
    Long clientId = savedClient.getId();
    ResponseEntity<Map> response = testRestTemplate.exchange("/clients/" + clientId, HttpMethod.PUT, new HttpEntity<>(new ClientDTO("testClientModified", "testClientModified@mail.com")), Map.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    Map<String, Object> updatedClient = response.getBody();
    System.out.println(updatedClient);
    assertThat(updatedClient.get("name")).isEqualTo("testClientModified");
    assertThat(updatedClient.get("email")).isEqualTo("testClientModified@mail.com");
  }

  @Test
  public void shouldReturnNotFoundWhenClientNotFound() {
    ResponseEntity<Map> response = testRestTemplate.getForEntity("/clients/" + 5, Map.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void shoudReturnInvalidEmailFormat() {
    ClientDTO toAdd = new ClientDTO("name", "wrong email format");
    ResponseEntity<Map> response = testRestTemplate.postForEntity("/clients", toAdd, Map.class);
    Map<String, Object> body = response.getBody();
    Map<String, Object> errorMessage = (Map<String, Object>) body.get("messages");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(errorMessage).isNotNull();
    assertThat(errorMessage.get("email")).isEqualTo("Invalid email format");
  }

}
