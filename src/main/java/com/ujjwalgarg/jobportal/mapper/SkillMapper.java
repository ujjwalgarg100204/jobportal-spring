package com.ujjwalgarg.jobportal.mapper;


import com.ujjwalgarg.jobportal.controller.payload.candidateprofile.SkillGetRequestDto;
import com.ujjwalgarg.jobportal.entity.Skill;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SkillMapper {

  SkillGetRequestDto toSkillGetRequest(Skill skill);
}
