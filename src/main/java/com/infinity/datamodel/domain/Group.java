package com.infinity.datamodel.domain;

import lombok.Data;

import java.util.List;

@Data public class Group {
    private String          name;
    private List<Component> components;
}
