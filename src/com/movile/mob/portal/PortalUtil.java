package com.movile.mob.portal;

import com.compera.portal.parser.bean.ArrayValue;
import com.compera.portal.parser.bean.Attribute;
import com.compera.portal.parser.bean.Item;

public class PortalUtil {

    private PortalUtil() {
    }

    public static String getAttributeSimpleValue(Item item, String name) throws Exception {

        Attribute attribute = item.getAttribute(name);

        if (attribute == null) {
            throw new Exception(name + " not found");
        }

        return attribute.getSimpleValue();
    }

    public static String[] getAttributeArrayValue(Item item, String name) throws Exception {

        Attribute attribute = item.getAttribute(name);

        if (attribute == null) {
            throw new Exception(name + " not found");
        }

        if (attribute.getValue().isSimple()) {

            return new String[] {getAttributeSimpleValue(item, name)};

        } else if (attribute.getValue().isArray()) {

            ArrayValue arrayValue = (ArrayValue) attribute.getValue();
            String[] result = new String[arrayValue.getElements().size()];
            for (int position = 0; position < arrayValue.getElements().size(); position++) {
                result[position] = arrayValue.getElement(position).getSimpleValue();
            }
            return result;
        }
        return new String[] {};
    }

}
