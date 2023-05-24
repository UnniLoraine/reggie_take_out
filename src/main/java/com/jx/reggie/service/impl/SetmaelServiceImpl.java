package com.jx.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.reggie.common.CustomException;
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

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    @Transactional//开启事务注解  要操作两张表  保持数据的一致性
    @Override
    public void removeWithDish(List<Long> ids) {
        //select count(*) from setmeal where id in (1,2,3) and status=1;
        //查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper .in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count=this.count(queryWrapper);

        if (count>0){
            //如果不也能删除，抛出一个业务异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        //如果可以删除，先删除套餐表中的数据----setmael
        this.removeByIds(ids);


        //delete from setmeal_dish where setmeal_id in(1,2,3)
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId,ids);

        //删除关系表中的数据------setmeal_dish
        setmealDishService.remove(queryWrapper1);


    }
}
