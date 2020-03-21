# Tincture: parser

This is a parser (a part of backend) for the service **[Tincture](https://tincture.ephemeralin.com/)**. It is created as an AWS Lambda function. 
The parser runs avery  hour, retrieves data and saves it to the database.

The Tincture parser is created using such technologies:

* Core Java;
* [Rome Tools](https://github.com/rometools/rome) for smooth RSS and Atom feeds parsing;
* [Jsoup](https://jsoup.org) when the Rome Tools can't manage it;
* JUnit 5 for tests;
* [AWS Lambda](https://aws.amazon.com/lambda/) for running the code in a serverless way;
* [AWS DynamoDB](https://aws.amazon.com/dynamodb/) - serverless NoSQL database for storing news feeds data;
* [Serverless Framework](https://serverless.com) - for fast deploying to AWS.

"Tincture" is a service which is aggregating different news feeds in one place. It is built for those who are annoyed by ads, subscriptions, "fat" web-sites, "smart" recommendations and infinite browsing between all that stuff.
