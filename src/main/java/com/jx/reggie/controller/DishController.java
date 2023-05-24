package com.jx.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jx.reggie.common.R;
import com.jx.reggie.dto.DishDto;
import com.jx.reggie.entity.Category;
import com.jx.reggie.entity.Dish;
import com.jx.reggie.entity.DishFlavor;
import com.jx.reggie.entity.Employee;
import com.jx.reggie.service.CategoryService;
import com.jx.reggie.service.DishFlavorService;
import com.jx.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     *
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("添加菜品成功");
    }

    /**
     * 菜品信息分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //构造分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();  //前端展示页面有菜品分类信息  而dish里面只有菜品分类id

        //构造条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件 搜索
        queryWrapper.like(name != null, Dish::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行查询
        dishService.page(pageInfo, queryWrapper);

        //对象拷贝  records为忽略拷贝的内容
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            //对象拷贝
            BeanUtils.copyProperties(item, dishDto);
            //获取菜品分类的id
            Long categoryId = item.getCategoryId();
            //通过菜品分类的id查询菜品分类的所有信息
            Category category = categoryService.getById(categoryId);
            //判断category是否为空 不为null 再赋值
            if (category!=null){
                //给dishDto 里面的categoryName菜品分类的名称  赋值
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }


    /**
     * 根据id查询菜品信息和对应的口味信息
     *    为了前端页面数据回写
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto>get(@PathVariable Long id){

        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String>update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功");
    }

    /**
     * 根据条件查询菜品数据
     * @param dish
     * @return
     */
    /*@GetMapping("/list")
    public R<List<Dish>>list(Dish dish){
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        //添加条件 查询状态为1的  起售 1    停售0
        queryWrapper.eq(Dish::getStatus,1);
        //排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        return R.success(list);
    }*/
    @GetMapping("/list")
    public R<List<DishDto>>list(Dish dish){
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        //添加条件 查询状态为1的  起售 1    停售0
        queryWrapper.eq(Dish::getStatus,1);
        //排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            //对象拷贝
            BeanUtils.copyProperties(item, dishDto);
            //获取菜品分类的id
            Long categoryId = item.getCategoryId();
            //通过菜品分类的id查询菜品分类的所有信息
            Category category = categoryService.getById(categoryId);
            //判断category是否为空 不为null 再赋值
            if (category!=null){
                //给dishDto 里面的categoryName菜品分类的名称  赋值
                dishDto.setCategoryName(category.getName());
            }

            Long dishId = item.getId();//当前菜品id
            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(queryWrapper1);//口味的集合
            dishDto.setFlavors(dishFlavorList);

            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }


}
