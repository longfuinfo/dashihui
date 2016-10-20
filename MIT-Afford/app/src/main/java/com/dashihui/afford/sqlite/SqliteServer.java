package com.dashihui.afford.sqlite;

import android.content.Context;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.thirdapi.greedsqlite.DaoSession;
import com.dashihui.afford.thirdapi.greedsqlite.Server;
import com.dashihui.afford.thirdapi.greedsqlite.ServerDao;
import com.dashihui.afford.util.list.UtilList;
import com.lidroid.xutils.util.LogUtils;

import java.util.List;

import de.greenrobot.dao.query.WhereCondition;

/**
 * Created by NiuFC on 2015/11/10.
 */
public class SqliteServer {

    private static SqliteServer instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private ServerDao mServerDao;

    public static SqliteServer getInstance(Context context) {
        if (instance == null) {
            instance = new SqliteServer();
            if (appContext == null){
                appContext = context.getApplicationContext();
            }
            instance.mDaoSession = AffordApp.getDaoSession(context);
            instance.mServerDao = instance.mDaoSession.getServerDao();
        }
        return instance;
    }

    /**
     * TODO 复杂查询
     * @Title: queryBuilder
     * @param ag0
     * @return   设定文件
     * @return List<Server>    返回类型
     * @author 牛丰产
     * @date 2014年10月7日 下午3:13:08
     * @维护人:
     * @version V1.0
     */
    public List<Server> queryServer(WhereCondition ag0){
        return mServerDao.queryBuilder().where(ag0).list();
    }

    /**
     * 根据两种条件查询List<Server>
     * @param ag0
     * @param ag1
     * @return
     */
    public List<Server> queryServer(WhereCondition ag0,WhereCondition ag1){
        return mServerDao.queryBuilder().where(ag0).where(ag1).list();
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
        List<Server> listServer = getListServerByUid(shopid);
        Boolean isBoolean = false;
        if (listServer!=null && listServer.size() > 0){
            isBoolean = true;
        }
        return isBoolean;
    }



    /**
     * 根据商品ID获取商品
     * TODO(这里用一句话描述这个方法的作用)
     * @Title: getListServerByUid
     * @param spid 商品ID
     * @return   设定文件
     * @return List<Server>    返回类型
     * @author 牛丰产
     * @date 2014年10月15日 下午8:40:59
     * @维护人:
     * @version V1.0
     */
    public List<Server> getListServerByUid(String spid) {
        List<Server> listServer = queryServer(ServerDao.Properties.ID.eq(spid));
        return listServer;
    }

    /**
     * 根据店铺ID获取
     * @param shopid
     * @return
     */
    public List<Server> getListServerByShopID(String shopid) {
        List<Server> listServer = queryServer(ServerDao.Properties.ShopID.eq(shopid));
        return listServer;
    }
    /**
     * 根据店铺ID获取 <Server>
     * @param shopid
     * @param ischoose
     * @return
     */
    public List<Server> getListServerByShopID(String shopid,boolean ischoose ) {
        List<Server> listServer = queryServer(ServerDao.Properties.ShopID.eq(shopid),ServerDao.Properties.Ischoose.eq(ischoose));
        return listServer;
    }

    /**
     *
     * @param id
     * @return
     */
    public Server getServerByUid(String id) {
        List<Server> shopCartList = queryServer(ServerDao.Properties.ID.eq(id));
        LogUtils.e("购物车============>"+shopCartList);
        Server Server;
        if (!UtilList.isEmpty(shopCartList)){
            LogUtils.e("购物车中同类物品要唯一，是否多条？============>"+shopCartList.size());
            Server= shopCartList.get(0);
        }else {
            Server = null;
        }
        return Server;
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
        List<Server> listServer = getListServerByUid(shoprid);
        Boolean isBoolean = false;
        if (listServer!=null && listServer.size() > 0){
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
        List<Server> listServer = getListServerByUid(shopid);
        long primarykey = -1;
        if (listServer!=null && listServer.size() > 0){
            primarykey = listServer.get(0).getId();
        }
        return primarykey;
    }

    public Server loadServer(long id) {
        return mServerDao.load(id);
    }

    public List<Server> loadAllServer(){
        return mServerDao.loadAll();
    }

    public List<Server> queryServer(String where, String... params){
        return mServerDao.queryRaw(where, params);
    }
    /**
     * 保存修改
     * TODO(这里用一句话描述这个方法的作用)
     * @Title: saveServer
     * @param Server
     * @return   设定文件
     * @return long    返回类型
     * @author 牛丰产
     * @date 2014年11月17日 上午10:42:21
     * @维护人:
     * @version V1.0
     */
    public long saveServer(Server Server){
        return mServerDao.insertOrReplace(Server);
    }

    /**
     * 有则修改，没有保存
     * TODO(这里用一句话描述这个方法的作用)
     * @Title: update
     * @param shopid  商品ID
     * @param Server   设定文件
     * @return void    返回类型
     * @author 牛丰产
     * @date 2014年11月17日 下午4:00:41
     * @维护人:
     * @version V1.0
     */
    public void update(String shopid,Server Server){
        if (getInforPrimarykey(shopid)>0) {//如果已经保存商品id,删除后保存
            deleteServer(getInforPrimarykey(shopid));
            saveServer(Server);
        }else {// 没有直接保存
            saveServer(Server);
        }

    }

    public void deleteServer(){
        mServerDao.deleteAll();
    }

    public void deleteServer(long id){
        mServerDao.deleteByKey(id);
    }

    public void deleteServer(Server Server){
        mServerDao.delete(Server);
    }
}
