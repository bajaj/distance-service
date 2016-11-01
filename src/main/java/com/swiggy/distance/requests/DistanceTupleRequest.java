package com.swiggy.distance.requests;

import lombok.*;
import java.util.List;

/**
 * Created by atulagrawal1 on 22/02/16.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class    DistanceTupleRequest {
    private List<Tuples> tuples;


}
