echo "deploying the branch "$1;
git fetch && git checkout $1 && git pull
gradle war
sudo service tomcat7 stop && sudo rm -rf /var/lib/tomcat7/webapps/* && sudo cp /home/ubuntu/distance-service/build/libs/distance-service.war /var/lib/tomcat7/webapps/ && sudo chmod 777 /var/lib/tomcat7/webapps/distance-service.war  && sudo service tomcat7 start