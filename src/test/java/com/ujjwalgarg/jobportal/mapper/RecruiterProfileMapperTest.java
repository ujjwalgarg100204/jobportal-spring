package com.ujjwalgarg.jobportal.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.ujjwalgarg.jobportal.controller.payload.auth.NewRecruiterRequest;
import com.ujjwalgarg.jobportal.entity.Address;
import com.ujjwalgarg.jobportal.entity.Company;
import com.ujjwalgarg.jobportal.entity.ContactInformation;
import com.ujjwalgarg.jobportal.entity.Education;
import com.ujjwalgarg.jobportal.entity.Interest;
import com.ujjwalgarg.jobportal.entity.RecruiterProfile;
import com.ujjwalgarg.jobportal.service.dto.recruiterprofileservice.RecruiterProfileUpdateDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RecruiterProfileMapperImpl.class, CompanyMapperImpl.class,
    AddressMapperImpl.class})
class RecruiterProfileMapperTest {

  @Autowired
  private RecruiterProfileMapper recruiterProfileMapper;

  private static Stream<Arguments> provideValidNewRecruiterRequests() {
    return Stream.of(
        Arguments.of(
            new NewRecruiterRequest(
                "recruiter@example.com",
                "password123",
                "Jane",
                "Smith",
                1,
                "Example Company",
                "New York",
                "NY",
                "USA"
            ),
            RecruiterProfile.builder()
                .firstName("Jane")
                .lastName("Smith")
                .hasProfilePhoto(false)
                .company(Company.builder()
                    .id(1)
                    .name("Example Company")
                    .address(Address.builder()
                        .city("New York")
                        .state("NY")
                        .country("USA")
                        .build())
                    .hasLogo(false)
                    .build())
                .build()
        ),
        Arguments.of(
            new NewRecruiterRequest(
                "another.recruiter@example.com",
                "password456",
                "Emily",
                "Johnson",
                2,
                "Another Company",
                null,
                "CA",
                "USA"
            ),
            RecruiterProfile.builder()
                .firstName("Emily")
                .lastName("Johnson")
                .hasProfilePhoto(false)
                .company(Company.builder()
                    .id(2)
                    .name("Another Company")
                    .address(Address.builder()
                        .state("CA")
                        .country("USA")
                        .build())
                    .hasLogo(false)
                    .build())
                .build()
        ),
        Arguments.of(
            new NewRecruiterRequest(
                "third.recruiter@example.com",
                "password789",
                "Michael",
                "Brown",
                3,
                "Third Company",
                "Seattle",
                "WA",
                "USA"
            ),
            RecruiterProfile.builder()
                .firstName("Michael")
                .lastName("Brown")
                .hasProfilePhoto(false)
                .company(Company.builder()
                    .id(3)
                    .name("Third Company")
                    .address(Address.builder()
                        .city("Seattle")
                        .state("WA")
                        .country("USA")
                        .build())
                    .hasLogo(false)
                    .build())
                .build()
        )
    );
  }

