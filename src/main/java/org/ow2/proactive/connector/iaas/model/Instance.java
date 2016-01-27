package org.ow2.proactive.connector.iaas.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Wither;


@EqualsAndHashCode(exclude = { "image", "number", "minRam", "minCores", "status" })
@Getter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Wither
@Builder
public class Instance {

    private String id;
    private String tag;
    private String image;
    private String number;
    private String minRam;
    private String minCores;
    private String status;

}