# INTERVIEW

## Assumptions:

 - Docker is installed on the local machine
 - The module "lib" is not tested because they act as a third party dependencies (which you can rely on)
 - The offers are supposed to be deleted, and don't stay in the database with the flag "expires_at"
 - "cancel" a offer means actually deleting it form the database
 - We favour feature test over unit tests. The application and the client are tested in their relative modules. 
 "api" module is implicitly tested through the application's and the client's feature tests 
 - Dto types are Strings or longs to have influence on how to handle the validation 
 (Another way could be to catch the exception thrown by when serializing)
 - There is a folder called "secrets" in the root of the application's module which is ignored by git. 
 Inside the folder a file called "database" has to be created and in the file should be written the password: "sa". 
 A similar file will be stored in a S3 bucket for playground and production and copied from the the bucket 
 to the container running in a EC2 instance which hosts the application that communicates with the database
 when the application is deployed to AWS (That is the application user's password, not the root's one)
 - Another config will be set only for jenkins. That one will have the postgres real database, and the local application
 should be run on an in-memory database
 - All the incoming and outgoing content can be logged
 - The modules "lib" and "client" are actually separated projects, not only separated modules
 
## TODOs -> From development to operations

- Deploy with jenkins and docker to AWS
- Test the client against the application running in a container from the docker image used in the deploy
- Metrics
- Idempotency Keys
- Migrate maven to gradle
- Type responses (The version of jackson that dropwizard is using seems to be failing when deserializing dates)
- Change snake case to camel case in "expires_at"


# HOW TO


## Running jenkins in docker for developing the pipeline locally
docker run  -u root --name jenkinsLocalContainer --rm -d -p 8080:8080 -p 50000:50000 -v jenkins-data:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock jenkinsci/blueocean

1. docker exec -ti jenkinsLocalContainer bin/bash
2. cat /var/jenkins_home/secrets/initialAdminPassword
3. copy the outcome and paste it in the input that appears in http//localhost:8080
4. Install whatever you want
5. Create a freestyle job with the git project
6. ...Under construction...
