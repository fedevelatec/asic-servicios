package com.fedevela.asic.util;

/**
 * Created by fvelazquez on 9/04/14.
 */
import java.util.Properties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class PropertyPlaceholderConfigurer extends org.springframework.beans.factory.config.PropertyPlaceholderConfigurer {

    /**
     * Unique properties, unified propreties.
     */
    private Properties properties;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties properties) throws BeansException {
        super.processProperties(beanFactoryToProcess, properties);
        this.properties = properties;
    }

    /**
     *
     * @return
     */
    public Properties getProperties() {
        return properties;
    }

    public String getProperty(final String key) {
        return properties.getProperty(key);
    }
}
