package com.dashihui.afford.sqlite;

import android.content.Context;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.thirdapi.greedsqlite.BrowseHistory;
import com.dashihui.afford.thirdapi.greedsqlite.BrowseHistoryDao;
import com.dashihui.afford.thirdapi.greedsqlite.DaoSession;
import com.dashihui.afford.util.list.UtilList;
import com.lidroid.xutils.util.LogUtils;

import java.util.List;

import de.greenrobot.dao.query.WhereCondition;

/**
 * 浏览历史
 * Created by NiuFC on 2015/11/10.
 */
public class SqliteBrowseHistory {

    private static SqliteBrowseHistory instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private BrowseHistoryDao mBrowseHistoryDao;

    public static SqliteBrowseHistory getInstance(Context context) {
        if (instance == null) {
            instance = new SqliteBrowseHistory();
            if (appContext == null){
                appContext = context.getApplicationContext();
            }
            instance.mDaoSession = AffordApp.getDaoSession(context);
            instance.mBrowseHistoryDao = instance.mDaoSession.getBrowseHistoryDao();
        }
        return instance;
    }

    /**
     * 按浏览数量倒序返回
     * TODO 复杂查询
     * @Title: queryBuilder
     * @param ag0
     * @return   设定文件
     * @return List<BrowseHistory>    返回类型
     * @author 牛丰产
     * @date 2014年10月7日 下午3:13:08
     * @维护人:
     * @version V1.0
     */
    public List<BrowseHistory> queryBrowseHistory(WhereCondition ag0){
        //按浏览数量倒序返回
        return mBrowseHistoryDao.queryBuilder().where(ag0).orderDesc(BrowseHistoryDao.Properties.Historynum).list();
    }

    /**
     * 根据两种条件查询List<BrowseHistory>
     * @param ag0
     * @param ag1
     * @return
     */
    public List<BrowseHistory> queryBrowseHistory(WhereCondition ag0,WhereCondition ag1){
        return mBrowseHistoryDao.queryBuilder().where(ag0).where(ag1).list();
    }



    /**
     * 购物车中该商品是否已经存在
     *
     * @Title: isExistsShop
     * @param shopid 商品ID
     * @return Boolean    返回类型
     * @author 牛丰产
     * @date 2014年10月15日 下午8:35:18
     * @维护人:
     * @version V1.0
     */
    public Boolean isExistsShop(String shopid) {
        List<BrowseHistory> listBrowseHistory = getListBrowseHistoryByUid(shopid);
        Boolean isBoolean = false;
        if (listBrowseHistory!=null && listBrowseHistory.size() > 0){
            isBoolean = true;
        }
        return isBoolean;
    }



    /**
     * 根据商品ID获取商品
     * TODO(这里用一句话描述这个方法的作用)
     * @Title: getListBrowseHistoryByUid
     * @param spid 商品ID
     * @return   设定文件
     * @return List<BrowseHistory>    返回类型
     * @author 牛丰产
     * @date 2014年10月15日 下午8:40:59
     * @维护人:
     * @version V1.0
     */
    public List<BrowseHistory> getListBrowseHistoryByUid(String spid) {
        List<BrowseHistory> listBrowseHistory = queryBrowseHistory(BrowseHistoryDao.Properties.ID.eq(spid));
        return listBrowseHistory;
    }

    /**
     * 根据店铺ID获取
     * @param shopid
     * @return
     */
    public List<BrowseHistory> getListBrowseHistoryByShopID(String shopid) {
        List<BrowseHistory> listBrowseHistory = queryBrowseHistory(BrowseHistoryDao.Properties.ShopID.eq(shopid));
        return listBrowseHistory;
    }

    /**
     *
     * @param id
     * @return
     */
    public BrowseHistory getBrowseHistoryByUid(String id) {
        List<BrowseHistory> shopCartList = queryBrowseHistory(BrowseHistoryDao.Properties.ID.eq(id));
        LogUtils.e("购物车============>"+shopCartList);
        BrowseHistory BrowseHistory;
        if (!UtilList.isEmpty(shopCartList)){
            LogUtils.e("购物车中同类物品要唯一，是否多条？============>"+shopCartList.size());
            BrowseHistory= shopCartList.get(0);
        }else {
            BrowseHistory = null;
        }
        return BrowseHistory;
    }
    /**
     * 是否保存过同样的商品id
     * TODO(这里用一句话描述这个方法的作用)
     * @Title: isSave
     * @param shoprid
     * @return   设定文件
     * @return Boolean    返回类型
     * @author 牛丰产
     * @date 2014年11月17日 下午3:43:00
     * @维护人:
     * @version V1.0
     */
    public Boolean isSave(String shoprid) {
        List<BrowseHistory> listBrowseHistory = getListBrowseHistoryByUid(shoprid);
        Boolean isBoolean = false;
        if (listBrowseHistory!=null && listBrowseHistory.size() > 0){
            isBoolean = true;
        }
        return isBoolean;
    }
    /**
     * 得到商品详情得主键
     * TODO(这里用一句话描述这个方法的作用)
     * @Title: getInforPrimarykey
     * @param shopid
     * @return   设定文件
     * @return int    返回类型
     * @author 牛丰产
     * @date 2014年11月17日 下午3:46:39
     * @维护人:
     * @version V1.0
     */
    public long getInforPrimarykey(String shopid) {
        List<BrowseHistory> listBrowseHistory = getListBrowseHistoryByUid(shopid);
        long primarykey = -1;
        if (listBrowseHistory!=null && listBrowseHistory.size() > 0){
            primarykey = listBrowseHistory.get(0).getId();
        }
        return primarykey;
    }

    public BrowseHistory loadBrowseHistory(long id) {
        return mBrowseHistoryDao.load(id);
    }

    public List<BrowseHistory> loadAllBrowseHistory(){
        return mBrowseHistoryDao.loadAll();
    }

    public List<BrowseHistory> queryBrowseHistory(String where, String... params){
        return mBrowseHistoryDao.queryRaw(where, params);
    }
    /**
     * 保存修改
     * TODO(这里用一句话描述这个方法的作用)
     * @Title: saveBrowseHistory
     * @param BrowseHistory
     * @return   设定文件
     * @return long    返回类型
     * @author 牛丰产
     * @date 2014年11月17日 上午10:42:21
     * @维护人:
     * @version V1.0
     */
    public long saveBrowseHistory(BrowseHistory BrowseHistory){
        return mBrowseHistoryDao.insertOrReplace(BrowseHistory);
    }

    /**
     * 有则修改，没有保存
     * TODO(这里用一句话描述这个方法的作用)
     * @Title: update
     * @param shopid  商品ID
     * @param BrowseHistory   设定文件
     * @return void    返回类型
     * @author 牛丰产
     * @date 2014年11月17日 下午4:00:41
     * @维护人:
     * @version V1.0
     */
    public void update(String shopid,BrowseHistory BrowseHistory){
        if (getInforPrimarykey(shopid)>0) {//如果已经保存商品id,删除后保存
            deleteBrowseHistory(getInforPrimarykey(shopid));
            saveBrowseHistory(BrowseHistory);
        }else {// 没有直接保存
            saveBrowseHistory(BrowseHistory);
        }

    }

    public void deleteBrowseHistory(){
        mBrowseHistoryDao.deleteAll();
    }

    public void deleteBrowseHistory(long id){
        mBrowseHistoryDao.deleteByKey(id);
    }

    public void deleteBrowseHistory(BrowseHistory BrowseHistory){
        mBrowseHistoryDao.delete(BrowseHistory);
    }
}
