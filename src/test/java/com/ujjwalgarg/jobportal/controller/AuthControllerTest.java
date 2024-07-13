package com.ujjwalgarg.jobportal.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ujjwalgarg.jobportal.controller.payload.auth.LoginRequest;
import com.ujjwalgarg.jobportal.controller.payload.auth.NewCandidateRequest;
import com.ujjwalgarg.jobportal.controller.payload.auth.NewRecruiterRequest;
import com.ujjwalgarg.jobportal.entity.CandidateProfile;
import com.ujjwalgarg.jobportal.entity.Company;
import com.ujjwalgarg.jobportal.entity.User;
import com.ujjwalgarg.jobportal.repository.CompanyRepository;
import com.ujjwalgarg.jobportal.service.UserService;
import java.util.stream.Stream;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
@AutoConfigureMockMvc
@ContextConfiguration
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserService userService;
  @Autowired
  private CompanyRepository companyRepository;

  private static Stream<Arguments> invalidLoginRequestProvider() {
    return Stream.of(
        Arguments.of("", "password123"),
        Arguments.of("test@example.com", ""),
        Arguments.of("", ""),
        Arguments.of(null, null)
    );
  }

  @Test
  @DisplayName("Test loginUser() when given valid credentials, returns token and OK status")
  @Transactional
  void loginUser_WithValidCredentials_ReturnsTokenAndOkStatus() throws Exception {
    var user = User.builder().email("candidate@gmail.com").password("password").build();
    var cProfile = CandidateProfile.builder().firstName("Seymour").lastName("Jakubowski").build();
    userService.createNewCandidate(user, cProfile);
    LoginRequest validLoginRequest = new LoginRequest(user.getEmail(), "password");

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validLoginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data", CoreMatchers.isA(String.class)))
        .andExpect(jsonPath("$.message", CoreMatchers.isA(String.class)));
  }

  @Test
  @DisplayName("Test loginUser() when given unknown user credentials, throws BadCredentialsException and returns Unauthorized status")
  void loginUser_WithInvalidCredentials_ThrowsException() throws Exception {
    LoginRequest validLoginRequest = new LoginRequest("test@gmail.com", "password");

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validLoginRequest)))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.data").doesNotHaveJsonPath())
        .andExpect(jsonPath("$.message", CoreMatchers.isA(String.class)));
  }

  @ParameterizedTest
  @MethodSource("invalidLoginRequestProvider")
  @DisplayName("Test loginUser() when given invalid input, returns Bad Request status")
  void loginUser_WithInvalidInput_ReturnsBadRequest(String email, String password)
      throws Exception {
    LoginRequest invalidRequest = new LoginRequest(email, password);

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.data").doesNotHaveJsonPath())
        .andExpect(jsonPath("$.message", CoreMatchers.isA(String.class)))
        .andExpect(jsonPath("$.validationErrors").isArray());
  }

  @Test
  @DisplayName("Test createNewCandidate() Valid request creates a new candidate")
  void createNewCandidate_ValidRequest_CreatesNewCandidate() throws Exception {
    NewCandidateRequest validRequest = NewCandidateRequest.builder()
        .email("candidate@example.com")
        .password("password123")
        .firstName("John")
        .lastName("Doe")
        .shortAbout("Experienced developer")
        .twitterHandle("https://twitter.com/johndoe")
        .linkedinHandle("https://linkedin.com/in/johndoe")
        .githubHandle("https://github.com/johndoe")
        .phone("+1234567890")
        .build();

    mockMvc.perform(post("/api/auth/candidate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Successfully created new candidate"));
  }

  @ParameterizedTest
  @MethodSource("invalidCandidateRequests")
  @DisplayName("Test createNewCandidate() Invalid request returns appropriate error")
  void createNewCandidate_InvalidRequest_ReturnsError(NewCandidateRequest invalidRequest,
      String expectedInvalidField) throws Exception {
    mockMvc.perform(post("/api/auth/candidate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").isString())
        .andExpect(jsonPath("$.validationErrors").hasJsonPath())
        .andExpect(jsonPath("$.validationErrors").isArray())
        .andExpect(jsonPath("$.validationErrors[0].message").isString())
        .andExpect(jsonPath("$.validationErrors[0].field").value(expectedInvalidField));
  }

  private static Stream<Arguments> invalidCandidateRequests() {
    return Stream.of(
        // Empty email
        Arguments.of(
            NewCandidateRequest.builder().email("").password("password123").firstName("John")
                .lastName("Doe").shortAbout("About").build(),
            "email"),
        // Invalid email format
        Arguments.of(NewCandidateRequest.builder().email("invalid-email").password("password123")
                .firstName("John").lastName("Doe").shortAbout("About").build(),
            "email"),
        // Empty password
        Arguments.of(
            NewCandidateRequest.builder().email("valid@email.com").password("").firstName("John")
                .lastName("Doe").shortAbout("About").build(),
            "password"),
        // Empty first name
        Arguments.of(NewCandidateRequest.builder().email("valid@email.com").password("password123")
                .firstName("").lastName("Doe").shortAbout("About").build(),
            "firstName"),
        // Empty last name
        Arguments.of(NewCandidateRequest.builder().email("valid@email.com").password("password123")
                .firstName("John").lastName("").shortAbout("About").build(),
            "lastName"),
        // Empty short about
        Arguments.of(NewCandidateRequest.builder().email("valid@email.com").password("password123")
                .firstName("John").lastName("Doe").shortAbout("").build(),
            "shortAbout"),
        // Invalid URL for Twitter handle
        Arguments.of(NewCandidateRequest.builder().email("valid@email.com").password("password123")
                .firstName("John").lastName("Doe").shortAbout("About").twitterHandle("invalid-url")
                .build(),
            "twitterHandle"),
        // Invalid URL for LinkedIn handle
        Arguments.of(NewCandidateRequest.builder().email("valid@email.com").password("password123")
                .firstName("John").lastName("Doe").shortAbout("About").linkedinHandle("invalid-url")
                .build(),
            "linkedinHandle"),
        // Invalid URL for GitHub handle
        Arguments.of(NewCandidateRequest.builder().email("valid@email.com").password("password123")
                .firstName("John").lastName("Doe").shortAbout("About").githubHandle("invalid-url")
                .build(),
            "githubHandle")
    );
  }

  @Test
  @DisplayName("Test createNewRecruiter() Valid request with company ID creates a new recruiter")
  void createNewRecruiter_ValidRequestWithCompanyId_CreatesNewRecruiter() throws Exception {
    var company = companyRepository.save(Company.builder().name("Mosciski Inc").build());
    NewRecruiterRequest validRequest = NewRecruiterRequest.builder()
        .email("recruiter@example.com")
        .password("password123")
        .firstName("Jane")
        .lastName("Smith")
        .companyId(company.getId())
        .build();

    mockMvc.perform(post("/api/auth/recruiter")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Successfully created new recruiter"));
  }

  @Test
  @DisplayName("Test createNewRecruiter() Valid request with new company details creates a new recruiter")
  void createNewRecruiter_ValidRequestWithNewCompany_CreatesNewRecruiter() throws Exception {
    NewRecruiterRequest validRequest = NewRecruiterRequest.builder()
        .email("recruiter@newcompany.com")
        .password("password123")
        .firstName("Alice")
        .lastName("Johnson")
        .companyName("New Company Inc.")
        .companyAddressCity("New York")
        .companyAddressState("NY")
        .companyAddressCountry("USA")
        .build();

    mockMvc.perform(post("/api/auth/recruiter")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Successfully created new recruiter"));
  }

  @ParameterizedTest
  @MethodSource("invalidRecruiterRequests")
  @DisplayName("Test createNewRecruiter() Invalid request returns appropriate error")
  void createNewRecruiter_InvalidRequest_ReturnsError(NewRecruiterRequest invalidRequest,
      String expectedInvalidField) throws Exception {
    mockMvc.perform(post("/api/auth/recruiter")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").isString())
        .andExpect(jsonPath("$.validationErrors").exists())
        .andExpect(jsonPath("$.validationErrors").isArray())
        .andExpect(jsonPath(
            String.format("$.validationErrors[?(@.field == '%s')]", expectedInvalidField)).exists())
        .andExpect(jsonPath(
            String.format("$.validationErrors[?(@.field == '%s')].message",
                expectedInvalidField)).exists());
  }

  private static Stream<Arguments> invalidRecruiterRequests() {
    return Stream.of(
        // Empty email
        Arguments.of(
            NewRecruiterRequest.builder().email("").password("password123").firstName("Jane")
                .lastName("Smith").companyId(1).build(),
            "email"),
        // Invalid email format
        Arguments.of(NewRecruiterRequest.builder().email("invalid-email").password("password123")
                .firstName("Jane").lastName("Smith").companyId(1).build(),
            "email"),
        // Empty password
        Arguments.of(
            NewRecruiterRequest.builder().email("valid@email.com").password("").firstName("Jane")
                .lastName("Smith").companyId(1).build(),
            "password"),
        // Empty first name
        Arguments.of(NewRecruiterRequest.builder().email("valid@email.com").password("password123")
                .firstName("").lastName("Smith").companyId(1).build(),
            "firstName"),
        // Empty last name
        Arguments.of(NewRecruiterRequest.builder().email("valid@email.com").password("password123")
                .firstName("Jane").lastName("").companyId(1).build(),
            "lastName"),
        // Company ID provided but also company name (should be mutually exclusive)
        Arguments.of(NewRecruiterRequest.builder().email("valid@email.com").password("password123")
                .firstName("Jane").lastName("Smith").companyId(1).companyName("Test Company").build(),
            "companyName"),
        Arguments.of(NewRecruiterRequest.builder().email("valid@email.com").password("password123")
                .firstName("Jane").lastName("Smith").companyId(1).companyAddressCity("Test Company")
                .build(),
            "companyAddressCity"),
        Arguments.of(NewRecruiterRequest.builder().email("valid@email.com").password("password123")
                .firstName("Jane").lastName("Smith").companyId(1).companyAddressState("Test Company")
                .build(),
            "companyAddressState"),
        Arguments.of(NewRecruiterRequest.builder().email("valid@email.com").password("password123")
                .firstName("Jane").lastName("Smith").companyId(1).companyAddressCountry("Test Company")
                .build(),
            "companyAddressCountry"),
        // No company ID and missing required company details
        Arguments.of(NewRecruiterRequest.builder().email("valid@email.com").password("password123")
                .firstName("Jane").lastName("Smith").companyName("Test Company").build(),
            "companyAddressState"),
        Arguments.of(NewRecruiterRequest.builder().email("valid@email.com").password("password123")
                .firstName("Jane").lastName("Smith").companyName("Test Company").build(),
            "companyAddressCountry")
    );
  }
}
