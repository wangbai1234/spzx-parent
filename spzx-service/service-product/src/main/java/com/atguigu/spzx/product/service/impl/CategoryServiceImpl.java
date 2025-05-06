package com.atguigu.spzx.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.model.entity.product.Category;
import com.atguigu.spzx.product.mapper.CategoryMapper;
import com.atguigu.spzx.product.service.CategoryService;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private RedisTemplate<String , String> redisTemplate ;
    @Override
    //@Cacheable(value = "findcategory" , key = "'one'")
    public List<Category> findOneCategory() {
        // 从Redis缓存中查询所有的一级分类数据
        String categoryListJSON = redisTemplate.opsForValue().get("category:one");
        if(!StringUtils.isEmpty(categoryListJSON)) {
            List<Category> categoryList = JSON.parseArray(categoryListJSON, Category.class);
            log.info("从Redis缓存中查询到了所有的一级分类数据");
            return categoryList ;
        }

        List<Category> categoryList = categoryMapper.findOneCategory();
        log.info("从数据库中查询到了所有的一级分类数据");
        redisTemplate.opsForValue().set("category:one" , JSON.toJSONString(categoryList) , 7 , TimeUnit.DAYS);
        return categoryList ;
    }

    @Override
    @Cacheable(value = "category" , key = "'all'")
    public List<Category> findCategoryTree() {
       //获取所有分类
        List<Category> categoryList=categoryMapper.findAll();
        //获取所有的一级分类
        List<Category> oneCategoryList = categoryList.stream()
                                       .filter(item -> item.getParentId().longValue() == 0)
                                       .collect(Collectors.toList());
        //获取所有的二级分类
        if(!CollectionUtils.isEmpty(oneCategoryList)){
        oneCategoryList.forEach(oneCategory-> {
                            List<Category> twoCategoryList = categoryList.stream().filter(item ->
                            item.getParentId().longValue() == oneCategory.getId().longValue())
                            .collect(Collectors.toList());
            oneCategory.setChildren(twoCategoryList);
        //获取所有的三级分类
        if(!CollectionUtils.isEmpty(twoCategoryList)) {
        twoCategoryList.forEach(twoCategory -> {
                            List<Category> threeCategoryList = categoryList.stream().filter(item ->
                            item.getParentId().longValue() == twoCategory.getId().longValue())
                            .collect(Collectors.toList());
            twoCategory.setChildren(threeCategoryList);
                        });
                    }
                });

        }
        return oneCategoryList;
    }
}
