package com.jx.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.reggie.dto.SetmealDto;
import com.jx.reggie.entity.Setmeal;
import com.jx.reggie.entity.SetmealDish;
import com.jx.reggie.mapper.SetmealMapper;
import com.jx.reggie.service.SetmealDishService;
import com.jx.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmaelServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;


    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    @Transactional//开启事务注解  要操作两张表  保持数据的一致性
    @Override
    public void saveWithDish(SetmealDto setmealDto) {


        //保存套餐的基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            //获取setmealId
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联信息，操做setmeal_dish,执行insert
        setmealDishService.saveBatch(setmealDishes);


    }
}
