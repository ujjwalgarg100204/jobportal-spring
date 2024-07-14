package com.ujjwalgarg.jobportal.util;

import static org.junit.jupiter.api.Assertions.*;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MapperUtilsTest {

  private TestObject testObject;

  @BeforeEach
  void setUp() {
    testObject = new TestObject();
  }

  @Test
  @DisplayName("Should return true when all properties are null")
  void shouldReturnTrueWhenAllPropertiesAreNull() {
    assertTrue(MapperUtils.hasAllPropertiesNull(testObject));
  }

  @Test
  @DisplayName("Should return false when at least one property is not null")
  void shouldReturnFalseWhenAtLeastOnePropertyIsNotNull() {
    testObject.setStringField("Not null");
    assertFalse(MapperUtils.hasAllPropertiesNull(testObject));

    testObject.setStringField(null);
    testObject.setIntField(42);
    assertFalse(MapperUtils.hasAllPropertiesNull(testObject));

    testObject.setIntField(null);
    testObject.setBoolField(true);
    assertFalse(MapperUtils.hasAllPropertiesNull(testObject));
  }

  @Test
  @DisplayName("Should return true for an object with no properties")
  void shouldReturnTrueForObjectWithNoProperties() {
    class EmptyObject {

    }
    assertTrue(MapperUtils.hasAllPropertiesNull(new EmptyObject()));
  }

  @Test
  @DisplayName("Should throw NullPointerException when object is null")
  void shouldThrowNullPointerExceptionWhenObjectIsNull() {
    assertThrows(NullPointerException.class, () -> MapperUtils.hasAllPropertiesNull(null));
  }

  @Test
  @DisplayName("Should ignore non-getter methods")
  void shouldIgnoreNonGetterMethods() {
    class ObjectWithNonGetter {

      public void setSomething(String s) {
      }

      public String getNonNull() {
        return "Not null";
      }
    }
    assertFalse(MapperUtils.hasAllPropertiesNull(new ObjectWithNonGetter()));
  }

  @Setter
  @Getter
  private static class TestObject {

    private String stringField;
    private Integer intField;
    private Boolean boolField;

  }
}
