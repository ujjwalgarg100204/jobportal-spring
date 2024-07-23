package com.ujjwalgarg.jobportal.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.ujjwalgarg.jobportal.constant.EmploymentType;
import com.ujjwalgarg.jobportal.constant.ExperienceLevel;
import com.ujjwalgarg.jobportal.constant.WorkAuthorization;
import com.ujjwalgarg.jobportal.controller.payload.auth.NewCandidateRequest;
import com.ujjwalgarg.jobportal.controller.payload.candidateprofile.CandidateProfileGetRequestDto;
import com.ujjwalgarg.jobportal.controller.payload.candidateprofile.SkillGetRequestDto;
import com.ujjwalgarg.jobportal.controller.payload.common.SkillDetailsDto;
import com.ujjwalgarg.jobportal.entity.Address;
import com.ujjwalgarg.jobportal.entity.CandidateBookmarkedJob;
import com.ujjwalgarg.jobportal.entity.CandidateJobApplication;
import com.ujjwalgarg.jobportal.entity.CandidateProfile;
import com.ujjwalgarg.jobportal.entity.ContactInformation;
import com.ujjwalgarg.jobportal.entity.Education;
import com.ujjwalgarg.jobportal.entity.Interest;
import com.ujjwalgarg.jobportal.entity.Skill;
import com.ujjwalgarg.jobportal.entity.User;
import com.ujjwalgarg.jobportal.service.dto.candidateprofileservice.CandidateProfileUpdateDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CandidateProfileMapperTest {

  @Autowired
  private CandidateProfileMapper candidateProfileMapper;

  private static Stream<Arguments> provideValidNewCandidateRequests() {
    return Stream.of(
        Arguments.of(
            new NewCandidateRequest(
                "john.doe@example.com",
                "password123",
                "John",
                "Doe",
                "Short bio about John",
                "https://twitter.com/johndoe",
                "https://linkedin.com/in/johndoe",
                "https://github.com/johndoe",
                "+1234567890"
            ),
            CandidateProfile.builder()
                .firstName("John")
                .lastName("Doe")
                .shortAbout("Short bio about John")
                .hasProfilePhoto(false)
                .hasResume(false)
                .contactInformation(ContactInformation.builder()
                    .phone("+1234567890")
                    .twitterHandle("https://twitter.com/johndoe")
                    .linkedinHandle("https://linkedin.com/in/johndoe")
                    .githubHandle("https://github.com/johndoe")
                    .build())
                .build()
        ),
        Arguments.of(
            new NewCandidateRequest(
                "jane.smith@example.com",
                "password456",
                "Jane",
                "Smith",
                "Short bio about Jane",
                null,
                null,
                null,
                null
            ),
            CandidateProfile.builder()
                .firstName("Jane")
                .lastName("Smith")
                .shortAbout("Short bio about Jane")
                .hasProfilePhoto(false)
                .hasResume(false)
                .contactInformation(null)
                .build()
        ),
        Arguments.of(
            new NewCandidateRequest(
                "alice.wong@example.com",
                "password789",
                "Alice",
                "Wong",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium",
                "https://twitter.com/alicewong",
                "https://linkedin.com/in/alicewong",
                "https://github.com/alicewong",
                "+9876543210"
            ),
            CandidateProfile.builder()
                .firstName("Alice")
                .lastName("Wong")
                .shortAbout(
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium")
                .hasProfilePhoto(false)
                .hasResume(false)
                .contactInformation(ContactInformation.builder()
                    .phone("+9876543210")
                    .twitterHandle("https://twitter.com/alicewong")
                    .linkedinHandle("https://linkedin.com/in/alicewong")
                    .githubHandle("https://github.com/alicewong")
                    .build())
                .build()
        )
    );
  }

  @ParameterizedTest(name = "{index} - {displayName}")
  @MethodSource("provideValidNewCandidateRequests")
  @DisplayName("Test fromNewCandidateRequest() with Valid Requests")
  void testFromNewCandidateRequest(
      NewCandidateRequest request, CandidateProfile expected) {
    CandidateProfile actual = candidateProfileMapper.fromNewCandidateRequest(request);

    assertThat(actual).usingRecursiveComparison()
        .isEqualTo(expected);
  }

  private static Stream<Arguments> updateCandidateProfileScenarios() {
    return Stream.of(
        // Scenario 1: Complete update
        Arguments.of(
            "Complete update",
            CandidateProfileUpdateDTO.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .about("Updated about")
                .portfolioWebsite("https://johndoe.com")
                .workAuthorization(WorkAuthorization.CANADIAN_CITIZEN)
                .preferredEmploymentType(EmploymentType.FULL_TIME)
                .address(
                    Address.builder().city("New City").state("New State")
                        .country("New Country").build())
                .contactInformation(
                    ContactInformation.builder().phone("1234567890")
                        .twitterHandle("twitter").linkedinHandle("linkedin").githubHandle("github")
                        .build())
                .educations(new ArrayList<>(List.of(
                    Education.builder().title("New University").description("New Degree").build())))
                .interests(
                    new ArrayList<>(List.of(Interest.builder().title("New Interest").build())))
                .skills(
                    new ArrayList<>(List.of(SkillDetailsDto.builder().name("New Skill").build())))
                .build(),
            CandidateProfile.builder()
                .id(1)
                .firstName("Initial")
                .lastName("Name")
                .shortAbout("Initial short about")
                .about("Initial about")
                .hasProfilePhoto(false)
                .hasResume(false)
                .portfolioWebsite("https://initial.com")
                .workAuthorization(WorkAuthorization.H1_VISA)
                .preferredEmploymentType(EmploymentType.PART_TIME)
                .user(new User())
                .address(Address.builder().city("Initial City")
                    .state("Initial State").country("Initial Country").build())
                .contactInformation(
                    ContactInformation.builder().phone("1111111111")
                        .twitterHandle("initial_twitter").linkedinHandle("initial_linkedin")
                        .githubHandle("initial_github").build())
                .educations(new ArrayList<>(List.of(
                    Education.builder().title("Initial University").description("Initial Degree").
                        build())))
                .interests(
                    new ArrayList<>(List.of(Interest.builder().title("Initial Interest").build())))
                .skills(new ArrayList<>(List.of(Skill.builder().name("Initial Skill").build())))
                .jobApplications(new ArrayList<>(List.of(new CandidateJobApplication())))
                .bookmarkedJobs(new ArrayList<>(List.of(new CandidateBookmarkedJob())))
                .build(),
            CandidateProfile.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .shortAbout("Initial short about")
                .about("Updated about")
                .hasProfilePhoto(false)
                .hasResume(false)
                .portfolioWebsite("https://johndoe.com")
                .workAuthorization(WorkAuthorization.CANADIAN_CITIZEN)
                .preferredEmploymentType(EmploymentType.FULL_TIME)
                .user(new User())
                .address(Address.builder().city("New City").state("New State")
                    .country("New Country").build())
                .contactInformation(
                    ContactInformation.builder().phone("1234567890")
                        .twitterHandle("twitter").linkedinHandle("linkedin").githubHandle("github")
                        .build())
                .educations(new ArrayList<>(List.of(
                    Education.builder().title("New University").description("New Degree")
                        .build())))
                .interests(
                    new ArrayList<>(List.of(Interest.builder().title("New Interest").build())))
                .skills(new ArrayList<>(List.of(Skill.builder().name("New Skill").build())))
                .jobApplications(new ArrayList<>(List.of(new CandidateJobApplication())))
                .bookmarkedJobs(new ArrayList<>(List.of(new CandidateBookmarkedJob())))
                .build()
        ),
        // Scenario 2: Partial update
        Arguments.of(
            "Partial update",
            CandidateProfileUpdateDTO.builder()
                .id(1)
                .firstName("Jane")
                .lastName("Doe")
                .about("Partially updated about")
                .build(),
            CandidateProfile.builder()
                .id(1)
                .firstName("Initial")
                .lastName("Name")
                .shortAbout("Initial short about")
                .about("Initial about")
                .hasProfilePhoto(false)
                .hasResume(false)
                .portfolioWebsite("https://initial.com")
                .workAuthorization(WorkAuthorization.H1_VISA)
                .preferredEmploymentType(EmploymentType.PART_TIME)
                .user(new User())
                .address(Address.builder().city("Initial City")
                    .state("Initial State").country("Initial Country").build())
                .contactInformation(
                    ContactInformation.builder().phone("1111111111")
                        .twitterHandle("initial_twitter").linkedinHandle("initial_linkedin")
                        .githubHandle("initial_github").build())
                .educations(new ArrayList<>(List.of(
                    Education.builder().title("Initial University").description("Initial Degree")
                        .build())))
                .interests(
                    new ArrayList<>(List.of(Interest.builder().title("Initial Interest").build())))
                .skills(new ArrayList<>(List.of(Skill.builder().name("Initial Skill").build())))
                .jobApplications(new ArrayList<>(List.of(new CandidateJobApplication())))
                .bookmarkedJobs(new ArrayList<>(List.of(new CandidateBookmarkedJob())))
                .build(),
            CandidateProfile.builder()
                .id(1)
                .firstName("Jane")
                .lastName("Doe")
                .shortAbout("Initial short about")
                .about("Partially updated about")
                .hasProfilePhoto(false)
                .hasResume(false)
                .portfolioWebsite("https://initial.com")
                .workAuthorization(WorkAuthorization.H1_VISA)
                .preferredEmploymentType(EmploymentType.PART_TIME)
                .user(new User())
                .address(Address.builder().city("Initial City")
                    .state("Initial State").country("Initial Country").build())
                .contactInformation(
                    ContactInformation.builder().phone("1111111111")
                        .twitterHandle("initial_twitter").linkedinHandle("initial_linkedin")
                        .githubHandle("initial_github").build())
                .educations(new ArrayList<>(List.of(
                    Education.builder().title("Initial University").description("Initial Degree")
                        .build())))
                .interests(
                    new ArrayList<>(List.of(Interest.builder().title("Initial Interest").build())))
                .skills(new ArrayList<>(List.of(Skill.builder().name("Initial Skill").build())))
                .jobApplications(new ArrayList<>(List.of(new CandidateJobApplication())))
                .bookmarkedJobs(new ArrayList<>(List.of(new CandidateBookmarkedJob())))
                .build()
        ),
        // Scenario 3: No updates (DTO with only ID)
        Arguments.of(
            "No updates",
            CandidateProfileUpdateDTO.builder()
                .id(1)
                .build(),
            CandidateProfile.builder()
                .id(1)
                .firstName("Initial")
                .lastName("Name")
                .shortAbout("Initial short about")
                .about("Initial about")
                .hasProfilePhoto(false)
                .hasResume(false)
                .portfolioWebsite("https://initial.com")
                .workAuthorization(WorkAuthorization.H1_VISA)
                .preferredEmploymentType(EmploymentType.PART_TIME)
                .user(new User())
                .address(Address.builder().city("Initial City")
                    .state("Initial State").country("Initial Country").build())
                .contactInformation(
                    ContactInformation.builder().phone("1111111111")
                        .twitterHandle("initial_twitter").linkedinHandle("initial_linkedin")
                        .githubHandle("initial_github").build())
                .educations(new ArrayList<>(List.of(
                    Education.builder().title("Initial University").description("Initial Degree")
                        .build())))
                .interests(
                    new ArrayList<>(List.of(Interest.builder().title("Initial Interest").build())))
                .skills(new ArrayList<>(List.of(Skill.builder().name("Initial Skill").build())))
                .jobApplications(new ArrayList<>(List.of(new CandidateJobApplication())))
                .bookmarkedJobs(new ArrayList<>(List.of(new CandidateBookmarkedJob())))
                .build(),
            CandidateProfile.builder()
                .id(1)
                .firstName("Initial")
                .lastName("Name")
                .shortAbout("Initial short about")
                .about("Initial about")
                .hasProfilePhoto(false)
                .hasResume(false)
                .portfolioWebsite("https://initial.com")
                .workAuthorization(WorkAuthorization.H1_VISA)
                .preferredEmploymentType(EmploymentType.PART_TIME)
                .user(new User())
                .address(Address.builder().city("Initial City")
                    .state("Initial State").country("Initial Country").build())
                .contactInformation(
                    ContactInformation.builder().phone("1111111111")
                        .twitterHandle("initial_twitter").linkedinHandle("initial_linkedin")
                        .githubHandle("initial_github").build())
                .educations(new ArrayList<>(List.of(
                    Education.builder().title("Initial University").description("Initial Degree")
                        .build())))
                .interests(
                    new ArrayList<>(List.of(Interest.builder().title("Initial Interest").build())))
                .skills(new ArrayList<>(List.of(Skill.builder().name("Initial Skill").build())))
                .jobApplications(new ArrayList<>(List.of(new CandidateJobApplication())))
                .bookmarkedJobs(new ArrayList<>(List.of(new CandidateBookmarkedJob())))
                .build()
        )
    );
  }

  @DisplayName("Update CandidateProfile from DTO scenarios")
  @ParameterizedTest(name = "{0}")
  @MethodSource("updateCandidateProfileScenarios")
  void testUpdateCandidateProfileFromDTO(String scenario, CandidateProfileUpdateDTO input,
      CandidateProfile initialEntity, CandidateProfile expectedEntity) {
    candidateProfileMapper.updateCandidateProfileFromDTO(input, initialEntity);

    assertThat(initialEntity)
        .usingRecursiveComparison()
        .ignoringFields("user", "jobApplications", "bookmarkedJobs", "hasProfilePhoto", "hasResume")
        .isEqualTo(expectedEntity);

    // Verify ignored fields remain unchanged
    assertThat(initialEntity.getUser()).isEqualTo(expectedEntity.getUser());
    assertThat(initialEntity.getJobApplications()).isEqualTo(expectedEntity.getJobApplications());
    assertThat(initialEntity.getBookmarkedJobs()).isEqualTo(expectedEntity.getBookmarkedJobs());
    assertThat(initialEntity.getHasProfilePhoto()).isEqualTo(expectedEntity.getHasProfilePhoto());
    assertThat(initialEntity.getHasResume()).isEqualTo(expectedEntity.getHasResume());
  }

  private static Stream<Arguments> provideValidCandidateProfiles() {
    return Stream.of(
        Arguments.of(
            createFullCandidateProfile(),
            "https://example.com/photo.jpg",
            "https://example.com/resume.pdf",
            new CandidateProfileGetRequestDto(
                1, "John", "Doe", "Software Developer", "Experienced Java developer",
                "https://example.com/photo.jpg", "https://example.com/resume.pdf",
                "https://johndoe.com",
                WorkAuthorization.CANADIAN_CITIZEN, EmploymentType.FULL_TIME,
                new Address(), new ContactInformation(),
                Arrays.asList(new Education(), new Education()),
                Arrays.asList(new Interest(), new Interest()),
                Arrays.asList(new SkillGetRequestDto(1, "Skill", "4 yrs", ExperienceLevel.ADVANCE),
                    new SkillGetRequestDto(2, "SKillName", "5 yrs", ExperienceLevel.INTERMEDIATE))
            )
        ),
        Arguments.of(
            createMinimalCandidateProfile(),
            null,
            null,
            new CandidateProfileGetRequestDto(
                2, "Jane", "Smith", "Data Scientist", null,
                null, null, null, null, null,
                null, null, null, null, null
            )
        )
    );
  }

  private static CandidateProfile createFullCandidateProfile() {
    return CandidateProfile.builder()
        .id(1)
        .firstName("John")
        .lastName("Doe")
        .shortAbout("Software Developer")
        .about("Experienced Java developer")
        .hasProfilePhoto(true)
        .hasResume(true)
        .portfolioWebsite("https://johndoe.com")
        .workAuthorization(WorkAuthorization.CANADIAN_CITIZEN)
        .preferredEmploymentType(EmploymentType.FULL_TIME)
        .address(new Address())
        .contactInformation(new ContactInformation())
        .educations(Arrays.asList(new Education(), new Education()))
        .interests(Arrays.asList(new Interest(), new Interest()))
        .skills(Arrays.asList(new Skill(1, "Skill", "4 yrs", ExperienceLevel.ADVANCE, null),
            new Skill(2, "SKillName", "5 yrs", ExperienceLevel.INTERMEDIATE, null)))
        .build();
  }

  private static CandidateProfile createMinimalCandidateProfile() {
    return CandidateProfile.builder()
        .id(2)
        .firstName("Jane")
        .lastName("Smith")
        .shortAbout("Data Scientist")
        .build();
  }

  @ParameterizedTest(name = "{index} - {displayName}")
  @MethodSource("provideValidCandidateProfiles")
  @DisplayName("Test toCandidateProfileGetRequest() with valid CandidateProfile and URLs")
  void toCandidateProfileGetRequest_validProfileAndUrls_returnsMappedDto(
      CandidateProfile profile, String profilePhotoUrl, String resumeUrl,
      CandidateProfileGetRequestDto expected) {
    CandidateProfileGetRequestDto actual = candidateProfileMapper.toCandidateProfileGetRequest(
        profile, profilePhotoUrl, resumeUrl);
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  @DisplayName("Test toCandidateProfileGetRequest() when input is null")
  void toCandidateProfileGetRequest_nullInput_returnsNull() {
    assertNull(candidateProfileMapper.toCandidateProfileGetRequest(null, null, null));
  }

  @Test
  @DisplayName("Test toCandidateProfileGetRequest() with valid profile but null URLs")
  void toCandidateProfileGetRequest_validProfileNullUrls_returnsMappedDtoWithNullUrls() {
    CandidateProfile profile = createFullCandidateProfile();
    CandidateProfileGetRequestDto dto = candidateProfileMapper.toCandidateProfileGetRequest(profile,
        null, null);
    assertThat(dto.profilePhotoUrl()).isNull();
    assertThat(dto.resumeUrl()).isNull();
  }
}
