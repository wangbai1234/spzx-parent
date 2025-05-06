package com.atguigu.spzx.manager.utils;

import com.atguigu.spzx.model.entity.system.SysMenu;

import java.util.ArrayList;
import java.util.List;
//封装树形菜单的数据
public class MenuHelper {
    //查询所有菜单，返回list集合
    public  static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {

     List<SysMenu> trees=new ArrayList<>();
        for (SysMenu sysMenu : sysMenuList) {
            if (sysMenu.getParentId().longValue()==0) {
                trees.add(findChildren(sysMenu, sysMenuList));
            }
        }
        return trees;
    }

    public static SysMenu findChildren(SysMenu sysMenu, List<SysMenu> treeNodes) {
         //进行递归查询
        //初始化子节点
        sysMenu.setChildren(new ArrayList<SysMenu>());
        //查询实现当前用户id和parentid的值是否系统
        for (SysMenu it : treeNodes) {
            if(sysMenu.getId().longValue() == it.getParentId().longValue()) {

                sysMenu.getChildren().add(findChildren(it,treeNodes));
            }
        }
        return sysMenu;
    }
}
