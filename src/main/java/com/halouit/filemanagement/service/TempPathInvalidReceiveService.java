package com.halouit.filemanagement.service;

import com.halouit.filemanagement.entity.FileInfo;
import com.halouit.filemanagement.repository.FileInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RabbitListener(queues = "temp.invalid")
public class TempPathInvalidReceiveService {
    @Autowired
    private FileInfoRepository fileInfoRepository;

    @RabbitHandler
    public void receive(Long id){
        deleteTempPath(id);
    }

    @Transactional
    public void deleteTempPath(Long id){
        FileInfo fileInfo = fileInfoRepository.findById(id).orElseThrow(() -> new RuntimeException("文件不存在"));
        log.info("id:{}移除tempId",id);
        //更新
        fileInfo.setTempPath("");
        fileInfoRepository.save(fileInfo);
    }

}
