package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_submenu")
public class OcSubmenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 菜单id
     */
    @Column(name = "menu_id")
    private Integer menuId;

    /**
     * 子菜单标题
     */
    @Column(name = "submenu_title")
    private String submenuTitle;

    /**
     * 是否为svg
     */
    @Column(name = "is_svg")
    private Boolean isSvg;

    /**
     * 子菜单图标名称
     */
    @Column(name = "submenu_icon")
    private String submenuIcon;

    /**
     * 子菜单路径
     */
    @Column(name = "submenu_path")
    private String submenuPath;

    /**
     * 子菜单排序
     */
    @Column(name = "submenu_order")
    private Integer submenuOrder;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取菜单id
     *
     * @return menu_id - 菜单id
     */
    public Integer getMenuId() {
        return menuId;
    }

    /**
     * 设置菜单id
     *
     * @param menuId 菜单id
     */
    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    /**
     * 获取子菜单标题
     *
     * @return submenu_title - 子菜单标题
     */
    public String getSubmenuTitle() {
        return submenuTitle;
    }

    /**
     * 设置子菜单标题
     *
     * @param submenuTitle 子菜单标题
     */
    public void setSubmenuTitle(String submenuTitle) {
        this.submenuTitle = submenuTitle;
    }

    /**
     * 获取是否为svg
     *
     * @return is_svg - 是否为svg
     */
    public Boolean getIsSvg() {
        return isSvg;
    }

    /**
     * 设置是否为svg
     *
     * @param isSvg 是否为svg
     */
    public void setIsSvg(Boolean isSvg) {
        this.isSvg = isSvg;
    }

    /**
     * 获取子菜单图标名称
     *
     * @return submenu_icon - 子菜单图标名称
     */
    public String getSubmenuIcon() {
        return submenuIcon;
    }

    /**
     * 设置子菜单图标名称
     *
     * @param submenuIcon 子菜单图标名称
     */
    public void setSubmenuIcon(String submenuIcon) {
        this.submenuIcon = submenuIcon;
    }

    /**
     * 获取子菜单路径
     *
     * @return submenu_path - 子菜单路径
     */
    public String getSubmenuPath() {
        return submenuPath;
    }

    /**
     * 设置子菜单路径
     *
     * @param submenuPath 子菜单路径
     */
    public void setSubmenuPath(String submenuPath) {
        this.submenuPath = submenuPath;
    }

    /**
     * 获取子菜单排序
     *
     * @return submenu_order - 子菜单排序
     */
    public Integer getSubmenuOrder() {
        return submenuOrder;
    }

    /**
     * 设置子菜单排序
     *
     * @param submenuOrder 子菜单排序
     */
    public void setSubmenuOrder(Integer submenuOrder) {
        this.submenuOrder = submenuOrder;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}