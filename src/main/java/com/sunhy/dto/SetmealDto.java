package com.sunhy.dto;

import com.sunhy.entity.Setmeal;
import com.sunhy.entity.SetmealDish;
import com.sunhy.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
