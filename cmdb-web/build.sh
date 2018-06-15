# build war
gradle clean war -DpkgName=opscloud -Denv=online -refresh-dependencies -Dorg.gradle.daemon=false
