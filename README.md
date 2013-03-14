## Create an Application 

We will start by creating an application named JBossEAP MongoDB application named localjobs. The application will use the git repository provided in the from-code as the base code.
```
rhc app create localjobs jbosseap mongodb-2.2 --from-code=git://github.com/shekhargulati/localjobs.git
```


## Importing Jobs Data to MongoDB

```
rhc app show -a localjobs 

scp jobs-data.json <ssh url>:app-root/data

rhc app ssh -a localjobs

cd app-root/data

mongoimport -d localjobs -c jobs --file jobs-data.json -u $OPENSHIFT_MONGODB_DB_USERNAME -p $OPENSHIFT_MONGODB_DB_PASSWORD -h $OPENSHIFT_MONGODB_DB_HOST -port $OPENSHIFT_MONGODB_DB_PORT

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

## Application in Cloud

The application will be up and running on OpenShift at http://localjobs-t20.rhcloud.com/