package com.halouit.filemanagement.repository;

import com.halouit.filemanagement.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface FileInfoRepository extends JpaRepository<FileInfo,Long> {
    FileInfo findFileInfoByTempPath(String pathId);
}
