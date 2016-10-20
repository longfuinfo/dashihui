package com.dashihui.afford.sqlite;

import android.content.Context;

import com.dashihui.afford.AffordApp;
import com.dashihui.afford.thirdapi.greedsqlite.DaoSession;
import com.dashihui.afford.thirdapi.greedsqlite.Search;
import com.dashihui.afford.thirdapi.greedsqlite.SearchDao;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/** 
 * 应用搜索功能数据库服务类
* @ClassName: DbServiceSearch 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author niufc
* @date 2014-11-26 下午4:25:57 
* @维护人: 
* @version V1.0   
*/ 
public class SqliteServiceSearch {
	private static SqliteServiceSearch instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	private SearchDao mSearchDao;

	public static SqliteServiceSearch getInstance(Context context) {
		if (instance == null) {
			instance = new SqliteServiceSearch();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = AffordApp.getDaoSession(context);
			instance.mSearchDao = instance.mDaoSession.getSearchDao();
		}
		return instance;
	}

	/**
	 * 添加一条搜索记录
	* @Title: addOne 
	* @param @param query    搜索文字 
	* @return void    返回类型 
	* @author niufc
	* @date 2014-11-26 下午4:51:42 
	* @维护人: 
	* @version V1.0
	 */
	public void addOne(String query) {
		QueryBuilder<Search> qb = mSearchDao.queryBuilder();
		Search search = new Search();
		if (query != null && !query.equals("")) {
			qb.where(SearchDao.Properties.Query.eq(query));
			if (qb.list().size() > 0) {
				search = qb.list().get(0);
				search.setDate(System.currentTimeMillis());
				//如果已有同query字段的记录，则更新时间
				mSearchDao.insertOrReplace(search);
			}else{
				search.setQuery(query);
				search.setDate(System.currentTimeMillis());
				mSearchDao.insert(search);
			}
		}
//		Cursor c = mContext.getContentResolver().query(Search.URI, null, null, null, null);
//		if (c.getCount() > 10) {
//			//当记录的条数大于10的时候，删除最早的一条
//			String sql = "delete from search where date = (select min(date) from search);";
//			UmDatabaseHelper.getInstance(mContext).getWritableDatabase().execSQL(sql);
//			Log.d(TAG, " 删除了最早的搜索记录");
//		}
//		c.close();
	}
	
	/** 
	 * 根据传入的query文字查询匹配的历史搜索记录列表
	* @Title: getSearchList 
	* @param @param query	如果为""，则查询所有记录
	* @param @return    设定文件 
	* @return List<Search>    返回类型 
	* @author niufc
	* @date 2014-11-26 下午4:55:16 
	* @维护人: 
	* @version V1.0  
	*/ 
	public List<Search> getSearchList(String query) {
		QueryBuilder<Search> qb = mSearchDao.queryBuilder();
		if (query.equals("")) {
			return qb.orderDesc(SearchDao.Properties.Date).list();
		} else {
			return qb.where(SearchDao.Properties.Query.like( "%" + query + "%" )).list();
		}
	}
	
	/** 
	 * 删除所有历史搜索记录
	* @Title: deleteAll 
	* @param     
	* @return void    返回类型 
	* @author niufc
	* @date 2014-11-26 下午4:56:59 
	* @维护人: 
	* @version V1.0  
	*/ 
	public void deleteAll(){
		mSearchDao.deleteAll();
	}
	
	/** 
	 * 删除指定记录
	* @Title: deleteOne 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param search    设定文件 
	* @return void    返回类型 
	* @author niufc
	* @date 2014-12-2 下午2:18:33 
	* @维护人: 
	* @version V1.0  
	*/ 
	public void deleteOne(Search search){
		mSearchDao.delete(search);
	}
}
