package com.jx.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jx.reggie.dto.DishDto;
import com.jx.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    //新增菜品，同时插入菜品对应的够味数据，需要操作两张表：dish dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);

    //修改菜品信息和对应的口味信息
    void updateWithFlavor(DishDto dishDto);
}
