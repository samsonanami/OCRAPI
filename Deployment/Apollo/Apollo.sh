#!/bin/bash


####################### set the values #############################

INSTANCE_NUMBER="01"
VERSION_NUMBER="1.0.1-0.0.2--"
JAVA_HOME="/usr/local/java/jdk1.8.0_111/"
NATIVE_LIBRARY_PATH="/opt/oracle/DocumentDataExtractor/"


######################## do not change below #######################
NAME="Apollo_$INSTANCE_NUMBER" DESC="Apollo image processing agent instance $INSTANCE_NUMBER"

EXEC="/usr/bin/jsvc"

FILE_PATH="/opt/oracle/apollo_$INSTANCE_NUMBER"

CLASSPATH="/$FILE_PATH/ApolloAgent-$VERSION_NUMBER.jar:/$FILE_PATH/lib/commons-daemon-1.0.15.jar:/$FILE_PATH/lib/*:/$FILE_PATH/config/*:."


CLASS="com.fintech.oracle.apollo.service.Service"

USER="idapi"

PID="/$FILE_PATH/$NAME.pid"

DATE=$(date +"_%Y%m%d")

# System.out writes to this file...
LOG_OUT="/$FILE_PATH/$NAME$DATE.out"

# System.err writes to this file...
LOG_ERR="/$FILE_PATH/$NAME$DATE.err"


jsvc_exec() {
    cd /$FILE_PATH
    $EXEC -home $JAVA_HOME -cp $CLASSPATH -DapplicationContextLoadFrom="file" -DcontextFilePath="$FILE_PATH/config/" -DcontextFileName="production-application-context.xml" -user $USER -outfile $LOG_OUT -errfile $LOG_ERR -pidfile $PID $1 $CLASS
}

case "$1" in
    start)
        echo "Initializing..."
        if [[ -z "${LD_LIBRARY_PATH}" ]]; then
                 export LD_LIBRARY_PATH="$NATIVE_LIBRARY_PATH"
        else
                if [[ $LD_LIBRARY_PATH == *"$NATIVE_LIBRARY_PATH" ]]; then
                        export LD_LIBRARY_PATH="$LD_LIBRARY_PATH:$NATIVE_LIBRARY_PATH"
                fi
        fi
        echo "Launching apollo $INSTANCE_NUMBER"
        jsvc_exec
        echo "Stabilizing orbit"
        sleep 5
        if [ -f "$PID" ]; then
            echo "Launch successful"
            echo "Please use ps aux | grep apollo_$INSTANCE_NUMBER to check the status"
        else
            echo "Unable to stabilize orbit"
            echo "Initiating self destruct sequence"
            ecoo "You may find more details about this unsuccessful launch in $LOG_ERR file"
        fi

    ;;
    stop)
        echo "Initiating shut down sequence for apollo $INSTANCE_NUMBER"
        echo "Calculating atmospheric re entry path"
        echo "Shutting down ... "
        jsvc_exec "-stop"
        echo "Shutdown successful. Please wait for re entry and landing"
        sleep 5
        echo "apollo $INSTANCE_NUMBER landed successfully"
        echo "Please use ps aux | grep apollo_$INSTANCE_NUMBER to check the status"

    ;;
    restart)
        echo "Not supported at the moment"
    ;;
    *)
        echo "Usage: {start|stop}"
    ;;
esac