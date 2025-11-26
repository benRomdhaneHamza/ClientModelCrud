package com.hamzabnr.ClientModelCrud.Controllers;

import com.hamzabnr.ClientModelCrud.Models.ClientModel;
import com.hamzabnr.ClientModelCrud.Models.UserModel;
import com.hamzabnr.ClientModelCrud.Repositories.ClientRepository;
import com.hamzabnr.ClientModelCrud.Repositories.UserRepository;
import com.hamzabnr.ClientModelCrud.Security.JwtUtil;
import com.hamzabnr.ClientModelCrud.TestUtils.AuthTestHelper;
import com.hamzabnr.ClientModelCrud.dto.ClientDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

// Tells Spring to use the test profile when running the test.
// Loads configuration files like application-test.properties
@ActiveProfiles("test")
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ClientControllerTest {

  private static final Logger log = LoggerFactory.getLogger(ClientControllerTest.class);

  @Autowired
  private ClientRepository clientRepository;
  @Autowired
  private AuthTestHelper authTestHelper;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  TestRestTemplate testRestTemplate;

  @BeforeEach
  void setup() {
    clientRepository.deleteAll();
    clientRepository.save(new ClientModel(null, "Alice", "alice@example.com"));
    clientRepository.save(new ClientModel(null, "Bob", "bob@example.com"));
    clientRepository.save(new ClientModel(null, "Marco", "marco@example.com"));
  }

  public <T> HttpEntity<T> getEntityWithHeaders(@Nullable T requestBody) {
    String adminToken = authTestHelper.getAdminToken();
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(adminToken);

    // You can add common headers here
    headers.setContentType(MediaType.APPLICATION_JSON);

    return new HttpEntity<>(requestBody, headers);
  }

  @Test
  public void shouldGetHealthCheck() {
    ResponseEntity<String> response = testRestTemplate.exchange("/health", HttpMethod.GET, getEntityWithHeaders(null), String.class);
    String result = response.getBody();
    assert(result.equals("Api is running ..."));
  }

  @Test
  public void shouldReturnNotFoundWhenClientNotFound() {
    ResponseEntity<Map> response = testRestTemplate.exchange("/clients/999", HttpMethod.GET, getEntityWithHeaders(null), Map.class);
    log.trace(response.toString());
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldGetAllClients() {
    // Act
    ResponseEntity<Map> response = testRestTemplate.exchange("/clients", HttpMethod.GET, getEntityWithHeaders(null), Map.class);
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

    ResponseEntity<Map> response = testRestTemplate.exchange("/clients/" + clientId, HttpMethod.GET, getEntityWithHeaders(null), Map.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    Map<String, Object> client = response.getBody();
    assertThat(client).isNotNull();
    assertThat(client.get("name")).isEqualTo("testClient");
    assertThat(client.get("email")).isEqualTo("testClient@mail.com");
  }

  @Test
  public void shouldAddClient() {
    // Arrange
    ClientDTO toAddClient = new ClientDTO("hamza", "hamza@gmail.com");
    // Act
    HttpEntity<ClientDTO> entity = getEntityWithHeaders(toAddClient);
    ResponseEntity<Map> response = testRestTemplate.exchange("/clients", HttpMethod.POST, entity, Map.class);
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
    ResponseEntity<Map> response = testRestTemplate.exchange("/clients/" + clientId, HttpMethod.PUT, getEntityWithHeaders(new ClientDTO("testClientModified", "testClientModified@mail.com")), Map.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    Map<String, Object> updatedClient = response.getBody();
    System.out.println(updatedClient);
    assertThat(updatedClient.get("name")).isEqualTo("testClientModified");
    assertThat(updatedClient.get("email")).isEqualTo("testClientModified@mail.com");
  }


  @Test
  public void shoudReturnInvalidEmailFormat() {
    ClientDTO toAdd = new ClientDTO("name", "wrong email format");
    ResponseEntity<Map> response = testRestTemplate.exchange("/clients", HttpMethod.POST, getEntityWithHeaders(toAdd), Map.class);
    Map<String, Object> body = response.getBody();
    Map<String, Object> errorMessage = (Map<String, Object>) body.get("messages");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(errorMessage).isNotNull();
    assertThat(errorMessage.get("email")).isEqualTo("Invalid email format");
  }

}
