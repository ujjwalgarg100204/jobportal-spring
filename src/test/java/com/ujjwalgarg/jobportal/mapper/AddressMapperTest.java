package com.ujjwalgarg.jobportal.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.ujjwalgarg.jobportal.controller.payload.auth.NewRecruiterRequest;
import com.ujjwalgarg.jobportal.entity.Address;
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
@ContextConfiguration(classes = AddressMapperImpl.class)
class AddressMapperTest {

  @Autowired
  private AddressMapper addressMapper;

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
            Address.builder()
                .city("San Francisco")
                .state("California")
                .country("USA")
                .build()
        ),
        Arguments.of(
            new NewRecruiterRequest(
                "recruiter2@example.com",
                "password456",
                "Bob",
                "Smith",
                42,
                "Company B",
                "Los Angeles",
                "California",
                "USA"
            ),
            Address.builder()
                .city("Los Angeles")
                .state("California")
                .country("USA")
                .build()
        ),
        Arguments.of(
            new NewRecruiterRequest(
                "recruiter3@example.com",
                "password789",
                "Charlie",
                "Brown",
                null,
                "Company C",
                "Austin",
                "Texas",
                "USA"
            ),
            Address.builder()
                .city("Austin")
                .state("Texas")
                .country("USA")
                .build()
        ),
        Arguments.of(
            new NewRecruiterRequest(
                "recruiter3@example.com",
                "password789",
                "Charlie",
                "Brown",
                null,
                "Company C",
                null,
                "Texas",
                "USA"
            ),
            Address.builder()
                .city(null)
                .state("Texas")
                .country("USA")
                .build()
        ),
        Arguments.of(
            new NewRecruiterRequest(
                "recruiter3@example.com",
                "password789",
                "Charlie",
                "Brown",
                null,
                "Company C",
                null,
                null,
                null
            ),
            new Address()
        )
    );
  }

  @ParameterizedTest
  @MethodSource("provideNewRecruiterRequestSamples")
  @DisplayName("Test fromNewRecruiterRequest()")
  void testFromNewRecruiterRequest(NewRecruiterRequest request, Address expectedAddress) {
    Address address = addressMapper.fromNewRecruiterRequest(request);

    assertNotNull(address);
    assertNull(address.getId());
    assertEquals(expectedAddress.getCity(), address.getCity());
    assertEquals(expectedAddress.getState(), address.getState());
    assertEquals(expectedAddress.getCountry(), address.getCountry());
  }

}
