package com.ujjwalgarg.jobportal.service;

import com.ujjwalgarg.jobportal.exception.NotFoundException;
import java.util.Map;

public interface FileUploadService {

  void deleteFile(String filePath) throws NotFoundException;

  boolean uploadFile(String filePath, byte[] file);

  boolean uploadFile(String filePath, byte[] file, Map<String, String> metadata);

  String getFileUrl(String filePath) throws NotFoundException;
}
