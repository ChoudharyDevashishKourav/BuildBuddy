// UpdateApplicationRequest.java
package com.buildbuddy.application.dto;

import lombok.Data;

@Data
public class UpdateApplicationRequest {
    private String status; // ACCEPTED or REJECTED
}