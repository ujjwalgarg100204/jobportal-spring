package com.ujjwalgarg.jobportal.mapper;

import com.ujjwalgarg.jobportal.controller.payload.auth.NewRecruiterRequest;
import com.ujjwalgarg.jobportal.entity.Address;
import com.ujjwalgarg.jobportal.entity.Company;
import com.ujjwalgarg.jobportal.entity.RecruiterProfile;
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

  @ParameterizedTest(name = "{index} - Mapping fromNewRecruiterRequest() - {0}")
  @MethodSource("provideValidNewRecruiterRequests")
  @DisplayName("Test fromNewRecruiterRequest() with different scenarios")
  void testFromNewRecruiterRequest(NewRecruiterRequest request, RecruiterProfile expected) {
    RecruiterProfile actual = recruiterProfileMapper.fromNewRecruiterRequest(request);

    Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

}
