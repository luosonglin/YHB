package com.medmeeting.m.zhiyi.Data.API;


import com.medmeeting.m.zhiyi.UI.Entity.BannerDto;
import com.medmeeting.m.zhiyi.UI.Entity.BlogDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveTagDto;
import com.medmeeting.m.zhiyi.UI.Entity.MeetingDto;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.LifeCache;
import io.rx_cache.Reply;
import rx.Observable;

/**
 * 缓存API接口
 * @LifeCache设置缓存过期时间. 如果没有设置@LifeCache , 数据将被永久缓存理除非你使用了 EvictProvider, EvictDynamicKey or EvictDynamicKeyGroup .
 * EvictProvider可以明确地清理清理所有缓存数据.
 * EvictDynamicKey可以明确地清理指定的数据 DynamicKey.
 * EvictDynamicKeyGroup 允许明确地清理一组特定的数据. DynamicKeyGroup.
 * DynamicKey驱逐与一个特定的键使用EvictDynamicKey相关的数据。比如分页，排序或筛选要求
 * DynamicKeyGroup。驱逐一组与key关联的数据，使用EvictDynamicKeyGroup。比如分页，排序或筛选要求
 */
public interface CacheProviders {

//    // 缓存时间 1天
//    @LifeCache(duration = 7, timeUnit = TimeUnit.DAYS)
//    Observable<Reply<List<VideoListDto>>> getVideoList(Observable<List<VideoListDto>> oRepos, DynamicKey userName, EvictDynamicKey evictDynamicKey);
//
//    // 缓存时间 永久
//    Observable<Reply<List<BookListDto>>> getBookList(Observable<List<BookListDto>> oRepos, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    // 缓存时间
    @LifeCache(duration = 1, timeUnit = TimeUnit.DAYS)
    Observable<Reply<List<BannerDto>>> getBannerList(Observable<List<BannerDto>> oRepos, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 1, timeUnit = TimeUnit.DAYS)
    Observable<Reply<MeetingDto>> getMeetingList(Observable<MeetingDto> oRepos, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 1, timeUnit = TimeUnit.DAYS)
    Observable<Reply<List<BlogDto>>> getRecommendNews(Observable<List<BlogDto>> oRepos, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    Observable<Reply<LiveTagDto>> getLiveTagList(Observable<LiveTagDto> oRepos, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 1, timeUnit = TimeUnit.DAYS)
    Observable<Reply<LiveDto>> getAllLiveList(Observable<LiveDto> oRepos, DynamicKey userName, EvictDynamicKey evictDynamicKey);

}
