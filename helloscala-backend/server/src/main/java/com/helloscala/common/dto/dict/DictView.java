package com.helloscala.common.dto.dict;

import com.helloscala.common.entity.DictData;
import lombok.Data;

import java.util.List;

/**
 * @author Steve Zou
 */
@Data
public class DictView {
    private String name;

    private String type;

    private DictData defaultValue;


    private List<DictData> values;
}
