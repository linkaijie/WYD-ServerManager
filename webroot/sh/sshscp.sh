expect -c "
        set timeout 1200;
        spawn /usr/bin/scp -P $6 -r $1 $4@$2:$3
        expect {
                \"*yes/no*\" {send \"yes\r\"; exp_continue}
                \"*password*\" {send \"$5\r\";}
        }
expect eof;"
