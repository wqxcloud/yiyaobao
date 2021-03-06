package com.yiyaobao.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.yiyaobao.entity.Medicine;

import com.yiyaobao.dao.MedicineDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig medicineDaoConfig;

    private final MedicineDao medicineDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        medicineDaoConfig = daoConfigMap.get(MedicineDao.class).clone();
        medicineDaoConfig.initIdentityScope(type);

        medicineDao = new MedicineDao(medicineDaoConfig, this);

        registerDao(Medicine.class, medicineDao);
    }
    
    public void clear() {
        medicineDaoConfig.clearIdentityScope();
    }

    public MedicineDao getMedicineDao() {
        return medicineDao;
    }

}
