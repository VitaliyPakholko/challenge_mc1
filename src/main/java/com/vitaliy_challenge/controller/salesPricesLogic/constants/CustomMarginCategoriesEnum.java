package com.vitaliy_challenge.controller.salesPricesLogic.constants;

import lombok.Data;
import lombok.Getter;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public enum CustomMarginCategoriesEnum
{
    FIT("FIT", Constants.FIT_MARGIN),
    FOT("FOT", Constants.FOT_MARGIN),
    GIO("GIO", Constants.GIO_MARGIN),
    HDD("HDD", Constants.HDD_MARGIN),
    HOM("HOM", Constants.HOM_MARGIN),
    LET("LET", Constants.LET_MARGIN),
    MON("MON", Constants.MON_MARGIN),
    NET("NET", Constants.NET_MARGIN),
    PCN("PCN", Constants.PCN_MARGIN),
    PIC("PIC", Constants.PIC_MARGIN);


    private static final Map<String, CustomMarginCategoriesEnum> MARGIN_BY_CATEGORY_MAP = new HashMap<>();

    static {
        for (CustomMarginCategoriesEnum e : values()) {
            MARGIN_BY_CATEGORY_MAP.put(e.label, e);
        }
    }

    public final String label;
    public final Double streetPriceMargin;

    private CustomMarginCategoriesEnum(String label, Double streetPriceMargin) {
        this.label = label;
        this.streetPriceMargin = streetPriceMargin;
    }

    public static Double streetMarginByCategory(String label) {
        if(MARGIN_BY_CATEGORY_MAP.containsKey(label))
            return MARGIN_BY_CATEGORY_MAP.get(label).streetPriceMargin;
        return Constants.DEFAULT_STREET_MARGIN;
    }
}
