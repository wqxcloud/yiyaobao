package com.yiyaobao;

import android.content.Context;
import android.util.Log;

import com.yiyaobao.dao.MedicineDao;
import com.yiyaobao.entity.Medicine;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by XU on 2018/5/3.
 */

public class DaoUtils {
    private static final String TAG = DaoUtils.class.getSimpleName();
    private DaoManager mManager;

    public DaoUtils(Context context){
        mManager = DaoManager.getInstance();
        mManager.init(context);
        mManager.setDebug();
    }

    /**
     * 完成Medicine记录的插入，如果表未创建，先创建Medicine表
     * @param Medicine
     * @return
     */
    public boolean insertMedicine(Medicine Medicine){
        boolean flag = false;
        flag = mManager.getDaoSession().getMedicineDao().insert(Medicine) == -1 ? false : true;
        Log.i(TAG, "insert Medicine :" + flag + "-->" + Medicine.toString());
        return flag;
    }
    public boolean insertOrReplace(Medicine Medicine){
        boolean flag = false;
        flag = mManager.getDaoSession().getMedicineDao().insertOrReplace(Medicine) == -1 ? false : true;
        Log.i(TAG, "insert Medicine :" + flag + "-->" + Medicine.toString());
        return flag;
    }
    /**
     * 插入多条数据，在子线程操作
     * @param MedicineList
     * @return
     */
    public boolean insertMultMedicine(final List<Medicine> MedicineList) {
        boolean flag = false;
        try {
            mManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (Medicine Medicine : MedicineList) {
                        mManager.getDaoSession().insertOrReplace(Medicine);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 修改一条数据
     * @param Medicine
     * @return
     */
    public boolean updateMedicine(Medicine Medicine){
        Log.i(TAG, "updateMedicine: Medicine.getId="+Medicine.getId());
        Log.i(TAG, "updateMedicine: Medicine.getName="+Medicine.getName());
        boolean flag = false;
        try {
            mManager.getDaoSession().update(Medicine);
            flag = true;
        }catch (Exception e){
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除单条记录
     * @param Medicine
     * @return
     */
    public boolean deleteMedicine(Medicine Medicine){
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().delete(Medicine);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除所有记录
     * @return
     */
    public boolean deleteAll(){
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().deleteAll(Medicine.class);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询所有记录
     * @return
     */
    public List<Medicine> queryAllMedicine(){
        Log.i(TAG, "queryAllMedicine: ");
        //mManager.getDaoSession().clear();
        return mManager.getDaoSession().loadAll(Medicine.class);
    }

    /**
     * 根据主键id查询记录
     * @param key
     * @return
     */
    public Medicine queryMedicineById(long key){
        Log.i(TAG, "queryMedicineById: ");
        return mManager.getDaoSession().load(Medicine.class, key);
    }

    /**
     * 使用native sql进行查询操作
     */
    public List<Medicine> queryMedicineByNativeSql(String sql, String[] conditions){
        return mManager.getDaoSession().queryRaw(Medicine.class, sql, conditions);
    }

    /**
     * 使用queryBuilder进行查询
     * @return
     */
    public List<Medicine> queryMedicineByQueryBuilder(long id){
        QueryBuilder<Medicine> queryBuilder = mManager.getDaoSession().queryBuilder(Medicine.class);
        return queryBuilder.where(MedicineDao.Properties.Id.eq(id)).list();
    }
}
