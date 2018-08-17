package com.dbscarlet.common.dataResource

/**
 * Created by Daibing Wang on 2018/7/16.
 */
enum class State {
    /**
     * 加载中
     */
    LOADING,
    /**
     * 获取数据成功
     */
    SUCCESS,
    /**
     * 未获取到预期数据（比如密码错误）
     */
    FAILED,
    /**
     * 获取数据异常，（比如网络超时、解析报错）
     */
    EXCEPTION,
    /**
     * 已取消
     */
    Cancel
}