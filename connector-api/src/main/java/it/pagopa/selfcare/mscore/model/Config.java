package it.pagopa.selfcare.mscore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Config {

    private String id;

    private String productFilter;

    private int firstPage;

    private int lastPage;

    private boolean enableKafkaScheduler;

}
