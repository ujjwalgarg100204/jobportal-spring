package com.ujjwalgarg.jobportal.util;

import com.ujjwalgarg.jobportal.exception.GetterMethodInvokeException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

@Slf4j(topic = "MAPPER_UTILS")
public class MapperUtils {

  private MapperUtils() {
  }

  public static boolean hasAllPropertiesNull(Object obj) {
    return Arrays.stream(BeanUtils.getPropertyDescriptors(obj.getClass()))
        .filter(propertyDescriptor -> !propertyDescriptor.getName().equals("class"))
        .map(descriptor -> {
          Method method = descriptor.getReadMethod();
          try {
            return method.invoke(obj);
          } catch (Exception e) {
            log.error("Couldn't call getter method:{} of object:{} due to error:{}",
                method.getName(), obj.getClass(), e.toString());
            throw new GetterMethodInvokeException(
                String.format("Couldn't call getter method:%s of object:%s due to error:%s",
                    method.getName(),
                    obj.getClass(), e.getMessage()));
          }
        }).allMatch(Objects::isNull);
  }
}
