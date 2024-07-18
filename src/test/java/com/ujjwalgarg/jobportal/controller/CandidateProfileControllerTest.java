package com.ujjwalgarg.jobportal.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ujjwalgarg.jobportal.annotation.WithMockCandidate;
import com.ujjwalgarg.jobportal.constant.ExperienceLevel;
import com.ujjwalgarg.jobportal.controller.payload.common.SkillDetailsDto;
import com.ujjwalgarg.jobportal.entity.Address;
import com.ujjwalgarg.jobportal.entity.CandidateProfile;
import com.ujjwalgarg.jobportal.entity.ContactInformation;
import com.ujjwalgarg.jobportal.entity.Education;
import com.ujjwalgarg.jobportal.entity.Interest;
import com.ujjwalgarg.jobportal.entity.Skill;
import com.ujjwalgarg.jobportal.repository.CandidateProfileRepository;
import com.ujjwalgarg.jobportal.service.FileUploadService;
import com.ujjwalgarg.jobportal.service.dto.candidateprofileservice.CandidateProfileUpdateDTO;
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
@WithMockCandidate
class CandidateProfileControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FileUploadService service;

  @Autowired
  private CandidateProfileRepository candidateProfileRepository;

  @Autowired
  private ObjectMapper objectMapper;

  static Stream<Arguments> profileInvalidFields() {
    var missingIdDto = getCandidateProfileUpdateDTO(null);
    missingIdDto.setId(null);

    var firstNameMandatoryDto = getCandidateProfileUpdateDTO(null);
    firstNameMandatoryDto.setFirstName(null);

    var lastNameMandatoryDto = getCandidateProfileUpdateDTO(null);
    lastNameMandatoryDto.setLastName(null);

    var allowedCharsInAboutDto = getCandidateProfileUpdateDTO(null);
    allowedCharsInAboutDto.setAbout("a".repeat(100001));

    var addressStateIsBlankDto = getCandidateProfileUpdateDTO(null);
    addressStateIsBlankDto.setAddress(Address.builder().country("India").build());

    var addressCountryIsBlankDto = getCandidateProfileUpdateDTO(null);
    addressCountryIsBlankDto.setAddress(Address.builder().state("India").build());

    var malformedPortfolioUrlDto = getCandidateProfileUpdateDTO(null);
    malformedPortfolioUrlDto.setPortfolioWebsite("badurl");

    var malformedContactInformationUrlDto = getCandidateProfileUpdateDTO(null);
    malformedContactInformationUrlDto.getContactInformation().setLinkedinHandle("invalid-url");

    var educationTitleBlankDto = getCandidateProfileUpdateDTO(null);
    educationTitleBlankDto.setEducations(List.of(
        Education.builder()
            .title("")
            .description("Description")
            .build()
    ));

    var educationDescriptionBlankDto = getCandidateProfileUpdateDTO(null);
    educationDescriptionBlankDto.setEducations(List.of(
        Education.builder()
            .title("Title")
            .description("")
            .build()
    ));

    var interestTitleBlankDto = getCandidateProfileUpdateDTO(null);
    interestTitleBlankDto.setInterests(List.of(
        Interest.builder()
            .title("")
            .build()));

    var skillNameBlankDto = getCandidateProfileUpdateDTO(null);
    skillNameBlankDto.setSkills(List.of(SkillDetailsDto.builder()
        .name("")
        .yearsOfExperience("5+")
        .experienceLevel(ExperienceLevel.ADVANCE)
        .build()));
    var skillExpNameBlankDto = getCandidateProfileUpdateDTO(null);
    skillExpNameBlankDto.setSkills(List.of(SkillDetailsDto.builder()
        .name("Java Programming")
        .yearsOfExperience("")
        .experienceLevel(ExperienceLevel.ADVANCE)
        .build()));

    var skillExpLevelNullDto = getCandidateProfileUpdateDTO(null);
    skillExpLevelNullDto.setSkills(List.of(SkillDetailsDto.builder()
        .name("Java Programming")
        .yearsOfExperience("5+")
        .experienceLevel(null)
        .build()));

    return Stream.of(
        Arguments.of(missingIdDto), // missing id
        Arguments.of(firstNameMandatoryDto),// First Name is mandatory
        Arguments.of(lastNameMandatoryDto),// Last Name is mandatory
        Arguments.of(allowedCharsInAboutDto), // Maximum number of characters allowed is 10,000
        Arguments.of(addressStateIsBlankDto),// Invalid: state is blank
        Arguments.of(addressCountryIsBlankDto),// Invalid: country is blank
        Arguments.of(malformedPortfolioUrlDto),// Invalid: URL is malformed
        Arguments.of(malformedContactInformationUrlDto),// Invalid: URL is malformed
        Arguments.of(educationTitleBlankDto),// Invalid: title is blank
        Arguments.of(educationDescriptionBlankDto),// Invalid: description is blank
        Arguments.of(interestTitleBlankDto),// Invalid: title is blank
        Arguments.of(skillNameBlankDto),
        Arguments.of(skillExpLevelNullDto)
    );
  }

  static Stream<Arguments> provideInvalidMultipartFiles() {
    return Stream.of(
        Arguments.of(
            new MockMultipartFile("profilePhoto", "profile.txt", "text/plain",
                "invalid content".getBytes())  // Invalid content type
            , null
        ),
        Arguments.of(
            new MockMultipartFile("profilePhoto", "profile.jpg", "image/jpeg", new byte[0])
            // File size must be between 100 bytes and 5 MB
            , null
        ),
        Arguments.of(
            new MockMultipartFile("profilePhoto", "profile.jpg", "image/jpeg", new byte[6000000])
            // File size must be between 100 bytes and 5 MB
            , null
        ),
        Arguments.of(null,
            new MockMultipartFile("resume", "resume.txt", "text/plain",
                "invalid content".getBytes())  // Invalid content type

        ),
        Arguments.of(null,
            new MockMultipartFile("resume", "resume.pdf", "application/pdf", new byte[0])
            // File size must be between 100 bytes and 5 MB
        ),
        Arguments.of(null,
            new MockMultipartFile("resume", "resume.pdf", "application/pdf", new byte[6000000])
            // File size must be between 100 bytes and 5 MB
        )
    );
  }

  private static CandidateProfileUpdateDTO getCandidateProfileUpdateDTO(CandidateProfile profile) {
    if (profile != null) {
      return CandidateProfileUpdateDTO.builder()
          .id(profile.getId())
          .firstName(profile.getFirstName())
          .lastName(profile.getLastName())
          .about(profile.getAbout())
          .portfolioWebsite(profile.getPortfolioWebsite())
          .workAuthorization(profile.getWorkAuthorization())
          .preferredEmploymentType(profile.getPreferredEmploymentType())
          .address(profile.getAddress())
          .contactInformation(profile.getContactInformation())
          .educations(profile.getEducations())
          .interests(profile.getInterests())
          .skills(profile.getSkills().stream().map(
                  skill -> SkillDetailsDto.builder()
                      .id(skill.getId())
                      .name(skill.getName())
                      .experienceLevel(skill.getExperienceLevel())
                      .yearsOfExperience(skill.getYearsOfExperience())
                      .build())
              .toList())
          .build();
    } else {
      return CandidateProfileUpdateDTO.builder()
          .id(1)
          .firstName("John")
          .lastName("Doe")
          .about("About text")
          .contactInformation(ContactInformation.builder().githubHandle("https://www.github.com")
              .linkedinHandle("https://www.linkedin.com").twitterHandle("https://www.twitter.com")
              .build())
          .build();
    }
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
  @DisplayName("Update existing skills")
  @Transactional
  void updateCandidateProfile_UpdateExistingSkills() throws Exception {
    var existingProfile = candidateProfileRepository.findById(1).orElseThrow();
    var existingSkill = existingProfile.getSkills().getFirst();

    var updatedSkill = SkillDetailsDto.builder()
        .id(existingSkill.getId())
        .name("Updated Skill")
        .yearsOfExperience("6+")
        .experienceLevel(ExperienceLevel.BEGINNER)
        .build();

    var dto = getCandidateProfileUpdateDTO(existingProfile);
    dto.setSkills(List.of(updatedSkill));

    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/candidate/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString());

    CandidateProfile updatedProfile = candidateProfileRepository.findById(existingProfile.getId())
        .orElseThrow();

    Skill updatedSkillEntity = updatedProfile.getSkills().stream()
        .filter(skill -> skill.getId().equals(existingSkill.getId()))
        .findFirst()
        .orElseThrow();

    assertThat(updatedSkillEntity.getName()).isEqualTo("Updated Skill");
    assertThat(updatedSkillEntity.getYearsOfExperience()).isEqualTo("6+");
    assertThat(updatedSkillEntity.getExperienceLevel()).isEqualTo(ExperienceLevel.BEGINNER);
  }

  @SqlGroup({
      @Sql(scripts = "/seed-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
  })
  @Test
  @DisplayName("Add new skills")
  @Transactional
  void updateCandidateProfile_AddNewSkills() throws Exception {
    var existingProfile = candidateProfileRepository.findById(1).orElseThrow();
    var dto = getCandidateProfileUpdateDTO(existingProfile);

    var newSkill1 = SkillDetailsDto.builder()
        .name("New Skill 1")
        .yearsOfExperience("2-3")
        .experienceLevel(ExperienceLevel.INTERMEDIATE)
        .build();

    var newSkill2 = SkillDetailsDto.builder()
        .name("New Skill 2")
        .yearsOfExperience("4-5")
        .experienceLevel(ExperienceLevel.ADVANCE)
        .build();

    List<SkillDetailsDto> updatedSkills = new ArrayList<>(dto.getSkills());
    updatedSkills.add(newSkill1);
    updatedSkills.add(newSkill2);
    dto.setSkills(updatedSkills);

    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/candidate/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString());

    CandidateProfile updatedProfile = candidateProfileRepository.findById(existingProfile.getId())
        .orElseThrow();

    List<Skill> updatedSkillEntities = updatedProfile.getSkills();

    Assertions.assertThat(updatedSkillEntities).hasSize(updatedSkills.size());
    Assertions.assertThat(updatedSkillEntities).extracting(Skill::getName)
        .contains("New Skill 1", "New Skill 2");
  }

  @SqlGroup({
      @Sql(scripts = "/seed-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
  })
  @Test
  @DisplayName("Delete skills")
  @Transactional
  void updateCandidateProfile_DeleteSkills() throws Exception {
    var existingProfile = candidateProfileRepository.findById(1).orElseThrow();
    var dto = getCandidateProfileUpdateDTO(existingProfile);
    dto.setSkills(new ArrayList<>());

    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/candidate/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString());

    CandidateProfile updatedProfile = candidateProfileRepository.findById(existingProfile.getId())
        .orElseThrow();

    Assertions.assertThat(updatedProfile.getSkills()).isEmpty();
  }

  @SqlGroup({
      @Sql(scripts = "/seed-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
  })
  @Test
  @DisplayName("Update address")
  @Transactional
  void updateCandidateProfile_UpdateAddress() throws Exception {
    var existingProfile = candidateProfileRepository.findById(1).orElseThrow();

    var updatedAddress = Address.builder()
        .id(existingProfile.getAddress().getId())
        .city("Updated City")
        .state("Updated State")
        .country("Updated Country")
        .build();

    var dto = getCandidateProfileUpdateDTO(existingProfile);
    dto.setAddress(updatedAddress);

    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/candidate/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString());

    CandidateProfile updatedProfile = candidateProfileRepository.findById(existingProfile.getId())
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
  void updateCandidateProfile_UpdateContactInformation() throws Exception {
    var existingProfile = candidateProfileRepository.findById(1).orElseThrow();

    var updatedContactInformation = ContactInformation.builder()
        .id(existingProfile.getContactInformation().getId())
        .phone("Updated Phone")
        .twitterHandle("https://twitter.com/updated")
        .linkedinHandle("https://linkedin.com/updated")
        .githubHandle("https://github.com/updated")
        .build();

    var dto = getCandidateProfileUpdateDTO(existingProfile);
    dto.setContactInformation(updatedContactInformation);

    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/candidate/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString());

    CandidateProfile updatedProfile = candidateProfileRepository.findById(existingProfile.getId())
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
  void updateCandidateProfile_DeleteInterests() throws Exception {
    var existingProfile = candidateProfileRepository.findById(1).orElseThrow();
    var dto = getCandidateProfileUpdateDTO(existingProfile);
    dto.setInterests(new ArrayList<>());

    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/candidate/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString());

    CandidateProfile updatedProfile = candidateProfileRepository.findById(existingProfile.getId())
        .orElseThrow();

    Assertions.assertThat(updatedProfile.getInterests()).isEmpty();
  }

  @SqlGroup({
      @Sql(scripts = "/seed-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
  })
  @Test
  @DisplayName("Update existing interests")
  @Transactional
  void updateCandidateProfile_UpdateExistingInterests() throws Exception {
    var existingProfile = candidateProfileRepository.findById(1).orElseThrow();
    var existingInterest = existingProfile.getInterests().getFirst();

    var updatedInterest = Interest.builder()
        .id(existingInterest.getId())
        .title("Updated Interest")
        .build();

    var dto = getCandidateProfileUpdateDTO(existingProfile);
    dto.setInterests(List.of(updatedInterest));

    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/candidate/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString());

    CandidateProfile updatedProfile = candidateProfileRepository.findById(existingProfile.getId())
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
  void updateCandidateProfile_CreateNewInterests() throws Exception {
    var existingProfile = candidateProfileRepository.findById(1).orElseThrow();
    var dto = getCandidateProfileUpdateDTO(existingProfile);
    dto.setInterests(List.of(
        existingProfile.getInterests().getFirst(),
        Interest.builder().title("New Interest 1").build(),
        Interest.builder().title("New Interest 2").build()));

    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/candidate/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString());

    CandidateProfile updatedProfile = candidateProfileRepository.findById(existingProfile.getId())
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
  void updateCandidateProfile_DeletesExistingEducations_WhenIdIsMissing() throws Exception {
    // save some education to profile
    var existingProfile = candidateProfileRepository.findById(1).orElseThrow();
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
    existingProfile = candidateProfileRepository.save(existingProfile);

    var dto = getCandidateProfileUpdateDTO(existingProfile);
    dto.setEducations(List.of(existingProfile.getEducations().getFirst()));

    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/candidate/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString());

    CandidateProfile updatedProfile = candidateProfileRepository.findById(existingProfile.getId())
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
  void updateCandidateProfile_UpdateExistingEducations() throws Exception {
    // save some education to profile
    var existingProfile = candidateProfileRepository.findById(1).orElseThrow();
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
    existingProfile = candidateProfileRepository.save(existingProfile);

    var existingEducation = candidateProfileRepository.findById(1).orElseThrow().getEducations()
        .getFirst();

    var updatedEducation = Education.builder()
        .id(existingEducation.getId())
        .title("Updated Title")
        .description("Updated Description")
        .build();

    var dto = getCandidateProfileUpdateDTO(existingProfile);
    dto.setEducations(List.of(updatedEducation, existingProfile.getEducations().get(1)));

    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/candidate/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString());

    CandidateProfile updatedProfile = candidateProfileRepository.findById(existingProfile.getId())
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
  void updateCandidateProfile_CreateNewEducations() throws Exception {
    var existingProfile = candidateProfileRepository.findById(1).orElseThrow();
    var dto = getCandidateProfileUpdateDTO(existingProfile);
    dto.setEducations(List.of(
        Education.builder().title("New Title 1").description("New Description 1").build(),
        Education.builder().title("New Title 2").description("New Description 2").build())
    );

    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/candidate/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString());

    CandidateProfile updatedProfile = candidateProfileRepository.findById(existingProfile.getId())
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
  @DisplayName("Update firstName, lastName, and about fields")
  @Transactional
  void updateCandidateProfile_ChangeNameAndAbout() throws Exception {
    var existingProfile = candidateProfileRepository.findById(1).orElseThrow();
    var dto = getCandidateProfileUpdateDTO(existingProfile);
    dto.setFirstName("Ram");
    dto.setLastName("Rohan");
    dto.setAbout("Updated about section.");

    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/candidate/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString());

    // Retrieve the profile again to ensure the changes
    var updatedProfile = candidateProfileRepository.findById(1).orElseThrow();

    assertThat(updatedProfile.getFirstName()).isEqualTo(dto.getFirstName());
    assertThat(updatedProfile.getLastName()).isEqualTo(dto.getLastName());
    assertThat(updatedProfile.getAbout()).isEqualTo(dto.getAbout());
  }

  @SqlGroup({
      @Sql(scripts = "/seed-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
  })
  @Test
  @Transactional
  void updateCandidateProfile_withValidCompanyId_ReturnsOk() throws Exception {
    var existingProfile = candidateProfileRepository.findById(1).orElseThrow();
    var dto = getCandidateProfileUpdateDTO(existingProfile);
    mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/candidate/profile")
                .file(convertToMultipartFile(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").isString())
        .andExpect(jsonPath("$.data").doesNotHaveJsonPath());

    var updatedProfile = candidateProfileRepository.findById(1).orElseThrow();
    assertThat(updatedProfile).usingRecursiveComparison().isEqualTo(existingProfile);
  }

  @ParameterizedTest
  @MethodSource("profileInvalidFields")
  void updateCandidateProfile_whenInvalidFields_ReturnsBadRequest(CandidateProfileUpdateDTO dto)
      throws Exception {
    var requestBuilder = multipart(HttpMethod.PATCH, "/api/candidate/profile")
        .file(convertToMultipartFile(dto))
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE);

    mockMvc.perform(requestBuilder)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.validationErrors").isArray())
        .andExpect(jsonPath("$.validationErrors[0]").isString());
  }

  @ParameterizedTest
  @MethodSource("provideInvalidMultipartFiles")
  void updateCandidateProfile_whenInvalidMultipartFiles_ReturnsBadRequest(MockMultipartFile resume,
      MockMultipartFile profilePhoto)
      throws Exception {
    var dto = getCandidateProfileUpdateDTO(null);
    var multipartReq = multipart(HttpMethod.PATCH, "/api/candidate/profile")
        .file(convertToMultipartFile(dto));
    if (resume != null) {
      multipartReq.file(resume);
    }
    if (profilePhoto != null) {
      multipartReq.file(profilePhoto);
    }
    mockMvc.perform(multipartReq.contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.validationErrors").isArray())
        .andExpect(jsonPath("$.validationErrors[0]").isString());
  }

  private MockMultipartFile convertToMultipartFile(CandidateProfileUpdateDTO dto) throws Exception {
    return new MockMultipartFile("updateDetails", "", MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(dto));
  }
}
