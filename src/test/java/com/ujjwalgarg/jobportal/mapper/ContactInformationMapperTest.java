package com.ujjwalgarg.jobportal.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.ujjwalgarg.jobportal.controller.payload.auth.NewCandidateRequest;
import com.ujjwalgarg.jobportal.entity.ContactInformation;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ContactInformationMapperTest {

  @Autowired
  private ContactInformationMapper contactInformationMapper;

  private static Stream<Arguments> contactInformationMappingScenarios() {
    return Stream.of(
        Arguments.of(
            "All fields present",
            NewCandidateRequest.builder()
                .twitterHandle("https://twitter.com/johndoe")
                .linkedinHandle("https://linkedin.com/in/johndoe")
                .githubHandle("https://github.com/johndoe")
                .phone("1234567890")
                .build(),
            ContactInformation.builder()
                .phone("1234567890")
                .twitterHandle("https://twitter.com/johndoe")
                .linkedinHandle("https://linkedin.com/in/johndoe")
                .githubHandle("https://github.com/johndoe")
                .build()
        ),
        Arguments.of(
            "Without phone field",
            NewCandidateRequest.builder()
                .twitterHandle("https://twitter.com/janesmith")
                .linkedinHandle("https://linkedin.com/in/janesmith")
                .githubHandle("https://github.com/janesmith")
                .build(),
            ContactInformation.builder()
                .twitterHandle("https://twitter.com/janesmith")
                .linkedinHandle("https://linkedin.com/in/janesmith")
                .githubHandle("https://github.com/janesmith")
                .build()
        ),
        Arguments.of(
            "All fields null",
            NewCandidateRequest.builder().build(),
            null
        )
    );
  }

  @DisplayName("Contact information mapping scenarios")
  @ParameterizedTest(name = "{0}")
  @MethodSource("contactInformationMappingScenarios")
  void testContactInformationMapping(String scenario, NewCandidateRequest input,
      ContactInformation expected) {
    ContactInformation result = contactInformationMapper.fromNewCandidateRequest(input);

    assertThat(result)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(expected);

    if (result != null) {
      assertThat(result.getId()).isNull();
    }
  }
}
