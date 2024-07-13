package com.ujjwalgarg.jobportal.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.ujjwalgarg.jobportal.controller.payload.auth.NewCandidateRequest;
import com.ujjwalgarg.jobportal.entity.CandidateProfile;
import com.ujjwalgarg.jobportal.entity.ContactInformation;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CandidateProfileMapperImpl.class,
    ContactInformationMapperImpl.class})
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
                .contactInformation(ContactInformation.builder()
                    .phone(null)
                    .twitterHandle(null)
                    .linkedinHandle(null)
                    .githubHandle(null)
                    .build())
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


}
