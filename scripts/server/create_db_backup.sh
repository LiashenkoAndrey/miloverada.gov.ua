DIR=`date +%d-%m-%y`
DEST=~/db_backups/$DIR
mkdir $DEST

PG_HOST='127.0.0.1'
PG_PORT='5432'
PG_USER='postgres'
PG_PASS='1wj@2Zx+4vH$'
DB_NAME='postgres'
 https://rclone.org/drive/#making-your-own-client-id
pg_dump --inserts --column-inserts --username=$PG_USER --password=$PG_PASS --host=$PG_HOST --port=$PG_PORT $DB_NAME > $DEST/backup.sql
