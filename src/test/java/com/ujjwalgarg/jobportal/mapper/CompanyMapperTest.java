package com.ujjwalgarg.jobportal.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.ujjwalgarg.jobportal.controller.payload.auth.NewRecruiterRequest;
import com.ujjwalgarg.jobportal.entity.Address;
import com.ujjwalgarg.jobportal.entity.Company;
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
@ContextConfiguration(classes = {CompanyMapperImpl.class, AddressMapperImpl.class})
class CompanyMapperTest {

  @Autowired
  private CompanyMapper companyMapper;

  private static Stream<Arguments> provideNewRecruiterRequests() {
    return Stream.of(
        Arguments.of(
            new NewRecruiterRequest(
                "recruiter@example.com",
                "securePassword",
                "Jane",
                "Smith",
                1,
                "Example Company",
                "CityName",
                "StateName",
                "CountryName"
            ),
            Company.builder()
                .id(1)
                .name("Example Company")
                .address(Address.builder()
                    .city("CityName")
                    .state("StateName")
                    .country("CountryName")
                    .build())
                .hasLogo(false)
                .build()
        ),
        Arguments.of(
            new NewRecruiterRequest(
                "recruiter2@example.com",
                "securePassword2",
                "John",
                "Doe",
                null,
                "Another Company",
                "AnotherCity",
                "AnotherState",
                "AnotherCountry"
            ),
            Company.builder()
                .name("Another Company")
                .address(Address.builder()
                    .city("AnotherCity")
                    .state("AnotherState")
                    .country("AnotherCountry")
                    .build())
                .hasLogo(false)
                .build()
        )
    );
  }

  @ParameterizedTest
  @MethodSource("provideNewRecruiterRequests")
  @DisplayName("Test fromNewRecruiterRequest()")
  void fromNewRecruiterRequest(NewRecruiterRequest request, Company expected) {
    Company actual = companyMapper.fromNewRecruiterRequest(request);

    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getAddress(), actual.getAddress());
    assertFalse(actual.getHasLogo());
    assertNull(actual.getRecruiterProfile());
    assertNull(actual.getJobs());
  }

}
