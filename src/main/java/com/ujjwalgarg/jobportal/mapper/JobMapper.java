package com.ujjwalgarg.jobportal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ujjwalgarg.jobportal.controller.payload.response.GetJobByIdResponse;
import com.ujjwalgarg.jobportal.entity.Job;

/**
 * JobMapper
 */
@Mapper(componentModel = "spring")
public interface JobMapper {

    @Mapping(target = "GetRecruiterProfileByIdResponse.profilePhotoUrl", ignore = true)
    GetJobByIdResponse toGetJobByIdResponse(Job job);

}
