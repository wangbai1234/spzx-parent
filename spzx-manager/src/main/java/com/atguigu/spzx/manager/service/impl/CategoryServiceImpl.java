package com.atguigu.spzx.manager.service.impl;

import cn.hutool.http.server.HttpServerResponse;
import com.alibaba.excel.EasyExcel;
import com.atguigu.spzx.common.exception.GuiguException;
import com.atguigu.spzx.manager.listener.ExcelListener;
import com.atguigu.spzx.manager.mapper.CategoryMapper;
import com.atguigu.spzx.manager.service.CategoryService;
import com.atguigu.spzx.model.entity.product.Category;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.product.CategoryExcelVo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
@Autowired
private CategoryMapper categoryMapper ;
    @Override
    public List<Category> findByParentId(Long parentId) {
        //根据id查询出所有的list集合
        List<Category> list=categoryMapper.selectByParentId(parentId);
        //遍历list集合，判断是否有子节点，如果有设置hasChildren为true
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(category -> {
                int count=categoryMapper.countByParentId(category.getId());
                if(count>0){
                    category.setHasChildren(true);
                }else{
                    category.setHasChildren(false);
                }
            });
        }
        return list;
    }

    //下载商品分类信息到本地Excel
    @Override
    public void exportData(HttpServletResponse response) {
        try {
            //1.设置响应头信息和其他信息
            // 设置响应结果类型
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("分类数据", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            //2.调用mapper方法查询所有分类，并放到list集合中
            List<Category> categoryList = categoryMapper.selectAll();
            List<CategoryExcelVo> categoryExcelVoList = new ArrayList<>();
            for (Category category : categoryList) {
                CategoryExcelVo categoryExcelVo = new CategoryExcelVo();
                BeanUtils.copyProperties(category,categoryExcelVo);
                categoryExcelVoList.add(categoryExcelVo);
            }
            //3.调用easyexcel的write方法将list集合写入到excel中
            EasyExcel.write(response.getOutputStream(), CategoryExcelVo.class)
                    .sheet("分类数据").doWrite(categoryExcelVoList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }
    }

    //将excel中的信息上传到前端商品分类信息中
    @Override
    public void importData(MultipartFile file) {
        ExcelListener<CategoryExcelVo> excelListener = new ExcelListener(categoryMapper);
        try {
            EasyExcel.read(file.getInputStream(), CategoryExcelVo.class, excelListener).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }

    }
}
