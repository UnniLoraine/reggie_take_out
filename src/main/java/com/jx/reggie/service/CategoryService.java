package com.jx.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jx.reggie.entity.Category;

public interface CategoryService extends IService<Category> {

    //自定义删除分类
    public void remove(Long id);

}
