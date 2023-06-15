package com.infinity.datamodel.domain;

import lombok.Data;

@Data public class Relation {

    private String         name;
    private String         sourceName;
    private String         destinationName;
    private String         relationText;
    private String         type;

    private FunctionalFlow functionalFlow;

}
