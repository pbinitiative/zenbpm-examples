package eu.bpm4.portal.zenbpm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartitionedResponse<T> {

    private List<Partition<T>> partitions;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Partition<T> {
        private List<T> items;
    }
}
