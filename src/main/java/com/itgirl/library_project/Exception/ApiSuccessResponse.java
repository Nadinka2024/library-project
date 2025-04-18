package com.itgirl.library_project.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiSuccessResponse {
    private String message;
    private Object data;
}