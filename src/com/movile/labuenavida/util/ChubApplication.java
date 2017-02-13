/**
 * Movile connect > everyone
 */
package com.movile.labuenavida.util;

import java.util.ArrayList;
import java.util.List;

import com.movile.chub.commons.to.CategoryTO;

/**
 * Application from cHub
 * @author Carlos Rodriguez
 */
public class ChubApplication {

    /**
     * Application identifier from cHub
     */
    public static final long ID = 736;

    /**
     * Categories linked to application
     */
    private List<CategoryTO> categoryList;

    /**
     * @return the categoryList
     */
    public List<CategoryTO> getCategoryList() {
        return categoryList;
    }

    /**
     * @param categoryList the categoryList to set
     */
    public void setCategoryList(List<CategoryTO> categoryList) {
        this.categoryList = categoryList;
    }

    /**
     * Add a new category to list
     * @param category {@link CategoryTO} to be added
     */
    public void addCategoryToList(CategoryTO category) {
        if (this.categoryList == null) {
            categoryList = new ArrayList<CategoryTO>();
        }
        this.categoryList.add(category);
    }

}
