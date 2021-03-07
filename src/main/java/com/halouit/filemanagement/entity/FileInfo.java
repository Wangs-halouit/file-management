package com.halouit.filemanagement.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 文件原始名称
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 文件扩展名称
     */
    @Column(length = 20)
    private String ext;

    /**
     * 文件大小，byte
     */
    @Column(nullable = false)
    private Long size;

    /**
     * 文件保存路径
     */
    @Column(nullable = false)
    private String path;

    /**
     * 附属信息，如图片的宽高
     */
    @Column(columnDefinition = "json")
    private String meta;
}
