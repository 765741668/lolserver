package com.lol.tool;


import com.lol.buffer.GameUpBuffer;
import com.lol.core.Connection;
import com.lol.dto.SelectModel;

import java.util.List;
import java.util.function.Consumer;

public class EventUtil {

    /**
     * 初始化选人房间事件
     *
     * @param teamOne
     * @param teamTwo
     */
    public static InitSelectRoom<List> initSelectRoom;

    /**
     * 初始化选人房间事件
     *
     * @param teamOne
     * @param teamTwo
     */
    public static InitFightRoom<SelectModel> initFightRoom;

    /**
     * 创建选人模块事件
     *
     * @param teamOne
     * @param teamTwo
     */
    public static CreateSelect<List, GameUpBuffer> createSelect;

    /**
     * 移除选人模块事件  选人房间关闭
     *
     * @param roomId
     */
    public static Consumer<Integer> destorySelect;
    /**
     * 创建战斗模块事件
     *
     * @param teamOne
     * @param teamTwo
     */
    public static CreateFight<SelectModel, GameUpBuffer> createFight;
    /**
     * 战斗结束事件
     *
     * @param roomId
     */
    public static Consumer<Integer> destoryFight;

    /**
     * 客户端断开链接
     */
    public static Consumer<Connection> disconnect;

    @FunctionalInterface
    public interface InitSelectRoom<List> {
        void init(List teamOne, List teamTwo);
    }

    @FunctionalInterface
    public interface InitFightRoom<SelectModel> {
        void init(SelectModel[] teamOne, SelectModel[] teamTwo);
    }

    @FunctionalInterface
    public interface CreateSelect<List, GameUpBuffer> {
        void create(List teamOne, List teamTwo, GameUpBuffer buffer);
    }

    @FunctionalInterface
    public interface CreateFight<SelectModel, GameUpBuffer> {
        void create(SelectModel[] teamOne, SelectModel[] teamTwo, GameUpBuffer buffer);
    }
}