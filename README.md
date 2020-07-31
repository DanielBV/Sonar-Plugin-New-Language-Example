# Example of a Sonar Plugin to add a New language

This repository contains a template to create a plugin of Sonarqube that adds a new language. **The plugin is functional**, it just checks files with the extension .ml (my language) and it raises an exception if the file is empty.

## Glossary
List of relevant terms used across the plugin:
* **Rule**: A standard or condition that the code should follow. If it doesn't the plugin raises an **issue**. For example  ["=+" should not be used instead of "+="](https://rules.sonarsource.com/java/RSPEC-2757).
* **Check**: Synonym for key. Classes that represent rules often end with the suffix "Check".
* **Key**: Unique identifier for something. Several things require a unique id, like rules, repositories, quality profiles, and languages.
* **Rule repository**: A set of rules. Plugins can create multiple sets, but this one uses only one.
* **Quality Profile**: A set of **active** rules. The user can creaate multiple quality profiles to configure which rules should be active in a specific project. Every language must have a default quality profile.
* **Squid**: [Apparently squid means SonarQube Unique ID](https://stackoverflow.com/questions/50842908/what-does-the-squid-prefix-mean-in-sonarlint-rules). The name "MyLanguageSquidSensor" comes from [JavaSquidSensor.java in the plugin SonarJava](https://github.com/SonarSource/sonar-java/blob/master/sonar-java-plugin/src/main/java/org/sonar/plugins/java/JavaSquidSensor.java)
* **Sensor**: The entry point of the plugin.
 
## Code Structure
* **org.sonar.mylanguage**: Includes the two classes that act as the entry point of the plugin: `MyLanguagePlugin` and `MyLanguageSquidSensor`.
* **plugin**: Contains the classes associated with the plugin.
* **rules**: Contains (you guessed it) the classes that implements the rules.

The classes `MyLanguagePlugin`, `MyLanguage`, `MyLanguageQualityProfile` and `MyLanguageRuleRepository` declare what the plugins do. Meanwhile `MyLanguageSquidSensor` contains some configuration and also is the entry point of the plugin. You can implement the method `execute` as you wish. I extracted the necessary configuration from the context and delegated the analysis to the class `MyLanguageScanner`.

## How to use it
The plugin is already functional. To create your own plugin from it you should change several things. To begin with, replace every "MyLanguage" with the name of the language in the name and contents of each file.

Also you probably want to add a parser to the plugin. You can use any parser you want (SSLR, ANTLR, Javacc, etc). The parser should be called from the method `scanFile` of the file `MyLanguageScanner.java`.


### Adding new rules
Every rule takes the same data object (a  `MyLanguageRuleContext`). So you should store all the information the rules need in that object.

Then create a class that implements `MyLanguageCheck`. The method `scan` should check if the rule is broken, and if it is, it should generate an issue and sotore it in the `MyLanguageRuleContext`.

Once the class is implemented, it must be added to the Rule Repository and activated in the quality profile.

#### Addding a rule to the Rule Repository
There are three ways of adding a rule to the Rule Repository:
1. Manually, using methods
2. Loading them from a XML file
3. Using annotations

This template uses the last one. I recommend using this one because it keeps the metadata close to the data, and it's easier to implement. For the record, to add the rule manually you can do something like:

```
repository.createRule(LowerCaseCheck.CHECK_KEY).setName("LowerCase").setMarkdownDescription("Every symbol should be in lower case");
```

To use annotations, you need to add the `@Rule` decorator. Like the class `EmptyFileCheck`. Afterwards, add the rule to the method `getChecks` inside `MyLanguageRuleRepository`. Like this:

```
set.add(MyNewRule.class);
```

#### Adding the rule to the Quality Profile
When you add the rule to the repository, it becomes available for use. But to use it by default you must add it to the default quality profile.

It's easy. Just add the line:

```
 profile.activateRule(MyLanguageRuleRepository.REPOSITORY_KEY, MyNewRule.CHECK_KEY);
```

Done, reinstall the plugin and the rule should be work.




### Adding parameters
#### Analysis parameters
[Analysis parameters](https://docs.sonarqube.org/latest/analysis/analysis-parameters/) allow you to configure how the analysis is done. They can be used to do things like pointing to an auxiliary server or adding the option to create more precise logs.

The parameters of an analysis are stored in the class `SensorContext`. You can extract the parameters in the method `execute` of `MyLanguageScanner`. I recommend getting the parameters there and using them to configure the Scanner. You could also extract them inside the Scanner, but it's less organized.

An example would be:

```
String auxiliaryService = sensorContext.config().get(AUXILIARY_SERVICE_PARAMETER).orElse("").trim();
```

#### Rule parameters

Rule parameters allow you to change the conditions that trigger a rule. For example, if you have a rule to detect magic numbers, you can add a parameter to establish how many times a number must appear to be considered a magic number.

To add a rule parameter, you have to declare it inside the class that represents the rule. For example:
```
 @RuleProperty(
    key = "minimum-repetitions",
    defaultValue = DEFAULT_REPETITIONS,
    description = "Minimum times a number must appear to be considered a magic number. Must be greater than 0.")
public String repetitions = DEFAULT_REPETITIONS ;
```

Take into account that:
* You must set the default value manually.
* The key must be unique
* The default quality profile will use the default value of the parameter. To change the parameter you must create another quality profile.

I'm not sure if defining rule parameters this way require to load the rules using annotations. But I haven't seen any other way of adding parameters.




## Installing the plugin
Package the plugin with:
```
mvn package
```
This will generate a file `mylanguage-example-plugin-1.0-SNAPSHOT.jar` inside the `target/` folder. Copy this file to the folder `extensions/plugins/` of the sonarqube instance. Then restart the instance.

The folder `exampleAnalysis` contains two files to test if the plugin is working properly. Note that if you change things (the extension of the language, the rules, etc) it might not raise the expected exceptions.

It's recommended to run a scan with the test files before modifying the template, so that you know it's working. To run the scan  execute this command inside the folder `exampleAnalysis`

```
sonar-scanner -Dsonar.login=your-token
```

(The `sonar-project.properties` file inside the folder `exampleAnalysis` assumes the SonarQube instance is in http://localhost:9000, if it isn't you should change it)

The results of the analysis should contain a code smell in the file `empty.ml`.

## Troubleshooting
### The plugin isn't detecting the files
The plugin only analyzes files that have the language extension. This extension can be found in the file attribute `DEFAULT_FILE_SUFFIXES` of the file `MyLanguage.java`. 

Remember to include the dot, write `.py` instead of `py`.