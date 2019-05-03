package com.yiyaobao.entity;
import com.yiyaobao.cn.CN;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Medicine implements CN {

    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String name;//药品名称
    private String number; //国药准字/批准文号
    private String changshang;//药品厂商
    private String guiGe;//药品规格
    private String type;//药品类型
    private String jiXing;//药品剂型
    private float inPrice;//药品进价
    private float outPrice;//药品卖价
    private int fuHe;//搜索时，关键字匹配 1:名称符合  2:厂商符合  3:名称和厂商都符合
    @Generated(hash = 358522908)
    public Medicine(Long id, @NotNull String name, String number, String changshang,
            String guiGe, String type, String jiXing, float inPrice, float outPrice,
            int fuHe) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.changshang = changshang;
        this.guiGe = guiGe;
        this.type = type;
        this.jiXing = jiXing;
        this.inPrice = inPrice;
        this.outPrice = outPrice;
        this.fuHe = fuHe;
    }
    @Generated(hash = 1065091254)
    public Medicine() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNumber() {
        return this.number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getChangshang() {
        return this.changshang;
    }
    public void setChangshang(String changshang) {
        this.changshang = changshang;
    }
    public String getGuiGe() {
        return this.guiGe;
    }
    public void setGuiGe(String guiGe) {
        this.guiGe = guiGe;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getJiXing() {
        return this.jiXing;
    }
    public void setJiXing(String jiXing) {
        this.jiXing = jiXing;
    }
    public float getInPrice() {
        return this.inPrice;
    }
    public void setInPrice(float inPrice) {
        this.inPrice = inPrice;
    }
    public float getOutPrice() {
        return this.outPrice;
    }
    public void setOutPrice(float outPrice) {
        this.outPrice = outPrice;
    }
    public int getFuHe() {
        return this.fuHe;
    }
    public void setFuHe(int fuHe) {
        this.fuHe = fuHe;
    }

    @Override
    public String chinese() {
        return name + "" + changshang;
    }
}