  private static Stream<Arguments> provideUpdateScenarios() {
    return Stream.of(
        Arguments.of(
            // Scenario 1: Update basic fields
            RecruiterProfileUpdateDTO.builder()
                .id(1)
                .firstName("Updated First")
                .lastName("Updated Last")
                .about("Updated About")
                .build(),
            RecruiterProfile.builder()
                .id(1)
                .firstName("Original First")
                .lastName("Original Last")
                .about("Original About")
                .build(),
            RecruiterProfile.builder()
                .id(1)
                .firstName("Updated First")
                .lastName("Updated Last")
                .about("Updated About")
                .company(Company.builder().address(Address.builder().build()).build())
                .build()
        ),
        Arguments.of(
            // Scenario 2: Update nested objects
            RecruiterProfileUpdateDTO.builder()
                .id(1)
                .address(
                    Address.builder().city("New City").state("New State").country("New Country")
                        .build())
                .contactInformation(ContactInformation.builder().phone("1234567890").build())
                .companyName("New Company")
                .build(),
            RecruiterProfile.builder()
                .id(1)
                .address(
                    Address.builder().city("Old City").state("Old State").country("Old Country")
                        .build())
                .contactInformation(ContactInformation.builder().phone("0987654321").build())
                .company(Company.builder().name("Old Company").build())
                .build(),
            RecruiterProfile.builder()
                .id(1)
                .address(
                    Address.builder().city("New City").state("New State").country("New Country")
                        .build())
                .contactInformation(ContactInformation.builder().phone("1234567890").build())
                .company(Company.builder().name("New Company").address(Address.builder().build())
                    .build())
                .build()
        ),
        Arguments.of(
            // Scenario 3: Update lists
            RecruiterProfileUpdateDTO.builder()
                .id(1)
                .educations(new ArrayList<>(List.of(
                    Education.builder().title("New Education 1").description("New Description 1")
                        .build(),
                    Education.builder().title("New Education 2").description("New Description 2")
                        .build()
                )))
                .interests(new ArrayList<>(List.of(
                    Interest.builder().title("New Interest 1").build(),
                    Interest.builder().title("New Interest 2").build()
                )))
                .build(),
            RecruiterProfile.builder()
                .id(1)
                .educations(new ArrayList<>(List.of(
                    Education.builder().title("Old Education").description("Old Description")
                        .build()
                )))
                .interests(new ArrayList<>(List.of(
                    Interest.builder().title("Old Interest").build()
                )))
                .build(),
            RecruiterProfile.builder()
                .id(1)
                .educations(List.of(
                    Education.builder().title("New Education 1").description("New Description 1")
                        .build(),
                    Education.builder().title("New Education 2").description("New Description 2")
                        .build()
                ))
                .interests(List.of(
                    Interest.builder().title("New Interest 1").build(),
                    Interest.builder().title("New Interest 2").build()
                ))
                .company(Company.builder().address(Address.builder().build()).build())
                .build()
        ),
        Arguments.of(
            // Scenario 4: Partial update (some fields null)
            RecruiterProfileUpdateDTO.builder()
                .id(1)
                .firstName("Updated First")
                .build(),
            RecruiterProfile.builder()
                .id(1)
                .firstName("Original First")
                .lastName("Original Last")
                .about("Original About")
                .build(),
            RecruiterProfile.builder()
                .id(1)
                .firstName("Updated First")
                .lastName("Original Last")
                .about("Original About")
                .company(Company.builder().address(new Address()).build())
                .build()
        ),
        Arguments.of(
            // Scenario 5: Complete change of all attributes
            RecruiterProfileUpdateDTO.builder()
                .id(1)
                .firstName("Completely New First")
                .lastName("Completely New Last")
                .about("Completely new about section")
                .address(Address.builder()
                    .city("New City")
                    .state("New State")
                    .country("New Country")
                    .build())
                .contactInformation(ContactInformation.builder()
                    .phone("9876543210")
                    .twitterHandle("https://twitter.com/newhandle")
                    .linkedinHandle("https://linkedin.com/in/newhandle")
                    .githubHandle("https://github.com/newhandle")
                    .build())
                .educations(new ArrayList<>(List.of(
                    Education.builder().title("New Degree").description("New University").build(),
                    Education.builder().title("Another New Degree")
                        .description("Another New University").build()
                )))
                .interests(new ArrayList<>(List.of(
                    Interest.builder().title("New Interest 1").build(),
                    Interest.builder().title("New Interest 2").build(),
                    Interest.builder().title("New Interest 3").build()
                )))
                .companyName("Completely New Company")
                .companyAddressCity("New Company City")
                .companyAddressState("New Company State")
                .companyAddressCountry("New Company Country")
                .build(),
            RecruiterProfile.builder()
                .id(1)
                .firstName("Original First")
                .lastName("Original Last")
                .about("Original about section")
                .address(Address.builder()
                    .city("Original City")
                    .state("Original State")
                    .country("Original Country")
                    .build())
                .contactInformation(ContactInformation.builder()
                    .phone("1234567890")
                    .twitterHandle("https://twitter.com/originalhandle")
                    .linkedinHandle("https://linkedin.com/in/originalhandle")
                    .githubHandle("https://github.com/originalhandle")
                    .build())
                .educations(new ArrayList<>(List.of(
                    Education.builder().title("Original Degree").description("Original University")
                        .build()
                )))
                .interests(new ArrayList<>(List.of(
                    Interest.builder().title("Original Interest").build()
                )))
                .company(Company.builder()
                    .name("Original Company")
                    .address(Address.builder()
                        .city("Original Company City")
                        .state("Original Company State")
                        .country("Original Company Country")
                        .build())
                    .build())
                .build(),
            RecruiterProfile.builder()
                .id(1)
                .firstName("Completely New First")
                .lastName("Completely New Last")
                .about("Completely new about section")
                .address(Address.builder()
                    .city("New City")
                    .state("New State")
                    .country("New Country")
                    .build())
                .contactInformation(ContactInformation.builder()
                    .phone("9876543210")
                    .twitterHandle("https://twitter.com/newhandle")
                    .linkedinHandle("https://linkedin.com/in/newhandle")
                    .githubHandle("https://github.com/newhandle")
                    .build())
                .educations(List.of(
                    Education.builder().title("New Degree").description("New University").build(),
                    Education.builder().title("Another New Degree")
                        .description("Another New University").build()
                ))
                .interests(List.of(
                    Interest.builder().title("New Interest 1").build(),
                    Interest.builder().title("New Interest 2").build(),
                    Interest.builder().title("New Interest 3").build()
                ))
                .company(Company.builder()
                    .name("Completely New Company")
                    .address(Address.builder()
                        .city("New Company City")
                        .state("New Company State")
                        .country("New Company Country")
                        .build())
                    .build())
                .build()
        )
    );
  }

  @ParameterizedTest(name = "{index} - Mapping fromNewRecruiterRequest() - {0}")
  @MethodSource("provideValidNewRecruiterRequests")
  @DisplayName("Test fromNewRecruiterRequest() with different scenarios")
  void testFromNewRecruiterRequest(NewRecruiterRequest request, RecruiterProfile expected) {
    RecruiterProfile actual = recruiterProfileMapper.fromNewRecruiterRequest(request);

    Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("provideUpdateScenarios")
  @DisplayName("Test updateRecruiterProfile() with various update scenarios")
  void updateRecruiterProfile_WithVariousScenarios_UpdatesCorrectly(
      RecruiterProfileUpdateDTO updateDTO,
      RecruiterProfile originalProfile,
      RecruiterProfile expectedProfile
  ) {
    System.out.println(updateDTO);
    recruiterProfileMapper.updateRecruiterProfile(updateDTO, originalProfile);

    assertThat(originalProfile)
        .usingRecursiveComparison()
        .ignoringFields("user", "jobs", "hasProfilePhoto")
        .isEqualTo(expectedProfile);
  }
}
