package com.ujjwalgarg.jobportal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.bind.annotation.GetMapping;

import com.ujjwalgarg.jobportal.controller.payload.response.GetJobByIdResponse;
import com.ujjwalgarg.jobportal.entity.Job;

/**
 * JobMapper
 */
@Mapper(componentModel = "spring")
public interface JobMapper {

    @Mapping(target = "noOfAppliedCandidates", source = "noOfAppliedCandidates")
    GetJobByIdResponse toGetJobByIdResponse(Job job, Integer noOfAppliedCandidates);

}
