#!/bin/bash
declare -A opt_spec
unset instance
unset version
unset native
unset user
unset path
opt_spec=(
  [instance:]='instance'
  [version:]='version'
  [native:]='native'
  [user:]='user'
  [path:]='path'
)
parsed_opts=$(
  IFS=,
  getopt -o + -l "${!opt_spec[*]}" -- "$@"
) || exit
while [ "$#" -gt 0 ]; do
  o=$1; shift
  case $o in
    (--) break;;
    (--*)
      o=${o#--}
      if ((${opt_spec[$o]+1})); then # opt without argument
        eval "${opt_spec[$o]}=true"
      else
        o=$o:
        case "${opt_spec[$o]}" in
          (*'()') eval "${opt_spec[$o]%??}+=(\"\$1\")";;
          (*) eval "${opt_spec[$o]}=\$1"
        esac
        shift
      fi
  esac
done
####################### updating variables with input arguments #############################
INSTANCE_NUMBER=${instance:-"01"}
VERSION_NUMBER=${version:-"0.0.0-0.0.0"}
NATIVE_LIBRARY_PATH=${native:-"/opt/oracle/DocumentDataExtractor/"}
NAME=${name:-"Apollo_$INSTANCE_NUMBER"}
FILE_PATH=${path:-"/opt/oracle/apollo_$INSTANCE_NUMBER"}
APP_USER=${user:-"idapi"}


JAVA_HOME=echo printenv JAVA_HOME
DESC="Apollo image processing agent instance $INSTANCE_NUMBER"
EXEC="/usr/bin/jsvc"
CLASSPATH="/$FILE_PATH/ApolloAgent-$VERSION_NUMBER.jar:/$FILE_PATH/lib/commons-daemon-1.0.15.jar:/$FILE_PATH/lib/*:/$FILE_PATH/config/*:."
CLASS="com.fintech.oracle.apollo.service.Service"
PID="$FILE_PATH/$NAME.pid"
DATE=$(date +"_%Y%m%d")
# System.out writes to this file...
LOG_OUT="$FILE_PATH/$NAME$DATE.out"
# System.err writes to this file...
LOG_ERR="$FILE_PATH/$NAME$DATE.err"

echo "NAME : $NAME"

jsvc_exec() {
    cd /$FILE_PATH
    $EXEC -home $JAVA_HOME -cp $CLASSPATH -DapplicationContextLoadFrom="file" -DcontextFilePath="$FILE_PATH/config/" -DcontextFileName="production-application-context.xml" -user $APP_USER -outfile $LOG_OUT -errfile $LOG_ERR -pidfile $PID $1 $CLASS
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
            echo "You may find more details about this unsuccessful launch in $LOG_ERR file"
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
        echo "Usage: [--instance Instance] [--version Agent Version] [--native Native Library Path]
        [--user User] [--path File Path] {start|stop}"
    ;;
esac
