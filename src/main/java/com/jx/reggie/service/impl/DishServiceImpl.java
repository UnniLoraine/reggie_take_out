package com.jx.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.reggie.dto.DishDto;
import com.jx.reggie.entity.Dish;
import com.jx.reggie.entity.DishFlavor;
import com.jx.reggie.mapper.DishMapper;
import com.jx.reggie.service.DishFlavorService;
import com.jx.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品，同时插入菜品对应的够味数据
     * @param dishDto
     */
    @Override
    @Transactional//事务控制 涉及到多张表
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        //获取dishId
        Long dishId = dishDto.getId();

        //菜品口味  这个集合里面的数据没有dishId
        List<DishFlavor> flavors = dishDto.getFlavors();
        //遍历集合将dishId赋值给DishFlavor的dishId
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());


        //保存菜品的口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);

    }
}
