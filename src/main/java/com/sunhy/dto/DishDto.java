package com.sunhy.dto;

import com.sunhy.entity.Dish;
import com.sunhy.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
//这个地方的继承很关键
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
