package com.ujjwalgarg.jobportal.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.ujjwalgarg.jobportal.controller.payload.auth.NewCandidateRequest;
import com.ujjwalgarg.jobportal.controller.payload.auth.NewRecruiterRequest;
import com.ujjwalgarg.jobportal.entity.User;
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
@ContextConfiguration(classes = {UserMapperImpl.class})
class UserMapperTest {

  @Autowired
  private UserMapper userMapper;

  private static Stream<Arguments> provideNewCandidateRequestSamples() {
    return Stream.of(
        Arguments.of(
            new NewCandidateRequest(
                "candidate1@example.com",
                "password123",
                "Alice",
                "Johnson",
                "Short about Alice",
                "https://twitter.com/alice",
                "https://linkedin.com/in/alice",
                "https://github.com/alice",
                "1234567890"
            ),
            User.builder()
                .email("candidate1@example.com")
                .password("password123")
                .build()
        ),
        Arguments.of(
            new NewCandidateRequest(
                "candidate2@example.com",
                "password456",
                "Bob",
                "Smith",
                "Short about Bob",
                null,
                null,
                null,
                "0987654321"
            ),
            User.builder()
                .email("candidate2@example.com")
                .password("password456")
                .build()
        ),
        Arguments.of(
            new NewCandidateRequest(
                "candidate3@example.com",
                "password789",
                "Charlie",
                "Brown",
                "Short about Charlie",
                "https://twitter.com/charlie",
                "https://linkedin.com/in/charlie",
                "https://github.com/charlie",
                null
            ),
            User.builder()
                .email("candidate3@example.com")
                .password("password789")
                .build()
        )
    );
  }

  private static Stream<Arguments> provideNewRecruiterRequestSamples() {
    return Stream.of(
        Arguments.of(
            new NewRecruiterRequest(
                "recruiter1@example.com",
                "password123",
                "Alice",
                "Johnson",
                null,
                "Tech Solutions Inc.",
                "San Francisco",
                "California",
                "USA"
            ),
            User.builder()
                .email("recruiter1@example.com")
                .password("password123")
                .build()
        ),
        Arguments.of(
            new NewRecruiterRequest(
                "recruiter2@example.com",
                "password456",
                "Bob",
                "Smith",
                42,
                null,
                null,
                null,
                null
            ),
            User.builder()
                .email("recruiter2@example.com")
                .password("password456")
                .build()
        ),
        Arguments.of(
            new NewRecruiterRequest(
                "recruiter3@example.com",
                "password789",
                "Charlie",
                "Brown",
                null,
                null,
                null,
                null,
                null
            ),
            User.builder()
                .email("recruiter3@example.com")
                .password("password789")
                .build()
        )
    );
  }

  @ParameterizedTest
  @MethodSource("provideNewCandidateRequestSamples")
  @DisplayName("Test fromNewCandidateRequest()")
  void testFromNewCandidateRequest(NewCandidateRequest request, User expectedUser) {
    User user = userMapper.fromNewCandidateRequest(request);

    assertNotNull(user);
    assertNull(user.getId());
    assertEquals(expectedUser.getEmail(), user.getEmail());
    assertEquals(expectedUser.getPassword(), user.getPassword());
    assertNull(user.getRegistrationDate());
    assertNull(user.getRole());
  }

  @ParameterizedTest
  @MethodSource("provideNewRecruiterRequestSamples")
  @DisplayName("Test fromNewRecruiterRequest()")
  void testFromNewRecruiterRequest(NewRecruiterRequest request, User expectedUser) {
    User user = userMapper.fromNewRecruiterRequest(request);

    assertNotNull(user);
    assertNull(user.getId());
    assertEquals(expectedUser.getEmail(), user.getEmail());
    assertEquals(expectedUser.getPassword(), user.getPassword());
    assertNull(user.getRegistrationDate());
    assertNull(user.getRole());
  }
}
