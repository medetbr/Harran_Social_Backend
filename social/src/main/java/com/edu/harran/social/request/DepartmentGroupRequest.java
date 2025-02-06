package com.edu.harran.social.request;

import lombok.Data;

@Data
public class DepartmentGroupRequest {
    private String chatName;
    private String chatImage;
    private String department_id;
    private String user_id;
}
