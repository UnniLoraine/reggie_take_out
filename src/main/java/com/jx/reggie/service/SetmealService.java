package com.jx.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jx.reggie.dto.SetmealDto;
import com.jx.reggie.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);
}
