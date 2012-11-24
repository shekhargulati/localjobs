## Create an Application 

rhc app create -a localjobs -t jbossews-1.0 -l <openshift_login_email> -d

## Adding MongoDB and RockMongo Client Cartridge

```
rhc cartridge add -a localjobs -c mongodb-2.0 -l <openshift_login_email>
rhc cartridge add -a localjobs -c rockmongo-1.1 -l <openshift_login_email>
```

## Pulling code from github and pushing to OpenShift

After you have created the application using rhc create app command and added MongoDB and RockMongoDB client cartridge using rhc app cartridge add command you have to checkout the code from my github. To do that follow the steps mentioned below.

```
git remote add localjobs -m master git@github.com:shekhargulati/localjobs.git
 
git pull -s recursive -X theirs localjobs master

```
## Importing Jobs Data to MongoDB

```
rhc app show -a localjobs -l <openshift_login_email>

scp -r jobs-data.json <instance_ssh_access>:localjobs/data

ssh <instance_ssh_access>

mongoimport -d localjobs -c jobs --file jobs-data.json -u $OPENSHIFT_NOSQL_DB_USERNAME -p $OPENSHIFT_NOSQL_DB_PASSWORD -h $OPENSHIFT_NOSQL_DB_HOST -port $OPENSHIFT_NOSQL_DB_PORT

login to database using mongo client and create a 2d index
> db.jobs.ensureIndex({"location":"2d"})

```

## Run MongoDB queries

While you are in mongo shell lets execute some commands

```
Count of all the Java jobs near to my location

db.jobs.find({"location":{$near : [48.1530699,11.5992338]},"skills":"java"}).limit(2)


Give me Address of all the Java jobs near to my location

db.jobs.find({"location":{$near : [48.1530699,11.5992338]},"skills":"java"},{"formattedAddress":1}).limit(2)
```

## Deploy the application to cloud

```
git push
```

## Application in Cloud

The application will be up and running on OpenShift and you can see results on following urls

http://localjobs-domain-name.rhcloud.com/

http://localjobs-domain-name.rhcloud.com/jobs

http://localjobs-domain-name.rhcloud.com/id

http://localjobs-domain-name.rhcloud.com/jobs/near?latitude=48.1530699&longitude=11.5992338

http://localjobs-domain-name.rhcloud.com/jobs/near/AM%20TUCHERPARK%207,MUNICH,%20GERMANY/

http://localjobs-domain-name.rhcloud.com/jobs/near/mongodb?latitude=48.1530699&longitude=11.5992338

http://localjobs-domain-name.rhcloud.com/jobs/near/AM%20TUCHERPARK%207,MUNICH,%20GERMANY/mongodb