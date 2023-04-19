package com.jx.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.reggie.entity.Setmeal;
import com.jx.reggie.mapper.SetmealMapper;
import com.jx.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SetmaelServiceImpl extends ServiceImpl<SetmealMapper,Setmeal>implements SetmealService{
}
