I2B Properties-util
===================

This utility provided to the developer community by [I2B technologies](http://www.i2btech.com/) allows to inject parameters into class fields, retrieved from a property file or a jdbc connection.  

Our primary goal in this project is ease of use, so you can start using it right away with almost no instructions, that's why we're using only one annotation `@Properties`.

# Use Cases
For following examples we are using a file named test.properties located in same package as classes referenced here:

> `test.properties`
```
firstname=John
lastname=Doe
email=john.doe@site.com
correo=unknown.person@example.com
```

## From File

In two simple steps you might start using the `@Properties` annotation to retrieve values from .properties file

### First Step
Annotate your class indicating the relative path of the .properties file and annotate fields to 

``` java
import cl.i2b.properties.annotations.Properties;

@Properties("./test.properties") // .properties file located in same class package
public class User {

    private String firstname; // no value to be injected
    
    @Properties
    private String lastname; // will inject value of lastname key from test.properties
    
    @Properties("correo")
    private String email;  // will inject value of correo key from test.properties
    
    ... // getters and setters
}
```

### Second Step: Inject Values
``` java
import cl.i2b.properties.container.Container;

public class Client {
    public static void main(String args[]) {
        User u = new User();
        
        Container.injectProperties(u);
        System.out.println("firstname: " + u.getfirstName());
        System.out.println("lastname: " + u.getLastname());
        System.out.println("email: " + u.getEmail());
    }
}
```
Result from execution of this java class would be:
``` console
firstname: null
lastname: Doe
email: unknown.person@example.com
```


## From Database
For this example we will be using a MySQL database called `mydb`, that contains a table called `parameters` and with three rows as shown in the following sql script:
``` sql
create database mydb;
use mydb;
create table parameters (pkey varchar(40) primary key, pvalue varchar(120) not null);
insert into parameters (pkey, pvalue) values ('firstname', 'Mary'), ('lastname', 'Max'), ('email', 'mary.max@site.com');

```

Then you can retrieve the keys to be injected in the class fields from a jdbc connection.  Be sure that driver class specified next is available in the classpath:

``` java
import cl.i2b.properties.annotations.Properties;

@Properties(jdbc="jdbc:mysql://localhost:3306/mydb?user=i2b&password=i2b", // jdbc connection url
            driverClassName="com.mysql.jdbc.Driver", // jdbc driver
            query="select p.pkey, p.pvalue from parameters p")  // sql query that retrieves key/value pairs, this query must return key first and value later
public class User {

    private String firstname; // no value to be injected
    
    @Properties
    private String lastname; // will inject value of lastname key from test.properties
    
    @Properties
    private String email;  // will inject value of correo key from test.properties
    
    ... // getters and setters
}
```

## Multiple Databases
For this example in addition to mydb database, we are using a h2 database called `h2altdb`, created with the following script:
``` sql
create table if not exists dynamicparameters (paramKey varchar(50) primary key, paramValue varchar(120) not null);

insert into dynamicparameters (paramKey, paramValue) values ('language', 'java');
insert into dynamicparameters (paramKey, paramValue) values ('os', 'linux');
```


So you can inject values from multiple databases like this:
``` java
public class SimplePojo {
   
   @Properties(driverClassName="org.h2.Driver",
               jdbc="jdbc:h2:~/h2altdb;IFEXISTS=TRUE;USER=i2b;PASSWORD=i2b",
               query="select dp.paramKey, dp.paramValue from dynamicparameters dp")
   private String language; // injects language value from dynamicparameters table

   @Properties(driverClassName="org.h2.Driver",
           jdbc="jdbc:h2:~/h2altdb;IFEXISTS=TRUE;USER=i2b;PASSWORD=i2b",
           query="select dp.paramKey, dp.paramValue from dynamicparameters dp",
           key="os")
   private String operatingSystem; // injects value os from dynamicparameters table
   
   @Properties(jdbc="jdbc:mysql://localhost:3306/mydb?user=i2b&password=i2b", 
            driverClassName="com.mysql.jdbc.Driver",
            query="select p.pkey, p.pvalue from parameters p")
   private String firstname; // injects firstname from parameters in mydb
   
   private String email;
   
   // ... getters and setters
}
```
Annotating the class will define a default key/value pairs source:

``` java
@Properties(driverClassName="org.h2.Driver",
            jdbc="jdbc:h2:~/h2altdb;IFEXISTS=TRUE;USER=i2b;PASSWORD=i2b",
            query="select dp.paramKey, dp.paramValue from dynamicparameters dp")
public class SimplePojo {
   
   @Properties
   private String language; // injects language value from dynamicparameters table in h2altdb

   @Properties(key="os") 
   private String operatingSystem; // injects value os from dynamicparameters table in h2altdb
   
   @Properties(path="./test.properties")
   private String firstname; // injects firstname from test.properties file
   
   @Properties(jdbc="jdbc:mysql://localhost:3306/mydb?user=i2b&password=i2b", 
            driverClassName="com.mysql.jdbc.Driver",
            query="select p.pkey, p.pvalue from parameters p")
   private String email; // injects email from table parameters in mydb
   
   // ... getters and setters
}
```
An implementation would be:
``` java
public class TestPropertiesUtil {
    public void static void main(String args[]) {
        SimplePojo sp = new SimplePojo();
        Container.injectProperties(sp);
        System.out.println("language: " + sp.getLanguage());
        System.out.println("operatingSystem: " + sp.getOperatingSystem());
        System.out.println("firstname: " + sp.getFirstname());
        System.out.println("email: " + sp.getEmail());
    }
}
```
The console would display this:
``` console
language: java
operatingSystem: linux
firstname: John
email: mary.max@site.com

```
# Installation
- Simply download the jar and add it to your classpath
- If you need the jdbc implementation be sure to have the required drivers in your classpath as well

# License

MIT

**Free for anything you need**

# Todo's:
- Implement as Java EE service
- Allow to extend (eg. XML source, HTTP source, etc.)
- Optimize performance by allowing to inject values on compile time
- Include as maven dependecy
- Be able to specify bundle resources like packages or properties file
- Use jdbc connections pool
- Add jndi data source connections
- Create release version

# Contributors
- Robinson Mesino
- Aarón Santiago
- Edwin Vargas

# Contact
Feel free to drop us an email at [community@i2btech.com](mailto:community@i2btech.com) with comments and suggestions