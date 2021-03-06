package com.medmeeting.m.zhiyi.Util;

/**
 * Created by Administrator on 2016/11/18 0018.
 */
public interface ConstanceValue {
    String DATA = "data";
    String DATA_SELECTED = "dataSelected";
    String DATA_UNSELECTED = "dataUnselected";

    String ARTICLE_GENRE_GALLERY = "gallery";
    String ARTICLE_GENRE_VIDEO = "video";
    String  ARTICLE_GENRE_ARTICLE = "article";
    String URL = "url";
    String GROUP_ID = "groupId";
    String ITEM_ID = "itemId";
    String SP_THEME = "theme";
    int THEME_LIGHT = 1;
    int THEME_NIGHT = 2;


    /**
     * 修改主题
     */
    int MSG_TYPE_CHANGE_THEME = 100;

    /**
     * 新闻首页标签
     */
    String TITLE_SELECTED = "explore_title_selected";
    String TITLE_UNSELECTED = "explore_title_unselected";

    /**
     * 会议首页标签
     */
    String MEETING_SELECTED = "explore_meeting_selected";
    String MEETING_UNSELECTED = "explore_meeting_unselected";

    /**
     * 历史搜索词条
     */
    String HISTORY_WORD = "history_word";
}
