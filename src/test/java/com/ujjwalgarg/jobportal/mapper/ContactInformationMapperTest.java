package com.ujjwalgarg.jobportal.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.ujjwalgarg.jobportal.controller.payload.auth.NewCandidateRequest;
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
@ContextConfiguration(classes = {ContactInformationMapperImpl.class})
class ContactInformationMapperTest {

  @Autowired
  private ContactInformationMapper contactInformationMapper;

  private static Stream<Arguments> provideNewCandidateRequests() {
    return Stream.of(
        Arguments.of(
            new NewCandidateRequest(
                "candidate@example.com",
                "securePassword",
                "John",
                "Doe",
                "A short description",
                "https://twitter.com/johndoe",
                "https://linkedin.com/in/johndoe",
                "https://github.com/johndoe",
                "1234567890"
            ),
            new ContactInformation(
                null,
                "1234567890",
                "https://twitter.com/johndoe",
                "https://linkedin.com/in/johndoe",
                "https://github.com/johndoe"
            )
        ),
        Arguments.of(
            new NewCandidateRequest(
                "candidate2@example.com",
                "securePassword2",
                "Jane",
                "Smith",
                "Another short description",
                "https://twitter.com/janesmith",
                "https://linkedin.com/in/janesmith",
                "https://github.com/janesmith",
                null
            ),
            new ContactInformation(
                null,
                null,
                "https://twitter.com/janesmith",
                "https://linkedin.com/in/janesmith",
                "https://github.com/janesmith"
            )
        )
    );
  }

  @ParameterizedTest
  @MethodSource("provideNewCandidateRequests")
  @DisplayName("Test fromNewCandidateRequest()")
  void fromNewCandidateRequest(NewCandidateRequest request, ContactInformation expected) {
    ContactInformation actual = contactInformationMapper.fromNewCandidateRequest(request);

    assertNull(actual.getId());
    assertEquals(expected.getPhone(), actual.getPhone());
    assertEquals(expected.getTwitterHandle(), actual.getTwitterHandle());
    assertEquals(expected.getLinkedinHandle(), actual.getLinkedinHandle());
    assertEquals(expected.getGithubHandle(), actual.getGithubHandle());
  }
}
