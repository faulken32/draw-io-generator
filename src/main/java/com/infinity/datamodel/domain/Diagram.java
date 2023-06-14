package com.infinity.datamodel.domain;

import java.util.List;

@lombok.Data public class Diagram {

    private String         name;
    private List<Group>    groups;
    private List<Relation> relations;
}
