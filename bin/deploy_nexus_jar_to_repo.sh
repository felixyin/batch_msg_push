#!/bin/sh

mvn deploy:deploy-file -Dfile=./lib/darcula.jar -DgroupId=com.darcula -DartifactId=darcula-lnf -Dpackaging=jar -Dversion=1.0   -Durl=http://qtrj.i234.me:8081/repository/maven-releases/ -DrepositoryId=qtrj-nexus
mvn deploy:deploy-file -Dfile=./lib/beautyeye_lnf.jar -DgroupId=com.beautyeye -DartifactId=beautyeye-lnf -Dpackaging=jar -Dversion=1.0   -Durl=http://qtrj.i234.me:8081/repository/maven-releases/ -DrepositoryId=qtrj-nexus
mvn deploy:deploy-file -Dfile=./lib/taobao-sdk-java-auto.jar -DgroupId=com.taobao -DartifactId=taobao-sdk-java-auto -Dpackaging=jar -Dversion=1.0.0   -Durl=http://qtrj.i234.me:8081/repository/maven-releases/ -DrepositoryId=qtrj-nexus
mvn deploy:deploy-file -Dfile=./lib/weblaf-1.29.jar -DgroupId=com.weblaf -DartifactId=weblaf-lnf -Dpackaging=jar -Dversion=1.2.9   -Durl=http://qtrj.i234.me:8081/repository/maven-releases/ -DrepositoryId=qtrj-nexus
mvn deploy:deploy-file -Dfile=./lib/antlr-2.7.4.jar -DgroupId=net.sourceforge.cpdetector -DartifactId=antlr -Dpackaging=jar -Dversion=2.7.4   -Durl=http://qtrj.i234.me:8081/repository/maven-releases/ -DrepositoryId=qtrj-nexus
mvn deploy:deploy-file -Dfile=./lib/chardet-1.0.jar -DgroupId=net.sourceforge.cpdetector -DartifactId=chardet -Dpackaging=jar -Dversion=1.0.0   -Durl=http://qtrj.i234.me:8081/repository/maven-releases/ -DrepositoryId=qtrj-nexus
mvn deploy:deploy-file -Dfile=./lib/cpdetector_1.0.10.jar -DgroupId=net.sourceforge.cpdetector -DartifactId=cpdetector -Dpackaging=jar -Dversion=1.0.10   -Durl=http://qtrj.i234.me:8081/repository/maven-releases/ -DrepositoryId=qtrj-nexus
mvn deploy:deploy-file -Dfile=./lib/jargs-1.0.jar -DgroupId=net.sourceforge.cpdetector -DartifactId=jargs -Dpackaging=jar -Dversion=1.0.0   -Durl=http://qtrj.i234.me:8081/repository/maven-releases/ -DrepositoryId=qtrj-nexus
