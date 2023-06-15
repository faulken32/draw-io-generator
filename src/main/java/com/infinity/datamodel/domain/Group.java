package com.infinity.datamodel.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class Group {
    private String          name;
    private List<Component> components;
}
