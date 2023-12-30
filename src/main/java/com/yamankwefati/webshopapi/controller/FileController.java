package com.yamankwefati.webshopapi.controller;

import com.yamankwefati.webshopapi.dao.storage.FileSystemStorageDAO;
import com.yamankwefati.webshopapi.exception.StorageFileNotFoundException;
import com.yamankwefati.webshopapi.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileSystemStorageDAO storageService;

    @Autowired
    public FileController(FileSystemStorageDAO storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/")
    public ApiResponse<String> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                RedirectAttributes redirectAttributes) {
        storageService.store(file);
        return new ApiResponse<>(HttpStatus.ACCEPTED, file.getOriginalFilename());
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
