package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class File {
    String fileId;
    String name;
    String contentType;
    Long size;
    Integer userId;
    byte[] data;
}
