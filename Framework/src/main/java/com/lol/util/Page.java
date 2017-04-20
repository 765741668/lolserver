/*
 * @(#)ObjectUtil.java        1.0 2009-8-11
 *
 * Copyright (c) 2007-2009 Kuangxf, Co., Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * Kuangxf
 * You shall not disclose such Confidential Information and shall use 
 * it only in accordance with the terms of the license agreement you 
 * entered into with Kuangxf.
 */

package com.lol.util;

/**
 * 分页
 */
public final class Page {

    /**
     * 当前页码
     */
    protected int currPage = 1;
    /**
     * 每页显示的记录数
     */
    protected int pageSize = 20;


    /**
     * 上页记录数（翻页显示上衣页的行数）
     */
    protected int preRow = 0;


    /**
     * 总的记录数
     */
    protected int totalRecord = 0;


    /**
     * 排序的字段
     */
    private String orderField = null;

    /**
     * 排序的方向
     */
    private String orderDirection = null;

    public Page() {
        super();
    }

    /**
     * @param pageSize 每页记录数
     */
    public Page(final int pageSize) {
        setPageSize(pageSize);
    }

    /**
     * @param pageSize  每页记录数
     * @param curPageNo 当前记录数
     */
    public Page(final int curPageNo, final int pageSize) {
        setPageSize(pageSize);
        this.currPage = curPageNo;
    }

    @Override
    public String toString() {
        return "Page{" +
                "currPage=" + currPage +
                ", pageSize=" + pageSize +
                ", preRow=" + preRow +
                ", totalRecord=" + totalRecord +
                ", orderField='" + orderField + '\'' +
                ", orderDirection='" + orderDirection + '\'' +
                '}';
    }

    /**
     * 获得当前页的页号
     */
    public int getCurrPage() {
        return currPage;
    }

    /**
     * 设置当前页的页号,低于1时自动调整为1.
     *
     * @param curPageNo
     */
    public void setCurrPage(final int curPageNo) {
        this.currPage = curPageNo;

        if (curPageNo < 1) {
            this.currPage = 1;
        }
    }

    /**
     * 获得每页的记录数
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页的记录数
     *
     * @param pageSize
     */
    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 根据curPageNo和pageSize（每页显示记录数）计算当前页第一条记录在总结果集中的位置
     */
    public int getStartRowIndex() {
        int startRowIndex = 0;
        if (currPage > 1) {
            startRowIndex = ((currPage - 1) * pageSize) - preRow;
        } else {
            startRowIndex = ((currPage - 1) * pageSize);
        }
        if (startRowIndex < 0) {
            startRowIndex = 0;
        }
        return startRowIndex;
    }


    /**
     * 取得总记录数
     */
    public int getTotalRecord() {
        return totalRecord;
    }

    /**
     * 设置总记录数
     *
     * @param totalCount
     */
    public void setTotalRecord(final int totalCount) {
        this.totalRecord = totalCount;
    }

    /**
     * 根据pageSize与totalCount计算总页
     */
    public int getTotalPages() {
        if (totalRecord < 0)
            return 0;

        int count = totalRecord / pageSize;
        if (totalRecord % pageSize > 0) {
            count++;
        }
        return count;
    }

    /**
     * 是否还有下一页
     */
    public boolean isHasNext() {
        return (currPage + 1 <= getTotalPages());
    }

    public int getNextPage() {
        if (isHasNext())
            return currPage + 1;
        else
            return currPage;
    }

    /**
     * 是否还有上一页
     */
    public boolean isHasPre() {
        return (currPage - 1 >= 1);
    }

    /**
     * 取得上页
     */
    public int getPrePage() {
        if (isHasPre())
            return currPage - 1;
        else
            return currPage;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public String getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }


}
