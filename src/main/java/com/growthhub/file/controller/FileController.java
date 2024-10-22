package com.growthhub.file.controller;

import com.growthhub.file.service.NCPStorageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final NCPStorageService ncpStorageService;

//    @Operation(summary = "파일 업로드", description = "multipart/form-data를 이용한 파일 업로드 api<br>" +
//            "path: bucket에 저장되는 파일 경로, request parameter로 값 전송<br>" +
//            "file: 사진과 같은 파일, form-data로 전송")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestParam("path") String path,
            @RequestPart("file") MultipartFile multipartFile
    ) {
        String fileUrl = ncpStorageService.uploadFile(multipartFile, path);
        return ResponseEntity.ok(fileUrl);
    }
}
