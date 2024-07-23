package com.ujjwalgarg.jobportal.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.ujjwalgarg.jobportal.constant.ExperienceLevel;
import com.ujjwalgarg.jobportal.controller.payload.candidateprofile.SkillGetRequestDto;
import com.ujjwalgarg.jobportal.entity.Skill;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SkillMapperTest {

  @Autowired
  private SkillMapper skillMapper;

  private static Stream<Arguments> provideValidSkills() {
    return Stream.of(
        Arguments.of(
            createSkill(1, "Java", "5 years", ExperienceLevel.ADVANCE),
            new SkillGetRequestDto(1, "Java", "5 years", ExperienceLevel.ADVANCE)
        ),
        Arguments.of(
            createSkill(2, "Python", "2 years", ExperienceLevel.INTERMEDIATE),
            new SkillGetRequestDto(2, "Python", "2 years", ExperienceLevel.INTERMEDIATE)
        ),
        Arguments.of(
            createSkill(3, "React", "1 year", ExperienceLevel.BEGINNER),
            new SkillGetRequestDto(3, "React", "1 year", ExperienceLevel.BEGINNER)
        ),
        Arguments.of(
            createSkill(null, "Docker", "3 years", ExperienceLevel.INTERMEDIATE),
            new SkillGetRequestDto(null, "Docker", "3 years", ExperienceLevel.INTERMEDIATE)
        )
    );
  }

  private static Skill createSkill(Integer id, String name, String yearsOfExperience,
      ExperienceLevel experienceLevel) {
    Skill skill = new Skill();
    skill.setId(id);
    skill.setName(name);
    skill.setYearsOfExperience(yearsOfExperience);
    skill.setExperienceLevel(experienceLevel);
    return skill;
  }

  @ParameterizedTest(name = "{index} - {displayName}")
  @MethodSource("provideValidSkills")
  @DisplayName("Test toSkillGetRequest() with valid Skill")
  void toSkillGetRequest_validSkill_returnsMappedDto(Skill skill, SkillGetRequestDto expected) {
    SkillGetRequestDto actual = skillMapper.toSkillGetRequest(skill);
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  @DisplayName("Test toSkillGetRequest() when input is null")
  void toSkillGetRequest_nullInput_returnsNull() {
    assertNull(skillMapper.toSkillGetRequest(null));
  }
}
