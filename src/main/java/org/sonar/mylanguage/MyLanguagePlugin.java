
package org.sonar.mylanguage;

import org.sonar.api.Plugin;


import org.sonar.mylanguage.plugin.MyLanguageRuleRepository;
import org.sonar.mylanguage.plugin.MyLanguage;
import org.sonar.mylanguage.plugin.MyLanguageQualityProfile;


public class MyLanguagePlugin implements Plugin {


    @Override
    public void define(Context context) {

        context.addExtensions(MyLanguage.class, MyLanguageQualityProfile.class);
        context.addExtensions( MyLanguageSquidSensor.class, MyLanguageRuleRepository.class);

    }

}
