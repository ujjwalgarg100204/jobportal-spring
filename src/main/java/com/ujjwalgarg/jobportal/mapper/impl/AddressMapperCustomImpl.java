package com.ujjwalgarg.jobportal.mapper.impl;

import com.ujjwalgarg.jobportal.entity.Address;
import com.ujjwalgarg.jobportal.mapper.AddressMapper;
import com.ujjwalgarg.jobportal.mapper.AddressMapperImpl;
import com.ujjwalgarg.jobportal.util.MapperUtils;
import com.ujjwalgarg.jobportal.validator.validatable.CompanyDetailsValidatable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class AddressMapperCustomImpl implements AddressMapper {

  private final AddressMapperImpl addressMapper;

  @Override
  public Address fromCompanyDetailsValidatable(CompanyDetailsValidatable request) {
    var address = addressMapper.fromCompanyDetailsValidatable(request);
    return (MapperUtils.hasAllPropertiesNull(address)) ? null : address;
  }
}
