@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  web startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and WEB_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args
if "%@eval[2+2]" == "4" goto 4NT_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*
goto execute

:4NT_args
@rem Get arguments from the 4NT Shell from JP Software
set CMD_LINE_ARGS=%$

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\productsync-web-1.0.0.20170118.jar;%APP_HOME%\lib\productsync-facade-impl-1.0.0.20170118.jar;%APP_HOME%\lib\mybatis-spring-boot-starter-1.1.1.jar;%APP_HOME%\lib\productsync-domain-1.0.0.20170118.jar;%APP_HOME%\lib\job-1.0.0.20170118.jar;%APP_HOME%\lib\productsync-facade-interface-1.0.0.SNAPSHOT.jar;%APP_HOME%\lib\mybatis-spring-boot-autoconfigure-1.1.1.jar;%APP_HOME%\lib\productsync-infrastructure-1.0.0.20170118.jar;%APP_HOME%\lib\ordersync-facade-model-1.0.0.SNAPSHOT.jar;%APP_HOME%\lib\mybatis-3.4.0.jar;%APP_HOME%\lib\mybatis-spring-1.3.0.jar;%APP_HOME%\lib\spring-boot-starter-web-1.4.2.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-data-mongodb-1.4.2.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-test-1.4.2.RELEASE.jar;%APP_HOME%\lib\spring-context-support-4.2.7.RELEASE.jar;%APP_HOME%\lib\jtds-1.3.1.jar;%APP_HOME%\lib\jcl-over-slf4j-1.7.21.jar;%APP_HOME%\lib\slf4j-api-1.7.21.jar;%APP_HOME%\lib\dubbo-2.8.4.jar;%APP_HOME%\lib\druid-1.0.18.jar;%APP_HOME%\lib\logback-classic-1.1.7.jar;%APP_HOME%\lib\resteasy-jaxrs-3.0.16.Final.jar;%APP_HOME%\lib\resteasy-client-3.0.16.Final.jar;%APP_HOME%\lib\httpclient-4.5.2.jar;%APP_HOME%\lib\zkclient-0.1.jar;%APP_HOME%\lib\kryo-3.0.3.jar;%APP_HOME%\lib\kryo-serializers-0.37.jar;%APP_HOME%\lib\quartz-2.2.3.jar;%APP_HOME%\lib\performancemonitorclient-1.1.3.jar;%APP_HOME%\lib\errorreporter-logback-1.0.7.jar;%APP_HOME%\lib\disconf-client-2.6.39.jar;%APP_HOME%\lib\hibernate-validator-5.2.4.Final.jar;%APP_HOME%\lib\jackson-jaxrs-json-provider-2.7.4.jar;%APP_HOME%\lib\fastjson-1.2.12.jar;%APP_HOME%\lib\resteasy-jackson2-provider-3.0.16.Final.jar;%APP_HOME%\lib\el-api-2.2.jar;%APP_HOME%\lib\spring-boot-starter-1.4.2.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-tomcat-1.4.2.RELEASE.jar;%APP_HOME%\lib\jackson-databind-2.8.4.jar;%APP_HOME%\lib\spring-web-4.3.4.RELEASE.jar;%APP_HOME%\lib\spring-webmvc-4.3.4.RELEASE.jar;%APP_HOME%\lib\mongodb-driver-3.2.2.jar;%APP_HOME%\lib\spring-data-mongodb-1.9.5.RELEASE.jar;%APP_HOME%\lib\spring-boot-test-1.4.2.RELEASE.jar;%APP_HOME%\lib\spring-boot-test-autoconfigure-1.4.2.RELEASE.jar;%APP_HOME%\lib\json-path-2.2.0.jar;%APP_HOME%\lib\assertj-core-2.5.0.jar;%APP_HOME%\lib\mockito-core-1.10.19.jar;%APP_HOME%\lib\hamcrest-core-1.3.jar;%APP_HOME%\lib\hamcrest-library-1.3.jar;%APP_HOME%\lib\jsonassert-1.3.0.jar;%APP_HOME%\lib\spring-test-4.3.4.RELEASE.jar;%APP_HOME%\lib\aopalliance-1.0.jar;%APP_HOME%\lib\netty-3.7.0.Final.jar;%APP_HOME%\lib\javax.servlet-api-3.1.0.jar;%APP_HOME%\lib\commons-pool-1.6.jar;%APP_HOME%\lib\curator-framework-2.5.0.jar;%APP_HOME%\lib\curator-client-2.5.0.jar;%APP_HOME%\lib\logback-core-1.1.7.jar;%APP_HOME%\lib\jboss-jaxrs-api_2.0_spec-1.0.0.Final.jar;%APP_HOME%\lib\jboss-annotations-api_1.2_spec-1.0.0.Final.jar;%APP_HOME%\lib\activation-1.1.1.jar;%APP_HOME%\lib\commons-io-2.1.jar;%APP_HOME%\lib\jcip-annotations-1.0.jar;%APP_HOME%\lib\reflectasm-1.10.1.jar;%APP_HOME%\lib\minlog-1.3.0.jar;%APP_HOME%\lib\objenesis-2.1.jar;%APP_HOME%\lib\protobuf-java-2.6.1.jar;%APP_HOME%\lib\httpasyncclient-4.1.2.jar;%APP_HOME%\lib\aspectjtools-1.8.7.jar;%APP_HOME%\lib\cat-client-1.3.6-SNAPSHOT.jar;%APP_HOME%\lib\errorreporter-core-1.0.7.jar;%APP_HOME%\lib\disconf-core-2.6.39.jar;%APP_HOME%\lib\gson-2.2.4.jar;%APP_HOME%\lib\commons-lang-2.4.jar;%APP_HOME%\lib\reflections-0.9.9-RC1.jar;%APP_HOME%\lib\validation-api-1.1.0.Final.jar;%APP_HOME%\lib\classmate-1.1.0.jar;%APP_HOME%\lib\jackson-jaxrs-base-2.7.4.jar;%APP_HOME%\lib\jackson-module-jaxb-annotations-2.7.4.jar;%APP_HOME%\lib\spring-boot-starter-logging-1.4.2.RELEASE.jar;%APP_HOME%\lib\snakeyaml-1.17.jar;%APP_HOME%\lib\tomcat-embed-core-8.5.6.jar;%APP_HOME%\lib\tomcat-embed-el-8.5.6.jar;%APP_HOME%\lib\tomcat-embed-websocket-8.5.6.jar;%APP_HOME%\lib\mongodb-driver-core-3.2.2.jar;%APP_HOME%\lib\bson-3.2.2.jar;%APP_HOME%\lib\spring-data-commons-1.12.5.RELEASE.jar;%APP_HOME%\lib\json-smart-2.2.1.jar;%APP_HOME%\lib\json-20090211.jar;%APP_HOME%\lib\asm-5.0.3.jar;%APP_HOME%\lib\httpcore-nio-4.4.5.jar;%APP_HOME%\lib\foundation-service-2.2.0.jar;%APP_HOME%\lib\netty-all-4.0.24.Final.jar;%APP_HOME%\lib\commons-lang3-3.4.jar;%APP_HOME%\lib\zookeeper-3.4.6.jar;%APP_HOME%\lib\dom4j-1.6.1.jar;%APP_HOME%\lib\jul-to-slf4j-1.7.21.jar;%APP_HOME%\lib\log4j-over-slf4j-1.7.21.jar;%APP_HOME%\lib\accessors-smart-1.1.jar;%APP_HOME%\lib\plexus-container-default-1.5.5.jar;%APP_HOME%\lib\plexus-utils-3.0.8.jar;%APP_HOME%\lib\aspectjrt-1.5.4.jar;%APP_HOME%\lib\jline-0.9.94.jar;%APP_HOME%\lib\xml-apis-1.0.b2.jar;%APP_HOME%\lib\plexus-classworlds-2.2.2.jar;%APP_HOME%\lib\xbean-reflect-3.4.jar;%APP_HOME%\lib\commons-logging-api-1.1.jar;%APP_HOME%\lib\spring-boot-starter-jdbc-1.4.2.RELEASE.jar;%APP_HOME%\lib\tomcat-jdbc-8.5.6.jar;%APP_HOME%\lib\spring-jdbc-4.3.4.RELEASE.jar;%APP_HOME%\lib\tomcat-juli-8.5.6.jar;%APP_HOME%\lib\spring-core-4.3.4.RELEASE.jar;%APP_HOME%\lib\spring-context-4.3.4.RELEASE.jar;%APP_HOME%\lib\spring-beans-4.3.4.RELEASE.jar;%APP_HOME%\lib\httpcore-4.4.5.jar;%APP_HOME%\lib\log4j-1.2.17.jar;%APP_HOME%\lib\guava-19.0-rc2.jar;%APP_HOME%\lib\jboss-logging-3.2.1.Final.jar;%APP_HOME%\lib\jackson-annotations-2.8.0.jar;%APP_HOME%\lib\spring-boot-1.4.2.RELEASE.jar;%APP_HOME%\lib\spring-boot-autoconfigure-1.4.2.RELEASE.jar;%APP_HOME%\lib\spring-aop-4.3.4.RELEASE.jar;%APP_HOME%\lib\spring-expression-4.3.4.RELEASE.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\javassist-3.16.1-GA.jar;%APP_HOME%\lib\spring-tx-4.3.4.RELEASE.jar;%APP_HOME%\lib\commons-codec-1.9.jar;%APP_HOME%\lib\jackson-core-2.8.4.jar

@rem Execute web
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %WEB_OPTS%  -classpath "%CLASSPATH%" com.ymatou.productsync.web.ProductSyncApplication %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable WEB_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%WEB_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
