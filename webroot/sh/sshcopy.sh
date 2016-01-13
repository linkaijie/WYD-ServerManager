expect -c "
        set timeout 1200;
        spawn /usr/bin/ssh-copy-id developer@$1
        expect {
                \"*yes/no*\" {send \"yes\r\"; exp_continue}
                \"*password*\" {send \"$2\r\";}
        }
expect eof;"
