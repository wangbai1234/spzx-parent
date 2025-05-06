package com.atguigu.spzx.manager.controller;

import cn.hutool.http.server.HttpServerResponse;
import com.atguigu.spzx.manager.service.CategoryService;
import com.atguigu.spzx.model.entity.product.Category;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/product/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //将excel中的信息上传到前端商品分类信息中
    @PostMapping("importData")
    public Result importData(MultipartFile file) {
        categoryService.importData(file);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //下载商品分类信息到本地Excel
    @GetMapping(value = "/exportData")
    public void exportData(HttpServletResponse response) {
        categoryService.exportData(response);
    }

    //通过懒加载获取商品信息
    @GetMapping(value = "/findByParentId/{parentId}")
    public Result<List<Category>> findByParentId(@PathVariable Long parentId) {
        List<Category> list = categoryService.findByParentId(parentId);
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }
}
