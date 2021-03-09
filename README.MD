Tras haber accedido por SSH a la maquina, haber actualizado los paquetes y haber instalador la jdk open 11 y copiado el jar mediante scp.

```
sudo apt-get update && sudo apt install -y openjdk-11-jdk
```

Ecsribimos el siguiente contenido en el fichero mediante el siguien comando:

```
sudo nano /usr/local/bin/events.sh
```

```
#!/bin/sh 
SERVICE_NAME=events
PATH_TO_JAR=/home/ubuntu/ja.devicentel-0.0.1-SNAPSHOT.jar
PID_PATH_NAME=/tmp/events-pid
case $1 in start)
       echo "Starting $SERVICE_NAME ..."
  if [ ! -f $PID_PATH_NAME ]; then 
       nohup java -jar $PATH_TO_JAR --spring.profiles.active=production --amazon.s3.region=eu-west-3 --amazon.s3.bucket-name=joaquin-antonio --spring.datasource.url=jdbc:mysql://events.cgphefsn4gpu.eu-west-3.rds.amazonaws.com/events --spring.datasource.username=joaquin --spring.datasource.password=joaquin1 2>> /dev/null >> /dev/null &      
       echo $! > $PID_PATH_NAME  
       echo "$SERVICE_NAME started ..."         
  else 
       echo "$SERVICE_NAME is already running ..."
  fi
;;
stop)
  if [ -f $PID_PATH_NAME ]; then
         PID=$(cat $PID_PATH_NAME);
         echo "$SERVICE_NAME stoping ..." 
         kill $PID;         
         echo "$SERVICE_NAME stopped ..." 
         rm $PID_PATH_NAME       
  else          
         echo "$SERVICE_NAME is not running ..."   
  fi    
;;    
restart)  
  if [ -f $PID_PATH_NAME ]; then 
      PID=$(cat $PID_PATH_NAME);    
      echo "$SERVICE_NAME stopping ..."; 
      kill $PID;           
      echo "$SERVICE_NAME stopped ...";  
      rm $PID_PATH_NAME     
      echo "$SERVICE_NAME starting ..."  
       nohup java -jar $PATH_TO_JAR --spring.profiles.active=production --amazon.s3.region=eu-west-3 --amazon.s3.bucket-name=joaquin-antonio --spring.datasource.url=jdbc:mysql://events.cgphefsn4gpu.eu-west-3.rds.amazonaws.com/events --spring.datasource.username=joaquin --spring.datasource.password=joaquin1 2>> /dev/null >> /dev/null &                     
      echo $! > $PID_PATH_NAME  
      echo "$SERVICE_NAME started ..."    
  else           
      echo "$SERVICE_NAME is not running ..."    
     fi     ;;
 esac
```
