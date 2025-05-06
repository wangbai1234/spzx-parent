package com.atguigu.spzx.manager.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.atguigu.spzx.manager.mapper.CategoryMapper;
import com.atguigu.spzx.model.vo.product.CategoryExcelVo;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public class ExcelListener<T> implements ReadListener<T> {

    //构造传递mapper，用来加载到数据库
    private CategoryMapper categoryMapper;
    public ExcelListener(CategoryMapper categoryMapper){
        this.categoryMapper=categoryMapper;
    }

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<CategoryExcelVo> categoryList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
       //每次解析一行数据就放入list，当达到100就清空list
        categoryList.add((CategoryExcelVo)t);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (categoryList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            categoryList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }



    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
    }
    private void saveData() {
        categoryMapper.batchInsert((List<CategoryExcelVo>) categoryList);
    }
}
