package com.dashihui.afford.sqlite;

import android.content.Context;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.common.constants.CommConstans;
import com.dashihui.afford.thirdapi.greedsqlite.DaoSession;
import com.dashihui.afford.thirdapi.greedsqlite.ShoppingCart;
import com.dashihui.afford.thirdapi.greedsqlite.ShoppingCartDao;
import com.dashihui.afford.util.list.UtilList;
import com.lidroid.xutils.util.LogUtils;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;

/**
 * Created by NiuFC on 2015/11/10.
 */
public class SqliteShoppingCart {

    private static SqliteShoppingCart instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private ShoppingCartDao mShoppingCartDao;

    public static SqliteShoppingCart getInstance(Context context) {
        if (instance == null) {
            instance = new SqliteShoppingCart();
            if (appContext == null){
                appContext = context.getApplicationContext();
            }
            instance.mDaoSession = AffordApp.getDaoSession(context);
            instance.mShoppingCartDao = instance.mDaoSession.getShoppingCartDao();
        }
        return instance;
    }

    /**
     * TODO 复杂查询
     * @Title: queryBuilder
     * @param ag0
     * @return   设定文件
     * @return List<ShoppingCart>    返回类型
     * @author 牛丰产
     * @date 2014年10月7日 下午3:13:08
     * @维护人:
     * @version V1.0
     */
    public List<ShoppingCart> queryShoppingCart(WhereCondition ag0){
        //倒序
        QueryBuilder qb = mShoppingCartDao.queryBuilder();
        return qb.where(qb.or(ag0,ShoppingCartDao.Properties.ShopID.eq(CommConstans.SELF_TYPE))).orderDesc(ShoppingCartDao.Properties.Id).list();
    }

    /**
     * 根据两种条件查询List<ShoppingCart>
     *     eq 等于
     neq 不等于
     gt 大于
     egt 大于等于
     lt 小于
     elt 小于等于
     like LIKE
     between BETWEEN
     notnull IS NUT NULL
     null IS NULL
     * @param ag0
     * @param ag1
     * @return
     */
    public List<ShoppingCart> queryShoppingCart(WhereCondition ag0,WhereCondition ag1){
//        return mShoppingCartDao.queryBuilder().where(ag0).where(ag1).list();

        QueryBuilder qb = mShoppingCartDao.queryBuilder();
        return qb.where(qb.or(ag0, ShoppingCartDao.Properties.ShopID.eq(CommConstans.SELF_TYPE))).where(ag1).list();
    }
    /**
     * 根据单个条件获取列表
     * @param ag0
     * @return
     */
    public List<ShoppingCart> getListByID(WhereCondition ag0){
        //倒序
        QueryBuilder qb = mShoppingCartDao.queryBuilder();
        return qb.where(ag0).list();
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
        List<ShoppingCart> listShoppingCart = getListShoppingCartByUid(shopid);
        Boolean isBoolean = false;
        if (listShoppingCart!=null && listShoppingCart.size() > 0){
            isBoolean = true;
        }
        return isBoolean;
    }



    /**
     * 根据商品ID获取商品
     * TODO(这里用一句话描述这个方法的作用)
     * @Title: getListShoppingCartByUid
     * @param spid 商品ID
     * @return   设定文件
     * @return List<ShoppingCart>    返回类型
     * @author 牛丰产
     * @date 2014年10月15日 下午8:40:59
     * @维护人:
     * @version V1.0
     */
    public List<ShoppingCart> getListShoppingCartByUid(String spid) {
        List<ShoppingCart> listShoppingCart = getListByID(ShoppingCartDao.Properties.ID.eq(spid));
        return listShoppingCart;
    }

    /**
     * 根据店铺ID购物车商品列表
     * @param shopid
     * @return
     */
    public List<ShoppingCart> getListByShopID(String shopid) {
        List<ShoppingCart> listShoppingCart = queryShoppingCart(ShoppingCartDao.Properties.ShopID.eq(shopid));
        return listShoppingCart;
    }
    /**
     * 根据店铺ID获取 <ShoppingCart>
     * @param shopid
     * @param ischoose
     * @return
     */
    public List<ShoppingCart> getListByShopID(String shopid, boolean ischoose) {
        List<ShoppingCart> listShoppingCart = queryShoppingCart(ShoppingCartDao.Properties.ShopID.eq(shopid),
                ShoppingCartDao.Properties.Ischoose.eq(ischoose));
        return listShoppingCart;
    }

    /**
     *
     * @param id
     * @return
     */
    public ShoppingCart getShoppingCartByUid(String id) {
        List<ShoppingCart> shopCartList = getListByID(ShoppingCartDao.Properties.ID.eq(id));
        ShoppingCart shoppingCart;
        if (!UtilList.isEmpty(shopCartList)){
            LogUtils.e("购物车中同类物品要唯一，是否多条？============>"+shopCartList.size());
            shoppingCart= shopCartList.get(0);
        }else {
            shoppingCart = null;
        }
        return shoppingCart;
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
        List<ShoppingCart> listShoppingCart = getListShoppingCartByUid(shoprid);
        Boolean isBoolean = false;
        if (listShoppingCart!=null && listShoppingCart.size() > 0){
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
        List<ShoppingCart> listShoppingCart = getListShoppingCartByUid(shopid);
        long primarykey = -1;
        if (listShoppingCart!=null && listShoppingCart.size() > 0){
            primarykey = listShoppingCart.get(0).getId();
        }
        return primarykey;
    }

    public ShoppingCart loadShoppingCart(long id) {
        return mShoppingCartDao.load(id);
    }

    public List<ShoppingCart> loadAllShoppingCart(){
        return mShoppingCartDao.loadAll();
    }

    public List<ShoppingCart> queryShoppingCart(String where, String... params){
        return mShoppingCartDao.queryRaw(where, params);
    }
    /**
     * 保存修改
     * TODO(这里用一句话描述这个方法的作用)
     * @Title: saveShoppingCart
     * @param shoppingCart
     * @return   设定文件
     * @return long    返回类型
     * @author 牛丰产
     * @date 2014年11月17日 上午10:42:21
     * @维护人:
     * @version V1.0
     */
    public long saveShoppingCart(ShoppingCart shoppingCart){
        LogUtils.e("addShopCart===addShopCart===66666===ID======>" + shoppingCart.getID());
        return mShoppingCartDao.insertOrReplace(shoppingCart);
    }

    /**
     * 有则修改，没有保存
     * TODO(这里用一句话描述这个方法的作用)
     * @Title: update
     * @param id  商品ID
     * @param shoppingCart   设定文件
     * @return void    返回类型
     * @author 牛丰产
     * @date 2014年11月17日 下午4:00:41
     * @维护人:
     * @version V1.0
     */
    public void update(String id,ShoppingCart shoppingCart){
        if (getInforPrimarykey(id)>0) {//如果已经保存商品id,删除后保存
            deleteShoppingCart(getInforPrimarykey(id));
            saveShoppingCart(shoppingCart);
        }else {// 没有直接保存
            saveShoppingCart(shoppingCart);
        }

    }

    public void deleteShoppingCart(){
        mShoppingCartDao.deleteAll();
    }

    public void deleteShoppingCart(long id){
        mShoppingCartDao.deleteByKey(id);
    }

    public void deleteShoppingCart(ShoppingCart ShoppingCart){
        mShoppingCartDao.delete(ShoppingCart);
    }
}
