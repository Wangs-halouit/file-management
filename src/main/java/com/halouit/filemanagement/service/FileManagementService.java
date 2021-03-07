package com.halouit.filemanagement.service;

import com.halouit.filemanagement.config.FileConfig;
import com.halouit.filemanagement.dto.FileInfoDTO;
import com.halouit.filemanagement.dto.FileTempInfoDTO;
import com.halouit.filemanagement.entity.FileInfo;
import com.halouit.filemanagement.repository.FileInfoRepository;
import com.halouit.filemanagement.utils.ModelMapperUtils;
import com.halouit.filemanagement.utils.UuidUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FileManagementService {
    @Autowired
    private FileConfig fileConfig;

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Autowired
    private FileRedisService fileRedisService;

    @Autowired
    private TempPathInvalidSendService  sendService;

    public FileInfoDTO upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }

        FileInfo fileInfo = getFileInfo(file);
        File localFile = getFile(fileInfo);

        try {
            if (!localFile.getParentFile().exists()) {
                localFile.getParentFile().mkdirs();
                localFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        Path localFilePath = Paths.get(localFile.getAbsolutePath());

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, localFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件保存异常");
        }

        fileInfoRepository.save(fileInfo);

        return ModelMapperUtils.mapTo(fileInfo, FileInfoDTO.class);
    }

    public FileTempInfoDTO getTempInfo(Long id) {
        String uuid = UuidUtils.getId();
        fileRedisService.set(uuid, id);

        FileTempInfoDTO result = new FileTempInfoDTO();
        result.setId(uuid);

        FileInfo fileInfo = fileInfoRepository.findById(id).orElseThrow(() -> new RuntimeException("文件不存在"));
        fileInfo.setTempPath(uuid);
        fileInfoRepository.save(fileInfo);
        sendService.send(id);

        return result;
    }

    public FileSystemResource getFileByTempId(String id) {
        Path absolutePath = getPathByTempId(id);
        return new FileSystemResource(new File(absolutePath.toString()));
    }

    public Path getPathByTempId(String id) {
        Long fileId = fileRedisService.get(id);

        FileInfo fileInfo = fileInfoRepository.findById(fileId).orElseThrow(() -> new RuntimeException(id + "文件不存在"));
        String path = fileInfo.getPath();
        return Paths.get(fileConfig.getPath(), path);
    }



    //rabbit
    public FileSystemResource findFileByTempId(String id) {
        FileInfo fileInfo = fileInfoRepository.findFileInfoByTempPath(id);
        if(fileInfo == null){
            throw new RuntimeException("文件不存在");
        }
        String path = fileInfo.getPath();
        Path absolutePath = Paths.get(fileConfig.getPath(), path);
        return new FileSystemResource(new File(absolutePath.toString()));
    }


    public ResponseEntity<byte[]> getTempImage(String id) throws IOException {
        Path absolutePath = getPathByTempId(id);

        String path = absolutePath.toString();
        String[] split = StringUtils.split(path, ".");
        String ext = split[split.length - 1];
        ext = ext.toLowerCase();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        switch (ext) {
            case "png":
                headers.setContentType(MediaType.IMAGE_PNG);
                break;
            case "gif":
                headers.setContentType(MediaType.IMAGE_GIF);
                break;
            case "bmp":
                headers.setContentType(MediaType.parseMediaType("image/bmp"));
            default:
                headers.setContentType(MediaType.IMAGE_JPEG);
        }
        byte[] bytes = Files.readAllBytes(absolutePath);

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }


    private File getFile(FileInfo fileInfo) {
        Path path = Paths.get(fileConfig.getPath(), fileInfo.getPath());
        return new File(path.toAbsolutePath().toString());
    }

    private FileInfo getFileInfo(MultipartFile file) {
        final String originalFilename = file.getOriginalFilename();
        RuntimeException exception = new RuntimeException("文件为空！");
        if (StringUtils.isBlank(originalFilename)) {
            throw exception;
        }

        String[] names = originalFilename.split("\\.");
        String ext = names[names.length - 1];
        if (StringUtils.isBlank(ext)) {
            throw exception;
        }

        FileInfo result = new FileInfo();
        result.setName(originalFilename.replace("." + ext, ""));
        result.setExt(ext);

        String path = "";
        path += "/" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
        path += "/" + UuidUtils.getId();
        path += '.' + ext;

        result.setPath(path);
        result.setSize(file.getSize());
        return result;
    }
}
