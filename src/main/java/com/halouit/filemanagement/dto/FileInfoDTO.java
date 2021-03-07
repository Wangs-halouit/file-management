package com.halouit.filemanagement.dto;

import lombok.Data;

@Data
public class FileInfoDTO {
    private Long id;
    /**
     * 文件原始名称
     */
    private String name;

    /**
     * 文件扩展名称
     */
    private String ext;

    /**
     * 文件大小，kb
     */
    private Long size;

}
