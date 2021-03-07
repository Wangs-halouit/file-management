package com.halouit.filemanagement.service;

import com.halouit.filemanagement.config.FileConfig;
import com.halouit.filemanagement.config.RedisConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class FileRedisService {
    @Autowired
    private RedisConfig redisConfig;

    @Autowired
    private FileConfig fileConfig;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    public void set(String tempId,Long id){
        redisTemplate.opsForValue()
                .set(redisConfig.getPrefix()+"_"+tempId,id.toString(),fileConfig.getExpiration());
    }

    public Long get(String tempId){

        String s = redisTemplate.opsForValue().get(redisConfig.getPrefix() + "_" + tempId);
        if(StringUtils.isBlank(s)){
            throw new RuntimeException("您访问的文件不存在");
        }
        return Long.valueOf(s);
    }
}
