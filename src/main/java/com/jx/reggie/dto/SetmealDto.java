package com.jx.reggie.dto;


import com.jx.reggie.entity.Setmeal;
import com.jx.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
