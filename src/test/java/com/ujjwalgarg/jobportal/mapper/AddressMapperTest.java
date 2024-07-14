package com.ujjwalgarg.jobportal.mapper;


import static org.assertj.core.api.Assertions.assertThat;

import com.ujjwalgarg.jobportal.entity.Address;
import com.ujjwalgarg.jobportal.mapper.impl.AddressMapperCustomImpl;
import com.ujjwalgarg.jobportal.validator.validatable.CompanyDetailsValidatable;
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
@ContextConfiguration(classes = {AddressMapperCustomImpl.class, AddressMapperImpl.class})
class AddressMapperTest {

  @Autowired
  private AddressMapperCustomImpl addressMapper;

  private static Stream<Arguments> addressMappingScenarios() {
    return Stream.of(
        Arguments.of(
            "All fields present",
            CompanyDetailsValidatableBuilder.builder()
                .companyId(1)
                .companyName("Test Company")
                .companyAddressCity("Test City")
                .companyAddressState("Test State")
                .companyAddressCountry("Test Country")
                .build(),
            Address.builder()
                .city("Test City")
                .state("Test State")
                .country("Test Country")
                .build()
        ),
        Arguments.of(
            "Only mandatory fields present",
            CompanyDetailsValidatableBuilder.builder()
                .companyAddressState("Test State")
                .companyAddressCountry("Test Country")
                .build(),
            Address.builder()
                .state("Test State")
                .country("Test Country")
                .build()
        ),
        Arguments.of(
            "All address fields null",
            CompanyDetailsValidatableBuilder.builder()
                .companyId(1)
                .companyName("Test Company")
                .build(),
            null
        )
    );
  }

  @DisplayName("Address mapping scenarios")
  @ParameterizedTest(name = "{0}")
  @MethodSource("addressMappingScenarios")
  void testAddressMapping(String scenario, CompanyDetailsValidatable input, Address expected) {
    Address result = addressMapper.fromCompanyDetailsValidatable(input);
    assertThat(result)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(expected);
  }

  // Helper class to build CompanyDetailsValidatable instances
  private static class CompanyDetailsValidatableBuilder {

    private Integer companyId;
    private String companyName;
    private String companyAddressCity;
    private String companyAddressState;
    private String companyAddressCountry;

    private CompanyDetailsValidatableBuilder() {
    }

    public static CompanyDetailsValidatableBuilder builder() {
      return new CompanyDetailsValidatableBuilder();
    }

    public CompanyDetailsValidatableBuilder companyId(Integer companyId) {
      this.companyId = companyId;
      return this;
    }

    public CompanyDetailsValidatableBuilder companyName(String companyName) {
      this.companyName = companyName;
      return this;
    }

    public CompanyDetailsValidatableBuilder companyAddressCity(String companyAddressCity) {
      this.companyAddressCity = companyAddressCity;
      return this;
    }

    public CompanyDetailsValidatableBuilder companyAddressState(String companyAddressState) {
      this.companyAddressState = companyAddressState;
      return this;
    }

    public CompanyDetailsValidatableBuilder companyAddressCountry(String companyAddressCountry) {
      this.companyAddressCountry = companyAddressCountry;
      return this;
    }

    public CompanyDetailsValidatable build() {
      return new CompanyDetailsValidatable() {
        @Override
        public Integer getCompanyId() {
          return companyId;
        }

        @Override
        public String getCompanyName() {
          return companyName;
        }

        @Override
        public String getCompanyAddressCity() {
          return companyAddressCity;
        }

        @Override
        public String getCompanyAddressState() {
          return companyAddressState;
        }

        @Override
        public String getCompanyAddressCountry() {
          return companyAddressCountry;
        }
      };
    }
  }
}
