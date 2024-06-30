package com.ujjwalgarg.jobportal.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ujjwalgarg.jobportal.annotation.FileContentType;
import com.ujjwalgarg.jobportal.annotation.FileSize;
import com.ujjwalgarg.jobportal.controller.payload.Response;
import com.ujjwalgarg.jobportal.controller.payload.request.CreateNewCompanyRequest;
import com.ujjwalgarg.jobportal.controller.payload.request.UpdateCompanyRequest;
import com.ujjwalgarg.jobportal.controller.payload.response.GetAllCompaniesResponse;
import com.ujjwalgarg.jobportal.entity.Company;
import com.ujjwalgarg.jobportal.exception.EntityNotFoundException;
import com.ujjwalgarg.jobportal.exception.ResourceNotFoundException;
import com.ujjwalgarg.jobportal.service.CompanyService;
import com.ujjwalgarg.jobportal.service.S3FileService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

/**
 * CompanyController
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class CompanyController {

    private final CompanyService companyService;
    private final S3FileService s3FileService;

    @GetMapping(value = "/company", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Response<List<GetAllCompaniesResponse>>> getAllCompany() {
        List<Company> companies = this.companyService.findAll();
        List<GetAllCompaniesResponse> responseCompanies = companies.stream()
                .map(c -> {
                    String logoUrl = null;
                    if (c.getHasLogo()) {
                        logoUrl = this.s3FileService
                                .getFilePresignedUrl(CompanyService.getCompanyLogoS3Path(c.getId()));
                    }
                    return GetAllCompaniesResponse.fromCompany(c, logoUrl);

                }).toList();

        var response = Response.<List<GetAllCompaniesResponse>>builder()
                .success(true)
                .data(responseCompanies)
                .message("Companies fetched successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/company/{id}/logo", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Response<String>> getCompanyLogo(
            @Valid @NotNull @PathVariable("id") Integer companyId) {
        Company company = this.companyService
                .findById(companyId)
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format("Company not found with id:%d", companyId)));
        if (!company.getHasLogo()) {
            throw new ResourceNotFoundException(
                    String.format("Company with id:%d does not have a logo, but was requested", companyId));
        }

        String presignedUrl = this.s3FileService.getFilePresignedUrl(
                CompanyService.getCompanyLogoS3Path(company.getId()));
        var response = Response.<String>builder()
                .success(true)
                .data(presignedUrl)
                .message("Presigned URL generated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @Transactional
    @PostMapping(value = "/company", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<Void>> createNewCompany(
            @Valid @RequestPart(name = "company", required = true) CreateNewCompanyRequest companyRequest,
            @Valid @FileSize(min = 100, max = (long) 5e6, message = "File size must be between 100 bytes and 5 MB.") @FileContentType(allowedTypes = {
                    "image/png", "image/jpg",
                    "image/jpeg" }) @RequestPart(name = "logo", required = false) MultipartFile logo)
            throws IOException {
        Company newCompany = companyRequest.toCompany();

        Company savedCompany = this.companyService.createNew(newCompany);

        if (logo != null) {
            String logoPath = CompanyService.getCompanyLogoS3Path(savedCompany.getId());
            Map<String, String> metadata = new HashMap<>();
            metadata.put("Content-Type", logo.getContentType());
            boolean success = this.s3FileService.uploadFile(logoPath, logo.getBytes(), metadata);
            savedCompany.setHasLogo(success);
        }

        this.companyService.updateJob(savedCompany);

        var response = Response.<Void>builder()
                .success(true)
                .data(null)
                .message("Company created successfully")
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/company", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<Void>> updateCompany(
            @Valid @RequestPart(name = "company", required = true) UpdateCompanyRequest companyRequest,
            @Valid @FileSize(min = 100, max = (long) 5e6, message = "File size must be between 100 bytes and 5 MB.") @FileContentType(allowedTypes = {
                    "image/png", "image/jpg",
                    "image/jpeg" }) @RequestPart(name = "logo", required = false) MultipartFile logo)
            throws IOException {
        Company company = companyRequest.toCompany();

        if (logo != null) {
            String logoPath = CompanyService.getCompanyLogoS3Path(company.getId());
            Map<String, String> metadata = new HashMap<>();
            metadata.put("Content-Type", logo.getContentType());
            boolean success = this.s3FileService.uploadFile(logoPath, logo.getBytes(), metadata);
            company.setHasLogo(success);
        }

        if (!company.getHasLogo()) {
            this.s3FileService.deleteFile(CompanyService.getCompanyLogoS3Path(company.getId()));
        }

        this.companyService.updateJob(company);

        var response = Response.<Void>builder()
                .success(true)
                .data(null)
                .message("Company updated successfully")
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
