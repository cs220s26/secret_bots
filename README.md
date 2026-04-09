## Secrets Bot

This repo contains a simple Discord bot written in Java.  The bot knows a set of secret words, and and it listens in a specific channel.  If a user says one of those words, the bot will respond with the secret.

This repo demonstrates:

* Configuration of a Discord bot (using a `.env` file)
* Deploying a bot locally (dev) and on EC2 (prod)
* Database management via scripts



## Setup

After you clone the repo, complete the following steps:

* Compile, test, and package:

  ```
  mvn package
  ```
  
* The bot reads the discord token and the channel name from an `.env` file.  Create this file in the root of the project:

  ```
  DISCORD_TOKEN=<token>
  CHANNEL_NAME=<your channel>
  ```


## Local Deploy

To run the bot on your laptop:

* In one terminal window run redis:

  ```
  redis-server
  ```
 
* (Optional) Do the data management steps (below)
* In a second terminal window run the bot

  ```
  java -jar target/secretbot-1.0.0-jar-with-dependencies.jar
  ```


## Data Management (Redis)

The secrets bot reads data from redis.  It uses a hash named `secrets`.  Within this hash, each key/value pair is a message/response pair.  That is, if a user types the message then the bot will reply with the response.

The repo contains three scripts to manage the data in Redis:

* `resetdb.sh` - Remove all data from Redis
* `addsecret.sh` - Given a message and response (single words), add the data to Redis
* `secrets.sh` - Add a fixed set of messages and responses to redis


## EC2 Deploy

To deploy on EC2 you need to:

* Install git

  ```
  sudo yum install -y git
  ```
  
* Install java and maven

  ```
  sudo yum install -y maven-amazon-corretto21
  ```

* Install redis - On Amazon Linux, redis is in the package `redis6`

  ```
  sudo yum install -y redis6
  ```
  
* Launch redis using SystemD

  ```
  sudo systemctl start redis6
  ```
  
  You can verify that redis is running with `ping` (it should respond with `PONG`):
  
  ```  
  redis6-cli ping
  ```

* Clone the repo
* Do the setup steps (above)
* (Optional) Do the data management steps (above)

* Run the bot (manually)

  ```
  java -jar target/secretbot-1.0.0-jar-with-dependencies.jar
  ```
  
* TODO: Configure the bot to run via SystemD - make a `.service` file

   
