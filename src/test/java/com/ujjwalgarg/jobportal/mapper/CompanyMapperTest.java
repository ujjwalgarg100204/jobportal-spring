package com.ujjwalgarg.jobportal.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.ujjwalgarg.jobportal.entity.Address;
import com.ujjwalgarg.jobportal.entity.Company;
import com.ujjwalgarg.jobportal.validator.validatable.CompanyDetailsValidatable;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CompanyMapperTest {

  @Autowired
  private CompanyMapper companyMapper;

  private static Stream<Arguments> companyMappingScenarios() {
    return Stream.of(
        Arguments.of(
            "All fields present",
            CompanyDetailsValidatableTestImpl.builder()
                .companyId(1)
                .companyName("Test Company")
                .companyAddressCity("Test City")
                .companyAddressState("Test State")
                .companyAddressCountry("Test Country")
                .build(),
            Company.builder()
                .id(1)
                .name("Test Company")
                .hasLogo(false)
                .address(Address.builder()
                    .city("Test City")
                    .state("Test State")
                    .country("Test Country")
                    .build())
                .build()
        ),
        Arguments.of(
            "Only mandatory fields present",
            CompanyDetailsValidatableTestImpl.builder()
                .companyId(2)
                .companyName("Mandatory Company")
                .build(),
            Company.builder()
                .id(2)
                .name("Mandatory Company")
                .hasLogo(false)
                .build()
        ),
        Arguments.of(
            "All fields null",
            CompanyDetailsValidatableTestImpl.builder().build(),
            Company.builder().build()
        )
    );
  }

  @DisplayName("Company mapping scenarios")
  @ParameterizedTest(name = "{0}")
  @MethodSource("companyMappingScenarios")
  void testCompanyMapping(String scenario, CompanyDetailsValidatableTestImpl input,
      Company expected) {
    Company result = companyMapper.fromCompanyDetailsValidatable(input);

    assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

  // Helper class to implement CompanyDetailsValidatable using Lombok
  @Builder
  @Getter
  private static class CompanyDetailsValidatableTestImpl implements CompanyDetailsValidatable {

    Integer companyId;
    String companyName;
    String companyAddressCity;
    String companyAddressState;
    String companyAddressCountry;
  }
}
