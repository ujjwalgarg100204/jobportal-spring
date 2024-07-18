package com.ujjwalgarg.jobportal.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ujjwalgarg.jobportal.annotation.WithMockRecruiter;
import com.ujjwalgarg.jobportal.entity.Address;
import com.ujjwalgarg.jobportal.entity.ContactInformation;
import com.ujjwalgarg.jobportal.entity.Education;
import com.ujjwalgarg.jobportal.entity.Interest;
import com.ujjwalgarg.jobportal.entity.RecruiterProfile;
import com.ujjwalgarg.jobportal.repository.RecruiterProfileRepository;
import com.ujjwalgarg.jobportal.service.FileUploadService;
import com.ujjwalgarg.jobportal.service.dto.recruiterprofileservice.RecruiterProfileUpdateDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ContextConfiguration
@WithMockRecruiter
class RecruiterProfileControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FileUploadService service;

  @Autowired
  private RecruiterProfileRepository recruiterProfileRepository;

  @Autowired
  private ObjectMapper objectMapper;

  static Stream<Arguments> profileInvalidFields() {
    return Stream.of(
        Arguments.of(
            RecruiterProfileUpdateDTO.builder()
                .firstName("Jane")
                .lastName("Smith")
                .about("Updated about")
                .companyId(1)
                .build()
        ), // missing id
        Arguments.of(
            RecruiterProfileUpdateDTO.builder()
                .id(1)
                .firstName("")
                .lastName("Smith")
                .about("Updated about")
                .companyId(1)
                .build()
        ),// First Name is mandatory
        Arguments.of(
            RecruiterProfileUpdateDTO.builder()
                .id(1)
                .firstName("Jane")
                .lastName("")
                .about("Updated about")
                .companyId(1)
                .build()
        ),// Last Name is mandatory
        Arguments.of(
            RecruiterProfileUpdateDTO.builder()
                .id(1)
                .firstName("Jane")
                .lastName("Smith")
                .about("a".repeat(10001))
                .companyId(1)
                .build()
        ), // Maximum number of characters allowed is 10,000
        Arguments.of(
            RecruiterProfileUpdateDTO.builder()
                .id(1)
                .firstName("Jane")
                .lastName("Smith")
                .about("Updated about")
                .companyId(1)
                .companyName("SomeCompany")
                .build()
        ),// Company name must be null if company ID is specified.
        Arguments.of(
            RecruiterProfileUpdateDTO.builder()
                .id(1)
                .firstName("Jane")
                .lastName("Smith")
                .about("Updated about")
                .companyId(1)
                .companyAddressState("CA")
                .build()
        ),// Company state must be null if company ID is specified.
        Arguments.of(
            RecruiterProfileUpdateDTO.builder()
                .id(1)
                .firstName("Jane")
                .lastName("Smith")
                .about("Updated about")
                .companyId(1)
                .companyAddressCountry("USA")
                .build()
        ),// Company country must be null if company ID is specified.
        Arguments.of(
            RecruiterProfileUpdateDTO.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .about("About text")
                .companyId(1)
                .address(Address.builder()
                    .state("")
                    .country("Country")
                    .build())
                .build()
        ),// Invalid: state is blank
        Arguments.of(
            RecruiterProfileUpdateDTO.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .about("About text")
                .companyId(1)
                .address(Address.builder()
                    .state("State")
                    .country("")
                    .build())
                .build()
        ),// Invalid: country is blank
        Arguments.of(
            RecruiterProfileUpdateDTO.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .about("About text")
                .companyId(1)
                .contactInformation(ContactInformation.builder()
                    .linkedinHandle("invalid-url")
                    .build())
                .build()
        ),// Invalid: URL is malformed
        Arguments.of(
            RecruiterProfileUpdateDTO.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .about("About text")
                .companyId(1)
                .educations(List.of(
                    Education.builder()
                        .title("")
                        .description("Description")
                        .build()))
                .build()
        ),// Invalid: title is blank
        Arguments.of(
            RecruiterProfileUpdateDTO.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .about("About text")
                .companyId(1)
                .educations(List.of(
                    Education.builder()
                        .title("Title")
                        .description("")
                        .build()))
                .build()
        ),// Invalid: description is blank
        Arguments.of(
            RecruiterProfileUpdateDTO.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .about("About text")
                .companyId(1)
                .interests(List.of(
                    Interest.builder()
                        .title("")
                        .build()))
                .build()
        )// Invalid: title is blank
    );
  }

  static Stream<Arguments> provideInvalidProfilePhotoMultipartFiles() {
    return Stream.of(
        Arguments.of(
            new MockMultipartFile("profilePhoto", "profile.txt", "text/plain",
                "invalid content".getBytes())  // Invalid content type
        ),
        Arguments.of(
            new MockMultipartFile("profilePhoto", "profile.jpg", "image/jpeg", new byte[0])
            // File size must be between 100 bytes and 5 MB
        ),
        Arguments.of(
            new MockMultipartFile("profilePhoto", "profile.jpg", "image/jpeg", new byte[6000000])
            // File size must be between 100 bytes and 5 MB
        )
    );
  }

  @BeforeEach
  void setUp() {
    when(service.uploadFile(any(), any())).thenReturn(true);
    doNothing().when(service).deleteFile(anyString());
  }

  @SqlGroup({
      @Sql(scripts = "/seed-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
  })
  @Test
  @DisplayName("Update address")
  @Transactional
  void updateRecruiterProfile_UpdateAddress() throws Exception {
    var existingProfile = recruiterProfileRepository.findById(2).orElseThrow();

    var updatedAddress = Address.builder()
        .id(existingProfile.getAddress().getId())
        .city("Updated City")
        .state("Updated State")
        .country("Updated Country")
        .build();

    var dto = getRecruiterProfileUpdateDTO(existingProfile);
    dto.setAddress(updatedAddress);

    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/recruiter/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString());

    RecruiterProfile updatedProfile = recruiterProfileRepository.findById(existingProfile.getId())
        .orElseThrow();

    Address updatedAddr = updatedProfile.getAddress();

    assertThat(updatedAddr.getCity()).isEqualTo("Updated City");
    assertThat(updatedAddr.getState()).isEqualTo("Updated State");
    assertThat(updatedAddr.getCountry()).isEqualTo("Updated Country");
  }

  @SqlGroup({
      @Sql(scripts = "/seed-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
  })
  @Test
  @DisplayName("Update contact information")
  @Transactional
  void updateRecruiterProfile_UpdateContactInformation() throws Exception {
    var existingProfile = recruiterProfileRepository.findById(2).orElseThrow();

    var updatedContactInformation = ContactInformation.builder()
        .id(existingProfile.getContactInformation().getId())
        .phone("Updated Phone")
        .twitterHandle("https://twitter.com/updated")
        .linkedinHandle("https://linkedin.com/updated")
        .githubHandle("https://github.com/updated")
        .build();

    var dto = getRecruiterProfileUpdateDTO(existingProfile);
    dto.setContactInformation(updatedContactInformation);

    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/recruiter/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString());

    RecruiterProfile updatedProfile = recruiterProfileRepository.findById(existingProfile.getId())
        .orElseThrow();

    ContactInformation updatedContactInfo = updatedProfile.getContactInformation();

    assertThat(updatedContactInfo.getPhone()).isEqualTo("Updated Phone");
    assertThat(updatedContactInfo.getTwitterHandle()).isEqualTo("https://twitter.com/updated");
    assertThat(updatedContactInfo.getLinkedinHandle()).isEqualTo("https://linkedin.com/updated");
    assertThat(updatedContactInfo.getGithubHandle()).isEqualTo("https://github.com/updated");
  }

  @SqlGroup({
      @Sql(scripts = "/seed-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
  })
  @Test
  @DisplayName("Delete interests")
  @Transactional
  void updateRecruiterProfile_DeleteInterests() throws Exception {
    var existingProfile = recruiterProfileRepository.findById(2).orElseThrow();
    var dto = getRecruiterProfileUpdateDTO(existingProfile);
    dto.setInterests(new ArrayList<>());

    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/recruiter/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString());

    RecruiterProfile updatedProfile = recruiterProfileRepository.findById(existingProfile.getId())
        .orElseThrow();

    Assertions.assertThat(updatedProfile.getInterests()).isEmpty();
  }

  @SqlGroup({
      @Sql(scripts = "/seed-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
  })
  @Test
  @DisplayName("Update existing interests")
  @Transactional
  void updateRecruiterProfile_UpdateExistingInterests() throws Exception {
    var existingProfile = recruiterProfileRepository.findById(2).orElseThrow();
    var existingInterest = existingProfile.getInterests().getFirst();

    var updatedInterest = Interest.builder()
        .id(existingInterest.getId())
        .title("Updated Interest")
        .build();

    var dto = getRecruiterProfileUpdateDTO(existingProfile);
    dto.setInterests(List.of(updatedInterest));

    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/recruiter/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString());

    RecruiterProfile updatedProfile = recruiterProfileRepository.findById(existingProfile.getId())
        .orElseThrow();

    Interest updatedInt = updatedProfile.getInterests().stream()
        .filter(interest -> interest.getId().equals(existingInterest.getId()))
        .findFirst()
        .orElseThrow();

    assertThat(updatedInt.getTitle()).isEqualTo("Updated Interest");
  }

  @SqlGroup({
      @Sql(scripts = "/seed-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
  })
  @Test
  @DisplayName("Create new interests")
  @Transactional
  void updateRecruiterProfile_CreateNewInterests() throws Exception {
    var existingProfile = recruiterProfileRepository.findById(2).orElseThrow();
    var dto = getRecruiterProfileUpdateDTO(existingProfile);
    dto.setInterests(List.of(
        existingProfile.getInterests().getFirst(),
        Interest.builder().title("New Interest 1").build(),
        Interest.builder().title("New Interest 2").build()));

    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/recruiter/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString());

    RecruiterProfile updatedProfile = recruiterProfileRepository.findById(existingProfile.getId())
        .orElseThrow();

    List<Interest> updatedInterests = updatedProfile.getInterests();

    Assertions.assertThat(updatedInterests).hasSize(3);
    assertThat(updatedInterests.get(1).getTitle()).isEqualTo("New Interest 1");
    assertThat(updatedInterests.get(2).getTitle()).isEqualTo("New Interest 2");
  }

  @SqlGroup({
      @Sql(scripts = "/seed-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
  })
  @Test
  @DisplayName("Deletes education if id is missing")
  @Transactional
  void updateRecruiterProfile_DeletesExistingEducations_WhenIdIsMissing() throws Exception {
    // save some education to profile
    var existingProfile = recruiterProfileRepository.findById(2).orElseThrow();
    List<Education> educations = new ArrayList<>();
    educations.add(Education.builder()
        .title("Education 1")
        .description("Description 1")
        .build());
    educations.add(Education.builder()
        .title("Education 2")
        .description("Description 2")
        .build());
    existingProfile.setEducations(educations);
    existingProfile = recruiterProfileRepository.save(existingProfile);

    var dto = getRecruiterProfileUpdateDTO(existingProfile);
    dto.setEducations(List.of(existingProfile.getEducations().getFirst()));

    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/recruiter/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString());

    RecruiterProfile updatedProfile = recruiterProfileRepository.findById(existingProfile.getId())
        .orElseThrow();

    Assertions.assertThat(updatedProfile.getEducations()).hasSize(1);

    assertThat(updatedProfile.getEducations().getFirst().getTitle()).isEqualTo("Education 1");
    assertThat(updatedProfile.getEducations().getFirst().getDescription()).isEqualTo(
        "Description 1");
  }

  @SqlGroup({
      @Sql(scripts = "/seed-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
  })
  @Test
  @DisplayName("Update existing educations")
  @Transactional
  void updateRecruiterProfile_UpdateExistingEducations() throws Exception {
    // save some education to profile
    var existingProfile = recruiterProfileRepository.findById(2).orElseThrow();
    List<Education> educations = new ArrayList<>();
    educations.add(Education.builder()
        .title("Education 1")
        .description("Description 1")
        .build());
    educations.add(Education.builder()
        .title("Education 2")
        .description("Description 2")
        .build());
    existingProfile.setEducations(educations);
    existingProfile = recruiterProfileRepository.save(existingProfile);

    var existingEducation = recruiterProfileRepository.findById(2).orElseThrow().getEducations()
        .getFirst();

    var updatedEducation = Education.builder()
        .id(existingEducation.getId())
        .title("Updated Title")
        .description("Updated Description")
        .build();

    var dto = getRecruiterProfileUpdateDTO(existingProfile);
    dto.setEducations(List.of(updatedEducation, existingProfile.getEducations().get(1)));

    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/recruiter/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString());

    RecruiterProfile updatedProfile = recruiterProfileRepository.findById(existingProfile.getId())
        .orElseThrow();

    Education updatedEdu = updatedProfile.getEducations().stream()
        .filter(edu -> edu.getId().equals(existingEducation.getId()))
        .findFirst()
        .orElseThrow();

    assertThat(updatedEdu.getTitle()).isEqualTo("Updated Title");
    assertThat(updatedEdu.getDescription()).isEqualTo("Updated Description");

    Education unUpdatedEdu = updatedProfile.getEducations().stream()
        .filter(edu -> !edu.getId().equals(existingEducation.getId()))
        .findFirst()
        .orElseThrow();
    assertThat(unUpdatedEdu.getTitle()).isEqualTo("Education 2");
    assertThat(unUpdatedEdu.getDescription()).isEqualTo("Description 2");
    Assertions.assertThat(updatedProfile.getEducations()).hasSize(2);
  }

  @SqlGroup({
      @Sql(scripts = "/seed-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
  })
  @Test
  @DisplayName("Create new educations")
  @Transactional
  void updateRecruiterProfile_CreateNewEducations() throws Exception {
    var existingProfile = recruiterProfileRepository.findById(2).orElseThrow();
    var dto = getRecruiterProfileUpdateDTO(existingProfile);
    dto.setEducations(List.of(
        Education.builder().title("New Title 1").description("New Description 1").build(),
        Education.builder().title("New Title 2").description("New Description 2").build())
    );

    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/recruiter/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString());

    RecruiterProfile updatedProfile = recruiterProfileRepository.findById(existingProfile.getId())
        .orElseThrow();

    List<Education> updatedEducations = updatedProfile.getEducations();

    Assertions.assertThat(updatedEducations).hasSize(2);
    assertThat(updatedEducations.get(0).getTitle()).isEqualTo("New Title 1");
    assertThat(updatedEducations.get(0).getDescription()).isEqualTo("New Description 1");
    assertThat(updatedEducations.get(1).getTitle()).isEqualTo("New Title 2");
    assertThat(updatedEducations.get(1).getDescription()).isEqualTo("New Description 2");
  }

  @SqlGroup({
      @Sql(scripts = "/seed-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
  })
  @Test
  @DisplayName("Update with a new company creation")
  @Transactional
  void updateRecruiterProfile_CreateNewCompany() throws Exception {
    var existingProfile = recruiterProfileRepository.findById(2).orElseThrow();
    var dto = getRecruiterProfileUpdateDTO(existingProfile);
    dto.setFirstName("Jane");
    dto.setLastName("Smith");
    dto.setAbout("Updated about section.");
    dto.setCompanyId(null);
    dto.setCompanyName("New Company");
    dto.setCompanyAddressCity("New City");
    dto.setCompanyAddressState("New State");
    dto.setCompanyAddressCountry("New Country");

    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/recruiter/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString());

    // Retrieve the updated profile from the database
    RecruiterProfile updatedProfile = recruiterProfileRepository.findById(existingProfile.getId())
        .orElseThrow();
    assertThat(updatedProfile).isNotNull();
    assertThat(updatedProfile.getFirstName()).isEqualTo(dto.getFirstName());
    assertThat(updatedProfile.getLastName()).isEqualTo(dto.getLastName());
    assertThat(updatedProfile.getAbout()).isEqualTo(dto.getAbout());

    // Check if a new company has been created
    assertThat(updatedProfile.getCompany().getId()).isNotNull();
    assertThat(updatedProfile.getCompany().getName()).isEqualTo(dto.getCompanyName());
    assertThat(updatedProfile.getCompany().getAddress().getCity()).isEqualTo(
        dto.getCompanyAddressCity());
    assertThat(updatedProfile.getCompany().getAddress().getState()).isEqualTo(
        dto.getCompanyAddressState());
    assertThat(updatedProfile.getCompany().getAddress().getCountry()).isEqualTo(
        dto.getCompanyAddressCountry());
  }

  @SqlGroup({
      @Sql(scripts = "/seed-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
  })
  @Test
  @DisplayName("Update firstName, lastName, and about fields")
  @Transactional
  void updateRecruiterProfile_ChangeNameAndAbout() throws Exception {
    var existingProfile = recruiterProfileRepository.findById(2).orElseThrow();
    var dto = getRecruiterProfileUpdateDTO(existingProfile);
    dto.setFirstName("Ram");
    dto.setLastName("Rohan");
    dto.setAbout("Updated about section.");

    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/recruiter/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString());

    // Retrieve the profile again to ensure the changes
    var updatedProfile = recruiterProfileRepository.findById(2).orElseThrow();

    assertThat(updatedProfile.getFirstName()).isEqualTo(dto.getFirstName());
    assertThat(updatedProfile.getLastName()).isEqualTo(dto.getLastName());
    assertThat(updatedProfile.getAbout()).isEqualTo(dto.getAbout());
    assertThat(updatedProfile.getCompany()).isEqualTo(existingProfile.getCompany());
  }

  @SqlGroup({
      @Sql(scripts = "/seed-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
  })
  @Test
  @Transactional
  void updateRecruiterProfile_withValidCompanyId_ReturnsOk() throws Exception {
    var existingProfile = recruiterProfileRepository.findById(2).orElseThrow();
    var dto = getRecruiterProfileUpdateDTO(existingProfile);
    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/recruiter/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString())
        .andExpect(jsonPath("$.data").doesNotHaveJsonPath());

    var updatedProfile = recruiterProfileRepository.findById(2).orElseThrow();
    assertThat(updatedProfile.getFirstName()).isEqualTo(existingProfile.getFirstName());
    assertThat(updatedProfile.getLastName()).isEqualTo(existingProfile.getLastName());
    assertThat(updatedProfile.getAbout()).isEqualTo(existingProfile.getAbout());
    assertThat(updatedProfile.getCompany()).isEqualTo(existingProfile.getCompany());
  }

  @ParameterizedTest
  @MethodSource("profileInvalidFields")
  void updateRecruiterProfile_whenInvalidFields_ReturnsBadRequest(RecruiterProfileUpdateDTO dto)
      throws Exception {
    var requestBuilder = multipart(HttpMethod.PATCH, "/api/recruiter/profile")
        .file(convertToMultipartFile(dto))
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE);

    mockMvc.perform(requestBuilder)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.validationErrors").isArray())
        .andExpect(jsonPath("$.validationErrors[0]").isString());
  }

  @ParameterizedTest
  @MethodSource("provideInvalidProfilePhotoMultipartFiles")
  void updateRecruiterProfile_whenInvalidProfilePhoto_ReturnsBadRequest(MockMultipartFile file)
      throws Exception {
    var dto = getRecruiterProfileUpdateDTO(null);
    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/recruiter/profile")
                .file(file)
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.validationErrors").isArray())
        .andExpect(jsonPath("$.validationErrors[0]").isString());
  }

  private RecruiterProfileUpdateDTO getRecruiterProfileUpdateDTO(RecruiterProfile profile) {
    if (profile != null) {
      return RecruiterProfileUpdateDTO.builder()
          .id(profile.getId())
          .firstName(profile.getFirstName())
          .lastName(profile.getLastName())
          .about(profile.getAbout())
          .address(profile.getAddress())
          .contactInformation(profile.getContactInformation())
          .educations(profile.getEducations())
          .interests(profile.getInterests())
          .companyId(profile.getCompany().getId())
          .build();
    } else {
      return RecruiterProfileUpdateDTO.builder()
          .id(1)
          .firstName("John")
          .lastName("Doe")
          .about("About text")
          .companyId(1)
          .build();
    }
  }

  private MockMultipartFile convertToMultipartFile(RecruiterProfileUpdateDTO dto) throws Exception {
    return new MockMultipartFile("updateDetails", "", MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(dto));
  }
}
