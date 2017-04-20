/******************************************************************************
 *
 * Module Name:  util - PropertiesUtils.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: Jun 3, 2016
 * Last Updated By: java
 * Last Updated Date: Jun 3, 2016
 * Description:
 *
 *******************************************************************************

 COPYRIGHT  STATEMENT

 Copyright(c) 2011
 by The Hong Kong Jockey Club

 All rights reserved. Copying, compilation, modification, distribution
 or any other use whatsoever of this material is strictly prohibited
 except in accordance with a Software License Agreement with
 The Hong Kong Jockey Club.

 ******************************************************************************/
package com.lol.util;

import org.springframework.core.env.ConfigurablePropertyResolver;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySourcesPropertyResolver;

import java.io.IOException;
import java.util.Properties;

public class PropertiesExUtil {

    public static String getProperties(String filePath, String prefix) {
        MutablePropertySources propertySources = new MutablePropertySources();
        ConfigurablePropertyResolver propertyResolver = new PropertySourcesPropertyResolver(propertySources);
        Properties properties = new Properties();
        try {
            properties.load(PropertiesExUtil.class.getResourceAsStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        propertySources.addFirst(new PropertiesPropertySource("testProperties", properties));

        return propertyResolver.getProperty(prefix);
    }

}

